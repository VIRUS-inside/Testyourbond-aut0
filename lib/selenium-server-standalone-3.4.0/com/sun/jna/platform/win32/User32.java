package com.sun.jna.platform.win32;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.WString;
import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;
























public abstract interface User32
  extends StdCallLibrary, WinUser, WinNT
{
  public static final User32 INSTANCE = (User32)Native.loadLibrary("user32", User32.class, W32APIOptions.DEFAULT_OPTIONS);
  




  public static final WinDef.HWND HWND_MESSAGE = new WinDef.HWND(Pointer.createConstant(-3));
  public static final int CS_GLOBALCLASS = 16384;
  public static final int WS_EX_TOPMOST = 8;
  public static final int WS_OVERLAPPED = 0;
  public static final int DEVICE_NOTIFY_WINDOW_HANDLE = 0;
  public static final int DEVICE_NOTIFY_SERVICE_HANDLE = 1;
  public static final int DEVICE_NOTIFY_ALL_INTERFACE_CLASSES = 4;
  
  public abstract WinDef.HDC GetDC(WinDef.HWND paramHWND);
  
  public abstract int ReleaseDC(WinDef.HWND paramHWND, WinDef.HDC paramHDC);
  
  public abstract WinDef.HWND FindWindow(String paramString1, String paramString2);
  
  public abstract int GetClassName(WinDef.HWND paramHWND, char[] paramArrayOfChar, int paramInt);
  
  public abstract boolean GetGUIThreadInfo(int paramInt, WinUser.GUITHREADINFO paramGUITHREADINFO);
  
  public abstract boolean GetWindowInfo(WinDef.HWND paramHWND, WinUser.WINDOWINFO paramWINDOWINFO);
  
  public abstract boolean GetWindowRect(WinDef.HWND paramHWND, WinDef.RECT paramRECT);
  
  public abstract int GetWindowText(WinDef.HWND paramHWND, char[] paramArrayOfChar, int paramInt);
  
  public abstract int GetWindowTextLength(WinDef.HWND paramHWND);
  
  public abstract int GetWindowModuleFileName(WinDef.HWND paramHWND, char[] paramArrayOfChar, int paramInt);
  
  public abstract int GetWindowThreadProcessId(WinDef.HWND paramHWND, IntByReference paramIntByReference);
  
  public abstract boolean EnumWindows(WinUser.WNDENUMPROC paramWNDENUMPROC, Pointer paramPointer);
  
  public abstract boolean EnumChildWindows(WinDef.HWND paramHWND, WinUser.WNDENUMPROC paramWNDENUMPROC, Pointer paramPointer);
  
  public abstract boolean EnumThreadWindows(int paramInt, WinUser.WNDENUMPROC paramWNDENUMPROC, Pointer paramPointer);
  
  public abstract boolean FlashWindowEx(WinUser.FLASHWINFO paramFLASHWINFO);
  
  public abstract WinDef.HICON LoadIcon(WinDef.HINSTANCE paramHINSTANCE, String paramString);
  
  public abstract WinNT.HANDLE LoadImage(WinDef.HINSTANCE paramHINSTANCE, String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  public abstract boolean DestroyIcon(WinDef.HICON paramHICON);
  
  public abstract int GetWindowLong(WinDef.HWND paramHWND, int paramInt);
  
  public abstract int SetWindowLong(WinDef.HWND paramHWND, int paramInt1, int paramInt2);
  
  public abstract Pointer SetWindowLong(WinDef.HWND paramHWND, int paramInt, Pointer paramPointer);
  
  public abstract BaseTSD.LONG_PTR GetWindowLongPtr(WinDef.HWND paramHWND, int paramInt);
  
  public abstract BaseTSD.LONG_PTR SetWindowLongPtr(WinDef.HWND paramHWND, int paramInt, BaseTSD.LONG_PTR paramLONG_PTR);
  
  public abstract Pointer SetWindowLongPtr(WinDef.HWND paramHWND, int paramInt, Pointer paramPointer);
  
  public abstract boolean SetLayeredWindowAttributes(WinDef.HWND paramHWND, int paramInt1, byte paramByte, int paramInt2);
  
  public abstract boolean GetLayeredWindowAttributes(WinDef.HWND paramHWND, IntByReference paramIntByReference1, ByteByReference paramByteByReference, IntByReference paramIntByReference2);
  
  public abstract boolean UpdateLayeredWindow(WinDef.HWND paramHWND, WinDef.HDC paramHDC1, WinDef.POINT paramPOINT1, WinUser.SIZE paramSIZE, WinDef.HDC paramHDC2, WinDef.POINT paramPOINT2, int paramInt1, WinUser.BLENDFUNCTION paramBLENDFUNCTION, int paramInt2);
  
  public abstract int SetWindowRgn(WinDef.HWND paramHWND, WinDef.HRGN paramHRGN, boolean paramBoolean);
  
  public abstract boolean GetKeyboardState(byte[] paramArrayOfByte);
  
  public abstract short GetAsyncKeyState(int paramInt);
  
  public abstract WinUser.HHOOK SetWindowsHookEx(int paramInt1, WinUser.HOOKPROC paramHOOKPROC, WinDef.HINSTANCE paramHINSTANCE, int paramInt2);
  
  public abstract WinDef.LRESULT CallNextHookEx(WinUser.HHOOK paramHHOOK, int paramInt, WinDef.WPARAM paramWPARAM, WinDef.LPARAM paramLPARAM);
  
  public abstract WinDef.LRESULT CallNextHookEx(WinUser.HHOOK paramHHOOK, int paramInt, WinDef.WPARAM paramWPARAM, Pointer paramPointer);
  
  public abstract boolean UnhookWindowsHookEx(WinUser.HHOOK paramHHOOK);
  
  public abstract int GetMessage(WinUser.MSG paramMSG, WinDef.HWND paramHWND, int paramInt1, int paramInt2);
  
  public abstract boolean PeekMessage(WinUser.MSG paramMSG, WinDef.HWND paramHWND, int paramInt1, int paramInt2, int paramInt3);
  
  public abstract boolean TranslateMessage(WinUser.MSG paramMSG);
  
  public abstract WinDef.LRESULT DispatchMessage(WinUser.MSG paramMSG);
  
  public abstract void PostMessage(WinDef.HWND paramHWND, int paramInt, WinDef.WPARAM paramWPARAM, WinDef.LPARAM paramLPARAM);
  
  public abstract void PostQuitMessage(int paramInt);
  
  public abstract int GetSystemMetrics(int paramInt);
  
  public abstract WinDef.HWND SetParent(WinDef.HWND paramHWND1, WinDef.HWND paramHWND2);
  
  public abstract boolean IsWindowVisible(WinDef.HWND paramHWND);
  
  public abstract boolean MoveWindow(WinDef.HWND paramHWND, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean);
  
  public abstract boolean SetWindowPos(WinDef.HWND paramHWND1, WinDef.HWND paramHWND2, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);
  
  public abstract boolean AttachThreadInput(WinDef.DWORD paramDWORD1, WinDef.DWORD paramDWORD2, boolean paramBoolean);
  
  public abstract boolean SetForegroundWindow(WinDef.HWND paramHWND);
  
  public abstract WinDef.HWND GetForegroundWindow();
  
  public abstract WinDef.HWND SetFocus(WinDef.HWND paramHWND);
  
  public abstract WinDef.DWORD SendInput(WinDef.DWORD paramDWORD, WinUser.INPUT[] paramArrayOfINPUT, int paramInt);
  
  public abstract WinDef.DWORD WaitForInputIdle(WinNT.HANDLE paramHANDLE, WinDef.DWORD paramDWORD);
  
  public abstract boolean InvalidateRect(WinDef.HWND paramHWND, WinDef.RECT paramRECT, boolean paramBoolean);
  
  public abstract boolean RedrawWindow(WinDef.HWND paramHWND, WinDef.RECT paramRECT, WinDef.HRGN paramHRGN, WinDef.DWORD paramDWORD);
  
  public abstract WinDef.HWND GetWindow(WinDef.HWND paramHWND, WinDef.DWORD paramDWORD);
  
  public abstract boolean UpdateWindow(WinDef.HWND paramHWND);
  
  public abstract boolean ShowWindow(WinDef.HWND paramHWND, int paramInt);
  
  public abstract boolean CloseWindow(WinDef.HWND paramHWND);
  
  public abstract boolean RegisterHotKey(WinDef.HWND paramHWND, int paramInt1, int paramInt2, int paramInt3);
  
  public abstract boolean UnregisterHotKey(Pointer paramPointer, int paramInt);
  
  public abstract boolean GetLastInputInfo(WinUser.LASTINPUTINFO paramLASTINPUTINFO);
  
  public abstract WinDef.ATOM RegisterClassEx(WinUser.WNDCLASSEX paramWNDCLASSEX);
  
  public abstract boolean UnregisterClass(WString paramWString, WinDef.HINSTANCE paramHINSTANCE);
  
  public abstract WinDef.HWND CreateWindowEx(int paramInt1, WString paramWString, String paramString, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, WinDef.HWND paramHWND, WinDef.HMENU paramHMENU, WinDef.HINSTANCE paramHINSTANCE, WinDef.LPVOID paramLPVOID);
  
  public abstract boolean DestroyWindow(WinDef.HWND paramHWND);
  
  public abstract boolean GetClassInfoEx(WinDef.HINSTANCE paramHINSTANCE, WString paramWString, WinUser.WNDCLASSEX paramWNDCLASSEX);
  
  public abstract WinDef.LRESULT DefWindowProc(WinDef.HWND paramHWND, int paramInt, WinDef.WPARAM paramWPARAM, WinDef.LPARAM paramLPARAM);
  
  public abstract WinUser.HDEVNOTIFY RegisterDeviceNotification(WinNT.HANDLE paramHANDLE, Structure paramStructure, int paramInt);
  
  public abstract boolean UnregisterDeviceNotification(WinUser.HDEVNOTIFY paramHDEVNOTIFY);
  
  public abstract int RegisterWindowMessage(String paramString);
}
