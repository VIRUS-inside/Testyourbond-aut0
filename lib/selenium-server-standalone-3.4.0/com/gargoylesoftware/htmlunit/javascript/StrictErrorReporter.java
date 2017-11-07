package com.gargoylesoftware.htmlunit.javascript;

import java.io.Serializable;
import net.sourceforge.htmlunit.corejs.javascript.ErrorReporter;
import net.sourceforge.htmlunit.corejs.javascript.EvaluatorException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;























public class StrictErrorReporter
  implements ErrorReporter, Serializable
{
  private static final Log LOG = LogFactory.getLog(StrictErrorReporter.class);
  





  public StrictErrorReporter() {}
  




  public void warning(String message, String sourceName, int line, String lineSource, int lineOffset)
  {
    LOG.warn(format("warning", message, sourceName, line, lineSource, lineOffset));
  }
  










  public void error(String message, String sourceName, int line, String lineSource, int lineOffset)
  {
    LOG.error(format("error", message, sourceName, line, lineSource, lineOffset));
    throw new EvaluatorException(message, sourceName, line, lineSource, lineOffset);
  }
  












  public EvaluatorException runtimeError(String message, String sourceName, int line, String lineSource, int lineOffset)
  {
    LOG.error(format("runtimeError", message, sourceName, line, lineSource, lineOffset));
    return new EvaluatorException(message, sourceName, line, lineSource, lineOffset);
  }
  

  private static String format(String prefix, String message, String sourceName, int line, String lineSource, int lineOffset)
  {
    return 
      prefix + ": message=[" + message + "] sourceName=[" + sourceName + "] line=[" + line + "] lineSource=[" + lineSource + "] lineOffset=[" + lineOffset + "]";
  }
}
