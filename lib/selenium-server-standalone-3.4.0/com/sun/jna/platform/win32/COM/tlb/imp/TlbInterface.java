package com.sun.jna.platform.win32.COM.tlb.imp;

import com.sun.jna.platform.win32.COM.TypeInfoUtil;
import com.sun.jna.platform.win32.COM.TypeInfoUtil.TypeInfoDoc;
import com.sun.jna.platform.win32.COM.TypeLibUtil;
import com.sun.jna.platform.win32.COM.TypeLibUtil.TypeLibDoc;
import com.sun.jna.platform.win32.Guid.GUID;
import com.sun.jna.platform.win32.OaIdl.MEMBERID;
import com.sun.jna.platform.win32.OaIdl.TYPEATTR;
import com.sun.jna.platform.win32.OaIdl.VARDESC;
import com.sun.jna.platform.win32.OaIdl.VARDESC._VARDESC;
import com.sun.jna.platform.win32.Variant.VARIANT;
import com.sun.jna.platform.win32.WinDef.WORD;






















public class TlbInterface
  extends TlbBase
{
  public TlbInterface(int index, String packagename, TypeLibUtil typeLibUtil)
  {
    super(index, typeLibUtil, null);
    
    TypeLibUtil.TypeLibDoc typeLibDoc = this.typeLibUtil.getDocumentation(index);
    String docString = typeLibDoc.getDocString();
    
    if (typeLibDoc.getName().length() > 0) {
      name = typeLibDoc.getName();
    }
    logInfo("Type of kind 'Interface' found: " + name);
    
    createPackageName(packagename);
    createClassName(name);
    setFilename(name);
    

    TypeInfoUtil typeInfoUtil = typeLibUtil.getTypeInfoUtil(index);
    OaIdl.TYPEATTR typeAttr = typeInfoUtil.getTypeAttr();
    
    createJavaDocHeader(guid.toGuidString(), docString);
    
    int cVars = cVars.intValue();
    for (int i = 0; i < cVars; i++)
    {
      OaIdl.VARDESC varDesc = typeInfoUtil.getVarDesc(i);
      Variant.VARIANT constValue = _vardesc.lpvarValue;
      Object value = constValue.getValue();
      

      OaIdl.MEMBERID memberID = memid;
      

      TypeInfoUtil.TypeInfoDoc typeInfoDoc2 = typeInfoUtil.getDocumentation(memberID);
      content = (content + "\t\t//" + typeInfoDoc2.getName() + "\n");
      content = (content + "\t\tpublic static final int " + typeInfoDoc2.getName() + " = " + value.toString() + ";");
      

      if (i < cVars - 1) {
        content += "\n";
      }
    }
    createContent(content);
  }
  







  protected void createJavaDocHeader(String guid, String helpstring)
  {
    replaceVariable("uuid", guid);
    replaceVariable("helpstring", helpstring);
  }
  





  protected String getClassTemplate()
  {
    return "com/sun/jna/platform/win32/COM/tlb/imp/TlbInterface.template";
  }
}
