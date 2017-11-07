package com.sun.jna.platform.win32;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.ByReference;
import com.sun.jna.Union;
import com.sun.jna.platform.win32.COM.Dispatch;
import com.sun.jna.platform.win32.COM.Dispatch.ByReference;
import com.sun.jna.platform.win32.COM.IDispatch;
import com.sun.jna.platform.win32.COM.IRecordInfo;
import com.sun.jna.platform.win32.COM.Unknown;
import com.sun.jna.platform.win32.COM.Unknown.ByReference;
import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.ptr.DoubleByReference;
import com.sun.jna.ptr.FloatByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.ShortByReference;
import java.util.Arrays;
import java.util.Date;
import java.util.List;































public abstract interface Variant
{
  public static final int VT_EMPTY = 0;
  public static final int VT_NULL = 1;
  public static final int VT_I2 = 2;
  public static final int VT_I4 = 3;
  public static final int VT_R4 = 4;
  public static final int VT_R8 = 5;
  public static final int VT_CY = 6;
  public static final int VT_DATE = 7;
  public static final int VT_BSTR = 8;
  public static final int VT_DISPATCH = 9;
  public static final int VT_ERROR = 10;
  public static final int VT_BOOL = 11;
  public static final int VT_VARIANT = 12;
  public static final int VT_UNKNOWN = 13;
  public static final int VT_DECIMAL = 14;
  public static final int VT_I1 = 16;
  public static final int VT_UI1 = 17;
  public static final int VT_UI2 = 18;
  public static final int VT_UI4 = 19;
  public static final int VT_I8 = 20;
  public static final int VT_UI8 = 21;
  public static final int VT_INT = 22;
  public static final int VT_UINT = 23;
  public static final int VT_VOID = 24;
  public static final int VT_HRESULT = 25;
  public static final int VT_PTR = 26;
  public static final int VT_SAFEARRAY = 27;
  public static final int VT_CARRAY = 28;
  public static final int VT_USERDEFINED = 29;
  public static final int VT_LPSTR = 30;
  public static final int VT_LPWSTR = 31;
  public static final int VT_RECORD = 36;
  public static final int VT_INT_PTR = 37;
  public static final int VT_UINT_PTR = 38;
  public static final int VT_FILETIME = 64;
  public static final int VT_BLOB = 65;
  public static final int VT_STREAM = 66;
  public static final int VT_STORAGE = 67;
  public static final int VT_STREAMED_OBJECT = 68;
  public static final int VT_STORED_OBJECT = 69;
  public static final int VT_BLOB_OBJECT = 70;
  public static final int VT_CF = 71;
  public static final int VT_CLSID = 72;
  public static final int VT_VERSIONED_STREAM = 73;
  public static final int VT_BSTR_BLOB = 4095;
  public static final int VT_VECTOR = 4096;
  public static final int VT_ARRAY = 8192;
  public static final int VT_BYREF = 16384;
  public static final int VT_RESERVED = 32768;
  public static final int VT_ILLEGAL = 65535;
  public static final int VT_ILLEGALMASKED = 4095;
  public static final int VT_TYPEMASK = 4095;
  public static final OaIdl.VARIANT_BOOL VARIANT_TRUE = new OaIdl.VARIANT_BOOL(65535L);
  public static final OaIdl.VARIANT_BOOL VARIANT_FALSE = new OaIdl.VARIANT_BOOL(0L);
  

  public static final long COM_DAYS_ADJUSTMENT = 25569L;
  

  public static final long MICRO_SECONDS_PER_DAY = 86400000L;
  

