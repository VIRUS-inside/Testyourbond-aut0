package com.sun.jna.platform.win32;

import com.sun.jna.Callback;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.ByReference;
import com.sun.jna.Union;
import com.sun.jna.WString;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.StdCallLibrary.StdCallCallback;
import java.util.Arrays;
import java.util.List;

























public abstract interface WinUser
  extends StdCallLibrary, WinDef
{
  public static final WinDef.HWND HWND_BROADCAST = new WinDef.HWND(Pointer.createConstant(65535));
  public static final WinDef.HWND HWND_MESSAGE = new WinDef.HWND(Pointer.createConstant(-3));
  public static final int FLASHW_STOP = 0;
  public static final int FLASHW_CAPTION = 1;
  public static final int FLASHW_TRAY = 2;
  
  public static class HDEVNOTIFY extends WinDef.PVOID {
    public HDEVNOTIFY() {}
    
    public HDEVNOTIFY(Pointer p) {
      super();
    }
  }
  

  public static final int FLASHW_ALL = 3;
  
  public static final int FLASHW_TIMER = 4;
  
  public static final int FLASHW_TIMERNOFG = 12;
  
  public static final int IMAGE_BITMAP = 0;
  
  public static final int IMAGE_ICON = 1;
  
  public static final int IMAGE_CURSOR = 2;
  
  public static final int IMAGE_ENHMETAFILE = 3;
  
  public static final int LR_DEFAULTCOLOR = 0;
  public static final int LR_MONOCHROME = 1;
  public static final int LR_COLOR = 2;
  public static final int LR_COPYRETURNORG = 4;
  public static final int LR_COPYDELETEORG = 8;
  public static final int LR_LOADFROMFILE = 16;
  public static final int LR_LOADTRANSPARENT = 32;
  public static final int LR_DEFAULTSIZE = 64;
  
  public static class GUITHREADINFO
    extends Structure
  {
    public int cbSize = size();
    

    public int flags;
    
    public WinDef.HWND hwndActive;
    public WinDef.HWND hwndFocus;
    public WinDef.HWND hwndCapture;
    
    public GUITHREADINFO() {}
    
    protected List getFieldOrder() { return Arrays.asList(new String[] { "cbSize", "flags", "hwndActive", "hwndFocus", "hwndCapture", "hwndMenuOwner", "hwndMoveSize", "hwndCaret", "rcCaret" }); }
    
    public WinDef.HWND hwndMenuOwner;
    public WinDef.HWND hwndMoveSize;
    public WinDef.HWND hwndCaret;
    public WinDef.RECT rcCaret; }
  
  public static class WINDOWINFO extends Structure { public int cbSize = size();
    
    public WinDef.RECT rcWindow;
    
    public WinDef.RECT rcClient;
    public int dwStyle;
    public int dwExStyle;
    
    public WINDOWINFO() {}
    
    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "cbSize", "rcWindow", "rcClient", "dwStyle", "dwExStyle", "dwWindowStatus", "cxWindowBorders", "cyWindowBorders", "atomWindowType", "wCreatorVersion" });
    }
    


    public int dwWindowStatus;
    

    public int cxWindowBorders;
    
    public int cyWindowBorders;
    
    public short atomWindowType;
    
    public short wCreatorVersion;
  }
  

  public static final int LR_VGACOLOR = 128;
  public static final int LR_LOADMAP3DCOLORS = 4096;
  public static final int LR_CREATEDIBSECTION = 8192;
  public static final int LR_COPYFROMRESOURCE = 16384;
  public static final int LR_SHARED = 32768;
  public static final int GWL_EXSTYLE = -20;
  public static final int GWL_STYLE = -16;
  public static final int GWL_WNDPROC = -4;
  public static final int GWL_HINSTANCE = -6;
  public static final int GWL_ID = -12;
  public static final int GWL_USERDATA = -21;
  public static final int DWL_DLGPROC = 4;
  public static final int DWL_MSGRESULT = 0;
  public static final int DWL_USER = 8;
  public static final int WS_MAXIMIZE = 16777216;
  public static final int WS_VISIBLE = 268435456;
  public static final int WS_MINIMIZE = 536870912;
  public static final int WS_CHILD = 1073741824;
  
  public static class MSG
    extends Structure
  {
    public MSG() {}
    
    protected List getFieldOrder() { return Arrays.asList(new String[] { "hWnd", "message", "wParam", "lParam", "time", "pt" }); }
    
    public WinDef.HWND hWnd;
    public int message;
    public WinDef.WPARAM wParam;
    public WinDef.LPARAM lParam;
    public int time;
    public WinDef.POINT pt; }
  
  public static class FLASHWINFO extends Structure { public int cbSize;
    
    public FLASHWINFO() {}
    
    protected List getFieldOrder() { return Arrays.asList(new String[] { "cbSize", "hWnd", "dwFlags", "uCount", "dwTimeout" }); }
    
    public WinNT.HANDLE hWnd;
    public int dwFlags;
    public int uCount;
    public int dwTimeout;
  }
  
  public static abstract interface WNDENUMPROC extends StdCallLibrary.StdCallCallback {
    public abstract boolean callback(WinDef.HWND paramHWND, Pointer paramPointer);
  }
  
  public static abstract interface LowLevelKeyboardProc extends WinUser.HOOKPROC {
    public abstract WinDef.LRESULT callback(int paramInt, WinDef.WPARAM paramWPARAM, WinUser.KBDLLHOOKSTRUCT paramKBDLLHOOKSTRUCT);
  }
  
  public static class SIZE extends Structure {
    public int cx;
    public int cy;
    
    public SIZE() {}
    
    public SIZE(int w, int h) { cx = w;
      cy = h;
    }
    
    protected List getFieldOrder() {
      return Arrays.asList(new String[] { "cx", "cy" });
    }
  }
  



  public static class BLENDFUNCTION
    extends Structure
  {
    public byte BlendOp = 0;
    public byte BlendFlags = 0;
    public byte SourceConstantAlpha;
    
    public BLENDFUNCTION() {}
    
    protected List getFieldOrder() { return Arrays.asList(new String[] { "BlendOp", "BlendFlags", "SourceConstantAlpha", "AlphaFormat" }); }
    

    public byte AlphaFormat;
  }
  

  public static final int WS_POPUP = Integer.MIN_VALUE;
  
  public static final int WS_EX_COMPOSITED = 536870912;
  
  public static final int WS_EX_LAYERED = 524288;
  
  public static final int WS_EX_TRANSPARENT = 32;
  
  public static final int LWA_COLORKEY = 1;
  
  public static final int LWA_ALPHA = 2;
  
  public static final int ULW_COLORKEY = 1;
  
  public static final int ULW_ALPHA = 2;
  
  public static final int ULW_OPAQUE = 4;
  
  public static final int AC_SRC_OVER = 0;
  
  public static final int AC_SRC_ALPHA = 1;
  public static final int AC_SRC_NO_PREMULT_ALPHA = 1;
  public static final int AC_SRC_NO_ALPHA = 2;
  public static final int VK_SHIFT = 16;
  public static final int VK_LSHIFT = 160;
  public static final int VK_RSHIFT = 161;
  public static final int VK_CONTROL = 17;
  public static final int VK_LCONTROL = 162;
  public static final int VK_RCONTROL = 163;
  public static final int VK_MENU = 18;
  public static final int VK_LMENU = 164;
  public static final int VK_RMENU = 165;
  public static final int MOD_ALT = 1;
  public static final int MOD_CONTROL = 2;
  public static final int MOD_NOREPEAT = 16384;
  public static final int MOD_SHIFT = 4;
  public static final int MOD_WIN = 8;
  public static final int WH_KEYBOARD = 2;
  public static final int WH_MOUSE = 7;
  public static final int WH_KEYBOARD_LL = 13;
  public static final int WH_MOUSE_LL = 14;
  public static final int WM_PAINT = 15;
  public static final int WM_CLOSE = 16;
  public static final int WM_QUIT = 18;
  public static final int WM_SHOWWINDOW = 24;
  public static final int WM_DRAWITEM = 43;
  public static final int WM_KEYDOWN = 256;
  public static final int WM_CHAR = 258;
  public static final int WM_SYSCOMMAND = 274;
  public static final int WM_MDIMAXIMIZE = 549;
  public static final int WM_HOTKEY = 786;
  public static final int WM_KEYUP = 257;
  public static final int WM_SYSKEYDOWN = 260;
  public static final int WM_SYSKEYUP = 261;
  public static final int WM_SESSION_CHANGE = 689;
  public static final int WM_CREATE = 1;
  public static final int WM_SIZE = 5;
  public static final int WM_DESTROY = 2;
  public static final int WM_DEVICECHANGE = 537;
  public static final int SM_CXSCREEN = 0;
  public static final int SM_CYSCREEN = 1;
  public static final int SM_CXVSCROLL = 2;
  public static final int SM_CYHSCROLL = 3;
  public static final int SM_CYCAPTION = 4;
  public static final int SM_CXBORDER = 5;
  public static final int SM_CYBORDER = 6;
  public static final int SM_CXDLGFRAME = 7;
  public static final int SM_CYDLGFRAME = 8;
  public static final int SM_CYVTHUMB = 9;
  public static final int SM_CXHTHUMB = 10;
  public static final int SM_CXICON = 11;
  public static final int SM_CYICON = 12;
  public static final int SM_CXCURSOR = 13;
  public static final int SM_CYCURSOR = 14;
  public static final int SM_CYMENU = 15;
  public static final int SM_CXFULLSCREEN = 16;
  public static final int SM_CYFULLSCREEN = 17;
  public static final int SM_CYKANJIWINDOW = 18;
  public static final int SM_MOUSEPRESENT = 19;
  public static final int SM_CYVSCROLL = 20;
  public static final int SM_CXHSCROLL = 21;
  public static final int SM_DEBUG = 22;
  public static final int SM_SWAPBUTTON = 23;
  public static class HHOOK
    extends WinNT.HANDLE
  {
    public HHOOK() {}
  }
  
  public static abstract interface HOOKPROC
    extends StdCallLibrary.StdCallCallback
  {}
  
  public static class KBDLLHOOKSTRUCT
    extends Structure
  {
    public int vkCode;
    public int scanCode;
    public int flags;
    public int time;
    public BaseTSD.ULONG_PTR dwExtraInfo;
    
    public KBDLLHOOKSTRUCT() {}
    
    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "vkCode", "scanCode", "flags", "time", "dwExtraInfo" });
    }
  }
  

  public static final int SM_RESERVED1 = 24;
  
  public static final int SM_RESERVED2 = 25;
  
  public static final int SM_RESERVED3 = 26;
  
  public static final int SM_RESERVED4 = 27;
  
  public static final int SM_CXMIN = 28;
  
  public static final int SM_CYMIN = 29;
  
  public static final int SM_CXSIZE = 30;
  
  public static final int SM_CYSIZE = 31;
  
  public static final int SM_CXFRAME = 32;
  
  public static final int SM_CYFRAME = 33;
  
  public static final int SM_CXMINTRACK = 34;
  
  public static final int SM_CYMINTRACK = 35;
  
  public static final int SM_CXDOUBLECLK = 36;
  
  public static final int SM_CYDOUBLECLK = 37;
  
  public static final int SM_CXICONSPACING = 38;
  
  public static final int SM_CYICONSPACING = 39;
  
  public static final int SM_MENUDROPALIGNMENT = 40;
  
  public static final int SM_PENWINDOWS = 41;
  
  public static final int SM_DBCSENABLED = 42;
  
  public static final int SM_CMOUSEBUTTONS = 43;
  
  public static final int SM_CXFIXEDFRAME = 7;
  
  public static final int SM_CYFIXEDFRAME = 8;
  
  public static final int SM_CXSIZEFRAME = 32;
  
  public static final int SM_CYSIZEFRAME = 33;
  
  public static final int SM_SECURE = 44;
  
  public static final int SM_CXEDGE = 45;
  
  public static final int SM_CYEDGE = 46;
  
  public static final int SM_CXMINSPACING = 47;
  
  public static final int SM_CYMINSPACING = 48;
  
  public static final int SM_CXSMICON = 49;
  
  public static final int SM_CYSMICON = 50;
  
  public static final int SM_CYSMCAPTION = 51;
  
  public static final int SM_CXSMSIZE = 52;
  
  public static final int SM_CYSMSIZE = 53;
  
  public static final int SM_CXMENUSIZE = 54;
  
  public static final int SM_CYMENUSIZE = 55;
  
  public static final int SM_ARRANGE = 56;
  
  public static final int SM_CXMINIMIZED = 57;
  
  public static final int SM_CYMINIMIZED = 58;
  
  public static final int SM_CXMAXTRACK = 59;
  
  public static final int SM_CYMAXTRACK = 60;
  
  public static final int SM_CXMAXIMIZED = 61;
  
  public static final int SM_CYMAXIMIZED = 62;
  
  public static final int SM_NETWORK = 63;
  
  public static final int SM_CLEANBOOT = 67;
  
  public static final int SM_CXDRAG = 68;
  
  public static final int SM_CYDRAG = 69;
  
  public static final int SM_SHOWSOUNDS = 70;
  
  public static final int SM_CXMENUCHECK = 71;
  
  public static final int SM_CYMENUCHECK = 72;
  
  public static final int SM_SLOWMACHINE = 73;
  
  public static final int SM_MIDEASTENABLED = 74;
  
  public static final int SM_MOUSEWHEELPRESENT = 75;
  
  public static final int SM_XVIRTUALSCREEN = 76;
  
  public static final int SM_YVIRTUALSCREEN = 77;
  
  public static final int SM_CXVIRTUALSCREEN = 78;
  
  public static final int SM_CYVIRTUALSCREEN = 79;
  
  public static final int SM_CMONITORS = 80;
  
  public static final int SM_SAMEDISPLAYFORMAT = 81;
  
  public static final int SM_IMMENABLED = 82;
  
  public static final int SM_CXFOCUSBORDER = 83;
  
  public static final int SM_CYFOCUSBORDER = 84;
  
  public static final int SM_TABLETPC = 86;
  
  public static final int SM_MEDIACENTER = 87;
  
  public static final int SM_STARTER = 88;
  
  public static final int SM_SERVERR2 = 89;
  
  public static final int SM_MOUSEHORIZONTALWHEELPRESENT = 91;
  
  public static final int SM_CXPADDEDBORDER = 92;
  
  public static final int SM_REMOTESESSION = 4096;
  
  public static final int SM_SHUTTINGDOWN = 8192;
  
  public static final int SM_REMOTECONTROL = 8193;
  
  public static final int SM_CARETBLINKINGENABLED = 8194;
  
  public static final int SW_HIDE = 0;
  
  public static final int SW_SHOWNORMAL = 1;
  
  public static final int SW_NORMAL = 1;
  
  public static final int SW_SHOWMINIMIZED = 2;
  
  public static final int SW_SHOWMAXIMIZED = 3;
  
  public static final int SW_MAXIMIZE = 3;
  
  public static final int SW_SHOWNOACTIVATE = 4;
  
  public static final int SW_SHOW = 5;
  
  public static final int SW_MINIMIZE = 6;
  
  public static final int SW_SHOWMINNOACTIVE = 7;
  
  public static final int SW_SHOWNA = 8;
  
  public static final int SW_RESTORE = 9;
  
  public static final int SW_SHOWDEFAULT = 10;
  
  public static final int SW_FORCEMINIMIZE = 11;
  
  public static final int SW_MAX = 11;
  
  public static final int RDW_INVALIDATE = 1;
  
  public static final int RDW_INTERNALPAINT = 2;
  
  public static final int RDW_ERASE = 4;
  
  public static final int RDW_VALIDATE = 8;
  
  public static final int RDW_NOINTERNALPAINT = 16;
  
  public static final int RDW_NOERASE = 32;
  
  public static final int RDW_NOCHILDREN = 64;
  
  public static final int RDW_ALLCHILDREN = 128;
  
  public static final int RDW_UPDATENOW = 256;
  
  public static final int RDW_ERASENOW = 512;
  public static final int RDW_FRAME = 1024;
  public static final int RDW_NOFRAME = 2048;
  public static final int GW_HWNDFIRST = 0;
  public static final int GW_HWNDLAST = 1;
  public static final int GW_HWNDNEXT = 2;
  public static final int GW_HWNDPREV = 3;
  public static final int GW_OWNER = 4;
  public static final int GW_CHILD = 5;
  public static final int GW_ENABLEDPOPUP = 6;
  public static final int SWP_NOZORDER = 4;
  public static final int SC_MINIMIZE = 61472;
  public static final int SC_MAXIMIZE = 61488;
  public static class HARDWAREINPUT
    extends Structure
  {
    public WinDef.DWORD uMsg;
    public WinDef.WORD wParamL;
    public WinDef.WORD wParamH;
    public HARDWAREINPUT() {}
    
    public static class ByReference
      extends WinUser.HARDWAREINPUT
      implements Structure.ByReference
    {
      public ByReference() {}
      
      public ByReference(Pointer memory)
      {
        super();
      }
    }
    


    public HARDWAREINPUT(Pointer memory)
    {
      super();
      read();
    }
    



    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "uMsg", "wParamL", "wParamH" });
    }
  }
  
  public static class INPUT
    extends Structure
  {
    public static final int INPUT_MOUSE = 0;
    public static final int INPUT_KEYBOARD = 1;
    public static final int INPUT_HARDWARE = 2;
    public WinDef.DWORD type;
    public INPUT() {}
    
    public static class ByReference
      extends WinUser.INPUT implements Structure.ByReference
    {
      public ByReference() {}
      
      public ByReference(Pointer memory)
      {
        super();
      }
    }
    


    public INPUT(Pointer memory)
    {
      super();
      read();
    }
    

    public INPUT_UNION input = new INPUT_UNION();
    

    protected List getFieldOrder() { return Arrays.asList(new String[] { "type", "input" }); }
    
    public static class INPUT_UNION extends Union {
      public WinUser.MOUSEINPUT mi;
      public WinUser.KEYBDINPUT ki;
      public WinUser.HARDWAREINPUT hi;
      
      public INPUT_UNION() {}
      
      public INPUT_UNION(Pointer memory) { super();
        read();
      }
    }
  }
  


  public static class KEYBDINPUT
    extends Structure
  {
    public static final int KEYEVENTF_EXTENDEDKEY = 1;
    

    public static final int KEYEVENTF_KEYUP = 2;
    

    public static final int KEYEVENTF_UNICODE = 4;
    

    public static final int KEYEVENTF_SCANCODE = 8;
    

    public WinDef.WORD wVk;
    
    public WinDef.WORD wScan;
    
    public WinDef.DWORD dwFlags;
    
    public WinDef.DWORD time;
    
    public BaseTSD.ULONG_PTR dwExtraInfo;
    
    public KEYBDINPUT() {}
    

    public static class ByReference
      extends WinUser.KEYBDINPUT
      implements Structure.ByReference
    {
      public ByReference() {}
      

      public ByReference(Pointer memory)
      {
        super();
      }
    }
    


    public KEYBDINPUT(Pointer memory)
    {
      super();
      read();
    }
    






























    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "wVk", "wScan", "dwFlags", "time", "dwExtraInfo" });
    }
  }
  
  public static class MOUSEINPUT extends Structure {
    public WinDef.LONG dx;
    public WinDef.LONG dy;
    public WinDef.DWORD mouseData;
    public WinDef.DWORD dwFlags;
    public WinDef.DWORD time;
    public BaseTSD.ULONG_PTR dwExtraInfo;
    public MOUSEINPUT() {}
    
    public static class ByReference extends WinUser.MOUSEINPUT implements Structure.ByReference {
      public ByReference() {}
      
      public ByReference(Pointer memory) { super(); }
    }
    



    public MOUSEINPUT(Pointer memory)
    {
      super();
      read();
    }
    






    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "dx", "dy", "mouseData", "dwFlags", "time", "dwExtraInfo" });
    }
  }
  


  public static class LASTINPUTINFO
    extends Structure
  {
    public int cbSize = size();
    public int dwTime;
    
    public LASTINPUTINFO() {}
    
    protected List getFieldOrder() {
      return Arrays.asList(new String[] { "cbSize", "dwTime" });
    }
  }
  












  public static class WNDCLASSEX
    extends Structure
  {
    public WNDCLASSEX() {}
    












    public WNDCLASSEX(Pointer memory)
    {
      super();
      read();
    }
    

    public int cbSize = size();
    


    public int style;
    


    public Callback lpfnWndProc;
    


    public int cbClsExtra;
    

    public int cbWndExtra;
    

    public WinDef.HINSTANCE hInstance;
    

    public WinDef.HICON hIcon;
    

    public WinDef.HCURSOR hCursor;
    

    public WinDef.HBRUSH hbrBackground;
    

    public String lpszMenuName;
    

    public WString lpszClassName;
    

    public WinDef.HICON hIconSm;
    


    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "cbSize", "style", "lpfnWndProc", "cbClsExtra", "cbWndExtra", "hInstance", "hIcon", "hCursor", "hbrBackground", "lpszMenuName", "lpszClassName", "hIconSm" });
    }
    
    public static class ByReference
      extends WinUser.WNDCLASSEX
      implements Structure.ByReference
    {
      public ByReference() {}
    }
  }
  
  public static abstract interface WindowProc
    extends Callback
  {
    public abstract WinDef.LRESULT callback(WinDef.HWND paramHWND, int paramInt, WinDef.WPARAM paramWPARAM, WinDef.LPARAM paramLPARAM);
  }
}
