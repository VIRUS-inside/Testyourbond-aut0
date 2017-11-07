package org.openqa.selenium.remote.server.handler;

import com.google.gson.JsonObject;
import org.openqa.selenium.internal.BuildInfo;
import org.openqa.selenium.remote.Response;
import org.openqa.selenium.remote.server.rest.RestishHandler;



















public class Status
  implements RestishHandler<Response>
{
  public Status() {}
  
  public Response handle()
    throws Exception
  {
    Response response = new Response();
    response.setStatus(Integer.valueOf(0));
    response.setState("success");
    
    BuildInfo buildInfo = new BuildInfo();
    
    JsonObject info = new JsonObject();
    JsonObject build = new JsonObject();
    build.addProperty("version", buildInfo.getReleaseLabel());
    build.addProperty("revision", buildInfo.getBuildRevision());
    build.addProperty("time", buildInfo.getBuildTime());
    info.add("build", build);
    JsonObject os = new JsonObject();
    os.addProperty("name", System.getProperty("os.name"));
    os.addProperty("arch", System.getProperty("os.arch"));
    os.addProperty("version", System.getProperty("os.version"));
    info.add("os", os);
    JsonObject java = new JsonObject();
    java.addProperty("version", System.getProperty("java.version"));
    info.add("java", java);
    
    response.setValue(info);
    return response;
  }
}
