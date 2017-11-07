package com.sun.jna.platform.win32;

import com.sun.jna.Pointer;
import com.sun.jna.WString;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import java.util.ArrayList;
















































































public abstract class Netapi32Util
{
  public Netapi32Util() {}
  
  public static String getDCName()
  {
    return getDCName(null, null);
  }
  









  public static String getDCName(String serverName, String domainName)
  {
    PointerByReference bufptr = new PointerByReference();
    try {
      int rc = Netapi32.INSTANCE.NetGetDCName(domainName, serverName, bufptr);
      if (0 != rc) {
        throw new Win32Exception(rc);
      }
      return bufptr.getValue().getWideString(0L);
    } finally {
      if (0 != Netapi32.INSTANCE.NetApiBufferFree(bufptr.getValue())) {
        throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
      }
    }
  }
  



  public static int getJoinStatus()
  {
    return getJoinStatus(null);
  }
  




  public static int getJoinStatus(String computerName)
  {
    PointerByReference lpNameBuffer = new PointerByReference();
    IntByReference bufferType = new IntByReference();
    try
    {
      int rc = Netapi32.INSTANCE.NetGetJoinInformation(computerName, lpNameBuffer, bufferType);
      if (0 != rc)
        throw new Win32Exception(rc);
      int rc;
      return bufferType.getValue();
    } finally {
      if (lpNameBuffer.getPointer() != null) {
        int rc = Netapi32.INSTANCE.NetApiBufferFree(lpNameBuffer.getValue());
        if (0 != rc) {
          throw new Win32Exception(rc);
        }
      }
    }
  }
  




  public static String getDomainName(String computerName)
  {
    PointerByReference lpNameBuffer = new PointerByReference();
    IntByReference bufferType = new IntByReference();
    try
    {
      int rc = Netapi32.INSTANCE.NetGetJoinInformation(computerName, lpNameBuffer, bufferType);
      if (0 != rc) {
        throw new Win32Exception(rc);
      }
      int rc;
      return lpNameBuffer.getValue().getWideString(0L);
    } finally {
      if (lpNameBuffer.getPointer() != null) {
        int rc = Netapi32.INSTANCE.NetApiBufferFree(lpNameBuffer.getValue());
        if (0 != rc) {
          throw new Win32Exception(rc);
        }
      }
    }
  }
  



  public static LocalGroup[] getLocalGroups()
  {
    return getLocalGroups(null);
  }
  




  public static LocalGroup[] getLocalGroups(String serverName)
  {
    PointerByReference bufptr = new PointerByReference();
    IntByReference entriesRead = new IntByReference();
    IntByReference totalEntries = new IntByReference();
    try {
      int rc = Netapi32.INSTANCE.NetLocalGroupEnum(serverName, 1, bufptr, -1, entriesRead, totalEntries, null);
      if ((0 != rc) || (bufptr.getValue() == Pointer.NULL)) {
        throw new Win32Exception(rc);
      }
      LMAccess.LOCALGROUP_INFO_1 group = new LMAccess.LOCALGROUP_INFO_1(bufptr.getValue());
      LMAccess.LOCALGROUP_INFO_1[] groups = (LMAccess.LOCALGROUP_INFO_1[])group.toArray(entriesRead.getValue());
      
      ArrayList<LocalGroup> result = new ArrayList();
      for (LMAccess.LOCALGROUP_INFO_1 lgpi : groups) {
        LocalGroup lgp = new LocalGroup();
        if (lgrui1_name != null) {
          name = lgrui1_name.toString();
        }
        if (lgrui1_comment != null) {
          comment = lgrui1_comment.toString();
        }
        result.add(lgp); }
      int rc;
      return (LocalGroup[])result.toArray(new LocalGroup[0]);
    } finally {
      if (bufptr.getValue() != Pointer.NULL) {
        int rc = Netapi32.INSTANCE.NetApiBufferFree(bufptr.getValue());
        if (0 != rc) {
          throw new Win32Exception(rc);
        }
      }
    }
  }
  



  public static Group[] getGlobalGroups()
  {
    return getGlobalGroups(null);
  }
  




