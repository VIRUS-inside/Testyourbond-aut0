package com.sun.jna.platform.win32.COM;

import com.sun.jna.platform.win32.OaIdl.EXCEPINFO;
import com.sun.jna.ptr.IntByReference;


































public class COMException
  extends RuntimeException
{
  private OaIdl.EXCEPINFO pExcepInfo;
  private IntByReference puArgErr;
  private int uArgErr;
  
  public COMException() {}
  
  public COMException(String message, Throwable cause)
  {
    super(message, cause);
  }
  





  public COMException(String message)
  {
    super(message);
  }
  










  public COMException(String message, OaIdl.EXCEPINFO pExcepInfo, IntByReference puArgErr)
  {
    super(message + " (puArgErr=" + puArgErr.getValue() + ")");
    this.pExcepInfo = pExcepInfo;
    this.puArgErr = puArgErr;
  }
  





  public COMException(Throwable cause)
  {
    super(cause);
  }
  




  public OaIdl.EXCEPINFO getExcepInfo()
  {
    return pExcepInfo;
  }
  




  public IntByReference getArgErr()
  {
    return puArgErr;
  }
  
  public int getuArgErr() {
    return uArgErr;
  }
  
  public void setuArgErr(int uArgErr) {
    this.uArgErr = uArgErr;
  }
}
