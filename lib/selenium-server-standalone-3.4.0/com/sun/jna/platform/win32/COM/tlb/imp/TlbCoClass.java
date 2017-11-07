package com.sun.jna.platform.win32.COM.tlb.imp;

import com.sun.jna.platform.win32.COM.ITypeInfo;
import com.sun.jna.platform.win32.COM.TypeInfoUtil;
import com.sun.jna.platform.win32.COM.TypeInfoUtil.TypeInfoDoc;
import com.sun.jna.platform.win32.COM.TypeLibUtil;
import com.sun.jna.platform.win32.COM.TypeLibUtil.TypeLibDoc;
import com.sun.jna.platform.win32.Guid.GUID;
import com.sun.jna.platform.win32.OaIdl.FUNCDESC;
import com.sun.jna.platform.win32.OaIdl.HREFTYPE;
import com.sun.jna.platform.win32.OaIdl.INVOKEKIND;
import com.sun.jna.platform.win32.OaIdl.MEMBERID;
import com.sun.jna.platform.win32.OaIdl.TLIBATTR;
import com.sun.jna.platform.win32.OaIdl.TYPEATTR;
import com.sun.jna.platform.win32.WinDef.WORD;






















public class TlbCoClass
  extends TlbBase
{
  public TlbCoClass(int index, String packagename, TypeLibUtil typeLibUtil, String bindingMode)
  {
    super(index, typeLibUtil, null);
    
    TypeInfoUtil typeInfoUtil = typeLibUtil.getTypeInfoUtil(index);
    
    TypeLibUtil.TypeLibDoc typeLibDoc = this.typeLibUtil.getDocumentation(index);
    String docString = typeLibDoc.getDocString();
    
    if (typeLibDoc.getName().length() > 0) {
      name = typeLibDoc.getName();
    }
    logInfo("Type of kind 'CoClass' found: " + name);
    
    createPackageName(packagename);
    createClassName(name);
    setFilename(name);
    
    String guidStr = typeLibUtilgetLibAttrguid.toGuidString();
    int majorVerNum = typeLibUtilgetLibAttrwMajorVerNum.intValue();
    int minorVerNum = typeLibUtilgetLibAttrwMinorVerNum.intValue();
    String version = majorVerNum + "." + minorVerNum;
    String clsid = getTypeAttrguid.toGuidString();
    
    createJavaDocHeader(guidStr, version, docString);
    createCLSID(clsid);
    createCLSIDName(name);
    

    OaIdl.TYPEATTR typeAttr = typeInfoUtil.getTypeAttr();
    int cImplTypes = cImplTypes.intValue();
    String interfaces = "";
    
    for (int i = 0; i < cImplTypes; i++) {
      OaIdl.HREFTYPE refTypeOfImplType = typeInfoUtil.getRefTypeOfImplType(i);
      ITypeInfo refTypeInfo = typeInfoUtil.getRefTypeInfo(refTypeOfImplType);
      
      TypeInfoUtil refTypeInfoUtil = new TypeInfoUtil(refTypeInfo);
      createFunctions(refTypeInfoUtil, bindingMode);
      TypeInfoUtil.TypeInfoDoc documentation = refTypeInfoUtil.getDocumentation(new OaIdl.MEMBERID(-1));
      
      interfaces = interfaces + documentation.getName();
      
      if (i < cImplTypes - 1) {
        interfaces = interfaces + ", ";
      }
    }
    createInterfaces(interfaces);
    createContent(content);
  }
  
  protected void createFunctions(TypeInfoUtil typeInfoUtil, String bindingMode) {
    OaIdl.TYPEATTR typeAttr = typeInfoUtil.getTypeAttr();
    int cFuncs = cFuncs.intValue();
    for (int i = 0; i < cFuncs; i++)
    {
      OaIdl.FUNCDESC funcDesc = typeInfoUtil.getFuncDesc(i);
      
      TlbAbstractMethod method = null;
      if (invkind.equals(OaIdl.INVOKEKIND.INVOKE_FUNC)) {
        if (isVTableMode()) {
          method = new TlbFunctionVTable(i, index, typeLibUtil, funcDesc, typeInfoUtil);
        } else
          method = new TlbFunctionDispId(i, index, typeLibUtil, funcDesc, typeInfoUtil);
      } else if (invkind.equals(OaIdl.INVOKEKIND.INVOKE_PROPERTYGET)) {
        method = new TlbPropertyGet(i, index, typeLibUtil, funcDesc, typeInfoUtil);
      }
      else if (invkind.equals(OaIdl.INVOKEKIND.INVOKE_PROPERTYPUT)) {
        method = new TlbPropertyPut(i, index, typeLibUtil, funcDesc, typeInfoUtil);
      }
      else if (invkind.equals(OaIdl.INVOKEKIND.INVOKE_PROPERTYPUTREF))
      {
        method = new TlbPropertyPut(i, index, typeLibUtil, funcDesc, typeInfoUtil);
      }
      

      if (!isReservedMethod(method.getMethodName()))
      {
        content += method.getClassBuffer();
        
        if (i < cFuncs - 1) {
          content += "\n";
        }
      }
      
      typeInfoUtil.ReleaseFuncDesc(funcDesc);
    }
  }
  
  protected void createJavaDocHeader(String guid, String version, String helpstring)
  {
    replaceVariable("uuid", guid);
    replaceVariable("version", version);
    replaceVariable("helpstring", helpstring);
  }
  
  protected void createCLSIDName(String clsidName) {
    replaceVariable("clsidname", clsidName.toUpperCase());
  }
  
  protected void createCLSID(String clsid) {
    replaceVariable("clsid", clsid);
  }
  
  protected void createInterfaces(String interfaces) {
    replaceVariable("interfaces", interfaces);
  }
  





  protected String getClassTemplate()
  {
    return "com/sun/jna/platform/win32/COM/tlb/imp/TlbCoClass.template";
  }
}
