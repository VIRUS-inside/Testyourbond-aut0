package org.apache.xpath.functions;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Properties;
import javax.xml.transform.TransformerException;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.WrappedRuntimeException;
import org.apache.xpath.Expression;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XString;


































public class FuncSystemProperty
  extends FunctionOneArg
{
  static final long serialVersionUID = 3694874980992204867L;
  static final String XSLT_PROPERTIES = "org/apache/xalan/res/XSLTInfo.properties";
  
  public FuncSystemProperty() {}
  
  public XObject execute(XPathContext xctxt)
    throws TransformerException
  {
    String fullName = m_arg0.execute(xctxt).str();
    int indexOfNSSep = fullName.indexOf(':');
    String result = null;
    String propName = "";
    


    Properties xsltInfo = new Properties();
    
    loadPropertyFile("org/apache/xalan/res/XSLTInfo.properties", xsltInfo);
    
    if (indexOfNSSep > 0)
    {
      String prefix = indexOfNSSep >= 0 ? fullName.substring(0, indexOfNSSep) : "";
      


      String namespace = xctxt.getNamespaceContext().getNamespaceForPrefix(prefix);
      propName = indexOfNSSep < 0 ? fullName : fullName.substring(indexOfNSSep + 1);
      

      if ((namespace.startsWith("http://www.w3.org/XSL/Transform")) || (namespace.equals("http://www.w3.org/1999/XSL/Transform")))
      {

        result = xsltInfo.getProperty(propName);
        
        if (null == result)
        {
          warn(xctxt, "WG_PROPERTY_NOT_SUPPORTED", new Object[] { fullName });
          

          return XString.EMPTYSTRING;
        }
      }
      else
      {
        warn(xctxt, "WG_DONT_DO_ANYTHING_WITH_NS", new Object[] { namespace, fullName });
        



        try
        {
          if (!xctxt.isSecureProcessing())
          {
            result = System.getProperty(propName);
          }
          else
          {
            warn(xctxt, "WG_SECURITY_EXCEPTION", new Object[] { fullName });
          }
          
          if (null == result)
          {
            return XString.EMPTYSTRING;
          }
        }
        catch (SecurityException se)
        {
          warn(xctxt, "WG_SECURITY_EXCEPTION", new Object[] { fullName });
          

          return XString.EMPTYSTRING;
        }
        
      }
    }
    else
    {
      try
      {
        if (!xctxt.isSecureProcessing())
        {
          result = System.getProperty(fullName);
        }
        else
        {
          warn(xctxt, "WG_SECURITY_EXCEPTION", new Object[] { fullName });
        }
        
        if (null == result)
        {
          return XString.EMPTYSTRING;
        }
      }
      catch (SecurityException se)
      {
        warn(xctxt, "WG_SECURITY_EXCEPTION", new Object[] { fullName });
        

        return XString.EMPTYSTRING;
      }
    }
    
    if ((propName.equals("version")) && (result.length() > 0))
    {
      try
      {

        return new XString("1.0");
      }
      catch (Exception ex)
      {
        return new XString(result);
      }
    }
    
    return new XString(result);
  }
  








  public void loadPropertyFile(String file, Properties target)
  {
    try
    {
      InputStream is = SecuritySupport.getResourceAsStream(ObjectFactory.findClassLoader(), file);
      


      BufferedInputStream bis = new BufferedInputStream(is);
      
      target.load(bis);
      bis.close();

    }
    catch (Exception ex)
    {
      throw new WrappedRuntimeException(ex);
    }
  }
}
