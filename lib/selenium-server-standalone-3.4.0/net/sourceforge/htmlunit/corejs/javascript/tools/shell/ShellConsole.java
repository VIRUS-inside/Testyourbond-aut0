package net.sourceforge.htmlunit.corejs.javascript.tools.shell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.charset.Charset;
import net.sourceforge.htmlunit.corejs.javascript.Kit;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;













public abstract class ShellConsole
{
  private static final Class[] NO_ARG = new Class[0];
  private static final Class[] BOOLEAN_ARG = { Boolean.TYPE };
  private static final Class[] STRING_ARG = { String.class };
  private static final Class[] CHARSEQ_ARG = { CharSequence.class };
  


  protected ShellConsole() {}
  


  public abstract InputStream getIn();
  


  public abstract String readLine()
    throws IOException;
  


  public abstract String readLine(String paramString)
    throws IOException;
  


  public abstract void flush()
    throws IOException;
  


  public abstract void print(String paramString)
    throws IOException;
  


  public abstract void println()
    throws IOException;
  

  public abstract void println(String paramString)
    throws IOException;
  

  private static Object tryInvoke(Object obj, String method, Class[] paramTypes, Object... args)
  {
    try
    {
      Method m = obj.getClass().getDeclaredMethod(method, paramTypes);
      if (m != null) {
        return m.invoke(obj, args);
      }
    }
    catch (NoSuchMethodException localNoSuchMethodException) {}catch (IllegalArgumentException localIllegalArgumentException) {}catch (IllegalAccessException localIllegalAccessException) {}catch (InvocationTargetException localInvocationTargetException) {}
    


    return null;
  }
  
  private static class JLineShellConsoleV1
    extends ShellConsole
  {
    private final Object reader;
    private final InputStream in;
    
    JLineShellConsoleV1(Object reader, Charset cs)
    {
      this.reader = reader;
      in = new ShellConsole.ConsoleInputStream(this, cs);
    }
    
    public InputStream getIn()
    {
      return in;
    }
    
    public String readLine() throws IOException
    {
      return (String)ShellConsole.tryInvoke(reader, "readLine", ShellConsole.NO_ARG, new Object[0]);
    }
    
    public String readLine(String prompt) throws IOException
    {
      return (String)ShellConsole.tryInvoke(reader, "readLine", ShellConsole.STRING_ARG, new Object[] { prompt });
    }
    
    public void flush() throws IOException
    {
      ShellConsole.tryInvoke(reader, "flushConsole", ShellConsole.NO_ARG, new Object[0]);
    }
    
    public void print(String s) throws IOException
    {
      ShellConsole.tryInvoke(reader, "printString", ShellConsole.STRING_ARG, new Object[] { s });
    }
    
    public void println() throws IOException
    {
      ShellConsole.tryInvoke(reader, "printNewline", ShellConsole.NO_ARG, new Object[0]);
    }
    
    public void println(String s) throws IOException
    {
      ShellConsole.tryInvoke(reader, "printString", ShellConsole.STRING_ARG, new Object[] { s });
      ShellConsole.tryInvoke(reader, "printNewline", ShellConsole.NO_ARG, new Object[0]);
    }
  }
  
  private static class JLineShellConsoleV2
    extends ShellConsole
  {
    private final Object reader;
    private final InputStream in;
    
    JLineShellConsoleV2(Object reader, Charset cs)
    {
      this.reader = reader;
      in = new ShellConsole.ConsoleInputStream(this, cs);
    }
    
    public InputStream getIn()
    {
      return in;
    }
    
    public String readLine() throws IOException
    {
      return (String)ShellConsole.tryInvoke(reader, "readLine", ShellConsole.NO_ARG, new Object[0]);
    }
    
    public String readLine(String prompt) throws IOException
    {
      return (String)ShellConsole.tryInvoke(reader, "readLine", ShellConsole.STRING_ARG, new Object[] { prompt });
    }
    
    public void flush() throws IOException
    {
      ShellConsole.tryInvoke(reader, "flush", ShellConsole.NO_ARG, new Object[0]);
    }
    
    public void print(String s) throws IOException
    {
      ShellConsole.tryInvoke(reader, "print", ShellConsole.CHARSEQ_ARG, new Object[] { s });
    }
    
    public void println() throws IOException
    {
      ShellConsole.tryInvoke(reader, "println", ShellConsole.NO_ARG, new Object[0]);
    }
    
    public void println(String s) throws IOException
    {
      ShellConsole.tryInvoke(reader, "println", ShellConsole.CHARSEQ_ARG, new Object[] { s });
    }
  }
  


