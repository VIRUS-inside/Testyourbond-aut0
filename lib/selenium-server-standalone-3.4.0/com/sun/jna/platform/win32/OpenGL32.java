package com.sun.jna.platform.win32;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.win32.StdCallLibrary;
















public abstract interface OpenGL32
  extends StdCallLibrary
{
  public static final OpenGL32 INSTANCE = (OpenGL32)Native.loadLibrary("opengl32", OpenGL32.class);
  
  public abstract String glGetString(int paramInt);
  
  public abstract WinDef.HGLRC wglCreateContext(WinDef.HDC paramHDC);
  
  public abstract WinDef.HGLRC wglGetCurrentContext();
  
  public abstract boolean wglMakeCurrent(WinDef.HDC paramHDC, WinDef.HGLRC paramHGLRC);
  
  public abstract boolean wglDeleteContext(WinDef.HGLRC paramHGLRC);
  
  public abstract Pointer wglGetProcAddress(String paramString);
}
