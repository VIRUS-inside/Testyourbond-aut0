package org.apache.xml.serialize;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import org.xml.sax.ContentHandler;
import org.xml.sax.DocumentHandler;

/**
 * @deprecated
 */
public abstract interface Serializer
{
  public abstract void setOutputByteStream(OutputStream paramOutputStream);
  
  public abstract void setOutputCharStream(Writer paramWriter);
  
  public abstract void setOutputFormat(OutputFormat paramOutputFormat);
  
  public abstract DocumentHandler asDocumentHandler()
    throws IOException;
  
  public abstract ContentHandler asContentHandler()
    throws IOException;
  
  public abstract DOMSerializer asDOMSerializer()
    throws IOException;
}
