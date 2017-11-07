package org.apache.xerces.impl.xs.traversers;

import org.apache.xerces.impl.xs.SchemaGrammar;
import org.apache.xerces.impl.xs.SchemaSymbols;
import org.apache.xerces.impl.xs.XSElementDecl;
import org.apache.xerces.impl.xs.identity.IdentityConstraint;
import org.apache.xerces.impl.xs.identity.UniqueOrKey;
import org.apache.xerces.util.DOMUtil;
import org.w3c.dom.Element;

class XSDUniqueOrKeyTraverser
  extends XSDAbstractIDConstraintTraverser
{
  public XSDUniqueOrKeyTraverser(XSDHandler paramXSDHandler, XSAttributeChecker paramXSAttributeChecker)
  {
    super(paramXSDHandler, paramXSAttributeChecker);
  }
  
  void traverse(Element paramElement, XSElementDecl paramXSElementDecl, XSDocumentInfo paramXSDocumentInfo, SchemaGrammar paramSchemaGrammar)
  {
    Object[] arrayOfObject = fAttrChecker.checkAttributes(paramElement, false, paramXSDocumentInfo);
    String str1 = (String)arrayOfObject[XSAttributeChecker.ATTIDX_NAME];
    if (str1 == null)
    {
      reportSchemaError("s4s-att-must-appear", new Object[] { DOMUtil.getLocalName(paramElement), SchemaSymbols.ATT_NAME }, paramElement);
      fAttrChecker.returnAttrArray(arrayOfObject, paramXSDocumentInfo);
      return;
    }
    UniqueOrKey localUniqueOrKey = null;
    if (DOMUtil.getLocalName(paramElement).equals(SchemaSymbols.ELT_UNIQUE)) {
      localUniqueOrKey = new UniqueOrKey(fTargetNamespace, str1, fName, (short)3);
    } else {
      localUniqueOrKey = new UniqueOrKey(fTargetNamespace, str1, fName, (short)1);
    }
    if (traverseIdentityConstraint(localUniqueOrKey, paramElement, paramXSDocumentInfo, arrayOfObject))
    {
      if (paramSchemaGrammar.getIDConstraintDecl(localUniqueOrKey.getIdentityConstraintName()) == null) {
        paramSchemaGrammar.addIDConstraintDecl(paramXSElementDecl, localUniqueOrKey);
      }
      String str2 = fSchemaHandler.schemaDocument2SystemId(paramXSDocumentInfo);
      IdentityConstraint localIdentityConstraint = paramSchemaGrammar.getIDConstraintDecl(localUniqueOrKey.getIdentityConstraintName(), str2);
      if (localIdentityConstraint == null) {
        paramSchemaGrammar.addIDConstraintDecl(paramXSElementDecl, localUniqueOrKey, str2);
      }
      if (fSchemaHandler.fTolerateDuplicates)
      {
        if ((localIdentityConstraint != null) && ((localIdentityConstraint instanceof UniqueOrKey))) {
          localUniqueOrKey = localUniqueOrKey;
        }
        fSchemaHandler.addIDConstraintDecl(localUniqueOrKey);
      }
    }
    fAttrChecker.returnAttrArray(arrayOfObject, paramXSDocumentInfo);
  }
}
