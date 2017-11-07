package org.apache.xerces.stax.events;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Namespace;
import javax.xml.stream.events.StartElement;
import org.apache.xerces.stax.DefaultNamespaceContext;

public final class StartElementImpl
  extends ElementImpl
  implements StartElement
{
  private static final Comparator QNAME_COMPARATOR = new Comparator()
  {
    public int compare(Object paramAnonymousObject1, Object paramAnonymousObject2)
    {
      if (paramAnonymousObject1.equals(paramAnonymousObject2)) {
        return 0;
      }
      QName localQName1 = (QName)paramAnonymousObject1;
      QName localQName2 = (QName)paramAnonymousObject2;
      return localQName1.toString().compareTo(localQName2.toString());
    }
  };
  private final Map fAttributes;
  private final NamespaceContext fNamespaceContext;
  
  public StartElementImpl(QName paramQName, Iterator paramIterator1, Iterator paramIterator2, NamespaceContext paramNamespaceContext, Location paramLocation)
  {
    super(paramQName, true, paramIterator2, paramLocation);
    if ((paramIterator1 != null) && (paramIterator1.hasNext()))
    {
      fAttributes = new TreeMap(QNAME_COMPARATOR);
      do
      {
        Attribute localAttribute = (Attribute)paramIterator1.next();
        fAttributes.put(localAttribute.getName(), localAttribute);
      } while (paramIterator1.hasNext());
    }
    else
    {
      fAttributes = Collections.EMPTY_MAP;
    }
    fNamespaceContext = (paramNamespaceContext != null ? paramNamespaceContext : DefaultNamespaceContext.getInstance());
  }
  
  public Iterator getAttributes()
  {
    return ElementImpl.createImmutableIterator(fAttributes.values().iterator());
  }
  
  public Attribute getAttributeByName(QName paramQName)
  {
    return (Attribute)fAttributes.get(paramQName);
  }
  
  public NamespaceContext getNamespaceContext()
  {
    return fNamespaceContext;
  }
  
  public String getNamespaceURI(String paramString)
  {
    return fNamespaceContext.getNamespaceURI(paramString);
  }
  
  public void writeAsEncodedUnicode(Writer paramWriter)
    throws XMLStreamException
  {
    try
    {
      paramWriter.write(60);
      QName localQName = getName();
      String str = localQName.getPrefix();
      if ((str != null) && (str.length() > 0))
      {
        paramWriter.write(str);
        paramWriter.write(58);
      }
      paramWriter.write(localQName.getLocalPart());
      Iterator localIterator = getNamespaces();
      while (localIterator.hasNext())
      {
        localObject = (Namespace)localIterator.next();
        paramWriter.write(32);
        ((Namespace)localObject).writeAsEncodedUnicode(paramWriter);
      }
      Object localObject = getAttributes();
      while (((Iterator)localObject).hasNext())
      {
        Attribute localAttribute = (Attribute)((Iterator)localObject).next();
        paramWriter.write(32);
        localAttribute.writeAsEncodedUnicode(paramWriter);
      }
      paramWriter.write(62);
    }
    catch (IOException localIOException)
    {
      throw new XMLStreamException(localIOException);
    }
  }
}
