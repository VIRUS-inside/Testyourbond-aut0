package com.steadystate.css.dom;

import com.steadystate.css.format.CSSFormat;
import com.steadystate.css.parser.CSSOMParser;
import com.steadystate.css.util.LangUtils;
import java.io.IOException;
import java.io.StringReader;
import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSUnknownRule;






















public class CSSUnknownRuleImpl
  extends AbstractCSSRuleImpl
  implements CSSUnknownRule
{
  private static final long serialVersionUID = -268104019127675990L;
  private String text_;
  
  public String getText()
  {
    return text_;
  }
  
  public void setText(String text) {
    text_ = text;
  }
  


  public CSSUnknownRuleImpl(CSSStyleSheetImpl parentStyleSheet, CSSRule parentRule, String text)
  {
    super(parentStyleSheet, parentRule);
    text_ = text;
  }
  

  public CSSUnknownRuleImpl() {}
  
  public short getType()
  {
    return 0;
  }
  



  public String getCssText(CSSFormat format)
  {
    if (null == text_) {
      return "";
    }
    return text_;
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
      

      if (r.getType() == 0) {
        text_ = text_;
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
  
  public String toString()
  {
    return getCssText(null);
  }
  
  public boolean equals(Object obj)
  {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof CSSUnknownRule)) {
      return false;
    }
    CSSUnknownRule cur = (CSSUnknownRule)obj;
    
    return (super.equals(obj)) && (LangUtils.equals(getCssText(), cur.getCssText()));
  }
  
  public int hashCode()
  {
    int hash = super.hashCode();
    hash = LangUtils.hashCode(hash, text_);
    return hash;
  }
}
