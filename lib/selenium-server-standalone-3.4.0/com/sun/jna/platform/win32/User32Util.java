package com.sun.jna.platform.win32;

import com.sun.jna.WString;

















public final class User32Util
{
  public User32Util() {}
  
  public static final int registerWindowMessage(String lpString)
  {
    int messageId = User32.INSTANCE.RegisterWindowMessage(lpString);
    if (messageId == 0)
      throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
    return messageId;
  }
  
  public static final WinDef.HWND createWindow(String className, String windowName, int style, int x, int y, int width, int height, WinDef.HWND parent, WinDef.HMENU menu, WinDef.HINSTANCE instance, WinDef.LPVOID param)
  {
    return createWindowEx(0, className, windowName, style, x, y, width, height, parent, menu, instance, param);
  }
  
  public static final WinDef.HWND createWindowEx(int exStyle, String className, String windowName, int style, int x, int y, int width, int height, WinDef.HWND parent, WinDef.HMENU menu, WinDef.HINSTANCE instance, WinDef.LPVOID param)
  {
    WinDef.HWND hWnd = User32.INSTANCE.CreateWindowEx(exStyle, new WString(className), windowName, style, x, y, width, height, parent, menu, instance, param);
    
    if (hWnd == null)
      throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
    return hWnd;
  }
  
  public static final void destroyWindow(WinDef.HWND hWnd) {
    if (!User32.INSTANCE.DestroyWindow(hWnd)) {
      throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
    }
  }
}
