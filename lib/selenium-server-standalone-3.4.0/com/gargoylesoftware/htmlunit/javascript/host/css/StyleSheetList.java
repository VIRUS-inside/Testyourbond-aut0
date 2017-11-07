package com.gargoylesoftware.htmlunit.javascript.host.css;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlAttributeChangeEvent;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlLink;
import com.gargoylesoftware.htmlunit.html.HtmlStyle;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.dom.AbstractList.EffectOnCache;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLCollection;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLLinkElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLStyleElement;
import com.steadystate.css.dom.MediaListImpl;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;
import org.apache.commons.lang3.StringUtils;
import org.w3c.css.sac.SACMediaList;







































@JsxClass
public class StyleSheetList
  extends SimpleScriptable
{
  private HTMLCollection nodes_;
  
  public static boolean isStyleSheetLink(DomNode domNode)
  {
    return ((domNode instanceof HtmlLink)) && 
      ("stylesheet".equalsIgnoreCase(((HtmlLink)domNode).getRelAttribute()));
  }
  





  public boolean isActiveStyleSheetLink(DomNode domNode)
  {
    if ((domNode instanceof HtmlLink)) {
      HtmlLink link = (HtmlLink)domNode;
      if ("stylesheet".equalsIgnoreCase(link.getRelAttribute())) {
        String media = link.getMediaAttribute();
        if (StringUtils.isBlank(media)) {
          return true;
        }
        WebClient webClient = getWindow().getWebWindow().getWebClient();
        SACMediaList mediaList = CSSStyleSheet.parseMedia(webClient.getCssErrorHandler(), media);
        return CSSStyleSheet.isActive(this, new MediaListImpl(mediaList));
      }
    }
    return false;
  }
  




  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public StyleSheetList() {}
  




  public StyleSheetList(HTMLDocument document)
  {
    setParentScope(document);
    setPrototype(getPrototype(getClass()));
    
    WebClient webClient = getWindow().getWebWindow().getWebClient();
    boolean cssEnabled = webClient.getOptions().isCssEnabled();
    final boolean onlyActive = webClient.getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_STYLESHEETLIST_ACTIVE_ONLY);
    
    if (cssEnabled) {
      nodes_ = new HTMLCollection(document.getDomNodeOrDie(), true)
      {
        protected boolean isMatching(DomNode node) {
          if ((node instanceof HtmlStyle)) {
            return true;
          }
          if (onlyActive) {
            return isActiveStyleSheetLink(node);
          }
          return StyleSheetList.isStyleSheetLink(node);
        }
        
        protected AbstractList.EffectOnCache getEffectOnCache(HtmlAttributeChangeEvent event)
        {
          HtmlElement node = event.getHtmlElement();
          if (((node instanceof HtmlLink)) && ("rel".equalsIgnoreCase(event.getName()))) {
            return AbstractList.EffectOnCache.RESET;
          }
          return AbstractList.EffectOnCache.NONE;
        }
        
      };
    } else {
      nodes_ = HTMLCollection.emptyCollection(getWindow().getDomNodeOrDie());
    }
  }
  




  @JsxGetter
  public int getLength()
  {
    return nodes_.getLength();
  }
  





  @JsxFunction
  public Object item(int index)
  {
    if ((nodes_ == null) || (index < 0) || (index >= nodes_.getLength())) {
      return Undefined.instance;
    }
    
    HTMLElement element = (HTMLElement)nodes_.item(Integer.valueOf(index));
    
    CSSStyleSheet sheet;
    CSSStyleSheet sheet;
    if ((element instanceof HTMLStyleElement)) {
      sheet = ((HTMLStyleElement)element).getSheet();
    }
    else
    {
      sheet = ((HTMLLinkElement)element).getSheet();
    }
    
    return sheet;
  }
  



  public Object get(int index, Scriptable start)
  {
    if (this == start) {
      return item(index);
    }
    return super.get(index, start);
  }
  



  protected Object equivalentValues(Object value)
  {
    if ((getClass() == value.getClass()) && (getDomNodeOrNull() == ((StyleSheetList)value).getDomNodeOrNull())) return Boolean.valueOf(true); return Boolean.valueOf(false);
  }
}
