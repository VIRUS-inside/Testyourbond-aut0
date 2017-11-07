package org.xml.sax;

public abstract interface XMLFilter
  extends XMLReader
{
  public abstract void setParent(XMLReader paramXMLReader);
  
  public abstract XMLReader getParent();
}
