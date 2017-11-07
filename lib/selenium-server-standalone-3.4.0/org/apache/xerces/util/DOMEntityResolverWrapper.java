package org.apache.xerces.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.grammars.XMLGrammarDescription;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

public class DOMEntityResolverWrapper
  implements XMLEntityResolver
{
  private static final String XML_TYPE = "http://www.w3.org/TR/REC-xml";
  private static final String XSD_TYPE = "http://www.w3.org/2001/XMLSchema";
  protected LSResourceResolver fEntityResolver;
  
  public DOMEntityResolverWrapper() {}
  
  public DOMEntityResolverWrapper(LSResourceResolver paramLSResourceResolver)
  {
    setEntityResolver(paramLSResourceResolver);
  }
  
  public void setEntityResolver(LSResourceResolver paramLSResourceResolver)
  {
    fEntityResolver = paramLSResourceResolver;
  }
  
  public LSResourceResolver getEntityResolver()
  {
    return fEntityResolver;
  }
  
  public XMLInputSource resolveEntity(XMLResourceIdentifier paramXMLResourceIdentifier)
    throws XNIException, IOException
  {
    if (fEntityResolver != null)
    {
      LSInput localLSInput = paramXMLResourceIdentifier == null ? fEntityResolver.resolveResource(null, null, null, null, null) : fEntityResolver.resolveResource(getType(paramXMLResourceIdentifier), paramXMLResourceIdentifier.getNamespace(), paramXMLResourceIdentifier.getPublicId(), paramXMLResourceIdentifier.getLiteralSystemId(), paramXMLResourceIdentifier.getBaseSystemId());
      if (localLSInput != null)
      {
        String str1 = localLSInput.getPublicId();
        String str2 = localLSInput.getSystemId();
        String str3 = localLSInput.getBaseURI();
        InputStream localInputStream = localLSInput.getByteStream();
        Reader localReader = localLSInput.getCharacterStream();
        String str4 = localLSInput.getEncoding();
        String str5 = localLSInput.getStringData();
        XMLInputSource localXMLInputSource = new XMLInputSource(str1, str2, str3);
        if (localReader != null) {
          localXMLInputSource.setCharacterStream(localReader);
        } else if (localInputStream != null) {
          localXMLInputSource.setByteStream(localInputStream);
        } else if ((str5 != null) && (str5.length() != 0)) {
          localXMLInputSource.setCharacterStream(new StringReader(str5));
        }
        localXMLInputSource.setEncoding(str4);
        return localXMLInputSource;
      }
    }
    return null;
  }
  
  private String getType(XMLResourceIdentifier paramXMLResourceIdentifier)
  {
    if ((paramXMLResourceIdentifier instanceof XMLGrammarDescription))
    {
      XMLGrammarDescription localXMLGrammarDescription = (XMLGrammarDescription)paramXMLResourceIdentifier;
      if ("http://www.w3.org/2001/XMLSchema".equals(localXMLGrammarDescription.getGrammarType())) {
        return "http://www.w3.org/2001/XMLSchema";
      }
    }
    return "http://www.w3.org/TR/REC-xml";
  }
}
