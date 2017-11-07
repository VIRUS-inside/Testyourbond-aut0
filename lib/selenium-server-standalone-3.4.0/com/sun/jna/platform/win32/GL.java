package com.sun.jna.platform.win32;

import com.sun.jna.win32.StdCallLibrary;

public abstract interface GL
  extends StdCallLibrary
{
  public static final int GL_VENDOR = 7936;
  public static final int GL_RENDERER = 7937;
  public static final int GL_VERSION = 7938;
  public static final int GL_EXTENSIONS = 7939;
}
