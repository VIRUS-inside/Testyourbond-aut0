package com.sun.jna.platform.win32;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.ByReference;
import com.sun.jna.win32.StdCallLibrary;
import java.util.Arrays;
import java.util.List;

















public abstract interface Tlhelp32
  extends StdCallLibrary
{
  public static final WinDef.DWORD TH32CS_SNAPHEAPLIST = new WinDef.DWORD(1L);
  



  public static final WinDef.DWORD TH32CS_SNAPPROCESS = new WinDef.DWORD(2L);
  



  public static final WinDef.DWORD TH32CS_SNAPTHREAD = new WinDef.DWORD(4L);
  




  public static final WinDef.DWORD TH32CS_SNAPMODULE = new WinDef.DWORD(8L);
  





  public static final WinDef.DWORD TH32CS_SNAPMODULE32 = new WinDef.DWORD(16L);
  



  public static final WinDef.DWORD TH32CS_SNAPALL = new WinDef.DWORD(TH32CS_SNAPHEAPLIST.intValue() | TH32CS_SNAPPROCESS.intValue() | TH32CS_SNAPTHREAD.intValue() | TH32CS_SNAPMODULE.intValue());
  




  public static final WinDef.DWORD TH32CS_INHERIT = new WinDef.DWORD(-2147483648L);
  
  public static class PROCESSENTRY32 extends Structure {
    public WinDef.DWORD dwSize;
    public WinDef.DWORD cntUsage;
    public WinDef.DWORD th32ProcessID;
    public BaseTSD.ULONG_PTR th32DefaultHeapID;
    
    public static class ByReference extends Tlhelp32.PROCESSENTRY32 implements Structure.ByReference {
      public ByReference() {}
      
      public ByReference(Pointer memory) {
        super();
      }
    }
    
    public PROCESSENTRY32() {
      dwSize = new WinDef.DWORD(size());
    }
    
    public PROCESSENTRY32(Pointer memory) {
      super();
      read();
    }
    








    public WinDef.DWORD th32ModuleID;
    







    public WinDef.DWORD cntThreads;
    







    public WinDef.DWORD th32ParentProcessID;
    







    public WinDef.LONG pcPriClassBase;
    







    public WinDef.DWORD dwFlags;
    






    public char[] szExeFile = new char['Ą'];
    
    protected List getFieldOrder() {
      return Arrays.asList(new String[] { "dwSize", "cntUsage", "th32ProcessID", "th32DefaultHeapID", "th32ModuleID", "cntThreads", "th32ParentProcessID", "pcPriClassBase", "dwFlags", "szExeFile" });
    }
  }
}
