package com.sun.jna.platform.win32.COM.tlb.imp;

import com.sun.jna.platform.win32.COM.TypeInfoUtil;
import com.sun.jna.platform.win32.COM.TypeInfoUtil.TypeInfoDoc;
import com.sun.jna.platform.win32.COM.TypeLibUtil;
import com.sun.jna.platform.win32.OaIdl.ELEMDESC;
import com.sun.jna.platform.win32.OaIdl.FUNCDESC;






























public class TlbFunctionStub
  extends TlbAbstractMethod
{
  public TlbFunctionStub(int index, TypeLibUtil typeLibUtil, OaIdl.FUNCDESC funcDesc, TypeInfoUtil typeInfoUtil)
  {
    super(index, typeLibUtil, funcDesc, typeInfoUtil);
    
    TypeInfoUtil.TypeInfoDoc typeInfoDoc = typeInfoUtil.getDocumentation(memid);
    String methodname = typeInfoDoc.getName();
    String docStr = typeInfoDoc.getDocString();
    String[] names = typeInfoUtil.getNames(memid, paramCount + 1);
    

    if (paramCount > 0) {
      methodvariables = ", ";
    }
    for (int i = 0; i < paramCount; i++) {
      OaIdl.ELEMDESC elemdesc = lprgelemdescParam.elemDescArg[i];
      String methodName = names[(i + 1)].toLowerCase();
      methodparams = (methodparams + getType(tdesc) + " " + replaceJavaKeyword(methodName));
      
      methodvariables += methodName;
      

      if (i < paramCount - 1) {
        methodparams += ", ";
        methodvariables += ", ";
      }
    }
    
    replaceVariable("helpstring", docStr);
    replaceVariable("returntype", returnType);
    replaceVariable("methodname", methodname);
    replaceVariable("methodparams", methodparams);
    replaceVariable("vtableid", String.valueOf(vtableId));
    replaceVariable("memberid", String.valueOf(memberid));
  }
  





  protected String getClassTemplate()
  {
    return "com/sun/jna/platform/win32/COM/tlb/imp/TlbFunctionStub.template";
  }
}
