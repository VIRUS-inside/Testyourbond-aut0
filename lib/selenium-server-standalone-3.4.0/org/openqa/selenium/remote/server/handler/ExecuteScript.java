package org.openqa.selenium.remote.server.handler;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.remote.server.JsonParametersAware;
import org.openqa.selenium.remote.server.Session;
import org.openqa.selenium.remote.server.handler.internal.ArgumentConverter;
import org.openqa.selenium.remote.server.handler.internal.ResultConverter;

















public class ExecuteScript
  extends WebDriverHandler<Object>
  implements JsonParametersAware
{
  private volatile String script;
  private volatile List<Object> args = new ArrayList();
  
  public ExecuteScript(Session session) {
    super(session);
  }
  
  public void setJsonParameters(Map<String, Object> allParameters) throws Exception {
    script = ((String)allParameters.get("script"));
    
    List<?> params = (List)allParameters.get("args");
    
    args = Lists.newArrayList(Iterables.transform(params, new ArgumentConverter(
      getKnownElements())));
  }
  
  public Object call() throws Exception {
    Object value;
    Object value;
    if (args.size() > 0) {
      value = ((JavascriptExecutor)getDriver()).executeScript(script, args.toArray());
    } else {
      value = ((JavascriptExecutor)getDriver()).executeScript(script, new Object[0]);
    }
    
    return new ResultConverter(getKnownElements()).apply(value);
  }
  
  public String toString()
  {
    return String.format("[execute script: %s, %s]", new Object[] { script, args });
  }
}
