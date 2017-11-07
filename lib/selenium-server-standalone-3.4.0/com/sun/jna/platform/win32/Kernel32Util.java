package com.sun.jna.platform.win32;

import com.sun.jna.LastErrorException;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;























public abstract class Kernel32Util
  implements WinDef
{
  public Kernel32Util() {}
  
  public static String getComputerName()
  {
    char[] buffer = new char[WinBase.MAX_COMPUTERNAME_LENGTH + 1];
    IntByReference lpnSize = new IntByReference(buffer.length);
    if (!Kernel32.INSTANCE.GetComputerName(buffer, lpnSize)) {
      throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
    }
    return Native.toString(buffer);
  }
  







  public static String formatMessage(int code)
  {
    PointerByReference buffer = new PointerByReference();
    if (0 == Kernel32.INSTANCE.FormatMessage(4864, null, code, 0, buffer, 0, null))
    {





      throw new LastErrorException(Kernel32.INSTANCE.GetLastError());
    }
    String s = buffer.getValue().getWideString(0L);
    Kernel32.INSTANCE.LocalFree(buffer.getValue());
    return s.trim();
  }
  








  public static String formatMessage(WinNT.HRESULT code) { return formatMessage(code.intValue()); }
  
  /**
   * @deprecated
   */
  public static String formatMessageFromHR(WinNT.HRESULT code) { return formatMessage(code.intValue()); }
  







  public static String formatMessageFromLastErrorCode(int code)
  {
    return formatMessageFromHR(W32Errors.HRESULT_FROM_WIN32(code));
  }
  




  public static String getTempPath()
  {
    WinDef.DWORD nBufferLength = new WinDef.DWORD(260L);
    char[] buffer = new char[nBufferLength.intValue()];
    if (Kernel32.INSTANCE.GetTempPath(nBufferLength, buffer).intValue() == 0) {
      throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
    }
    return Native.toString(buffer);
  }
  
  public static void deleteFile(String filename) {
    if (!Kernel32.INSTANCE.DeleteFile(filename)) {
      throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
    }
  }
  




  public static String[] getLogicalDriveStrings()
  {
    WinDef.DWORD dwSize = Kernel32.INSTANCE.GetLogicalDriveStrings(new WinDef.DWORD(0L), null);
    
    if (dwSize.intValue() <= 0) {
      throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
    }
    
    char[] buf = new char[dwSize.intValue()];
    dwSize = Kernel32.INSTANCE.GetLogicalDriveStrings(dwSize, buf);
    if (dwSize.intValue() <= 0) {
      throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
    }
    
    List<String> drives = new ArrayList();
    String drive = "";
    
    for (int i = 0; i < buf.length - 1; i++) {
      if (buf[i] == 0) {
        drives.add(drive);
        drive = "";
      } else {
        drive = drive + buf[i];
      }
    }
    return (String[])drives.toArray(new String[0]);
  }
  






  public static int getFileAttributes(String fileName)
  {
    int fileAttributes = Kernel32.INSTANCE.GetFileAttributes(fileName);
    if (fileAttributes == -1) {
      throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
    }
    return fileAttributes;
  }
  

  public static int getFileType(String fileName)
    throws FileNotFoundException
  {
    File f = new File(fileName);
    if (!f.exists()) {
      throw new FileNotFoundException(fileName);
    }
    
    WinNT.HANDLE hFile = null;
    try {
      hFile = Kernel32.INSTANCE.CreateFile(fileName, Integer.MIN_VALUE, 1, new WinBase.SECURITY_ATTRIBUTES(), 3, 128, new WinNT.HANDLEByReference().getValue());
      



      if (WinBase.INVALID_HANDLE_VALUE.equals(hFile)) {
        throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
      }
      
      int type = Kernel32.INSTANCE.GetFileType(hFile);
      switch (type) {
      case 0: 
        int err = Kernel32.INSTANCE.GetLastError();
        switch (err) {
        case 0: 
          break;
        default: 
          throw new Win32Exception(err);
        }
        
        break;
      }
      return type;
    }
    finally {
      if ((hFile != null) && 
        (!Kernel32.INSTANCE.CloseHandle(hFile))) {
        throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
      }
    }
  }
  



  public static int getDriveType(String rootName)
  {
    return Kernel32.INSTANCE.GetDriveType(rootName);
  }
  







  public static String getEnvironmentVariable(String name)
  {
    int size = Kernel32.INSTANCE.GetEnvironmentVariable(name, null, 0);
    if (size == 0)
      return null;
    if (size < 0) {
      throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
    }
    
    char[] buffer = new char[size];
    size = Kernel32.INSTANCE.GetEnvironmentVariable(name, buffer, buffer.length);
    
    if (size <= 0) {
      throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
    }
    return Native.toString(buffer);
  }
  




















  public static final int getPrivateProfileInt(String appName, String keyName, int defaultValue, String fileName)
  {
    return Kernel32.INSTANCE.GetPrivateProfileInt(appName, keyName, defaultValue, fileName);
  }
  


















































  public static final String getPrivateProfileString(String lpAppName, String lpKeyName, String lpDefault, String lpFileName)
  {
    char[] buffer = new char['Ѐ'];
    Kernel32.INSTANCE.GetPrivateProfileString(lpAppName, lpKeyName, lpDefault, buffer, new WinDef.DWORD(buffer.length), lpFileName);
    
    return Native.toString(buffer);
  }
  
  public static final void writePrivateProfileString(String appName, String keyName, String string, String fileName)
  {
    if (!Kernel32.INSTANCE.WritePrivateProfileString(appName, keyName, string, fileName))
    {
      throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
    }
  }
  




  public static final WinNT.SYSTEM_LOGICAL_PROCESSOR_INFORMATION[] getLogicalProcessorInformation()
  {
    int sizePerStruct = new WinNT.SYSTEM_LOGICAL_PROCESSOR_INFORMATION().size();
    
    WinDef.DWORDByReference bufferSize = new WinDef.DWORDByReference(new WinDef.DWORD(sizePerStruct));
    Memory memory;
    for (;;)
    {
      memory = new Memory(bufferSize.getValue().intValue());
      if (Kernel32.INSTANCE.GetLogicalProcessorInformation(memory, bufferSize))
        break;
      int err = Kernel32.INSTANCE.GetLastError();
      if (err != 122) {
        throw new Win32Exception(err);
      }
    }
    

    WinNT.SYSTEM_LOGICAL_PROCESSOR_INFORMATION firstInformation = new WinNT.SYSTEM_LOGICAL_PROCESSOR_INFORMATION(memory);
    
    int returnedStructCount = bufferSize.getValue().intValue() / sizePerStruct;
    
    return (WinNT.SYSTEM_LOGICAL_PROCESSOR_INFORMATION[])firstInformation.toArray(new WinNT.SYSTEM_LOGICAL_PROCESSOR_INFORMATION[returnedStructCount]);
  }
  

















  public static final String[] getPrivateProfileSection(String appName, String fileName)
  {
    char[] buffer = new char[32768];
    if (Kernel32.INSTANCE.GetPrivateProfileSection(appName, buffer, new WinDef.DWORD(buffer.length), fileName).intValue() == 0) {
      throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
    }
    return new String(buffer).split("\000");
  }
  










  public static final String[] getPrivateProfileSectionNames(String fileName)
  {
    char[] buffer = new char[65536];
    if (Kernel32.INSTANCE.GetPrivateProfileSectionNames(buffer, new WinDef.DWORD(buffer.length), fileName).intValue() == 0) {
      throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
    }
    return new String(buffer).split("\000");
  }
  








  public static final void writePrivateProfileSection(String appName, String[] strings, String fileName)
  {
    StringBuilder buffer = new StringBuilder();
    for (String string : strings)
      buffer.append(string).append('\000');
    buffer.append('\000');
    if (!Kernel32.INSTANCE.WritePrivateProfileSection(appName, buffer.toString(), fileName)) {
      throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
    }
  }
}
