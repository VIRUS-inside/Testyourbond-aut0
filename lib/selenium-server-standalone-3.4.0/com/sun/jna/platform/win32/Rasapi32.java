package com.sun.jna.platform.win32;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure.ByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;
























public abstract interface Rasapi32
  extends StdCallLibrary
{
  public static final Rasapi32 INSTANCE = (Rasapi32)Native.loadLibrary("Rasapi32", Rasapi32.class, W32APIOptions.UNICODE_OPTIONS);
  
  public abstract int RasDial(WinRas.RASDIALEXTENSIONS.ByReference paramByReference, String paramString, WinRas.RASDIALPARAMS.ByReference paramByReference1, int paramInt, WinRas.RasDialFunc2 paramRasDialFunc2, WinNT.HANDLEByReference paramHANDLEByReference);
  
  public abstract int RasEnumConnections(WinRas.RASCONN[] paramArrayOfRASCONN, IntByReference paramIntByReference1, IntByReference paramIntByReference2);
  
  public abstract int RasGetConnectionStatistics(WinNT.HANDLE paramHANDLE, Structure.ByReference paramByReference);
  
  public abstract int RasGetConnectStatus(WinNT.HANDLE paramHANDLE, Structure.ByReference paramByReference);
  
  public abstract int RasGetCredentials(String paramString1, String paramString2, WinRas.RASCREDENTIALS.ByReference paramByReference);
  
  public abstract int RasGetEntryProperties(String paramString1, String paramString2, WinRas.RASENTRY.ByReference paramByReference, IntByReference paramIntByReference, Pointer paramPointer1, Pointer paramPointer2);
  
  public abstract int RasGetProjectionInfo(WinNT.HANDLE paramHANDLE, int paramInt, Pointer paramPointer, IntByReference paramIntByReference);
  
  public abstract int RasHangUp(WinNT.HANDLE paramHANDLE);
  
  public abstract int RasSetEntryProperties(String paramString1, String paramString2, WinRas.RASENTRY.ByReference paramByReference, int paramInt1, byte[] paramArrayOfByte, int paramInt2);
  
  public abstract int RasGetEntryDialParams(String paramString, WinRas.RASDIALPARAMS.ByReference paramByReference, WinDef.BOOLByReference paramBOOLByReference);
  
  public abstract int RasGetErrorString(int paramInt1, char[] paramArrayOfChar, int paramInt2);
}
