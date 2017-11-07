package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;
































public class BrokenInputStream
  extends InputStream
{
  private final IOException exception;
  
  public BrokenInputStream(IOException exception)
  {
    this.exception = exception;
  }
  


  public BrokenInputStream()
  {
    this(new IOException("Broken input stream"));
  }
  





  public int read()
    throws IOException
  {
    throw exception;
  }
  





  public int available()
    throws IOException
  {
    throw exception;
  }
  






  public long skip(long n)
    throws IOException
  {
    throw exception;
  }
  




  public synchronized void reset()
    throws IOException
  {
    throw exception;
  }
  




  public void close()
    throws IOException
  {
    throw exception;
  }
}
