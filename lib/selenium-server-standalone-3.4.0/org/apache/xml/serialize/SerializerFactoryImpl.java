package org.apache.xml.serialize;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import org.apache.xerces.dom.DOMMessageFormatter;

/**
 * @deprecated
 */
final class SerializerFactoryImpl
  extends SerializerFactory
{
  private String _method;
  
  SerializerFactoryImpl(String paramString)
  {
    _method = paramString;
    if ((!_method.equals("xml")) && (!_method.equals("html")) && (!_method.equals("xhtml")) && (!_method.equals("text")))
    {
      String str = DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "MethodNotSupported", new Object[] { paramString });
      throw new IllegalArgumentException(str);
    }
  }
  
  public Serializer makeSerializer(OutputFormat paramOutputFormat)
  {
    Serializer localSerializer = getSerializer(paramOutputFormat);
    localSerializer.setOutputFormat(paramOutputFormat);
    return localSerializer;
  }
  
  public Serializer makeSerializer(Writer paramWriter, OutputFormat paramOutputFormat)
  {
    Serializer localSerializer = getSerializer(paramOutputFormat);
    localSerializer.setOutputCharStream(paramWriter);
    return localSerializer;
  }
  
  public Serializer makeSerializer(OutputStream paramOutputStream, OutputFormat paramOutputFormat)
    throws UnsupportedEncodingException
  {
    Serializer localSerializer = getSerializer(paramOutputFormat);
    localSerializer.setOutputByteStream(paramOutputStream);
    return localSerializer;
  }
  
  private Serializer getSerializer(OutputFormat paramOutputFormat)
  {
    if (_method.equals("xml")) {
      return new XMLSerializer(paramOutputFormat);
    }
    if (_method.equals("html")) {
      return new HTMLSerializer(paramOutputFormat);
    }
    if (_method.equals("xhtml")) {
      return new XHTMLSerializer(paramOutputFormat);
    }
    if (_method.equals("text")) {
      return new TextSerializer();
    }
    String str = DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "MethodNotSupported", new Object[] { _method });
    throw new IllegalStateException(str);
  }
  
  protected String getSupportedMethod()
  {
    return _method;
  }
}
