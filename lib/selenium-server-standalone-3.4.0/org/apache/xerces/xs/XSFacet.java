package org.apache.xerces.xs;

public abstract interface XSFacet
  extends XSObject
{
  public abstract short getFacetKind();
  
  public abstract String getLexicalFacetValue();
  
  public abstract int getIntFacetValue();
  
  public abstract Object getActualFacetValue();
  
  public abstract boolean getFixed();
  
  public abstract XSAnnotation getAnnotation();
  
  public abstract XSObjectList getAnnotations();
}
