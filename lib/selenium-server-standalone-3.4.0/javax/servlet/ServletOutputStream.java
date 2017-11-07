package javax.servlet;

import java.io.CharConversionException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.ResourceBundle;











































































public abstract class ServletOutputStream
  extends OutputStream
{
  private static final String LSTRING_FILE = "javax.servlet.LocalStrings";
  private static ResourceBundle lStrings = ResourceBundle.getBundle("javax.servlet.LocalStrings");
  










  protected ServletOutputStream() {}
  










  public void print(String s)
    throws IOException
  {
    if (s == null) s = "null";
    int len = s.length();
    for (int i = 0; i < len; i++) {
      char c = s.charAt(i);
      






      if ((c & 0xFF00) != 0) {
        String errMsg = lStrings.getString("err.not_iso8859_1");
        Object[] errArgs = new Object[1];
        errArgs[0] = Character.valueOf(c);
        errMsg = MessageFormat.format(errMsg, errArgs);
        throw new CharConversionException(errMsg);
      }
      write(c);
    }
  }
  




  public void print(boolean b)
    throws IOException
  {
    String msg;
    



    String msg;
    


    if (b) {
      msg = lStrings.getString("value.true");
    } else {
      msg = lStrings.getString("value.false");
    }
    print(msg);
  }
  











  public void print(char c)
    throws IOException
  {
    print(String.valueOf(c));
  }
  













  public void print(int i)
    throws IOException
  {
    print(String.valueOf(i));
  }
  














  public void print(long l)
    throws IOException
  {
    print(String.valueOf(l));
  }
  













  public void print(float f)
    throws IOException
  {
    print(String.valueOf(f));
  }
  












  public void print(double d)
    throws IOException
  {
    print(String.valueOf(d));
  }
  










  public void println()
    throws IOException
  {
    print("\r\n");
  }
  











  public void println(String s)
    throws IOException
  {
    print(s);
    println();
  }
  















  public void println(boolean b)
    throws IOException
  {
    print(b);
    println();
  }
  











  public void println(char c)
    throws IOException
  {
    print(c);
    println();
  }
  












  public void println(int i)
    throws IOException
  {
    print(i);
    println();
  }
  












  public void println(long l)
    throws IOException
  {
    print(l);
    println();
  }
  














  public void println(float f)
    throws IOException
  {
    print(f);
    println();
  }
  













  public void println(double d)
    throws IOException
  {
    print(d);
    println();
  }
  
  public abstract boolean isReady();
  
  public abstract void setWriteListener(WriteListener paramWriteListener);
}
