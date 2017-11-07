package com.sun.jna.platform.win32.COM;

import com.sun.jna.platform.win32.Guid.REFIID;
import com.sun.jna.platform.win32.OaIdl.EXCEPINFO.ByReference;
import com.sun.jna.platform.win32.OaIdl.FUNCDESC;
import com.sun.jna.platform.win32.OaIdl.HREFTYPE;
import com.sun.jna.platform.win32.OaIdl.HREFTYPEByReference;
import com.sun.jna.platform.win32.OaIdl.INVOKEKIND;
import com.sun.jna.platform.win32.OaIdl.MEMBERID;
import com.sun.jna.platform.win32.OaIdl.TYPEATTR;
import com.sun.jna.platform.win32.OaIdl.VARDESC;
import com.sun.jna.platform.win32.OleAuto.DISPPARAMS.ByReference;
import com.sun.jna.platform.win32.Variant.VARIANT.ByReference;
import com.sun.jna.platform.win32.WTypes.BSTR;
import com.sun.jna.platform.win32.WTypes.BSTRByReference;
import com.sun.jna.platform.win32.WTypes.LPOLESTR;
import com.sun.jna.platform.win32.WinDef.DWORDByReference;
import com.sun.jna.platform.win32.WinDef.PVOID;
import com.sun.jna.platform.win32.WinDef.UINT;
import com.sun.jna.platform.win32.WinDef.UINTByReference;
import com.sun.jna.platform.win32.WinDef.WORD;
import com.sun.jna.platform.win32.WinDef.WORDByReference;
import com.sun.jna.platform.win32.WinNT.HRESULT;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

public abstract interface ITypeInfo
  extends IUnknown
{
  public abstract WinNT.HRESULT GetTypeAttr(PointerByReference paramPointerByReference);
  
  public abstract WinNT.HRESULT GetTypeComp(PointerByReference paramPointerByReference);
  
  public abstract WinNT.HRESULT GetFuncDesc(WinDef.UINT paramUINT, PointerByReference paramPointerByReference);
  
  public abstract WinNT.HRESULT GetVarDesc(WinDef.UINT paramUINT, PointerByReference paramPointerByReference);
  
  public abstract WinNT.HRESULT GetNames(OaIdl.MEMBERID paramMEMBERID, WTypes.BSTR[] paramArrayOfBSTR, WinDef.UINT paramUINT, WinDef.UINTByReference paramUINTByReference);
  
  public abstract WinNT.HRESULT GetRefTypeOfImplType(WinDef.UINT paramUINT, OaIdl.HREFTYPEByReference paramHREFTYPEByReference);
  
  public abstract WinNT.HRESULT GetImplTypeFlags(WinDef.UINT paramUINT, IntByReference paramIntByReference);
  
  public abstract WinNT.HRESULT GetIDsOfNames(WTypes.LPOLESTR[] paramArrayOfLPOLESTR, WinDef.UINT paramUINT, OaIdl.MEMBERID[] paramArrayOfMEMBERID);
  
  public abstract WinNT.HRESULT Invoke(WinDef.PVOID paramPVOID, OaIdl.MEMBERID paramMEMBERID, WinDef.WORD paramWORD, OleAuto.DISPPARAMS.ByReference paramByReference, Variant.VARIANT.ByReference paramByReference1, OaIdl.EXCEPINFO.ByReference paramByReference2, WinDef.UINTByReference paramUINTByReference);
  
  public abstract WinNT.HRESULT GetDocumentation(OaIdl.MEMBERID paramMEMBERID, WTypes.BSTRByReference paramBSTRByReference1, WTypes.BSTRByReference paramBSTRByReference2, WinDef.DWORDByReference paramDWORDByReference, WTypes.BSTRByReference paramBSTRByReference3);
  
  public abstract WinNT.HRESULT GetDllEntry(OaIdl.MEMBERID paramMEMBERID, OaIdl.INVOKEKIND paramINVOKEKIND, WTypes.BSTRByReference paramBSTRByReference1, WTypes.BSTRByReference paramBSTRByReference2, WinDef.WORDByReference paramWORDByReference);
  
  public abstract WinNT.HRESULT GetRefTypeInfo(OaIdl.HREFTYPE paramHREFTYPE, PointerByReference paramPointerByReference);
  
  public abstract WinNT.HRESULT AddressOfMember(OaIdl.MEMBERID paramMEMBERID, OaIdl.INVOKEKIND paramINVOKEKIND, PointerByReference paramPointerByReference);
  
  public abstract WinNT.HRESULT CreateInstance(IUnknown paramIUnknown, Guid.REFIID paramREFIID, PointerByReference paramPointerByReference);
  
  public abstract WinNT.HRESULT GetMops(OaIdl.MEMBERID paramMEMBERID, WTypes.BSTRByReference paramBSTRByReference);
  
  public abstract WinNT.HRESULT GetContainingTypeLib(PointerByReference paramPointerByReference, WinDef.UINTByReference paramUINTByReference);
  
  public abstract void ReleaseTypeAttr(OaIdl.TYPEATTR paramTYPEATTR);
  
  public abstract void ReleaseFuncDesc(OaIdl.FUNCDESC paramFUNCDESC);
  
  public abstract void ReleaseVarDesc(OaIdl.VARDESC paramVARDESC);
}
