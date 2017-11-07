package org.apache.xerces.dom;

import java.util.ArrayList;
import org.apache.xerces.impl.xs.XSImplementationImpl;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.DOMImplementationList;

public class DOMXSImplementationSourceImpl
  extends DOMImplementationSourceImpl
{
  public DOMXSImplementationSourceImpl() {}
  
  public DOMImplementation getDOMImplementation(String paramString)
  {
    DOMImplementation localDOMImplementation = super.getDOMImplementation(paramString);
    if (localDOMImplementation != null) {
      return localDOMImplementation;
    }
    localDOMImplementation = PSVIDOMImplementationImpl.getDOMImplementation();
    if (testImpl(localDOMImplementation, paramString)) {
      return localDOMImplementation;
    }
    localDOMImplementation = XSImplementationImpl.getDOMImplementation();
    if (testImpl(localDOMImplementation, paramString)) {
      return localDOMImplementation;
    }
    return null;
  }
  
  public DOMImplementationList getDOMImplementationList(String paramString)
  {
    ArrayList localArrayList = new ArrayList();
    DOMImplementationList localDOMImplementationList = super.getDOMImplementationList(paramString);
    for (int i = 0; i < localDOMImplementationList.getLength(); i++) {
      localArrayList.add(localDOMImplementationList.item(i));
    }
    DOMImplementation localDOMImplementation = PSVIDOMImplementationImpl.getDOMImplementation();
    if (testImpl(localDOMImplementation, paramString)) {
      localArrayList.add(localDOMImplementation);
    }
    localDOMImplementation = XSImplementationImpl.getDOMImplementation();
    if (testImpl(localDOMImplementation, paramString)) {
      localArrayList.add(localDOMImplementation);
    }
    return new DOMImplementationListImpl(localArrayList);
  }
}
