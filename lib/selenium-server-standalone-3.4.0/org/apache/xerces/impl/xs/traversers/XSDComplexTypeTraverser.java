package org.apache.xerces.impl.xs.traversers;

import org.apache.xerces.impl.dv.InvalidDatatypeFacetException;
import org.apache.xerces.impl.dv.SchemaDVFactory;
import org.apache.xerces.impl.dv.XSFacets;
import org.apache.xerces.impl.dv.XSSimpleType;
import org.apache.xerces.impl.dv.xs.XSSimpleTypeDecl;
import org.apache.xerces.impl.validation.ValidationState;
import org.apache.xerces.impl.xs.SchemaGrammar;
import org.apache.xerces.impl.xs.SchemaSymbols;
import org.apache.xerces.impl.xs.XSAnnotationImpl;
import org.apache.xerces.impl.xs.XSAttributeDecl;
import org.apache.xerces.impl.xs.XSAttributeGroupDecl;
import org.apache.xerces.impl.xs.XSAttributeUseImpl;
import org.apache.xerces.impl.xs.XSComplexTypeDecl;
import org.apache.xerces.impl.xs.XSConstraints;
import org.apache.xerces.impl.xs.XSModelGroupImpl;
import org.apache.xerces.impl.xs.XSParticleDecl;
import org.apache.xerces.impl.xs.XSWildcardDecl;
import org.apache.xerces.impl.xs.util.XInt;
import org.apache.xerces.impl.xs.util.XSObjectListImpl;
import org.apache.xerces.util.DOMUtil;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xs.XSAttributeUse;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSTerm;
import org.apache.xerces.xs.XSTypeDefinition;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

