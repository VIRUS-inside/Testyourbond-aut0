package org.openqa.selenium.remote.server.handler;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.server.JsonParametersAware;
import org.openqa.selenium.remote.server.Session;

















public class SendKeys
  extends WebElementHandler<Void>
  implements JsonParametersAware
{
  private final List<CharSequence> keys = new CopyOnWriteArrayList();
  
  public SendKeys(Session session) {
    super(session);
  }
  
  public void setJsonParameters(Map<String, Object> allParameters) throws Exception
  {
    List<String> rawKeys = (List)allParameters.get("value");
    List<String> temp = Lists.newArrayList();
    for (String key : rawKeys) {
      temp.add(key);
    }
    keys.addAll(temp);
  }
  
  public Void call() throws Exception
  {
    String[] keysToSend = (String[])keys.toArray(new String[0]);
    getElement().sendKeys(keysToSend);
    
    return null;
  }
  
  public String toString()
  {
    return String.format("[send keys: %s, %s]", new Object[] { getElementAsString(), keys });
  }
}
