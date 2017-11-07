package org.apache.xerces.dom;

import java.util.ArrayList;
import java.util.Vector;
import org.w3c.dom.DOMStringList;

public class DOMStringListImpl
  implements DOMStringList
{
  private final ArrayList fStrings;
  
  public DOMStringListImpl()
  {
    fStrings = new ArrayList();
  }
  
  public DOMStringListImpl(ArrayList paramArrayList)
  {
    fStrings = paramArrayList;
  }
  
  public DOMStringListImpl(Vector paramVector)
  {
    fStrings = new ArrayList(paramVector);
  }
  
  public String item(int paramInt)
  {
    int i = getLength();
    if ((paramInt >= 0) && (paramInt < i)) {
      return (String)fStrings.get(paramInt);
    }
    return null;
  }
  
  public int getLength()
  {
    return fStrings.size();
  }
  
  public boolean contains(String paramString)
  {
    return fStrings.contains(paramString);
  }
  
  public void add(String paramString)
  {
    fStrings.add(paramString);
  }
}
