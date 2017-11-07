package com.gargoylesoftware.htmlunit.activex.javascript.msxml;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;





















public class MSXMLScriptable
  extends SimpleScriptable
{
  private static final Log LOG = LogFactory.getLog(MSXMLScriptable.class);
  
  private MSXMLJavaScriptEnvironment environment_;
  

  public MSXMLScriptable() {}
  
  public void setParentScope(Scriptable m)
  {
    super.setParentScope(m);
    
    if ((m instanceof MSXMLScriptable)) {
      setEnvironment(((MSXMLScriptable)m).getEnvironment());
    }
  }
  







  public SimpleScriptable makeScriptableFor(DomNode domNode)
  {
    Class<? extends MSXMLScriptable> javaScriptClass = null;
    for (Class<?> c = domNode.getClass(); (javaScriptClass == null) && (c != null); c = c.getSuperclass()) {
      javaScriptClass = getEnvironment().getJavaScriptClass(c);
    }
    

    if (javaScriptClass == null)
    {
      MSXMLScriptable scriptable = new XMLDOMElement();
      if (LOG.isDebugEnabled()) {
        LOG.debug("No MSXML JavaScript class found for element <" + domNode.getNodeName() + 
          ">. Using XMLDOMElement");
      }
    }
    else {
      try {
        scriptable = (MSXMLScriptable)javaScriptClass.newInstance();
      } catch (Exception e) {
        MSXMLScriptable scriptable;
        throw Context.throwAsScriptRuntimeEx(e);
      } }
    MSXMLScriptable scriptable;
    initParentScope(domNode, scriptable);
    
    scriptable.setPrototype(getPrototype(javaScriptClass));
    scriptable.setDomNode(domNode);
    scriptable.setEnvironment(getEnvironment());
    
    return scriptable;
  }
  






  public Scriptable getPrototype(Class<? extends SimpleScriptable> javaScriptClass)
  {
    Scriptable prototype = getEnvironment().getPrototype(javaScriptClass);
    if ((prototype == null) && (javaScriptClass != SimpleScriptable.class)) {
      return getPrototype(javaScriptClass.getSuperclass());
    }
    return prototype;
  }
  
  protected boolean isReadOnlySettable(String name, Object value)
  {
    throw ScriptRuntime.typeError3("msg.set.prop.no.setter", 
      name, getClassName(), Context.toString(value));
  }
  



  public String getClassName()
  {
    return "Object";
  }
  


  public MSXMLJavaScriptEnvironment getEnvironment()
  {
    return environment_;
  }
  


  public void setEnvironment(MSXMLJavaScriptEnvironment environment)
  {
    environment_ = environment;
  }
}
