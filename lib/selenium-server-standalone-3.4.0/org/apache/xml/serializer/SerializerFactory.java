package org.apache.xml.serializer;

import java.util.Hashtable;
import java.util.Properties;
import org.apache.xml.serializer.utils.Messages;
import org.apache.xml.serializer.utils.Utils;
import org.apache.xml.serializer.utils.WrappedRuntimeException;
import org.xml.sax.ContentHandler;






























































public final class SerializerFactory
{
  private static Hashtable m_formats = new Hashtable();
  





  private SerializerFactory() {}
  





  public static Serializer getSerializer(Properties format)
  {
    Serializer ser;
    



    try
    {
      String method = format.getProperty("method");
      
      if (method == null) {
        String msg = Utils.messages.createMessage("ER_FACTORY_PROPERTY_MISSING", new Object[] { "method" });
        

        throw new IllegalArgumentException(msg);
      }
      
      String className = format.getProperty("{http://xml.apache.org/xalan}content-handler");
      


      if (null == className)
      {

        Properties methodDefaults = OutputPropertiesFactory.getDefaultMethodProperties(method);
        
        className = methodDefaults.getProperty("{http://xml.apache.org/xalan}content-handler");
        
        if (null == className) {
          String msg = Utils.messages.createMessage("ER_FACTORY_PROPERTY_MISSING", new Object[] { "{http://xml.apache.org/xalan}content-handler" });
          

          throw new IllegalArgumentException(msg);
        }
      }
      



      ClassLoader loader = ObjectFactory.findClassLoader();
      
      Class cls = ObjectFactory.findProviderClass(className, loader, true);
      


      Object obj = cls.newInstance();
      
      if ((obj instanceof SerializationHandler))
      {

        Serializer ser = (Serializer)cls.newInstance();
        ser.setOutputFormat(format);
      }
      else
      {
        Serializer ser;
        


        if ((obj instanceof ContentHandler))
        {







          className = SerializerConstants.DEFAULT_SAX_SERIALIZER;
          cls = ObjectFactory.findProviderClass(className, loader, true);
          SerializationHandler sh = (SerializationHandler)cls.newInstance();
          
          sh.setContentHandler((ContentHandler)obj);
          sh.setOutputFormat(format);
          
          ser = sh;

        }
        else
        {

          throw new Exception(Utils.messages.createMessage("ER_SERIALIZER_NOT_CONTENTHANDLER", new Object[] { className }));
        }
        
      }
      

    }
    catch (Exception e)
    {

      throw new WrappedRuntimeException(e);
    }
    

    return ser;
  }
}
