package org.apache.xerces.dom;

import org.w3c.dom.DOMLocator;
import org.w3c.dom.Node;

public class DOMLocatorImpl
  implements DOMLocator
{
  public int fColumnNumber = -1;
  public int fLineNumber = -1;
  public Node fRelatedNode = null;
  public String fUri = null;
  public int fByteOffset = -1;
  public int fUtf16Offset = -1;
  
  public DOMLocatorImpl() {}
  
  public DOMLocatorImpl(int paramInt1, int paramInt2, String paramString)
  {
    fLineNumber = paramInt1;
    fColumnNumber = paramInt2;
    fUri = paramString;
  }
  
  public DOMLocatorImpl(int paramInt1, int paramInt2, int paramInt3, String paramString)
  {
    fLineNumber = paramInt1;
    fColumnNumber = paramInt2;
    fUri = paramString;
    fUtf16Offset = paramInt3;
  }
  
  public DOMLocatorImpl(int paramInt1, int paramInt2, int paramInt3, Node paramNode, String paramString)
  {
    fLineNumber = paramInt1;
    fColumnNumber = paramInt2;
    fByteOffset = paramInt3;
    fRelatedNode = paramNode;
    fUri = paramString;
  }
  
  public DOMLocatorImpl(int paramInt1, int paramInt2, int paramInt3, Node paramNode, String paramString, int paramInt4)
  {
    fLineNumber = paramInt1;
    fColumnNumber = paramInt2;
    fByteOffset = paramInt3;
    fRelatedNode = paramNode;
    fUri = paramString;
    fUtf16Offset = paramInt4;
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