  private static class ConsoleInputStream
    extends InputStream
  {
    private static final byte[] EMPTY = new byte[0];
    private final ShellConsole console;
    private final Charset cs;
    private byte[] buffer = EMPTY;
    private int cursor = -1;
    private boolean atEOF = false;
    
    public ConsoleInputStream(ShellConsole console, Charset cs) {
      this.console = console;
      this.cs = cs;
    }
    
    public synchronized int read(byte[] b, int off, int len)
      throws IOException
    {
      if (b == null)
        throw new NullPointerException();
      if ((off < 0) || (len < 0) || (len > b.length - off))
        throw new IndexOutOfBoundsException();
      if (len == 0) {
        return 0;
      }
      if (!ensureInput()) {
        return -1;
      }
      int n = Math.min(len, buffer.length - cursor);
      for (int i = 0; i < n; i++) {
        b[(off + i)] = buffer[(cursor + i)];
      }
      if (n < len) {
        b[(off + n++)] = 10;
      }
      cursor += n;
      return n;
    }
    
    public synchronized int read() throws IOException
    {
      if (!ensureInput()) {
        return -1;
      }
      if (cursor == buffer.length) {
        cursor += 1;
        return 10;
      }
      return buffer[(cursor++)];
    }
    
    private boolean ensureInput() throws IOException {
      if (atEOF) {
        return false;
      }
      if ((cursor < 0) || (cursor > buffer.length)) {
        if (readNextLine() == -1) {
          atEOF = true;
          return false;
        }
        cursor = 0;
      }
      return true;
    }
    
    private int readNextLine() throws IOException {
      String line = console.readLine(null);
      if (line != null) {
        buffer = line.getBytes(cs);
        return buffer.length;
      }
      buffer = EMPTY;
      return -1;
    }
  }
  
  private static class SimpleShellConsole extends ShellConsole
  {
    private final InputStream in;
    private final PrintWriter out;
    private final BufferedReader reader;
    
    SimpleShellConsole(InputStream in, PrintStream ps, Charset cs) {
      this.in = in;
      out = new PrintWriter(ps);
      reader = new BufferedReader(new InputStreamReader(in, cs));
    }
    
    public InputStream getIn()
    {
      return in;
    }
    
    public String readLine() throws IOException
    {
      return reader.readLine();
    }
    
    public String readLine(String prompt) throws IOException
    {
      if (prompt != null) {
        out.write(prompt);
        out.flush();
      }
      return reader.readLine();
    }
    
    public void flush() throws IOException
    {
      out.flush();
    }
    
    public void print(String s) throws IOException
    {
      out.print(s);
    }
    
    public void println() throws IOException
    {
      out.println();
    }
    
    public void println(String s) throws IOException
    {
      out.println(s);
    }
  }
  




  public static ShellConsole getConsole(InputStream in, PrintStream ps, Charset cs)
  {
    return new SimpleShellConsole(in, ps, cs);
  }
  






  public static ShellConsole getConsole(Scriptable scope, Charset cs)
  {
    ClassLoader classLoader = ShellConsole.class.getClassLoader();
    if (classLoader == null)
    {

      classLoader = ClassLoader.getSystemClassLoader();
    }
    if (classLoader == null)
    {

      return null;
    }
    try
    {
      Class<?> readerClass = Kit.classOrNull(classLoader, "jline.console.ConsoleReader");
      
      if (readerClass != null) {
        return getJLineShellConsoleV2(classLoader, readerClass, scope, cs);
      }
      

      readerClass = Kit.classOrNull(classLoader, "jline.ConsoleReader");
      if (readerClass != null) {
        return getJLineShellConsoleV1(classLoader, readerClass, scope, cs);
      }
    }
    catch (NoSuchMethodException localNoSuchMethodException) {}catch (IllegalAccessException localIllegalAccessException) {}catch (InstantiationException localInstantiationException) {}catch (InvocationTargetException localInvocationTargetException) {}
    



    return null;
  }
  


  private static JLineShellConsoleV1 getJLineShellConsoleV1(ClassLoader classLoader, Class<?> readerClass, Scriptable scope, Charset cs)
    throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException
  {
    Constructor<?> c = readerClass.getConstructor(new Class[0]);
    Object reader = c.newInstance(new Object[0]);
    

    tryInvoke(reader, "setBellEnabled", BOOLEAN_ARG, new Object[] { Boolean.FALSE });
    

    Class<?> completorClass = Kit.classOrNull(classLoader, "jline.Completor");
    
    Object completor = Proxy.newProxyInstance(classLoader, new Class[] { completorClass }, new FlexibleCompletor(completorClass, scope));
    

    tryInvoke(reader, "addCompletor", new Class[] { completorClass }, new Object[] { completor });
    

    return new JLineShellConsoleV1(reader, cs);
  }
  


  private static JLineShellConsoleV2 getJLineShellConsoleV2(ClassLoader classLoader, Class<?> readerClass, Scriptable scope, Charset cs)
    throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException
  {
    Constructor<?> c = readerClass.getConstructor(new Class[0]);
    Object reader = c.newInstance(new Object[0]);
    

    tryInvoke(reader, "setBellEnabled", BOOLEAN_ARG, new Object[] { Boolean.FALSE });
    

    Class<?> completorClass = Kit.classOrNull(classLoader, "jline.console.completer.Completer");
    
    Object completor = Proxy.newProxyInstance(classLoader, new Class[] { completorClass }, new FlexibleCompletor(completorClass, scope));
    

    tryInvoke(reader, "addCompleter", new Class[] { completorClass }, new Object[] { completor });
    

    return new JLineShellConsoleV2(reader, cs);
  }
}
