package com.gargoylesoftware.htmlunit.javascript.host;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.NativeFunction;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import org.apache.xml.utils.PrefixResolver;
import org.w3c.dom.Node;
























public class NativeFunctionPrefixResolver
  implements PrefixResolver
{
  private NativeFunction resolverFn_;
  private Scriptable scope_;
  
  public NativeFunctionPrefixResolver(NativeFunction resolverFn, Scriptable scope)
  {
    resolverFn_ = resolverFn;
    scope_ = scope;
  }
  



  public String getBaseIdentifier()
  {
    Object result = Context.call(null, resolverFn_, scope_, null, new Object[0]);
    return result != null ? result.toString() : null;
  }
  



  public String getNamespaceForPrefix(String prefix)
  {
    Object result = Context.call(null, resolverFn_, scope_, null, new Object[] { prefix });
    return result != null ? result.toString() : null;
  }
  



  public String getNamespaceForPrefix(String prefix, Node node)
  {
    throw new UnsupportedOperationException();
  }
  



  public boolean handlesNullPrefixes()
  {
    return false;
  }
}
