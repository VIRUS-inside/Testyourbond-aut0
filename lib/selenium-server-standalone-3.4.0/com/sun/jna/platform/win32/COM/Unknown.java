package com.sun.jna.platform.win32.COM;

import com.sun.jna.Pointer;
import com.sun.jna.Structure.ByReference;
import com.sun.jna.platform.win32.Guid.IID;
import com.sun.jna.platform.win32.WinNT.HRESULT;
import com.sun.jna.ptr.PointerByReference;






























public class Unknown
  extends COMInvoker
  implements IUnknown
{
  public Unknown() {}
  
  public Unknown(Pointer pvInstance)
  {
    setPointer(pvInstance);
  }
  








  public WinNT.HRESULT QueryInterface(Guid.IID riid, PointerByReference ppvObject)
  {
    return (WinNT.HRESULT)_invokeNativeObject(0, new Object[] { getPointer(), riid, ppvObject }, WinNT.HRESULT.class);
  }
  

  public int AddRef()
  {
    return _invokeNativeInt(1, new Object[] { getPointer() });
  }
  
  public int Release() {
    return _invokeNativeInt(2, new Object[] { getPointer() });
  }
  
  public static class ByReference
    extends Unknown
    implements Structure.ByReference
  {
    public ByReference() {}
  }
}
