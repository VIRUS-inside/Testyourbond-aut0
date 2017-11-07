package com.sun.jna.platform.win32.COM.tlb.imp;

import com.sun.jna.platform.win32.COM.TypeInfoUtil;
import com.sun.jna.platform.win32.COM.TypeLibUtil;
import com.sun.jna.platform.win32.OaIdl.FUNCDESC;






























public class TlbPropertyGet
  extends TlbAbstractMethod
{
  public TlbPropertyGet(int count, int index, TypeLibUtil typeLibUtil, OaIdl.FUNCDESC funcDesc, TypeInfoUtil typeInfoUtil)
  {
    super(index, typeLibUtil, funcDesc, typeInfoUtil);
    
    methodName = ("get" + getMethodName());
    
    replaceVariable("helpstring", docStr);
    replaceVariable("returntype", returnType);
    replaceVariable("methodname", methodName);
    replaceVariable("vtableid", String.valueOf(vtableId));
    replaceVariable("memberid", String.valueOf(memberid));
    replaceVariable("functionCount", String.valueOf(count));
  }
  





  protected String getClassTemplate()
  {
    return "com/sun/jna/platform/win32/COM/tlb/imp/TlbPropertyGet.template";
  }
}
