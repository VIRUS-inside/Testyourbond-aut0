package net.sourceforge.htmlunit.cyberneko;

import org.apache.xerces.xni.parser.XMLComponent;

public abstract interface HTMLComponent
  extends XMLComponent
{
  public abstract Boolean getFeatureDefault(String paramString);
  
  public abstract Object getPropertyDefault(String paramString);
}
