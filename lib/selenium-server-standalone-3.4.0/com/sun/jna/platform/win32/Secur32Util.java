package com.sun.jna.platform.win32;

import com.sun.jna.Native;
import com.sun.jna.WString;
import com.sun.jna.ptr.IntByReference;
import java.util.ArrayList;





































public abstract class Secur32Util
{
  public Secur32Util() {}
  
  public static String getUserNameEx(int format)
  {
    char[] buffer = new char[''];
    IntByReference len = new IntByReference(buffer.length);
    boolean result = Secur32.INSTANCE.GetUserNameEx(format, buffer, len);
    
    if (!result)
    {
      int rc = Kernel32.INSTANCE.GetLastError();
      
      switch (rc) {
      case 234: 
        buffer = new char[len.getValue() + 1];
        break;
      default: 
        throw new Win32Exception(Native.getLastError());
      }
      
      result = Secur32.INSTANCE.GetUserNameEx(format, buffer, len);
    }
    
    if (!result) {
      throw new Win32Exception(Native.getLastError());
    }
    
    return Native.toString(buffer);
  }
  




  public static SecurityPackage[] getSecurityPackages()
  {
    IntByReference pcPackages = new IntByReference();
    Sspi.PSecPkgInfo pPackageInfo = new Sspi.PSecPkgInfo();
    int rc = Secur32.INSTANCE.EnumerateSecurityPackages(pcPackages, pPackageInfo);
    if (0 != rc) {
      throw new Win32Exception(rc);
    }
    Sspi.SecPkgInfo[] packagesInfo = pPackageInfo.toArray(pcPackages.getValue());
    ArrayList<SecurityPackage> packages = new ArrayList(pcPackages.getValue());
    for (Sspi.SecPkgInfo packageInfo : packagesInfo) {
      SecurityPackage securityPackage = new SecurityPackage();
      name = Name.toString();
      comment = Comment.toString();
      packages.add(securityPackage);
    }
    rc = Secur32.INSTANCE.FreeContextBuffer(pPkgInfo.getPointer());
    if (0 != rc) {
      throw new Win32Exception(rc);
    }
    return (SecurityPackage[])packages.toArray(new SecurityPackage[0]);
  }
  
  public static class SecurityPackage
  {
    public String name;
    public String comment;
    
    public SecurityPackage() {}
  }
}
