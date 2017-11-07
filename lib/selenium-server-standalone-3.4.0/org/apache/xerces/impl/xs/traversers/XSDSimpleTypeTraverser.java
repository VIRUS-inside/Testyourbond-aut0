package org.apache.xerces.impl.xs.traversers;

import java.util.ArrayList;
import java.util.Vector;
import org.apache.xerces.impl.dv.InvalidDatatypeFacetException;
import org.apache.xerces.impl.dv.SchemaDVFactory;
import org.apache.xerces.impl.dv.XSSimpleType;
import org.apache.xerces.impl.dv.xs.XSSimpleTypeDecl;
import org.apache.xerces.impl.validation.ValidationState;
import org.apache.xerces.impl.xs.SchemaGrammar;
import org.apache.xerces.impl.xs.SchemaGrammar.BuiltinSchemaGrammar;
import org.apache.xerces.impl.xs.SchemaSymbols;
import org.apache.xerces.impl.xs.XSAnnotationImpl;
import org.apache.xerces.impl.xs.util.XInt;
import org.apache.xerces.impl.xs.util.XSObjectListImpl;
import org.apache.xerces.util.DOMUtil;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xs.XSObject;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSTypeDefinition;
import org.w3c.dom.Element;

class XSDSimpleTypeTraverser
  extends XSDAbstractTraverser
{
  private boolean fIsBuiltIn = false;
  
  XSDSimpleTypeTraverser(XSDHandler paramXSDHandler, XSAttributeChecker paramXSAttributeChecker)
  {
    super(paramXSDHandler, paramXSAttributeChecker);
  }
  
  XSSimpleType traverseGlobal(Element paramElement, XSDocumentInfo paramXSDocumentInfo, SchemaGrammar paramSchemaGrammar)
  {
    Object[] arrayOfObject = fAttrChecker.checkAttributes(paramElement, true, paramXSDocumentInfo);
    String str1 = (String)arrayOfObject[XSAttributeChecker.ATTIDX_NAME];
    if (str1 == null) {
      arrayOfObject[XSAttributeChecker.ATTIDX_NAME] = "(no name)";
    }
    XSSimpleType localXSSimpleType = traverseSimpleTypeDecl(paramElement, arrayOfObject, paramXSDocumentInfo, paramSchemaGrammar);
    fAttrChecker.returnAttrArray(arrayOfObject, paramXSDocumentInfo);
    if (str1 == null)
    {
      reportSchemaError("s4s-att-must-appear", new Object[] { SchemaSymbols.ELT_SIMPLETYPE, SchemaSymbols.ATT_NAME }, paramElement);
      localXSSimpleType = null;
    }
    if (localXSSimpleType != null)
    {
      if (paramSchemaGrammar.getGlobalTypeDecl(localXSSimpleType.getName()) == null) {
        paramSchemaGrammar.addGlobalSimpleTypeDecl(localXSSimpleType);
      }
      String str2 = fSchemaHandler.schemaDocument2SystemId(paramXSDocumentInfo);
      XSTypeDefinition localXSTypeDefinition = paramSchemaGrammar.getGlobalTypeDecl(localXSSimpleType.getName(), str2);
      if (localXSTypeDefinition == null) {
        paramSchemaGrammar.addGlobalSimpleTypeDecl(localXSSimpleType, str2);
      }
      if (fSchemaHandler.fTolerateDuplicates)
      {
        if ((localXSTypeDefinition != null) && ((localXSTypeDefinition instanceof XSSimpleType))) {
          localXSSimpleType = (XSSimpleType)localXSTypeDefinition;
        }
        fSchemaHandler.addGlobalTypeDecl(localXSSimpleType);
      }
    }
    return localXSSimpleType;
  }
  
  XSSimpleType traverseLocal(Element paramElement, XSDocumentInfo paramXSDocumentInfo, SchemaGrammar paramSchemaGrammar)
  {
    Object[] arrayOfObject = fAttrChecker.checkAttributes(paramElement, false, paramXSDocumentInfo);
    String str = genAnonTypeName(paramElement);
    XSSimpleType localXSSimpleType = getSimpleType(str, paramElement, arrayOfObject, paramXSDocumentInfo, paramSchemaGrammar);
    if ((localXSSimpleType instanceof XSSimpleTypeDecl)) {
      ((XSSimpleTypeDecl)localXSSimpleType).setAnonymous(true);
    }
    fAttrChecker.returnAttrArray(arrayOfObject, paramXSDocumentInfo);
    return localXSSimpleType;
  }
  
  private XSSimpleType traverseSimpleTypeDecl(Element paramElement, Object[] paramArrayOfObject, XSDocumentInfo paramXSDocumentInfo, SchemaGrammar paramSchemaGrammar)
  {
    String str = (String)paramArrayOfObject[XSAttributeChecker.ATTIDX_NAME];
    return getSimpleType(str, paramElement, paramArrayOfObject, paramXSDocumentInfo, paramSchemaGrammar);
  }
  
  private String genAnonTypeName(Element paramElement)
  {
    StringBuffer localStringBuffer = new StringBuffer("#AnonType_");
    for (Element localElement = DOMUtil.getParent(paramElement); (localElement != null) && (localElement != DOMUtil.getRoot(DOMUtil.getDocument(localElement))); localElement = DOMUtil.getParent(localElement)) {
      localStringBuffer.append(localElement.getAttribute(SchemaSymbols.ATT_NAME));
    }
    return localStringBuffer.toString();
  }
  
  private XSSimpleType getSimpleType(String paramString, Element paramElement, Object[] paramArrayOfObject, XSDocumentInfo paramXSDocumentInfo, SchemaGrammar paramSchemaGrammar)
  {
    XInt localXInt = (XInt)paramArrayOfObject[XSAttributeChecker.ATTIDX_FINAL];
    int i = localXInt == null ? fFinalDefault : localXInt.intValue();
    Element localElement1 = DOMUtil.getFirstChildElement(paramElement);
    Object localObject1 = null;
    if ((localElement1 != null) && (DOMUtil.getLocalName(localElement1).equals(SchemaSymbols.ELT_ANNOTATION)))
    {
      localObject2 = traverseAnnotationDecl(localElement1, paramArrayOfObject, false, paramXSDocumentInfo);
      if (localObject2 != null) {
        localObject1 = new XSAnnotationImpl[] { localObject2 };
      }
      localElement1 = DOMUtil.getNextSiblingElement(localElement1);
    }
    else
    {
      localObject2 = DOMUtil.getSyntheticAnnotation(paramElement);
      if (localObject2 != null)
      {
        XSAnnotationImpl localXSAnnotationImpl = traverseSyntheticAnnotation(paramElement, (String)localObject2, paramArrayOfObject, false, paramXSDocumentInfo);
        localObject1 = new XSAnnotationImpl[] { localXSAnnotationImpl };
      }
    }
    if (localElement1 == null)
    {
      reportSchemaError("s4s-elt-must-match.2", new Object[] { SchemaSymbols.ELT_SIMPLETYPE, "(annotation?, (restriction | list | union))" }, paramElement);
      return errorType(paramString, fTargetNamespace, (short)2);
    }
    Object localObject2 = DOMUtil.getLocalName(localElement1);
    short s = 2;
    int j = 0;
    int k = 0;
    int m = 0;
    if (((String)localObject2).equals(SchemaSymbols.ELT_RESTRICTION))
    {
      s = 2;
      j = 1;
    }
    else if (((String)localObject2).equals(SchemaSymbols.ELT_LIST))
    {
      s = 16;
      k = 1;
    }
    else if (((String)localObject2).equals(SchemaSymbols.ELT_UNION))
    {
      s = 8;
      m = 1;
    }
    else
    {
      reportSchemaError("s4s-elt-must-match.1", new Object[] { SchemaSymbols.ELT_SIMPLETYPE, "(annotation?, (restriction | list | union))", localObject2 }, paramElement);
      return errorType(paramString, fTargetNamespace, (short)2);
    }
    Element localElement2 = DOMUtil.getNextSiblingElement(localElement1);
    if (localElement2 != null) {
      reportSchemaError("s4s-elt-must-match.1", new Object[] { SchemaSymbols.ELT_SIMPLETYPE, "(annotation?, (restriction | list | union))", DOMUtil.getLocalName(localElement2) }, localElement2);
    }
    Object[] arrayOfObject = fAttrChecker.checkAttributes(localElement1, false, paramXSDocumentInfo);
    QName localQName = (QName)arrayOfObject[XSAttributeChecker.ATTIDX_ITEMTYPE];
    Vector localVector = (Vector)arrayOfObject[XSAttributeChecker.ATTIDX_MEMBERTYPES];
    Element localElement3 = DOMUtil.getFirstChildElement(localElement1);
    if ((localElement3 != null) && (DOMUtil.getLocalName(localElement3).equals(SchemaSymbols.ELT_ANNOTATION)))
    {
      localObject3 = traverseAnnotationDecl(localElement3, arrayOfObject, false, paramXSDocumentInfo);
      if (localObject3 != null) {
        if (localObject1 == null)
        {
          localObject1 = new XSAnnotationImpl[] { localObject3 };
        }
        else
        {
          localObject4 = new XSAnnotationImpl[2];
          localObject4[0] = localObject1[0];
          localObject1 = localObject4;
          localObject1[1] = localObject3;
        }
      }
      localElement3 = DOMUtil.getNextSiblingElement(localElement3);
    }
    else
    {
      localObject3 = DOMUtil.getSyntheticAnnotation(localElement1);
      if (localObject3 != null)
      {
        localObject4 = traverseSyntheticAnnotation(localElement1, (String)localObject3, arrayOfObject, false, paramXSDocumentInfo);
        if (localObject1 == null)
        {
          localObject1 = new XSAnnotationImpl[] { localObject4 };
        }
        else
        {
          localObject5 = new XSAnnotationImpl[2];
          localObject5[0] = localObject1[0];
          localObject1 = localObject5;
          localObject1[1] = localObject4;
        }
      }
    }
    Object localObject3 = null;
    if (((j != 0) || (k != 0)) && (localQName != null))
    {
      localObject3 = findDTValidator(localElement1, paramString, localQName, s, paramXSDocumentInfo);
      if ((localObject3 == null) && (fIsBuiltIn))
      {
        fIsBuiltIn = false;
        return null;
      }
    }
    Object localObject4 = null;
    Object localObject5 = null;
    int n;
    XSObjectList localXSObjectList;
    if ((m != 0) && (localVector != null) && (localVector.size() > 0))
    {
      n = localVector.size();
      localObject4 = new ArrayList(n);
      for (int i1 = 0; i1 < n; i1++)
      {
        localObject5 = findDTValidator(localElement1, paramString, (QName)localVector.elementAt(i1), (short)8, paramXSDocumentInfo);
        if (localObject5 != null) {
          if (((XSSimpleType)localObject5).getVariety() == 3)
          {
            localXSObjectList = ((XSSimpleType)localObject5).getMemberTypes();
            for (int i2 = 0; i2 < localXSObjectList.getLength(); i2++) {
              ((ArrayList)localObject4).add(localXSObjectList.item(i2));
            }
          }
          else
          {
            ((ArrayList)localObject4).add(localObject5);
          }
        }
      }
    }
    if ((localElement3 != null) && (DOMUtil.getLocalName(localElement3).equals(SchemaSymbols.ELT_SIMPLETYPE)))
    {
      if ((j != 0) || (k != 0))
      {
        if (localQName != null) {
          reportSchemaError(k != 0 ? "src-simple-type.3.a" : "src-simple-type.2.a", null, localElement3);
        }
        if (localObject3 == null) {
          localObject3 = traverseLocal(localElement3, paramXSDocumentInfo, paramSchemaGrammar);
        }
        localElement3 = DOMUtil.getNextSiblingElement(localElement3);
      }
      else if (m != 0)
      {
        if (localObject4 == null) {
          localObject4 = new ArrayList(2);
        }
        do
        {
          localObject5 = traverseLocal(localElement3, paramXSDocumentInfo, paramSchemaGrammar);
          if (localObject5 != null) {
            if (((XSSimpleType)localObject5).getVariety() == 3)
            {
              localXSObjectList = ((XSSimpleType)localObject5).getMemberTypes();
              for (n = 0; n < localXSObjectList.getLength(); n++) {
                ((ArrayList)localObject4).add(localXSObjectList.item(n));
              }
            }
            else
            {
              ((ArrayList)localObject4).add(localObject5);
            }
          }
          localElement3 = DOMUtil.getNextSiblingElement(localElement3);
          if (localElement3 == null) {
            break;
          }
        } while (DOMUtil.getLocalName(localElement3).equals(SchemaSymbols.ELT_SIMPLETYPE));
      }
    }
    else if (((j != 0) || (k != 0)) && (localQName == null)) {
      reportSchemaError(k != 0 ? "src-simple-type.3.b" : "src-simple-type.2.b", null, localElement1);
    } else if ((m != 0) && ((localVector == null) || (localVector.size() == 0))) {
      reportSchemaError("src-union-memberTypes-or-simpleTypes", null, localElement1);
    }
    if (((j != 0) || (k != 0)) && (localObject3 == null))
    {
      fAttrChecker.returnAttrArray(arrayOfObject, paramXSDocumentInfo);
      return errorType(paramString, fTargetNamespace, (short)(j != 0 ? 2 : 16));
    }
    if ((m != 0) && ((localObject4 == null) || (((ArrayList)localObject4).size() == 0)))
    {
      fAttrChecker.returnAttrArray(arrayOfObject, paramXSDocumentInfo);
      return errorType(paramString, fTargetNamespace, (short)8);
    }
    if ((k != 0) && (isListDatatype((XSSimpleType)localObject3)))
    {
      reportSchemaError("cos-st-restricts.2.1", new Object[] { paramString, ((XSSimpleType)localObject3).getName() }, localElement1);
      fAttrChecker.returnAttrArray(arrayOfObject, paramXSDocumentInfo);
      return errorType(paramString, fTargetNamespace, (short)16);
    }
    XSSimpleType localXSSimpleType = null;
    Object localObject6;
    if (j != 0)
    {
      localXSSimpleType = fSchemaHandler.fDVFactory.createTypeRestriction(paramString, fTargetNamespace, (short)i, (XSSimpleType)localObject3, localObject1 == null ? null : new XSObjectListImpl((XSObject[])localObject1, localObject1.length));
    }
    else if (k != 0)
    {
      localXSSimpleType = fSchemaHandler.fDVFactory.createTypeList(paramString, fTargetNamespace, (short)i, (XSSimpleType)localObject3, localObject1 == null ? null : new XSObjectListImpl((XSObject[])localObject1, localObject1.length));
    }
    else if (m != 0)
    {
      localObject6 = (XSSimpleType[])((ArrayList)localObject4).toArray(new XSSimpleType[((ArrayList)localObject4).size()]);
      localXSSimpleType = fSchemaHandler.fDVFactory.createTypeUnion(paramString, fTargetNamespace, (short)i, (XSSimpleType[])localObject6, localObject1 == null ? null : new XSObjectListImpl((XSObject[])localObject1, localObject1.length));
    }
    if ((j != 0) && (localElement3 != null))
    {
      localObject6 = traverseFacets(localElement3, (XSSimpleType)localObject3, paramXSDocumentInfo);
      localElement3 = nodeAfterFacets;
      try
      {
        fValidationState.setNamespaceSupport(fNamespaceSupport);
        localXSSimpleType.applyFacets(facetdata, fPresentFacets, fFixedFacets, fValidationState);
      }
      catch (InvalidDatatypeFacetException localInvalidDatatypeFacetException)
      {
        reportSchemaError(localInvalidDatatypeFacetException.getKey(), localInvalidDatatypeFacetException.getArgs(), localElement1);
        localXSSimpleType = fSchemaHandler.fDVFactory.createTypeRestriction(paramString, fTargetNamespace, (short)i, (XSSimpleType)localObject3, localObject1 == null ? null : new XSObjectListImpl((XSObject[])localObject1, localObject1.length));
      }
    }
    if (localElement3 != null) {
      if (j != 0) {
        reportSchemaError("s4s-elt-must-match.1", new Object[] { SchemaSymbols.ELT_RESTRICTION, "(annotation?, (simpleType?, (minExclusive | minInclusive | maxExclusive | maxInclusive | totalDigits | fractionDigits | length | minLength | maxLength | enumeration | whiteSpace | pattern)*))", DOMUtil.getLocalName(localElement3) }, localElement3);
      } else if (k != 0) {
        reportSchemaError("s4s-elt-must-match.1", new Object[] { SchemaSymbols.ELT_LIST, "(annotation?, (simpleType?))", DOMUtil.getLocalName(localElement3) }, localElement3);
      } else if (m != 0) {
        reportSchemaError("s4s-elt-must-match.1", new Object[] { SchemaSymbols.ELT_UNION, "(annotation?, (simpleType*))", DOMUtil.getLocalName(localElement3) }, localElement3);
      }
    }
    fAttrChecker.returnAttrArray(arrayOfObject, paramXSDocumentInfo);
    return localXSSimpleType;
  }
  
  private XSSimpleType findDTValidator(Element paramElement, String paramString, QName paramQName, short paramShort, XSDocumentInfo paramXSDocumentInfo)
  {
    if (paramQName == null) {
      return null;
    }
    XSTypeDefinition localXSTypeDefinition = (XSTypeDefinition)fSchemaHandler.getGlobalDecl(paramXSDocumentInfo, 7, paramQName, paramElement);
    if (localXSTypeDefinition == null) {
      return null;
    }
    if (localXSTypeDefinition.getTypeCategory() != 16)
    {
      reportSchemaError("cos-st-restricts.1.1", new Object[] { rawname, paramString }, paramElement);
      return null;
    }
    if ((localXSTypeDefinition == SchemaGrammar.fAnySimpleType) && (paramShort == 2))
    {
      if (checkBuiltIn(paramString, fTargetNamespace)) {
        return null;
      }
      reportSchemaError("cos-st-restricts.1.1", new Object[] { rawname, paramString }, paramElement);
      return null;
    }
    if ((localXSTypeDefinition.getFinal() & paramShort) != 0)
    {
      if (paramShort == 2) {
        reportSchemaError("st-props-correct.3", new Object[] { paramString, rawname }, paramElement);
      } else if (paramShort == 16) {
        reportSchemaError("cos-st-restricts.2.3.1.1", new Object[] { rawname, paramString }, paramElement);
      } else if (paramShort == 8) {
        reportSchemaError("cos-st-restricts.3.3.1.1", new Object[] { rawname, paramString }, paramElement);
      }
      return null;
    }
    return (XSSimpleType)localXSTypeDefinition;
  }
  
  private final boolean checkBuiltIn(String paramString1, String paramString2)
  {
    if (paramString2 != SchemaSymbols.URI_SCHEMAFORSCHEMA) {
      return false;
    }
    if (SchemaGrammar.SG_SchemaNS.getGlobalTypeDecl(paramString1) != null) {
      fIsBuiltIn = true;
    }
    return fIsBuiltIn;
  }
  
  private boolean isListDatatype(XSSimpleType paramXSSimpleType)
  {
    if (paramXSSimpleType.getVariety() == 2) {
      return true;
    }
    if (paramXSSimpleType.getVariety() == 3)
    {
      XSObjectList localXSObjectList = paramXSSimpleType.getMemberTypes();
      for (int i = 0; i < localXSObjectList.getLength(); i++) {
        if (((XSSimpleType)localXSObjectList.item(i)).getVariety() == 2) {
          return true;
        }
      }
    }
    return false;
  }
  
  private XSSimpleType errorType(String paramString1, String paramString2, short paramShort)
  {
    XSSimpleType localXSSimpleType = (XSSimpleType)SchemaGrammar.SG_SchemaNS.getTypeDefinition("string");
    switch (paramShort)
    {
    case 2: 
      return fSchemaHandler.fDVFactory.createTypeRestriction(paramString1, paramString2, (short)0, localXSSimpleType, null);
    case 16: 
      return fSchemaHandler.fDVFactory.createTypeList(paramString1, paramString2, (short)0, localXSSimpleType, null);
    case 8: 
      return fSchemaHandler.fDVFactory.createTypeUnion(paramString1, paramString2, (short)0, new XSSimpleType[] { localXSSimpleType }, null);
    }
    return null;
  }
}
