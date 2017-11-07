package com.sun.jna.platform.win32.COM;

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

public abstract interface ITypeLib
  extends IUnknown
{
  public abstract WinDef.UINT GetTypeInfoCount();
  
  public abstract WinNT.HRESULT GetTypeInfo(WinDef.UINT paramUINT, PointerByReference paramPointerByReference);
  
  public abstract WinNT.HRESULT GetTypeInfoType(WinDef.UINT paramUINT, OaIdl.TYPEKIND.ByReference paramByReference);
  
  public abstract WinNT.HRESULT GetTypeInfoOfGuid(Guid.GUID paramGUID, PointerByReference paramPointerByReference);
  
  public abstract WinNT.HRESULT GetLibAttr(PointerByReference paramPointerByReference);
  
  public abstract WinNT.HRESULT GetTypeComp(PointerByReference paramPointerByReference);
  
  public abstract WinNT.HRESULT GetDocumentation(int paramInt, WTypes.BSTRByReference paramBSTRByReference1, WTypes.BSTRByReference paramBSTRByReference2, WinDef.DWORDByReference paramDWORDByReference, WTypes.BSTRByReference paramBSTRByReference3);
  
  public abstract WinNT.HRESULT IsName(WTypes.LPOLESTR paramLPOLESTR, WinDef.ULONG paramULONG, WinDef.BOOLByReference paramBOOLByReference);
  
  public abstract WinNT.HRESULT FindName(WTypes.BSTRByReference paramBSTRByReference, WinDef.ULONG paramULONG, ITypeInfo[] paramArrayOfITypeInfo, OaIdl.MEMBERID[] paramArrayOfMEMBERID, WinDef.USHORTByReference paramUSHORTByReference);
  
  public abstract void ReleaseTLibAttr(OaIdl.TLIBATTR paramTLIBATTR);
}
