package org.apache.xerces.impl.xs;

import org.apache.xerces.impl.xs.util.XSObjectListImpl;
import org.apache.xerces.xs.XSNamespaceItem;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSParticle;
import org.apache.xerces.xs.XSTerm;

public class XSParticleDecl
  implements XSParticle
{
  public static final short PARTICLE_EMPTY = 0;
  public static final short PARTICLE_ELEMENT = 1;
  public static final short PARTICLE_WILDCARD = 2;
  public static final short PARTICLE_MODELGROUP = 3;
  public static final short PARTICLE_ZERO_OR_MORE = 4;
  public static final short PARTICLE_ZERO_OR_ONE = 5;
  public static final short PARTICLE_ONE_OR_MORE = 6;
  public short fType = 0;
  public XSTerm fValue = null;
  public int fMinOccurs = 1;
  public int fMaxOccurs = 1;
  public XSObjectList fAnnotations = null;
  private String fDescription = null;
  
  public XSParticleDecl() {}
  
  public XSParticleDecl makeClone()
  {
    XSParticleDecl localXSParticleDecl = new XSParticleDecl();
    fType = fType;
    fMinOccurs = fMinOccurs;
    fMaxOccurs = fMaxOccurs;
    fDescription = fDescription;
    fValue = fValue;
    fAnnotations = fAnnotations;
    return localXSParticleDecl;
  }
  
  public boolean emptiable()
  {
    return minEffectiveTotalRange() == 0;
  }
  
  public boolean isEmpty()
  {
    if (fType == 0) {
      return true;
    }
    if ((fType == 1) || (fType == 2)) {
      return false;
    }
    return ((XSModelGroupImpl)fValue).isEmpty();
  }
  
  public int minEffectiveTotalRange()
  {
    if (fType == 0) {
      return 0;
    }
    if (fType == 3) {
      return ((XSModelGroupImpl)fValue).minEffectiveTotalRange() * fMinOccurs;
    }
    return fMinOccurs;
  }
  
  public int maxEffectiveTotalRange()
  {
    if (fType == 0) {
      return 0;
    }
    if (fType == 3)
    {
      int i = ((XSModelGroupImpl)fValue).maxEffectiveTotalRange();
      if (i == -1) {
        return -1;
      }
      if ((i != 0) && (fMaxOccurs == -1)) {
        return -1;
      }
      return i * fMaxOccurs;
    }
    return fMaxOccurs;
  }
  
  public String toString()
  {
    if (fDescription == null)
    {
      StringBuffer localStringBuffer = new StringBuffer();
      appendParticle(localStringBuffer);
      if (((fMinOccurs != 0) || (fMaxOccurs != 0)) && ((fMinOccurs != 1) || (fMaxOccurs != 1)))
      {
        localStringBuffer.append('{').append(fMinOccurs);
        if (fMaxOccurs == -1) {
          localStringBuffer.append("-UNBOUNDED");
        } else if (fMinOccurs != fMaxOccurs) {
          localStringBuffer.append('-').append(fMaxOccurs);
        }
        localStringBuffer.append('}');
      }
      fDescription = localStringBuffer.toString();
    }
    return fDescription;
  }
  
  void appendParticle(StringBuffer paramStringBuffer)
  {
    switch (fType)
    {
    case 0: 
      paramStringBuffer.append("EMPTY");
      break;
    case 1: 
      paramStringBuffer.append(fValue.toString());
      break;
    case 2: 
      paramStringBuffer.append('(');
      paramStringBuffer.append(fValue.toString());
      paramStringBuffer.append(')');
      break;
    case 3: 
      paramStringBuffer.append(fValue.toString());
    }
  }
  
  public void reset()
  {
    fType = 0;
    fValue = null;
    fMinOccurs = 1;
    fMaxOccurs = 1;
    fDescription = null;
    fAnnotations = null;
  }
  
  public short getType()
  {
    return 8;
  }
  
  public String getName()
  {
    return null;
  }
  
  public String getNamespace()
  {
    return null;
  }
  
  public int getMinOccurs()
  {
    return fMinOccurs;
  }
  
  public boolean getMaxOccursUnbounded()
  {
    return fMaxOccurs == -1;
  }
  
  public int getMaxOccurs()
  {
    return fMaxOccurs;
  }
  
  public XSTerm getTerm()
  {
    return fValue;
  }
  
  public XSNamespaceItem getNamespaceItem()
  {
    return null;
  }
  
  public XSObjectList getAnnotations()
  {
    return fAnnotations != null ? fAnnotations : XSObjectListImpl.EMPTY_LIST;
  }
}
