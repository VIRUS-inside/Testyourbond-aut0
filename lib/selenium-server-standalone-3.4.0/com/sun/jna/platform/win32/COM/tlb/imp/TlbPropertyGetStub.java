package com.sun.jna.platform.win32.COM.tlb.imp;

import com.sun.jna.platform.win32.COM.TypeInfoUtil;
import com.sun.jna.platform.win32.COM.TypeInfoUtil.TypeInfoDoc;
import com.sun.jna.platform.win32.COM.TypeLibUtil;
import com.sun.jna.platform.win32.OaIdl.FUNCDESC;






























public class TlbPropertyGetStub
  extends TlbAbstractMethod
{
  public TlbPropertyGetStub(int index, TypeLibUtil typeLibUtil, OaIdl.FUNCDESC funcDesc, TypeInfoUtil typeInfoUtil)
  {
    super(index, typeLibUtil, funcDesc, typeInfoUtil);
    
    TypeInfoUtil.TypeInfoDoc typeInfoDoc = typeInfoUtil.getDocumentation(memid);
    String docStr = typeInfoDoc.getDocString();
    String methodname = "get" + typeInfoDoc.getName();
    
    replaceVariable("helpstring", docStr);
    replaceVariable("returntype", returnType);
    replaceVariable("methodname", methodname);
    replaceVariable("vtableid", String.valueOf(vtableId));
    replaceVariable("memberid", String.valueOf(memberid));
  }
  





  protected String getClassTemplate()
  {
    return "com/sun/jna/platform/win32/COM/tlb/imp/TlbPropertyGetStub.template";
  }
}
