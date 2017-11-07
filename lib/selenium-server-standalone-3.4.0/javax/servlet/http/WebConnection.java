package javax.servlet.http;

import java.io.IOException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;

public abstract interface WebConnection
  extends AutoCloseable
{
  public abstract ServletInputStream getInputStream()
    throws IOException;
  
  public abstract ServletOutputStream getOutputStream()
    throws IOException;
}
