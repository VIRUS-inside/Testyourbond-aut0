package com.steadystate.css.parser.selectors;

import com.steadystate.css.format.CSSFormat;
import com.steadystate.css.format.CSSFormatable;
import com.steadystate.css.parser.LocatableImpl;
import java.io.Serializable;
import org.w3c.css.sac.ElementSelector;






















public class PseudoElementSelectorImpl
  extends LocatableImpl
  implements ElementSelector, CSSFormatable, Serializable
{
  private static final long serialVersionUID = 2913936296006875268L;
  private String localName_;
  
  public void setLocaleName(String localName)
  {
    localName_ = localName;
  }
  
  public PseudoElementSelectorImpl(String localName) {
    setLocaleName(localName);
  }
  
  public short getSelectorType() {
    return 9;
  }
  
  public String getNamespaceURI() {
    return null;
  }
  
  public String getLocalName() {
    return localName_;
  }
  


  public String getCssText(CSSFormat format)
  {
    return localName_;
  }
  
  public String toString()
  {
    return getCssText(null);
  }
}
