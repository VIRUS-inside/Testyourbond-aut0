package com.steadystate.css.dom;

import com.steadystate.css.format.CSSFormat;
import com.steadystate.css.format.CSSFormatable;
import com.steadystate.css.util.LangUtils;
import org.w3c.dom.css.CSSValue;
























public class Property
  extends CSSOMObjectImpl
  implements CSSFormatable
{
  private static final long serialVersionUID = 8720637891949104989L;
  private String name_;
  private CSSValue value_;
  private boolean important_;
  
  public Property(String name, CSSValue value, boolean important)
  {
    name_ = name;
    value_ = value;
    important_ = important;
  }
  





  public Property() {}
  




  public String getName()
  {
    return name_;
  }
  



  public void setName(String name)
  {
    name_ = name;
  }
  



  public CSSValue getValue()
  {
    return value_;
  }
  



  public boolean isImportant()
  {
    return important_;
  }
  



  public void setValue(CSSValue value)
  {
    value_ = value;
  }
  



  public void setImportant(boolean important)
  {
    important_ = important;
  }
  




  public String getCssText()
  {
    return getCssText(null);
  }
  


  public String getCssText(CSSFormat format)
  {
    StringBuilder sb = new StringBuilder();
    sb.append(name_);
    if (null != value_) {
      sb.append(": ");
      sb.append(((CSSValueImpl)value_).getCssText(format));
    }
    if (important_) {
      sb.append(" !important");
    }
    return sb.toString();
  }
  



  public String toString()
  {
    return getCssText(null);
  }
  



  public boolean equals(Object obj)
  {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Property)) {
      return false;
    }
    Property p = (Property)obj;
    if ((super.equals(obj)) && (important_ == important_)) {}
    

    return (LangUtils.equals(name_, name_)) && (LangUtils.equals(value_, value_));
  }
  



  public int hashCode()
  {
    int hash = super.hashCode();
    hash = LangUtils.hashCode(hash, important_);
    hash = LangUtils.hashCode(hash, name_);
    hash = LangUtils.hashCode(hash, value_);
    return hash;
  }
}
