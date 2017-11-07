package com.sun.jna.platform.win32;









public class Win32Exception
  extends RuntimeException
{
  private static final long serialVersionUID = 1L;
  






  private WinNT.HRESULT _hr;
  







  public WinNT.HRESULT getHR()
  {
    return _hr;
  }
  




  public Win32Exception(WinNT.HRESULT hr)
  {
    super(Kernel32Util.formatMessage(hr));
    _hr = hr;
  }
  




  public Win32Exception(int code)
  {
    this(W32Errors.HRESULT_FROM_WIN32(code));
  }
}
