package com.sun.jna.platform.win32.COM.tlb.imp;

import com.sun.jna.platform.win32.COM.TypeInfoUtil;
import com.sun.jna.platform.win32.COM.TypeLibUtil;
import com.sun.jna.platform.win32.OaIdl.ELEMDESC;
import com.sun.jna.platform.win32.OaIdl.FUNCDESC;






























public class TlbFunctionDispId
  extends TlbAbstractMethod
{
  public TlbFunctionDispId(int count, int index, TypeLibUtil typeLibUtil, OaIdl.FUNCDESC funcDesc, TypeInfoUtil typeInfoUtil)
  {
    super(index, typeLibUtil, funcDesc, typeInfoUtil);
    
    String[] names = typeInfoUtil.getNames(memid, paramCount + 1);
    
    for (int i = 0; i < paramCount; i++) {
      OaIdl.ELEMDESC elemdesc = lprgelemdescParam.elemDescArg[i];
      String methodName = names[(i + 1)].toLowerCase();
      String type = getType(tdesc);
      String _methodName = replaceJavaKeyword(methodName);
      methodparams = (methodparams + type + " " + _methodName);
      

      if (type.equals("VARIANT")) {
        methodvariables += _methodName;
      } else {
        methodvariables = (methodvariables + "new VARIANT(" + _methodName + ")");
      }
      
      if (i < paramCount - 1) {
        methodparams += ", ";
        methodvariables += ", ";
      }
    }
    String returnValue;
    String returnValue;
    if (returnType.equalsIgnoreCase("VARIANT")) {
      returnValue = "pResult";
    } else {
      returnValue = "((" + returnType + ") pResult.getValue())";
    }
    replaceVariable("helpstring", docStr);
    replaceVariable("returntype", returnType);
    replaceVariable("returnvalue", returnValue);
    replaceVariable("methodname", this.methodName);
    replaceVariable("methodparams", methodparams);
    replaceVariable("methodvariables", methodvariables);
    replaceVariable("vtableid", String.valueOf(vtableId));
    replaceVariable("memberid", String.valueOf(memberid));
    replaceVariable("functionCount", String.valueOf(count));
  }
  





  protected String getClassTemplate()
  {
    return "com/sun/jna/platform/win32/COM/tlb/imp/TlbFunctionDispId.template";
  }
}
