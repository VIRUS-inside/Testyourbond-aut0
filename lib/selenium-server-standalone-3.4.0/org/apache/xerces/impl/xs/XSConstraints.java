package org.apache.xerces.impl.xs;

import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;
import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidatedInfo;
import org.apache.xerces.impl.dv.ValidationContext;
import org.apache.xerces.impl.dv.XSSimpleType;
import org.apache.xerces.impl.xs.models.CMBuilder;
import org.apache.xerces.impl.xs.models.XSCMValidator;
import org.apache.xerces.impl.xs.util.SimpleLocator;
import org.apache.xerces.impl.xs.util.XSObjectListImpl;
import org.apache.xerces.util.SymbolHash;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSTypeDefinition;

public class XSConstraints
{
  static final int OCCURRENCE_UNKNOWN = -2;
  static final XSSimpleType STRING_TYPE = (XSSimpleType)SchemaGrammar.SG_SchemaNS.getGlobalTypeDecl("string");
  private static XSParticleDecl fEmptyParticle = null;
  private static final Comparator ELEMENT_PARTICLE_COMPARATOR = new Comparator()
  {
    public int compare(Object paramAnonymousObject1, Object paramAnonymousObject2)
    {
      XSParticleDecl localXSParticleDecl1 = (XSParticleDecl)paramAnonymousObject1;
      XSParticleDecl localXSParticleDecl2 = (XSParticleDecl)paramAnonymousObject2;
      XSElementDecl localXSElementDecl1 = (XSElementDecl)fValue;
      XSElementDecl localXSElementDecl2 = (XSElementDecl)fValue;
      String str1 = localXSElementDecl1.getNamespace();
      String str2 = localXSElementDecl2.getNamespace();
      String str3 = localXSElementDecl1.getName();
      String str4 = localXSElementDecl2.getName();
      int i = str1 == str2 ? 1 : 0;
      int j = 0;
      if (i == 0) {
        if (str1 != null)
        {
          if (str2 != null) {
            j = str1.compareTo(str2);
          } else {
            j = 1;
          }
        }
        else {
          j = -1;
        }
      }
      return j != 0 ? j : str3.compareTo(str4);
    }
  };
  
  public XSConstraints() {}
  
  public static XSParticleDecl getEmptySequence()
  {
    if (fEmptyParticle == null)
    {
      XSModelGroupImpl localXSModelGroupImpl = new XSModelGroupImpl();
      fCompositor = 102;
      fParticleCount = 0;
      fParticles = null;
      fAnnotations = XSObjectListImpl.EMPTY_LIST;
      XSParticleDecl localXSParticleDecl = new XSParticleDecl();
      fType = 3;
      fValue = localXSModelGroupImpl;
      fAnnotations = XSObjectListImpl.EMPTY_LIST;
      fEmptyParticle = localXSParticleDecl;
    }
    return fEmptyParticle;
  }
  
  public static boolean checkTypeDerivationOk(XSTypeDefinition paramXSTypeDefinition1, XSTypeDefinition paramXSTypeDefinition2, short paramShort)
  {
    if (paramXSTypeDefinition1 == SchemaGrammar.fAnyType) {
      return paramXSTypeDefinition1 == paramXSTypeDefinition2;
    }
    if (paramXSTypeDefinition1 == SchemaGrammar.fAnySimpleType) {
      return (paramXSTypeDefinition2 == SchemaGrammar.fAnyType) || (paramXSTypeDefinition2 == SchemaGrammar.fAnySimpleType);
    }
    if (paramXSTypeDefinition1.getTypeCategory() == 16)
    {
      if (paramXSTypeDefinition2.getTypeCategory() == 15) {
        if (paramXSTypeDefinition2 == SchemaGrammar.fAnyType) {
          paramXSTypeDefinition2 = SchemaGrammar.fAnySimpleType;
        } else {
          return false;
        }
      }
      return checkSimpleDerivation((XSSimpleType)paramXSTypeDefinition1, (XSSimpleType)paramXSTypeDefinition2, paramShort);
    }
    return checkComplexDerivation((XSComplexTypeDecl)paramXSTypeDefinition1, paramXSTypeDefinition2, paramShort);
  }
  
  public static boolean checkSimpleDerivationOk(XSSimpleType paramXSSimpleType, XSTypeDefinition paramXSTypeDefinition, short paramShort)
  {
    if (paramXSSimpleType == SchemaGrammar.fAnySimpleType) {
      return (paramXSTypeDefinition == SchemaGrammar.fAnyType) || (paramXSTypeDefinition == SchemaGrammar.fAnySimpleType);
    }
    if (paramXSTypeDefinition.getTypeCategory() == 15) {
      if (paramXSTypeDefinition == SchemaGrammar.fAnyType) {
        paramXSTypeDefinition = SchemaGrammar.fAnySimpleType;
      } else {
        return false;
      }
    }
    return checkSimpleDerivation(paramXSSimpleType, (XSSimpleType)paramXSTypeDefinition, paramShort);
  }
  
  public static boolean checkComplexDerivationOk(XSComplexTypeDecl paramXSComplexTypeDecl, XSTypeDefinition paramXSTypeDefinition, short paramShort)
  {
    if (paramXSComplexTypeDecl == SchemaGrammar.fAnyType) {
      return paramXSComplexTypeDecl == paramXSTypeDefinition;
    }
    return checkComplexDerivation(paramXSComplexTypeDecl, paramXSTypeDefinition, paramShort);
  }
  
