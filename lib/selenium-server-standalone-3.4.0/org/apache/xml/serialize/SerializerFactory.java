package org.apache.xml.serialize;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Hashtable;
import java.util.StringTokenizer;

/**
 * @deprecated
 */
public abstract class SerializerFactory
{
  public static final String FactoriesProperty = "org.apache.xml.serialize.factories";
  private static Hashtable _factories = new Hashtable();
  
  public SerializerFactory() {}
  
  public static void registerSerializerFactory(SerializerFactory paramSerializerFactory)
  {
    synchronized (_factories)
    {
      String str = paramSerializerFactory.getSupportedMethod();
      _factories.put(str, paramSerializerFactory);
    }
  }
  
  public static SerializerFactory getSerializerFactory(String paramString)
  {
    return (SerializerFactory)_factories.get(paramString);
  }
  
  protected abstract String getSupportedMethod();
  
  public abstract Serializer makeSerializer(OutputFormat paramOutputFormat);
  
  public abstract Serializer makeSerializer(Writer paramWriter, OutputFormat paramOutputFormat);
  
  public abstract Serializer makeSerializer(OutputStream paramOutputStream, OutputFormat paramOutputFormat)
    throws UnsupportedEncodingException;
  
  static
  {
    Object localObject = new SerializerFactoryImpl("xml");
    registerSerializerFactory((SerializerFactory)localObject);
    localObject = new SerializerFactoryImpl("html");
    registerSerializerFactory((SerializerFactory)localObject);
    localObject = new SerializerFactoryImpl("xhtml");
    registerSerializerFactory((SerializerFactory)localObject);
    localObject = new SerializerFactoryImpl("text");
    registerSerializerFactory((SerializerFactory)localObject);
    String str1 = SecuritySupport.getSystemProperty("org.apache.xml.serialize.factories");
    if (str1 != null)
    {
      StringTokenizer localStringTokenizer = new StringTokenizer(str1, " ;,:");
      while (localStringTokenizer.hasMoreTokens())
      {
        String str2 = localStringTokenizer.nextToken();
        try
        {
          localObject = (SerializerFactory)ObjectFactory.newInstance(str2, SerializerFactory.class.getClassLoader(), true);
          if (_factories.containsKey(((SerializerFactory)localObject).getSupportedMethod())) {
            _factories.put(((SerializerFactory)localObject).getSupportedMethod(), localObject);
          }
        }
        catch (Exception localException) {}
      }
    }
  }
}
