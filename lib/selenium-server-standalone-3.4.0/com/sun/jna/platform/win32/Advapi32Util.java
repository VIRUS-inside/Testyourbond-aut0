package com.sun.jna.platform.win32;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.WString;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import com.sun.jna.ptr.PointerByReference;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;










































































public abstract class Advapi32Util
{
  public Advapi32Util() {}
  
  public static String getUserName()
  {
    char[] buffer = new char[''];
    IntByReference len = new IntByReference(buffer.length);
    boolean result = Advapi32.INSTANCE.GetUserNameW(buffer, len);
    
    if (!result) {
      switch (Kernel32.INSTANCE.GetLastError()) {
      case 122: 
        buffer = new char[len.getValue()];
        break;
      
      default: 
        throw new Win32Exception(Native.getLastError());
      }
      
      result = Advapi32.INSTANCE.GetUserNameW(buffer, len);
    }
    
    if (!result) {
      throw new Win32Exception(Native.getLastError());
    }
    
    return Native.toString(buffer);
  }
  







  public static Account getAccountByName(String accountName)
  {
    return getAccountByName(null, accountName);
  }
  








  public static Account getAccountByName(String systemName, String accountName)
  {
    IntByReference pSid = new IntByReference(0);
    IntByReference cchDomainName = new IntByReference(0);
    PointerByReference peUse = new PointerByReference();
    
    if (Advapi32.INSTANCE.LookupAccountName(systemName, accountName, null, pSid, null, cchDomainName, peUse))
    {
      throw new RuntimeException("LookupAccountNameW was expected to fail with ERROR_INSUFFICIENT_BUFFER");
    }
    

    int rc = Kernel32.INSTANCE.GetLastError();
    if ((pSid.getValue() == 0) || (rc != 122)) {
      throw new Win32Exception(rc);
    }
    
    Memory sidMemory = new Memory(pSid.getValue());
    WinNT.PSID result = new WinNT.PSID(sidMemory);
    char[] referencedDomainName = new char[cchDomainName.getValue() + 1];
    
    if (!Advapi32.INSTANCE.LookupAccountName(systemName, accountName, result, pSid, referencedDomainName, cchDomainName, peUse))
    {
      throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
    }
    
    Account account = new Account();
    accountType = peUse.getPointer().getInt(0L);
    name = accountName;
    
    String[] accountNamePartsBs = accountName.split("\\\\", 2);
    String[] accountNamePartsAt = accountName.split("@", 2);
    
    if (accountNamePartsBs.length == 2) {
      name = accountNamePartsBs[1];
    } else if (accountNamePartsAt.length == 2) {
      name = accountNamePartsAt[0];
    } else {
      name = accountName;
    }
    
    if (cchDomainName.getValue() > 0) {
      domain = Native.toString(referencedDomainName);
      fqn = (domain + "\\" + name);
    } else {
      fqn = name;
    }
    
    sid = result.getBytes();
    sidString = convertSidToStringSid(new WinNT.PSID(sid));
    return account;
  }
  






  public static Account getAccountBySid(WinNT.PSID sid)
  {
    return getAccountBySid(null, sid);
  }
  








  public static Account getAccountBySid(String systemName, WinNT.PSID sid)
  {
    IntByReference cchName = new IntByReference();
    IntByReference cchDomainName = new IntByReference();
    PointerByReference peUse = new PointerByReference();
    
    if (Advapi32.INSTANCE.LookupAccountSid(null, sid, null, cchName, null, cchDomainName, peUse))
    {
      throw new RuntimeException("LookupAccountSidW was expected to fail with ERROR_INSUFFICIENT_BUFFER");
    }
    

    int rc = Kernel32.INSTANCE.GetLastError();
    if ((cchName.getValue() == 0) || (rc != 122))
    {
      throw new Win32Exception(rc);
    }
    
    char[] domainName = new char[cchDomainName.getValue()];
    char[] name = new char[cchName.getValue()];
    
    if (!Advapi32.INSTANCE.LookupAccountSid(null, sid, name, cchName, domainName, cchDomainName, peUse))
    {
      throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
    }
    
    Account account = new Account();
    accountType = peUse.getPointer().getInt(0L);
    name = Native.toString(name);
    
    if (cchDomainName.getValue() > 0) {
      domain = Native.toString(domainName);
      fqn = (domain + "\\" + name);
    } else {
      fqn = name;
    }
    
    sid = sid.getBytes();
    sidString = convertSidToStringSid(sid);
    return account;
  }
  







  public static String convertSidToStringSid(WinNT.PSID sid)
  {
    PointerByReference stringSid = new PointerByReference();
    if (!Advapi32.INSTANCE.ConvertSidToStringSid(sid, stringSid)) {
      throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
    }
    String result = stringSid.getValue().getWideString(0L);
    Kernel32.INSTANCE.LocalFree(stringSid.getValue());
    return result;
  }
  







  public static byte[] convertStringSidToSid(String sidString)
  {
    WinNT.PSIDByReference pSID = new WinNT.PSIDByReference();
    if (!Advapi32.INSTANCE.ConvertStringSidToSid(sidString, pSID)) {
      throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
    }
    return pSID.getValue().getBytes();
  }
  









  public static boolean isWellKnownSid(String sidString, int wellKnownSidType)
  {
    WinNT.PSIDByReference pSID = new WinNT.PSIDByReference();
    if (!Advapi32.INSTANCE.ConvertStringSidToSid(sidString, pSID)) {
      throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
    }
    return Advapi32.INSTANCE.IsWellKnownSid(pSID.getValue(), wellKnownSidType);
  }
  










  public static boolean isWellKnownSid(byte[] sidBytes, int wellKnownSidType)
  {
    WinNT.PSID pSID = new WinNT.PSID(sidBytes);
    return Advapi32.INSTANCE.IsWellKnownSid(pSID, wellKnownSidType);
  }
  






  public static Account getAccountBySid(String sidString)
  {
    return getAccountBySid(null, sidString);
  }
  








  public static Account getAccountBySid(String systemName, String sidString)
  {
    return getAccountBySid(systemName, new WinNT.PSID(convertStringSidToSid(sidString)));
  }
  









