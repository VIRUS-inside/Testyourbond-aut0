package org.apache.xerces.impl.xs.traversers;

import org.apache.xerces.impl.xs.SchemaGrammar;
import org.apache.xerces.impl.xs.SchemaSymbols;
import org.apache.xerces.impl.xs.XSAnnotationImpl;
import org.apache.xerces.impl.xs.XSNotationDecl;
import org.apache.xerces.impl.xs.util.XSObjectListImpl;
import org.apache.xerces.util.DOMUtil;
import org.apache.xerces.xs.XSObjectList;
import org.w3c.dom.Element;

class XSDNotationTraverser
  extends XSDAbstractTraverser
{
  XSDNotationTraverser(XSDHandler paramXSDHandler, XSAttributeChecker paramXSAttributeChecker)
  {
    super(paramXSDHandler, paramXSAttributeChecker);
  }
  
  XSNotationDecl traverse(Element paramElement, XSDocumentInfo paramXSDocumentInfo, SchemaGrammar paramSchemaGrammar)
  {
    Object[] arrayOfObject = fAttrChecker.checkAttributes(paramElement, true, paramXSDocumentInfo);
    String str1 = (String)arrayOfObject[XSAttributeChecker.ATTIDX_NAME];
    String str2 = (String)arrayOfObject[XSAttributeChecker.ATTIDX_PUBLIC];
    String str3 = (String)arrayOfObject[XSAttributeChecker.ATTIDX_SYSTEM];
    if (str1 == null)
    {
      reportSchemaError("s4s-att-must-appear", new Object[] { SchemaSymbols.ELT_NOTATION, SchemaSymbols.ATT_NAME }, paramElement);
      fAttrChecker.returnAttrArray(arrayOfObject, paramXSDocumentInfo);
      return null;
    }
    if ((str3 == null) && (str2 == null))
    {
      reportSchemaError("PublicSystemOnNotation", null, paramElement);
      str2 = "missing";
    }
    Object localObject1 = new XSNotationDecl();
    fName = str1;
    fTargetNamespace = fTargetNamespace;
    fPublicId = str2;
    fSystemId = str3;
    Element localElement = DOMUtil.getFirstChildElement(paramElement);
    XSAnnotationImpl localXSAnnotationImpl = null;
    Object localObject2;
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
    if (localElement != null)
    {
      localObject3 = new Object[] { SchemaSymbols.ELT_NOTATION, "(annotation?)", DOMUtil.getLocalName(localElement) };
      reportSchemaError("s4s-elt-must-match.1", (Object[])localObject3, localElement);
    }
    if (paramSchemaGrammar.getGlobalNotationDecl(fName) == null) {
      paramSchemaGrammar.addGlobalNotationDecl((XSNotationDecl)localObject1);
    }
    Object localObject3 = fSchemaHandler.schemaDocument2SystemId(paramXSDocumentInfo);
    XSNotationDecl localXSNotationDecl = paramSchemaGrammar.getGlobalNotationDecl(fName, (String)localObject3);
    if (localXSNotationDecl == null) {
      paramSchemaGrammar.addGlobalNotationDecl((XSNotationDecl)localObject1, (String)localObject3);
    }
    if (fSchemaHandler.fTolerateDuplicates)
    {
      if (localXSNotationDecl != null) {
        localObject1 = localXSNotationDecl;
      }
      fSchemaHandler.addGlobalNotationDecl((XSNotationDecl)localObject1);
    }
    fAttrChecker.returnAttrArray(arrayOfObject, paramXSDocumentInfo);
    return localObject1;
  }
}
