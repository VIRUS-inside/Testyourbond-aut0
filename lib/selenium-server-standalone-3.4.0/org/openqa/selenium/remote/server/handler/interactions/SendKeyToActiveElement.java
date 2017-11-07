package org.openqa.selenium.remote.server.handler.interactions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Keyboard;
import org.openqa.selenium.remote.server.JsonParametersAware;
import org.openqa.selenium.remote.server.Session;
import org.openqa.selenium.remote.server.handler.WebDriverHandler;

















public class SendKeyToActiveElement
  extends WebDriverHandler<Void>
  implements JsonParametersAware
{
  private final List<CharSequence> keys = new CopyOnWriteArrayList();
  
  public SendKeyToActiveElement(Session session) {
    super(session);
  }
  

  public void setJsonParameters(Map<String, Object> allParameters)
    throws Exception
  {
    List<String> rawKeys = (List)allParameters.get("value");
    List<String> temp = new ArrayList();
    for (String key : rawKeys) {
      temp.add(key);
    }
    keys.addAll(temp);
  }
  
  public Void call() throws Exception
  {
    Keyboard keyboard = ((HasInputDevices)getDriver()).getKeyboard();
    
    String[] keysToSend = (String[])keys.toArray(new String[0]);
    keyboard.sendKeys(keysToSend);
    
    return null;
  }
  
  public String toString()
  {
    return String.format("[send keys to active: %s]", new Object[] { keys });
  }
}
