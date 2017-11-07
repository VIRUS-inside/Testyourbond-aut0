package org.apache.xpath.jaxp;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathFactoryConfigurationException;
import javax.xml.xpath.XPathFunctionResolver;
import javax.xml.xpath.XPathVariableResolver;
import org.apache.xalan.res.XSLMessages;


































public class XPathFactoryImpl
  extends XPathFactory
{
  private static final String CLASS_NAME = "XPathFactoryImpl";
  private XPathFunctionResolver xPathFunctionResolver = null;
  



  private XPathVariableResolver xPathVariableResolver = null;
  



  private boolean featureSecureProcessing = false;
  






  public XPathFactoryImpl() {}
  





  public boolean isObjectModelSupported(String objectModel)
  {
    if (objectModel == null) {
      String fmsg = XSLMessages.createXPATHMessage("ER_OBJECT_MODEL_NULL", new Object[] { getClass().getName() });
      


      throw new NullPointerException(fmsg);
    }
    
    if (objectModel.length() == 0) {
      String fmsg = XSLMessages.createXPATHMessage("ER_OBJECT_MODEL_EMPTY", new Object[] { getClass().getName() });
      

      throw new IllegalArgumentException(fmsg);
    }
    

    if (objectModel.equals("http://java.sun.com/jaxp/xpath/dom")) {
      return true;
    }
    

    return false;
  }
  





  public XPath newXPath()
  {
    return new XPathImpl(xPathVariableResolver, xPathFunctionResolver, featureSecureProcessing);
  }
  





























  public void setFeature(String name, boolean value)
    throws XPathFactoryConfigurationException
  {
    if (name == null) {
      String fmsg = XSLMessages.createXPATHMessage("ER_FEATURE_NAME_NULL", new Object[] { "XPathFactoryImpl", value ? Boolean.TRUE : Boolean.FALSE });
      

      throw new NullPointerException(fmsg);
    }
    

    if (name.equals("http://javax.xml.XMLConstants/feature/secure-processing"))
    {
      featureSecureProcessing = value;
      

      return;
    }
    

    String fmsg = XSLMessages.createXPATHMessage("ER_FEATURE_UNKNOWN", new Object[] { name, "XPathFactoryImpl", value ? Boolean.TRUE : Boolean.FALSE });
    

    throw new XPathFactoryConfigurationException(fmsg);
  }
  
























  public boolean getFeature(String name)
    throws XPathFactoryConfigurationException
  {
    if (name == null) {
      String fmsg = XSLMessages.createXPATHMessage("ER_GETTING_NULL_FEATURE", new Object[] { "XPathFactoryImpl" });
      

      throw new NullPointerException(fmsg);
    }
    

    if (name.equals("http://javax.xml.XMLConstants/feature/secure-processing")) {
      return featureSecureProcessing;
    }
    

    String fmsg = XSLMessages.createXPATHMessage("ER_GETTING_UNKNOWN_FEATURE", new Object[] { name, "XPathFactoryImpl" });
    


    throw new XPathFactoryConfigurationException(fmsg);
  }
  















  public void setXPathFunctionResolver(XPathFunctionResolver resolver)
  {
    if (resolver == null) {
      String fmsg = XSLMessages.createXPATHMessage("ER_NULL_XPATH_FUNCTION_RESOLVER", new Object[] { "XPathFactoryImpl" });
      

      throw new NullPointerException(fmsg);
    }
    
    xPathFunctionResolver = resolver;
  }
  














  public void setXPathVariableResolver(XPathVariableResolver resolver)
  {
    if (resolver == null) {
      String fmsg = XSLMessages.createXPATHMessage("ER_NULL_XPATH_VARIABLE_RESOLVER", new Object[] { "XPathFactoryImpl" });
      

      throw new NullPointerException(fmsg);
    }
    
    xPathVariableResolver = resolver;
  }
}
