package com.sun.jna.win32;

import com.sun.jna.Callback;
import com.sun.jna.FunctionMapper;
import com.sun.jna.Library;















public abstract interface StdCallLibrary
  extends Library, StdCall
{
  public static final int STDCALL_CONVENTION = 1;
  public static final FunctionMapper FUNCTION_MAPPER = new StdCallFunctionMapper();
  
  public static abstract interface StdCallCallback
    extends Callback, StdCall
  {}
}
