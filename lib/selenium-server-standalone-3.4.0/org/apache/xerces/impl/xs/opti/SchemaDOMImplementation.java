package org.apache.xerces.impl.xs.opti;

import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;

final class SchemaDOMImplementation
  implements DOMImplementation
{
  private static final SchemaDOMImplementation singleton = new SchemaDOMImplementation();
  
  public static DOMImplementation getDOMImplementation()
  {
    return singleton;
  }
  
  private SchemaDOMImplementation() {}
  
  public Document createDocument(String paramString1, String paramString2, DocumentType paramDocumentType)
    throws DOMException
  {
    throw new DOMException((short)9, "Method not supported");
  }
  
  public DocumentType createDocumentType(String paramString1, String paramString2, String paramString3)
    throws DOMException
  {
    throw new DOMException((short)9, "Method not supported");
  }
  
  public Object getFeature(String paramString1, String paramString2)
  {
    if (singleton.hasFeature(paramString1, paramString2)) {
      return singleton;
    }
    return null;
  }
  
  public boolean hasFeature(String paramString1, String paramString2)
  {
    int i = (paramString2 == null) || (paramString2.length() == 0) ? 1 : 0;
    return ((paramString1.equalsIgnoreCase("Core")) || (paramString1.equalsIgnoreCase("XML"))) && ((i != 0) || (paramString2.equals("1.0")) || (paramString2.equals("2.0")) || (paramString2.equals("3.0")));
  }
}
