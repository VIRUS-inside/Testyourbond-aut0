package com.sun.jna.platform.win32;

import com.sun.jna.Function;
import com.sun.jna.Pointer;






















public abstract class OpenGL32Util
{
  public OpenGL32Util() {}
  
  public static Function wglGetProcAddress(String procName)
  {
    Pointer funcPointer = OpenGL32.INSTANCE.wglGetProcAddress("wglEnumGpusNV");
    return funcPointer == null ? null : Function.getFunction(funcPointer);
  }
  





  public static int countGpusNV()
  {
    WinDef.HWND hWnd = User32Util.createWindow("Message", null, 0, 0, 0, 0, 0, null, null, null, null);
    WinDef.HDC hdc = User32.INSTANCE.GetDC(hWnd);
    

    WinGDI.PIXELFORMATDESCRIPTOR.ByReference pfd = new WinGDI.PIXELFORMATDESCRIPTOR.ByReference();
    nVersion = 1;
    dwFlags = 37;
    iPixelType = 0;
    cColorBits = 24;
    cDepthBits = 16;
    iLayerType = 0;
    GDI32.INSTANCE.SetPixelFormat(hdc, GDI32.INSTANCE.ChoosePixelFormat(hdc, pfd), pfd);
    

    WinDef.HGLRC hGLRC = OpenGL32.INSTANCE.wglCreateContext(hdc);
    OpenGL32.INSTANCE.wglMakeCurrent(hdc, hGLRC);
    Pointer funcPointer = OpenGL32.INSTANCE.wglGetProcAddress("wglEnumGpusNV");
    Function fncEnumGpusNV = funcPointer == null ? null : Function.getFunction(funcPointer);
    OpenGL32.INSTANCE.wglDeleteContext(hGLRC);
    

    User32.INSTANCE.ReleaseDC(hWnd, hdc);
    User32Util.destroyWindow(hWnd);
    

    if (fncEnumGpusNV == null) { return 0;
    }
    
    WinDef.HGLRCByReference hGPU = new WinDef.HGLRCByReference();
    for (int i = 0; i < 16; i++) {
      Boolean ok = (Boolean)fncEnumGpusNV.invoke(Boolean.class, new Object[] { Integer.valueOf(i), hGPU });
      if (!ok.booleanValue()) { return i;
      }
    }
    return 0;
  }
}
