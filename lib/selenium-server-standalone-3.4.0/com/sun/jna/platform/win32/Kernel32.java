package com.sun.jna.platform.win32;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.win32.W32APIOptions;
import java.nio.Buffer;


















public abstract interface Kernel32
  extends WinNT
{
  public static final Kernel32 INSTANCE = (Kernel32)Native.loadLibrary("kernel32", Kernel32.class, W32APIOptions.UNICODE_OPTIONS);
  
  public abstract int FormatMessage(int paramInt1, Pointer paramPointer1, int paramInt2, int paramInt3, Buffer paramBuffer, int paramInt4, Pointer paramPointer2);
  
  public abstract boolean ReadFile(WinNT.HANDLE paramHANDLE, Buffer paramBuffer, int paramInt, IntByReference paramIntByReference, WinBase.OVERLAPPED paramOVERLAPPED);
  
  public abstract Pointer LocalFree(Pointer paramPointer);
  
  public abstract Pointer GlobalFree(Pointer paramPointer);
  
  public abstract WinDef.HMODULE GetModuleHandle(String paramString);
  
  public abstract void GetSystemTime(WinBase.SYSTEMTIME paramSYSTEMTIME);
  
  public abstract void GetLocalTime(WinBase.SYSTEMTIME paramSYSTEMTIME);
  
  public abstract int GetTickCount();
  
  public abstract int GetCurrentThreadId();
  
  public abstract WinNT.HANDLE GetCurrentThread();
  
  public abstract int GetCurrentProcessId();
  
  public abstract WinNT.HANDLE GetCurrentProcess();
  
  public abstract int GetProcessId(WinNT.HANDLE paramHANDLE);
  
  public abstract int GetProcessVersion(int paramInt);
  
  public abstract boolean GetExitCodeProcess(WinNT.HANDLE paramHANDLE, IntByReference paramIntByReference);
  
  public abstract boolean TerminateProcess(WinNT.HANDLE paramHANDLE, int paramInt);
  
  public abstract int GetLastError();
  
  public abstract void SetLastError(int paramInt);
  
  public abstract int GetDriveType(String paramString);
  
  public abstract int FormatMessage(int paramInt1, Pointer paramPointer1, int paramInt2, int paramInt3, Pointer paramPointer2, int paramInt4, Pointer paramPointer3);
  
  public abstract int FormatMessage(int paramInt1, Pointer paramPointer1, int paramInt2, int paramInt3, PointerByReference paramPointerByReference, int paramInt4, Pointer paramPointer2);
  
  public abstract WinNT.HANDLE CreateFile(String paramString, int paramInt1, int paramInt2, WinBase.SECURITY_ATTRIBUTES paramSECURITY_ATTRIBUTES, int paramInt3, int paramInt4, WinNT.HANDLE paramHANDLE);
  
  public abstract boolean CopyFile(String paramString1, String paramString2, boolean paramBoolean);
  
  public abstract boolean MoveFile(String paramString1, String paramString2);
  
  public abstract boolean MoveFileEx(String paramString1, String paramString2, WinDef.DWORD paramDWORD);
  
  public abstract boolean CreateDirectory(String paramString, WinBase.SECURITY_ATTRIBUTES paramSECURITY_ATTRIBUTES);
  
  public abstract boolean ReadFile(WinNT.HANDLE paramHANDLE, Pointer paramPointer, int paramInt, IntByReference paramIntByReference, WinBase.OVERLAPPED paramOVERLAPPED);
  
  public abstract WinNT.HANDLE CreateIoCompletionPort(WinNT.HANDLE paramHANDLE1, WinNT.HANDLE paramHANDLE2, Pointer paramPointer, int paramInt);
  
  public abstract boolean GetQueuedCompletionStatus(WinNT.HANDLE paramHANDLE, IntByReference paramIntByReference, BaseTSD.ULONG_PTRByReference paramULONG_PTRByReference, PointerByReference paramPointerByReference, int paramInt);
  
  public abstract boolean PostQueuedCompletionStatus(WinNT.HANDLE paramHANDLE, int paramInt, Pointer paramPointer, WinBase.OVERLAPPED paramOVERLAPPED);
  
  public abstract int WaitForSingleObject(WinNT.HANDLE paramHANDLE, int paramInt);
  
  public abstract int WaitForMultipleObjects(int paramInt1, WinNT.HANDLE[] paramArrayOfHANDLE, boolean paramBoolean, int paramInt2);
  
  public abstract boolean DuplicateHandle(WinNT.HANDLE paramHANDLE1, WinNT.HANDLE paramHANDLE2, WinNT.HANDLE paramHANDLE3, WinNT.HANDLEByReference paramHANDLEByReference, int paramInt1, boolean paramBoolean, int paramInt2);
  
  public abstract boolean CloseHandle(WinNT.HANDLE paramHANDLE);
  
  public abstract boolean ReadDirectoryChangesW(WinNT.HANDLE paramHANDLE, WinNT.FILE_NOTIFY_INFORMATION paramFILE_NOTIFY_INFORMATION, int paramInt1, boolean paramBoolean, int paramInt2, IntByReference paramIntByReference, WinBase.OVERLAPPED paramOVERLAPPED, WinNT.OVERLAPPED_COMPLETION_ROUTINE paramOVERLAPPED_COMPLETION_ROUTINE);
  
  public abstract int GetShortPathName(String paramString, char[] paramArrayOfChar, int paramInt);
  
  public abstract Pointer LocalAlloc(int paramInt1, int paramInt2);
  
  public abstract boolean WriteFile(WinNT.HANDLE paramHANDLE, byte[] paramArrayOfByte, int paramInt, IntByReference paramIntByReference, WinBase.OVERLAPPED paramOVERLAPPED);
  
  public abstract WinNT.HANDLE CreateEvent(WinBase.SECURITY_ATTRIBUTES paramSECURITY_ATTRIBUTES, boolean paramBoolean1, boolean paramBoolean2, String paramString);
  
  public abstract boolean SetEvent(WinNT.HANDLE paramHANDLE);
  
  public abstract boolean ResetEvent(WinNT.HANDLE paramHANDLE);
  
  public abstract boolean PulseEvent(WinNT.HANDLE paramHANDLE);
  
  public abstract WinNT.HANDLE CreateFileMapping(WinNT.HANDLE paramHANDLE, WinBase.SECURITY_ATTRIBUTES paramSECURITY_ATTRIBUTES, int paramInt1, int paramInt2, int paramInt3, String paramString);
  
  public abstract Pointer MapViewOfFile(WinNT.HANDLE paramHANDLE, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  public abstract boolean UnmapViewOfFile(Pointer paramPointer);
  
  public abstract boolean GetComputerName(char[] paramArrayOfChar, IntByReference paramIntByReference);
  
  public abstract WinNT.HANDLE OpenThread(int paramInt1, boolean paramBoolean, int paramInt2);
  
  public abstract boolean CreateProcess(String paramString1, String paramString2, WinBase.SECURITY_ATTRIBUTES paramSECURITY_ATTRIBUTES1, WinBase.SECURITY_ATTRIBUTES paramSECURITY_ATTRIBUTES2, boolean paramBoolean, WinDef.DWORD paramDWORD, Pointer paramPointer, String paramString3, WinBase.STARTUPINFO paramSTARTUPINFO, WinBase.PROCESS_INFORMATION paramPROCESS_INFORMATION);
  
  public abstract boolean CreateProcessW(String paramString1, char[] paramArrayOfChar, WinBase.SECURITY_ATTRIBUTES paramSECURITY_ATTRIBUTES1, WinBase.SECURITY_ATTRIBUTES paramSECURITY_ATTRIBUTES2, boolean paramBoolean, WinDef.DWORD paramDWORD, Pointer paramPointer, String paramString2, WinBase.STARTUPINFO paramSTARTUPINFO, WinBase.PROCESS_INFORMATION paramPROCESS_INFORMATION);
  
  public abstract WinNT.HANDLE OpenProcess(int paramInt1, boolean paramBoolean, int paramInt2);
  
  public abstract WinDef.DWORD GetTempPath(WinDef.DWORD paramDWORD, char[] paramArrayOfChar);
  
  public abstract WinDef.DWORD GetVersion();
  
  public abstract boolean GetVersionEx(WinNT.OSVERSIONINFO paramOSVERSIONINFO);
  
  public abstract boolean GetVersionEx(WinNT.OSVERSIONINFOEX paramOSVERSIONINFOEX);
  
  public abstract void GetSystemInfo(WinBase.SYSTEM_INFO paramSYSTEM_INFO);
  
  public abstract void GetNativeSystemInfo(WinBase.SYSTEM_INFO paramSYSTEM_INFO);
  
  public abstract boolean IsWow64Process(WinNT.HANDLE paramHANDLE, IntByReference paramIntByReference);
  
  public abstract boolean GetLogicalProcessorInformation(Pointer paramPointer, WinDef.DWORDByReference paramDWORDByReference);
  
  public abstract boolean GlobalMemoryStatusEx(WinBase.MEMORYSTATUSEX paramMEMORYSTATUSEX);
  
  public abstract boolean GetFileTime(WinNT.HANDLE paramHANDLE, WinBase.FILETIME paramFILETIME1, WinBase.FILETIME paramFILETIME2, WinBase.FILETIME paramFILETIME3);
  
  public abstract int SetFileTime(WinNT.HANDLE paramHANDLE, WinBase.FILETIME paramFILETIME1, WinBase.FILETIME paramFILETIME2, WinBase.FILETIME paramFILETIME3);
  
  public abstract boolean SetFileAttributes(String paramString, WinDef.DWORD paramDWORD);
  
  public abstract WinDef.DWORD GetLogicalDriveStrings(WinDef.DWORD paramDWORD, char[] paramArrayOfChar);
  
  public abstract boolean GetDiskFreeSpaceEx(String paramString, WinNT.LARGE_INTEGER paramLARGE_INTEGER1, WinNT.LARGE_INTEGER paramLARGE_INTEGER2, WinNT.LARGE_INTEGER paramLARGE_INTEGER3);
  
  public abstract boolean DeleteFile(String paramString);
  
  public abstract boolean CreatePipe(WinNT.HANDLEByReference paramHANDLEByReference1, WinNT.HANDLEByReference paramHANDLEByReference2, WinBase.SECURITY_ATTRIBUTES paramSECURITY_ATTRIBUTES, int paramInt);
  
  public abstract boolean SetHandleInformation(WinNT.HANDLE paramHANDLE, int paramInt1, int paramInt2);
  
  public abstract int GetFileAttributes(String paramString);
  
  public abstract int GetFileType(WinNT.HANDLE paramHANDLE);
  
  public abstract boolean DeviceIoControl(WinNT.HANDLE paramHANDLE, int paramInt1, Pointer paramPointer1, int paramInt2, Pointer paramPointer2, int paramInt3, IntByReference paramIntByReference, Pointer paramPointer3);
  
  public abstract boolean GetDiskFreeSpaceEx(String paramString, LongByReference paramLongByReference1, LongByReference paramLongByReference2, LongByReference paramLongByReference3);
  
  public abstract WinNT.HANDLE CreateToolhelp32Snapshot(WinDef.DWORD paramDWORD1, WinDef.DWORD paramDWORD2);
  
  public abstract boolean Process32First(WinNT.HANDLE paramHANDLE, Tlhelp32.PROCESSENTRY32 paramPROCESSENTRY32);
  
  public abstract boolean Process32Next(WinNT.HANDLE paramHANDLE, Tlhelp32.PROCESSENTRY32 paramPROCESSENTRY32);
  
  public abstract boolean SetEnvironmentVariable(String paramString1, String paramString2);
  
  public abstract int GetEnvironmentVariable(String paramString, char[] paramArrayOfChar, int paramInt);
  
  public abstract WinDef.LCID GetSystemDefaultLCID();
  
  public abstract WinDef.LCID GetUserDefaultLCID();
  
  public abstract int GetPrivateProfileInt(String paramString1, String paramString2, int paramInt, String paramString3);
  
  public abstract WinDef.DWORD GetPrivateProfileString(String paramString1, String paramString2, String paramString3, char[] paramArrayOfChar, WinDef.DWORD paramDWORD, String paramString4);
  
  public abstract boolean WritePrivateProfileString(String paramString1, String paramString2, String paramString3, String paramString4);
  
  public abstract WinDef.DWORD GetPrivateProfileSection(String paramString1, char[] paramArrayOfChar, WinDef.DWORD paramDWORD, String paramString2);
  
  public abstract WinDef.DWORD GetPrivateProfileSectionNames(char[] paramArrayOfChar, WinDef.DWORD paramDWORD, String paramString);
  
  public abstract boolean WritePrivateProfileSection(String paramString1, String paramString2, String paramString3);
}
