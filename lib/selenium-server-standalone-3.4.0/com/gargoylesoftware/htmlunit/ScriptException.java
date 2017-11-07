package com.gargoylesoftware.htmlunit;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.StringTokenizer;
import net.sourceforge.htmlunit.corejs.javascript.EcmaError;
import net.sourceforge.htmlunit.corejs.javascript.JavaScriptException;
import net.sourceforge.htmlunit.corejs.javascript.RhinoException;
import net.sourceforge.htmlunit.corejs.javascript.WrappedException;
































public class ScriptException
  extends RuntimeException
{
  private final String scriptSourceCode_;
  private final HtmlPage page_;
  
  public ScriptException(HtmlPage page, Throwable throwable, String scriptSourceCode)
  {
    super(getMessageFrom(throwable), throwable);
    scriptSourceCode_ = scriptSourceCode;
    page_ = page;
  }
  
  private static String getMessageFrom(Throwable throwable) {
    if (throwable == null) {
      return "null";
    }
    return throwable.getMessage();
  }
  




  public ScriptException(HtmlPage page, Throwable throwable)
  {
    this(page, throwable, null);
  }
  




  public void printStackTrace()
  {
    printStackTrace(System.out);
  }
  






  public void printStackTrace(PrintWriter writer)
  {
    writer.write(createPrintableStackTrace());
  }
  






  public void printStackTrace(PrintStream stream)
  {
    stream.print(createPrintableStackTrace());
  }
  
  private String createPrintableStackTrace() {
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    
    printWriter.println("======= EXCEPTION START ========");
    
    if (getCause() != null) {
      if ((getCause() instanceof EcmaError)) {
        EcmaError ecmaError = (EcmaError)getCause();
        printWriter.print("EcmaError: ");
        printWriter.print("lineNumber=[");
        printWriter.print(ecmaError.lineNumber());
        printWriter.print("] column=[");
        printWriter.print(ecmaError.columnNumber());
        printWriter.print("] lineSource=[");
        printWriter.print(getFailingLine());
        printWriter.print("] name=[");
        printWriter.print(ecmaError.getName());
        printWriter.print("] sourceName=[");
        printWriter.print(ecmaError.sourceName());
        printWriter.print("] message=[");
        printWriter.print(ecmaError.getMessage());
        printWriter.print("]");
        printWriter.println();
      }
      else {
        printWriter.println("Exception class=[" + getCause().getClass().getName() + "]");
      }
    }
    
    super.printStackTrace(printWriter);
    if ((getCause() != null) && ((getCause() instanceof JavaScriptException))) {
      Object value = ((JavaScriptException)getCause()).getValue();
      
      printWriter.print("JavaScriptException value = ");
      if ((value instanceof Throwable)) {
        ((Throwable)value).printStackTrace(printWriter);
      }
      else {
        printWriter.println(value);
      }
    }
    else if ((getCause() != null) && ((getCause() instanceof WrappedException))) {
      WrappedException wrappedException = (WrappedException)getCause();
      printWriter.print("WrappedException: ");
      wrappedException.printStackTrace(printWriter);
      
      Throwable innerException = wrappedException.getWrappedException();
      if (innerException == null) {
        printWriter.println("Inside wrapped exception: null");
      }
      else {
        printWriter.println("Inside wrapped exception:");
        innerException.printStackTrace(printWriter);
      }
    }
    else if (getCause() != null) {
      printWriter.println("Enclosed exception: ");
      getCause().printStackTrace(printWriter);
    }
    
    if ((scriptSourceCode_ != null) && (!scriptSourceCode_.isEmpty())) {
      printWriter.println("== CALLING JAVASCRIPT ==");
      printWriter.println(scriptSourceCode_);
    }
    printWriter.println("======= EXCEPTION END ========");
    
    return stringWriter.toString();
  }
  



  public String getScriptSourceCode()
  {
    return scriptSourceCode_;
  }
  






  public String getFailingLine()
  {
    int lineNumber = getFailingLineNumber();
    if ((lineNumber == -1) || (scriptSourceCode_ == null)) {
      return "<no source>";
    }
    try {
      Object localObject1 = null;Object localObject4 = null; Object localObject3; try { BufferedReader reader = new BufferedReader(new StringReader(scriptSourceCode_));
        try { for (int i = 0; i < lineNumber - 1; i++) {
            reader.readLine();
          }
          return reader.readLine();
        } finally { if (reader != null) reader.close(); } } finally { if (localObject2 == null) localObject3 = localThrowable; else if (localObject3 != localThrowable) { localObject3.addSuppressed(localThrowable);
        }
      }
      


      return "";
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  







  public int getFailingLineNumber()
  {
    if ((getCause() instanceof RhinoException)) {
      RhinoException cause = (RhinoException)getCause();
      return cause.lineNumber();
    }
    
    return -1;
  }
  





  public int getFailingColumnNumber()
  {
    if ((getCause() instanceof RhinoException)) {
      RhinoException cause = (RhinoException)getCause();
      return cause.columnNumber();
    }
    
    return -1;
  }
  





  public HtmlPage getPage()
  {
    return page_;
  }
  




  public void printScriptStackTrace(PrintWriter writer)
  {
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    
    getCause().printStackTrace(printWriter);
    
    writer.print(getCause().getMessage());
    StringTokenizer st = new StringTokenizer(stringWriter.toString(), "\r\n");
    while (st.hasMoreTokens()) {
      String line = st.nextToken();
      if (line.contains("at script")) {
        writer.println();
        writer.print(line.replaceFirst("at script\\.?", "at "));
      }
    }
  }
}
