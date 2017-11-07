package com.gargoylesoftware.htmlunit.javascript.host.css;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.Cache;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.DisabledElement;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlLink;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlStyle;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.host.Element;
import com.gargoylesoftware.htmlunit.javascript.host.Screen;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;
import com.gargoylesoftware.htmlunit.util.UrlUtils;
import com.steadystate.css.dom.CSSImportRuleImpl;
import com.steadystate.css.dom.CSSMediaRuleImpl;
import com.steadystate.css.dom.CSSRuleListImpl;
import com.steadystate.css.dom.CSSStyleRuleImpl;
import com.steadystate.css.dom.CSSStyleSheetImpl;
import com.steadystate.css.dom.CSSValueImpl;
import com.steadystate.css.dom.MediaListImpl;
import com.steadystate.css.dom.Property;
import com.steadystate.css.parser.CSSOMParser;
import com.steadystate.css.parser.SACMediaListImpl;
import com.steadystate.css.parser.SACParserCSS3;
import com.steadystate.css.parser.SelectorListImpl;
import com.steadystate.css.parser.media.MediaQuery;
import com.steadystate.css.parser.selectors.GeneralAdjacentSelectorImpl;
import com.steadystate.css.parser.selectors.PrefixAttributeConditionImpl;
import com.steadystate.css.parser.selectors.PseudoClassConditionImpl;
import com.steadystate.css.parser.selectors.SubstringAttributeConditionImpl;
import com.steadystate.css.parser.selectors.SuffixAttributeConditionImpl;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.css.sac.AttributeCondition;
import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.CSSParseException;
import org.w3c.css.sac.CombinatorCondition;
import org.w3c.css.sac.Condition;
import org.w3c.css.sac.ConditionalSelector;
import org.w3c.css.sac.ContentCondition;
import org.w3c.css.sac.DescendantSelector;
import org.w3c.css.sac.ElementSelector;
import org.w3c.css.sac.ErrorHandler;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.LangCondition;
import org.w3c.css.sac.NegativeCondition;
import org.w3c.css.sac.NegativeSelector;
import org.w3c.css.sac.SACMediaList;
import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SelectorList;
import org.w3c.css.sac.SiblingSelector;
import org.w3c.css.sac.SimpleSelector;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSCharsetRule;
import org.w3c.dom.css.CSSImportRule;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSValue;
import org.w3c.dom.stylesheets.MediaList;



































