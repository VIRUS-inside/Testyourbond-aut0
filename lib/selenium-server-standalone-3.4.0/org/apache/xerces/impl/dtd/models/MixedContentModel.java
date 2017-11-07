package org.apache.xerces.impl.dtd.models;

import org.apache.xerces.xni.QName;

public class MixedContentModel
  implements ContentModelValidator
{
  private final int fCount;
  private final QName[] fChildren;
  private final int[] fChildrenType;
  private final boolean fOrdered;
  
  public MixedContentModel(QName[] paramArrayOfQName, int[] paramArrayOfInt, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    fCount = paramInt2;
    fChildren = new QName[fCount];
    fChildrenType = new int[fCount];
    for (int i = 0; i < fCount; i++)
    {
      fChildren[i] = new QName(paramArrayOfQName[(paramInt1 + i)]);
      fChildrenType[i] = paramArrayOfInt[(paramInt1 + i)];
    }
    fOrdered = paramBoolean;
  }
  
  public int validate(QName[] paramArrayOfQName, int paramInt1, int paramInt2)
  {
    int i;
    int m;
    String str;
    if (fOrdered)
    {
      i = 0;
      for (int j = 0; j < paramInt2; j++)
      {
        QName localQName2 = paramArrayOfQName[(paramInt1 + j)];
        if (localpart != null)
        {
          m = fChildrenType[i];
          if (m == 0)
          {
            if (fChildren[i].rawname != rawname) {
              return j;
            }
          }
          else if (m == 6)
          {
            str = fChildren[i].uri;
            if ((str != null) && (str != uri)) {
              return j;
            }
          }
          else if (m == 8)
          {
            if (uri != null) {
              return j;
            }
          }
          else if ((m == 7) && (fChildren[i].uri == uri))
          {
            return j;
          }
          i++;
        }
      }
    }
    else
    {
      for (i = 0; i < paramInt2; i++)
      {
        QName localQName1 = paramArrayOfQName[(paramInt1 + i)];
        if (localpart != null)
        {
          for (int k = 0; k < fCount; k++)
          {
            m = fChildrenType[k];
            if (m == 0)
            {
              if (rawname == fChildren[k].rawname) {
                break;
              }
            }
            else if (m == 6)
            {
              str = fChildren[k].uri;
              if (str == null) {
                break;
              }
              if (str == uri) {
                break;
              }
            }
            else
            {
              if (m == 8 ? uri != null : (m == 7) && (fChildren[k].uri != uri)) {
                break;
              }
            }
          }
          if (k == fCount) {
            return i;
          }
        }
      }
    }
    return -1;
  }
}
