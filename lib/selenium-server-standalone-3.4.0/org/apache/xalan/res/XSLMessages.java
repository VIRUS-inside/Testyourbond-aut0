package org.apache.xalan.res;

import java.util.ListResourceBundle;
import org.apache.xpath.res.XPATHMessages;




























public class XSLMessages
  extends XPATHMessages
{
  private static ListResourceBundle XSLTBundle = null;
  



  private static final String XSLT_ERROR_RESOURCES = "org.apache.xalan.res.XSLTErrorResources";
  




  public XSLMessages() {}
  



  public static final String createMessage(String msgKey, Object[] args)
  {
    if (XSLTBundle == null) {
      XSLTBundle = loadResourceBundle("org.apache.xalan.res.XSLTErrorResources");
    }
    if (XSLTBundle != null)
    {
      return createMsg(XSLTBundle, msgKey, args);
    }
    
    return "Could not load any resource bundles.";
  }
  










  public static final String createWarning(String msgKey, Object[] args)
  {
    if (XSLTBundle == null) {
      XSLTBundle = loadResourceBundle("org.apache.xalan.res.XSLTErrorResources");
    }
    if (XSLTBundle != null)
    {
      return createMsg(XSLTBundle, msgKey, args);
    }
    
    return "Could not load any resource bundles.";
  }
}
