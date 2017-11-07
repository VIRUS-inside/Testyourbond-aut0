package org.apache.xerces.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class EntityResolverWrapper
  implements XMLEntityResolver
{
  protected EntityResolver fEntityResolver;
  
  public EntityResolverWrapper() {}
  
  public EntityResolverWrapper(EntityResolver paramEntityResolver)
  {
    setEntityResolver(paramEntityResolver);
  }
  
  public void setEntityResolver(EntityResolver paramEntityResolver)
  {
    fEntityResolver = paramEntityResolver;
  }
  
  public EntityResolver getEntityResolver()
  {
    return fEntityResolver;
  }
  
  public XMLInputSource resolveEntity(XMLResourceIdentifier paramXMLResourceIdentifier)
    throws XNIException, IOException
  {
    String str1 = paramXMLResourceIdentifier.getPublicId();
    String str2 = paramXMLResourceIdentifier.getExpandedSystemId();
    if ((str1 == null) && (str2 == null)) {
      return null;
    }
    if ((fEntityResolver != null) && (paramXMLResourceIdentifier != null)) {
      try
      {
        InputSource localInputSource = fEntityResolver.resolveEntity(str1, str2);
        if (localInputSource != null)
        {
          localObject = localInputSource.getPublicId();
          String str3 = localInputSource.getSystemId();
          String str4 = paramXMLResourceIdentifier.getBaseSystemId();
          InputStream localInputStream = localInputSource.getByteStream();
          Reader localReader = localInputSource.getCharacterStream();
          String str5 = localInputSource.getEncoding();
          XMLInputSource localXMLInputSource = new XMLInputSource((String)localObject, str3, str4);
          localXMLInputSource.setByteStream(localInputStream);
          localXMLInputSource.setCharacterStream(localReader);
          localXMLInputSource.setEncoding(str5);
          return localXMLInputSource;
        }
      }
      catch (SAXException localSAXException)
      {
        Object localObject = localSAXException.getException();
        if (localObject == null) {
          localObject = localSAXException;
        }
        throw new XNIException((Exception)localObject);
      }
    }
    return null;
  }
}
