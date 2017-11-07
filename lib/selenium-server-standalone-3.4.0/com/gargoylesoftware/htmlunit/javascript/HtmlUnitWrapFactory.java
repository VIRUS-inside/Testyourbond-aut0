package com.gargoylesoftware.htmlunit.javascript;

import java.io.Serializable;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.WrapFactory;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;






















public class HtmlUnitWrapFactory
  extends WrapFactory
  implements Serializable
{
  public HtmlUnitWrapFactory()
  {
    setJavaPrimitiveWrap(false);
  }
  



  public Scriptable wrapAsJavaObject(Context context, Scriptable scope, Object javaObject, Class<?> staticType)
  {
    Scriptable resp;
    

    Scriptable resp;
    

    if ((NodeList.class.equals(staticType)) || 
      (NamedNodeMap.class.equals(staticType))) {
      resp = new ScriptableWrapper(scope, javaObject, staticType);
    }
    else {
      resp = super.wrapAsJavaObject(context, scope, javaObject, 
        staticType);
    }
    return resp;
  }
}
