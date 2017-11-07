package org.apache.xml.serialize;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.ArrayList;
import org.apache.xerces.dom.CoreDocumentImpl;
import org.apache.xerces.dom.DOMErrorImpl;
import org.apache.xerces.dom.DOMLocatorImpl;
import org.apache.xerces.dom.DOMMessageFormatter;
import org.apache.xerces.dom.DOMNormalizer;
import org.apache.xerces.dom.DOMStringListImpl;
import org.apache.xerces.impl.XMLEntityManager;
import org.apache.xerces.util.DOMUtil;
import org.apache.xerces.util.NamespaceSupport;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XML11Char;
import org.apache.xerces.util.XMLChar;
import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMError;
import org.w3c.dom.DOMErrorHandler;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMStringList;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.ls.LSException;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import org.w3c.dom.ls.LSSerializerFilter;

/**
 * @deprecated
 */
public class DOMSerializerImpl
  implements LSSerializer, DOMConfiguration
{
  private XMLSerializer serializer = new XMLSerializer();
  private XML11Serializer xml11Serializer;
  private DOMStringList fRecognizedParameters;
  protected short features = 0;
  protected static final short NAMESPACES = 1;
  protected static final short WELLFORMED = 2;
  protected static final short ENTITIES = 4;
  protected static final short CDATA = 8;
  protected static final short SPLITCDATA = 16;
  protected static final short COMMENTS = 32;
  protected static final short DISCARDDEFAULT = 64;
  protected static final short INFOSET = 128;
  protected static final short XMLDECL = 256;
  protected static final short NSDECL = 512;
  protected static final short DOM_ELEMENT_CONTENT_WHITESPACE = 1024;
  protected static final short PRETTY_PRINT = 2048;
  private DOMErrorHandler fErrorHandler = null;
  private final DOMErrorImpl fError = new DOMErrorImpl();
  private final DOMLocatorImpl fLocator = new DOMLocatorImpl();
  
  public DOMSerializerImpl()
  {
    initSerializer(serializer);
  }
  
  public DOMConfiguration getDomConfig()
  {
    return this;
  }
  
  public void setParameter(String paramString, Object paramObject)
    throws DOMException
  {
    if ((paramObject instanceof Boolean))
    {
      boolean bool = ((Boolean)paramObject).booleanValue();
      if (paramString.equalsIgnoreCase("infoset"))
      {
        if (bool)
        {
          features = ((short)(features & 0xFFFFFFFB));
          features = ((short)(features & 0xFFFFFFF7));
          features = ((short)(features | 0x1));
          features = ((short)(features | 0x200));
          features = ((short)(features | 0x2));
          features = ((short)(features | 0x20));
        }
      }
      else if (paramString.equalsIgnoreCase("xml-declaration"))
      {
        features = (bool ? (short)(features | 0x100) : (short)(features & 0xFEFF));
      }
      else if (paramString.equalsIgnoreCase("namespaces"))
      {
        features = (bool ? (short)(features | 0x1) : (short)(features & 0xFFFFFFFE));
        serializer.fNamespaces = bool;
      }
      else if (paramString.equalsIgnoreCase("split-cdata-sections"))
      {
        features = (bool ? (short)(features | 0x10) : (short)(features & 0xFFFFFFEF));
      }
      else if (paramString.equalsIgnoreCase("discard-default-content"))
      {
        features = (bool ? (short)(features | 0x40) : (short)(features & 0xFFFFFFBF));
      }
      else if (paramString.equalsIgnoreCase("well-formed"))
      {
        features = (bool ? (short)(features | 0x2) : (short)(features & 0xFFFFFFFD));
      }
      else if (paramString.equalsIgnoreCase("entities"))
      {
        features = (bool ? (short)(features | 0x4) : (short)(features & 0xFFFFFFFB));
      }
      else if (paramString.equalsIgnoreCase("cdata-sections"))
      {
        features = (bool ? (short)(features | 0x8) : (short)(features & 0xFFFFFFF7));
      }
      else if (paramString.equalsIgnoreCase("comments"))
      {
        features = (bool ? (short)(features | 0x20) : (short)(features & 0xFFFFFFDF));
      }
      else if (paramString.equalsIgnoreCase("format-pretty-print"))
      {
        features = (bool ? (short)(features | 0x800) : (short)(features & 0xF7FF));
      }
      else
      {
        String str2;
        if ((paramString.equalsIgnoreCase("canonical-form")) || (paramString.equalsIgnoreCase("validate-if-schema")) || (paramString.equalsIgnoreCase("validate")) || (paramString.equalsIgnoreCase("check-character-normalization")) || (paramString.equalsIgnoreCase("datatype-normalization")) || (paramString.equalsIgnoreCase("normalize-characters")))
        {
          if (bool)
          {
            str2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "FEATURE_NOT_SUPPORTED", new Object[] { paramString });
            throw new DOMException((short)9, str2);
          }
        }
        else if (paramString.equalsIgnoreCase("namespace-declarations"))
        {
          features = (bool ? (short)(features | 0x200) : (short)(features & 0xFDFF));
          serializer.fNamespacePrefixes = bool;
        }
        else if ((paramString.equalsIgnoreCase("element-content-whitespace")) || (paramString.equalsIgnoreCase("ignore-unknown-character-denormalizations")))
        {
          if (!bool)
          {
            str2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "FEATURE_NOT_SUPPORTED", new Object[] { paramString });
            throw new DOMException((short)9, str2);
          }
        }
        else
        {
          str2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "FEATURE_NOT_FOUND", new Object[] { paramString });
          throw new DOMException((short)9, str2);
        }
      }
    }
    else
    {
      String str1;
      if (paramString.equalsIgnoreCase("error-handler"))
      {
        if ((paramObject == null) || ((paramObject instanceof DOMErrorHandler)))
        {
          fErrorHandler = ((DOMErrorHandler)paramObject);
        }
        else
        {
          str1 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "TYPE_MISMATCH_ERR", new Object[] { paramString });
          throw new DOMException((short)17, str1);
        }
      }
      else
      {
        if ((paramString.equalsIgnoreCase("resource-resolver")) || (paramString.equalsIgnoreCase("schema-location")) || ((paramString.equalsIgnoreCase("schema-type")) && (paramObject != null)))
        {
          str1 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "FEATURE_NOT_SUPPORTED", new Object[] { paramString });
          throw new DOMException((short)9, str1);
        }
        str1 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "FEATURE_NOT_FOUND", new Object[] { paramString });
        throw new DOMException((short)8, str1);
      }
    }
  }
  
  public boolean canSetParameter(String paramString, Object paramObject)
  {
    if (paramObject == null) {
      return true;
    }
    if ((paramObject instanceof Boolean))
    {
      boolean bool = ((Boolean)paramObject).booleanValue();
      if ((paramString.equalsIgnoreCase("namespaces")) || (paramString.equalsIgnoreCase("split-cdata-sections")) || (paramString.equalsIgnoreCase("discard-default-content")) || (paramString.equalsIgnoreCase("xml-declaration")) || (paramString.equalsIgnoreCase("well-formed")) || (paramString.equalsIgnoreCase("infoset")) || (paramString.equalsIgnoreCase("entities")) || (paramString.equalsIgnoreCase("cdata-sections")) || (paramString.equalsIgnoreCase("comments")) || (paramString.equalsIgnoreCase("format-pretty-print")) || (paramString.equalsIgnoreCase("namespace-declarations"))) {
        return true;
      }
      if ((paramString.equalsIgnoreCase("canonical-form")) || (paramString.equalsIgnoreCase("validate-if-schema")) || (paramString.equalsIgnoreCase("validate")) || (paramString.equalsIgnoreCase("check-character-normalization")) || (paramString.equalsIgnoreCase("datatype-normalization")) || (paramString.equalsIgnoreCase("normalize-characters"))) {
        return !bool;
      }
      if ((paramString.equalsIgnoreCase("element-content-whitespace")) || (paramString.equalsIgnoreCase("ignore-unknown-character-denormalizations"))) {
        return bool;
      }
    }
    else if (((paramString.equalsIgnoreCase("error-handler")) && (paramObject == null)) || ((paramObject instanceof DOMErrorHandler)))
    {
      return true;
    }
    return false;
  }
  
  public DOMStringList getParameterNames()
  {
    if (fRecognizedParameters == null)
    {
      ArrayList localArrayList = new ArrayList();
      localArrayList.add("namespaces");
      localArrayList.add("split-cdata-sections");
      localArrayList.add("discard-default-content");
      localArrayList.add("xml-declaration");
      localArrayList.add("canonical-form");
      localArrayList.add("validate-if-schema");
      localArrayList.add("validate");
      localArrayList.add("check-character-normalization");
      localArrayList.add("datatype-normalization");
      localArrayList.add("format-pretty-print");
      localArrayList.add("normalize-characters");
      localArrayList.add("well-formed");
      localArrayList.add("infoset");
      localArrayList.add("namespace-declarations");
      localArrayList.add("element-content-whitespace");
      localArrayList.add("entities");
      localArrayList.add("cdata-sections");
      localArrayList.add("comments");
      localArrayList.add("ignore-unknown-character-denormalizations");
      localArrayList.add("error-handler");
      fRecognizedParameters = new DOMStringListImpl(localArrayList);
    }
    return fRecognizedParameters;
  }
  
  public Object getParameter(String paramString)
    throws DOMException
  {
    if (paramString.equalsIgnoreCase("comments")) {
      return (features & 0x20) != 0 ? Boolean.TRUE : Boolean.FALSE;
    }
    if (paramString.equalsIgnoreCase("namespaces")) {
      return (features & 0x1) != 0 ? Boolean.TRUE : Boolean.FALSE;
    }
    if (paramString.equalsIgnoreCase("xml-declaration")) {
      return (features & 0x100) != 0 ? Boolean.TRUE : Boolean.FALSE;
    }
    if (paramString.equalsIgnoreCase("cdata-sections")) {
      return (features & 0x8) != 0 ? Boolean.TRUE : Boolean.FALSE;
    }
    if (paramString.equalsIgnoreCase("entities")) {
      return (features & 0x4) != 0 ? Boolean.TRUE : Boolean.FALSE;
    }
    if (paramString.equalsIgnoreCase("split-cdata-sections")) {
      return (features & 0x10) != 0 ? Boolean.TRUE : Boolean.FALSE;
    }
    if (paramString.equalsIgnoreCase("well-formed")) {
      return (features & 0x2) != 0 ? Boolean.TRUE : Boolean.FALSE;
    }
    if (paramString.equalsIgnoreCase("namespace-declarations")) {
      return (features & 0x200) != 0 ? Boolean.TRUE : Boolean.FALSE;
    }
    if ((paramString.equalsIgnoreCase("element-content-whitespace")) || (paramString.equalsIgnoreCase("ignore-unknown-character-denormalizations"))) {
      return Boolean.TRUE;
    }
    if (paramString.equalsIgnoreCase("discard-default-content")) {
      return (features & 0x40) != 0 ? Boolean.TRUE : Boolean.FALSE;
    }
    if (paramString.equalsIgnoreCase("format-pretty-print")) {
      return (features & 0x800) != 0 ? Boolean.TRUE : Boolean.FALSE;
    }
    if (paramString.equalsIgnoreCase("infoset"))
    {
      if (((features & 0x4) == 0) && ((features & 0x8) == 0) && ((features & 0x1) != 0) && ((features & 0x200) != 0) && ((features & 0x2) != 0) && ((features & 0x20) != 0)) {
        return Boolean.TRUE;
      }
      return Boolean.FALSE;
    }
    if ((paramString.equalsIgnoreCase("normalize-characters")) || (paramString.equalsIgnoreCase("canonical-form")) || (paramString.equalsIgnoreCase("validate-if-schema")) || (paramString.equalsIgnoreCase("check-character-normalization")) || (paramString.equalsIgnoreCase("validate")) || (paramString.equalsIgnoreCase("validate-if-schema")) || (paramString.equalsIgnoreCase("datatype-normalization"))) {
      return Boolean.FALSE;
    }
    if (paramString.equalsIgnoreCase("error-handler")) {
      return fErrorHandler;
    }
    if ((paramString.equalsIgnoreCase("resource-resolver")) || (paramString.equalsIgnoreCase("schema-location")) || (paramString.equalsIgnoreCase("schema-type")))
    {
      str = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "FEATURE_NOT_SUPPORTED", new Object[] { paramString });
      throw new DOMException((short)9, str);
    }
    String str = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "FEATURE_NOT_FOUND", new Object[] { paramString });
    throw new DOMException((short)8, str);
  }
  
  public String writeToString(Node paramNode)
    throws DOMException, LSException
  {
    Object localObject1 = null;
    String str1 = _getXmlVersion(paramNode);
    if ((str1 != null) && (str1.equals("1.1")))
    {
      if (xml11Serializer == null)
      {
        xml11Serializer = new XML11Serializer();
        initSerializer(xml11Serializer);
      }
      copySettings(serializer, xml11Serializer);
      localObject1 = xml11Serializer;
    }
    else
    {
      localObject1 = serializer;
    }
    StringWriter localStringWriter = new StringWriter();
    try
    {
      prepareForSerialization((XMLSerializer)localObject1, paramNode);
      _format.setEncoding("UTF-16");
      ((XMLSerializer)localObject1).setOutputCharStream(localStringWriter);
      if (paramNode.getNodeType() == 9)
      {
        ((XMLSerializer)localObject1).serialize((Document)paramNode);
      }
      else if (paramNode.getNodeType() == 11)
      {
        ((XMLSerializer)localObject1).serialize((DocumentFragment)paramNode);
      }
      else if (paramNode.getNodeType() == 1)
      {
        ((XMLSerializer)localObject1).serialize((Element)paramNode);
      }
      else
      {
        String str2 = DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "unable-to-serialize-node", null);
        if (fDOMErrorHandler != null)
        {
          DOMErrorImpl localDOMErrorImpl = new DOMErrorImpl();
          fType = "unable-to-serialize-node";
          fMessage = str2;
          fSeverity = 3;
          fDOMErrorHandler.handleError(localDOMErrorImpl);
        }
        throw new LSException((short)82, str2);
      }
    }
    catch (LSException localLSException)
    {
      throw localLSException;
    }
    catch (RuntimeException localRuntimeException)
    {
      if (localRuntimeException == DOMNormalizer.abort)
      {
        String str3 = null;
        return str3;
      }
      throw ((LSException)DOMUtil.createLSException((short)82, localRuntimeException).fillInStackTrace());
    }
    catch (IOException localIOException)
    {
      String str4 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "STRING_TOO_LONG", new Object[] { localIOException.getMessage() });
      throw new DOMException((short)2, str4);
    }
    finally
    {
      ((XMLSerializer)localObject1).clearDocumentState();
    }
    return localStringWriter.toString();
  }
  
  public void setNewLine(String paramString)
  {
    serializer._format.setLineSeparator(paramString);
  }
  
  public String getNewLine()
  {
    return serializer._format.getLineSeparator();
  }
  
  public LSSerializerFilter getFilter()
  {
    return serializer.fDOMFilter;
  }
  
  public void setFilter(LSSerializerFilter paramLSSerializerFilter)
  {
    serializer.fDOMFilter = paramLSSerializerFilter;
  }
  
  private void initSerializer(XMLSerializer paramXMLSerializer)
  {
    fNSBinder = new NamespaceSupport();
    fLocalNSBinder = new NamespaceSupport();
    fSymbolTable = new SymbolTable();
  }
  
  private void copySettings(XMLSerializer paramXMLSerializer1, XMLSerializer paramXMLSerializer2)
  {
    fDOMErrorHandler = fErrorHandler;
    _format.setEncoding(_format.getEncoding());
    _format.setLineSeparator(_format.getLineSeparator());
    fDOMFilter = fDOMFilter;
  }
  
  public boolean write(Node paramNode, LSOutput paramLSOutput)
    throws LSException
  {
    if (paramNode == null) {
      return false;
    }
    Object localObject1 = null;
    String str1 = _getXmlVersion(paramNode);
    if ((str1 != null) && (str1.equals("1.1")))
    {
      if (xml11Serializer == null)
      {
        xml11Serializer = new XML11Serializer();
        initSerializer(xml11Serializer);
      }
      copySettings(serializer, xml11Serializer);
      localObject1 = xml11Serializer;
    }
    else
    {
      localObject1 = serializer;
    }
    String str2 = null;
    if ((str2 = paramLSOutput.getEncoding()) == null)
    {
      str2 = _getInputEncoding(paramNode);
      if (str2 == null)
      {
        str2 = _getXmlEncoding(paramNode);
        if (str2 == null) {
          str2 = "UTF-8";
        }
      }
    }
    try
    {
      prepareForSerialization((XMLSerializer)localObject1, paramNode);
      _format.setEncoding(str2);
      OutputStream localOutputStream = paramLSOutput.getByteStream();
      localObject2 = paramLSOutput.getCharacterStream();
      String str3 = paramLSOutput.getSystemId();
      if (localObject2 == null)
      {
        if (localOutputStream == null)
        {
          if (str3 == null)
          {
            String str4 = DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "no-output-specified", null);
            if (fDOMErrorHandler != null)
            {
              localDOMErrorImpl = new DOMErrorImpl();
              fType = "no-output-specified";
              fMessage = str4;
              fSeverity = 3;
              fDOMErrorHandler.handleError(localDOMErrorImpl);
            }
            throw new LSException((short)82, str4);
          }
          ((XMLSerializer)localObject1).setOutputByteStream(XMLEntityManager.createOutputStream(str3));
        }
        else
        {
          ((XMLSerializer)localObject1).setOutputByteStream(localOutputStream);
        }
      }
      else {
        ((XMLSerializer)localObject1).setOutputCharStream((Writer)localObject2);
      }
      if (paramNode.getNodeType() == 9)
      {
        ((XMLSerializer)localObject1).serialize((Document)paramNode);
      }
      else if (paramNode.getNodeType() == 11)
      {
        ((XMLSerializer)localObject1).serialize((DocumentFragment)paramNode);
      }
      else if (paramNode.getNodeType() == 1)
      {
        ((XMLSerializer)localObject1).serialize((Element)paramNode);
      }
      else
      {
        bool = false;
        return bool;
      }
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      Object localObject2;
      if (fDOMErrorHandler != null)
      {
        localObject2 = new DOMErrorImpl();
        fException = localUnsupportedEncodingException;
        fType = "unsupported-encoding";
        fMessage = localUnsupportedEncodingException.getMessage();
        fSeverity = 3;
        fDOMErrorHandler.handleError((DOMError)localObject2);
      }
      throw new LSException((short)82, DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "unsupported-encoding", null));
    }
    catch (LSException localLSException)
    {
      throw localLSException;
    }
    catch (RuntimeException localRuntimeException)
    {
      boolean bool;
      if (localRuntimeException == DOMNormalizer.abort)
      {
        bool = false;
        return bool;
      }
      throw ((LSException)DOMUtil.createLSException((short)82, localRuntimeException).fillInStackTrace());
    }
    catch (Exception localException)
    {
      DOMErrorImpl localDOMErrorImpl;
      if (fDOMErrorHandler != null)
      {
        localDOMErrorImpl = new DOMErrorImpl();
        fException = localException;
        fMessage = localException.getMessage();
        fSeverity = 2;
        fDOMErrorHandler.handleError(localDOMErrorImpl);
      }
      throw ((LSException)DOMUtil.createLSException((short)82, localException).fillInStackTrace());
    }
    finally
    {
      ((XMLSerializer)localObject1).clearDocumentState();
    }
    return true;
  }
  
  public boolean writeToURI(Node paramNode, String paramString)
    throws LSException
  {
    if (paramNode == null) {
      return false;
    }
    Object localObject1 = null;
    String str1 = _getXmlVersion(paramNode);
    if ((str1 != null) && (str1.equals("1.1")))
    {
      if (xml11Serializer == null)
      {
        xml11Serializer = new XML11Serializer();
        initSerializer(xml11Serializer);
      }
      copySettings(serializer, xml11Serializer);
      localObject1 = xml11Serializer;
    }
    else
    {
      localObject1 = serializer;
    }
    String str2 = _getInputEncoding(paramNode);
    if (str2 == null)
    {
      str2 = _getXmlEncoding(paramNode);
      if (str2 == null) {
        str2 = "UTF-8";
      }
    }
    try
    {
      prepareForSerialization((XMLSerializer)localObject1, paramNode);
      _format.setEncoding(str2);
      ((XMLSerializer)localObject1).setOutputByteStream(XMLEntityManager.createOutputStream(paramString));
      if (paramNode.getNodeType() == 9)
      {
        ((XMLSerializer)localObject1).serialize((Document)paramNode);
      }
      else if (paramNode.getNodeType() == 11)
      {
        ((XMLSerializer)localObject1).serialize((DocumentFragment)paramNode);
      }
      else if (paramNode.getNodeType() == 1)
      {
        ((XMLSerializer)localObject1).serialize((Element)paramNode);
      }
      else
      {
        boolean bool1 = false;
        return bool1;
      }
    }
    catch (LSException localLSException)
    {
      throw localLSException;
    }
    catch (RuntimeException localRuntimeException)
    {
      if (localRuntimeException == DOMNormalizer.abort)
      {
        boolean bool2 = false;
        return bool2;
      }
      throw ((LSException)DOMUtil.createLSException((short)82, localRuntimeException).fillInStackTrace());
    }
    catch (Exception localException)
    {
      if (fDOMErrorHandler != null)
      {
        DOMErrorImpl localDOMErrorImpl = new DOMErrorImpl();
        fException = localException;
        fMessage = localException.getMessage();
        fSeverity = 2;
        fDOMErrorHandler.handleError(localDOMErrorImpl);
      }
      throw ((LSException)DOMUtil.createLSException((short)82, localException).fillInStackTrace());
    }
    finally
    {
      ((XMLSerializer)localObject1).clearDocumentState();
    }
    return true;
  }
  
  private void prepareForSerialization(XMLSerializer paramXMLSerializer, Node paramNode)
  {
    paramXMLSerializer.reset();
    features = features;
    fDOMErrorHandler = fErrorHandler;
    fNamespaces = ((features & 0x1) != 0);
    fNamespacePrefixes = ((features & 0x200) != 0);
    _format.setIndenting((features & 0x800) != 0);
    _format.setOmitComments((features & 0x20) == 0);
    _format.setOmitXMLDeclaration((features & 0x100) == 0);
    if ((features & 0x2) != 0)
    {
      Node localNode2 = paramNode;
      boolean bool = true;
      Document localDocument = paramNode.getNodeType() == 9 ? (Document)paramNode : paramNode.getOwnerDocument();
      try
      {
        Method localMethod = localDocument.getClass().getMethod("isXMLVersionChanged()", new Class[0]);
        if (localMethod != null) {
          bool = ((Boolean)localMethod.invoke(localDocument, (Object[])null)).booleanValue();
        }
      }
      catch (Exception localException) {}
      if (paramNode.getFirstChild() != null) {
        while (paramNode != null)
        {
          verify(paramNode, bool, false);
          Node localNode1 = paramNode.getFirstChild();
          while (localNode1 == null)
          {
            localNode1 = paramNode.getNextSibling();
            if (localNode1 == null)
            {
              paramNode = paramNode.getParentNode();
              if (localNode2 == paramNode)
              {
                localNode1 = null;
                break;
              }
              localNode1 = paramNode.getNextSibling();
            }
          }
          paramNode = localNode1;
        }
      } else {
        verify(paramNode, bool, false);
      }
    }
  }
  
  private void verify(Node paramNode, boolean paramBoolean1, boolean paramBoolean2)
  {
    int i = paramNode.getNodeType();
    fLocator.fRelatedNode = paramNode;
    boolean bool;
    Object localObject1;
    Object localObject2;
    switch (i)
    {
    case 9: 
      break;
    case 10: 
      break;
    case 1: 
      if (paramBoolean1)
      {
        if ((features & 0x1) != 0) {
          bool = CoreDocumentImpl.isValidQName(paramNode.getPrefix(), paramNode.getLocalName(), paramBoolean2);
        } else {
          bool = CoreDocumentImpl.isXMLName(paramNode.getNodeName(), paramBoolean2);
        }
        if ((!bool) && (fErrorHandler != null))
        {
          localObject1 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "wf-invalid-character-in-node-name", new Object[] { "Element", paramNode.getNodeName() });
          DOMNormalizer.reportDOMError(fErrorHandler, fError, fLocator, (String)localObject1, (short)3, "wf-invalid-character-in-node-name");
        }
      }
      localObject1 = paramNode.hasAttributes() ? paramNode.getAttributes() : null;
      if (localObject1 != null) {
        for (int j = 0; j < ((NamedNodeMap)localObject1).getLength(); j++)
        {
          localObject2 = (Attr)((NamedNodeMap)localObject1).item(j);
          fLocator.fRelatedNode = ((Node)localObject2);
          DOMNormalizer.isAttrValueWF(fErrorHandler, fError, fLocator, (NamedNodeMap)localObject1, (Attr)localObject2, ((Attr)localObject2).getValue(), paramBoolean2);
          if (paramBoolean1)
          {
            bool = CoreDocumentImpl.isXMLName(((Attr)localObject2).getNodeName(), paramBoolean2);
            if (!bool)
            {
              String str2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "wf-invalid-character-in-node-name", new Object[] { "Attr", paramNode.getNodeName() });
              DOMNormalizer.reportDOMError(fErrorHandler, fError, fLocator, str2, (short)3, "wf-invalid-character-in-node-name");
            }
          }
        }
      }
      break;
    case 8: 
      if ((goto 582) && ((features & 0x20) != 0)) {
        DOMNormalizer.isCommentWF(fErrorHandler, fError, fLocator, ((Comment)paramNode).getData(), paramBoolean2);
      }
      break;
    case 5: 
      if ((paramBoolean1) && ((features & 0x4) != 0)) {
        CoreDocumentImpl.isXMLName(paramNode.getNodeName(), paramBoolean2);
      }
      break;
    case 4: 
      DOMNormalizer.isXMLCharWF(fErrorHandler, fError, fLocator, paramNode.getNodeValue(), paramBoolean2);
      break;
    case 3: 
      DOMNormalizer.isXMLCharWF(fErrorHandler, fError, fLocator, paramNode.getNodeValue(), paramBoolean2);
      break;
    case 7: 
      localObject1 = (ProcessingInstruction)paramNode;
      String str1 = ((ProcessingInstruction)localObject1).getTarget();
      if (paramBoolean1)
      {
        if (paramBoolean2) {
          bool = XML11Char.isXML11ValidName(str1);
        } else {
          bool = XMLChar.isValidName(str1);
        }
        if (!bool)
        {
          localObject2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "wf-invalid-character-in-node-name", new Object[] { "Element", paramNode.getNodeName() });
          DOMNormalizer.reportDOMError(fErrorHandler, fError, fLocator, (String)localObject2, (short)3, "wf-invalid-character-in-node-name");
        }
      }
      DOMNormalizer.isXMLCharWF(fErrorHandler, fError, fLocator, ((ProcessingInstruction)localObject1).getData(), paramBoolean2);
    }
    fLocator.fRelatedNode = null;
  }
  
  private String _getXmlVersion(Node paramNode)
  {
    Document localDocument = paramNode.getNodeType() == 9 ? (Document)paramNode : paramNode.getOwnerDocument();
    if ((localDocument != null) && (DocumentMethods.fgDocumentMethodsAvailable)) {
      try
      {
        return (String)DocumentMethods.fgDocumentGetXmlVersionMethod.invoke(localDocument, (Object[])null);
      }
      catch (VirtualMachineError localVirtualMachineError)
      {
        throw localVirtualMachineError;
      }
      catch (ThreadDeath localThreadDeath)
      {
        throw localThreadDeath;
      }
      catch (Throwable localThrowable) {}
    }
    return null;
  }
  
  private String _getInputEncoding(Node paramNode)
  {
    Document localDocument = paramNode.getNodeType() == 9 ? (Document)paramNode : paramNode.getOwnerDocument();
    if ((localDocument != null) && (DocumentMethods.fgDocumentMethodsAvailable)) {
      try
      {
        return (String)DocumentMethods.fgDocumentGetInputEncodingMethod.invoke(localDocument, (Object[])null);
      }
      catch (VirtualMachineError localVirtualMachineError)
      {
        throw localVirtualMachineError;
      }
      catch (ThreadDeath localThreadDeath)
      {
        throw localThreadDeath;
      }
      catch (Throwable localThrowable) {}
    }
    return null;
  }
  
  private String _getXmlEncoding(Node paramNode)
  {
    Document localDocument = paramNode.getNodeType() == 9 ? (Document)paramNode : paramNode.getOwnerDocument();
    if ((localDocument != null) && (DocumentMethods.fgDocumentMethodsAvailable)) {
      try
      {
        return (String)DocumentMethods.fgDocumentGetXmlEncodingMethod.invoke(localDocument, (Object[])null);
      }
      catch (VirtualMachineError localVirtualMachineError)
      {
        throw localVirtualMachineError;
      }
      catch (ThreadDeath localThreadDeath)
      {
        throw localThreadDeath;
      }
      catch (Throwable localThrowable) {}
    }
    return null;
  }
  
  static class DocumentMethods
  {
    private static Method fgDocumentGetXmlVersionMethod = null;
    private static Method fgDocumentGetInputEncodingMethod = null;
    private static Method fgDocumentGetXmlEncodingMethod = null;
    private static boolean fgDocumentMethodsAvailable = false;
    
    private DocumentMethods() {}
    
    static
    {
      try
      {
        fgDocumentGetXmlVersionMethod = Document.class.getMethod("getXmlVersion", new Class[0]);
        fgDocumentGetInputEncodingMethod = Document.class.getMethod("getInputEncoding", new Class[0]);
        fgDocumentGetXmlEncodingMethod = Document.class.getMethod("getXmlEncoding", new Class[0]);
        fgDocumentMethodsAvailable = true;
      }
      catch (Exception localException)
      {
        fgDocumentGetXmlVersionMethod = null;
        fgDocumentGetInputEncodingMethod = null;
        fgDocumentGetXmlEncodingMethod = null;
        fgDocumentMethodsAvailable = false;
      }
    }
  }
}
