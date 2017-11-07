package org.xml.sax.helpers;

import org.xml.sax.Attributes;

public class AttributesImpl
  implements Attributes
{
  int length;
  String[] data;
  
  public AttributesImpl()
  {
    length = 0;
    data = null;
  }
  
  public AttributesImpl(Attributes paramAttributes)
  {
    setAttributes(paramAttributes);
  }
  
  public int getLength()
  {
    return length;
  }
  
  public String getURI(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt < length)) {
      return data[(paramInt * 5)];
    }
    return null;
  }
  
  public String getLocalName(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt < length)) {
      return data[(paramInt * 5 + 1)];
    }
    return null;
  }
  
  public String getQName(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt < length)) {
      return data[(paramInt * 5 + 2)];
    }
    return null;
  }
  
  public String getType(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt < length)) {
      return data[(paramInt * 5 + 3)];
    }
    return null;
  }
  
  public String getValue(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt < length)) {
      return data[(paramInt * 5 + 4)];
    }
    return null;
  }
  
  public int getIndex(String paramString1, String paramString2)
  {
    int i = length * 5;
    for (int j = 0; j < i; j += 5) {
      if ((data[j].equals(paramString1)) && (data[(j + 1)].equals(paramString2))) {
        return j / 5;
      }
    }
    return -1;
  }
  
  public int getIndex(String paramString)
  {
    int i = length * 5;
    for (int j = 0; j < i; j += 5) {
      if (data[(j + 2)].equals(paramString)) {
        return j / 5;
      }
    }
    return -1;
  }
  
  public String getType(String paramString1, String paramString2)
  {
    int i = length * 5;
    for (int j = 0; j < i; j += 5) {
      if ((data[j].equals(paramString1)) && (data[(j + 1)].equals(paramString2))) {
        return data[(j + 3)];
      }
    }
    return null;
  }
  
  public String getType(String paramString)
  {
    int i = length * 5;
    for (int j = 0; j < i; j += 5) {
      if (data[(j + 2)].equals(paramString)) {
        return data[(j + 3)];
      }
    }
    return null;
  }
  
  public String getValue(String paramString1, String paramString2)
  {
    int i = length * 5;
    for (int j = 0; j < i; j += 5) {
      if ((data[j].equals(paramString1)) && (data[(j + 1)].equals(paramString2))) {
        return data[(j + 4)];
      }
    }
    return null;
  }
  
  public String getValue(String paramString)
  {
    int i = length * 5;
    for (int j = 0; j < i; j += 5) {
      if (data[(j + 2)].equals(paramString)) {
        return data[(j + 4)];
      }
    }
    return null;
  }
  
  public void clear()
  {
    if (data != null) {
      for (int i = 0; i < length * 5; i++) {
        data[i] = null;
      }
    }
    length = 0;
  }
  
  public void setAttributes(Attributes paramAttributes)
  {
    clear();
    length = paramAttributes.getLength();
    if (length > 0)
    {
      data = new String[length * 5];
      for (int i = 0; i < length; i++)
      {
        data[(i * 5)] = paramAttributes.getURI(i);
        data[(i * 5 + 1)] = paramAttributes.getLocalName(i);
        data[(i * 5 + 2)] = paramAttributes.getQName(i);
        data[(i * 5 + 3)] = paramAttributes.getType(i);
        data[(i * 5 + 4)] = paramAttributes.getValue(i);
      }
    }
  }
  
  public void addAttribute(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
  {
    ensureCapacity(length + 1);
    data[(length * 5)] = paramString1;
    data[(length * 5 + 1)] = paramString2;
    data[(length * 5 + 2)] = paramString3;
    data[(length * 5 + 3)] = paramString4;
    data[(length * 5 + 4)] = paramString5;
    length += 1;
  }
  
  public void setAttribute(int paramInt, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
  {
    if ((paramInt >= 0) && (paramInt < length))
    {
      data[(paramInt * 5)] = paramString1;
      data[(paramInt * 5 + 1)] = paramString2;
      data[(paramInt * 5 + 2)] = paramString3;
      data[(paramInt * 5 + 3)] = paramString4;
      data[(paramInt * 5 + 4)] = paramString5;
    }
    else
    {
      badIndex(paramInt);
    }
  }
  
  public void removeAttribute(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt < length))
    {
      if (paramInt < length - 1) {
        System.arraycopy(data, (paramInt + 1) * 5, data, paramInt * 5, (length - paramInt - 1) * 5);
      }
      paramInt = (length - 1) * 5;
      data[(paramInt++)] = null;
      data[(paramInt++)] = null;
      data[(paramInt++)] = null;
      data[(paramInt++)] = null;
      data[paramInt] = null;
      length -= 1;
    }
    else
    {
      badIndex(paramInt);
    }
  }
  
  public void setURI(int paramInt, String paramString)
  {
    if ((paramInt >= 0) && (paramInt < length)) {
      data[(paramInt * 5)] = paramString;
    } else {
      badIndex(paramInt);
    }
  }
  
  public void setLocalName(int paramInt, String paramString)
  {
    if ((paramInt >= 0) && (paramInt < length)) {
      data[(paramInt * 5 + 1)] = paramString;
    } else {
      badIndex(paramInt);
    }
  }
  
  public void setQName(int paramInt, String paramString)
  {
    if ((paramInt >= 0) && (paramInt < length)) {
      data[(paramInt * 5 + 2)] = paramString;
    } else {
      badIndex(paramInt);
    }
  }
  
  public void setType(int paramInt, String paramString)
  {
    if ((paramInt >= 0) && (paramInt < length)) {
      data[(paramInt * 5 + 3)] = paramString;
    } else {
      badIndex(paramInt);
    }
  }
  
  public void setValue(int paramInt, String paramString)
  {
    if ((paramInt >= 0) && (paramInt < length)) {
      data[(paramInt * 5 + 4)] = paramString;
    } else {
      badIndex(paramInt);
    }
  }
  
  private void ensureCapacity(int paramInt)
  {
    if (paramInt <= 0) {
      return;
    }
    int i;
    if ((data == null) || (data.length == 0))
    {
      i = 25;
    }
    else
    {
      if (data.length >= paramInt * 5) {
        return;
      }
      i = data.length;
    }
    while (i < paramInt * 5) {
      i *= 2;
    }
    String[] arrayOfString = new String[i];
    if (length > 0) {
      System.arraycopy(data, 0, arrayOfString, 0, length * 5);
    }
    data = arrayOfString;
  }
  
  private void badIndex(int paramInt)
    throws ArrayIndexOutOfBoundsException
  {
    String str = "Attempt to modify attribute at illegal index: " + paramInt;
    throw new ArrayIndexOutOfBoundsException(str);
  }
}
