package com.steadystate.css.parser.selectors;

import com.steadystate.css.format.CSSFormat;
import com.steadystate.css.format.CSSFormatable;
import com.steadystate.css.parser.LocatableImpl;
import java.io.Serializable;
import org.w3c.css.sac.AttributeCondition;





















public class SubstringAttributeConditionImpl
  extends LocatableImpl
  implements AttributeCondition, CSSFormatable, Serializable
{
  private static final long serialVersionUID = 7628763646156568710L;
  private String localName_;
  private String value_;
  private boolean specified_;
  
  public void setLocalName(String localName)
  {
    localName_ = localName;
  }
  
  public void setValue(String value) {
    value_ = value;
  }
  
  public void setSpecified(boolean specified) {
    specified_ = specified;
  }
  
  public SubstringAttributeConditionImpl(String localName, String value, boolean specified) {
    setLocalName(localName);
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
      return "[" + getLocalName() + "*=\"" + value + "\"]";
    }
    return "[" + getLocalName() + "]";
  }
  
  public String toString()
  {
    return getCssText(null);
  }
}
