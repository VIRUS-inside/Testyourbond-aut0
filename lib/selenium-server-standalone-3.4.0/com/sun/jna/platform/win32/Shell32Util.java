package com.sun.jna.platform.win32;

import com.sun.jna.Native;





























public abstract class Shell32Util
{
  public Shell32Util() {}
  
  public static String getFolderPath(WinDef.HWND hwnd, int nFolder, WinDef.DWORD dwFlags)
  {
    char[] pszPath = new char['Ą'];
    WinNT.HRESULT hr = Shell32.INSTANCE.SHGetFolderPath(hwnd, nFolder, null, dwFlags, pszPath);
    

    if (!hr.equals(W32Errors.S_OK)) {
      throw new Win32Exception(hr);
    }
    return Native.toString(pszPath);
  }
  






  public static String getFolderPath(int nFolder)
  {
    return getFolderPath(null, nFolder, ShlObj.SHGFP_TYPE_CURRENT);
  }
  









  public static final String getSpecialFolderPath(int csidl, boolean create)
  {
    char[] pszPath = new char['Ą'];
    if (!Shell32.INSTANCE.SHGetSpecialFolderPath(null, pszPath, csidl, create))
      throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
    return Native.toString(pszPath);
  }
}
