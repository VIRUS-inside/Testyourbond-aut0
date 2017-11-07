package com.sun.jna.platform.win32.COM;

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





















public abstract interface IDispatch
  extends IUnknown
{
  public static final Guid.IID IID_IDISPATCH = new Guid.IID("00020400-0000-0000-C000-000000000046");
  
  public abstract WinNT.HRESULT GetTypeInfoCount(WinDef.UINTByReference paramUINTByReference);
  
  public abstract WinNT.HRESULT GetTypeInfo(WinDef.UINT paramUINT, WinDef.LCID paramLCID, PointerByReference paramPointerByReference);
  
  public abstract WinNT.HRESULT GetIDsOfNames(Guid.IID paramIID, WString[] paramArrayOfWString, int paramInt, WinDef.LCID paramLCID, OaIdl.DISPIDByReference paramDISPIDByReference);
  
  public abstract WinNT.HRESULT Invoke(OaIdl.DISPID paramDISPID1, Guid.IID paramIID, WinDef.LCID paramLCID, OaIdl.DISPID paramDISPID2, OleAuto.DISPPARAMS paramDISPPARAMS, Variant.VARIANT.ByReference paramByReference, OaIdl.EXCEPINFO.ByReference paramByReference1, IntByReference paramIntByReference);
}
