package com.steadystate.css.parser.selectors;

import com.steadystate.css.format.CSSFormat;
import com.steadystate.css.format.CSSFormatable;
import com.steadystate.css.parser.LocatableImpl;
import java.io.Serializable;
import org.w3c.css.sac.AttributeCondition;






















public class ClassConditionImpl
  extends LocatableImpl
  implements AttributeCondition, CSSFormatable, Serializable
{
  private static final long serialVersionUID = -2216489300949054242L;
  private String value_;
  
  public void setValue(String value)
  {
    value_ = value;
  }
  
  public ClassConditionImpl(String value) {
    setValue(value);
  }
  
  public short getConditionType() {
    return 9;
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
      return "." + value;
    }
    return ".";
  }
  
  public String toString()
  {
    return getCssText(null);
  }
}
