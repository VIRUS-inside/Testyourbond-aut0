package com.gargoylesoftware.htmlunit.html;

import java.util.Collection;

public abstract interface FormFieldWithNameHistory
{
  public abstract String getOriginalName();
  
  public abstract Collection<String> getNewNames();
}
