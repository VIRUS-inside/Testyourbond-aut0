package javax.xml.stream.events;

import java.util.Iterator;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;

public abstract interface StartElement
  extends XMLEvent
{
  public abstract Attribute getAttributeByName(QName paramQName);
  
  public abstract Iterator getAttributes();
  
  public abstract QName getName();
  
  public abstract NamespaceContext getNamespaceContext();
  
  public abstract Iterator getNamespaces();
  
  public abstract String getNamespaceURI(String paramString);
}
