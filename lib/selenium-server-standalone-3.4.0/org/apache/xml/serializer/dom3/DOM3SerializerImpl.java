package org.apache.xml.serializer.dom3;

import java.io.IOException;
import org.apache.xml.serializer.DOM3Serializer;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xml.serializer.utils.WrappedRuntimeException;
import org.w3c.dom.DOMErrorHandler;
import org.w3c.dom.Node;
import org.w3c.dom.ls.LSSerializerFilter;
import org.xml.sax.SAXException;








































public final class DOM3SerializerImpl
  implements DOM3Serializer
{
  private DOMErrorHandler fErrorHandler;
  private LSSerializerFilter fSerializerFilter;
  private String fNewLine;
  private SerializationHandler fSerializationHandler;
  
  public DOM3SerializerImpl(SerializationHandler handler)
  {
    fSerializationHandler = handler;
  }
  








  public DOMErrorHandler getErrorHandler()
  {
    return fErrorHandler;
  }
  







  public LSSerializerFilter getNodeFilter()
  {
    return fSerializerFilter;
  }
  


  public char[] getNewLine()
  {
    return fNewLine != null ? fNewLine.toCharArray() : null;
  }
  







  public void serializeDOM3(Node node)
    throws IOException
  {
    try
    {
      DOM3TreeWalker walker = new DOM3TreeWalker(fSerializationHandler, fErrorHandler, fSerializerFilter, fNewLine);
      

      walker.traverse(node);
    } catch (SAXException se) {
      throw new WrappedRuntimeException(se);
    }
  }
  






  public void setErrorHandler(DOMErrorHandler handler)
  {
    fErrorHandler = handler;
  }
  







  public void setNodeFilter(LSSerializerFilter filter)
  {
    fSerializerFilter = filter;
  }
  






  public void setSerializationHandler(SerializationHandler handler)
  {
    fSerializationHandler = handler;
  }
  



  public void setNewLine(char[] newLine)
  {
    fNewLine = (newLine != null ? new String(newLine) : null);
  }
}
