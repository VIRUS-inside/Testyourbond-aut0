package com.steadystate.css.parser;

import org.w3c.css.sac.Locator;

















public class LocatableImpl
  implements Locatable
{
  private Locator locator_;
  
  public LocatableImpl() {}
  
  public Locator getLocator()
  {
    return locator_;
  }
  
  public void setLocator(Locator locator) {
    locator_ = locator;
  }
}
