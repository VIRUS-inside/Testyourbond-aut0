package javax.servlet.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

public abstract interface Part
{
  public abstract InputStream getInputStream()
    throws IOException;
  
  public abstract String getContentType();
  
  public abstract String getName();
  
  public abstract String getSubmittedFileName();
  
  public abstract long getSize();
  
  public abstract void write(String paramString)
    throws IOException;
  
  public abstract void delete()
    throws IOException;
  
  public abstract String getHeader(String paramString);
  
  public abstract Collection<String> getHeaders(String paramString);
  
  public abstract Collection<String> getHeaderNames();
}
