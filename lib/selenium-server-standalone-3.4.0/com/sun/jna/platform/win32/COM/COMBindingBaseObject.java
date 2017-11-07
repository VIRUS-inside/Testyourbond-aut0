package com.sun.jna.platform.win32.COM;

import com.sun.jna.WString;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.Guid.CLSID;
import com.sun.jna.platform.win32.Guid.CLSID.ByReference;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.OaIdl;
import com.sun.jna.platform.win32.OaIdl.DISPID;
import com.sun.jna.platform.win32.OaIdl.DISPIDByReference;
import com.sun.jna.platform.win32.OaIdl.EXCEPINFO.ByReference;
import com.sun.jna.platform.win32.Ole32;
import com.sun.jna.platform.win32.OleAuto;
import com.sun.jna.platform.win32.OleAuto.DISPPARAMS;
import com.sun.jna.platform.win32.Variant.VARIANT;
import com.sun.jna.platform.win32.Variant.VARIANT.ByReference;
import com.sun.jna.platform.win32.Variant.VariantArg.ByReference;
import com.sun.jna.platform.win32.WinDef.LCID;
import com.sun.jna.platform.win32.WinDef.UINT;
import com.sun.jna.platform.win32.WinNT.HRESULT;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;


















public class COMBindingBaseObject
  extends COMInvoker
{
  public static final WinDef.LCID LOCALE_USER_DEFAULT = Kernel32.INSTANCE.GetUserDefaultLCID();
  


  public static final WinDef.LCID LOCALE_SYSTEM_DEFAULT = Kernel32.INSTANCE.GetSystemDefaultLCID();
  


  private IUnknown iUnknown;
  

  private IDispatch iDispatch;
  

  private PointerByReference pDispatch = new PointerByReference();
  

  private PointerByReference pUnknown = new PointerByReference();
  
  public COMBindingBaseObject(IDispatch dispatch)
  {
    iDispatch = dispatch;
  }
  
  public COMBindingBaseObject(Guid.CLSID clsid, boolean useActiveInstance) {
    this(clsid, useActiveInstance, 21);
  }
  

  public COMBindingBaseObject(Guid.CLSID clsid, boolean useActiveInstance, int dwClsContext)
  {
    WinNT.HRESULT hr = Ole32.INSTANCE.CoInitialize(null);
    
    if (COMUtils.FAILED(hr)) {
      Ole32.INSTANCE.CoUninitialize();
      throw new COMException("CoInitialize() failed!");
    }
    
    if (useActiveInstance) {
      hr = OleAuto.INSTANCE.GetActiveObject(clsid, null, pUnknown);
      
      if (COMUtils.SUCCEEDED(hr)) {
        iUnknown = new Unknown(pUnknown.getValue());
        hr = iUnknown.QueryInterface(IDispatch.IID_IDISPATCH, pDispatch);
      }
      else {
        hr = Ole32.INSTANCE.CoCreateInstance(clsid, null, dwClsContext, IDispatch.IID_IDISPATCH, pDispatch);
      }
    }
    else {
      hr = Ole32.INSTANCE.CoCreateInstance(clsid, null, dwClsContext, IDispatch.IID_IDISPATCH, pDispatch);
    }
    

    if (COMUtils.FAILED(hr)) {
      throw new COMException("COM object with CLSID " + clsid.toGuidString() + " not registered properly!");
    }
    

    iDispatch = new Dispatch(pDispatch.getValue());
  }
  
  public COMBindingBaseObject(String progId, boolean useActiveInstance, int dwClsContext)
    throws COMException
  {
    WinNT.HRESULT hr = Ole32.INSTANCE.CoInitialize(null);
    
    if (COMUtils.FAILED(hr)) {
      release();
      throw new COMException("CoInitialize() failed!");
    }
    

    Guid.CLSID.ByReference clsid = new Guid.CLSID.ByReference();
    hr = Ole32.INSTANCE.CLSIDFromProgID(progId, clsid);
    
    if (COMUtils.FAILED(hr)) {
      Ole32.INSTANCE.CoUninitialize();
      throw new COMException("CLSIDFromProgID() failed!");
    }
    
    if (useActiveInstance) {
      hr = OleAuto.INSTANCE.GetActiveObject(clsid, null, pUnknown);
      
      if (COMUtils.SUCCEEDED(hr)) {
        iUnknown = new Unknown(pUnknown.getValue());
        hr = iUnknown.QueryInterface(IDispatch.IID_IDISPATCH, pDispatch);
      }
      else {
        hr = Ole32.INSTANCE.CoCreateInstance(clsid, null, dwClsContext, IDispatch.IID_IDISPATCH, pDispatch);
      }
    }
    else {
      hr = Ole32.INSTANCE.CoCreateInstance(clsid, null, dwClsContext, IDispatch.IID_IDISPATCH, pDispatch);
    }
    

    if (COMUtils.FAILED(hr)) {
      throw new COMException("COM object with ProgID '" + progId + "' and CLSID " + clsid.toGuidString() + " not registered properly!");
    }
    


    iDispatch = new Dispatch(pDispatch.getValue());
  }
  
  public COMBindingBaseObject(String progId, boolean useActiveInstance) throws COMException
  {
    this(progId, useActiveInstance, 21);
  }
  




  public IDispatch getIDispatch()
  {
    return iDispatch;
  }
  




  public PointerByReference getIDispatchPointer()
  {
    return pDispatch;
  }
  




  public IUnknown getIUnknown()
  {
    return iUnknown;
  }
  




  public PointerByReference getIUnknownPointer()
  {
    return pUnknown;
  }
  


  public void release()
  {
    if (iDispatch != null) {
      iDispatch.Release();
    }
    Ole32.INSTANCE.CoUninitialize();
  }
  
  protected WinNT.HRESULT oleMethod(int nType, Variant.VARIANT.ByReference pvResult, IDispatch pDisp, String name, Variant.VARIANT[] pArgs)
    throws COMException
  {
    if (pDisp == null) {
      throw new COMException("pDisp (IDispatch) parameter is null!");
    }
    
    WString[] ptName = { new WString(name) };
    OaIdl.DISPIDByReference pdispID = new OaIdl.DISPIDByReference();
    

    WinNT.HRESULT hr = pDisp.GetIDsOfNames(Guid.IID_NULL, ptName, 1, LOCALE_USER_DEFAULT, pdispID);
    

    COMUtils.checkRC(hr);
    
    return oleMethod(nType, pvResult, pDisp, pdispID.getValue(), pArgs);
  }
  


  protected WinNT.HRESULT oleMethod(int nType, Variant.VARIANT.ByReference pvResult, IDispatch pDisp, OaIdl.DISPID dispId, Variant.VARIANT[] pArgs)
    throws COMException
  {
    if (pDisp == null) {
      throw new COMException("pDisp (IDispatch) parameter is null!");
    }
    
    int _argsLen = 0;
    Variant.VARIANT[] _args = null;
    OleAuto.DISPPARAMS dp = new OleAuto.DISPPARAMS();
    OaIdl.EXCEPINFO.ByReference pExcepInfo = new OaIdl.EXCEPINFO.ByReference();
    IntByReference puArgErr = new IntByReference();
    

    if ((pArgs != null) && (pArgs.length > 0)) {
      _argsLen = pArgs.length;
      _args = new Variant.VARIANT[_argsLen];
      
      int revCount = _argsLen;
      for (int i = 0; i < _argsLen; i++) {
        _args[i] = pArgs[(--revCount)];
      }
    }
    

    if (nType == 4) {
      cNamedArgs = new WinDef.UINT(_argsLen);
      rgdispidNamedArgs = new OaIdl.DISPIDByReference(OaIdl.DISPID_PROPERTYPUT);
    }
    


    if (_argsLen > 0) {
      cArgs = new WinDef.UINT(_args.length);
      
      rgvarg = new Variant.VariantArg.ByReference(_args);
      

      dp.write();
    }
    

    WinNT.HRESULT hr = pDisp.Invoke(dispId, Guid.IID_NULL, LOCALE_SYSTEM_DEFAULT, new OaIdl.DISPID(nType), dp, pvResult, pExcepInfo, puArgErr);
    

    COMUtils.checkRC(hr, pExcepInfo, puArgErr);
    return hr;
  }
  

















  protected WinNT.HRESULT oleMethod(int nType, Variant.VARIANT.ByReference pvResult, IDispatch pDisp, String name, Variant.VARIANT pArg)
    throws COMException
  {
    return oleMethod(nType, pvResult, pDisp, name, new Variant.VARIANT[] { pArg });
  }
  

  protected WinNT.HRESULT oleMethod(int nType, Variant.VARIANT.ByReference pvResult, IDispatch pDisp, OaIdl.DISPID dispId, Variant.VARIANT pArg)
    throws COMException
  {
    return oleMethod(nType, pvResult, pDisp, dispId, new Variant.VARIANT[] { pArg });
  }
  
















  protected WinNT.HRESULT oleMethod(int nType, Variant.VARIANT.ByReference pvResult, IDispatch pDisp, String name)
    throws COMException
  {
    return oleMethod(nType, pvResult, pDisp, name, (Variant.VARIANT[])null);
  }
  
  protected WinNT.HRESULT oleMethod(int nType, Variant.VARIANT.ByReference pvResult, IDispatch pDisp, OaIdl.DISPID dispId)
    throws COMException
  {
    return oleMethod(nType, pvResult, pDisp, dispId, (Variant.VARIANT[])null);
  }
  





  protected void checkFailed(WinNT.HRESULT hr)
  {
    COMUtils.checkRC(hr, null, null);
  }
}
