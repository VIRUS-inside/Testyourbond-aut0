package com.sun.jna.platform.win32.COM.tlb.imp;

import com.sun.jna.platform.win32.COM.TypeInfoUtil;
import com.sun.jna.platform.win32.COM.TypeLibUtil;
import com.sun.jna.platform.win32.OaIdl.ELEMDESC;
import com.sun.jna.platform.win32.OaIdl.FUNCDESC;






























public class TlbPropertyPut
  extends TlbAbstractMethod
{
  public TlbPropertyPut(int count, int index, TypeLibUtil typeLibUtil, OaIdl.FUNCDESC funcDesc, TypeInfoUtil typeInfoUtil)
  {
    super(index, typeLibUtil, funcDesc, typeInfoUtil);
    
    methodName = ("set" + getMethodName());
    String[] names = typeInfoUtil.getNames(memid, paramCount + 1);
    
    if (paramCount > 0) {
      methodvariables += ", ";
    }
    for (int i = 0; i < paramCount; i++) {
      OaIdl.ELEMDESC elemdesc = lprgelemdescParam.elemDescArg[i];
      String varType = getType(elemdesc);
      methodparams = (methodparams + varType + " " + replaceJavaKeyword(names[i].toLowerCase()));
      
      methodvariables += replaceJavaKeyword(names[i].toLowerCase());
      

      if (i < paramCount - 1) {
        methodparams += ", ";
        methodvariables += ", ";
      }
    }
    
    replaceVariable("helpstring", docStr);
    replaceVariable("methodname", methodName);
    replaceVariable("methodparams", methodparams);
    replaceVariable("methodvariables", methodvariables);
    replaceVariable("vtableid", String.valueOf(vtableId));
    replaceVariable("memberid", String.valueOf(memberid));
    replaceVariable("functionCount", String.valueOf(count));
  }
  





  protected String getClassTemplate()
  {
    return "com/sun/jna/platform/win32/COM/tlb/imp/TlbPropertyPut.template";
  }
}
