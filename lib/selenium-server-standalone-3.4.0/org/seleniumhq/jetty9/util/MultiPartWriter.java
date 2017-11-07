package org.seleniumhq.jetty9.util;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;































public class MultiPartWriter
  extends FilterWriter
{
  private static final String __CRLF = "\r\n";
  private static final String __DASHDASH = "--";
  public static final String MULTIPART_MIXED = "multipart/mixed";
  public static final String MULTIPART_X_MIXED_REPLACE = "multipart/x-mixed-replace";
  private String boundary;
  private boolean inPart = false;
  

  public MultiPartWriter(Writer out)
    throws IOException
  {
    super(out);
    
    boundary = ("jetty" + System.identityHashCode(this) + Long.toString(System.currentTimeMillis(), 36));
    
    inPart = false;
  }
  





  public void close()
    throws IOException
  {
    try
    {
      if (inPart)
        out.write("\r\n");
      out.write("--");
      out.write(boundary);
      out.write("--");
      out.write("\r\n");
      inPart = false;
      


      super.close(); } finally { super.close();
    }
  }
  

  public String getBoundary()
  {
    return boundary;
  }
  





  public void startPart(String contentType)
    throws IOException
  {
    if (inPart)
      out.write("\r\n");
    out.write("--");
    out.write(boundary);
    out.write("\r\n");
    out.write("Content-Type: ");
    out.write(contentType);
    out.write("\r\n");
    out.write("\r\n");
    inPart = true;
  }
  




  public void endPart()
    throws IOException
  {
    if (inPart)
      out.write("\r\n");
    inPart = false;
  }
  






  public void startPart(String contentType, String[] headers)
    throws IOException
  {
    if (inPart)
      out.write("\r\n");
    out.write("--");
    out.write(boundary);
    out.write("\r\n");
    out.write("Content-Type: ");
    out.write(contentType);
    out.write("\r\n");
    for (int i = 0; (headers != null) && (i < headers.length); i++)
    {
      out.write(headers[i]);
      out.write("\r\n");
    }
    out.write("\r\n");
    inPart = true;
  }
}
