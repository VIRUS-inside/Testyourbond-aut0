package com.sun.jna.platform.win32;

import com.sun.jna.ptr.IntByReference;
import java.util.HashMap;
import java.util.Map;



























public abstract class Rasapi32Util
{
  private static final int RASP_PppIp = 32801;
  private static Object phoneBookMutex = new Object();
  
  public static final Map CONNECTION_STATE_TEXT = new HashMap();
  
  static {
    CONNECTION_STATE_TEXT.put(Integer.valueOf(0), "Opening the port...");
    CONNECTION_STATE_TEXT.put(Integer.valueOf(1), "Port has been opened successfully");
    CONNECTION_STATE_TEXT.put(Integer.valueOf(2), "Connecting to the device...");
    CONNECTION_STATE_TEXT.put(Integer.valueOf(3), "The device has connected successfully.");
    CONNECTION_STATE_TEXT.put(Integer.valueOf(4), "All devices in the device chain have successfully connected.");
    CONNECTION_STATE_TEXT.put(Integer.valueOf(5), "Verifying the user name and password...");
    CONNECTION_STATE_TEXT.put(Integer.valueOf(6), "An authentication event has occurred.");
    CONNECTION_STATE_TEXT.put(Integer.valueOf(7), "Requested another validation attempt with a new user.");
    CONNECTION_STATE_TEXT.put(Integer.valueOf(8), "Server has requested a callback number.");
    CONNECTION_STATE_TEXT.put(Integer.valueOf(9), "The client has requested to change the password");
    CONNECTION_STATE_TEXT.put(Integer.valueOf(10), "Registering your computer on the network...");
    CONNECTION_STATE_TEXT.put(Integer.valueOf(11), "The link-speed calculation phase is starting...");
    CONNECTION_STATE_TEXT.put(Integer.valueOf(12), "An authentication request is being acknowledged.");
    CONNECTION_STATE_TEXT.put(Integer.valueOf(13), "Reauthentication (after callback) is starting.");
    CONNECTION_STATE_TEXT.put(Integer.valueOf(14), "The client has successfully completed authentication.");
    CONNECTION_STATE_TEXT.put(Integer.valueOf(15), "The line is about to disconnect for callback.");
    CONNECTION_STATE_TEXT.put(Integer.valueOf(16), "Delaying to give the modem time to reset for callback.");
    CONNECTION_STATE_TEXT.put(Integer.valueOf(17), "Waiting for an incoming call from server.");
    CONNECTION_STATE_TEXT.put(Integer.valueOf(18), "Projection result information is available.");
    CONNECTION_STATE_TEXT.put(Integer.valueOf(19), "User authentication is being initiated or retried.");
    CONNECTION_STATE_TEXT.put(Integer.valueOf(20), "Client has been called back and is about to resume authentication.");
    CONNECTION_STATE_TEXT.put(Integer.valueOf(21), "Logging on to the network...");
    CONNECTION_STATE_TEXT.put(Integer.valueOf(22), "Subentry has been connected");
    CONNECTION_STATE_TEXT.put(Integer.valueOf(23), "Subentry has been disconnected");
    CONNECTION_STATE_TEXT.put(Integer.valueOf(4096), "Terminal state supported by RASPHONE.EXE.");
    CONNECTION_STATE_TEXT.put(Integer.valueOf(4097), "Retry authentication state supported by RASPHONE.EXE.");
    CONNECTION_STATE_TEXT.put(Integer.valueOf(4098), "Callback state supported by RASPHONE.EXE.");
    CONNECTION_STATE_TEXT.put(Integer.valueOf(4099), "Change password state supported by RASPHONE.EXE.");
    CONNECTION_STATE_TEXT.put(Integer.valueOf(4100), "Displaying authentication UI");
    CONNECTION_STATE_TEXT.put(Integer.valueOf(8192), "Connected to remote server successfully");
    CONNECTION_STATE_TEXT.put(Integer.valueOf(8193), "Disconnected");
  }
  



