package com.gargoylesoftware.htmlunit.javascript.host.dom;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.StringWebResponse;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBodyElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLHeadElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLHtmlElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTitleElement;
import com.gargoylesoftware.htmlunit.javascript.host.xml.XMLDocument;
import com.gargoylesoftware.htmlunit.xml.XmlPage;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;




























































@JsxClass
public class DOMImplementation
  extends SimpleScriptable
{
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public DOMImplementation() {}
  
  @JsxFunction
  public boolean hasFeature(String feature, String version)
  {
    String str1;
    switch ((str1 = feature).hashCode()) {case -2030438022:  if (str1.equals("http://www.w3.org/TR/SVG11/feature#BasicStructure")) {} break; case -1676424894:  if (str1.equals("MutationEvents")) {} break; case -1021867874:  if (str1.equals("Traversal")) {} break; case -895672156:  if (str1.equals("HTMLEvents")) {} break; case -848979770:  if (str1.equals("TextEvents")) {} break; case -289890515:  if (str1.equals("MutationNameEvents")) {} break; case 2439:  if (str1.equals("LS")) {} break; case 67043:  if (str1.equals("CSS")) {} break; case 87031:  if (str1.equals("XML")) break; break; case 2078383:  if (str1.equals("CSS2")) {} break; case 2078384:  if (str1.equals("CSS3")) {} break; case 2106303:  if (str1.equals("Core")) break; break; case 2228139:  if (str1.equals("HTML")) break; break; case 78727453:  if (str1.equals("Range")) {} break; case 82651726:  if (str1.equals("Views")) {} break; case 83497987:  if (str1.equals("XHTML")) break; break; case 83750045:  if (str1.equals("XPath")) {} break; case 265787597:  if (str1.equals("UIEvents")) {} break; case 1309847190:  if (str1.equals("LS-Async")) {} break; case 1361393637:  if (str1.equals("StyleSheets")) {} break; case 1574516588:  if (str1.equals("http://www.w3.org/TR/SVG11/feature#Shape")) {} break; case 1788298240:  if (str1.equals("KeyboardEvents")) {} break; case 1949901977:  if (str1.equals("Validation")) {} break; case 2087505209:  if (str1.equals("Events")) {} break; case 2102518270:  if (!str1.equals("MouseEvents"))
      {
        break label1528;
        
        String str2;
        switch ((str2 = version).hashCode()) {case 48563:  if (str2.equals("1.0")) break; break; case 49524:  if (str2.equals("2.0")) break; break; case 50485:  if (!str2.equals("3.0"))
          {
            break label1528;
            return true;
          } else {
            return getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_DOMIMPLEMENTATION_FEATURE_CORE_3);
          }
          break;
        }
        break label1528;
        String str3;
        switch ((str3 = version).hashCode()) {case 48563:  if (str3.equals("1.0")) break; break; case 49524:  if (str3.equals("2.0")) {} break; case 50485:  if (!str3.equals("3.0")) {
            break label1528;
            return getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_DOMIMPLEMENTATION_FEATURE_VIEWS_1);
            
            return true;
          } else {
            return getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_DOMIMPLEMENTATION_FEATURE_VIEWS_3);
          }
          break;
        }
        
        break label1528;
        return getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_DOMIMPLEMENTATION_FEATURE_STYLESHEETS);
        
        String str4;
        switch ((str4 = version).hashCode()) {case 48563:  if (str4.equals("1.0")) break; break; case 49524:  if (str4.equals("2.0")) {} break; case 50485:  if (!str4.equals("3.0")) {
            break label1528;
            return getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_DOMIMPLEMENTATION_FEATURE_CSS_1);
            
            return getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_DOMIMPLEMENTATION_FEATURE_CSS_2);
          } else {
            return getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_DOMIMPLEMENTATION_FEATURE_CSS_3);
          }
          break;
        }
        break label1528;
        String str5;
        switch ((str5 = version).hashCode()) {case 48563:  if (str5.equals("1.0")) break; break; case 49524:  if (str5.equals("2.0")) {} break; case 50485:  if (!str5.equals("3.0")) {
            break label1528;
            return getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_DOMIMPLEMENTATION_FEATURE_CSS2_1);
            
            return true;
          } else {
            return getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_DOMIMPLEMENTATION_FEATURE_CSS2_3);
          }
          break;
        }
        break label1528;
        String str6;
        switch ((str6 = version).hashCode()) {case 48563:  if (str6.equals("1.0")) break; break; case 49524:  if (str6.equals("2.0")) {} break; case 50485:  if (!str6.equals("3.0")) {
            break label1528;
            return getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_DOMIMPLEMENTATION_FEATURE_CSS3_1);
            
            return getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_DOMIMPLEMENTATION_FEATURE_CSS3_2);
          } else {
            return getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_DOMIMPLEMENTATION_FEATURE_CSS3_3);
          }
          break;
        }
        
      }
      else
      {
        String str7;
        switch ((str7 = version).hashCode()) {case 48563:  if (str7.equals("1.0")) break; break; case 49524:  if (str7.equals("2.0")) {} break; case 50485:  if (!str7.equals("3.0")) {
            break label1528;
            return getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_DOMIMPLEMENTATION_FEATURE_EVENTS_1);
          }
          else {
            return true;
          }
          break;
        }
        break label1528;
        String str8;
        switch ((str8 = version).hashCode()) {case 48563:  if (str8.equals("1.0")) break; break; case 49524:  if (str8.equals("2.0")) break; break; case 50485:  if (!str8.equals("3.0"))
          {
            break label1528;
            return getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_DOMIMPLEMENTATION_FEATURE_UIEVENTS_2);
          } else {
            return true;
          }
          break;
        }
        
        break label1528;
        return getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_DOMIMPLEMENTATION_FEATURE_KEYBOARDEVENTS);
        

        return getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_DOMIMPLEMENTATION_FEATURE_MUTATIONNAMEEVENTS);
        

        return getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_DOMIMPLEMENTATION_FEATURE_TEXTEVENTS);
        


        return getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_DOMIMPLEMENTATION_FEATURE_LS);
        
        String str9;
        
        switch ((str9 = version).hashCode()) {case 48563:  if (str9.equals("1.0")) break; break; case 49524:  if (str9.equals("2.0")) {} break; case 50485:  if (!str9.equals("3.0")) {
            break label1528;
            return getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_DOMIMPLEMENTATION_FEATURE_RANGE_1);
            
            return true;
          } else {
            return getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_DOMIMPLEMENTATION_FEATURE_RANGE_3);
          }
          break;
        }
        
        break label1528;
        return getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_DOMIMPLEMENTATION_FEATURE_VALIDATION);
        

        return getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_DOMIMPLEMENTATION_FEATURE_XPATH);
        
        String str10;
        
        switch ((str10 = version).hashCode()) {case 48563:  if (str10.equals("1.0")) break; break; case 48564:  if (str10.equals("1.1")) break; break; case 48565:  if (!str10.equals("1.2"))
          {
            break label1528;
            return true;
          } else {
            return getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_DOMIMPLEMENTATION_FEATURE_SVG_BASICSTRUCTURE_1_2);
          }
          break;
        }
      }
      break;
    }
    label1528:
    return false;
  }
  








  @JsxFunction
  public XMLDocument createDocument(String namespaceURI, String qualifiedName, DocumentType doctype)
  {
    XMLDocument document = new XMLDocument(getWindow().getWebWindow());
    document.setParentScope(getParentScope());
    document.setPrototype(getPrototype(document.getClass()));
    if ((qualifiedName != null) && (!qualifiedName.isEmpty())) {
      XmlPage page = (XmlPage)document.getDomNodeOrDie();
      page.appendChild(page.createElementNS("".equals(namespaceURI) ? null : namespaceURI, qualifiedName));
    }
    return document;
  }
  







  @JsxFunction
  public HTMLDocument createHTMLDocument(Object titleObj)
  {
    if ((titleObj == Undefined.instance) && 
      (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_DOMIMPLEMENTATION_CREATE_HTMLDOCOMENT_REQUIRES_TITLE))) {
      throw Context.reportRuntimeError("Title is required");
    }
    
    HTMLDocument document = new HTMLDocument();
    document.setParentScope(getParentScope());
    document.setPrototype(getPrototype(document.getClass()));
    


    WebResponse resp = new StringWebResponse("", WebClient.URL_ABOUT_BLANK);
    HtmlPage page = new HtmlPage(resp, getWindow().getWebWindow());
    page.setEnclosingWindow(null);
    document.setDomNode(page);
    
    HTMLHtmlElement html = (HTMLHtmlElement)document.createElement("html");
    page.appendChild(html.getDomNodeOrDie());
    
    HTMLHeadElement head = (HTMLHeadElement)document.createElement("head");
    html.appendChild(head);
    
    if (titleObj != Undefined.instance) {
      HTMLTitleElement title = (HTMLTitleElement)document.createElement("title");
      head.appendChild(title);
      title.setTextContent(Context.toString(titleObj));
    }
    
    HTMLBodyElement body = (HTMLBodyElement)document.createElement("body");
    html.appendChild(body);
    
    return document;
  }
}