  public static Group[] getGlobalGroups(String serverName)
  {
    PointerByReference bufptr = new PointerByReference();
    IntByReference entriesRead = new IntByReference();
    IntByReference totalEntries = new IntByReference();
    try {
      int rc = Netapi32.INSTANCE.NetGroupEnum(serverName, 1, bufptr, -1, entriesRead, totalEntries, null);
      

      if ((0 != rc) || (bufptr.getValue() == Pointer.NULL)) {
        throw new Win32Exception(rc);
      }
      LMAccess.GROUP_INFO_1 group = new LMAccess.GROUP_INFO_1(bufptr.getValue());
      LMAccess.GROUP_INFO_1[] groups = (LMAccess.GROUP_INFO_1[])group.toArray(entriesRead.getValue());
      
      ArrayList<LocalGroup> result = new ArrayList();
      for (LMAccess.GROUP_INFO_1 lgpi : groups) {
        LocalGroup lgp = new LocalGroup();
        if (grpi1_name != null) {
          name = grpi1_name.toString();
        }
        if (grpi1_comment != null) {
          comment = grpi1_comment.toString();
        }
        result.add(lgp); }
      int rc;
      return (Group[])result.toArray(new LocalGroup[0]);
    } finally {
      if (bufptr.getValue() != Pointer.NULL) {
        int rc = Netapi32.INSTANCE.NetApiBufferFree(bufptr.getValue());
        if (0 != rc) {
          throw new Win32Exception(rc);
        }
      }
    }
  }
  



  public static User[] getUsers()
  {
    return getUsers(null);
  }
  




  public static User[] getUsers(String serverName)
  {
    PointerByReference bufptr = new PointerByReference();
    IntByReference entriesRead = new IntByReference();
    IntByReference totalEntries = new IntByReference();
    try {
      int rc = Netapi32.INSTANCE.NetUserEnum(serverName, 1, 0, bufptr, -1, entriesRead, totalEntries, null);
      


      if ((0 != rc) || (bufptr.getValue() == Pointer.NULL)) {
        throw new Win32Exception(rc);
      }
      LMAccess.USER_INFO_1 user = new LMAccess.USER_INFO_1(bufptr.getValue());
      LMAccess.USER_INFO_1[] users = (LMAccess.USER_INFO_1[])user.toArray(entriesRead.getValue());
      ArrayList<User> result = new ArrayList();
      for (LMAccess.USER_INFO_1 lu : users) {
        User auser = new User();
        if (usri1_name != null) {
          name = usri1_name.toString();
        }
        result.add(auser); }
      int rc;
      return (User[])result.toArray(new User[0]);
    } finally {
      if (bufptr.getValue() != Pointer.NULL) {
        int rc = Netapi32.INSTANCE.NetApiBufferFree(bufptr.getValue());
        if (0 != rc) {
          throw new Win32Exception(rc);
        }
      }
    }
  }
  



  public static Group[] getCurrentUserLocalGroups()
  {
    return getUserLocalGroups(Secur32Util.getUserNameEx(2));
  }
  




  public static Group[] getUserLocalGroups(String userName)
  {
    return getUserLocalGroups(userName, null);
  }
  





  public static Group[] getUserLocalGroups(String userName, String serverName)
  {
    PointerByReference bufptr = new PointerByReference();
    IntByReference entriesread = new IntByReference();
    IntByReference totalentries = new IntByReference();
    try {
      int rc = Netapi32.INSTANCE.NetUserGetLocalGroups(serverName, userName, 0, 0, bufptr, -1, entriesread, totalentries);
      

      if (rc != 0) {
        throw new Win32Exception(rc);
      }
      LMAccess.LOCALGROUP_USERS_INFO_0 lgroup = new LMAccess.LOCALGROUP_USERS_INFO_0(bufptr.getValue());
      LMAccess.LOCALGROUP_USERS_INFO_0[] lgroups = (LMAccess.LOCALGROUP_USERS_INFO_0[])lgroup.toArray(entriesread.getValue());
      ArrayList<Group> result = new ArrayList();
      for (LMAccess.LOCALGROUP_USERS_INFO_0 lgpi : lgroups) {
        LocalGroup lgp = new LocalGroup();
        if (lgrui0_name != null) {
          name = lgrui0_name.toString();
        }
        result.add(lgp); }
      int rc;
      return (Group[])result.toArray(new Group[0]);
    } finally {
      if (bufptr.getValue() != Pointer.NULL) {
        int rc = Netapi32.INSTANCE.NetApiBufferFree(bufptr.getValue());
        if (0 != rc) {
          throw new Win32Exception(rc);
        }
      }
    }
  }
  




