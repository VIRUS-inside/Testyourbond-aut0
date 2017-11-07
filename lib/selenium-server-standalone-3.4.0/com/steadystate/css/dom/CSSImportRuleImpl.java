package com.steadystate.css.dom;

import com.steadystate.css.format.CSSFormat;
import com.steadystate.css.parser.CSSOMParser;
import com.steadystate.css.util.LangUtils;
import java.io.IOException;
import java.io.StringReader;
import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSImportRule;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSStyleSheet;
import org.w3c.dom.stylesheets.MediaList;
























public class CSSImportRuleImpl
  extends AbstractCSSRuleImpl
  implements CSSImportRule
{
  private static final long serialVersionUID = 7807829682009179339L;
  private String href_;
  private MediaList media_;
  
  public void setHref(String href)
  {
    href_ = href;
  }
  
  public void setMedia(MediaList media) {
    media_ = media;
  }
  



  public CSSImportRuleImpl(CSSStyleSheetImpl parentStyleSheet, CSSRule parentRule, String href, MediaList media)
  {
    super(parentStyleSheet, parentRule);
    href_ = href;
    media_ = media;
  }
  

  public CSSImportRuleImpl() {}
  
  public short getType()
  {
    return 3;
  }
  



  public String getCssText(CSSFormat format)
  {
    StringBuilder sb = new StringBuilder();
    sb.append("@import");
    
    String href = getHref();
    if (null != href) {
      sb.append(" url(").append(href).append(")");
    }
    
    MediaList ml = getMedia();
    if ((null != ml) && (ml.getLength() > 0)) {
      sb.append(" ").append(((MediaListImpl)getMedia()).getMediaText(format));
    }
    sb.append(";");
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
      

      if (r.getType() == 3) {
        href_ = href_;
        media_ = media_;
      }
      else {
        throw new DOMExceptionImpl((short)13, 6);

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
  
  public String getHref() {
    return href_;
  }
  
  public MediaList getMedia() {
    return media_;
  }
  
  public CSSStyleSheet getStyleSheet() {
    return null;
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
    if (!(obj instanceof CSSImportRule)) {
      return false;
    }
    CSSImportRule cir = (CSSImportRule)obj;
    

    return (super.equals(obj)) && (LangUtils.equals(getHref(), cir.getHref())) && (LangUtils.equals(getMedia(), cir.getMedia()));
  }
  
  public int hashCode()
  {
    int hash = super.hashCode();
    hash = LangUtils.hashCode(hash, href_);
    hash = LangUtils.hashCode(hash, media_);
    return hash;
  }
}
