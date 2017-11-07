package net.sourceforge.htmlunit.corejs.javascript;








public class ContinuationPending
  extends RuntimeException
{
  private static final long serialVersionUID = 4956008116771118856L;
  






  private NativeContinuation continuationState;
  





  private Object applicationState;
  






  ContinuationPending(NativeContinuation continuationState)
  {
    this.continuationState = continuationState;
  }
  





  public Object getContinuation()
  {
    return continuationState;
  }
  


  NativeContinuation getContinuationState()
  {
    return continuationState;
  }
  






  public void setApplicationState(Object applicationState)
  {
    this.applicationState = applicationState;
  }
  


  public Object getApplicationState()
  {
    return applicationState;
  }
}