@JsxClass
public class CSSStyleSheet
  extends StyleSheet
{
  private static final Log LOG = LogFactory.getLog(CSSStyleSheet.class);
  private static final Pattern NTH_NUMERIC = Pattern.compile("\\d+");
  private static final Pattern NTH_COMPLEX = Pattern.compile("[+-]?\\d*n\\w*([+-]\\w\\d*)?");
  private static final Pattern UNESCAPE_SELECTOR = Pattern.compile("\\\\([\\[\\]\\.:])");
  

  private final org.w3c.dom.css.CSSStyleSheet wrapped_;
  

  private final HTMLElement ownerNode_;
  

  private CSSRuleList cssRules_;
  
  private List<Integer> cssRulesIndexFix_;
  
  private final Map<CSSImportRule, CSSStyleSheet> imports_ = new HashMap();
  

  private String uri_;
  
  private boolean enabled_ = true;
  
  private static final Set<String> CSS2_PSEUDO_CLASSES = new HashSet(Arrays.asList(new String[] {
    "link", "visited", "hover", "active", 
    "focus", "lang", "first-child" }));
  
  private static final Set<String> CSS3_PSEUDO_CLASSES = new HashSet(Arrays.asList(new String[] {
    "checked", "disabled", "enabled", "indeterminated", "root", "target", "not()", 
    "nth-child()", "nth-last-child()", "nth-of-type()", "nth-last-of-type()", 
    "last-child", "first-of-type", "last-of-type", "only-child", "only-of-type", "empty", 
    "optional", "required" }));
  
  static {
    CSS3_PSEUDO_CLASSES.addAll(CSS2_PSEUDO_CLASSES);
  }
  


  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public CSSStyleSheet()
  {
    wrapped_ = new CSSStyleSheetImpl();
    ownerNode_ = null;
  }
  





  public CSSStyleSheet(HTMLElement element, InputSource source, String uri)
  {
    setParentScope(element.getWindow());
    setPrototype(getPrototype(CSSStyleSheet.class));
    if (source != null) {
      source.setURI(uri);
    }
    wrapped_ = parseCSS(source);
    uri_ = uri;
    ownerNode_ = element;
  }
  





  public CSSStyleSheet(HTMLElement element, org.w3c.dom.css.CSSStyleSheet wrapped, String uri)
  {
    setParentScope(element.getWindow());
    setPrototype(getPrototype(CSSStyleSheet.class));
    wrapped_ = wrapped;
    uri_ = uri;
    ownerNode_ = element;
  }
  



  public org.w3c.dom.css.CSSStyleSheet getWrappedSheet()
  {
    return wrapped_;
  }
  









  public void modifyIfNecessary(ComputedCSSStyleDeclaration style, Element element, String pseudoElement)
  {
    org.w3c.dom.css.CSSRuleList rules = getWrappedSheet().getCssRules();
    modifyIfNecessary(style, element, pseudoElement, rules, new HashSet());
  }
  
  private void modifyIfNecessary(ComputedCSSStyleDeclaration style, Element element, String pseudoElement, org.w3c.dom.css.CSSRuleList rules, Set<String> alreadyProcessing)
  {
    if (rules == null) {
      return;
    }
    
    BrowserVersion browser = getBrowserVersion();
    DomElement e = element.getDomNodeOrDie();
    int rulesLength = rules.getLength();
    for (int i = 0; i < rulesLength; i++) {
      org.w3c.dom.css.CSSRule rule = rules.item(i);
      
      short ruleType = rule.getType();
      if (1 == ruleType) {
        CSSStyleRuleImpl styleRule = (CSSStyleRuleImpl)rule;
        SelectorList selectors = styleRule.getSelectors();
        for (int j = 0; j < selectors.getLength(); j++) {
          Selector selector = selectors.item(j);
          boolean selected = selects(browser, selector, e, pseudoElement, false);
          if (selected) {
            CSSStyleDeclaration dec = styleRule.getStyle();
            style.applyStyleFromSelector(dec, selector);
          }
        }
      }
      else if (3 == ruleType) {
        CSSImportRuleImpl importRule = (CSSImportRuleImpl)rule;
        MediaList mediaList = importRule.getMedia();
        if (isActive(this, mediaList)) {
          CSSStyleSheet sheet = (CSSStyleSheet)imports_.get(importRule);
          if (sheet == null)
          {
            String uri = uri_ != null ? uri_ : e.getPage().getUrl().toExternalForm();
            String href = importRule.getHref();
            String url = UrlUtils.resolveUrl(uri, href);
            sheet = loadStylesheet(getWindow(), ownerNode_, null, url);
            imports_.put(importRule, sheet);
          }
          
          if (!alreadyProcessing.contains(sheet.getUri())) {
            org.w3c.dom.css.CSSRuleList sheetRules = sheet.getWrappedSheet().getCssRules();
            alreadyProcessing.add(getUri());
            sheet.modifyIfNecessary(style, element, pseudoElement, sheetRules, alreadyProcessing);
          }
        }
      }
      else if (4 == ruleType) {
        CSSMediaRuleImpl mediaRule = (CSSMediaRuleImpl)rule;
        MediaList mediaList = mediaRule.getMedia();
        if (isActive(this, mediaList)) {
          org.w3c.dom.css.CSSRuleList internalRules = mediaRule.getCssRules();
          modifyIfNecessary(style, element, pseudoElement, internalRules, alreadyProcessing);
        }
      }
    }
  }
  









  public static CSSStyleSheet loadStylesheet(Window window, HTMLElement element, HtmlLink link, String url)
  {
    HtmlPage page = (HtmlPage)element.getDomNodeOrDie().getPage();
    String uri = page.getUrl().toExternalForm();
    

    try
    {
      WebClient client = page.getWebClient();
      WebResponse response; WebRequest request; WebResponse response; if (link != null)
      {
        WebRequest request = link.getWebRequest();
        
        if (element.getBrowserVersion().hasFeature(BrowserVersionFeatures.HTMLLINK_CHECK_TYPE_FOR_STYLESHEET)) {
          String type = link.getTypeAttribute();
          if ((StringUtils.isNotBlank(type)) && (!"text/css".equals(type))) {
            InputSource source = new InputSource(new StringReader(""));
            return new CSSStyleSheet(element, source, uri);
          }
        }
        



        response = link.getWebResponse(true, request);
      }
      else
      {
        String accept = client.getBrowserVersion().getCssAcceptHeader();
        request = new WebRequest(new URL(url), accept);
        String referer = page.getUrl().toExternalForm();
        request.setAdditionalHeader("Referer", referer);
        



        response = client.loadWebResponse(request);
      }
      


      Cache cache = client.getCache();
      Object fromCache = cache.getCachedObject(request);
      CSSStyleSheet sheet; if ((fromCache != null) && ((fromCache instanceof org.w3c.dom.css.CSSStyleSheet))) {
        uri = request.getUrl().toExternalForm();
        sheet = new CSSStyleSheet(element, (org.w3c.dom.css.CSSStyleSheet)fromCache, uri);
      }
      else {
        uri = response.getWebRequest().getUrl().toExternalForm();
        client.printContentIfNecessary(response);
        client.throwFailingHttpStatusCodeExceptionIfNecessary(response);
        

        InputSource source = new InputSource();
        String contentType = response.getContentType();
        if ((StringUtils.isEmpty(contentType)) || ("text/css".equals(contentType))) {
          source.setByteStream(response.getContentAsStream());
          source.setEncoding(response.getContentCharset().name());
        }
        else {
          source.setCharacterStream(new StringReader(""));
        }
        CSSStyleSheet sheet = new CSSStyleSheet(element, source, uri);
        

        if (!cache.cacheIfPossible(request, response, sheet.getWrappedSheet())) {
          response.cleanUp();
        }
      }
    }
    catch (FailingHttpStatusCodeException e)
    {
      LOG.error("Exception loading " + uri, e);
      InputSource source = new InputSource(new StringReader(""));
      sheet = new CSSStyleSheet(element, source, uri);
    }
    catch (IOException e) {
      CSSStyleSheet sheet;
      LOG.error("IOException loading " + uri, e);
      InputSource source = new InputSource(new StringReader(""));
      sheet = new CSSStyleSheet(element, source, uri);
    }
    catch (RuntimeException e) {
      CSSStyleSheet sheet;
      LOG.error("RuntimeException loading " + uri, e);
      throw Context.reportRuntimeError("Exception: " + e);
    }
    catch (Exception e)
    {
      LOG.error("Exception loading " + uri, e);
      throw Context.reportRuntimeError("Exception: " + e); }
    CSSStyleSheet sheet;
    return sheet;
  }
  










  public static boolean selects(BrowserVersion browserVersion, Selector selector, DomElement element, String pseudoElement, boolean fromQuerySelectorAll)
  {
    switch (selector.getSelectorType()) {
    case 1: 
      if ((selector instanceof GeneralAdjacentSelectorImpl)) {
        SiblingSelector ss = (SiblingSelector)selector;
        Selector ssSelector = ss.getSelector();
        SimpleSelector ssSiblingSelector = ss.getSiblingSelector();
        for (DomNode prev = element.getPreviousSibling(); prev != null; prev = prev.getPreviousSibling()) {
          if ((prev instanceof HtmlElement))
          {
            if (selects(browserVersion, ssSelector, (HtmlElement)prev, pseudoElement, fromQuerySelectorAll))
            {
              if (selects(browserVersion, ssSiblingSelector, element, pseudoElement, fromQuerySelectorAll))
                return true; }
          }
        }
        return false;
      }
      
      return true;
    case 11: 
      DomNode parentNode = element.getParentNode();
      if (parentNode == element.getPage()) {
        return false;
      }
      if (!(parentNode instanceof HtmlElement)) {
        return false;
      }
      DescendantSelector cs = (DescendantSelector)selector;
      if (selects(browserVersion, cs.getSimpleSelector(), element, pseudoElement, fromQuerySelectorAll))
      {
        if (selects(browserVersion, cs.getAncestorSelector(), (HtmlElement)parentNode, pseudoElement, fromQuerySelectorAll))
          return true; } return false;
    

    case 10: 
      DescendantSelector ds = (DescendantSelector)selector;
      SimpleSelector simpleSelector = ds.getSimpleSelector();
      if (selects(browserVersion, simpleSelector, element, pseudoElement, fromQuerySelectorAll)) {
        DomNode ancestor = element;
        if (simpleSelector.getSelectorType() != 9) {
          ancestor = ancestor.getParentNode();
        }
        Selector dsAncestorSelector = ds.getAncestorSelector();
        while ((ancestor instanceof HtmlElement))
        {
          if (selects(browserVersion, dsAncestorSelector, (HtmlElement)ancestor, pseudoElement, fromQuerySelectorAll)) {
            return true;
          }
          ancestor = ancestor.getParentNode();
        }
      }
      return false;
    case 0: 
      ConditionalSelector conditional = (ConditionalSelector)selector;
      SimpleSelector simpleSel = conditional.getSimpleSelector();
      return ((simpleSel == null) || 
        (selects(browserVersion, simpleSel, element, pseudoElement, fromQuerySelectorAll))) && 
        (selects(browserVersion, conditional.getCondition(), element, fromQuerySelectorAll));
    case 4: 
      ElementSelector es = (ElementSelector)selector;
      String name = es.getLocalName();
      return (name == null) || (name.equalsIgnoreCase(element.getLocalName()));
    case 2: 
      return "html".equalsIgnoreCase(element.getTagName());
    case 12: 
      SiblingSelector ss = (SiblingSelector)selector;
      DomNode prev = element.getPreviousSibling();
      while ((prev != null) && (!(prev instanceof HtmlElement))) {
        prev = prev.getPreviousSibling();
      }
      if (prev != null)
      {
        if ((selects(browserVersion, ss.getSelector(), (HtmlElement)prev, pseudoElement, fromQuerySelectorAll)) && 
          (selects(browserVersion, ss.getSiblingSelector(), element, pseudoElement, fromQuerySelectorAll)))
          return true; } return false;
    


    case 3: 
      NegativeSelector ns = (NegativeSelector)selector;
      return !selects(browserVersion, ns.getSimpleSelector(), element, pseudoElement, fromQuerySelectorAll);
    case 9: 
      if ((pseudoElement != null) && (!pseudoElement.isEmpty()) && (pseudoElement.charAt(0) == ':')) {
        String pseudoName = ((ElementSelector)selector).getLocalName();
        return pseudoName.equals(pseudoElement.substring(1));
      }
      return false;
    case 5: 
    case 6: 
    case 7: 
    case 8: 
      return false;
    }
    LOG.error("Unknown CSS selector type '" + selector.getSelectorType() + "'.");
    return false;
  }
  










  static boolean selects(BrowserVersion browserVersion, Condition condition, DomElement element, boolean fromQuerySelectorAll)
  {
    if ((condition instanceof PrefixAttributeConditionImpl)) {
      AttributeCondition ac = (AttributeCondition)condition;
      String value = ac.getValue();
      return (!"".equals(value)) && (element.getAttribute(ac.getLocalName()).startsWith(value));
    }
    if ((condition instanceof SuffixAttributeConditionImpl)) {
      AttributeCondition ac = (AttributeCondition)condition;
      String value = ac.getValue();
      return (!"".equals(value)) && (element.getAttribute(ac.getLocalName()).endsWith(value));
    }
    if ((condition instanceof SubstringAttributeConditionImpl)) {
      AttributeCondition ac = (AttributeCondition)condition;
      String value = ac.getValue();
      return (!"".equals(value)) && (element.getAttribute(ac.getLocalName()).contains(value));
    }
    switch (condition.getConditionType()) {
    case 5: 
      AttributeCondition ac4 = (AttributeCondition)condition;
      return ac4.getValue().equals(element.getId());
    case 9: 
      AttributeCondition ac3 = (AttributeCondition)condition;
      String v3 = ac3.getValue();
      if (v3.indexOf('\\') > -1) {
        v3 = UNESCAPE_SELECTOR.matcher(v3).replaceAll("$1");
      }
      String a3 = element.getAttribute("class");
      return selectsWhitespaceSeparated(v3, a3);
    case 0: 
      CombinatorCondition cc1 = (CombinatorCondition)condition;
      return (selects(browserVersion, cc1.getFirstCondition(), element, fromQuerySelectorAll)) && 
        (selects(browserVersion, cc1.getSecondCondition(), element, fromQuerySelectorAll));
    case 4: 
      AttributeCondition ac1 = (AttributeCondition)condition;
      if (ac1.getSpecified()) {
        String value = ac1.getValue();
        if (value.indexOf('\\') > -1) {
          value = UNESCAPE_SELECTOR.matcher(value).replaceAll("$1");
        }
        String attrValue = element.getAttribute(ac1.getLocalName());
        return (DomElement.ATTRIBUTE_NOT_DEFINED != attrValue) && (attrValue.equals(value));
      }
      return element.hasAttribute(ac1.getLocalName());
    case 8: 
      AttributeCondition ac2 = (AttributeCondition)condition;
      String v = ac2.getValue();
      String a = element.getAttribute(ac2.getLocalName());
      return selects(v, a, '-');
    case 7: 
      AttributeCondition ac5 = (AttributeCondition)condition;
      String v2 = ac5.getValue();
      String a2 = element.getAttribute(ac5.getLocalName());
      return selects(v2, a2, ' ');
    case 1: 
      CombinatorCondition cc2 = (CombinatorCondition)condition;
      return (selects(browserVersion, cc2.getFirstCondition(), element, fromQuerySelectorAll)) || 
        (selects(browserVersion, cc2.getSecondCondition(), element, fromQuerySelectorAll));
    case 2: 
      NegativeCondition nc = (NegativeCondition)condition;
      return !selects(browserVersion, nc.getCondition(), element, fromQuerySelectorAll);
    case 11: 
      return element.getParentNode().getChildNodes().getLength() == 1;
    case 13: 
      ContentCondition cc = (ContentCondition)condition;
      return element.asText().contains(cc.getData());
    case 6: 
      String lcLang = ((LangCondition)condition).getLang();
      int lcLangLength = lcLang.length();
      for (DomNode node = element; (node instanceof HtmlElement); node = node.getParentNode()) {
        String nodeLang = ((HtmlElement)node).getAttribute("lang");
        if (DomElement.ATTRIBUTE_NOT_DEFINED != nodeLang)
        {
          return (nodeLang.startsWith(lcLang)) && (
            (nodeLang.length() == lcLangLength) || ('-' == nodeLang.charAt(lcLangLength)));
        }
      }
      return false;
    case 12: 
      String tagName = element.getTagName();
      return ((HtmlPage)element.getPage()).getElementsByTagName(tagName).getLength() == 1;
    case 10: 
      return selectsPseudoClass(browserVersion, 
        (AttributeCondition)condition, element, fromQuerySelectorAll);
    case 3: 
      return false;
    }
    LOG.error("Unknown CSS condition type '" + condition.getConditionType() + "'.");
    return false;
  }
  




  private static boolean selects(String condition, String attribute, char separator)
  {
    int conditionLength = condition.length();
    if (conditionLength < 1) {
      return false;
    }
    
    int attribLength = attribute.length();
    if (attribLength < conditionLength) {
      return false;
    }
    if (attribLength > conditionLength) {
      if ((separator == attribute.charAt(conditionLength)) && 
        (attribute.startsWith(condition))) {
        return true;
      }
      if ((separator == attribute.charAt(attribLength - conditionLength - 1)) && 
        (attribute.endsWith(condition))) {
        return true;
      }
      if (attribLength + 1 > conditionLength) {
        StringBuilder tmp = new StringBuilder(conditionLength + 2);
        tmp.append(separator).append(condition).append(separator);
        return attribute.contains(tmp);
      }
      return false;
    }
    return attribute.equals(condition);
  }
  
  private static boolean selectsWhitespaceSeparated(String condition, String attribute) {
    int conditionLength = condition.length();
    if (conditionLength < 1) {
      return false;
    }
    
    int attribLength = attribute.length();
    if (attribLength < conditionLength) {
      return false;
    }
    
    int pos = attribute.indexOf(condition);
    while (pos != -1) {
      if ((pos > 0) && (!Character.isWhitespace(attribute.charAt(pos - 1)))) {
        pos = attribute.indexOf(condition, pos + 1);
      }
      else {
        int lastPos = pos + condition.length();
        if ((lastPos >= attribLength) || (Character.isWhitespace(attribute.charAt(lastPos)))) {
          return true;
        }
        pos = attribute.indexOf(condition, pos + 1);
      }
    }
    
    return false;
  }
  
  private static boolean selectsPseudoClass(BrowserVersion browserVersion, AttributeCondition condition, DomElement element, boolean fromQuerySelectorAll)
  {
    if (browserVersion.hasFeature(BrowserVersionFeatures.QUERYSELECTORALL_NOT_IN_QUIRKS)) {
      Object sobj = element.getPage().getScriptableObject();
      if (((sobj instanceof HTMLDocument)) && (((HTMLDocument)sobj).getDocumentMode() < 8)) {
        return false;
      }
    }
    
    String value = condition.getValue();
    String str1; switch ((str1 = value).hashCode()) {case -2136991809:  if (str1.equals("first-child")) {} break; case -1609594047:  if (str1.equals("enabled")) {} break; case -947996741:  if (str1.equals("only-child")) {} break; case -880905839:  if (str1.equals("target")) {} break; case -393139297:  if (str1.equals("required")) {} break; case -79017120:  if (str1.equals("optional")) {} break; case 3506402:  if (str1.equals("root")) break; break; case 96634189:  if (str1.equals("empty")) {} break; case 97604824:  if (str1.equals("focus")) {} break; case 99469628:  if (str1.equals("hover")) {} break; case 270940796:  if (str1.equals("disabled")) {} break; case 742313895:  if (str1.equals("checked")) {} break; case 835834661:  if (str1.equals("last-child")) {} break; case 1292941139:  if (str1.equals("first-of-type")) {} break; case 1455900751:  if (str1.equals("only-of-type")) {} break; case 2025926969:  if (!str1.equals("last-of-type")) {
        break label1010;
        return element == element.getPage().getDocumentElement();
        

        return ((element instanceof DisabledElement)) && (!((DisabledElement)element).isDisabled());
        

        return ((element instanceof DisabledElement)) && (((DisabledElement)element).isDisabled());
        

        HtmlPage htmlPage = element.getHtmlPageOrNull();
        if (htmlPage != null) {
          DomElement focus = htmlPage.getFocusedElement();
          return element == focus;
        }
        return false;
        

        return (((element instanceof HtmlCheckBoxInput)) && (((HtmlCheckBoxInput)element).isChecked())) || 
          (((element instanceof HtmlRadioButtonInput)) && (((HtmlRadioButtonInput)element).isChecked())) || (
          ((element instanceof HtmlOption)) && (((HtmlOption)element).isSelected()));
        

        return (((element instanceof HtmlInput)) || 
          ((element instanceof HtmlSelect)) || 
          ((element instanceof HtmlTextArea))) && 
          (element.hasAttribute("required"));
        

        return (((element instanceof HtmlInput)) || 
          ((element instanceof HtmlSelect)) || 
          ((element instanceof HtmlTextArea))) && 
          (!element.hasAttribute("required"));
        

        for (DomNode n = element.getPreviousSibling(); n != null; n = n.getPreviousSibling()) {
          if ((n instanceof DomElement)) {
            return false;
          }
        }
        return true;
        

        for (DomNode n = element.getNextSibling(); n != null; n = n.getNextSibling()) {
          if ((n instanceof DomElement)) {
            return false;
          }
        }
        return true;
        

        String firstType = element.getNodeName();
        for (DomNode n = element.getPreviousSibling(); n != null; n = n.getPreviousSibling()) {
          if (((n instanceof DomElement)) && (n.getNodeName().equals(firstType))) {
            return false;
          }
        }
        return true;
      }
      else {
        String lastType = element.getNodeName();
        for (DomNode n = element.getNextSibling(); n != null; n = n.getNextSibling()) {
          if (((n instanceof DomElement)) && (n.getNodeName().equals(lastType))) {
            return false;
          }
        }
        return true;
        

        for (DomNode n = element.getPreviousSibling(); n != null; n = n.getPreviousSibling()) {
          if ((n instanceof DomElement)) {
            return false;
          }
        }
        for (DomNode n = element.getNextSibling(); n != null; n = n.getNextSibling()) {
          if ((n instanceof DomElement)) {
            return false;
          }
        }
        return true;
        

        String type = element.getNodeName();
        for (DomNode n = element.getPreviousSibling(); n != null; n = n.getPreviousSibling()) {
          if (((n instanceof DomElement)) && (n.getNodeName().equals(type))) {
            return false;
          }
        }
        for (DomNode n = element.getNextSibling(); n != null; n = n.getNextSibling()) {
          if (((n instanceof DomElement)) && (n.getNodeName().equals(type))) {
            return false;
          }
        }
        return true;
        

        return isEmpty(element);
        

        if ((fromQuerySelectorAll) && (browserVersion.hasFeature(BrowserVersionFeatures.QUERYSELECTORALL_NO_TARGET))) {
          return false;
        }
        String ref = element.getPage().getUrl().getRef();
        return (StringUtils.isNotBlank(ref)) && (ref.equals(element.getId()));
        

        return element.isMouseOver(); }
      break; }
    label1010:
    if (value.startsWith("nth-child(")) {
      String nth = value.substring(value.indexOf('(') + 1, value.length() - 1);
      int index = 0;
      for (DomNode n = element; n != null; n = n.getPreviousSibling()) {
        if ((n instanceof DomElement)) {
          index++;
        }
      }
      return getNth(nth, index);
    }
    if (value.startsWith("nth-last-child(")) {
      String nth = value.substring(value.indexOf('(') + 1, value.length() - 1);
      int index = 0;
      for (DomNode n = element; n != null; n = n.getNextSibling()) {
        if ((n instanceof DomElement)) {
          index++;
        }
      }
      return getNth(nth, index);
    }
    if (value.startsWith("nth-of-type(")) {
      String nthType = element.getNodeName();
      String nth = value.substring(value.indexOf('(') + 1, value.length() - 1);
      int index = 0;
      for (DomNode n = element; n != null; n = n.getPreviousSibling()) {
        if (((n instanceof DomElement)) && (n.getNodeName().equals(nthType))) {
          index++;
        }
      }
      return getNth(nth, index);
    }
    if (value.startsWith("nth-last-of-type(")) {
      String nthLastType = element.getNodeName();
      String nth = value.substring(value.indexOf('(') + 1, value.length() - 1);
      int index = 0;
      for (DomNode n = element; n != null; n = n.getNextSibling()) {
        if (((n instanceof DomElement)) && (n.getNodeName().equals(nthLastType))) {
          index++;
        }
      }
      return getNth(nth, index);
    }
    if (value.startsWith("not(")) {
      String selectors = value.substring(value.indexOf('(') + 1, value.length() - 1);
      AtomicBoolean errorOccured = new AtomicBoolean(false);
      ErrorHandler errorHandler = new ErrorHandler()
      {
        public void warning(CSSParseException exception)
          throws CSSException
        {}
        
        public void fatalError(CSSParseException exception) throws CSSException
        {
          set(true);
        }
        
        public void error(CSSParseException exception) throws CSSException
        {
          set(true);
        }
      };
      CSSOMParser parser = new CSSOMParser(new SACParserCSS3());
      parser.setErrorHandler(errorHandler);
      try {
        SelectorList selectorList = 
          parser.parseSelectors(new InputSource(new StringReader(selectors)));
        if ((errorOccured.get()) || (selectorList == null) || (selectorList.getLength() != 1)) {
          throw new CSSException("Invalid selectors: " + selectors);
        }
        
        validateSelectors(selectorList, 9, element);
        
        return 
          !selects(browserVersion, selectorList.item(0), element, null, fromQuerySelectorAll);
      }
      catch (IOException e) {
        throw new CSSException("Error parsing CSS selectors from '" + selectors + "': " + 
          e.getMessage());
      }
    }
    return false;
  }
  
  private static boolean isEmpty(DomElement element)
  {
    for (DomNode n = element.getFirstChild(); n != null; n = n.getNextSibling()) {
      if (((n instanceof DomElement)) || ((n instanceof DomText))) {
        return false;
      }
    }
    return true;
  }
  
  private static boolean getNth(String nth, int index) {
    if ("odd".equalsIgnoreCase(nth)) {
      return index % 2 != 0;
    }
    
    if ("even".equalsIgnoreCase(nth)) {
      return index % 2 == 0;
    }
    

    int nIndex = nth.indexOf('n');
    int a = 0;
    if (nIndex != -1) {
      String value = nth.substring(0, nIndex).trim();
      if ("-".equals(value)) {
        a = -1;
      }
      else {
        if (value.startsWith("+")) {
          value = value.substring(1);
        }
        a = NumberUtils.toInt(value, 1);
      }
    }
    
    String value = nth.substring(nIndex + 1).trim();
    if (value.startsWith("+")) {
      value = value.substring(1);
    }
    int b = NumberUtils.toInt(value, 0);
    if (a == 0) {
      return (index == b) && (b > 0);
    }
    
    double n = (index - b) / a;
    return (n >= 0.0D) && (n % 1.0D == 0.0D);
  }
  



  private org.w3c.dom.css.CSSStyleSheet parseCSS(InputSource source)
  {
    org.w3c.dom.css.CSSStyleSheet ss;
    

    try
    {
      ErrorHandler errorHandler = getWindow().getWebWindow().getWebClient().getCssErrorHandler();
      CSSOMParser parser = new CSSOMParser(new SACParserCSS3());
      parser.setErrorHandler(errorHandler);
      ss = parser.parseStyleSheet(source, null, null);
    } catch (Throwable t) {
      org.w3c.dom.css.CSSStyleSheet ss;
      LOG.error("Error parsing CSS from '" + toString(source) + "': " + t.getMessage(), t);
      ss = new CSSStyleSheetImpl();
    }
    return ss;
  }
  



  public SelectorList parseSelectors(InputSource source)
  {
    SelectorList selectors;
    

    try
    {
      ErrorHandler errorHandler = getWindow().getWebWindow().getWebClient().getCssErrorHandler();
      CSSOMParser parser = new CSSOMParser(new SACParserCSS3());
      parser.setErrorHandler(errorHandler);
      SelectorList selectors = parser.parseSelectors(source);
      
      if (selectors == null) {
        selectors = new SelectorListImpl();
      }
    }
    catch (Throwable t) {
      LOG.error("Error parsing CSS selectors from '" + toString(source) + "': " + t.getMessage(), t);
      selectors = new SelectorListImpl();
    }
    return selectors;
  }
  





  static SACMediaList parseMedia(ErrorHandler errorHandler, String mediaString)
  {
    try
    {
      CSSOMParser parser = new CSSOMParser(new SACParserCSS3());
      parser.setErrorHandler(errorHandler);
      
      InputSource source = new InputSource(new StringReader(mediaString));
      SACMediaList media = parser.parseMedia(source);
      if (media != null) {
        return media;
      }
    }
    catch (Exception e) {
      LOG.error("Error parsing CSS media from '" + mediaString + "': " + e.getMessage(), e);
    }
    return new SACMediaListImpl();
  }
  



  private static String toString(InputSource source)
  {
    try
    {
      Reader reader = source.getCharacterStream();
      if (reader != null)
      {
        if ((reader instanceof StringReader)) {
          StringReader sr = (StringReader)reader;
          sr.reset();
        }
        return IOUtils.toString(reader);
      }
      InputStream is = source.getByteStream();
      if (is != null)
      {
        if ((is instanceof ByteArrayInputStream)) {
          ByteArrayInputStream bis = (ByteArrayInputStream)is;
          bis.reset();
        }
        return IOUtils.toString(is, StandardCharsets.ISO_8859_1);
      }
      return "";
    }
    catch (IOException e) {}
    return "";
  }
  




  @JsxGetter
  public HTMLElement getOwnerNode()
  {
    return ownerNode_;
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public HTMLElement getOwningElement()
  {
    return ownerNode_;
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
  public CSSRuleList getRules()
  {
    return getCssRules();
  }
  



  @JsxGetter
  public CSSRuleList getCssRules()
  {
    initCssRules();
    return cssRules_;
  }
  



  @JsxGetter
  public String getHref()
  {
    BrowserVersion version = getBrowserVersion();
    
    if (ownerNode_ != null) {
      DomNode node = ownerNode_.getDomNodeOrDie();
      if ((node instanceof HtmlLink))
      {
        HtmlLink link = (HtmlLink)node;
        HtmlPage page = (HtmlPage)link.getPage();
        String href = link.getHrefAttribute();
        if (("".equals(href)) && (version.hasFeature(BrowserVersionFeatures.STYLESHEET_HREF_EMPTY_IS_NULL))) {
          return null;
        }
        try
        {
          URL url = page.getFullyQualifiedUrl(href);
          return url.toExternalForm();
        }
        catch (MalformedURLException e)
        {
          LOG.warn(e.getMessage(), e);
        }
      }
    }
    
    return null;
  }
  





  @JsxFunction
  public int insertRule(String rule, int position)
  {
    try
    {
      initCssRules();
      int result = wrapped_.insertRule(rule, fixIndex(position));
      refreshCssRules();
      return result;
    }
    catch (DOMException e) {
      throw Context.throwAsScriptRuntimeEx(e);
    }
  }
  
  private void refreshCssRules() {
    if (cssRules_ == null) {
      return;
    }
    
    cssRules_.clearRules();
    cssRulesIndexFix_.clear();
    
    org.w3c.dom.css.CSSRuleList ruleList = getWrappedSheet().getCssRules();
    List<org.w3c.dom.css.CSSRule> rules = ((CSSRuleListImpl)ruleList).getRules();
    int pos = 0;
    for (Iterator<org.w3c.dom.css.CSSRule> it = rules.iterator(); it.hasNext();) {
      org.w3c.dom.css.CSSRule rule = (org.w3c.dom.css.CSSRule)it.next();
      if ((rule instanceof CSSCharsetRule)) {
        cssRulesIndexFix_.add(Integer.valueOf(pos));
      }
      else
      {
        CSSRule cssRule = 
          CSSRule.create(this, rule);
        if (cssRule == null) {
          cssRulesIndexFix_.add(Integer.valueOf(pos));
        }
        else {
          cssRules_.addRule(cssRule);
        }
        pos++;
      }
    }
  }
  
  private int fixIndex(int index) { for (Iterator localIterator = cssRulesIndexFix_.iterator(); localIterator.hasNext();) { int fix = ((Integer)localIterator.next()).intValue();
      if (fix > index) {
        return index;
      }
      index++;
    }
    return index;
  }
  



  @JsxFunction
  public void deleteRule(int position)
  {
    try
    {
      initCssRules();
      wrapped_.deleteRule(fixIndex(position));
      refreshCssRules();
    }
    catch (DOMException e) {
      throw Context.throwAsScriptRuntimeEx(e);
    }
  }
  






  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
  public int addRule(String selector, String rule)
  {
    String completeRule = selector + " {" + rule + "}";
    try {
      initCssRules();
      wrapped_.insertRule(completeRule, wrapped_.getCssRules().getLength());
      refreshCssRules();
    }
    catch (DOMException e) {
      throw Context.throwAsScriptRuntimeEx(e);
    }
    return -1;
  }
  



  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
  public void removeRule(int position)
  {
    try
    {
      initCssRules();
      wrapped_.deleteRule(fixIndex(position));
      refreshCssRules();
    }
    catch (DOMException e) {
      throw Context.throwAsScriptRuntimeEx(e);
    }
  }
  



  public String getUri()
  {
    return uri_;
  }
  




  public boolean isActive()
  {
    HtmlElement e = ownerNode_.getDomNodeOrNull();
    String media; if ((e instanceof HtmlStyle)) {
      HtmlStyle style = (HtmlStyle)e;
      media = style.getMediaAttribute();
    } else { String media;
      if ((e instanceof HtmlLink)) {
        HtmlLink link = (HtmlLink)e;
        media = link.getMediaAttribute();
      }
      else {
        return true;
      } }
    String media;
    if (StringUtils.isBlank(media)) {
      return true;
    }
    
    WebClient webClient = getWindow().getWebWindow().getWebClient();
    SACMediaList mediaList = parseMedia(webClient.getCssErrorHandler(), media);
    return isActive(this, new MediaListImpl(mediaList));
  }
  



  public boolean isEnabled()
  {
    return enabled_;
  }
  



  public void setEnabled(boolean enabled)
  {
    enabled_ = enabled;
  }
  





  static boolean isActive(SimpleScriptable scriptable, MediaList mediaList)
  {
    if (mediaList.getLength() == 0) {
      return true;
    }
    
    for (int i = 0; i < mediaList.getLength(); i++) {
      MediaQuery mediaQuery = ((MediaListImpl)mediaList).mediaQuery(i);
      boolean isActive = isActive(scriptable, mediaQuery);
      if (mediaQuery.isNot()) {
        isActive = !isActive;
      }
      if (isActive) {
        return true;
      }
    }
    return false;
  }
  
  private static boolean isActive(SimpleScriptable scriptable, MediaQuery mediaQuery) {
    String mediaType = mediaQuery.getMedia();
    if (("screen".equalsIgnoreCase(mediaType)) || ("all".equalsIgnoreCase(mediaType))) {
      for (Property property : mediaQuery.getProperties()) {
        String str1;
        switch ((str1 = property.getName()).hashCode()) {case -1959210941:  if (str1.equals("min-device-height")) {} break; case -1662432227:  if (str1.equals("max-width")) break; break; case -1600030548:  if (str1.equals("resolution")) {} break; case -1439500848:  if (str1.equals("orientation")) {} break; case -889953653:  if (str1.equals("min-width")) {} break; case -428786256:  if (str1.equals("max-height")) {} break; case -215041867:  if (str1.equals("max-resolution")) {} break; case 20711381:  if (str1.equals("max-device-height")) {} break; case 441874183:  if (str1.equals("min-resolution")) {} break; case 1751882538:  if (str1.equals("min-device-width")) {} break; case 1815751000:  if (str1.equals("max-device-width")) {} break; case 2043213058:  if (!str1.equals("min-height"))
          {
            continue;float val = pixelValue((CSSValueImpl)property.getValue(), scriptable);
            if (val < scriptable.getWindow().getWebWindow().getInnerWidth()) {
              return false;
              



              float val = pixelValue((CSSValueImpl)property.getValue(), scriptable);
              if (val > scriptable.getWindow().getWebWindow().getInnerWidth()) {
                return false;
                



                float val = pixelValue((CSSValueImpl)property.getValue(), scriptable);
                if (val < scriptable.getWindow().getScreen().getWidth()) {
                  return false;
                  



                  float val = pixelValue((CSSValueImpl)property.getValue(), scriptable);
                  if (val > scriptable.getWindow().getScreen().getWidth()) {
                    return false;
                    



                    float val = pixelValue((CSSValueImpl)property.getValue(), scriptable);
                    if (val < scriptable.getWindow().getWebWindow().getInnerWidth())
                      return false;
                  }
                }
              }
            }
          } else { float val = pixelValue((CSSValueImpl)property.getValue(), scriptable);
            if (val > scriptable.getWindow().getWebWindow().getInnerWidth()) {
              return false;
              



              float val = pixelValue((CSSValueImpl)property.getValue(), scriptable);
              if (val < scriptable.getWindow().getScreen().getWidth()) {
                return false;
                



                float val = pixelValue((CSSValueImpl)property.getValue(), scriptable);
                if (val > scriptable.getWindow().getScreen().getWidth()) {
                  return false;
                  



                  float val = resolutionValue((CSSValueImpl)property.getValue());
                  if (Math.round(val) != scriptable.getWindow().getScreen().getDeviceXDPI()) {
                    return false;
                    



                    float val = resolutionValue((CSSValueImpl)property.getValue());
                    if (val < scriptable.getWindow().getScreen().getDeviceXDPI()) {
                      return false;
                      



                      float val = resolutionValue((CSSValueImpl)property.getValue());
                      if (val > scriptable.getWindow().getScreen().getDeviceXDPI()) {
                        return false;
                        



                        String orient = property.getValue().getCssText();
                        WebWindow window = scriptable.getWindow().getWebWindow();
                        if ("portrait".equals(orient)) {
                          if (window.getInnerWidth() > window.getInnerHeight()) {
                            return false;
                          }
                        }
                        else if ("landscape".equals(orient)) {
                          if (window.getInnerWidth() < window.getInnerHeight()) {
                            return false;
                          }
                        }
                        else {
                          LOG.warn("CSSValue '" + property.getValue().getCssText() + 
                            "' not supported for feature 'orientation'.");
                          return false;
                        }
                      }
                    }
                  }
                } } } }
          break; } }
      return true;
    }
    return false;
  }
  
  private static float pixelValue(CSSValueImpl cssValue, SimpleScriptable scriptable)
  {
    switch (cssValue.getPrimitiveType()) {
    case 5: 
      return cssValue.getFloatValue((short)5);
    
    case 3: 
      return 16.0F * cssValue.getFloatValue((short)3);
    
    case 2: 
      return 0.16F * cssValue.getFloatValue((short)2);
    
    case 4: 
      return 0.16F * cssValue.getFloatValue((short)4);
    case 7: 
      int dpi = scriptable.getWindow().getScreen().getDeviceXDPI();
      return dpi / 25.4F * cssValue.getFloatValue((short)7);
    case 6: 
      int dpi = scriptable.getWindow().getScreen().getDeviceXDPI();
      return dpi / 254.0F * cssValue.getFloatValue((short)6);
    case 9: 
      int dpi = scriptable.getWindow().getScreen().getDeviceXDPI();
      return dpi / 72.0F * cssValue.getFloatValue((short)9);
    }
    
    
    LOG.warn("CSSValue '" + cssValue.getCssText() + "' has to be a 'px', 'em', '%', 'mm', 'ex', or 'pt' value.");
    return -1.0F;
  }
  
  private static float resolutionValue(CSSValueImpl cssValue) {
    if (cssValue.getPrimitiveType() == 18) {
      String text = cssValue.getCssText();
      if (text.endsWith("dpi")) {
        return cssValue.getFloatValue((short)18);
      }
      if (text.endsWith("dpcm")) {
        return 2.54F * cssValue.getFloatValue((short)18);
      }
      if (text.endsWith("dppx")) {
        return 96.0F * cssValue.getFloatValue((short)18);
      }
    }
    
    LOG.warn("CSSValue '" + cssValue.getCssText() + "' has to be a 'dpi', 'dpcm', or 'dppx' value.");
    return -1.0F;
  }
  






  public static void validateSelectors(SelectorList selectorList, int documentMode, DomNode domNode)
    throws CSSException
  {
    for (int i = 0; i < selectorList.getLength(); i++) {
      Selector item = selectorList.item(i);
      if (!isValidSelector(item, documentMode, domNode)) {
        throw new CSSException("Invalid selector: " + item);
      }
    }
  }
  


  private static boolean isValidSelector(Selector selector, int documentMode, DomNode domNode)
  {
    switch (selector.getSelectorType()) {
    case 4: 
      return true;
    case 0: 
      ConditionalSelector conditional = (ConditionalSelector)selector;
      SimpleSelector simpleSel = conditional.getSimpleSelector();
      return ((simpleSel == null) || (isValidSelector(simpleSel, documentMode, domNode))) && 
        (isValidCondition(conditional.getCondition(), documentMode, domNode));
    case 10: 
    case 11: 
      DescendantSelector ds = (DescendantSelector)selector;
      return (isValidSelector(ds.getAncestorSelector(), documentMode, domNode)) && 
        (isValidSelector(ds.getSimpleSelector(), documentMode, domNode));
    case 12: 
      SiblingSelector ss = (SiblingSelector)selector;
      return (isValidSelector(ss.getSelector(), documentMode, domNode)) && 
        (isValidSelector(ss.getSiblingSelector(), documentMode, domNode));
    case 1: 
      if ((selector instanceof SiblingSelector)) {
        SiblingSelector sibling = (SiblingSelector)selector;
        return (isValidSelector(sibling.getSelector(), documentMode, domNode)) && 
          (isValidSelector(sibling.getSiblingSelector(), documentMode, domNode));
      }
      break;
    }
    LOG.warn("Unhandled CSS selector type '" + selector.getSelectorType() + "'. Accepting it silently.");
    return true;
  }
  



  private static boolean isValidCondition(Condition condition, int documentMode, DomNode domNode)
  {
    switch (condition.getConditionType()) {
    case 0: 
      CombinatorCondition cc1 = (CombinatorCondition)condition;
      return (isValidCondition(cc1.getFirstCondition(), documentMode, domNode)) && 
        (isValidCondition(cc1.getSecondCondition(), documentMode, domNode));
    case 4: 
    case 5: 
    case 6: 
    case 7: 
    case 8: 
    case 9: 
    case 11: 
    case 12: 
    case 13: 
      return true;
    case 10: 
      PseudoClassConditionImpl pcc = (PseudoClassConditionImpl)condition;
      String value = pcc.getValue();
      if (value.endsWith(")")) {
        if (value.endsWith("()")) {
          return false;
        }
        value = value.substring(0, value.indexOf('(') + 1) + ')';
      }
      if (documentMode < 9) {
        return CSS2_PSEUDO_CLASSES.contains(value);
      }
      
      if ((!CSS2_PSEUDO_CLASSES.contains(value)) && 
        (domNode.hasFeature(BrowserVersionFeatures.QUERYSELECTOR_CSS3_PSEUDO_REQUIRE_ATTACHED_NODE)) && 
        (!domNode.isAttachedToPage()) && 
        (!domNode.hasChildNodes())) {
        throw new CSSException("Syntax Error");
      }
      
      if ("nth-child()".equals(value)) {
        String arg = StringUtils.substringBetween(pcc.getValue(), "(", ")").trim();
        return ("even".equalsIgnoreCase(arg)) || ("odd".equalsIgnoreCase(arg)) || 
          (NTH_NUMERIC.matcher(arg).matches()) || 
          (NTH_COMPLEX.matcher(arg).matches());
      }
      return CSS3_PSEUDO_CLASSES.contains(value);
    }
    LOG.warn("Unhandled CSS condition type '" + condition.getConditionType() + "'. Accepting it silently.");
    return true;
  }
  
  private void initCssRules()
  {
    if (cssRules_ == null) {
      cssRules_ = new CSSRuleList(this);
      cssRulesIndexFix_ = new ArrayList();
      refreshCssRules();
    }
  }
}
