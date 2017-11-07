package com.sun.jna.platform.win32.COM.tlb.imp;

import com.sun.jna.platform.win32.COM.TypeInfoUtil;
import com.sun.jna.platform.win32.COM.TypeInfoUtil.TypeInfoDoc;
import com.sun.jna.platform.win32.COM.TypeLibUtil;
import com.sun.jna.platform.win32.OaIdl.ELEMDESC;
import com.sun.jna.platform.win32.OaIdl.FUNCDESC;






























public class TlbPropertyPutStub
  extends TlbAbstractMethod
{
  public TlbPropertyPutStub(int index, TypeLibUtil typeLibUtil, OaIdl.FUNCDESC funcDesc, TypeInfoUtil typeInfoUtil)
  {
    super(index, typeLibUtil, funcDesc, typeInfoUtil);
    
    TypeInfoUtil.TypeInfoDoc typeInfoDoc = typeInfoUtil.getDocumentation(memid);
    String docStr = typeInfoDoc.getDocString();
    String methodname = "set" + typeInfoDoc.getName();
    String[] names = typeInfoUtil.getNames(memid, paramCount + 1);
    
    for (int i = 0; i < paramCount; i++) {
      OaIdl.ELEMDESC elemdesc = lprgelemdescParam.elemDescArg[i];
      String varType = getType(elemdesc);
      methodparams = (methodparams + varType + " " + replaceJavaKeyword(names[i].toLowerCase()));
      


      if (i < paramCount - 1) {
        methodparams += ", ";
      }
    }
    
    replaceVariable("helpstring", docStr);
    replaceVariable("methodname", methodname);
    replaceVariable("methodparams", methodparams);
    replaceVariable("vtableid", String.valueOf(vtableId));
    replaceVariable("memberid", String.valueOf(memberid));
  }
  





  protected String getClassTemplate()
  {
    return "com/sun/jna/platform/win32/COM/tlb/imp/TlbPropertyPutStub.template";
  }
}
