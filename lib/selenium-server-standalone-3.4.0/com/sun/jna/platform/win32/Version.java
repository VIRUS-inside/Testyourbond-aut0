package com.sun.jna.platform.win32;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;













public abstract interface Version
  extends StdCallLibrary
{
  public static final Version INSTANCE = (Version)Native.loadLibrary("version", Version.class, W32APIOptions.DEFAULT_OPTIONS);
  
  public abstract int GetFileVersionInfoSize(String paramString, IntByReference paramIntByReference);
  
  public abstract boolean GetFileVersionInfo(String paramString, int paramInt1, int paramInt2, Pointer paramPointer);
  
  public abstract boolean VerQueryValue(Pointer paramPointer, String paramString, PointerByReference paramPointerByReference, IntByReference paramIntByReference);
}
