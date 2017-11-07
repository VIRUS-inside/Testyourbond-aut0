package org.apache.xalan.xsltc.compiler;







public final class CompilerException
  extends Exception
{
  static final long serialVersionUID = 1732939618562742663L;
  





  private String _msg;
  






  public CompilerException() {}
  






  public CompilerException(Exception e)
  {
    super(e.toString());
    _msg = e.toString();
  }
  
  public CompilerException(String message) {
    super(message);
    _msg = message;
  }
  
  public String getMessage() {
    int col = _msg.indexOf(':');
    
    if (col > -1) {
      return _msg.substring(col);
    }
    return _msg;
  }
}
