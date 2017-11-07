package com.sun.jna.platform.win32.COM.tlb.imp;

import com.sun.jna.platform.win32.COM.TypeInfoUtil;
import com.sun.jna.platform.win32.COM.TypeLibUtil;
import com.sun.jna.platform.win32.OaIdl.ELEMDESC;
import com.sun.jna.platform.win32.OaIdl.FUNCDESC;






























public class TlbFunctionVTable
  extends TlbAbstractMethod
{
  public TlbFunctionVTable(int count, int index, TypeLibUtil typeLibUtil, OaIdl.FUNCDESC funcDesc, TypeInfoUtil typeInfoUtil)
  {
    super(index, typeLibUtil, funcDesc, typeInfoUtil);
    
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
    replaceVariable("methodname", this.methodName);
    replaceVariable("methodparams", methodparams);
    replaceVariable("methodvariables", methodvariables);
    replaceVariable("vtableid", String.valueOf(vtableId));
    replaceVariable("memberid", String.valueOf(memberid));
    replaceVariable("functionCount", String.valueOf(count));
  }
  





  protected String getClassTemplate()
  {
    return "com/sun/jna/platform/win32/COM/tlb/imp/TlbFunctionVTable.template";
  }
}
