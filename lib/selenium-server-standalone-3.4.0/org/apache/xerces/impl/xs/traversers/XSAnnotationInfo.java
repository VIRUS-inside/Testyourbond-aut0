package org.apache.xerces.impl.xs.traversers;

import org.apache.xerces.impl.xs.opti.ElementImpl;
import org.w3c.dom.Element;

final class XSAnnotationInfo
{
  String fAnnotation;
  int fLine;
  int fColumn;
  int fCharOffset;
  XSAnnotationInfo next;
  
  XSAnnotationInfo(String paramString, int paramInt1, int paramInt2, int paramInt3)
  {
    fAnnotation = paramString;
    fLine = paramInt1;
    fColumn = paramInt2;
    fCharOffset = paramInt3;
  }
  
  XSAnnotationInfo(String paramString, Element paramElement)
  {
    fAnnotation = paramString;
    if ((paramElement instanceof ElementImpl))
    {
      ElementImpl localElementImpl = (ElementImpl)paramElement;
      fLine = localElementImpl.getLineNumber();
      fColumn = localElementImpl.getColumnNumber();
      fCharOffset = localElementImpl.getCharacterOffset();
    }
    else
    {
      fLine = -1;
      fColumn = -1;
      fCharOffset = -1;
    }
  }
}
