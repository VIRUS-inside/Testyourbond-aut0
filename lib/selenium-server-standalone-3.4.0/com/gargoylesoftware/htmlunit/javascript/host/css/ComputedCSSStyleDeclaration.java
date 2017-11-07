package com.gargoylesoftware.htmlunit.javascript.host.css;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.css.SelectorSpecificity;
import com.gargoylesoftware.htmlunit.css.StyleElement;
import com.gargoylesoftware.htmlunit.html.BaseFrameElement;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlFileInput;
import com.gargoylesoftware.htmlunit.html.HtmlHiddenInput;
import com.gargoylesoftware.htmlunit.html.HtmlInlineFrame;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlResetInput;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.host.Element;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Text;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBodyElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLCanvasElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import org.apache.commons.lang3.StringUtils;
import org.w3c.css.sac.Selector;




































































































@JsxClass(isJSObject=false, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
public class ComputedCSSStyleDeclaration
  extends CSSStyleDeclaration
{
  private static final String EMPTY_FINAL = new String("");
  

  private static final int PIXELS_PER_CHAR = 10;
  

  private static final Set<StyleAttributes.Definition> INHERITABLE_DEFINITIONS = EnumSet.of(
    StyleAttributes.Definition.AZIMUTH, new StyleAttributes.Definition[] {
    StyleAttributes.Definition.BORDER_COLLAPSE, 
    StyleAttributes.Definition.BORDER_SPACING, 
    StyleAttributes.Definition.CAPTION_SIDE, 
    StyleAttributes.Definition.COLOR, 
    StyleAttributes.Definition.CURSOR, 
    StyleAttributes.Definition.DIRECTION, 
    StyleAttributes.Definition.ELEVATION, 
    StyleAttributes.Definition.EMPTY_CELLS, 
    StyleAttributes.Definition.FONT_FAMILY, 
    StyleAttributes.Definition.FONT_SIZE, 
    StyleAttributes.Definition.FONT_STYLE, 
    StyleAttributes.Definition.FONT_VARIANT, 
    StyleAttributes.Definition.FONT_WEIGHT, 
    StyleAttributes.Definition.FONT, 
    StyleAttributes.Definition.LETTER_SPACING, 
    StyleAttributes.Definition.LINE_HEIGHT, 
    StyleAttributes.Definition.LIST_STYLE_IMAGE, 
    StyleAttributes.Definition.LIST_STYLE_POSITION, 
    StyleAttributes.Definition.LIST_STYLE_TYPE, 
    StyleAttributes.Definition.LIST_STYLE, 
    StyleAttributes.Definition.ORPHANS, 
    StyleAttributes.Definition.PITCH_RANGE, 
    StyleAttributes.Definition.PITCH, 
    StyleAttributes.Definition.QUOTES, 
    StyleAttributes.Definition.RICHNESS, 
    StyleAttributes.Definition.SPEAK_HEADER, 
    StyleAttributes.Definition.SPEAK_NUMERAL, 
    StyleAttributes.Definition.SPEAK_PUNCTUATION, 
    StyleAttributes.Definition.SPEAK, 
    StyleAttributes.Definition.SPEECH_RATE, 
    StyleAttributes.Definition.STRESS, 
    StyleAttributes.Definition.TEXT_ALIGN, 
    StyleAttributes.Definition.TEXT_INDENT, 
    StyleAttributes.Definition.TEXT_TRANSFORM, 
    StyleAttributes.Definition.VISIBILITY, 
    StyleAttributes.Definition.VOICE_FAMILY, 
    StyleAttributes.Definition.VOICE_FAMILY, 
    StyleAttributes.Definition.VOLUME, 
    StyleAttributes.Definition.WHITE_SPACE, 
    StyleAttributes.Definition.WIDOWS, 
    StyleAttributes.Definition.WORD_SPACING });
  




  private final SortedMap<String, StyleElement> localModifications_ = new TreeMap();
  


  private Integer width_;
  


  private Integer height_;
  


  private Integer height2_;
  


  private Integer paddingHorizontal_;
  


  private Integer paddingVertical_;
  


  private Integer borderHorizontal_;
  


  private Integer borderVertical_;
  


  private Integer top_;
  



  public ComputedCSSStyleDeclaration() {}
  



  public ComputedCSSStyleDeclaration(CSSStyleDeclaration style)
  {
    super(style.getElement());
    getElement().setDefaults(this);
  }
  







  protected void setStyleAttribute(String name, String newValue) {}
  






  public void applyStyleFromSelector(org.w3c.dom.css.CSSStyleDeclaration declaration, Selector selector)
  {
    BrowserVersion browserVersion = getBrowserVersion();
    SelectorSpecificity specificity = new SelectorSpecificity(selector);
    for (int k = 0; k < declaration.getLength(); k++) {
      String name = declaration.item(k);
      String value = declaration.getPropertyValue(name);
      String priority = declaration.getPropertyPriority(name);
      if ((!"z-index".equals(name)) || (!browserVersion.hasFeature(BrowserVersionFeatures.CSS_COMPUTED_NO_Z_INDEX))) {
        applyLocalStyleAttribute(name, value, priority, specificity);
      }
    }
  }
  
  private void applyLocalStyleAttribute(String name, String newValue, String priority, SelectorSpecificity specificity)
  {
    if (!"important".equals(priority)) {
      StyleElement existingElement = (StyleElement)localModifications_.get(name);
      if (existingElement != null) {
        if ("important".equals(existingElement.getPriority())) {
          return;
        }
        if (specificity.compareTo(existingElement.getSpecificity()) < 0) {
          return;
        }
      }
    }
    StyleElement element = new StyleElement(name, newValue, priority, specificity);
    localModifications_.put(name, element);
  }
  







  public void setDefaultLocalStyleAttribute(String name, String newValue)
  {
    StyleElement element = new StyleElement(name, newValue);
    localModifications_.put(name, element);
  }
  
  protected StyleElement getStyleElement(String name)
  {
    StyleElement existent = super.getStyleElement(name);
    
    if (localModifications_ != null) {
      StyleElement localStyleMod = (StyleElement)localModifications_.get(name);
      if (localStyleMod == null) {
        return existent;
      }
      
      if (existent == null)
      {


        return localStyleMod;
      }
      

      if ("important".equals(localStyleMod.getPriority())) {
        if ("important".equals(existent.getPriority())) {
          if (existent.getSpecificity().compareTo(localStyleMod.getSpecificity()) < 0) {
            return localStyleMod;
          }
        }
        else {
          return localStyleMod;
        }
      }
    }
    return existent;
  }
  
  private String defaultIfEmpty(String str, StyleAttributes.Definition definition) {
    return defaultIfEmpty(str, definition, false);
  }
  
  private String defaultIfEmpty(String str, StyleAttributes.Definition definition, boolean isPixel)
  {
    if ((!getElement().getDomNodeOrDie().isAttachedToPage()) && 
      (getBrowserVersion().hasFeature(BrowserVersionFeatures.CSS_COMPUTED_NO_Z_INDEX))) {
      return EMPTY_FINAL;
    }
    if ((str == null) || (str.isEmpty())) {
      return definition.getDefaultComputedValue(getBrowserVersion());
    }
    if (isPixel) {
      return pixelString(str);
    }
    return str;
  }
  




  private String defaultIfEmpty(String str, String toReturnIfEmptyOrDefault, String defaultValue)
  {
    if ((!getElement().getDomNodeOrDie().isAttachedToPage()) && 
      (getBrowserVersion().hasFeature(BrowserVersionFeatures.CSS_COMPUTED_NO_Z_INDEX))) {
      return EMPTY_FINAL;
    }
    if ((str == null) || (str.isEmpty()) || (str.equals(defaultValue))) {
      return toReturnIfEmptyOrDefault;
    }
    return str;
  }
  



  public String getAccelerator()
  {
    return defaultIfEmpty(getStyleAttribute(StyleAttributes.Definition.ACCELERATOR, false), StyleAttributes.Definition.ACCELERATOR);
  }
  



  public String getBackgroundAttachment()
  {
    return defaultIfEmpty(super.getBackgroundAttachment(), StyleAttributes.Definition.BACKGROUND_ATTACHMENT);
  }
  



  public String getBackgroundColor()
  {
    String value = super.getBackgroundColor();
    if (StringUtils.isEmpty(value)) {
      return StyleAttributes.Definition.BACKGROUND_COLOR.getDefaultComputedValue(getBrowserVersion());
    }
    return toRGBColor(value);
  }
  



  public String getBackgroundImage()
  {
    return defaultIfEmpty(super.getBackgroundImage(), StyleAttributes.Definition.BACKGROUND_IMAGE);
  }
  




  public String getBackgroundPosition()
  {
    return defaultIfEmpty(super.getBackgroundPosition(), StyleAttributes.Definition.BACKGROUND_POSITION);
  }
  



  public String getBackgroundRepeat()
  {
    return defaultIfEmpty(super.getBackgroundRepeat(), StyleAttributes.Definition.BACKGROUND_REPEAT);
  }
  



  public String getBorderBottomColor()
  {
    return defaultIfEmpty(super.getBorderBottomColor(), StyleAttributes.Definition.BORDER_BOTTOM_COLOR);
  }
  



  public String getBorderBottomStyle()
  {
    return defaultIfEmpty(super.getBorderBottomStyle(), StyleAttributes.Definition.BORDER_BOTTOM_STYLE);
  }
  



  public String getBorderBottomWidth()
  {
    return pixelString(defaultIfEmpty(super.getBorderBottomWidth(), StyleAttributes.Definition.BORDER_BOTTOM_WIDTH));
  }
  



  public String getBorderLeftColor()
  {
    return defaultIfEmpty(super.getBorderLeftColor(), StyleAttributes.Definition.BORDER_LEFT_COLOR);
  }
  



  public String getBorderLeftStyle()
  {
    return defaultIfEmpty(super.getBorderLeftStyle(), StyleAttributes.Definition.BORDER_LEFT_STYLE);
  }
  



  public String getBorderLeftWidth()
  {
    return pixelString(defaultIfEmpty(super.getBorderLeftWidth(), "0px", null));
  }
  



  public String getBorderRightColor()
  {
    return defaultIfEmpty(super.getBorderRightColor(), "rgb(0, 0, 0)", null);
  }
  



  public String getBorderRightStyle()
  {
    return defaultIfEmpty(super.getBorderRightStyle(), "none", null);
  }
  



  public String getBorderRightWidth()
  {
    return pixelString(defaultIfEmpty(super.getBorderRightWidth(), "0px", null));
  }
  



  public String getBorderTopColor()
  {
    return defaultIfEmpty(super.getBorderTopColor(), "rgb(0, 0, 0)", null);
  }
  



  public String getBorderTopStyle()
  {
    return defaultIfEmpty(super.getBorderTopStyle(), "none", null);
  }
  



  public String getBorderTopWidth()
  {
    return pixelString(defaultIfEmpty(super.getBorderTopWidth(), "0px", null));
  }
  



  public String getBottom()
  {
    return defaultIfEmpty(super.getBottom(), "auto", null);
  }
  



  public String getColor()
  {
    String value = defaultIfEmpty(super.getColor(), "rgb(0, 0, 0)", null);
    return toRGBColor(value);
  }
  



  public String getCssFloat()
  {
    return defaultIfEmpty(super.getCssFloat(), StyleAttributes.Definition.CSS_FLOAT);
  }
  



  public String getDisplay()
  {
    return getDisplay(false);
  }
  
  /* Error */
  public String getDisplay(boolean ignoreBlockIfNotAttached)
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 193	com/gargoylesoftware/htmlunit/javascript/host/css/ComputedCSSStyleDeclaration:getElement	()Lcom/gargoylesoftware/htmlunit/javascript/host/Element;
    //   4: astore_2
    //   5: aload_2
    //   6: invokevirtual 315	com/gargoylesoftware/htmlunit/javascript/host/Element:getDomNodeOrDie	()Lcom/gargoylesoftware/htmlunit/html/DomElement;
    //   9: astore_3
    //   10: iconst_0
    //   11: istore 4
    //   13: aload_3
    //   14: invokevirtual 319	com/gargoylesoftware/htmlunit/html/DomElement:isAttachedToPage	()Z
    //   17: ifne +41 -> 58
    //   20: aload_0
    //   21: invokevirtual 208	com/gargoylesoftware/htmlunit/javascript/host/css/ComputedCSSStyleDeclaration:getBrowserVersion	()Lcom/gargoylesoftware/htmlunit/BrowserVersion;
    //   24: astore 5
    //   26: aload 5
    //   28: getstatic 236	com/gargoylesoftware/htmlunit/BrowserVersionFeatures:CSS_COMPUTED_NO_Z_INDEX	Lcom/gargoylesoftware/htmlunit/BrowserVersionFeatures;
    //   31: invokevirtual 242	com/gargoylesoftware/htmlunit/BrowserVersion:hasFeature	(Lcom/gargoylesoftware/htmlunit/BrowserVersionFeatures;)Z
    //   34: ifeq +6 -> 40
    //   37: ldc 32
    //   39: areturn
    //   40: iload_1
    //   41: ifne +17 -> 58
    //   44: aload 5
    //   46: getstatic 465	com/gargoylesoftware/htmlunit/BrowserVersionFeatures:CSS_COMPUTED_BLOCK_IF_NOT_ATTACHED	Lcom/gargoylesoftware/htmlunit/BrowserVersionFeatures;
    //   49: invokevirtual 242	com/gargoylesoftware/htmlunit/BrowserVersion:hasFeature	(Lcom/gargoylesoftware/htmlunit/BrowserVersionFeatures;)Z
    //   52: ifeq +6 -> 58
    //   55: iconst_1
    //   56: istore 4
    //   58: aload_0
    //   59: getstatic 468	com/gargoylesoftware/htmlunit/javascript/host/css/StyleAttributes$Definition:DISPLAY	Lcom/gargoylesoftware/htmlunit/javascript/host/css/StyleAttributes$Definition;
    //   62: iconst_0
    //   63: invokespecial 471	com/gargoylesoftware/htmlunit/javascript/host/css/CSSStyleDeclaration:getStyleAttribute	(Lcom/gargoylesoftware/htmlunit/javascript/host/css/StyleAttributes$Definition;Z)Ljava/lang/String;
    //   66: astore 5
    //   68: aload 5
    //   70: invokestatic 359	org/apache/commons/lang3/StringUtils:isEmpty	(Ljava/lang/CharSequence;)Z
    //   73: ifeq +321 -> 394
    //   76: aload_3
    //   77: instanceof 472
    //   80: ifeq +311 -> 391
    //   83: aload_3
    //   84: checkcast 472	com/gargoylesoftware/htmlunit/html/HtmlElement
    //   87: invokevirtual 474	com/gargoylesoftware/htmlunit/html/HtmlElement:getDefaultStyleDisplay	()Lcom/gargoylesoftware/htmlunit/html/HtmlElement$DisplayStyle;
    //   90: invokevirtual 478	com/gargoylesoftware/htmlunit/html/HtmlElement$DisplayStyle:value	()Ljava/lang/String;
    //   93: astore 6
    //   95: iload 4
    //   97: ifeq +291 -> 388
    //   100: aload 6
    //   102: dup
    //   103: astore 7
    //   105: invokevirtual 482	java/lang/String:hashCode	()I
    //   108: lookupswitch	default:+280->388, -2088784499:+108->216, -2042894484:+122->230, -1989684389:+136->244, -1845523225:+150->258, -1551130623:+164->272, -1270571294:+178->286, -1183997287:+192->300, -718838695:+206->314, -289731865:+220->328, -273629643:+234->342, 3511770:+248->356, 610604766:+262->370
    //   216: aload 7
    //   218: ldc_w 485
    //   221: invokevirtual 232	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   224: ifne +160 -> 384
    //   227: goto +161 -> 388
    //   230: aload 7
    //   232: ldc_w 487
    //   235: invokevirtual 232	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   238: ifne +146 -> 384
    //   241: goto +147 -> 388
    //   244: aload 7
    //   246: ldc_w 489
    //   249: invokevirtual 232	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   252: ifne +132 -> 384
    //   255: goto +133 -> 388
    //   258: aload 7
    //   260: ldc_w 491
    //   263: invokevirtual 232	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   266: ifne +118 -> 384
    //   269: goto +119 -> 388
    //   272: aload 7
    //   274: ldc_w 493
    //   277: invokevirtual 232	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   280: ifne +104 -> 384
    //   283: goto +105 -> 388
    //   286: aload 7
    //   288: ldc_w 495
    //   291: invokevirtual 232	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   294: ifne +90 -> 384
    //   297: goto +91 -> 388
    //   300: aload 7
    //   302: ldc_w 497
    //   305: invokevirtual 232	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   308: ifne +76 -> 384
    //   311: goto +77 -> 388
    //   314: aload 7
    //   316: ldc_w 499
    //   319: invokevirtual 232	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   322: ifne +62 -> 384
    //   325: goto +63 -> 388
    //   328: aload 7
    //   330: ldc_w 501
    //   333: invokevirtual 232	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   336: ifne +48 -> 384
    //   339: goto +49 -> 388
    //   342: aload 7
    //   344: ldc_w 503
    //   347: invokevirtual 232	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   350: ifne +34 -> 384
    //   353: goto +35 -> 388
    //   356: aload 7
    //   358: ldc_w 505
    //   361: invokevirtual 232	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   364: ifne +20 -> 384
    //   367: goto +21 -> 388
    //   370: aload 7
    //   372: ldc_w 507
    //   375: invokevirtual 232	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   378: ifne +6 -> 384
    //   381: goto +7 -> 388
    //   384: ldc_w 509
    //   387: areturn
    //   388: aload 6
    //   390: areturn
    //   391: ldc 32
    //   393: areturn
    //   394: aload 5
    //   396: areturn
    // Line number table:
    //   Java source line #568	-> byte code offset #0
    //   Java source line #569	-> byte code offset #5
    //   Java source line #570	-> byte code offset #10
    //   Java source line #571	-> byte code offset #13
    //   Java source line #572	-> byte code offset #20
    //   Java source line #573	-> byte code offset #26
    //   Java source line #574	-> byte code offset #37
    //   Java source line #576	-> byte code offset #40
    //   Java source line #577	-> byte code offset #55
    //   Java source line #580	-> byte code offset #58
    //   Java source line #581	-> byte code offset #68
    //   Java source line #582	-> byte code offset #76
    //   Java source line #583	-> byte code offset #83
    //   Java source line #584	-> byte code offset #95
    //   Java source line #585	-> byte code offset #100
    //   Java source line #598	-> byte code offset #384
    //   Java source line #603	-> byte code offset #388
    //   Java source line #605	-> byte code offset #391
    //   Java source line #607	-> byte code offset #394
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	397	0	this	ComputedCSSStyleDeclaration
    //   0	397	1	ignoreBlockIfNotAttached	boolean
    //   4	2	2	elem	Element
    //   9	75	3	domElem	DomElement
    //   11	85	4	changeValueIfEmpty	boolean
    //   24	21	5	browserVersion	BrowserVersion
    //   66	329	5	value	String
    //   93	296	6	defaultValue	String
    //   103	268	7	str1	String
  }
  
  public String getFont()
  {
    if ((getBrowserVersion().hasFeature(BrowserVersionFeatures.CSS_COMPUTED_NO_Z_INDEX)) && 
      (getElement().getDomNodeOrDie().isAttachedToPage())) {
      return 
      

        getStyleAttribute(StyleAttributes.Definition.FONT_STYLE, true) + ' ' + getStyleAttribute(StyleAttributes.Definition.FONT_VARIANT, true) + ' ' + getStyleAttribute(StyleAttributes.Definition.FONT_WEIGHT, true) + ' ' + getStyleAttribute(StyleAttributes.Definition.FONT_STRETCH, true) + ' ' + getFontSize() + ' ' + '/' + ' ' + getStyleAttribute(StyleAttributes.Definition.LINE_HEIGHT, true) + ' ' + getStyleAttribute(StyleAttributes.Definition.FONT_FAMILY, true);
    }
    return "";
  }
  



  public String getFontSize()
  {
    String value = super.getFontSize();
    if (!value.isEmpty()) {
      value = pixelValue(value) + "px";
    }
    return value;
  }
  



  public String getLineHeight()
  {
    return defaultIfEmpty(super.getLineHeight(), StyleAttributes.Definition.LINE_HEIGHT);
  }
  



  public String getFontFamily()
  {
    return defaultIfEmpty(super.getFontFamily(), StyleAttributes.Definition.FONT_FAMILY);
  }
  



  public String getHeight()
  {
    final Element elem = getElement();
    if (!elem.getDomNodeOrDie().isAttachedToPage()) {
      if (getBrowserVersion().hasFeature(BrowserVersionFeatures.CSS_COMPUTED_NO_Z_INDEX)) {
        return "";
      }
      if (getStyleAttribute(StyleAttributes.Definition.HEIGHT, true).isEmpty()) {
        return "auto";
      }
    }
    int windowHeight = elem.getWindow().getWebWindow().getInnerHeight();
    pixelString(elem, new CSSStyleDeclaration.CssValue(0, windowHeight)
    {
      public String get(ComputedCSSStyleDeclaration style) {
        String offsetHeight = ((HTMLElement)elem).getOffsetHeight() + "px";
        return ComputedCSSStyleDeclaration.this.defaultIfEmpty(style.getStyleAttribute(StyleAttributes.Definition.HEIGHT, true), offsetHeight, "auto");
      }
    });
  }
  



  public String getLeft()
  {
    String superLeft = super.getLeft();
    if (!superLeft.endsWith("%")) {
      return defaultIfEmpty(superLeft, "auto", null);
    }
    
    final Element elem = getElement();
    pixelString(elem, new CSSStyleDeclaration.CssValue(0, 0)
    {
      public String get(ComputedCSSStyleDeclaration style) {
        if (style.getElement() == elem) {
          return style.getStyleAttribute(StyleAttributes.Definition.LEFT, true);
        }
        return style.getStyleAttribute(StyleAttributes.Definition.WIDTH, true);
      }
    });
  }
  



  public String getLetterSpacing()
  {
    return defaultIfEmpty(super.getLetterSpacing(), "normal", null);
  }
  



  public String getMargin()
  {
    return defaultIfEmpty(super.getMargin(), StyleAttributes.Definition.MARGIN, true);
  }
  



  public String getMarginBottom()
  {
    return pixelString(defaultIfEmpty(super.getMarginBottom(), "0px", null));
  }
  



  public String getMarginLeft()
  {
    return getMarginX(super.getMarginLeft(), StyleAttributes.Definition.MARGIN_LEFT);
  }
  



  public String getMarginRight()
  {
    return getMarginX(super.getMarginRight(), StyleAttributes.Definition.MARGIN_RIGHT);
  }
  
  private String getMarginX(String superMarginX, final StyleAttributes.Definition definition) {
    if (!superMarginX.endsWith("%")) {
      return pixelString(defaultIfEmpty(superMarginX, "0px", null));
    }
    final Element elem = getElement();
    if ((!elem.getDomNodeOrDie().isAttachedToPage()) && (getBrowserVersion().hasFeature(BrowserVersionFeatures.CSS_COMPUTED_NO_Z_INDEX))) {
      return "";
    }
    
    int windowWidth = elem.getWindow().getWebWindow().getInnerWidth();
    pixelString(elem, new CSSStyleDeclaration.CssValue(0, windowWidth)
    {
      public String get(ComputedCSSStyleDeclaration style) {
        if (style.getElement() == elem) {
          return style.getStyleAttribute(definition, true);
        }
        return style.getStyleAttribute(StyleAttributes.Definition.WIDTH, true);
      }
    });
  }
  



  public String getMarginTop()
  {
    return pixelString(defaultIfEmpty(super.getMarginTop(), "0px", null));
  }
  



  public String getMaxHeight()
  {
    return defaultIfEmpty(super.getMaxHeight(), "none", null);
  }
  



  public String getMaxWidth()
  {
    return defaultIfEmpty(super.getMaxWidth(), "none", null);
  }
  



  public String getMinHeight()
  {
    return defaultIfEmpty(super.getMinHeight(), "0px", null);
  }
  



  public String getMinWidth()
  {
    return defaultIfEmpty(super.getMinWidth(), "0px", null);
  }
  



  public String getOpacity()
  {
    return defaultIfEmpty(super.getOpacity(), "1", null);
  }
  



  public String getOutlineWidth()
  {
    return defaultIfEmpty(super.getOutlineWidth(), "0px", null);
  }
  



  public String getPadding()
  {
    return defaultIfEmpty(super.getPadding(), StyleAttributes.Definition.PADDING, true);
  }
  



  public String getPaddingBottom()
  {
    return pixelString(defaultIfEmpty(super.getPaddingBottom(), "0px", null));
  }
  



  public String getPaddingLeft()
  {
    return pixelString(defaultIfEmpty(super.getPaddingLeft(), "0px", null));
  }
  



  public String getPaddingRight()
  {
    return pixelString(defaultIfEmpty(super.getPaddingRight(), "0px", null));
  }
  



  public String getPaddingTop()
  {
    return pixelString(defaultIfEmpty(super.getPaddingTop(), "0px", null));
  }
  



  public String getRight()
  {
    return defaultIfEmpty(super.getRight(), "auto", null);
  }
  



  public String getTextIndent()
  {
    return defaultIfEmpty(super.getTextIndent(), "0px", null);
  }
  



  public String getTop()
  {
    final Element elem = getElement();
    if ((!elem.getDomNodeOrDie().isAttachedToPage()) && 
      (getBrowserVersion().hasFeature(BrowserVersionFeatures.CSS_COMPUTED_NO_Z_INDEX))) {
      return "";
    }
    String superTop = super.getTop();
    if (!superTop.endsWith("%")) {
      return defaultIfEmpty(superTop, StyleAttributes.Definition.TOP);
    }
    
    pixelString(elem, new CSSStyleDeclaration.CssValue(0, 0)
    {
      public String get(ComputedCSSStyleDeclaration style) {
        if (style.getElement() == elem) {
          return style.getStyleAttribute(StyleAttributes.Definition.TOP, true);
        }
        return style.getStyleAttribute(StyleAttributes.Definition.HEIGHT, true);
      }
    });
  }
  



  public String getVerticalAlign()
  {
    return defaultIfEmpty(super.getVerticalAlign(), "baseline", null);
  }
  



  public String getWidows()
  {
    return defaultIfEmpty(super.getWidows(), StyleAttributes.Definition.WIDOWS);
  }
  



  public String getOrphans()
  {
    return defaultIfEmpty(super.getOrphans(), StyleAttributes.Definition.ORPHANS);
  }
  



  public String getPosition()
  {
    return defaultIfEmpty(super.getPosition(), StyleAttributes.Definition.POSITION);
  }
  



  public String getWidth()
  {
    if ("none".equals(getDisplay())) {
      return "auto";
    }
    
    final Element elem = getElement();
    if (!elem.getDomNodeOrDie().isAttachedToPage()) {
      if (getBrowserVersion().hasFeature(BrowserVersionFeatures.CSS_COMPUTED_NO_Z_INDEX)) {
        return "";
      }
      if (getStyleAttribute(StyleAttributes.Definition.WIDTH, true).isEmpty()) {
        return "auto";
      }
    }
    
    int windowWidth = elem.getWindow().getWebWindow().getInnerWidth();
    pixelString(elem, new CSSStyleDeclaration.CssValue(0, windowWidth)
    {
      public String get(ComputedCSSStyleDeclaration style) {
        String value = style.getStyleAttribute(StyleAttributes.Definition.WIDTH, true);
        if (StringUtils.isEmpty(value)) {
          if ("absolute".equals(getStyleAttribute(StyleAttributes.Definition.POSITION, true))) {
            String content = getDomNodeOrDie().getTextContent();
            

            if ((content != null) && (content.length() < 13)) {
              return content.length() * 7 + "px";
            }
          }
          
          int windowDefaultValue = getWindowDefaultValue();
          if ((elem instanceof HTMLBodyElement)) {
            windowDefaultValue -= 16;
          }
          return windowDefaultValue + "px";
        }
        if ("auto".equals(value)) {
          int windowDefaultValue = getWindowDefaultValue();
          if ((elem instanceof HTMLBodyElement)) {
            windowDefaultValue -= 16;
          }
          return windowDefaultValue + "px";
        }
        
        return value;
      }
    });
  }
  





  public int getCalculatedWidth(boolean includeBorder, boolean includePadding)
  {
    if (!getElement().getDomNodeOrNull().isAttachedToPage()) {
      return 0;
    }
    int width = getCalculatedWidth();
    if (!"border-box".equals(getStyleAttribute(StyleAttributes.Definition.BOX_SIZING))) {
      if (includeBorder) {
        width += getBorderHorizontal();
      }
      else if ((isScrollable(true, true)) && (!(getElement() instanceof HTMLBodyElement)) && 
        (getElement().getDomNodeOrDie().isAttachedToPage())) {
        width -= 17;
      }
      
      if (includePadding) {
        width += getPaddingHorizontal();
      }
    }
    return width;
  }
  
  private int getCalculatedWidth() {
    if (width_ != null) {
      return width_.intValue();
    }
    
    Element element = getElement();
    DomNode node = element.getDomNodeOrDie();
    if (!node.mayBeDisplayed()) {
      width_ = Integer.valueOf(0);
      return 0;
    }
    
    String display = getDisplay();
    if ("none".equals(display)) {
      width_ = Integer.valueOf(0);
      return 0;
    }
    
    int windowWidth = element.getWindow().getWebWindow().getInnerWidth();
    

    String styleWidth = super.getWidth();
    DomNode parent = node.getParentNode();
    int width;
    int width;
    if ((("inline".equals(display)) || (StringUtils.isEmpty(styleWidth))) && ((parent instanceof HtmlElement)))
    {
      if ((element instanceof HTMLCanvasElement)) {
        return 300;
      }
      

      String cssFloat = getCssFloat();
      int width; if (("right".equals(cssFloat)) || ("left".equals(cssFloat)) || 
        ("absolute".equals(getStyleAttribute(StyleAttributes.Definition.POSITION, true))))
      {
        width = node.getTextContent().length() * 10;
      } else { int width;
        if ("block".equals(display)) { int width;
          if ((element instanceof HTMLBodyElement)) {
            width = windowWidth - 16;
          }
          else
          {
            HTMLElement parentJS = (HTMLElement)parent.getScriptableObject();
            width = pixelValue(parentJS, new CSSStyleDeclaration.CssValue(0, windowWidth) {
              public String get(ComputedCSSStyleDeclaration style) {
                return style.getWidth();
              }
            }) - (getBorderHorizontal() + getPaddingHorizontal());
          }
        } else { int width;
          if (((node instanceof HtmlSubmitInput)) || ((node instanceof HtmlResetInput)) || 
            ((node instanceof HtmlButtonInput)) || ((node instanceof HtmlButton)) || 
            ((node instanceof HtmlFileInput))) {
            String text = node.asText();
            
            width = 10 + (int)(text.length() * 10 * 0.9D);
          } else { int width;
            if (((node instanceof HtmlTextInput)) || ((node instanceof HtmlPasswordInput))) {
              BrowserVersion browserVersion = getBrowserVersion();
              if (browserVersion.hasFeature(BrowserVersionFeatures.JS_CLIENTWIDTH_INPUT_TEXT_143)) {
                return 143;
              }
              if (browserVersion.hasFeature(BrowserVersionFeatures.JS_CLIENTWIDTH_INPUT_TEXT_169)) {
                return 169;
              }
              width = 141;
            } else { int width;
              if (((node instanceof HtmlRadioButtonInput)) || ((node instanceof HtmlCheckBoxInput))) {
                width = 13;
              } else { int width;
                if ((node instanceof HtmlTextArea)) {
                  width = 100;
                }
                else
                {
                  width = getContentWidth(); }
              }
            } } } } } else { int width;
      if ("auto".equals(styleWidth)) {
        width = windowWidth;
      }
      else
      {
        width = pixelValue(element, new CSSStyleDeclaration.CssValue(0, windowWidth) {
          public String get(ComputedCSSStyleDeclaration style) {
            return style.getStyleAttribute(StyleAttributes.Definition.WIDTH, true);
          }
        });
      }
    }
    width_ = Integer.valueOf(width);
    return width;
  }
  



  public int getContentWidth()
  {
    int width = 0;
    DomNode domNode = getDomNodeOrDie();
    Iterable<DomNode> children = domNode.getChildren();
    HtmlPage htmlPage; if ((domNode instanceof BaseFrameElement)) {
      Page enclosedPage = ((BaseFrameElement)domNode).getEnclosedPage();
      if ((enclosedPage != null) && (enclosedPage.isHtmlPage())) {
        htmlPage = (HtmlPage)enclosedPage;
        children = htmlPage.getChildren();
      }
    }
    for (DomNode child : children) {
      if ((child.getScriptableObject() instanceof HTMLElement)) {
        HTMLElement e = (HTMLElement)child.getScriptableObject();
        ComputedCSSStyleDeclaration style = e.getWindow().getComputedStyle(e, null);
        int w = style.getCalculatedWidth(true, true);
        width += w;
      }
      else if ((child.getScriptableObject() instanceof Text)) {
        DomNode parent = child.getParentNode();
        if ((parent instanceof HtmlElement)) {
          HTMLElement e = (HTMLElement)child.getParentNode().getScriptableObject();
          ComputedCSSStyleDeclaration style = e.getWindow().getComputedStyle(e, null);
          int height = ComputedHeight.getHeight(getBrowserVersion(), style.getFontSize());
          width += child.getTextContent().length() * (int)(height / 1.8F);
        }
        else {
          width += child.getTextContent().length() * 10;
        }
      }
    }
    return width;
  }
  





  public int getCalculatedHeight(boolean includeBorder, boolean includePadding)
  {
    if (!getElement().getDomNodeOrNull().isAttachedToPage()) {
      return 0;
    }
    int height = getCalculatedHeight();
    if (!"border-box".equals(getStyleAttribute(StyleAttributes.Definition.BOX_SIZING))) {
      if (includeBorder) {
        height += getBorderVertical();
      }
      else if ((isScrollable(false, true)) && (!(getElement() instanceof HTMLBodyElement)) && 
        (getElement().getDomNodeOrDie().isAttachedToPage())) {
        height -= 17;
      }
      
      if (includePadding) {
        height += getPaddingVertical();
      }
    }
    return height;
  }
  



  private int getCalculatedHeight()
  {
    if (height_ != null) {
      return height_.intValue();
    }
    
    int elementHeight = getEmptyHeight();
    if (elementHeight == 0) {
      height_ = Integer.valueOf(elementHeight);
      return elementHeight;
    }
    
    int contentHeight = getContentHeight();
    boolean explicitHeightSpecified = !super.getHeight().isEmpty();
    int height;
    int height;
    if ((contentHeight > 0) && (!explicitHeightSpecified)) {
      height = contentHeight;
    }
    else {
      height = elementHeight;
    }
    
    height_ = Integer.valueOf(height);
    return height;
  }
  






  private int getEmptyHeight()
  {
    if (height2_ != null) {
      return height2_.intValue();
    }
    
    DomNode node = getElement().getDomNodeOrDie();
    if (!node.mayBeDisplayed()) {
      height2_ = Integer.valueOf(0);
      return 0;
    }
    
    if ("none".equals(getDisplay())) {
      height2_ = Integer.valueOf(0);
      return 0;
    }
    
    Element elem = getElement();
    int windowHeight = elem.getWindow().getWebWindow().getInnerHeight();
    
    if ((elem instanceof HTMLBodyElement)) {
      height2_ = Integer.valueOf(windowHeight);
      return windowHeight;
    }
    
    boolean explicitHeightSpecified = !super.getHeight().isEmpty();
    int defaultHeight;
    int defaultHeight;
    if (((node instanceof HtmlDivision)) && (node.getTextContent().trim().isEmpty())) {
      defaultHeight = 0;
    } else { int defaultHeight;
      if (elem.getFirstChild() == null) { int defaultHeight;
        if (((node instanceof HtmlRadioButtonInput)) || ((node instanceof HtmlCheckBoxInput))) {
          defaultHeight = 13;
        } else { int defaultHeight;
          if ((node instanceof HtmlButton)) {
            defaultHeight = 20;
          } else { int defaultHeight;
            if (((node instanceof HtmlInput)) && (!(node instanceof HtmlHiddenInput))) { int defaultHeight;
              if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_CLIENTHIGHT_INPUT_17)) {
                defaultHeight = 17;
              }
              else
                defaultHeight = 21;
            } else {
              int defaultHeight;
              if ((node instanceof HtmlSelect)) {
                defaultHeight = 20;
              } else { int defaultHeight;
                if ((node instanceof HtmlTextArea)) {
                  defaultHeight = 49;
                } else { int defaultHeight;
                  if ((node instanceof HtmlInlineFrame)) {
                    defaultHeight = 154;
                  }
                  else
                    defaultHeight = 0;
                }
              }
            }
          } } } else { defaultHeight = ComputedHeight.getHeight(getBrowserVersion(), getFontSize());
        if ((node instanceof HtmlDivision)) {
          defaultHeight *= (StringUtils.countMatches(node.asText(), '\n') + 1);
        }
      }
    }
    int defaultWindowHeight = (elem instanceof HTMLCanvasElement) ? 150 : windowHeight;
    
    int height = pixelValue(elem, new CSSStyleDeclaration.CssValue(defaultHeight, defaultWindowHeight) {
      public String get(ComputedCSSStyleDeclaration style) {
        Element element = style.getElement();
        if ((element instanceof HTMLBodyElement)) {
          return String.valueOf(element.getWindow().getWebWindow().getInnerHeight());
        }
        return style.getStyleAttribute(StyleAttributes.Definition.HEIGHT, true);
      }
    });
    
    if ((height == 0) && (!explicitHeightSpecified)) {
      height = defaultHeight;
    }
    
    height2_ = Integer.valueOf(height);
    return height;
  }
  







  public int getContentHeight()
  {
    DomNode node = getElement().getDomNodeOrDie();
    if (!node.mayBeDisplayed()) {
      return 0;
    }
    
    ComputedCSSStyleDeclaration lastFlowing = null;
    Set<ComputedCSSStyleDeclaration> styles = new HashSet();
    Object scriptObj; for (DomNode child : node.getChildren()) {
      if (child.mayBeDisplayed()) {
        scriptObj = child.getScriptableObject();
        if ((scriptObj instanceof HTMLElement)) {
          HTMLElement e = (HTMLElement)scriptObj;
          ComputedCSSStyleDeclaration style = e.getWindow().getComputedStyle(e, null);
          String pos = style.getPositionWithInheritance();
          if (("static".equals(pos)) || ("relative".equals(pos))) {
            lastFlowing = style;
          }
          else if ("absolute".equals(pos)) {
            styles.add(style);
          }
        }
      }
    }
    
    if (lastFlowing != null) {
      styles.add(lastFlowing);
    }
    
    int max = 0;
    for (ComputedCSSStyleDeclaration style : styles) {
      int h = style.getTop(true, false, false) + style.getCalculatedHeight(true, true);
      if (h > max) {
        max = h;
      }
    }
    return max;
  }
  





  public boolean isScrollable(boolean horizontal)
  {
    return isScrollable(horizontal, false);
  }
  



  private boolean isScrollable(boolean horizontal, boolean ignoreSize)
  {
    Element node = getElement();
    String overflow = getStyleAttribute(StyleAttributes.Definition.OVERFLOW, true);
    boolean scrollable; boolean scrollable; if (horizontal)
    {
      scrollable = (((node instanceof HTMLBodyElement)) || ("scroll".equals(overflow)) || ("auto".equals(overflow))) && (
        (ignoreSize) || (getContentWidth() > getCalculatedWidth()));
    }
    else
    {
      scrollable = (((node instanceof HTMLBodyElement)) || ("scroll".equals(overflow)) || ("auto".equals(overflow))) && (
        (ignoreSize) || (getContentHeight() > getEmptyHeight()));
    }
    return scrollable;
  }
  






  public int getTop(boolean includeMargin, boolean includeBorder, boolean includePadding)
  {
    int top = 0;
    if (top_ == null) {
      String p = getPositionWithInheritance();
      if ("absolute".equals(p)) {
        top = getTopForAbsolutePositionWithInheritance();
      }
      else
      {
        DomNode prev = getElement().getDomNodeOrDie().getPreviousSibling();
        boolean prevHadComputedTop = false;
        while ((prev != null) && (!prevHadComputedTop)) {
          if ((prev instanceof HtmlElement)) {
            HTMLElement e = (HTMLElement)((HtmlElement)prev).getScriptableObject();
            ComputedCSSStyleDeclaration style = e.getWindow().getComputedStyle(e, null);
            int prevTop = 0;
            if (top_ == null) {
              String prevPosition = style.getPositionWithInheritance();
              if ("absolute".equals(prevPosition)) {
                prevTop += style.getTopForAbsolutePositionWithInheritance();

              }
              else if ("relative".equals(prevPosition)) {
                String t = style.getTopWithInheritance();
                prevTop += pixelValue(t);
              }
            }
            else
            {
              prevHadComputedTop = true;
              prevTop += top_.intValue();
            }
            prevTop += style.getCalculatedHeight(true, true);
            int margin = pixelValue(style.getMarginTop());
            prevTop += margin;
            top += prevTop;
          }
          prev = prev.getPreviousSibling();
        }
        
        if ("relative".equals(p)) {
          String t = getTopWithInheritance();
          top += pixelValue(t);
        }
      }
      top_ = Integer.valueOf(top);
    }
    else {
      top = top_.intValue();
    }
    
    if (includeMargin) {
      int margin = pixelValue(getMarginTop());
      top += margin;
    }
    
    if (includeBorder) {
      int border = pixelValue(getBorderTopWidth());
      top += border;
    }
    
    if (includePadding) {
      int padding = getPaddingTopValue();
      top += padding;
    }
    
    return top;
  }
  
  private int getTopForAbsolutePositionWithInheritance() {
    int top = 0;
    String t = getTopWithInheritance();
    
    if (!"auto".equals(t))
    {
      top = pixelValue(t);
    }
    else {
      String b = getBottomWithInheritance();
      
      if (!"auto".equals(b))
      {


        top = 0;
        DomNode child = getElement().getDomNodeOrDie().getParentNode().getFirstChild();
        while (child != null) {
          if (((child instanceof HtmlElement)) && (child.mayBeDisplayed())) {
            top += 20;
          }
          child = child.getNextSibling();
        }
        top -= pixelValue(b);
      }
    }
    return top;
  }
  






  public int getLeft(boolean includeMargin, boolean includeBorder, boolean includePadding)
  {
    String p = getPositionWithInheritance();
    String l = getLeftWithInheritance();
    String r = getRightWithInheritance();
    int left;
    int left;
    if (("absolute".equals(p)) && (!"auto".equals(l)))
    {
      left = pixelValue(l);
    } else { int left;
      if (("absolute".equals(p)) && (!"auto".equals(r)))
      {
        HTMLElement parent = (HTMLElement)getElement().getParentElement();
        ComputedCSSStyleDeclaration style = parent.getWindow().getComputedStyle(parent, null);
        int parentWidth = style.getCalculatedWidth(false, false);
        left = parentWidth - pixelValue(r);
      } else { int left;
        if (("fixed".equals(p)) && (!"auto".equals(r))) {
          HTMLElement parent = (HTMLElement)getElement().getParentElement();
          ComputedCSSStyleDeclaration style = getWindow().getComputedStyle(getElement(), null);
          ComputedCSSStyleDeclaration parentStyle = parent.getWindow().getComputedStyle(parent, null);
          left = pixelValue(parentStyle.getWidth()) - pixelValue(style.getWidth()) - pixelValue(r);
        } else { int left;
          if (("fixed".equals(p)) && ("auto".equals(l)))
          {
            HTMLElement parent = (HTMLElement)getElement().getParentElement();
            ComputedCSSStyleDeclaration style = parent.getWindow().getComputedStyle(parent, null);
            left = pixelValue(style.getLeftWithInheritance());
          }
          else if ("static".equals(p))
          {
            int left = 0;
            for (DomNode n = getDomNodeOrDie(); n != null; n = n.getPreviousSibling()) {
              if ((n.getScriptableObject() instanceof HTMLElement)) {
                HTMLElement e = (HTMLElement)n.getScriptableObject();
                ComputedCSSStyleDeclaration style = e.getWindow().getComputedStyle(e, null);
                String d = style.getDisplay();
                if ("block".equals(d)) {
                  break;
                }
                if (!"none".equals(d)) {
                  left += style.getCalculatedWidth(true, true);
                }
              }
              else if ((n.getScriptableObject() instanceof Text)) {
                left += n.getTextContent().length() * 10;
              }
              if ((n instanceof HtmlTableRow)) {
                break;
              }
            }
          }
          else
          {
            left = pixelValue(l);
          }
        } } }
    if (includeMargin) {
      int margin = getMarginLeftValue();
      left += margin;
    }
    
    if (includeBorder) {
      int border = pixelValue(getBorderLeftWidth());
      left += border;
    }
    
    if (includePadding) {
      int padding = getPaddingLeftValue();
      left += padding;
    }
    
    return left;
  }
  



  public String getPositionWithInheritance()
  {
    String p = getStyleAttribute(StyleAttributes.Definition.POSITION, true);
    if ("inherit".equals(p)) {
      HTMLElement parent = (HTMLElement)getElement().getParentElement();
      if (parent == null) {
        p = "static";
      }
      else {
        ComputedCSSStyleDeclaration style = parent.getWindow().getComputedStyle(parent, null);
        p = style.getPositionWithInheritance();
      }
    }
    return p;
  }
  



  public String getLeftWithInheritance()
  {
    String left = getLeft();
    if ("inherit".equals(left)) {
      HTMLElement parent = (HTMLElement)getElement().getParentElement();
      if (parent == null) {
        left = "auto";
      }
      else {
        ComputedCSSStyleDeclaration style = parent.getWindow().getComputedStyle(parent, null);
        left = style.getLeftWithInheritance();
      }
    }
    return left;
  }
  



  public String getRightWithInheritance()
  {
    String right = getRight();
    if ("inherit".equals(right)) {
      HTMLElement parent = (HTMLElement)getElement().getParentElement();
      if (parent == null) {
        right = "auto";
      }
      else {
        ComputedCSSStyleDeclaration style = parent.getWindow().getComputedStyle(parent, null);
        right = style.getRightWithInheritance();
      }
    }
    return right;
  }
  



  public String getTopWithInheritance()
  {
    String top = getTop();
    if ("inherit".equals(top)) {
      HTMLElement parent = (HTMLElement)getElement().getParentElement();
      if (parent == null) {
        top = "auto";
      }
      else {
        ComputedCSSStyleDeclaration style = parent.getWindow().getComputedStyle(parent, null);
        top = style.getTopWithInheritance();
      }
    }
    return top;
  }
  



  public String getBottomWithInheritance()
  {
    String bottom = getBottom();
    if ("inherit".equals(bottom)) {
      HTMLElement parent = (HTMLElement)getElement().getParentElement();
      if (parent == null) {
        bottom = "auto";
      }
      else {
        ComputedCSSStyleDeclaration style = parent.getWindow().getComputedStyle(parent, null);
        bottom = style.getBottomWithInheritance();
      }
    }
    return bottom;
  }
  



  public int getMarginLeftValue()
  {
    return pixelValue(getMarginLeft());
  }
  



  public int getMarginRightValue()
  {
    return pixelValue(getMarginRight());
  }
  



  public int getMarginTopValue()
  {
    return pixelValue(getMarginTop());
  }
  



  public int getMarginBottomValue()
  {
    return pixelValue(getMarginBottom());
  }
  



  public int getPaddingLeftValue()
  {
    return pixelValue(getPaddingLeft());
  }
  



  public int getPaddingRightValue()
  {
    return pixelValue(getPaddingRight());
  }
  



  public int getPaddingTopValue()
  {
    return pixelValue(getPaddingTop());
  }
  



  public int getPaddingBottomValue()
  {
    return pixelValue(getPaddingBottom());
  }
  
  private int getPaddingHorizontal() {
    if (paddingHorizontal_ == null) {
      paddingHorizontal_ = 
        Integer.valueOf("none".equals(getDisplay()) ? 0 : getPaddingLeftValue() + getPaddingRightValue());
    }
    return paddingHorizontal_.intValue();
  }
  
  private int getPaddingVertical() {
    if (paddingVertical_ == null) {
      paddingVertical_ = 
        Integer.valueOf("none".equals(getDisplay()) ? 0 : getPaddingTopValue() + getPaddingBottomValue());
    }
    return paddingVertical_.intValue();
  }
  



  public int getBorderLeftValue()
  {
    return pixelValue(getBorderLeftWidth());
  }
  



  public int getBorderRightValue()
  {
    return pixelValue(getBorderRightWidth());
  }
  



  public int getBorderTopValue()
  {
    return pixelValue(getBorderTopWidth());
  }
  



  public int getBorderBottomValue()
  {
    return pixelValue(getBorderBottomWidth());
  }
  
  private int getBorderHorizontal() {
    if (borderHorizontal_ == null) {
      borderHorizontal_ = 
        Integer.valueOf("none".equals(getDisplay()) ? 0 : getBorderLeftValue() + getBorderRightValue());
    }
    return borderHorizontal_.intValue();
  }
  
  private int getBorderVertical() {
    if (borderVertical_ == null) {
      borderVertical_ = 
        Integer.valueOf("none".equals(getDisplay()) ? 0 : getBorderTopValue() + getBorderBottomValue());
    }
    return borderVertical_.intValue();
  }
  



  public String getWordSpacing()
  {
    return defaultIfEmpty(super.getWordSpacing(), StyleAttributes.Definition.WORD_SPACING);
  }
  



  public String getStyleAttribute(StyleAttributes.Definition style, boolean getDefaultValueIfEmpty)
  {
    if ((!getElement().getDomNodeOrDie().isAttachedToPage()) && 
      (getBrowserVersion().hasFeature(BrowserVersionFeatures.CSS_COMPUTED_NO_Z_INDEX))) {
      return EMPTY_FINAL;
    }
    String value = super.getStyleAttribute(style, getDefaultValueIfEmpty);
    if (value.isEmpty()) {
      Element parent = getElement().getParentElement();
      if ((INHERITABLE_DEFINITIONS.contains(style)) && (parent != null)) {
        value = getWindow().getComputedStyle(parent, null).getStyleAttribute(style, getDefaultValueIfEmpty);
      }
      else if (getDefaultValueIfEmpty) {
        value = style.getDefaultComputedValue(getBrowserVersion());
      }
    }
    
    return value;
  }
  



  public Object getZIndex()
  {
    Object response = super.getZIndex();
    if (response.toString().isEmpty()) {
      return "auto";
    }
    return response;
  }
  




  public String getPropertyValue(String name)
  {
    Object property = getProperty(this, camelize(name));
    if (property == NOT_FOUND) {
      return super.getPropertyValue(name);
    }
    return Context.toString(property);
  }
  







  protected String pixelString(String value)
  {
    if ((value == EMPTY_FINAL) || (value.endsWith("px"))) {
      return value;
    }
    return pixelValue(value) + "px";
  }
  








  protected String pixelString(Element element, CSSStyleDeclaration.CssValue value)
  {
    String s = value.get(element);
    if (s.endsWith("px")) {
      return s;
    }
    return pixelValue(element, value) + "px";
  }
}
