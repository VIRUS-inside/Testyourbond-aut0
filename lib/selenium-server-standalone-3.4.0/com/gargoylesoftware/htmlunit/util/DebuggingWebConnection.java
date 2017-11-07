package com.gargoylesoftware.htmlunit.util;

import com.gargoylesoftware.htmlunit.FormEncodingType;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebConnection;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebResponseData;
import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ContextAction;
import net.sourceforge.htmlunit.corejs.javascript.ContextFactory;
import net.sourceforge.htmlunit.corejs.javascript.Script;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;








































public class DebuggingWebConnection
  extends WebConnectionWrapper
{
  private static final Log LOG = LogFactory.getLog(DebuggingWebConnection.class);
  
  private static final Pattern ESCAPE_QUOTE_PATTERN = Pattern.compile("'");
  
  private int counter_;
  private final WebConnection wrappedWebConnection_;
  private final File javaScriptFile_;
  private final File reportFolder_;
  private boolean uncompressJavaScript_ = true;
  







  public DebuggingWebConnection(WebConnection webConnection, String dirName)
    throws IOException
  {
    super(webConnection);
    
    wrappedWebConnection_ = webConnection;
    File tmpDir = new File(System.getProperty("java.io.tmpdir"));
    reportFolder_ = new File(tmpDir, dirName);
    if (reportFolder_.exists()) {
      FileUtils.forceDelete(reportFolder_);
    }
    FileUtils.forceMkdir(reportFolder_);
    javaScriptFile_ = new File(reportFolder_, "hu.js");
    createOverview();
  }
  



  public WebResponse getResponse(WebRequest request)
    throws IOException
  {
    WebResponse response = wrappedWebConnection_.getResponse(request);
    if ((isUncompressJavaScript()) && (isJavaScript(response.getContentType()))) {
      response = uncompressJavaScript(response);
    }
    saveResponse(response, request);
    return response;
  }
  




  protected WebResponse uncompressJavaScript(WebResponse response)
  {
    WebRequest request = response.getWebRequest();
    final String scriptName = request.getUrl().toString();
    final String scriptSource = response.getContentAsString();
    


    ContextFactory factory = new ContextFactory();
    ContextAction action = new ContextAction()
    {
      public Object run(Context cx) {
        cx.setOptimizationLevel(-1);
        Script script = cx.compileString(scriptSource, scriptName, 0, null);
        return cx.decompileScript(script, 4);
      }
    };
    try
    {
      String decompileScript = (String)factory.call(action);
      List<NameValuePair> responseHeaders = new ArrayList(response.getResponseHeaders());
      for (int i = responseHeaders.size() - 1; i >= 0; i--) {
        if ("content-encoding".equalsIgnoreCase(((NameValuePair)responseHeaders.get(i)).getName())) {
          responseHeaders.remove(i);
        }
      }
      WebResponseData wrd = new WebResponseData(decompileScript.getBytes(), response.getStatusCode(), 
        response.getStatusMessage(), responseHeaders);
      return new WebResponse(wrd, response.getWebRequest().getUrl(), 
        response.getWebRequest().getHttpMethod(), response.getLoadTime());
    }
    catch (Exception e) {
      LOG.warn("Failed to decompress JavaScript response. Delivering as it.", e);
    }
    
    return response;
  }
  



  public void addMark(String mark)
    throws IOException
  {
    if (mark != null) {
      mark = mark.replace("\"", "\\\"");
    }
    appendToJSFile("tab[tab.length] = \"" + mark + "\";\n");
    LOG.info("--- " + mark + " ---");
  }
  





  protected void saveResponse(WebResponse response, WebRequest request)
    throws IOException
  {
    counter_ += 1;
    String extension = chooseExtension(response.getContentType());
    File f = createFile(request.getUrl(), extension);
    int length = 0;
    Object localObject1 = null;Object localObject4 = null; label126: Object localObject3; try { InputStream input = response.getContentAsStream();
      try { Object localObject5 = null;Object localObject8 = null; Object localObject7; try { output = new FileOutputStream(f);
        } finally { OutputStream output;
          localObject7 = localThrowable2; break label126; if (localObject7 != localThrowable2) localObject7.addSuppressed(localThrowable2);
        } } catch (EOFException localEOFException1) { localEOFException1 = localEOFException1;
        

        if (input != null) input.close(); } finally { localObject2 = finally; if (input != null) input.close(); throw localObject2; } } finally { if (localObject2 == null) localObject3 = localThrowable1; else if (localObject3 != localThrowable1) localObject3.addSuppressed(localThrowable1);
    }
    URL url = response.getWebRequest().getUrl();
    LOG.info("Created file " + f.getAbsolutePath() + " for response " + counter_ + ": " + url);
    
    StringBuilder bduiler = new StringBuilder();
    bduiler.append("tab[tab.length] = {code: " + response.getStatusCode() + ", ");
    bduiler.append("fileName: '" + f.getName() + "', ");
    bduiler.append("contentType: '" + response.getContentType() + "', ");
    bduiler.append("method: '" + request.getHttpMethod().name() + "', ");
    if ((request.getHttpMethod() == HttpMethod.POST) && (request.getEncodingType() == FormEncodingType.URL_ENCODED)) {
      bduiler.append("postParameters: " + nameValueListToJsMap(request.getRequestParameters()) + ", ");
    }
    bduiler.append("url: '" + escapeJSString(url.toString()) + "', ");
    bduiler.append("loadTime: " + response.getLoadTime() + ", ");
    bduiler.append("responseSize: " + length + ", ");
    bduiler.append("responseHeaders: " + nameValueListToJsMap(response.getResponseHeaders()));
    bduiler.append("};\n");
    appendToJSFile(bduiler.toString());
  }
  
  static String escapeJSString(String string) {
    return ESCAPE_QUOTE_PATTERN.matcher(string).replaceAll("\\\\'");
  }
  
  static String chooseExtension(String contentType) {
    if (isJavaScript(contentType)) {
      return ".js";
    }
    if ("text/html".equals(contentType)) {
      return ".html";
    }
    if ("text/css".equals(contentType)) {
      return ".css";
    }
    if ("text/xml".equals(contentType)) {
      return ".xml";
    }
    if ("image/gif".equals(contentType)) {
      return ".gif";
    }
    return ".txt";
  }
  




  static boolean isJavaScript(String contentType)
  {
    return (contentType.contains("javascript")) || (contentType.contains("ecmascript")) || (
      (contentType.startsWith("text/")) && (contentType.endsWith("js")));
  }
  



  public boolean isUncompressJavaScript()
  {
    return uncompressJavaScript_;
  }
  





  public void setUncompressJavaScript(boolean decompress)
  {
    uncompressJavaScript_ = decompress;
  }
  
  private void appendToJSFile(String str) throws IOException {
    Object localObject1 = null;Object localObject4 = null; Object localObject3; label70: try { jsFileWriter = new FileWriter(javaScriptFile_, true);
    } finally { FileWriter jsFileWriter;
      localObject3 = localThrowable; break label70; if (localObject3 != localThrowable) { localObject3.addSuppressed(localThrowable);
      }
    }
  }
  



  private File createFile(URL url, String extension)
    throws IOException
  {
    String name = url.getPath().replaceFirst("/$", "").replaceAll(".*/", "");
    name = org.apache.commons.lang3.StringUtils.substringBefore(name, "?");
    name = org.apache.commons.lang3.StringUtils.substringBefore(name, ";");
    name = org.apache.commons.lang3.StringUtils.substring(name, 0, 30);
    name = StringUtils.sanitizeForFileName(name);
    if (!name.endsWith(extension)) {
      name = name + extension;
    }
    int counter = 0;
    for (;;) { String fileName;
      String fileName;
      if (counter != 0) {
        fileName = 
          org.apache.commons.lang3.StringUtils.substringBeforeLast(name, ".") + "_" + counter + "." + org.apache.commons.lang3.StringUtils.substringAfterLast(name, ".");
      }
      else {
        fileName = name;
      }
      File f = new File(reportFolder_, fileName);
      if (f.createNewFile()) {
        return f;
      }
      counter++;
    }
  }
  




  static String nameValueListToJsMap(List<NameValuePair> headers)
  {
    if ((headers == null) || (headers.isEmpty())) {
      return "{}";
    }
    StringBuilder bduiler = new StringBuilder("{");
    for (NameValuePair header : headers) {
      bduiler.append("'" + header.getName() + "': '" + escapeJSString(header.getValue()) + "', ");
    }
    bduiler.delete(bduiler.length() - 2, bduiler.length());
    bduiler.append("}");
    return bduiler.toString();
  }
  


  private void createOverview()
    throws IOException
  {
    FileUtils.writeStringToFile(javaScriptFile_, "var tab = [];\n", StandardCharsets.ISO_8859_1);
    
    URL indexResource = DebuggingWebConnection.class.getResource("DebuggingWebConnection.index.html");
    if (indexResource == null) {
      throw new RuntimeException("Missing dependency DebuggingWebConnection.index.html");
    }
    File summary = new File(reportFolder_, "index.html");
    FileUtils.copyURLToFile(indexResource, summary);
    
    LOG.info("Summary will be in " + summary.getAbsolutePath());
  }
  
  File getReportFolder() {
    return reportFolder_;
  }
}