  private static boolean checkSimpleDerivation(XSSimpleType paramXSSimpleType1, XSSimpleType paramXSSimpleType2, short paramShort)
  {
    if (paramXSSimpleType1 == paramXSSimpleType2) {
      return true;
    }
    if (((paramShort & 0x2) != 0) || ((paramXSSimpleType1.getBaseType().getFinal() & 0x2) != 0)) {
      return false;
    }
    XSSimpleType localXSSimpleType = (XSSimpleType)paramXSSimpleType1.getBaseType();
    if (localXSSimpleType == paramXSSimpleType2) {
      return true;
    }
    if ((localXSSimpleType != SchemaGrammar.fAnySimpleType) && (checkSimpleDerivation(localXSSimpleType, paramXSSimpleType2, paramShort))) {
      return true;
    }
    if (((paramXSSimpleType1.getVariety() == 2) || (paramXSSimpleType1.getVariety() == 3)) && (paramXSSimpleType2 == SchemaGrammar.fAnySimpleType)) {
      return true;
    }
    if (paramXSSimpleType2.getVariety() == 3)
    {
      XSObjectList localXSObjectList = paramXSSimpleType2.getMemberTypes();
      int i = localXSObjectList.getLength();
      for (int j = 0; j < i; j++)
      {
        paramXSSimpleType2 = (XSSimpleType)localXSObjectList.item(j);
        if (checkSimpleDerivation(paramXSSimpleType1, paramXSSimpleType2, paramShort)) {
          return true;
        }
      }
    }
    return false;
  }
  
  private static boolean checkComplexDerivation(XSComplexTypeDecl paramXSComplexTypeDecl, XSTypeDefinition paramXSTypeDefinition, short paramShort)
  {
    if (paramXSComplexTypeDecl == paramXSTypeDefinition) {
      return true;
    }
    if ((fDerivedBy & paramShort) != 0) {
      return false;
    }
    XSTypeDefinition localXSTypeDefinition = fBaseType;
    if (localXSTypeDefinition == paramXSTypeDefinition) {
      return true;
    }
    if ((localXSTypeDefinition == SchemaGrammar.fAnyType) || (localXSTypeDefinition == SchemaGrammar.fAnySimpleType)) {
      return false;
    }
    if (localXSTypeDefinition.getTypeCategory() == 15) {
      return checkComplexDerivation((XSComplexTypeDecl)localXSTypeDefinition, paramXSTypeDefinition, paramShort);
    }
    if (localXSTypeDefinition.getTypeCategory() == 16)
    {
      if (paramXSTypeDefinition.getTypeCategory() == 15) {
        if (paramXSTypeDefinition == SchemaGrammar.fAnyType) {
          paramXSTypeDefinition = SchemaGrammar.fAnySimpleType;
        } else {
          return false;
        }
      }
      return checkSimpleDerivation((XSSimpleType)localXSTypeDefinition, (XSSimpleType)paramXSTypeDefinition, paramShort);
    }
    return false;
  }
  
  public static Object ElementDefaultValidImmediate(XSTypeDefinition paramXSTypeDefinition, String paramString, ValidationContext paramValidationContext, ValidatedInfo paramValidatedInfo)
  {
    XSSimpleType localXSSimpleType = null;
    if (paramXSTypeDefinition.getTypeCategory() == 16)
    {
      localXSSimpleType = (XSSimpleType)paramXSTypeDefinition;
    }
    else
    {
      localObject = (XSComplexTypeDecl)paramXSTypeDefinition;
      if (fContentType == 1) {
        localXSSimpleType = fXSSimpleType;
      } else if (fContentType == 3)
      {
        if (!((XSParticleDecl)((XSComplexTypeDecl)localObject).getParticle()).emptiable()) {
          return null;
        }
      }
      else {
        return null;
      }
    }
    Object localObject = null;
    if (localXSSimpleType == null) {
      localXSSimpleType = STRING_TYPE;
    }
    try
    {
      localObject = localXSSimpleType.validate(paramString, paramValidationContext, paramValidatedInfo);
      if (paramValidatedInfo != null) {
        localObject = localXSSimpleType.validate(paramValidatedInfo.stringValue(), paramValidationContext, paramValidatedInfo);
      }
    }
    catch (InvalidDatatypeValueException localInvalidDatatypeValueException)
    {
      return null;
    }
    return localObject;
  }
  
  static void reportSchemaError(XMLErrorReporter paramXMLErrorReporter, SimpleLocator paramSimpleLocator, String paramString, Object[] paramArrayOfObject)
  {
    if (paramSimpleLocator != null) {
      paramXMLErrorReporter.reportError(paramSimpleLocator, "http://www.w3.org/TR/xml-schema-1", paramString, paramArrayOfObject, (short)1);
    } else {
      paramXMLErrorReporter.reportError("http://www.w3.org/TR/xml-schema-1", paramString, paramArrayOfObject, (short)1);
    }
  }
  
