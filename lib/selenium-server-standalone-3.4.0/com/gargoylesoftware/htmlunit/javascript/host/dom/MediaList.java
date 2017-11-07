package com.gargoylesoftware.htmlunit.javascript.host.dom;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleSheet;
import com.steadystate.css.dom.MediaListImpl;
import com.steadystate.css.parser.media.MediaQuery;





























@JsxClass
public class MediaList
  extends SimpleScriptable
{
  private final org.w3c.dom.stylesheets.MediaList wrappedList_;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public MediaList()
  {
    wrappedList_ = null;
  }
  




  public MediaList(CSSStyleSheet parent, org.w3c.dom.stylesheets.MediaList wrappedList)
  {
    wrappedList_ = wrappedList;
    setParentScope(parent);
    setPrototype(getPrototype(getClass()));
  }
  




  @JsxFunction
  public String item(int index)
  {
    if ((index < 0) || (index >= getLength())) {
      return null;
    }
    MediaQuery mq = ((MediaListImpl)wrappedList_).mediaQuery(index);
    return mq.toString();
  }
  



  @JsxGetter
  public int getLength()
  {
    return wrappedList_.getLength();
  }
  




  @JsxGetter
  public String getMediaText()
  {
    return wrappedList_.getMediaText();
  }
  
  public Object getDefaultValue(Class<?> hint)
  {
    if (getPrototype() != null) {
      BrowserVersion browserVersion = getBrowserVersion();
      if (browserVersion.hasFeature(BrowserVersionFeatures.JS_MEDIA_LIST_EMPTY_STRING)) {
        return "";
      }
      if (browserVersion.hasFeature(BrowserVersionFeatures.JS_MEDIA_LIST_ALL)) {
        return "all";
      }
    }
    return super.getDefaultValue(hint);
  }
}
