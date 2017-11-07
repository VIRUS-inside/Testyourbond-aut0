package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomCharacterData;
import com.gargoylesoftware.htmlunit.html.DomComment;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HTMLParser;
import com.gargoylesoftware.htmlunit.html.HtmlBody;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlElement.DisplayStyle;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableDataCell;
import com.gargoylesoftware.htmlunit.html.SubmittableElement;
import com.gargoylesoftware.htmlunit.javascript.NamedNodeMap;
import com.gargoylesoftware.htmlunit.javascript.background.BackgroundJavaScriptFactory;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJob;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJobManager;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClasses;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.ClientRect;
import com.gargoylesoftware.htmlunit.javascript.host.ClientRectList;
import com.gargoylesoftware.htmlunit.javascript.host.Element;
import com.gargoylesoftware.htmlunit.javascript.host.Navigator;
import com.gargoylesoftware.htmlunit.javascript.host.Screen;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleDeclaration;
import com.gargoylesoftware.htmlunit.javascript.host.css.ComputedCSSStyleDeclaration;
import com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Attr;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DOMStringMap;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DOMTokenList;
import com.gargoylesoftware.htmlunit.javascript.host.dom.NodeList;
import com.gargoylesoftware.htmlunit.javascript.host.dom.TextRange;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventHandler;
import com.gargoylesoftware.htmlunit.javascript.host.event.MouseEvent;
import com.gargoylesoftware.htmlunit.util.StringUtils;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;


















































































































