  public static Group[] getUserGroups(String userName)
  {
    return getUserGroups(userName, null);
  }
  





  public static Group[] getUserGroups(String userName, String serverName)
  {
    PointerByReference bufptr = new PointerByReference();
    IntByReference entriesread = new IntByReference();
    IntByReference totalentries = new IntByReference();
    try {
      int rc = Netapi32.INSTANCE.NetUserGetGroups(serverName, userName, 0, bufptr, -1, entriesread, totalentries);
      

      if (rc != 0) {
        throw new Win32Exception(rc);
      }
      LMAccess.GROUP_USERS_INFO_0 lgroup = new LMAccess.GROUP_USERS_INFO_0(bufptr.getValue());
      LMAccess.GROUP_USERS_INFO_0[] lgroups = (LMAccess.GROUP_USERS_INFO_0[])lgroup.toArray(entriesread.getValue());
      ArrayList<Group> result = new ArrayList();
      for (LMAccess.GROUP_USERS_INFO_0 lgpi : lgroups) {
        Group lgp = new Group();
        if (grui0_name != null) {
          name = grui0_name.toString();
        }
        result.add(lgp); }
      int rc;
      return (Group[])result.toArray(new Group[0]);
    } finally {
      if (bufptr.getValue() != Pointer.NULL) {
        int rc = Netapi32.INSTANCE.NetApiBufferFree(bufptr.getValue());
        if (0 != rc) {
          throw new Win32Exception(rc);
        }
      }
    }
  }
  












































  public static DomainController getDC()
  {
    DsGetDC.PDOMAIN_CONTROLLER_INFO pdci = new DsGetDC.PDOMAIN_CONTROLLER_INFO();
    int rc = Netapi32.INSTANCE.DsGetDcName(null, null, null, null, 0, pdci);
    if (0 != rc) {
      throw new Win32Exception(rc);
    }
    DomainController dc = new DomainController();
    if (dci.DomainControllerAddress != null) {
      address = dci.DomainControllerAddress.toString();
    }
    addressType = dci.DomainControllerAddressType;
    if (dci.ClientSiteName != null) {
      clientSiteName = dci.ClientSiteName.toString();
    }
    if (dci.DnsForestName != null) {
      dnsForestName = dci.DnsForestName.toString();
    }
    domainGuid = dci.DomainGuid;
    if (dci.DomainName != null) {
      domainName = dci.DomainName.toString();
    }
    flags = dci.Flags;
    if (dci.DomainControllerName != null) {
      name = dci.DomainControllerName.toString();
    }
    rc = Netapi32.INSTANCE.NetApiBufferFree(dci.getPointer());
    if (0 != rc) {
      throw new Win32Exception(rc);
    }
    return dc;
  }
  




  public static class DomainTrust
  {
    public String NetbiosDomainName;
    



    public String DnsDomainName;
    



    public WinNT.PSID DomainSid;
    


    public String DomainSidString;
    


    public Guid.GUID DomainGuid;
    


    public String DomainGuidString;
    


    private int flags;
    



    public DomainTrust() {}
    



    public boolean isInForest()
    {
      return (flags & 0x1) != 0;
    }
    






    public boolean isOutbound()
    {
      return (flags & 0x2) != 0;
    }
    






    public boolean isRoot()
    {
      return (flags & 0x4) != 0;
    }
    





    public boolean isPrimary()
    {
      return (flags & 0x8) != 0;
    }
    




    public boolean isNativeMode()
    {
      return (flags & 0x10) != 0;
    }
    






    public boolean isInbound()
    {
      return (flags & 0x20) != 0;
    }
  }
  




