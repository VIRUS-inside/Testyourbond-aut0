package com.gargoylesoftware.htmlunit.javascript.host.css;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.css.StyleElement;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.Element;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLCanvasElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLHtmlElement;
import com.steadystate.css.dom.CSSValueImpl;
import com.steadystate.css.parser.CSSOMParser;
import com.steadystate.css.parser.SACParserCSS3;
import java.awt.Color;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.css.sac.ErrorHandler;
import org.w3c.css.sac.InputSource;











































































































@JsxClass
public class CSSStyleDeclaration
  extends SimpleScriptable
{
  private static final Pattern TO_INT_PATTERN = Pattern.compile("(\\d+).*");
  
  private static final Pattern URL_PATTERN = Pattern.compile("url\\(\\s*[\"']?(.*?)[\"']?\\s*\\)");
  
  private static final Pattern POSITION_PATTERN = Pattern.compile("(\\d+\\s*(%|px|cm|mm|in|pt|pc|em|ex))\\s*(\\d+\\s*(%|px|cm|mm|in|pt|pc|em|ex)|top|bottom|center)");
  

  private static final Pattern POSITION_PATTERN2 = Pattern.compile("(left|right|center)\\s*(\\d+\\s*(%|px|cm|mm|in|pt|pc|em|ex)|top|bottom|center)");
  
  private static final Pattern POSITION_PATTERN3 = Pattern.compile("(top|bottom|center)\\s*(\\d+\\s*(%|px|cm|mm|in|pt|pc|em|ex)|left|right|center)");
  
  private static final Set<String> LENGTH_PROPERTIES_FFFF = new HashSet(Arrays.asList(new String[] {
    StyleAttributes.Definition.BORDER_TOP_WIDTH.getAttributeName(), 
    StyleAttributes.Definition.BORDER_LEFT_WIDTH.getAttributeName(), 
    StyleAttributes.Definition.BORDER_BOTTOM_WIDTH.getAttributeName(), 
    StyleAttributes.Definition.BORDER_RIGHT_WIDTH.getAttributeName(), 
    StyleAttributes.Definition.LETTER_SPACING.getAttributeName() }));
  
  private static final Set<String> LENGTH_PROPERTIES_TTFF = new HashSet(Arrays.asList(new String[] {
    StyleAttributes.Definition.HEIGHT.getAttributeName(), 
    StyleAttributes.Definition.WIDTH.getAttributeName(), 
    StyleAttributes.Definition.TOP.getAttributeName(), 
    StyleAttributes.Definition.LEFT.getAttributeName(), 
    StyleAttributes.Definition.BOTTOM.getAttributeName(), 
    StyleAttributes.Definition.RIGHT.getAttributeName(), 
    StyleAttributes.Definition.MARGIN_TOP.getAttributeName(), 
    StyleAttributes.Definition.MARGIN_LEFT.getAttributeName(), 
    StyleAttributes.Definition.MARGIN_BOTTOM.getAttributeName(), 
    StyleAttributes.Definition.MARGIN_RIGHT.getAttributeName(), 
    StyleAttributes.Definition.MIN_HEIGHT.getAttributeName(), 
    StyleAttributes.Definition.MIN_WIDTH.getAttributeName() }));
  

  private static final Set<String> LENGTH_PROPERTIES_FTFF = new HashSet(Arrays.asList(new String[] {
    StyleAttributes.Definition.FONT_SIZE.getAttributeName(), 
    StyleAttributes.Definition.TEXT_INDENT.getAttributeName(), 
    StyleAttributes.Definition.PADDING_TOP.getAttributeName(), 
    StyleAttributes.Definition.PADDING_LEFT.getAttributeName(), 
    StyleAttributes.Definition.PADDING_BOTTOM.getAttributeName(), 
    StyleAttributes.Definition.PADDING_RIGHT.getAttributeName(), 
    StyleAttributes.Definition.MAX_HEIGHT.getAttributeName(), 
    StyleAttributes.Definition.MAX_WIDTH.getAttributeName() }));
  

  private static final Log LOG = LogFactory.getLog(CSSStyleDeclaration.class);
  private static final Map<String, String> CSSColors_ = new HashMap();
  

  private static final Map<String, String> CamelizeCache_ = Collections.synchronizedMap(new HashMap());
  

  private static final MessageFormat URL_FORMAT = new MessageFormat("url({0})");
  
  private Element jsElement_;
  
  private org.w3c.dom.css.CSSStyleDeclaration styleDeclaration_;
  

  static
  {
    CSSColors_.put("aqua", "rgb(0, 255, 255)");
    CSSColors_.put("black", "rgb(0, 0, 0)");
    CSSColors_.put("blue", "rgb(0, 0, 255)");
    CSSColors_.put("fuchsia", "rgb(255, 0, 255)");
    CSSColors_.put("gray", "rgb(128, 128, 128)");
    CSSColors_.put("green", "rgb(0, 128, 0)");
    CSSColors_.put("lime", "rgb(0, 255, 0)");
    CSSColors_.put("maroon", "rgb(128, 0, 0)");
    CSSColors_.put("navy", "rgb(0, 0, 128)");
    CSSColors_.put("olive", "rgb(128, 128, 0)");
    CSSColors_.put("purple", "rgb(128, 0, 128)");
    CSSColors_.put("red", "rgb(255, 0, 0)");
    CSSColors_.put("silver", "rgb(192, 192, 192)");
    CSSColors_.put("teal", "rgb(0, 128, 128)");
    CSSColors_.put("white", "rgb(255, 255, 255)");
    CSSColors_.put("yellow", "rgb(255, 255, 0)");
  }
  




  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public CSSStyleDeclaration() {}
  



  public CSSStyleDeclaration(Element element)
  {
    setParentScope(element.getParentScope());
    setPrototype(getPrototype(getClass()));
    initialize(element);
  }
  




  CSSStyleDeclaration(Scriptable parentScope, org.w3c.dom.css.CSSStyleDeclaration styleDeclaration)
  {
    setParentScope(parentScope);
    setPrototype(getPrototype(getClass()));
    styleDeclaration_ = styleDeclaration;
  }
  




  void initialize(Element element)
  {
    WebAssert.notNull("htmlElement", element);
    jsElement_ = element;
    setDomNode(element.getDomNodeOrNull(), false);
    

    if ((getBrowserVersion().hasFeature(BrowserVersionFeatures.CSS_SUPPORTS_BEHAVIOR_PROPERTY)) && 
      ((element instanceof HTMLElement))) {
      HTMLElement htmlElement = (HTMLElement)element;
      String behavior = getStyleAttribute(StyleAttributes.Definition.BEHAVIOR);
      if (org.apache.commons.lang3.StringUtils.isNotBlank(behavior)) {
        try {
          Object[] url = URL_FORMAT.parse(behavior);
          if (url.length > 0) {
            htmlElement.addBehavior((String)url[0]);
          }
        }
        catch (ParseException e) {
          LOG.warn("Invalid behavior: '" + behavior + "'.");
        }
      }
    }
  }
  





  protected Object getWithPreemption(String name)
  {
    if ((getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_STYLE_UNSUPPORTED_PROPERTY_GETTER)) && (jsElement_ != null)) {
      StyleElement element = getStyleElement(name);
      if ((element != null) && (element.getValue() != null)) {
        return element.getValue();
      }
    }
    
    return NOT_FOUND;
  }
  



  protected Element getElement()
  {
    return jsElement_;
  }
  





  protected String getStylePriority(String name)
  {
    if (styleDeclaration_ != null) {
      return styleDeclaration_.getPropertyPriority(name);
    }
    StyleElement element = getStyleElement(name);
    if ((element != null) && (element.getValue() != null)) {
      return element.getPriority();
    }
    return "";
  }
  





  protected StyleElement getStyleElement(String name)
  {
    if (jsElement_ == null) {
      return null;
    }
    return jsElement_.getDomNodeOrDie().getStyleElement(name);
  }
  






  private StyleElement getStyleElementCaseInSensitive(String name)
  {
    if (jsElement_ == null) {
      return null;
    }
    return jsElement_.getDomNodeOrDie().getStyleElementCaseInSensitive(name);
  }
  






  private String getStyleAttribute(StyleAttributes.Definition name1, StyleAttributes.Definition name2)
  {
    String value;
    





    String value;
    




    if (styleDeclaration_ != null) {
      String value1 = styleDeclaration_.getPropertyValue(name1.getAttributeName());
      String value2 = styleDeclaration_.getPropertyValue(name2.getAttributeName());
      
      if (("".equals(value1)) && ("".equals(value2))) {
        return "";
      }
      if ((!"".equals(value1)) && ("".equals(value2))) {
        return value1;
      }
      value = value2;
    }
    else {
      StyleElement element1 = getStyleElement(name1.getAttributeName());
      StyleElement element2 = getStyleElement(name2.getAttributeName());
      
      if (element2 == null) {
        if (element1 == null) {
          return "";
        }
        return element1.getValue(); }
      String value;
      if (element1 == null) {
        value = element2.getValue();
      } else {
        if (element1.getIndex() > element2.getIndex()) {
          return element1.getValue();
        }
        
        value = element2.getValue();
      }
    }
    
    String[] values = org.apache.commons.lang3.StringUtils.split(value);
    if (name1.name().contains("TOP")) {
      return values[0];
    }
    if (name1.name().contains("RIGHT")) {
      if (values.length > 1) {
        return values[1];
      }
      return values[0];
    }
    if (name1.name().contains("BOTTOM")) {
      if (values.length > 2) {
        return values[2];
      }
      return values[0];
    }
    if (name1.name().contains("LEFT")) {
      if (values.length > 3) {
        return values[3];
      }
      if (values.length > 1) {
        return values[1];
      }
      
      return values[0];
    }
    

    throw new IllegalStateException("Unsupported definitino: " + name1);
  }
  





  protected void setStyleAttribute(String name, String newValue)
  {
    setStyleAttribute(name, newValue, "");
  }
  





  protected void setStyleAttribute(String name, String newValue, String important)
  {
    if ((newValue == null) || ("null".equals(newValue))) {
      newValue = "";
    }
    if (styleDeclaration_ != null) {
      styleDeclaration_.setProperty(name, newValue, important);
      return;
    }
    
    jsElement_.getDomNodeOrDie().replaceStyleAttribute(name, newValue, important);
  }
  



  private String removeStyleAttribute(String name)
  {
    if (styleDeclaration_ != null) {
      return styleDeclaration_.removeProperty(name);
    }
    
    return jsElement_.getDomNodeOrDie().removeStyleAttribute(name);
  }
  





  private Map<String, StyleElement> getStyleMap()
  {
    if (jsElement_ == null) {
      return Collections.emptyMap();
    }
    return jsElement_.getDomNodeOrDie().getStyleMap();
  }
  






  protected static final String camelize(String string)
  {
    if (string == null) {
      return null;
    }
    
    String result = (String)CamelizeCache_.get(string);
    if (result != null) {
      return result;
    }
    

    int pos = string.indexOf('-');
    if ((pos == -1) || (pos == string.length() - 1))
    {
      CamelizeCache_.put(string, string);
      return string;
    }
    
    StringBuilder builder = new StringBuilder(string);
    builder.deleteCharAt(pos);
    builder.setCharAt(pos, Character.toUpperCase(builder.charAt(pos)));
    
    int i = pos + 1;
    while (i < builder.length() - 1) {
      if (builder.charAt(i) == '-') {
        builder.deleteCharAt(i);
        builder.setCharAt(i, Character.toUpperCase(builder.charAt(i)));
      }
      i++;
    }
    result = builder.toString();
    CamelizeCache_.put(string, result);
    
    return result;
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public String getAccelerator()
  {
    return (String)org.apache.commons.lang3.StringUtils.defaultIfEmpty(getStyleAttribute(StyleAttributes.Definition.ACCELERATOR), "false");
  }
  



  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void setAccelerator(String accelerator)
  {
    setStyleAttribute(StyleAttributes.Definition.ACCELERATOR.getAttributeName(), accelerator);
  }
  



  @JsxGetter
  public String getBackgroundAttachment()
  {
    String value = getStyleAttribute(StyleAttributes.Definition.BACKGROUND_ATTACHMENT, false);
    if (org.apache.commons.lang3.StringUtils.isBlank(value)) {
      String bg = getStyleAttribute(StyleAttributes.Definition.BACKGROUND);
      if (org.apache.commons.lang3.StringUtils.isNotBlank(bg)) {
        value = findAttachment(bg);
        if (value == null) {
          if ((getBrowserVersion().hasFeature(BrowserVersionFeatures.CSS_BACKGROUND_INITIAL)) && 
            (getClass() == CSSStyleDeclaration.class)) {
            return "initial";
          }
          return "scroll";
        }
        return value;
      }
      return "";
    }
    
    return value;
  }
  



  @JsxSetter
  public void setBackgroundAttachment(String backgroundAttachment)
  {
    setStyleAttribute(StyleAttributes.Definition.BACKGROUND_ATTACHMENT.getAttributeName(), backgroundAttachment);
  }
  



  @JsxGetter
  public String getBackgroundColor()
  {
    String value = getStyleAttribute(StyleAttributes.Definition.BACKGROUND_COLOR, false);
    if (org.apache.commons.lang3.StringUtils.isBlank(value)) {
      String bg = getStyleAttribute(StyleAttributes.Definition.BACKGROUND, false);
      if (org.apache.commons.lang3.StringUtils.isBlank(bg)) {
        return "";
      }
      value = findColor(bg);
      if (value == null) {
        if (getBrowserVersion().hasFeature(BrowserVersionFeatures.CSS_BACKGROUND_INITIAL)) {
          if (getClass() == CSSStyleDeclaration.class) {
            return "initial";
          }
          return "rgba(0, 0, 0, 0)";
        }
        return "transparent";
      }
      return value;
    }
    if (org.apache.commons.lang3.StringUtils.isBlank(value)) {
      return "";
    }
    return value;
  }
  



  @JsxSetter
  public void setBackgroundColor(String backgroundColor)
  {
    setStyleAttribute(StyleAttributes.Definition.BACKGROUND_COLOR.getAttributeName(), backgroundColor);
  }
  



  @JsxGetter
  public String getBackgroundImage()
  {
    String value = getStyleAttribute(StyleAttributes.Definition.BACKGROUND_IMAGE, false);
    if (org.apache.commons.lang3.StringUtils.isBlank(value)) {
      String bg = getStyleAttribute(StyleAttributes.Definition.BACKGROUND, false);
      if (org.apache.commons.lang3.StringUtils.isNotBlank(bg)) {
        value = findImageUrl(bg);
        boolean isComputed = getClass() != CSSStyleDeclaration.class;
        boolean backgroundInitial = getBrowserVersion().hasFeature(BrowserVersionFeatures.CSS_BACKGROUND_INITIAL);
        if (value == null) {
          return (backgroundInitial) && (!isComputed) ? "initial" : "none";
        }
        if (isComputed) {
          try {
            value = value.substring(5, value.length() - 2);
            return "url(\"" + ((HtmlElement)jsElement_.getDomNodeOrDie()).getHtmlPageOrNull()
              .getFullyQualifiedUrl(value) + "\")";
          }
          catch (Exception localException) {}
        }
        

        return value;
      }
      return "";
    }
    
    return value;
  }
  



  @JsxSetter
  public void setBackgroundImage(String backgroundImage)
  {
    setStyleAttribute(StyleAttributes.Definition.BACKGROUND_IMAGE.getAttributeName(), backgroundImage);
  }
  



  @JsxGetter
  public String getBackgroundPosition()
  {
    String value = getStyleAttribute(StyleAttributes.Definition.BACKGROUND_POSITION, false);
    if (value == null) {
      return null;
    }
    if (org.apache.commons.lang3.StringUtils.isBlank(value)) {
      String bg = getStyleAttribute(StyleAttributes.Definition.BACKGROUND, false);
      if (bg == null) {
        return null;
      }
      if (org.apache.commons.lang3.StringUtils.isNotBlank(bg)) {
        value = findPosition(bg);
        boolean isInitial = getBrowserVersion().hasFeature(BrowserVersionFeatures.CSS_BACKGROUND_INITIAL);
        boolean isComputed = getClass() != CSSStyleDeclaration.class;
        if (value == null) {
          if (isInitial) {
            return isComputed ? "" : "initial";
          }
          return "0% 0%";
        }
        if (getBrowserVersion().hasFeature(BrowserVersionFeatures.CSS_ZINDEX_TYPE_INTEGER)) {
          String[] values = value.split(" ");
          if ("center".equals(values[0])) {
            values[0] = "";
          }
          if ("center".equals(values[1])) {
            values[1] = "";
          }
          if ((!isComputed) || (value.contains("top"))) {
            return (values[0] + ' ' + values[1]).trim();
          }
        }
        if (isComputed) {
          String[] values = value.split(" ");
          String str1; switch ((str1 = values[0]).hashCode()) {case -1364013995:  if (str1.equals("center")) {} break; case 3317767:  if (str1.equals("left")) break; break; case 108511772:  if (!str1.equals("right")) {
              break label341;
              values[0] = "0%";
              
              break label341;
              
              values[0] = "50%";
            }
            else
            {
              values[0] = "100%";
            }
            break; }
          label341:
          String str2;
          switch ((str2 = values[1]).hashCode()) {case -1383228885:  if (str2.equals("bottom")) {} break; case -1364013995:  if (str2.equals("center")) break;  case 115029:  if ((goto 453) && (str2.equals("top")))
            {
              values[1] = "0%";
              
              break label453;
              
              values[1] = "50%";
              
              break label453;
              
              values[1] = "100%";
            }
            break;
          }
          label453:
          value = values[0] + ' ' + values[1];
        }
        return value;
      }
      return "";
    }
    
    return value;
  }
  



  @JsxSetter
  public void setBackgroundPosition(String backgroundPosition)
  {
    setStyleAttribute(StyleAttributes.Definition.BACKGROUND_POSITION.getAttributeName(), backgroundPosition);
  }
  



  @JsxGetter
  public String getBackgroundRepeat()
  {
    String value = getStyleAttribute(StyleAttributes.Definition.BACKGROUND_REPEAT, false);
    if (org.apache.commons.lang3.StringUtils.isBlank(value)) {
      String bg = getStyleAttribute(StyleAttributes.Definition.BACKGROUND, false);
      if (org.apache.commons.lang3.StringUtils.isNotBlank(bg)) {
        value = findRepeat(bg);
        if (value == null) {
          if ((getBrowserVersion().hasFeature(BrowserVersionFeatures.CSS_BACKGROUND_INITIAL)) && 
            (getClass() == CSSStyleDeclaration.class)) {
            return "initial";
          }
          return "repeat";
        }
        return value;
      }
      return "";
    }
    
    return value;
  }
  



  @JsxSetter
  public void setBackgroundRepeat(String backgroundRepeat)
  {
    setStyleAttribute(StyleAttributes.Definition.BACKGROUND_REPEAT.getAttributeName(), backgroundRepeat);
  }
  



  @JsxGetter
  public String getBorderBottomColor()
  {
    String value = getStyleAttribute(StyleAttributes.Definition.BORDER_BOTTOM_COLOR, false);
    if (value.isEmpty()) {
      value = findColor(getStyleAttribute(StyleAttributes.Definition.BORDER_BOTTOM, false));
      if (value == null) {
        value = findColor(getStyleAttribute(StyleAttributes.Definition.BORDER, false));
      }
      if (value == null) {
        value = "";
      }
    }
    return value;
  }
  



  @JsxSetter
  public void setBorderBottomColor(String borderBottomColor)
  {
    setStyleAttribute(StyleAttributes.Definition.BORDER_BOTTOM_COLOR.getAttributeName(), borderBottomColor);
  }
  



  @JsxGetter
  public String getBorderBottomStyle()
  {
    String value = getStyleAttribute(StyleAttributes.Definition.BORDER_BOTTOM_STYLE, false);
    if (value.isEmpty()) {
      value = findBorderStyle(getStyleAttribute(StyleAttributes.Definition.BORDER_BOTTOM, false));
      if (value == null) {
        value = findBorderStyle(getStyleAttribute(StyleAttributes.Definition.BORDER, false));
      }
      if (value == null) {
        value = "";
      }
    }
    return value;
  }
  



  @JsxSetter
  public void setBorderBottomStyle(String borderBottomStyle)
  {
    setStyleAttribute(StyleAttributes.Definition.BORDER_BOTTOM_STYLE.getAttributeName(), borderBottomStyle);
  }
  



  @JsxGetter
  public String getBorderBottomWidth()
  {
    return getBorderWidth(StyleAttributes.Definition.BORDER_BOTTOM_WIDTH, StyleAttributes.Definition.BORDER_BOTTOM);
  }
  



  @JsxSetter
  public void setBorderBottomWidth(Object borderBottomWidth)
  {
    setStyleLengthAttribute(StyleAttributes.Definition.BORDER_BOTTOM_WIDTH.getAttributeName(), borderBottomWidth, "", 
      false, false, false, false, false);
  }
  



  @JsxGetter
  public String getBorderLeftColor()
  {
    String value = getStyleAttribute(StyleAttributes.Definition.BORDER_LEFT_COLOR, false);
    if (value.isEmpty()) {
      value = findColor(getStyleAttribute(StyleAttributes.Definition.BORDER_LEFT, false));
      if (value == null) {
        value = findColor(getStyleAttribute(StyleAttributes.Definition.BORDER, false));
      }
      if (value == null) {
        value = "";
      }
    }
    return value;
  }
  



  @JsxSetter
  public void setBorderLeftColor(String borderLeftColor)
  {
    setStyleAttribute(StyleAttributes.Definition.BORDER_LEFT_COLOR.getAttributeName(), borderLeftColor);
  }
  



  @JsxGetter
  public String getBorderLeftStyle()
  {
    String value = getStyleAttribute(StyleAttributes.Definition.BORDER_LEFT_STYLE, false);
    if (value.isEmpty()) {
      value = findBorderStyle(getStyleAttribute(StyleAttributes.Definition.BORDER_LEFT, false));
      if (value == null) {
        value = findBorderStyle(getStyleAttribute(StyleAttributes.Definition.BORDER, false));
      }
      if (value == null) {
        value = "";
      }
    }
    return value;
  }
  



  @JsxSetter
  public void setBorderLeftStyle(String borderLeftStyle)
  {
    setStyleAttribute(StyleAttributes.Definition.BORDER_LEFT_STYLE.getAttributeName(), borderLeftStyle);
  }
  



  @JsxGetter
  public String getBorderLeftWidth()
  {
    return getBorderWidth(StyleAttributes.Definition.BORDER_LEFT_WIDTH, StyleAttributes.Definition.BORDER_LEFT);
  }
  





  private String getBorderWidth(StyleAttributes.Definition borderSideWidth, StyleAttributes.Definition borderSide)
  {
    String value = getStyleAttribute(borderSideWidth, false);
    if (value.isEmpty()) {
      value = findBorderWidth(getStyleAttribute(borderSide, false));
      if (value == null) {
        String borderWidth = getStyleAttribute(StyleAttributes.Definition.BORDER_WIDTH, false);
        if (!org.apache.commons.lang3.StringUtils.isEmpty(borderWidth)) {
          String[] values = org.apache.commons.lang3.StringUtils.split(borderWidth);
          int index = values.length;
          if (borderSideWidth.name().contains("TOP")) {
            index = 0;
          }
          else if (borderSideWidth.name().contains("RIGHT")) {
            index = 1;
          }
          else if (borderSideWidth.name().contains("BOTTOM")) {
            index = 2;
          }
          else if (borderSideWidth.name().contains("LEFT")) {
            index = 3;
          }
          if (index < values.length) {
            value = values[index];
          }
        }
      }
      if (value == null) {
        value = findBorderWidth(getStyleAttribute(StyleAttributes.Definition.BORDER, false));
      }
      if (value == null) {
        value = "";
      }
    }
    return value;
  }
  



  @JsxSetter
  public void setBorderLeftWidth(Object borderLeftWidth)
  {
    setStyleLengthAttribute(StyleAttributes.Definition.BORDER_LEFT_WIDTH.getAttributeName(), borderLeftWidth, "", 
      false, false, false, false, false);
  }
  



  @JsxGetter
  public String getBorderRightColor()
  {
    String value = getStyleAttribute(StyleAttributes.Definition.BORDER_RIGHT_COLOR, false);
    if (value.isEmpty()) {
      value = findColor(getStyleAttribute(StyleAttributes.Definition.BORDER_RIGHT, false));
      if (value == null) {
        value = findColor(getStyleAttribute(StyleAttributes.Definition.BORDER, false));
      }
      if (value == null) {
        value = "";
      }
    }
    return value;
  }
  



  @JsxSetter
  public void setBorderRightColor(String borderRightColor)
  {
    setStyleAttribute(StyleAttributes.Definition.BORDER_RIGHT_COLOR.getAttributeName(), borderRightColor);
  }
  



  @JsxGetter
  public String getBorderRightStyle()
  {
    String value = getStyleAttribute(StyleAttributes.Definition.BORDER_RIGHT_STYLE, false);
    if (value.isEmpty()) {
      value = findBorderStyle(getStyleAttribute(StyleAttributes.Definition.BORDER_RIGHT, false));
      if (value == null) {
        value = findBorderStyle(getStyleAttribute(StyleAttributes.Definition.BORDER, false));
      }
      if (value == null) {
        value = "";
      }
    }
    return value;
  }
  



  @JsxSetter
  public void setBorderRightStyle(String borderRightStyle)
  {
    setStyleAttribute(StyleAttributes.Definition.BORDER_RIGHT_STYLE.getAttributeName(), borderRightStyle);
  }
  



  @JsxGetter
  public String getBorderRightWidth()
  {
    return getBorderWidth(StyleAttributes.Definition.BORDER_RIGHT_WIDTH, StyleAttributes.Definition.BORDER_RIGHT);
  }
  



  @JsxSetter
  public void setBorderRightWidth(Object borderRightWidth)
  {
    setStyleLengthAttribute(StyleAttributes.Definition.BORDER_RIGHT_WIDTH.getAttributeName(), borderRightWidth, "", 
      false, false, false, false, false);
  }
  



  @JsxSetter
  public void setBorderTop(String borderTop)
  {
    setStyleAttribute(StyleAttributes.Definition.BORDER_TOP.getAttributeName(), borderTop);
  }
  



  @JsxGetter
  public String getBorderTopColor()
  {
    String value = getStyleAttribute(StyleAttributes.Definition.BORDER_TOP_COLOR, false);
    if (value.isEmpty()) {
      value = findColor(getStyleAttribute(StyleAttributes.Definition.BORDER_TOP, false));
      if (value == null) {
        value = findColor(getStyleAttribute(StyleAttributes.Definition.BORDER, false));
      }
      if (value == null) {
        value = "";
      }
    }
    return value;
  }
  



  @JsxSetter
  public void setBorderTopColor(String borderTopColor)
  {
    setStyleAttribute(StyleAttributes.Definition.BORDER_TOP_COLOR.getAttributeName(), borderTopColor);
  }
  



  @JsxGetter
  public String getBorderTopStyle()
  {
    String value = getStyleAttribute(StyleAttributes.Definition.BORDER_TOP_STYLE, false);
    if (value.isEmpty()) {
      value = findBorderStyle(getStyleAttribute(StyleAttributes.Definition.BORDER_TOP, false));
      if (value == null) {
        value = findBorderStyle(getStyleAttribute(StyleAttributes.Definition.BORDER, false));
      }
      if (value == null) {
        value = "";
      }
    }
    return value;
  }
  



  @JsxSetter
  public void setBorderTopStyle(String borderTopStyle)
  {
    setStyleAttribute(StyleAttributes.Definition.BORDER_TOP_STYLE.getAttributeName(), borderTopStyle);
  }
  



  @JsxGetter
  public String getBorderTopWidth()
  {
    return getBorderWidth(StyleAttributes.Definition.BORDER_TOP_WIDTH, StyleAttributes.Definition.BORDER_TOP);
  }
  



  @JsxSetter
  public void setBorderTopWidth(Object borderTopWidth)
  {
    setStyleLengthAttribute(StyleAttributes.Definition.BORDER_TOP_WIDTH.getAttributeName(), borderTopWidth, "", 
      false, false, false, false, false);
  }
  



  @JsxGetter
  public String getBottom()
  {
    return getStyleAttribute(StyleAttributes.Definition.BOTTOM);
  }
  



  @JsxSetter
  public void setBottom(Object bottom)
  {
    setStyleLengthAttribute(StyleAttributes.Definition.BOTTOM.getAttributeName(), bottom, "", true, true, false, false, false);
  }
  



  @JsxGetter
  public String getColor()
  {
    return getStyleAttribute(StyleAttributes.Definition.COLOR);
  }
  



  @JsxSetter
  public void setColor(String color)
  {
    setStyleAttribute(StyleAttributes.Definition.COLOR.getAttributeName(), color);
  }
  



  @JsxGetter
  public String getCssFloat()
  {
    return getStyleAttribute(StyleAttributes.Definition.FLOAT);
  }
  



  @JsxSetter
  public void setCssFloat(String value)
  {
    setStyleAttribute(StyleAttributes.Definition.FLOAT.getAttributeName(), value);
  }
  



  @JsxGetter
  public String getCssText()
  {
    return jsElement_.getDomNodeOrDie().getAttribute("style");
  }
  



  @JsxSetter
  public void setCssText(String value)
  {
    jsElement_.getDomNodeOrDie().setAttribute("style", value);
  }
  



  @JsxGetter
  public String getDisplay()
  {
    return getStyleAttribute(StyleAttributes.Definition.DISPLAY);
  }
  



  @JsxSetter
  public void setDisplay(String display)
  {
    setStyleAttribute(StyleAttributes.Definition.DISPLAY.getAttributeName(), display);
  }
  



  @JsxGetter
  public String getFontSize()
  {
    return getStyleAttribute(StyleAttributes.Definition.FONT_SIZE);
  }
  



  @JsxSetter
  public void setFontSize(Object fontSize)
  {
    setStyleLengthAttribute(StyleAttributes.Definition.FONT_SIZE.getAttributeName(), fontSize, "", false, true, false, false, false);
    updateFont(getFont(), false);
  }
  



  @JsxGetter
  public String getLineHeight()
  {
    return getStyleAttribute(StyleAttributes.Definition.LINE_HEIGHT);
  }
  



  @JsxSetter
  public void setLineHeight(String lineHeight)
  {
    setStyleAttribute(StyleAttributes.Definition.LINE_HEIGHT.getAttributeName(), lineHeight);
    updateFont(getFont(), false);
  }
  



  @JsxGetter
  public String getFontFamily()
  {
    return getStyleAttribute(StyleAttributes.Definition.FONT_FAMILY);
  }
  



  @JsxSetter
  public void setFontFamily(String fontFamily)
  {
    setStyleAttribute(StyleAttributes.Definition.FONT_FAMILY.getAttributeName(), fontFamily);
    updateFont(getFont(), false);
  }
  
  private void updateFont(String font, boolean force) {
    BrowserVersion browserVersion = getBrowserVersion();
    String[] details = ComputedFont.getDetails(font, !browserVersion.hasFeature(BrowserVersionFeatures.CSS_SET_NULL_THROWS));
    if ((details != null) || (force)) {
      StringBuilder newFont = new StringBuilder();
      newFont.append(getFontSize());
      String lineHeight = getLineHeight();
      String defaultLineHeight = StyleAttributes.Definition.LINE_HEIGHT.getDefaultComputedValue(browserVersion);
      if (lineHeight.isEmpty()) {
        lineHeight = defaultLineHeight;
      }
      
      if ((browserVersion.hasFeature(BrowserVersionFeatures.CSS_ZINDEX_TYPE_INTEGER)) || (!lineHeight.equals(defaultLineHeight))) {
        newFont.append('/');
        if (!lineHeight.equals(defaultLineHeight)) {
          newFont.append(lineHeight);
        }
        else {
          newFont.append(StyleAttributes.Definition.LINE_HEIGHT.getDefaultComputedValue(browserVersion));
        }
      }
      
      newFont.append(' ').append(getFontFamily());
      setStyleAttribute(StyleAttributes.Definition.FONT.getAttributeName(), newFont.toString());
    }
  }
  



  @JsxGetter
  public String getFont()
  {
    return getStyleAttribute(StyleAttributes.Definition.FONT);
  }
  



  @JsxSetter
  public void setFont(String font)
  {
    String[] details = ComputedFont.getDetails(font, !getBrowserVersion().hasFeature(BrowserVersionFeatures.CSS_SET_NULL_THROWS));
    if (details != null) {
      setStyleAttribute(StyleAttributes.Definition.FONT_FAMILY.getAttributeName(), details[5]);
      String fontSize = details[3];
      if (details[4] != null) {
        setStyleAttribute(StyleAttributes.Definition.LINE_HEIGHT.getAttributeName(), details[4]);
      }
      setStyleAttribute(StyleAttributes.Definition.FONT_SIZE.getAttributeName(), fontSize);
      updateFont(font, true);
    }
  }
  



  @JsxGetter
  public String getHeight()
  {
    return getStyleAttribute(StyleAttributes.Definition.HEIGHT);
  }
  



  @JsxSetter
  public void setHeight(Object height)
  {
    setStyleLengthAttribute(StyleAttributes.Definition.HEIGHT.getAttributeName(), height, "", true, true, false, false, false);
  }
  



  @JsxGetter
  public String getLeft()
  {
    return getStyleAttribute(StyleAttributes.Definition.LEFT);
  }
  



  @JsxSetter
  public void setLeft(Object left)
  {
    setStyleLengthAttribute(StyleAttributes.Definition.LEFT.getAttributeName(), left, "", true, true, false, false, false);
  }
  



  @JsxGetter
  public int getLength()
  {
    return getStyleMap().size();
  }
  



  @JsxGetter
  public String getLetterSpacing()
  {
    return getStyleAttribute(StyleAttributes.Definition.LETTER_SPACING);
  }
  



  @JsxSetter
  public void setLetterSpacing(Object letterSpacing)
  {
    setStyleLengthAttribute(StyleAttributes.Definition.LETTER_SPACING.getAttributeName(), letterSpacing, "", 
      false, false, false, false, false);
  }
  



  @JsxGetter
  public String getMargin()
  {
    return getStyleAttribute(StyleAttributes.Definition.MARGIN);
  }
  



  @JsxSetter
  public void setMargin(String margin)
  {
    setStyleAttribute(StyleAttributes.Definition.MARGIN.getAttributeName(), margin);
  }
  



  @JsxGetter
  public String getMarginBottom()
  {
    return getStyleAttribute(StyleAttributes.Definition.MARGIN_BOTTOM, StyleAttributes.Definition.MARGIN);
  }
  



  @JsxSetter
  public void setMarginBottom(Object marginBottom)
  {
    setStyleLengthAttribute(StyleAttributes.Definition.MARGIN_BOTTOM.getAttributeName(), marginBottom, "", true, true, false, false, false);
  }
  



  @JsxGetter
  public String getMarginLeft()
  {
    return getStyleAttribute(StyleAttributes.Definition.MARGIN_LEFT, StyleAttributes.Definition.MARGIN);
  }
  



  @JsxSetter
  public void setMarginLeft(Object marginLeft)
  {
    setStyleLengthAttribute(StyleAttributes.Definition.MARGIN_LEFT.getAttributeName(), marginLeft, "", true, true, false, false, false);
  }
  



  @JsxGetter
  public String getMarginRight()
  {
    return getStyleAttribute(StyleAttributes.Definition.MARGIN_RIGHT, StyleAttributes.Definition.MARGIN);
  }
  



  @JsxSetter
  public void setMarginRight(Object marginRight)
  {
    setStyleLengthAttribute(StyleAttributes.Definition.MARGIN_RIGHT.getAttributeName(), marginRight, "", true, true, false, false, false);
  }
  



  @JsxGetter
  public String getMarginTop()
  {
    return getStyleAttribute(StyleAttributes.Definition.MARGIN_TOP, StyleAttributes.Definition.MARGIN);
  }
  



  @JsxSetter
  public void setMarginTop(Object marginTop)
  {
    setStyleLengthAttribute(StyleAttributes.Definition.MARGIN_TOP.getAttributeName(), marginTop, "", true, true, false, false, false);
  }
  



  @JsxGetter
  public String getMaxHeight()
  {
    return getStyleAttribute(StyleAttributes.Definition.MAX_HEIGHT);
  }
  



  @JsxSetter
  public void setMaxHeight(Object maxHeight)
  {
    setStyleLengthAttribute(StyleAttributes.Definition.MAX_HEIGHT.getAttributeName(), maxHeight, "", false, true, false, false, false);
  }
  



  @JsxGetter
  public String getMaxWidth()
  {
    return getStyleAttribute(StyleAttributes.Definition.MAX_WIDTH);
  }
  



  @JsxSetter
  public void setMaxWidth(Object maxWidth)
  {
    setStyleLengthAttribute(StyleAttributes.Definition.MAX_WIDTH.getAttributeName(), maxWidth, "", false, true, false, false, false);
  }
  



  @JsxGetter
  public String getMinHeight()
  {
    return getStyleAttribute(StyleAttributes.Definition.MIN_HEIGHT);
  }
  



  @JsxSetter
  public void setMinHeight(Object minHeight)
  {
    setStyleLengthAttribute(StyleAttributes.Definition.MIN_HEIGHT.getAttributeName(), minHeight, "", true, true, false, false, false);
  }
  



  @JsxGetter
  public String getMinWidth()
  {
    return getStyleAttribute(StyleAttributes.Definition.MIN_WIDTH);
  }
  



  @JsxSetter
  public void setMinWidth(Object minWidth)
  {
    setStyleLengthAttribute(StyleAttributes.Definition.MIN_WIDTH.getAttributeName(), minWidth, "", true, true, false, false, false);
  }
  



  public Object get(String name, Scriptable start)
  {
    if (this != start) {
      return super.get(name, start);
    }
    
    Scriptable prototype = getPrototype();
    while (prototype != null) {
      Object value = prototype.get(name, start);
      if (value != Scriptable.NOT_FOUND) {
        return value;
      }
      prototype = prototype.getPrototype();
    }
    
    StyleAttributes.Definition style = StyleAttributes.getDefinition(name, getBrowserVersion());
    if (style != null) {
      return getStyleAttribute(style);
    }
    
    return super.get(name, start);
  }
  
  public Object get(int index, Scriptable start)
  {
    if (index < 0) {
      return Undefined.instance;
    }
    
    Map<String, StyleElement> style = getStyleMap();
    int size = style.size();
    if (index >= size) {
      if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_STYLE_WRONG_INDEX_RETURNS_UNDEFINED)) {
        return Undefined.instance;
      }
      return "";
    }
    return ((String[])style.keySet().toArray(new String[size]))[index];
  }
  




  public final String getStyleAttribute(StyleAttributes.Definition definition)
  {
    return getStyleAttribute(definition, true);
  }
  





  public String getStyleAttribute(StyleAttributes.Definition definition, boolean getDefaultValueIfEmpty)
  {
    return getStyleAttributeImpl(definition.getAttributeName());
  }
  
  private String getStyleAttributeImpl(String string) {
    if (styleDeclaration_ != null) {
      return styleDeclaration_.getPropertyValue(string);
    }
    StyleElement element = getStyleElement(string);
    if ((element != null) && (element.getValue() != null)) {
      String value = element.getValue();
      if ((!value.contains("url")) && 
        (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_STYLE_SET_PROPERTY_IMPORTANT_IGNORES_CASE))) {
        return value.toLowerCase(Locale.ROOT);
      }
      return value;
    }
    return "";
  }
  
  public void put(String name, Scriptable start, Object value)
  {
    if (this != start) {
      super.put(name, start, value);
      return;
    }
    
    Scriptable prototype = getPrototype();
    if ((prototype != null) && (!"constructor".equals(name)) && (prototype.get(name, start) != Scriptable.NOT_FOUND)) {
      prototype.put(name, start, value);
      return;
    }
    
    if (getDomNodeOrNull() != null) {
      StyleAttributes.Definition style = StyleAttributes.getDefinition(name, getBrowserVersion());
      if (style != null) {
        String stringValue = Context.toString(value);
        setStyleAttribute(style.getAttributeName(), stringValue);
        return;
      }
    }
    
    super.put(name, start, value);
  }
  
  public boolean has(String name, Scriptable start)
  {
    if (this != start) {
      return super.has(name, start);
    }
    
    StyleAttributes.Definition style = StyleAttributes.getDefinition(name, getBrowserVersion());
    if (style != null) {
      return true;
    }
    
    return super.has(name, start);
  }
  
  public Object[] getIds()
  {
    List<Object> ids = new ArrayList();
    for (StyleAttributes.Definition styleAttribute : StyleAttributes.getDefinitions(getBrowserVersion())) {
      ids.add(styleAttribute.getPropertyName());
    }
    Object[] normalIds = super.getIds();
    for (Object o : normalIds) {
      if (!ids.contains(o)) {
        ids.add(o);
      }
    }
    return ids.toArray();
  }
  



  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void setMsImeAlign(String msImeAlign)
  {
    setStyleAttribute(StyleAttributes.Definition.MS_IME_ALIGN.getAttributeName(), msImeAlign);
  }
  



  @JsxGetter
  public String getOpacity()
  {
    String opacity = getStyleAttribute(StyleAttributes.Definition.OPACITY, false);
    if ((opacity == null) || (opacity.isEmpty())) {
      return "";
    }
    
    String trimedOpacity = opacity.trim();
    try {
      double value = Double.parseDouble(trimedOpacity);
      if (value % 1.0D == 0.0D) {
        return Integer.toString((int)value);
      }
      return Double.toString(value);
    }
    catch (NumberFormatException localNumberFormatException) {}
    

    return "";
  }
  



  @JsxSetter
  public void setOpacity(Object opacity)
  {
    if (ScriptRuntime.NaNobj == opacity) {
      return;
    }
    
    double doubleValue;
    if ((opacity instanceof Number)) {
      doubleValue = ((Number)opacity).doubleValue();
    }
    else {
      String valueString = Context.toString(opacity);
      
      if (valueString.isEmpty()) {
        setStyleAttribute(StyleAttributes.Definition.OPACITY.getAttributeName(), valueString);
        return;
      }
      
      valueString = valueString.trim();
      try {
        doubleValue = Double.parseDouble(valueString);
      }
      catch (NumberFormatException e) {
        double doubleValue;
        return;
      }
    }
    double doubleValue;
    if ((Double.isNaN(doubleValue)) || (Double.isInfinite(doubleValue))) {
      return;
    }
    setStyleAttribute(StyleAttributes.Definition.OPACITY.getAttributeName(), Double.toString(doubleValue));
  }
  



  @JsxGetter
  public String getOutline()
  {
    return getStyleAttribute(StyleAttributes.Definition.OUTLINE);
  }
  



  @JsxSetter
  public void setOutline(String outline)
  {
    setStyleAttribute(StyleAttributes.Definition.OUTLINE.getAttributeName(), outline);
  }
  



  @JsxGetter
  public String getOutlineWidth()
  {
    return getStyleAttribute(StyleAttributes.Definition.OUTLINE_WIDTH);
  }
  



  @JsxSetter
  public void setOutlineWidth(Object outlineWidth)
  {
    boolean requiresUnit = !getBrowserVersion().hasFeature(BrowserVersionFeatures.CSS_OUTLINE_WIDTH_UNIT_NOT_REQUIRED);
    setStyleLengthAttribute(StyleAttributes.Definition.OUTLINE_WIDTH.getAttributeName(), outlineWidth, "", 
      false, false, true, requiresUnit, false);
  }
  



  @JsxGetter
  public String getPadding()
  {
    return getStyleAttribute(StyleAttributes.Definition.PADDING);
  }
  



  @JsxSetter
  public void setPadding(String padding)
  {
    setStyleAttribute(StyleAttributes.Definition.PADDING.getAttributeName(), padding);
  }
  



  @JsxGetter
  public String getPaddingBottom()
  {
    return getStyleAttribute(StyleAttributes.Definition.PADDING_BOTTOM, StyleAttributes.Definition.PADDING);
  }
  



  @JsxSetter
  public void setPaddingBottom(Object paddingBottom)
  {
    setStyleLengthAttribute(StyleAttributes.Definition.PADDING_BOTTOM.getAttributeName(), paddingBottom, "", false, true, false, false, false);
  }
  



  @JsxGetter
  public String getPaddingLeft()
  {
    return getStyleAttribute(StyleAttributes.Definition.PADDING_LEFT, StyleAttributes.Definition.PADDING);
  }
  



  @JsxSetter
  public void setPaddingLeft(Object paddingLeft)
  {
    setStyleLengthAttribute(StyleAttributes.Definition.PADDING_LEFT.getAttributeName(), paddingLeft, "", false, true, false, false, false);
  }
  



  @JsxGetter
  public String getPaddingRight()
  {
    return getStyleAttribute(StyleAttributes.Definition.PADDING_RIGHT, StyleAttributes.Definition.PADDING);
  }
  



  @JsxSetter
  public void setPaddingRight(Object paddingRight)
  {
    setStyleLengthAttribute(StyleAttributes.Definition.PADDING_RIGHT.getAttributeName(), paddingRight, "", false, true, false, false, false);
  }
  



  @JsxGetter
  public String getPaddingTop()
  {
    return getStyleAttribute(StyleAttributes.Definition.PADDING_TOP, StyleAttributes.Definition.PADDING);
  }
  



  @JsxSetter
  public void setPaddingTop(Object paddingTop)
  {
    setStyleLengthAttribute(StyleAttributes.Definition.PADDING_TOP.getAttributeName(), paddingTop, "", false, true, false, false, false);
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
  public String getPage()
  {
    return getStyleAttribute(StyleAttributes.Definition.PAGE);
  }
  



  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
  public void setPage(String page)
  {
    setStyleAttribute(StyleAttributes.Definition.PAGE.getAttributeName(), page);
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public int getPixelBottom()
  {
    return pixelValue(getBottom());
  }
  



  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void setPixelBottom(int pixelBottom)
  {
    setBottom(pixelBottom + "px");
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public int getPixelHeight()
  {
    return pixelValue(getHeight());
  }
  



  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void setPixelHeight(int pixelHeight)
  {
    setHeight(pixelHeight + "px");
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public int getPixelLeft()
  {
    return pixelValue(getLeft());
  }
  



  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void setPixelLeft(int pixelLeft)
  {
    setLeft(pixelLeft + "px");
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public int getPixelRight()
  {
    return pixelValue(getRight());
  }
  



  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void setPixelRight(int pixelRight)
  {
    setRight(pixelRight + "px");
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public int getPixelTop()
  {
    return pixelValue(getTop());
  }
  



  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void setPixelTop(int pixelTop)
  {
    setTop(pixelTop + "px");
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public int getPixelWidth()
  {
    return pixelValue(getWidth());
  }
  



  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void setPixelWidth(int pixelWidth)
  {
    setWidth(pixelWidth + "px");
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public int getPosBottom()
  {
    return 0;
  }
  





  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void setPosBottom(int posBottom) {}
  




  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public int getPosHeight()
  {
    return 0;
  }
  





  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void setPosHeight(int posHeight) {}
  




  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public int getPosLeft()
  {
    return 0;
  }
  





  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void setPosLeft(int posLeft) {}
  




  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public int getPosRight()
  {
    return 0;
  }
  





  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void setPosRight(int posRight) {}
  




  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public int getPosTop()
  {
    return 0;
  }
  





  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void setPosTop(int posTop) {}
  




  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public int getPosWidth()
  {
    return 0;
  }
  





  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void setPosWidth(int posWidth) {}
  




  @JsxGetter
  public String getRight()
  {
    return getStyleAttribute(StyleAttributes.Definition.RIGHT);
  }
  



  @JsxSetter
  public void setRight(Object right)
  {
    setStyleLengthAttribute(StyleAttributes.Definition.RIGHT.getAttributeName(), right, "", true, true, false, false, false);
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public String getRubyAlign()
  {
    return getStyleAttribute(StyleAttributes.Definition.RUBY_ALIGN);
  }
  



  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public void setRubyAlign(String rubyAlign)
  {
    setStyleAttribute(StyleAttributes.Definition.RUBY_ALIGN.getAttributeName(), rubyAlign);
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
  public String getSize()
  {
    return getStyleAttribute(StyleAttributes.Definition.SIZE);
  }
  



  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
  public void setSize(String size)
  {
    setStyleAttribute(StyleAttributes.Definition.SIZE.getAttributeName(), size);
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public boolean getTextDecorationBlink()
  {
    return false;
  }
  





  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void setTextDecorationBlink(boolean textDecorationBlink) {}
  




  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public boolean getTextDecorationLineThrough()
  {
    return false;
  }
  





  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void setTextDecorationLineThrough(boolean textDecorationLineThrough) {}
  




  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public boolean getTextDecorationNone()
  {
    return false;
  }
  





  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void setTextDecorationNone(boolean textDecorationNone) {}
  




  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public boolean getTextDecorationOverline()
  {
    return false;
  }
  





  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void setTextDecorationOverline(boolean textDecorationOverline) {}
  




  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public boolean getTextDecorationUnderline()
  {
    return false;
  }
  





  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void setTextDecorationUnderline(boolean textDecorationUnderline) {}
  




  @JsxGetter
  public String getTextIndent()
  {
    return getStyleAttribute(StyleAttributes.Definition.TEXT_INDENT);
  }
  



  @JsxSetter
  public void setTextIndent(Object textIndent)
  {
    setStyleLengthAttribute(StyleAttributes.Definition.TEXT_INDENT.getAttributeName(), textIndent, "", false, true, false, false, false);
  }
  



  @JsxGetter
  public String getTop()
  {
    return getStyleAttribute(StyleAttributes.Definition.TOP);
  }
  



  @JsxSetter
  public void setTop(Object top)
  {
    setStyleLengthAttribute(StyleAttributes.Definition.TOP.getAttributeName(), top, "", true, true, false, false, false);
  }
  



  @JsxGetter
  public String getVerticalAlign()
  {
    return getStyleAttribute(StyleAttributes.Definition.VERTICAL_ALIGN);
  }
  



  @JsxSetter
  public void setVerticalAlign(Object verticalAlign)
  {
    boolean auto = getBrowserVersion().hasFeature(BrowserVersionFeatures.CSS_VERTICAL_ALIGN_SUPPORTS_AUTO);
    setStyleLengthAttribute(StyleAttributes.Definition.VERTICAL_ALIGN.getAttributeName(), verticalAlign, "", auto, true, false, false, false);
  }
  



  @JsxGetter
  public String getWidth()
  {
    return getStyleAttribute(StyleAttributes.Definition.WIDTH);
  }
  



  @JsxSetter
  public void setWidth(Object width)
  {
    setStyleLengthAttribute(StyleAttributes.Definition.WIDTH.getAttributeName(), width, "", true, true, false, false, false);
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public String getWidows()
  {
    return getStyleAttribute(StyleAttributes.Definition.WIDOWS);
  }
  



  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void setWidows(String widows)
  {
    if (getBrowserVersion().hasFeature(BrowserVersionFeatures.CSS_BACKGROUND_INITIAL)) {
      try {
        if (Integer.parseInt(widows) <= 0) {
          return;
        }
      }
      catch (NumberFormatException e) {
        return;
      }
    }
    setStyleAttribute(StyleAttributes.Definition.WIDOWS.getAttributeName(), widows);
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public String getOrphans()
  {
    return getStyleAttribute(StyleAttributes.Definition.ORPHANS);
  }
  



  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void setOrphans(String orphans)
  {
    if (getBrowserVersion().hasFeature(BrowserVersionFeatures.CSS_BACKGROUND_INITIAL)) {
      try {
        if (Integer.parseInt(orphans) <= 0) {
          return;
        }
      }
      catch (NumberFormatException e) {
        return;
      }
    }
    setStyleAttribute(StyleAttributes.Definition.ORPHANS.getAttributeName(), orphans);
  }
  



  @JsxGetter
  public String getPosition()
  {
    return getStyleAttribute(StyleAttributes.Definition.POSITION);
  }
  



  @JsxSetter
  public void setPosition(String position)
  {
    if ((position.isEmpty()) || ("static".equalsIgnoreCase(position)) || ("absolute".equalsIgnoreCase(position)) || 
      ("fixed".equalsIgnoreCase(position)) || ("relative".equalsIgnoreCase(position)) || 
      ("initial".equalsIgnoreCase(position)) || ("inherit".equalsIgnoreCase(position))) {
      setStyleAttribute(StyleAttributes.Definition.POSITION.getAttributeName(), position.toLowerCase());
    }
  }
  



  @JsxGetter
  public String getWordSpacing()
  {
    return getStyleAttribute(StyleAttributes.Definition.WORD_SPACING);
  }
  



  @JsxSetter
  public void setWordSpacing(Object wordSpacing)
  {
    setStyleLengthAttribute(StyleAttributes.Definition.WORD_SPACING.getAttributeName(), wordSpacing, "", 
      false, getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_STYLE_WORD_SPACING_ACCEPTS_PERCENT), false, false, false);
  }
  



  @JsxGetter
  public Object getZIndex()
  {
    String value = getStyleAttribute(StyleAttributes.Definition.Z_INDEX_);
    if (getBrowserVersion().hasFeature(BrowserVersionFeatures.CSS_ZINDEX_TYPE_INTEGER)) {
      try {
        return Integer.valueOf(value);
      }
      catch (NumberFormatException e) {
        return "";
      }
    }
    
    try
    {
      Integer.parseInt(value);
      return value;
    }
    catch (NumberFormatException e) {}
    return "";
  }
  





  @JsxSetter
  public void setZIndex(Object zIndex)
  {
    if ((zIndex == null) || (org.apache.commons.lang3.StringUtils.isEmpty(zIndex.toString()))) {
      setStyleAttribute(StyleAttributes.Definition.Z_INDEX_.getAttributeName(), "");
      return;
    }
    
    if (Undefined.instance.equals(zIndex)) {
      return;
    }
    

    if ((zIndex instanceof Number)) {
      Number number = (Number)zIndex;
      if (number.doubleValue() % 1.0D == 0.0D) {
        setStyleAttribute(StyleAttributes.Definition.Z_INDEX_.getAttributeName(), Integer.toString(number.intValue()));
      }
      return;
    }
    try {
      int i = Integer.parseInt(zIndex.toString());
      setStyleAttribute(StyleAttributes.Definition.Z_INDEX_.getAttributeName(), Integer.toString(i));
    }
    catch (NumberFormatException localNumberFormatException) {}
  }
  






  @JsxFunction
  public String getPropertyValue(String name)
  {
    if ((name != null) && (name.contains("-"))) {
      Object value = getProperty(this, camelize(name));
      if ((value instanceof String)) {
        return (String)value;
      }
    }
    return getStyleAttributeImpl(name);
  }
  




  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public CSSValue getPropertyCSSValue(String name)
  {
    LOG.info("getPropertyCSSValue(" + name + "): getPropertyCSSValue support is experimental");
    

    if (styleDeclaration_ == null) {
      String uri = getDomNodeOrDie().getPage().getWebResponse().getWebRequest()
        .getUrl().toExternalForm();
      String styleAttribute = jsElement_.getDomNodeOrDie().getAttribute("style");
      InputSource source = new InputSource(new StringReader(styleAttribute));
      source.setURI(uri);
      ErrorHandler errorHandler = getWindow().getWebWindow().getWebClient().getCssErrorHandler();
      CSSOMParser parser = new CSSOMParser(new SACParserCSS3());
      parser.setErrorHandler(errorHandler);
      try {
        styleDeclaration_ = parser.parseStyleDeclaration(source);
      }
      catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    org.w3c.dom.css.CSSValue cssValue = styleDeclaration_.getPropertyCSSValue(name);
    if (cssValue == null) {
      CSSValueImpl newValue = new CSSValueImpl();
      newValue.setFloatValue((short)5, 0.0F);
      cssValue = newValue;
    }
    

    String cssText = cssValue.getCssText();
    if (cssText.startsWith("rgb(")) {
      String formatedCssText = org.apache.commons.lang3.StringUtils.replace(cssText, ",", ", ");
      cssValue.setCssText(formatedCssText);
    }
    
    return new CSSPrimitiveValue(jsElement_, (org.w3c.dom.css.CSSPrimitiveValue)cssValue);
  }
  




  @JsxFunction
  public String getPropertyPriority(String name)
  {
    return getStylePriority(name);
  }
  






  @JsxFunction
  public void setProperty(String name, Object value, String important)
  {
    String imp = "";
    if ((!org.apache.commons.lang3.StringUtils.isEmpty(important)) && (!"null".equals(important))) {
      if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_STYLE_SET_PROPERTY_IMPORTANT_IGNORES_CASE)) {
        if ("important".equalsIgnoreCase(important)) {}



      }
      else if (!"important".equals(important)) {
        return;
      }
      
      imp = "important";
    }
    
    if (LENGTH_PROPERTIES_FFFF.contains(name)) {
      setStyleLengthAttribute(name, value, imp, false, false, false, false, 
        getBrowserVersion().hasFeature(BrowserVersionFeatures.CSS_LENGTH_UNDEFINED_AS_EMPTY));
    }
    else if (LENGTH_PROPERTIES_TTFF.contains(name)) {
      setStyleLengthAttribute(name, value, imp, true, true, false, false, 
        getBrowserVersion().hasFeature(BrowserVersionFeatures.CSS_LENGTH_UNDEFINED_AS_EMPTY));
    }
    else if (LENGTH_PROPERTIES_FTFF.contains(name)) {
      setStyleLengthAttribute(name, value, imp, false, true, false, false, 
        getBrowserVersion().hasFeature(BrowserVersionFeatures.CSS_LENGTH_UNDEFINED_AS_EMPTY));
    }
    else if (StyleAttributes.Definition.OUTLINE_WIDTH.getAttributeName().equals(name)) {
      boolean requiresUnit = !getBrowserVersion().hasFeature(BrowserVersionFeatures.CSS_OUTLINE_WIDTH_UNIT_NOT_REQUIRED);
      setStyleLengthAttribute(StyleAttributes.Definition.OUTLINE_WIDTH.getAttributeName(), value, imp, false, false, true, requiresUnit, 
        getBrowserVersion().hasFeature(BrowserVersionFeatures.CSS_LENGTH_UNDEFINED_AS_EMPTY));
    }
    else if (StyleAttributes.Definition.WORD_SPACING.getAttributeName().equals(name)) {
      setStyleLengthAttribute(StyleAttributes.Definition.WORD_SPACING.getAttributeName(), value, imp, 
        false, getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_STYLE_WORD_SPACING_ACCEPTS_PERCENT), false, false, 
        getBrowserVersion().hasFeature(BrowserVersionFeatures.CSS_LENGTH_UNDEFINED_AS_EMPTY));
    }
    else if (StyleAttributes.Definition.VERTICAL_ALIGN.getAttributeName().equals(name)) {
      boolean auto = getBrowserVersion().hasFeature(BrowserVersionFeatures.CSS_VERTICAL_ALIGN_SUPPORTS_AUTO);
      setStyleLengthAttribute(StyleAttributes.Definition.VERTICAL_ALIGN.getAttributeName(), value, imp, auto, true, false, false, 
        getBrowserVersion().hasFeature(BrowserVersionFeatures.CSS_LENGTH_UNDEFINED_AS_EMPTY));
    }
    else {
      setStyleAttribute(name, Context.toString(value), imp);
    }
  }
  




  @JsxFunction
  public String removeProperty(Object name)
  {
    return removeStyleAttribute(Context.toString(name));
  }
  









  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public Object getAttribute(String name, int flag)
  {
    StyleElement style = getStyleElementCaseInSensitive(name);
    if (style == null) {
      return "";
    }
    return style.getValue();
  }
  








  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void setAttribute(String name, String value, Object flag)
  {
    StyleElement style = getStyleElementCaseInSensitive(name);
    if (style != null) {
      setStyleAttribute(style.getName(), value);
    }
  }
  








  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public boolean removeAttribute(String name, Object flag)
  {
    StyleElement style = getStyleElementCaseInSensitive(name);
    if (style != null) {
      removeStyleAttribute(style.getName());
      return true;
    }
    return false;
  }
  




  private static String findColor(String text)
  {
    Color tmpColor = com.gargoylesoftware.htmlunit.util.StringUtils.findColorRGB(text);
    if (tmpColor != null) {
      return com.gargoylesoftware.htmlunit.util.StringUtils.formatColor(tmpColor);
    }
    
    String[] tokens = org.apache.commons.lang3.StringUtils.split(text, ' ');
    for (String token : tokens) {
      if (isColorKeyword(token)) {
        return token;
      }
      
      tmpColor = com.gargoylesoftware.htmlunit.util.StringUtils.asColorHexadecimal(token);
      if (tmpColor != null) {
        return com.gargoylesoftware.htmlunit.util.StringUtils.formatColor(tmpColor);
      }
    }
    return null;
  }
  




  private static String findImageUrl(String text)
  {
    Matcher m = URL_PATTERN.matcher(text);
    if (m.find()) {
      return "url(\"" + m.group(1) + "\")";
    }
    return null;
  }
  




  private static String findPosition(String text)
  {
    Matcher m = POSITION_PATTERN.matcher(text);
    if (m.find()) {
      return m.group(1) + " " + m.group(3);
    }
    m = POSITION_PATTERN2.matcher(text);
    if (m.find()) {
      return m.group(1) + " " + m.group(2);
    }
    m = POSITION_PATTERN3.matcher(text);
    if (m.find()) {
      return m.group(2) + " " + m.group(1);
    }
    return null;
  }
  




  private static String findRepeat(String text)
  {
    if (text.contains("repeat-x")) {
      return "repeat-x";
    }
    if (text.contains("repeat-y")) {
      return "repeat-y";
    }
    if (text.contains("no-repeat")) {
      return "no-repeat";
    }
    if (text.contains("repeat")) {
      return "repeat";
    }
    return null;
  }
  




  private static String findAttachment(String text)
  {
    if (text.contains("scroll")) {
      return "scroll";
    }
    if (text.contains("fixed")) {
      return "fixed";
    }
    return null;
  }
  




  private static String findBorderStyle(String text)
  {
    for (String token : org.apache.commons.lang3.StringUtils.split(text, ' ')) {
      if (isBorderStyle(token)) {
        return token;
      }
    }
    return null;
  }
  




  private static String findBorderWidth(String text)
  {
    for (String token : org.apache.commons.lang3.StringUtils.split(text, ' ')) {
      if (isBorderWidth(token)) {
        return token;
      }
    }
    return null;
  }
  




  private static boolean isColorKeyword(String token)
  {
    return CSSColors_.containsKey(token.toLowerCase(Locale.ROOT));
  }
  





  public static String toRGBColor(String color)
  {
    String rgbValue = (String)CSSColors_.get(color.toLowerCase(Locale.ROOT));
    if (rgbValue != null) {
      return rgbValue;
    }
    return color;
  }
  




  private static boolean isBorderStyle(String token)
  {
    return ("none".equalsIgnoreCase(token)) || ("hidden".equalsIgnoreCase(token)) || 
      ("dotted".equalsIgnoreCase(token)) || ("dashed".equalsIgnoreCase(token)) || 
      ("solid".equalsIgnoreCase(token)) || ("double".equalsIgnoreCase(token)) || 
      ("groove".equalsIgnoreCase(token)) || ("ridge".equalsIgnoreCase(token)) || 
      ("inset".equalsIgnoreCase(token)) || ("outset".equalsIgnoreCase(token));
  }
  




  private static boolean isBorderWidth(String token)
  {
    return ("thin".equalsIgnoreCase(token)) || ("medium".equalsIgnoreCase(token)) || 
      ("thick".equalsIgnoreCase(token)) || (isLength(token));
  }
  




  static boolean isLength(String token)
  {
    if ((token.endsWith("em")) || (token.endsWith("ex")) || (token.endsWith("px")) || (token.endsWith("in")) || 
      (token.endsWith("cm")) || (token.endsWith("mm")) || (token.endsWith("pt")) || (token.endsWith("pc")) || 
      (token.endsWith("%")))
    {
      if (token.endsWith("%")) {
        token = token.substring(0, token.length() - 1);
      }
      else {
        token = token.substring(0, token.length() - 2);
      }
      try {
        Double.parseDouble(token);
        return true;
      }
      catch (NumberFormatException localNumberFormatException) {}
    }
    

    return false;
  }
  








  protected static int pixelValue(Element element, CssValue value)
  {
    return pixelValue(element, value, false);
  }
  
  private static int pixelValue(Element element, CssValue value, boolean percentMode) {
    String s = value.get(element);
    if ((s.endsWith("%")) || ((s.isEmpty()) && ((element instanceof HTMLHtmlElement)))) {
      int i = NumberUtils.toInt(TO_INT_PATTERN.matcher(s).replaceAll("$1"), 100);
      Element parent = element.getParentElement();
      int absoluteValue = parent == null ? 
        value.getWindowDefaultValue() : pixelValue(parent, value, true);
      return (int)(i / 100.0D * absoluteValue);
    }
    if ("auto".equals(s)) {
      return value.getDefaultValue();
    }
    if (s.isEmpty()) {
      if ((element instanceof HTMLCanvasElement)) {
        return value.getWindowDefaultValue();
      }
      


      if (percentMode) {
        Element parent = element.getParentElement();
        if ((parent == null) || ((parent instanceof HTMLHtmlElement))) {
          return value.getWindowDefaultValue();
        }
        return pixelValue(parent, value, true);
      }
      
      return 0;
    }
    return pixelValue(s);
  }
  








  protected static int pixelValue(String value)
  {
    int i = NumberUtils.toInt(TO_INT_PATTERN.matcher(value).replaceAll("$1"), 0);
    if (value.length() < 2) {
      return i;
    }
    
    if (!value.endsWith("px"))
    {

      if (value.endsWith("em")) {
        i *= 16;
      }
      else if (value.endsWith("%")) {
        i = i * 16 / 100;
      }
      else if (value.endsWith("ex")) {
        i *= 10;
      }
      else if (value.endsWith("in")) {
        i *= 150;
      }
      else if (value.endsWith("cm")) {
        i *= 50;
      }
      else if (value.endsWith("mm")) {
        i *= 5;
      }
      else if (value.endsWith("pt")) {
        i *= 2;
      }
      else if (value.endsWith("pc"))
        i *= 24;
    }
    return i;
  }
  


  protected static abstract class CssValue
  {
    private final int defaultValue_;
    

    private final int windowDefaultValue_;
    


    public CssValue(int defaultValue, int windowDefaultValue)
    {
      defaultValue_ = defaultValue;
      windowDefaultValue_ = windowDefaultValue;
    }
    



    public int getDefaultValue()
    {
      return defaultValue_;
    }
    



    public int getWindowDefaultValue()
    {
      return windowDefaultValue_;
    }
    




    public final String get(Element element)
    {
      ComputedCSSStyleDeclaration style = element.getWindow().getComputedStyle(element, null);
      String value = get(style);
      return value;
    }
    




    public abstract String get(ComputedCSSStyleDeclaration paramComputedCSSStyleDeclaration);
  }
  




  public String toString()
  {
    if (jsElement_ == null) {
      return "CSSStyleDeclaration for 'null'";
    }
    String style = jsElement_.getDomNodeOrDie().getAttribute("style");
    return "CSSStyleDeclaration for '" + style + "'";
  }
  













  private void setStyleLengthAttribute(String name, Object value, String important, boolean auto, boolean percent, boolean thinMedThick, boolean unitRequired, boolean undefinedAsEmpty)
  {
    if (ScriptRuntime.NaNobj == value) {
      return;
    }
    

    String unit = "px";
    double doubleValue; double doubleValue; if ((value instanceof Number)) {
      if (unitRequired) {
        return;
      }
      doubleValue = ((Number)value).doubleValue();
    }
    else {
      String valueString = Context.toString(value);
      if ((undefinedAsEmpty) && (Undefined.instance == value)) {
        valueString = "";
      }
      else if (value == null) {
        valueString = "";
      }
      
      if (org.apache.commons.lang3.StringUtils.isEmpty(valueString)) {
        setStyleAttribute(name, valueString, important);
        return;
      }
      
      if (((auto) && ("auto".equals(valueString))) || 
        (("initial".equals(valueString)) && (getBrowserVersion().hasFeature(BrowserVersionFeatures.CSS_LENGTH_INITIAL))) || 
        ("inherit".equals(valueString))) {
        setStyleAttribute(name, valueString, important);
        return;
      }
      
      if (((thinMedThick) && ("thin".equals(valueString))) || 
        ("medium".equals(valueString)) || 
        ("thick".equals(valueString))) {
        setStyleAttribute(name, valueString, important);
        return;
      }
      
      if ((percent) && (valueString.endsWith("%"))) {
        unit = valueString.substring(valueString.length() - 1);
        valueString = valueString.substring(0, valueString.length() - 1);
      }
      else if ((valueString.endsWith("px")) || 
        (valueString.endsWith("em")) || 
        (valueString.endsWith("ex")) || 
        (valueString.endsWith("px")) || 
        (valueString.endsWith("cm")) || 
        (valueString.endsWith("mm")) || 
        (valueString.endsWith("in")) || 
        (valueString.endsWith("pc")) || 
        (valueString.endsWith("pc")) || 
        (valueString.endsWith("ch")) || 
        (valueString.endsWith("vh")) || 
        (valueString.endsWith("vw"))) {
        unit = valueString.substring(valueString.length() - 2);
        valueString = valueString.substring(0, valueString.length() - 2);
      }
      else if ((valueString.endsWith("rem")) || 
        (valueString.endsWith("vmin")) || 
        (valueString.endsWith("vmax"))) {
        unit = valueString.substring(valueString.length() - 3);
        valueString = valueString.substring(0, valueString.length() - 3);
      }
      else if (unitRequired) {
        return;
      }
      
      doubleValue = Context.toNumber(valueString);
    }
    try
    {
      if ((Double.isNaN(doubleValue)) || (Double.isInfinite(doubleValue))) {
        return;
      }
      String valueString;
      String valueString;
      if (doubleValue % 1.0D == 0.0D) {
        valueString = Integer.toString((int)doubleValue) + unit;
      }
      else {
        valueString = Double.toString(doubleValue) + unit;
      }
      
      setStyleAttribute(name, valueString, important);
    }
    catch (Exception localException) {}
  }
}
