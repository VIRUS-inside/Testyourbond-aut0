package org.apache.xerces.impl.xs.traversers;

import org.apache.xerces.impl.xs.SchemaGrammar;
import org.apache.xerces.impl.xs.SchemaSymbols;
import org.apache.xerces.impl.xs.XSElementDecl;
import org.apache.xerces.impl.xs.identity.IdentityConstraint;
import org.apache.xerces.impl.xs.identity.KeyRef;
import org.apache.xerces.impl.xs.identity.UniqueOrKey;
import org.apache.xerces.xni.QName;
import org.w3c.dom.Element;

class XSDKeyrefTraverser
  extends XSDAbstractIDConstraintTraverser
{
  public XSDKeyrefTraverser(XSDHandler paramXSDHandler, XSAttributeChecker paramXSAttributeChecker)
  {
    super(paramXSDHandler, paramXSAttributeChecker);
  }
  
  void traverse(Element paramElement, XSElementDecl paramXSElementDecl, XSDocumentInfo paramXSDocumentInfo, SchemaGrammar paramSchemaGrammar)
  {
    Object[] arrayOfObject = fAttrChecker.checkAttributes(paramElement, false, paramXSDocumentInfo);
    String str1 = (String)arrayOfObject[XSAttributeChecker.ATTIDX_NAME];
    if (str1 == null)
    {
      reportSchemaError("s4s-att-must-appear", new Object[] { SchemaSymbols.ELT_KEYREF, SchemaSymbols.ATT_NAME }, paramElement);
      fAttrChecker.returnAttrArray(arrayOfObject, paramXSDocumentInfo);
      return;
    }
    QName localQName = (QName)arrayOfObject[XSAttributeChecker.ATTIDX_REFER];
    if (localQName == null)
    {
      reportSchemaError("s4s-att-must-appear", new Object[] { SchemaSymbols.ELT_KEYREF, SchemaSymbols.ATT_REFER }, paramElement);
      fAttrChecker.returnAttrArray(arrayOfObject, paramXSDocumentInfo);
      return;
    }
    UniqueOrKey localUniqueOrKey = null;
    IdentityConstraint localIdentityConstraint1 = (IdentityConstraint)fSchemaHandler.getGlobalDecl(paramXSDocumentInfo, 5, localQName, paramElement);
    if (localIdentityConstraint1 != null) {
      if ((localIdentityConstraint1.getCategory() == 1) || (localIdentityConstraint1.getCategory() == 3)) {
        localUniqueOrKey = (UniqueOrKey)localIdentityConstraint1;
      } else {
        reportSchemaError("src-resolve", new Object[] { rawname, "identity constraint key/unique" }, paramElement);
      }
    }
    if (localUniqueOrKey == null)
    {
      fAttrChecker.returnAttrArray(arrayOfObject, paramXSDocumentInfo);
      return;
    }
    KeyRef localKeyRef = new KeyRef(fTargetNamespace, str1, fName, localUniqueOrKey);
    if (traverseIdentityConstraint(localKeyRef, paramElement, paramXSDocumentInfo, arrayOfObject)) {
      if (localUniqueOrKey.getFieldCount() != localKeyRef.getFieldCount())
      {
        reportSchemaError("c-props-correct.2", new Object[] { str1, localUniqueOrKey.getIdentityConstraintName() }, paramElement);
      }
      else
      {
        if (paramSchemaGrammar.getIDConstraintDecl(localKeyRef.getIdentityConstraintName()) == null) {
          paramSchemaGrammar.addIDConstraintDecl(paramXSElementDecl, localKeyRef);
        }
        String str2 = fSchemaHandler.schemaDocument2SystemId(paramXSDocumentInfo);
        IdentityConstraint localIdentityConstraint2 = paramSchemaGrammar.getIDConstraintDecl(localKeyRef.getIdentityConstraintName(), str2);
        if (localIdentityConstraint2 == null) {
          paramSchemaGrammar.addIDConstraintDecl(paramXSElementDecl, localKeyRef, str2);
        }
        if (fSchemaHandler.fTolerateDuplicates)
        {
          if ((localIdentityConstraint2 != null) && ((localIdentityConstraint2 instanceof KeyRef))) {
            localKeyRef = (KeyRef)localIdentityConstraint2;
          }
          fSchemaHandler.addIDConstraintDecl(localKeyRef);
        }
      }
    }
    fAttrChecker.returnAttrArray(arrayOfObject, paramXSDocumentInfo);
  }
}
