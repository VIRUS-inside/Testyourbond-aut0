package org.apache.xerces.impl.xs;

import org.apache.xerces.impl.xs.util.XSObjectListImpl;
import org.apache.xerces.xs.XSAnnotation;
import org.apache.xerces.xs.XSModelGroup;
import org.apache.xerces.xs.XSNamespaceItem;
import org.apache.xerces.xs.XSObjectList;

public class XSModelGroupImpl
  implements XSModelGroup
{
  public static final short MODELGROUP_CHOICE = 101;
  public static final short MODELGROUP_SEQUENCE = 102;
  public static final short MODELGROUP_ALL = 103;
  public short fCompositor;
  public XSParticleDecl[] fParticles = null;
  public int fParticleCount = 0;
  public XSObjectList fAnnotations = null;
  private String fDescription = null;
  
  public XSModelGroupImpl() {}
  
  public boolean isEmpty()
  {
    for (int i = 0; i < fParticleCount; i++) {
      if (!fParticles[i].isEmpty()) {
        return false;
      }
    }
    return true;
  }
  
  public int minEffectiveTotalRange()
  {
    if (fCompositor == 101) {
      return minEffectiveTotalRangeChoice();
    }
    return minEffectiveTotalRangeAllSeq();
  }
  
  private int minEffectiveTotalRangeAllSeq()
  {
    int i = 0;
    for (int j = 0; j < fParticleCount; j++) {
      i += fParticles[j].minEffectiveTotalRange();
    }
    return i;
  }
  
  private int minEffectiveTotalRangeChoice()
  {
    int i = 0;
    if (fParticleCount > 0) {
      i = fParticles[0].minEffectiveTotalRange();
    }
    for (int k = 1; k < fParticleCount; k++)
    {
      int j = fParticles[k].minEffectiveTotalRange();
      if (j < i) {
        i = j;
      }
    }
    return i;
  }
  
  public int maxEffectiveTotalRange()
  {
    if (fCompositor == 101) {
      return maxEffectiveTotalRangeChoice();
    }
    return maxEffectiveTotalRangeAllSeq();
  }
  
  private int maxEffectiveTotalRangeAllSeq()
  {
    int i = 0;
    for (int k = 0; k < fParticleCount; k++)
    {
      int j = fParticles[k].maxEffectiveTotalRange();
      if (j == -1) {
        return -1;
      }
      i += j;
    }
    return i;
  }
  
  private int maxEffectiveTotalRangeChoice()
  {
    int i = 0;
    if (fParticleCount > 0)
    {
      i = fParticles[0].maxEffectiveTotalRange();
      if (i == -1) {
        return -1;
      }
    }
    for (int k = 1; k < fParticleCount; k++)
    {
      int j = fParticles[k].maxEffectiveTotalRange();
      if (j == -1) {
        return -1;
      }
      if (j > i) {
        i = j;
      }
    }
    return i;
  }
  
  public String toString()
  {
    if (fDescription == null)
    {
      StringBuffer localStringBuffer = new StringBuffer();
      if (fCompositor == 103) {
        localStringBuffer.append("all(");
      } else {
        localStringBuffer.append('(');
      }
      if (fParticleCount > 0) {
        localStringBuffer.append(fParticles[0].toString());
      }
      for (int i = 1; i < fParticleCount; i++)
      {
        if (fCompositor == 101) {
          localStringBuffer.append('|');
        } else {
          localStringBuffer.append(',');
        }
        localStringBuffer.append(fParticles[i].toString());
      }
      localStringBuffer.append(')');
      fDescription = localStringBuffer.toString();
    }
    return fDescription;
  }
  
  public void reset()
  {
    fCompositor = 102;
    fParticles = null;
    fParticleCount = 0;
    fDescription = null;
    fAnnotations = null;
  }
  
  public short getType()
  {
    return 7;
  }
  
  public String getName()
  {
    return null;
  }
  
  public String getNamespace()
  {
    return null;
  }
  
  public short getCompositor()
  {
    if (fCompositor == 101) {
      return 2;
    }
    if (fCompositor == 102) {
      return 1;
    }
    return 3;
  }
  
  public XSObjectList getParticles()
  {
    return new XSObjectListImpl(fParticles, fParticleCount);
  }
  
  public XSAnnotation getAnnotation()
  {
    return fAnnotations != null ? (XSAnnotation)fAnnotations.item(0) : null;
  }
  
  public XSObjectList getAnnotations()
  {
    return fAnnotations != null ? fAnnotations : XSObjectListImpl.EMPTY_LIST;
  }
  
  public XSNamespaceItem getNamespaceItem()
  {
    return null;
  }
}