  public static Account[] getTokenGroups(WinNT.HANDLE hToken)
  {
    IntByReference tokenInformationLength = new IntByReference();
    if (Advapi32.INSTANCE.GetTokenInformation(hToken, 2, null, 0, tokenInformationLength))
    {

      throw new RuntimeException("Expected GetTokenInformation to fail with ERROR_INSUFFICIENT_BUFFER");
    }
    
    int rc = Kernel32.INSTANCE.GetLastError();
    if (rc != 122) {
      throw new Win32Exception(rc);
    }
    
    WinNT.TOKEN_GROUPS groups = new WinNT.TOKEN_GROUPS(tokenInformationLength.getValue());
    
    if (!Advapi32.INSTANCE.GetTokenInformation(hToken, 2, groups, tokenInformationLength.getValue(), tokenInformationLength))
    {

      throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
    }
    ArrayList<Account> userGroups = new ArrayList();
    
    for (WinNT.SID_AND_ATTRIBUTES sidAndAttribute : groups.getGroups()) {
      Account group = null;
      try {
        group = getAccountBySid(Sid);
      } catch (Exception e) {
        group = new Account();
        sid = Sid.getBytes();
        sidString = convertSidToStringSid(Sid);
        
        name = sidString;
        fqn = sidString;
        accountType = 2;
      }
      userGroups.add(group);
    }
    return (Account[])userGroups.toArray(new Account[0]);
  }
  








  public static Account getTokenAccount(WinNT.HANDLE hToken)
  {
    IntByReference tokenInformationLength = new IntByReference();
    if (Advapi32.INSTANCE.GetTokenInformation(hToken, 1, null, 0, tokenInformationLength))
    {

      throw new RuntimeException("Expected GetTokenInformation to fail with ERROR_INSUFFICIENT_BUFFER");
    }
    
    int rc = Kernel32.INSTANCE.GetLastError();
    if (rc != 122) {
      throw new Win32Exception(rc);
    }
    
    WinNT.TOKEN_USER user = new WinNT.TOKEN_USER(tokenInformationLength.getValue());
    
    if (!Advapi32.INSTANCE.GetTokenInformation(hToken, 1, user, tokenInformationLength.getValue(), tokenInformationLength))
    {

      throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
    }
    return getAccountBySid(User.Sid);
  }
  




