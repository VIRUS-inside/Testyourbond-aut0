package org.xml.sax.helpers;

import java.util.Vector;
import org.xml.sax.AttributeList;

/**
 * @deprecated
 */
public class AttributeListImpl
  implements AttributeList
{
  Vector names = new Vector();
  Vector types = new Vector();
  Vector values = new Vector();
  
  public AttributeListImpl() {}
  
  public AttributeListImpl(AttributeList paramAttributeList)
  {
    setAttributeList(paramAttributeList);
  }
  
  public void setAttributeList(AttributeList paramAttributeList)
  {
    int i = paramAttributeList.getLength();
    clear();
    for (int j = 0; j < i; j++) {
      addAttribute(paramAttributeList.getName(j), paramAttributeList.getType(j), paramAttributeList.getValue(j));
    }
  }
  
  public void addAttribute(String paramString1, String paramString2, String paramString3)
  {
    names.addElement(paramString1);
    types.addElement(paramString2);
    values.addElement(paramString3);
  }
  
  public void removeAttribute(String paramString)
  {
    int i = names.indexOf(paramString);
    if (i >= 0)
    {
      names.removeElementAt(i);
      types.removeElementAt(i);
      values.removeElementAt(i);
    }
  }
  
  public void clear()
  {
    names.removeAllElements();
    types.removeAllElements();
    values.removeAllElements();
  }
  
  public int getLength()
  {
    return names.size();
  }
  
  public String getName(int paramInt)
  {
    if (paramInt < 0) {
      return null;
    }
    try
    {
      return (String)names.elementAt(paramInt);
    }
    catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {}
    return null;
  }
  
  public String getType(int paramInt)
  {
    if (paramInt < 0) {
      return null;
    }
    try
    {
      return (String)types.elementAt(paramInt);
    }
    catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {}
    return null;
  }
  
  public String getValue(int paramInt)
  {
    if (paramInt < 0) {
      return null;
    }
    try
    {
      return (String)values.elementAt(paramInt);
    }
    catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {}
    return null;
  }
  
  public String getType(String paramString)
  {
    return getType(names.indexOf(paramString));
  }
  
  public String getValue(String paramString)
  {
    return getValue(names.indexOf(paramString));
  }
}
