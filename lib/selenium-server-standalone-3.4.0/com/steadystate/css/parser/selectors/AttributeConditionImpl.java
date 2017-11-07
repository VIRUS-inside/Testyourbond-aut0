package com.steadystate.css.parser.selectors;

import com.steadystate.css.format.CSSFormat;
import com.steadystate.css.format.CSSFormatable;
import com.steadystate.css.parser.LocatableImpl;
import java.io.Serializable;
import org.w3c.css.sac.AttributeCondition;





















public class AttributeConditionImpl
  extends LocatableImpl
  implements AttributeCondition, CSSFormatable, Serializable
{
  private static final long serialVersionUID = 9035418830958954213L;
  private String localName_;
  private String value_;
  private boolean specified_;
  
  public void setLocaleName(String localName)
  {
    localName_ = localName;
  }
  
  public void setValue(String value) {
    value_ = value;
  }
  
  public void setSpecified(boolean specified) {
    specified_ = specified;
  }
  
  public AttributeConditionImpl(String localName, String value, boolean specified) {
    setLocaleName(localName);
    setValue(value);
    setSpecified(specified);
  }
  
  public short getConditionType() {
    return 4;
  }
  
  public String getNamespaceURI() {
    return null;
  }
  
  public String getLocalName() {
    return localName_;
  }
  
  public boolean getSpecified() {
    return specified_;
  }
  
  public String getValue() {
    return value_;
  }
  


  public String getCssText(CSSFormat format)
  {
    String value = getValue();
    if (value != null) {
      return "[" + getLocalName() + "=\"" + value + "\"]";
    }
    return "[" + getLocalName() + "]";
  }
  
  public String toString()
  {
    return getCssText(null);
  }
}
