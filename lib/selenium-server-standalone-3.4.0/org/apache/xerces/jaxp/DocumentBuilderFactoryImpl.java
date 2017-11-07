package org.apache.xerces.jaxp;

import java.util.Hashtable;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import org.apache.xerces.parsers.DOMParser;
import org.apache.xerces.util.SAXMessageFormatter;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

public class DocumentBuilderFactoryImpl
  extends DocumentBuilderFactory
{
  private static final String NAMESPACES_FEATURE = "http://xml.org/sax/features/namespaces";
  private static final String VALIDATION_FEATURE = "http://xml.org/sax/features/validation";
  private static final String XINCLUDE_FEATURE = "http://apache.org/xml/features/xinclude";
  private static final String INCLUDE_IGNORABLE_WHITESPACE = "http://apache.org/xml/features/dom/include-ignorable-whitespace";
  private static final String CREATE_ENTITY_REF_NODES_FEATURE = "http://apache.org/xml/features/dom/create-entity-ref-nodes";
  private static final String INCLUDE_COMMENTS_FEATURE = "http://apache.org/xml/features/include-comments";
  private static final String CREATE_CDATA_NODES_FEATURE = "http://apache.org/xml/features/create-cdata-nodes";
  private Hashtable attributes;
  private Hashtable features;
  private Schema grammar;
  private boolean isXIncludeAware;
  private boolean fSecureProcess = false;
  
  public DocumentBuilderFactoryImpl() {}
  
  public DocumentBuilder newDocumentBuilder()
    throws ParserConfigurationException
  {
    if ((grammar != null) && (attributes != null))
    {
      if (attributes.containsKey("http://java.sun.com/xml/jaxp/properties/schemaLanguage")) {
        throw new ParserConfigurationException(SAXMessageFormatter.formatMessage(null, "schema-already-specified", new Object[] { "http://java.sun.com/xml/jaxp/properties/schemaLanguage" }));
      }
      if (attributes.containsKey("http://java.sun.com/xml/jaxp/properties/schemaSource")) {
        throw new ParserConfigurationException(SAXMessageFormatter.formatMessage(null, "schema-already-specified", new Object[] { "http://java.sun.com/xml/jaxp/properties/schemaSource" }));
      }
    }
    try
    {
      return new DocumentBuilderImpl(this, attributes, features, fSecureProcess);
    }
    catch (SAXException localSAXException)
    {
      throw new ParserConfigurationException(localSAXException.getMessage());
    }
  }
  
  public void setAttribute(String paramString, Object paramObject)
    throws IllegalArgumentException
  {
    if (paramObject == null)
    {
      if (attributes != null) {
        attributes.remove(paramString);
      }
      return;
    }
    if (attributes == null) {
      attributes = new Hashtable();
    }
    attributes.put(paramString, paramObject);
    try
    {
      new DocumentBuilderImpl(this, attributes, features);
    }
    catch (Exception localException)
    {
      attributes.remove(paramString);
      throw new IllegalArgumentException(localException.getMessage());
    }
  }
  
  public Object getAttribute(String paramString)
    throws IllegalArgumentException
  {
    if (attributes != null)
    {
      localObject = attributes.get(paramString);
      if (localObject != null) {
        return localObject;
      }
    }
    Object localObject = null;
    try
    {
      localObject = new DocumentBuilderImpl(this, attributes, features).getDOMParser();
      return ((DOMParser)localObject).getProperty(paramString);
    }
    catch (SAXException localSAXException1)
    {
      try
      {
        boolean bool = ((DOMParser)localObject).getFeature(paramString);
        return bool ? Boolean.TRUE : Boolean.FALSE;
      }
      catch (SAXException localSAXException2)
      {
        throw new IllegalArgumentException(localSAXException1.getMessage());
      }
    }
  }
  
  public Schema getSchema()
  {
    return grammar;
  }
  
  public void setSchema(Schema paramSchema)
  {
    grammar = paramSchema;
  }
  
  public boolean isXIncludeAware()
  {
    return isXIncludeAware;
  }
  
  public void setXIncludeAware(boolean paramBoolean)
  {
    isXIncludeAware = paramBoolean;
  }
  
  public boolean getFeature(String paramString)
    throws ParserConfigurationException
  {
    if (paramString.equals("http://javax.xml.XMLConstants/feature/secure-processing")) {
      return fSecureProcess;
    }
    if (paramString.equals("http://xml.org/sax/features/namespaces")) {
      return isNamespaceAware();
    }
    if (paramString.equals("http://xml.org/sax/features/validation")) {
      return isValidating();
    }
    if (paramString.equals("http://apache.org/xml/features/xinclude")) {
      return isXIncludeAware();
    }
    if (paramString.equals("http://apache.org/xml/features/dom/include-ignorable-whitespace")) {
      return !isIgnoringElementContentWhitespace();
    }
    if (paramString.equals("http://apache.org/xml/features/dom/create-entity-ref-nodes")) {
      return !isExpandEntityReferences();
    }
    if (paramString.equals("http://apache.org/xml/features/include-comments")) {
      return !isIgnoringComments();
    }
    if (paramString.equals("http://apache.org/xml/features/create-cdata-nodes")) {
      return !isCoalescing();
    }
    Object localObject;
    if (features != null)
    {
      localObject = features.get(paramString);
      if (localObject != null) {
        return ((Boolean)localObject).booleanValue();
      }
    }
    try
    {
      localObject = new DocumentBuilderImpl(this, attributes, features).getDOMParser();
      return ((DOMParser)localObject).getFeature(paramString);
    }
    catch (SAXException localSAXException)
    {
      throw new ParserConfigurationException(localSAXException.getMessage());
    }
  }
  
  public void setFeature(String paramString, boolean paramBoolean)
    throws ParserConfigurationException
  {
    if (paramString.equals("http://javax.xml.XMLConstants/feature/secure-processing"))
    {
      fSecureProcess = paramBoolean;
      return;
    }
    if (paramString.equals("http://xml.org/sax/features/namespaces"))
    {
      setNamespaceAware(paramBoolean);
      return;
    }
    if (paramString.equals("http://xml.org/sax/features/validation"))
    {
      setValidating(paramBoolean);
      return;
    }
    if (paramString.equals("http://apache.org/xml/features/xinclude"))
    {
      setXIncludeAware(paramBoolean);
      return;
    }
    if (paramString.equals("http://apache.org/xml/features/dom/include-ignorable-whitespace"))
    {
      setIgnoringElementContentWhitespace(!paramBoolean);
      return;
    }
    if (paramString.equals("http://apache.org/xml/features/dom/create-entity-ref-nodes"))
    {
      setExpandEntityReferences(!paramBoolean);
      return;
    }
    if (paramString.equals("http://apache.org/xml/features/include-comments"))
    {
      setIgnoringComments(!paramBoolean);
      return;
    }
    if (paramString.equals("http://apache.org/xml/features/create-cdata-nodes"))
    {
      setCoalescing(!paramBoolean);
      return;
    }
    if (features == null) {
      features = new Hashtable();
    }
    features.put(paramString, paramBoolean ? Boolean.TRUE : Boolean.FALSE);
    try
    {
      new DocumentBuilderImpl(this, attributes, features);
    }
    catch (SAXNotSupportedException localSAXNotSupportedException)
    {
      features.remove(paramString);
      throw new ParserConfigurationException(localSAXNotSupportedException.getMessage());
    }
    catch (SAXNotRecognizedException localSAXNotRecognizedException)
    {
      features.remove(paramString);
      throw new ParserConfigurationException(localSAXNotRecognizedException.getMessage());
    }
  }
}
