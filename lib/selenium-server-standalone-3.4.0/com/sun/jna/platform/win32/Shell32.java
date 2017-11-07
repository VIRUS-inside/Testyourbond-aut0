package com.sun.jna.platform.win32;

import com.sun.jna.Native;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;





















public abstract interface Shell32
  extends ShellAPI, StdCallLibrary
{
  public static final Shell32 INSTANCE = (Shell32)Native.loadLibrary("shell32", Shell32.class, W32APIOptions.UNICODE_OPTIONS);
  
  public abstract int SHFileOperation(ShellAPI.SHFILEOPSTRUCT paramSHFILEOPSTRUCT);
  
  public abstract WinNT.HRESULT SHGetFolderPath(WinDef.HWND paramHWND, int paramInt, WinNT.HANDLE paramHANDLE, WinDef.DWORD paramDWORD, char[] paramArrayOfChar);
  
  public abstract WinNT.HRESULT SHGetDesktopFolder(PointerByReference paramPointerByReference);
  
  public abstract WinDef.INT_PTR ShellExecute(WinDef.HWND paramHWND, String paramString1, String paramString2, String paramString3, String paramString4, int paramInt);
  
  public abstract boolean SHGetSpecialFolderPath(WinDef.HWND paramHWND, char[] paramArrayOfChar, int paramInt, boolean paramBoolean);
  
  public abstract WinDef.UINT_PTR SHAppBarMessage(WinDef.DWORD paramDWORD, ShellAPI.APPBARDATA paramAPPBARDATA);
}
