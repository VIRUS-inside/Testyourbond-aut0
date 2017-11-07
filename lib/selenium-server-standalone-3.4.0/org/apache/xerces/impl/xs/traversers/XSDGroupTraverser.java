package org.apache.xerces.impl.xs.traversers;

import org.apache.xerces.impl.xs.SchemaGrammar;
import org.apache.xerces.impl.xs.SchemaSymbols;
import org.apache.xerces.impl.xs.XSAnnotationImpl;
import org.apache.xerces.impl.xs.XSConstraints;
import org.apache.xerces.impl.xs.XSDeclarationPool;
import org.apache.xerces.impl.xs.XSGroupDecl;
import org.apache.xerces.impl.xs.XSModelGroupImpl;
import org.apache.xerces.impl.xs.XSParticleDecl;
import org.apache.xerces.impl.xs.util.XInt;
import org.apache.xerces.impl.xs.util.XSObjectListImpl;
import org.apache.xerces.util.DOMUtil;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xs.XSObject;
import org.apache.xerces.xs.XSObjectList;
import org.w3c.dom.Element;

class XSDGroupTraverser
  extends XSDAbstractParticleTraverser
{
  XSDGroupTraverser(XSDHandler paramXSDHandler, XSAttributeChecker paramXSAttributeChecker)
  {
    super(paramXSDHandler, paramXSAttributeChecker);
  }
  
  XSParticleDecl traverseLocal(Element paramElement, XSDocumentInfo paramXSDocumentInfo, SchemaGrammar paramSchemaGrammar)
  {
    Object[] arrayOfObject = fAttrChecker.checkAttributes(paramElement, false, paramXSDocumentInfo);
    QName localQName = (QName)arrayOfObject[XSAttributeChecker.ATTIDX_REF];
    XInt localXInt1 = (XInt)arrayOfObject[XSAttributeChecker.ATTIDX_MINOCCURS];
    XInt localXInt2 = (XInt)arrayOfObject[XSAttributeChecker.ATTIDX_MAXOCCURS];
    XSGroupDecl localXSGroupDecl = null;
    if (localQName == null) {
      reportSchemaError("s4s-att-must-appear", new Object[] { "group (local)", "ref" }, paramElement);
    } else {
      localXSGroupDecl = (XSGroupDecl)fSchemaHandler.getGlobalDecl(paramXSDocumentInfo, 4, localQName, paramElement);
    }
    XSAnnotationImpl localXSAnnotationImpl = null;
    Element localElement = DOMUtil.getFirstChildElement(paramElement);
    if ((localElement != null) && (DOMUtil.getLocalName(localElement).equals(SchemaSymbols.ELT_ANNOTATION)))
    {
      localXSAnnotationImpl = traverseAnnotationDecl(localElement, arrayOfObject, false, paramXSDocumentInfo);
      localElement = DOMUtil.getNextSiblingElement(localElement);
    }
    else
    {
      String str = DOMUtil.getSyntheticAnnotation(paramElement);
      if (str != null) {
        localXSAnnotationImpl = traverseSyntheticAnnotation(paramElement, str, arrayOfObject, false, paramXSDocumentInfo);
      }
    }
    if (localElement != null) {
      reportSchemaError("s4s-elt-must-match.1", new Object[] { "group (local)", "(annotation?)", DOMUtil.getLocalName(paramElement) }, paramElement);
    }
    int i = localXInt1.intValue();
    int j = localXInt2.intValue();
    XSParticleDecl localXSParticleDecl = null;
    if ((localXSGroupDecl != null) && (fModelGroup != null) && ((i != 0) || (j != 0)))
    {
      if (fSchemaHandler.fDeclPool != null) {
        localXSParticleDecl = fSchemaHandler.fDeclPool.getParticleDecl();
      } else {
        localXSParticleDecl = new XSParticleDecl();
      }
      fType = 3;
      fValue = fModelGroup;
      fMinOccurs = i;
      fMaxOccurs = j;
      Object localObject;
      if (fModelGroup.fCompositor == 103)
      {
        localObject = (Long)arrayOfObject[XSAttributeChecker.ATTIDX_FROMDEFAULT];
        localXSParticleDecl = checkOccurrences(localXSParticleDecl, SchemaSymbols.ELT_GROUP, (Element)paramElement.getParentNode(), 2, ((Long)localObject).longValue());
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
        fAnnotations = fAnnotations;
      }
    }
    fAttrChecker.returnAttrArray(arrayOfObject, paramXSDocumentInfo);
    return localXSParticleDecl;
  }
  
  XSGroupDecl traverseGlobal(Element paramElement, XSDocumentInfo paramXSDocumentInfo, SchemaGrammar paramSchemaGrammar)
  {
    Object[] arrayOfObject = fAttrChecker.checkAttributes(paramElement, true, paramXSDocumentInfo);
    String str1 = (String)arrayOfObject[XSAttributeChecker.ATTIDX_NAME];
    if (str1 == null) {
      reportSchemaError("s4s-att-must-appear", new Object[] { "group (global)", "name" }, paramElement);
    }
    Object localObject1 = new XSGroupDecl();
    XSParticleDecl localXSParticleDecl = null;
    Element localElement = DOMUtil.getFirstChildElement(paramElement);
    XSAnnotationImpl localXSAnnotationImpl = null;
    Object localObject2;
    String str2;
    if (localElement == null)
    {
      reportSchemaError("s4s-elt-must-match.2", new Object[] { "group (global)", "(annotation?, (all | choice | sequence))" }, paramElement);
    }
    else
    {
      localObject2 = localElement.getLocalName();
      if (((String)localObject2).equals(SchemaSymbols.ELT_ANNOTATION))
      {
        localXSAnnotationImpl = traverseAnnotationDecl(localElement, arrayOfObject, true, paramXSDocumentInfo);
        localElement = DOMUtil.getNextSiblingElement(localElement);
        if (localElement != null) {
          localObject2 = localElement.getLocalName();
        }
      }
      else
      {
        str2 = DOMUtil.getSyntheticAnnotation(paramElement);
        if (str2 != null) {
          localXSAnnotationImpl = traverseSyntheticAnnotation(paramElement, str2, arrayOfObject, false, paramXSDocumentInfo);
        }
      }
      if (localElement == null) {
        reportSchemaError("s4s-elt-must-match.2", new Object[] { "group (global)", "(annotation?, (all | choice | sequence))" }, paramElement);
      } else if (((String)localObject2).equals(SchemaSymbols.ELT_ALL)) {
        localXSParticleDecl = traverseAll(localElement, paramXSDocumentInfo, paramSchemaGrammar, 4, (XSObject)localObject1);
      } else if (((String)localObject2).equals(SchemaSymbols.ELT_CHOICE)) {
        localXSParticleDecl = traverseChoice(localElement, paramXSDocumentInfo, paramSchemaGrammar, 4, (XSObject)localObject1);
      } else if (((String)localObject2).equals(SchemaSymbols.ELT_SEQUENCE)) {
        localXSParticleDecl = traverseSequence(localElement, paramXSDocumentInfo, paramSchemaGrammar, 4, (XSObject)localObject1);
      } else {
        reportSchemaError("s4s-elt-must-match.1", new Object[] { "group (global)", "(annotation?, (all | choice | sequence))", DOMUtil.getLocalName(localElement) }, localElement);
      }
      if ((localElement != null) && (DOMUtil.getNextSiblingElement(localElement) != null)) {
        reportSchemaError("s4s-elt-must-match.1", new Object[] { "group (global)", "(annotation?, (all | choice | sequence))", DOMUtil.getLocalName(DOMUtil.getNextSiblingElement(localElement)) }, DOMUtil.getNextSiblingElement(localElement));
      }
    }
    if (str1 != null)
    {
      fName = str1;
      fTargetNamespace = fTargetNamespace;
      if (localXSParticleDecl == null) {
        localXSParticleDecl = XSConstraints.getEmptySequence();
      }
      fModelGroup = ((XSModelGroupImpl)fValue);
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
      if (paramSchemaGrammar.getGlobalGroupDecl(fName) == null) {
        paramSchemaGrammar.addGlobalGroupDecl((XSGroupDecl)localObject1);
      }
      str2 = fSchemaHandler.schemaDocument2SystemId(paramXSDocumentInfo);
      XSGroupDecl localXSGroupDecl = paramSchemaGrammar.getGlobalGroupDecl(fName, str2);
      if (localXSGroupDecl == null) {
        paramSchemaGrammar.addGlobalGroupDecl((XSGroupDecl)localObject1, str2);
      }
      if (fSchemaHandler.fTolerateDuplicates)
      {
        if (localXSGroupDecl != null) {
          localObject1 = localXSGroupDecl;
        }
        fSchemaHandler.addGlobalGroupDecl((XSGroupDecl)localObject1);
      }
    }
    else
    {
      localObject1 = null;
    }
    if (localObject1 != null)
    {
      localObject2 = fSchemaHandler.getGrpOrAttrGrpRedefinedByRestriction(4, new QName(XMLSymbols.EMPTY_STRING, str1, str1, fTargetNamespace), paramXSDocumentInfo, paramElement);
      if (localObject2 != null) {
        paramSchemaGrammar.addRedefinedGroupDecl((XSGroupDecl)localObject1, (XSGroupDecl)localObject2, fSchemaHandler.element2Locator(paramElement));
      }
    }
    fAttrChecker.returnAttrArray(arrayOfObject, paramXSDocumentInfo);
    return localObject1;
  }
}
