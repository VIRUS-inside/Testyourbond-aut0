package com.gargoylesoftware.htmlunit.activex.javascript.msxml;

import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import net.sourceforge.htmlunit.corejs.javascript.Context;
































@JsxClass(domClass=DomText.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
public class XMLDOMText
  extends XMLDOMCharacterData
{
  public XMLDOMText() {}
  
  public Object getText()
  {
    DomText domText = getDomNodeOrDie();
    return domText.getWholeText();
  }
  





  @JsxFunction
  public Object splitText(int offset)
  {
    if (offset < 0) {
      throw Context.reportRuntimeError("The offset must be 0 or a positive number that is not greater than the number of characters in the data.");
    }
    

    DomText domText = getDomNodeOrDie();
    if (offset > domText.getLength()) {
      throw Context.reportRuntimeError("The offset must be 0 or a positive number that is not greater than the number of characters in the data.");
    }
    

    return getScriptableFor(domText.splitText(offset));
  }
  



  public DomText getDomNodeOrDie()
  {
    return (DomText)super.getDomNodeOrDie();
  }
}
