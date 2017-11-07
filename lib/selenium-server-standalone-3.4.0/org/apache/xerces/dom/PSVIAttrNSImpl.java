package org.apache.xerces.dom;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.apache.xerces.impl.dv.ValidatedInfo;
import org.apache.xerces.impl.xs.util.StringListImpl;
import org.apache.xerces.xs.AttributePSVI;
import org.apache.xerces.xs.ShortList;
import org.apache.xerces.xs.StringList;
import org.apache.xerces.xs.XSAttributeDeclaration;
import org.apache.xerces.xs.XSSimpleTypeDefinition;
import org.apache.xerces.xs.XSTypeDefinition;
import org.apache.xerces.xs.XSValue;

public class PSVIAttrNSImpl
  extends AttrNSImpl
  implements AttributePSVI
{
  static final long serialVersionUID = -3241738699421018889L;
  protected XSAttributeDeclaration fDeclaration = null;
  protected XSTypeDefinition fTypeDecl = null;
  protected boolean fSpecified = true;
  protected ValidatedInfo fValue = new ValidatedInfo();
  protected short fValidationAttempted = 0;
  protected short fValidity = 0;
  protected StringList fErrorCodes = null;
  protected StringList fErrorMessages = null;
  protected String fValidationContext = null;
  
  public PSVIAttrNSImpl(CoreDocumentImpl paramCoreDocumentImpl, String paramString1, String paramString2, String paramString3)
  {
    super(paramCoreDocumentImpl, paramString1, paramString2, paramString3);
  }
  
  public PSVIAttrNSImpl(CoreDocumentImpl paramCoreDocumentImpl, String paramString1, String paramString2)
  {
    super(paramCoreDocumentImpl, paramString1, paramString2);
  }
  
  public String getSchemaDefault()
  {
    return fDeclaration == null ? null : fDeclaration.getConstraintValue();
  }
  
  public String getSchemaNormalizedValue()
  {
    return fValue.getNormalizedValue();
  }
  
  public boolean getIsSchemaSpecified()
  {
    return fSpecified;
  }
  
  public short getValidationAttempted()
  {
    return fValidationAttempted;
  }
  
  public short getValidity()
  {
    return fValidity;
  }
  
  public StringList getErrorCodes()
  {
    if (fErrorCodes != null) {
      return fErrorCodes;
    }
    return StringListImpl.EMPTY_LIST;
  }
  
  public StringList getErrorMessages()
  {
    if (fErrorMessages != null) {
      return fErrorMessages;
    }
    return StringListImpl.EMPTY_LIST;
  }
  
  public String getValidationContext()
  {
    return fValidationContext;
  }
  
  public XSTypeDefinition getTypeDefinition()
  {
    return fTypeDecl;
  }
  
  public XSSimpleTypeDefinition getMemberTypeDefinition()
  {
    return fValue.getMemberTypeDefinition();
  }
  
  public XSAttributeDeclaration getAttributeDeclaration()
  {
    return fDeclaration;
  }
  
  public void setPSVI(AttributePSVI paramAttributePSVI)
  {
    fDeclaration = paramAttributePSVI.getAttributeDeclaration();
    fValidationContext = paramAttributePSVI.getValidationContext();
    fValidity = paramAttributePSVI.getValidity();
    fValidationAttempted = paramAttributePSVI.getValidationAttempted();
    fErrorCodes = paramAttributePSVI.getErrorCodes();
    fErrorMessages = paramAttributePSVI.getErrorMessages();
    fValue.copyFrom(paramAttributePSVI.getSchemaValue());
    fTypeDecl = paramAttributePSVI.getTypeDefinition();
    fSpecified = paramAttributePSVI.getIsSchemaSpecified();
  }
  
  public Object getActualNormalizedValue()
  {
    return fValue.getActualValue();
  }
  
  public short getActualNormalizedValueType()
  {
    return fValue.getActualValueType();
  }
  
  public ShortList getItemValueTypes()
  {
    return fValue.getListValueTypes();
  }
  
  public XSValue getSchemaValue()
  {
    return fValue;
  }
  
  private void writeObject(ObjectOutputStream paramObjectOutputStream)
    throws IOException
  {
    throw new NotSerializableException(getClass().getName());
  }
  
  private void readObject(ObjectInputStream paramObjectInputStream)
    throws IOException, ClassNotFoundException
  {
    throw new NotSerializableException(getClass().getName());
  }
}
