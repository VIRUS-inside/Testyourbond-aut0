package org.apache.xerces.stax.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.events.Namespace;

abstract class ElementImpl
  extends XMLEventImpl
{
  private final QName fName;
  private final List fNamespaces;
  
  ElementImpl(QName paramQName, boolean paramBoolean, Iterator paramIterator, Location paramLocation)
  {
    super(paramBoolean ? 1 : 2, paramLocation);
    fName = paramQName;
    if ((paramIterator != null) && (paramIterator.hasNext()))
    {
      fNamespaces = new ArrayList();
      do
      {
        Namespace localNamespace = (Namespace)paramIterator.next();
        fNamespaces.add(localNamespace);
      } while (paramIterator.hasNext());
    }
    else
    {
      fNamespaces = Collections.EMPTY_LIST;
    }
  }
  
  public final QName getName()
  {
    return fName;
  }
  
  public final Iterator getNamespaces()
  {
    return createImmutableIterator(fNamespaces.iterator());
  }
  
  static Iterator createImmutableIterator(Iterator paramIterator)
  {
    return new NoRemoveIterator(paramIterator);
  }
  
  private static final class NoRemoveIterator
    implements Iterator
  {
    private final Iterator fWrapped;
    
    public NoRemoveIterator(Iterator paramIterator)
    {
      fWrapped = paramIterator;
    }
    
    public boolean hasNext()
    {
      return fWrapped.hasNext();
    }
    
    public Object next()
    {
      return fWrapped.next();
    }
    
    public void remove()
    {
      throw new UnsupportedOperationException("Attributes iterator is read-only.");
    }
  }
}
