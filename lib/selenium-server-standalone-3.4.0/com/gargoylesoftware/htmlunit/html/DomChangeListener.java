package com.gargoylesoftware.htmlunit.html;

import java.io.Serializable;

public abstract interface DomChangeListener
  extends Serializable
{
  public abstract void nodeAdded(DomChangeEvent paramDomChangeEvent);
  
  public abstract void nodeDeleted(DomChangeEvent paramDomChangeEvent);
}
