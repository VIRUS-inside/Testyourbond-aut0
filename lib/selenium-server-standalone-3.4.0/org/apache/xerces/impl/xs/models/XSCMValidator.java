package org.apache.xerces.impl.xs.models;

import java.util.Vector;
import org.apache.xerces.impl.xs.SubstitutionGroupHandler;
import org.apache.xerces.impl.xs.XMLSchemaException;
import org.apache.xerces.xni.QName;

public abstract interface XSCMValidator
{
  public static final short FIRST_ERROR = -1;
  public static final short SUBSEQUENT_ERROR = -2;
  
  public abstract int[] startContentModel();
  
  public abstract Object oneTransition(QName paramQName, int[] paramArrayOfInt, SubstitutionGroupHandler paramSubstitutionGroupHandler);
  
  public abstract boolean endContentModel(int[] paramArrayOfInt);
  
  public abstract boolean checkUniqueParticleAttribution(SubstitutionGroupHandler paramSubstitutionGroupHandler)
    throws XMLSchemaException;
  
  public abstract Vector whatCanGoHere(int[] paramArrayOfInt);
  
  public abstract int[] occurenceInfo(int[] paramArrayOfInt);
  
  public abstract String getTermName(int paramInt);
  
  public abstract boolean isCompactedForUPA();
}
