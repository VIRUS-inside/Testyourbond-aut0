package com.sun.jna.platform.win32;

import com.sun.jna.IntegerType;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;
import com.sun.jna.Structure;
import com.sun.jna.Structure.ByReference;
import com.sun.jna.ptr.ByReference;
import com.sun.jna.win32.StdCallLibrary;
import java.awt.Rectangle;
import java.util.Arrays;
import java.util.List;




























public abstract interface WinDef
  extends StdCallLibrary
{
  public static final int MAX_PATH = 260;
  
  public static class WORD
    extends IntegerType
  {
    public static final int SIZE = 2;
    
    public WORD()
    {
      this(0L);
    }
    





    public WORD(long value)
    {
      super(value, true);
    }
  }
  




  public static class WORDByReference
    extends ByReference
  {
    public WORDByReference()
    {
      this(new WinDef.WORD(0L));
    }
    




    public WORDByReference(WinDef.WORD value)
    {
      super();
      setValue(value);
    }
    




    public void setValue(WinDef.WORD value)
    {
      getPointer().setShort(0L, value.shortValue());
    }
    




    public WinDef.WORD getValue()
    {
      return new WinDef.WORD(getPointer().getInt(0L));
    }
  }
  



  public static class DWORD
    extends IntegerType
  {
    public static final int SIZE = 4;
    


    public DWORD()
    {
      this(0L);
    }
    





    public DWORD(long value)
    {
      super(value, true);
    }
    




    public WinDef.WORD getLow()
    {
      return new WinDef.WORD(longValue() & 0xFF);
    }
    




    public WinDef.WORD getHigh()
    {
      return new WinDef.WORD(longValue() >> 16 & 0xFF);
    }
  }
  




  public static class DWORDByReference
    extends ByReference
  {
    public DWORDByReference()
    {
      this(new WinDef.DWORD(0L));
    }
    




    public DWORDByReference(WinDef.DWORD value)
    {
      super();
      setValue(value);
    }
    




    public void setValue(WinDef.DWORD value)
    {
      getPointer().setInt(0L, value.intValue());
    }
    




    public WinDef.DWORD getValue()
    {
      return new WinDef.DWORD(getPointer().getInt(0L));
    }
  }
  



  public static class LONG
    extends IntegerType
  {
    public static final int SIZE = Native.LONG_SIZE;
    


    public LONG()
    {
      this(0L);
    }
    




    public LONG(long value)
    {
      super(value);
    }
  }
  




  public static class LONGByReference
    extends ByReference
  {
    public LONGByReference()
    {
      this(new WinDef.LONG(0L));
    }
    




    public LONGByReference(WinDef.LONG value)
    {
      super();
      setValue(value);
    }
    




    public void setValue(WinDef.LONG value)
    {
      getPointer().setInt(0L, value.intValue());
    }
    




    public WinDef.LONG getValue()
    {
      return new WinDef.LONG(getPointer().getInt(0L));
    }
  }
  



  public static class LONGLONG
    extends IntegerType
  {
    public static final int SIZE = Native.LONG_SIZE * 2;
    


    public LONGLONG()
    {
      this(0L);
    }
    




    public LONGLONG(long value)
    {
      super(value, false);
    }
  }
  




  public static class LONGLONGByReference
    extends ByReference
  {
    public LONGLONGByReference()
    {
      this(new WinDef.LONGLONG(0L));
    }
    




    public LONGLONGByReference(WinDef.LONGLONG value)
    {
      super();
      setValue(value);
    }
    




    public void setValue(WinDef.LONGLONG value)
    {
      getPointer().setLong(0L, value.longValue());
    }
    




    public WinDef.LONGLONG getValue()
    {
      return new WinDef.LONGLONG(getPointer().getLong(0L));
    }
  }
  






  public static class HDC
    extends WinNT.HANDLE
  {
    public HDC() {}
    






    public HDC(Pointer p)
    {
      super();
    }
  }
  






  public static class HICON
    extends WinNT.HANDLE
  {
    public HICON() {}
    






    public HICON(Pointer p)
    {
      super();
    }
  }
  






  public static class HCURSOR
    extends WinDef.HICON
  {
    public HCURSOR() {}
    






    public HCURSOR(Pointer p)
    {
      super();
    }
  }
  






  public static class HMENU
    extends WinNT.HANDLE
  {
    public HMENU() {}
    






    public HMENU(Pointer p)
    {
      super();
    }
  }
  






  public static class HPEN
    extends WinNT.HANDLE
  {
    public HPEN() {}
    






    public HPEN(Pointer p)
    {
      super();
    }
  }
  






  public static class HRSRC
    extends WinNT.HANDLE
  {
    public HRSRC() {}
    






    public HRSRC(Pointer p)
    {
      super();
    }
  }
  






  public static class HPALETTE
    extends WinNT.HANDLE
  {
    public HPALETTE() {}
    






    public HPALETTE(Pointer p)
    {
      super();
    }
  }
  






  public static class HBITMAP
    extends WinNT.HANDLE
  {
    public HBITMAP() {}
    






    public HBITMAP(Pointer p)
    {
      super();
    }
  }
  






  public static class HRGN
    extends WinNT.HANDLE
  {
    public HRGN() {}
    






    public HRGN(Pointer p)
    {
      super();
    }
  }
  






  public static class HWND
    extends WinNT.HANDLE
  {
    public HWND() {}
    






    public HWND(Pointer p)
    {
      super();
    }
  }
  




  public static class HINSTANCE
    extends WinNT.HANDLE
  {
    public HINSTANCE() {}
  }
  




  public static class HMODULE
    extends WinDef.HINSTANCE
  {
    public HMODULE() {}
  }
  



  public static class HFONT
    extends WinNT.HANDLE
  {
    public HFONT() {}
    



    public HFONT(Pointer p)
    {
      super();
    }
  }
  




  public static class LPARAM
    extends BaseTSD.LONG_PTR
  {
    public LPARAM()
    {
      this(0L);
    }
    





    public LPARAM(long value)
    {
      super();
    }
  }
  




  public static class LRESULT
    extends BaseTSD.LONG_PTR
  {
    public LRESULT()
    {
      this(0L);
    }
    





    public LRESULT(long value)
    {
      super();
    }
  }
  


  public static class INT_PTR
    extends IntegerType
  {
    public INT_PTR()
    {
      super();
    }
    





    public INT_PTR(long value)
    {
      super(value);
    }
    




    public Pointer toPointer()
    {
      return Pointer.createConstant(longValue());
    }
  }
  




  public static class UINT_PTR
    extends IntegerType
  {
    public UINT_PTR()
    {
      super();
    }
    





    public UINT_PTR(long value)
    {
      super(value, true);
    }
    




    public Pointer toPointer()
    {
      return Pointer.createConstant(longValue());
    }
  }
  




  public static class WPARAM
    extends WinDef.UINT_PTR
  {
    public WPARAM()
    {
      this(0L);
    }
    





    public WPARAM(long value)
    {
      super();
    }
  }
  


  public static class RECT
    extends Structure
  {
    public int left;
    

    public int top;
    

    public int right;
    

    public int bottom;
    


    public RECT() {}
    

    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "left", "top", "right", "bottom" });
    }
    





    public Rectangle toRectangle()
    {
      return new Rectangle(left, top, right - left, bottom - top);
    }
    




    public String toString()
    {
      return "[(" + left + "," + top + ")(" + right + "," + bottom + ")]";
    }
  }
  



  public static class ULONG
    extends IntegerType
  {
    public static final int SIZE = Native.LONG_SIZE;
    


    public ULONG()
    {
      this(0);
    }
    





    public ULONG(int value)
    {
      super(value, true);
    }
  }
  




  public static class ULONGByReference
    extends ByReference
  {
    public ULONGByReference()
    {
      this(new WinDef.ULONG(0));
    }
    




    public ULONGByReference(WinDef.ULONG value)
    {
      super();
      setValue(value);
    }
    




    public void setValue(WinDef.ULONG value)
    {
      getPointer().setInt(0L, value.intValue());
    }
    




    public WinDef.ULONG getValue()
    {
      return new WinDef.ULONG(getPointer().getInt(0L));
    }
  }
  



  public static class ULONGLONG
    extends IntegerType
  {
    public static final int SIZE = Native.LONG_SIZE * 2;
    


    public ULONGLONG()
    {
      this(0L);
    }
    




    public ULONGLONG(long value)
    {
      super(value, true);
    }
  }
  




  public static class ULONGLONGByReference
    extends ByReference
  {
    public ULONGLONGByReference()
    {
      this(new WinDef.ULONGLONG(0L));
    }
    




    public ULONGLONGByReference(WinDef.ULONGLONG value)
    {
      super();
      setValue(value);
    }
    




    public void setValue(WinDef.ULONGLONG value)
    {
      getPointer().setLong(0L, value.longValue());
    }
    




    public WinDef.ULONGLONG getValue()
    {
      return new WinDef.ULONGLONG(getPointer().getLong(0L));
    }
  }
  



  public static class DWORDLONG
    extends IntegerType
  {
    public static final int SIZE = 8;
    


    public DWORDLONG()
    {
      this(0L);
    }
    





    public DWORDLONG(long value)
    {
      super(value, true);
    }
  }
  






  public static class HBRUSH
    extends WinNT.HANDLE
  {
    public HBRUSH() {}
    






    public HBRUSH(Pointer p)
    {
      super();
    }
  }
  




  public static class ATOM
    extends WinDef.WORD
  {
    public ATOM()
    {
      this(0L);
    }
    





    public ATOM(long value)
    {
      super();
    }
  }
  




  public static class PVOID
    extends PointerType
  {
    public PVOID() {}
    




    public PVOID(Pointer pointer)
    {
      super();
    }
  }
  




  public static class LPVOID
    extends BaseTSD.LONG_PTR
  {
    public LPVOID()
    {
      this(0L);
    }
    





    public LPVOID(long value)
    {
      super();
    }
  }
  




  public static class POINT
    extends Structure
  {
    public int x;
    



    public int y;
    




    public POINT() {}
    



    public POINT(Pointer memory)
    {
      super();
      read();
    }
    










    public POINT(int x, int y)
    {
      this.x = x;
      this.y = y;
    }
    




    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "x", "y" });
    }
    
    public static class ByReference
      extends WinDef.POINT implements Structure.ByReference
    {
      public ByReference() {}
    }
  }
  
  public static class USHORT extends IntegerType
  {
    public static final int SIZE = 2;
    
    public USHORT()
    {
      this(0L);
    }
    





    public USHORT(long value)
    {
      super(value, true);
    }
  }
  




  public static class USHORTByReference
    extends ByReference
  {
    public USHORTByReference()
    {
      this(new WinDef.USHORT(0L));
    }
    




    public USHORTByReference(WinDef.USHORT value)
    {
      super();
      setValue(value);
    }
    




    public USHORTByReference(short value)
    {
      super();
      setValue(new WinDef.USHORT(value));
    }
    




    public void setValue(WinDef.USHORT value)
    {
      getPointer().setShort(0L, value.shortValue());
    }
    




    public WinDef.USHORT getValue()
    {
      return new WinDef.USHORT(getPointer().getShort(0L));
    }
  }
  



  public static class SHORT
    extends IntegerType
  {
    public static final int SIZE = 2;
    


    public SHORT()
    {
      this(0L);
    }
    





    public SHORT(long value)
    {
      super(value, false);
    }
  }
  



  public static class UINT
    extends IntegerType
  {
    public static final int SIZE = 4;
    


    public UINT()
    {
      this(0L);
    }
    





    public UINT(long value)
    {
      super(value, true);
    }
  }
  




  public static class UINTByReference
    extends ByReference
  {
    public UINTByReference()
    {
      this(new WinDef.UINT(0L));
    }
    




    public UINTByReference(WinDef.UINT value)
    {
      super();
      setValue(value);
    }
    




    public void setValue(WinDef.UINT value)
    {
      getPointer().setInt(0L, value.intValue());
    }
    




    public WinDef.UINT getValue()
    {
      return new WinDef.UINT(getPointer().getInt(0L));
    }
  }
  




  public static class SCODE
    extends WinDef.ULONG
  {
    public SCODE()
    {
      this(0);
    }
    





    public SCODE(int value)
    {
      super();
    }
  }
  




  public static class SCODEByReference
    extends ByReference
  {
    public SCODEByReference()
    {
      this(new WinDef.SCODE(0));
    }
    




    public SCODEByReference(WinDef.SCODE value)
    {
      super();
      setValue(value);
    }
    




    public void setValue(WinDef.SCODE value)
    {
      getPointer().setInt(0L, value.intValue());
    }
    




    public WinDef.SCODE getValue()
    {
      return new WinDef.SCODE(getPointer().getInt(0L));
    }
  }
  




  public static class LCID
    extends WinDef.DWORD
  {
    public LCID()
    {
      super();
    }
    




    public LCID(long value)
    {
      super();
    }
  }
  



  public static class BOOL
    extends IntegerType
  {
    public static final int SIZE = 4;
    


    public BOOL()
    {
      this(0L);
    }
    




    public BOOL(long value)
    {
      super(value, false);
    }
    
    public boolean booleanValue() {
      if (intValue() > 0) {
        return true;
      }
      return false;
    }
  }
  




  public static class BOOLByReference
    extends ByReference
  {
    public BOOLByReference()
    {
      this(new WinDef.BOOL(0L));
    }
    




    public BOOLByReference(WinDef.BOOL value)
    {
      super();
      setValue(value);
    }
    




    public void setValue(WinDef.BOOL value)
    {
      getPointer().setInt(0L, value.intValue());
    }
    




    public WinDef.BOOL getValue()
    {
      return new WinDef.BOOL(getPointer().getInt(0L));
    }
  }
  



  public static class UCHAR
    extends IntegerType
  {
    public static final int SIZE = 1;
    


    public UCHAR()
    {
      this(0L);
    }
    




    public UCHAR(long value)
    {
      super(value, true);
    }
  }
  




  public static class BYTE
    extends WinDef.UCHAR
  {
    public BYTE()
    {
      this(0L);
    }
    




    public BYTE(long value)
    {
      super();
    }
  }
  



  public static class CHAR
    extends IntegerType
  {
    public static final int SIZE = 1;
    


    public CHAR()
    {
      this(0L);
    }
    




    public CHAR(long value)
    {
      super(value, false);
    }
  }
  




  public static class CHARByReference
    extends ByReference
  {
    public CHARByReference()
    {
      this(new WinDef.CHAR(0L));
    }
    




    public CHARByReference(WinDef.CHAR value)
    {
      super();
      setValue(value);
    }
    




    public void setValue(WinDef.CHAR value)
    {
      getPointer().setByte(0L, value.byteValue());
    }
    




    public WinDef.CHAR getValue()
    {
      return new WinDef.CHAR(getPointer().getChar(0L));
    }
  }
  






  public static class HGLRC
    extends WinNT.HANDLE
  {
    public HGLRC() {}
    






    public HGLRC(Pointer p)
    {
      super();
    }
  }
  






  public static class HGLRCByReference
    extends WinNT.HANDLEByReference
  {
    public HGLRCByReference() {}
    






    public HGLRCByReference(WinDef.HGLRC h)
    {
      super();
    }
  }
}
