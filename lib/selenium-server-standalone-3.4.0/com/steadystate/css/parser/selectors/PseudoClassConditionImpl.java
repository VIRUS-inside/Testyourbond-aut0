package com.steadystate.css.parser.selectors;

import com.steadystate.css.format.CSSFormat;
import com.steadystate.css.format.CSSFormatable;
import com.steadystate.css.parser.LocatableImpl;
import java.io.Serializable;
import org.w3c.css.sac.AttributeCondition;






















public class PseudoClassConditionImpl
  extends LocatableImpl
  implements AttributeCondition, CSSFormatable, Serializable
{
  private static final long serialVersionUID = 1798016773089155610L;
  private String value_;
  
  public void setValue(String value)
  {
    value_ = value;
  }
  
  public PseudoClassConditionImpl(String value) {
    setValue(value);
  }
  
  public short getConditionType() {
    return 10;
  }
  
  public String getNamespaceURI() {
    return null;
  }
  
  public String getLocalName() {
    return null;
  }
  
  public boolean getSpecified() {
    return true;
  }
  
  public String getValue() {
    return value_;
  }
  


  public String getCssText(CSSFormat format)
  {
    String value = getValue();
    if (value != null) {
      return ":" + value;
    }
    return ":";
  }
  
  public String toString()
  {
    return getCssText(null);
  }
}
