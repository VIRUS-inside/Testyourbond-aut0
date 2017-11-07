package com.sun.jna.platform.win32;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.ByReference;
import com.sun.jna.WString;
import com.sun.jna.ptr.DoubleByReference;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;
import java.util.Arrays;
import java.util.List;





































































public abstract interface OleAuto
  extends StdCallLibrary
{
  public static final int DISPATCH_METHOD = 1;
  public static final int DISPATCH_PROPERTYGET = 2;
  public static final int DISPATCH_PROPERTYPUT = 4;
  public static final int DISPATCH_PROPERTYPUTREF = 8;
  public static final int FADF_AUTO = 1;
  public static final int FADF_STATIC = 2;
  public static final int FADF_EMBEDDED = 4;
  public static final int FADF_FIXEDSIZE = 16;
  public static final int FADF_RECORD = 32;
  public static final int FADF_HAVEIID = 64;
  public static final int FADF_HAVEVARTYPE = 128;
  public static final int FADF_BSTR = 256;
  public static final int FADF_UNKNOWN = 512;
  public static final int FADF_DISPATCH = 1024;
  public static final int FADF_VARIANT = 2048;
  public static final int FADF_RESERVED = 61448;
  public static final OleAuto INSTANCE = (OleAuto)Native.loadLibrary("OleAut32", OleAuto.class, W32APIOptions.UNICODE_OPTIONS);
  












  public abstract WTypes.BSTR SysAllocString(String paramString);
  












  public abstract void SysFreeString(WTypes.BSTR paramBSTR);
  












  public abstract void VariantInit(Variant.VARIANT.ByReference paramByReference);
  












  public abstract void VariantInit(Variant.VARIANT paramVARIANT);
  












  public abstract WinNT.HRESULT VariantCopy(Pointer paramPointer, Variant.VARIANT paramVARIANT);
  












  public abstract WinNT.HRESULT VariantClear(Pointer paramPointer);
  












  public abstract OaIdl.SAFEARRAY.ByReference SafeArrayCreate(WTypes.VARTYPE paramVARTYPE, int paramInt, OaIdl.SAFEARRAYBOUND[] paramArrayOfSAFEARRAYBOUND);
  












  public abstract WinNT.HRESULT SafeArrayPutElement(OaIdl.SAFEARRAY paramSAFEARRAY, long[] paramArrayOfLong, Variant.VARIANT paramVARIANT);
  












  public abstract WinNT.HRESULT SafeArrayGetElement(OaIdl.SAFEARRAY paramSAFEARRAY, long[] paramArrayOfLong, Pointer paramPointer);
  












  public abstract WinNT.HRESULT SafeArrayLock(OaIdl.SAFEARRAY paramSAFEARRAY);
  












  public abstract WinNT.HRESULT SafeArrayUnLock(OaIdl.SAFEARRAY paramSAFEARRAY);
  












  public abstract WinNT.HRESULT GetActiveObject(Guid.GUID paramGUID, WinDef.PVOID paramPVOID, PointerByReference paramPointerByReference);
  











  public abstract WinNT.HRESULT LoadRegTypeLib(Guid.GUID paramGUID, int paramInt1, int paramInt2, WinDef.LCID paramLCID, PointerByReference paramPointerByReference);
  











  public abstract WinNT.HRESULT LoadTypeLib(WString paramWString, PointerByReference paramPointerByReference);
  











  public abstract int SystemTimeToVariantTime(WinBase.SYSTEMTIME paramSYSTEMTIME, DoubleByReference paramDoubleByReference);
  











  public static class DISPPARAMS
    extends Structure
  {
    public Variant.VariantArg.ByReference rgvarg;
    










    public OaIdl.DISPIDByReference rgdispidNamedArgs;
    










    public WinDef.UINT cArgs;
    










    public WinDef.UINT cNamedArgs;
    











    public DISPPARAMS() {}
    











    public DISPPARAMS(Pointer memory)
    {
      super();
      read();
    }
    





    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "rgvarg", "rgdispidNamedArgs", "cArgs", "cNamedArgs" });
    }
    
    public static class ByReference
      extends OleAuto.DISPPARAMS
      implements Structure.ByReference
    {
      public ByReference() {}
    }
  }
}
