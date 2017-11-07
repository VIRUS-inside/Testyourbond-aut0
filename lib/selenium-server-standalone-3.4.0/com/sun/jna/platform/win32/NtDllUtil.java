package com.sun.jna.platform.win32;

import com.sun.jna.ptr.IntByReference;






















public abstract class NtDllUtil
{
  public NtDllUtil() {}
  
  public static String getKeyName(WinReg.HKEY hkey)
  {
    IntByReference resultLength = new IntByReference();
    int rc = NtDll.INSTANCE.ZwQueryKey(hkey, 0, null, 0, resultLength);
    
    if ((rc != -1073741789) || (resultLength.getValue() <= 0)) {
      throw new Win32Exception(rc);
    }
    Wdm.KEY_BASIC_INFORMATION keyInformation = new Wdm.KEY_BASIC_INFORMATION(resultLength.getValue());
    
    rc = NtDll.INSTANCE.ZwQueryKey(hkey, 0, keyInformation, resultLength.getValue(), resultLength);
    
    if (rc != 0) {
      throw new Win32Exception(rc);
    }
    return keyInformation.getName();
  }
}
