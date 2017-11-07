package org.apache.bcel.verifier;

import java.util.ArrayList;


















































































public abstract class PassVerifier
{
  private ArrayList messages = new ArrayList();
  

  private VerificationResult verificationResult = null;
  





  public PassVerifier() {}
  





  public VerificationResult verify()
  {
    if (verificationResult == null) {
      verificationResult = do_verify();
    }
    return verificationResult;
  }
  




  public abstract VerificationResult do_verify();
  




  public void addMessage(String message)
  {
    messages.add(message);
  }
  






  public String[] getMessages()
  {
    verify();
    String[] ret = new String[messages.size()];
    for (int i = 0; i < messages.size(); i++) {
      ret[i] = ((String)messages.get(i));
    }
    return ret;
  }
}
