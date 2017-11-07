package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlAbbreviated;
import com.gargoylesoftware.htmlunit.html.HtmlAcronym;
import com.gargoylesoftware.htmlunit.html.HtmlBidirectionalOverride;
import com.gargoylesoftware.htmlunit.html.HtmlBig;
import com.gargoylesoftware.htmlunit.html.HtmlBlink;
import com.gargoylesoftware.htmlunit.html.HtmlBold;
import com.gargoylesoftware.htmlunit.html.HtmlCitation;
import com.gargoylesoftware.htmlunit.html.HtmlCode;
import com.gargoylesoftware.htmlunit.html.HtmlDefinition;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlEmphasis;
import com.gargoylesoftware.htmlunit.html.HtmlItalic;
import com.gargoylesoftware.htmlunit.html.HtmlKeyboard;
import com.gargoylesoftware.htmlunit.html.HtmlNoBreak;
import com.gargoylesoftware.htmlunit.html.HtmlS;
import com.gargoylesoftware.htmlunit.html.HtmlSample;
import com.gargoylesoftware.htmlunit.html.HtmlSmall;
import com.gargoylesoftware.htmlunit.html.HtmlStrike;
import com.gargoylesoftware.htmlunit.html.HtmlStrong;
import com.gargoylesoftware.htmlunit.html.HtmlSubscript;
import com.gargoylesoftware.htmlunit.html.HtmlSuperscript;
import com.gargoylesoftware.htmlunit.html.HtmlTeletype;
import com.gargoylesoftware.htmlunit.html.HtmlUnderlined;
import com.gargoylesoftware.htmlunit.html.HtmlVariable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClasses;
import com.gargoylesoftware.htmlunit.javascript.host.ActiveXObject;
import com.gargoylesoftware.htmlunit.javascript.host.Window;





















































@JsxClasses({@com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=HtmlAbbreviated.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=HtmlAcronym.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=HtmlBidirectionalOverride.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=HtmlBig.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=HtmlBlink.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=HtmlBold.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=HtmlCitation.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=HtmlCode.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=HtmlDefinition.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=HtmlEmphasis.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=HtmlItalic.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=HtmlKeyboard.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=HtmlNoBreak.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlRt.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlRp.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlRuby.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=HtmlS.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=HtmlSample.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=HtmlSmall.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=HtmlStrike.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=HtmlSubscript.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=HtmlSuperscript.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=HtmlStrong.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=HtmlTeletype.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=HtmlUnderlined.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=HtmlVariable.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})})
public class HTMLPhraseElement
  extends HTMLElement
{
  public HTMLPhraseElement() {}
  
  public void setDomNode(DomNode domNode)
  {
    super.setDomNode(domNode);
    
    if ((getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_XML_SUPPORT_VIA_ACTIVEXOBJECT)) && (
      (((domNode instanceof HtmlAbbreviated)) && (getBrowserVersion().hasFeature(BrowserVersionFeatures.HTMLABBREVIATED))) || 
      ((domNode instanceof HtmlAcronym)) || 
      ((domNode instanceof HtmlBidirectionalOverride)) || 
      ((domNode instanceof HtmlBig)) || 
      ((domNode instanceof HtmlBlink)) || 
      ((domNode instanceof HtmlBold)) || 
      ((domNode instanceof HtmlCitation)) || 
      ((domNode instanceof HtmlCode)) || 
      ((domNode instanceof HtmlDefinition)) || 
      ((domNode instanceof HtmlEmphasis)) || 
      ((domNode instanceof HtmlItalic)) || 
      ((domNode instanceof HtmlKeyboard)) || 
      ((domNode instanceof HtmlNoBreak)) || 
      ((domNode instanceof HtmlS)) || 
      ((domNode instanceof HtmlSample)) || 
      ((domNode instanceof HtmlSmall)) || 
      ((domNode instanceof HtmlStrong)) || 
      ((domNode instanceof HtmlStrike)) || 
      ((domNode instanceof HtmlSubscript)) || 
      ((domNode instanceof HtmlSuperscript)) || 
      ((domNode instanceof HtmlTeletype)) || 
      ((domNode instanceof HtmlUnderlined)) || 
      ((domNode instanceof HtmlVariable))))
    {
      ActiveXObject.addProperty(this, "cite", true, true);
      ActiveXObject.addProperty(this, "dateTime", true, true);
    }
  }
  




  public String getCite()
  {
    String cite = getDomNodeOrDie().getAttribute("cite");
    return cite;
  }
  



  public void setCite(String cite)
  {
    getDomNodeOrDie().setAttribute("cite", cite);
  }
  



  public String getDateTime()
  {
    String dateTime = getDomNodeOrDie().getAttribute("datetime");
    return dateTime;
  }
  



  public void setDateTime(String dateTime)
  {
    getDomNodeOrDie().setAttribute("datetime", dateTime);
  }
  





  protected boolean isEndTagForbidden()
  {
    return false;
  }
  



  public String getClassName()
  {
    if ((getWindow().getWebWindow() != null) && (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_PHRASE_COMMON_CLASS_NAME))) {
      return "HTMLElement";
    }
    return super.getClassName();
  }
}
