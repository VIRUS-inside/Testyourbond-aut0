package com.gargoylesoftware.htmlunit.javascript.host.dom;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.host.NativeFunctionPrefixResolver;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.NativeFunction;
import org.apache.xml.utils.PrefixResolver;





































@JsxClass(browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
public class XPathEvaluator
  extends SimpleScriptable
{
  @JsxConstructor
  public XPathEvaluator() {}
  
  @JsxFunction
  public XPathNSResolver createNSResolver(Node nodeResolver)
  {
    XPathNSResolver resolver = new XPathNSResolver();
    resolver.setElement(nodeResolver);
    resolver.setParentScope(getWindow());
    resolver.setPrototype(getPrototype(resolver.getClass()));
    return resolver;
  }
  










  @JsxFunction
  public XPathResult evaluate(String expression, Object contextNodeObj, Object resolver, int type, Object result)
  {
    XPathResult xPathResult = (XPathResult)result;
    if (xPathResult == null) {
      xPathResult = new XPathResult();
      xPathResult.setParentScope(getParentScope());
      xPathResult.setPrototype(getPrototype(xPathResult.getClass()));
    }
    
    Node contextNode = null;
    if (!(contextNodeObj instanceof Node)) {
      throw Context.reportRuntimeError("Illegal value for parameter 'context'");
    }
    contextNode = (Node)contextNodeObj;
    PrefixResolver prefixResolver = null;
    if ((resolver instanceof PrefixResolver)) {
      prefixResolver = (PrefixResolver)resolver;
    }
    else if ((resolver instanceof NativeFunction)) {
      prefixResolver = new NativeFunctionPrefixResolver((NativeFunction)resolver, contextNode.getParentScope());
    }
    xPathResult.init(contextNode.getDomNodeOrDie().getByXPath(expression, prefixResolver), type);
    return xPathResult;
  }
}
