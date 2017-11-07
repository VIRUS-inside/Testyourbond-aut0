package com.sun.jna.platform.win32.COM;

import com.sun.jna.Pointer;
import com.sun.jna.Structure.ByReference;
import com.sun.jna.platform.win32.Guid.GUID;
import com.sun.jna.platform.win32.OaIdl.MEMBERID;
import com.sun.jna.platform.win32.OaIdl.TLIBATTR;
import com.sun.jna.platform.win32.OaIdl.TYPEKIND.ByReference;
import com.sun.jna.platform.win32.WTypes.BSTRByReference;
import com.sun.jna.platform.win32.WTypes.LPOLESTR;
import com.sun.jna.platform.win32.WinDef.BOOLByReference;
import com.sun.jna.platform.win32.WinDef.DWORDByReference;
import com.sun.jna.platform.win32.WinDef.UINT;
import com.sun.jna.platform.win32.WinDef.ULONG;
import com.sun.jna.platform.win32.WinDef.USHORTByReference;
import com.sun.jna.platform.win32.WinNT.HRESULT;
import com.sun.jna.ptr.PointerByReference;






























public class TypeLib
  extends Unknown
  implements ITypeLib
{
  public TypeLib() {}
  
  public TypeLib(Pointer pvInstance)
  {
    super(pvInstance);
  }
  




  public WinDef.UINT GetTypeInfoCount()
  {
    return (WinDef.UINT)_invokeNativeObject(3, new Object[] { getPointer() }, WinDef.UINT.class);
  }
  












  public WinNT.HRESULT GetTypeInfo(WinDef.UINT index, PointerByReference pTInfo)
  {
    return (WinNT.HRESULT)_invokeNativeObject(4, new Object[] { getPointer(), index, pTInfo }, WinNT.HRESULT.class);
  }
  













  public WinNT.HRESULT GetTypeInfoType(WinDef.UINT index, OaIdl.TYPEKIND.ByReference pTKind)
  {
    return (WinNT.HRESULT)_invokeNativeObject(5, new Object[] { getPointer(), index, pTKind }, WinNT.HRESULT.class);
  }
  













  public WinNT.HRESULT GetTypeInfoOfGuid(Guid.GUID guid, PointerByReference pTinfo)
  {
    return (WinNT.HRESULT)_invokeNativeObject(6, new Object[] { getPointer(), guid, pTinfo }, WinNT.HRESULT.class);
  }
  










  public WinNT.HRESULT GetLibAttr(PointerByReference ppTLibAttr)
  {
    return (WinNT.HRESULT)_invokeNativeObject(7, new Object[] { getPointer(), ppTLibAttr }, WinNT.HRESULT.class);
  }
  









  public WinNT.HRESULT GetTypeComp(PointerByReference pTComp)
  {
    return (WinNT.HRESULT)_invokeNativeObject(8, new Object[] { getPointer(), pTComp }, WinNT.HRESULT.class);
  }
  





















  public WinNT.HRESULT GetDocumentation(int index, WTypes.BSTRByReference pBstrName, WTypes.BSTRByReference pBstrDocString, WinDef.DWORDByReference pdwHelpContext, WTypes.BSTRByReference pBstrHelpFile)
  {
    return (WinNT.HRESULT)_invokeNativeObject(9, new Object[] { getPointer(), Integer.valueOf(index), pBstrName, pBstrDocString, pdwHelpContext, pBstrHelpFile }, WinNT.HRESULT.class);
  }
  


















  public WinNT.HRESULT IsName(WTypes.LPOLESTR szNameBuf, WinDef.ULONG lHashVal, WinDef.BOOLByReference pfName)
  {
    return (WinNT.HRESULT)_invokeNativeObject(10, new Object[] { getPointer(), szNameBuf, lHashVal, pfName }, WinNT.HRESULT.class);
  }
  























  public WinNT.HRESULT FindName(WTypes.BSTRByReference szNameBuf, WinDef.ULONG lHashVal, ITypeInfo[] ppTInfo, OaIdl.MEMBERID[] rgMemId, WinDef.USHORTByReference pcFound)
  {
    return (WinNT.HRESULT)_invokeNativeObject(11, new Object[] { getPointer(), szNameBuf, lHashVal, ppTInfo, rgMemId, pcFound }, WinNT.HRESULT.class);
  }
  







  public void ReleaseTLibAttr(OaIdl.TLIBATTR pTLibAttr)
  {
    _invokeNativeObject(12, new Object[] { getPointer() }, WinNT.HRESULT.class);
  }
  
  public static class ByReference
    extends TypeLib
    implements Structure.ByReference
  {
    public ByReference() {}
  }
}
