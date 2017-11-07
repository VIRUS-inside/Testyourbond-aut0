package org.openqa.selenium.io;

import java.io.IOException;
import java.io.OutputStream;



















public class MultiOutputStream
  extends OutputStream
{
  private final OutputStream mandatory;
  private final OutputStream optional;
  
  public MultiOutputStream(OutputStream mandatory, OutputStream optional)
  {
    this.mandatory = mandatory;
    this.optional = optional;
  }
  
  public void write(int b) throws IOException
  {
    mandatory.write(b);
    if (optional != null) {
      optional.write(b);
    }
  }
  
  public void flush() throws IOException
  {
    mandatory.flush();
    if (optional != null) {
      optional.flush();
    }
  }
  
  public void close() throws IOException
  {
    mandatory.close();
    if (optional != null) {
      optional.close();
    }
  }
}
