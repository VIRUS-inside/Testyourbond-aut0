package org.apache.xerces.impl.xs.opti;

import java.io.IOException;
import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.impl.xs.SchemaSymbols;
import org.apache.xerces.util.XMLAttributesImpl;
import org.apache.xerces.util.XMLChar;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.apache.xerces.xni.parser.XMLParserConfiguration;
import org.w3c.dom.Document;

public class SchemaDOMParser
  extends DefaultXMLDocumentHandler
{
  public static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
  public static final String GENERATE_SYNTHETIC_ANNOTATION = "http://apache.org/xml/features/generate-synthetic-annotations";
  protected XMLLocator fLocator;
  protected NamespaceContext fNamespaceContext = null;
  SchemaDOM schemaDOM;
  XMLParserConfiguration config;
  private ElementImpl fCurrentAnnotationElement;
  private int fAnnotationDepth = -1;
  private int fInnerAnnotationDepth = -1;
  private int fDepth = -1;
  XMLErrorReporter fErrorReporter;
  private boolean fGenerateSyntheticAnnotation = false;
  private BooleanStack fHasNonSchemaAttributes = new BooleanStack();
  private BooleanStack fSawAnnotation = new BooleanStack();
  private XMLAttributes fEmptyAttr = new XMLAttributesImpl();
  
  public SchemaDOMParser(XMLParserConfiguration paramXMLParserConfiguration)
  {
    config = paramXMLParserConfiguration;
    paramXMLParserConfiguration.setDocumentHandler(this);
    paramXMLParserConfiguration.setDTDHandler(this);
    paramXMLParserConfiguration.setDTDContentModelHandler(this);
  }
  
  public void startDocument(XMLLocator paramXMLLocator, String paramString, NamespaceContext paramNamespaceContext, Augmentations paramAugmentations)
    throws XNIException
  {
    fErrorReporter = ((XMLErrorReporter)config.getProperty("http://apache.org/xml/properties/internal/error-reporter"));
    fGenerateSyntheticAnnotation = config.getFeature("http://apache.org/xml/features/generate-synthetic-annotations");
    fHasNonSchemaAttributes.clear();
    fSawAnnotation.clear();
    schemaDOM = new SchemaDOM();
    fCurrentAnnotationElement = null;
    fAnnotationDepth = -1;
    fInnerAnnotationDepth = -1;
    fDepth = -1;
    fLocator = paramXMLLocator;
    fNamespaceContext = paramNamespaceContext;
    schemaDOM.setDocumentURI(paramXMLLocator.getExpandedSystemId());
  }
  
  public void endDocument(Augmentations paramAugmentations)
    throws XNIException
  {}
  
  public void comment(XMLString paramXMLString, Augmentations paramAugmentations)
    throws XNIException
  {
    if (fAnnotationDepth > -1) {
      schemaDOM.comment(paramXMLString);
    }
  }
  
  public void processingInstruction(String paramString, XMLString paramXMLString, Augmentations paramAugmentations)
    throws XNIException
  {
    if (fAnnotationDepth > -1) {
      schemaDOM.processingInstruction(paramString, paramXMLString);
    }
  }
  
  public void characters(XMLString paramXMLString, Augmentations paramAugmentations)
    throws XNIException
  {
    if (fInnerAnnotationDepth == -1) {
      for (int i = offset; i < offset + length; i++) {
        if (!XMLChar.isSpace(ch[i]))
        {
          String str = new String(ch, i, length + offset - i);
          fErrorReporter.reportError(fLocator, "http://www.w3.org/TR/xml-schema-1", "s4s-elt-character", new Object[] { str }, (short)1);
          break;
        }
      }
    } else {
      schemaDOM.characters(paramXMLString);
    }
  }
  
  public void startElement(QName paramQName, XMLAttributes paramXMLAttributes, Augmentations paramAugmentations)
    throws XNIException
  {
    fDepth += 1;
    if (fAnnotationDepth == -1)
    {
      if ((uri == SchemaSymbols.URI_SCHEMAFORSCHEMA) && (localpart == SchemaSymbols.ELT_ANNOTATION))
      {
        if (fGenerateSyntheticAnnotation)
        {
          if (fSawAnnotation.size() > 0) {
            fSawAnnotation.pop();
          }
          fSawAnnotation.push(true);
        }
        fAnnotationDepth = fDepth;
        schemaDOM.startAnnotation(paramQName, paramXMLAttributes, fNamespaceContext);
        fCurrentAnnotationElement = schemaDOM.startElement(paramQName, paramXMLAttributes, fLocator.getLineNumber(), fLocator.getColumnNumber(), fLocator.getCharacterOffset());
        return;
      }
      if ((uri == SchemaSymbols.URI_SCHEMAFORSCHEMA) && (fGenerateSyntheticAnnotation))
      {
        fSawAnnotation.push(false);
        fHasNonSchemaAttributes.push(hasNonSchemaAttributes(paramQName, paramXMLAttributes));
      }
    }
    else if (fDepth == fAnnotationDepth + 1)
    {
      fInnerAnnotationDepth = fDepth;
      schemaDOM.startAnnotationElement(paramQName, paramXMLAttributes);
    }
    else
    {
      schemaDOM.startAnnotationElement(paramQName, paramXMLAttributes);
      return;
    }
    schemaDOM.startElement(paramQName, paramXMLAttributes, fLocator.getLineNumber(), fLocator.getColumnNumber(), fLocator.getCharacterOffset());
  }
  
  public void emptyElement(QName paramQName, XMLAttributes paramXMLAttributes, Augmentations paramAugmentations)
    throws XNIException
  {
    if ((fGenerateSyntheticAnnotation) && (fAnnotationDepth == -1) && (uri == SchemaSymbols.URI_SCHEMAFORSCHEMA) && (localpart != SchemaSymbols.ELT_ANNOTATION) && (hasNonSchemaAttributes(paramQName, paramXMLAttributes)))
    {
      schemaDOM.startElement(paramQName, paramXMLAttributes, fLocator.getLineNumber(), fLocator.getColumnNumber(), fLocator.getCharacterOffset());
      paramXMLAttributes.removeAllAttributes();
      localObject = fNamespaceContext.getPrefix(SchemaSymbols.URI_SCHEMAFORSCHEMA);
      String str1 = (String)localObject + ':' + SchemaSymbols.ELT_ANNOTATION;
      schemaDOM.startAnnotation(str1, paramXMLAttributes, fNamespaceContext);
      String str2 = (String)localObject + ':' + SchemaSymbols.ELT_DOCUMENTATION;
      schemaDOM.startAnnotationElement(str2, paramXMLAttributes);
      schemaDOM.charactersRaw("SYNTHETIC_ANNOTATION");
      schemaDOM.endSyntheticAnnotationElement(str2, false);
      schemaDOM.endSyntheticAnnotationElement(str1, true);
      schemaDOM.endElement();
      return;
    }
    if (fAnnotationDepth == -1)
    {
      if ((uri == SchemaSymbols.URI_SCHEMAFORSCHEMA) && (localpart == SchemaSymbols.ELT_ANNOTATION)) {
        schemaDOM.startAnnotation(paramQName, paramXMLAttributes, fNamespaceContext);
      }
    }
    else {
      schemaDOM.startAnnotationElement(paramQName, paramXMLAttributes);
    }
    Object localObject = schemaDOM.emptyElement(paramQName, paramXMLAttributes, fLocator.getLineNumber(), fLocator.getColumnNumber(), fLocator.getCharacterOffset());
    if (fAnnotationDepth == -1)
    {
      if ((uri == SchemaSymbols.URI_SCHEMAFORSCHEMA) && (localpart == SchemaSymbols.ELT_ANNOTATION)) {
        schemaDOM.endAnnotation(paramQName, (ElementImpl)localObject);
      }
    }
    else {
      schemaDOM.endAnnotationElement(paramQName);
    }
  }
  
  public void endElement(QName paramQName, Augmentations paramAugmentations)
    throws XNIException
  {
    if (fAnnotationDepth > -1)
    {
      if (fInnerAnnotationDepth == fDepth)
      {
        fInnerAnnotationDepth = -1;
        schemaDOM.endAnnotationElement(paramQName);
        schemaDOM.endElement();
      }
      else if (fAnnotationDepth == fDepth)
      {
        fAnnotationDepth = -1;
        schemaDOM.endAnnotation(paramQName, fCurrentAnnotationElement);
        schemaDOM.endElement();
      }
      else
      {
        schemaDOM.endAnnotationElement(paramQName);
      }
    }
    else
    {
      if ((uri == SchemaSymbols.URI_SCHEMAFORSCHEMA) && (fGenerateSyntheticAnnotation))
      {
        boolean bool1 = fHasNonSchemaAttributes.pop();
        boolean bool2 = fSawAnnotation.pop();
        if ((bool1) && (!bool2))
        {
          String str1 = fNamespaceContext.getPrefix(SchemaSymbols.URI_SCHEMAFORSCHEMA);
          String str2 = str1 + ':' + SchemaSymbols.ELT_ANNOTATION;
          schemaDOM.startAnnotation(str2, fEmptyAttr, fNamespaceContext);
          String str3 = str1 + ':' + SchemaSymbols.ELT_DOCUMENTATION;
          schemaDOM.startAnnotationElement(str3, fEmptyAttr);
          schemaDOM.charactersRaw("SYNTHETIC_ANNOTATION");
          schemaDOM.endSyntheticAnnotationElement(str3, false);
          schemaDOM.endSyntheticAnnotationElement(str2, true);
        }
      }
      schemaDOM.endElement();
    }
    fDepth -= 1;
  }
  
  private boolean hasNonSchemaAttributes(QName paramQName, XMLAttributes paramXMLAttributes)
  {
    int i = paramXMLAttributes.getLength();
    for (int j = 0; j < i; j++)
    {
      String str = paramXMLAttributes.getURI(j);
      if ((str != null) && (str != SchemaSymbols.URI_SCHEMAFORSCHEMA) && (str != NamespaceContext.XMLNS_URI) && ((str != NamespaceContext.XML_URI) || (paramXMLAttributes.getQName(j) != SchemaSymbols.ATT_XML_LANG) || (localpart != SchemaSymbols.ELT_SCHEMA))) {
        return true;
      }
    }
    return false;
  }
  
  public void ignorableWhitespace(XMLString paramXMLString, Augmentations paramAugmentations)
    throws XNIException
  {
    if (fAnnotationDepth != -1) {
      schemaDOM.characters(paramXMLString);
    }
  }
  
  public void startCDATA(Augmentations paramAugmentations)
    throws XNIException
  {
    if (fAnnotationDepth != -1) {
      schemaDOM.startAnnotationCDATA();
    }
  }
  
  public void endCDATA(Augmentations paramAugmentations)
    throws XNIException
  {
    if (fAnnotationDepth != -1) {
      schemaDOM.endAnnotationCDATA();
    }
  }
  
  public Document getDocument()
  {
    return schemaDOM;
  }
  
  public void setFeature(String paramString, boolean paramBoolean)
  {
    config.setFeature(paramString, paramBoolean);
  }
  
  public boolean getFeature(String paramString)
  {
    return config.getFeature(paramString);
  }
  
  public void setProperty(String paramString, Object paramObject)
  {
    config.setProperty(paramString, paramObject);
  }
  
  public Object getProperty(String paramString)
  {
    return config.getProperty(paramString);
  }
  
  public void setEntityResolver(XMLEntityResolver paramXMLEntityResolver)
  {
    config.setEntityResolver(paramXMLEntityResolver);
  }
  
  public void parse(XMLInputSource paramXMLInputSource)
    throws IOException
  {
    config.parse(paramXMLInputSource);
  }
  
  public void reset()
  {
    ((SchemaParsingConfig)config).reset();
  }
  
  public void resetNodePool()
  {
    ((SchemaParsingConfig)config).resetNodePool();
  }
  
  private static final class BooleanStack
  {
    private int fDepth;
    private boolean[] fData;
    
    public BooleanStack() {}
    
    public int size()
    {
      return fDepth;
    }
    
    public void push(boolean paramBoolean)
    {
      ensureCapacity(fDepth + 1);
      fData[(fDepth++)] = paramBoolean;
    }
    
    public boolean pop()
    {
      return fData[(--fDepth)];
    }
    
    public void clear()
    {
      fDepth = 0;
    }
    
    private void ensureCapacity(int paramInt)
    {
      if (fData == null)
      {
        fData = new boolean[32];
      }
      else if (fData.length <= paramInt)
      {
        boolean[] arrayOfBoolean = new boolean[fData.length * 2];
        System.arraycopy(fData, 0, arrayOfBoolean, 0, fData.length);
        fData = arrayOfBoolean;
      }
    }
  }
}
