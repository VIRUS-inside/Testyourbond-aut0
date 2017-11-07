package org.apache.xerces.impl.xs.traversers;

import org.apache.xerces.impl.xs.SchemaGrammar;
import org.apache.xerces.impl.xs.SchemaSymbols;
import org.apache.xerces.impl.xs.XSAnnotationImpl;
import org.apache.xerces.impl.xs.XSAttributeGroupDecl;
import org.apache.xerces.impl.xs.util.XSObjectListImpl;
import org.apache.xerces.util.DOMUtil;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xs.XSObjectList;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

class XSDAttributeGroupTraverser
  extends XSDAbstractTraverser
{
  XSDAttributeGroupTraverser(XSDHandler paramXSDHandler, XSAttributeChecker paramXSAttributeChecker)
  {
    super(paramXSDHandler, paramXSAttributeChecker);
  }
  
  XSAttributeGroupDecl traverseLocal(Element paramElement, XSDocumentInfo paramXSDocumentInfo, SchemaGrammar paramSchemaGrammar)
  {
    Object[] arrayOfObject = fAttrChecker.checkAttributes(paramElement, false, paramXSDocumentInfo);
    QName localQName = (QName)arrayOfObject[XSAttributeChecker.ATTIDX_REF];
    XSAttributeGroupDecl localXSAttributeGroupDecl = null;
    if (localQName == null)
    {
      reportSchemaError("s4s-att-must-appear", new Object[] { "attributeGroup (local)", "ref" }, paramElement);
      fAttrChecker.returnAttrArray(arrayOfObject, paramXSDocumentInfo);
      return null;
    }
    localXSAttributeGroupDecl = (XSAttributeGroupDecl)fSchemaHandler.getGlobalDecl(paramXSDocumentInfo, 2, localQName, paramElement);
    Element localElement = DOMUtil.getFirstChildElement(paramElement);
    if (localElement != null)
    {
      String str = DOMUtil.getLocalName(localElement);
      Object localObject;
      if (str.equals(SchemaSymbols.ELT_ANNOTATION))
      {
        traverseAnnotationDecl(localElement, arrayOfObject, false, paramXSDocumentInfo);
        localElement = DOMUtil.getNextSiblingElement(localElement);
      }
      else
      {
        localObject = DOMUtil.getSyntheticAnnotation(localElement);
        if (localObject != null) {
          traverseSyntheticAnnotation(localElement, (String)localObject, arrayOfObject, false, paramXSDocumentInfo);
        }
      }
      if (localElement != null)
      {
        localObject = new Object[] { rawname, "(annotation?)", DOMUtil.getLocalName(localElement) };
        reportSchemaError("s4s-elt-must-match.1", (Object[])localObject, localElement);
      }
    }
    fAttrChecker.returnAttrArray(arrayOfObject, paramXSDocumentInfo);
    return localXSAttributeGroupDecl;
  }
  
  XSAttributeGroupDecl traverseGlobal(Element paramElement, XSDocumentInfo paramXSDocumentInfo, SchemaGrammar paramSchemaGrammar)
  {
    Object localObject1 = new XSAttributeGroupDecl();
    Object[] arrayOfObject = fAttrChecker.checkAttributes(paramElement, true, paramXSDocumentInfo);
    String str1 = (String)arrayOfObject[XSAttributeChecker.ATTIDX_NAME];
    if (str1 == null)
    {
      reportSchemaError("s4s-att-must-appear", new Object[] { "attributeGroup (global)", "name" }, paramElement);
      str1 = "(no name)";
    }
    fName = str1;
    fTargetNamespace = fTargetNamespace;
    Element localElement = DOMUtil.getFirstChildElement(paramElement);
    XSAnnotationImpl localXSAnnotationImpl = null;
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
    Object localObject2 = traverseAttrsAndAttrGrps(localElement, (XSAttributeGroupDecl)localObject1, paramXSDocumentInfo, paramSchemaGrammar, null);
    if (localObject2 != null)
    {
      localObject3 = new Object[] { str1, "(annotation?, ((attribute | attributeGroup)*, anyAttribute?))", DOMUtil.getLocalName((Node)localObject2) };
      reportSchemaError("s4s-elt-must-match.1", (Object[])localObject3, (Element)localObject2);
    }
    if (str1.equals("(no name)"))
    {
      fAttrChecker.returnAttrArray(arrayOfObject, paramXSDocumentInfo);
      return null;
    }
    ((XSAttributeGroupDecl)localObject1).removeProhibitedAttrs();
    Object localObject3 = (XSAttributeGroupDecl)fSchemaHandler.getGrpOrAttrGrpRedefinedByRestriction(2, new QName(XMLSymbols.EMPTY_STRING, str1, str1, fTargetNamespace), paramXSDocumentInfo, paramElement);
    Object localObject4;
    if (localObject3 != null)
    {
      localObject4 = ((XSAttributeGroupDecl)localObject1).validRestrictionOf(str1, (XSAttributeGroupDecl)localObject3);
      if (localObject4 != null)
      {
        reportSchemaError((String)localObject4[(localObject4.length - 1)], (Object[])localObject4, localElement);
        reportSchemaError("src-redefine.7.2.2", new Object[] { str1, localObject4[(localObject4.length - 1)] }, localElement);
      }
    }
    if (localXSAnnotationImpl != null)
    {
      localObject4 = new XSObjectListImpl();
      ((XSObjectListImpl)localObject4).addXSObject(localXSAnnotationImpl);
    }
    else
    {
      localObject4 = XSObjectListImpl.EMPTY_LIST;
    }
    fAnnotations = ((XSObjectList)localObject4);
    if (paramSchemaGrammar.getGlobalAttributeGroupDecl(fName) == null) {
      paramSchemaGrammar.addGlobalAttributeGroupDecl((XSAttributeGroupDecl)localObject1);
    }
    String str2 = fSchemaHandler.schemaDocument2SystemId(paramXSDocumentInfo);
    XSAttributeGroupDecl localXSAttributeGroupDecl = paramSchemaGrammar.getGlobalAttributeGroupDecl(fName, str2);
    if (localXSAttributeGroupDecl == null) {
      paramSchemaGrammar.addGlobalAttributeGroupDecl((XSAttributeGroupDecl)localObject1, str2);
    }
    if (fSchemaHandler.fTolerateDuplicates)
    {
      if (localXSAttributeGroupDecl != null) {
        localObject1 = localXSAttributeGroupDecl;
      }
      fSchemaHandler.addGlobalAttributeGroupDecl((XSAttributeGroupDecl)localObject1);
    }
    fAttrChecker.returnAttrArray(arrayOfObject, paramXSDocumentInfo);
    return localObject1;
  }
}
