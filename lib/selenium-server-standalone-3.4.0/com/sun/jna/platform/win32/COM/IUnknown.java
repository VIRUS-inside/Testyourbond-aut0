package com.sun.jna.platform.win32.COM;

import com.sun.jna.platform.win32.Guid.IID;
import com.sun.jna.platform.win32.WinNT.HRESULT;
import com.sun.jna.ptr.PointerByReference;























public abstract interface IUnknown
{
  public static final Guid.IID IID_IUNKNOWN = new Guid.IID("{00000000-0000-0000-C000-000000000046}");
  
  public abstract WinNT.HRESULT QueryInterface(Guid.IID paramIID, PointerByReference paramPointerByReference);
  
  public abstract int AddRef();
  
  public abstract int Release();
}
