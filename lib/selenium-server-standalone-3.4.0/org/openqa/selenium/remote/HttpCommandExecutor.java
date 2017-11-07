package org.openqa.selenium.remote;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.UnsupportedCommandException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.logging.LocalLogs;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.NeedsLocalLogs;
import org.openqa.selenium.logging.profiler.HttpProfilerLogEntry;
import org.openqa.selenium.remote.http.HttpClient;
import org.openqa.selenium.remote.http.HttpClient.Factory;
import org.openqa.selenium.remote.http.HttpRequest;
import org.openqa.selenium.remote.http.HttpResponse;
import org.openqa.selenium.remote.internal.ApacheHttpClient.Factory;
























public class HttpCommandExecutor
  implements CommandExecutor, NeedsLocalLogs
{
  private static HttpClient.Factory defaultClientFactory;
  private final URL remoteServer;
  private final HttpClient client;
  private final Map<String, CommandInfo> additionalCommands;
  private CommandCodec<HttpRequest> commandCodec;
  private ResponseCodec<HttpResponse> responseCodec;
  private LocalLogs logs = LocalLogs.getNullLogger();
  
  public HttpCommandExecutor(URL addressOfRemoteServer) {
    this(ImmutableMap.of(), addressOfRemoteServer);
  }
  








  public HttpCommandExecutor(Map<String, CommandInfo> additionalCommands, URL addressOfRemoteServer)
  {
    this(additionalCommands, addressOfRemoteServer, getDefaultClientFactory());
  }
  


  public HttpCommandExecutor(Map<String, CommandInfo> additionalCommands, URL addressOfRemoteServer, HttpClient.Factory httpClientFactory)
  {
    try
    {
      remoteServer = (addressOfRemoteServer == null ? new URL(System.getProperty("webdriver.remote.server", "http://localhost:4444/wd/hub")) : addressOfRemoteServer);
    }
    catch (MalformedURLException e) {
      throw new WebDriverException(e);
    }
    
    this.additionalCommands = additionalCommands;
    client = httpClientFactory.createClient(remoteServer);
  }
  
  private static synchronized HttpClient.Factory getDefaultClientFactory() {
    if (defaultClientFactory == null) {
      defaultClientFactory = new ApacheHttpClient.Factory();
    }
    return defaultClientFactory;
  }
  







  protected void defineCommand(String commandName, CommandInfo info)
  {
    Preconditions.checkNotNull(commandName);
    Preconditions.checkNotNull(info);
    commandCodec.defineCommand(commandName, info.getMethod(), info.getUrl());
  }
  
  public void setLocalLogs(LocalLogs logs) {
    this.logs = logs;
  }
  
  private void log(String logType, LogEntry entry) {
    logs.addEntry(logType, entry);
  }
  
  public URL getAddressOfRemoteServer() {
    return remoteServer;
  }
  
  public Response execute(Command command) throws IOException {
    if (command.getSessionId() == null) {
      if ("quit".equals(command.getName())) {
        return new Response();
      }
      if ((!"getAllSessions".equals(command.getName())) && 
        (!"newSession".equals(command.getName()))) {
        throw new NoSuchSessionException("Session ID is null. Using WebDriver after calling quit()?");
      }
    }
    

    if ("newSession".equals(command.getName())) {
      if (commandCodec != null) {
        throw new SessionNotCreatedException("Session already exists");
      }
      ProtocolHandshake handshake = new ProtocolHandshake();
      log("profiler", new HttpProfilerLogEntry(command.getName(), true));
      ProtocolHandshake.Result result = handshake.createSession(client, command);
      Dialect dialect = result.getDialect();
      commandCodec = dialect.getCommandCodec();
      for (Map.Entry<String, CommandInfo> entry : additionalCommands.entrySet()) {
        defineCommand((String)entry.getKey(), (CommandInfo)entry.getValue());
      }
      responseCodec = dialect.getResponseCodec();
      log("profiler", new HttpProfilerLogEntry(command.getName(), false));
      return result.createResponse();
    }
    
    if ((commandCodec == null) || (responseCodec == null)) {
      throw new WebDriverException("No command or response codec has been defined. Unable to proceed");
    }
    

    HttpRequest httpRequest = (HttpRequest)commandCodec.encode(command);
    try {
      log("profiler", new HttpProfilerLogEntry(command.getName(), true));
      HttpResponse httpResponse = client.execute(httpRequest, true);
      log("profiler", new HttpProfilerLogEntry(command.getName(), false));
      
      Response response = responseCodec.decode(httpResponse);
      if (response.getSessionId() == null) {
        if (httpResponse.getTargetHost() != null) {
          response.setSessionId(HttpSessionId.getSessionId(httpResponse.getTargetHost()));
        }
        else {
          response.setSessionId(command.getSessionId().toString());
        }
      }
      if ("quit".equals(command.getName())) {
        client.close();
      }
      return response;
    } catch (UnsupportedCommandException e) {
      if ((e.getMessage() == null) || ("".equals(e.getMessage())))
      {

        throw new UnsupportedOperationException("No information from server. Command name was: " + command.getName(), e.getCause());
      }
      throw e;
    }
  }
}
