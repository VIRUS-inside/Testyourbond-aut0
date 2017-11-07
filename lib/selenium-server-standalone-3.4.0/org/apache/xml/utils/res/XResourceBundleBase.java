package org.apache.xml.utils.res;

import java.util.ListResourceBundle;

public abstract class XResourceBundleBase
  extends ListResourceBundle
{
  public XResourceBundleBase() {}
  
  public abstract String getMessageKey(int paramInt);
  
  public abstract String getWarningKey(int paramInt);
}
