package com.sun.jna.platform.win32.COM;

import com.sun.jna.WString;
import com.sun.jna.platform.win32.Guid.GUID;
import com.sun.jna.platform.win32.Guid.IID;
import com.sun.jna.platform.win32.Variant.VARIANT;
import com.sun.jna.platform.win32.WTypes.BSTR;
import com.sun.jna.platform.win32.WinDef.BOOL;
import com.sun.jna.platform.win32.WinDef.PVOID;
import com.sun.jna.platform.win32.WinDef.ULONG;
import com.sun.jna.platform.win32.WinNT.HRESULT;


















public abstract interface IRecordInfo
  extends IUnknown
{
  public static final Guid.IID IID_IRecordInfo = new Guid.IID("{0000002F-0000-0000-C000-000000000046}");
  
  public abstract WinNT.HRESULT RecordInit(WinDef.PVOID paramPVOID);
  
  public abstract WinNT.HRESULT RecordClear(WinDef.PVOID paramPVOID);
  
  public abstract WinNT.HRESULT RecordCopy(WinDef.PVOID paramPVOID1, WinDef.PVOID paramPVOID2);
  
  public abstract WinNT.HRESULT GetGuid(Guid.GUID paramGUID);
  
  public abstract WinNT.HRESULT GetName(WTypes.BSTR paramBSTR);
  
  public abstract WinNT.HRESULT GetSize(WinDef.ULONG paramULONG);
  
  public abstract WinNT.HRESULT GetTypeInfo(ITypeInfo paramITypeInfo);
  
  public abstract WinNT.HRESULT GetField(WinDef.PVOID paramPVOID, WString paramWString, Variant.VARIANT paramVARIANT);
  
  public abstract WinNT.HRESULT GetFieldNoCopy(WinDef.PVOID paramPVOID1, WString paramWString, Variant.VARIANT paramVARIANT, WinDef.PVOID paramPVOID2);
  
  public abstract WinNT.HRESULT PutField(WinDef.ULONG paramULONG, WinDef.PVOID paramPVOID, WString paramWString, Variant.VARIANT paramVARIANT);
  
  public abstract WinNT.HRESULT PutFieldNoCopy(WinDef.ULONG paramULONG, WinDef.PVOID paramPVOID, WString paramWString, Variant.VARIANT paramVARIANT);
  
  public abstract WinNT.HRESULT GetFieldNames(WinDef.ULONG paramULONG, WTypes.BSTR paramBSTR);
  
  public abstract WinDef.BOOL IsMatchingType(IRecordInfo paramIRecordInfo);
  
  public abstract WinDef.PVOID RecordCreate();
  
  public abstract WinNT.HRESULT RecordCreateCopy(WinDef.PVOID paramPVOID1, WinDef.PVOID paramPVOID2);
  
  public abstract WinNT.HRESULT RecordDestroy(WinDef.PVOID paramPVOID);
}
