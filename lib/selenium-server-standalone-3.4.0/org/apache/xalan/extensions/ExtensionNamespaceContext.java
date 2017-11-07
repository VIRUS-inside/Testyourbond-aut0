package org.apache.xalan.extensions;

import java.util.Iterator;
import javax.xml.namespace.NamespaceContext;
import org.apache.xalan.res.XSLMessages;

























public class ExtensionNamespaceContext
  implements NamespaceContext
{
  public static final String EXSLT_PREFIX = "exslt";
  public static final String EXSLT_URI = "http://exslt.org/common";
  public static final String EXSLT_MATH_PREFIX = "math";
  public static final String EXSLT_MATH_URI = "http://exslt.org/math";
  public static final String EXSLT_SET_PREFIX = "set";
  public static final String EXSLT_SET_URI = "http://exslt.org/sets";
  public static final String EXSLT_STRING_PREFIX = "str";
  public static final String EXSLT_STRING_URI = "http://exslt.org/strings";
  public static final String EXSLT_DATETIME_PREFIX = "datetime";
  public static final String EXSLT_DATETIME_URI = "http://exslt.org/dates-and-times";
  public static final String EXSLT_DYNAMIC_PREFIX = "dyn";
  public static final String EXSLT_DYNAMIC_URI = "http://exslt.org/dynamic";
  public static final String JAVA_EXT_PREFIX = "java";
  public static final String JAVA_EXT_URI = "http://xml.apache.org/xalan/java";
  
  public ExtensionNamespaceContext() {}
  
  public String getNamespaceURI(String prefix)
  {
    if (prefix == null) {
      throw new IllegalArgumentException(XSLMessages.createMessage("ER_NAMESPACE_CONTEXT_NULL_PREFIX", null));
    }
    

    if (prefix.equals(""))
      return "";
    if (prefix.equals("xml"))
      return "http://www.w3.org/XML/1998/namespace";
    if (prefix.equals("xmlns"))
      return "http://www.w3.org/2000/xmlns/";
    if (prefix.equals("exslt"))
      return "http://exslt.org/common";
    if (prefix.equals("math"))
      return "http://exslt.org/math";
    if (prefix.equals("set"))
      return "http://exslt.org/sets";
    if (prefix.equals("str"))
      return "http://exslt.org/strings";
    if (prefix.equals("datetime"))
      return "http://exslt.org/dates-and-times";
    if (prefix.equals("dyn"))
      return "http://exslt.org/dynamic";
    if (prefix.equals("java")) {
      return "http://xml.apache.org/xalan/java";
    }
    return "";
  }
  



  public String getPrefix(String namespace)
  {
    if (namespace == null) {
      throw new IllegalArgumentException(XSLMessages.createMessage("ER_NAMESPACE_CONTEXT_NULL_NAMESPACE", null));
    }
    

    if (namespace.equals("http://www.w3.org/XML/1998/namespace"))
      return "xml";
    if (namespace.equals("http://www.w3.org/2000/xmlns/"))
      return "xmlns";
    if (namespace.equals("http://exslt.org/common"))
      return "exslt";
    if (namespace.equals("http://exslt.org/math"))
      return "math";
    if (namespace.equals("http://exslt.org/sets"))
      return "set";
    if (namespace.equals("http://exslt.org/strings"))
      return "str";
    if (namespace.equals("http://exslt.org/dates-and-times"))
      return "datetime";
    if (namespace.equals("http://exslt.org/dynamic"))
      return "dyn";
    if (namespace.equals("http://xml.apache.org/xalan/java")) {
      return "java";
    }
    return null;
  }
  
  public Iterator getPrefixes(String namespace)
  {
    String result = getPrefix(namespace);
    
    new Iterator() {
      private boolean isFirstIteration;
      private final String val$result;
      
      public boolean hasNext() {
        return isFirstIteration;
      }
      
      public Object next() {
        if (isFirstIteration) {
          isFirstIteration = false;
          return val$result;
        }
        
        return null;
      }
      
      public void remove() {
        throw new UnsupportedOperationException();
      }
    };
  }
}
