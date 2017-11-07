package org.apache.xerces.impl.xs.traversers;

import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidatedInfo;
import org.apache.xerces.impl.dv.XSSimpleType;
import org.apache.xerces.impl.validation.ValidationState;
import org.apache.xerces.impl.xs.SchemaGrammar;
import org.apache.xerces.impl.xs.SchemaSymbols;
import org.apache.xerces.impl.xs.XSAnnotationImpl;
import org.apache.xerces.impl.xs.XSAttributeDecl;
import org.apache.xerces.impl.xs.XSAttributeUseImpl;
import org.apache.xerces.impl.xs.XSComplexTypeDecl;
import org.apache.xerces.impl.xs.XSDeclarationPool;
import org.apache.xerces.impl.xs.util.XInt;
import org.apache.xerces.impl.xs.util.XSObjectListImpl;
import org.apache.xerces.util.DOMUtil;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSTypeDefinition;
import org.w3c.dom.Element;

class XSDAttributeTraverser
  extends XSDAbstractTraverser
{
  public XSDAttributeTraverser(XSDHandler paramXSDHandler, XSAttributeChecker paramXSAttributeChecker)
  {
    super(paramXSDHandler, paramXSAttributeChecker);
  }
  
  protected XSAttributeUseImpl traverseLocal(Element paramElement, XSDocumentInfo paramXSDocumentInfo, SchemaGrammar paramSchemaGrammar, XSComplexTypeDecl paramXSComplexTypeDecl)
  {
    Object[] arrayOfObject = fAttrChecker.checkAttributes(paramElement, false, paramXSDocumentInfo);
    Object localObject1 = (String)arrayOfObject[XSAttributeChecker.ATTIDX_DEFAULT];
    String str1 = (String)arrayOfObject[XSAttributeChecker.ATTIDX_FIXED];
    String str2 = (String)arrayOfObject[XSAttributeChecker.ATTIDX_NAME];
    QName localQName = (QName)arrayOfObject[XSAttributeChecker.ATTIDX_REF];
    XInt localXInt = (XInt)arrayOfObject[XSAttributeChecker.ATTIDX_USE];
    XSAttributeDecl localXSAttributeDecl = null;
    XSAnnotationImpl localXSAnnotationImpl = null;
    if (paramElement.getAttributeNode(SchemaSymbols.ATT_REF) != null)
    {
      if (localQName != null)
      {
        localXSAttributeDecl = (XSAttributeDecl)fSchemaHandler.getGlobalDecl(paramXSDocumentInfo, 1, localQName, paramElement);
        Element localElement = DOMUtil.getFirstChildElement(paramElement);
        if ((localElement != null) && (DOMUtil.getLocalName(localElement).equals(SchemaSymbols.ELT_ANNOTATION)))
        {
          localXSAnnotationImpl = traverseAnnotationDecl(localElement, arrayOfObject, false, paramXSDocumentInfo);
          localElement = DOMUtil.getNextSiblingElement(localElement);
        }
        else
        {
          localObject2 = DOMUtil.getSyntheticAnnotation(paramElement);
          if (localObject2 != null) {
            localXSAnnotationImpl = traverseSyntheticAnnotation(paramElement, (String)localObject2, arrayOfObject, false, paramXSDocumentInfo);
          }
        }
        if (localElement != null) {
          reportSchemaError("src-attribute.3.2", new Object[] { rawname }, localElement);
        }
        str2 = localpart;
      }
      else
      {
        localXSAttributeDecl = null;
      }
    }
    else {
      localXSAttributeDecl = traverseNamedAttr(paramElement, arrayOfObject, paramXSDocumentInfo, paramSchemaGrammar, false, paramXSComplexTypeDecl);
    }
    short s = 0;
    if (localObject1 != null)
    {
      s = 1;
    }
    else if (str1 != null)
    {
      s = 2;
      localObject1 = str1;
      str1 = null;
    }
    Object localObject2 = null;
    if (localXSAttributeDecl != null)
    {
      if (fSchemaHandler.fDeclPool != null) {
        localObject2 = fSchemaHandler.fDeclPool.getAttributeUse();
      } else {
        localObject2 = new XSAttributeUseImpl();
      }
      fAttrDecl = localXSAttributeDecl;
      fUse = localXInt.shortValue();
      fConstraintType = s;
      if (localObject1 != null)
      {
        fDefault = new ValidatedInfo();
        fDefault.normalizedValue = ((String)localObject1);
      }
      if (paramElement.getAttributeNode(SchemaSymbols.ATT_REF) == null)
      {
        fAnnotations = localXSAttributeDecl.getAnnotations();
      }
      else
      {
        XSObjectListImpl localXSObjectListImpl;
        if (localXSAnnotationImpl != null)
        {
          localXSObjectListImpl = new XSObjectListImpl();
          ((XSObjectListImpl)localXSObjectListImpl).addXSObject(localXSAnnotationImpl);
        }
        else
        {
          localXSObjectListImpl = XSObjectListImpl.EMPTY_LIST;
        }
        fAnnotations = localXSObjectListImpl;
      }
    }
    if ((localObject1 != null) && (str1 != null)) {
      reportSchemaError("src-attribute.1", new Object[] { str2 }, paramElement);
    }
    if ((s == 1) && (localXInt != null) && (localXInt.intValue() != 0))
    {
      reportSchemaError("src-attribute.2", new Object[] { str2 }, paramElement);
      fUse = 0;
    }
    if ((localObject1 != null) && (localObject2 != null))
    {
      fValidationState.setNamespaceSupport(fNamespaceSupport);
      try
      {
        checkDefaultValid((XSAttributeUseImpl)localObject2);
      }
      catch (InvalidDatatypeValueException localInvalidDatatypeValueException)
      {
        reportSchemaError(localInvalidDatatypeValueException.getKey(), localInvalidDatatypeValueException.getArgs(), paramElement);
        reportSchemaError("a-props-correct.2", new Object[] { str2, localObject1 }, paramElement);
        fDefault = null;
        fConstraintType = 0;
      }
      if (((XSSimpleType)localXSAttributeDecl.getTypeDefinition()).isIDType())
      {
        reportSchemaError("a-props-correct.3", new Object[] { str2 }, paramElement);
        fDefault = null;
        fConstraintType = 0;
      }
      if ((fAttrDecl.getConstraintType() == 2) && (fConstraintType != 0) && ((fConstraintType != 2) || (!fAttrDecl.getValInfo().actualValue.equals(fDefault.actualValue))))
      {
        reportSchemaError("au-props-correct.2", new Object[] { str2, fAttrDecl.getValInfo().stringValue() }, paramElement);
        fDefault = fAttrDecl.getValInfo();
        fConstraintType = 2;
      }
    }
    fAttrChecker.returnAttrArray(arrayOfObject, paramXSDocumentInfo);
    return localObject2;
  }
  
  protected XSAttributeDecl traverseGlobal(Element paramElement, XSDocumentInfo paramXSDocumentInfo, SchemaGrammar paramSchemaGrammar)
  {
    Object[] arrayOfObject = fAttrChecker.checkAttributes(paramElement, true, paramXSDocumentInfo);
    XSAttributeDecl localXSAttributeDecl = traverseNamedAttr(paramElement, arrayOfObject, paramXSDocumentInfo, paramSchemaGrammar, true, null);
    fAttrChecker.returnAttrArray(arrayOfObject, paramXSDocumentInfo);
    return localXSAttributeDecl;
  }
  
  XSAttributeDecl traverseNamedAttr(Element paramElement, Object[] paramArrayOfObject, XSDocumentInfo paramXSDocumentInfo, SchemaGrammar paramSchemaGrammar, boolean paramBoolean, XSComplexTypeDecl paramXSComplexTypeDecl)
  {
    String str1 = (String)paramArrayOfObject[XSAttributeChecker.ATTIDX_DEFAULT];
    String str2 = (String)paramArrayOfObject[XSAttributeChecker.ATTIDX_FIXED];
    XInt localXInt = (XInt)paramArrayOfObject[XSAttributeChecker.ATTIDX_FORM];
    String str3 = (String)paramArrayOfObject[XSAttributeChecker.ATTIDX_NAME];
    QName localQName = (QName)paramArrayOfObject[XSAttributeChecker.ATTIDX_TYPE];
    Object localObject1 = null;
    if (fSchemaHandler.fDeclPool != null) {
      localObject1 = fSchemaHandler.fDeclPool.getAttributeDecl();
    } else {
      localObject1 = new XSAttributeDecl();
    }
    if (str3 != null) {
      str3 = fSymbolTable.addSymbol(str3);
    }
    String str4 = null;
    XSComplexTypeDecl localXSComplexTypeDecl = null;
    short s1 = 0;
    if (paramBoolean)
    {
      str4 = fTargetNamespace;
      s1 = 1;
    }
    else
    {
      if (paramXSComplexTypeDecl != null)
      {
        localXSComplexTypeDecl = paramXSComplexTypeDecl;
        s1 = 2;
      }
      if (localXInt != null)
      {
        if (localXInt.intValue() == 1) {
          str4 = fTargetNamespace;
        }
      }
      else if (fAreLocalAttributesQualified) {
        str4 = fTargetNamespace;
      }
    }
    ValidatedInfo localValidatedInfo = null;
    short s2 = 0;
    if (paramBoolean) {
      if (str2 != null)
      {
        localValidatedInfo = new ValidatedInfo();
        normalizedValue = str2;
        s2 = 2;
      }
      else if (str1 != null)
      {
        localValidatedInfo = new ValidatedInfo();
        normalizedValue = str1;
        s2 = 1;
      }
    }
    Element localElement = DOMUtil.getFirstChildElement(paramElement);
    XSAnnotationImpl localXSAnnotationImpl = null;
    if ((localElement != null) && (DOMUtil.getLocalName(localElement).equals(SchemaSymbols.ELT_ANNOTATION)))
    {
      localXSAnnotationImpl = traverseAnnotationDecl(localElement, paramArrayOfObject, false, paramXSDocumentInfo);
      localElement = DOMUtil.getNextSiblingElement(localElement);
    }
    else
    {
      localObject2 = DOMUtil.getSyntheticAnnotation(paramElement);
      if (localObject2 != null) {
        localXSAnnotationImpl = traverseSyntheticAnnotation(paramElement, (String)localObject2, paramArrayOfObject, false, paramXSDocumentInfo);
      }
    }
    Object localObject2 = null;
    int i = 0;
    Object localObject3;
    if (localElement != null)
    {
      localObject3 = DOMUtil.getLocalName(localElement);
      if (((String)localObject3).equals(SchemaSymbols.ELT_SIMPLETYPE))
      {
        localObject2 = fSchemaHandler.fSimpleTypeTraverser.traverseLocal(localElement, paramXSDocumentInfo, paramSchemaGrammar);
        i = 1;
        localElement = DOMUtil.getNextSiblingElement(localElement);
      }
    }
    if ((localObject2 == null) && (localQName != null))
    {
      localObject3 = (XSTypeDefinition)fSchemaHandler.getGlobalDecl(paramXSDocumentInfo, 7, localQName, paramElement);
      if ((localObject3 != null) && (((XSTypeDefinition)localObject3).getTypeCategory() == 16))
      {
        localObject2 = (XSSimpleType)localObject3;
      }
      else
      {
        reportSchemaError("src-resolve", new Object[] { rawname, "simpleType definition" }, paramElement);
        if (localObject3 == null) {
          fUnresolvedTypeName = localQName;
        }
      }
    }
    if (localObject2 == null) {
      localObject2 = SchemaGrammar.fAnySimpleType;
    }
    if (localXSAnnotationImpl != null)
    {
      localObject3 = new XSObjectListImpl();
      ((XSObjectListImpl)localObject3).addXSObject(localXSAnnotationImpl);
    }
    else
    {
      localObject3 = XSObjectListImpl.EMPTY_LIST;
    }
    ((XSAttributeDecl)localObject1).setValues(str3, str4, (XSSimpleType)localObject2, s2, s1, localValidatedInfo, localXSComplexTypeDecl, (XSObjectList)localObject3);
    if (str3 == null)
    {
      if (paramBoolean) {
        reportSchemaError("s4s-att-must-appear", new Object[] { SchemaSymbols.ELT_ATTRIBUTE, SchemaSymbols.ATT_NAME }, paramElement);
      } else {
        reportSchemaError("src-attribute.3.1", null, paramElement);
      }
      str3 = "(no name)";
    }
    if (localElement != null) {
      reportSchemaError("s4s-elt-must-match.1", new Object[] { str3, "(annotation?, (simpleType?))", DOMUtil.getLocalName(localElement) }, localElement);
    }
    if ((str1 != null) && (str2 != null)) {
      reportSchemaError("src-attribute.1", new Object[] { str3 }, paramElement);
    }
    if ((i != 0) && (localQName != null)) {
      reportSchemaError("src-attribute.4", new Object[] { str3 }, paramElement);
    }
    checkNotationType(str3, (XSTypeDefinition)localObject2, paramElement);
    if (localValidatedInfo != null)
    {
      fValidationState.setNamespaceSupport(fNamespaceSupport);
      try
      {
        checkDefaultValid((XSAttributeDecl)localObject1);
      }
      catch (InvalidDatatypeValueException localInvalidDatatypeValueException)
      {
        reportSchemaError(localInvalidDatatypeValueException.getKey(), localInvalidDatatypeValueException.getArgs(), paramElement);
        reportSchemaError("a-props-correct.2", new Object[] { str3, normalizedValue }, paramElement);
        localValidatedInfo = null;
        s2 = 0;
        ((XSAttributeDecl)localObject1).setValues(str3, str4, (XSSimpleType)localObject2, s2, s1, localValidatedInfo, localXSComplexTypeDecl, (XSObjectList)localObject3);
      }
    }
    if ((localValidatedInfo != null) && (((XSSimpleType)localObject2).isIDType()))
    {
      reportSchemaError("a-props-correct.3", new Object[] { str3 }, paramElement);
      localValidatedInfo = null;
      s2 = 0;
      ((XSAttributeDecl)localObject1).setValues(str3, str4, (XSSimpleType)localObject2, s2, s1, localValidatedInfo, localXSComplexTypeDecl, (XSObjectList)localObject3);
    }
    if ((str3 != null) && (str3.equals(XMLSymbols.PREFIX_XMLNS)))
    {
      reportSchemaError("no-xmlns", null, paramElement);
      return null;
    }
    if ((str4 != null) && (str4.equals(SchemaSymbols.URI_XSI)))
    {
      reportSchemaError("no-xsi", new Object[] { SchemaSymbols.URI_XSI }, paramElement);
      return null;
    }
    if (str3.equals("(no name)")) {
      return null;
    }
    if (paramBoolean)
    {
      if (paramSchemaGrammar.getGlobalAttributeDecl(str3) == null) {
        paramSchemaGrammar.addGlobalAttributeDecl((XSAttributeDecl)localObject1);
      }
      String str5 = fSchemaHandler.schemaDocument2SystemId(paramXSDocumentInfo);
      XSAttributeDecl localXSAttributeDecl = paramSchemaGrammar.getGlobalAttributeDecl(str3, str5);
      if (localXSAttributeDecl == null) {
        paramSchemaGrammar.addGlobalAttributeDecl((XSAttributeDecl)localObject1, str5);
      }
      if (fSchemaHandler.fTolerateDuplicates)
      {
        if (localXSAttributeDecl != null) {
          localObject1 = localXSAttributeDecl;
        }
        fSchemaHandler.addGlobalAttributeDecl((XSAttributeDecl)localObject1);
      }
    }
    return localObject1;
  }
  
  void checkDefaultValid(XSAttributeDecl paramXSAttributeDecl)
    throws InvalidDatatypeValueException
  {
    ((XSSimpleType)paramXSAttributeDecl.getTypeDefinition()).validate(getValInfonormalizedValue, fValidationState, paramXSAttributeDecl.getValInfo());
    ((XSSimpleType)paramXSAttributeDecl.getTypeDefinition()).validate(paramXSAttributeDecl.getValInfo().stringValue(), fValidationState, paramXSAttributeDecl.getValInfo());
  }
  
  void checkDefaultValid(XSAttributeUseImpl paramXSAttributeUseImpl)
    throws InvalidDatatypeValueException
  {
    ((XSSimpleType)fAttrDecl.getTypeDefinition()).validate(fDefault.normalizedValue, fValidationState, fDefault);
    ((XSSimpleType)fAttrDecl.getTypeDefinition()).validate(fDefault.stringValue(), fValidationState, fDefault);
  }
}
