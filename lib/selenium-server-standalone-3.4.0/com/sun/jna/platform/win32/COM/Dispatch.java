package com.sun.jna.platform.win32.COM;

import com.sun.jna.Pointer;
import com.sun.jna.Structure.ByReference;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.Guid.IID;
import com.sun.jna.platform.win32.OaIdl.DISPID;
import com.sun.jna.platform.win32.OaIdl.DISPIDByReference;
import com.sun.jna.platform.win32.OaIdl.EXCEPINFO.ByReference;
import com.sun.jna.platform.win32.OleAuto.DISPPARAMS;
import com.sun.jna.platform.win32.Variant.VARIANT.ByReference;
import com.sun.jna.platform.win32.WinDef.LCID;
import com.sun.jna.platform.win32.WinDef.UINT;
import com.sun.jna.platform.win32.WinDef.UINTByReference;
import com.sun.jna.platform.win32.WinNT.HRESULT;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
























public class Dispatch
  extends Unknown
  implements IDispatch
{
  public Dispatch() {}
  
  public Dispatch(Pointer pvInstance)
  {
    super(pvInstance);
  }
  








  public WinNT.HRESULT GetTypeInfoCount(WinDef.UINTByReference pctinfo)
  {
    return (WinNT.HRESULT)_invokeNativeObject(3, new Object[] { getPointer(), pctinfo }, WinNT.HRESULT.class);
  }
  












  public WinNT.HRESULT GetTypeInfo(WinDef.UINT iTInfo, WinDef.LCID lcid, PointerByReference ppTInfo)
  {
    return (WinNT.HRESULT)_invokeNativeObject(4, new Object[] { getPointer(), iTInfo, lcid, ppTInfo }, WinNT.HRESULT.class);
  }
  

















  public WinNT.HRESULT GetIDsOfNames(Guid.IID riid, WString[] rgszNames, int cNames, WinDef.LCID lcid, OaIdl.DISPIDByReference rgDispId)
  {
    return (WinNT.HRESULT)_invokeNativeObject(5, new Object[] { getPointer(), riid, rgszNames, Integer.valueOf(cNames), lcid, rgDispId }, WinNT.HRESULT.class);
  }
  

























  public WinNT.HRESULT Invoke(OaIdl.DISPID dispIdMember, Guid.IID riid, WinDef.LCID lcid, OaIdl.DISPID wFlags, OleAuto.DISPPARAMS pDispParams, Variant.VARIANT.ByReference pVarResult, OaIdl.EXCEPINFO.ByReference pExcepInfo, IntByReference puArgErr)
  {
    return (WinNT.HRESULT)_invokeNativeObject(6, new Object[] { getPointer(), dispIdMember, riid, lcid, wFlags, pDispParams, pVarResult, pExcepInfo, puArgErr }, WinNT.HRESULT.class);
  }
  
  public static class ByReference
    extends Dispatch
    implements Structure.ByReference
  {
    public ByReference() {}
  }
}
