package net.sourceforge.htmlunit.corejs.javascript.ast;


public class ParseProblem
{
  private Type type;
  
  private String message;
  private String sourceName;
  private int offset;
  private int length;
  
  public static enum Type
  {
    Error,  Warning;
    




    private Type() {}
  }
  



  public ParseProblem(Type type, String message, String sourceName, int offset, int length)
  {
    setType(type);
    setMessage(message);
    setSourceName(sourceName);
    setFileOffset(offset);
    setLength(length);
  }
  
  public Type getType() {
    return type;
  }
  
  public void setType(Type type) {
    this.type = type;
  }
  
  public String getMessage() {
    return message;
  }
  
  public void setMessage(String msg) {
    message = msg;
  }
  
  public String getSourceName() {
    return sourceName;
  }
  
  public void setSourceName(String name) {
    sourceName = name;
  }
  
  public int getFileOffset() {
    return offset;
  }
  
  public void setFileOffset(int offset) {
    this.offset = offset;
  }
  
  public int getLength() {
    return length;
  }
  
  public void setLength(int length) {
    this.length = length;
  }
  
  public String toString()
  {
    StringBuilder sb = new StringBuilder(200);
    sb.append(sourceName).append(":");
    sb.append("offset=").append(offset).append(",");
    sb.append("length=").append(length).append(",");
    sb.append(type == Type.Error ? "error: " : "warning: ");
    sb.append(message);
    return sb.toString();
  }
}
