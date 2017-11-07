package org.apache.bcel.verifier.exc;























public final class AssertionViolatedException
  extends RuntimeException
{
  private String detailMessage;
  





















  public AssertionViolatedException() {}
  





















  public AssertionViolatedException(String message)
  {
    super(message = "INTERNAL ERROR: " + message);
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
  


  public static void main(String[] args)
  {
    AssertionViolatedException ave = new AssertionViolatedException("Oops!");
    ave.extendMessage("\nFOUND:\n\t", "\nExiting!!\n");
    throw ave;
  }
  



  public String getStackTrace()
  {
    return Utility.getStackTrace(this);
  }
}
