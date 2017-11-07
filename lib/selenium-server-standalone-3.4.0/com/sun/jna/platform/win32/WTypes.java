package com.sun.jna.platform.win32;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;
import com.sun.jna.Structure.ByReference;
import com.sun.jna.ptr.ByReference;



























public abstract interface WTypes
{
  public static final int CLSCTX_INPROC_SERVER = 1;
  public static final int CLSCTX_INPROC_HANDLER = 2;
  public static final int CLSCTX_LOCAL_SERVER = 4;
  public static final int CLSCTX_INPROC_SERVER16 = 8;
  public static final int CLSCTX_REMOTE_SERVER = 16;
  public static final int CLSCTX_INPROC_HANDLER16 = 32;
  public static final int CLSCTX_RESERVED1 = 64;
  public static final int CLSCTX_RESERVED2 = 128;
  public static final int CLSCTX_RESERVED3 = 256;
  public static final int CLSCTX_RESERVED4 = 512;
  public static final int CLSCTX_NO_CODE_DOWNLOAD = 1024;
  public static final int CLSCTX_RESERVED5 = 2048;
  public static final int CLSCTX_NO_CUSTOM_MARSHAL = 4096;
  public static final int CLSCTX_ENABLE_CODE_DOWNLOAD = 8192;
  public static final int CLSCTX_NO_FAILURE_LOG = 16384;
  public static final int CLSCTX_DISABLE_AAA = 32768;
  public static final int CLSCTX_ENABLE_AAA = 65536;
  public static final int CLSCTX_FROM_DEFAULT_CONTEXT = 131072;
  public static final int CLSCTX_ACTIVATE_32_BIT_SERVER = 262144;
  public static final int CLSCTX_ACTIVATE_64_BIT_SERVER = 524288;
  public static final int CLSCTX_ENABLE_CLOAKING = 1048576;
  public static final int CLSCTX_APPCONTAINER = 4194304;
  public static final int CLSCTX_ACTIVATE_AAA_AS_IU = 8388608;
  public static final int CLSCTX_PS_DLL = Integer.MIN_VALUE;
  public static final int CLSCTX_SERVER = 21;
  public static final int CLSCTX_ALL = 7;
  
  public static class BSTR
    extends PointerType
  {
    public BSTR()
    {
      super();
    }
    
    public BSTR(Pointer pointer) {
      super();
    }
    
    public BSTR(String value) {
      super();
      setValue(value);
    }
    
    public void setValue(String value) {
      getPointer().setWideString(0L, value);
    }
    
    public String getValue() {
      Pointer pointer = getPointer();
      String str = null;
      if (pointer != null) {
        str = pointer.getWideString(0L);
      }
      return str;
    }
    


    public String toString() { return getValue(); }
    
    public static class ByReference extends WTypes.BSTR implements Structure.ByReference { public ByReference() {}
    }
  }
  
  public static class BSTRByReference extends ByReference { public BSTRByReference() { super(); }
    
    public BSTRByReference(WTypes.BSTR value)
    {
      this();
      setValue(value);
    }
    
    public void setValue(WTypes.BSTR value) {
      getPointer().setPointer(0L, value.getPointer());
    }
    
    public WTypes.BSTR getValue() {
      return new WTypes.BSTR(getPointer().getPointer(0L));
    }
    
    public String getString() {
      return getValue().getValue();
    }
  }
  

  public static class LPSTR
    extends PointerType
  {
    public LPSTR()
    {
      super();
    }
    
    public LPSTR(Pointer pointer) {
      super();
    }
    
    public LPSTR(String value) {
      this();
      setValue(value);
    }
    
    public void setValue(String value) {
      getPointer().setWideString(0L, value);
    }
    
    public String getValue() {
      Pointer pointer = getPointer();
      String str = null;
      if (pointer != null) {
        str = pointer.getWideString(0L);
      }
      return str;
    }
    
    public String toString()
    {
      return getValue();
    }
    
    public static class ByReference extends WTypes.BSTR implements Structure.ByReference {
      public ByReference() {}
    }
  }
  
  public static class LPWSTR extends PointerType {
    public LPWSTR() {
      super();
    }
    
    public LPWSTR(Pointer pointer) {
      super();
    }
    
    public LPWSTR(String value) {
      this();
      setValue(value);
    }
    
    public void setValue(String value) {
      getPointer().setWideString(0L, value);
    }
    
    public String getValue() {
      Pointer pointer = getPointer();
      String str = null;
      if (pointer != null) {
        str = pointer.getWideString(0L);
      }
      return str;
    }
    
    public String toString()
    {
      return getValue();
    }
    
    public static class ByReference extends WTypes.BSTR implements Structure.ByReference {
      public ByReference() {}
    }
  }
  
  public static class LPOLESTR extends PointerType {
    public LPOLESTR() {
      super();
    }
    
    public LPOLESTR(Pointer pointer) {
      super();
    }
    
    public LPOLESTR(String value) {
      super();
      setValue(value);
    }
    
    public void setValue(String value) {
      getPointer().setWideString(0L, value);
    }
    
    public String getValue() {
      Pointer pointer = getPointer();
      String str = null;
      if (pointer != null) {
        str = pointer.getWideString(0L);
      }
      return str;
    }
    


    public String toString() { return getValue(); }
    
    public static class ByReference extends WTypes.BSTR implements Structure.ByReference { public ByReference() {}
    }
  }
  
  public static class VARTYPE extends WinDef.USHORT { public VARTYPE() { this(0); }
    
    public VARTYPE(int value)
    {
      super();
    }
  }
}
