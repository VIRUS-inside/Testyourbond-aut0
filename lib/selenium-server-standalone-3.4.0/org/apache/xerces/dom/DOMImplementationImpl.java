package org.apache.xerces.dom;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.DocumentType;

public class DOMImplementationImpl
  extends CoreDOMImplementationImpl
  implements DOMImplementation
{
  static final DOMImplementationImpl singleton = new DOMImplementationImpl();
  
  public DOMImplementationImpl() {}
  
  public static DOMImplementation getDOMImplementation()
  {
    return singleton;
  }
  
  public boolean hasFeature(String paramString1, String paramString2)
  {
    boolean bool = super.hasFeature(paramString1, paramString2);
    if (!bool)
    {
      int i = (paramString2 == null) || (paramString2.length() == 0) ? 1 : 0;
      if (paramString1.startsWith("+")) {
        paramString1 = paramString1.substring(1);
      }
      return ((paramString1.equalsIgnoreCase("Events")) && ((i != 0) || (paramString2.equals("2.0")))) || ((paramString1.equalsIgnoreCase("MutationEvents")) && ((i != 0) || (paramString2.equals("2.0")))) || ((paramString1.equalsIgnoreCase("Traversal")) && ((i != 0) || (paramString2.equals("2.0")))) || ((paramString1.equalsIgnoreCase("Range")) && ((i != 0) || (paramString2.equals("2.0")))) || ((paramString1.equalsIgnoreCase("MutationEvents")) && ((i != 0) || (paramString2.equals("2.0"))));
    }
    return bool;
  }
  
  protected CoreDocumentImpl createDocument(DocumentType paramDocumentType)
  {
    return new DocumentImpl(paramDocumentType);
  }
}
