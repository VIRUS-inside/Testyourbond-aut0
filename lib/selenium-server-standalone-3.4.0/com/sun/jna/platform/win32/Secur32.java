package com.sun.jna.platform.win32;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;






















public abstract interface Secur32
  extends StdCallLibrary
{
  public static final Secur32 INSTANCE = (Secur32)Native.loadLibrary("Secur32", Secur32.class, W32APIOptions.UNICODE_OPTIONS);
  
  public abstract boolean GetUserNameEx(int paramInt, char[] paramArrayOfChar, IntByReference paramIntByReference);
  
  public abstract int AcquireCredentialsHandle(String paramString1, String paramString2, int paramInt, WinNT.LUID paramLUID, Pointer paramPointer1, Pointer paramPointer2, Pointer paramPointer3, Sspi.CredHandle paramCredHandle, Sspi.TimeStamp paramTimeStamp);
  
  public abstract int InitializeSecurityContext(Sspi.CredHandle paramCredHandle, Sspi.CtxtHandle paramCtxtHandle1, String paramString, int paramInt1, int paramInt2, int paramInt3, Sspi.SecBufferDesc paramSecBufferDesc1, int paramInt4, Sspi.CtxtHandle paramCtxtHandle2, Sspi.SecBufferDesc paramSecBufferDesc2, IntByReference paramIntByReference, Sspi.TimeStamp paramTimeStamp);
  
  public abstract int DeleteSecurityContext(Sspi.CtxtHandle paramCtxtHandle);
  
  public abstract int FreeCredentialsHandle(Sspi.CredHandle paramCredHandle);
  
  public abstract int AcceptSecurityContext(Sspi.CredHandle paramCredHandle, Sspi.CtxtHandle paramCtxtHandle1, Sspi.SecBufferDesc paramSecBufferDesc1, int paramInt1, int paramInt2, Sspi.CtxtHandle paramCtxtHandle2, Sspi.SecBufferDesc paramSecBufferDesc2, IntByReference paramIntByReference, Sspi.TimeStamp paramTimeStamp);
  
  public abstract int EnumerateSecurityPackages(IntByReference paramIntByReference, Sspi.PSecPkgInfo paramPSecPkgInfo);
  
  public abstract int FreeContextBuffer(Pointer paramPointer);
  
  public abstract int QuerySecurityContextToken(Sspi.CtxtHandle paramCtxtHandle, WinNT.HANDLEByReference paramHANDLEByReference);
  
  public abstract int ImpersonateSecurityContext(Sspi.CtxtHandle paramCtxtHandle);
  
  public abstract int RevertSecurityContext(Sspi.CtxtHandle paramCtxtHandle);
  
  public static abstract class EXTENDED_NAME_FORMAT
  {
    public static final int NameUnknown = 0;
    public static final int NameFullyQualifiedDN = 1;
    public static final int NameSamCompatible = 2;
    public static final int NameDisplay = 3;
    public static final int NameUniqueId = 6;
    public static final int NameCanonical = 7;
    public static final int NameUserPrincipal = 8;
    public static final int NameCanonicalEx = 9;
    public static final int NameServicePrincipal = 10;
    public static final int NameDnsDomain = 12;
    
    public EXTENDED_NAME_FORMAT() {}
  }
}