  public static void fullSchemaChecking(XSGrammarBucket paramXSGrammarBucket, SubstitutionGroupHandler paramSubstitutionGroupHandler, CMBuilder paramCMBuilder, XMLErrorReporter paramXMLErrorReporter)
  {
    SchemaGrammar[] arrayOfSchemaGrammar = paramXSGrammarBucket.getGrammars();
    for (int i = arrayOfSchemaGrammar.length - 1; i >= 0; i--) {
      paramSubstitutionGroupHandler.addSubstitutionGroup(arrayOfSchemaGrammar[i].getSubstitutionGroups());
    }
    XSParticleDecl localXSParticleDecl1 = new XSParticleDecl();
    XSParticleDecl localXSParticleDecl2 = new XSParticleDecl();
    fType = 3;
    fType = 3;
    Object localObject1;
    SimpleLocator[] arrayOfSimpleLocator;
    int k;
    for (int j = arrayOfSchemaGrammar.length - 1; j >= 0; j--)
    {
      localObject1 = arrayOfSchemaGrammar[j].getRedefinedGroupDecls();
      arrayOfSimpleLocator = arrayOfSchemaGrammar[j].getRGLocators();
      k = 0;
      while (k < localObject1.length)
      {
        Object localObject2 = localObject1[(k++)];
        XSModelGroupImpl localXSModelGroupImpl1 = fModelGroup;
        localSymbolHash = localObject1[(k++)];
        XSModelGroupImpl localXSModelGroupImpl2 = fModelGroup;
        fValue = localXSModelGroupImpl1;
        fValue = localXSModelGroupImpl2;
        if (localXSModelGroupImpl2 == null)
        {
          if (localXSModelGroupImpl1 != null) {
            reportSchemaError(paramXMLErrorReporter, arrayOfSimpleLocator[(k / 2 - 1)], "src-redefine.6.2.2", new Object[] { fName, "rcase-Recurse.2" });
          }
        }
        else if (localXSModelGroupImpl1 == null)
        {
          if (!localXSParticleDecl2.emptiable()) {
            reportSchemaError(paramXMLErrorReporter, arrayOfSimpleLocator[(k / 2 - 1)], "src-redefine.6.2.2", new Object[] { fName, "rcase-Recurse.2" });
          }
        }
        else {
          try
          {
            particleValidRestriction(localXSParticleDecl1, paramSubstitutionGroupHandler, localXSParticleDecl2, paramSubstitutionGroupHandler);
          }
          catch (XMLSchemaException localXMLSchemaException1)
          {
            String str = localXMLSchemaException1.getKey();
            reportSchemaError(paramXMLErrorReporter, arrayOfSimpleLocator[(k / 2 - 1)], str, localXMLSchemaException1.getArgs());
            reportSchemaError(paramXMLErrorReporter, arrayOfSimpleLocator[(k / 2 - 1)], "src-redefine.6.2.2", new Object[] { fName, str });
          }
        }
      }
    }
    SymbolHash localSymbolHash = new SymbolHash();
    for (int n = arrayOfSchemaGrammar.length - 1; n >= 0; n--)
    {
      int m = 0;
      boolean bool2 = fFullChecked;
      localObject1 = arrayOfSchemaGrammar[n].getUncheckedComplexTypeDecls();
      arrayOfSimpleLocator = arrayOfSchemaGrammar[n].getUncheckedCTLocators();
      for (int i1 = 0; i1 < localObject1.length; i1++)
      {
        if ((!bool2) && (fParticle != null))
        {
          localSymbolHash.clear();
          try
          {
            checkElementDeclsConsistent(localObject1[i1], fParticle, localSymbolHash, paramSubstitutionGroupHandler);
          }
          catch (XMLSchemaException localXMLSchemaException2)
          {
            reportSchemaError(paramXMLErrorReporter, arrayOfSimpleLocator[i1], localXMLSchemaException2.getKey(), localXMLSchemaException2.getArgs());
          }
        }
        if ((fBaseType != null) && (fBaseType != SchemaGrammar.fAnyType) && (fDerivedBy == 2) && ((fBaseType instanceof XSComplexTypeDecl)))
        {
          localObject3 = fParticle;
          XSParticleDecl localXSParticleDecl3 = fBaseType).fParticle;
          if (localObject3 == null)
          {
            if ((localXSParticleDecl3 != null) && (!localXSParticleDecl3.emptiable())) {
              reportSchemaError(paramXMLErrorReporter, arrayOfSimpleLocator[i1], "derivation-ok-restriction.5.3.2", new Object[] { fName, fBaseType.getName() });
            }
          }
          else if (localXSParticleDecl3 != null) {
            try
            {
              particleValidRestriction(fParticle, paramSubstitutionGroupHandler, fBaseType).fParticle, paramSubstitutionGroupHandler);
            }
            catch (XMLSchemaException localXMLSchemaException4)
            {
              reportSchemaError(paramXMLErrorReporter, arrayOfSimpleLocator[i1], localXMLSchemaException4.getKey(), localXMLSchemaException4.getArgs());
              reportSchemaError(paramXMLErrorReporter, arrayOfSimpleLocator[i1], "derivation-ok-restriction.5.4.2", new Object[] { fName });
            }
          } else {
            reportSchemaError(paramXMLErrorReporter, arrayOfSimpleLocator[i1], "derivation-ok-restriction.5.4.2", new Object[] { fName });
          }
        }
        Object localObject3 = localObject1[i1].getContentModel(paramCMBuilder, true);
        k = 0;
        boolean bool1;
        if (localObject3 != null) {
          try
          {
            bool1 = ((XSCMValidator)localObject3).checkUniqueParticleAttribution(paramSubstitutionGroupHandler);
          }
          catch (XMLSchemaException localXMLSchemaException3)
          {
            reportSchemaError(paramXMLErrorReporter, arrayOfSimpleLocator[i1], localXMLSchemaException3.getKey(), localXMLSchemaException3.getArgs());
          }
        }
        if ((!bool2) && (bool1)) {
          localObject1[(m++)] = localObject1[i1];
        }
      }
      if (!bool2)
      {
        arrayOfSchemaGrammar[n].setUncheckedTypeNum(m);
        fFullChecked = true;
      }
    }
  }
  
  public static void checkElementDeclsConsistent(XSComplexTypeDecl paramXSComplexTypeDecl, XSParticleDecl paramXSParticleDecl, SymbolHash paramSymbolHash, SubstitutionGroupHandler paramSubstitutionGroupHandler)
    throws XMLSchemaException
  {
    int i = fType;
    if (i == 2) {
      return;
    }
    if (i == 1)
    {
      localObject = (XSElementDecl)fValue;
      findElemInTable(paramXSComplexTypeDecl, (XSElementDecl)localObject, paramSymbolHash);
      if (fScope == 1)
      {
        XSElementDecl[] arrayOfXSElementDecl = paramSubstitutionGroupHandler.getSubstitutionGroup((XSElementDecl)localObject);
        for (int k = 0; k < arrayOfXSElementDecl.length; k++) {
          findElemInTable(paramXSComplexTypeDecl, arrayOfXSElementDecl[k], paramSymbolHash);
        }
      }
      return;
    }
    Object localObject = (XSModelGroupImpl)fValue;
    for (int j = 0; j < fParticleCount; j++) {
      checkElementDeclsConsistent(paramXSComplexTypeDecl, fParticles[j], paramSymbolHash, paramSubstitutionGroupHandler);
    }
  }
  
  public static void findElemInTable(XSComplexTypeDecl paramXSComplexTypeDecl, XSElementDecl paramXSElementDecl, SymbolHash paramSymbolHash)
    throws XMLSchemaException
  {
    String str = fName + "," + fTargetNamespace;
    XSElementDecl localXSElementDecl = null;
    if ((localXSElementDecl = (XSElementDecl)paramSymbolHash.get(str)) == null)
    {
      paramSymbolHash.put(str, paramXSElementDecl);
    }
    else
    {
      if (paramXSElementDecl == localXSElementDecl) {
        return;
      }
      if (fType != fType) {
        throw new XMLSchemaException("cos-element-consistent", new Object[] { fName, fName });
      }
    }
  }
  
  private static boolean particleValidRestriction(XSParticleDecl paramXSParticleDecl1, SubstitutionGroupHandler paramSubstitutionGroupHandler1, XSParticleDecl paramXSParticleDecl2, SubstitutionGroupHandler paramSubstitutionGroupHandler2)
    throws XMLSchemaException
  {
    return particleValidRestriction(paramXSParticleDecl1, paramSubstitutionGroupHandler1, paramXSParticleDecl2, paramSubstitutionGroupHandler2, true);
  }
  
  private static boolean particleValidRestriction(XSParticleDecl paramXSParticleDecl1, SubstitutionGroupHandler paramSubstitutionGroupHandler1, XSParticleDecl paramXSParticleDecl2, SubstitutionGroupHandler paramSubstitutionGroupHandler2, boolean paramBoolean)
    throws XMLSchemaException
  {
    Vector localVector1 = null;
    Vector localVector2 = null;
    int i = -2;
    int j = -2;
    boolean bool = false;
    if ((paramXSParticleDecl1.isEmpty()) && (!paramXSParticleDecl2.emptiable())) {
      throw new XMLSchemaException("cos-particle-restrict.a", null);
    }
    if ((!paramXSParticleDecl1.isEmpty()) && (paramXSParticleDecl2.isEmpty())) {
      throw new XMLSchemaException("cos-particle-restrict.b", null);
    }
    int k = fType;
    if (k == 3)
    {
      k = fValue).fCompositor;
      XSParticleDecl localXSParticleDecl = getNonUnaryGroup(paramXSParticleDecl1);
      if (localXSParticleDecl != paramXSParticleDecl1)
      {
        paramXSParticleDecl1 = localXSParticleDecl;
        k = fType;
        if (k == 3) {
          k = fValue).fCompositor;
        }
      }
      localVector1 = removePointlessChildren(paramXSParticleDecl1);
    }
    int m = fMinOccurs;
    int n = fMaxOccurs;
    Object localObject;
    if ((paramSubstitutionGroupHandler1 != null) && (k == 1))
    {
      XSElementDecl localXSElementDecl1 = (XSElementDecl)fValue;
      if (fScope == 1)
      {
        localObject = paramSubstitutionGroupHandler1.getSubstitutionGroup(localXSElementDecl1);
        if (localObject.length > 0)
        {
          k = 101;
          i = m;
          j = n;
          localVector1 = new Vector(localObject.length + 1);
          for (i3 = 0; i3 < localObject.length; i3++) {
            addElementToParticleVector(localVector1, localObject[i3]);
          }
          addElementToParticleVector(localVector1, localXSElementDecl1);
          Collections.sort(localVector1, ELEMENT_PARTICLE_COMPARATOR);
          paramSubstitutionGroupHandler1 = null;
        }
      }
    }
    int i1 = fType;
    if (i1 == 3)
    {
      i1 = fValue).fCompositor;
      localObject = getNonUnaryGroup(paramXSParticleDecl2);
      if (localObject != paramXSParticleDecl2)
      {
        paramXSParticleDecl2 = (XSParticleDecl)localObject;
        i1 = fType;
        if (i1 == 3) {
          i1 = fValue).fCompositor;
        }
      }
      localVector2 = removePointlessChildren(paramXSParticleDecl2);
    }
    int i2 = fMinOccurs;
    int i3 = fMaxOccurs;
    if ((paramSubstitutionGroupHandler2 != null) && (i1 == 1))
    {
      XSElementDecl localXSElementDecl2 = (XSElementDecl)fValue;
      if (fScope == 1)
      {
        XSElementDecl[] arrayOfXSElementDecl = paramSubstitutionGroupHandler2.getSubstitutionGroup(localXSElementDecl2);
        if (arrayOfXSElementDecl.length > 0)
        {
          i1 = 101;
          localVector2 = new Vector(arrayOfXSElementDecl.length + 1);
          for (int i6 = 0; i6 < arrayOfXSElementDecl.length; i6++) {
            addElementToParticleVector(localVector2, arrayOfXSElementDecl[i6]);
          }
          addElementToParticleVector(localVector2, localXSElementDecl2);
          Collections.sort(localVector2, ELEMENT_PARTICLE_COMPARATOR);
          paramSubstitutionGroupHandler2 = null;
          bool = true;
        }
      }
    }
    switch (k)
    {
    case 1: 
      switch (i1)
      {
      case 1: 
        checkNameAndTypeOK((XSElementDecl)fValue, m, n, (XSElementDecl)fValue, i2, i3);
        return bool;
      case 2: 
        checkNSCompat((XSElementDecl)fValue, m, n, (XSWildcardDecl)fValue, i2, i3, paramBoolean);
        return bool;
      case 101: 
        localVector1 = new Vector();
        localVector1.addElement(paramXSParticleDecl1);
        checkRecurseLax(localVector1, 1, 1, paramSubstitutionGroupHandler1, localVector2, i2, i3, paramSubstitutionGroupHandler2);
        return bool;
      case 102: 
      case 103: 
        localVector1 = new Vector();
        localVector1.addElement(paramXSParticleDecl1);
        checkRecurse(localVector1, 1, 1, paramSubstitutionGroupHandler1, localVector2, i2, i3, paramSubstitutionGroupHandler2);
        return bool;
      }
      throw new XMLSchemaException("Internal-Error", new Object[] { "in particleValidRestriction" });
    case 2: 
      switch (i1)
      {
      case 2: 
        checkNSSubset((XSWildcardDecl)fValue, m, n, (XSWildcardDecl)fValue, i2, i3);
        return bool;
      case 1: 
      case 101: 
      case 102: 
      case 103: 
        throw new XMLSchemaException("cos-particle-restrict.2", new Object[] { "any:choice,sequence,all,elt" });
      }
      throw new XMLSchemaException("Internal-Error", new Object[] { "in particleValidRestriction" });
    case 103: 
      switch (i1)
      {
      case 2: 
        if (i == -2) {
          i = paramXSParticleDecl1.minEffectiveTotalRange();
        }
        if (j == -2) {
          j = paramXSParticleDecl1.maxEffectiveTotalRange();
        }
        checkNSRecurseCheckCardinality(localVector1, i, j, paramSubstitutionGroupHandler1, paramXSParticleDecl2, i2, i3, paramBoolean);
        return bool;
      case 103: 
        checkRecurse(localVector1, m, n, paramSubstitutionGroupHandler1, localVector2, i2, i3, paramSubstitutionGroupHandler2);
        return bool;
      case 1: 
      case 101: 
      case 102: 
        throw new XMLSchemaException("cos-particle-restrict.2", new Object[] { "all:choice,sequence,elt" });
      }
      throw new XMLSchemaException("Internal-Error", new Object[] { "in particleValidRestriction" });
    case 101: 
      switch (i1)
      {
      case 2: 
        if (i == -2) {
          i = paramXSParticleDecl1.minEffectiveTotalRange();
        }
        if (j == -2) {
          j = paramXSParticleDecl1.maxEffectiveTotalRange();
        }
        checkNSRecurseCheckCardinality(localVector1, i, j, paramSubstitutionGroupHandler1, paramXSParticleDecl2, i2, i3, paramBoolean);
        return bool;
      case 101: 
        checkRecurseLax(localVector1, m, n, paramSubstitutionGroupHandler1, localVector2, i2, i3, paramSubstitutionGroupHandler2);
        return bool;
      case 1: 
      case 102: 
      case 103: 
        throw new XMLSchemaException("cos-particle-restrict.2", new Object[] { "choice:all,sequence,elt" });
      }
      throw new XMLSchemaException("Internal-Error", new Object[] { "in particleValidRestriction" });
    case 102: 
      switch (i1)
      {
      case 2: 
        if (i == -2) {
          i = paramXSParticleDecl1.minEffectiveTotalRange();
        }
        if (j == -2) {
          j = paramXSParticleDecl1.maxEffectiveTotalRange();
        }
        checkNSRecurseCheckCardinality(localVector1, i, j, paramSubstitutionGroupHandler1, paramXSParticleDecl2, i2, i3, paramBoolean);
        return bool;
      case 103: 
        checkRecurseUnordered(localVector1, m, n, paramSubstitutionGroupHandler1, localVector2, i2, i3, paramSubstitutionGroupHandler2);
        return bool;
      case 102: 
        checkRecurse(localVector1, m, n, paramSubstitutionGroupHandler1, localVector2, i2, i3, paramSubstitutionGroupHandler2);
        return bool;
      case 101: 
        int i4 = m * localVector1.size();
        int i5 = n == -1 ? n : n * localVector1.size();
        checkMapAndSum(localVector1, i4, i5, paramSubstitutionGroupHandler1, localVector2, i2, i3, paramSubstitutionGroupHandler2);
        return bool;
      case 1: 
        throw new XMLSchemaException("cos-particle-restrict.2", new Object[] { "seq:elt" });
      }
      throw new XMLSchemaException("Internal-Error", new Object[] { "in particleValidRestriction" });
    }
    return bool;
  }
  
  private static void addElementToParticleVector(Vector paramVector, XSElementDecl paramXSElementDecl)
  {
    XSParticleDecl localXSParticleDecl = new XSParticleDecl();
    fValue = paramXSElementDecl;
    fType = 1;
    paramVector.addElement(localXSParticleDecl);
  }
  
  private static XSParticleDecl getNonUnaryGroup(XSParticleDecl paramXSParticleDecl)
  {
    if ((fType == 1) || (fType == 2)) {
      return paramXSParticleDecl;
    }
    if ((fMinOccurs == 1) && (fMaxOccurs == 1) && (fValue != null) && (fValue).fParticleCount == 1)) {
      return getNonUnaryGroup(fValue).fParticles[0]);
    }
    return paramXSParticleDecl;
  }
  
  private static Vector removePointlessChildren(XSParticleDecl paramXSParticleDecl)
  {
    if ((fType == 1) || (fType == 2)) {
      return null;
    }
    Vector localVector = new Vector();
    XSModelGroupImpl localXSModelGroupImpl = (XSModelGroupImpl)fValue;
    for (int i = 0; i < fParticleCount; i++) {
      gatherChildren(fCompositor, fParticles[i], localVector);
    }
    return localVector;
  }
  
  private static void gatherChildren(int paramInt, XSParticleDecl paramXSParticleDecl, Vector paramVector)
  {
    int i = fMinOccurs;
    int j = fMaxOccurs;
    int k = fType;
    if (k == 3) {
      k = fValue).fCompositor;
    }
    if ((k == 1) || (k == 2))
    {
      paramVector.addElement(paramXSParticleDecl);
      return;
    }
    if ((i != 1) || (j != 1))
    {
      paramVector.addElement(paramXSParticleDecl);
    }
    else if (paramInt == k)
    {
      XSModelGroupImpl localXSModelGroupImpl = (XSModelGroupImpl)fValue;
      for (int m = 0; m < fParticleCount; m++) {
        gatherChildren(k, fParticles[m], paramVector);
      }
    }
    else if (!paramXSParticleDecl.isEmpty())
    {
      paramVector.addElement(paramXSParticleDecl);
    }
  }
  
  private static void checkNameAndTypeOK(XSElementDecl paramXSElementDecl1, int paramInt1, int paramInt2, XSElementDecl paramXSElementDecl2, int paramInt3, int paramInt4)
    throws XMLSchemaException
  {
    if ((fName != fName) || (fTargetNamespace != fTargetNamespace)) {
      throw new XMLSchemaException("rcase-NameAndTypeOK.1", new Object[] { fName, fTargetNamespace, fName, fTargetNamespace });
    }
    if ((!paramXSElementDecl2.getNillable()) && (paramXSElementDecl1.getNillable())) {
      throw new XMLSchemaException("rcase-NameAndTypeOK.2", new Object[] { fName });
    }
    if (!checkOccurrenceRange(paramInt1, paramInt2, paramInt3, paramInt4)) {
      throw new XMLSchemaException("rcase-NameAndTypeOK.3", new Object[] { fName, Integer.toString(paramInt1), paramInt2 == -1 ? "unbounded" : Integer.toString(paramInt2), Integer.toString(paramInt3), paramInt4 == -1 ? "unbounded" : Integer.toString(paramInt4) });
    }
    if (paramXSElementDecl2.getConstraintType() == 2)
    {
      if (paramXSElementDecl1.getConstraintType() != 2) {
        throw new XMLSchemaException("rcase-NameAndTypeOK.4.a", new Object[] { fName, fDefault.stringValue() });
      }
      i = 0;
      if ((fType.getTypeCategory() == 16) || (fType).fContentType == 1)) {
        i = 1;
      }
      if (((i == 0) && (!fDefault.normalizedValue.equals(fDefault.normalizedValue))) || ((i != 0) && (!fDefault.actualValue.equals(fDefault.actualValue)))) {
        throw new XMLSchemaException("rcase-NameAndTypeOK.4.b", new Object[] { fName, fDefault.stringValue(), fDefault.stringValue() });
      }
    }
    checkIDConstraintRestriction(paramXSElementDecl1, paramXSElementDecl2);
    int i = fBlock;
    int j = fBlock;
    if (((i & j) != j) || ((i == 0) && (j != 0))) {
      throw new XMLSchemaException("rcase-NameAndTypeOK.6", new Object[] { fName });
    }
    if (!checkTypeDerivationOk(fType, fType, (short)25)) {
      throw new XMLSchemaException("rcase-NameAndTypeOK.7", new Object[] { fName, fType.getName(), fType.getName() });
    }
  }
  
  private static void checkIDConstraintRestriction(XSElementDecl paramXSElementDecl1, XSElementDecl paramXSElementDecl2)
    throws XMLSchemaException
  {}
  
  private static boolean checkOccurrenceRange(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    return (paramInt1 >= paramInt3) && ((paramInt4 == -1) || ((paramInt2 != -1) && (paramInt2 <= paramInt4)));
  }
  
  private static void checkNSCompat(XSElementDecl paramXSElementDecl, int paramInt1, int paramInt2, XSWildcardDecl paramXSWildcardDecl, int paramInt3, int paramInt4, boolean paramBoolean)
    throws XMLSchemaException
  {
    if ((paramBoolean) && (!checkOccurrenceRange(paramInt1, paramInt2, paramInt3, paramInt4))) {
      throw new XMLSchemaException("rcase-NSCompat.2", new Object[] { fName, Integer.toString(paramInt1), paramInt2 == -1 ? "unbounded" : Integer.toString(paramInt2), Integer.toString(paramInt3), paramInt4 == -1 ? "unbounded" : Integer.toString(paramInt4) });
    }
    if (!paramXSWildcardDecl.allowNamespace(fTargetNamespace)) {
      throw new XMLSchemaException("rcase-NSCompat.1", new Object[] { fName, fTargetNamespace });
    }
  }
  
  private static void checkNSSubset(XSWildcardDecl paramXSWildcardDecl1, int paramInt1, int paramInt2, XSWildcardDecl paramXSWildcardDecl2, int paramInt3, int paramInt4)
    throws XMLSchemaException
  {
    if (!checkOccurrenceRange(paramInt1, paramInt2, paramInt3, paramInt4)) {
      throw new XMLSchemaException("rcase-NSSubset.2", new Object[] { Integer.toString(paramInt1), paramInt2 == -1 ? "unbounded" : Integer.toString(paramInt2), Integer.toString(paramInt3), paramInt4 == -1 ? "unbounded" : Integer.toString(paramInt4) });
    }
    if (!paramXSWildcardDecl1.isSubsetOf(paramXSWildcardDecl2)) {
      throw new XMLSchemaException("rcase-NSSubset.1", null);
    }
    if (paramXSWildcardDecl1.weakerProcessContents(paramXSWildcardDecl2)) {
      throw new XMLSchemaException("rcase-NSSubset.3", new Object[] { paramXSWildcardDecl1.getProcessContentsAsString(), paramXSWildcardDecl2.getProcessContentsAsString() });
    }
  }
  
  private static void checkNSRecurseCheckCardinality(Vector paramVector, int paramInt1, int paramInt2, SubstitutionGroupHandler paramSubstitutionGroupHandler, XSParticleDecl paramXSParticleDecl, int paramInt3, int paramInt4, boolean paramBoolean)
    throws XMLSchemaException
  {
    if ((paramBoolean) && (!checkOccurrenceRange(paramInt1, paramInt2, paramInt3, paramInt4))) {
      throw new XMLSchemaException("rcase-NSRecurseCheckCardinality.2", new Object[] { Integer.toString(paramInt1), paramInt2 == -1 ? "unbounded" : Integer.toString(paramInt2), Integer.toString(paramInt3), paramInt4 == -1 ? "unbounded" : Integer.toString(paramInt4) });
    }
    int i = paramVector.size();
    try
    {
      for (int j = 0; j < i; j++)
      {
        XSParticleDecl localXSParticleDecl = (XSParticleDecl)paramVector.elementAt(j);
        particleValidRestriction(localXSParticleDecl, paramSubstitutionGroupHandler, paramXSParticleDecl, null, false);
      }
    }
    catch (XMLSchemaException localXMLSchemaException)
    {
      throw new XMLSchemaException("rcase-NSRecurseCheckCardinality.1", null);
    }
  }
  
  private static void checkRecurse(Vector paramVector1, int paramInt1, int paramInt2, SubstitutionGroupHandler paramSubstitutionGroupHandler1, Vector paramVector2, int paramInt3, int paramInt4, SubstitutionGroupHandler paramSubstitutionGroupHandler2)
    throws XMLSchemaException
  {
    if (!checkOccurrenceRange(paramInt1, paramInt2, paramInt3, paramInt4)) {
      throw new XMLSchemaException("rcase-Recurse.1", new Object[] { Integer.toString(paramInt1), paramInt2 == -1 ? "unbounded" : Integer.toString(paramInt2), Integer.toString(paramInt3), paramInt4 == -1 ? "unbounded" : Integer.toString(paramInt4) });
    }
    int i = paramVector1.size();
    XSParticleDecl localXSParticleDecl1 = paramVector2.size();
    XSParticleDecl localXSParticleDecl2 = 0;
    XSParticleDecl localXSParticleDecl4;
    for (int j = 0; j < i; j++)
    {
      localXSParticleDecl3 = (XSParticleDecl)paramVector1.elementAt(j);
      localXSParticleDecl4 = localXSParticleDecl2;
      while (localXSParticleDecl4 < localXSParticleDecl1)
      {
        XSParticleDecl localXSParticleDecl5 = (XSParticleDecl)paramVector2.elementAt(localXSParticleDecl4);
        localXSParticleDecl2++;
        try
        {
          particleValidRestriction(localXSParticleDecl3, paramSubstitutionGroupHandler1, localXSParticleDecl5, paramSubstitutionGroupHandler2);
        }
        catch (XMLSchemaException localXMLSchemaException)
        {
          if (!localXSParticleDecl5.emptiable()) {
            throw new XMLSchemaException("rcase-Recurse.2", null);
          }
          localXSParticleDecl4++;
        }
      }
      throw new XMLSchemaException("rcase-Recurse.2", null);
    }
    for (XSParticleDecl localXSParticleDecl3 = localXSParticleDecl2; localXSParticleDecl3 < localXSParticleDecl1; localXSParticleDecl3++)
    {
      localXSParticleDecl4 = (XSParticleDecl)paramVector2.elementAt(localXSParticleDecl3);
      if (!localXSParticleDecl4.emptiable()) {
        throw new XMLSchemaException("rcase-Recurse.2", null);
      }
    }
  }
  
  private static void checkRecurseUnordered(Vector paramVector1, int paramInt1, int paramInt2, SubstitutionGroupHandler paramSubstitutionGroupHandler1, Vector paramVector2, int paramInt3, int paramInt4, SubstitutionGroupHandler paramSubstitutionGroupHandler2)
    throws XMLSchemaException
  {
    if (!checkOccurrenceRange(paramInt1, paramInt2, paramInt3, paramInt4)) {
      throw new XMLSchemaException("rcase-RecurseUnordered.1", new Object[] { Integer.toString(paramInt1), paramInt2 == -1 ? "unbounded" : Integer.toString(paramInt2), Integer.toString(paramInt3), paramInt4 == -1 ? "unbounded" : Integer.toString(paramInt4) });
    }
    int i = paramVector1.size();
    int j = paramVector2.size();
    boolean[] arrayOfBoolean = new boolean[j];
    for (int k = 0; k < i; k++)
    {
      XSParticleDecl localXSParticleDecl1 = (XSParticleDecl)paramVector1.elementAt(k);
      int n = 0;
      while (n < j)
      {
        XSParticleDecl localXSParticleDecl3 = (XSParticleDecl)paramVector2.elementAt(n);
        try
        {
          particleValidRestriction(localXSParticleDecl1, paramSubstitutionGroupHandler1, localXSParticleDecl3, paramSubstitutionGroupHandler2);
          if (arrayOfBoolean[n] != 0) {
            throw new XMLSchemaException("rcase-RecurseUnordered.2", null);
          }
          arrayOfBoolean[n] = true;
        }
        catch (XMLSchemaException localXMLSchemaException)
        {
          n++;
        }
      }
      throw new XMLSchemaException("rcase-RecurseUnordered.2", null);
    }
    for (int m = 0; m < j; m++)
    {
      XSParticleDecl localXSParticleDecl2 = (XSParticleDecl)paramVector2.elementAt(m);
      if ((arrayOfBoolean[m] == 0) && (!localXSParticleDecl2.emptiable())) {
        throw new XMLSchemaException("rcase-RecurseUnordered.2", null);
      }
    }
  }
  
  private static void checkRecurseLax(Vector paramVector1, int paramInt1, int paramInt2, SubstitutionGroupHandler paramSubstitutionGroupHandler1, Vector paramVector2, int paramInt3, int paramInt4, SubstitutionGroupHandler paramSubstitutionGroupHandler2)
    throws XMLSchemaException
  {
    if (!checkOccurrenceRange(paramInt1, paramInt2, paramInt3, paramInt4)) {
      throw new XMLSchemaException("rcase-RecurseLax.1", new Object[] { Integer.toString(paramInt1), paramInt2 == -1 ? "unbounded" : Integer.toString(paramInt2), Integer.toString(paramInt3), paramInt4 == -1 ? "unbounded" : Integer.toString(paramInt4) });
    }
    int i = paramVector1.size();
    int j = paramVector2.size();
    int k = 0;
    for (int m = 0; m < i; m++)
    {
      XSParticleDecl localXSParticleDecl1 = (XSParticleDecl)paramVector1.elementAt(m);
      int n = k;
      while (n < j)
      {
        XSParticleDecl localXSParticleDecl2 = (XSParticleDecl)paramVector2.elementAt(n);
        k++;
        try
        {
          if (!particleValidRestriction(localXSParticleDecl1, paramSubstitutionGroupHandler1, localXSParticleDecl2, paramSubstitutionGroupHandler2)) {
            break;
          }
          k--;
        }
        catch (XMLSchemaException localXMLSchemaException)
        {
          n++;
        }
      }
      throw new XMLSchemaException("rcase-RecurseLax.2", null);
    }
  }
  
  private static void checkMapAndSum(Vector paramVector1, int paramInt1, int paramInt2, SubstitutionGroupHandler paramSubstitutionGroupHandler1, Vector paramVector2, int paramInt3, int paramInt4, SubstitutionGroupHandler paramSubstitutionGroupHandler2)
    throws XMLSchemaException
  {
    if (!checkOccurrenceRange(paramInt1, paramInt2, paramInt3, paramInt4)) {
      throw new XMLSchemaException("rcase-MapAndSum.2", new Object[] { Integer.toString(paramInt1), paramInt2 == -1 ? "unbounded" : Integer.toString(paramInt2), Integer.toString(paramInt3), paramInt4 == -1 ? "unbounded" : Integer.toString(paramInt4) });
    }
    int i = paramVector1.size();
    int j = paramVector2.size();
    for (int k = 0; k < i; k++)
    {
      XSParticleDecl localXSParticleDecl1 = (XSParticleDecl)paramVector1.elementAt(k);
      int m = 0;
      while (m < j)
      {
        XSParticleDecl localXSParticleDecl2 = (XSParticleDecl)paramVector2.elementAt(m);
        try
        {
          particleValidRestriction(localXSParticleDecl1, paramSubstitutionGroupHandler1, localXSParticleDecl2, paramSubstitutionGroupHandler2);
        }
        catch (XMLSchemaException localXMLSchemaException)
        {
          m++;
        }
      }
      throw new XMLSchemaException("rcase-MapAndSum.1", null);
    }
  }
  
  public static boolean overlapUPA(XSElementDecl paramXSElementDecl1, XSElementDecl paramXSElementDecl2, SubstitutionGroupHandler paramSubstitutionGroupHandler)
  {
    if ((fName == fName) && (fTargetNamespace == fTargetNamespace)) {
      return true;
    }
    XSElementDecl[] arrayOfXSElementDecl = paramSubstitutionGroupHandler.getSubstitutionGroup(paramXSElementDecl1);
    for (int i = arrayOfXSElementDecl.length - 1; i >= 0; i--) {
      if ((fName == fName) && (fTargetNamespace == fTargetNamespace)) {
        return true;
      }
    }
    arrayOfXSElementDecl = paramSubstitutionGroupHandler.getSubstitutionGroup(paramXSElementDecl2);
    for (int j = arrayOfXSElementDecl.length - 1; j >= 0; j--) {
      if ((fName == fName) && (fTargetNamespace == fTargetNamespace)) {
        return true;
      }
    }
    return false;
  }
  
  public static boolean overlapUPA(XSElementDecl paramXSElementDecl, XSWildcardDecl paramXSWildcardDecl, SubstitutionGroupHandler paramSubstitutionGroupHandler)
  {
    if (paramXSWildcardDecl.allowNamespace(fTargetNamespace)) {
      return true;
    }
    XSElementDecl[] arrayOfXSElementDecl = paramSubstitutionGroupHandler.getSubstitutionGroup(paramXSElementDecl);
    for (int i = arrayOfXSElementDecl.length - 1; i >= 0; i--) {
      if (paramXSWildcardDecl.allowNamespace(fTargetNamespace)) {
        return true;
      }
    }
    return false;
  }
  
  public static boolean overlapUPA(XSWildcardDecl paramXSWildcardDecl1, XSWildcardDecl paramXSWildcardDecl2)
  {
    XSWildcardDecl localXSWildcardDecl = paramXSWildcardDecl1.performIntersectionWith(paramXSWildcardDecl2, fProcessContents);
    return (localXSWildcardDecl == null) || (fType != 3) || (fNamespaceList.length != 0);
  }
  
  public static boolean overlapUPA(Object paramObject1, Object paramObject2, SubstitutionGroupHandler paramSubstitutionGroupHandler)
  {
    if ((paramObject1 instanceof XSElementDecl))
    {
      if ((paramObject2 instanceof XSElementDecl)) {
        return overlapUPA((XSElementDecl)paramObject1, (XSElementDecl)paramObject2, paramSubstitutionGroupHandler);
      }
      return overlapUPA((XSElementDecl)paramObject1, (XSWildcardDecl)paramObject2, paramSubstitutionGroupHandler);
    }
    if ((paramObject2 instanceof XSElementDecl)) {
      return overlapUPA((XSElementDecl)paramObject2, (XSWildcardDecl)paramObject1, paramSubstitutionGroupHandler);
    }
    return overlapUPA((XSWildcardDecl)paramObject1, (XSWildcardDecl)paramObject2);
  }
}
