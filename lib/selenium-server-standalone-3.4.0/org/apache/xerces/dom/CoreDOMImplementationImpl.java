package org.apache.xerces.dom;

import java.lang.ref.SoftReference;
import org.apache.xerces.impl.RevalidationHandler;
import org.apache.xerces.impl.dtd.XMLDTDLoader;
import org.apache.xerces.parsers.DOMParserImpl;
import org.apache.xerces.util.XMLChar;
import org.apache.xml.serialize.DOMSerializerImpl;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSParser;
import org.w3c.dom.ls.LSSerializer;

public class CoreDOMImplementationImpl
  implements DOMImplementation, DOMImplementationLS
{
  private static final int SIZE = 2;
  private SoftReference[] schemaValidators = new SoftReference[2];
  private SoftReference[] xml10DTDValidators = new SoftReference[2];
  private SoftReference[] xml11DTDValidators = new SoftReference[2];
  private int freeSchemaValidatorIndex = -1;
  private int freeXML10DTDValidatorIndex = -1;
  private int freeXML11DTDValidatorIndex = -1;
  private int schemaValidatorsCurrentSize = 2;
  private int xml10DTDValidatorsCurrentSize = 2;
  private int xml11DTDValidatorsCurrentSize = 2;
  private SoftReference[] xml10DTDLoaders = new SoftReference[2];
  private SoftReference[] xml11DTDLoaders = new SoftReference[2];
  private int freeXML10DTDLoaderIndex = -1;
  private int freeXML11DTDLoaderIndex = -1;
  private int xml10DTDLoaderCurrentSize = 2;
  private int xml11DTDLoaderCurrentSize = 2;
  private int docAndDoctypeCounter = 0;
  static final CoreDOMImplementationImpl singleton = new CoreDOMImplementationImpl();
  
  public CoreDOMImplementationImpl() {}
  
  public static DOMImplementation getDOMImplementation()
  {
    return singleton;
  }
  
  public boolean hasFeature(String paramString1, String paramString2)
  {
    int i = (paramString2 == null) || (paramString2.length() == 0) ? 1 : 0;
    if ((paramString1.equalsIgnoreCase("+XPath")) && ((i != 0) || (paramString2.equals("3.0"))))
    {
      try
      {
        Class localClass = ObjectFactory.findProviderClass("org.apache.xpath.domapi.XPathEvaluatorImpl", ObjectFactory.findClassLoader(), true);
        Class[] arrayOfClass = localClass.getInterfaces();
        for (int j = 0; j < arrayOfClass.length; j++) {
          if (arrayOfClass[j].getName().equals("org.w3c.dom.xpath.XPathEvaluator")) {
            return true;
          }
        }
      }
      catch (Exception localException)
      {
        return false;
      }
      return true;
    }
    if (paramString1.startsWith("+")) {
      paramString1 = paramString1.substring(1);
    }
    return ((paramString1.equalsIgnoreCase("Core")) && ((i != 0) || (paramString2.equals("1.0")) || (paramString2.equals("2.0")) || (paramString2.equals("3.0")))) || ((paramString1.equalsIgnoreCase("XML")) && ((i != 0) || (paramString2.equals("1.0")) || (paramString2.equals("2.0")) || (paramString2.equals("3.0")))) || ((paramString1.equalsIgnoreCase("XMLVersion")) && ((i != 0) || (paramString2.equals("1.0")) || (paramString2.equals("1.1")))) || ((paramString1.equalsIgnoreCase("LS")) && ((i != 0) || (paramString2.equals("3.0")))) || ((paramString1.equalsIgnoreCase("ElementTraversal")) && ((i != 0) || (paramString2.equals("1.0"))));
  }
  
  public DocumentType createDocumentType(String paramString1, String paramString2, String paramString3)
  {
    checkQName(paramString1);
    return new DocumentTypeImpl(null, paramString1, paramString2, paramString3);
  }
  
  final void checkQName(String paramString)
  {
    int i = paramString.indexOf(':');
    int j = paramString.lastIndexOf(':');
    int k = paramString.length();
    if ((i == 0) || (i == k - 1) || (j != i))
    {
      String str1 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NAMESPACE_ERR", null);
      throw new DOMException((short)14, str1);
    }
    int m = 0;
    String str4;
    if (i > 0)
    {
      if (!XMLChar.isNCNameStart(paramString.charAt(m)))
      {
        String str2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_CHARACTER_ERR", null);
        throw new DOMException((short)5, str2);
      }
      for (int n = 1; n < i; n++) {
        if (!XMLChar.isNCName(paramString.charAt(n)))
        {
          str4 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_CHARACTER_ERR", null);
          throw new DOMException((short)5, str4);
        }
      }
      m = i + 1;
    }
    if (!XMLChar.isNCNameStart(paramString.charAt(m)))
    {
      String str3 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_CHARACTER_ERR", null);
      throw new DOMException((short)5, str3);
    }
    for (int i1 = m + 1; i1 < k; i1++) {
      if (!XMLChar.isNCName(paramString.charAt(i1)))
      {
        str4 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_CHARACTER_ERR", null);
        throw new DOMException((short)5, str4);
      }
    }
  }
  
  public Document createDocument(String paramString1, String paramString2, DocumentType paramDocumentType)
    throws DOMException
  {
    if ((paramDocumentType != null) && (paramDocumentType.getOwnerDocument() != null))
    {
      localObject = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", null);
      throw new DOMException((short)4, (String)localObject);
    }
    Object localObject = createDocument(paramDocumentType);
    if ((paramString2 != null) || (paramString1 != null))
    {
      Element localElement = ((CoreDocumentImpl)localObject).createElementNS(paramString1, paramString2);
      ((CoreDocumentImpl)localObject).appendChild(localElement);
    }
    return localObject;
  }
  
  protected CoreDocumentImpl createDocument(DocumentType paramDocumentType)
  {
    return new CoreDocumentImpl(paramDocumentType);
  }
  
  public Object getFeature(String paramString1, String paramString2)
  {
    if (singleton.hasFeature(paramString1, paramString2)) {
      if (paramString1.equalsIgnoreCase("+XPath")) {
        try
        {
          Class localClass = ObjectFactory.findProviderClass("org.apache.xpath.domapi.XPathEvaluatorImpl", ObjectFactory.findClassLoader(), true);
          Class[] arrayOfClass = localClass.getInterfaces();
          for (int i = 0; i < arrayOfClass.length; i++) {
            if (arrayOfClass[i].getName().equals("org.w3c.dom.xpath.XPathEvaluator")) {
              return localClass.newInstance();
            }
          }
        }
        catch (Exception localException)
        {
          return null;
        }
      } else {
        return singleton;
      }
    }
    return null;
  }
  
  public LSParser createLSParser(short paramShort, String paramString)
    throws DOMException
  {
    if ((paramShort != 1) || ((paramString != null) && (!"http://www.w3.org/2001/XMLSchema".equals(paramString)) && (!"http://www.w3.org/TR/REC-xml".equals(paramString))))
    {
      String str = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", null);
      throw new DOMException((short)9, str);
    }
    if ((paramString != null) && (paramString.equals("http://www.w3.org/TR/REC-xml"))) {
      return new DOMParserImpl("org.apache.xerces.parsers.DTDConfiguration", paramString);
    }
    return new DOMParserImpl("org.apache.xerces.parsers.XIncludeAwareParserConfiguration", paramString);
  }
  
  public LSSerializer createLSSerializer()
  {
    try
    {
      Class localClass = ObjectFactory.findProviderClass("org.apache.xml.serializer.dom3.LSSerializerImpl", ObjectFactory.findClassLoader(), true);
      return (LSSerializer)localClass.newInstance();
    }
    catch (Exception localException) {}
    return new DOMSerializerImpl();
  }
  
  public LSInput createLSInput()
  {
    return new DOMInputImpl();
  }
  
  synchronized RevalidationHandler getValidator(String paramString1, String paramString2)
  {
    SoftReference localSoftReference;
    RevalidationHandlerHolder localRevalidationHandlerHolder;
    RevalidationHandler localRevalidationHandler;
    if (paramString1 == "http://www.w3.org/2001/XMLSchema")
    {
      while (freeSchemaValidatorIndex >= 0)
      {
        localSoftReference = schemaValidators[freeSchemaValidatorIndex];
        localRevalidationHandlerHolder = (RevalidationHandlerHolder)localSoftReference.get();
        if ((localRevalidationHandlerHolder != null) && (handler != null))
        {
          localRevalidationHandler = handler;
          handler = null;
          freeSchemaValidatorIndex -= 1;
          return localRevalidationHandler;
        }
        schemaValidators[(freeSchemaValidatorIndex--)] = null;
      }
      return (RevalidationHandler)ObjectFactory.newInstance("org.apache.xerces.impl.xs.XMLSchemaValidator", ObjectFactory.findClassLoader(), true);
    }
    if (paramString1 == "http://www.w3.org/TR/REC-xml")
    {
      if ("1.1".equals(paramString2))
      {
        while (freeXML11DTDValidatorIndex >= 0)
        {
          localSoftReference = xml11DTDValidators[freeXML11DTDValidatorIndex];
          localRevalidationHandlerHolder = (RevalidationHandlerHolder)localSoftReference.get();
          if ((localRevalidationHandlerHolder != null) && (handler != null))
          {
            localRevalidationHandler = handler;
            handler = null;
            freeXML11DTDValidatorIndex -= 1;
            return localRevalidationHandler;
          }
          xml11DTDValidators[(freeXML11DTDValidatorIndex--)] = null;
        }
        return (RevalidationHandler)ObjectFactory.newInstance("org.apache.xerces.impl.dtd.XML11DTDValidator", ObjectFactory.findClassLoader(), true);
      }
      while (freeXML10DTDValidatorIndex >= 0)
      {
        localSoftReference = xml10DTDValidators[freeXML10DTDValidatorIndex];
        localRevalidationHandlerHolder = (RevalidationHandlerHolder)localSoftReference.get();
        if ((localRevalidationHandlerHolder != null) && (handler != null))
        {
          localRevalidationHandler = handler;
          handler = null;
          freeXML10DTDValidatorIndex -= 1;
          return localRevalidationHandler;
        }
        xml10DTDValidators[(freeXML10DTDValidatorIndex--)] = null;
      }
      return (RevalidationHandler)ObjectFactory.newInstance("org.apache.xerces.impl.dtd.XMLDTDValidator", ObjectFactory.findClassLoader(), true);
    }
    return null;
  }
  
  synchronized void releaseValidator(String paramString1, String paramString2, RevalidationHandler paramRevalidationHandler)
  {
    Object localObject;
    RevalidationHandlerHolder localRevalidationHandlerHolder;
    if (paramString1 == "http://www.w3.org/2001/XMLSchema")
    {
      freeSchemaValidatorIndex += 1;
      if (schemaValidators.length == freeSchemaValidatorIndex)
      {
        schemaValidatorsCurrentSize += 2;
        localObject = new SoftReference[schemaValidatorsCurrentSize];
        System.arraycopy(schemaValidators, 0, localObject, 0, schemaValidators.length);
        schemaValidators = ((SoftReference[])localObject);
      }
      localObject = schemaValidators[freeSchemaValidatorIndex];
      if (localObject != null)
      {
        localRevalidationHandlerHolder = (RevalidationHandlerHolder)((SoftReference)localObject).get();
        if (localRevalidationHandlerHolder != null)
        {
          handler = paramRevalidationHandler;
          return;
        }
      }
      schemaValidators[freeSchemaValidatorIndex] = new SoftReference(new RevalidationHandlerHolder(paramRevalidationHandler));
    }
    else if (paramString1 == "http://www.w3.org/TR/REC-xml")
    {
      if ("1.1".equals(paramString2))
      {
        freeXML11DTDValidatorIndex += 1;
        if (xml11DTDValidators.length == freeXML11DTDValidatorIndex)
        {
          xml11DTDValidatorsCurrentSize += 2;
          localObject = new SoftReference[xml11DTDValidatorsCurrentSize];
          System.arraycopy(xml11DTDValidators, 0, localObject, 0, xml11DTDValidators.length);
          xml11DTDValidators = ((SoftReference[])localObject);
        }
        localObject = xml11DTDValidators[freeXML11DTDValidatorIndex];
        if (localObject != null)
        {
          localRevalidationHandlerHolder = (RevalidationHandlerHolder)((SoftReference)localObject).get();
          if (localRevalidationHandlerHolder != null)
          {
            handler = paramRevalidationHandler;
            return;
          }
        }
        xml11DTDValidators[freeXML11DTDValidatorIndex] = new SoftReference(new RevalidationHandlerHolder(paramRevalidationHandler));
      }
      else
      {
        freeXML10DTDValidatorIndex += 1;
        if (xml10DTDValidators.length == freeXML10DTDValidatorIndex)
        {
          xml10DTDValidatorsCurrentSize += 2;
          localObject = new SoftReference[xml10DTDValidatorsCurrentSize];
          System.arraycopy(xml10DTDValidators, 0, localObject, 0, xml10DTDValidators.length);
          xml10DTDValidators = ((SoftReference[])localObject);
        }
        localObject = xml10DTDValidators[freeXML10DTDValidatorIndex];
        if (localObject != null)
        {
          localRevalidationHandlerHolder = (RevalidationHandlerHolder)((SoftReference)localObject).get();
          if (localRevalidationHandlerHolder != null)
          {
            handler = paramRevalidationHandler;
            return;
          }
        }
        xml10DTDValidators[freeXML10DTDValidatorIndex] = new SoftReference(new RevalidationHandlerHolder(paramRevalidationHandler));
      }
    }
  }
  
  final synchronized XMLDTDLoader getDTDLoader(String paramString)
  {
    if ("1.1".equals(paramString))
    {
      while (freeXML11DTDLoaderIndex >= 0)
      {
        localSoftReference = xml11DTDLoaders[freeXML11DTDLoaderIndex];
        localXMLDTDLoaderHolder = (XMLDTDLoaderHolder)localSoftReference.get();
        if ((localXMLDTDLoaderHolder != null) && (loader != null))
        {
          localXMLDTDLoader = loader;
          loader = null;
          freeXML11DTDLoaderIndex -= 1;
          return localXMLDTDLoader;
        }
        xml11DTDLoaders[(freeXML11DTDLoaderIndex--)] = null;
      }
      return (XMLDTDLoader)ObjectFactory.newInstance("org.apache.xerces.impl.dtd.XML11DTDProcessor", ObjectFactory.findClassLoader(), true);
    }
    while (freeXML10DTDLoaderIndex >= 0)
    {
      XMLDTDLoader localXMLDTDLoader;
      SoftReference localSoftReference = xml10DTDLoaders[freeXML10DTDLoaderIndex];
      XMLDTDLoaderHolder localXMLDTDLoaderHolder = (XMLDTDLoaderHolder)localSoftReference.get();
      if ((localXMLDTDLoaderHolder != null) && (loader != null))
      {
        localXMLDTDLoader = loader;
        loader = null;
        freeXML10DTDLoaderIndex -= 1;
        return localXMLDTDLoader;
      }
      xml10DTDLoaders[(freeXML10DTDLoaderIndex--)] = null;
    }
    return new XMLDTDLoader();
  }
  
  final synchronized void releaseDTDLoader(String paramString, XMLDTDLoader paramXMLDTDLoader)
  {
    Object localObject;
    XMLDTDLoaderHolder localXMLDTDLoaderHolder;
    if ("1.1".equals(paramString))
    {
      freeXML11DTDLoaderIndex += 1;
      if (xml11DTDLoaders.length == freeXML11DTDLoaderIndex)
      {
        xml11DTDLoaderCurrentSize += 2;
        localObject = new SoftReference[xml11DTDLoaderCurrentSize];
        System.arraycopy(xml11DTDLoaders, 0, localObject, 0, xml11DTDLoaders.length);
        xml11DTDLoaders = ((SoftReference[])localObject);
      }
      localObject = xml11DTDLoaders[freeXML11DTDLoaderIndex];
      if (localObject != null)
      {
        localXMLDTDLoaderHolder = (XMLDTDLoaderHolder)((SoftReference)localObject).get();
        if (localXMLDTDLoaderHolder != null)
        {
          loader = paramXMLDTDLoader;
          return;
        }
      }
      xml11DTDLoaders[freeXML11DTDLoaderIndex] = new SoftReference(new XMLDTDLoaderHolder(paramXMLDTDLoader));
    }
    else
    {
      freeXML10DTDLoaderIndex += 1;
      if (xml10DTDLoaders.length == freeXML10DTDLoaderIndex)
      {
        xml10DTDLoaderCurrentSize += 2;
        localObject = new SoftReference[xml10DTDLoaderCurrentSize];
        System.arraycopy(xml10DTDLoaders, 0, localObject, 0, xml10DTDLoaders.length);
        xml10DTDLoaders = ((SoftReference[])localObject);
      }
      localObject = xml10DTDLoaders[freeXML10DTDLoaderIndex];
      if (localObject != null)
      {
        localXMLDTDLoaderHolder = (XMLDTDLoaderHolder)((SoftReference)localObject).get();
        if (localXMLDTDLoaderHolder != null)
        {
          loader = paramXMLDTDLoader;
          return;
        }
      }
      xml10DTDLoaders[freeXML10DTDLoaderIndex] = new SoftReference(new XMLDTDLoaderHolder(paramXMLDTDLoader));
    }
  }
  
  protected synchronized int assignDocumentNumber()
  {
    return ++docAndDoctypeCounter;
  }
  
  protected synchronized int assignDocTypeNumber()
  {
    return ++docAndDoctypeCounter;
  }
  
  public LSOutput createLSOutput()
  {
    return new DOMOutputImpl();
  }
  
  static final class XMLDTDLoaderHolder
  {
    XMLDTDLoader loader;
    
    XMLDTDLoaderHolder(XMLDTDLoader paramXMLDTDLoader)
    {
      loader = paramXMLDTDLoader;
    }
  }
  
  static final class RevalidationHandlerHolder
  {
    RevalidationHandler handler;
    
    RevalidationHandlerHolder(RevalidationHandler paramRevalidationHandler)
    {
      handler = paramRevalidationHandler;
    }
  }
}
