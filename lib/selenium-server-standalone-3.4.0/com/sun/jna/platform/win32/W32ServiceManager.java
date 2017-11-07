package com.sun.jna.platform.win32;



















public class W32ServiceManager
{
  Winsvc.SC_HANDLE _handle = null;
  String _machineName = null;
  String _databaseName = null;
  
  public W32ServiceManager() {}
  
  public W32ServiceManager(String machineName, String databaseName)
  {
    _machineName = machineName;
    _databaseName = databaseName;
  }
  




  public void open(int permissions)
  {
    close();
    
    _handle = Advapi32.INSTANCE.OpenSCManager(_machineName, _databaseName, permissions);
    

    if (_handle == null) {
      throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
    }
  }
  


  public void close()
  {
    if (_handle != null) {
      if (!Advapi32.INSTANCE.CloseServiceHandle(_handle)) {
        throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
      }
      _handle = null;
    }
  }
  








  public W32Service openService(String serviceName, int permissions)
  {
    Winsvc.SC_HANDLE serviceHandle = Advapi32.INSTANCE.OpenService(_handle, serviceName, permissions);
    

    if (serviceHandle == null) {
      throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
    }
    
    return new W32Service(serviceHandle);
  }
  




  public Winsvc.SC_HANDLE getHandle()
  {
    return _handle;
  }
}
