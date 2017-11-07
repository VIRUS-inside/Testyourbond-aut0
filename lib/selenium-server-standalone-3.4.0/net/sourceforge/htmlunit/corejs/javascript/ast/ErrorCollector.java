package net.sourceforge.htmlunit.corejs.javascript.ast;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.htmlunit.corejs.javascript.EvaluatorException;
















public class ErrorCollector
  implements IdeErrorReporter
{
  private List<ParseProblem> errors = new ArrayList();
  


  public ErrorCollector() {}
  


  public void warning(String message, String sourceName, int line, String lineSource, int lineOffset)
  {
    throw new UnsupportedOperationException();
  }
  



  public void warning(String message, String sourceName, int offset, int length)
  {
    errors.add(new ParseProblem(ParseProblem.Type.Warning, message, sourceName, offset, length));
  }
  







  public void error(String message, String sourceName, int line, String lineSource, int lineOffset)
  {
    throw new UnsupportedOperationException();
  }
  



  public void error(String message, String sourceName, int fileOffset, int length)
  {
    errors.add(new ParseProblem(ParseProblem.Type.Error, message, sourceName, fileOffset, length));
  }
  




  public EvaluatorException runtimeError(String message, String sourceName, int line, String lineSource, int lineOffset)
  {
    throw new UnsupportedOperationException();
  }
  


  public List<ParseProblem> getErrors()
  {
    return errors;
  }
  
  public String toString()
  {
    StringBuilder sb = new StringBuilder(errors.size() * 100);
    for (ParseProblem pp : errors) {
      sb.append(pp.toString()).append("\n");
    }
    return sb.toString();
  }
}
