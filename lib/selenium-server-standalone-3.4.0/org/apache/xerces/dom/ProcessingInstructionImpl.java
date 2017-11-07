package org.apache.xerces.dom;

import org.w3c.dom.ProcessingInstruction;

public class ProcessingInstructionImpl
  extends CharacterDataImpl
  implements ProcessingInstruction
{
  static final long serialVersionUID = 7554435174099981510L;
  protected String target;
  
  public ProcessingInstructionImpl(CoreDocumentImpl paramCoreDocumentImpl, String paramString1, String paramString2)
  {
    super(paramCoreDocumentImpl, paramString2);
    target = paramString1;
  }
  
  public short getNodeType()
  {
    return 7;
  }
  
  public String getNodeName()
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    return target;
  }
  
  public String getTarget()
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    return target;
  }
  
  public String getBaseURI()
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    return ownerNode.getBaseURI();
  }
}
