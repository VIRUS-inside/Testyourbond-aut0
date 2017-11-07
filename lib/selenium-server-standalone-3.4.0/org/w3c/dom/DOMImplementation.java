package org.w3c.dom;

public abstract interface DOMImplementation
{
  public abstract boolean hasFeature(String paramString1, String paramString2);
  
  public abstract DocumentType createDocumentType(String paramString1, String paramString2, String paramString3)
    throws DOMException;
  
  public abstract Document createDocument(String paramString1, String paramString2, DocumentType paramDocumentType)
    throws DOMException;
  
  public abstract Object getFeature(String paramString1, String paramString2);
}
