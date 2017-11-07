package org.apache.xml.dtm.ref.sax2dtm;

import java.io.PrintStream;
import java.util.Hashtable;
import java.util.Vector;
import javax.xml.transform.Source;
import javax.xml.transform.SourceLocator;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.dtm.DTMWSFilter;
import org.apache.xml.dtm.ref.DTMDefaultBaseIterators;
import org.apache.xml.dtm.ref.DTMManagerDefault;
import org.apache.xml.dtm.ref.DTMStringPool;
import org.apache.xml.dtm.ref.DTMTreeWalker;
import org.apache.xml.dtm.ref.ExpandedNameTable;
import org.apache.xml.dtm.ref.IncrementalSAXSource;
import org.apache.xml.dtm.ref.IncrementalSAXSource_Filter;
import org.apache.xml.dtm.ref.NodeLocator;
import org.apache.xml.res.XMLMessages;
import org.apache.xml.utils.FastStringBuffer;
import org.apache.xml.utils.IntStack;
import org.apache.xml.utils.IntVector;
import org.apache.xml.utils.StringVector;
import org.apache.xml.utils.SuballocatedIntVector;
import org.apache.xml.utils.SystemIDResolver;
import org.apache.xml.utils.WrappedRuntimeException;
import org.apache.xml.utils.XMLString;
import org.apache.xml.utils.XMLStringFactory;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;























