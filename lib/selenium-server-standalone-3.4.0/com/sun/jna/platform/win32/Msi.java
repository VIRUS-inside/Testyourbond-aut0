package com.sun.jna.platform.win32;

import com.sun.jna.Native;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;













public abstract interface Msi
  extends StdCallLibrary
{
  public static final Msi INSTANCE = (Msi)Native.loadLibrary("msi", Msi.class, W32APIOptions.UNICODE_OPTIONS);
  public static final int INSTALLSTATE_NOTUSED = -7;
  public static final int INSTALLSTATE_BADCONFIG = -6;
  public static final int INSTALLSTATE_INCOMPLETE = -5;
  public static final int INSTALLSTATE_SOURCEABSENT = -4;
  public static final int INSTALLSTATE_MOREDATA = -3;
  public static final int INSTALLSTATE_INVALIDARG = -2;
  public static final int INSTALLSTATE_UNKNOWN = -1;
  public static final int INSTALLSTATE_BROKEN = 0;
  public static final int INSTALLSTATE_ADVERTISED = 1;
  public static final int INSTALLSTATE_REMOVED = 1;
  public static final int INSTALLSTATE_ABSENT = 2;
  public static final int INSTALLSTATE_LOCAL = 3;
  public static final int INSTALLSTATE_SOURCE = 4;
  public static final int INSTALLSTATE_DEFAULT = 5;
  
  public abstract int MsiGetComponentPath(String paramString1, String paramString2, char[] paramArrayOfChar, IntByReference paramIntByReference);
  
  public abstract int MsiLocateComponent(String paramString, char[] paramArrayOfChar, IntByReference paramIntByReference);
  
  public abstract int MsiGetProductCode(String paramString, char[] paramArrayOfChar);
  
  public abstract int MsiEnumComponents(WinDef.DWORD paramDWORD, char[] paramArrayOfChar);
}
