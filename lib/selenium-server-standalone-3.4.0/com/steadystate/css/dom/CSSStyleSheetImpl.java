package com.steadystate.css.dom;

import com.steadystate.css.format.CSSFormat;
import com.steadystate.css.format.CSSFormatable;
import com.steadystate.css.parser.CSSOMParser;
import com.steadystate.css.util.LangUtils;
import com.steadystate.css.util.ThrowCssExceptionErrorHandler;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.SACMediaList;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.css.CSSImportRule;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleSheet;
import org.w3c.dom.stylesheets.MediaList;
import org.w3c.dom.stylesheets.StyleSheet;























public class CSSStyleSheetImpl
  implements CSSStyleSheet, CSSFormatable, Serializable
{
  private static final long serialVersionUID = -2300541300646796363L;
  private boolean disabled_;
  private Node ownerNode_;
  private StyleSheet parentStyleSheet_;
  private String href_;
  private String title_;
  private MediaList media_;
  private CSSRule ownerRule_;
  private boolean readOnly_;
  private CSSRuleList cssRules_;
  private String baseUri_;
  
  public void setMedia(MediaList media)
  {
    media_ = media;
  }
  
  private String getBaseUri() {
    return baseUri_;
  }
  
  public void setBaseUri(String baseUri) {
    baseUri_ = baseUri;
  }
  

  public CSSStyleSheetImpl() {}
  
  public String getType()
  {
    return "text/css";
  }
  
  public boolean getDisabled() {
    return disabled_;
  }
  



  public void setDisabled(boolean disabled)
  {
    disabled_ = disabled;
  }
  
  public Node getOwnerNode() {
    return ownerNode_;
  }
  
  public StyleSheet getParentStyleSheet() {
    return parentStyleSheet_;
  }
  
  public String getHref() {
    return href_;
  }
  
  public String getTitle() {
    return title_;
  }
  
  public MediaList getMedia() {
    return media_;
  }
  
  public CSSRule getOwnerRule() {
    return ownerRule_;
  }
  
  public CSSRuleList getCssRules() {
    if (cssRules_ == null) {
      cssRules_ = new CSSRuleListImpl();
    }
    return cssRules_;
  }
  
  public int insertRule(String rule, int index) throws DOMException {
    if (readOnly_) {
      throw new DOMExceptionImpl((short)7, 2);
    }
    

    try
    {
      InputSource is = new InputSource(new StringReader(rule));
      CSSOMParser parser = new CSSOMParser();
      parser.setParentStyleSheet(this);
      parser.setErrorHandler(ThrowCssExceptionErrorHandler.INSTANCE);
      CSSRule r = parser.parseRule(is);
      
      if (r == null)
      {
        throw new DOMExceptionImpl(12, 0, "Parsing rule '" + rule + "' failed.");
      }
      



      if (getCssRules().getLength() > 0)
      {

        int msg = -1;
        if (r.getType() == 2)
        {

          if (index != 0) {
            msg = 15;
          }
          else if (getCssRules().item(0).getType() == 2) {
            msg = 16;
          }
        }
        else if (r.getType() == 3)
        {

          if (index <= getCssRules().getLength()) {
            for (int i = 0; i < index; i++) {
              int rt = getCssRules().item(i).getType();
              if ((rt != 2) && (rt != 3)) {
                msg = 17;
                break;
              }
              
            }
          }
        }
        else if (index <= getCssRules().getLength()) {
          for (int i = index; i < getCssRules().getLength(); i++) {
            int rt = getCssRules().item(i).getType();
            if ((rt == 2) || (rt == 3)) {
              msg = 20;
              break;
            }
          }
        }
        
        if (msg > -1) {
          throw new DOMExceptionImpl((short)3, msg);
        }
      }
      

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
    if (readOnly_) {
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
  
  public boolean isReadOnly() {
    return readOnly_;
  }
  
  public void setReadOnly(boolean b) {
    readOnly_ = b;
  }
  
  public void setOwnerNode(Node ownerNode) {
    ownerNode_ = ownerNode;
  }
  
  public void setParentStyleSheet(StyleSheet parentStyleSheet) {
    parentStyleSheet_ = parentStyleSheet;
  }
  
  public void setHref(String href) {
    href_ = href;
  }
  
  public void setTitle(String title) {
    title_ = title;
  }
  
  public void setMediaText(String mediaText) {
    InputSource source = new InputSource(new StringReader(mediaText));
    try {
      CSSOMParser parser = new CSSOMParser();
      SACMediaList sml = parser.parseMedia(source);
      media_ = new MediaListImpl(sml);
    }
    catch (IOException e) {}
  }
  

  public void setOwnerRule(CSSRule ownerRule)
  {
    ownerRule_ = ownerRule;
  }
  
  public void setCssRules(CSSRuleList rules) {
    cssRules_ = rules;
  }
  




  public String getCssText()
  {
    return getCssText(null);
  }
  


  public String getCssText(CSSFormat format)
  {
    CSSRuleList rules = getCssRules();
    if ((rules instanceof CSSFormatable)) {
      return ((CSSRuleListImpl)rules).getCssText(format);
    }
    return getCssRules().toString();
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
    if (!(obj instanceof CSSStyleSheet)) {
      return false;
    }
    CSSStyleSheet css = (CSSStyleSheet)obj;
    boolean eq = LangUtils.equals(getCssRules(), css.getCssRules());
    eq = (eq) && (getDisabled() == css.getDisabled());
    eq = (eq) && (LangUtils.equals(getHref(), css.getHref()));
    eq = (eq) && (LangUtils.equals(getMedia(), css.getMedia()));
    



    eq = (eq) && (LangUtils.equals(getTitle(), css.getTitle()));
    return eq;
  }
  
  public int hashCode()
  {
    int hash = 17;
    hash = LangUtils.hashCode(hash, baseUri_);
    hash = LangUtils.hashCode(hash, cssRules_);
    hash = LangUtils.hashCode(hash, disabled_);
    hash = LangUtils.hashCode(hash, href_);
    hash = LangUtils.hashCode(hash, media_);
    hash = LangUtils.hashCode(hash, ownerNode_);
    

    hash = LangUtils.hashCode(hash, readOnly_);
    hash = LangUtils.hashCode(hash, title_);
    return hash;
  }
  
  private void writeObject(ObjectOutputStream out) throws IOException {
    out.writeObject(baseUri_);
    out.writeObject(cssRules_);
    out.writeBoolean(disabled_);
    out.writeObject(href_);
    out.writeObject(media_);
    

    out.writeBoolean(readOnly_);
    out.writeObject(title_);
  }
  
  private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    baseUri_ = ((String)in.readObject());
    cssRules_ = ((CSSRuleList)in.readObject());
    if (cssRules_ != null) {
      for (int i = 0; i < cssRules_.getLength(); i++) {
        CSSRule cssRule = cssRules_.item(i);
        if ((cssRule instanceof AbstractCSSRuleImpl)) {
          ((AbstractCSSRuleImpl)cssRule).setParentStyleSheet(this);
        }
      }
    }
    disabled_ = in.readBoolean();
    href_ = ((String)in.readObject());
    media_ = ((MediaList)in.readObject());
    

    readOnly_ = in.readBoolean();
    title_ = ((String)in.readObject());
  }
  




  public void importImports(boolean recursive)
    throws DOMException
  {
    for (int i = 0; i < getCssRules().getLength(); i++) {
      CSSRule cssRule = getCssRules().item(i);
      if (cssRule.getType() == 3) {
        CSSImportRule cssImportRule = (CSSImportRule)cssRule;
        try {
          URI importURI = new URI(getBaseUri()).resolve(cssImportRule.getHref());
          
          CSSStyleSheetImpl importedCSS = (CSSStyleSheetImpl)new CSSOMParser().parseStyleSheet(new InputSource(importURI
            .toString()), null, importURI.toString());
          if (recursive) {
            importedCSS.importImports(recursive);
          }
          MediaList mediaList = cssImportRule.getMedia();
          if (mediaList.getLength() == 0) {
            mediaList.appendMedium("all");
          }
          CSSMediaRuleImpl cssMediaRule = new CSSMediaRuleImpl(this, null, mediaList);
          cssMediaRule.setRuleList((CSSRuleListImpl)importedCSS.getCssRules());
          deleteRule(i);
          ((CSSRuleListImpl)getCssRules()).insert(cssMediaRule, i);
        }
        catch (URISyntaxException e) {
          throw new DOMException((short)12, e.getLocalizedMessage());
        }
        catch (IOException e) {}
      }
    }
  }
}
