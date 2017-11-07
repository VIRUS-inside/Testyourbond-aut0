package org.openqa.selenium.interactions;

import com.google.common.collect.ImmutableList;
import java.time.Duration;
import java.util.List;






















@Deprecated
public class PauseAction
  implements Action, IsInteraction
{
  private final long pause;
  
  public PauseAction(long pause)
  {
    this.pause = pause;
  }
  
  public void perform() {
    try {
      Thread.sleep(pause);
    }
    catch (InterruptedException localInterruptedException) {}
  }
  
  public List<Interaction> asInteractions(PointerInput mouse, KeyInput keyboard)
  {
    return ImmutableList.of(new Pause(keyboard, Duration.ofMillis(pause)));
  }
}