  public static Account[] getCurrentUserGroups()
  {
    WinNT.HANDLEByReference phToken = new WinNT.HANDLEByReference();
    try
    {
      WinNT.HANDLE threadHandle = Kernel32.INSTANCE.GetCurrentThread();
      WinNT.HANDLE processHandle; if (!Advapi32.INSTANCE.OpenThreadToken(threadHandle, 10, true, phToken))
      {
        if (1008 != Kernel32.INSTANCE.GetLastError())
        {
          throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        processHandle = Kernel32.INSTANCE.GetCurrentProcess();
        if (!Advapi32.INSTANCE.OpenProcessToken(processHandle, 10, phToken))
        {
          throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
      }
      return getTokenGroups(phToken.getValue());
    } finally {
      if ((phToken.getValue() != WinBase.INVALID_HANDLE_VALUE) && 
        (!Kernel32.INSTANCE.CloseHandle(phToken.getValue()))) {
        throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
      }
    }
  }
  









  public static boolean registryKeyExists(WinReg.HKEY root, String key)
  {
    WinReg.HKEYByReference phkKey = new WinReg.HKEYByReference();
    int rc = Advapi32.INSTANCE.RegOpenKeyEx(root, key, 0, 131097, phkKey);
    
    switch (rc) {
    case 0: 
      Advapi32.INSTANCE.RegCloseKey(phkKey.getValue());
      return true;
    case 2: 
      return false;
    }
    throw new Win32Exception(rc);
  }
  












  public static boolean registryValueExists(WinReg.HKEY root, String key, String value)
  {
    WinReg.HKEYByReference phkKey = new WinReg.HKEYByReference();
    int rc = Advapi32.INSTANCE.RegOpenKeyEx(root, key, 0, 131097, phkKey);
    try
    {
      switch (rc) {
      case 0: 
        break;
      case 2: 
        return false;
      default: 
        throw new Win32Exception(rc);
      }
      IntByReference lpcbData = new IntByReference();
      IntByReference lpType = new IntByReference();
      rc = Advapi32.INSTANCE.RegQueryValueEx(phkKey.getValue(), value, 0, lpType, (char[])null, lpcbData);
      boolean bool2;
      switch (rc) {
      case 0: 
      case 122: 
      case 234: 
        return true;
      case 2: 
        return false;
      }
      throw new Win32Exception(rc);
    }
    finally {
      if (phkKey.getValue() != WinBase.INVALID_HANDLE_VALUE) {
        rc = Advapi32.INSTANCE.RegCloseKey(phkKey.getValue());
        if (rc != 0) {
          throw new Win32Exception(rc);
        }
      }
    }
  }
  











  public static String registryGetStringValue(WinReg.HKEY root, String key, String value)
  {
    WinReg.HKEYByReference phkKey = new WinReg.HKEYByReference();
    int rc = Advapi32.INSTANCE.RegOpenKeyEx(root, key, 0, 131097, phkKey);
    
    if (rc != 0) {
      throw new Win32Exception(rc);
    }
    try {
      return registryGetStringValue(phkKey.getValue(), value);
    } finally {
      rc = Advapi32.INSTANCE.RegCloseKey(phkKey.getValue());
      if (rc != 0) {
        throw new Win32Exception(rc);
      }
    }
  }
  








  public static String registryGetStringValue(WinReg.HKEY hKey, String value)
  {
    IntByReference lpcbData = new IntByReference();
    IntByReference lpType = new IntByReference();
    int rc = Advapi32.INSTANCE.RegQueryValueEx(hKey, value, 0, lpType, (char[])null, lpcbData);
    
    if ((rc != 0) && (rc != 122))
    {
      throw new Win32Exception(rc);
    }
    if ((lpType.getValue() != 1) && (lpType.getValue() != 2))
    {
      throw new RuntimeException("Unexpected registry type " + lpType.getValue() + ", expected REG_SZ or REG_EXPAND_SZ");
    }
    

    char[] data = new char[lpcbData.getValue()];
    rc = Advapi32.INSTANCE.RegQueryValueEx(hKey, value, 0, lpType, data, lpcbData);
    
    if ((rc != 0) && (rc != 122))
    {
      throw new Win32Exception(rc);
    }
    return Native.toString(data);
  }
  











  public static String registryGetExpandableStringValue(WinReg.HKEY root, String key, String value)
  {
    WinReg.HKEYByReference phkKey = new WinReg.HKEYByReference();
    int rc = Advapi32.INSTANCE.RegOpenKeyEx(root, key, 0, 131097, phkKey);
    
    if (rc != 0) {
      throw new Win32Exception(rc);
    }
    try {
      return registryGetExpandableStringValue(phkKey.getValue(), value);
    } finally {
      rc = Advapi32.INSTANCE.RegCloseKey(phkKey.getValue());
      if (rc != 0) {
        throw new Win32Exception(rc);
      }
    }
  }
  








  public static String registryGetExpandableStringValue(WinReg.HKEY hKey, String value)
  {
    IntByReference lpcbData = new IntByReference();
    IntByReference lpType = new IntByReference();
    int rc = Advapi32.INSTANCE.RegQueryValueEx(hKey, value, 0, lpType, (char[])null, lpcbData);
    
    if ((rc != 0) && (rc != 122))
    {
      throw new Win32Exception(rc);
    }
    if (lpType.getValue() != 2) {
      throw new RuntimeException("Unexpected registry type " + lpType.getValue() + ", expected REG_SZ");
    }
    
    char[] data = new char[lpcbData.getValue()];
    rc = Advapi32.INSTANCE.RegQueryValueEx(hKey, value, 0, lpType, data, lpcbData);
    
    if ((rc != 0) && (rc != 122))
    {
      throw new Win32Exception(rc);
    }
    return Native.toString(data);
  }
  











  public static String[] registryGetStringArray(WinReg.HKEY root, String key, String value)
  {
    WinReg.HKEYByReference phkKey = new WinReg.HKEYByReference();
    int rc = Advapi32.INSTANCE.RegOpenKeyEx(root, key, 0, 131097, phkKey);
    
    if (rc != 0) {
      throw new Win32Exception(rc);
    }
    try {
      return registryGetStringArray(phkKey.getValue(), value);
    } finally {
      rc = Advapi32.INSTANCE.RegCloseKey(phkKey.getValue());
      if (rc != 0) {
        throw new Win32Exception(rc);
      }
    }
  }
  








  public static String[] registryGetStringArray(WinReg.HKEY hKey, String value)
  {
    IntByReference lpcbData = new IntByReference();
    IntByReference lpType = new IntByReference();
    int rc = Advapi32.INSTANCE.RegQueryValueEx(hKey, value, 0, lpType, (char[])null, lpcbData);
    
    if ((rc != 0) && (rc != 122))
    {
      throw new Win32Exception(rc);
    }
    if (lpType.getValue() != 7) {
      throw new RuntimeException("Unexpected registry type " + lpType.getValue() + ", expected REG_SZ");
    }
    
    Memory data = new Memory(lpcbData.getValue());
    rc = Advapi32.INSTANCE.RegQueryValueEx(hKey, value, 0, lpType, data, lpcbData);
    
    if ((rc != 0) && (rc != 122))
    {
      throw new Win32Exception(rc);
    }
    ArrayList<String> result = new ArrayList();
    int offset = 0;
    while (offset < data.size()) {
      String s = data.getWideString(offset);
      offset += s.length() * Native.WCHAR_SIZE;
      offset += Native.WCHAR_SIZE;
      if ((s.length() != 0) || (offset != data.size()))
      {

        result.add(s);
      }
    }
    return (String[])result.toArray(new String[0]);
  }
  











  public static byte[] registryGetBinaryValue(WinReg.HKEY root, String key, String value)
  {
    WinReg.HKEYByReference phkKey = new WinReg.HKEYByReference();
    int rc = Advapi32.INSTANCE.RegOpenKeyEx(root, key, 0, 131097, phkKey);
    
    if (rc != 0) {
      throw new Win32Exception(rc);
    }
    try {
      return registryGetBinaryValue(phkKey.getValue(), value);
    } finally {
      rc = Advapi32.INSTANCE.RegCloseKey(phkKey.getValue());
      if (rc != 0) {
        throw new Win32Exception(rc);
      }
    }
  }
  








  public static byte[] registryGetBinaryValue(WinReg.HKEY hKey, String value)
  {
    IntByReference lpcbData = new IntByReference();
    IntByReference lpType = new IntByReference();
    int rc = Advapi32.INSTANCE.RegQueryValueEx(hKey, value, 0, lpType, (char[])null, lpcbData);
    
    if ((rc != 0) && (rc != 122))
    {
      throw new Win32Exception(rc);
    }
    if (lpType.getValue() != 3) {
      throw new RuntimeException("Unexpected registry type " + lpType.getValue() + ", expected REG_BINARY");
    }
    
    byte[] data = new byte[lpcbData.getValue()];
    rc = Advapi32.INSTANCE.RegQueryValueEx(hKey, value, 0, lpType, data, lpcbData);
    
    if ((rc != 0) && (rc != 122))
    {
      throw new Win32Exception(rc);
    }
    return data;
  }
  










  public static int registryGetIntValue(WinReg.HKEY root, String key, String value)
  {
    WinReg.HKEYByReference phkKey = new WinReg.HKEYByReference();
    int rc = Advapi32.INSTANCE.RegOpenKeyEx(root, key, 0, 131097, phkKey);
    
    if (rc != 0) {
      throw new Win32Exception(rc);
    }
    try {
      return registryGetIntValue(phkKey.getValue(), value);
    } finally {
      rc = Advapi32.INSTANCE.RegCloseKey(phkKey.getValue());
      if (rc != 0) {
        throw new Win32Exception(rc);
      }
    }
  }
  








  public static int registryGetIntValue(WinReg.HKEY hKey, String value)
  {
    IntByReference lpcbData = new IntByReference();
    IntByReference lpType = new IntByReference();
    int rc = Advapi32.INSTANCE.RegQueryValueEx(hKey, value, 0, lpType, (char[])null, lpcbData);
    
    if ((rc != 0) && (rc != 122))
    {
      throw new Win32Exception(rc);
    }
    if (lpType.getValue() != 4) {
      throw new RuntimeException("Unexpected registry type " + lpType.getValue() + ", expected REG_DWORD");
    }
    
    IntByReference data = new IntByReference();
    rc = Advapi32.INSTANCE.RegQueryValueEx(hKey, value, 0, lpType, data, lpcbData);
    
    if ((rc != 0) && (rc != 122))
    {
      throw new Win32Exception(rc);
    }
    return data.getValue();
  }
  










  public static long registryGetLongValue(WinReg.HKEY root, String key, String value)
  {
    WinReg.HKEYByReference phkKey = new WinReg.HKEYByReference();
    int rc = Advapi32.INSTANCE.RegOpenKeyEx(root, key, 0, 131097, phkKey);
    
    if (rc != 0) {
      throw new Win32Exception(rc);
    }
    try {
      return registryGetLongValue(phkKey.getValue(), value);
    } finally {
      rc = Advapi32.INSTANCE.RegCloseKey(phkKey.getValue());
      if (rc != 0) {
        throw new Win32Exception(rc);
      }
    }
  }
  








  public static long registryGetLongValue(WinReg.HKEY hKey, String value)
  {
    IntByReference lpcbData = new IntByReference();
    IntByReference lpType = new IntByReference();
    int rc = Advapi32.INSTANCE.RegQueryValueEx(hKey, value, 0, lpType, (char[])null, lpcbData);
    
    if ((rc != 0) && (rc != 122))
    {
      throw new Win32Exception(rc);
    }
    if (lpType.getValue() != 11) {
      throw new RuntimeException("Unexpected registry type " + lpType.getValue() + ", expected REG_QWORD");
    }
    
    LongByReference data = new LongByReference();
    rc = Advapi32.INSTANCE.RegQueryValueEx(hKey, value, 0, lpType, data, lpcbData);
    
    if ((rc != 0) && (rc != 122))
    {
      throw new Win32Exception(rc);
    }
    return data.getValue();
  }
  












  public static Object registryGetValue(WinReg.HKEY hkKey, String subKey, String lpValueName)
  {
    Object result = null;
    IntByReference lpType = new IntByReference();
    byte[] lpData = new byte['㿿'];
    IntByReference lpcbData = new IntByReference(16383);
    
    int rc = Advapi32.INSTANCE.RegGetValue(hkKey, subKey, lpValueName, 65535, lpType, lpData, lpcbData);
    


    if (lpType.getValue() == 0) {
      return null;
    }
    if ((rc != 0) && (rc != 122))
    {
      throw new Win32Exception(rc);
    }
    
    Memory byteData = new Memory(lpcbData.getValue());
    byteData.write(0L, lpData, 0, lpcbData.getValue());
    
    if (lpType.getValue() == 4) {
      result = new Integer(byteData.getInt(0L));
    } else if (lpType.getValue() == 11) {
      result = new Long(byteData.getLong(0L));
    } else if (lpType.getValue() == 3) {
      result = byteData.getByteArray(0L, lpcbData.getValue());
    } else if ((lpType.getValue() == 1) || (lpType.getValue() == 2))
    {
      result = byteData.getWideString(0L);
    }
    
    return result;
  }
  








  public static boolean registryCreateKey(WinReg.HKEY hKey, String keyName)
  {
    WinReg.HKEYByReference phkResult = new WinReg.HKEYByReference();
    IntByReference lpdwDisposition = new IntByReference();
    int rc = Advapi32.INSTANCE.RegCreateKeyEx(hKey, keyName, 0, null, 0, 131097, null, phkResult, lpdwDisposition);
    

    if (rc != 0) {
      throw new Win32Exception(rc);
    }
    rc = Advapi32.INSTANCE.RegCloseKey(phkResult.getValue());
    if (rc != 0) {
      throw new Win32Exception(rc);
    }
    return 1 == lpdwDisposition.getValue();
  }
  











  public static boolean registryCreateKey(WinReg.HKEY root, String parentPath, String keyName)
  {
    WinReg.HKEYByReference phkKey = new WinReg.HKEYByReference();
    int rc = Advapi32.INSTANCE.RegOpenKeyEx(root, parentPath, 0, 4, phkKey);
    
    if (rc != 0) {
      throw new Win32Exception(rc);
    }
    try {
      return registryCreateKey(phkKey.getValue(), keyName);
    } finally {
      rc = Advapi32.INSTANCE.RegCloseKey(phkKey.getValue());
      if (rc != 0) {
        throw new Win32Exception(rc);
      }
    }
  }
  









  public static void registrySetIntValue(WinReg.HKEY hKey, String name, int value)
  {
    byte[] data = new byte[4];
    data[0] = ((byte)(value & 0xFF));
    data[1] = ((byte)(value >> 8 & 0xFF));
    data[2] = ((byte)(value >> 16 & 0xFF));
    data[3] = ((byte)(value >> 24 & 0xFF));
    int rc = Advapi32.INSTANCE.RegSetValueEx(hKey, name, 0, 4, data, 4);
    
    if (rc != 0) {
      throw new Win32Exception(rc);
    }
  }
  












  public static void registrySetIntValue(WinReg.HKEY root, String keyPath, String name, int value)
  {
    WinReg.HKEYByReference phkKey = new WinReg.HKEYByReference();
    int rc = Advapi32.INSTANCE.RegOpenKeyEx(root, keyPath, 0, 131103, phkKey);
    
    if (rc != 0) {
      throw new Win32Exception(rc);
    }
    try {
      registrySetIntValue(phkKey.getValue(), name, value);
    } finally {
      rc = Advapi32.INSTANCE.RegCloseKey(phkKey.getValue());
      if (rc != 0) {
        throw new Win32Exception(rc);
      }
    }
  }
  









  public static void registrySetLongValue(WinReg.HKEY hKey, String name, long value)
  {
    byte[] data = new byte[8];
    data[0] = ((byte)(int)(value & 0xFF));
    data[1] = ((byte)(int)(value >> 8 & 0xFF));
    data[2] = ((byte)(int)(value >> 16 & 0xFF));
    data[3] = ((byte)(int)(value >> 24 & 0xFF));
    data[4] = ((byte)(int)(value >> 32 & 0xFF));
    data[5] = ((byte)(int)(value >> 40 & 0xFF));
    data[6] = ((byte)(int)(value >> 48 & 0xFF));
    data[7] = ((byte)(int)(value >> 56 & 0xFF));
    int rc = Advapi32.INSTANCE.RegSetValueEx(hKey, name, 0, 11, data, 8);
    
    if (rc != 0) {
      throw new Win32Exception(rc);
    }
  }
  












  public static void registrySetLongValue(WinReg.HKEY root, String keyPath, String name, long value)
  {
    WinReg.HKEYByReference phkKey = new WinReg.HKEYByReference();
    int rc = Advapi32.INSTANCE.RegOpenKeyEx(root, keyPath, 0, 131103, phkKey);
    
    if (rc != 0) {
      throw new Win32Exception(rc);
    }
    try {
      registrySetLongValue(phkKey.getValue(), name, value);
    } finally {
      rc = Advapi32.INSTANCE.RegCloseKey(phkKey.getValue());
      if (rc != 0) {
        throw new Win32Exception(rc);
      }
    }
  }
  










  public static void registrySetStringValue(WinReg.HKEY hKey, String name, String value)
  {
    char[] data = Native.toCharArray(value);
    int rc = Advapi32.INSTANCE.RegSetValueEx(hKey, name, 0, 1, data, data.length * Native.WCHAR_SIZE);
    
    if (rc != 0) {
      throw new Win32Exception(rc);
    }
  }
  












  public static void registrySetStringValue(WinReg.HKEY root, String keyPath, String name, String value)
  {
    WinReg.HKEYByReference phkKey = new WinReg.HKEYByReference();
    int rc = Advapi32.INSTANCE.RegOpenKeyEx(root, keyPath, 0, 131103, phkKey);
    
    if (rc != 0) {
      throw new Win32Exception(rc);
    }
    try {
      registrySetStringValue(phkKey.getValue(), name, value);
    } finally {
      rc = Advapi32.INSTANCE.RegCloseKey(phkKey.getValue());
      if (rc != 0) {
        throw new Win32Exception(rc);
      }
    }
  }
  










  public static void registrySetExpandableStringValue(WinReg.HKEY hKey, String name, String value)
  {
    char[] data = Native.toCharArray(value);
    int rc = Advapi32.INSTANCE.RegSetValueEx(hKey, name, 0, 2, data, data.length * Native.WCHAR_SIZE);
    
    if (rc != 0) {
      throw new Win32Exception(rc);
    }
  }
  












  public static void registrySetExpandableStringValue(WinReg.HKEY root, String keyPath, String name, String value)
  {
    WinReg.HKEYByReference phkKey = new WinReg.HKEYByReference();
    int rc = Advapi32.INSTANCE.RegOpenKeyEx(root, keyPath, 0, 131103, phkKey);
    
    if (rc != 0) {
      throw new Win32Exception(rc);
    }
    try {
      registrySetExpandableStringValue(phkKey.getValue(), name, value);
    } finally {
      rc = Advapi32.INSTANCE.RegCloseKey(phkKey.getValue());
      if (rc != 0) {
        throw new Win32Exception(rc);
      }
    }
  }
  










  public static void registrySetStringArray(WinReg.HKEY hKey, String name, String[] arr)
  {
    int size = 0;
    for (String s : arr) {
      size += s.length() * Native.WCHAR_SIZE;
      size += Native.WCHAR_SIZE;
    }
    size += Native.WCHAR_SIZE;
    
    int offset = 0;
    Memory data = new Memory(size);
    for (String s : arr) {
      data.setWideString(offset, s);
      offset += s.length() * Native.WCHAR_SIZE;
      offset += Native.WCHAR_SIZE;
    }
    for (int i = 0; i < Native.WCHAR_SIZE; i++) {
      data.setByte(offset++, (byte)0);
    }
    
    int rc = Advapi32.INSTANCE.RegSetValueEx(hKey, name, 0, 7, data.getByteArray(0L, size), size);
    

    if (rc != 0) {
      throw new Win32Exception(rc);
    }
  }
  












  public static void registrySetStringArray(WinReg.HKEY root, String keyPath, String name, String[] arr)
  {
    WinReg.HKEYByReference phkKey = new WinReg.HKEYByReference();
    int rc = Advapi32.INSTANCE.RegOpenKeyEx(root, keyPath, 0, 131103, phkKey);
    
    if (rc != 0) {
      throw new Win32Exception(rc);
    }
    try {
      registrySetStringArray(phkKey.getValue(), name, arr);
    } finally {
      rc = Advapi32.INSTANCE.RegCloseKey(phkKey.getValue());
      if (rc != 0) {
        throw new Win32Exception(rc);
      }
    }
  }
  










  public static void registrySetBinaryValue(WinReg.HKEY hKey, String name, byte[] data)
  {
    int rc = Advapi32.INSTANCE.RegSetValueEx(hKey, name, 0, 3, data, data.length);
    
    if (rc != 0) {
      throw new Win32Exception(rc);
    }
  }
  












  public static void registrySetBinaryValue(WinReg.HKEY root, String keyPath, String name, byte[] data)
  {
    WinReg.HKEYByReference phkKey = new WinReg.HKEYByReference();
    int rc = Advapi32.INSTANCE.RegOpenKeyEx(root, keyPath, 0, 131103, phkKey);
    
    if (rc != 0) {
      throw new Win32Exception(rc);
    }
    try {
      registrySetBinaryValue(phkKey.getValue(), name, data);
    } finally {
      rc = Advapi32.INSTANCE.RegCloseKey(phkKey.getValue());
      if (rc != 0) {
        throw new Win32Exception(rc);
      }
    }
  }
  







  public static void registryDeleteKey(WinReg.HKEY hKey, String keyName)
  {
    int rc = Advapi32.INSTANCE.RegDeleteKey(hKey, keyName);
    if (rc != 0) {
      throw new Win32Exception(rc);
    }
  }
  










  public static void registryDeleteKey(WinReg.HKEY root, String keyPath, String keyName)
  {
    WinReg.HKEYByReference phkKey = new WinReg.HKEYByReference();
    int rc = Advapi32.INSTANCE.RegOpenKeyEx(root, keyPath, 0, 131103, phkKey);
    
    if (rc != 0) {
      throw new Win32Exception(rc);
    }
    try {
      registryDeleteKey(phkKey.getValue(), keyName);
    } finally {
      rc = Advapi32.INSTANCE.RegCloseKey(phkKey.getValue());
      if (rc != 0) {
        throw new Win32Exception(rc);
      }
    }
  }
  







  public static void registryDeleteValue(WinReg.HKEY hKey, String valueName)
  {
    int rc = Advapi32.INSTANCE.RegDeleteValue(hKey, valueName);
    if (rc != 0) {
      throw new Win32Exception(rc);
    }
  }
  










  public static void registryDeleteValue(WinReg.HKEY root, String keyPath, String valueName)
  {
    WinReg.HKEYByReference phkKey = new WinReg.HKEYByReference();
    int rc = Advapi32.INSTANCE.RegOpenKeyEx(root, keyPath, 0, 131103, phkKey);
    
    if (rc != 0) {
      throw new Win32Exception(rc);
    }
    try {
      registryDeleteValue(phkKey.getValue(), valueName);
    } finally {
      rc = Advapi32.INSTANCE.RegCloseKey(phkKey.getValue());
      if (rc != 0) {
        throw new Win32Exception(rc);
      }
    }
  }
  






  public static String[] registryGetKeys(WinReg.HKEY hKey)
  {
    IntByReference lpcSubKeys = new IntByReference();
    IntByReference lpcMaxSubKeyLen = new IntByReference();
    int rc = Advapi32.INSTANCE.RegQueryInfoKey(hKey, null, null, null, lpcSubKeys, lpcMaxSubKeyLen, null, null, null, null, null, null);
    

    if (rc != 0) {
      throw new Win32Exception(rc);
    }
    ArrayList<String> keys = new ArrayList(lpcSubKeys.getValue());
    char[] name = new char[lpcMaxSubKeyLen.getValue() + 1];
    for (int i = 0; i < lpcSubKeys.getValue(); i++) {
      IntByReference lpcchValueName = new IntByReference(lpcMaxSubKeyLen.getValue() + 1);
      
      rc = Advapi32.INSTANCE.RegEnumKeyEx(hKey, i, name, lpcchValueName, null, null, null, null);
      
      if (rc != 0) {
        throw new Win32Exception(rc);
      }
      keys.add(Native.toString(name));
    }
    return (String[])keys.toArray(new String[0]);
  }
  








  public static String[] registryGetKeys(WinReg.HKEY root, String keyPath)
  {
    WinReg.HKEYByReference phkKey = new WinReg.HKEYByReference();
    int rc = Advapi32.INSTANCE.RegOpenKeyEx(root, keyPath, 0, 131097, phkKey);
    
    if (rc != 0) {
      throw new Win32Exception(rc);
    }
    try {
      return registryGetKeys(phkKey.getValue());
    } finally {
      rc = Advapi32.INSTANCE.RegCloseKey(phkKey.getValue());
      if (rc != 0) {
        throw new Win32Exception(rc);
      }
    }
  }
  

  public static class Account
  {
    public String name;
    public String domain;
    public byte[] sid;
    public String sidString;
    public int accountType;
    public String fqn;
    
    public Account() {}
  }
  
  public static WinReg.HKEYByReference registryGetKey(WinReg.HKEY root, String keyPath, int samDesired)
  {
    WinReg.HKEYByReference phkKey = new WinReg.HKEYByReference();
    int rc = Advapi32.INSTANCE.RegOpenKeyEx(root, keyPath, 0, samDesired, phkKey);
    
    if (rc != 0) {
      throw new Win32Exception(rc);
    }
    
    return phkKey;
  }
  





  public static void registryCloseKey(WinReg.HKEY hKey)
  {
    int rc = Advapi32.INSTANCE.RegCloseKey(hKey);
    if (rc != 0) {
      throw new Win32Exception(rc);
    }
  }
  






  public static TreeMap<String, Object> registryGetValues(WinReg.HKEY hKey)
  {
    IntByReference lpcValues = new IntByReference();
    IntByReference lpcMaxValueNameLen = new IntByReference();
    IntByReference lpcMaxValueLen = new IntByReference();
    int rc = Advapi32.INSTANCE.RegQueryInfoKey(hKey, null, null, null, null, null, null, lpcValues, lpcMaxValueNameLen, lpcMaxValueLen, null, null);
    

    if (rc != 0) {
      throw new Win32Exception(rc);
    }
    TreeMap<String, Object> keyValues = new TreeMap();
    char[] name = new char[lpcMaxValueNameLen.getValue() + 1];
    byte[] data = new byte[lpcMaxValueLen.getValue()];
    for (int i = 0; i < lpcValues.getValue(); i++) {
      IntByReference lpcchValueName = new IntByReference(lpcMaxValueNameLen.getValue() + 1);
      
      IntByReference lpcbData = new IntByReference(lpcMaxValueLen.getValue());
      
      IntByReference lpType = new IntByReference();
      rc = Advapi32.INSTANCE.RegEnumValue(hKey, i, name, lpcchValueName, null, lpType, data, lpcbData);
      
      if (rc != 0) {
        throw new Win32Exception(rc);
      }
      
      String nameString = Native.toString(name);
      
      if (lpcbData.getValue() == 0) {
        switch (lpType.getValue()) {
        case 3: 
          keyValues.put(nameString, new byte[0]);
          break;
        
        case 1: 
        case 2: 
          keyValues.put(nameString, new char[0]);
          break;
        
        case 7: 
          keyValues.put(nameString, new String[0]);
          break;
        
        case 0: 
          keyValues.put(nameString, null);
          break;
        case 4: case 5: 
        case 6: default: 
          throw new RuntimeException("Unsupported empty type: " + lpType.getValue());
        }
        
      }
      else
      {
        Memory byteData = new Memory(lpcbData.getValue());
        byteData.write(0L, data, 0, lpcbData.getValue());
        
        switch (lpType.getValue()) {
        case 11: 
          keyValues.put(nameString, Long.valueOf(byteData.getLong(0L)));
          break;
        
        case 4: 
          keyValues.put(nameString, Integer.valueOf(byteData.getInt(0L)));
          break;
        
        case 1: 
        case 2: 
          keyValues.put(nameString, byteData.getWideString(0L));
          break;
        
        case 3: 
          keyValues.put(nameString, byteData.getByteArray(0L, lpcbData.getValue()));
          
          break;
        
        case 7: 
          Memory stringData = new Memory(lpcbData.getValue());
          stringData.write(0L, data, 0, lpcbData.getValue());
          ArrayList<String> result = new ArrayList();
          int offset = 0;
          while (offset < stringData.size()) {
            String s = stringData.getWideString(offset);
            offset += s.length() * Native.WCHAR_SIZE;
            offset += Native.WCHAR_SIZE;
            if ((s.length() != 0) || (offset != stringData.size()))
            {

              result.add(s);
            }
          }
          keyValues.put(nameString, result.toArray(new String[0]));
          break;
        case 5: case 6: case 8: 
        case 9: case 10: default: 
          throw new RuntimeException("Unsupported type: " + lpType.getValue());
        }
      }
    }
    return keyValues;
  }
  









  public static TreeMap<String, Object> registryGetValues(WinReg.HKEY root, String keyPath)
  {
    WinReg.HKEYByReference phkKey = new WinReg.HKEYByReference();
    int rc = Advapi32.INSTANCE.RegOpenKeyEx(root, keyPath, 0, 131097, phkKey);
    
    if (rc != 0) {
      throw new Win32Exception(rc);
    }
    try {
      return registryGetValues(phkKey.getValue());
    } finally {
      rc = Advapi32.INSTANCE.RegCloseKey(phkKey.getValue());
      if (rc != 0) {
        throw new Win32Exception(rc);
      }
    }
  }
  









  public static InfoKey registryQueryInfoKey(WinReg.HKEY hKey, int lpcbSecurityDescriptor)
  {
    InfoKey infoKey = new InfoKey(hKey, lpcbSecurityDescriptor);
    int rc = Advapi32.INSTANCE.RegQueryInfoKey(hKey, lpClass, lpcClass, null, lpcSubKeys, lpcMaxSubKeyLen, lpcMaxClassLen, lpcValues, lpcMaxValueNameLen, lpcMaxValueLen, lpcbSecurityDescriptor, lpftLastWriteTime);
    





    if (rc != 0) {
      throw new Win32Exception(rc);
    }
    
    return infoKey;
  }
  
  public static class InfoKey {
    public WinReg.HKEY hKey;
    public char[] lpClass = new char['Ą'];
    public IntByReference lpcClass = new IntByReference(260);
    public IntByReference lpcSubKeys = new IntByReference();
    public IntByReference lpcMaxSubKeyLen = new IntByReference();
    public IntByReference lpcMaxClassLen = new IntByReference();
    public IntByReference lpcValues = new IntByReference();
    public IntByReference lpcMaxValueNameLen = new IntByReference();
    public IntByReference lpcMaxValueLen = new IntByReference();
    public IntByReference lpcbSecurityDescriptor = new IntByReference();
    public WinBase.FILETIME lpftLastWriteTime = new WinBase.FILETIME();
    
    public InfoKey() {}
    
    public InfoKey(WinReg.HKEY hKey, int securityDescriptor)
    {
      this.hKey = hKey;
      lpcbSecurityDescriptor = new IntByReference(securityDescriptor);
    }
  }
  







  public static EnumKey registryRegEnumKey(WinReg.HKEY hKey, int dwIndex)
  {
    EnumKey enumKey = new EnumKey(hKey, dwIndex);
    int rc = Advapi32.INSTANCE.RegEnumKeyEx(hKey, dwIndex, lpName, lpcName, null, lpClass, lpcbClass, lpftLastWriteTime);
    


    if (rc != 0) {
      throw new Win32Exception(rc);
    }
    
    return enumKey;
  }
  
  public static class EnumKey {
    public WinReg.HKEY hKey;
    public int dwIndex = 0;
    public char[] lpName = new char['ÿ'];
    public IntByReference lpcName = new IntByReference(255);
    
    public char[] lpClass = new char['ÿ'];
    public IntByReference lpcbClass = new IntByReference(255);
    
    public WinBase.FILETIME lpftLastWriteTime = new WinBase.FILETIME();
    
    public EnumKey() {}
    
    public EnumKey(WinReg.HKEY hKey, int dwIndex)
    {
      this.hKey = hKey;
      this.dwIndex = dwIndex;
    }
  }
  









  public static String getEnvironmentBlock(Map<String, String> environment)
  {
    StringBuilder out = new StringBuilder();
    for (Map.Entry<String, String> entry : environment.entrySet()) {
      if (entry.getValue() != null) {
        out.append((String)entry.getKey() + "=" + (String)entry.getValue() + "\000");
      }
    }
    return out.toString() + "\000";
  }
  


  public static enum EventLogType
  {
    Error,  Warning,  Informational,  AuditSuccess,  AuditFailure;
    
    private EventLogType() {}
  }
  
  public static class EventLogRecord
  {
    private WinNT.EVENTLOGRECORD _record = null;
    
    private String _source;
    
    private byte[] _data;
    
    private String[] _strings;
    

    public WinNT.EVENTLOGRECORD getRecord()
    {
      return _record;
    }
    




    public int getEventId()
    {
      return _record.EventID.intValue();
    }
    




    public String getSource()
    {
      return _source;
    }
    




    public int getStatusCode()
    {
      return _record.EventID.intValue() & 0xFFFF;
    }
    






    public int getRecordNumber()
    {
      return _record.RecordNumber.intValue();
    }
    




    public int getLength()
    {
      return _record.Length.intValue();
    }
    




    public String[] getStrings()
    {
      return _strings;
    }
    




    public Advapi32Util.EventLogType getType()
    {
      switch (_record.EventType.intValue()) {
      case 0: 
      case 4: 
        return Advapi32Util.EventLogType.Informational;
      case 16: 
        return Advapi32Util.EventLogType.AuditFailure;
      case 8: 
        return Advapi32Util.EventLogType.AuditSuccess;
      case 1: 
        return Advapi32Util.EventLogType.Error;
      case 2: 
        return Advapi32Util.EventLogType.Warning;
      }
      throw new RuntimeException("Invalid type: " + _record.EventType.intValue());
    }
    






    public byte[] getData()
    {
      return _data;
    }
    
    public EventLogRecord(Pointer pevlr) {
      _record = new WinNT.EVENTLOGRECORD(pevlr);
      _source = pevlr.getWideString(_record.size());
      
      if (_record.DataLength.intValue() > 0) {
        _data = pevlr.getByteArray(_record.DataOffset.intValue(), _record.DataLength.intValue());
      }
      

      if (_record.NumStrings.intValue() > 0) {
        ArrayList<String> strings = new ArrayList();
        int count = _record.NumStrings.intValue();
        long offset = _record.StringOffset.intValue();
        while (count > 0) {
          String s = pevlr.getWideString(0L);
          strings.add(s);
          offset += s.length() * Native.WCHAR_SIZE;
          offset += Native.WCHAR_SIZE;
          count--;
        }
        _strings = ((String[])strings.toArray(new String[0]));
      }
    }
  }
  



  public static class EventLogIterator
    implements Iterable<Advapi32Util.EventLogRecord>, Iterator<Advapi32Util.EventLogRecord>
  {
    private WinNT.HANDLE _h = null;
    private Memory _buffer = new Memory(65536L);
    
    private boolean _done = false;
    private int _dwRead = 0;
    
    private Pointer _pevlr = null;
    private int _flags = 4;
    
    public EventLogIterator(String sourceName) {
      this(null, sourceName, 4);
    }
    
    public EventLogIterator(String serverName, String sourceName, int flags) {
      _flags = flags;
      _h = Advapi32.INSTANCE.OpenEventLog(serverName, sourceName);
      if (_h == null) {
        throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
      }
    }
    
    private boolean read()
    {
      if ((_done) || (_dwRead > 0)) {
        return false;
      }
      
      IntByReference pnBytesRead = new IntByReference();
      IntByReference pnMinNumberOfBytesNeeded = new IntByReference();
      
      if (!Advapi32.INSTANCE.ReadEventLog(_h, 0x1 | _flags, 0, _buffer, (int)_buffer.size(), pnBytesRead, pnMinNumberOfBytesNeeded))
      {



        int rc = Kernel32.INSTANCE.GetLastError();
        

        if (rc == 122) {
          _buffer = new Memory(pnMinNumberOfBytesNeeded.getValue());
          
          if (!Advapi32.INSTANCE.ReadEventLog(_h, 0x1 | _flags, 0, _buffer, (int)_buffer.size(), pnBytesRead, pnMinNumberOfBytesNeeded))
          {


            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
          }
        }
        else
        {
          close();
          if (rc != 38) {
            throw new Win32Exception(rc);
          }
          return false;
        }
      }
      
      _dwRead = pnBytesRead.getValue();
      _pevlr = _buffer;
      return true;
    }
    



    public void close()
    {
      _done = true;
      if (_h != null) {
        if (!Advapi32.INSTANCE.CloseEventLog(_h)) {
          throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        _h = null;
      }
    }
    

    public Iterator<Advapi32Util.EventLogRecord> iterator()
    {
      return this;
    }
    

    public boolean hasNext()
    {
      read();
      return !_done;
    }
    

    public Advapi32Util.EventLogRecord next()
    {
      read();
      Advapi32Util.EventLogRecord record = new Advapi32Util.EventLogRecord(_pevlr);
      _dwRead -= record.getLength();
      _pevlr = _pevlr.share(record.getLength());
      return record;
    }
    


    public void remove() {}
  }
  

  public static WinNT.ACCESS_ACEStructure[] getFileSecurity(String fileName, boolean compact)
  {
    int infoType = 4;
    int nLength = 1024;
    boolean repeat = false;
    Memory memory = null;
    do
    {
      repeat = false;
      memory = new Memory(nLength);
      IntByReference lpnSize = new IntByReference();
      boolean succeded = Advapi32.INSTANCE.GetFileSecurity(new WString(fileName), infoType, memory, nLength, lpnSize);
      

      if (!succeded) {
        int lastError = Kernel32.INSTANCE.GetLastError();
        memory.clear();
        if (122 != lastError) {
          throw new Win32Exception(lastError);
        }
      }
      int lengthNeeded = lpnSize.getValue();
      if (nLength < lengthNeeded) {
        repeat = true;
        nLength = lengthNeeded;
        memory.clear();
      }
    } while (repeat);
    
    WinNT.SECURITY_DESCRIPTOR_RELATIVE sdr = new WinNT.SECURITY_DESCRIPTOR_RELATIVE(memory);
    
    memory.clear();
    WinNT.ACL dacl = sdr.getDiscretionaryACL();
    WinNT.ACCESS_ACEStructure[] aceStructures = dacl.getACEStructures();
    
    if (compact) {
      Map<String, WinNT.ACCESS_ACEStructure> aceMap = new HashMap();
      for (WinNT.ACCESS_ACEStructure aceStructure : aceStructures) {
        boolean inherted = (AceFlags & 0x1F) != 0;
        String key = aceStructure.getSidString() + "/" + inherted + "/" + aceStructure.getClass().getName();
        
        WinNT.ACCESS_ACEStructure aceStructure2 = (WinNT.ACCESS_ACEStructure)aceMap.get(key);
        if (aceStructure2 != null) {
          int accessMask = Mask;
          accessMask |= Mask;
          Mask = accessMask;
        } else {
          aceMap.put(key, aceStructure);
        }
      }
      return (WinNT.ACCESS_ACEStructure[])aceMap.values().toArray(new WinNT.ACCESS_ACEStructure[aceMap.size()]);
    }
    
    return aceStructures;
  }
  
  public static enum AccessCheckPermission {
    READ(Integer.MIN_VALUE), 
    WRITE(1073741824), 
    EXECUTE(536870912);
    
    final int code;
    
    private AccessCheckPermission(int code) {
      this.code = code;
    }
    
    public int getCode() {
      return code;
    }
  }
  
  private static Memory getSecurityDescriptorForFile(String absoluteFilePath)
  {
    int infoType = 7;
    

    IntByReference lpnSize = new IntByReference();
    boolean succeeded = Advapi32.INSTANCE.GetFileSecurity(new WString(absoluteFilePath), 7, null, 0, lpnSize);
    




    if (!succeeded) {
      int lastError = Kernel32.INSTANCE.GetLastError();
      if (122 != lastError) {
        throw new Win32Exception(lastError);
      }
    }
    
    int nLength = lpnSize.getValue();
    Memory securityDescriptorMemoryPointer = new Memory(nLength);
    succeeded = Advapi32.INSTANCE.GetFileSecurity(new WString(absoluteFilePath), 7, securityDescriptorMemoryPointer, nLength, lpnSize);
    

    if (!succeeded) {
      securityDescriptorMemoryPointer.clear();
      throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
    }
    
    return securityDescriptorMemoryPointer;
  }
  





  public static boolean accessCheck(File file, AccessCheckPermission permissionToCheck)
  {
    boolean hasAccess = false;
    Memory securityDescriptorMemoryPointer = getSecurityDescriptorForFile(file.getAbsolutePath().replaceAll("/", "\\"));
    
    WinNT.HANDLEByReference openedAccessToken = null;
    WinNT.HANDLEByReference duplicatedToken = new WinNT.HANDLEByReference();
    try {
      openedAccessToken = new WinNT.HANDLEByReference();
      
      int desireAccess = 131086;
      if (!Advapi32.INSTANCE.OpenProcessToken(Kernel32.INSTANCE.GetCurrentProcess(), 131086, openedAccessToken)) {
        throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
      }
      
      if (!Advapi32.INSTANCE.DuplicateToken(openedAccessToken.getValue(), 2, duplicatedToken)) {
        throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
      }
      
      WinNT.GENERIC_MAPPING mapping = new WinNT.GENERIC_MAPPING();
      genericRead = new WinDef.DWORD(1179785L);
      genericWrite = new WinDef.DWORD(1179926L);
      genericExecute = new WinDef.DWORD(1179808L);
      genericAll = new WinDef.DWORD(2032127L);
      
      WinDef.DWORDByReference rights = new WinDef.DWORDByReference(new WinDef.DWORD(permissionToCheck.getCode()));
      Advapi32.INSTANCE.MapGenericMask(rights, mapping);
      
      WinNT.PRIVILEGE_SET privileges = new WinNT.PRIVILEGE_SET(1);
      PrivilegeCount = new WinDef.DWORD(0L);
      WinDef.DWORDByReference privilegeLength = new WinDef.DWORDByReference(new WinDef.DWORD(privileges.size()));
      
      WinDef.DWORDByReference grantedAccess = new WinDef.DWORDByReference();
      WinDef.BOOLByReference result = new WinDef.BOOLByReference();
      if (!Advapi32.INSTANCE.AccessCheck(securityDescriptorMemoryPointer, duplicatedToken.getValue(), rights.getValue(), mapping, privileges, privilegeLength, grantedAccess, result))
      {



        throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
      }
      
      hasAccess = result.getValue().booleanValue();
    }
    finally
    {
      if ((openedAccessToken != null) && (openedAccessToken.getValue() != null)) {
        Kernel32.INSTANCE.CloseHandle(openedAccessToken.getValue());
      }
      
      if ((duplicatedToken != null) && (duplicatedToken.getValue() != null)) {
        Kernel32.INSTANCE.CloseHandle(duplicatedToken.getValue());
      }
      
      if (securityDescriptorMemoryPointer != null) {
        securityDescriptorMemoryPointer.clear();
      }
    }
    
    return hasAccess;
  }
}
