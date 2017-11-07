package com.steadystate.css.dom;

import com.steadystate.css.util.LangUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.w3c.dom.css.CSSStyleSheet;
import org.w3c.dom.stylesheets.StyleSheet;
import org.w3c.dom.stylesheets.StyleSheetList;





















public class CSSStyleSheetListImpl
  implements StyleSheetList
{
  private List<CSSStyleSheet> cssStyleSheets_;
  
  public List<CSSStyleSheet> getCSSStyleSheets()
  {
    if (cssStyleSheets_ == null) {
      cssStyleSheets_ = new ArrayList();
    }
    return cssStyleSheets_;
  }
  
  public void setCSSStyleSheets(List<CSSStyleSheet> cssStyleSheets) {
    cssStyleSheets_ = cssStyleSheets;
  }
  



  public CSSStyleSheetListImpl() {}
  


  public int getLength()
  {
    return getCSSStyleSheets().size();
  }
  
  public StyleSheet item(int index) {
    return (StyleSheet)getCSSStyleSheets().get(index);
  }
  




  public void add(CSSStyleSheet cssStyleSheet)
  {
    getCSSStyleSheets().add(cssStyleSheet);
  }
  





  public StyleSheet merge()
  {
    CSSStyleSheetImpl merged = new CSSStyleSheetImpl();
    CSSRuleListImpl cssRuleList = new CSSRuleListImpl();
    Iterator<CSSStyleSheet> it = getCSSStyleSheets().iterator();
    while (it.hasNext()) {
      CSSStyleSheetImpl cssStyleSheet = (CSSStyleSheetImpl)it.next();
      CSSMediaRuleImpl cssMediaRule = new CSSMediaRuleImpl(merged, null, cssStyleSheet.getMedia());
      cssMediaRule.setRuleList((CSSRuleListImpl)cssStyleSheet.getCssRules());
      cssRuleList.add(cssMediaRule);
    }
    merged.setCssRules(cssRuleList);
    merged.setMediaText("all");
    return merged;
  }
  
  public boolean equals(Object obj)
  {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof StyleSheetList)) {
      return false;
    }
    StyleSheetList ssl = (StyleSheetList)obj;
    return equalsStyleSheets(ssl);
  }
  
  private boolean equalsStyleSheets(StyleSheetList ssl) {
    if ((ssl == null) || (getLength() != ssl.getLength())) {
      return false;
    }
    for (int i = 0; i < getLength(); i++) {
      StyleSheet styleSheet1 = item(i);
      StyleSheet styleSheet2 = ssl.item(i);
      if (!LangUtils.equals(styleSheet1, styleSheet2)) {
        return false;
      }
    }
    return true;
  }
  
  public int hashCode()
  {
    int hash = 17;
    hash = LangUtils.hashCode(hash, cssStyleSheets_);
    return hash;
  }
}
