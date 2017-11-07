package com.steadystate.css.parser.selectors;

import com.steadystate.css.format.CSSFormat;
import com.steadystate.css.format.CSSFormatable;
import com.steadystate.css.parser.LocatableImpl;
import java.io.Serializable;
import org.w3c.css.sac.LangCondition;






















public class LangConditionImpl
  extends LocatableImpl
  implements LangCondition, CSSFormatable, Serializable
{
  private static final long serialVersionUID = 1701599531953055387L;
  private String lang_;
  
  public void setLang(String lang)
  {
    lang_ = lang;
  }
  
  public LangConditionImpl(String lang) {
    setLang(lang);
  }
  
  public short getConditionType() {
    return 6;
  }
  
  public String getLang() {
    return lang_;
  }
  


  public String getCssText(CSSFormat format)
  {
    StringBuilder result = new StringBuilder();
    result.append(":lang(");
    
    String lang = getLang();
    if (null != lang) {
      result.append(lang);
    }
    
    result.append(")");
    return result.toString();
  }
  
  public String toString()
  {
    return getCssText(null);
  }
}
