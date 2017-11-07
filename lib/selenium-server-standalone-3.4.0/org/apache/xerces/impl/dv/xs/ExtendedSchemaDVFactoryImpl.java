package org.apache.xerces.impl.dv.xs;

import org.apache.xerces.impl.dv.XSSimpleType;
import org.apache.xerces.util.SymbolHash;

public class ExtendedSchemaDVFactoryImpl
  extends BaseSchemaDVFactory
{
  static SymbolHash fBuiltInTypes = new SymbolHash();
  
  public ExtendedSchemaDVFactoryImpl() {}
  
  static void createBuiltInTypes()
  {
    BaseSchemaDVFactory.createBuiltInTypes(fBuiltInTypes, XSSimpleTypeDecl.fAnyAtomicType);
    fBuiltInTypes.put("anyAtomicType", XSSimpleTypeDecl.fAnyAtomicType);
    XSSimpleTypeDecl localXSSimpleTypeDecl = (XSSimpleTypeDecl)fBuiltInTypes.get("duration");
    fBuiltInTypes.put("yearMonthDuration", new XSSimpleTypeDecl(localXSSimpleTypeDecl, "yearMonthDuration", (short)27, (short)1, false, false, false, true, (short)46));
    fBuiltInTypes.put("dayTimeDuration", new XSSimpleTypeDecl(localXSSimpleTypeDecl, "dayTimeDuration", (short)28, (short)1, false, false, false, true, (short)47));
  }
  
  public XSSimpleType getBuiltInType(String paramString)
  {
    return (XSSimpleType)fBuiltInTypes.get(paramString);
  }
  
  public SymbolHash getBuiltInTypes()
  {
    return fBuiltInTypes.makeClone();
  }
  
  static
  {
    createBuiltInTypes();
  }
}
