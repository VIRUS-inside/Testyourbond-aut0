package org.xml.sax.ext;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;

public class Attributes2Impl
  extends AttributesImpl
  implements Attributes2
{
  private boolean[] declared;
  private boolean[] specified;
  
  public Attributes2Impl() {}
  
  public Attributes2Impl(Attributes paramAttributes)
  {
    super(paramAttributes);
  }
  
  public boolean isDeclared(int paramInt)
  {
    if ((paramInt < 0) || (paramInt >= getLength())) {
      throw new ArrayIndexOutOfBoundsException("No attribute at index: " + paramInt);
    }
    return declared[paramInt];
  }
  
  public boolean isDeclared(String paramString1, String paramString2)
  {
    int i = getIndex(paramString1, paramString2);
    if (i < 0) {
      throw new IllegalArgumentException("No such attribute: local=" + paramString2 + ", namespace=" + paramString1);
    }
    return declared[i];
  }
  
  public boolean isDeclared(String paramString)
  {
    int i = getIndex(paramString);
    if (i < 0) {
      throw new IllegalArgumentException("No such attribute: " + paramString);
    }
    return declared[i];
  }
  
  public boolean isSpecified(int paramInt)
  {
    if ((paramInt < 0) || (paramInt >= getLength())) {
      throw new ArrayIndexOutOfBoundsException("No attribute at index: " + paramInt);
    }
    return specified[paramInt];
  }
  
  public boolean isSpecified(String paramString1, String paramString2)
  {
    int i = getIndex(paramString1, paramString2);
    if (i < 0) {
      throw new IllegalArgumentException("No such attribute: local=" + paramString2 + ", namespace=" + paramString1);
    }
    return specified[i];
  }
  
  public boolean isSpecified(String paramString)
  {
    int i = getIndex(paramString);
    if (i < 0) {
      throw new IllegalArgumentException("No such attribute: " + paramString);
    }
    return specified[i];
  }
  
  public void setAttributes(Attributes paramAttributes)
  {
    int i = paramAttributes.getLength();
    super.setAttributes(paramAttributes);
    declared = new boolean[i];
    specified = new boolean[i];
    if ((paramAttributes instanceof Attributes2))
    {
      Attributes2 localAttributes2 = (Attributes2)paramAttributes;
      for (int k = 0; k < i; k++)
      {
        declared[k] = localAttributes2.isDeclared(k);
        specified[k] = localAttributes2.isSpecified(k);
      }
    }
    else
    {
      for (int j = 0; j < i; j++)
      {
        declared[j] = (!"CDATA".equals(paramAttributes.getType(j)) ? 1 : false);
        specified[j] = true;
      }
    }
  }
  
  public void addAttribute(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
  {
    super.addAttribute(paramString1, paramString2, paramString3, paramString4, paramString5);
    int i = getLength();
    if (i < specified.length)
    {
      boolean[] arrayOfBoolean = new boolean[i];
      System.arraycopy(declared, 0, arrayOfBoolean, 0, declared.length);
      declared = arrayOfBoolean;
      arrayOfBoolean = new boolean[i];
      System.arraycopy(specified, 0, arrayOfBoolean, 0, specified.length);
      specified = arrayOfBoolean;
    }
    specified[(i - 1)] = true;
    declared[(i - 1)] = (!"CDATA".equals(paramString4) ? 1 : false);
  }
  
  public void removeAttribute(int paramInt)
  {
    int i = getLength() - 1;
    super.removeAttribute(paramInt);
    if (paramInt != i)
    {
      System.arraycopy(declared, paramInt + 1, declared, paramInt, i - paramInt);
      System.arraycopy(specified, paramInt + 1, specified, paramInt, i - paramInt);
    }
  }
  
  public void setDeclared(int paramInt, boolean paramBoolean)
  {
    if ((paramInt < 0) || (paramInt >= getLength())) {
      throw new ArrayIndexOutOfBoundsException("No attribute at index: " + paramInt);
    }
    declared[paramInt] = paramBoolean;
  }
  
  public void setSpecified(int paramInt, boolean paramBoolean)
  {
    if ((paramInt < 0) || (paramInt >= getLength())) {
      throw new ArrayIndexOutOfBoundsException("No attribute at index: " + paramInt);
    }
    specified[paramInt] = paramBoolean;
  }
}
