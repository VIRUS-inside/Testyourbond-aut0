package com.sun.jna.platform.win32;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.ByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;
import java.util.Arrays;
import java.util.List;













public abstract interface SetupApi
  extends StdCallLibrary
{
  public static final SetupApi INSTANCE = (SetupApi)Native.loadLibrary("setupapi", SetupApi.class, W32APIOptions.DEFAULT_OPTIONS);
  




  public static final Guid.GUID GUID_DEVINTERFACE_DISK = new Guid.GUID(new byte[] { 7, 99, -11, 83, -65, -74, -48, 17, -108, -14, 0, -96, -55, 30, -5, -117 });
  










  public static final int DIGCF_DEFAULT = 1;
  










  public static final int DIGCF_PRESENT = 2;
  










  public static final int DIGCF_ALLCLASSES = 4;
  










  public static final int DIGCF_PROFILE = 8;
  









  public static final int DIGCF_DEVICEINTERFACE = 16;
  









  public static final int SPDRP_REMOVAL_POLICY = 31;
  









  public static final int CM_DEVCAP_REMOVABLE = 4;
  










  public abstract WinNT.HANDLE SetupDiGetClassDevs(Guid.GUID paramGUID, Pointer paramPointer1, Pointer paramPointer2, int paramInt);
  










  public abstract boolean SetupDiDestroyDeviceInfoList(WinNT.HANDLE paramHANDLE);
  










  public abstract boolean SetupDiEnumDeviceInterfaces(WinNT.HANDLE paramHANDLE, Pointer paramPointer, Guid.GUID paramGUID, int paramInt, SP_DEVICE_INTERFACE_DATA paramSP_DEVICE_INTERFACE_DATA);
  










  public abstract boolean SetupDiGetDeviceInterfaceDetail(WinNT.HANDLE paramHANDLE, SP_DEVICE_INTERFACE_DATA paramSP_DEVICE_INTERFACE_DATA, Pointer paramPointer, int paramInt, IntByReference paramIntByReference, SP_DEVINFO_DATA paramSP_DEVINFO_DATA);
  










  public abstract boolean SetupDiGetDeviceRegistryProperty(WinNT.HANDLE paramHANDLE, SP_DEVINFO_DATA paramSP_DEVINFO_DATA, int paramInt1, IntByReference paramIntByReference1, Pointer paramPointer, int paramInt2, IntByReference paramIntByReference2);
  










  public static class SP_DEVICE_INTERFACE_DATA
    extends Structure
  {
    public int cbSize;
    









    public Guid.GUID InterfaceClassGuid;
    









    public int Flags;
    









    public Pointer Reserved;
    










    public static class ByReference
      extends SetupApi.SP_DEVINFO_DATA
      implements Structure.ByReference
    {
      public ByReference() {}
      










      public ByReference(Pointer memory)
      {
        super();
      }
    }
    
    public SP_DEVICE_INTERFACE_DATA() {
      cbSize = size();
    }
    
    public SP_DEVICE_INTERFACE_DATA(Pointer memory) {
      super();
      read();
    }
    






















    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "cbSize", "InterfaceClassGuid", "Flags", "Reserved" });
    }
  }
  
  public static class SP_DEVINFO_DATA extends Structure {
    public int cbSize;
    public Guid.GUID InterfaceClassGuid;
    public int DevInst;
    public Pointer Reserved;
    
    public static class ByReference extends SetupApi.SP_DEVINFO_DATA implements Structure.ByReference {
      public ByReference() {}
      
      public ByReference(Pointer memory) {
        super();
      }
    }
    
    public SP_DEVINFO_DATA() {
      cbSize = size();
    }
    
    public SP_DEVINFO_DATA(Pointer memory) {
      super();
      read();
    }
    























    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "cbSize", "InterfaceClassGuid", "DevInst", "Reserved" });
    }
  }
}
