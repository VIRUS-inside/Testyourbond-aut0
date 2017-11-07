package org.apache.xerces.impl.xs.traversers;

import java.util.Stack;
import java.util.Vector;
import org.apache.xerces.impl.validation.ValidationState;
import org.apache.xerces.impl.xs.SchemaNamespaceSupport;
import org.apache.xerces.impl.xs.XMLSchemaException;
import org.apache.xerces.impl.xs.opti.SchemaDOM;
import org.apache.xerces.impl.xs.util.XInt;
import org.apache.xerces.util.SymbolTable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

class XSDocumentInfo
{
  protected SchemaNamespaceSupport fNamespaceSupport;
  protected SchemaNamespaceSupport fNamespaceSupportRoot;
  protected Stack SchemaNamespaceSupportStack = new Stack();
  protected boolean fAreLocalAttributesQualified;
  protected boolean fAreLocalElementsQualified;
  protected short fBlockDefault;
  protected short fFinalDefault;
  String fTargetNamespace;
  protected boolean fIsChameleonSchema;
  protected Element fSchemaElement;
  Vector fImportedNS = new Vector();
  protected ValidationState fValidationContext = new ValidationState();
  SymbolTable fSymbolTable = null;
  protected XSAttributeChecker fAttrChecker;
  protected Object[] fSchemaAttrs;
  protected XSAnnotationInfo fAnnotations = null;
  private Vector fReportedTNS = null;
  
  XSDocumentInfo(Element paramElement, XSAttributeChecker paramXSAttributeChecker, SymbolTable paramSymbolTable)
    throws XMLSchemaException
  {
    fSchemaElement = paramElement;
    fNamespaceSupport = new SchemaNamespaceSupport(paramElement, paramSymbolTable);
    fNamespaceSupport.reset();
    fIsChameleonSchema = false;
    fSymbolTable = paramSymbolTable;
    fAttrChecker = paramXSAttributeChecker;
    if (paramElement != null)
    {
      Element localElement = paramElement;
      fSchemaAttrs = paramXSAttributeChecker.checkAttributes(localElement, true, this);
      if (fSchemaAttrs == null) {
        throw new XMLSchemaException(null, null);
      }
      fAreLocalAttributesQualified = (((XInt)fSchemaAttrs[XSAttributeChecker.ATTIDX_AFORMDEFAULT]).intValue() == 1);
      fAreLocalElementsQualified = (((XInt)fSchemaAttrs[XSAttributeChecker.ATTIDX_EFORMDEFAULT]).intValue() == 1);
      fBlockDefault = ((XInt)fSchemaAttrs[XSAttributeChecker.ATTIDX_BLOCKDEFAULT]).shortValue();
      fFinalDefault = ((XInt)fSchemaAttrs[XSAttributeChecker.ATTIDX_FINALDEFAULT]).shortValue();
      fTargetNamespace = ((String)fSchemaAttrs[XSAttributeChecker.ATTIDX_TARGETNAMESPACE]);
      if (fTargetNamespace != null) {
        fTargetNamespace = paramSymbolTable.addSymbol(fTargetNamespace);
      }
      fNamespaceSupportRoot = new SchemaNamespaceSupport(fNamespaceSupport);
      fValidationContext.setNamespaceSupport(fNamespaceSupport);
      fValidationContext.setSymbolTable(paramSymbolTable);
    }
  }
  
  void backupNSSupport(SchemaNamespaceSupport paramSchemaNamespaceSupport)
  {
    SchemaNamespaceSupportStack.push(fNamespaceSupport);
    if (paramSchemaNamespaceSupport == null) {
      paramSchemaNamespaceSupport = fNamespaceSupportRoot;
    }
    fNamespaceSupport = new SchemaNamespaceSupport(paramSchemaNamespaceSupport);
    fValidationContext.setNamespaceSupport(fNamespaceSupport);
  }
  
  void restoreNSSupport()
  {
    fNamespaceSupport = ((SchemaNamespaceSupport)SchemaNamespaceSupportStack.pop());
    fValidationContext.setNamespaceSupport(fNamespaceSupport);
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    if (fTargetNamespace == null)
    {
      localStringBuffer.append("no targetNamspace");
    }
    else
    {
      localStringBuffer.append("targetNamespace is ");
      localStringBuffer.append(fTargetNamespace);
    }
    Object localObject = fSchemaElement != null ? fSchemaElement.getOwnerDocument() : null;
    if ((localObject instanceof SchemaDOM))
    {
      String str = localObject.getDocumentURI();
      if ((str != null) && (str.length() > 0))
      {
        localStringBuffer.append(" :: schemaLocation is ");
        localStringBuffer.append(str);
      }
    }
    return localStringBuffer.toString();
  }
  
  public void addAllowedNS(String paramString)
  {
    fImportedNS.addElement(paramString == null ? "" : paramString);
  }
  
  public boolean isAllowedNS(String paramString)
  {
    return fImportedNS.contains(paramString == null ? "" : paramString);
  }
  
  final boolean needReportTNSError(String paramString)
  {
    if (fReportedTNS == null) {
      fReportedTNS = new Vector();
    } else if (fReportedTNS.contains(paramString)) {
      return false;
    }
    fReportedTNS.addElement(paramString);
    return true;
  }
  
  Object[] getSchemaAttrs()
  {
    return fSchemaAttrs;
  }
  
  void returnSchemaAttrs()
  {
    fAttrChecker.returnAttrArray(fSchemaAttrs, null);
    fSchemaAttrs = null;
  }
  
  void addAnnotation(XSAnnotationInfo paramXSAnnotationInfo)
  {
    next = fAnnotations;
    fAnnotations = paramXSAnnotationInfo;
  }
  
  XSAnnotationInfo getAnnotations()
  {
    return fAnnotations;
  }
  
  void removeAnnotations()
  {
    fAnnotations = null;
  }
}
