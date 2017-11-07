package com.gargoylesoftware.htmlunit.javascript.host.dom;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.NamedNodeMap;





































@JsxClass
public class DOMTokenList
  extends SimpleScriptable
{
  private static final String WHITESPACE_CHARS = " \t\r\n\f";
  private static final String WHITESPACE_CHARS_IE_11 = " \t\r\n\f\013";
  private String attributeName_;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public DOMTokenList() {}
  
  public DOMTokenList(Node node, String attributeName)
  {
    setDomNode(node.getDomNodeOrDie(), false);
    setParentScope(node.getParentScope());
    setPrototype(getPrototype(getClass()));
    attributeName_ = attributeName;
  }
  



  @JsxGetter
  public int getLength()
  {
    String value = getDefaultValue(null);
    return StringUtils.split(value, whitespaceChars()).length;
  }
  



  public String getDefaultValue(Class<?> hint)
  {
    if (getPrototype() == null) {
      return (String)super.getDefaultValue(hint);
    }
    DomNode node = getDomNodeOrNull();
    if (node != null) {
      DomAttr attr = (DomAttr)node.getAttributes().getNamedItem(attributeName_);
      if (attr != null) {
        String value = attr.getValue();
        if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_DOMTOKENLIST_REMOVE_WHITESPACE_CHARS_ON_EDIT)) {
          value = String.join(" ", StringUtils.split(value, whitespaceChars()));
        }
        return value;
      }
    }
    return "";
  }
  



  @JsxFunction
  public void add(String token)
  {
    if (!contains(token)) {
      String value = getDefaultValue(null);
      if ((value.length() != 0) && (!isWhitespache(value.charAt(value.length() - 1)))) {
        value = value + " ";
      }
      value = value + token;
      updateAttribute(value);
    }
  }
  



  @JsxFunction
  public void remove(String token)
  {
    if (StringUtils.isEmpty(token)) {
      throw Context.reportRuntimeError("Empty imput not allowed");
    }
    if (StringUtils.containsAny(token, whitespaceChars())) {
      throw Context.reportRuntimeError("Empty imput not allowed");
    }
    String value = getDefaultValue(null);
    int pos = position(value, token);
    while (pos != -1) {
      int from = pos;
      int to = pos + token.length();
      do
      {
        from--;
        if (from <= 0) break; } while (isWhitespache(value.charAt(from - 1)));
      

      while ((to < value.length() - 1) && (isWhitespache(value.charAt(to)))) {
        to++;
      }
      
      StringBuilder result = new StringBuilder();
      if (from > 0) {
        result.append(value, 0, from);
        if (to < value.length()) {
          result.append(" ");
        }
      }
      result.append(value, to, value.length());
      
      value = result.toString();
      
      if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_DOMTOKENLIST_REMOVE_WHITESPACE_CHARS_ON_REMOVE)) {
        value = String.join(" ", StringUtils.split(value, whitespaceChars()));
      }
      updateAttribute(value);
      
      pos = position(value, token);
    }
  }
  




  @JsxFunction
  public boolean toggle(String token)
  {
    if (contains(token)) {
      remove(token);
      return false;
    }
    add(token);
    return true;
  }
  




  @JsxFunction
  public boolean contains(String token)
  {
    if (StringUtils.isEmpty(token)) {
      throw Context.reportRuntimeError("Empty imput not allowed");
    }
    if (StringUtils.containsAny(token, whitespaceChars())) {
      throw Context.reportRuntimeError("Empty imput not allowed");
    }
    return position(getDefaultValue(null), token) > -1;
  }
  




  @JsxFunction
  public Object item(int index)
  {
    if (index < 0) {
      return null;
    }
    String value = getDefaultValue(null);
    String[] values = StringUtils.split(value, whitespaceChars());
    if (index < values.length) {
      return values[index];
    }
    return null;
  }
  



  public Object get(int index, Scriptable start)
  {
    Object value = item(index);
    if ((value == null) && (!getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_DOMTOKENLIST_GET_NULL_IF_OUTSIDE))) {
      return Undefined.instance;
    }
    return value;
  }
  
  private void updateAttribute(String value) {
    DomElement domNode = (DomElement)getDomNodeOrDie();
    DomAttr attr = (DomAttr)domNode.getAttributes().getNamedItem(attributeName_);
    if (attr == null) {
      attr = domNode.getPage().createAttribute(attributeName_);
      domNode.setAttributeNode(attr);
    }
    attr.setValue(value);
  }
  
  private int position(String value, String token) {
    int pos = value.indexOf(token);
    if (pos < 0) {
      return -1;
    }
    

    if ((pos != 0) && (!isWhitespache(value.charAt(pos - 1)))) {
      return -1;
    }
    

    int end = pos + token.length();
    if ((end != value.length()) && (!isWhitespache(value.charAt(end)))) {
      return -1;
    }
    return pos;
  }
  
  private String whitespaceChars() {
    if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_DOMTOKENLIST_ENHANCED_WHITESPACE_CHARS)) {
      return " \t\r\n\f\013";
    }
    return " \t\r\n\f";
  }
  
  private boolean isWhitespache(int ch) {
    return whitespaceChars().indexOf(ch) > -1;
  }
}
