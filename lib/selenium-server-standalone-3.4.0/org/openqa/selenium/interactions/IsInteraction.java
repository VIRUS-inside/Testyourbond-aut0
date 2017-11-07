package org.openqa.selenium.interactions;

import java.util.List;
import org.openqa.selenium.Beta;

@Beta
public abstract interface IsInteraction
{
  public abstract List<Interaction> asInteractions(PointerInput paramPointerInput, KeyInput paramKeyInput);
}
