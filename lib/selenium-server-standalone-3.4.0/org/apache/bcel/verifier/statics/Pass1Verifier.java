package org.apache.bcel.verifier.statics;

import org.apache.bcel.Repository;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.verifier.PassVerifier;
import org.apache.bcel.verifier.VerificationResult;
import org.apache.bcel.verifier.Verifier;
import org.apache.bcel.verifier.exc.LoadingException;
import org.apache.bcel.verifier.exc.Utility;
import org.apache.bcel.verifier.exc.VerifierConstraintViolatedException;





































































public final class Pass1Verifier
  extends PassVerifier
{
  private JavaClass jc;
  private Verifier myOwner;
  
  private JavaClass getJavaClass()
  {
    if (jc == null) {
      jc = Repository.lookupClass(myOwner.getClassName());
    }
    return jc;
  }
  




  public Pass1Verifier(Verifier owner)
  {
    myOwner = owner;
  }
  




























  public VerificationResult do_verify()
  {
    JavaClass jc;
    



























    try
    {
      jc = getJavaClass();
      
      if (jc != null)
      {
        if (!myOwner.getClassName().equals(jc.getClassName()))
        {

          throw new LoadingException("Wrong name: the internal name of the .class file '" + jc.getClassName() + "' does not match the file's name '" + myOwner.getClassName() + "'.");
        }
      }
    }
    catch (LoadingException e)
    {
      return new VerificationResult(2, e.getMessage());
    }
    catch (ClassFormatError e)
    {
      return new VerificationResult(2, e.getMessage());

    }
    catch (RuntimeException e)
    {
      return new VerificationResult(2, "Parsing via BCEL did not succeed. " + e.getClass().getName() + " occured:\n" + Utility.getStackTrace(e));
    }
    
    if (jc != null) {
      return VerificationResult.VR_OK;
    }
    


    return new VerificationResult(2, "Repository.lookup() failed. FILE NOT FOUND?");
  }
  











  public String[] getMessages()
  {
    return super.getMessages();
  }
}
