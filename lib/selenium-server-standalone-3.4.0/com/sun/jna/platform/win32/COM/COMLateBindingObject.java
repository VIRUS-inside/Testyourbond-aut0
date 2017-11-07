package com.sun.jna.platform.win32.COM;

import com.sun.jna.platform.win32.Guid.CLSID;
import com.sun.jna.platform.win32.OaIdl.VARIANT_BOOL;
import com.sun.jna.platform.win32.Variant.VARIANT;
import com.sun.jna.platform.win32.Variant.VARIANT.ByReference;
import com.sun.jna.platform.win32.WinDef.LONG;
import com.sun.jna.platform.win32.WinDef.SHORT;
import java.util.Date;
























public class COMLateBindingObject
  extends COMBindingBaseObject
{
  public COMLateBindingObject(IDispatch iDispatch)
  {
    super(iDispatch);
  }
  







  public COMLateBindingObject(Guid.CLSID clsid, boolean useActiveInstance)
  {
    super(clsid, useActiveInstance);
  }
  









  public COMLateBindingObject(String progId, boolean useActiveInstance)
    throws COMException
  {
    super(progId, useActiveInstance);
  }
  






  protected IDispatch getAutomationProperty(String propertyName)
  {
    Variant.VARIANT.ByReference result = new Variant.VARIANT.ByReference();
    oleMethod(2, result, getIDispatch(), propertyName);
    

    return (IDispatch)result.getValue();
  }
  









  protected IDispatch getAutomationProperty(String propertyName, COMLateBindingObject comObject)
  {
    Variant.VARIANT.ByReference result = new Variant.VARIANT.ByReference();
    oleMethod(2, result, comObject.getIDispatch(), propertyName);
    

    return (IDispatch)result.getValue();
  }
  











  protected IDispatch getAutomationProperty(String propertyName, COMLateBindingObject comObject, Variant.VARIANT value)
  {
    Variant.VARIANT.ByReference result = new Variant.VARIANT.ByReference();
    oleMethod(2, result, comObject.getIDispatch(), propertyName, value);
    

    return (IDispatch)result.getValue();
  }
  









  protected IDispatch getAutomationProperty(String propertyName, IDispatch iDispatch)
  {
    Variant.VARIANT.ByReference result = new Variant.VARIANT.ByReference();
    oleMethod(2, result, getIDispatch(), propertyName);
    

    return (IDispatch)result.getValue();
  }
  






  protected boolean getBooleanProperty(String propertyName)
  {
    Variant.VARIANT.ByReference result = new Variant.VARIANT.ByReference();
    oleMethod(2, result, getIDispatch(), propertyName);
    

    return ((OaIdl.VARIANT_BOOL)result.getValue()).intValue() != 0;
  }
  






  protected Date getDateProperty(String propertyName)
  {
    Variant.VARIANT.ByReference result = new Variant.VARIANT.ByReference();
    oleMethod(2, result, getIDispatch(), propertyName);
    

    return result.dateValue();
  }
  






  protected int getIntProperty(String propertyName)
  {
    Variant.VARIANT.ByReference result = new Variant.VARIANT.ByReference();
    oleMethod(2, result, getIDispatch(), propertyName);
    

    return ((WinDef.LONG)result.getValue()).intValue();
  }
  






  protected short getShortProperty(String propertyName)
  {
    Variant.VARIANT.ByReference result = new Variant.VARIANT.ByReference();
    oleMethod(2, result, getIDispatch(), propertyName);
    

    return ((WinDef.SHORT)result.getValue()).shortValue();
  }
  






  protected String getStringProperty(String propertyName)
  {
    Variant.VARIANT.ByReference result = new Variant.VARIANT.ByReference();
    oleMethod(2, result, getIDispatch(), propertyName);
    

    return result.getValue().toString();
  }
  






  protected Variant.VARIANT invoke(String methodName)
  {
    Variant.VARIANT.ByReference result = new Variant.VARIANT.ByReference();
    oleMethod(1, result, getIDispatch(), methodName);
    

    return result;
  }
  








  protected Variant.VARIANT invoke(String methodName, Variant.VARIANT arg)
  {
    Variant.VARIANT.ByReference result = new Variant.VARIANT.ByReference();
    oleMethod(1, result, getIDispatch(), methodName, arg);
    

    return result;
  }
  








  protected Variant.VARIANT invoke(String methodName, Variant.VARIANT[] args)
  {
    Variant.VARIANT.ByReference result = new Variant.VARIANT.ByReference();
    oleMethod(1, result, getIDispatch(), methodName, args);
    

    return result;
  }
  










  protected Variant.VARIANT invoke(String methodName, Variant.VARIANT arg1, Variant.VARIANT arg2)
  {
    return invoke(methodName, new Variant.VARIANT[] { arg1, arg2 });
  }
  













  protected Variant.VARIANT invoke(String methodName, Variant.VARIANT arg1, Variant.VARIANT arg2, Variant.VARIANT arg3)
  {
    return invoke(methodName, new Variant.VARIANT[] { arg1, arg2, arg3 });
  }
  















  protected Variant.VARIANT invoke(String methodName, Variant.VARIANT arg1, Variant.VARIANT arg2, Variant.VARIANT arg3, Variant.VARIANT arg4)
  {
    return invoke(methodName, new Variant.VARIANT[] { arg1, arg2, arg3, arg4 });
  }
  







  protected void invokeNoReply(String methodName, IDispatch dispatch)
  {
    oleMethod(1, null, dispatch, methodName);
  }
  








  protected void invokeNoReply(String methodName, COMLateBindingObject comObject)
  {
    oleMethod(1, null, comObject.getIDispatch(), methodName);
  }
  











  protected void invokeNoReply(String methodName, IDispatch dispatch, Variant.VARIANT arg)
  {
    oleMethod(1, null, dispatch, methodName, arg);
  }
  












  protected void invokeNoReply(String methodName, IDispatch dispatch, Variant.VARIANT arg1, Variant.VARIANT arg2)
  {
    oleMethod(1, null, dispatch, methodName, new Variant.VARIANT[] { arg1, arg2 });
  }
  











  protected void invokeNoReply(String methodName, COMLateBindingObject comObject, Variant.VARIANT arg)
  {
    oleMethod(1, null, comObject.getIDispatch(), methodName, arg);
  }
  











  protected void invokeNoReply(String methodName, IDispatch dispatch, Variant.VARIANT[] args)
  {
    oleMethod(1, null, dispatch, methodName, args);
  }
  






  protected void invokeNoReply(String methodName)
  {
    Variant.VARIANT.ByReference result = new Variant.VARIANT.ByReference();
    oleMethod(1, result, getIDispatch(), methodName);
  }
  








  protected void invokeNoReply(String methodName, Variant.VARIANT arg)
  {
    Variant.VARIANT.ByReference result = new Variant.VARIANT.ByReference();
    oleMethod(1, result, getIDispatch(), methodName, arg);
  }
  








  protected void invokeNoReply(String methodName, Variant.VARIANT[] args)
  {
    Variant.VARIANT.ByReference result = new Variant.VARIANT.ByReference();
    oleMethod(1, result, getIDispatch(), methodName, args);
  }
  










  protected void invokeNoReply(String methodName, Variant.VARIANT arg1, Variant.VARIANT arg2)
  {
    invokeNoReply(methodName, new Variant.VARIANT[] { arg1, arg2 });
  }
  












  protected void invokeNoReply(String methodName, Variant.VARIANT arg1, Variant.VARIANT arg2, Variant.VARIANT arg3)
  {
    invokeNoReply(methodName, new Variant.VARIANT[] { arg1, arg2, arg3 });
  }
  














  protected void invokeNoReply(String methodName, Variant.VARIANT arg1, Variant.VARIANT arg2, Variant.VARIANT arg3, Variant.VARIANT arg4)
  {
    invokeNoReply(methodName, new Variant.VARIANT[] { arg1, arg2, arg3, arg4 });
  }
  







  protected void setProperty(String propertyName, boolean value)
  {
    oleMethod(4, null, getIDispatch(), propertyName, new Variant.VARIANT(value));
  }
  








  protected void setProperty(String propertyName, Date value)
  {
    oleMethod(4, null, getIDispatch(), propertyName, new Variant.VARIANT(value));
  }
  








  protected void setProperty(String propertyName, IDispatch value)
  {
    oleMethod(4, null, getIDispatch(), propertyName, new Variant.VARIANT(value));
  }
  








  protected void setProperty(String propertyName, int value)
  {
    oleMethod(4, null, getIDispatch(), propertyName, new Variant.VARIANT(value));
  }
  








  protected void setProperty(String propertyName, short value)
  {
    oleMethod(4, null, getIDispatch(), propertyName, new Variant.VARIANT(value));
  }
  








  protected void setProperty(String propertyName, String value)
  {
    oleMethod(4, null, getIDispatch(), propertyName, new Variant.VARIANT(value));
  }
  











  protected void setProperty(String propertyName, IDispatch iDispatch, Variant.VARIANT value)
  {
    oleMethod(4, null, iDispatch, propertyName, value);
  }
  











  protected void setProperty(String propertyName, COMLateBindingObject comObject, Variant.VARIANT value)
  {
    oleMethod(4, null, comObject.getIDispatch(), propertyName, value);
  }
  





  public Variant.VARIANT toVariant()
  {
    return new Variant.VARIANT(getIDispatch());
  }
}
