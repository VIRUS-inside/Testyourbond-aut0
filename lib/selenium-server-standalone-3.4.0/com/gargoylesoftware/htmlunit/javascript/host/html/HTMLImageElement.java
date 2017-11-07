package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.ElementFactory;
import com.gargoylesoftware.htmlunit.html.HTMLParser;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import org.apache.commons.lang3.StringUtils;
import org.xml.sax.helpers.AttributesImpl;
































@JsxClass(domClass=HtmlImage.class)
public class HTMLImageElement
  extends HTMLElement
{
  private static final Map<String, String> NORMALIZED_ALIGN_VALUES = new HashMap();
  static { NORMALIZED_ALIGN_VALUES.put("center", "center");
    NORMALIZED_ALIGN_VALUES.put("left", "left");
    NORMALIZED_ALIGN_VALUES.put("right", "right");
    NORMALIZED_ALIGN_VALUES.put("bottom", "bottom");
    NORMALIZED_ALIGN_VALUES.put("middle", "middle");
    NORMALIZED_ALIGN_VALUES.put("top", "top");
    NORMALIZED_ALIGN_VALUES.put("absbottom", "absBottom");
    NORMALIZED_ALIGN_VALUES.put("absmiddle", "absMiddle");
    NORMALIZED_ALIGN_VALUES.put("baseline", "baseline");
    NORMALIZED_ALIGN_VALUES.put("texttop", "textTop");
  }
  
  private boolean endTagForbidden_ = true;
  
  public HTMLImageElement() {}
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public void jsConstructor()
  {
    SgmlPage page = (SgmlPage)getWindow().getWebWindow().getEnclosedPage();
    DomElement fake = 
      HTMLParser.getFactory("img").createElement(page, "img", new AttributesImpl());
    setDomNode(fake);
  }
  



  public void setDomNode(DomNode domNode)
  {
    super.setDomNode(domNode);
    if ("image".equalsIgnoreCase(domNode.getLocalName())) {
      endTagForbidden_ = false;
    }
  }
  



  @JsxSetter
  public void setSrc(String src)
  {
    HtmlElement img = getDomNodeOrDie();
    img.setAttribute("src", src);
  }
  



  @JsxGetter
  public String getSrc()
  {
    HtmlImage img = (HtmlImage)getDomNodeOrDie();
    String src = img.getSrcAttribute();
    if ("".equals(src)) {
      return src;
    }
    try {
      HtmlPage page = (HtmlPage)img.getPage();
      return page.getFullyQualifiedUrl(src).toExternalForm();
    }
    catch (MalformedURLException e) {
      String msg = "Unable to create fully qualified URL for src attribute of image " + e.getMessage();
      throw Context.reportRuntimeError(msg);
    }
  }
  



  @JsxSetter
  public void setOnload(Object onloadHandler)
  {
    setEventHandlerProp("onload", onloadHandler);
    

    HtmlImage img = (HtmlImage)getDomNodeOrDie();
    img.doOnLoad();
  }
  



  @JsxGetter
  public Object getOnload()
  {
    return getEventHandlerProp("onload");
  }
  



  @JsxGetter
  public String getAlt()
  {
    String alt = getDomNodeOrDie().getAttribute("alt");
    return alt;
  }
  



  @JsxSetter
  public void setAlt(String alt)
  {
    getDomNodeOrDie().setAttribute("alt", alt);
  }
  



  @JsxGetter
  public String getBorder()
  {
    String border = getDomNodeOrDie().getAttribute("border");
    return border;
  }
  



  @JsxSetter
  public void setBorder(String border)
  {
    getDomNodeOrDie().setAttribute("border", border);
  }
  



  @JsxGetter
  public String getAlign()
  {
    boolean acceptArbitraryValues = getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_ALIGN_ACCEPTS_ARBITRARY_VALUES);
    
    String align = getDomNodeOrDie().getAttribute("align");
    if (acceptArbitraryValues) {
      return align;
    }
    
    String normalizedValue = (String)NORMALIZED_ALIGN_VALUES.get(align.toLowerCase(Locale.ROOT));
    if (normalizedValue != null) {
      return normalizedValue;
    }
    return "";
  }
  



  @JsxSetter
  public void setAlign(String align)
  {
    boolean acceptArbitraryValues = getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_ALIGN_ACCEPTS_ARBITRARY_VALUES);
    if (acceptArbitraryValues) {
      getDomNodeOrDie().setAttribute("align", align);
      return;
    }
    
    String normalizedValue = (String)NORMALIZED_ALIGN_VALUES.get(align.toLowerCase(Locale.ROOT));
    if (normalizedValue != null) {
      getDomNodeOrDie().setAttribute("align", normalizedValue);
      return;
    }
    
    throw Context.reportRuntimeError("Cannot set the align property to invalid value: '" + align + "'");
  }
  




  @JsxGetter
  public int getWidth()
  {
    HtmlImage img = (HtmlImage)getDomNodeOrDie();
    String widthAttrib = img.getWidthAttribute();
    
    if (DomElement.ATTRIBUTE_NOT_DEFINED != widthAttrib) {
      try {
        return Integer.parseInt(widthAttrib);
      }
      catch (NumberFormatException localNumberFormatException) {}
    }
    


    String src = img.getSrcAttribute();
    if (DomElement.ATTRIBUTE_NOT_DEFINED != src) {
      try {
        return img.getWidth();
      }
      catch (IOException e) {
        BrowserVersion browserVersion = getBrowserVersion();
        if (browserVersion.hasFeature(BrowserVersionFeatures.JS_IMAGE_WIDTH_HEIGHT_RETURNS_28x30_28x30)) {
          return 28;
        }
        if (browserVersion.hasFeature(BrowserVersionFeatures.JS_IMAGE_WIDTH_HEIGHT_RETURNS_18x20_0x0)) {
          if (StringUtils.isBlank(src)) {
            return 0;
          }
          return 20;
        }
        return 24;
      }
    }
    
    BrowserVersion browserVersion = getBrowserVersion();
    if (browserVersion.hasFeature(BrowserVersionFeatures.JS_IMAGE_WIDTH_HEIGHT_RETURNS_28x30_28x30)) {
      return 28;
    }
    if ((browserVersion.hasFeature(BrowserVersionFeatures.JS_IMAGE_WIDTH_HEIGHT_RETURNS_18x20_0x0)) || 
      (browserVersion.hasFeature(BrowserVersionFeatures.JS_IMAGE_WIDTH_HEIGHT_RETURNS_0x0_0x0))) {
      return 0;
    }
    return 24;
  }
  



  @JsxSetter
  public void setWidth(String width)
  {
    getDomNodeOrDie().setAttribute("width", width);
  }
  




  @JsxGetter
  public int getHeight()
  {
    HtmlImage img = (HtmlImage)getDomNodeOrDie();
    String height = img.getHeightAttribute();
    
    if (DomElement.ATTRIBUTE_NOT_DEFINED != height) {
      try {
        return Integer.parseInt(height);
      }
      catch (NumberFormatException localNumberFormatException) {}
    }
    


    String src = img.getSrcAttribute();
    if (DomElement.ATTRIBUTE_NOT_DEFINED != src) {
      try {
        return img.getHeight();
      }
      catch (IOException e) {
        BrowserVersion browserVersion = getBrowserVersion();
        if (browserVersion.hasFeature(BrowserVersionFeatures.JS_IMAGE_WIDTH_HEIGHT_RETURNS_28x30_28x30)) {
          return 30;
        }
        if (browserVersion.hasFeature(BrowserVersionFeatures.JS_IMAGE_WIDTH_HEIGHT_RETURNS_18x20_0x0)) {
          if (StringUtils.isBlank(src)) {
            return 0;
          }
          return 20;
        }
        return 24;
      }
    }
    
    BrowserVersion browserVersion = getBrowserVersion();
    if (browserVersion.hasFeature(BrowserVersionFeatures.JS_IMAGE_WIDTH_HEIGHT_RETURNS_28x30_28x30)) {
      return 30;
    }
    if ((browserVersion.hasFeature(BrowserVersionFeatures.JS_IMAGE_WIDTH_HEIGHT_RETURNS_18x20_0x0)) || 
      (browserVersion.hasFeature(BrowserVersionFeatures.JS_IMAGE_WIDTH_HEIGHT_RETURNS_0x0_0x0))) {
      return 0;
    }
    return 24;
  }
  



  @JsxSetter
  public void setHeight(String height)
  {
    getDomNodeOrDie().setAttribute("height", height);
  }
  



  protected boolean isEndTagForbidden()
  {
    return endTagForbidden_;
  }
  



  @JsxGetter
  public boolean isComplete()
  {
    return ((HtmlImage)getDomNodeOrDie()).isComplete();
  }
  



  @JsxGetter
  public int getNaturalWidth()
  {
    HtmlImage img = (HtmlImage)getDomNodeOrDie();
    try {
      return img.getWidth();
    }
    catch (IOException e) {}
    return 0;
  }
  




  @JsxGetter
  public int getNaturalHeight()
  {
    HtmlImage img = (HtmlImage)getDomNodeOrDie();
    try {
      return img.getHeight();
    }
    catch (IOException e) {}
    return 0;
  }
  




  @JsxGetter
  public String getName()
  {
    return getDomNodeOrDie().getAttribute("name");
  }
  



  @JsxSetter
  public void setName(String name)
  {
    getDomNodeOrDie().setAttribute("name", name);
  }
}
