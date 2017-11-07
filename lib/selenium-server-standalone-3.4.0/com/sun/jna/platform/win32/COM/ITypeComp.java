package com.sun.jna.platform.win32.COM;

import com.sun.jna.WString;
import com.sun.jna.platform.win32.OaIdl.BINDPTR.ByReference;
import com.sun.jna.platform.win32.OaIdl.DESCKIND.ByReference;
import com.sun.jna.platform.win32.WinDef.ULONG;
import com.sun.jna.platform.win32.WinDef.WORD;
import com.sun.jna.platform.win32.WinNT.HRESULT;
import com.sun.jna.ptr.PointerByReference;

public abstract interface ITypeComp
  extends IUnknown
{
  public abstract WinNT.HRESULT Bind(WString paramWString, WinDef.ULONG paramULONG, WinDef.WORD paramWORD, PointerByReference paramPointerByReference, OaIdl.DESCKIND.ByReference paramByReference, OaIdl.BINDPTR.ByReference paramByReference1);
  
  public abstract WinNT.HRESULT BindType(WString paramWString, WinDef.ULONG paramULONG, PointerByReference paramPointerByReference1, PointerByReference paramPointerByReference2);
}
