package com.sun.jna.platform.win32.COM;

import com.sun.jna.Function;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;









public abstract class COMInvoker
  extends PointerType
{
  public COMInvoker() {}
  
  protected int _invokeNativeInt(int vtableId, Object[] args)
  {
    Pointer vptr = getPointer().getPointer(0L);
    

    Function func = Function.getFunction(vptr.getPointer(vtableId * Pointer.SIZE));
    
    return func.invokeInt(args);
  }
  
  protected Object _invokeNativeObject(int vtableId, Object[] args, Class returnType)
  {
    Pointer vptr = getPointer().getPointer(0L);
    

    Function func = Function.getFunction(vptr.getPointer(vtableId * Pointer.SIZE));
    
    return func.invoke(returnType, args);
  }
  
  protected void _invokeNativeVoid(int vtableId, Object[] args) {
    Pointer vptr = getPointer().getPointer(0L);
    

    Function func = Function.getFunction(vptr.getPointer(vtableId * Pointer.SIZE));
    
    func.invokeVoid(args);
  }
}
