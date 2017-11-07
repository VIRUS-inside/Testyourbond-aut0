package com.steadystate.css.dom;

import com.steadystate.css.format.CSSFormat;
import com.steadystate.css.parser.CSSOMParser;
import com.steadystate.css.util.LangUtils;
import java.io.IOException;
import java.io.StringReader;
import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSFontFaceRule;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSStyleDeclaration;






















public class CSSFontFaceRuleImpl
  extends AbstractCSSRuleImpl
  implements CSSFontFaceRule
{
  private static final long serialVersionUID = -3604191834588759088L;
  private CSSStyleDeclarationImpl style_;
  
  public CSSFontFaceRuleImpl(CSSStyleSheetImpl parentStyleSheet, CSSRule parentRule)
  {
    super(parentStyleSheet, parentRule);
  }
  

  public CSSFontFaceRuleImpl() {}
  
  public short getType()
  {
    return 5;
  }
  



  public String getCssText(CSSFormat format)
  {
    StringBuilder sb = new StringBuilder();
    sb.append("@font-face {");
    
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
      

      if (r.getType() == 5) {
        style_ = style_;
      }
      else {
        throw new DOMExceptionImpl((short)13, 8);

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
  
  public CSSStyleDeclaration getStyle() {
    return style_;
  }
  
  public void setStyle(CSSStyleDeclarationImpl style) {
    style_ = style;
  }
  
  public boolean equals(Object obj)
  {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof CSSFontFaceRule)) {
      return false;
    }
    CSSFontFaceRule cffr = (CSSFontFaceRule)obj;
    
    return (super.equals(obj)) && (LangUtils.equals(getStyle(), cffr.getStyle()));
  }
  
  public int hashCode()
  {
    int hash = super.hashCode();
    hash = LangUtils.hashCode(hash, style_);
    return hash;
  }
  
  public String toString()
  {
    return getCssText(null);
  }
}
