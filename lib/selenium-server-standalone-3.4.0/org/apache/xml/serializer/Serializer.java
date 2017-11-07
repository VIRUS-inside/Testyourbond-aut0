package org.apache.xml.serializer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Properties;
import org.xml.sax.ContentHandler;

public abstract interface Serializer
{
  public abstract void setOutputStream(OutputStream paramOutputStream);
  
  public abstract OutputStream getOutputStream();
  
  public abstract void setWriter(Writer paramWriter);
  
  public abstract Writer getWriter();
  
  public abstract void setOutputFormat(Properties paramProperties);
  
  public abstract Properties getOutputFormat();
  
  public abstract ContentHandler asContentHandler()
    throws IOException;
  
  public abstract DOMSerializer asDOMSerializer()
    throws IOException;
  
  public abstract boolean reset();
  
  public abstract Object asDOM3Serializer()
    throws IOException;
}
