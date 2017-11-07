package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClasses;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;

@JsxClasses({@com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlSpan.class), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlKeygen.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})})
public class HTMLSpanElement
  extends HTMLElement
{
  private boolean endTagForbidden_;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public HTMLSpanElement() {}
  
  /* Error */
  public void setDomNode(com.gargoylesoftware.htmlunit.html.DomNode domNode)
  {
    // Byte code:
    //   0: aload_0
    //   1: aload_1
    //   2: invokespecial 26	com/gargoylesoftware/htmlunit/javascript/host/html/HTMLElement:setDomNode	(Lcom/gargoylesoftware/htmlunit/html/DomNode;)V
    //   5: aload_0
    //   6: invokevirtual 28	com/gargoylesoftware/htmlunit/javascript/host/html/HTMLSpanElement:getBrowserVersion	()Lcom/gargoylesoftware/htmlunit/BrowserVersion;
    //   9: astore_2
    //   10: aload_2
    //   11: getstatic 32	com/gargoylesoftware/htmlunit/BrowserVersionFeatures:HTMLBASEFONT_END_TAG_FORBIDDEN	Lcom/gargoylesoftware/htmlunit/BrowserVersionFeatures;
    //   14: invokevirtual 38	com/gargoylesoftware/htmlunit/BrowserVersion:hasFeature	(Lcom/gargoylesoftware/htmlunit/BrowserVersionFeatures;)Z
    //   17: ifeq +72 -> 89
    //   20: aload_1
    //   21: invokevirtual 44	com/gargoylesoftware/htmlunit/html/DomNode:getLocalName	()Ljava/lang/String;
    //   24: getstatic 50	java/util/Locale:ROOT	Ljava/util/Locale;
    //   27: invokevirtual 56	java/lang/String:toLowerCase	(Ljava/util/Locale;)Ljava/lang/String;
    //   30: dup
    //   31: astore_3
    //   32: invokevirtual 62	java/lang/String:hashCode	()I
    //   35: lookupswitch	default:+54->89, -1720958304:+25->60, -1134665583:+37->72
    //   60: aload_3
    //   61: ldc 66
    //   63: invokevirtual 68	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   66: ifne +18 -> 84
    //   69: goto +20 -> 89
    //   72: aload_3
    //   73: ldc 72
    //   75: invokevirtual 68	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   78: ifne +6 -> 84
    //   81: goto +8 -> 89
    //   84: aload_0
    //   85: iconst_1
    //   86: putfield 74	com/gargoylesoftware/htmlunit/javascript/host/html/HTMLSpanElement:endTagForbidden_	Z
    //   89: return
    // Line number table:
    //   Java source line #58	-> byte code offset #0
    //   Java source line #59	-> byte code offset #5
    //   Java source line #60	-> byte code offset #10
    //   Java source line #61	-> byte code offset #20
    //   Java source line #64	-> byte code offset #84
    //   Java source line #69	-> byte code offset #89
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	90	0	this	HTMLSpanElement
    //   0	90	1	domNode	com.gargoylesoftware.htmlunit.html.DomNode
    //   9	2	2	browser	com.gargoylesoftware.htmlunit.BrowserVersion
    //   31	42	3	str	String
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
  



  protected boolean isLowerCaseInOuterHtml()
  {
    return super.isLowerCaseInOuterHtml();
  }
  





  protected boolean isEndTagForbidden()
  {
    return endTagForbidden_;
  }
}
