package org.apache.xml.serializer.dom3;

import org.w3c.dom.DOMLocator;
import org.w3c.dom.Node;



































































final class DOMLocatorImpl
  implements DOMLocator
{
  private final int fColumnNumber;
  private final int fLineNumber;
  private final Node fRelatedNode;
  private final String fUri;
  private final int fByteOffset;
  private final int fUtf16Offset;
  
  DOMLocatorImpl()
  {
    fColumnNumber = -1;
    fLineNumber = -1;
    fRelatedNode = null;
    fUri = null;
    fByteOffset = -1;
    fUtf16Offset = -1;
  }
  
  DOMLocatorImpl(int lineNumber, int columnNumber, String uri) {
    fLineNumber = lineNumber;
    fColumnNumber = columnNumber;
    fUri = uri;
    
    fRelatedNode = null;
    fByteOffset = -1;
    fUtf16Offset = -1;
  }
  
  DOMLocatorImpl(int lineNumber, int columnNumber, int utf16Offset, String uri) {
    fLineNumber = lineNumber;
    fColumnNumber = columnNumber;
    fUri = uri;
    fUtf16Offset = utf16Offset;
    

    fRelatedNode = null;
    fByteOffset = -1;
  }
  
  DOMLocatorImpl(int lineNumber, int columnNumber, int byteoffset, Node relatedData, String uri) {
    fLineNumber = lineNumber;
    fColumnNumber = columnNumber;
    fByteOffset = byteoffset;
    fRelatedNode = relatedData;
    fUri = uri;
    
    fUtf16Offset = -1;
  }
  
  DOMLocatorImpl(int lineNumber, int columnNumber, int byteoffset, Node relatedData, String uri, int utf16Offset) {
    fLineNumber = lineNumber;
    fColumnNumber = columnNumber;
    fByteOffset = byteoffset;
    fRelatedNode = relatedData;
    fUri = uri;
    fUtf16Offset = utf16Offset;
  }
  




  public int getLineNumber()
  {
    return fLineNumber;
  }
  



  public int getColumnNumber()
  {
    return fColumnNumber;
  }
  



  public String getUri()
  {
    return fUri;
  }
  
  public Node getRelatedNode()
  {
    return fRelatedNode;
  }
  




  public int getByteOffset()
  {
    return fByteOffset;
  }
  




  public int getUtf16Offset()
  {
    return fUtf16Offset;
  }
}
