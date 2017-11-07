package org.apache.xml.serialize;

import java.io.IOException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.AttributeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * @deprecated
 */
public class TextSerializer
  extends BaseMarkupSerializer
{
  public TextSerializer()
  {
    super(new OutputFormat("text", null, false));
  }
  
  public void setOutputFormat(OutputFormat paramOutputFormat)
  {
    super.setOutputFormat(paramOutputFormat != null ? paramOutputFormat : new OutputFormat("text", null, false));
  }
  
  public void startElement(String paramString1, String paramString2, String paramString3, Attributes paramAttributes)
    throws SAXException
  {
    startElement(paramString3 == null ? paramString2 : paramString3, null);
  }
  
  public void endElement(String paramString1, String paramString2, String paramString3)
    throws SAXException
  {
    endElement(paramString3 == null ? paramString2 : paramString3);
  }
  
  public void startElement(String paramString, AttributeList paramAttributeList)
    throws SAXException
  {
    try
    {
      ElementState localElementState = getElementState();
      if ((isDocumentState()) && (!_started)) {
        startDocument(paramString);
      }
      boolean bool = preserveSpace;
      localElementState = enterElementState(null, null, paramString, bool);
    }
    catch (IOException localIOException)
    {
      throw new SAXException(localIOException);
    }
  }
  
  public void endElement(String paramString)
    throws SAXException
  {
    try
    {
      endElementIO(paramString);
    }
    catch (IOException localIOException)
    {
      throw new SAXException(localIOException);
    }
  }
  
  public void endElementIO(String paramString)
    throws IOException
  {
    ElementState localElementState = getElementState();
    localElementState = leaveElementState();
    afterElement = true;
    empty = false;
    if (isDocumentState()) {
      _printer.flush();
    }
  }
  
  public void processingInstructionIO(String paramString1, String paramString2)
    throws IOException
  {}
  
  public void comment(String paramString) {}
  
  public void comment(char[] paramArrayOfChar, int paramInt1, int paramInt2) {}
  
  public void characters(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws SAXException
  {
    try
    {
      ElementState localElementState = content();
      doCData = (localElementState.inCData = 0);
      printText(paramArrayOfChar, paramInt1, paramInt2, true, true);
    }
    catch (IOException localIOException)
    {
      throw new SAXException(localIOException);
    }
  }
  
  protected void characters(String paramString, boolean paramBoolean)
    throws IOException
  {
    ElementState localElementState = content();
    doCData = (localElementState.inCData = 0);
    printText(paramString, true, true);
  }
  
  protected void startDocument(String paramString)
    throws IOException
  {
    _printer.leaveDTD();
    _started = true;
    serializePreRoot();
  }
  
  protected void serializeElement(Element paramElement)
    throws IOException
  {
    String str = paramElement.getTagName();
    ElementState localElementState = getElementState();
    if ((isDocumentState()) && (!_started)) {
      startDocument(str);
    }
    boolean bool = preserveSpace;
    if (paramElement.hasChildNodes())
    {
      localElementState = enterElementState(null, null, str, bool);
      for (Node localNode = paramElement.getFirstChild(); localNode != null; localNode = localNode.getNextSibling()) {
        serializeNode(localNode);
      }
      endElementIO(str);
    }
    else if (!isDocumentState())
    {
      afterElement = true;
      empty = false;
    }
  }
  
  protected void serializeNode(Node paramNode)
    throws IOException
  {
    Object localObject;
    switch (paramNode.getNodeType())
    {
    case 3: 
      localObject = paramNode.getNodeValue();
      if (localObject != null) {
        characters(paramNode.getNodeValue(), true);
      }
      break;
    case 4: 
      localObject = paramNode.getNodeValue();
      if (localObject != null) {
        characters(paramNode.getNodeValue(), true);
      }
      break;
    case 8: 
      break;
    case 5: 
      break;
    case 7: 
      break;
    case 1: 
      serializeElement((Element)paramNode);
      break;
    case 9: 
    case 11: 
      localObject = paramNode.getFirstChild();
      for (;;)
      {
        serializeNode((Node)localObject);
        localObject = ((Node)localObject).getNextSibling();
        if (localObject == null) {
          break;
        }
      }
    }
  }
  
  protected ElementState content()
  {
    ElementState localElementState = getElementState();
    if (!isDocumentState())
    {
      if (empty) {
        empty = false;
      }
      afterElement = false;
    }
    return localElementState;
  }
  
  protected String getEntityRef(int paramInt)
  {
    return null;
  }
}
