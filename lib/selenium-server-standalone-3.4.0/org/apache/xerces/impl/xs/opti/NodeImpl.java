package org.apache.xerces.impl.xs.opti;

public class NodeImpl
  extends DefaultNode
{
  String prefix;
  String localpart;
  String rawname;
  String uri;
  short nodeType;
  boolean hidden;
  
  public NodeImpl() {}
  
  public NodeImpl(String paramString1, String paramString2, String paramString3, String paramString4, short paramShort)
  {
    prefix = paramString1;
    localpart = paramString2;
    rawname = paramString3;
    uri = paramString4;
    nodeType = paramShort;
  }
  
  public String getNodeName()
  {
    return rawname;
  }
  
  public String getNamespaceURI()
  {
    return uri;
  }
  
  public String getPrefix()
  {
    return prefix;
  }
  
  public String getLocalName()
  {
    return localpart;
  }
  
  public short getNodeType()
  {
    return nodeType;
  }
  
  public void setReadOnly(boolean paramBoolean1, boolean paramBoolean2)
  {
    hidden = paramBoolean1;
  }
  
  public boolean getReadOnly()
  {
    return hidden;
  }
  
  public String toString()
  {
    return "[" + getNodeName() + ": " + getNodeValue() + "]";
  }
}
