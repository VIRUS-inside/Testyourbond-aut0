package org.apache.xerces.util;

import java.io.IOException;
import javax.xml.parsers.SAXParserFactory;
import org.apache.xerces.dom.DOMInputImpl;
import org.apache.xerces.jaxp.SAXParserFactoryImpl;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.apache.xml.resolver.Catalog;
import org.apache.xml.resolver.CatalogManager;
import org.apache.xml.resolver.readers.SAXCatalogReader;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.ext.EntityResolver2;

public class XMLCatalogResolver
  implements XMLEntityResolver, EntityResolver2, LSResourceResolver
{
  private CatalogManager fResolverCatalogManager = null;
  private Catalog fCatalog = null;
  private String[] fCatalogsList = null;
  private boolean fCatalogsChanged = true;
  private boolean fPreferPublic = true;
  private boolean fUseLiteralSystemId = true;
  
  public XMLCatalogResolver()
  {
    this(null, true);
  }
  
  public XMLCatalogResolver(String[] paramArrayOfString)
  {
    this(paramArrayOfString, true);
  }
  
  public XMLCatalogResolver(String[] paramArrayOfString, boolean paramBoolean)
  {
    init(paramArrayOfString, paramBoolean);
  }
  
  public final synchronized String[] getCatalogList()
  {
    return fCatalogsList != null ? (String[])fCatalogsList.clone() : null;
  }
  
  public final synchronized void setCatalogList(String[] paramArrayOfString)
  {
    fCatalogsChanged = true;
    fCatalogsList = (paramArrayOfString != null ? (String[])paramArrayOfString.clone() : null);
  }
  
  public final synchronized void clear()
  {
    fCatalog = null;
  }
  
  public final boolean getPreferPublic()
  {
    return fPreferPublic;
  }
  
  public final void setPreferPublic(boolean paramBoolean)
  {
    fPreferPublic = paramBoolean;
    fResolverCatalogManager.setPreferPublic(paramBoolean);
  }
  
  public final boolean getUseLiteralSystemId()
  {
    return fUseLiteralSystemId;
  }
  
  public final void setUseLiteralSystemId(boolean paramBoolean)
  {
    fUseLiteralSystemId = paramBoolean;
  }
  
  public InputSource resolveEntity(String paramString1, String paramString2)
    throws SAXException, IOException
  {
    String str = null;
    if ((paramString1 != null) && (paramString2 != null)) {
      str = resolvePublic(paramString1, paramString2);
    } else if (paramString2 != null) {
      str = resolveSystem(paramString2);
    }
    if (str != null)
    {
      InputSource localInputSource = new InputSource(str);
      localInputSource.setPublicId(paramString1);
      return localInputSource;
    }
    return null;
  }
  
  public InputSource resolveEntity(String paramString1, String paramString2, String paramString3, String paramString4)
    throws SAXException, IOException
  {
    String str = null;
    if ((!getUseLiteralSystemId()) && (paramString3 != null)) {
      try
      {
        URI localURI = new URI(new URI(paramString3), paramString4);
        paramString4 = localURI.toString();
      }
      catch (URI.MalformedURIException localMalformedURIException) {}
    }
    if ((paramString2 != null) && (paramString4 != null)) {
      str = resolvePublic(paramString2, paramString4);
    } else if (paramString4 != null) {
      str = resolveSystem(paramString4);
    }
    if (str != null)
    {
      InputSource localInputSource = new InputSource(str);
      localInputSource.setPublicId(paramString2);
      return localInputSource;
    }
    return null;
  }
  
  public InputSource getExternalSubset(String paramString1, String paramString2)
    throws SAXException, IOException
  {
    return null;
  }
  
  public LSInput resolveResource(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
  {
    String str = null;
    try
    {
      if (paramString2 != null) {
        str = resolveURI(paramString2);
      }
      if ((!getUseLiteralSystemId()) && (paramString5 != null)) {
        try
        {
          URI localURI = new URI(new URI(paramString5), paramString4);
          paramString4 = localURI.toString();
        }
        catch (URI.MalformedURIException localMalformedURIException) {}
      }
      if (str == null) {
        if ((paramString3 != null) && (paramString4 != null)) {
          str = resolvePublic(paramString3, paramString4);
        } else if (paramString4 != null) {
          str = resolveSystem(paramString4);
        }
      }
    }
    catch (IOException localIOException) {}
    if (str != null) {
      return new DOMInputImpl(paramString3, str, paramString5);
    }
    return null;
  }
  
  public XMLInputSource resolveEntity(XMLResourceIdentifier paramXMLResourceIdentifier)
    throws XNIException, IOException
  {
    String str = resolveIdentifier(paramXMLResourceIdentifier);
    if (str != null) {
      return new XMLInputSource(paramXMLResourceIdentifier.getPublicId(), str, paramXMLResourceIdentifier.getBaseSystemId());
    }
    return null;
  }
  
  public String resolveIdentifier(XMLResourceIdentifier paramXMLResourceIdentifier)
    throws IOException, XNIException
  {
    String str1 = null;
    String str2 = paramXMLResourceIdentifier.getNamespace();
    if (str2 != null) {
      str1 = resolveURI(str2);
    }
    if (str1 == null)
    {
      String str3 = paramXMLResourceIdentifier.getPublicId();
      String str4 = getUseLiteralSystemId() ? paramXMLResourceIdentifier.getLiteralSystemId() : paramXMLResourceIdentifier.getExpandedSystemId();
      if ((str3 != null) && (str4 != null)) {
        str1 = resolvePublic(str3, str4);
      } else if (str4 != null) {
        str1 = resolveSystem(str4);
      }
    }
    return str1;
  }
  
  public final synchronized String resolveSystem(String paramString)
    throws IOException
  {
    if (fCatalogsChanged)
    {
      parseCatalogs();
      fCatalogsChanged = false;
    }
    return fCatalog != null ? fCatalog.resolveSystem(paramString) : null;
  }
  
  public final synchronized String resolvePublic(String paramString1, String paramString2)
    throws IOException
  {
    if (fCatalogsChanged)
    {
      parseCatalogs();
      fCatalogsChanged = false;
    }
    return fCatalog != null ? fCatalog.resolvePublic(paramString1, paramString2) : null;
  }
  
  public final synchronized String resolveURI(String paramString)
    throws IOException
  {
    if (fCatalogsChanged)
    {
      parseCatalogs();
      fCatalogsChanged = false;
    }
    return fCatalog != null ? fCatalog.resolveURI(paramString) : null;
  }
  
  private void init(String[] paramArrayOfString, boolean paramBoolean)
  {
    fCatalogsList = (paramArrayOfString != null ? (String[])paramArrayOfString.clone() : null);
    fPreferPublic = paramBoolean;
    fResolverCatalogManager = new CatalogManager();
    fResolverCatalogManager.setAllowOasisXMLCatalogPI(false);
    fResolverCatalogManager.setCatalogClassName("org.apache.xml.resolver.Catalog");
    fResolverCatalogManager.setCatalogFiles("");
    fResolverCatalogManager.setIgnoreMissingProperties(true);
    fResolverCatalogManager.setPreferPublic(fPreferPublic);
    fResolverCatalogManager.setRelativeCatalogs(false);
    fResolverCatalogManager.setUseStaticCatalog(false);
    fResolverCatalogManager.setVerbosity(0);
  }
  
  private void parseCatalogs()
    throws IOException
  {
    if (fCatalogsList != null)
    {
      fCatalog = new Catalog(fResolverCatalogManager);
      attachReaderToCatalog(fCatalog);
      for (int i = 0; i < fCatalogsList.length; i++)
      {
        String str = fCatalogsList[i];
        if ((str != null) && (str.length() > 0)) {
          fCatalog.parseCatalog(str);
        }
      }
    }
    else
    {
      fCatalog = null;
    }
  }
  
  private void attachReaderToCatalog(Catalog paramCatalog)
  {
    SAXParserFactoryImpl localSAXParserFactoryImpl = new SAXParserFactoryImpl();
    localSAXParserFactoryImpl.setNamespaceAware(true);
    localSAXParserFactoryImpl.setValidating(false);
    SAXCatalogReader localSAXCatalogReader = new SAXCatalogReader(localSAXParserFactoryImpl);
    localSAXCatalogReader.setCatalogParser("urn:oasis:names:tc:entity:xmlns:xml:catalog", "catalog", "org.apache.xml.resolver.readers.OASISXMLCatalogReader");
    paramCatalog.addReader("application/xml", localSAXCatalogReader);
  }
}
