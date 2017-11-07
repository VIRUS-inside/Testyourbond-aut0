package org.openqa.selenium.remote;

import com.google.common.collect.ImmutableMap;
import org.openqa.selenium.interactions.Keyboard;



















public class RemoteKeyboard
  implements Keyboard
{
  protected final ExecuteMethod executor;
  
  public RemoteKeyboard(ExecuteMethod executor)
  {
    this.executor = executor;
  }
  
  public void sendKeys(CharSequence... keysToSend) {
    executor.execute("sendKeysToActiveElement", 
      ImmutableMap.of("value", keysToSend));
  }
  

  public void pressKey(CharSequence keyToPress)
  {
    CharSequence[] sequence = { keyToPress };
    executor.execute("sendKeysToActiveElement", 
      ImmutableMap.of("value", sequence));
  }
  

  public void releaseKey(CharSequence keyToRelease)
  {
    CharSequence[] sequence = { keyToRelease };
    executor.execute("sendKeysToActiveElement", 
      ImmutableMap.of("value", sequence));
  }
}