@JsxClasses({@com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlAbbreviated.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlAcronym.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlAddress.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlArticle.class), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlAside.class), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlBaseFont.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlBidirectionalIsolation.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlBidirectionalOverride.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlBig.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlBold.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlCenter.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlCitation.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlCode.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlDefinition.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlDefinitionDescription.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlDefinitionTerm.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=HtmlElement.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlEmphasis.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlExample.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlFigure.class), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlFigureCaption.class), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlFooter.class), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlHeader.class), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlItalic.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlKeyboard.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlLayer.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlListing.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlMark.class), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlNav.class), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlNoBreak.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlNoEmbed.class), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlNoFrames.class), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlNoLayer.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlNoScript.class), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlPlainText.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlRuby.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlRp.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlRt.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlS.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlSample.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlSection.class), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlSmall.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlStrike.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlStrong.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlSubscript.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlSummary.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlSuperscript.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlTeletype.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlUnderlined.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlWordBreak.class), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlMain.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlVariable.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})})
public class HTMLElement
  extends Element
{
  private static final Class<?>[] METHOD_PARAMS_OBJECT = { Object.class };
  private static final Pattern PERCENT_VALUE = Pattern.compile("\\d+%");
  
  private static final Map<String, String> COLORS_MAP_IE = new HashMap();
  
  private static final Log LOG = LogFactory.getLog(HTMLElement.class);
  
  private static final int BEHAVIOR_ID_UNKNOWN = -1;
  
  public static final int BEHAVIOR_ID_CLIENT_CAPS = 0;
  
  public static final int BEHAVIOR_ID_HOMEPAGE = 1;
  
  public static final int BEHAVIOR_ID_DOWNLOAD = 2;
  
  private static final String BEHAVIOR_CLIENT_CAPS = "#default#clientCaps";
  
  private static final String BEHAVIOR_HOMEPAGE = "#default#homePage";
  private static final String BEHAVIOR_DOWNLOAD = "#default#download";
  private static final Pattern CLASS_NAMES_SPLIT_PATTERN = Pattern.compile("\\s");
  private static final Pattern PRINT_NODE_PATTERN = Pattern.compile("  ");
  private static final Pattern PRINT_NODE_QUOTE_PATTERN = Pattern.compile("\"");
  
  static final String POSITION_BEFORE_BEGIN = "beforebegin";
  
  static final String POSITION_AFTER_BEGIN = "afterbegin";
  
  static final String POSITION_BEFORE_END = "beforeend";
  
  static final String POSITION_AFTER_END = "afterend";
  
  private static int UniqueID_Counter_ = 1;
  
  private final Set<String> behaviors_ = new HashSet();
  private int scrollLeft_;
  private int scrollTop_;
  private String uniqueID_;
  private boolean endTagForbidden_;
  
  static { COLORS_MAP_IE.put("AliceBlue", "#F0F8FF");
    COLORS_MAP_IE.put("AntiqueWhite", "#FAEBD7");
    COLORS_MAP_IE.put("Aqua", "#00FFFF");
    COLORS_MAP_IE.put("Aquamarine", "#7FFFD4");
    COLORS_MAP_IE.put("Azure", "#F0FFFF");
    COLORS_MAP_IE.put("Beige", "#F5F5DC");
    COLORS_MAP_IE.put("Bisque", "#FFE4C4");
    COLORS_MAP_IE.put("Black", "#000000");
    COLORS_MAP_IE.put("BlanchedAlmond", "#FFEBCD");
    COLORS_MAP_IE.put("Blue", "#0000FF");
    COLORS_MAP_IE.put("BlueViolet", "#8A2BE2");
    COLORS_MAP_IE.put("Brown", "#A52A2A");
    COLORS_MAP_IE.put("BurlyWood", "#DEB887");
    COLORS_MAP_IE.put("CadetBlue", "#5F9EA0");
    COLORS_MAP_IE.put("Chartreuse", "#7FFF00");
    COLORS_MAP_IE.put("Chocolate", "#D2691E");
    COLORS_MAP_IE.put("Coral", "#FF7F50");
    COLORS_MAP_IE.put("CornflowerBlue", "#6495ED");
    COLORS_MAP_IE.put("Cornsilk", "#FFF8DC");
    COLORS_MAP_IE.put("Crimson", "#DC143C");
    COLORS_MAP_IE.put("Cyan", "#00FFFF");
    COLORS_MAP_IE.put("DarkBlue", "#00008B");
    COLORS_MAP_IE.put("DarkCyan", "#008B8B");
    COLORS_MAP_IE.put("DarkGoldenrod", "#B8860B");
    COLORS_MAP_IE.put("DarkGray", "#A9A9A9");
    COLORS_MAP_IE.put("DarkGrey", "#A9A9A9");
    COLORS_MAP_IE.put("DarkGreen", "#006400");
    COLORS_MAP_IE.put("DarkKhaki", "#BDB76B");
    COLORS_MAP_IE.put("DarkMagenta", "#8B008B");
    COLORS_MAP_IE.put("DarkOliveGreen", "#556B2F");
    COLORS_MAP_IE.put("DarkOrange", "#FF8C00");
    COLORS_MAP_IE.put("DarkOrchid", "#9932CC");
    COLORS_MAP_IE.put("DarkRed", "#8B0000");
    COLORS_MAP_IE.put("DarkSalmon", "#E9967A");
    COLORS_MAP_IE.put("DarkSeaGreen", "#8FBC8F");
    COLORS_MAP_IE.put("DarkSlateBlue", "#483D8B");
    COLORS_MAP_IE.put("DarkSlateGray", "#2F4F4F");
    COLORS_MAP_IE.put("DarkSlateGrey", "#2F4F4F");
    COLORS_MAP_IE.put("DarkTurquoise", "#00CED1");
    COLORS_MAP_IE.put("DarkViolet", "#9400D3");
    COLORS_MAP_IE.put("DeepPink", "#FF1493");
    COLORS_MAP_IE.put("DeepSkyBlue", "#00BFFF");
    COLORS_MAP_IE.put("DimGray", "#696969");
    COLORS_MAP_IE.put("DimGrey", "#696969");
    COLORS_MAP_IE.put("DodgerBlue", "#1E90FF");
    COLORS_MAP_IE.put("FireBrick", "#B22222");
    COLORS_MAP_IE.put("FloralWhite", "#FFFAF0");
    COLORS_MAP_IE.put("ForestGreen", "#228B22");
    COLORS_MAP_IE.put("Fuchsia", "#FF00FF");
    COLORS_MAP_IE.put("Gainsboro", "#DCDCDC");
    COLORS_MAP_IE.put("GhostWhite", "#F8F8FF");
    COLORS_MAP_IE.put("Gold", "#FFD700");
    COLORS_MAP_IE.put("Goldenrod", "#DAA520");
    COLORS_MAP_IE.put("Gray", "#808080");
    COLORS_MAP_IE.put("Grey", "#808080");
    COLORS_MAP_IE.put("Green", "#008000");
    COLORS_MAP_IE.put("GreenYellow", "#ADFF2F");
    COLORS_MAP_IE.put("Honeydew", "#F0FFF0");
    COLORS_MAP_IE.put("HotPink", "#FF69B4");
    COLORS_MAP_IE.put("IndianRed", "#CD5C5C");
    COLORS_MAP_IE.put("Indigo", "#4B0082");
    COLORS_MAP_IE.put("Ivory", "#FFFFF0");
    COLORS_MAP_IE.put("Khaki", "#F0E68C");
    COLORS_MAP_IE.put("Lavender", "#E6E6FA");
    COLORS_MAP_IE.put("LavenderBlush", "#FFF0F5");
    COLORS_MAP_IE.put("LawnGreen", "#7CFC00");
    COLORS_MAP_IE.put("LemonChiffon", "#FFFACD");
    COLORS_MAP_IE.put("LightBlue", "#ADD8E6");
    COLORS_MAP_IE.put("LightCoral", "#F08080");
    COLORS_MAP_IE.put("LightCyan", "#E0FFFF");
    COLORS_MAP_IE.put("LightGoldenrodYellow", "#FAFAD2");
    COLORS_MAP_IE.put("LightGreen", "#90EE90");
    COLORS_MAP_IE.put("LightGray", "#D3D3D3");
    COLORS_MAP_IE.put("LightGrey", "#D3D3D3");
    COLORS_MAP_IE.put("LightPink", "#FFB6C1");
    COLORS_MAP_IE.put("LightSalmon", "#FFA07A");
    COLORS_MAP_IE.put("LightSeaGreen", "#20B2AA");
    COLORS_MAP_IE.put("LightSkyBlue", "#87CEFA");
    COLORS_MAP_IE.put("LightSlateGray", "#778899");
    COLORS_MAP_IE.put("LightSlateGrey", "#778899");
    COLORS_MAP_IE.put("LightSteelBlue", "#B0C4DE");
    COLORS_MAP_IE.put("LightYellow", "#FFFFE0");
    COLORS_MAP_IE.put("Lime", "#00FF00");
    COLORS_MAP_IE.put("LimeGreen", "#32CD32");
    COLORS_MAP_IE.put("Linen", "#FAF0E6");
    COLORS_MAP_IE.put("Magenta", "#FF00FF");
    COLORS_MAP_IE.put("Maroon", "#800000");
    COLORS_MAP_IE.put("MediumAquamarine", "#66CDAA");
    COLORS_MAP_IE.put("MediumBlue", "#0000CD");
    COLORS_MAP_IE.put("MediumOrchid", "#BA55D3");
    COLORS_MAP_IE.put("MediumPurple", "#9370DB");
    COLORS_MAP_IE.put("MediumSeaGreen", "#3CB371");
    COLORS_MAP_IE.put("MediumSlateBlue", "#7B68EE");
    COLORS_MAP_IE.put("MediumSpringGreen", "#00FA9A");
    COLORS_MAP_IE.put("MediumTurquoise", "#48D1CC");
    COLORS_MAP_IE.put("MediumVioletRed", "#C71585");
    COLORS_MAP_IE.put("MidnightBlue", "#191970");
    COLORS_MAP_IE.put("MintCream", "#F5FFFA");
    COLORS_MAP_IE.put("MistyRose", "#FFE4E1");
    COLORS_MAP_IE.put("Moccasin", "#FFE4B5");
    COLORS_MAP_IE.put("NavajoWhite", "#FFDEAD");
    COLORS_MAP_IE.put("Navy", "#000080");
    COLORS_MAP_IE.put("OldLace", "#FDF5E6");
    COLORS_MAP_IE.put("Olive", "#808000");
    COLORS_MAP_IE.put("OliveDrab", "#6B8E23");
    COLORS_MAP_IE.put("Orange", "#FFA500");
    COLORS_MAP_IE.put("OrangeRed", "#FF4500");
    COLORS_MAP_IE.put("Orchid", "#DA70D6");
    COLORS_MAP_IE.put("PaleGoldenrod", "#EEE8AA");
    COLORS_MAP_IE.put("PaleGreen", "#98FB98");
    COLORS_MAP_IE.put("PaleTurquoise", "#AFEEEE");
    COLORS_MAP_IE.put("PaleVioletRed", "#DB7093");
    COLORS_MAP_IE.put("PapayaWhip", "#FFEFD5");
    COLORS_MAP_IE.put("PeachPuff", "#FFDAB9");
    COLORS_MAP_IE.put("Peru", "#CD853F");
    COLORS_MAP_IE.put("Pink", "#FFC0CB");
    COLORS_MAP_IE.put("Plum", "#DDA0DD");
    COLORS_MAP_IE.put("PowderBlue", "#B0E0E6");
    COLORS_MAP_IE.put("Purple", "#800080");
    COLORS_MAP_IE.put("Red", "#FF0000");
    COLORS_MAP_IE.put("RosyBrown", "#BC8F8F");
    COLORS_MAP_IE.put("RoyalBlue", "#4169E1");
    COLORS_MAP_IE.put("SaddleBrown", "#8B4513");
    COLORS_MAP_IE.put("Salmon", "#FA8072");
    COLORS_MAP_IE.put("SandyBrown", "#F4A460");
    COLORS_MAP_IE.put("SeaGreen", "#2E8B57");
    COLORS_MAP_IE.put("Seashell", "#FFF5EE");
    COLORS_MAP_IE.put("Sienna", "#A0522D");
    COLORS_MAP_IE.put("Silver", "#C0C0C0");
    COLORS_MAP_IE.put("SkyBlue", "#87CEEB");
    COLORS_MAP_IE.put("SlateBlue", "#6A5ACD");
    COLORS_MAP_IE.put("SlateGray", "#708090");
    COLORS_MAP_IE.put("SlateGrey", "#708090");
    COLORS_MAP_IE.put("Snow", "#FFFAFA");
    COLORS_MAP_IE.put("SpringGreen", "#00FF7F");
    COLORS_MAP_IE.put("SteelBlue", "#4682B4");
    COLORS_MAP_IE.put("Tan", "#D2B48C");
    COLORS_MAP_IE.put("Teal", "#008080");
    COLORS_MAP_IE.put("Thistle", "#D8BFD8");
    COLORS_MAP_IE.put("Tomato", "#FF6347");
    COLORS_MAP_IE.put("Turquoise", "#40E0D0");
    COLORS_MAP_IE.put("Violet", "#EE82EE");
    COLORS_MAP_IE.put("Wheat", "#F5DEB3");
    COLORS_MAP_IE.put("White", "#FFFFFF");
    COLORS_MAP_IE.put("WhiteSmoke", "#F5F5F5");
    COLORS_MAP_IE.put("Yellow", "#FFFF00");
    COLORS_MAP_IE.put("YellowGreen", "#9ACD32");
  }
  





  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public HTMLElement() {}
  





  public void setDomNode(DomNode domNode)
  {
    super.setDomNode(domNode);
    
    String name = domNode.getLocalName();
    if (("wbr".equalsIgnoreCase(name)) || 
      ("basefont".equalsIgnoreCase(name)) || 
      ("keygen".equalsIgnoreCase(name)) || 
      ("track".equalsIgnoreCase(name))) {
      endTagForbidden_ = true;
    }
  }
  



  @JsxGetter
  public String getId()
  {
    return getDomNodeOrDie().getId();
  }
  



  @JsxSetter
  public void setId(String newId)
  {
    getDomNodeOrDie().setId(newId);
  }
  



  @JsxGetter
  public String getTitle()
  {
    return getDomNodeOrDie().getAttribute("title");
  }
  



  @JsxSetter
  public void setTitle(String newTitle)
  {
    getDomNodeOrDie().setAttribute("title", newTitle);
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public boolean getDisabled()
  {
    return getDomNodeOrDie().hasAttribute("disabled");
  }
  



  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void setDisabled(boolean disabled)
  {
    HtmlElement element = getDomNodeOrDie();
    if (disabled) {
      element.setAttribute("disabled", "disabled");
    }
    else {
      element.removeAttribute("disabled");
    }
  }
  



  public String getLocalName()
  {
    DomNode domNode = getDomNodeOrDie();
    if (domNode.getHtmlPageOrNull() != null) {
      String prefix = domNode.getPrefix();
      if (prefix != null)
      {
        StringBuilder localName = new StringBuilder(prefix.toLowerCase(Locale.ROOT));
        localName.append(':');
        localName.append(domNode.getLocalName().toLowerCase(Locale.ROOT));
        return localName.toString();
      }
      return domNode.getLocalName().toLowerCase(Locale.ROOT);
    }
    return domNode.getLocalName();
  }
  


  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void clearAttributes()
  {
    HtmlElement node = getDomNodeOrDie();
    

    List<String> removals = new ArrayList();
    for (String attributeName : node.getAttributesMap().keySet())
    {

      if (!ScriptableObject.hasProperty(getPrototype(), attributeName)) {
        removals.add(attributeName);
      }
    }
    for (String attributeName : removals) {
      node.removeAttribute(attributeName);
    }
    

    for (Object id : getAllIds()) {
      if ((id instanceof Integer)) {
        int i = ((Integer)id).intValue();
        delete(i);
      }
      else if ((id instanceof String)) {
        delete((String)id);
      }
    }
  }
  





  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void mergeAttributes(HTMLElement source, Object preserveIdentity)
  {
    HtmlElement src = source.getDomNodeOrDie();
    HtmlElement target = getDomNodeOrDie();
    

    if (((preserveIdentity instanceof Boolean)) && (!((Boolean)preserveIdentity).booleanValue())) {
      target.setId(src.getId());
      target.setAttribute("name", src.getAttribute("name"));
    }
  }
  





  @JsxFunction
  public Object getAttributeNodeNS(String namespaceURI, String localName)
  {
    return getDomNodeOrDie().getAttributeNodeNS(namespaceURI, localName).getScriptableObject();
  }
  








  public void setAttribute(String name, String value)
  {
    getDomNodeOrDie().setAttribute(name, value);
    

    if (!name.isEmpty()) {
      name = name.toLowerCase(Locale.ROOT);
      if (name.startsWith("on")) {
        try {
          name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
          Method method = getClass().getMethod("set" + name, METHOD_PARAMS_OBJECT);
          method.invoke(this, new Object[] { new EventHandler(getDomNodeOrDie(), name.substring(2), value) });



        }
        catch (NoSuchMethodException localNoSuchMethodException) {}catch (IllegalAccessException localIllegalAccessException) {}catch (InvocationTargetException e)
        {


          throw new RuntimeException(e.getCause());
        }
      }
    }
  }
  



  @JsxFunction
  public void removeAttributeNode(Attr attribute)
  {
    String name = attribute.getName();
    Object namespaceUri = attribute.getNamespaceURI();
    if ((namespaceUri instanceof String)) {
      removeAttributeNS((String)namespaceUri, name);
      return;
    }
    removeAttributeNS(null, name);
  }
  




  protected AttributesImpl readAttributes(HtmlElement element)
  {
    AttributesImpl attributes = new AttributesImpl();
    for (DomAttr entry : element.getAttributesMap().values()) {
      String name = entry.getName();
      String value = entry.getValue();
      attributes.addAttribute(null, name, name, null, value);
    }
    
    return attributes;
  }
  




  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public HTMLElement removeNode(boolean removeChildren)
  {
    HTMLElement parent = (HTMLElement)getParentElement();
    if (parent != null) {
      parent.removeChild(this);
      if (!removeChildren) {
        NodeList collection = getChildNodes();
        int length = collection.getLength();
        for (int i = 0; i < length; i++) {
          com.gargoylesoftware.htmlunit.javascript.host.dom.Node object = (com.gargoylesoftware.htmlunit.javascript.host.dom.Node)collection.item(Integer.valueOf(0));
          parent.appendChild(object);
        }
      }
    }
    return this;
  }
  





  public Object getAttributeNode(String attributeName)
  {
    return getAttributes().getNamedItem(attributeName);
  }
  




  @JsxFunction
  public HTMLCollection getElementsByClassName(String className)
  {
    HtmlElement elt = getDomNodeOrDie();
    final String[] classNames = CLASS_NAMES_SPLIT_PATTERN.split(className, 0);
    
    HTMLCollection collection = new HTMLCollection(elt, true)
    {
      protected boolean isMatching(DomNode node) {
        if (!(node instanceof HtmlElement)) {
          return false;
        }
        String classAttribute = ((HtmlElement)node).getAttribute("class");
        if (classAttribute == DomElement.ATTRIBUTE_NOT_DEFINED) {
          return false;
        }
        
        classAttribute = " " + classAttribute + " ";
        for (String aClassName : classNames) {
          if (!classAttribute.contains(" " + aClassName + " ")) {
            return false;
          }
        }
        return true;
      }
      
    };
    return collection;
  }
  



  @JsxGetter(propertyName="className")
  public Object getClassName_js()
  {
    return getDomNodeOrDie().getAttribute("class");
  }
  



  @JsxGetter
  public int getClientHeight()
  {
    ComputedCSSStyleDeclaration style = getWindow().getComputedStyle(this, null);
    return style.getCalculatedHeight(false, true);
  }
  



  @JsxGetter
  public int getClientWidth()
  {
    ComputedCSSStyleDeclaration style = getWindow().getComputedStyle(this, null);
    return style.getCalculatedWidth(false, true);
  }
  



  @JsxSetter(propertyName="className")
  public void setClassName_js(String className)
  {
    getDomNodeOrDie().setAttribute("class", className);
  }
  



  @JsxGetter
  public String getInnerHTML()
  {
    try
    {
      domNode = getDomNodeOrDie();
    } catch (IllegalStateException e) {
      DomNode domNode;
      Context.throwAsScriptRuntimeEx(e);
      return ""; }
    DomNode domNode;
    return getInnerHTML(domNode);
  }
  




  protected String getInnerHTML(DomNode domNode)
  {
    StringBuilder buf = new StringBuilder();
    
    String tagName = getTagName();
    boolean isPlain = "SCRIPT".equals(tagName);
    
    isPlain = (isPlain) || ("STYLE".equals(tagName));
    

    printChildren(buf, domNode, !isPlain);
    return buf.toString();
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public String getInnerText()
  {
    StringBuilder buf = new StringBuilder();
    
    printChildren(buf, getDomNodeOrDie(), false);
    return buf.toString();
  }
  




  @JsxGetter
  public String getOuterHTML()
  {
    StringBuilder buf = new StringBuilder();
    
    printNode(buf, getDomNodeOrDie(), true);
    return buf.toString();
  }
  





  private void printChildren(StringBuilder builder, DomNode node, boolean html)
  {
    for (DomNode child : node.getChildren()) {
      printNode(builder, child, html);
    }
  }
  
  private void printNode(StringBuilder builder, DomNode node, boolean html) {
    if ((node instanceof DomComment)) {
      if (html)
      {
        String s = PRINT_NODE_PATTERN.matcher(node.getNodeValue()).replaceAll(" ");
        builder.append("<!--").append(s).append("-->");
      }
    }
    else if ((node instanceof DomCharacterData))
    {
      String s = node.getNodeValue();
      if (html) {
        s = StringUtils.escapeXmlChars(s);
      }
      builder.append(s);
    }
    else if (html) {
      DomElement element = (DomElement)node;
      Element scriptObject = (Element)node.getScriptableObject();
      String tag = element.getTagName();
      
      HTMLElement htmlElement = null;
      if ((scriptObject instanceof HTMLElement)) {
        htmlElement = (HTMLElement)scriptObject;
      }
      builder.append("<").append(tag);
      
      for (DomAttr attr : element.getAttributesMap().values())
        if (attr.getSpecified())
        {


          String name = attr.getName();
          String value = PRINT_NODE_QUOTE_PATTERN.matcher(attr.getValue()).replaceAll("&quot;");
          builder.append(' ').append(name).append("=");
          builder.append("\"");
          builder.append(value);
          builder.append("\"");
        }
      builder.append(">");
      
      boolean isHtml = (html) && 
        (!(scriptObject instanceof HTMLScriptElement)) && 
        (!(scriptObject instanceof HTMLStyleElement));
      printChildren(builder, node, isHtml);
      if ((htmlElement == null) || (!htmlElement.isEndTagForbidden())) {
        builder.append("</").append(tag).append(">");
      }
    }
    else {
      HtmlElement element = (HtmlElement)node;
      if ("p".equals(element.getTagName())) {
        if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_INNER_TEXT_CR_NL)) {
          builder.append("\r\n");
        }
        else {
          int i = builder.length() - 1;
          while ((i >= 0) && (Character.isWhitespace(builder.charAt(i)))) {
            i--;
          }
          builder.setLength(i + 1);
          builder.append("\n");
        }
      }
      if (!"script".equals(element.getTagName())) {
        printChildren(builder, node, html);
      }
    }
  }
  



  @JsxSetter
  public void setInnerHTML(Object value)
  {
    try
    {
      domNode = getDomNodeOrDie();
    } catch (IllegalStateException e) {
      DomNode domNode;
      Context.throwAsScriptRuntimeEx(e); return;
    }
    
    DomNode domNode;
    domNode.removeAllChildren();
    
    boolean addChildForNull = getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_INNER_HTML_ADD_CHILD_FOR_NULL_VALUE);
    if (((value == null) && (addChildForNull)) || ((value != null) && (!"".equals(value))))
    {
      String valueAsString = Context.toString(value);
      parseHtmlSnippet(domNode, valueAsString);
    }
  }
  

  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public void setInnerText(Object value)
  {
    String valueString;
    
    String valueString;
    if ((value == null) && (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_INNER_TEXT_VALUE_NULL))) {
      valueString = null;
    }
    else {
      valueString = Context.toString(value);
    }
    setInnerTextImpl(valueString);
  }
  



  protected void setInnerTextImpl(String value)
  {
    DomNode domNode = getDomNodeOrDie();
    
    domNode.removeAllChildren();
    
    if ((value != null) && (!value.isEmpty())) {
      domNode.appendChild(new DomText(domNode.getPage(), value));
    }
  }
  




  public void setTextContent(Object value)
  {
    setInnerTextImpl(value == null ? null : Context.toString(value));
  }
  



  @JsxSetter
  public void setOuterHTML(Object value)
  {
    DomNode domNode = getDomNodeOrDie();
    DomNode parent = domNode.getParentNode();
    if (parent == null) {
      if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_OUTER_HTML_REMOVES_CHILDREN_FOR_DETACHED)) {
        domNode.removeAllChildren();
      }
      if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_OUTER_HTML_THROWS_FOR_DETACHED)) {
        throw Context.reportRuntimeError("outerHTML is readonly for detached nodes");
      }
      return;
    }
    
    if ((value == null) && (!getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_OUTER_HTML_NULL_AS_STRING))) {
      domNode.remove();
      return;
    }
    String valueStr = Context.toString(value);
    if (valueStr.isEmpty()) {
      domNode.remove();
      return;
    }
    
    DomNode nextSibling = domNode.getNextSibling();
    domNode.remove();
    boolean append;
    DomNode target;
    boolean append;
    if (nextSibling != null) {
      DomNode target = nextSibling;
      append = false;
    }
    else {
      target = parent;
      append = true;
    }
    
    DomNode proxyDomNode = new ProxyDomNode(target.getPage(), target, append);
    parseHtmlSnippet(proxyDomNode, valueStr);
  }
  



  private static void parseHtmlSnippet(DomNode target, String source)
  {
    try
    {
      HTMLParser.parseFragment(target, source);
    }
    catch (IOException e) {
      LogFactory.getLog(HtmlElement.class).error("Unexpected exception occurred while parsing HTML snippet", e);
      throw Context.reportRuntimeError("Unexpected exception occurred while parsing HTML snippet: " + 
        e.getMessage());
    }
    catch (SAXException e) {
      LogFactory.getLog(HtmlElement.class).error("Unexpected exception occurred while parsing HTML snippet", e);
      throw Context.reportRuntimeError("Unexpected exception occurred while parsing HTML snippet: " + 
        e.getMessage());
    }
  }
  



  public static class ProxyDomNode
    extends HtmlDivision
  {
    private final DomNode target_;
    

    private final boolean append_;
    


    public ProxyDomNode(SgmlPage page, DomNode target, boolean append)
    {
      super(page, null);
      target_ = target;
      append_ = append;
    }
    



    public DomNode appendChild(org.w3c.dom.Node node)
    {
      DomNode domNode = (DomNode)node;
      if (append_) {
        return target_.appendChild(domNode);
      }
      target_.insertBefore(domNode);
      return domNode;
    }
    



    public DomNode getDomNode()
    {
      return target_;
    }
    



    public boolean isAppend()
    {
      return append_;
    }
  }
  












  @JsxFunction
  public void insertAdjacentHTML(String position, String text)
  {
    Object[] values = getInsertAdjacentLocation(position);
    DomNode domNode = (DomNode)values[0];
    boolean append = ((Boolean)values[1]).booleanValue();
    

    DomNode proxyDomNode = new ProxyDomNode(domNode.getPage(), domNode, append);
    parseHtmlSnippet(proxyDomNode, text);
  }
  








  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public Object insertAdjacentElement(String where, Object insertedElement)
  {
    if ((insertedElement instanceof com.gargoylesoftware.htmlunit.javascript.host.dom.Node)) {
      DomNode childNode = ((com.gargoylesoftware.htmlunit.javascript.host.dom.Node)insertedElement).getDomNodeOrDie();
      Object[] values = getInsertAdjacentLocation(where);
      DomNode node = (DomNode)values[0];
      boolean append = ((Boolean)values[1]).booleanValue();
      
      if (append) {
        node.appendChild(childNode);
      }
      else {
        node.insertBefore(childNode);
      }
      return insertedElement;
    }
    throw Context.reportRuntimeError("Passed object is not an element: " + insertedElement);
  }
  







  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void insertAdjacentText(String where, String text)
  {
    Object[] values = getInsertAdjacentLocation(where);
    DomNode node = (DomNode)values[0];
    boolean append = ((Boolean)values[1]).booleanValue();
    
    DomText domText = new DomText(node.getPage(), text);
    
    if (append) {
      node.appendChild(domText);
    }
    else {
      node.insertBefore(domText);
    }
  }
  








  private Object[] getInsertAdjacentLocation(String where)
  {
    DomNode currentNode = getDomNodeOrDie();
    

    boolean append;
    
    if ("afterbegin".equalsIgnoreCase(where)) { boolean append;
      if (currentNode.getFirstChild() == null)
      {
        DomNode node = currentNode;
        append = true;
      }
      else
      {
        DomNode node = currentNode.getFirstChild();
        append = false;
      }
    } else { boolean append;
      if ("beforebegin".equalsIgnoreCase(where))
      {
        DomNode node = currentNode;
        append = false;
      } else { boolean append;
        if ("beforeend".equalsIgnoreCase(where))
        {
          DomNode node = currentNode;
          append = true;
        } else { boolean append;
          if ("afterend".equalsIgnoreCase(where)) { boolean append;
            if (currentNode.getNextSibling() == null)
            {
              DomNode node = currentNode.getParentNode();
              append = true;
            }
            else
            {
              DomNode node = currentNode.getNextSibling();
              append = false;
            }
          }
          else {
            throw Context.reportRuntimeError("Illegal position value: \"" + where + "\""); } } } }
    boolean append;
    DomNode node;
    if (append) {
      return new Object[] { node, Boolean.TRUE };
    }
    return new Object[] { node, Boolean.FALSE };
  }
  











  public int addBehavior(String behavior)
  {
    if (behaviors_.contains(behavior)) {
      return 0;
    }
    
    Class<? extends HTMLElement> c = getClass();
    if ("#default#clientCaps".equalsIgnoreCase(behavior)) {
      defineProperty("availHeight", c, 0);
      defineProperty("availWidth", c, 0);
      defineProperty("bufferDepth", c, 0);
      defineProperty("colorDepth", c, 0);
      defineProperty("connectionType", c, 0);
      defineProperty("cookieEnabled", c, 0);
      defineProperty("cpuClass", c, 0);
      defineProperty("height", c, 0);
      defineProperty("javaEnabled", c, 0);
      defineProperty("platform", c, 0);
      defineProperty("systemLanguage", c, 0);
      defineProperty("userLanguage", c, 0);
      defineProperty("width", c, 0);
      defineFunctionProperties(new String[] { "addComponentRequest" }, c, 0);
      defineFunctionProperties(new String[] { "clearComponentRequest" }, c, 0);
      defineFunctionProperties(new String[] { "compareVersions" }, c, 0);
      defineFunctionProperties(new String[] { "doComponentRequest" }, c, 0);
      defineFunctionProperties(new String[] { "getComponentVersion" }, c, 0);
      defineFunctionProperties(new String[] { "isComponentInstalled" }, c, 0);
      behaviors_.add("#default#clientCaps");
      return 0;
    }
    if ("#default#homePage".equalsIgnoreCase(behavior)) {
      defineFunctionProperties(new String[] { "isHomePage" }, c, 0);
      defineFunctionProperties(new String[] { "setHomePage" }, c, 0);
      defineFunctionProperties(new String[] { "navigateHomePage" }, c, 0);
      behaviors_.add("#default#clientCaps");
      return 1;
    }
    if ("#default#download".equalsIgnoreCase(behavior)) {
      defineFunctionProperties(new String[] { "startDownload" }, c, 0);
      behaviors_.add("#default#download");
      return 2;
    }
    
    LOG.warn("Unimplemented behavior: " + behavior);
    return -1;
  }
  




  public void removeBehavior(int id)
  {
    switch (id) {
    case 0: 
      delete("availHeight");
      delete("availWidth");
      delete("bufferDepth");
      delete("colorDepth");
      delete("connectionType");
      delete("cookieEnabled");
      delete("cpuClass");
      delete("height");
      delete("javaEnabled");
      delete("platform");
      delete("systemLanguage");
      delete("userLanguage");
      delete("width");
      delete("addComponentRequest");
      delete("clearComponentRequest");
      delete("compareVersions");
      delete("doComponentRequest");
      delete("getComponentVersion");
      delete("isComponentInstalled");
      behaviors_.remove("#default#clientCaps");
      break;
    case 1: 
      delete("isHomePage");
      delete("setHomePage");
      delete("navigateHomePage");
      behaviors_.remove("#default#homePage");
      break;
    case 2: 
      delete("startDownload");
      behaviors_.remove("#default#download");
      break;
    default: 
      LOG.warn("Unexpected behavior id: " + id + ". Ignoring.");
    }
    
  }
  





  public int getAvailHeight()
  {
    return getWindow().getScreen().getAvailHeight();
  }
  




  public int getAvailWidth()
  {
    return getWindow().getScreen().getAvailWidth();
  }
  




  public int getBufferDepth()
  {
    return getWindow().getScreen().getBufferDepth();
  }
  




  public int getColorDepth()
  {
    return getWindow().getScreen().getColorDepth();
  }
  





  public String getConnectionType()
  {
    return "modem";
  }
  




  public boolean getCookieEnabled()
  {
    return getWindow().getNavigator().getCookieEnabled();
  }
  




  public String getCpuClass()
  {
    return getWindow().getNavigator().getCpuClass();
  }
  




  public int getHeight()
  {
    return getWindow().getScreen().getHeight();
  }
  




  public boolean getJavaEnabled()
  {
    return getWindow().getNavigator().javaEnabled();
  }
  




  public String getPlatform()
  {
    return getWindow().getNavigator().getPlatform();
  }
  




  public String getSystemLanguage()
  {
    return getWindow().getNavigator().getSystemLanguage();
  }
  




  public String getUserLanguage()
  {
    return getWindow().getNavigator().getUserLanguage();
  }
  




  public int getWidth()
  {
    return getWindow().getScreen().getWidth();
  }
  







  public void addComponentRequest(String id, String idType, String minVersion)
  {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Call to addComponentRequest(" + id + ", " + idType + ", " + minVersion + ") ignored.");
    }
  }
  




  public void clearComponentRequest()
  {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Call to clearComponentRequest() ignored.");
    }
  }
  






  public int compareVersions(String v1, String v2)
  {
    int i = v1.compareTo(v2);
    if (i == 0) {
      return 0;
    }
    if (i < 0) {
      return -1;
    }
    
    return 1;
  }
  





  public boolean doComponentRequest()
  {
    return false;
  }
  





  public String getComponentVersion(String id, String idType)
  {
    if ("{E5D12C4E-7B4F-11D3-B5C9-0050045C3C96}".equals(id))
    {
      return "";
    }
    
    return "1.0";
  }
  






  public boolean isComponentInstalled(String id, String idType, String minVersion)
  {
    return false;
  }
  







  public void startDownload(String uri, Function callback)
    throws MalformedURLException
  {
    HtmlPage page = (HtmlPage)getWindow().getWebWindow().getEnclosedPage();
    URL url = page.getFullyQualifiedUrl(uri);
    if (!page.getUrl().getHost().equals(url.getHost())) {
      throw Context.reportRuntimeError("Not authorized url: " + url);
    }
    JavaScriptJob job = BackgroundJavaScriptFactory.theFactory()
      .createDownloadBehaviorJob(url, callback, getWindow().getWebWindow().getWebClient());
    page.getEnclosingWindow().getJobManager().addJob(job, page);
  }
  










  public boolean isHomePage(String url)
  {
    try
    {
      URL newUrl = new URL(url);
      URL currentUrl = getDomNodeOrDie().getPage().getUrl();
      String home = getDomNodeOrDie().getPage().getEnclosingWindow()
        .getWebClient().getOptions().getHomePage();
      boolean sameDomains = newUrl.getHost().equalsIgnoreCase(currentUrl.getHost());
      boolean isHomePage = (home != null) && (home.equals(url));
      return (sameDomains) && (isHomePage);
    }
    catch (MalformedURLException e) {}
    return false;
  }
  





  public void setHomePage(String url)
  {
    getDomNodeOrDie().getPage().getEnclosingWindow().getWebClient().getOptions().setHomePage(url);
  }
  



  public void navigateHomePage()
    throws IOException
  {
    WebClient webClient = getDomNodeOrDie().getPage().getEnclosingWindow().getWebClient();
    webClient.getPage(webClient.getOptions().getHomePage());
  }
  









  @JsxGetter
  public int getOffsetHeight()
  {
    if ((isDisplayNone()) || (!getDomNodeOrDie().isAttachedToPage())) {
      return 0;
    }
    MouseEvent event = MouseEvent.getCurrentMouseEvent();
    if (isAncestorOfEventTarget(event))
    {
      return event.getClientY() - getPosY() + 50;
    }
    ComputedCSSStyleDeclaration style = getWindow().getComputedStyle(this, null);
    return style.getCalculatedHeight(true, true);
  }
  



  protected final boolean isDisplayNone()
  {
    HTMLElement element = this;
    while (element != null) {
      CSSStyleDeclaration style = element.getWindow().getComputedStyle(element, null);
      String display = style.getDisplay();
      if (HtmlElement.DisplayStyle.NONE.value().equals(display)) {
        return true;
      }
      element = element.getParentHTMLElement();
    }
    return false;
  }
  







  @JsxGetter
  public int getOffsetWidth()
  {
    if ((isDisplayNone()) || (!getDomNodeOrDie().isAttachedToPage())) {
      return 0;
    }
    
    MouseEvent event = MouseEvent.getCurrentMouseEvent();
    if (isAncestorOfEventTarget(event))
    {
      return event.getClientX() - getPosX() + 50;
    }
    ComputedCSSStyleDeclaration style = getWindow().getComputedStyle(this, null);
    return style.getCalculatedWidth(true, true);
  }
  




  protected boolean isAncestorOfEventTarget(MouseEvent event)
  {
    if (event == null) {
      return false;
    }
    if (!(event.getSrcElement() instanceof HTMLElement)) {
      return false;
    }
    HTMLElement srcElement = (HTMLElement)event.getSrcElement();
    return getDomNodeOrDie().isAncestorOf(srcElement.getDomNodeOrDie());
  }
  



  public String toString()
  {
    return "HTMLElement for " + getDomNodeOrNull();
  }
  






  @JsxGetter
  public int getScrollTop()
  {
    if (scrollTop_ < 0) {
      scrollTop_ = 0;
    }
    else if (scrollTop_ > 0) {
      ComputedCSSStyleDeclaration style = getWindow().getComputedStyle(this, null);
      if (!style.isScrollable(false)) {
        scrollTop_ = 0;
      }
    }
    return scrollTop_;
  }
  



  @JsxSetter
  public void setScrollTop(int scroll)
  {
    scrollTop_ = scroll;
  }
  






  @JsxGetter
  public int getScrollLeft()
  {
    if (scrollLeft_ < 0) {
      scrollLeft_ = 0;
    }
    else if (scrollLeft_ > 0) {
      ComputedCSSStyleDeclaration style = getWindow().getComputedStyle(this, null);
      if (!style.isScrollable(true)) {
        scrollLeft_ = 0;
      }
    }
    return scrollLeft_;
  }
  



  @JsxSetter
  public void setScrollLeft(int scroll)
  {
    scrollLeft_ = scroll;
  }
  




  @JsxGetter
  public int getScrollHeight()
  {
    return getClientHeight();
  }
  




  @JsxGetter
  public int getScrollWidth()
  {
    return getClientWidth();
  }
  




  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void setTagUrn(String tagUrn)
  {
    throw Context.reportRuntimeError("Error trying to set tagUrn to '" + tagUrn + "'.");
  }
  





  public HTMLElement getParentHTMLElement()
  {
    com.gargoylesoftware.htmlunit.javascript.host.dom.Node parent = getParent();
    while ((parent != null) && (!(parent instanceof HTMLElement))) {
      parent = parent.getParent();
    }
    return (HTMLElement)parent;
  }
  





  @JsxFunction
  public void scrollIntoView() {}
  




  @JsxFunction
  public Object getClientRects()
  {
    ClientRectList rectList = new ClientRectList();
    rectList.setParentScope(getWindow());
    rectList.setPrototype(getPrototype(rectList.getClass()));
    
    if ((!isDisplayNone()) && (getDomNodeOrDie().isAttachedToPage())) {
      ClientRect rect = new ClientRect(0, 0, 1, 1);
      rect.setParentScope(getWindow());
      rect.setPrototype(getPrototype(rect.getClass()));
      rectList.add(rect);
    }
    
    return rectList;
  }
  




  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public String getUniqueID()
  {
    if (uniqueID_ == null) {
      uniqueID_ = ("ms__id" + UniqueID_Counter_++);
    }
    return uniqueID_;
  }
  



  public HtmlElement getDomNodeOrDie()
  {
    return (HtmlElement)super.getDomNodeOrDie();
  }
  



  public HtmlElement getDomNodeOrNull()
  {
    return (HtmlElement)super.getDomNodeOrNull();
  }
  



  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void blur()
  {
    super.blur();
  }
  



  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public Object createTextRange()
  {
    TextRange range = new TextRange(this);
    range.setParentScope(getParentScope());
    range.setPrototype(getPrototype(range.getClass()));
    return range;
  }
  


  @JsxFunction
  public void focus()
  {
    HtmlElement domNode = getDomNodeOrDie();
    if ((domNode instanceof SubmittableElement)) {
      domNode.focus();
    }
  }
  





  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void setActive()
  {
    Window window = getWindow();
    HTMLDocument document = (HTMLDocument)window.getDocument();
    document.setActiveElement(this);
    if (window.getWebWindow() == window.getWebWindow().getWebClient().getCurrentWindow()) {
      HtmlElement element = getDomNodeOrDie();
      ((HtmlPage)element.getPage()).setFocusedElement(element);
    }
  }
  



  public String getNodeName()
  {
    DomNode domNode = getDomNodeOrDie();
    String nodeName = domNode.getNodeName();
    if (domNode.getHtmlPageOrNull() != null) {
      nodeName = nodeName.toUpperCase(Locale.ROOT);
    }
    return nodeName;
  }
  



  public String getPrefix()
  {
    return null;
  }
  


  @JsxFunction
  public void click()
    throws IOException
  {
    getDomNodeOrDie().click();
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public boolean getSpellcheck()
  {
    return Context.toBoolean(getDomNodeOrDie().getAttribute("spellcheck"));
  }
  



  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public void setSpellcheck(boolean spellcheck)
  {
    getDomNodeOrDie().setAttribute("spellcheck", Boolean.toString(spellcheck));
  }
  



  @JsxGetter
  public String getLang()
  {
    return getDomNodeOrDie().getAttribute("lang");
  }
  



  @JsxSetter
  public void setLang(String lang)
  {
    getDomNodeOrDie().setAttribute("lang", lang);
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public String getLanguage()
  {
    return getDomNodeOrDie().getAttribute("language");
  }
  



  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void setLanguage(String language)
  {
    getDomNodeOrDie().setAttribute("language", language);
  }
  



  @JsxGetter
  public String getDir()
  {
    return getDomNodeOrDie().getAttribute("dir");
  }
  



  @JsxSetter
  public void setDir(String dir)
  {
    getDomNodeOrDie().setAttribute("dir", dir);
  }
  



  @JsxGetter
  public int getTabIndex()
  {
    return (int)Context.toNumber(getDomNodeOrDie().getAttribute("tabindex"));
  }
  



  @JsxSetter
  public void setTabIndex(int tabIndex)
  {
    getDomNodeOrDie().setAttribute("tabindex", Integer.toString(tabIndex));
  }
  



  @JsxGetter
  public String getAccessKey()
  {
    return getDomNodeOrDie().getAttribute("accesskey");
  }
  



  @JsxSetter
  public void setAccessKey(String accessKey)
  {
    getDomNodeOrDie().setAttribute("accesskey", accessKey);
  }
  







  protected String getWidthOrHeight(String attributeName, Boolean returnNegativeValues)
  {
    String value = getDomNodeOrDie().getAttribute(attributeName);
    if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_WIDTH_HEIGHT_ACCEPTS_ARBITRARY_VALUES)) {
      return value;
    }
    if (!PERCENT_VALUE.matcher(value).matches()) {
      try {
        Float f = Float.valueOf(value);
        int i = f.intValue();
        if (i < 0) {
          if (returnNegativeValues == null) {
            value = "0";
          }
          else if (!returnNegativeValues.booleanValue()) {
            value = "";
          }
          else {
            value = Integer.toString(i);
          }
        }
        else {
          value = Integer.toString(i);
        }
      }
      catch (NumberFormatException e) {
        if (!getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_WIDTH_HEIGHT_ACCEPTS_ARBITRARY_VALUES)) {
          value = "";
        }
      }
    }
    return value;
  }
  








  protected void setWidthOrHeight(String attributeName, String value, boolean allowNegativeValues)
  {
    if ((!getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_WIDTH_HEIGHT_ACCEPTS_ARBITRARY_VALUES)) && (!value.isEmpty())) {
      if (value.endsWith("px")) {
        value = value.substring(0, value.length() - 2);
      }
      boolean error = false;
      if (!PERCENT_VALUE.matcher(value).matches()) {
        try {
          Float f = Float.valueOf(value);
          int i = f.intValue();
          if ((i < 0) && 
            (!allowNegativeValues)) {
            error = true;
          }
        }
        catch (NumberFormatException e)
        {
          error = true;
        }
      }
      if (error) {
        Exception e = new Exception("Cannot set the '" + attributeName + 
          "' property to invalid value: '" + value + "'");
        Context.throwAsScriptRuntimeEx(e);
      }
    }
    getDomNodeOrDie().setAttribute(attributeName, value);
  }
  




  protected void setColorAttribute(String name, String value)
  {
    String s = value;
    if (!s.isEmpty()) {
      boolean restrict = getBrowserVersion().hasFeature(BrowserVersionFeatures.HTML_COLOR_RESTRICT);
      
      boolean isName = false;
      if (restrict) {
        for (String key : COLORS_MAP_IE.keySet()) {
          if (key.equalsIgnoreCase(value)) {
            isName = true;
            break;
          }
        }
      }
      if ((!isName) && 
        (restrict)) {
        if (s.charAt(0) == '#') {
          s = s.substring(1);
        }
        StringBuilder builder = new StringBuilder(7);
        for (int x = 0; (x < 6) && (x < s.length()); x++) {
          char ch = s.charAt(x);
          if (((ch >= '0') && (ch <= '9')) || ((ch >= 'a') && (ch <= 'f')) || ((ch >= 'A') && (ch <= 'F'))) {
            builder.append(ch);
          }
          else {
            builder.append('0');
          }
        }
        builder.insert(0, '#');
        s = builder.toString();
      }
      
      if (getBrowserVersion().hasFeature(BrowserVersionFeatures.HTML_COLOR_TO_LOWER)) {
        s = s.toLowerCase(Locale.ROOT);
      }
    }
    getDomNodeOrDie().setAttribute(name, s);
  }
  





  protected String getAlign(boolean returnInvalidValues)
  {
    boolean acceptArbitraryValues = getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_ALIGN_ACCEPTS_ARBITRARY_VALUES);
    
    String align = getDomNodeOrDie().getAttribute("align");
    if ((returnInvalidValues) || (acceptArbitraryValues) || 
      ("center".equals(align)) || 
      ("justify".equals(align)) || 
      ("left".equals(align)) || 
      ("right".equals(align))) {
      return align;
    }
    return "";
  }
  





  protected void setAlign(String align, boolean ignoreIfNoError)
  {
    String alignLC = align.toLowerCase(Locale.ROOT);
    boolean acceptArbitraryValues = getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_ALIGN_ACCEPTS_ARBITRARY_VALUES);
    if ((acceptArbitraryValues) || 
      ("center".equals(alignLC)) || 
      ("justify".equals(alignLC)) || 
      ("left".equals(alignLC)) || 
      ("right".equals(alignLC)) || 
      ("bottom".equals(alignLC)) || 
      ("middle".equals(alignLC)) || 
      ("top".equals(alignLC))) {
      if (!ignoreIfNoError) {
        String newValue = acceptArbitraryValues ? align : alignLC;
        getDomNodeOrDie().setAttribute("align", newValue);
      }
      return;
    }
    
    throw Context.reportRuntimeError("Cannot set the align property to invalid value: '" + align + "'");
  }
  





  protected String getVAlign(String[] valid, String defaultValue)
  {
    String valign = getDomNodeOrDie().getAttribute("valign");
    if ((valid == null) || (ArrayUtils.contains(valid, valign))) {
      return valign;
    }
    return defaultValue;
  }
  




  protected void setVAlign(Object vAlign, String[] valid)
  {
    String s = Context.toString(vAlign).toLowerCase(Locale.ROOT);
    if ((valid == null) || (ArrayUtils.contains(valid, s))) {
      getDomNodeOrDie().setAttribute("valign", s);
    }
    else {
      throw Context.reportRuntimeError("Cannot set the vAlign property to invalid value: " + vAlign);
    }
  }
  



  protected String getCh()
  {
    return getDomNodeOrDie().getAttribute("char");
  }
  



  protected void setCh(String ch)
  {
    getDomNodeOrDie().setAttribute("char", ch);
  }
  



  protected String getChOff()
  {
    return getDomNodeOrDie().getAttribute("charOff");
  }
  


  protected void setChOff(String chOff)
  {
    try
    {
      float f = Float.parseFloat(chOff);
      int i = (int)f;
      if (i == f) {
        chOff = Integer.toString(i);
      }
      else {
        chOff = Float.toString(f);
      }
    }
    catch (NumberFormatException localNumberFormatException) {}
    

    getDomNodeOrDie().setAttribute("charOff", chOff);
  }
  








  @JsxGetter
  public int getOffsetLeft()
  {
    if ((this instanceof HTMLBodyElement)) {
      return 0;
    }
    
    int left = 0;
    HTMLElement offsetParent = getOffsetParent();
    

    DomNode node = getDomNodeOrDie();
    HTMLElement element = (HTMLElement)node.getScriptableObject();
    ComputedCSSStyleDeclaration style = element.getWindow().getComputedStyle(element, null);
    left += style.getLeft(true, false, false);
    

    String position = style.getPositionWithInheritance();
    if ("absolute".equals(position)) {
      return left;
    }
    

    node = node.getParentNode();
    while ((node != null) && (node.getScriptableObject() != offsetParent)) {
      if ((node.getScriptableObject() instanceof HTMLElement)) {
        element = (HTMLElement)node.getScriptableObject();
        style = element.getWindow().getComputedStyle(element, null);
        left += style.getLeft(true, true, true);
      }
      node = node.getParentNode();
    }
    
    if (offsetParent != null) {
      style = offsetParent.getWindow().getComputedStyle(offsetParent, null);
      left += style.getMarginLeftValue();
      left += style.getPaddingLeftValue();
    }
    
    return left;
  }
  



  public int getPosX()
  {
    int cumulativeOffset = 0;
    HTMLElement element = this;
    while (element != null) {
      cumulativeOffset += element.getOffsetLeft();
      if (element != this) {
        ComputedCSSStyleDeclaration style = element.getWindow().getComputedStyle(element, null);
        cumulativeOffset += style.getBorderLeftValue();
      }
      element = element.getOffsetParent();
    }
    return cumulativeOffset;
  }
  



  public int getPosY()
  {
    int cumulativeOffset = 0;
    HTMLElement element = this;
    while (element != null) {
      cumulativeOffset += element.getOffsetTop();
      if (element != this) {
        ComputedCSSStyleDeclaration style = element.getWindow().getComputedStyle(element, null);
        cumulativeOffset += style.getBorderTopValue();
      }
      element = element.getOffsetParent();
    }
    return cumulativeOffset;
  }
  



  private HTMLElement getOffsetParent()
  {
    Object offsetParent = getOffsetParentInternal(false);
    if ((offsetParent instanceof HTMLElement)) {
      return (HTMLElement)offsetParent;
    }
    return null;
  }
  



  @JsxGetter
  public int getClientLeft()
  {
    ComputedCSSStyleDeclaration style = getWindow().getComputedStyle(this, null);
    return style.getBorderLeftValue();
  }
  



  @JsxGetter
  public int getClientTop()
  {
    ComputedCSSStyleDeclaration style = getWindow().getComputedStyle(this, null);
    return style.getBorderTopValue();
  }
  








  @JsxGetter
  public int getOffsetTop()
  {
    if ((this instanceof HTMLBodyElement)) {
      return 0;
    }
    
    int top = 0;
    HTMLElement offsetParent = getOffsetParent();
    

    DomNode node = getDomNodeOrDie();
    HTMLElement element = (HTMLElement)node.getScriptableObject();
    ComputedCSSStyleDeclaration style = element.getWindow().getComputedStyle(element, null);
    top += style.getTop(true, false, false);
    

    String position = style.getPositionWithInheritance();
    if ("absolute".equals(position)) {
      return top;
    }
    

    node = node.getParentNode();
    while ((node != null) && (node.getScriptableObject() != offsetParent)) {
      if ((node.getScriptableObject() instanceof HTMLElement)) {
        element = (HTMLElement)node.getScriptableObject();
        style = element.getWindow().getComputedStyle(element, null);
        top += style.getTop(false, true, true);
      }
      node = node.getParentNode();
    }
    
    if (offsetParent != null) {
      HTMLElement thiz = (HTMLElement)getDomNodeOrDie().getScriptableObject();
      style = thiz.getWindow().getComputedStyle(thiz, null);
      boolean thisElementHasTopMargin = style.getMarginTopValue() != 0;
      
      style = offsetParent.getWindow().getComputedStyle(offsetParent, null);
      if (!thisElementHasTopMargin) {
        top += style.getMarginTopValue();
      }
      top += style.getPaddingTopValue();
    }
    
    return top;
  }
  











  @JsxGetter(propertyName="offsetParent")
  public Object getOffsetParent_js()
  {
    return getOffsetParentInternal(getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_OFFSET_PARENT_NULL_IF_FIXED));
  }
  
  private Object getOffsetParentInternal(boolean returnNullIfFixed) {
    DomNode currentElement = getDomNodeOrDie();
    
    if (currentElement.getParentNode() == null) {
      return null;
    }
    
    Object offsetParent = null;
    HTMLElement htmlElement = (HTMLElement)currentElement.getScriptableObject();
    if ((returnNullIfFixed) && 
      ("fixed".equals(htmlElement.getStyle().getStyleAttribute(StyleAttributes.Definition.POSITION, true)))) {
      return null;
    }
    
    ComputedCSSStyleDeclaration style = htmlElement.getWindow().getComputedStyle(htmlElement, null);
    String position = style.getPositionWithInheritance();
    boolean staticPos = "static".equals(position);
    
    boolean useTables = staticPos;
    
    while (currentElement != null)
    {
      DomNode parentNode = currentElement.getParentNode();
      if (((parentNode instanceof HtmlBody)) || 
        ((useTables) && ((parentNode instanceof HtmlTableDataCell))) || (
        (useTables) && ((parentNode instanceof HtmlTable)))) {
        offsetParent = parentNode.getScriptableObject();
        break;
      }
      
      if ((parentNode != null) && ((parentNode.getScriptableObject() instanceof HTMLElement))) {
        HTMLElement parentElement = (HTMLElement)parentNode.getScriptableObject();
        ComputedCSSStyleDeclaration parentStyle = 
          parentElement.getWindow().getComputedStyle(parentElement, null);
        String parentPosition = parentStyle.getPositionWithInheritance();
        boolean parentIsStatic = "static".equals(parentPosition);
        if (!parentIsStatic) {
          offsetParent = parentNode.getScriptableObject();
          break;
        }
      }
      
      currentElement = currentElement.getParentNode();
    }
    
    return offsetParent;
  }
  



  public ClientRect getBoundingClientRect()
  {
    ClientRect textRectangle = super.getBoundingClientRect();
    
    int left = getPosX();
    int top = getPosY();
    

    Object parentNode = getOffsetParentInternal(false);
    while ((parentNode != null) && 
      ((parentNode instanceof HTMLElement)) && 
      (!(parentNode instanceof HTMLBodyElement))) {
      HTMLElement elem = (HTMLElement)parentNode;
      left -= elem.getScrollLeft();
      top -= elem.getScrollTop();
      
      parentNode = elem.getParentNode();
    }
    
    textRectangle.setBottom(top + getOffsetHeight());
    textRectangle.setLeft(left);
    textRectangle.setRight(left + getOffsetWidth());
    textRectangle.setTop(top);
    
    return textRectangle;
  }
  




  @JsxGetter
  public DOMTokenList getClassList()
  {
    return new DOMTokenList(this, "class");
  }
  



  @JsxFunction
  public boolean hasAttribute(String name)
  {
    return super.hasAttribute(name);
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public HTMLCollection getChildren()
  {
    return super.getChildren();
  }
  



  @JsxGetter
  public Element getParentElement()
  {
    return super.getParentElement();
  }
  




  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public DOMStringMap getDataset()
  {
    return new DOMStringMap(this);
  }
  




  protected boolean isEndTagForbidden()
  {
    return endTagForbidden_;
  }
  




  protected boolean isLowerCaseInOuterHtml()
  {
    return false;
  }
  



  @JsxSetter
  public void setOnchange(Object onchange)
  {
    setEventHandlerProp("onchange", onchange);
  }
  



  @JsxGetter
  public Function getOnchange()
  {
    return getEventHandler("onchange");
  }
  



  @JsxGetter
  public Object getOnsubmit()
  {
    return getEventHandlerProp("onsubmit");
  }
  



  @JsxSetter
  public void setOnsubmit(Object onsubmit)
  {
    setEventHandlerProp("onsubmit", onsubmit);
  }
  





  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void setCapture(boolean retargetToElement) {}
  





  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public boolean releaseCapture()
  {
    return true;
  }
  



  @JsxGetter
  public String getContentEditable()
  {
    String attribute = getDomNodeOrDie().getAttribute("contentEditable");
    if (attribute == DomElement.ATTRIBUTE_NOT_DEFINED) {
      return "inherit";
    }
    if (attribute == DomElement.ATTRIBUTE_VALUE_EMPTY) {
      return "true";
    }
    return attribute;
  }
  



  @JsxSetter
  public void setContentEditable(String contentEditable)
  {
    getDomNodeOrDie().setAttribute("contentEditable", contentEditable);
  }
  



  @JsxGetter
  public boolean getIsContentEditable()
  {
    String attribute = getContentEditable();
    if ("true".equals(attribute)) {
      return true;
    }
    if ("inherit".equals(attribute)) {
      DomNode parent = getDomNodeOrDie().getParentNode();
      if (parent != null) {
        Object parentScriptable = parent.getScriptableObject();
        if ((parentScriptable instanceof HTMLElement)) {
          return ((HTMLElement)parentScriptable).getIsContentEditable();
        }
      }
    }
    return false;
  }
}
