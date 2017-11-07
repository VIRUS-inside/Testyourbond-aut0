package net.sourceforge.htmlunit.corejs.javascript;

import java.io.Serializable;












public final class ScriptStackElement
  implements Serializable
{
  static final long serialVersionUID = -6416688260860477449L;
  public final String fileName;
  public final String functionName;
  public final int lineNumber;
  
  public ScriptStackElement(String fileName, String functionName, int lineNumber)
  {
    this.fileName = fileName;
    this.functionName = functionName;
    this.lineNumber = lineNumber;
  }
  
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    renderMozillaStyle(sb);
    return sb.toString();
  }
  






  public void renderJavaStyle(StringBuilder sb)
  {
    sb.append("\tat ").append(fileName);
    if (lineNumber > -1) {
      sb.append(':').append(lineNumber);
    }
    if (functionName != null) {
      sb.append(" (").append(functionName).append(')');
    }
  }
  






  public void renderMozillaStyle(StringBuilder sb)
  {
    if (functionName != null) {
      sb.append(functionName).append("()");
    }
    sb.append('@').append(fileName);
    if (lineNumber > -1) {
      sb.append(':').append(lineNumber);
    }
  }
  







  public void renderV8Style(StringBuilder sb)
  {
    sb.append("    at ");
    
    if ((functionName == null) || ("anonymous".equals(functionName)) || 
      ("undefined".equals(functionName)))
    {
      appendV8Location(sb);
    }
    else {
      sb.append(functionName).append(" (");
      appendV8Location(sb);
      sb.append(')');
    }
  }
  
  private void appendV8Location(StringBuilder sb) {
    sb.append(fileName);
    if (lineNumber > -1) {
      sb.append(':').append(lineNumber);
    }
  }
}
