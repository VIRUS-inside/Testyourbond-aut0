package com.sun.jna.platform.win32;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.WString;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;























public abstract interface Ole32
  extends StdCallLibrary
{
  public static final Ole32 INSTANCE = (Ole32)Native.loadLibrary("Ole32", Ole32.class, W32APIOptions.UNICODE_OPTIONS);
  public static final int COINIT_APARTMENTTHREADED = 2;
  public static final int COINIT_MULTITHREADED = 0;
  public static final int COINIT_DISABLE_OLE1DDE = 4;
  public static final int COINIT_SPEED_OVER_MEMORY = 8;
  
  public abstract WinNT.HRESULT CoCreateGuid(Guid.GUID paramGUID);
  
  /**
   * @deprecated
   */
  public abstract WinNT.HRESULT CoCreateGuid(Guid.GUID.ByReference paramByReference);
  
  public abstract int StringFromGUID2(Guid.GUID paramGUID, char[] paramArrayOfChar, int paramInt);
  
  public abstract WinNT.HRESULT IIDFromString(String paramString, Guid.GUID paramGUID);
  
  public abstract WinNT.HRESULT CoInitialize(WinDef.LPVOID paramLPVOID);
  
  public abstract WinNT.HRESULT CoInitializeEx(Pointer paramPointer, int paramInt);
  
  public abstract void CoUninitialize();
  
  public abstract WinNT.HRESULT CoCreateInstance(Guid.GUID paramGUID1, Pointer paramPointer, int paramInt, Guid.GUID paramGUID2, PointerByReference paramPointerByReference);
  
  public abstract WinNT.HRESULT CLSIDFromProgID(String paramString, Guid.CLSID.ByReference paramByReference);
  
  public abstract WinNT.HRESULT CLSIDFromString(WString paramWString, Guid.CLSID.ByReference paramByReference);
}
