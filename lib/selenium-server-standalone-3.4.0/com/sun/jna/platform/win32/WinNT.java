package com.sun.jna.platform.win32;

import com.sun.jna.FromNativeContext;
import com.sun.jna.Memory;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;
import com.sun.jna.Structure;
import com.sun.jna.Structure.ByReference;
import com.sun.jna.Union;
import com.sun.jna.ptr.ByReference;
import com.sun.jna.win32.StdCallLibrary.StdCallCallback;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
























public abstract interface WinNT
  extends WinError, WinDef, WinBase, BaseTSD
{
  public static final int DELETE = 65536;
  public static final int READ_CONTROL = 131072;
  public static final int WRITE_DAC = 262144;
  public static final int WRITE_OWNER = 524288;
  public static final int SYNCHRONIZE = 1048576;
  public static final int STANDARD_RIGHTS_REQUIRED = 983040;
  public static final int STANDARD_RIGHTS_READ = 131072;
  public static final int STANDARD_RIGHTS_WRITE = 131072;
  public static final int STANDARD_RIGHTS_EXECUTE = 131072;
  public static final int STANDARD_RIGHTS_ALL = 2031616;
  public static final int SPECIFIC_RIGHTS_ALL = 65535;
  public static final int TOKEN_ASSIGN_PRIMARY = 1;
  public static final int TOKEN_DUPLICATE = 2;
  public static final int TOKEN_IMPERSONATE = 4;
  public static final int TOKEN_QUERY = 8;
  public static final int TOKEN_QUERY_SOURCE = 16;
  public static final int TOKEN_ADJUST_PRIVILEGES = 32;
  public static final int TOKEN_ADJUST_GROUPS = 64;
  public static final int TOKEN_ADJUST_DEFAULT = 128;
  public static final int TOKEN_ADJUST_SESSIONID = 256;
  public static final int TOKEN_ALL_ACCESS_P = 983295;
  public static final int TOKEN_ALL_ACCESS = 983551;
  public static final int TOKEN_READ = 131080;
  public static final int TOKEN_WRITE = 131296;
  public static final int TOKEN_EXECUTE = 131072;
  public static final int THREAD_TERMINATE = 1;
  public static final int THREAD_SUSPEND_RESUME = 2;
  public static final int THREAD_GET_CONTEXT = 8;
  public static final int THREAD_SET_CONTEXT = 16;
  public static final int THREAD_QUERY_INFORMATION = 64;
  public static final int THREAD_SET_INFORMATION = 32;
  public static final int THREAD_SET_THREAD_TOKEN = 128;
  public static final int THREAD_IMPERSONATE = 256;
  public static final int THREAD_DIRECT_IMPERSONATION = 512;
  public static final int THREAD_SET_LIMITED_INFORMATION = 1024;
  public static final int THREAD_QUERY_LIMITED_INFORMATION = 2048;
  public static final int THREAD_ALL_ACCESS = 2032639;
  public static final int FILE_READ_DATA = 1;
  public static final int FILE_LIST_DIRECTORY = 1;
  public static final int FILE_WRITE_DATA = 2;
  public static final int FILE_ADD_FILE = 2;
  public static final int FILE_APPEND_DATA = 4;
  public static final int FILE_ADD_SUBDIRECTORY = 4;
  public static final int FILE_CREATE_PIPE_INSTANCE = 4;
  public static final int FILE_READ_EA = 8;
  public static final int FILE_WRITE_EA = 16;
  public static final int FILE_EXECUTE = 32;
  public static final int FILE_TRAVERSE = 32;
  public static final int FILE_DELETE_CHILD = 64;
  public static final int FILE_READ_ATTRIBUTES = 128;
  public static final int FILE_WRITE_ATTRIBUTES = 256;
  public static final int FILE_ALL_ACCESS = 2032127;
  public static final int FILE_GENERIC_READ = 1179785;
  public static final int FILE_GENERIC_WRITE = 1179926;
  public static final int FILE_GENERIC_EXECUTE = 1179808;
  public static final int CREATE_NEW = 1;
  public static final int CREATE_ALWAYS = 2;
  public static final int OPEN_EXISTING = 3;
  public static final int OPEN_ALWAYS = 4;
  public static final int TRUNCATE_EXISTING = 5;
  public static final int FILE_FLAG_WRITE_THROUGH = Integer.MIN_VALUE;
  public static final int FILE_FLAG_OVERLAPPED = 1073741824;
  public static final int FILE_FLAG_NO_BUFFERING = 536870912;
  public static final int FILE_FLAG_RANDOM_ACCESS = 268435456;
  public static final int FILE_FLAG_SEQUENTIAL_SCAN = 134217728;
  public static final int FILE_FLAG_DELETE_ON_CLOSE = 67108864;
  public static final int FILE_FLAG_BACKUP_SEMANTICS = 33554432;
  public static final int FILE_FLAG_POSIX_SEMANTICS = 16777216;
  public static final int FILE_FLAG_OPEN_REPARSE_POINT = 2097152;
  public static final int FILE_FLAG_OPEN_NO_RECALL = 1048576;
  public static final int GENERIC_READ = Integer.MIN_VALUE;
  public static final int GENERIC_WRITE = 1073741824;
  public static final int GENERIC_EXECUTE = 536870912;
  public static final int GENERIC_ALL = 268435456;
  public static final int ACCESS_SYSTEM_SECURITY = 16777216;
  public static final int PAGE_READONLY = 2;
  public static final int PAGE_READWRITE = 4;
  public static final int PAGE_WRITECOPY = 8;
  public static final int PAGE_EXECUTE = 16;
  public static final int PAGE_EXECUTE_READ = 32;
  public static final int PAGE_EXECUTE_READWRITE = 64;
  public static final int SECTION_QUERY = 1;
  public static final int SECTION_MAP_WRITE = 2;
  public static final int SECTION_MAP_READ = 4;
  public static final int SECTION_MAP_EXECUTE = 8;
  public static final int SECTION_EXTEND_SIZE = 16;
  public static final int FILE_SHARE_READ = 1;
  public static final int FILE_SHARE_WRITE = 2;
  public static final int FILE_SHARE_DELETE = 4;
  public static final int FILE_TYPE_CHAR = 2;
  public static final int FILE_TYPE_DISK = 1;
  public static final int FILE_TYPE_PIPE = 3;
  public static final int FILE_TYPE_REMOTE = 32768;
  public static final int FILE_TYPE_UNKNOWN = 0;
  public static final int FILE_ATTRIBUTE_READONLY = 1;
  public static final int FILE_ATTRIBUTE_HIDDEN = 2;
  public static final int FILE_ATTRIBUTE_SYSTEM = 4;
  public static final int FILE_ATTRIBUTE_DIRECTORY = 16;
  public static final int FILE_ATTRIBUTE_ARCHIVE = 32;
  public static final int FILE_ATTRIBUTE_DEVICE = 64;
  public static final int FILE_ATTRIBUTE_NORMAL = 128;
  public static final int FILE_ATTRIBUTE_TEMPORARY = 256;
  public static final int FILE_ATTRIBUTE_SPARSE_FILE = 512;
  public static final int FILE_ATTRIBUTE_REPARSE_POINT = 1024;
  public static final int FILE_ATTRIBUTE_COMPRESSED = 2048;
  public static final int FILE_ATTRIBUTE_OFFLINE = 4096;
  public static final int FILE_ATTRIBUTE_NOT_CONTENT_INDEXED = 8192;
  public static final int FILE_ATTRIBUTE_ENCRYPTED = 16384;
  public static final int FILE_ATTRIBUTE_VIRTUAL = 65536;
  public static final int FILE_NOTIFY_CHANGE_FILE_NAME = 1;
  public static final int FILE_NOTIFY_CHANGE_DIR_NAME = 2;
  public static final int FILE_NOTIFY_CHANGE_NAME = 3;
  public static final int FILE_NOTIFY_CHANGE_ATTRIBUTES = 4;
  public static final int FILE_NOTIFY_CHANGE_SIZE = 8;
  public static final int FILE_NOTIFY_CHANGE_LAST_WRITE = 16;
  public static final int FILE_NOTIFY_CHANGE_LAST_ACCESS = 32;
  public static final int FILE_NOTIFY_CHANGE_CREATION = 64;
  public static final int FILE_NOTIFY_CHANGE_SECURITY = 256;
  public static final int FILE_ACTION_ADDED = 1;
  public static final int FILE_ACTION_REMOVED = 2;
  public static final int FILE_ACTION_MODIFIED = 3;
  public static final int FILE_ACTION_RENAMED_OLD_NAME = 4;
  public static final int FILE_ACTION_RENAMED_NEW_NAME = 5;
  public static final int FILE_CASE_SENSITIVE_SEARCH = 1;
  public static final int FILE_CASE_PRESERVED_NAMES = 2;
  public static final int FILE_UNICODE_ON_DISK = 4;
  public static final int FILE_PERSISTENT_ACLS = 8;
  public static final int FILE_FILE_COMPRESSION = 16;
  public static final int FILE_VOLUME_QUOTAS = 32;
  public static final int FILE_SUPPORTS_SPARSE_FILES = 64;
  public static final int FILE_SUPPORTS_REPARSE_POINTS = 128;
  public static final int FILE_SUPPORTS_REMOTE_STORAGE = 256;
  public static final int FILE_VOLUME_IS_COMPRESSED = 32768;
  public static final int FILE_SUPPORTS_OBJECT_IDS = 65536;
  public static final int FILE_SUPPORTS_ENCRYPTION = 131072;
  public static final int FILE_NAMED_STREAMS = 262144;
  public static final int FILE_READ_ONLY_VOLUME = 524288;
  public static final int FILE_SEQUENTIAL_WRITE_ONCE = 1048576;
  public static final int FILE_SUPPORTS_TRANSACTIONS = 2097152;
  public static final int KEY_QUERY_VALUE = 1;
  public static final int KEY_SET_VALUE = 2;
  public static final int KEY_CREATE_SUB_KEY = 4;
  public static final int KEY_ENUMERATE_SUB_KEYS = 8;
  public static final int KEY_NOTIFY = 16;
  public static final int KEY_CREATE_LINK = 32;
  public static final int KEY_WOW64_32KEY = 512;
  public static final int KEY_WOW64_64KEY = 256;
  public static final int KEY_WOW64_RES = 768;
  public static final int KEY_READ = 131097;
  public static final int KEY_WRITE = 131078;
  public static final int KEY_EXECUTE = 131097;
  public static final int KEY_ALL_ACCESS = 2031679;
  public static final int REG_OPTION_RESERVED = 0;
  public static final int REG_OPTION_NON_VOLATILE = 0;
  public static final int REG_OPTION_VOLATILE = 1;
  public static final int REG_OPTION_CREATE_LINK = 2;
  
  public static abstract class SECURITY_IMPERSONATION_LEVEL
  {
    public static final int SecurityAnonymous = 0;
    public static final int SecurityIdentification = 1;
    public static final int SecurityImpersonation = 2;
    public static final int SecurityDelegation = 3;
    
    public SECURITY_IMPERSONATION_LEVEL() {}
  }
  
  public static abstract class TOKEN_INFORMATION_CLASS
  {
    public static final int TokenUser = 1;
    public static final int TokenGroups = 2;
    public static final int TokenPrivileges = 3;
    public static final int TokenOwner = 4;
    public static final int TokenPrimaryGroup = 5;
    public static final int TokenDefaultDacl = 6;
    public static final int TokenSource = 7;
    public static final int TokenType = 8;
    public static final int TokenImpersonationLevel = 9;
    public static final int TokenStatistics = 10;
    public static final int TokenRestrictedSids = 11;
    public static final int TokenSessionId = 12;
    public static final int TokenGroupsAndPrivileges = 13;
    public static final int TokenSessionReference = 14;
    public static final int TokenSandBoxInert = 15;
    public static final int TokenAuditPolicy = 16;
    public static final int TokenOrigin = 17;
    public static final int TokenElevationType = 18;
    public static final int TokenLinkedToken = 19;
    public static final int TokenElevation = 20;
    public static final int TokenHasRestrictions = 21;
    public static final int TokenAccessInformation = 22;
    public static final int TokenVirtualizationAllowed = 23;
    public static final int TokenVirtualizationEnabled = 24;
    public static final int TokenIntegrityLevel = 25;
    public static final int TokenUIAccess = 26;
    public static final int TokenMandatoryPolicy = 27;
    public static final int TokenLogonSid = 28;
    
    public TOKEN_INFORMATION_CLASS() {}
  }
  
  public static abstract class TOKEN_TYPE
  {
    public static final int TokenPrimary = 1;
    public static final int TokenImpersonation = 2;
    
    public TOKEN_TYPE() {}
  }
  
  public static class LUID_AND_ATTRIBUTES
    extends Structure
  {
    protected List getFieldOrder() { return Arrays.asList(new String[] { "Luid", "Attributes" }); }
    
    public WinNT.LUID Luid;
    public WinDef.DWORD Attributes;
    public LUID_AND_ATTRIBUTES() {}
    
    public LUID_AND_ATTRIBUTES(WinNT.LUID luid, WinDef.DWORD attributes) {
      Luid = luid;
      Attributes = attributes;
    }
  }
  
  public static class SID_AND_ATTRIBUTES extends Structure
  {
    public WinNT.PSID.ByReference Sid;
    public int Attributes;
    
    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "Sid", "Attributes" });
    }
    

    public SID_AND_ATTRIBUTES() {}
    
    public SID_AND_ATTRIBUTES(Pointer memory)
    {
      super();
    }
  }
  






  public static class TOKEN_OWNER
    extends Structure
  {
    public WinNT.PSID.ByReference Owner;
    





    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "Owner" });
    }
    

    public TOKEN_OWNER() {}
    
    public TOKEN_OWNER(int size)
    {
      super();
    }
    
    public TOKEN_OWNER(Pointer memory) {
      super();
      read();
    }
  }
  


  public static class PSID
    extends Structure
  {
    public Pointer sid;
    


    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "sid" });
    }
    

    public PSID() {}
    
    public PSID(byte[] data)
    {
      super();
      getPointer().write(0L, data, 0, data.length);
      read();
    }
    
    public PSID(int size) {
      super();
    }
    
    public PSID(Pointer memory) {
      super();
      read();
    }
    
    public byte[] getBytes() {
      int len = Advapi32.INSTANCE.GetLengthSid(this);
      return getPointer().getByteArray(0L, len);
    }
    
    public static class ByReference extends WinNT.PSID implements Structure.ByReference {
      public ByReference() {}
    }
  }
  
  public static class PSIDByReference extends ByReference { public PSIDByReference() { this(null); }
    
    public PSIDByReference(WinNT.PSID h)
    {
      super();
      setValue(h);
    }
    
    public void setValue(WinNT.PSID h) {
      getPointer().setPointer(0L, h != null ? h.getPointer() : null);
    }
    
    public WinNT.PSID getValue() {
      Pointer p = getPointer().getPointer(0L);
      if (p == null) {
        return null;
      }
      
      return new WinNT.PSID(p);
    }
  }
  

  public static class TOKEN_USER
    extends Structure
  {
    public WinNT.SID_AND_ATTRIBUTES User;
    
    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "User" });
    }
    

    public TOKEN_USER() {}
    
    public TOKEN_USER(Pointer memory)
    {
      super();
      read();
    }
    
    public TOKEN_USER(int size) {
      super();
    }
  }
  


  public static class TOKEN_GROUPS
    extends Structure
  {
    public int GroupCount;
    

    public WinNT.SID_AND_ATTRIBUTES Group0;
    


    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "GroupCount", "Group0" });
    }
    

    public TOKEN_GROUPS() {}
    
    public TOKEN_GROUPS(Pointer memory)
    {
      super();
      read();
    }
    
    public TOKEN_GROUPS(int size) {
      super();
    }
    









    public WinNT.SID_AND_ATTRIBUTES[] getGroups()
    {
      return (WinNT.SID_AND_ATTRIBUTES[])Group0.toArray(GroupCount);
    }
  }
  

  public static class PRIVILEGE_SET
    extends Structure
  {
    public WinDef.DWORD PrivilegeCount;
    
    public WinDef.DWORD Control;
    public WinNT.LUID_AND_ATTRIBUTES[] Privileges;
    
    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "PrivilegeCount", "Control", "Privileges" });
    }
    
    public PRIVILEGE_SET() {
      this(0);
    }
    


    public PRIVILEGE_SET(int nbOfPrivileges)
    {
      PrivilegeCount = new WinDef.DWORD(nbOfPrivileges);
      if (nbOfPrivileges > 0) {
        Privileges = new WinNT.LUID_AND_ATTRIBUTES[nbOfPrivileges];
      }
    }
    
    public PRIVILEGE_SET(Pointer p)
    {
      super();
      int count = p.getInt(0L);
      PrivilegeCount = new WinDef.DWORD(count);
      if (count > 0) {
        Privileges = new WinNT.LUID_AND_ATTRIBUTES[count];
      }
      read();
    }
  }
  



  public static class TOKEN_PRIVILEGES
    extends Structure
  {
    public WinDef.DWORD PrivilegeCount;
    


    public WinNT.LUID_AND_ATTRIBUTES[] Privileges;
    



    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "PrivilegeCount", "Privileges" });
    }
    
    public TOKEN_PRIVILEGES()
    {
      this(0);
    }
    


    public TOKEN_PRIVILEGES(int nbOfPrivileges)
    {
      PrivilegeCount = new WinDef.DWORD(nbOfPrivileges);
      Privileges = new WinNT.LUID_AND_ATTRIBUTES[nbOfPrivileges];
    }
    
    public TOKEN_PRIVILEGES(Pointer p)
    {
      super();
      int count = p.getInt(0L);
      PrivilegeCount = new WinDef.DWORD(count);
      Privileges = new WinNT.LUID_AND_ATTRIBUTES[count];
      read();
    }
  }
  












  public static abstract class SID_NAME_USE
  {
    public static final int SidTypeUser = 1;
    











    public static final int SidTypeGroup = 2;
    











    public static final int SidTypeDomain = 3;
    











    public static final int SidTypeAlias = 4;
    











    public static final int SidTypeWellKnownGroup = 5;
    











    public static final int SidTypeDeletedAccount = 6;
    











    public static final int SidTypeInvalid = 7;
    











    public static final int SidTypeUnknown = 8;
    











    public static final int SidTypeComputer = 9;
    










    public static final int SidTypeLabel = 10;
    











    public SID_NAME_USE() {}
  }
  











  public static class FILE_NOTIFY_INFORMATION
    extends Structure
  {
    public int NextEntryOffset;
    










    public int Action;
    










    public int FileNameLength;
    










    public char[] FileName = new char[1];
    
    protected List getFieldOrder() {
      return Arrays.asList(new String[] { "NextEntryOffset", "Action", "FileNameLength", "FileName" });
    }
    
    private FILE_NOTIFY_INFORMATION() {}
    
    public FILE_NOTIFY_INFORMATION(int size)
    {
      if (size < size()) {
        throw new IllegalArgumentException("Size must greater than " + size() + ", requested " + size);
      }
      
      allocateMemory(size);
    }
    



    public String getFilename()
    {
      return new String(FileName, 0, FileNameLength / 2);
    }
    
    public void read()
    {
      FileName = new char[0];
      super.read();
      FileName = getPointer().getCharArray(12L, FileNameLength / 2);
    }
    
    public FILE_NOTIFY_INFORMATION next() {
      if (NextEntryOffset == 0) {
        return null;
      }
      FILE_NOTIFY_INFORMATION next = new FILE_NOTIFY_INFORMATION();
      next.useMemory(getPointer(), NextEntryOffset);
      next.read();
      return next;
    }
  }
  


  public static final int REG_OPTION_BACKUP_RESTORE = 4;
  

  public static final int REG_OPTION_OPEN_LINK = 8;
  
  public static final int REG_LEGAL_OPTION = 15;
  
  public static final int REG_CREATED_NEW_KEY = 1;
  
  public static final int REG_OPENED_EXISTING_KEY = 2;
  
  public static final int REG_STANDARD_FORMAT = 1;
  
  public static final int REG_LATEST_FORMAT = 2;
  
  public static final int REG_NO_COMPRESSION = 4;
  
  public static final int REG_WHOLE_HIVE_VOLATILE = 1;
  
  public static final int REG_REFRESH_HIVE = 2;
  
  public static final int REG_NO_LAZY_FLUSH = 4;
  
  public static final int REG_FORCE_RESTORE = 8;
  
  public static final int REG_APP_HIVE = 16;
  
  public static final int REG_PROCESS_PRIVATE = 32;
  
  public static final int REG_START_JOURNAL = 64;
  
  public static final int REG_HIVE_EXACT_FILE_GROWTH = 128;
  
  public static final int REG_HIVE_NO_RM = 256;
  
  public static final int REG_HIVE_SINGLE_LOG = 512;
  
  public static final int REG_FORCE_UNLOAD = 1;
  
  public static final int REG_NOTIFY_CHANGE_NAME = 1;
  
  public static final int REG_NOTIFY_CHANGE_ATTRIBUTES = 2;
  
  public static final int REG_NOTIFY_CHANGE_LAST_SET = 4;
  
  public static final int REG_NOTIFY_CHANGE_SECURITY = 8;
  
  public static final int REG_LEGAL_CHANGE_FILTER = 15;
  
  public static final int REG_NONE = 0;
  
  public static final int REG_SZ = 1;
  
  public static final int REG_EXPAND_SZ = 2;
  
  public static final int REG_BINARY = 3;
  
  public static final int REG_DWORD = 4;
  
  public static final int REG_DWORD_LITTLE_ENDIAN = 4;
  
  public static final int REG_DWORD_BIG_ENDIAN = 5;
  
  public static final int REG_LINK = 6;
  
  public static final int REG_MULTI_SZ = 7;
  
  public static final int REG_RESOURCE_LIST = 8;
  
  public static final int REG_FULL_RESOURCE_DESCRIPTOR = 9;
  
  public static final int REG_RESOURCE_REQUIREMENTS_LIST = 10;
  
  public static final int REG_QWORD = 11;
  
  public static final int REG_QWORD_LITTLE_ENDIAN = 11;
  
  public static final int SID_REVISION = 1;
  
  public static final int SID_MAX_SUB_AUTHORITIES = 15;
  
  public static final int SID_RECOMMENDED_SUB_AUTHORITIES = 1;
  
  public static final int SECURITY_MAX_SID_SIZE = 68;
  
  public static final int VER_EQUAL = 1;
  
  public static final int VER_GREATER = 2;
  
  public static final int VER_GREATER_EQUAL = 3;
  
  public static final int VER_LESS = 4;
  
  public static final int VER_LESS_EQUAL = 5;
  
  public static final int VER_AND = 6;
  
  public static final int VER_OR = 7;
  
  public static final int VER_CONDITION_MASK = 7;
  
  public static final int VER_NUM_BITS_PER_CONDITION_MASK = 3;
  
  public static final int VER_MINORVERSION = 1;
  
  public static final int VER_MAJORVERSION = 2;
  
  public static final int VER_BUILDNUMBER = 4;
  
  public static final int VER_PLATFORMID = 8;
  
  public static final int VER_SERVICEPACKMINOR = 16;
  
  public static final int VER_SERVICEPACKMAJOR = 32;
  
  public static final int VER_SUITENAME = 64;
  
  public static final int VER_PRODUCT_TYPE = 128;
  
  public static final int VER_NT_WORKSTATION = 1;
  
  public static final int VER_NT_DOMAIN_CONTROLLER = 2;
  
  public static final int VER_NT_SERVER = 3;
  
  public static final int VER_PLATFORM_WIN32s = 0;
  
  public static final int VER_PLATFORM_WIN32_WINDOWS = 1;
  
  public static final int VER_PLATFORM_WIN32_NT = 2;
  
  public static final int EVENTLOG_SEQUENTIAL_READ = 1;
  
  public static final int EVENTLOG_SEEK_READ = 2;
  
  public static final int EVENTLOG_FORWARDS_READ = 4;
  
  public static final int EVENTLOG_BACKWARDS_READ = 8;
  
  public static final int EVENTLOG_SUCCESS = 0;
  public static final int EVENTLOG_ERROR_TYPE = 1;
  public static final int EVENTLOG_WARNING_TYPE = 2;
  public static final int EVENTLOG_INFORMATION_TYPE = 4;
  public static final int EVENTLOG_AUDIT_SUCCESS = 8;
  public static final int EVENTLOG_AUDIT_FAILURE = 16;
  public static final int SERVICE_KERNEL_DRIVER = 1;
  public static final int SERVICE_FILE_SYSTEM_DRIVER = 2;
  public static final int SERVICE_ADAPTER = 4;
  public static final int SERVICE_RECOGNIZER_DRIVER = 8;
  public static final int SERVICE_DRIVER = 11;
  public static final int SERVICE_WIN32_OWN_PROCESS = 16;
  public static final int SERVICE_WIN32_SHARE_PROCESS = 32;
  public static final int SERVICE_WIN32 = 48;
  public static final int SERVICE_INTERACTIVE_PROCESS = 256;
  public static final int SERVICE_TYPE_ALL = 319;
  public static final int STATUS_PENDING = 259;
  public static final String SE_CREATE_TOKEN_NAME = "SeCreateTokenPrivilege";
  public static final String SE_ASSIGNPRIMARYTOKEN_NAME = "SeAssignPrimaryTokenPrivilege";
  public static final String SE_LOCK_MEMORY_NAME = "SeLockMemoryPrivilege";
  public static final String SE_INCREASE_QUOTA_NAME = "SeIncreaseQuotaPrivilege";
  public static final String SE_UNSOLICITED_INPUT_NAME = "SeUnsolicitedInputPrivilege";
  public static final String SE_MACHINE_ACCOUNT_NAME = "SeMachineAccountPrivilege";
  public static final String SE_TCB_NAME = "SeTcbPrivilege";
  public static final String SE_SECURITY_NAME = "SeSecurityPrivilege";
  public static final String SE_TAKE_OWNERSHIP_NAME = "SeTakeOwnershipPrivilege";
  public static final String SE_LOAD_DRIVER_NAME = "SeLoadDriverPrivilege";
  public static final String SE_SYSTEM_PROFILE_NAME = "SeSystemProfilePrivilege";
  public static final String SE_SYSTEMTIME_NAME = "SeSystemtimePrivilege";
  public static final String SE_PROF_SINGLE_PROCESS_NAME = "SeProfileSingleProcessPrivilege";
  public static final String SE_INC_BASE_PRIORITY_NAME = "SeIncreaseBasePriorityPrivilege";
  public static final String SE_CREATE_PAGEFILE_NAME = "SeCreatePagefilePrivilege";
  public static final String SE_CREATE_PERMANENT_NAME = "SeCreatePermanentPrivilege";
  public static final String SE_BACKUP_NAME = "SeBackupPrivilege";
  public static final String SE_RESTORE_NAME = "SeRestorePrivilege";
  public static final String SE_SHUTDOWN_NAME = "SeShutdownPrivilege";
  public static final String SE_DEBUG_NAME = "SeDebugPrivilege";
  public static final String SE_AUDIT_NAME = "SeAuditPrivilege";
  public static final String SE_SYSTEM_ENVIRONMENT_NAME = "SeSystemEnvironmentPrivilege";
  public static final String SE_CHANGE_NOTIFY_NAME = "SeChangeNotifyPrivilege";
  public static final String SE_REMOTE_SHUTDOWN_NAME = "SeRemoteShutdownPrivilege";
  public static final String SE_UNDOCK_NAME = "SeUndockPrivilege";
  public static final String SE_SYNC_AGENT_NAME = "SeSyncAgentPrivilege";
  public static final String SE_ENABLE_DELEGATION_NAME = "SeEnableDelegationPrivilege";
  public static final String SE_MANAGE_VOLUME_NAME = "SeManageVolumePrivilege";
  public static final String SE_IMPERSONATE_NAME = "SeImpersonatePrivilege";
  public static final String SE_CREATE_GLOBAL_NAME = "SeCreateGlobalPrivilege";
  public static final int SE_PRIVILEGE_ENABLED_BY_DEFAULT = 1;
  public static final int SE_PRIVILEGE_ENABLED = 2;
  public static final int SE_PRIVILEGE_REMOVED = 4;
  public static final int SE_PRIVILEGE_USED_FOR_ACCESS = Integer.MIN_VALUE;
  public static final int PROCESS_TERMINATE = 1;
  public static final int PROCESS_SYNCHRONIZE = 1048576;
  public static final int OWNER_SECURITY_INFORMATION = 1;
  public static final int GROUP_SECURITY_INFORMATION = 2;
  public static final int DACL_SECURITY_INFORMATION = 4;
  public static final int SACL_SECURITY_INFORMATION = 8;
  public static final int LABEL_SECURITY_INFORMATION = 16;
  public static final int PROTECTED_DACL_SECURITY_INFORMATION = Integer.MIN_VALUE;
  public static final int PROTECTED_SACL_SECURITY_INFORMATION = 1073741824;
  public static final int UNPROTECTED_DACL_SECURITY_INFORMATION = 536870912;
  public static final int UNPROTECTED_SACL_SECURITY_INFORMATION = 268435456;
  public static final byte ACCESS_ALLOWED_ACE_TYPE = 0;
  public static final byte ACCESS_DENIED_ACE_TYPE = 1;
  public static final byte SYSTEM_AUDIT_ACE_TYPE = 2;
  public static final byte SYSTEM_ALARM_ACE_TYPE = 3;
  public static final byte ACCESS_ALLOWED_COMPOUND_ACE_TYPE = 4;
  public static final byte ACCESS_ALLOWED_OBJECT_ACE_TYPE = 5;
  public static final byte ACCESS_DENIED_OBJECT_ACE_TYPE = 6;
  public static final byte SYSTEM_AUDIT_OBJECT_ACE_TYPE = 7;
  public static final byte SYSTEM_ALARM_OBJECT_ACE_TYPE = 8;
  public static final byte ACCESS_ALLOWED_CALLBACK_ACE_TYPE = 9;
  public static final byte ACCESS_DENIED_CALLBACK_ACE_TYPE = 10;
  public static final byte ACCESS_ALLOWED_CALLBACK_OBJECT_ACE_TYPE = 11;
  public static final byte ACCESS_DENIED_CALLBACK_OBJECT_ACE_TYPE = 12;
  public static final byte SYSTEM_AUDIT_CALLBACK_ACE_TYPE = 13;
  public static final byte SYSTEM_ALARM_CALLBACK_ACE_TYPE = 14;
  public static final byte SYSTEM_AUDIT_CALLBACK_OBJECT_ACE_TYPE = 15;
  public static final byte SYSTEM_ALARM_CALLBACK_OBJECT_ACE_TYPE = 16;
  public static final byte SYSTEM_MANDATORY_LABEL_ACE_TYPE = 17;
  public static final byte OBJECT_INHERIT_ACE = 1;
  public static final byte CONTAINER_INHERIT_ACE = 2;
  public static final byte NO_PROPAGATE_INHERIT_ACE = 4;
  public static final byte INHERIT_ONLY_ACE = 8;
  public static final byte INHERITED_ACE = 16;
  public static final byte VALID_INHERIT_FLAGS = 31;
  public static final byte CACHE_FULLY_ASSOCIATIVE = -1;
  public static class LUID
    extends Structure
  {
    public int LowPart;
    public int HighPart;
    
    public LUID() {}
    
    protected List getFieldOrder() { return Arrays.asList(new String[] { "LowPart", "HighPart" }); }
  }
  
  public static class LARGE_INTEGER extends Structure { public UNION u;
    public LARGE_INTEGER() {}
    
    public static class UNION extends Union { public WinNT.LARGE_INTEGER.LowHigh lh;
      public long value;
      
      public UNION() {} }
    
    public static class LowHigh extends Structure { public WinDef.DWORD LowPart;
      public WinDef.DWORD HighPart;
      
      public LowHigh() {}
      
      protected List getFieldOrder() { return Arrays.asList(new String[] { "LowPart", "HighPart" }); }
    }
    







    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "u" });
    }
    




    public WinDef.DWORD getLow()
    {
      return u.lh.LowPart;
    }
    




    public WinDef.DWORD getHigh()
    {
      return u.lh.HighPart;
    }
    




    public long getValue()
    {
      return u.value;
    }
    
    public static class ByReference extends WinNT.LARGE_INTEGER implements Structure.ByReference {
      public ByReference() {}
    }
  }
  
  public static class HANDLE extends PointerType {
    private boolean immutable;
    
    public HANDLE() {}
    
    public HANDLE(Pointer p) {
      setPointer(p);
      immutable = true;
    }
    
    public Object fromNative(Object nativeValue, FromNativeContext context)
    {
      Object o = super.fromNative(nativeValue, context);
      if (WinBase.INVALID_HANDLE_VALUE.equals(o)) {
        return WinBase.INVALID_HANDLE_VALUE;
      }
      return o;
    }
    
    public void setPointer(Pointer p) {
      if (immutable) {
        throw new UnsupportedOperationException("immutable reference");
      }
      
      super.setPointer(p);
    }
  }
  
  public static class HANDLEByReference
    extends ByReference
  {
    public HANDLEByReference()
    {
      this(null);
    }
    
    public HANDLEByReference(WinNT.HANDLE h) {
      super();
      setValue(h);
    }
    
    public void setValue(WinNT.HANDLE h) {
      getPointer().setPointer(0L, h != null ? h.getPointer() : null);
    }
    
    public WinNT.HANDLE getValue() {
      Pointer p = getPointer().getPointer(0L);
      if (p == null) {
        return null;
      }
      if (WinBase.INVALID_HANDLE_VALUE.getPointer().equals(p)) {
        return WinBase.INVALID_HANDLE_VALUE;
      }
      WinNT.HANDLE h = new WinNT.HANDLE();
      h.setPointer(p);
      return h;
    }
  }
  

  public static class HRESULT
    extends NativeLong
  {
    public HRESULT() {}
    

    public HRESULT(int value)
    {
      super();
    }
  }
  





  public static abstract class WELL_KNOWN_SID_TYPE
  {
    public static final int WinNullSid = 0;
    




    public static final int WinWorldSid = 1;
    




    public static final int WinLocalSid = 2;
    




    public static final int WinCreatorOwnerSid = 3;
    




    public static final int WinCreatorGroupSid = 4;
    




    public static final int WinCreatorOwnerServerSid = 5;
    




    public static final int WinCreatorGroupServerSid = 6;
    




    public static final int WinNtAuthoritySid = 7;
    




    public static final int WinDialupSid = 8;
    




    public static final int WinNetworkSid = 9;
    




    public static final int WinBatchSid = 10;
    




    public static final int WinInteractiveSid = 11;
    




    public static final int WinServiceSid = 12;
    




    public static final int WinAnonymousSid = 13;
    




    public static final int WinProxySid = 14;
    




    public static final int WinEnterpriseControllersSid = 15;
    




    public static final int WinSelfSid = 16;
    




    public static final int WinAuthenticatedUserSid = 17;
    




    public static final int WinRestrictedCodeSid = 18;
    




    public static final int WinTerminalServerSid = 19;
    




    public static final int WinRemoteLogonIdSid = 20;
    




    public static final int WinLogonIdsSid = 21;
    




    public static final int WinLocalSystemSid = 22;
    




    public static final int WinLocalServiceSid = 23;
    




    public static final int WinNetworkServiceSid = 24;
    




    public static final int WinBuiltinDomainSid = 25;
    




    public static final int WinBuiltinAdministratorsSid = 26;
    




    public static final int WinBuiltinUsersSid = 27;
    




    public static final int WinBuiltinGuestsSid = 28;
    




    public static final int WinBuiltinPowerUsersSid = 29;
    




    public static final int WinBuiltinAccountOperatorsSid = 30;
    




    public static final int WinBuiltinSystemOperatorsSid = 31;
    



    public static final int WinBuiltinPrintOperatorsSid = 32;
    



    public static final int WinBuiltinBackupOperatorsSid = 33;
    



    public static final int WinBuiltinReplicatorSid = 34;
    



    public static final int WinBuiltinPreWindows2000CompatibleAccessSid = 35;
    



    public static final int WinBuiltinRemoteDesktopUsersSid = 36;
    



    public static final int WinBuiltinNetworkConfigurationOperatorsSid = 37;
    



    public static final int WinAccountAdministratorSid = 38;
    



    public static final int WinAccountGuestSid = 39;
    



    public static final int WinAccountKrbtgtSid = 40;
    



    public static final int WinAccountDomainAdminsSid = 41;
    



    public static final int WinAccountDomainUsersSid = 42;
    



    public static final int WinAccountDomainGuestsSid = 43;
    



    public static final int WinAccountComputersSid = 44;
    



    public static final int WinAccountControllersSid = 45;
    



    public static final int WinAccountCertAdminsSid = 46;
    



    public static final int WinAccountSchemaAdminsSid = 47;
    



    public static final int WinAccountEnterpriseAdminsSid = 48;
    



    public static final int WinAccountPolicyAdminsSid = 49;
    



    public static final int WinAccountRasAndIasServersSid = 50;
    



    public static final int WinNTLMAuthenticationSid = 51;
    



    public static final int WinDigestAuthenticationSid = 52;
    



    public static final int WinSChannelAuthenticationSid = 53;
    



    public static final int WinThisOrganizationSid = 54;
    



    public static final int WinOtherOrganizationSid = 55;
    



    public static final int WinBuiltinIncomingForestTrustBuildersSid = 56;
    



    public static final int WinBuiltinPerfMonitoringUsersSid = 57;
    



    public static final int WinBuiltinPerfLoggingUsersSid = 58;
    



    public static final int WinBuiltinAuthorizationAccessSid = 59;
    



    public static final int WinBuiltinTerminalServerLicenseServersSid = 60;
    



    public static final int WinBuiltinDCOMUsersSid = 61;
    



    public static final int WinBuiltinIUsersSid = 62;
    



    public static final int WinIUserSid = 63;
    



    public static final int WinBuiltinCryptoOperatorsSid = 64;
    



    public static final int WinUntrustedLabelSid = 65;
    



    public static final int WinLowLabelSid = 66;
    



    public static final int WinMediumLabelSid = 67;
    



    public static final int WinHighLabelSid = 68;
    



    public static final int WinSystemLabelSid = 69;
    



    public static final int WinWriteRestrictedCodeSid = 70;
    



    public static final int WinCreatorOwnerRightsSid = 71;
    



    public static final int WinCacheablePrincipalsGroupSid = 72;
    



    public static final int WinNonCacheablePrincipalsGroupSid = 73;
    



    public static final int WinEnterpriseReadonlyControllersSid = 74;
    



    public static final int WinAccountReadonlyControllersSid = 75;
    



    public static final int WinBuiltinEventLogReadersGroup = 76;
    




    public WELL_KNOWN_SID_TYPE() {}
  }
  




  public static class OSVERSIONINFO
    extends Structure
  {
    public WinDef.DWORD dwOSVersionInfoSize;
    



    public WinDef.DWORD dwMajorVersion;
    



    public WinDef.DWORD dwMinorVersion;
    



    public WinDef.DWORD dwBuildNumber;
    



    public WinDef.DWORD dwPlatformId;
    



    public char[] szCSDVersion;
    




    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "dwOSVersionInfoSize", "dwMajorVersion", "dwMinorVersion", "dwBuildNumber", "dwPlatformId", "szCSDVersion" });
    }
    
    public OSVERSIONINFO() {
      szCSDVersion = new char[''];
      dwOSVersionInfoSize = new WinDef.DWORD(size());
    }
    
    public OSVERSIONINFO(Pointer memory) {
      super();
      read();
    }
  }
  





  public static class OSVERSIONINFOEX
    extends Structure
  {
    public WinDef.DWORD dwOSVersionInfoSize;
    




    public WinDef.DWORD dwMajorVersion;
    




    public WinDef.DWORD dwMinorVersion;
    




    public WinDef.DWORD dwBuildNumber;
    




    public WinDef.DWORD dwPlatformId;
    




    public char[] szCSDVersion;
    



    public WinDef.WORD wServicePackMajor;
    



    public WinDef.WORD wServicePackMinor;
    



    public WinDef.WORD wSuiteMask;
    



    public byte wProductType;
    



    public byte wReserved;
    




    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "dwOSVersionInfoSize", "dwMajorVersion", "dwMinorVersion", "dwBuildNumber", "dwPlatformId", "szCSDVersion", "wServicePackMajor", "wServicePackMinor", "wSuiteMask", "wProductType", "wReserved" });
    }
    
    public OSVERSIONINFOEX() {
      szCSDVersion = new char[''];
      dwOSVersionInfoSize = new WinDef.DWORD(size());
    }
    
    public OSVERSIONINFOEX(Pointer memory) {
      super();
      read();
    }
  }
  










  public static class EVENTLOGRECORD
    extends Structure
  {
    public WinDef.DWORD Length;
    









    public WinDef.DWORD Reserved;
    









    public WinDef.DWORD RecordNumber;
    









    public WinDef.DWORD TimeGenerated;
    









    public WinDef.DWORD TimeWritten;
    









    public WinDef.DWORD EventID;
    









    public WinDef.WORD EventType;
    









    public WinDef.WORD NumStrings;
    









    public WinDef.WORD EventCategory;
    









    public WinDef.WORD ReservedFlags;
    









    public WinDef.DWORD ClosingRecordNumber;
    









    public WinDef.DWORD StringOffset;
    









    public WinDef.DWORD UserSidLength;
    









    public WinDef.DWORD UserSidOffset;
    









    public WinDef.DWORD DataLength;
    









    public WinDef.DWORD DataOffset;
    









    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "Length", "Reserved", "RecordNumber", "TimeGenerated", "TimeWritten", "EventID", "EventType", "NumStrings", "EventCategory", "ReservedFlags", "ClosingRecordNumber", "StringOffset", "UserSidLength", "UserSidOffset", "DataLength", "DataOffset" });
    }
    
    public EVENTLOGRECORD() {}
    
    public EVENTLOGRECORD(Pointer p)
    {
      super();
      read();
    }
  }
  























  public static class SECURITY_DESCRIPTOR
    extends Structure
  {
    public byte[] data;
    























    public SECURITY_DESCRIPTOR() {}
    























    public SECURITY_DESCRIPTOR(byte[] data)
    {
      this.data = data;
      useMemory(new Memory(data.length));
    }
    
    public SECURITY_DESCRIPTOR(Pointer memory) {
      super();
      read();
    }
    



    protected List getFieldOrder() { return Arrays.asList(new String[] { "data" }); }
    
    public static class ByReference extends WinNT.SECURITY_DESCRIPTOR implements Structure.ByReference { public ByReference() {}
    } }
  
  public static class ACL extends Structure { public byte AclRevision;
    
    protected List getFieldOrder() { return Arrays.asList(new String[] { "AclRevision", "Sbz1", "AclSize", "AceCount", "Sbz2" }); }
    

    public ACL() {}
    
    public ACL(Pointer pointer)
    {
      super();
      read();
      ACEs = new WinNT.ACCESS_ACEStructure[AceCount];
      int offset = size();
      for (int i = 0; i < AceCount; i++) {
        Pointer share = pointer.share(offset);
        
        byte aceType = share.getByte(0L);
        WinNT.ACCESS_ACEStructure ace = null;
        switch (aceType) {
        case 0: 
          ace = new WinNT.ACCESS_ALLOWED_ACE(share);
          break;
        case 1: 
          ace = new WinNT.ACCESS_DENIED_ACE(share);
          break;
        default: 
          throw new IllegalArgumentException("Unknwon ACE type " + aceType);
        }
        
        ACEs[i] = ace;
        offset += AceSize;
      }
    }
    

    public byte Sbz1;
    
    public short AclSize;
    public short AceCount;
    public short Sbz2;
    private WinNT.ACCESS_ACEStructure[] ACEs;
    public WinNT.ACCESS_ACEStructure[] getACEStructures()
    {
      return ACEs;
    }
  }
  
  public static class SECURITY_DESCRIPTOR_RELATIVE
    extends Structure
  {
    public byte Revision;
    public byte Sbz1;
    public short Control;
    public int Owner;
    public int Group;
    public int Sacl;
    public int Dacl;
    private WinNT.ACL DACL;
    
    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "Revision", "Sbz1", "Control", "Owner", "Group", "Sacl", "Dacl" });
    }
    

    public SECURITY_DESCRIPTOR_RELATIVE() {}
    

    public SECURITY_DESCRIPTOR_RELATIVE(byte[] data)
    {
      super();
      getPointer().write(0L, data, 0, data.length);
      setDacl();
    }
    
    public SECURITY_DESCRIPTOR_RELATIVE(Pointer p) {
      super();
      setDacl();
    }
    
    public WinNT.ACL getDiscretionaryACL() {
      return DACL;
    }
    
    private final void setDacl() {
      read();
      if (Dacl != 0)
        DACL = new WinNT.ACL(getPointer().share(Dacl));
    }
    
    public static class ByReference extends WinNT.SECURITY_DESCRIPTOR_RELATIVE implements Structure.ByReference { public ByReference() {}
    }
  }
  
  public static abstract class ACEStructure extends Structure { public byte AceType;
    public byte AceFlags;
    public short AceSize;
    WinNT.PSID psid;
    
    public ACEStructure() {}
    
    public ACEStructure(Pointer p) { super(); }
    
    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "AceType", "AceFlags", "AceSize" });
    }
    
    public String getSidString() {
      return Advapi32Util.convertSidToStringSid(psid);
    }
    
    public WinNT.PSID getSID() {
      return psid;
    }
  }
  
  public static class ACE_HEADER extends WinNT.ACEStructure {
    public ACE_HEADER() {}
    
    public ACE_HEADER(Pointer p) {
      super();
      read();
    }
  }
  
  public static abstract class ACCESS_ACEStructure extends WinNT.ACEStructure {
    public int Mask;
    public WinDef.DWORD SidStart;
    
    protected List getFieldOrder() {
      List list = new ArrayList(super.getFieldOrder());
      list.addAll(Arrays.asList(new String[] { "Mask", "SidStart" }));
      return list; }
    
    public ACCESS_ACEStructure() {}
    
    public ACCESS_ACEStructure(Pointer p) { super();
      read();
      

      int sizeOfSID = AceSize - size() + 4;
      
      int offsetOfSID = 8;
      byte[] data = p.getByteArray(offsetOfSID, sizeOfSID);
      psid = new WinNT.PSID(data);
    }
  }
  


  public static class ACCESS_ALLOWED_ACE
    extends WinNT.ACCESS_ACEStructure
  {
    public ACCESS_ALLOWED_ACE() {}
    


    public ACCESS_ALLOWED_ACE(Pointer p)
    {
      super();
    }
  }
  
  public static class ACCESS_DENIED_ACE extends WinNT.ACCESS_ACEStructure {
    public ACCESS_DENIED_ACE() {}
    
    public ACCESS_DENIED_ACE(Pointer p) {
      super();
    }
  }
  





  public static abstract interface OVERLAPPED_COMPLETION_ROUTINE
    extends StdCallLibrary.StdCallCallback
  {
    public abstract void callback(int paramInt1, int paramInt2, WinBase.OVERLAPPED paramOVERLAPPED);
  }
  





  public static class GENERIC_MAPPING
    extends Structure
  {
    public WinDef.DWORD genericRead;
    




    public WinDef.DWORD genericWrite;
    



    public WinDef.DWORD genericExecute;
    



    public WinDef.DWORD genericAll;
    




    public GENERIC_MAPPING() {}
    




    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "genericRead", "genericWrite", "genericExecute", "genericAll" });
    }
    



    public static class ByReference
      extends WinNT.GENERIC_MAPPING
      implements Structure.ByReference
    {
      public ByReference() {}
    }
  }
  



  public static class SYSTEM_LOGICAL_PROCESSOR_INFORMATION
    extends Structure
  {
    public BaseTSD.ULONG_PTR processorMask;
    

    public int relationship;
    

    public AnonymousUnionPayload payload;
    


    public SYSTEM_LOGICAL_PROCESSOR_INFORMATION() {}
    


    public SYSTEM_LOGICAL_PROCESSOR_INFORMATION(Pointer memory)
    {
      super();
      read();
    }
    
    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "processorMask", "relationship", "payload" });
    }
    






    public static class AnonymousUnionPayload
      extends Union
    {
      public WinNT.SYSTEM_LOGICAL_PROCESSOR_INFORMATION.AnonymousStructProcessorCore processorCore;
      




      public WinNT.SYSTEM_LOGICAL_PROCESSOR_INFORMATION.AnonymousStructNumaNode numaNode;
      




      public WinNT.CACHE_DESCRIPTOR cache;
      




      public WinDef.ULONGLONG[] reserved = new WinDef.ULONGLONG[2];
      

      public AnonymousUnionPayload() {}
    }
    

    public static class AnonymousStructProcessorCore
      extends Structure
    {
      public WinDef.BYTE flags;
      
      public AnonymousStructProcessorCore() {}
      
      protected List getFieldOrder()
      {
        return Arrays.asList(new String[] { "flags" });
      }
    }
    
    public static class AnonymousStructNumaNode
      extends Structure
    {
      public WinDef.DWORD nodeNumber;
      
      public AnonymousStructNumaNode() {}
      
      protected List getFieldOrder()
      {
        return Arrays.asList(new String[] { "nodeNumber" });
      }
    }
  }
  





  public static abstract interface LOGICAL_PROCESSOR_RELATIONSHIP
  {
    public static final int RelationProcessorCore = 0;
    




    public static final int RelationNumaNode = 1;
    




    public static final int RelationCache = 2;
    




    public static final int RelationProcessorPackage = 3;
    




    public static final int RelationGroup = 4;
    




    public static final int RelationAll = 65535;
  }
  





  public static class CACHE_DESCRIPTOR
    extends Structure
  {
    public WinDef.BYTE level;
    




    public WinDef.BYTE associativity;
    



    public WinDef.WORD lineSize;
    



    public WinDef.DWORD size;
    



    public int type;
    




    public CACHE_DESCRIPTOR() {}
    




    protected List getFieldOrder()
    {
      return Arrays.asList(new String[] { "level", "associativity", "lineSize", "size", "type" });
    }
  }
  





  public static abstract class PROCESSOR_CACHE_TYPE
  {
    public static int CacheUnified = 0;
    



    public static int CacheInstruction = 1;
    



    public static int CacheData = 2;
    



    public static int CacheTrace = 3;
    
    public PROCESSOR_CACHE_TYPE() {}
  }
}
