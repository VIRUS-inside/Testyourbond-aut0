package org.apache.xerces.impl.dv.xs;

import org.apache.xerces.impl.dv.SchemaDVFactory;
import org.apache.xerces.impl.dv.XSFacets;
import org.apache.xerces.impl.dv.XSSimpleType;
import org.apache.xerces.util.SymbolHash;
import org.apache.xerces.xs.XSObjectList;

public class BaseDVFactory
  extends SchemaDVFactory
{
  static final String URI_SCHEMAFORSCHEMA = "http://www.w3.org/2001/XMLSchema";
  static SymbolHash fBaseTypes = new SymbolHash(53);
  
  public BaseDVFactory() {}
  
  public XSSimpleType getBuiltInType(String paramString)
  {
    return (XSSimpleType)fBaseTypes.get(paramString);
  }
  
  public SymbolHash getBuiltInTypes()
  {
    return fBaseTypes.makeClone();
  }
  
  public XSSimpleType createTypeRestriction(String paramString1, String paramString2, short paramShort, XSSimpleType paramXSSimpleType, XSObjectList paramXSObjectList)
  {
    return new XSSimpleTypeDecl((XSSimpleTypeDecl)paramXSSimpleType, paramString1, paramString2, paramShort, false, paramXSObjectList);
  }
  
  public XSSimpleType createTypeList(String paramString1, String paramString2, short paramShort, XSSimpleType paramXSSimpleType, XSObjectList paramXSObjectList)
  {
    return new XSSimpleTypeDecl(paramString1, paramString2, paramShort, (XSSimpleTypeDecl)paramXSSimpleType, false, paramXSObjectList);
  }
  
  public XSSimpleType createTypeUnion(String paramString1, String paramString2, short paramShort, XSSimpleType[] paramArrayOfXSSimpleType, XSObjectList paramXSObjectList)
  {
    int i = paramArrayOfXSSimpleType.length;
    XSSimpleTypeDecl[] arrayOfXSSimpleTypeDecl = new XSSimpleTypeDecl[i];
    System.arraycopy(paramArrayOfXSSimpleType, 0, arrayOfXSSimpleTypeDecl, 0, i);
    return new XSSimpleTypeDecl(paramString1, paramString2, paramShort, arrayOfXSSimpleTypeDecl, paramXSObjectList);
  }
  
  static void createBuiltInTypes(SymbolHash paramSymbolHash)
  {
    XSFacets localXSFacets = new XSFacets();
    XSSimpleTypeDecl localXSSimpleTypeDecl1 = XSSimpleTypeDecl.fAnySimpleType;
    paramSymbolHash.put("anySimpleType", localXSSimpleTypeDecl1);
    XSSimpleTypeDecl localXSSimpleTypeDecl2 = new XSSimpleTypeDecl(localXSSimpleTypeDecl1, "string", (short)1, (short)0, false, false, false, true, (short)2);
    paramSymbolHash.put("string", localXSSimpleTypeDecl2);
    paramSymbolHash.put("boolean", new XSSimpleTypeDecl(localXSSimpleTypeDecl1, "boolean", (short)2, (short)0, false, true, false, true, (short)3));
    XSSimpleTypeDecl localXSSimpleTypeDecl3 = new XSSimpleTypeDecl(localXSSimpleTypeDecl1, "decimal", (short)3, (short)2, false, false, true, true, (short)4);
    paramSymbolHash.put("decimal", localXSSimpleTypeDecl3);
    paramSymbolHash.put("anyURI", new XSSimpleTypeDecl(localXSSimpleTypeDecl1, "anyURI", (short)17, (short)0, false, false, false, true, (short)18));
    paramSymbolHash.put("base64Binary", new XSSimpleTypeDecl(localXSSimpleTypeDecl1, "base64Binary", (short)16, (short)0, false, false, false, true, (short)17));
    paramSymbolHash.put("dateTime", new XSSimpleTypeDecl(localXSSimpleTypeDecl1, "dateTime", (short)7, (short)1, false, false, false, true, (short)8));
    paramSymbolHash.put("time", new XSSimpleTypeDecl(localXSSimpleTypeDecl1, "time", (short)8, (short)1, false, false, false, true, (short)9));
    paramSymbolHash.put("date", new XSSimpleTypeDecl(localXSSimpleTypeDecl1, "date", (short)9, (short)1, false, false, false, true, (short)10));
    paramSymbolHash.put("gYearMonth", new XSSimpleTypeDecl(localXSSimpleTypeDecl1, "gYearMonth", (short)10, (short)1, false, false, false, true, (short)11));
    paramSymbolHash.put("gYear", new XSSimpleTypeDecl(localXSSimpleTypeDecl1, "gYear", (short)11, (short)1, false, false, false, true, (short)12));
    paramSymbolHash.put("gMonthDay", new XSSimpleTypeDecl(localXSSimpleTypeDecl1, "gMonthDay", (short)12, (short)1, false, false, false, true, (short)13));
    paramSymbolHash.put("gDay", new XSSimpleTypeDecl(localXSSimpleTypeDecl1, "gDay", (short)13, (short)1, false, false, false, true, (short)14));
    paramSymbolHash.put("gMonth", new XSSimpleTypeDecl(localXSSimpleTypeDecl1, "gMonth", (short)14, (short)1, false, false, false, true, (short)15));
    XSSimpleTypeDecl localXSSimpleTypeDecl4 = new XSSimpleTypeDecl(localXSSimpleTypeDecl3, "integer", (short)24, (short)2, false, false, true, true, (short)30);
    paramSymbolHash.put("integer", localXSSimpleTypeDecl4);
    maxInclusive = "0";
    XSSimpleTypeDecl localXSSimpleTypeDecl5 = new XSSimpleTypeDecl(localXSSimpleTypeDecl4, "nonPositiveInteger", "http://www.w3.org/2001/XMLSchema", (short)0, false, null, (short)31);
    localXSSimpleTypeDecl5.applyFacets1(localXSFacets, (short)32, (short)0);
    paramSymbolHash.put("nonPositiveInteger", localXSSimpleTypeDecl5);
    maxInclusive = "-1";
    XSSimpleTypeDecl localXSSimpleTypeDecl6 = new XSSimpleTypeDecl(localXSSimpleTypeDecl5, "negativeInteger", "http://www.w3.org/2001/XMLSchema", (short)0, false, null, (short)32);
    localXSSimpleTypeDecl6.applyFacets1(localXSFacets, (short)32, (short)0);
    paramSymbolHash.put("negativeInteger", localXSSimpleTypeDecl6);
    maxInclusive = "9223372036854775807";
    minInclusive = "-9223372036854775808";
    XSSimpleTypeDecl localXSSimpleTypeDecl7 = new XSSimpleTypeDecl(localXSSimpleTypeDecl4, "long", "http://www.w3.org/2001/XMLSchema", (short)0, false, null, (short)33);
    localXSSimpleTypeDecl7.applyFacets1(localXSFacets, (short)288, (short)0);
    paramSymbolHash.put("long", localXSSimpleTypeDecl7);
    maxInclusive = "2147483647";
    minInclusive = "-2147483648";
    XSSimpleTypeDecl localXSSimpleTypeDecl8 = new XSSimpleTypeDecl(localXSSimpleTypeDecl7, "int", "http://www.w3.org/2001/XMLSchema", (short)0, false, null, (short)34);
    localXSSimpleTypeDecl8.applyFacets1(localXSFacets, (short)288, (short)0);
    paramSymbolHash.put("int", localXSSimpleTypeDecl8);
    maxInclusive = "32767";
    minInclusive = "-32768";
    XSSimpleTypeDecl localXSSimpleTypeDecl9 = new XSSimpleTypeDecl(localXSSimpleTypeDecl8, "short", "http://www.w3.org/2001/XMLSchema", (short)0, false, null, (short)35);
    localXSSimpleTypeDecl9.applyFacets1(localXSFacets, (short)288, (short)0);
    paramSymbolHash.put("short", localXSSimpleTypeDecl9);
    maxInclusive = "127";
    minInclusive = "-128";
    XSSimpleTypeDecl localXSSimpleTypeDecl10 = new XSSimpleTypeDecl(localXSSimpleTypeDecl9, "byte", "http://www.w3.org/2001/XMLSchema", (short)0, false, null, (short)36);
    localXSSimpleTypeDecl10.applyFacets1(localXSFacets, (short)288, (short)0);
    paramSymbolHash.put("byte", localXSSimpleTypeDecl10);
    minInclusive = "0";
    XSSimpleTypeDecl localXSSimpleTypeDecl11 = new XSSimpleTypeDecl(localXSSimpleTypeDecl4, "nonNegativeInteger", "http://www.w3.org/2001/XMLSchema", (short)0, false, null, (short)37);
    localXSSimpleTypeDecl11.applyFacets1(localXSFacets, (short)256, (short)0);
    paramSymbolHash.put("nonNegativeInteger", localXSSimpleTypeDecl11);
    maxInclusive = "18446744073709551615";
    XSSimpleTypeDecl localXSSimpleTypeDecl12 = new XSSimpleTypeDecl(localXSSimpleTypeDecl11, "unsignedLong", "http://www.w3.org/2001/XMLSchema", (short)0, false, null, (short)38);
    localXSSimpleTypeDecl12.applyFacets1(localXSFacets, (short)32, (short)0);
    paramSymbolHash.put("unsignedLong", localXSSimpleTypeDecl12);
    maxInclusive = "4294967295";
    XSSimpleTypeDecl localXSSimpleTypeDecl13 = new XSSimpleTypeDecl(localXSSimpleTypeDecl12, "unsignedInt", "http://www.w3.org/2001/XMLSchema", (short)0, false, null, (short)39);
    localXSSimpleTypeDecl13.applyFacets1(localXSFacets, (short)32, (short)0);
    paramSymbolHash.put("unsignedInt", localXSSimpleTypeDecl13);
    maxInclusive = "65535";
    XSSimpleTypeDecl localXSSimpleTypeDecl14 = new XSSimpleTypeDecl(localXSSimpleTypeDecl13, "unsignedShort", "http://www.w3.org/2001/XMLSchema", (short)0, false, null, (short)40);
    localXSSimpleTypeDecl14.applyFacets1(localXSFacets, (short)32, (short)0);
    paramSymbolHash.put("unsignedShort", localXSSimpleTypeDecl14);
    maxInclusive = "255";
    XSSimpleTypeDecl localXSSimpleTypeDecl15 = new XSSimpleTypeDecl(localXSSimpleTypeDecl14, "unsignedByte", "http://www.w3.org/2001/XMLSchema", (short)0, false, null, (short)41);
    localXSSimpleTypeDecl15.applyFacets1(localXSFacets, (short)32, (short)0);
    paramSymbolHash.put("unsignedByte", localXSSimpleTypeDecl15);
    minInclusive = "1";
    XSSimpleTypeDecl localXSSimpleTypeDecl16 = new XSSimpleTypeDecl(localXSSimpleTypeDecl11, "positiveInteger", "http://www.w3.org/2001/XMLSchema", (short)0, false, null, (short)42);
    localXSSimpleTypeDecl16.applyFacets1(localXSFacets, (short)256, (short)0);
    paramSymbolHash.put("positiveInteger", localXSSimpleTypeDecl16);
  }
  
  static
  {
    createBuiltInTypes(fBaseTypes);
  }
}
