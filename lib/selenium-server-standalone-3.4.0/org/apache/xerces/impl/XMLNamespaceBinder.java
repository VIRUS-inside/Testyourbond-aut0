package org.apache.xerces.impl;

import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLDocumentHandler;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLComponent;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLDocumentFilter;
import org.apache.xerces.xni.parser.XMLDocumentSource;

public class XMLNamespaceBinder
  implements XMLComponent, XMLDocumentFilter
{
  protected static final String NAMESPACES = "http://xml.org/sax/features/namespaces";
  protected static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
  protected static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
  private static final String[] RECOGNIZED_FEATURES = { "http://xml.org/sax/features/namespaces" };
  private static final Boolean[] FEATURE_DEFAULTS = { null };
  private static final String[] RECOGNIZED_PROPERTIES = { "http://apache.org/xml/properties/internal/symbol-table", "http://apache.org/xml/properties/internal/error-reporter" };
  private static final Object[] PROPERTY_DEFAULTS = { null, null };
  protected boolean fNamespaces;
  protected SymbolTable fSymbolTable;
  protected XMLErrorReporter fErrorReporter;
  protected XMLDocumentHandler fDocumentHandler;
  protected XMLDocumentSource fDocumentSource;
  protected boolean fOnlyPassPrefixMappingEvents;
  private NamespaceContext fNamespaceContext;
  private final QName fAttributeQName = new QName();
  
  public XMLNamespaceBinder() {}
  
  public void setOnlyPassPrefixMappingEvents(boolean paramBoolean)
  {
    fOnlyPassPrefixMappingEvents = paramBoolean;
  }
  
  public boolean getOnlyPassPrefixMappingEvents()
  {
    return fOnlyPassPrefixMappingEvents;
  }
  
  public void reset(XMLComponentManager paramXMLComponentManager)
    throws XNIException
  {
    try
    {
      fNamespaces = paramXMLComponentManager.getFeature("http://xml.org/sax/features/namespaces");
    }
    catch (XMLConfigurationException localXMLConfigurationException)
    {
      fNamespaces = true;
    }
    fSymbolTable = ((SymbolTable)paramXMLComponentManager.getProperty("http://apache.org/xml/properties/internal/symbol-table"));
    fErrorReporter = ((XMLErrorReporter)paramXMLComponentManager.getProperty("http://apache.org/xml/properties/internal/error-reporter"));
  }
  
  public String[] getRecognizedFeatures()
  {
    return (String[])RECOGNIZED_FEATURES.clone();
  }
  
  public void setFeature(String paramString, boolean paramBoolean)
    throws XMLConfigurationException
  {}
  
  public String[] getRecognizedProperties()
  {
    return (String[])RECOGNIZED_PROPERTIES.clone();
  }
  
  public void setProperty(String paramString, Object paramObject)
    throws XMLConfigurationException
  {
    if (paramString.startsWith("http://apache.org/xml/properties/"))
    {
      int i = paramString.length() - "http://apache.org/xml/properties/".length();
      if ((i == "internal/symbol-table".length()) && (paramString.endsWith("internal/symbol-table"))) {
        fSymbolTable = ((SymbolTable)paramObject);
      } else if ((i == "internal/error-reporter".length()) && (paramString.endsWith("internal/error-reporter"))) {
        fErrorReporter = ((XMLErrorReporter)paramObject);
      }
      return;
    }
  }
  
  public Boolean getFeatureDefault(String paramString)
  {
    for (int i = 0; i < RECOGNIZED_FEATURES.length; i++) {
      if (RECOGNIZED_FEATURES[i].equals(paramString)) {
        return FEATURE_DEFAULTS[i];
      }
    }
    return null;
  }
  
  public Object getPropertyDefault(String paramString)
  {
    for (int i = 0; i < RECOGNIZED_PROPERTIES.length; i++) {
      if (RECOGNIZED_PROPERTIES[i].equals(paramString)) {
        return PROPERTY_DEFAULTS[i];
      }
    }
    return null;
  }
  
  public void setDocumentHandler(XMLDocumentHandler paramXMLDocumentHandler)
  {
    fDocumentHandler = paramXMLDocumentHandler;
  }
  
  public XMLDocumentHandler getDocumentHandler()
  {
    return fDocumentHandler;
  }
  
  public void setDocumentSource(XMLDocumentSource paramXMLDocumentSource)
  {
    fDocumentSource = paramXMLDocumentSource;
  }
  
  public XMLDocumentSource getDocumentSource()
  {
    return fDocumentSource;
  }
  
  public void startGeneralEntity(String paramString1, XMLResourceIdentifier paramXMLResourceIdentifier, String paramString2, Augmentations paramAugmentations)
    throws XNIException
  {
    if ((fDocumentHandler != null) && (!fOnlyPassPrefixMappingEvents)) {
      fDocumentHandler.startGeneralEntity(paramString1, paramXMLResourceIdentifier, paramString2, paramAugmentations);
    }
  }
  
  public void textDecl(String paramString1, String paramString2, Augmentations paramAugmentations)
    throws XNIException
  {
    if ((fDocumentHandler != null) && (!fOnlyPassPrefixMappingEvents)) {
      fDocumentHandler.textDecl(paramString1, paramString2, paramAugmentations);
    }
  }
  
  public void startDocument(XMLLocator paramXMLLocator, String paramString, NamespaceContext paramNamespaceContext, Augmentations paramAugmentations)
    throws XNIException
  {
    fNamespaceContext = paramNamespaceContext;
    if ((fDocumentHandler != null) && (!fOnlyPassPrefixMappingEvents)) {
      fDocumentHandler.startDocument(paramXMLLocator, paramString, paramNamespaceContext, paramAugmentations);
    }
  }
  
  public void xmlDecl(String paramString1, String paramString2, String paramString3, Augmentations paramAugmentations)
    throws XNIException
  {
    if ((fDocumentHandler != null) && (!fOnlyPassPrefixMappingEvents)) {
      fDocumentHandler.xmlDecl(paramString1, paramString2, paramString3, paramAugmentations);
    }
  }
  
  public void doctypeDecl(String paramString1, String paramString2, String paramString3, Augmentations paramAugmentations)
    throws XNIException
  {
    if ((fDocumentHandler != null) && (!fOnlyPassPrefixMappingEvents)) {
      fDocumentHandler.doctypeDecl(paramString1, paramString2, paramString3, paramAugmentations);
    }
  }
  
  public void comment(XMLString paramXMLString, Augmentations paramAugmentations)
    throws XNIException
  {
    if ((fDocumentHandler != null) && (!fOnlyPassPrefixMappingEvents)) {
      fDocumentHandler.comment(paramXMLString, paramAugmentations);
    }
  }
  
  public void processingInstruction(String paramString, XMLString paramXMLString, Augmentations paramAugmentations)
    throws XNIException
  {
    if ((fDocumentHandler != null) && (!fOnlyPassPrefixMappingEvents)) {
      fDocumentHandler.processingInstruction(paramString, paramXMLString, paramAugmentations);
    }
  }
  
  public void startElement(QName paramQName, XMLAttributes paramXMLAttributes, Augmentations paramAugmentations)
    throws XNIException
  {
    if (fNamespaces) {
      handleStartElement(paramQName, paramXMLAttributes, paramAugmentations, false);
    } else if (fDocumentHandler != null) {
      fDocumentHandler.startElement(paramQName, paramXMLAttributes, paramAugmentations);
    }
  }
  
  public void emptyElement(QName paramQName, XMLAttributes paramXMLAttributes, Augmentations paramAugmentations)
    throws XNIException
  {
    if (fNamespaces)
    {
      handleStartElement(paramQName, paramXMLAttributes, paramAugmentations, true);
      handleEndElement(paramQName, paramAugmentations, true);
    }
    else if (fDocumentHandler != null)
    {
      fDocumentHandler.emptyElement(paramQName, paramXMLAttributes, paramAugmentations);
    }
  }
  
  public void characters(XMLString paramXMLString, Augmentations paramAugmentations)
    throws XNIException
  {
    if ((fDocumentHandler != null) && (!fOnlyPassPrefixMappingEvents)) {
      fDocumentHandler.characters(paramXMLString, paramAugmentations);
    }
  }
  
  public void ignorableWhitespace(XMLString paramXMLString, Augmentations paramAugmentations)
    throws XNIException
  {
    if ((fDocumentHandler != null) && (!fOnlyPassPrefixMappingEvents)) {
      fDocumentHandler.ignorableWhitespace(paramXMLString, paramAugmentations);
    }
  }
  
  public void endElement(QName paramQName, Augmentations paramAugmentations)
    throws XNIException
  {
    if (fNamespaces) {
      handleEndElement(paramQName, paramAugmentations, false);
    } else if (fDocumentHandler != null) {
      fDocumentHandler.endElement(paramQName, paramAugmentations);
    }
  }
  
  public void startCDATA(Augmentations paramAugmentations)
    throws XNIException
  {
    if ((fDocumentHandler != null) && (!fOnlyPassPrefixMappingEvents)) {
      fDocumentHandler.startCDATA(paramAugmentations);
    }
  }
  
  public void endCDATA(Augmentations paramAugmentations)
    throws XNIException
  {
    if ((fDocumentHandler != null) && (!fOnlyPassPrefixMappingEvents)) {
      fDocumentHandler.endCDATA(paramAugmentations);
    }
  }
  
  public void endDocument(Augmentations paramAugmentations)
    throws XNIException
  {
    if ((fDocumentHandler != null) && (!fOnlyPassPrefixMappingEvents)) {
      fDocumentHandler.endDocument(paramAugmentations);
    }
  }
  
  public void endGeneralEntity(String paramString, Augmentations paramAugmentations)
    throws XNIException
  {
    if ((fDocumentHandler != null) && (!fOnlyPassPrefixMappingEvents)) {
      fDocumentHandler.endGeneralEntity(paramString, paramAugmentations);
    }
  }
  
  protected void handleStartElement(QName paramQName, XMLAttributes paramXMLAttributes, Augmentations paramAugmentations, boolean paramBoolean)
    throws XNIException
  {
    fNamespaceContext.pushContext();
    if (prefix == XMLSymbols.PREFIX_XMLNS) {
      fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "ElementXMLNSPrefix", new Object[] { rawname }, (short)2);
    }
    int i = paramXMLAttributes.getLength();
    String str3;
    for (int j = 0; j < i; j++)
    {
      str1 = paramXMLAttributes.getLocalName(j);
      String str2 = paramXMLAttributes.getPrefix(j);
      if ((str2 == XMLSymbols.PREFIX_XMLNS) || ((str2 == XMLSymbols.EMPTY_STRING) && (str1 == XMLSymbols.PREFIX_XMLNS)))
      {
        str3 = fSymbolTable.addSymbol(paramXMLAttributes.getValue(j));
        if ((str2 == XMLSymbols.PREFIX_XMLNS) && (str1 == XMLSymbols.PREFIX_XMLNS)) {
          fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "CantBindXMLNS", new Object[] { paramXMLAttributes.getQName(j) }, (short)2);
        }
        if (str3 == NamespaceContext.XMLNS_URI) {
          fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "CantBindXMLNS", new Object[] { paramXMLAttributes.getQName(j) }, (short)2);
        }
        if (str1 == XMLSymbols.PREFIX_XML)
        {
          if (str3 != NamespaceContext.XML_URI) {
            fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "CantBindXML", new Object[] { paramXMLAttributes.getQName(j) }, (short)2);
          }
        }
        else if (str3 == NamespaceContext.XML_URI) {
          fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "CantBindXML", new Object[] { paramXMLAttributes.getQName(j) }, (short)2);
        }
        str2 = str1 != XMLSymbols.PREFIX_XMLNS ? str1 : XMLSymbols.EMPTY_STRING;
        if (prefixBoundToNullURI(str3, str1)) {
          fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "EmptyPrefixedAttName", new Object[] { paramXMLAttributes.getQName(j) }, (short)2);
        } else {
          fNamespaceContext.declarePrefix(str2, str3.length() != 0 ? str3 : null);
        }
      }
    }
    String str1 = prefix != null ? prefix : XMLSymbols.EMPTY_STRING;
    uri = fNamespaceContext.getURI(str1);
    if ((prefix == null) && (uri != null)) {
      prefix = XMLSymbols.EMPTY_STRING;
    }
    if ((prefix != null) && (uri == null)) {
      fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "ElementPrefixUnbound", new Object[] { prefix, rawname }, (short)2);
    }
    for (int k = 0; k < i; k++)
    {
      paramXMLAttributes.getName(k, fAttributeQName);
      str3 = fAttributeQName.prefix != null ? fAttributeQName.prefix : XMLSymbols.EMPTY_STRING;
      String str4 = fAttributeQName.rawname;
      if (str4 == XMLSymbols.PREFIX_XMLNS)
      {
        fAttributeQName.uri = fNamespaceContext.getURI(XMLSymbols.PREFIX_XMLNS);
        paramXMLAttributes.setName(k, fAttributeQName);
      }
      else if (str3 != XMLSymbols.EMPTY_STRING)
      {
        fAttributeQName.uri = fNamespaceContext.getURI(str3);
        if (fAttributeQName.uri == null) {
          fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "AttributePrefixUnbound", new Object[] { rawname, str4, str3 }, (short)2);
        }
        paramXMLAttributes.setName(k, fAttributeQName);
      }
    }
    int m = paramXMLAttributes.getLength();
    for (int n = 0; n < m - 1; n++)
    {
      String str5 = paramXMLAttributes.getURI(n);
      if ((str5 != null) && (str5 != NamespaceContext.XMLNS_URI))
      {
        String str6 = paramXMLAttributes.getLocalName(n);
        for (int i1 = n + 1; i1 < m; i1++)
        {
          String str7 = paramXMLAttributes.getLocalName(i1);
          String str8 = paramXMLAttributes.getURI(i1);
          if ((str6 == str7) && (str5 == str8)) {
            fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "AttributeNSNotUnique", new Object[] { rawname, str6, str5 }, (short)2);
          }
        }
      }
    }
    if ((fDocumentHandler != null) && (!fOnlyPassPrefixMappingEvents)) {
      if (paramBoolean) {
        fDocumentHandler.emptyElement(paramQName, paramXMLAttributes, paramAugmentations);
      } else {
        fDocumentHandler.startElement(paramQName, paramXMLAttributes, paramAugmentations);
      }
    }
  }
  
  protected void handleEndElement(QName paramQName, Augmentations paramAugmentations, boolean paramBoolean)
    throws XNIException
  {
    String str = prefix != null ? prefix : XMLSymbols.EMPTY_STRING;
    uri = fNamespaceContext.getURI(str);
    if (uri != null) {
      prefix = str;
    }
    if ((fDocumentHandler != null) && (!fOnlyPassPrefixMappingEvents) && (!paramBoolean)) {
      fDocumentHandler.endElement(paramQName, paramAugmentations);
    }
    fNamespaceContext.popContext();
  }
  
  protected boolean prefixBoundToNullURI(String paramString1, String paramString2)
  {
    return (paramString1 == XMLSymbols.EMPTY_STRING) && (paramString2 != XMLSymbols.PREFIX_XMLNS);
  }
}
