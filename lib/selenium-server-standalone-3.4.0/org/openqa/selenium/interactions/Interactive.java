package org.openqa.selenium.interactions;

import java.util.Collection;

public abstract interface Interactive
{
  public abstract void perform(Collection<Sequence> paramCollection);
  
  public abstract void resetInputState();
}