  public static class Ras32Exception
    extends RuntimeException
  {
    private static final long serialVersionUID = 1L;
    

    private final int code;
    


    public int getCode()
    {
      return code;
    }
    




    public Ras32Exception(int code)
    {
      super();
      this.code = code;
    }
  }
  




  public static String getRasErrorString(int code)
  {
    char[] msg = new char['Ѐ'];
    int err = Rasapi32.INSTANCE.RasGetErrorString(code, msg, msg.length);
    if (err != 0) return "Unknown error " + code;
    int len = 0;
    while ((len < msg.length) && (msg[len] != 0)) len++;
    return new String(msg, 0, len);
  }
  




  public static String getRasConnectionStatusText(int connStatus)
  {
    if (!CONNECTION_STATE_TEXT.containsKey(Integer.valueOf(connStatus))) return Integer.toString(connStatus);
    return (String)CONNECTION_STATE_TEXT.get(Integer.valueOf(connStatus));
  }
  





  public static WinNT.HANDLE getRasConnection(String connName)
    throws Rasapi32Util.Ras32Exception
  {
    IntByReference lpcb = new IntByReference(0);
    IntByReference lpcConnections = new IntByReference();
    int err = Rasapi32.INSTANCE.RasEnumConnections(null, lpcb, lpcConnections);
    if ((err != 0) && (err != 603)) throw new Ras32Exception(err);
    if (lpcb.getValue() == 0) { return null;
    }
    
    WinRas.RASCONN[] connections = new WinRas.RASCONN[lpcConnections.getValue()];
    for (int i = 0; i < lpcConnections.getValue(); i++) connections[i] = new WinRas.RASCONN();
    lpcb = new IntByReference(0dwSize * lpcConnections.getValue());
    err = Rasapi32.INSTANCE.RasEnumConnections(connections, lpcb, lpcConnections);
    if (err != 0) { throw new Ras32Exception(err);
    }
    
    for (int i = 0; i < lpcConnections.getValue(); i++) {
      if (new String(szEntryName).equals(connName)) return hrasconn;
    }
    return null;
  }
  



  public static void hangupRasConnection(String connName)
    throws Rasapi32Util.Ras32Exception
  {
    WinNT.HANDLE hrasConn = getRasConnection(connName);
    if (hrasConn == null) return;
    int err = Rasapi32.INSTANCE.RasHangUp(hrasConn);
    if (err != 0) { throw new Ras32Exception(err);
    }
  }
  


  public static void hangupRasConnection(WinNT.HANDLE hrasConn)
    throws Rasapi32Util.Ras32Exception
  {
    if (hrasConn == null) return;
    int err = Rasapi32.INSTANCE.RasHangUp(hrasConn);
    if (err != 0) { throw new Ras32Exception(err);
    }
  }
  



  public static WinRas.RASPPPIP getIPProjection(WinNT.HANDLE hrasConn)
    throws Rasapi32Util.Ras32Exception
  {
    WinRas.RASPPPIP pppIpProjection = new WinRas.RASPPPIP();
    IntByReference lpcb = new IntByReference(pppIpProjection.size());
    pppIpProjection.write();
    int err = Rasapi32.INSTANCE.RasGetProjectionInfo(hrasConn, 32801, pppIpProjection.getPointer(), lpcb);
    if (err != 0) throw new Ras32Exception(err);
    pppIpProjection.read();
    return pppIpProjection;
  }
  




  public static WinRas.RASENTRY.ByReference getPhoneBookEntry(String entryName)
    throws Rasapi32Util.Ras32Exception
  {
    synchronized (phoneBookMutex) {
      WinRas.RASENTRY.ByReference rasEntry = new WinRas.RASENTRY.ByReference();
      IntByReference lpdwEntryInfoSize = new IntByReference(rasEntry.size());
      int err = Rasapi32.INSTANCE.RasGetEntryProperties(null, entryName, rasEntry, lpdwEntryInfoSize, null, null);
      if (err != 0) throw new Ras32Exception(err);
      return rasEntry;
    }
  }
  




