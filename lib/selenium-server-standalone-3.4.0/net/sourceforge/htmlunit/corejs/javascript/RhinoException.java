package net.sourceforge.htmlunit.corejs.javascript;

import java.io.CharArrayWriter;
import java.io.FilenameFilter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;









public abstract class RhinoException
  extends RuntimeException
{
  private static final Pattern JAVA_STACK_PATTERN = Pattern.compile("_c_(.*)_\\d+");
  static final long serialVersionUID = 1883500631321581169L;
  
  RhinoException() { Evaluator e = Context.createInterpreter();
    if (e != null)
      e.captureStackInfo(this);
  }
  
  RhinoException(String details) {
    super(details);
    Evaluator e = Context.createInterpreter();
    if (e != null) {
      e.captureStackInfo(this);
    }
  }
  
  public final String getMessage() {
    String details = details();
    if ((sourceName == null) || (lineNumber <= 0)) {
      return details;
    }
    StringBuilder buf = new StringBuilder(details);
    buf.append(" (");
    if (sourceName != null) {
      buf.append(sourceName);
    }
    if (lineNumber > 0) {
      buf.append('#');
      buf.append(lineNumber);
    }
    buf.append(')');
    return buf.toString();
  }
  
  public String details() {
    return super.getMessage();
  }
  



  public final String sourceName()
  {
    return sourceName;
  }
  









  public final void initSourceName(String sourceName)
  {
    if (sourceName == null)
      throw new IllegalArgumentException();
    if (this.sourceName != null)
      throw new IllegalStateException();
    this.sourceName = sourceName;
  }
  



  public final int lineNumber()
  {
    return lineNumber;
  }
  









  public final void initLineNumber(int lineNumber)
  {
    if (lineNumber <= 0)
      throw new IllegalArgumentException(String.valueOf(lineNumber));
    if (this.lineNumber > 0)
      throw new IllegalStateException();
    this.lineNumber = lineNumber;
  }
  


  public final int columnNumber()
  {
    return columnNumber;
  }
  









  public final void initColumnNumber(int columnNumber)
  {
    if (columnNumber <= 0)
      throw new IllegalArgumentException(String.valueOf(columnNumber));
    if (this.columnNumber > 0)
      throw new IllegalStateException();
    this.columnNumber = columnNumber;
  }
  


  public final String lineSource()
  {
    return lineSource;
  }
  









  public final void initLineSource(String lineSource)
  {
    if (lineSource == null)
      throw new IllegalArgumentException();
    if (this.lineSource != null)
      throw new IllegalStateException();
    this.lineSource = lineSource;
  }
  

  final void recordErrorOrigin(String sourceName, int lineNumber, String lineSource, int columnNumber)
  {
    if (lineNumber == -1) {
      lineNumber = 0;
    }
    
    if (sourceName != null) {
      initSourceName(sourceName);
    }
    if (lineNumber != 0) {
      initLineNumber(lineNumber);
    }
    if (lineSource != null) {
      initLineSource(lineSource);
    }
    if (columnNumber != 0) {
      initColumnNumber(columnNumber);
    }
  }
  
  private String generateStackTrace()
  {
    CharArrayWriter writer = new CharArrayWriter();
    super.printStackTrace(new PrintWriter(writer));
    String origStackTrace = writer.toString();
    Evaluator e = Context.createInterpreter();
    if (e != null)
      return e.getPatchedStack(this, origStackTrace);
    return null;
  }
  








  public String getScriptStackTrace()
  {
    return getScriptStackTrace(-1, null);
  }
  















  public String getScriptStackTrace(int limit, String functionName)
  {
    ScriptStackElement[] stack = getScriptStack(limit, functionName);
    return formatStackTrace(stack, details());
  }
  
  static String formatStackTrace(ScriptStackElement[] stack, String message) {
    StringBuilder buffer = new StringBuilder();
    
    String lineSeparator = SecurityUtilities.getSystemProperty("line.separator");
    
    if ((stackStyle == StackStyle.V8) && (!"null".equals(message)))
    {
      buffer.append(message);
      buffer.append(lineSeparator);
    }
    
    for (ScriptStackElement elem : stack) {
      switch (1.$SwitchMap$net$sourceforge$htmlunit$corejs$javascript$StackStyle[stackStyle.ordinal()]) {
      case 1: 
        elem.renderMozillaStyle(buffer);
        break;
      case 2: 
        elem.renderV8Style(buffer);
        break;
      case 3: 
        elem.renderJavaStyle(buffer);
      }
      
      buffer.append(lineSeparator);
    }
    return buffer.toString();
  }
  










  @Deprecated
  public String getScriptStackTrace(FilenameFilter filter)
  {
    return getScriptStackTrace();
  }
  








  public ScriptStackElement[] getScriptStack()
  {
    return getScriptStack(-1, null);
  }
  













  public ScriptStackElement[] getScriptStack(int limit, String hideFunction)
  {
    List<ScriptStackElement> list = new ArrayList();
    ScriptStackElement[][] interpreterStack = (ScriptStackElement[][])null;
    if (interpreterStackInfo != null) {
      Evaluator interpreter = Context.createInterpreter();
      if ((interpreter instanceof Interpreter))
      {
        interpreterStack = ((Interpreter)interpreter).getScriptStackElements(this);
      }
    }
    int interpreterStackIndex = 0;
    StackTraceElement[] stack = getStackTrace();
    int count = 0;
    boolean printStarted = hideFunction == null;
    



    for (StackTraceElement e : stack) {
      String fileName = e.getFileName();
      String methodName; Matcher match; if ((e.getMethodName().startsWith("_c_")) && (e.getLineNumber() > -1) && (fileName != null) && 
        (!fileName.endsWith(".java"))) {
        methodName = e.getMethodName();
        match = JAVA_STACK_PATTERN.matcher(methodName);
        



        methodName = (!"_c_script_0".equals(methodName)) && (match.find()) ? match.group(1) : null;
        
        if ((!printStarted) && (hideFunction.equals(methodName))) {
          printStarted = true;
        } else if ((printStarted) && ((limit < 0) || (count < limit))) {
          list.add(new ScriptStackElement(fileName, methodName, e
            .getLineNumber()));
          count++;
        }
        
      }
      else if (("net.sourceforge.htmlunit.corejs.javascript.Interpreter".equals(e.getClassName())) && 
        ("interpretLoop".equals(e.getMethodName())) && (interpreterStack != null) && (interpreterStack.length > interpreterStackIndex))
      {


        methodName = interpreterStack[(interpreterStackIndex++)];match = methodName.length; for (Matcher localMatcher1 = 0; localMatcher1 < match; localMatcher1++) { ScriptStackElement elem = methodName[localMatcher1];
          if ((!printStarted) && 
            (hideFunction.equals(functionName))) {
            printStarted = true;
          } else if ((printStarted) && ((limit < 0) || (count < limit)))
          {
            list.add(elem);
            count++;
          }
        }
      }
    }
    return (ScriptStackElement[])list.toArray(new ScriptStackElement[list.size()]);
  }
  
  public void printStackTrace(PrintWriter s)
  {
    if (interpreterStackInfo == null) {
      super.printStackTrace(s);
    } else {
      s.print(generateStackTrace());
    }
  }
  
  public void printStackTrace(PrintStream s)
  {
    if (interpreterStackInfo == null) {
      super.printStackTrace(s);
    } else {
      s.print(generateStackTrace());
    }
  }
  










  public static boolean usesMozillaStackStyle()
  {
    return stackStyle == StackStyle.MOZILLA;
  }
  













  public static void useMozillaStackStyle(boolean flag)
  {
    stackStyle = flag ? StackStyle.MOZILLA : StackStyle.RHINO;
  }
  









  public static void setStackStyle(StackStyle style)
  {
    stackStyle = style;
  }
  



  public static StackStyle getStackStyle()
  {
    return stackStyle;
  }
  



  private static StackStyle stackStyle = StackStyle.RHINO;
  
  private String sourceName;
  
  private int lineNumber;
  private String lineSource;
  private int columnNumber;
  Object interpreterStackInfo;
  int[] interpreterLineData;
  
  static
  {
    String style = System.getProperty("rhino.stack.style");
    if (style != null) {
      if ("Rhino".equalsIgnoreCase(style)) {
        stackStyle = StackStyle.RHINO;
      } else if ("Mozilla".equalsIgnoreCase(style)) {
        stackStyle = StackStyle.MOZILLA;
      } else if ("V8".equalsIgnoreCase(style)) {
        stackStyle = StackStyle.V8;
      }
    }
  }
}
