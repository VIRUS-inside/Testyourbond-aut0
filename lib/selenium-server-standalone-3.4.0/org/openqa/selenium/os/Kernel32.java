package org.openqa.selenium.os;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.win32.W32APIOptions;















public abstract interface Kernel32
  extends com.sun.jna.platform.win32.Kernel32
{
  public static final Kernel32 INSTANCE = (Kernel32)Native.loadLibrary("kernel32", Kernel32.class, W32APIOptions.UNICODE_OPTIONS);
  
  public abstract int GetProcessId(WinNT.HANDLE paramHANDLE);
}
