package org.apache.xml.dtm.ref;

import java.io.PrintStream;
import javax.xml.transform.SourceLocator;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.DTMAxisTraverser;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.dtm.DTMWSFilter;
import org.apache.xml.utils.FastStringBuffer;
import org.apache.xml.utils.XMLString;
import org.apache.xml.utils.XMLStringFactory;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;















































public class DTMDocumentImpl
  implements DTM, ContentHandler, LexicalHandler
{
  protected static final byte DOCHANDLE_SHIFT = 22;
  protected static final int NODEHANDLE_MASK = 8388607;
  protected static final int DOCHANDLE_MASK = -8388608;
  int m_docHandle = -1;
  int m_docElement = -1;
  

  int currentParent = 0;
  int previousSibling = 0;
  protected int m_currentNode = -1;
  




  private boolean previousSiblingWasParent = false;
  
  int[] gotslot = new int[4];
  

  private boolean done = false;
  boolean m_isError = false;
  




  private static final boolean DEBUG = false;
  




  protected String m_documentBaseURI;
  




  private IncrementalSAXSource m_incrSAXSource = null;
  







  ChunkedIntArray nodes = new ChunkedIntArray(4);
  


  private FastStringBuffer m_char = new FastStringBuffer();
  

  private int m_char_current_start = 0;
  





  private DTMStringPool m_localNames = new DTMStringPool();
  private DTMStringPool m_nsNames = new DTMStringPool();
  private DTMStringPool m_prefixNames = new DTMStringPool();
  






  private ExpandedNameTable m_expandedNames = new ExpandedNameTable();
  






  private XMLStringFactory m_xsf;
  







  public DTMDocumentImpl(DTMManager mgr, int documentNumber, DTMWSFilter whiteSpaceFilter, XMLStringFactory xstringfactory)
  {
    initDocument(documentNumber);
    m_xsf = xstringfactory;
  }
  











  public void setIncrementalSAXSource(IncrementalSAXSource source)
  {
    m_incrSAXSource = source;
    

    source.setContentHandler(this);
    source.setLexicalHandler(this);
  }
  



















  private final int appendNode(int w0, int w1, int w2, int w3)
  {
    int slotnumber = nodes.appendSlot(w0, w1, w2, w3);
    


    if (previousSiblingWasParent) {
      nodes.writeEntry(previousSibling, 2, slotnumber);
    }
    previousSiblingWasParent = false;
    
    return slotnumber;
  }
  








  public void setFeature(String featureId, boolean state) {}
  








  public void setLocalNameTable(DTMStringPool poolRef)
  {
    m_localNames = poolRef;
  }
  




  public DTMStringPool getLocalNameTable()
  {
    return m_localNames;
  }
  






  public void setNsNameTable(DTMStringPool poolRef)
  {
    m_nsNames = poolRef;
  }
  




  public DTMStringPool getNsNameTable()
  {
    return m_nsNames;
  }
  






  public void setPrefixNameTable(DTMStringPool poolRef)
  {
    m_prefixNames = poolRef;
  }
  




  public DTMStringPool getPrefixNameTable()
  {
    return m_prefixNames;
  }
  





  void setContentBuffer(FastStringBuffer buffer)
  {
    m_char = buffer;
  }
  




  FastStringBuffer getContentBuffer()
  {
    return m_char;
  }
  









  public ContentHandler getContentHandler()
  {
    if ((m_incrSAXSource instanceof IncrementalSAXSource_Filter)) {
      return (ContentHandler)m_incrSAXSource;
    }
    return this;
  }
  











  public LexicalHandler getLexicalHandler()
  {
    if ((m_incrSAXSource instanceof IncrementalSAXSource_Filter)) {
      return (LexicalHandler)m_incrSAXSource;
    }
    return this;
  }
  






  public EntityResolver getEntityResolver()
  {
    return null;
  }
  






  public DTDHandler getDTDHandler()
  {
    return null;
  }
  






  public ErrorHandler getErrorHandler()
  {
    return null;
  }
  






  public DeclHandler getDeclHandler()
  {
    return null;
  }
  





  public boolean needsTwoThreads()
  {
    return null != m_incrSAXSource;
  }
  








  public void characters(char[] ch, int start, int length)
    throws SAXException
  {
    m_char.append(ch, start, length);
  }
  

  private void processAccumulatedText()
  {
    int len = m_char.length();
    if (len != m_char_current_start)
    {

      appendTextChild(m_char_current_start, len - m_char_current_start);
      m_char_current_start = len;
    }
  }
  

  public void endDocument()
    throws SAXException
  {
    appendEndDocument();
  }
  
  public void endElement(String namespaceURI, String localName, String qName)
    throws SAXException
  {
    processAccumulatedText();
    

    appendEndElement();
  }
  

  public void endPrefixMapping(String prefix)
    throws SAXException
  {}
  
  public void ignorableWhitespace(char[] ch, int start, int length)
    throws SAXException
  {}
  
  public void processingInstruction(String target, String data)
    throws SAXException
  {
    processAccumulatedText();
  }
  

  public void setDocumentLocator(Locator locator) {}
  

  public void skippedEntity(String name)
    throws SAXException
  {
    processAccumulatedText();
  }
  
  public void startDocument()
    throws SAXException
  {
    appendStartDocument();
  }
  
  public void startElement(String namespaceURI, String localName, String qName, Attributes atts)
    throws SAXException
  {
    processAccumulatedText();
    

    String prefix = null;
    int colon = qName.indexOf(':');
    if (colon > 0) {
      prefix = qName.substring(0, colon);
    }
    
    System.out.println("Prefix=" + prefix + " index=" + m_prefixNames.stringToIndex(prefix));
    appendStartElement(m_nsNames.stringToIndex(namespaceURI), m_localNames.stringToIndex(localName), m_prefixNames.stringToIndex(prefix));
    





    int nAtts = atts == null ? 0 : atts.getLength();
    
    for (int i = nAtts - 1; i >= 0; i--)
    {
      qName = atts.getQName(i);
      if ((qName.startsWith("xmlns:")) || ("xmlns".equals(qName)))
      {
        prefix = null;
        colon = qName.indexOf(':');
        if (colon > 0)
        {
          prefix = qName.substring(0, colon);

        }
        else
        {
          prefix = null;
        }
        

        appendNSDeclaration(m_prefixNames.stringToIndex(prefix), m_nsNames.stringToIndex(atts.getValue(i)), atts.getType(i).equalsIgnoreCase("ID"));
      }
    }
    



    for (int i = nAtts - 1; i >= 0; i--)
    {
      qName = atts.getQName(i);
      if ((!qName.startsWith("xmlns:")) && (!"xmlns".equals(qName)))
      {



        prefix = null;
        colon = qName.indexOf(':');
        if (colon > 0)
        {
          prefix = qName.substring(0, colon);
          localName = qName.substring(colon + 1);
        }
        else
        {
          prefix = "";
          localName = qName;
        }
        

        m_char.append(atts.getValue(i));
        int contentEnd = m_char.length();
        
        if ((!"xmlns".equals(prefix)) && (!"xmlns".equals(qName))) {
          appendAttribute(m_nsNames.stringToIndex(atts.getURI(i)), m_localNames.stringToIndex(localName), m_prefixNames.stringToIndex(prefix), atts.getType(i).equalsIgnoreCase("ID"), m_char_current_start, contentEnd - m_char_current_start);
        }
        


        m_char_current_start = contentEnd;
      }
    }
  }
  



  public void startPrefixMapping(String prefix, String uri)
    throws SAXException
  {}
  


  public void comment(char[] ch, int start, int length)
    throws SAXException
  {
    processAccumulatedText();
    
    m_char.append(ch, start, length);
    appendComment(m_char_current_start, length);
    m_char_current_start += length;
  }
  




  public void endCDATA()
    throws SAXException
  {}
  



  public void endDTD()
    throws SAXException
  {}
  



  public void endEntity(String name)
    throws SAXException
  {}
  



  public void startCDATA()
    throws SAXException
  {}
  



  public void startDTD(String name, String publicId, String systemId)
    throws SAXException
  {}
  



  public void startEntity(String name)
    throws SAXException
  {}
  



  final void initDocument(int documentNumber)
  {
    m_docHandle = (documentNumber << 22);
    

    nodes.writeSlot(0, 9, -1, -1, 0);
    
    done = false;
  }
  












































































































































































































































































































































































  public boolean hasChildNodes(int nodeHandle)
  {
    return getFirstChild(nodeHandle) != -1;
  }
  









  public int getFirstChild(int nodeHandle)
  {
    nodeHandle &= 0x7FFFFF;
    
    nodes.readSlot(nodeHandle, gotslot);
    

    short type = (short)(gotslot[0] & 0xFFFF);
    

    if ((type == 1) || (type == 9) || (type == 5))
    {








      int kid = nodeHandle + 1;
      nodes.readSlot(kid, gotslot);
      while (2 == (gotslot[0] & 0xFFFF))
      {
        kid = gotslot[2];
        
        if (kid == -1) return -1;
        nodes.readSlot(kid, gotslot);
      }
      
      if (gotslot[1] == nodeHandle)
      {
        int firstChild = kid | m_docHandle;
        
        return firstChild;
      }
    }
    

    return -1;
  }
  









  public int getLastChild(int nodeHandle)
  {
    nodeHandle &= 0x7FFFFF;
    
    int lastChild = -1;
    for (int nextkid = getFirstChild(nodeHandle); nextkid != -1; 
        nextkid = getNextSibling(nextkid)) {
      lastChild = nextkid;
    }
    return lastChild | m_docHandle;
  }
  











  public int getAttributeNode(int nodeHandle, String namespaceURI, String name)
  {
    int nsIndex = m_nsNames.stringToIndex(namespaceURI);
    int nameIndex = m_localNames.stringToIndex(name);
    nodeHandle &= 0x7FFFFF;
    nodes.readSlot(nodeHandle, gotslot);
    short type = (short)(gotslot[0] & 0xFFFF);
    
    if (type == 1) {
      nodeHandle++;
    }
    while (type == 2) {
      if ((nsIndex == gotslot[0] << 16) && (gotslot[3] == nameIndex)) {
        return nodeHandle | m_docHandle;
      }
      nodeHandle = gotslot[2];
      nodes.readSlot(nodeHandle, gotslot);
    }
    return -1;
  }
  





  public int getFirstAttribute(int nodeHandle)
  {
    nodeHandle &= 0x7FFFFF;
    






    if (1 != (nodes.readEntry(nodeHandle, 0) & 0xFFFF)) {
      return -1;
    }
    nodeHandle++;
    return 2 == (nodes.readEntry(nodeHandle, 0) & 0xFFFF) ? nodeHandle | m_docHandle : -1;
  }
  














  public int getFirstNamespaceNode(int nodeHandle, boolean inScope)
  {
    return -1;
  }
  
















  public int getNextSibling(int nodeHandle)
  {
    nodeHandle &= 0x7FFFFF;
    
    if (nodeHandle == 0) {
      return -1;
    }
    short type = (short)(nodes.readEntry(nodeHandle, 0) & 0xFFFF);
    if ((type == 1) || (type == 2) || (type == 5))
    {
      int nextSib = nodes.readEntry(nodeHandle, 2);
      if (nextSib == -1)
        return -1;
      if (nextSib != 0) {
        return m_docHandle | nextSib;
      }
    }
    
    int thisParent = nodes.readEntry(nodeHandle, 1);
    
    if (nodes.readEntry(++nodeHandle, 1) == thisParent) {
      return m_docHandle | nodeHandle;
    }
    return -1;
  }
  








  public int getPreviousSibling(int nodeHandle)
  {
    nodeHandle &= 0x7FFFFF;
    
    if (nodeHandle == 0) {
      return -1;
    }
    int parent = nodes.readEntry(nodeHandle, 1);
    int kid = -1;
    for (int nextkid = getFirstChild(parent); nextkid != nodeHandle; 
        nextkid = getNextSibling(nextkid)) {
      kid = nextkid;
    }
    return kid | m_docHandle;
  }
  








  public int getNextAttribute(int nodeHandle)
  {
    nodeHandle &= 0x7FFFFF;
    nodes.readSlot(nodeHandle, gotslot);
    




    short type = (short)(gotslot[0] & 0xFFFF);
    
    if (type == 1)
      return getFirstAttribute(nodeHandle);
    if ((type == 2) && 
      (gotslot[2] != -1)) {
      return m_docHandle | gotslot[2];
    }
    return -1;
  }
  










  public int getNextNamespaceNode(int baseHandle, int namespaceHandle, boolean inScope)
  {
    return -1;
  }
  









  public int getNextDescendant(int subtreeRootHandle, int nodeHandle)
  {
    subtreeRootHandle &= 0x7FFFFF;
    nodeHandle &= 0x7FFFFF;
    
    if (nodeHandle == 0)
      return -1;
    while (!m_isError)
    {
      if ((done) && (nodeHandle > nodes.slotsUsed()))
        break;
      if (nodeHandle > subtreeRootHandle) {
        nodes.readSlot(nodeHandle + 1, gotslot);
        if (gotslot[2] != 0) {
          short type = (short)(gotslot[0] & 0xFFFF);
          if (type == 2) {
            nodeHandle += 2;
          } else {
            int nextParentPos = gotslot[1];
            if (nextParentPos < subtreeRootHandle) break;
            return m_docHandle | nodeHandle + 1;
          }
        }
        else {
          if (done) {
            break;
          }
        }
      } else {
        nodeHandle++;
      }
    }
    
    return -1;
  }
  








  public int getNextFollowing(int axisContextHandle, int nodeHandle)
  {
    return -1;
  }
  








  public int getNextPreceding(int axisContextHandle, int nodeHandle)
  {
    nodeHandle &= 0x7FFFFF;
    while (nodeHandle > 1) {
      nodeHandle--;
      if (2 != (nodes.readEntry(nodeHandle, 0) & 0xFFFF))
      {









        return m_docHandle | nodes.specialFind(axisContextHandle, nodeHandle); }
    }
    return -1;
  }
  









  public int getParent(int nodeHandle)
  {
    return m_docHandle | nodes.readEntry(nodeHandle, 1);
  }
  



  public int getDocumentRoot()
  {
    return m_docHandle | m_docElement;
  }
  




  public int getDocument()
  {
    return m_docHandle;
  }
  













  public int getOwnerDocument(int nodeHandle)
  {
    if ((nodeHandle & 0x7FFFFF) == 0)
      return -1;
    return nodeHandle & 0xFF800000;
  }
  












  public int getDocumentRoot(int nodeHandle)
  {
    if ((nodeHandle & 0x7FFFFF) == 0)
      return -1;
    return nodeHandle & 0xFF800000;
  }
  









  public XMLString getStringValue(int nodeHandle)
  {
    nodes.readSlot(nodeHandle, gotslot);
    int nodetype = gotslot[0] & 0xFF;
    String value = null;
    
    switch (nodetype) {
    case 3: 
    case 4: 
    case 8: 
      value = m_char.getString(gotslot[2], gotslot[3]);
      break;
    }
    
    




    return m_xsf.newstr(value);
  }
  


























  public int getStringValueChunkCount(int nodeHandle)
  {
    return 0;
  }
  
























  public char[] getStringValueChunk(int nodeHandle, int chunkIndex, int[] startAndLen)
  {
    return new char[0];
  }
  





  public int getExpandedTypeID(int nodeHandle)
  {
    nodes.readSlot(nodeHandle, gotslot);
    String qName = m_localNames.indexToString(gotslot[3]);
    

    int colonpos = qName.indexOf(":");
    String localName = qName.substring(colonpos + 1);
    
    String namespace = m_nsNames.indexToString(gotslot[0] << 16);
    
    String expandedName = namespace + ":" + localName;
    int expandedNameID = m_nsNames.stringToIndex(expandedName);
    
    return expandedNameID;
  }
  













  public int getExpandedTypeID(String namespace, String localName, int type)
  {
    String expandedName = namespace + ":" + localName;
    int expandedNameID = m_nsNames.stringToIndex(expandedName);
    
    return expandedNameID;
  }
  








  public String getLocalNameFromExpandedNameID(int ExpandedNameID)
  {
    String expandedName = m_localNames.indexToString(ExpandedNameID);
    
    int colonpos = expandedName.indexOf(":");
    String localName = expandedName.substring(colonpos + 1);
    return localName;
  }
  








  public String getNamespaceFromExpandedNameID(int ExpandedNameID)
  {
    String expandedName = m_localNames.indexToString(ExpandedNameID);
    
    int colonpos = expandedName.indexOf(":");
    String nsName = expandedName.substring(0, colonpos);
    
    return nsName;
  }
  




  private static final String[] fixednames = { null, null, null, "#text", "#cdata_section", null, null, null, "#comment", "#document", null, "#document-fragment", null };
  















  public String getNodeName(int nodeHandle)
  {
    nodes.readSlot(nodeHandle, gotslot);
    short type = (short)(gotslot[0] & 0xFFFF);
    String name = fixednames[type];
    if (null == name) {
      int i = gotslot[3];
      System.out.println("got i=" + i + " " + (i >> 16) + "/" + (i & 0xFFFF));
      
      name = m_localNames.indexToString(i & 0xFFFF);
      String prefix = m_prefixNames.indexToString(i >> 16);
      if ((prefix != null) && (prefix.length() > 0))
        name = prefix + ":" + name;
    }
    return name;
  }
  






  public String getNodeNameX(int nodeHandle)
  {
    return null;
  }
  









  public String getLocalName(int nodeHandle)
  {
    nodes.readSlot(nodeHandle, gotslot);
    short type = (short)(gotslot[0] & 0xFFFF);
    String name = "";
    if ((type == 1) || (type == 2)) {
      int i = gotslot[3];
      name = m_localNames.indexToString(i & 0xFFFF);
      if (name == null) name = "";
    }
    return name;
  }
  













  public String getPrefix(int nodeHandle)
  {
    nodes.readSlot(nodeHandle, gotslot);
    short type = (short)(gotslot[0] & 0xFFFF);
    String name = "";
    if ((type == 1) || (type == 2)) {
      int i = gotslot[3];
      name = m_prefixNames.indexToString(i >> 16);
      if (name == null) name = "";
    }
    return name;
  }
  







  public String getNamespaceURI(int nodeHandle)
  {
    return null;
  }
  








  public String getNodeValue(int nodeHandle)
  {
    nodes.readSlot(nodeHandle, gotslot);
    int nodetype = gotslot[0] & 0xFF;
    String value = null;
    
    switch (nodetype) {
    case 2: 
      nodes.readSlot(nodeHandle + 1, gotslot);
    case 3: 
    case 4: 
    case 8: 
      value = m_char.getString(gotslot[2], gotslot[3]);
      break;
    }
    
    



    return value;
  }
  







  public short getNodeType(int nodeHandle)
  {
    return (short)(nodes.readEntry(nodeHandle, 0) & 0xFFFF);
  }
  







  public short getLevel(int nodeHandle)
  {
    short count = 0;
    while (nodeHandle != 0) {
      count = (short)(count + 1);
      nodeHandle = nodes.readEntry(nodeHandle, 1);
    }
    return count;
  }
  












  public boolean isSupported(String feature, String version)
  {
    return false;
  }
  







  public String getDocumentBaseURI()
  {
    return m_documentBaseURI;
  }
  






  public void setDocumentBaseURI(String baseURI)
  {
    m_documentBaseURI = baseURI;
  }
  





  public String getDocumentSystemIdentifier(int nodeHandle)
  {
    return null;
  }
  




  public String getDocumentEncoding(int nodeHandle)
  {
    return null;
  }
  







  public String getDocumentStandalone(int nodeHandle)
  {
    return null;
  }
  







  public String getDocumentVersion(int documentHandle)
  {
    return null;
  }
  







  public boolean getDocumentAllDeclarationsProcessed()
  {
    return false;
  }
  





  public String getDocumentTypeDeclarationSystemIdentifier()
  {
    return null;
  }
  





  public String getDocumentTypeDeclarationPublicIdentifier()
  {
    return null;
  }
  














  public int getElementById(String elementId)
  {
    return 0;
  }
  































  public String getUnparsedEntityURI(String name)
  {
    return null;
  }
  







  public boolean supportsPreStripping()
  {
    return false;
  }
  















  public boolean isNodeAfter(int nodeHandle1, int nodeHandle2)
  {
    return false;
  }
  













  public boolean isCharacterElementContentWhitespace(int nodeHandle)
  {
    return false;
  }
  









  public boolean isDocumentAllDeclarationsProcessed(int documentHandle)
  {
    return false;
  }
  






  public boolean isAttributeSpecified(int attributeHandle)
  {
    return false;
  }
  










  public void dispatchCharactersEvents(int nodeHandle, ContentHandler ch, boolean normalize)
    throws SAXException
  {}
  










  public void dispatchToEvents(int nodeHandle, ContentHandler ch)
    throws SAXException
  {}
  









  public Node getNode(int nodeHandle)
  {
    return null;
  }
  
















  public void appendChild(int newChild, boolean clone, boolean cloneDepth)
  {
    boolean sameDoc = (newChild & 0xFF800000) == m_docHandle;
    if ((!clone) && (!sameDoc)) {}
  }
  
















  public void appendTextChild(String str) {}
  















  void appendTextChild(int m_char_current_start, int contentLength)
  {
    int w0 = 3;
    
    int w1 = currentParent;
    
    int w2 = m_char_current_start;
    
    int w3 = contentLength;
    
    int ourslot = appendNode(w0, w1, w2, w3);
    previousSibling = ourslot;
  }
  









  void appendComment(int m_char_current_start, int contentLength)
  {
    int w0 = 8;
    
    int w1 = currentParent;
    
    int w2 = m_char_current_start;
    
    int w3 = contentLength;
    
    int ourslot = appendNode(w0, w1, w2, w3);
    previousSibling = ourslot;
  }
  






















  void appendStartElement(int namespaceIndex, int localNameIndex, int prefixIndex)
  {
    int w0 = namespaceIndex << 16 | 0x1;
    
    int w1 = currentParent;
    
    int w2 = 0;
    
    int w3 = localNameIndex | prefixIndex << 16;
    System.out.println("set w3=" + w3 + " " + (w3 >> 16) + "/" + (w3 & 0xFFFF));
    

    int ourslot = appendNode(w0, w1, w2, w3);
    currentParent = ourslot;
    previousSibling = 0;
    

    if (m_docElement == -1) {
      m_docElement = ourslot;
    }
  }
  





















  void appendNSDeclaration(int prefixIndex, int namespaceIndex, boolean isID)
  {
    int namespaceForNamespaces = m_nsNames.stringToIndex("http://www.w3.org/2000/xmlns/");
    

    int w0 = 0xD | m_nsNames.stringToIndex("http://www.w3.org/2000/xmlns/") << 16;
    

    int w1 = currentParent;
    
    int w2 = 0;
    
    int w3 = namespaceIndex;
    
    int ourslot = appendNode(w0, w1, w2, w3);
    previousSibling = ourslot;
    previousSiblingWasParent = false;
  }
  





















  void appendAttribute(int namespaceIndex, int localNameIndex, int prefixIndex, boolean isID, int m_char_current_start, int contentLength)
  {
    int w0 = 0x2 | namespaceIndex << 16;
    

    int w1 = currentParent;
    
    int w2 = 0;
    
    int w3 = localNameIndex | prefixIndex << 16;
    System.out.println("set w3=" + w3 + " " + (w3 >> 16) + "/" + (w3 & 0xFFFF));
    
    int ourslot = appendNode(w0, w1, w2, w3);
    previousSibling = ourslot;
    



    w0 = 3;
    
    w1 = ourslot;
    
    w2 = m_char_current_start;
    
    w3 = contentLength;
    appendNode(w0, w1, w2, w3);
    

    previousSiblingWasParent = true;
  }
  









  public DTMAxisTraverser getAxisTraverser(int axis)
  {
    return null;
  }
  











  public DTMAxisIterator getAxisIterator(int axis)
  {
    return null;
  }
  











  public DTMAxisIterator getTypedAxisIterator(int axis, int type)
  {
    return null;
  }
  






  void appendEndElement()
  {
    if (previousSiblingWasParent) {
      nodes.writeEntry(previousSibling, 2, -1);
    }
    
    previousSibling = currentParent;
    nodes.readSlot(currentParent, gotslot);
    currentParent = (gotslot[1] & 0xFFFF);
    


    previousSiblingWasParent = true;
  }
  









  void appendStartDocument()
  {
    m_docElement = -1;
    initDocument(0);
  }
  



  void appendEndDocument()
  {
    done = true;
  }
  









  public void setProperty(String property, Object value) {}
  









  public SourceLocator getSourceLocatorFor(int node)
  {
    return null;
  }
  
  public void documentRegistration() {}
  
  public void documentRelease() {}
  
  public void migrateTo(DTMManager manager) {}
}
