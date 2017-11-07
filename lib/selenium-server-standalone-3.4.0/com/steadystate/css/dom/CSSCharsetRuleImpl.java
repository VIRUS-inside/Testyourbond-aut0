package com.steadystate.css.dom;

import com.steadystate.css.format.CSSFormat;
import com.steadystate.css.parser.CSSOMParser;
import com.steadystate.css.util.LangUtils;
import java.io.IOException;
import java.io.StringReader;
import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSCharsetRule;
import org.w3c.dom.css.CSSRule;

























public class CSSCharsetRuleImpl
  extends AbstractCSSRuleImpl
  implements CSSCharsetRule
{
  private static final long serialVersionUID = -2472209213089007127L;
  private String encoding_;
  
  public CSSCharsetRuleImpl(CSSStyleSheetImpl parentStyleSheet, CSSRule parentRule, String encoding)
  {
    super(parentStyleSheet, parentRule);
    encoding_ = encoding;
  }
  

  public CSSCharsetRuleImpl() {}
  
  public short getType()
  {
    return 2;
  }
  



  public String getCssText(CSSFormat format)
  {
    StringBuilder sb = new StringBuilder();
    
    sb.append("@charset \"");
    
    String enc = getEncoding();
    if (null != enc) {
      sb.append(enc);
    }
    sb.append("\";");
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
      

      if (r.getType() == 2) {
        encoding_ = encoding_;
      }
      else {
        throw new DOMExceptionImpl((short)13, 5);

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
  
  public String getEncoding() {
    return encoding_;
  }
  
  public void setEncoding(String encoding) throws DOMException {
    encoding_ = encoding;
  }
  
  public boolean equals(Object obj)
  {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof CSSCharsetRule)) {
      return false;
    }
    CSSCharsetRule ccr = (CSSCharsetRule)obj;
    
    return (super.equals(obj)) && (LangUtils.equals(getEncoding(), ccr.getEncoding()));
  }
  
  public int hashCode()
  {
    int hash = super.hashCode();
    hash = LangUtils.hashCode(hash, encoding_);
    return hash;
  }
  
  public String toString()
  {
    return getCssText(null);
  }
}
