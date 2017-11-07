package com.sun.jna.platform.win32.COM.tlb.imp;

import com.sun.jna.platform.win32.COM.TypeInfoUtil;
import com.sun.jna.platform.win32.COM.TypeInfoUtil.TypeInfoDoc;
import com.sun.jna.platform.win32.COM.TypeLibUtil;
import com.sun.jna.platform.win32.COM.TypeLibUtil.TypeLibDoc;
import com.sun.jna.platform.win32.Guid.GUID;
import com.sun.jna.platform.win32.OaIdl.FUNCDESC;
import com.sun.jna.platform.win32.OaIdl.INVOKEKIND;
import com.sun.jna.platform.win32.OaIdl.MEMBERID;
import com.sun.jna.platform.win32.OaIdl.TYPEATTR;
import com.sun.jna.platform.win32.WinDef.WORD;



























public class TlbDispInterface
  extends TlbBase
{
  public TlbDispInterface(int index, String packagename, TypeLibUtil typeLibUtil)
  {
    super(index, typeLibUtil, null);
    
    TypeLibUtil.TypeLibDoc typeLibDoc = this.typeLibUtil.getDocumentation(index);
    String docString = typeLibDoc.getDocString();
    
    if (typeLibDoc.getName().length() > 0) {
      name = typeLibDoc.getName();
    }
    logInfo("Type of kind 'DispInterface' found: " + name);
    
    createPackageName(packagename);
    createClassName(name);
    setFilename(name);
    

    TypeInfoUtil typeInfoUtil = typeLibUtil.getTypeInfoUtil(index);
    OaIdl.TYPEATTR typeAttr = typeInfoUtil.getTypeAttr();
    
    createJavaDocHeader(guid.toGuidString(), docString);
    
    int cFuncs = cFuncs.intValue();
    for (int i = 0; i < cFuncs; i++)
    {
      OaIdl.FUNCDESC funcDesc = typeInfoUtil.getFuncDesc(i);
      

      OaIdl.MEMBERID memberID = memid;
      

      TypeInfoUtil.TypeInfoDoc typeInfoDoc2 = typeInfoUtil.getDocumentation(memberID);
      String methodName = typeInfoDoc2.getName();
      TlbAbstractMethod method = null;
      
      if (!isReservedMethod(methodName)) {
        if (invkind.equals(OaIdl.INVOKEKIND.INVOKE_FUNC)) {
          method = new TlbFunctionStub(index, typeLibUtil, funcDesc, typeInfoUtil);
        }
        else if (invkind.equals(OaIdl.INVOKEKIND.INVOKE_PROPERTYGET))
        {
          method = new TlbPropertyGetStub(index, typeLibUtil, funcDesc, typeInfoUtil);
        }
        else if (invkind.equals(OaIdl.INVOKEKIND.INVOKE_PROPERTYPUT))
        {
          method = new TlbPropertyPutStub(index, typeLibUtil, funcDesc, typeInfoUtil);
        }
        else if (invkind.equals(OaIdl.INVOKEKIND.INVOKE_PROPERTYPUTREF))
        {
          method = new TlbPropertyPutStub(index, typeLibUtil, funcDesc, typeInfoUtil);
        }
        

        content += method.getClassBuffer();
        
        if (i < cFuncs - 1) {
          content += "\n";
        }
      }
      
      typeInfoUtil.ReleaseFuncDesc(funcDesc);
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
    return "com/sun/jna/platform/win32/COM/tlb/imp/TlbDispInterface.template";
  }
}
