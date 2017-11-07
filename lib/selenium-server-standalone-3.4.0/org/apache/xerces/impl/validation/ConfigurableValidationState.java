package org.apache.xerces.impl.validation;

public final class ConfigurableValidationState
  extends ValidationState
{
  private boolean fIdIdrefChecking = true;
  private boolean fUnparsedEntityChecking = true;
  
  public ConfigurableValidationState() {}
  
  public void setIdIdrefChecking(boolean paramBoolean)
  {
    fIdIdrefChecking = paramBoolean;
  }
  
  public void setUnparsedEntityChecking(boolean paramBoolean)
  {
    fUnparsedEntityChecking = paramBoolean;
  }
  
  public String checkIDRefID()
  {
    return fIdIdrefChecking ? super.checkIDRefID() : null;
  }
  
  public boolean isIdDeclared(String paramString)
  {
    return fIdIdrefChecking ? super.isIdDeclared(paramString) : false;
  }
  
  public boolean isEntityDeclared(String paramString)
  {
    return fUnparsedEntityChecking ? super.isEntityDeclared(paramString) : true;
  }
  
  public boolean isEntityUnparsed(String paramString)
  {
    return fUnparsedEntityChecking ? super.isEntityUnparsed(paramString) : true;
  }
  
  public void addId(String paramString)
  {
    if (fIdIdrefChecking) {
      super.addId(paramString);
    }
  }
  
  public void addIdRef(String paramString)
  {
    if (fIdIdrefChecking) {
      super.addIdRef(paramString);
    }
  }
}
