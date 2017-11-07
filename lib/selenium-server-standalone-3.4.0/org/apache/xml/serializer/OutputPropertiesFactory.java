package org.apache.xml.serializer;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Enumeration;
import java.util.Properties;
import org.apache.xml.serializer.utils.Messages;
import org.apache.xml.serializer.utils.Utils;
import org.apache.xml.serializer.utils.WrappedRuntimeException;




















































































































































public final class OutputPropertiesFactory
{
  private static final String S_BUILTIN_EXTENSIONS_URL = "http://xml.apache.org/xalan";
  private static final String S_BUILTIN_OLD_EXTENSIONS_URL = "http://xml.apache.org/xslt";
  public static final String S_BUILTIN_EXTENSIONS_UNIVERSAL = "{http://xml.apache.org/xalan}";
  public static final String S_KEY_INDENT_AMOUNT = "{http://xml.apache.org/xalan}indent-amount";
  public static final String S_KEY_LINE_SEPARATOR = "{http://xml.apache.org/xalan}line-separator";
  public static final String S_KEY_CONTENT_HANDLER = "{http://xml.apache.org/xalan}content-handler";
  public static final String S_KEY_ENTITIES = "{http://xml.apache.org/xalan}entities";
  public static final String S_USE_URL_ESCAPING = "{http://xml.apache.org/xalan}use-url-escaping";
  public static final String S_OMIT_META_TAG = "{http://xml.apache.org/xalan}omit-meta-tag";
  public static final String S_BUILTIN_OLD_EXTENSIONS_UNIVERSAL = "{http://xml.apache.org/xslt}";
  public static final int S_BUILTIN_OLD_EXTENSIONS_UNIVERSAL_LEN = "{http://xml.apache.org/xslt}".length();
  


  private static final String S_XSLT_PREFIX = "xslt.output.";
  


  private static final int S_XSLT_PREFIX_LEN = "xslt.output.".length();
  private static final String S_XALAN_PREFIX = "org.apache.xslt.";
  private static final int S_XALAN_PREFIX_LEN = "org.apache.xslt.".length();
  

  private static Integer m_synch_object = new Integer(1);
  

  private static final String PROP_DIR = SerializerBase.PKG_PATH + '/';
  

  private static final String PROP_FILE_XML = "output_xml.properties";
  

  private static final String PROP_FILE_TEXT = "output_text.properties";
  

  private static final String PROP_FILE_HTML = "output_html.properties";
  

  private static final String PROP_FILE_UNKNOWN = "output_unknown.properties";
  

  private static Properties m_xml_properties = null;
  

  private static Properties m_html_properties = null;
  

  private static Properties m_text_properties = null;
  

  private static Properties m_unknown_properties = null;
  

  private static final Class ACCESS_CONTROLLER_CLASS = findAccessControllerClass();
  


  public OutputPropertiesFactory() {}
  


  private static Class findAccessControllerClass()
  {
    try
    {
      return Class.forName("java.security.AccessController");
    }
    catch (Exception e) {}
    




    return null;
  }
  













  public static final Properties getDefaultMethodProperties(String method)
  {
    String fileName = null;
    Properties defaultProperties = null;
    

    try
    {
      synchronized (m_synch_object)
      {
        if (null == m_xml_properties)
        {
          fileName = "output_xml.properties";
          m_xml_properties = loadPropertiesFile(fileName, null);
        }
      }
      
      if (method.equals("xml"))
      {
        defaultProperties = m_xml_properties;
      }
      else if (method.equals("html"))
      {
        if (null == m_html_properties)
        {
          fileName = "output_html.properties";
          m_html_properties = loadPropertiesFile(fileName, m_xml_properties);
        }
        

        defaultProperties = m_html_properties;
      }
      else if (method.equals("text"))
      {
        if (null == m_text_properties)
        {
          fileName = "output_text.properties";
          m_text_properties = loadPropertiesFile(fileName, m_xml_properties);
          
          if (null == m_text_properties.getProperty("encoding"))
          {

            String mimeEncoding = Encodings.getMimeEncoding(null);
            m_text_properties.put("encoding", mimeEncoding);
          }
        }
        


        defaultProperties = m_text_properties;
      }
      else if (method.equals(""))
      {
        if (null == m_unknown_properties)
        {
          fileName = "output_unknown.properties";
          m_unknown_properties = loadPropertiesFile(fileName, m_xml_properties);
        }
        

        defaultProperties = m_unknown_properties;

      }
      else
      {
        defaultProperties = m_xml_properties;
      }
    }
    catch (IOException ioe)
    {
      throw new WrappedRuntimeException(Utils.messages.createMessage("ER_COULD_NOT_LOAD_METHOD_PROPERTY", new Object[] { fileName, method }), ioe);
    }
    





    return new Properties(defaultProperties);
  }
  



















  private static Properties loadPropertiesFile(String resourceName, Properties defaults)
    throws IOException
  {
    Properties props = new Properties(defaults);
    
    InputStream is = null;
    BufferedInputStream bis = null;
    
    try
    {
      if (ACCESS_CONTROLLER_CLASS != null)
      {
        is = (InputStream)AccessController.doPrivileged(new PrivilegedAction() {
          private final String val$resourceName;
          
          public Object run() {
            return OutputPropertiesFactory.class.getResourceAsStream(val$resourceName);
          }
          

        });
      }
      else
      {
        is = OutputPropertiesFactory.class.getResourceAsStream(resourceName);
      }
      

      bis = new BufferedInputStream(is);
      props.load(bis);
    }
    catch (IOException ioe)
    {
      if (defaults == null)
      {
        throw ioe;
      }
      

      throw new WrappedRuntimeException(Utils.messages.createMessage("ER_COULD_NOT_LOAD_RESOURCE", new Object[] { resourceName }), ioe);




    }
    catch (SecurityException se)
    {



      if (defaults == null)
      {
        throw se;
      }
      

      throw new WrappedRuntimeException(Utils.messages.createMessage("ER_COULD_NOT_LOAD_RESOURCE", new Object[] { resourceName }), se);



    }
    finally
    {



      if (bis != null)
      {
        bis.close();
      }
      if (is != null)
      {
        is.close();
      }
    }
    














    Enumeration keys = ((Properties)props.clone()).keys();
    while (keys.hasMoreElements())
    {
      String key = (String)keys.nextElement();
      


      String value = null;
      try
      {
        value = System.getProperty(key);
      }
      catch (SecurityException se) {}
      


      if (value == null) {
        value = (String)props.get(key);
      }
      String newKey = fixupPropertyString(key, true);
      String newValue = null;
      try
      {
        newValue = System.getProperty(newKey);
      }
      catch (SecurityException se) {}
      


      if (newValue == null) {
        newValue = fixupPropertyString(value, false);
      } else {
        newValue = fixupPropertyString(newValue, false);
      }
      if ((key != newKey) || (value != newValue))
      {
        props.remove(key);
        props.put(newKey, newValue);
      }
    }
    

    return props;
  }
  








  private static String fixupPropertyString(String s, boolean doClipping)
  {
    if ((doClipping) && (s.startsWith("xslt.output.")))
    {
      s = s.substring(S_XSLT_PREFIX_LEN);
    }
    if (s.startsWith("org.apache.xslt."))
    {
      s = "{http://xml.apache.org/xalan}" + s.substring(S_XALAN_PREFIX_LEN);
    }
    
    int index;
    if ((index = s.indexOf("\\u003a")) > 0)
    {
      String temp = s.substring(index + 6);
      s = s.substring(0, index) + ":" + temp;
    }
    
    return s;
  }
}
