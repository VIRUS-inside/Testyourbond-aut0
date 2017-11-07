package com.steadystate.css.dom;

import com.steadystate.css.format.CSSFormat;
import com.steadystate.css.parser.CSSOMParser;
import com.steadystate.css.util.LangUtils;
import java.io.IOException;
import java.io.StringReader;
import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSPageRule;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSStyleDeclaration;



























public class CSSPageRuleImpl
  extends AbstractCSSRuleImpl
  implements CSSPageRule
{
  private static final long serialVersionUID = -6007519872104320812L;
  private String pseudoPage_;
  private CSSStyleDeclaration style_;
  
  public CSSPageRuleImpl(CSSStyleSheetImpl parentStyleSheet, CSSRule parentRule, String pseudoPage)
  {
    super(parentStyleSheet, parentRule);
    pseudoPage_ = pseudoPage;
  }
  

  public CSSPageRuleImpl() {}
  
  public short getType()
  {
    return 6;
  }
  



  public String getCssText(CSSFormat format)
  {
    StringBuilder sb = new StringBuilder();
    
    String sel = getSelectorText();
    sb.append("@page ").append(sel);
    
    if (sel.length() > 0) {
      sb.append(" ");
    }
    sb.append("{");
    
    CSSStyleDeclaration style = getStyle();
    if (null != style) {
      sb.append(style.getCssText());
    }
    sb.append("}");
    return sb.toString();
  }
  
  public void setCssText(String cssText) throws DOMException {
    CSSStyleSheetImpl parentStyleSheet = getParentStyleSheetImpl();
    if ((parentStyleSheet != null) && (parentStyleSheet.isReadOnly())) {
      throw new DOMExceptionImpl((short)7, 2);
    }
    

    try
    {
      InputSource is = new InputSource(new StringReader(cssText));
      CSSOMParser parser = new CSSOMParser();
      CSSRule r = parser.parseRule(is);
      

      if (r.getType() == 6) {
        pseudoPage_ = pseudoPage_;
        style_ = style_;
      }
      else {
        throw new DOMExceptionImpl((short)13, 9);

      }
      

    }
    catch (CSSException e)
    {

      throw new DOMExceptionImpl(12, 0, e.getMessage());

    }
    catch (IOException e)
    {

      throw new DOMExceptionImpl(12, 0, e.getMessage());
    }
  }
  
  public String getSelectorText() {
    if (null == pseudoPage_) {
      return "";
    }
    return pseudoPage_;
  }
  
  public void setSelectorText(String selectorText) throws DOMException
  {}
  
  public CSSStyleDeclaration getStyle() {
    return style_;
  }
  
  public void setPseudoPage(String pseudoPage) {
    pseudoPage_ = pseudoPage;
  }
  
  public void setStyle(CSSStyleDeclarationImpl style) {
    style_ = style;
  }
  
  public boolean equals(Object obj)
  {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof CSSPageRule)) {
      return false;
    }
    CSSPageRule cpr = (CSSPageRule)obj;
    

    return (super.equals(obj)) && (LangUtils.equals(getSelectorText(), cpr.getSelectorText())) && (LangUtils.equals(getStyle(), cpr.getStyle()));
  }
  
  public int hashCode()
  {
    int hash = super.hashCode();
    hash = LangUtils.hashCode(hash, pseudoPage_);
    hash = LangUtils.hashCode(hash, style_);
    return hash;
  }
  
  public String toString()
  {
    return getCssText(null);
  }
}
