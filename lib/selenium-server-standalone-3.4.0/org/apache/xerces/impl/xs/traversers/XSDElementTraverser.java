package org.apache.xerces.impl.xs.traversers;

import java.util.Locale;
import org.apache.xerces.impl.dv.ValidatedInfo;
import org.apache.xerces.impl.dv.XSSimpleType;
import org.apache.xerces.impl.validation.ValidationState;
import org.apache.xerces.impl.xs.SchemaGrammar;
import org.apache.xerces.impl.xs.SchemaNamespaceSupport;
import org.apache.xerces.impl.xs.SchemaSymbols;
import org.apache.xerces.impl.xs.XSAnnotationImpl;
import org.apache.xerces.impl.xs.XSComplexTypeDecl;
import org.apache.xerces.impl.xs.XSConstraints;
import org.apache.xerces.impl.xs.XSDeclarationPool;
import org.apache.xerces.impl.xs.XSElementDecl;
import org.apache.xerces.impl.xs.XSParticleDecl;
import org.apache.xerces.impl.xs.util.XInt;
import org.apache.xerces.impl.xs.util.XSObjectListImpl;
import org.apache.xerces.util.DOMUtil;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLChar;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xs.XSObject;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSTypeDefinition;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

class XSDElementTraverser
  extends XSDAbstractTraverser
{
  protected final XSElementDecl fTempElementDecl = new XSElementDecl();
  boolean fDeferTraversingLocalElements;
  
  XSDElementTraverser(XSDHandler paramXSDHandler, XSAttributeChecker paramXSAttributeChecker)
  {
    super(paramXSDHandler, paramXSAttributeChecker);
  }
  
  XSParticleDecl traverseLocal(Element paramElement, XSDocumentInfo paramXSDocumentInfo, SchemaGrammar paramSchemaGrammar, int paramInt, XSObject paramXSObject)
  {
    XSParticleDecl localXSParticleDecl = null;
    if (fSchemaHandler.fDeclPool != null) {
      localXSParticleDecl = fSchemaHandler.fDeclPool.getParticleDecl();
    } else {
      localXSParticleDecl = new XSParticleDecl();
    }
    if (fDeferTraversingLocalElements)
    {
      fType = 1;
      Attr localAttr = paramElement.getAttributeNode(SchemaSymbols.ATT_MINOCCURS);
      if (localAttr != null)
      {
        String str = localAttr.getValue();
        try
        {
          int i = Integer.parseInt(XMLChar.trim(str));
          if (i >= 0) {
            fMinOccurs = i;
          }
        }
        catch (NumberFormatException localNumberFormatException) {}
      }
      fSchemaHandler.fillInLocalElemInfo(paramElement, paramXSDocumentInfo, paramInt, paramXSObject, localXSParticleDecl);
    }
    else
    {
      traverseLocal(localXSParticleDecl, paramElement, paramXSDocumentInfo, paramSchemaGrammar, paramInt, paramXSObject, null);
      if (fType == 0) {
        localXSParticleDecl = null;
      }
    }
    return localXSParticleDecl;
  }
  
  protected void traverseLocal(XSParticleDecl paramXSParticleDecl, Element paramElement, XSDocumentInfo paramXSDocumentInfo, SchemaGrammar paramSchemaGrammar, int paramInt, XSObject paramXSObject, String[] paramArrayOfString)
  {
    if (paramArrayOfString != null) {
      fNamespaceSupport.setEffectiveContext(paramArrayOfString);
    }
    Object[] arrayOfObject = fAttrChecker.checkAttributes(paramElement, false, paramXSDocumentInfo);
    QName localQName = (QName)arrayOfObject[XSAttributeChecker.ATTIDX_REF];
    XInt localXInt1 = (XInt)arrayOfObject[XSAttributeChecker.ATTIDX_MINOCCURS];
    XInt localXInt2 = (XInt)arrayOfObject[XSAttributeChecker.ATTIDX_MAXOCCURS];
    XSElementDecl localXSElementDecl = null;
    XSAnnotationImpl localXSAnnotationImpl = null;
    if (paramElement.getAttributeNode(SchemaSymbols.ATT_REF) != null)
    {
      if (localQName != null)
      {
        localXSElementDecl = (XSElementDecl)fSchemaHandler.getGlobalDecl(paramXSDocumentInfo, 3, localQName, paramElement);
        localObject = DOMUtil.getFirstChildElement(paramElement);
        if ((localObject != null) && (DOMUtil.getLocalName((Node)localObject).equals(SchemaSymbols.ELT_ANNOTATION)))
        {
          localXSAnnotationImpl = traverseAnnotationDecl((Element)localObject, arrayOfObject, false, paramXSDocumentInfo);
          localObject = DOMUtil.getNextSiblingElement((Node)localObject);
        }
        else
        {
          String str = DOMUtil.getSyntheticAnnotation(paramElement);
          if (str != null) {
            localXSAnnotationImpl = traverseSyntheticAnnotation(paramElement, str, arrayOfObject, false, paramXSDocumentInfo);
          }
        }
        if (localObject != null) {
          reportSchemaError("src-element.2.2", new Object[] { rawname, DOMUtil.getLocalName((Node)localObject) }, (Element)localObject);
        }
      }
      else
      {
        localXSElementDecl = null;
      }
    }
    else {
      localXSElementDecl = traverseNamedElement(paramElement, arrayOfObject, paramXSDocumentInfo, paramSchemaGrammar, false, paramXSObject);
    }
    fMinOccurs = localXInt1.intValue();
    fMaxOccurs = localXInt2.intValue();
    if (localXSElementDecl != null)
    {
      fType = 1;
      fValue = localXSElementDecl;
    }
    else
    {
      fType = 0;
    }
    if (localQName != null)
    {
      if (localXSAnnotationImpl != null)
      {
        localObject = new XSObjectListImpl();
        ((XSObjectListImpl)localObject).addXSObject(localXSAnnotationImpl);
      }
      else
      {
        localObject = XSObjectListImpl.EMPTY_LIST;
      }
      fAnnotations = ((XSObjectList)localObject);
    }
    else
    {
      fAnnotations = (localXSElementDecl != null ? fAnnotations : XSObjectListImpl.EMPTY_LIST);
    }
    Object localObject = (Long)arrayOfObject[XSAttributeChecker.ATTIDX_FROMDEFAULT];
    checkOccurrences(paramXSParticleDecl, SchemaSymbols.ELT_ELEMENT, (Element)paramElement.getParentNode(), paramInt, ((Long)localObject).longValue());
    fAttrChecker.returnAttrArray(arrayOfObject, paramXSDocumentInfo);
  }
  
  XSElementDecl traverseGlobal(Element paramElement, XSDocumentInfo paramXSDocumentInfo, SchemaGrammar paramSchemaGrammar)
  {
    Object[] arrayOfObject = fAttrChecker.checkAttributes(paramElement, true, paramXSDocumentInfo);
    XSElementDecl localXSElementDecl = traverseNamedElement(paramElement, arrayOfObject, paramXSDocumentInfo, paramSchemaGrammar, true, null);
    fAttrChecker.returnAttrArray(arrayOfObject, paramXSDocumentInfo);
    return localXSElementDecl;
  }
  
  XSElementDecl traverseNamedElement(Element paramElement, Object[] paramArrayOfObject, XSDocumentInfo paramXSDocumentInfo, SchemaGrammar paramSchemaGrammar, boolean paramBoolean, XSObject paramXSObject)
  {
    Boolean localBoolean1 = (Boolean)paramArrayOfObject[XSAttributeChecker.ATTIDX_ABSTRACT];
    XInt localXInt1 = (XInt)paramArrayOfObject[XSAttributeChecker.ATTIDX_BLOCK];
    String str1 = (String)paramArrayOfObject[XSAttributeChecker.ATTIDX_DEFAULT];
    XInt localXInt2 = (XInt)paramArrayOfObject[XSAttributeChecker.ATTIDX_FINAL];
    String str2 = (String)paramArrayOfObject[XSAttributeChecker.ATTIDX_FIXED];
    XInt localXInt3 = (XInt)paramArrayOfObject[XSAttributeChecker.ATTIDX_FORM];
    String str3 = (String)paramArrayOfObject[XSAttributeChecker.ATTIDX_NAME];
    Boolean localBoolean2 = (Boolean)paramArrayOfObject[XSAttributeChecker.ATTIDX_NILLABLE];
    QName localQName1 = (QName)paramArrayOfObject[XSAttributeChecker.ATTIDX_SUBSGROUP];
    QName localQName2 = (QName)paramArrayOfObject[XSAttributeChecker.ATTIDX_TYPE];
    Object localObject1 = null;
    if (fSchemaHandler.fDeclPool != null) {
      localObject1 = fSchemaHandler.fDeclPool.getElementDecl();
    } else {
      localObject1 = new XSElementDecl();
    }
    if (str3 != null) {
      fName = fSymbolTable.addSymbol(str3);
    }
    if (paramBoolean)
    {
      fTargetNamespace = fTargetNamespace;
      ((XSElementDecl)localObject1).setIsGlobal();
    }
    else
    {
      if ((paramXSObject instanceof XSComplexTypeDecl)) {
        ((XSElementDecl)localObject1).setIsLocal((XSComplexTypeDecl)paramXSObject);
      }
      if (localXInt3 != null)
      {
        if (localXInt3.intValue() == 1) {
          fTargetNamespace = fTargetNamespace;
        } else {
          fTargetNamespace = null;
        }
      }
      else if (fAreLocalElementsQualified) {
        fTargetNamespace = fTargetNamespace;
      } else {
        fTargetNamespace = null;
      }
    }
    fBlock = (localXInt1 == null ? fBlockDefault : localXInt1.shortValue());
    fFinal = (localXInt2 == null ? fFinalDefault : localXInt2.shortValue());
    Object tmp302_300 = localObject1;
    302300fBlock = ((short)(302300fBlock & 0x7));
    Object tmp315_313 = localObject1;
    315313fFinal = ((short)(315313fFinal & 0x3));
    if (localBoolean2.booleanValue()) {
      ((XSElementDecl)localObject1).setIsNillable();
    }
    if ((localBoolean1 != null) && (localBoolean1.booleanValue())) {
      ((XSElementDecl)localObject1).setIsAbstract();
    }
    if (str2 != null)
    {
      fDefault = new ValidatedInfo();
      fDefault.normalizedValue = str2;
      ((XSElementDecl)localObject1).setConstraintType((short)2);
    }
    else if (str1 != null)
    {
      fDefault = new ValidatedInfo();
      fDefault.normalizedValue = str1;
      ((XSElementDecl)localObject1).setConstraintType((short)1);
    }
    else
    {
      ((XSElementDecl)localObject1).setConstraintType((short)0);
    }
    if (localQName1 != null) {
      fSubGroup = ((XSElementDecl)fSchemaHandler.getGlobalDecl(paramXSDocumentInfo, 3, localQName1, paramElement));
    }
    Element localElement = DOMUtil.getFirstChildElement(paramElement);
    XSAnnotationImpl localXSAnnotationImpl = null;
    Object localObject2;
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
    if (localXSAnnotationImpl != null)
    {
      localObject2 = new XSObjectListImpl();
      ((XSObjectListImpl)localObject2).addXSObject(localXSAnnotationImpl);
    }
    else
    {
      localObject2 = XSObjectListImpl.EMPTY_LIST;
    }
    fAnnotations = ((XSObjectList)localObject2);
    Object localObject3 = null;
    int i = 0;
    String str4;
    if (localElement != null)
    {
      str4 = DOMUtil.getLocalName(localElement);
      if (str4.equals(SchemaSymbols.ELT_COMPLEXTYPE))
      {
        localObject3 = fSchemaHandler.fComplexTypeTraverser.traverseLocal(localElement, paramXSDocumentInfo, paramSchemaGrammar);
        i = 1;
        localElement = DOMUtil.getNextSiblingElement(localElement);
      }
      else if (str4.equals(SchemaSymbols.ELT_SIMPLETYPE))
      {
        localObject3 = fSchemaHandler.fSimpleTypeTraverser.traverseLocal(localElement, paramXSDocumentInfo, paramSchemaGrammar);
        i = 1;
        localElement = DOMUtil.getNextSiblingElement(localElement);
      }
    }
    if ((localObject3 == null) && (localQName2 != null))
    {
      localObject3 = (XSTypeDefinition)fSchemaHandler.getGlobalDecl(paramXSDocumentInfo, 7, localQName2, paramElement);
      if (localObject3 == null) {
        fUnresolvedTypeName = localQName2;
      }
    }
    if ((localObject3 == null) && (fSubGroup != null)) {
      localObject3 = fSubGroup.fType;
    }
    if (localObject3 == null) {
      localObject3 = SchemaGrammar.fAnyType;
    }
    fType = ((XSTypeDefinition)localObject3);
    if (localElement != null)
    {
      str4 = DOMUtil.getLocalName(localElement);
      while ((localElement != null) && ((str4.equals(SchemaSymbols.ELT_KEY)) || (str4.equals(SchemaSymbols.ELT_KEYREF)) || (str4.equals(SchemaSymbols.ELT_UNIQUE))))
      {
        if ((str4.equals(SchemaSymbols.ELT_KEY)) || (str4.equals(SchemaSymbols.ELT_UNIQUE)))
        {
          DOMUtil.setHidden(localElement, fSchemaHandler.fHiddenNodes);
          fSchemaHandler.fUniqueOrKeyTraverser.traverse(localElement, (XSElementDecl)localObject1, paramXSDocumentInfo, paramSchemaGrammar);
          if (DOMUtil.getAttrValue(localElement, SchemaSymbols.ATT_NAME).length() != 0) {
            fSchemaHandler.checkForDuplicateNames(fTargetNamespace + "," + DOMUtil.getAttrValue(localElement, SchemaSymbols.ATT_NAME), 1, fSchemaHandler.getIDRegistry(), fSchemaHandler.getIDRegistry_sub(), localElement, paramXSDocumentInfo);
          }
        }
        else if (str4.equals(SchemaSymbols.ELT_KEYREF))
        {
          fSchemaHandler.storeKeyRef(localElement, paramXSDocumentInfo, (XSElementDecl)localObject1);
        }
        localElement = DOMUtil.getNextSiblingElement(localElement);
        if (localElement != null) {
          str4 = DOMUtil.getLocalName(localElement);
        }
      }
    }
    if (str3 == null)
    {
      if (paramBoolean) {
        reportSchemaError("s4s-att-must-appear", new Object[] { SchemaSymbols.ELT_ELEMENT, SchemaSymbols.ATT_NAME }, paramElement);
      } else {
        reportSchemaError("src-element.2.1", null, paramElement);
      }
      str3 = "(no name)";
    }
    if (localElement != null) {
      reportSchemaError("s4s-elt-must-match.1", new Object[] { str3, "(annotation?, (simpleType | complexType)?, (unique | key | keyref)*))", DOMUtil.getLocalName(localElement) }, localElement);
    }
    if ((str1 != null) && (str2 != null)) {
      reportSchemaError("src-element.1", new Object[] { str3 }, paramElement);
    }
    if ((i != 0) && (localQName2 != null)) {
      reportSchemaError("src-element.3", new Object[] { str3 }, paramElement);
    }
    checkNotationType(str3, (XSTypeDefinition)localObject3, paramElement);
    if (fDefault != null)
    {
      fValidationState.setNamespaceSupport(fNamespaceSupport);
      if (XSConstraints.ElementDefaultValidImmediate(fType, fDefault.normalizedValue, fValidationState, fDefault) == null)
      {
        reportSchemaError("e-props-correct.2", new Object[] { str3, fDefault.normalizedValue }, paramElement);
        fDefault = null;
        ((XSElementDecl)localObject1).setConstraintType((short)0);
      }
    }
    if ((fSubGroup != null) && (!XSConstraints.checkTypeDerivationOk(fType, fSubGroup.fType, fSubGroup.fFinal)))
    {
      reportSchemaError("e-props-correct.4", new Object[] { str3, prefix + ":" + localpart }, paramElement);
      fSubGroup = null;
    }
    if ((fDefault != null) && (((((XSTypeDefinition)localObject3).getTypeCategory() == 16) && (((XSSimpleType)localObject3).isIDType())) || ((((XSTypeDefinition)localObject3).getTypeCategory() == 15) && (((XSComplexTypeDecl)localObject3).containsTypeID()))))
    {
      reportSchemaError("e-props-correct.5", new Object[] { fName }, paramElement);
      fDefault = null;
      ((XSElementDecl)localObject1).setConstraintType((short)0);
    }
    if (fName == null) {
      return null;
    }
    if (paramBoolean)
    {
      paramSchemaGrammar.addGlobalElementDeclAll((XSElementDecl)localObject1);
      if (paramSchemaGrammar.getGlobalElementDecl(fName) == null) {
        paramSchemaGrammar.addGlobalElementDecl((XSElementDecl)localObject1);
      }
      str4 = fSchemaHandler.schemaDocument2SystemId(paramXSDocumentInfo);
      XSElementDecl localXSElementDecl = paramSchemaGrammar.getGlobalElementDecl(fName, str4);
      if (localXSElementDecl == null) {
        paramSchemaGrammar.addGlobalElementDecl((XSElementDecl)localObject1, str4);
      }
      if (fSchemaHandler.fTolerateDuplicates)
      {
        if (localXSElementDecl != null) {
          localObject1 = localXSElementDecl;
        }
        fSchemaHandler.addGlobalElementDecl((XSElementDecl)localObject1);
      }
    }
    return localObject1;
  }
  
  void reset(SymbolTable paramSymbolTable, boolean paramBoolean, Locale paramLocale)
  {
    super.reset(paramSymbolTable, paramBoolean, paramLocale);
    fDeferTraversingLocalElements = true;
  }
}
