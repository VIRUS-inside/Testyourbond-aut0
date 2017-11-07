package com.steadystate.css.parser.selectors;

import com.steadystate.css.format.CSSFormat;
import com.steadystate.css.format.CSSFormatable;
import com.steadystate.css.parser.LocatableImpl;
import java.io.Serializable;
import org.w3c.css.sac.ElementSelector;





















public class ElementSelectorImpl
  extends LocatableImpl
  implements ElementSelector, CSSFormatable, Serializable
{
  private static final long serialVersionUID = 7507121069969409061L;
  private String localName_;
  
  public void setLocalName(String localName)
  {
    localName_ = localName;
  }
  
  public ElementSelectorImpl(String localName) {
    localName_ = localName;
  }
  
  public short getSelectorType() {
    return 4;
  }
  
  public String getNamespaceURI() {
    return null;
  }
  
  public String getLocalName() {
    return localName_;
  }
  


  public String getCssText(CSSFormat format)
  {
    String localeName = getLocalName();
    if (localeName == null) {
      return "*";
    }
    return localeName;
  }
  
  public String toString()
  {
    return getCssText(null);
  }
}
