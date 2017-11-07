package com.sun.jna.win32;

import com.sun.jna.Callback;

public abstract interface DLLCallback
  extends Callback
{
  public static final int DLL_FPTRS = 16;
}
