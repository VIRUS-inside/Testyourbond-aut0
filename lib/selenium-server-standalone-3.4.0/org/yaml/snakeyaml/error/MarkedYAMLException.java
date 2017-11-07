package org.yaml.snakeyaml.error;



public class MarkedYAMLException
  extends YAMLException
{
  private static final long serialVersionUID = -9119388488683035101L;
  

  private String context;
  

  private Mark contextMark;
  

  private String problem;
  

  private Mark problemMark;
  

  private String note;
  


  protected MarkedYAMLException(String context, Mark contextMark, String problem, Mark problemMark, String note)
  {
    this(context, contextMark, problem, problemMark, note, null);
  }
  
  protected MarkedYAMLException(String context, Mark contextMark, String problem, Mark problemMark, String note, Throwable cause)
  {
    super(context + "; " + problem + "; " + problemMark, cause);
    this.context = context;
    this.contextMark = contextMark;
    this.problem = problem;
    this.problemMark = problemMark;
    this.note = note;
  }
  
  protected MarkedYAMLException(String context, Mark contextMark, String problem, Mark problemMark) {
    this(context, contextMark, problem, problemMark, null, null);
  }
  
  protected MarkedYAMLException(String context, Mark contextMark, String problem, Mark problemMark, Throwable cause)
  {
    this(context, contextMark, problem, problemMark, null, cause);
  }
  
  public String getMessage()
  {
    return toString();
  }
  
  public String toString()
  {
    StringBuilder lines = new StringBuilder();
    if (context != null) {
      lines.append(context);
      lines.append("\n");
    }
    if ((contextMark != null) && ((problem == null) || (problemMark == null) || (contextMark.getName().equals(problemMark.getName())) || (contextMark.getLine() != problemMark.getLine()) || (contextMark.getColumn() != problemMark.getColumn())))
    {



      lines.append(contextMark.toString());
      lines.append("\n");
    }
    if (problem != null) {
      lines.append(problem);
      lines.append("\n");
    }
    if (problemMark != null) {
      lines.append(problemMark.toString());
      lines.append("\n");
    }
    if (note != null) {
      lines.append(note);
      lines.append("\n");
    }
    return lines.toString();
  }
  
  public String getContext() {
    return context;
  }
  
  public Mark getContextMark() {
    return contextMark;
  }
  
  public String getProblem() {
    return problem;
  }
  
  public Mark getProblemMark() {
    return problemMark;
  }
}
