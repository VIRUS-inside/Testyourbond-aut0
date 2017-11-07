package com.sun.jna.platform;

import com.sun.jna.Platform;
import com.sun.jna.platform.unix.X11;
import com.sun.jna.platform.unix.X11.Display;
import com.sun.jna.platform.unix.X11.KeySym;
import com.sun.jna.platform.win32.User32;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;





















public class KeyboardUtils
{
  static final NativeKeyboardUtils INSTANCE;
  
  static
  {
    if (GraphicsEnvironment.isHeadless()) {
      throw new HeadlessException("KeyboardUtils requires a keyboard");
    }
    if (Platform.isWindows()) {
      INSTANCE = new W32KeyboardUtils(null);
    } else {
      if (Platform.isMac()) {
        INSTANCE = new MacKeyboardUtils(null);
        throw new UnsupportedOperationException("No support (yet) for " + System.getProperty("os.name"));
      }
      

      INSTANCE = new X11KeyboardUtils(null);
    }
  }
  
  public static boolean isPressed(int keycode, int location) {
    return INSTANCE.isPressed(keycode, location);
  }
  
  public static boolean isPressed(int keycode) { return INSTANCE.isPressed(keycode); }
  
  private static abstract class NativeKeyboardUtils { private NativeKeyboardUtils() {}
    
    public abstract boolean isPressed(int paramInt1, int paramInt2);
    
    public boolean isPressed(int keycode) { return isPressed(keycode, 0); } }
  
  public KeyboardUtils() {}
  
  private static class W32KeyboardUtils extends KeyboardUtils.NativeKeyboardUtils { private W32KeyboardUtils() { super(); }
    
    private int toNative(int code, int loc) { if (((code >= 65) && (code <= 90)) || ((code >= 48) && (code <= 57)))
      {
        return code;
      }
      if (code == 16) {
        if ((loc & 0x3) != 0) {
          return 161;
        }
        if ((loc & 0x2) != 0) {
          return 160;
        }
        return 16;
      }
      if (code == 17) {
        if ((loc & 0x3) != 0) {
          return 163;
        }
        if ((loc & 0x2) != 0) {
          return 162;
        }
        return 17;
      }
      if (code == 18) {
        if ((loc & 0x3) != 0) {
          return 165;
        }
        if ((loc & 0x2) != 0) {
          return 164;
        }
        return 18;
      }
      return 0;
    }
    
    public boolean isPressed(int keycode, int location) { User32 lib = User32.INSTANCE;
      return (lib.GetAsyncKeyState(toNative(keycode, location)) & 0x8000) != 0;
    } }
  
  private static class MacKeyboardUtils extends KeyboardUtils.NativeKeyboardUtils { private MacKeyboardUtils() { super(); }
    
    public boolean isPressed(int keycode, int location) { return false; }
  }
  
  private static class X11KeyboardUtils extends KeyboardUtils.NativeKeyboardUtils { private X11KeyboardUtils() { super(); }
    
    private int toKeySym(int code, int location)
    {
      if ((code >= 65) && (code <= 90))
        return 97 + (code - 65);
      if ((code >= 48) && (code <= 57))
        return 48 + (code - 48);
      if (code == 16) {
        if ((location & 0x3) != 0)
          return 65505;
        return 65505;
      }
      if (code == 17) {
        if ((location & 0x3) != 0)
          return 65508;
        return 65507;
      }
      if (code == 18) {
        if ((location & 0x3) != 0)
          return 65514;
        return 65513;
      }
      if (code == 157) {
        if ((location & 0x3) != 0)
          return 65512;
        return 65511;
      }
      return 0;
    }
    
    public boolean isPressed(int keycode, int location) { X11 lib = X11.INSTANCE;
      X11.Display dpy = lib.XOpenDisplay(null);
      if (dpy == null) {
        throw new Error("Can't open X Display");
      }
      try {
        byte[] keys = new byte[32];
        
        lib.XQueryKeymap(dpy, keys);
        int keysym = toKeySym(keycode, location);
        for (int code = 5; code < 256; code++) {
          int idx = code / 8;
          int shift = code % 8;
          if ((keys[idx] & 1 << shift) != 0) {
            int sym = lib.XKeycodeToKeysym(dpy, (byte)code, 0).intValue();
            if (sym == keysym) {
              return true;
            }
          }
        }
      } finally {
        lib.XCloseDisplay(dpy);
      }
      return false;
    }
  }
}