  public static class VARIANT
    extends Union
  {
    public _VARIANT _variant;
    
    public OaIdl.DECIMAL decVal;
    

    public VARIANT()
    {
      setType("_variant");
      read();
    }
    
    public VARIANT(Pointer pointer) {
      super();
      setType("_variant");
      read();
    }
    
    public VARIANT(WTypes.BSTR value) {
      this();
      setValue(8, value);
    }
    
    public VARIANT(WTypes.BSTRByReference value) {
      this();
      setValue(16392, value);
    }
    
    public VARIANT(OaIdl.VARIANT_BOOL value) {
      this();
      setValue(11, new WinDef.BOOL(value.intValue()));
    }
    
    public VARIANT(WinDef.BOOL value) {
      this();
      setValue(11, value);
    }
    
    public VARIANT(WinDef.LONG value) {
      this();
      setValue(3, value);
    }
    
    public VARIANT(WinDef.SHORT value) {
      this();
      setValue(2, value);
    }
    
    public VARIANT(OaIdl.DATE value) {
      this();
      setValue(7, value);
    }
    
    public VARIANT(short value) {
      this();
      setValue(2, Short.valueOf(value));
    }
    
    public VARIANT(int value) {
      this();
      setValue(3, Integer.valueOf(value));
    }
    
    public VARIANT(long value) {
      this();
      setValue(20, Long.valueOf(value));
    }
    
    public VARIANT(float value) {
      this();
      setValue(4, Float.valueOf(value));
    }
    
    public VARIANT(double value) {
      this();
      setValue(5, Double.valueOf(value));
    }
    
    public VARIANT(String value) {
      this();
      WTypes.BSTR bstrValue = OleAuto.INSTANCE.SysAllocString(value);
      setValue(8, bstrValue);
    }
    
    public VARIANT(boolean value) {
      this();
      if (value) {
        setValue(11, new WinDef.BOOL(Variant.VARIANT_TRUE.intValue()));
      } else
        setValue(11, new WinDef.BOOL(Variant.VARIANT_FALSE.intValue()));
    }
    
    public VARIANT(IDispatch value) {
      this();
      setValue(9, value);
    }
    
    public VARIANT(Date value) {
      this();
      OaIdl.DATE date = fromJavaDate(value);
      setValue(7, date);
    }
    
    public WTypes.VARTYPE getVarType() {
      read();
      return _variant.vt;
    }
    
    public void setVarType(short vt) {
      _variant.vt = new WTypes.VARTYPE(vt);
    }
    
    public void setValue(int vt, Object value) {
      setValue(new WTypes.VARTYPE(vt), value);
    }
    
    public void setValue(WTypes.VARTYPE vt, Object value) {
      switch (vt.intValue()) {
      case 2: 
        _variant.__variant.writeField("iVal", value);
        break;
      case 3: 
        _variant.__variant.writeField("lVal", value);
        break;
      case 20: 
        _variant.__variant.writeField("llVal", value);
        break;
      case 4: 
        _variant.__variant.writeField("fltVal", value);
        break;
      case 5: 
        _variant.__variant.writeField("dblVal", value);
        break;
      case 11: 
        _variant.__variant.writeField("boolVal", value);
        break;
      case 10: 
        _variant.__variant.writeField("scode", value);
        break;
      case 6: 
        _variant.__variant.writeField("cyVal", value);
        break;
      case 7: 
        _variant.__variant.writeField("date", value);
        break;
      case 8: 
        _variant.__variant.writeField("bstrVal", value);
        break;
      case 13: 
        _variant.__variant.writeField("punkVal", value);
        break;
      case 9: 
        _variant.__variant.writeField("pdispVal", value);
        break;
      case 27: 
        _variant.__variant.writeField("parray", value);
        break;
      case 8192: 
        _variant.__variant.writeField("parray", value);
        break;
      case 16401: 
        _variant.__variant.writeField("pbVal", value);
        break;
      case 16386: 
        _variant.__variant.writeField("piVal", value);
        break;
      case 16387: 
        _variant.__variant.writeField("plVal", value);
        break;
      case 16404: 
        _variant.__variant.writeField("pllVal", value);
        break;
      case 16388: 
        _variant.__variant.writeField("pfltVal", value);
        break;
      case 16389: 
        _variant.__variant.writeField("pdblVal", value);
        break;
      case 16395: 
        _variant.__variant.writeField("pboolVal", value);
        break;
      case 16394: 
        _variant.__variant.writeField("pscode", value);
        break;
      case 16390: 
        _variant.__variant.writeField("pcyVal", value);
        break;
      case 16391: 
        _variant.__variant.writeField("pdate", value);
        break;
      case 16392: 
        _variant.__variant.writeField("pbstrVal", value);
        break;
      case 16397: 
        _variant.__variant.writeField("ppunkVal", value);
        break;
      case 16393: 
        _variant.__variant.writeField("ppdispVal", value);
        break;
      case 24576: 
        _variant.__variant.writeField("pparray", value);
        break;
      case 16396: 
        _variant.__variant.writeField("pvarVal", value);
        break;
      case 16384: 
        _variant.__variant.writeField("byref", value);
        break;
      case 16: 
        _variant.__variant.writeField("cVal", value);
        break;
      case 18: 
        _variant.__variant.writeField("uiVal", value);
        break;
      case 19: 
        _variant.__variant.writeField("ulVal", value);
        break;
      case 21: 
        _variant.__variant.writeField("ullVal", value);
        break;
      case 22: 
        _variant.__variant.writeField("intVal", value);
        break;
      case 23: 
        _variant.__variant.writeField("uintVal", value);
        break;
      case 16398: 
        _variant.__variant.writeField("pdecVal", value);
        break;
      case 16400: 
        _variant.__variant.writeField("pcVal", value);
        break;
      case 16402: 
        _variant.__variant.writeField("puiVal", value);
        break;
      case 16403: 
        _variant.__variant.writeField("pulVal", value);
        break;
      case 16405: 
        _variant.__variant.writeField("pullVal", value);
        break;
      case 16406: 
        _variant.__variant.writeField("pintVal", value);
        break;
      case 16407: 
        _variant.__variant.writeField("puintVal", value);
      }
      
      
      _variant.writeField("vt", vt);
      write();
    }
    
    public Object getValue() {
      read();
      switch (getVarType().intValue()) {
      case 2: 
        return _variant.__variant.readField("iVal");
      case 3: 
        return _variant.__variant.readField("lVal");
      case 20: 
        return _variant.__variant.readField("llVal");
      case 4: 
        return _variant.__variant.readField("fltVal");
      case 5: 
        return _variant.__variant.readField("dblVal");
      case 11: 
        return _variant.__variant.readField("boolVal");
      case 10: 
        return _variant.__variant.readField("scode");
      case 6: 
        return _variant.__variant.readField("cyVal");
      case 7: 
        return _variant.__variant.readField("date");
      case 8: 
        return _variant.__variant.readField("bstrVal");
      case 13: 
        return _variant.__variant.readField("punkVal");
      case 9: 
        return _variant.__variant.readField("pdispVal");
      case 27: 
        return _variant.__variant.readField("parray");
      case 8192: 
        return _variant.__variant.readField("parray");
      case 16401: 
        return _variant.__variant.readField("pbVal");
      case 16386: 
        return _variant.__variant.readField("piVal");
      case 16387: 
        return _variant.__variant.readField("plVal");
      case 16404: 
        return _variant.__variant.readField("pllVal");
      case 16388: 
        return _variant.__variant.readField("pfltVal");
      case 16389: 
        return _variant.__variant.readField("pdblVal");
      case 16395: 
        return _variant.__variant.readField("pboolVal");
      case 16394: 
        return _variant.__variant.readField("pscode");
      case 16390: 
        return _variant.__variant.readField("pcyVal");
      case 16391: 
        return _variant.__variant.readField("pdate");
      case 16392: 
        return _variant.__variant.readField("pbstrVal");
      case 16397: 
        return _variant.__variant.readField("ppunkVal");
      case 16393: 
        return _variant.__variant.readField("ppdispVal");
      case 24576: 
        return _variant.__variant.readField("pparray");
      case 16396: 
        return _variant.__variant.readField("pvarVal");
      case 16384: 
        return _variant.__variant.readField("byref");
      case 16: 
        return _variant.__variant.readField("cVal");
      case 18: 
        return _variant.__variant.readField("uiVal");
      case 19: 
        return _variant.__variant.readField("ulVal");
      case 21: 
        return _variant.__variant.readField("ullVal");
      case 22: 
        return _variant.__variant.readField("intVal");
      case 23: 
        return _variant.__variant.readField("uintVal");
      case 16398: 
        return _variant.__variant.readField("pdecVal");
      case 16400: 
        return _variant.__variant.readField("pcVal");
      case 16402: 
        return _variant.__variant.readField("puiVal");
      case 16403: 
        return _variant.__variant.readField("pulVal");
      case 16405: 
        return _variant.__variant.readField("pullVal");
      case 16406: 
        return _variant.__variant.readField("pintVal");
      case 16407: 
        return _variant.__variant.readField("puintVal");
      }
      return null;
    }
    
    public int shortValue()
    {
      return ((Short)getValue()).shortValue();
    }
    
    public int intValue() {
      return ((Integer)getValue()).intValue();
    }
    
    public long longValue() {
      return ((Long)getValue()).longValue();
    }
    
    public float floatValue() {
      return ((Float)getValue()).floatValue();
    }
    
    public double doubleValue() {
      return ((Double)getValue()).doubleValue();
    }
    
    public String stringValue() {
      WTypes.BSTR bstr = (WTypes.BSTR)getValue();
      return bstr.getValue();
    }
    
    public boolean booleanValue() {
      return ((Boolean)getValue()).booleanValue();
    }
    
    public Date dateValue() {
      OaIdl.DATE varDate = (OaIdl.DATE)getValue();
      return toJavaDate(varDate);
    }
    
    protected Date toJavaDate(OaIdl.DATE varDate)
    {
      double doubleDate = date;
      long longDate = doubleDate;
      
      double doubleTime = doubleDate - longDate;
      long longTime = doubleTime * 86400000L;
      
      return new Date((longDate - 25569L) * 86400000L + longTime);
    }
    

    protected OaIdl.DATE fromJavaDate(Date javaDate)
    {
      long longTime = javaDate.getTime() % 86400000L;
      long longDate = (javaDate.getTime() - longTime) / 86400000L + 25569L;
      

      float floatTime = (float)longTime / 8.64E7F;
      
      float floatDateTime = floatTime + (float)longDate;
      return new OaIdl.DATE(floatDateTime);
    }
    
    public static class _VARIANT extends Structure
    {
      public WTypes.VARTYPE vt;
      public short wReserved1;
      public short wReserved2;
      public short wReserved3;
      public __VARIANT __variant;
      
      public _VARIANT() {}
      
      public _VARIANT(Pointer pointer)
      {
        super();
        read();
      }
      

      public static class __VARIANT
        extends Union
      {
        public WinDef.LONGLONG llVal;
        
        public WinDef.LONG lVal;
        
        public WinDef.BYTE bVal;
        
        public WinDef.SHORT iVal;
        
        public Float fltVal;
        
        public Double dblVal;
        
        public WinDef.BOOL boolVal;
        
        public WinDef.SCODE scode;
        
        public OaIdl.CURRENCY cyVal;
        
        public OaIdl.DATE date;
        
        public WTypes.BSTR bstrVal;
        
        public Unknown punkVal;
        
        public Dispatch pdispVal;
        
        public OaIdl.SAFEARRAY.ByReference parray;
        
        public ByteByReference pbVal;
        
        public ShortByReference piVal;
        
        public WinDef.LONGByReference plVal;
        
        public WinDef.LONGLONGByReference pllVal;
        
        public FloatByReference pfltVal;
        
        public DoubleByReference pdblVal;
        
        public OaIdl.VARIANT_BOOLByReference pboolVal;
        
        public OaIdl._VARIANT_BOOLByReference pbool;
        
        public WinDef.SCODEByReference pscode;
        
        public OaIdl.CURRENCY.ByReference pcyVal;
        
        public OaIdl.DATE.ByReference pdate;
        
        public WTypes.BSTR.ByReference pbstrVal;
        
        public Unknown.ByReference ppunkVal;
        
        public Dispatch.ByReference ppdispVal;
        
        public OaIdl.SAFEARRAY.ByReference pparray;
        
        public Variant.VARIANT.ByReference pvarVal;
        
        public WinDef.PVOID byref;
        
        public WinDef.CHAR cVal;
        
        public WinDef.USHORT uiVal;
        
        public WinDef.ULONG ulVal;
        
        public WinDef.ULONGLONG ullVal;
        
        public Integer intVal;
        
        public WinDef.UINT uintVal;
        
        public OaIdl.DECIMAL.ByReference pdecVal;
        
        public WinDef.CHARByReference pcVal;
        
        public WinDef.USHORTByReference puiVal;
        
        public WinDef.ULONGByReference pulVal;
        
        public WinDef.ULONGLONGByReference pullVal;
        
        public IntByReference pintVal;
        
        public WinDef.UINTByReference puintVal;
        

        public static class BRECORD
          extends Structure
        {
          public WinDef.PVOID pvRecord;
          public IRecordInfo pRecInfo;
          
          public BRECORD() {}
          
          public BRECORD(Pointer pointer)
          {
            super();
          }
          


          protected List getFieldOrder() { return Arrays.asList(new String[] { "pvRecord", "pRecInfo" }); }
          
          public static class ByReference extends Variant.VARIANT._VARIANT.__VARIANT.BRECORD implements Structure.ByReference {
            public ByReference() {}
          }
        }
        
        public __VARIANT() { read(); }
        
        public __VARIANT(Pointer pointer)
        {
          super();
          read();
        }
      }
      


      protected List getFieldOrder() { return Arrays.asList(new String[] { "vt", "wReserved1", "wReserved2", "wReserved3", "__variant" }); }
    }
    
    public static class ByReference extends Variant.VARIANT implements Structure.ByReference {
      public ByReference() {}
    }
  }
  
  public static class VariantArg extends Structure {
    public VariantArg() {}
    
    public static class ByReference extends Variant.VariantArg implements Structure.ByReference {
      public ByReference() {}
      
      public ByReference(Variant.VARIANT[] variantArg) { this.variantArg = variantArg; }
    }
    

    public Variant.VARIANT[] variantArg = new Variant.VARIANT[1];
    


    public VariantArg(Variant.VARIANT[] variantArg)
    {
      this.variantArg = variantArg;
    }
    
    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "variantArg" });
    }
  }
}