  public static void setPhoneBookEntry(String entryName, WinRas.RASENTRY.ByReference rasEntry)
    throws Rasapi32Util.Ras32Exception
  {
    synchronized (phoneBookMutex) {
      int err = Rasapi32.INSTANCE.RasSetEntryProperties(null, entryName, rasEntry, rasEntry.size(), null, 0);
      if (err != 0) { throw new Ras32Exception(err);
      }
    }
  }
  



  public static WinRas.RASDIALPARAMS getPhoneBookDialingParams(String entryName)
    throws Rasapi32Util.Ras32Exception
  {
    synchronized (phoneBookMutex) {
      WinRas.RASDIALPARAMS.ByReference rasDialParams = new WinRas.RASDIALPARAMS.ByReference();
      System.arraycopy(szEntryName, 0, entryName.toCharArray(), 0, entryName.length());
      WinDef.BOOLByReference lpfPassword = new WinDef.BOOLByReference();
      int err = Rasapi32.INSTANCE.RasGetEntryDialParams(null, rasDialParams, lpfPassword);
      if (err != 0) throw new Ras32Exception(err);
      return rasDialParams;
    }
  }
  




  public static WinNT.HANDLE dialEntry(String entryName)
    throws Rasapi32Util.Ras32Exception
  {
    WinRas.RASCREDENTIALS.ByReference credentials = new WinRas.RASCREDENTIALS.ByReference();
    synchronized (phoneBookMutex) {
      dwMask = 7;
      int err = Rasapi32.INSTANCE.RasGetCredentials(null, entryName, credentials);
      if (err != 0) { throw new Ras32Exception(err);
      }
    }
    
    WinRas.RASDIALPARAMS.ByReference rasDialParams = new WinRas.RASDIALPARAMS.ByReference();
    System.arraycopy(entryName.toCharArray(), 0, szEntryName, 0, entryName.length());
    System.arraycopy(szUserName, 0, szUserName, 0, szUserName.length);
    System.arraycopy(szPassword, 0, szPassword, 0, szPassword.length);
    System.arraycopy(szDomain, 0, szDomain, 0, szDomain.length);
    

    WinNT.HANDLEByReference hrasConn = new WinNT.HANDLEByReference();
    int err = Rasapi32.INSTANCE.RasDial(null, null, rasDialParams, 0, null, hrasConn);
    if (err != 0) {
      if (hrasConn.getValue() != null) Rasapi32.INSTANCE.RasHangUp(hrasConn.getValue());
      throw new Ras32Exception(err);
    }
    return hrasConn.getValue();
  }
  





  public static WinNT.HANDLE dialEntry(String entryName, WinRas.RasDialFunc2 func2)
    throws Rasapi32Util.Ras32Exception
  {
    WinRas.RASCREDENTIALS.ByReference credentials = new WinRas.RASCREDENTIALS.ByReference();
    synchronized (phoneBookMutex) {
      dwMask = 7;
      int err = Rasapi32.INSTANCE.RasGetCredentials(null, entryName, credentials);
      if (err != 0) { throw new Ras32Exception(err);
      }
    }
    
    WinRas.RASDIALPARAMS.ByReference rasDialParams = new WinRas.RASDIALPARAMS.ByReference();
    System.arraycopy(entryName.toCharArray(), 0, szEntryName, 0, entryName.length());
    System.arraycopy(szUserName, 0, szUserName, 0, szUserName.length);
    System.arraycopy(szPassword, 0, szPassword, 0, szPassword.length);
    System.arraycopy(szDomain, 0, szDomain, 0, szDomain.length);
    

    WinNT.HANDLEByReference hrasConn = new WinNT.HANDLEByReference();
    int err = Rasapi32.INSTANCE.RasDial(null, null, rasDialParams, 2, func2, hrasConn);
    if (err != 0) {
      if (hrasConn.getValue() != null) Rasapi32.INSTANCE.RasHangUp(hrasConn.getValue());
      throw new Ras32Exception(err);
    }
    return hrasConn.getValue();
  }
  
  public Rasapi32Util() {}
}
