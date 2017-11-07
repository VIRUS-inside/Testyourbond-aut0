package com.sun.jna.platform.win32.COM;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.Advapi32;
import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.Advapi32Util.EnumKey;
import com.sun.jna.platform.win32.Advapi32Util.InfoKey;
import com.sun.jna.platform.win32.Kernel32Util;
import com.sun.jna.platform.win32.OaIdl.EXCEPINFO;
import com.sun.jna.platform.win32.WinNT.HRESULT;
import com.sun.jna.platform.win32.WinReg;
import com.sun.jna.platform.win32.WinReg.HKEYByReference;
import com.sun.jna.ptr.IntByReference;
import java.util.ArrayList;



























public abstract class COMUtils
{
  public static final int S_OK = 0;
  
  public COMUtils() {}
  
  public static boolean SUCCEEDED(WinNT.HRESULT hr)
  {
    return SUCCEEDED(hr.intValue());
  }
  






  public static boolean SUCCEEDED(int hr)
  {
    if (hr == 0) {
      return true;
    }
    return false;
  }
  






  public static boolean FAILED(WinNT.HRESULT hr)
  {
    return FAILED(hr.intValue());
  }
  






  public static boolean FAILED(int hr)
  {
    if (hr != 0) {
      return true;
    }
    return false;
  }
  





  public static void checkRC(WinNT.HRESULT hr)
  {
    checkRC(hr, null, null);
  }
  










  public static void checkRC(WinNT.HRESULT hr, OaIdl.EXCEPINFO pExcepInfo, IntByReference puArgErr)
  {
    if (FAILED(hr)) {
      String formatMessageFromHR = Kernel32Util.formatMessage(hr);
      throw new COMException(formatMessageFromHR, pExcepInfo, puArgErr);
    }
  }
  




  public static ArrayList<COMInfo> getAllCOMInfoOnSystem()
  {
    WinReg.HKEYByReference phkResult = new WinReg.HKEYByReference();
    WinReg.HKEYByReference phkResult2 = new WinReg.HKEYByReference();
    
    ArrayList<COMInfo> comInfos = new ArrayList();
    
    try
    {
      phkResult = Advapi32Util.registryGetKey(WinReg.HKEY_CLASSES_ROOT, "CLSID", 2031679);
      

      Advapi32Util.InfoKey infoKey = Advapi32Util.registryQueryInfoKey(phkResult.getValue(), 2031679);
      

      for (int i = 0; i < lpcSubKeys.getValue(); i++) {
        Advapi32Util.EnumKey enumKey = Advapi32Util.registryRegEnumKey(phkResult.getValue(), i);
        
        String subKey = Native.toString(lpName);
        
        COMInfo comInfo = new COMInfo(subKey);
        
        phkResult2 = Advapi32Util.registryGetKey(phkResult.getValue(), subKey, 2031679);
        
        Advapi32Util.InfoKey infoKey2 = Advapi32Util.registryQueryInfoKey(phkResult2.getValue(), 2031679);
        

        for (int y = 0; y < lpcSubKeys.getValue(); y++) {
          Advapi32Util.EnumKey enumKey2 = Advapi32Util.registryRegEnumKey(phkResult2.getValue(), y);
          
          String subKey2 = Native.toString(lpName);
          
          if (subKey2.equals("InprocHandler32")) {
            inprocHandler32 = ((String)Advapi32Util.registryGetValue(phkResult2.getValue(), subKey2, null));

          }
          else if (subKey2.equals("InprocServer32")) {
            inprocServer32 = ((String)Advapi32Util.registryGetValue(phkResult2.getValue(), subKey2, null));

          }
          else if (subKey2.equals("LocalServer32")) {
            localServer32 = ((String)Advapi32Util.registryGetValue(phkResult2.getValue(), subKey2, null));

          }
          else if (subKey2.equals("ProgID")) {
            progID = ((String)Advapi32Util.registryGetValue(phkResult2.getValue(), subKey2, null));

          }
          else if (subKey2.equals("TypeLib")) {
            typeLib = ((String)Advapi32Util.registryGetValue(phkResult2.getValue(), subKey2, null));
          }
        }
        


        Advapi32.INSTANCE.RegCloseKey(phkResult2.getValue());
        comInfos.add(comInfo);
      }
    } finally {
      Advapi32.INSTANCE.RegCloseKey(phkResult.getValue());
      Advapi32.INSTANCE.RegCloseKey(phkResult2.getValue());
    }
    
    return comInfos;
  }
  




  public static class COMInfo
  {
    public String clsid;
    


    public String inprocHandler32;
    


    public String inprocServer32;
    


    public String localServer32;
    


    public String progID;
    


    public String typeLib;
    



    public COMInfo() {}
    



    public COMInfo(String clsid)
    {
      this.clsid = clsid;
    }
  }
}
