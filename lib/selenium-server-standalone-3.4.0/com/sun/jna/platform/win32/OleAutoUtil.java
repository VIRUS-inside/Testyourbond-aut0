package com.sun.jna.platform.win32;

import com.sun.jna.platform.win32.COM.COMUtils;




























public abstract class OleAutoUtil
{
  public OleAutoUtil() {}
  
  public static OaIdl.SAFEARRAY.ByReference createVarArray(int size)
  {
    OaIdl.SAFEARRAYBOUND[] rgsabound = new OaIdl.SAFEARRAYBOUND[1];
    rgsabound[0] = new OaIdl.SAFEARRAYBOUND(size, 0);
    
    OaIdl.SAFEARRAY.ByReference psa = OleAuto.INSTANCE.SafeArrayCreate(new WTypes.VARTYPE(12), 1, rgsabound);
    

    return psa;
  }
  







  public static void SafeArrayPutElement(OaIdl.SAFEARRAY array, long index, Variant.VARIANT arg)
  {
    long[] idx = new long[1];
    idx[0] = index;
    WinNT.HRESULT hr = OleAuto.INSTANCE.SafeArrayPutElement(array, idx, arg);
    COMUtils.SUCCEEDED(hr);
  }
  






  public static Variant.VARIANT SafeArrayGetElement(OaIdl.SAFEARRAY array, long index)
  {
    long[] idx = new long[1];
    idx[0] = index;
    Variant.VARIANT result = new Variant.VARIANT();
    WinNT.HRESULT hr = OleAuto.INSTANCE.SafeArrayGetElement(array, idx, result.getPointer());
    
    COMUtils.SUCCEEDED(hr);
    return result;
  }
}
