package org.openqa.selenium.phantomjs;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.HashMap;
import java.util.Map;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.CommandInfo;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.HttpCommandExecutor;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.Response;
import org.openqa.selenium.remote.http.HttpMethod;
import org.openqa.selenium.remote.internal.WebElementToJsonConverter;





































































public class PhantomJSDriver
  extends RemoteWebDriver
  implements TakesScreenshot
{
  private static final String COMMAND_EXECUTE_PHANTOM_SCRIPT = "executePhantomScript";
  
  public PhantomJSDriver()
  {
    this(DesiredCapabilities.phantomjs());
  }
  





  public PhantomJSDriver(Capabilities desiredCapabilities)
  {
    this(PhantomJSDriverService.createDefaultService(desiredCapabilities), desiredCapabilities);
  }
  






  public PhantomJSDriver(PhantomJSDriverService service, Capabilities desiredCapabilities)
  {
    super(new PhantomJSCommandExecutor(service), desiredCapabilities);
  }
  





  public PhantomJSDriver(HttpCommandExecutor executor, Capabilities desiredCapabilities)
  {
    super(executor, desiredCapabilities);
  }
  







  public <X> X getScreenshotAs(OutputType<X> target)
  {
    String base64 = (String)execute("screenshot").getValue();
    return target.convertFromBase64Png(base64);
  }
  























































  public Object executePhantomJS(String script, Object... args)
  {
    script = script.replaceAll("\"", "\\\"");
    
    Iterable<Object> convertedArgs = Iterables.transform(
      Lists.newArrayList(args), new WebElementToJsonConverter());
    Map<String, ?> params = ImmutableMap.of("script", script, "args", 
      Lists.newArrayList(convertedArgs));
    
    return execute("executePhantomScript", params).getValue();
  }
  

  protected static Map<String, CommandInfo> getCustomCommands()
  {
    Map<String, CommandInfo> customCommands = new HashMap();
    
    customCommands.put("executePhantomScript", new CommandInfo("/session/:sessionId/phantom/execute", HttpMethod.POST));
    

    return customCommands;
  }
}
