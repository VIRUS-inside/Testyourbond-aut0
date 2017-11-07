package com.sun.jna.platform.win32;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;





















public abstract interface GDI32
  extends StdCallLibrary
{
  public static final GDI32 INSTANCE = (GDI32)Native.loadLibrary("gdi32", GDI32.class, W32APIOptions.DEFAULT_OPTIONS);
  
  public abstract WinDef.HRGN ExtCreateRegion(Pointer paramPointer, int paramInt, WinGDI.RGNDATA paramRGNDATA);
  
  public abstract int CombineRgn(WinDef.HRGN paramHRGN1, WinDef.HRGN paramHRGN2, WinDef.HRGN paramHRGN3, int paramInt);
  
  public abstract WinDef.HRGN CreateRectRgn(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  public abstract WinDef.HRGN CreateRoundRectRgn(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6);
  
  public abstract WinDef.HRGN CreatePolyPolygonRgn(WinDef.POINT[] paramArrayOfPOINT, int[] paramArrayOfInt, int paramInt1, int paramInt2);
  
  public abstract boolean SetRectRgn(WinDef.HRGN paramHRGN, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  public abstract int SetPixel(WinDef.HDC paramHDC, int paramInt1, int paramInt2, int paramInt3);
  
  public abstract WinDef.HDC CreateCompatibleDC(WinDef.HDC paramHDC);
  
  public abstract boolean DeleteDC(WinDef.HDC paramHDC);
  
  public abstract WinDef.HBITMAP CreateDIBitmap(WinDef.HDC paramHDC, WinGDI.BITMAPINFOHEADER paramBITMAPINFOHEADER, int paramInt1, Pointer paramPointer, WinGDI.BITMAPINFO paramBITMAPINFO, int paramInt2);
  
  public abstract WinDef.HBITMAP CreateDIBSection(WinDef.HDC paramHDC, WinGDI.BITMAPINFO paramBITMAPINFO, int paramInt1, PointerByReference paramPointerByReference, Pointer paramPointer, int paramInt2);
  
  public abstract WinDef.HBITMAP CreateCompatibleBitmap(WinDef.HDC paramHDC, int paramInt1, int paramInt2);
  
  public abstract WinNT.HANDLE SelectObject(WinDef.HDC paramHDC, WinNT.HANDLE paramHANDLE);
  
  public abstract boolean DeleteObject(WinNT.HANDLE paramHANDLE);
  
  public abstract int GetDeviceCaps(WinDef.HDC paramHDC, int paramInt);
  
  public abstract int GetDIBits(WinDef.HDC paramHDC, WinDef.HBITMAP paramHBITMAP, int paramInt1, int paramInt2, Pointer paramPointer, WinGDI.BITMAPINFO paramBITMAPINFO, int paramInt3);
  
  public abstract int ChoosePixelFormat(WinDef.HDC paramHDC, WinGDI.PIXELFORMATDESCRIPTOR.ByReference paramByReference);
  
  public abstract boolean SetPixelFormat(WinDef.HDC paramHDC, int paramInt, WinGDI.PIXELFORMATDESCRIPTOR.ByReference paramByReference);
}
