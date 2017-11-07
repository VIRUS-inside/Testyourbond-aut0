package com.sun.jna.platform.win32;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.win32.StdCallLibrary;
import java.util.Arrays;
import java.util.List;





public abstract interface WinCrypt
  extends StdCallLibrary
{
  public static final int CRYPTPROTECT_PROMPT_ON_UNPROTECT = 1;
  public static final int CRYPTPROTECT_PROMPT_ON_PROTECT = 2;
  public static final int CRYPTPROTECT_PROMPT_RESERVED = 4;
  public static final int CRYPTPROTECT_PROMPT_STRONG = 8;
  public static final int CRYPTPROTECT_PROMPT_REQUIRE_STRONG = 16;
  public static final int CRYPTPROTECT_UI_FORBIDDEN = 1;
  public static final int CRYPTPROTECT_LOCAL_MACHINE = 4;
  public static final int CRYPTPROTECT_CRED_SYNC = 8;
  public static final int CRYPTPROTECT_AUDIT = 16;
  public static final int CRYPTPROTECT_NO_RECOVERY = 32;
  public static final int CRYPTPROTECT_VERIFY_PROTECTION = 64;
  public static final int CRYPTPROTECT_CRED_REGENERATE = 128;
  
  public static class DATA_BLOB
    extends Structure
  {
    public int cbData;
    public Pointer pbData;
    
    public DATA_BLOB() {}
    
    public DATA_BLOB(Pointer memory)
    {
      super();
      read();
    }
    
    public DATA_BLOB(byte[] data) {
      pbData = new Memory(data.length);
      pbData.write(0L, data, 0, data.length);
      cbData = data.length;
      allocateMemory();
    }
    
    public DATA_BLOB(String s) {
      this(Native.toByteArray(s));
    }
    








    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "cbData", "pbData" });
    }
    



    public byte[] getData()
    {
      return pbData == null ? null : pbData.getByteArray(0L, cbData);
    }
  }
  
  public static class CRYPTPROTECT_PROMPTSTRUCT extends Structure
  {
    public int cbSize;
    public int dwPromptFlags;
    public WinDef.HWND hwndApp;
    public String szPrompt;
    
    public CRYPTPROTECT_PROMPTSTRUCT() {}
    
    public CRYPTPROTECT_PROMPTSTRUCT(Pointer memory)
    {
      super();
      read();
    }
    
















    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "cbSize", "dwPromptFlags", "hwndApp", "szPrompt" });
    }
  }
}
