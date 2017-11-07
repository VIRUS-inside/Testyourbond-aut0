package com.sun.jna.platform.win32;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.ByReference;
import com.sun.jna.Union;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.StdCallLibrary.StdCallCallback;
import java.util.Arrays;
import java.util.List;

























public abstract interface WinRas
  extends StdCallLibrary
{
  public static final int ERROR_BUFFER_TOO_SMALL = 603;
  public static final int ERROR_CANNOT_FIND_PHONEBOOK_ENTRY = 623;
  public static final int MAX_PATH = 260;
  public static final int UNLEN = 256;
  public static final int PWLEN = 256;
  public static final int DNLEN = 15;
  public static final int RAS_MaxEntryName = 256;
  public static final int RAS_MaxPhoneNumber = 128;
  public static final int RAS_MaxCallbackNumber = 128;
  public static final int RAS_MaxDeviceType = 16;
  public static final int RAS_MaxDeviceName = 128;
  public static final int RAS_MaxDnsSuffix = 256;
  public static final int RAS_MaxAreaCode = 10;
  public static final int RAS_MaxX25Address = 200;
  public static final int RAS_MaxIpAddress = 15;
  public static final int RAS_MaxFacilities = 200;
  public static final int RAS_MaxUserData = 200;
  public static final int RAS_MaxPadType = 32;
  public static final int RASCS_Connected = 8192;
  public static final int RASCS_Disconnected = 8193;
  public static final int RASCM_UserName = 1;
  public static final int RASCM_Password = 2;
  public static final int RASCM_Domain = 4;
  public static final int RASTUNNELENDPOINT_IPv4 = 1;
  public static final int RASTUNNELENDPOINT_IPv6 = 2;
  public static final String RASDT_Modem = "modem";
  
  public static class RASEAPINFO
    extends Structure
  {
    public int dwSizeofEapInfo;
    public Pointer pbEapInfo;
    
    public RASEAPINFO() {}
    
    public RASEAPINFO(Pointer memory)
    {
      super();
      read();
    }
    
    public RASEAPINFO(byte[] data) {
      pbEapInfo = new Memory(data.length);
      pbEapInfo.write(0L, data, 0, data.length);
      dwSizeofEapInfo = data.length;
      allocateMemory();
    }
    
    public RASEAPINFO(String s) {
      this(Native.toByteArray(s));
    }
    










    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "dwSizeofEapInfo", "pbEapInfo" });
    }
    



    public byte[] getData()
    {
      return pbEapInfo == null ? null : pbEapInfo.getByteArray(0L, dwSizeofEapInfo);
    }
  }
  
  public static class RASDEVSPECIFICINFO
    extends Structure
  {
    public int dwSize;
    public Pointer pbDevSpecificInfo;
    
    public RASDEVSPECIFICINFO() {}
    
    public RASDEVSPECIFICINFO(Pointer memory)
    {
      super();
      read();
    }
    
    public RASDEVSPECIFICINFO(byte[] data) {
      pbDevSpecificInfo = new Memory(data.length);
      pbDevSpecificInfo.write(0L, data, 0, data.length);
      dwSize = data.length;
      allocateMemory();
    }
    
    public RASDEVSPECIFICINFO(String s) {
      this(Native.toByteArray(s));
    }
    










    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "dwSize", "pbDevSpecificInfo" });
    }
    





    public byte[] getData() { return pbDevSpecificInfo == null ? null : pbDevSpecificInfo.getByteArray(0L, dwSize); }
  }
  
  public static class RASDIALEXTENSIONS extends Structure {
    public int dwSize;
    public int dwfOptions;
    public WinDef.HWND hwndParent;
    public BaseTSD.ULONG_PTR reserved;
    public BaseTSD.ULONG_PTR reserved1;
    public WinRas.RASEAPINFO RasEapInfo;
    public WinDef.BOOL fSkipPppAuth;
    public WinRas.RASDEVSPECIFICINFO RasDevSpecificInfo;
    
    public RASDIALEXTENSIONS() { dwSize = size(); }
    
    public RASDIALEXTENSIONS(Pointer memory)
    {
      super();
      read();
    }
    










































    protected List getFieldOrder() { return Arrays.asList(new String[] { "dwSize", "dwfOptions", "hwndParent", "reserved", "reserved1", "RasEapInfo", "fSkipPppAuth", "RasDevSpecificInfo" }); }
    
    public static class ByReference extends WinRas.RASDIALEXTENSIONS implements Structure.ByReference {
      public ByReference() {}
    }
  }
  
  public static class RASDIALPARAMS extends Structure {
    public int dwSize;
    
    public RASDIALPARAMS() { dwSize = size(); }
    
    public RASDIALPARAMS(Pointer memory)
    {
      super();
      read();
    }
    











    public char[] szEntryName = new char['ā'];
    



    public char[] szPhoneNumber = new char[''];
    




    public char[] szCallbackNumber = new char[''];
    


    public char[] szUserName = new char['ā'];
    


    public char[] szPassword = new char['ā'];
    



    public char[] szDomain = new char[16];
    



    protected List getFieldOrder() { return Arrays.asList(new String[] { "dwSize", "szEntryName", "szPhoneNumber", "szCallbackNumber", "szUserName", "szPassword", "szDomain" }); }
    
    public static class ByReference extends WinRas.RASDIALPARAMS implements Structure.ByReference {
      public ByReference() {}
    }
  }
  
  public static class RASCONN extends Structure { public int dwSize;
    public WinNT.HANDLE hrasconn;
    
    public RASCONN() { dwSize = size(); }
    
    public RASCONN(Pointer memory)
    {
      super();
      read();
    }
    















    public char[] szEntryName = new char['ā'];
    


    public char[] szDeviceType = new char[17];
    


    public char[] szDeviceName = new char[''];
    


    public char[] szPhonebook = new char['Ą'];
    



    public int dwSubEntry;
    



    public Guid.GUID guidEntry;
    


    public int dwFlags;
    


    public WinNT.LUID luid;
    


    public Guid.GUID guidCorrelationId;
    


    protected List getFieldOrder() { return Arrays.asList(new String[] { "dwSize", "hrasconn", "szEntryName", "szDeviceType", "szDeviceName", "szPhonebook", "dwSubEntry", "guidEntry", "dwFlags", "luid", "guidCorrelationId" }); }
    
    public static class ByReference extends WinRas.RASCONN implements Structure.ByReference { public ByReference() {}
    }
  }
  
  public static class RAS_STATS extends Structure { public int dwSize;
    public int dwBytesXmited;
    public int dwBytesRcved;
    
    public RAS_STATS() { dwSize = size(); }
    
    public RAS_STATS(Pointer memory)
    {
      super();
      read();
    }
    




    public int dwFramesXmited;
    



    public int dwFramesRcved;
    



    public int dwCrcErr;
    



    public int dwTimeoutErr;
    



    public int dwAlignmentErr;
    



    public int dwHardwareOverrunErr;
    



    public int dwFramingErr;
    



    public int dwBufferOverrunErr;
    



    public int dwCompressionRatioIn;
    



    public int dwCompressionRatioOut;
    



    public int dwBps;
    


    public int dwConnectDuration;
    


    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "dwSize", "dwBytesXmited", "dwBytesRcved", "dwFramesXmited", "dwFramesRcved", "dwCrcErr", "dwTimeoutErr", "dwAlignmentErr", "dwHardwareOverrunErr", "dwFramingErr", "dwBufferOverrunErr", "dwCompressionRatioIn", "dwCompressionRatioOut", "dwBps", "dwConnectDuration" });
    }
  }
  

  public static class RASIPV4ADDR
    extends Structure
  {
    public RASIPV4ADDR() {}
    

    public RASIPV4ADDR(Pointer memory)
    {
      super();
      read();
    }
    



    public byte[] addr = new byte[8];
    

    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "addr" });
    }
  }
  

  public static class RASIPV6ADDR
    extends Structure
  {
    public RASIPV6ADDR() {}
    

    public RASIPV6ADDR(Pointer memory)
    {
      super();
      read();
    }
    



    public byte[] addr = new byte[16];
    

    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "addr" });
    }
  }
  
  public static class RASPPPIP extends Structure
  {
    public int dwSize;
    public int dwError;
    
    public RASPPPIP() {
      dwSize = size();
    }
    
    public RASPPPIP(Pointer memory) {
      super();
      read();
    }
    
















    public char[] szIpAddress = new char[16];
    





    public char[] szServerIpAddress = new char[16];
    



    public int dwOptions;
    


    public int dwServerOptions;
    


    protected List getFieldOrder() { return Arrays.asList(new String[] { "dwSize", "dwError", "szIpAddress", "szServerIpAddress", "dwOptions", "dwServerOptions" }); }
    
    public static class ByReference extends WinRas.RASPPPIP implements Structure.ByReference {
      public ByReference() {}
    }
  }
  
  public static class RASTUNNELENDPOINT extends Structure {
    public int dwType;
    public UNION u;
    
    public RASTUNNELENDPOINT() {}
    
    public RASTUNNELENDPOINT(Pointer memory) { super();
      read();
    }
    



















    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "dwType", "u" });
    }
    
    public void read()
    {
      super.read();
      
      switch (dwType) {
      case 1: 
        u.setType(WinRas.RASIPV4ADDR.class);
        break;
      case 2: 
        u.setType(WinRas.RASIPV6ADDR.class);
        break;
      default: 
        u.setType(WinRas.RASIPV4ADDR.class);
      }
      
      
      u.read();
    }
    
    public static class UNION extends Union { public WinRas.RASIPV4ADDR ipv4;
      public WinRas.RASIPV6ADDR ipv6;
      
      public UNION() {}
      
      public static class ByReference extends WinRas.RASTUNNELENDPOINT.UNION implements Structure.ByReference { public ByReference() {} } } }
  
  public static class RASCONNSTATUS extends Structure { public RASCONNSTATUS() { dwSize = size(); }
    
    public RASCONNSTATUS(Pointer memory)
    {
      super();
      read();
    }
    




    public int dwSize;
    



    public int rasconnstate;
    



    public int dwError;
    


    public char[] szDeviceType = new char[17];
    




    public char[] szDeviceName = new char[''];
    


    public char[] szPhoneNumber = new char[''];
    


    public WinRas.RASTUNNELENDPOINT localEndPoint;
    


    public WinRas.RASTUNNELENDPOINT remoteEndPoint;
    

    public int rasconnsubstate;
    


    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "dwSize", "rasconnstate", "dwError", "szDeviceType", "szDeviceName", "szPhoneNumber", "localEndPoint", "remoteEndPoint", "rasconnsubstate" });
    }
  }
  
  public static class RASCREDENTIALS extends Structure
  {
    public int dwSize;
    public int dwMask;
    
    public RASCREDENTIALS() {
      dwSize = size();
    }
    
    public RASCREDENTIALS(Pointer memory) {
      super();
      read();
    }
    














    public char[] szUserName = new char['ā'];
    


    public char[] szPassword = new char['ā'];
    


    public char[] szDomain = new char[16];
    

    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "dwSize", "dwMask", "szUserName", "szPassword", "szDomain" });
    }
    
    public static class ByReference extends WinRas.RASCREDENTIALS implements Structure.ByReference
    {
      public ByReference() {}
    }
  }
  
  public static class RASIPADDR extends Structure
  {
    public RASIPADDR() {}
    
    public RASIPADDR(Pointer memory) {
      super();
      read();
    }
    
    public byte[] addr = new byte[4];
    

    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "addr" });
    }
  }
  
  public static class RASENTRY extends Structure {
    public int dwSize;
    public int dwfOptions;
    public int dwCountryID;
    public int dwCountryCode;
    
    public RASENTRY() {
      dwSize = size();
    }
    
    public RASENTRY(Pointer memory) {
      super();
      read();
    }
    






























    public char[] szAreaCode = new char[11];
    



    public char[] szLocalPhoneNumber = new char[''];
    



    public int dwAlternateOffset;
    



    public WinRas.RASIPADDR ipaddr;
    



    public WinRas.RASIPADDR ipaddrDns;
    



    public WinRas.RASIPADDR ipaddrDnsAlt;
    



    public WinRas.RASIPADDR ipaddrWins;
    



    public WinRas.RASIPADDR ipaddrWinsAlt;
    



    public int dwFrameSize;
    



    public int dwfNetProtocols;
    



    public int dwFramingProtocol;
    



    public char[] szScript = new char['Ą'];
    



    public char[] szAutodialDll = new char['Ą'];
    


    public char[] szAutodialFunc = new char['Ą'];
    


    public char[] szDeviceType = new char[17];
    



    public char[] szDeviceName = new char[''];
    



    public char[] szX25PadType = new char[33];
    



    public char[] szX25Address = new char['É'];
    



    public char[] szX25Facilities = new char['É'];
    



    public char[] szX25UserData = new char['É'];
    



    public int dwChannels;
    



    public int dwReserved1;
    



    public int dwReserved2;
    



    public int dwSubEntries;
    



    public int dwDialMode;
    



    public int dwDialExtraPercent;
    



    public int dwDialExtraSampleSeconds;
    



    public int dwHangUpExtraPercent;
    



    public int dwHangUpExtraSampleSeconds;
    



    public int dwIdleDisconnectSeconds;
    



    public int dwType;
    



    public int dwEncryptionType;
    



    public int dwCustomAuthKey;
    



    public Guid.GUID guidId;
    


    public char[] szCustomDialDll = new char['Ą'];
    



    public int dwVpnStrategy;
    



    public int dwfOptions2;
    


    public int dwfOptions3;
    


    public char[] szDnsSuffix = new char['Ā'];
    



    public int dwTcpWindowSize;
    



    public char[] szPrerequisitePbk = new char['Ą'];
    





    public char[] szPrerequisiteEntry = new char['ā'];
    


    public int dwRedialCount;
    


    public int dwRedialPause;
    


    public WinRas.RASIPV6ADDR ipv6addrDns;
    


    public WinRas.RASIPV6ADDR ipv6addrDnsAlt;
    


    public int dwIPv4InterfaceMetric;
    


    public int dwIPv6InterfaceMetric;
    


    public WinRas.RASIPV6ADDR ipv6addr;
    


    public int dwIPv6PrefixLength;
    


    public int dwNetworkOutageTime;
    



    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "dwSize", "dwfOptions", "dwCountryID", "dwCountryCode", "szAreaCode", "szLocalPhoneNumber", "dwAlternateOffset", "ipaddr", "ipaddrDns", "ipaddrDnsAlt", "ipaddrWins", "ipaddrWinsAlt", "dwFrameSize", "dwfNetProtocols", "dwFramingProtocol", "szScript", "szAutodialDll", "szAutodialFunc", "szDeviceType", "szDeviceName", "szX25PadType", "szX25Address", "szX25Facilities", "szX25UserData", "dwChannels", "dwReserved1", "dwReserved2", "dwSubEntries", "dwDialMode", "dwDialExtraPercent", "dwDialExtraSampleSeconds", "dwHangUpExtraPercent", "dwHangUpExtraSampleSeconds", "dwIdleDisconnectSeconds", "dwType", "dwEncryptionType", "dwCustomAuthKey", "guidId", "szCustomDialDll", "dwVpnStrategy", "dwfOptions2", "dwfOptions3", "szDnsSuffix", "dwTcpWindowSize", "szPrerequisitePbk", "szPrerequisiteEntry", "dwRedialCount", "dwRedialPause", "ipv6addrDns", "ipv6addrDnsAlt", "dwIPv4InterfaceMetric", "dwIPv6InterfaceMetric", "ipv6addr", "dwIPv6PrefixLength", "dwNetworkOutageTime" });
    }
    
    public static class ByReference
      extends WinRas.RASENTRY
      implements Structure.ByReference
    {
      public ByReference() {}
    }
  }
  
  public static abstract interface RasDialFunc2
    extends StdCallLibrary.StdCallCallback
  {
    public abstract int dialNotification(int paramInt1, int paramInt2, WinNT.HANDLE paramHANDLE, int paramInt3, int paramInt4, int paramInt5, int paramInt6);
  }
}
