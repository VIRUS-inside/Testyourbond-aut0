package com.sun.jna.platform.win32;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;



















public abstract interface Netapi32
  extends StdCallLibrary
{
  public static final Netapi32 INSTANCE = (Netapi32)Native.loadLibrary("Netapi32", Netapi32.class, W32APIOptions.UNICODE_OPTIONS);
  
  public abstract int NetGetJoinInformation(String paramString, PointerByReference paramPointerByReference, IntByReference paramIntByReference);
  
  public abstract int NetApiBufferFree(Pointer paramPointer);
  
  public abstract int NetLocalGroupEnum(String paramString, int paramInt1, PointerByReference paramPointerByReference, int paramInt2, IntByReference paramIntByReference1, IntByReference paramIntByReference2, IntByReference paramIntByReference3);
  
  public abstract int NetGetDCName(String paramString1, String paramString2, PointerByReference paramPointerByReference);
  
  public abstract int NetGroupEnum(String paramString, int paramInt1, PointerByReference paramPointerByReference, int paramInt2, IntByReference paramIntByReference1, IntByReference paramIntByReference2, IntByReference paramIntByReference3);
  
  public abstract int NetUserEnum(String paramString, int paramInt1, int paramInt2, PointerByReference paramPointerByReference, int paramInt3, IntByReference paramIntByReference1, IntByReference paramIntByReference2, IntByReference paramIntByReference3);
  
  public abstract int NetUserGetGroups(String paramString1, String paramString2, int paramInt1, PointerByReference paramPointerByReference, int paramInt2, IntByReference paramIntByReference1, IntByReference paramIntByReference2);
  
  public abstract int NetUserGetLocalGroups(String paramString1, String paramString2, int paramInt1, int paramInt2, PointerByReference paramPointerByReference, int paramInt3, IntByReference paramIntByReference1, IntByReference paramIntByReference2);
  
  public abstract int NetUserAdd(String paramString, int paramInt, Structure paramStructure, IntByReference paramIntByReference);
  
  public abstract int NetUserDel(String paramString1, String paramString2);
  
  public abstract int NetUserChangePassword(String paramString1, String paramString2, String paramString3, String paramString4);
  
  public abstract int DsGetDcName(String paramString1, String paramString2, Guid.GUID paramGUID, String paramString3, int paramInt, DsGetDC.PDOMAIN_CONTROLLER_INFO paramPDOMAIN_CONTROLLER_INFO);
  
  public abstract int DsGetForestTrustInformation(String paramString1, String paramString2, int paramInt, NTSecApi.PLSA_FOREST_TRUST_INFORMATION paramPLSA_FOREST_TRUST_INFORMATION);
  
  public abstract int DsEnumerateDomainTrusts(String paramString, int paramInt, PointerByReference paramPointerByReference, IntByReference paramIntByReference);
  
  public abstract int NetUserGetInfo(String paramString1, String paramString2, int paramInt, PointerByReference paramPointerByReference);
}
