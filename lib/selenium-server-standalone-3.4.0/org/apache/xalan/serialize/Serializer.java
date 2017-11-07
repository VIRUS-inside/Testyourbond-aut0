package org.apache.xalan.serialize;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Properties;
import org.xml.sax.ContentHandler;

/**
 * @deprecated
 */
public abstract interface Serializer
{
  /**
   * @deprecated
   */
  public abstract void setOutputStream(OutputStream paramOutputStream);
  
  /**
   * @deprecated
   */
  public abstract OutputStream getOutputStream();
  
  /**
   * @deprecated
   */
  public abstract void setWriter(Writer paramWriter);
  
  /**
   * @deprecated
   */
  public abstract Writer getWriter();
  
  /**
   * @deprecated
   */
  public abstract void setOutputFormat(Properties paramProperties);
  
  /**
   * @deprecated
   */
  public abstract Properties getOutputFormat();
  
  /**
   * @deprecated
   */
  public abstract ContentHandler asContentHandler()
    throws IOException;
  
  /**
   * @deprecated
   */
  public abstract DOMSerializer asDOMSerializer()
    throws IOException;
  
  /**
   * @deprecated
   */
  public abstract boolean reset();
}
