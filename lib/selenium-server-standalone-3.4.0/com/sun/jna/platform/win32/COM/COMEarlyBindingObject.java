package com.sun.jna.platform.win32.COM;

import com.sun.jna.WString;
import com.sun.jna.platform.win32.Guid.CLSID;
import com.sun.jna.platform.win32.Guid.IID;
import com.sun.jna.platform.win32.OaIdl.DISPID;
import com.sun.jna.platform.win32.OaIdl.DISPIDByReference;
import com.sun.jna.platform.win32.OaIdl.EXCEPINFO.ByReference;
import com.sun.jna.platform.win32.OleAuto.DISPPARAMS;
import com.sun.jna.platform.win32.Variant.VARIANT;
import com.sun.jna.platform.win32.Variant.VARIANT.ByReference;
import com.sun.jna.platform.win32.WinDef.LCID;
import com.sun.jna.platform.win32.WinDef.UINT;
import com.sun.jna.platform.win32.WinDef.UINTByReference;
import com.sun.jna.platform.win32.WinNT.HRESULT;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;



















public class COMEarlyBindingObject
  extends COMBindingBaseObject
  implements IDispatch
{
  public COMEarlyBindingObject(Guid.CLSID clsid, boolean useActiveInstance, int dwClsContext)
  {
    super(clsid, useActiveInstance, dwClsContext);
  }
  
  protected String getStringProperty(OaIdl.DISPID dispId) {
    Variant.VARIANT.ByReference result = new Variant.VARIANT.ByReference();
    oleMethod(2, result, getIDispatch(), dispId);
    

    return result.getValue().toString();
  }
  
  protected void setProperty(OaIdl.DISPID dispId, boolean value) {
    oleMethod(4, null, getIDispatch(), dispId, new Variant.VARIANT(value));
  }
  

  public WinNT.HRESULT QueryInterface(Guid.IID riid, PointerByReference ppvObject)
  {
    return getIDispatch().QueryInterface(riid, ppvObject);
  }
  
  public int AddRef()
  {
    return getIDispatch().AddRef();
  }
  
  public int Release()
  {
    return getIDispatch().Release();
  }
  
  public WinNT.HRESULT GetTypeInfoCount(WinDef.UINTByReference pctinfo)
  {
    return getIDispatch().GetTypeInfoCount(pctinfo);
  }
  

  public WinNT.HRESULT GetTypeInfo(WinDef.UINT iTInfo, WinDef.LCID lcid, PointerByReference ppTInfo)
  {
    return getIDispatch().GetTypeInfo(iTInfo, lcid, ppTInfo);
  }
  

  public WinNT.HRESULT GetIDsOfNames(Guid.IID riid, WString[] rgszNames, int cNames, WinDef.LCID lcid, OaIdl.DISPIDByReference rgDispId)
  {
    return getIDispatch().GetIDsOfNames(riid, rgszNames, cNames, lcid, rgDispId);
  }
  




  public WinNT.HRESULT Invoke(OaIdl.DISPID dispIdMember, Guid.IID riid, WinDef.LCID lcid, OaIdl.DISPID wFlags, OleAuto.DISPPARAMS pDispParams, Variant.VARIANT.ByReference pVarResult, OaIdl.EXCEPINFO.ByReference pExcepInfo, IntByReference puArgErr)
  {
    return getIDispatch().Invoke(dispIdMember, riid, lcid, wFlags, pDispParams, pVarResult, pExcepInfo, puArgErr);
  }
}
