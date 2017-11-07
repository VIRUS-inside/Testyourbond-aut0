package javax.xml.stream.events;

import java.util.Iterator;
import javax.xml.namespace.QName;

public abstract interface EndElement
  extends XMLEvent
{
  public abstract QName getName();
  
  public abstract Iterator getNamespaces();
}
