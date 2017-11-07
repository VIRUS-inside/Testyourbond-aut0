package net.sourceforge.htmlunit.corejs.javascript.tools;

import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ErrorReporter;
import net.sourceforge.htmlunit.corejs.javascript.EvaluatorException;
import net.sourceforge.htmlunit.corejs.javascript.RhinoException;
import net.sourceforge.htmlunit.corejs.javascript.WrappedException;

public class ToolErrorReporter implements ErrorReporter
{
  private static final String messagePrefix = "js: ";
  private boolean hasReportedErrorFlag;
  private boolean reportWarnings;
  private PrintStream err;
  
  public ToolErrorReporter(boolean reportWarnings)
  {
    this(reportWarnings, System.err);
  }
  
  public ToolErrorReporter(boolean reportWarnings, PrintStream err) {
    this.reportWarnings = reportWarnings;
    this.err = err;
  }
  




  public static String getMessage(String messageId)
  {
    return getMessage(messageId, (Object[])null);
  }
  
  public static String getMessage(String messageId, String argument) {
    Object[] args = { argument };
    return getMessage(messageId, args);
  }
  
  public static String getMessage(String messageId, Object arg1, Object arg2)
  {
    Object[] args = { arg1, arg2 };
    return getMessage(messageId, args);
  }
  
  public static String getMessage(String messageId, Object[] args) {
    Context cx = Context.getCurrentContext();
    Locale locale = cx == null ? Locale.getDefault() : cx.getLocale();
    

    ResourceBundle rb = ResourceBundle.getBundle("net.sourceforge.htmlunit.corejs.javascript.tools.resources.Messages", locale);
    


    try
    {
      formatString = rb.getString(messageId);
    } catch (MissingResourceException mre) { String formatString;
      throw new RuntimeException("no message resource found for message property " + messageId);
    }
    
    String formatString;
    
    if (args == null) {
      return formatString;
    }
    MessageFormat formatter = new MessageFormat(formatString);
    return formatter.format(args);
  }
  
  private static String getExceptionMessage(RhinoException ex) {
    String msg;
    String msg;
    if ((ex instanceof net.sourceforge.htmlunit.corejs.javascript.JavaScriptException)) {
      msg = getMessage("msg.uncaughtJSException", ex.details()); } else { String msg;
      if ((ex instanceof net.sourceforge.htmlunit.corejs.javascript.EcmaError)) {
        msg = getMessage("msg.uncaughtEcmaError", ex.details()); } else { String msg;
        if ((ex instanceof EvaluatorException)) {
          msg = ex.details();
        } else
          msg = ex.toString();
      } }
    return msg;
  }
  
  public void warning(String message, String sourceName, int line, String lineSource, int lineOffset)
  {
    if (!reportWarnings)
      return;
    reportErrorMessage(message, sourceName, line, lineSource, lineOffset, true);
  }
  

  public void error(String message, String sourceName, int line, String lineSource, int lineOffset)
  {
    hasReportedErrorFlag = true;
    reportErrorMessage(message, sourceName, line, lineSource, lineOffset, false);
  }
  

  public EvaluatorException runtimeError(String message, String sourceName, int line, String lineSource, int lineOffset)
  {
    return new EvaluatorException(message, sourceName, line, lineSource, lineOffset);
  }
  
  public boolean hasReportedError()
  {
    return hasReportedErrorFlag;
  }
  
  public boolean isReportingWarnings() {
    return reportWarnings;
  }
  
  public void setIsReportingWarnings(boolean reportWarnings) {
    this.reportWarnings = reportWarnings;
  }
  
  public static void reportException(ErrorReporter er, RhinoException ex) {
    if ((er instanceof ToolErrorReporter)) {
      ((ToolErrorReporter)er).reportException(ex);
    } else {
      String msg = getExceptionMessage(ex);
      er.error(msg, ex.sourceName(), ex.lineNumber(), ex.lineSource(), ex
        .columnNumber());
    }
  }
  
  public void reportException(RhinoException ex) {
    if ((ex instanceof WrappedException)) {
      WrappedException we = (WrappedException)ex;
      we.printStackTrace(err);
    }
    else {
      String lineSeparator = net.sourceforge.htmlunit.corejs.javascript.SecurityUtilities.getSystemProperty("line.separator");
      
      String msg = getExceptionMessage(ex) + lineSeparator + ex.getScriptStackTrace();
      reportErrorMessage(msg, ex.sourceName(), ex.lineNumber(), ex
        .lineSource(), ex.columnNumber(), false);
    }
  }
  
  private void reportErrorMessage(String message, String sourceName, int line, String lineSource, int lineOffset, boolean justWarning)
  {
    if (line > 0) {
      String lineStr = String.valueOf(line);
      if (sourceName != null) {
        Object[] args = { sourceName, lineStr, message };
        message = getMessage("msg.format3", args);
      } else {
        Object[] args = { lineStr, message };
        message = getMessage("msg.format2", args);
      }
    } else {
      Object[] args = { message };
      message = getMessage("msg.format1", args);
    }
    if (justWarning) {
      message = getMessage("msg.warning", message);
    }
    err.println("js: " + message);
    if (null != lineSource) {
      err.println("js: " + lineSource);
      err.println("js: " + buildIndicator(lineOffset));
    }
  }
  
  private String buildIndicator(int offset) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < offset - 1; i++)
      sb.append(".");
    sb.append("^");
    return sb.toString();
  }
}