  public static DomainTrust[] getDomainTrusts()
  {
    return getDomainTrusts(null);
  }
  






  public static DomainTrust[] getDomainTrusts(String serverName)
  {
    IntByReference domainTrustCount = new IntByReference();
    PointerByReference domainsPointerRef = new PointerByReference();
    int rc = Netapi32.INSTANCE.DsEnumerateDomainTrusts(serverName, 63, domainsPointerRef, domainTrustCount);
    
    if (0 != rc) {
      throw new Win32Exception(rc);
    }
    try {
      DsGetDC.DS_DOMAIN_TRUSTS domainTrustRefs = new DsGetDC.DS_DOMAIN_TRUSTS(domainsPointerRef.getValue());
      DsGetDC.DS_DOMAIN_TRUSTS[] domainTrusts = (DsGetDC.DS_DOMAIN_TRUSTS[])domainTrustRefs.toArray(new DsGetDC.DS_DOMAIN_TRUSTS[domainTrustCount.getValue()]);
      ArrayList<DomainTrust> trusts = new ArrayList(domainTrustCount.getValue());
      for (DsGetDC.DS_DOMAIN_TRUSTS domainTrust : domainTrusts) {
        DomainTrust t = new DomainTrust();
        if (DnsDomainName != null) {
          DnsDomainName = DnsDomainName.toString();
        }
        if (NetbiosDomainName != null) {
          NetbiosDomainName = NetbiosDomainName.toString();
        }
        DomainSid = DomainSid;
        if (DomainSid != null) {
          DomainSidString = Advapi32Util.convertSidToStringSid(DomainSid);
        }
        DomainGuid = DomainGuid;
        if (DomainGuid != null) {
          DomainGuidString = Ole32Util.getStringFromGUID(DomainGuid);
        }
        flags = Flags;
        trusts.add(t);
      }
      return (DomainTrust[])trusts.toArray(new DomainTrust[0]);
    } finally {
      rc = Netapi32.INSTANCE.NetApiBufferFree(domainsPointerRef.getValue());
      if (0 != rc) {
        throw new Win32Exception(rc);
      }
    }
  }
  
  public static UserInfo getUserInfo(String accountName) {
    return getUserInfo(accountName, getDCName());
  }
  
  public static UserInfo getUserInfo(String accountName, String domainName) {
    PointerByReference bufptr = new PointerByReference();
    int rc = -1;
    try {
      rc = Netapi32.INSTANCE.NetUserGetInfo(domainName, accountName, 23, bufptr);
      if (rc == 0) {
        LMAccess.USER_INFO_23 info_23 = new LMAccess.USER_INFO_23(bufptr.getValue());
        UserInfo userInfo = new UserInfo();
        if (usri23_comment != null) {
          comment = usri23_comment.toString();
        }
        flags = usri23_flags;
        if (usri23_full_name != null) {
          fullName = usri23_full_name.toString();
        }
        if (usri23_name != null) {
          name = usri23_name.toString();
        }
        if (usri23_user_sid != null) {
          sidString = Advapi32Util.convertSidToStringSid(usri23_user_sid);
        }
        sid = usri23_user_sid;
        return userInfo;
      }
      throw new Win32Exception(rc);
    }
    finally {
      if (bufptr.getValue() != Pointer.NULL) {
        Netapi32.INSTANCE.NetApiBufferFree(bufptr.getValue());
      }
    }
  }
  
  public static class DomainController
  {
    public String name;
    public String address;
    public int addressType;
    public Guid.GUID domainGuid;
    public String domainName;
    public String dnsForestName;
    public int flags;
    public String clientSiteName;
    
    public DomainController() {}
  }
  
  public static class LocalGroup
    extends Netapi32Util.Group
  {
    public String comment;
    
    public LocalGroup() {}
  }
  
  public static class UserInfo
    extends Netapi32Util.User
  {
    public String fullName;
    public String sidString;
    public WinNT.PSID sid;
    public int flags;
    
    public UserInfo() {}
  }
  
  public static class User
  {
    public String name;
    public String comment;
    
    public User() {}
  }
  
  public static class Group
  {
    public String name;
    
    public Group() {}
  }
}
