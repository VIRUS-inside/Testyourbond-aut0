package com.steadystate.css.dom;

import com.steadystate.css.format.CSSFormat;
import com.steadystate.css.format.CSSFormatable;
import com.steadystate.css.parser.CSSOMParser;
import com.steadystate.css.util.LangUtils;
import java.io.IOException;
import java.io.StringReader;
import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.SelectorList;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleRule;






















public class CSSStyleRuleImpl
  extends AbstractCSSRuleImpl
  implements CSSStyleRule
{
  private static final long serialVersionUID = -697009251364657426L;
  private SelectorList selectors_;
  private CSSStyleDeclaration style_;
  
  public SelectorList getSelectors()
  {
    return selectors_;
  }
  
  public void setSelectors(SelectorList selectors) {
    selectors_ = selectors;
  }
  
  public CSSStyleRuleImpl(CSSStyleSheetImpl parentStyleSheet, CSSRule parentRule, SelectorList selectors)
  {
    super(parentStyleSheet, parentRule);
    selectors_ = selectors;
  }
  

  public CSSStyleRuleImpl() {}
  
  public short getType()
  {
    return 1;
  }
  



  public String getCssText(CSSFormat format)
  {
    CSSStyleDeclaration style = getStyle();
    if (null == style) {
      return "";
    }
    
    String selectorText = ((CSSFormatable)selectors_).getCssText(format);
    String styleText = ((CSSFormatable)style).getCssText(format);
    
    if ((null == styleText) || (styleText.length() == 0)) {
      return selectorText + " { }";
    }
    
    if ((format != null) && (format.getPropertiesInSeparateLines())) {
      return selectorText + " {" + styleText + "}";
    }
    
    return selectorText + " { " + styleText + " }";
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
      

      if (r.getType() == 1) {
        selectors_ = selectors_;
        style_ = style_;
      }
      else {
        throw new DOMExceptionImpl((short)13, 4);

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
    return selectors_.toString();
  }
  
  public void setSelectorText(String selectorText) throws DOMException {
    CSSStyleSheetImpl parentStyleSheet = getParentStyleSheetImpl();
    if ((parentStyleSheet != null) && (parentStyleSheet.isReadOnly())) {
      throw new DOMExceptionImpl((short)7, 2);
    }
    

    try
    {
      InputSource is = new InputSource(new StringReader(selectorText));
      CSSOMParser parser = new CSSOMParser();
      selectors_ = parser.parseSelectors(is);

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
  
  public CSSStyleDeclaration getStyle() {
    return style_;
  }
  
  public void setStyle(CSSStyleDeclaration style) {
    style_ = style;
  }
  
  public String toString()
  {
    return getCssText();
  }
  
  public boolean equals(Object obj)
  {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof CSSStyleRule)) {
      return false;
    }
    CSSStyleRule csr = (CSSStyleRule)obj;
    

    return (super.equals(obj)) && (LangUtils.equals(getSelectorText(), csr.getSelectorText())) && (LangUtils.equals(getStyle(), csr.getStyle()));
  }
  
  public int hashCode()
  {
    int hash = super.hashCode();
    hash = LangUtils.hashCode(hash, selectors_);
    hash = LangUtils.hashCode(hash, style_);
    return hash;
  }
}
