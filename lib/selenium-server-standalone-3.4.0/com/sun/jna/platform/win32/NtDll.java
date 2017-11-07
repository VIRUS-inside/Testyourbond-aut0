package com.sun.jna.platform.win32;

import com.sun.jna.Native;
import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;

















public abstract interface NtDll
  extends StdCallLibrary
{
  public static final NtDll INSTANCE = (NtDll)Native.loadLibrary("NtDll", NtDll.class, W32APIOptions.UNICODE_OPTIONS);
  
  public abstract int ZwQueryKey(WinNT.HANDLE paramHANDLE, int paramInt1, Structure paramStructure, int paramInt2, IntByReference paramIntByReference);
}
