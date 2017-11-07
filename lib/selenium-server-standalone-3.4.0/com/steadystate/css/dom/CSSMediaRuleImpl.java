package com.steadystate.css.dom;

import com.steadystate.css.format.CSSFormat;
import com.steadystate.css.parser.CSSOMParser;
import com.steadystate.css.util.LangUtils;
import com.steadystate.css.util.ThrowCssExceptionErrorHandler;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSMediaRule;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.stylesheets.MediaList;






















public class CSSMediaRuleImpl
  extends AbstractCSSRuleImpl
  implements CSSMediaRule
{
  private static final long serialVersionUID = 6603734096445214651L;
  private MediaList media_;
  private CSSRuleList cssRules_;
  
  public void setMedia(MediaList media)
  {
    media_ = media;
  }
  
  public void setCssRules(CSSRuleList cssRules) {
    cssRules_ = cssRules;
  }
  


  public CSSMediaRuleImpl(CSSStyleSheetImpl parentStyleSheet, CSSRule parentRule, MediaList media)
  {
    super(parentStyleSheet, parentRule);
    media_ = media;
  }
  

  public CSSMediaRuleImpl() {}
  
  public short getType()
  {
    return 4;
  }
  



  public String getCssText(CSSFormat format)
  {
    StringBuilder sb = new StringBuilder("@media ");
    
    sb.append(((MediaListImpl)getMedia()).getMediaText(format));
    sb.append(" {");
    for (int i = 0; i < getCssRules().getLength(); i++) {
      CSSRule rule = getCssRules().item(i);
      sb.append(rule.getCssText()).append(" ");
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
      

      if (r.getType() == 4) {
        media_ = media_;
        cssRules_ = cssRules_;
      }
      else {
        throw new DOMExceptionImpl((short)13, 7);

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
  
  public MediaList getMedia() {
    return media_;
  }
  
  public CSSRuleList getCssRules() {
    if (cssRules_ == null) {
      cssRules_ = new CSSRuleListImpl();
    }
    return cssRules_;
  }
  
  public int insertRule(String rule, int index) throws DOMException {
    CSSStyleSheetImpl parentStyleSheet = getParentStyleSheetImpl();
    if ((parentStyleSheet != null) && (parentStyleSheet.isReadOnly())) {
      throw new DOMExceptionImpl((short)7, 2);
    }
    

    try
    {
      InputSource is = new InputSource(new StringReader(rule));
      CSSOMParser parser = new CSSOMParser();
      parser.setParentStyleSheet(parentStyleSheet);
      parser.setErrorHandler(ThrowCssExceptionErrorHandler.INSTANCE);
      

      CSSRule r = parser.parseRule(is);
      

      ((CSSRuleListImpl)getCssRules()).insert(r, index);


    }
    catch (IndexOutOfBoundsException e)
    {

      throw new DOMExceptionImpl(1, 1, e.getMessage());

    }
    catch (CSSException e)
    {

      throw new DOMExceptionImpl(12, 0, e.getMessage());

    }
    catch (IOException e)
    {

      throw new DOMExceptionImpl(12, 0, e.getMessage());
    }
    return index;
  }
  
  public void deleteRule(int index) throws DOMException {
    CSSStyleSheetImpl parentStyleSheet = getParentStyleSheetImpl();
    if ((parentStyleSheet != null) && (parentStyleSheet.isReadOnly())) {
      throw new DOMExceptionImpl((short)7, 2);
    }
    
    try
    {
      ((CSSRuleListImpl)getCssRules()).delete(index);

    }
    catch (IndexOutOfBoundsException e)
    {

      throw new DOMExceptionImpl(1, 1, e.getMessage());
    }
  }
  
  public void setRuleList(CSSRuleListImpl rules) {
    cssRules_ = rules;
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
    if (!(obj instanceof CSSMediaRule)) {
      return false;
    }
    CSSMediaRule cmr = (CSSMediaRule)obj;
    

    return (super.equals(obj)) && (LangUtils.equals(getMedia(), cmr.getMedia())) && (LangUtils.equals(getCssRules(), cmr.getCssRules()));
  }
  
  public int hashCode()
  {
    int hash = super.hashCode();
    hash = LangUtils.hashCode(hash, media_);
    hash = LangUtils.hashCode(hash, cssRules_);
    return hash;
  }
  
  private void writeObject(ObjectOutputStream out) throws IOException {
    out.writeObject(cssRules_);
    out.writeObject(media_);
  }
  
  private void readObject(ObjectInputStream in)
    throws IOException, ClassNotFoundException
  {
    cssRules_ = ((CSSRuleList)in.readObject());
    if (cssRules_ != null) {
      for (int i = 0; i < cssRules_.getLength(); i++) {
        CSSRule cssRule = cssRules_.item(i);
        if ((cssRule instanceof AbstractCSSRuleImpl)) {
          ((AbstractCSSRuleImpl)cssRule).setParentRule(this);
          ((AbstractCSSRuleImpl)cssRule).setParentStyleSheet(getParentStyleSheetImpl());
        }
      }
    }
    media_ = ((MediaList)in.readObject());
  }
}
