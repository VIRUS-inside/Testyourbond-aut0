package net.sourceforge.htmlunit.corejs.javascript;










class DefaultErrorReporter
  implements ErrorReporter
{
  static final DefaultErrorReporter instance = new DefaultErrorReporter();
  
  private boolean forEval;
  private ErrorReporter chainedReporter;
  
  private DefaultErrorReporter() {}
  
  static ErrorReporter forEval(ErrorReporter reporter)
  {
    DefaultErrorReporter r = new DefaultErrorReporter();
    forEval = true;
    chainedReporter = reporter;
    return r;
  }
  
  public void warning(String message, String sourceURI, int line, String lineText, int lineOffset)
  {
    if (chainedReporter != null) {
      chainedReporter.warning(message, sourceURI, line, lineText, lineOffset);
    }
  }
  



  public void error(String message, String sourceURI, int line, String lineText, int lineOffset)
  {
    if (forEval)
    {


      String error = "SyntaxError";
      String TYPE_ERROR_NAME = "TypeError";
      String DELIMETER = ": ";
      String prefix = "TypeError: ";
      if (message.startsWith("TypeError: ")) {
        error = "TypeError";
        message = message.substring("TypeError: ".length());
      }
      throw ScriptRuntime.constructError(error, message, sourceURI, line, lineText, lineOffset);
    }
    
    if (chainedReporter != null) {
      chainedReporter.error(message, sourceURI, line, lineText, lineOffset);
    }
    else {
      throw runtimeError(message, sourceURI, line, lineText, lineOffset);
    }
  }
  
  public EvaluatorException runtimeError(String message, String sourceURI, int line, String lineText, int lineOffset)
  {
    if (chainedReporter != null) {
      return chainedReporter.runtimeError(message, sourceURI, line, lineText, lineOffset);
    }
    
    return new EvaluatorException(message, sourceURI, line, lineText, lineOffset);
  }
}
