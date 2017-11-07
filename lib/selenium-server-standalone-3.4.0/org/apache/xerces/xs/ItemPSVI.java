package org.apache.xerces.xs;

public abstract interface ItemPSVI
{
  public static final short VALIDITY_NOTKNOWN = 0;
  public static final short VALIDITY_INVALID = 1;
  public static final short VALIDITY_VALID = 2;
  public static final short VALIDATION_NONE = 0;
  public static final short VALIDATION_PARTIAL = 1;
  public static final short VALIDATION_FULL = 2;
  
  public abstract String getValidationContext();
  
  public abstract short getValidity();
  
  public abstract short getValidationAttempted();
  
  public abstract StringList getErrorCodes();
  
  public abstract StringList getErrorMessages();
  
  /**
   * @deprecated
   */
  public abstract String getSchemaNormalizedValue();
  
  /**
   * @deprecated
   */
  public abstract Object getActualNormalizedValue()
    throws XSException;
  
  /**
   * @deprecated
   */
  public abstract short getActualNormalizedValueType()
    throws XSException;
  
  /**
   * @deprecated
   */
  public abstract ShortList getItemValueTypes()
    throws XSException;
  
  public abstract XSValue getSchemaValue();
  
  public abstract XSTypeDefinition getTypeDefinition();
  
  public abstract XSSimpleTypeDefinition getMemberTypeDefinition();
  
  public abstract String getSchemaDefault();
  
  public abstract boolean getIsSchemaSpecified();
}
