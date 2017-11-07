package com.sun.jna.platform.win32;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.WString;
import com.sun.jna.win32.StdCallLibrary;
import java.util.Arrays;
import java.util.List;






public abstract interface LMAccess
  extends StdCallLibrary
{
  public static final int FILTER_TEMP_DUPLICATE_ACCOUNT = 1;
  public static final int FILTER_NORMAL_ACCOUNT = 2;
  public static final int FILTER_INTERDOMAIN_TRUST_ACCOUNT = 8;
  public static final int FILTER_WORKSTATION_TRUST_ACCOUNT = 16;
  public static final int FILTER_SERVER_TRUST_ACCOUNT = 32;
  public static final int USER_PRIV_MASK = 3;
  public static final int USER_PRIV_GUEST = 0;
  public static final int USER_PRIV_USER = 1;
  public static final int USER_PRIV_ADMIN = 2;
  
  public static class LOCALGROUP_INFO_0
    extends Structure
  {
    public WString lgrui0_name;
    
    public LOCALGROUP_INFO_0() {}
    
    public LOCALGROUP_INFO_0(Pointer memory)
    {
      super();
      read();
    }
    



    protected List getFieldOrder() { return Arrays.asList(new String[] { "lgrui0_name" }); }
  }
  
  public static class LOCALGROUP_INFO_1 extends Structure {
    public WString lgrui1_name;
    public WString lgrui1_comment;
    
    public LOCALGROUP_INFO_1() {}
    
    public LOCALGROUP_INFO_1(Pointer memory) {
      super();
      read();
    }
    

    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "lgrui1_name", "lgrui1_comment" });
    }
  }
  




  public static class USER_INFO_0
    extends Structure
  {
    public WString usri0_name;
    




    public USER_INFO_0() {}
    



    public USER_INFO_0(Pointer memory)
    {
      super();
      read();
    }
    





    protected List getFieldOrder() { return Arrays.asList(new String[] { "usri0_name" }); }
  }
  
  public static class USER_INFO_1 extends Structure {
    public WString usri1_name;
    public WString usri1_password;
    public int usri1_password_age;
    public int usri1_priv;
    public WString usri1_home_dir;
    public WString usri1_comment;
    public int usri1_flags;
    public WString usri1_script_path;
    
    public USER_INFO_1() {}
    
    public USER_INFO_1(Pointer memory) { super();
      read();
    }
    







































    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "usri1_name", "usri1_password", "usri1_password_age", "usri1_priv", "usri1_home_dir", "usri1_comment", "usri1_flags", "usri1_script_path" });
    }
  }
  

  public static class USER_INFO_23
    extends Structure
  {
    public WString usri23_name;
    
    public WString usri23_full_name;
    public WString usri23_comment;
    public int usri23_flags;
    public WinNT.PSID.ByReference usri23_user_sid;
    
    public USER_INFO_23() {}
    
    public USER_INFO_23(Pointer memory)
    {
      useMemory(memory);
      read();
    }
    


















































    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "usri23_name", "usri23_full_name", "usri23_comment", "usri23_flags", "usri23_user_sid" });
    }
  }
  
  public static class GROUP_USERS_INFO_0
    extends Structure
  {
    public WString grui0_name;
    
    public GROUP_USERS_INFO_0() {}
    
    public GROUP_USERS_INFO_0(Pointer memory)
    {
      super();
      read();
    }
    




    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "grui0_name" });
    }
  }
  
  public static class LOCALGROUP_USERS_INFO_0
    extends Structure
  {
    public WString lgrui0_name;
    
    public LOCALGROUP_USERS_INFO_0() {}
    
    public LOCALGROUP_USERS_INFO_0(Pointer memory)
    {
      super();
      read();
    }
    




    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "lgrui0_name" });
    }
  }
  

  public static class GROUP_INFO_0
    extends Structure
  {
    public WString grpi0_name;
    

    public GROUP_INFO_0() {}
    
    public GROUP_INFO_0(Pointer memory)
    {
      super();
      read();
    }
    





    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "grpi0_name" });
    }
  }
  
  public static class GROUP_INFO_1
    extends Structure
  {
    public WString grpi1_name;
    public WString grpi1_comment;
    
    public GROUP_INFO_1() {}
    
    public GROUP_INFO_1(Pointer memory)
    {
      super();
      read();
    }
    











    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "grpi1_name", "grpi1_comment" });
    }
  }
  
  public static class GROUP_INFO_2 extends Structure
  {
    public WString grpi2_name;
    public WString grpi2_comment;
    public int grpi2_group_id;
    public int grpi2_attributes;
    
    public GROUP_INFO_2() {}
    
    public GROUP_INFO_2(Pointer memory) {
      super();
      read();
    }
    





















    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "grpi2_name", "grpi2_comment", "grpi2_group_id", "grpi2_attributes" });
    }
  }
  
  public static class GROUP_INFO_3 extends Structure
  {
    public WString grpi3_name;
    public WString grpi3_comment;
    public WinNT.PSID.ByReference grpi3_group_sid;
    public int grpi3_attributes;
    
    public GROUP_INFO_3() {}
    
    public GROUP_INFO_3(Pointer memory) {
      super();
      read();
    }
    





















    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "grpi3_name", "grpi3_comment", "grpi3_group_sid", "grpi3_attributes" });
    }
  }
}