class XSDComplexTypeTraverser
  extends XSDAbstractParticleTraverser
{
  private static final int GLOBAL_NUM = 11;
  private static XSParticleDecl fErrorContent = null;
  private static XSWildcardDecl fErrorWildcard = null;
  private String fName = null;
  private String fTargetNamespace = null;
  private short fDerivedBy = 2;
  private short fFinal = 0;
  private short fBlock = 0;
  private short fContentType = 0;
  private XSTypeDefinition fBaseType = null;
  private XSAttributeGroupDecl fAttrGrp = null;
  private XSSimpleType fXSSimpleType = null;
  private XSParticleDecl fParticle = null;
  private boolean fIsAbstract = false;
  private XSComplexTypeDecl fComplexTypeDecl = null;
  private XSAnnotationImpl[] fAnnotations = null;
  private Object[] fGlobalStore = null;
  private int fGlobalStorePos = 0;
  private static final boolean DEBUG = false;
  
  private static XSParticleDecl getErrorContent()
  {
    if (fErrorContent == null)
    {
      XSParticleDecl localXSParticleDecl1 = new XSParticleDecl();
      fType = 2;
      fValue = getErrorWildcard();
      fMinOccurs = 0;
      fMaxOccurs = -1;
      XSModelGroupImpl localXSModelGroupImpl = new XSModelGroupImpl();
      fCompositor = 102;
      fParticleCount = 1;
      fParticles = new XSParticleDecl[1];
      fParticles[0] = localXSParticleDecl1;
      XSParticleDecl localXSParticleDecl2 = new XSParticleDecl();
      fType = 3;
      fValue = localXSModelGroupImpl;
      fErrorContent = localXSParticleDecl2;
    }
    return fErrorContent;
  }
  
  private static XSWildcardDecl getErrorWildcard()
  {
    if (fErrorWildcard == null)
    {
      XSWildcardDecl localXSWildcardDecl = new XSWildcardDecl();
      fProcessContents = 2;
      fErrorWildcard = localXSWildcardDecl;
    }
    return fErrorWildcard;
  }
  
  XSDComplexTypeTraverser(XSDHandler paramXSDHandler, XSAttributeChecker paramXSAttributeChecker)
  {
    super(paramXSDHandler, paramXSAttributeChecker);
  }
  
  XSComplexTypeDecl traverseLocal(Element paramElement, XSDocumentInfo paramXSDocumentInfo, SchemaGrammar paramSchemaGrammar)
  {
    Object[] arrayOfObject = fAttrChecker.checkAttributes(paramElement, false, paramXSDocumentInfo);
    String str = genAnonTypeName(paramElement);
    contentBackup();
    XSComplexTypeDecl localXSComplexTypeDecl = traverseComplexTypeDecl(paramElement, str, arrayOfObject, paramXSDocumentInfo, paramSchemaGrammar);
    contentRestore();
    paramSchemaGrammar.addComplexTypeDecl(localXSComplexTypeDecl, fSchemaHandler.element2Locator(paramElement));
    localXSComplexTypeDecl.setIsAnonymous();
    fAttrChecker.returnAttrArray(arrayOfObject, paramXSDocumentInfo);
    return localXSComplexTypeDecl;
  }
  
  XSComplexTypeDecl traverseGlobal(Element paramElement, XSDocumentInfo paramXSDocumentInfo, SchemaGrammar paramSchemaGrammar)
  {
    Object[] arrayOfObject = fAttrChecker.checkAttributes(paramElement, true, paramXSDocumentInfo);
    String str1 = (String)arrayOfObject[XSAttributeChecker.ATTIDX_NAME];
    contentBackup();
    XSComplexTypeDecl localXSComplexTypeDecl = traverseComplexTypeDecl(paramElement, str1, arrayOfObject, paramXSDocumentInfo, paramSchemaGrammar);
    contentRestore();
    paramSchemaGrammar.addComplexTypeDecl(localXSComplexTypeDecl, fSchemaHandler.element2Locator(paramElement));
    if (str1 == null)
    {
      reportSchemaError("s4s-att-must-appear", new Object[] { SchemaSymbols.ELT_COMPLEXTYPE, SchemaSymbols.ATT_NAME }, paramElement);
      localXSComplexTypeDecl = null;
    }
    else
    {
      if (paramSchemaGrammar.getGlobalTypeDecl(localXSComplexTypeDecl.getName()) == null) {
        paramSchemaGrammar.addGlobalComplexTypeDecl(localXSComplexTypeDecl);
      }
      String str2 = fSchemaHandler.schemaDocument2SystemId(paramXSDocumentInfo);
      XSTypeDefinition localXSTypeDefinition = paramSchemaGrammar.getGlobalTypeDecl(localXSComplexTypeDecl.getName(), str2);
      if (localXSTypeDefinition == null) {
        paramSchemaGrammar.addGlobalComplexTypeDecl(localXSComplexTypeDecl, str2);
      }
      if (fSchemaHandler.fTolerateDuplicates)
      {
        if ((localXSTypeDefinition != null) && ((localXSTypeDefinition instanceof XSComplexTypeDecl))) {
          localXSComplexTypeDecl = (XSComplexTypeDecl)localXSTypeDefinition;
        }
        fSchemaHandler.addGlobalTypeDecl(localXSComplexTypeDecl);
      }
    }
    fAttrChecker.returnAttrArray(arrayOfObject, paramXSDocumentInfo);
    return localXSComplexTypeDecl;
  }
  
  private XSComplexTypeDecl traverseComplexTypeDecl(Element paramElement, String paramString, Object[] paramArrayOfObject, XSDocumentInfo paramXSDocumentInfo, SchemaGrammar paramSchemaGrammar)
  {
    fComplexTypeDecl = new XSComplexTypeDecl();
    fAttrGrp = new XSAttributeGroupDecl();
    Boolean localBoolean1 = (Boolean)paramArrayOfObject[XSAttributeChecker.ATTIDX_ABSTRACT];
    XInt localXInt1 = (XInt)paramArrayOfObject[XSAttributeChecker.ATTIDX_BLOCK];
    Boolean localBoolean2 = (Boolean)paramArrayOfObject[XSAttributeChecker.ATTIDX_MIXED];
    XInt localXInt2 = (XInt)paramArrayOfObject[XSAttributeChecker.ATTIDX_FINAL];
    fName = paramString;
    fComplexTypeDecl.setName(fName);
    fTargetNamespace = fTargetNamespace;
    fBlock = (localXInt1 == null ? fBlockDefault : localXInt1.shortValue());
    fFinal = (localXInt2 == null ? fFinalDefault : localXInt2.shortValue());
    fBlock = ((short)(fBlock & 0x3));
    fFinal = ((short)(fFinal & 0x3));
    fIsAbstract = ((localBoolean1 != null) && (localBoolean1.booleanValue()));
    fAnnotations = null;
    Element localElement = null;
    try
    {
      localElement = DOMUtil.getFirstChildElement(paramElement);
      Object localObject;
      if (localElement != null)
      {
        if (DOMUtil.getLocalName(localElement).equals(SchemaSymbols.ELT_ANNOTATION))
        {
          addAnnotation(traverseAnnotationDecl(localElement, paramArrayOfObject, false, paramXSDocumentInfo));
          localElement = DOMUtil.getNextSiblingElement(localElement);
        }
        else
        {
          localObject = DOMUtil.getSyntheticAnnotation(paramElement);
          if (localObject != null) {
            addAnnotation(traverseSyntheticAnnotation(paramElement, (String)localObject, paramArrayOfObject, false, paramXSDocumentInfo));
          }
        }
        if ((localElement != null) && (DOMUtil.getLocalName(localElement).equals(SchemaSymbols.ELT_ANNOTATION))) {
          throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[] { fName, SchemaSymbols.ELT_ANNOTATION }, localElement);
        }
      }
      else
      {
        localObject = DOMUtil.getSyntheticAnnotation(paramElement);
        if (localObject != null) {
          addAnnotation(traverseSyntheticAnnotation(paramElement, (String)localObject, paramArrayOfObject, false, paramXSDocumentInfo));
        }
      }
      if (localElement == null)
      {
        fBaseType = SchemaGrammar.fAnyType;
        fDerivedBy = 2;
        processComplexContent(localElement, localBoolean2.booleanValue(), false, paramXSDocumentInfo, paramSchemaGrammar);
      }
      else
      {
        String str;
        if (DOMUtil.getLocalName(localElement).equals(SchemaSymbols.ELT_SIMPLECONTENT))
        {
          traverseSimpleContent(localElement, paramXSDocumentInfo, paramSchemaGrammar);
          localObject = DOMUtil.getNextSiblingElement(localElement);
          if (localObject != null)
          {
            str = DOMUtil.getLocalName((Node)localObject);
            throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[] { fName, str }, (Element)localObject);
          }
        }
        else if (DOMUtil.getLocalName(localElement).equals(SchemaSymbols.ELT_COMPLEXCONTENT))
        {
          traverseComplexContent(localElement, localBoolean2.booleanValue(), paramXSDocumentInfo, paramSchemaGrammar);
          localObject = DOMUtil.getNextSiblingElement(localElement);
          if (localObject != null)
          {
            str = DOMUtil.getLocalName((Node)localObject);
            throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[] { fName, str }, (Element)localObject);
          }
        }
        else
        {
          fBaseType = SchemaGrammar.fAnyType;
          fDerivedBy = 2;
          processComplexContent(localElement, localBoolean2.booleanValue(), false, paramXSDocumentInfo, paramSchemaGrammar);
        }
      }
    }
    catch (ComplexTypeRecoverableError localComplexTypeRecoverableError)
    {
      handleComplexTypeError(localComplexTypeRecoverableError.getMessage(), errorSubstText, errorElem);
    }
    fComplexTypeDecl.setValues(fName, fTargetNamespace, fBaseType, fDerivedBy, fFinal, fBlock, fContentType, fIsAbstract, fAttrGrp, fXSSimpleType, fParticle, new XSObjectListImpl(fAnnotations, fAnnotations == null ? 0 : fAnnotations.length));
    return fComplexTypeDecl;
  }
  
  private void traverseSimpleContent(Element paramElement, XSDocumentInfo paramXSDocumentInfo, SchemaGrammar paramSchemaGrammar)
    throws XSDComplexTypeTraverser.ComplexTypeRecoverableError
  {
    Object[] arrayOfObject1 = fAttrChecker.checkAttributes(paramElement, false, paramXSDocumentInfo);
    fContentType = 1;
    fParticle = null;
    Element localElement1 = DOMUtil.getFirstChildElement(paramElement);
    if ((localElement1 != null) && (DOMUtil.getLocalName(localElement1).equals(SchemaSymbols.ELT_ANNOTATION)))
    {
      addAnnotation(traverseAnnotationDecl(localElement1, arrayOfObject1, false, paramXSDocumentInfo));
      localElement1 = DOMUtil.getNextSiblingElement(localElement1);
    }
    else
    {
      str = DOMUtil.getSyntheticAnnotation(paramElement);
      if (str != null) {
        addAnnotation(traverseSyntheticAnnotation(paramElement, str, arrayOfObject1, false, paramXSDocumentInfo));
      }
    }
    if (localElement1 == null)
    {
      fAttrChecker.returnAttrArray(arrayOfObject1, paramXSDocumentInfo);
      throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.2", new Object[] { fName, SchemaSymbols.ELT_SIMPLECONTENT }, paramElement);
    }
    String str = DOMUtil.getLocalName(localElement1);
    if (str.equals(SchemaSymbols.ELT_RESTRICTION))
    {
      fDerivedBy = 2;
    }
    else if (str.equals(SchemaSymbols.ELT_EXTENSION))
    {
      fDerivedBy = 1;
    }
    else
    {
      fAttrChecker.returnAttrArray(arrayOfObject1, paramXSDocumentInfo);
      throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[] { fName, str }, localElement1);
    }
    Element localElement2 = DOMUtil.getNextSiblingElement(localElement1);
    if (localElement2 != null)
    {
      fAttrChecker.returnAttrArray(arrayOfObject1, paramXSDocumentInfo);
      localObject1 = DOMUtil.getLocalName(localElement2);
      throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[] { fName, localObject1 }, localElement2);
    }
    Object localObject1 = fAttrChecker.checkAttributes(localElement1, false, paramXSDocumentInfo);
    QName localQName = (QName)localObject1[XSAttributeChecker.ATTIDX_BASE];
    if (localQName == null)
    {
      fAttrChecker.returnAttrArray(arrayOfObject1, paramXSDocumentInfo);
      fAttrChecker.returnAttrArray((Object[])localObject1, paramXSDocumentInfo);
      throw new ComplexTypeRecoverableError("s4s-att-must-appear", new Object[] { str, "base" }, localElement1);
    }
    XSTypeDefinition localXSTypeDefinition = (XSTypeDefinition)fSchemaHandler.getGlobalDecl(paramXSDocumentInfo, 7, localQName, localElement1);
    if (localXSTypeDefinition == null)
    {
      fAttrChecker.returnAttrArray(arrayOfObject1, paramXSDocumentInfo);
      fAttrChecker.returnAttrArray((Object[])localObject1, paramXSDocumentInfo);
      throw new ComplexTypeRecoverableError();
    }
    fBaseType = localXSTypeDefinition;
    Object localObject2 = null;
    XSComplexTypeDecl localXSComplexTypeDecl = null;
    int i = 0;
    if (localXSTypeDefinition.getTypeCategory() == 15)
    {
      localXSComplexTypeDecl = (XSComplexTypeDecl)localXSTypeDefinition;
      i = localXSComplexTypeDecl.getFinal();
      if (localXSComplexTypeDecl.getContentType() == 1)
      {
        localObject2 = (XSSimpleType)localXSComplexTypeDecl.getSimpleType();
      }
      else if ((fDerivedBy != 2) || (localXSComplexTypeDecl.getContentType() != 3) || (!((XSParticleDecl)localXSComplexTypeDecl.getParticle()).emptiable()))
      {
        fAttrChecker.returnAttrArray(arrayOfObject1, paramXSDocumentInfo);
        fAttrChecker.returnAttrArray((Object[])localObject1, paramXSDocumentInfo);
        throw new ComplexTypeRecoverableError("src-ct.2.1", new Object[] { fName, localXSComplexTypeDecl.getName() }, localElement1);
      }
    }
    else
    {
      localObject2 = (XSSimpleType)localXSTypeDefinition;
      if (fDerivedBy == 2)
      {
        fAttrChecker.returnAttrArray(arrayOfObject1, paramXSDocumentInfo);
        fAttrChecker.returnAttrArray((Object[])localObject1, paramXSDocumentInfo);
        throw new ComplexTypeRecoverableError("src-ct.2.1", new Object[] { fName, ((XSSimpleType)localObject2).getName() }, localElement1);
      }
      i = ((XSSimpleType)localObject2).getFinal();
    }
    if ((i & fDerivedBy) != 0)
    {
      fAttrChecker.returnAttrArray(arrayOfObject1, paramXSDocumentInfo);
      fAttrChecker.returnAttrArray((Object[])localObject1, paramXSDocumentInfo);
      localObject3 = fDerivedBy == 1 ? "cos-ct-extends.1.1" : "derivation-ok-restriction.1";
      throw new ComplexTypeRecoverableError((String)localObject3, new Object[] { fName, fBaseType.getName() }, localElement1);
    }
    Object localObject3 = localElement1;
    localElement1 = DOMUtil.getFirstChildElement(localElement1);
    Object localObject4;
    if (localElement1 != null)
    {
      if (DOMUtil.getLocalName(localElement1).equals(SchemaSymbols.ELT_ANNOTATION))
      {
        addAnnotation(traverseAnnotationDecl(localElement1, (Object[])localObject1, false, paramXSDocumentInfo));
        localElement1 = DOMUtil.getNextSiblingElement(localElement1);
      }
      else
      {
        localObject4 = DOMUtil.getSyntheticAnnotation((Node)localObject3);
        if (localObject4 != null) {
          addAnnotation(traverseSyntheticAnnotation((Element)localObject3, (String)localObject4, (Object[])localObject1, false, paramXSDocumentInfo));
        }
      }
      if ((localElement1 != null) && (DOMUtil.getLocalName(localElement1).equals(SchemaSymbols.ELT_ANNOTATION)))
      {
        fAttrChecker.returnAttrArray(arrayOfObject1, paramXSDocumentInfo);
        fAttrChecker.returnAttrArray((Object[])localObject1, paramXSDocumentInfo);
        throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[] { fName, SchemaSymbols.ELT_ANNOTATION }, localElement1);
      }
    }
    else
    {
      localObject4 = DOMUtil.getSyntheticAnnotation((Node)localObject3);
      if (localObject4 != null) {
        addAnnotation(traverseSyntheticAnnotation((Element)localObject3, (String)localObject4, (Object[])localObject1, false, paramXSDocumentInfo));
      }
    }
    Object localObject5;
    if (fDerivedBy == 2)
    {
      if ((localElement1 != null) && (DOMUtil.getLocalName(localElement1).equals(SchemaSymbols.ELT_SIMPLETYPE)))
      {
        localObject4 = fSchemaHandler.fSimpleTypeTraverser.traverseLocal(localElement1, paramXSDocumentInfo, paramSchemaGrammar);
        if (localObject4 == null)
        {
          fAttrChecker.returnAttrArray(arrayOfObject1, paramXSDocumentInfo);
          fAttrChecker.returnAttrArray((Object[])localObject1, paramXSDocumentInfo);
          throw new ComplexTypeRecoverableError();
        }
        if ((localObject2 != null) && (!XSConstraints.checkSimpleDerivationOk((XSSimpleType)localObject4, (XSTypeDefinition)localObject2, ((XSSimpleType)localObject2).getFinal())))
        {
          fAttrChecker.returnAttrArray(arrayOfObject1, paramXSDocumentInfo);
          fAttrChecker.returnAttrArray((Object[])localObject1, paramXSDocumentInfo);
          throw new ComplexTypeRecoverableError("derivation-ok-restriction.5.2.2.1", new Object[] { fName, ((XSSimpleType)localObject4).getName(), ((XSSimpleType)localObject2).getName() }, localElement1);
        }
        localObject2 = localObject4;
        localElement1 = DOMUtil.getNextSiblingElement(localElement1);
      }
      if (localObject2 == null)
      {
        fAttrChecker.returnAttrArray(arrayOfObject1, paramXSDocumentInfo);
        fAttrChecker.returnAttrArray((Object[])localObject1, paramXSDocumentInfo);
        throw new ComplexTypeRecoverableError("src-ct.2.2", new Object[] { fName }, localElement1);
      }
      localObject4 = null;
      localObject5 = null;
      short s1 = 0;
      short s2 = 0;
      if (localElement1 != null)
      {
        localObject6 = traverseFacets(localElement1, (XSSimpleType)localObject2, paramXSDocumentInfo);
        localObject4 = nodeAfterFacets;
        localObject5 = facetdata;
        s1 = fPresentFacets;
        s2 = fFixedFacets;
      }
      Object localObject6 = genAnonTypeName(paramElement);
      fXSSimpleType = fSchemaHandler.fDVFactory.createTypeRestriction((String)localObject6, fTargetNamespace, (short)0, (XSSimpleType)localObject2, null);
      try
      {
        fValidationState.setNamespaceSupport(fNamespaceSupport);
        fXSSimpleType.applyFacets((XSFacets)localObject5, s1, s2, fValidationState);
      }
      catch (InvalidDatatypeFacetException localInvalidDatatypeFacetException)
      {
        reportSchemaError(localInvalidDatatypeFacetException.getKey(), localInvalidDatatypeFacetException.getArgs(), localElement1);
        fXSSimpleType = fSchemaHandler.fDVFactory.createTypeRestriction((String)localObject6, fTargetNamespace, (short)0, (XSSimpleType)localObject2, null);
      }
      if ((fXSSimpleType instanceof XSSimpleTypeDecl)) {
        ((XSSimpleTypeDecl)fXSSimpleType).setAnonymous(true);
      }
      if (localObject4 != null)
      {
        if (!isAttrOrAttrGroup((Element)localObject4))
        {
          fAttrChecker.returnAttrArray(arrayOfObject1, paramXSDocumentInfo);
          fAttrChecker.returnAttrArray((Object[])localObject1, paramXSDocumentInfo);
          throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[] { fName, DOMUtil.getLocalName((Node)localObject4) }, (Element)localObject4);
        }
        Element localElement3 = traverseAttrsAndAttrGrps((Element)localObject4, fAttrGrp, paramXSDocumentInfo, paramSchemaGrammar, fComplexTypeDecl);
        if (localElement3 != null)
        {
          fAttrChecker.returnAttrArray(arrayOfObject1, paramXSDocumentInfo);
          fAttrChecker.returnAttrArray((Object[])localObject1, paramXSDocumentInfo);
          throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[] { fName, DOMUtil.getLocalName(localElement3) }, localElement3);
        }
      }
      try
      {
        mergeAttributes(localXSComplexTypeDecl.getAttrGrp(), fAttrGrp, fName, false, paramElement);
      }
      catch (ComplexTypeRecoverableError localComplexTypeRecoverableError2)
      {
        fAttrChecker.returnAttrArray(arrayOfObject1, paramXSDocumentInfo);
        fAttrChecker.returnAttrArray((Object[])localObject1, paramXSDocumentInfo);
        throw localComplexTypeRecoverableError2;
      }
      fAttrGrp.removeProhibitedAttrs();
      Object[] arrayOfObject2 = fAttrGrp.validRestrictionOf(fName, localXSComplexTypeDecl.getAttrGrp());
      if (arrayOfObject2 != null)
      {
        fAttrChecker.returnAttrArray(arrayOfObject1, paramXSDocumentInfo);
        fAttrChecker.returnAttrArray((Object[])localObject1, paramXSDocumentInfo);
        throw new ComplexTypeRecoverableError((String)arrayOfObject2[(arrayOfObject2.length - 1)], arrayOfObject2, (Element)localObject4);
      }
    }
    else
    {
      fXSSimpleType = ((XSSimpleType)localObject2);
      if (localElement1 != null)
      {
        localObject4 = localElement1;
        if (!isAttrOrAttrGroup((Element)localObject4))
        {
          fAttrChecker.returnAttrArray(arrayOfObject1, paramXSDocumentInfo);
          fAttrChecker.returnAttrArray((Object[])localObject1, paramXSDocumentInfo);
          throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[] { fName, DOMUtil.getLocalName((Node)localObject4) }, (Element)localObject4);
        }
        localObject5 = traverseAttrsAndAttrGrps((Element)localObject4, fAttrGrp, paramXSDocumentInfo, paramSchemaGrammar, fComplexTypeDecl);
        if (localObject5 != null)
        {
          fAttrChecker.returnAttrArray(arrayOfObject1, paramXSDocumentInfo);
          fAttrChecker.returnAttrArray((Object[])localObject1, paramXSDocumentInfo);
          throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[] { fName, DOMUtil.getLocalName((Node)localObject5) }, (Element)localObject5);
        }
        fAttrGrp.removeProhibitedAttrs();
      }
      if (localXSComplexTypeDecl != null) {
        try
        {
          mergeAttributes(localXSComplexTypeDecl.getAttrGrp(), fAttrGrp, fName, true, paramElement);
        }
        catch (ComplexTypeRecoverableError localComplexTypeRecoverableError1)
        {
          fAttrChecker.returnAttrArray(arrayOfObject1, paramXSDocumentInfo);
          fAttrChecker.returnAttrArray((Object[])localObject1, paramXSDocumentInfo);
          throw localComplexTypeRecoverableError1;
        }
      }
    }
    fAttrChecker.returnAttrArray(arrayOfObject1, paramXSDocumentInfo);
    fAttrChecker.returnAttrArray((Object[])localObject1, paramXSDocumentInfo);
  }
  
  private void traverseComplexContent(Element paramElement, boolean paramBoolean, XSDocumentInfo paramXSDocumentInfo, SchemaGrammar paramSchemaGrammar)
    throws XSDComplexTypeTraverser.ComplexTypeRecoverableError
  {
    Object[] arrayOfObject = fAttrChecker.checkAttributes(paramElement, false, paramXSDocumentInfo);
    boolean bool = paramBoolean;
    Boolean localBoolean = (Boolean)arrayOfObject[XSAttributeChecker.ATTIDX_MIXED];
    if (localBoolean != null) {
      bool = localBoolean.booleanValue();
    }
    fXSSimpleType = null;
    Element localElement1 = DOMUtil.getFirstChildElement(paramElement);
    if ((localElement1 != null) && (DOMUtil.getLocalName(localElement1).equals(SchemaSymbols.ELT_ANNOTATION)))
    {
      addAnnotation(traverseAnnotationDecl(localElement1, arrayOfObject, false, paramXSDocumentInfo));
      localElement1 = DOMUtil.getNextSiblingElement(localElement1);
    }
    else
    {
      str1 = DOMUtil.getSyntheticAnnotation(paramElement);
      if (str1 != null) {
        addAnnotation(traverseSyntheticAnnotation(paramElement, str1, arrayOfObject, false, paramXSDocumentInfo));
      }
    }
    if (localElement1 == null)
    {
      fAttrChecker.returnAttrArray(arrayOfObject, paramXSDocumentInfo);
      throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.2", new Object[] { fName, SchemaSymbols.ELT_COMPLEXCONTENT }, paramElement);
    }
    String str1 = DOMUtil.getLocalName(localElement1);
    if (str1.equals(SchemaSymbols.ELT_RESTRICTION))
    {
      fDerivedBy = 2;
    }
    else if (str1.equals(SchemaSymbols.ELT_EXTENSION))
    {
      fDerivedBy = 1;
    }
    else
    {
      fAttrChecker.returnAttrArray(arrayOfObject, paramXSDocumentInfo);
      throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[] { fName, str1 }, localElement1);
    }
    Element localElement2 = DOMUtil.getNextSiblingElement(localElement1);
    if (localElement2 != null)
    {
      fAttrChecker.returnAttrArray(arrayOfObject, paramXSDocumentInfo);
      localObject1 = DOMUtil.getLocalName(localElement2);
      throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[] { fName, localObject1 }, localElement2);
    }
    Object localObject1 = fAttrChecker.checkAttributes(localElement1, false, paramXSDocumentInfo);
    QName localQName = (QName)localObject1[XSAttributeChecker.ATTIDX_BASE];
    if (localQName == null)
    {
      fAttrChecker.returnAttrArray(arrayOfObject, paramXSDocumentInfo);
      fAttrChecker.returnAttrArray((Object[])localObject1, paramXSDocumentInfo);
      throw new ComplexTypeRecoverableError("s4s-att-must-appear", new Object[] { str1, "base" }, localElement1);
    }
    XSTypeDefinition localXSTypeDefinition = (XSTypeDefinition)fSchemaHandler.getGlobalDecl(paramXSDocumentInfo, 7, localQName, localElement1);
    if (localXSTypeDefinition == null)
    {
      fAttrChecker.returnAttrArray(arrayOfObject, paramXSDocumentInfo);
      fAttrChecker.returnAttrArray((Object[])localObject1, paramXSDocumentInfo);
      throw new ComplexTypeRecoverableError();
    }
    if (!(localXSTypeDefinition instanceof XSComplexTypeDecl))
    {
      fAttrChecker.returnAttrArray(arrayOfObject, paramXSDocumentInfo);
      fAttrChecker.returnAttrArray((Object[])localObject1, paramXSDocumentInfo);
      throw new ComplexTypeRecoverableError("src-ct.1", new Object[] { fName, localXSTypeDefinition.getName() }, localElement1);
    }
    XSComplexTypeDecl localXSComplexTypeDecl = (XSComplexTypeDecl)localXSTypeDefinition;
    fBaseType = localXSComplexTypeDecl;
    String str2;
    if ((localXSComplexTypeDecl.getFinal() & fDerivedBy) != 0)
    {
      fAttrChecker.returnAttrArray(arrayOfObject, paramXSDocumentInfo);
      fAttrChecker.returnAttrArray((Object[])localObject1, paramXSDocumentInfo);
      str2 = fDerivedBy == 1 ? "cos-ct-extends.1.1" : "derivation-ok-restriction.1";
      throw new ComplexTypeRecoverableError(str2, new Object[] { fName, fBaseType.getName() }, localElement1);
    }
    localElement1 = DOMUtil.getFirstChildElement(localElement1);
    if (localElement1 != null)
    {
      if (DOMUtil.getLocalName(localElement1).equals(SchemaSymbols.ELT_ANNOTATION))
      {
        addAnnotation(traverseAnnotationDecl(localElement1, (Object[])localObject1, false, paramXSDocumentInfo));
        localElement1 = DOMUtil.getNextSiblingElement(localElement1);
      }
      else
      {
        str2 = DOMUtil.getSyntheticAnnotation(localElement1);
        if (str2 != null) {
          addAnnotation(traverseSyntheticAnnotation(localElement1, str2, (Object[])localObject1, false, paramXSDocumentInfo));
        }
      }
      if ((localElement1 != null) && (DOMUtil.getLocalName(localElement1).equals(SchemaSymbols.ELT_ANNOTATION)))
      {
        fAttrChecker.returnAttrArray(arrayOfObject, paramXSDocumentInfo);
        fAttrChecker.returnAttrArray((Object[])localObject1, paramXSDocumentInfo);
        throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[] { fName, SchemaSymbols.ELT_ANNOTATION }, localElement1);
      }
    }
    else
    {
      str2 = DOMUtil.getSyntheticAnnotation(localElement1);
      if (str2 != null) {
        addAnnotation(traverseSyntheticAnnotation(localElement1, str2, (Object[])localObject1, false, paramXSDocumentInfo));
      }
    }
    try
    {
      processComplexContent(localElement1, bool, true, paramXSDocumentInfo, paramSchemaGrammar);
    }
    catch (ComplexTypeRecoverableError localComplexTypeRecoverableError1)
    {
      fAttrChecker.returnAttrArray(arrayOfObject, paramXSDocumentInfo);
      fAttrChecker.returnAttrArray((Object[])localObject1, paramXSDocumentInfo);
      throw localComplexTypeRecoverableError1;
    }
    XSParticleDecl localXSParticleDecl1 = (XSParticleDecl)localXSComplexTypeDecl.getParticle();
    Object localObject2;
    if (fDerivedBy == 2)
    {
      if ((fContentType == 3) && (localXSComplexTypeDecl.getContentType() != 3))
      {
        fAttrChecker.returnAttrArray(arrayOfObject, paramXSDocumentInfo);
        fAttrChecker.returnAttrArray((Object[])localObject1, paramXSDocumentInfo);
        throw new ComplexTypeRecoverableError("derivation-ok-restriction.5.4.1.2", new Object[] { fName, localXSComplexTypeDecl.getName() }, localElement1);
      }
      try
      {
        mergeAttributes(localXSComplexTypeDecl.getAttrGrp(), fAttrGrp, fName, false, localElement1);
      }
      catch (ComplexTypeRecoverableError localComplexTypeRecoverableError2)
      {
        fAttrChecker.returnAttrArray(arrayOfObject, paramXSDocumentInfo);
        fAttrChecker.returnAttrArray((Object[])localObject1, paramXSDocumentInfo);
        throw localComplexTypeRecoverableError2;
      }
      fAttrGrp.removeProhibitedAttrs();
      if (localXSComplexTypeDecl != SchemaGrammar.fAnyType)
      {
        localObject2 = fAttrGrp.validRestrictionOf(fName, localXSComplexTypeDecl.getAttrGrp());
        if (localObject2 != null)
        {
          fAttrChecker.returnAttrArray(arrayOfObject, paramXSDocumentInfo);
          fAttrChecker.returnAttrArray((Object[])localObject1, paramXSDocumentInfo);
          throw new ComplexTypeRecoverableError((String)localObject2[(localObject2.length - 1)], (Object[])localObject2, localElement1);
        }
      }
    }
    else
    {
      if (fParticle == null)
      {
        fContentType = localXSComplexTypeDecl.getContentType();
        fXSSimpleType = ((XSSimpleType)localXSComplexTypeDecl.getSimpleType());
        fParticle = localXSParticleDecl1;
      }
      else if (localXSComplexTypeDecl.getContentType() != 0)
      {
        if ((fContentType == 2) && (localXSComplexTypeDecl.getContentType() != 2))
        {
          fAttrChecker.returnAttrArray(arrayOfObject, paramXSDocumentInfo);
          fAttrChecker.returnAttrArray((Object[])localObject1, paramXSDocumentInfo);
          throw new ComplexTypeRecoverableError("cos-ct-extends.1.4.3.2.2.1.a", new Object[] { fName }, localElement1);
        }
        if ((fContentType == 3) && (localXSComplexTypeDecl.getContentType() != 3))
        {
          fAttrChecker.returnAttrArray(arrayOfObject, paramXSDocumentInfo);
          fAttrChecker.returnAttrArray((Object[])localObject1, paramXSDocumentInfo);
          throw new ComplexTypeRecoverableError("cos-ct-extends.1.4.3.2.2.1.b", new Object[] { fName }, localElement1);
        }
        if (((fParticle.fType == 3) && (fParticle.fValue).fCompositor == 103)) || ((getParticlefType == 3) && (getParticlefValue).fCompositor == 103)))
        {
          fAttrChecker.returnAttrArray(arrayOfObject, paramXSDocumentInfo);
          fAttrChecker.returnAttrArray((Object[])localObject1, paramXSDocumentInfo);
          throw new ComplexTypeRecoverableError("cos-all-limited.1.2", new Object[0], localElement1);
        }
        localObject2 = new XSModelGroupImpl();
        fCompositor = 102;
        fParticleCount = 2;
        fParticles = new XSParticleDecl[2];
        fParticles[0] = ((XSParticleDecl)localXSComplexTypeDecl.getParticle());
        fParticles[1] = fParticle;
        fAnnotations = XSObjectListImpl.EMPTY_LIST;
        XSParticleDecl localXSParticleDecl2 = new XSParticleDecl();
        fType = 3;
        fValue = ((XSTerm)localObject2);
        fAnnotations = XSObjectListImpl.EMPTY_LIST;
        fParticle = localXSParticleDecl2;
      }
      fAttrGrp.removeProhibitedAttrs();
      try
      {
        mergeAttributes(localXSComplexTypeDecl.getAttrGrp(), fAttrGrp, fName, true, localElement1);
      }
      catch (ComplexTypeRecoverableError localComplexTypeRecoverableError3)
      {
        fAttrChecker.returnAttrArray(arrayOfObject, paramXSDocumentInfo);
        fAttrChecker.returnAttrArray((Object[])localObject1, paramXSDocumentInfo);
        throw localComplexTypeRecoverableError3;
      }
    }
    fAttrChecker.returnAttrArray(arrayOfObject, paramXSDocumentInfo);
    fAttrChecker.returnAttrArray((Object[])localObject1, paramXSDocumentInfo);
  }
  
  private void mergeAttributes(XSAttributeGroupDecl paramXSAttributeGroupDecl1, XSAttributeGroupDecl paramXSAttributeGroupDecl2, String paramString, boolean paramBoolean, Element paramElement)
    throws XSDComplexTypeTraverser.ComplexTypeRecoverableError
  {
    XSObjectList localXSObjectList = paramXSAttributeGroupDecl1.getAttributeUses();
    XSAttributeUseImpl localXSAttributeUseImpl = null;
    int i = localXSObjectList.getLength();
    for (int j = 0; j < i; j++)
    {
      localXSAttributeUseImpl = (XSAttributeUseImpl)localXSObjectList.item(j);
      XSAttributeUse localXSAttributeUse = paramXSAttributeGroupDecl2.getAttributeUse(fAttrDecl.getNamespace(), fAttrDecl.getName());
      if (localXSAttributeUse == null)
      {
        String str = paramXSAttributeGroupDecl2.addAttributeUse(localXSAttributeUseImpl);
        if (str != null) {
          throw new ComplexTypeRecoverableError("ct-props-correct.5", new Object[] { paramString, str, fAttrDecl.getName() }, paramElement);
        }
      }
      else if ((localXSAttributeUse != localXSAttributeUseImpl) && (paramBoolean))
      {
        reportSchemaError("ct-props-correct.4", new Object[] { paramString, fAttrDecl.getName() }, paramElement);
        paramXSAttributeGroupDecl2.replaceAttributeUse(localXSAttributeUse, localXSAttributeUseImpl);
      }
    }
    if (paramBoolean) {
      if (fAttributeWC == null)
      {
        fAttributeWC = fAttributeWC;
      }
      else if (fAttributeWC != null)
      {
        fAttributeWC = fAttributeWC.performUnionWith(fAttributeWC, fAttributeWC.fProcessContents);
        if (fAttributeWC == null) {
          throw new ComplexTypeRecoverableError("src-ct.5", new Object[] { paramString }, paramElement);
        }
      }
    }
  }
  
  private void processComplexContent(Element paramElement, boolean paramBoolean1, boolean paramBoolean2, XSDocumentInfo paramXSDocumentInfo, SchemaGrammar paramSchemaGrammar)
    throws XSDComplexTypeTraverser.ComplexTypeRecoverableError
  {
    Element localElement = null;
    XSParticleDecl localXSParticleDecl = null;
    int i = 0;
    Object localObject;
    if (paramElement != null)
    {
      localObject = DOMUtil.getLocalName(paramElement);
      if (((String)localObject).equals(SchemaSymbols.ELT_GROUP))
      {
        localXSParticleDecl = fSchemaHandler.fGroupTraverser.traverseLocal(paramElement, paramXSDocumentInfo, paramSchemaGrammar);
        localElement = DOMUtil.getNextSiblingElement(paramElement);
      }
      else
      {
        XSModelGroupImpl localXSModelGroupImpl;
        if (((String)localObject).equals(SchemaSymbols.ELT_SEQUENCE))
        {
          localXSParticleDecl = traverseSequence(paramElement, paramXSDocumentInfo, paramSchemaGrammar, 0, fComplexTypeDecl);
          if (localXSParticleDecl != null)
          {
            localXSModelGroupImpl = (XSModelGroupImpl)fValue;
            if (fParticleCount == 0) {
              i = 1;
            }
          }
          localElement = DOMUtil.getNextSiblingElement(paramElement);
        }
        else if (((String)localObject).equals(SchemaSymbols.ELT_CHOICE))
        {
          localXSParticleDecl = traverseChoice(paramElement, paramXSDocumentInfo, paramSchemaGrammar, 0, fComplexTypeDecl);
          if ((localXSParticleDecl != null) && (fMinOccurs == 0))
          {
            localXSModelGroupImpl = (XSModelGroupImpl)fValue;
            if (fParticleCount == 0) {
              i = 1;
            }
          }
          localElement = DOMUtil.getNextSiblingElement(paramElement);
        }
        else if (((String)localObject).equals(SchemaSymbols.ELT_ALL))
        {
          localXSParticleDecl = traverseAll(paramElement, paramXSDocumentInfo, paramSchemaGrammar, 8, fComplexTypeDecl);
          if (localXSParticleDecl != null)
          {
            localXSModelGroupImpl = (XSModelGroupImpl)fValue;
            if (fParticleCount == 0) {
              i = 1;
            }
          }
          localElement = DOMUtil.getNextSiblingElement(paramElement);
        }
        else
        {
          localElement = paramElement;
        }
      }
    }
    if (i != 0)
    {
      localObject = DOMUtil.getFirstChildElement(paramElement);
      if ((localObject != null) && (DOMUtil.getLocalName((Node)localObject).equals(SchemaSymbols.ELT_ANNOTATION))) {
        localObject = DOMUtil.getNextSiblingElement((Node)localObject);
      }
      if (localObject == null) {
        localXSParticleDecl = null;
      }
    }
    if ((localXSParticleDecl == null) && (paramBoolean1)) {
      localXSParticleDecl = XSConstraints.getEmptySequence();
    }
    fParticle = localXSParticleDecl;
    if (fParticle == null) {
      fContentType = 0;
    } else if (paramBoolean1) {
      fContentType = 3;
    } else {
      fContentType = 2;
    }
    if (localElement != null)
    {
      if (!isAttrOrAttrGroup(localElement)) {
        throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[] { fName, DOMUtil.getLocalName(localElement) }, localElement);
      }
      localObject = traverseAttrsAndAttrGrps(localElement, fAttrGrp, paramXSDocumentInfo, paramSchemaGrammar, fComplexTypeDecl);
      if (localObject != null) {
        throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[] { fName, DOMUtil.getLocalName((Node)localObject) }, (Element)localObject);
      }
      if (!paramBoolean2) {
        fAttrGrp.removeProhibitedAttrs();
      }
    }
  }
  
  private boolean isAttrOrAttrGroup(Element paramElement)
  {
    String str = DOMUtil.getLocalName(paramElement);
    return (str.equals(SchemaSymbols.ELT_ATTRIBUTE)) || (str.equals(SchemaSymbols.ELT_ATTRIBUTEGROUP)) || (str.equals(SchemaSymbols.ELT_ANYATTRIBUTE));
  }
  
  private void traverseSimpleContentDecl(Element paramElement) {}
  
  private void traverseComplexContentDecl(Element paramElement, boolean paramBoolean) {}
  
  private String genAnonTypeName(Element paramElement)
  {
    StringBuffer localStringBuffer = new StringBuffer("#AnonType_");
    for (Element localElement = DOMUtil.getParent(paramElement); (localElement != null) && (localElement != DOMUtil.getRoot(DOMUtil.getDocument(localElement))); localElement = DOMUtil.getParent(localElement)) {
      localStringBuffer.append(localElement.getAttribute(SchemaSymbols.ATT_NAME));
    }
    return localStringBuffer.toString();
  }
  
  private void handleComplexTypeError(String paramString, Object[] paramArrayOfObject, Element paramElement)
  {
    if (paramString != null) {
      reportSchemaError(paramString, paramArrayOfObject, paramElement);
    }
    fBaseType = SchemaGrammar.fAnyType;
    fContentType = 3;
    fXSSimpleType = null;
    fParticle = getErrorContent();
    fAttrGrp.fAttributeWC = getErrorWildcard();
  }
  
  private void contentBackup()
  {
    if (fGlobalStore == null)
    {
      fGlobalStore = new Object[11];
      fGlobalStorePos = 0;
    }
    if (fGlobalStorePos == fGlobalStore.length)
    {
      Object[] arrayOfObject = new Object[fGlobalStorePos + 11];
      System.arraycopy(fGlobalStore, 0, arrayOfObject, 0, fGlobalStorePos);
      fGlobalStore = arrayOfObject;
    }
    fGlobalStore[(fGlobalStorePos++)] = fComplexTypeDecl;
    fGlobalStore[(fGlobalStorePos++)] = (fIsAbstract ? Boolean.TRUE : Boolean.FALSE);
    fGlobalStore[(fGlobalStorePos++)] = fName;
    fGlobalStore[(fGlobalStorePos++)] = fTargetNamespace;
    fGlobalStore[(fGlobalStorePos++)] = new Integer((fDerivedBy << 16) + fFinal);
    fGlobalStore[(fGlobalStorePos++)] = new Integer((fBlock << 16) + fContentType);
    fGlobalStore[(fGlobalStorePos++)] = fBaseType;
    fGlobalStore[(fGlobalStorePos++)] = fAttrGrp;
    fGlobalStore[(fGlobalStorePos++)] = fParticle;
    fGlobalStore[(fGlobalStorePos++)] = fXSSimpleType;
    fGlobalStore[(fGlobalStorePos++)] = fAnnotations;
  }
  
  private void contentRestore()
  {
    fAnnotations = ((XSAnnotationImpl[])fGlobalStore[(--fGlobalStorePos)]);
    fXSSimpleType = ((XSSimpleType)fGlobalStore[(--fGlobalStorePos)]);
    fParticle = ((XSParticleDecl)fGlobalStore[(--fGlobalStorePos)]);
    fAttrGrp = ((XSAttributeGroupDecl)fGlobalStore[(--fGlobalStorePos)]);
    fBaseType = ((XSTypeDefinition)fGlobalStore[(--fGlobalStorePos)]);
    int i = ((Integer)fGlobalStore[(--fGlobalStorePos)]).intValue();
    fBlock = ((short)(i >> 16));
    fContentType = ((short)i);
    i = ((Integer)fGlobalStore[(--fGlobalStorePos)]).intValue();
    fDerivedBy = ((short)(i >> 16));
    fFinal = ((short)i);
    fTargetNamespace = ((String)fGlobalStore[(--fGlobalStorePos)]);
    fName = ((String)fGlobalStore[(--fGlobalStorePos)]);
    fIsAbstract = ((Boolean)fGlobalStore[(--fGlobalStorePos)]).booleanValue();
    fComplexTypeDecl = ((XSComplexTypeDecl)fGlobalStore[(--fGlobalStorePos)]);
  }
  
  private void addAnnotation(XSAnnotationImpl paramXSAnnotationImpl)
  {
    if (paramXSAnnotationImpl == null) {
      return;
    }
    if (fAnnotations == null)
    {
      fAnnotations = new XSAnnotationImpl[1];
    }
    else
    {
      XSAnnotationImpl[] arrayOfXSAnnotationImpl = new XSAnnotationImpl[fAnnotations.length + 1];
      System.arraycopy(fAnnotations, 0, arrayOfXSAnnotationImpl, 0, fAnnotations.length);
      fAnnotations = arrayOfXSAnnotationImpl;
    }
    fAnnotations[(fAnnotations.length - 1)] = paramXSAnnotationImpl;
  }
  
  private static final class ComplexTypeRecoverableError
    extends Exception
  {
    private static final long serialVersionUID = 6802729912091130335L;
    Object[] errorSubstText = null;
    Element errorElem = null;
    
    ComplexTypeRecoverableError() {}
    
    ComplexTypeRecoverableError(String paramString, Object[] paramArrayOfObject, Element paramElement)
    {
      super();
      errorSubstText = paramArrayOfObject;
      errorElem = paramElement;
    }
  }
}
