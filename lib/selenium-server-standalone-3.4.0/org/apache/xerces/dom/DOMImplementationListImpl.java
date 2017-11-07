package org.apache.xerces.dom;

import java.util.ArrayList;
import java.util.Vector;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.DOMImplementationList;

public class DOMImplementationListImpl
  implements DOMImplementationList
{
  private final ArrayList fImplementations;
  
  public DOMImplementationListImpl()
  {
    fImplementations = new ArrayList();
  }
  
  public DOMImplementationListImpl(ArrayList paramArrayList)
  {
    fImplementations = paramArrayList;
  }
  
  public DOMImplementationListImpl(Vector paramVector)
  {
    fImplementations = new ArrayList(paramVector);
  }
  
  public DOMImplementation item(int paramInt)
  {
    int i = getLength();
    if ((paramInt >= 0) && (paramInt < i)) {
      return (DOMImplementation)fImplementations.get(paramInt);
    }
    return null;
  }
  
  public int getLength()
  {
    return fImplementations.size();
  }
}