public class SAX2DTM
  extends DTMDefaultBaseIterators
  implements EntityResolver, DTDHandler, ContentHandler, ErrorHandler, DeclHandler, LexicalHandler
{
  private static final boolean DEBUG = false;
  private IncrementalSAXSource m_incrementalSAXSource = null;
  






  protected FastStringBuffer m_chars;
  






  protected SuballocatedIntVector m_data;
  





  protected transient IntStack m_parents;
  





  protected transient int m_previous = 0;
  



  protected transient Vector m_prefixMappings = new Vector();
  



  protected transient IntStack m_contextIndexes;
  


  protected transient int m_textType = 3;
  




  protected transient int m_coalescedTextType = 3;
  

  protected transient Locator m_locator = null;
  

  private transient String m_systemId = null;
  

  protected transient boolean m_insideDTD = false;
  

  protected DTMTreeWalker m_walker = new DTMTreeWalker();
  


  protected DTMStringPool m_valuesOrPrefixes;
  


  protected boolean m_endDocumentOccured = false;
  



  protected SuballocatedIntVector m_dataOrQName;
  


  protected Hashtable m_idAttributes = new Hashtable();
  



  private static final String[] m_fixednames = { null, null, null, "#text", "#cdata_section", null, null, null, "#comment", "#document", null, "#document-fragment", null };
  











  private Vector m_entities = null;
  


  private static final int ENTITY_FIELD_PUBLICID = 0;
  


  private static final int ENTITY_FIELD_SYSTEMID = 1;
  


  private static final int ENTITY_FIELD_NOTATIONNAME = 2;
  


  private static final int ENTITY_FIELD_NAME = 3;
  

  private static final int ENTITY_FIELDS_PER = 4;
  

  protected int m_textPendingStart = -1;
  






  protected boolean m_useSourceLocationProperty = false;
  





  protected StringVector m_sourceSystemId;
  





  protected IntVector m_sourceLine;
  





  protected IntVector m_sourceColumn;
  





  public SAX2DTM(DTMManager mgr, Source source, int dtmIdentity, DTMWSFilter whiteSpaceFilter, XMLStringFactory xstringfactory, boolean doIndexing)
  {
    this(mgr, source, dtmIdentity, whiteSpaceFilter, xstringfactory, doIndexing, 512, true, false);
  }
  
























  public SAX2DTM(DTMManager mgr, Source source, int dtmIdentity, DTMWSFilter whiteSpaceFilter, XMLStringFactory xstringfactory, boolean doIndexing, int blocksize, boolean usePrevsib, boolean newNameTable)
  {
    super(mgr, source, dtmIdentity, whiteSpaceFilter, xstringfactory, doIndexing, blocksize, usePrevsib, newNameTable);
    



    if (blocksize <= 64)
    {
      m_data = new SuballocatedIntVector(blocksize, 4);
      m_dataOrQName = new SuballocatedIntVector(blocksize, 4);
      m_valuesOrPrefixes = new DTMStringPool(16);
      m_chars = new FastStringBuffer(7, 10);
      m_contextIndexes = new IntStack(4);
      m_parents = new IntStack(4);
    }
    else
    {
      m_data = new SuballocatedIntVector(blocksize, 32);
      m_dataOrQName = new SuballocatedIntVector(blocksize, 32);
      m_valuesOrPrefixes = new DTMStringPool();
      m_chars = new FastStringBuffer(10, 13);
      m_contextIndexes = new IntStack();
      m_parents = new IntStack();
    }
    





    m_data.addElement(0);
    



    m_useSourceLocationProperty = mgr.getSource_location();
    m_sourceSystemId = (m_useSourceLocationProperty ? new StringVector() : null);
    m_sourceLine = (m_useSourceLocationProperty ? new IntVector() : null);
    m_sourceColumn = (m_useSourceLocationProperty ? new IntVector() : null);
  }
  




  public void setUseSourceLocation(boolean useSourceLocation)
  {
    m_useSourceLocationProperty = useSourceLocation;
  }
  








  protected int _dataOrQName(int identity)
  {
    if (identity < m_size) {
      return m_dataOrQName.elementAt(identity);
    }
    


    for (;;)
    {
      boolean isMore = nextNode();
      
      if (!isMore)
        return -1;
      if (identity < m_size) {
        return m_dataOrQName.elementAt(identity);
      }
    }
  }
  


  public void clearCoRoutine()
  {
    clearCoRoutine(true);
  }
  








  public void clearCoRoutine(boolean callDoTerminate)
  {
    if (null != m_incrementalSAXSource)
    {
      if (callDoTerminate) {
        m_incrementalSAXSource.deliverMoreNodes(false);
      }
      m_incrementalSAXSource = null;
    }
  }
  




















  public void setIncrementalSAXSource(IncrementalSAXSource incrementalSAXSource)
  {
    m_incrementalSAXSource = incrementalSAXSource;
    

    incrementalSAXSource.setContentHandler(this);
    incrementalSAXSource.setLexicalHandler(this);
    incrementalSAXSource.setDTDHandler(this);
  }
  


















  public ContentHandler getContentHandler()
  {
    if ((m_incrementalSAXSource instanceof IncrementalSAXSource_Filter)) {
      return (ContentHandler)m_incrementalSAXSource;
    }
    return this;
  }
  











  public LexicalHandler getLexicalHandler()
  {
    if ((m_incrementalSAXSource instanceof IncrementalSAXSource_Filter)) {
      return (LexicalHandler)m_incrementalSAXSource;
    }
    return this;
  }
  





  public EntityResolver getEntityResolver()
  {
    return this;
  }
  





  public DTDHandler getDTDHandler()
  {
    return this;
  }
  





  public ErrorHandler getErrorHandler()
  {
    return this;
  }
  





  public DeclHandler getDeclHandler()
  {
    return this;
  }
  






  public boolean needsTwoThreads()
  {
    return null != m_incrementalSAXSource;
  }
  



















  public void dispatchCharactersEvents(int nodeHandle, ContentHandler ch, boolean normalize)
    throws SAXException
  {
    int identity = makeNodeIdentity(nodeHandle);
    
    if (identity == -1) {
      return;
    }
    int type = _type(identity);
    
    if (isTextType(type))
    {
      int dataIndex = m_dataOrQName.elementAt(identity);
      int offset = m_data.elementAt(dataIndex);
      int length = m_data.elementAt(dataIndex + 1);
      
      if (normalize) {
        m_chars.sendNormalizedSAXcharacters(ch, offset, length);
      } else {
        m_chars.sendSAXcharacters(ch, offset, length);
      }
    }
    else {
      int firstChild = _firstch(identity);
      
      if (-1 != firstChild)
      {
        int offset = -1;
        int length = 0;
        int startNode = identity;
        
        identity = firstChild;
        do
        {
          type = _type(identity);
          
          if (isTextType(type))
          {
            int dataIndex = _dataOrQName(identity);
            
            if (-1 == offset)
            {
              offset = m_data.elementAt(dataIndex);
            }
            
            length += m_data.elementAt(dataIndex + 1);
          }
          
          identity = getNextNodeIdentity(identity);
        } while ((-1 != identity) && (_parent(identity) >= startNode));
        
        if (length > 0)
        {
          if (normalize) {
            m_chars.sendNormalizedSAXcharacters(ch, offset, length);
          } else {
            m_chars.sendSAXcharacters(ch, offset, length);
          }
        }
      } else if (type != 1)
      {
        int dataIndex = _dataOrQName(identity);
        
        if (dataIndex < 0)
        {
          dataIndex = -dataIndex;
          dataIndex = m_data.elementAt(dataIndex + 1);
        }
        
        String str = m_valuesOrPrefixes.indexToString(dataIndex);
        
        if (normalize) {
          FastStringBuffer.sendNormalizedSAXcharacters(str.toCharArray(), 0, str.length(), ch);
        }
        else {
          ch.characters(str.toCharArray(), 0, str.length());
        }
      }
    }
  }
  










  public String getNodeName(int nodeHandle)
  {
    int expandedTypeID = getExpandedTypeID(nodeHandle);
    
    int namespaceID = m_expandedNameTable.getNamespaceID(expandedTypeID);
    
    if (0 == namespaceID)
    {


      int type = getNodeType(nodeHandle);
      
      if (type == 13)
      {
        if (null == m_expandedNameTable.getLocalName(expandedTypeID)) {
          return "xmlns";
        }
        return "xmlns:" + m_expandedNameTable.getLocalName(expandedTypeID);
      }
      if (0 == m_expandedNameTable.getLocalNameID(expandedTypeID))
      {
        return m_fixednames[type];
      }
      
      return m_expandedNameTable.getLocalName(expandedTypeID);
    }
    

    int qnameIndex = m_dataOrQName.elementAt(makeNodeIdentity(nodeHandle));
    
    if (qnameIndex < 0)
    {
      qnameIndex = -qnameIndex;
      qnameIndex = m_data.elementAt(qnameIndex);
    }
    
    return m_valuesOrPrefixes.indexToString(qnameIndex);
  }
  










  public String getNodeNameX(int nodeHandle)
  {
    int expandedTypeID = getExpandedTypeID(nodeHandle);
    int namespaceID = m_expandedNameTable.getNamespaceID(expandedTypeID);
    
    if (0 == namespaceID)
    {
      String name = m_expandedNameTable.getLocalName(expandedTypeID);
      
      if (name == null) {
        return "";
      }
      return name;
    }
    

    int qnameIndex = m_dataOrQName.elementAt(makeNodeIdentity(nodeHandle));
    
    if (qnameIndex < 0)
    {
      qnameIndex = -qnameIndex;
      qnameIndex = m_data.elementAt(qnameIndex);
    }
    
    return m_valuesOrPrefixes.indexToString(qnameIndex);
  }
  












  public boolean isAttributeSpecified(int attributeHandle)
  {
    return true;
  }
  










  public String getDocumentTypeDeclarationSystemIdentifier()
  {
    error(XMLMessages.createXMLMessage("ER_METHOD_NOT_SUPPORTED", null));
    
    return null;
  }
  





  protected int getNextNodeIdentity(int identity)
  {
    
    



    while (identity >= m_size)
    {
      if (null == m_incrementalSAXSource) {
        return -1;
      }
      nextNode();
    }
    
    return identity;
  }
  









  public void dispatchToEvents(int nodeHandle, ContentHandler ch)
    throws SAXException
  {
    DTMTreeWalker treeWalker = m_walker;
    ContentHandler prevCH = treeWalker.getcontentHandler();
    
    if (null != prevCH)
    {
      treeWalker = new DTMTreeWalker();
    }
    
    treeWalker.setcontentHandler(ch);
    treeWalker.setDTM(this);
    
    try
    {
      treeWalker.traverse(nodeHandle);
    }
    finally
    {
      treeWalker.setcontentHandler(null);
    }
  }
  





  public int getNumberOfNodes()
  {
    return m_size;
  }
  







  protected boolean nextNode()
  {
    if (null == m_incrementalSAXSource) {
      return false;
    }
    if (m_endDocumentOccured)
    {
      clearCoRoutine();
      
      return false;
    }
    
    Object gotMore = m_incrementalSAXSource.deliverMoreNodes(true);
    







    if (!(gotMore instanceof Boolean))
    {
      if ((gotMore instanceof RuntimeException))
      {
        throw ((RuntimeException)gotMore);
      }
      if ((gotMore instanceof Exception))
      {
        throw new WrappedRuntimeException((Exception)gotMore);
      }
      
      clearCoRoutine();
      
      return false;
    }
    


    if (gotMore != Boolean.TRUE)
    {


      clearCoRoutine();
    }
    


    return true;
  }
  







  private final boolean isTextType(int type)
  {
    return (3 == type) || (4 == type);
  }
  
































  protected int addNode(int type, int expandedTypeID, int parentIndex, int previousSibling, int dataOrPrefix, boolean canHaveFirstChild)
  {
    int nodeIndex = m_size++;
    

    if (m_dtmIdent.size() == nodeIndex >>> 16)
    {
      addNewDTMID(nodeIndex);
    }
    
    m_firstch.addElement(canHaveFirstChild ? -2 : -1);
    m_nextsib.addElement(-2);
    m_parent.addElement(parentIndex);
    m_exptype.addElement(expandedTypeID);
    m_dataOrQName.addElement(dataOrPrefix);
    
    if (m_prevsib != null) {
      m_prevsib.addElement(previousSibling);
    }
    
    if (-1 != previousSibling) {
      m_nextsib.setElementAt(nodeIndex, previousSibling);
    }
    
    if ((m_locator != null) && (m_useSourceLocationProperty)) {
      setSourceLocation();
    }
    




    switch (type)
    {
    case 13: 
      declareNamespaceInContext(parentIndex, nodeIndex);
      break;
    case 2: 
      break;
    default: 
      if ((-1 == previousSibling) && (-1 != parentIndex)) {
        m_firstch.setElementAt(nodeIndex, parentIndex);
      }
      break;
    }
    
    return nodeIndex;
  }
  




  protected void addNewDTMID(int nodeIndex)
  {
    try
    {
      if (m_mgr == null) {
        throw new ClassCastException();
      }
      
      DTMManagerDefault mgrD = (DTMManagerDefault)m_mgr;
      int id = mgrD.getFirstFreeDTMID();
      mgrD.addDTM(this, id, nodeIndex);
      m_dtmIdent.addElement(id << 16);


    }
    catch (ClassCastException e)
    {

      error(XMLMessages.createXMLMessage("ER_NO_DTMIDS_AVAIL", null));
    }
  }
  






  public void migrateTo(DTMManager manager)
  {
    super.migrateTo(manager);
    


    int numDTMs = m_dtmIdent.size();
    int dtmId = m_mgrDefault.getFirstFreeDTMID();
    int nodeIndex = 0;
    for (int i = 0; i < numDTMs; i++)
    {
      m_dtmIdent.setElementAt(dtmId << 16, i);
      m_mgrDefault.addDTM(this, dtmId, nodeIndex);
      dtmId++;
      nodeIndex += 65536;
    }
  }
  



  protected void setSourceLocation()
  {
    m_sourceSystemId.addElement(m_locator.getSystemId());
    m_sourceLine.addElement(m_locator.getLineNumber());
    m_sourceColumn.addElement(m_locator.getColumnNumber());
    



    if (m_sourceSystemId.size() != m_size) {
      String msg = "CODING ERROR in Source Location: " + m_size + " != " + m_sourceSystemId.size();
      
      System.err.println(msg);
      throw new RuntimeException(msg);
    }
  }
  










  public String getNodeValue(int nodeHandle)
  {
    int identity = makeNodeIdentity(nodeHandle);
    int type = _type(identity);
    
    if (isTextType(type))
    {
      int dataIndex = _dataOrQName(identity);
      int offset = m_data.elementAt(dataIndex);
      int length = m_data.elementAt(dataIndex + 1);
      

      return m_chars.getString(offset, length);
    }
    if ((1 == type) || (11 == type) || (9 == type))
    {

      return null;
    }
    

    int dataIndex = _dataOrQName(identity);
    
    if (dataIndex < 0)
    {
      dataIndex = -dataIndex;
      dataIndex = m_data.elementAt(dataIndex + 1);
    }
    
    return m_valuesOrPrefixes.indexToString(dataIndex);
  }
  









  public String getLocalName(int nodeHandle)
  {
    return m_expandedNameTable.getLocalName(_exptype(makeNodeIdentity(nodeHandle)));
  }
  



































  public String getUnparsedEntityURI(String name)
  {
    String url = "";
    
    if (null == m_entities) {
      return url;
    }
    int n = m_entities.size();
    
    for (int i = 0; i < n; i += 4)
    {
      String ename = (String)m_entities.elementAt(i + 3);
      
      if ((null != ename) && (ename.equals(name)))
      {
        String nname = (String)m_entities.elementAt(i + 2);
        

        if (null == nname) {
          break;
        }
        








        url = (String)m_entities.elementAt(i + 1);
        
        if (null != url)
          break;
        url = (String)m_entities.elementAt(i + 0); break;
      }
    }
    




    return url;
  }
  













  public String getPrefix(int nodeHandle)
  {
    int identity = makeNodeIdentity(nodeHandle);
    int type = _type(identity);
    
    if (1 == type)
    {
      int prefixIndex = _dataOrQName(identity);
      
      if (0 == prefixIndex) {
        return "";
      }
      
      String qname = m_valuesOrPrefixes.indexToString(prefixIndex);
      
      return getPrefix(qname, null);
    }
    
    if (2 == type)
    {
      int prefixIndex = _dataOrQName(identity);
      
      if (prefixIndex < 0)
      {
        prefixIndex = m_data.elementAt(-prefixIndex);
        
        String qname = m_valuesOrPrefixes.indexToString(prefixIndex);
        
        return getPrefix(qname, null);
      }
    }
    
    return "";
  }
  














  public int getAttributeNode(int nodeHandle, String namespaceURI, String name)
  {
    for (int attrH = getFirstAttribute(nodeHandle); -1 != attrH; 
        attrH = getNextAttribute(attrH))
    {
      String attrNS = getNamespaceURI(attrH);
      String attrName = getLocalName(attrH);
      boolean nsMatch = (namespaceURI == attrNS) || ((namespaceURI != null) && (namespaceURI.equals(attrNS)));
      


      if ((nsMatch) && (name.equals(attrName))) {
        return attrH;
      }
    }
    return -1;
  }
  










  public String getDocumentTypeDeclarationPublicIdentifier()
  {
    error(XMLMessages.createXMLMessage("ER_METHOD_NOT_SUPPORTED", null));
    
    return null;
  }
  












  public String getNamespaceURI(int nodeHandle)
  {
    return m_expandedNameTable.getNamespace(_exptype(makeNodeIdentity(nodeHandle)));
  }
  









  public XMLString getStringValue(int nodeHandle)
  {
    int identity = makeNodeIdentity(nodeHandle);
    int type;
    int type; if (identity == -1) {
      type = -1;
    } else {
      type = _type(identity);
    }
    if (isTextType(type))
    {
      int dataIndex = _dataOrQName(identity);
      int offset = m_data.elementAt(dataIndex);
      int length = m_data.elementAt(dataIndex + 1);
      
      return m_xstrf.newstr(m_chars, offset, length);
    }
    

    int firstChild = _firstch(identity);
    
    if (-1 != firstChild)
    {
      int offset = -1;
      int length = 0;
      int startNode = identity;
      
      identity = firstChild;
      do
      {
        type = _type(identity);
        
        if (isTextType(type))
        {
          int dataIndex = _dataOrQName(identity);
          
          if (-1 == offset)
          {
            offset = m_data.elementAt(dataIndex);
          }
          
          length += m_data.elementAt(dataIndex + 1);
        }
        
        identity = getNextNodeIdentity(identity);
      } while ((-1 != identity) && (_parent(identity) >= startNode));
      
      if (length > 0)
      {
        return m_xstrf.newstr(m_chars, offset, length);
      }
    }
    else if (type != 1)
    {
      int dataIndex = _dataOrQName(identity);
      
      if (dataIndex < 0)
      {
        dataIndex = -dataIndex;
        dataIndex = m_data.elementAt(dataIndex + 1);
      }
      return m_xstrf.newstr(m_valuesOrPrefixes.indexToString(dataIndex));
    }
    

    return m_xstrf.emptystr();
  }
  







  public boolean isWhitespace(int nodeHandle)
  {
    int identity = makeNodeIdentity(nodeHandle);
    int type;
    int type; if (identity == -1) {
      type = -1;
    } else {
      type = _type(identity);
    }
    if (isTextType(type))
    {
      int dataIndex = _dataOrQName(identity);
      int offset = m_data.elementAt(dataIndex);
      int length = m_data.elementAt(dataIndex + 1);
      
      return m_chars.isWhitespace(offset, length);
    }
    return false;
  }
  



















  public int getElementById(String elementId)
  {
    boolean isMore = true;
    Integer intObj;
    do
    {
      intObj = (Integer)m_idAttributes.get(elementId);
      
      if (null != intObj) {
        return makeNodeHandle(intObj.intValue());
      }
      if ((!isMore) || (m_endDocumentOccured)) {
        break;
      }
      isMore = nextNode();
    }
    while (null == intObj);
    
    return -1;
  }
  











  public String getPrefix(String qname, String uri)
  {
    int uriIndex = -1;
    String prefix;
    String prefix; if ((null != uri) && (uri.length() > 0))
    {

      do
      {
        uriIndex = m_prefixMappings.indexOf(uri, ++uriIndex);
      } while ((uriIndex & 0x1) == 0);
      String prefix;
      if (uriIndex >= 0)
      {
        prefix = (String)m_prefixMappings.elementAt(uriIndex - 1);
      } else { String prefix;
        if (null != qname)
        {
          int indexOfNSSep = qname.indexOf(':');
          String prefix;
          if (qname.equals("xmlns")) {
            prefix = ""; } else { String prefix;
            if (qname.startsWith("xmlns:")) {
              prefix = qname.substring(indexOfNSSep + 1);
            } else {
              prefix = indexOfNSSep > 0 ? qname.substring(0, indexOfNSSep) : null;
            }
          }
        }
        else {
          prefix = null;
        }
      } } else { String prefix;
      if (null != qname)
      {
        int indexOfNSSep = qname.indexOf(':');
        String prefix;
        if (indexOfNSSep > 0) {
          String prefix;
          if (qname.startsWith("xmlns:")) {
            prefix = qname.substring(indexOfNSSep + 1);
          } else {
            prefix = qname.substring(0, indexOfNSSep);
          }
        } else {
          String prefix;
          if (qname.equals("xmlns")) {
            prefix = "";
          } else {
            prefix = null;
          }
        }
      }
      else {
        prefix = null;
      }
    }
    return prefix;
  }
  









  public int getIdForNamespace(String uri)
  {
    return m_valuesOrPrefixes.stringToIndex(uri);
  }
  








  public String getNamespaceURI(String prefix)
  {
    String uri = "";
    int prefixIndex = m_contextIndexes.peek() - 1;
    
    if (null == prefix) {
      prefix = "";
    }
    do
    {
      prefixIndex = m_prefixMappings.indexOf(prefix, ++prefixIndex);
    } while ((prefixIndex >= 0) && ((prefixIndex & 0x1) == 1));
    
    if (prefixIndex > -1)
    {
      uri = (String)m_prefixMappings.elementAt(prefixIndex + 1);
    }
    

    return uri;
  }
  






  public void setIDAttribute(String id, int elem)
  {
    m_idAttributes.put(id, new Integer(elem));
  }
  





  protected void charactersFlush()
  {
    if (m_textPendingStart >= 0)
    {
      int length = m_chars.size() - m_textPendingStart;
      boolean doStrip = false;
      
      if (getShouldStripWhitespace())
      {
        doStrip = m_chars.isWhitespace(m_textPendingStart, length);
      }
      
      if (doStrip) {
        m_chars.setLength(m_textPendingStart);


      }
      else if (length > 0) {
        int exName = m_expandedNameTable.getExpandedTypeID(3);
        int dataIndex = m_data.size();
        
        m_previous = addNode(m_coalescedTextType, exName, m_parents.peek(), m_previous, dataIndex, false);
        

        m_data.addElement(m_textPendingStart);
        m_data.addElement(length);
      }
      


      m_textPendingStart = -1;
      m_textType = (this.m_coalescedTextType = 3);
    }
  }
  

























  public InputSource resolveEntity(String publicId, String systemId)
    throws SAXException
  {
    return null;
  }
  






















  public void notationDecl(String name, String publicId, String systemId)
    throws SAXException
  {}
  






















  public void unparsedEntityDecl(String name, String publicId, String systemId, String notationName)
    throws SAXException
  {
    if (null == m_entities)
    {
      m_entities = new Vector();
    }
    
    try
    {
      systemId = SystemIDResolver.getAbsoluteURI(systemId, getDocumentBaseURI());

    }
    catch (Exception e)
    {
      throw new SAXException(e);
    }
    

    m_entities.addElement(publicId);
    

    m_entities.addElement(systemId);
    

    m_entities.addElement(notationName);
    

    m_entities.addElement(name);
  }
  















  public void setDocumentLocator(Locator locator)
  {
    m_locator = locator;
    m_systemId = locator.getSystemId();
  }
  










  public void startDocument()
    throws SAXException
  {
    int doc = addNode(9, m_expandedNameTable.getExpandedTypeID(9), -1, -1, 0, true);
    


    m_parents.push(doc);
    m_previous = -1;
    
    m_contextIndexes.push(m_prefixMappings.size());
  }
  









  public void endDocument()
    throws SAXException
  {
    charactersFlush();
    
    m_nextsib.setElementAt(-1, 0);
    
    if (m_firstch.elementAt(0) == -2) {
      m_firstch.setElementAt(-1, 0);
    }
    if (-1 != m_previous) {
      m_nextsib.setElementAt(-1, m_previous);
    }
    m_parents = null;
    m_prefixMappings = null;
    m_contextIndexes = null;
    
    m_endDocumentOccured = true;
    

    m_locator = null;
  }
  


















  public void startPrefixMapping(String prefix, String uri)
    throws SAXException
  {
    if (null == prefix)
      prefix = "";
    m_prefixMappings.addElement(prefix);
    m_prefixMappings.addElement(uri);
  }
  














  public void endPrefixMapping(String prefix)
    throws SAXException
  {
    if (null == prefix) {
      prefix = "";
    }
    int index = m_contextIndexes.peek() - 1;
    
    do
    {
      index = m_prefixMappings.indexOf(prefix, ++index);
    } while ((index >= 0) && ((index & 0x1) == 1));
    

    if (index > -1)
    {
      m_prefixMappings.setElementAt("%@$#^@#", index);
      m_prefixMappings.setElementAt("%@$#^@#", index + 1);
    }
  }
  











  protected boolean declAlreadyDeclared(String prefix)
  {
    int startDecls = m_contextIndexes.peek();
    Vector prefixMappings = m_prefixMappings;
    int nDecls = prefixMappings.size();
    
    for (int i = startDecls; i < nDecls; i += 2)
    {
      String prefixDecl = (String)prefixMappings.elementAt(i);
      
      if (prefixDecl != null)
      {

        if (prefixDecl.equals(prefix))
          return true;
      }
    }
    return false;
  }
  
  boolean m_pastFirstElement = false;
  











































  public void startElement(String uri, String localName, String qName, Attributes attributes)
    throws SAXException
  {
    charactersFlush();
    
    int exName = m_expandedNameTable.getExpandedTypeID(uri, localName, 1);
    String prefix = getPrefix(qName, uri);
    int prefixIndex = null != prefix ? m_valuesOrPrefixes.stringToIndex(qName) : 0;
    

    int elemNode = addNode(1, exName, m_parents.peek(), m_previous, prefixIndex, true);
    

    if (m_indexing) {
      indexNode(exName, elemNode);
    }
    
    m_parents.push(elemNode);
    
    int startDecls = m_contextIndexes.peek();
    int nDecls = m_prefixMappings.size();
    int prev = -1;
    
    if (!m_pastFirstElement)
    {

      prefix = "xml";
      String declURL = "http://www.w3.org/XML/1998/namespace";
      exName = m_expandedNameTable.getExpandedTypeID(null, prefix, 13);
      int val = m_valuesOrPrefixes.stringToIndex(declURL);
      prev = addNode(13, exName, elemNode, prev, val, false);
      
      m_pastFirstElement = true;
    }
    
    for (int i = startDecls; i < nDecls; i += 2)
    {
      prefix = (String)m_prefixMappings.elementAt(i);
      
      if (prefix != null)
      {

        String declURL = (String)m_prefixMappings.elementAt(i + 1);
        
        exName = m_expandedNameTable.getExpandedTypeID(null, prefix, 13);
        
        int val = m_valuesOrPrefixes.stringToIndex(declURL);
        
        prev = addNode(13, exName, elemNode, prev, val, false);
      }
    }
    
    int n = attributes.getLength();
    
    for (int i = 0; i < n; i++)
    {
      String attrUri = attributes.getURI(i);
      String attrQName = attributes.getQName(i);
      String valString = attributes.getValue(i);
      
      prefix = getPrefix(attrQName, attrUri);
      


      String attrLocalName = attributes.getLocalName(i);
      int nodeType;
      int nodeType; if ((null != attrQName) && ((attrQName.equals("xmlns")) || (attrQName.startsWith("xmlns:"))))
      {


        if (declAlreadyDeclared(prefix)) {
          continue;
        }
        nodeType = 13;
      }
      else
      {
        nodeType = 2;
        
        if (attributes.getType(i).equalsIgnoreCase("ID")) {
          setIDAttribute(valString, elemNode);
        }
      }
      

      if (null == valString) {
        valString = "";
      }
      int val = m_valuesOrPrefixes.stringToIndex(valString);
      

      if (null != prefix)
      {

        prefixIndex = m_valuesOrPrefixes.stringToIndex(attrQName);
        
        int dataIndex = m_data.size();
        
        m_data.addElement(prefixIndex);
        m_data.addElement(val);
        
        val = -dataIndex;
      }
      
      exName = m_expandedNameTable.getExpandedTypeID(attrUri, attrLocalName, nodeType);
      prev = addNode(nodeType, exName, elemNode, prev, val, false);
    }
    

    if (-1 != prev) {
      m_nextsib.setElementAt(-1, prev);
    }
    if (null != m_wsfilter)
    {
      short wsv = m_wsfilter.getShouldStripSpace(makeNodeHandle(elemNode), this);
      boolean shouldStrip = 2 == wsv ? true : 3 == wsv ? getShouldStripWhitespace() : false;
      


      pushShouldStripWhitespace(shouldStrip);
    }
    
    m_previous = -1;
    
    m_contextIndexes.push(m_prefixMappings.size());
  }
  
























  public void endElement(String uri, String localName, String qName)
    throws SAXException
  {
    charactersFlush();
    


    m_contextIndexes.quickPop(1);
    

    int topContextIndex = m_contextIndexes.peek();
    if (topContextIndex != m_prefixMappings.size()) {
      m_prefixMappings.setSize(topContextIndex);
    }
    
    int lastNode = m_previous;
    
    m_previous = m_parents.pop();
    

    if (-1 == lastNode) {
      m_firstch.setElementAt(-1, m_previous);
    } else {
      m_nextsib.setElementAt(-1, lastNode);
    }
    popShouldStripWhitespace();
  }
  















  public void characters(char[] ch, int start, int length)
    throws SAXException
  {
    if (m_textPendingStart == -1)
    {
      m_textPendingStart = m_chars.size();
      m_coalescedTextType = m_textType;




    }
    else if (m_textType == 3)
    {
      m_coalescedTextType = 3;
    }
    
    m_chars.append(ch, start, length);
  }
  



















  public void ignorableWhitespace(char[] ch, int start, int length)
    throws SAXException
  {
    characters(ch, start, length);
  }
  


















  public void processingInstruction(String target, String data)
    throws SAXException
  {
    charactersFlush();
    
    int exName = m_expandedNameTable.getExpandedTypeID(null, target, 7);
    
    int dataIndex = m_valuesOrPrefixes.stringToIndex(data);
    
    m_previous = addNode(7, exName, m_parents.peek(), m_previous, dataIndex, false);
  }
  



















  public void skippedEntity(String name)
    throws SAXException
  {}
  


















  public void warning(SAXParseException e)
    throws SAXException
  {
    System.err.println(e.getMessage());
  }
  













  public void error(SAXParseException e)
    throws SAXException
  {
    throw e;
  }
  
















  public void fatalError(SAXParseException e)
    throws SAXException
  {
    throw e;
  }
  





















  public void elementDecl(String name, String model)
    throws SAXException
  {}
  





















  public void attributeDecl(String eName, String aName, String type, String valueDefault, String value)
    throws SAXException
  {}
  




















  public void internalEntityDecl(String name, String value)
    throws SAXException
  {}
  




















  public void externalEntityDecl(String name, String publicId, String systemId)
    throws SAXException
  {}
  




















  public void startDTD(String name, String publicId, String systemId)
    throws SAXException
  {
    m_insideDTD = true;
  }
  






  public void endDTD()
    throws SAXException
  {
    m_insideDTD = false;
  }
  














  public void startEntity(String name)
    throws SAXException
  {}
  














  public void endEntity(String name)
    throws SAXException
  {}
  














  public void startCDATA()
    throws SAXException
  {
    m_textType = 4;
  }
  





  public void endCDATA()
    throws SAXException
  {
    m_textType = 3;
  }
  












  public void comment(char[] ch, int start, int length)
    throws SAXException
  {
    if (m_insideDTD) {
      return;
    }
    charactersFlush();
    
    int exName = m_expandedNameTable.getExpandedTypeID(8);
    


    int dataIndex = m_valuesOrPrefixes.stringToIndex(new String(ch, start, length));
    


    m_previous = addNode(8, exName, m_parents.peek(), m_previous, dataIndex, false);
  }
  










  public void setProperty(String property, Object value) {}
  










  public SourceLocator getSourceLocatorFor(int node)
  {
    if (m_useSourceLocationProperty)
    {

      node = makeNodeIdentity(node);
      

      return new NodeLocator(null, m_sourceSystemId.elementAt(node), m_sourceLine.elementAt(node), m_sourceColumn.elementAt(node));
    }
    


    if (m_locator != null)
    {
      return new NodeLocator(null, m_locator.getSystemId(), -1, -1);
    }
    if (m_systemId != null)
    {
      return new NodeLocator(null, m_systemId, -1, -1);
    }
    return null;
  }
  
  public String getFixedNames(int type) {
    return m_fixednames[type];
  }
}
