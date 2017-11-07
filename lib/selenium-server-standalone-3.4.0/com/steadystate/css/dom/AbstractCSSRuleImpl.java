package com.steadystate.css.dom;

import com.steadystate.css.format.CSSFormat;
import com.steadystate.css.format.CSSFormatable;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSStyleSheet;



















public abstract class AbstractCSSRuleImpl
  extends CSSOMObjectImpl
  implements CSSFormatable
{
  private static final long serialVersionUID = 7829784704712797815L;
  private CSSStyleSheetImpl parentStyleSheet_;
  private CSSRule parentRule_;
  
  protected CSSStyleSheetImpl getParentStyleSheetImpl()
  {
    return parentStyleSheet_;
  }
  
  public void setParentStyleSheet(CSSStyleSheetImpl parentStyleSheet) {
    parentStyleSheet_ = parentStyleSheet;
  }
  
  public void setParentRule(CSSRule parentRule) {
    parentRule_ = parentRule;
  }
  
  public AbstractCSSRuleImpl(CSSStyleSheetImpl parentStyleSheet, CSSRule parentRule)
  {
    parentStyleSheet_ = parentStyleSheet;
    parentRule_ = parentRule;
  }
  

  public AbstractCSSRuleImpl() {}
  
  public CSSStyleSheet getParentStyleSheet()
  {
    return parentStyleSheet_;
  }
  
  public CSSRule getParentRule() {
    return parentRule_;
  }
  




  public abstract String getCssText(CSSFormat paramCSSFormat);
  



  public String getCssText()
  {
    return getCssText(null);
  }
  
  public boolean equals(Object obj)
  {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof CSSRule)) {
      return false;
    }
    return super.equals(obj);
  }
  


  public int hashCode()
  {
    int hash = super.hashCode();
    

    return hash;
  }
}
