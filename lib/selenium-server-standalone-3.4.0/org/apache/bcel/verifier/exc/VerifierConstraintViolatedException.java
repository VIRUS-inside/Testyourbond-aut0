package org.apache.bcel.verifier.exc;

























public abstract class VerifierConstraintViolatedException
  extends RuntimeException
{
  private String detailMessage;
  
























  VerifierConstraintViolatedException() {}
  
























  VerifierConstraintViolatedException(String message)
  {
    super(message);
    detailMessage = message;
  }
  





  public void extendMessage(String pre, String post)
  {
    if (pre == null) pre = "";
    if (detailMessage == null) detailMessage = "";
    if (post == null) post = "";
    detailMessage = (pre + detailMessage + post);
  }
  


  public String getMessage()
  {
    return detailMessage;
  }
}
