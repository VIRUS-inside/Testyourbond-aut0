package net.sourceforge.htmlunit.cyberneko.xercesbridge;

import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.XMLDocumentHandler;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.parser.XMLDocumentFilter;
import org.apache.xerces.xni.parser.XMLDocumentSource;





















public abstract class XercesBridge
{
  private static final XercesBridge instance = ;
  

  public XercesBridge() {}
  

  public static XercesBridge getInstance()
  {
    return instance;
  }
  
  private static XercesBridge makeInstance()
  {
    String[] classNames = {
      "net.sourceforge.htmlunit.cyberneko.xercesbridge.XercesBridge_2_3", 
      "net.sourceforge.htmlunit.cyberneko.xercesbridge.XercesBridge_2_2" };
    

    for (int i = 0; i != classNames.length; i++) {
      String className = classNames[i];
      XercesBridge bridge = newInstanceOrNull(className);
      if (bridge != null) {
        return bridge;
      }
    }
    throw new IllegalStateException("Failed to create XercesBridge instance");
  }
  
  private static XercesBridge newInstanceOrNull(String className) {
    try {
      return (XercesBridge)Class.forName(className).newInstance();
    }
    catch (ClassNotFoundException localClassNotFoundException) {}catch (SecurityException localSecurityException) {}catch (LinkageError localLinkageError) {}catch (IllegalArgumentException localIllegalArgumentException) {}catch (IllegalAccessException localIllegalAccessException) {}catch (InstantiationException localInstantiationException) {}
    





    return null;
  }
  
  public void NamespaceContext_declarePrefix(NamespaceContext namespaceContext, String ns, String avalue) {}
  
  public abstract String getVersion();
  
  public abstract void XMLDocumentHandler_startDocument(XMLDocumentHandler paramXMLDocumentHandler, XMLLocator paramXMLLocator, String paramString, NamespaceContext paramNamespaceContext, Augmentations paramAugmentations);
  
  public void XMLDocumentHandler_startPrefixMapping(XMLDocumentHandler documentHandler, String prefix, String uri, Augmentations augs) {}
  
  public void XMLDocumentHandler_endPrefixMapping(XMLDocumentHandler documentHandler, String prefix, Augmentations augs) {}
  
  public void XMLDocumentFilter_setDocumentSource(XMLDocumentFilter filter, XMLDocumentSource lastSource) {}
}
