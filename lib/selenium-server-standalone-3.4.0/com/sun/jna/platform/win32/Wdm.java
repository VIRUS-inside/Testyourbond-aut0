package com.sun.jna.platform.win32;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.win32.StdCallLibrary;
import java.util.Arrays;
import java.util.List;




public abstract interface Wdm
  extends StdCallLibrary
{
  public static abstract class KEY_INFORMATION_CLASS
  {
    public static final int KeyBasicInformation = 0;
    public static final int KeyNodeInformation = 1;
    public static final int KeyFullInformation = 2;
    public static final int KeyNameInformation = 3;
    public static final int KeyCachedInformation = 4;
    public static final int KeyVirtualizationInformation = 5;
    
    public KEY_INFORMATION_CLASS() {}
  }
  
  public static class KEY_BASIC_INFORMATION
    extends Structure
  {
    public long LastWriteTime;
    public int TitleIndex;
    public int NameLength;
    public char[] Name;
    
    public KEY_BASIC_INFORMATION() {}
    
    public KEY_BASIC_INFORMATION(int size)
    {
      NameLength = (size - 16);
      Name = new char[NameLength];
      allocateMemory();
    }
    
    public KEY_BASIC_INFORMATION(Pointer memory) {
      super();
      read();
    }
    

















    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "LastWriteTime", "TitleIndex", "NameLength", "Name" });
    }
    


    public String getName()
    {
      return Native.toString(Name);
    }
    
    public void read() {
      super.read();
      Name = new char[NameLength / 2];
      readField("Name");
    }
  }
}
