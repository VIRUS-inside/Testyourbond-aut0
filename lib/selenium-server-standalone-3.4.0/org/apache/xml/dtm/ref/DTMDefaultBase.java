package org.apache.xml.dtm.ref;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Vector;
import javax.xml.transform.Source;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMAxisTraverser;
import org.apache.xml.dtm.DTMException;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.dtm.DTMWSFilter;
import org.apache.xml.res.XMLMessages;
import org.apache.xml.utils.BoolStack;
import org.apache.xml.utils.SuballocatedIntVector;
import org.apache.xml.utils.XMLString;
import org.apache.xml.utils.XMLStringFactory;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;






























public abstract class DTMDefaultBase
  implements DTM
{
  static final boolean JJK_DEBUG = false;
  public static final int ROOTNODE = 0;
  protected int m_size = 0;
  

  protected SuballocatedIntVector m_exptype;
  

  protected SuballocatedIntVector m_firstch;
  

  protected SuballocatedIntVector m_nextsib;
  

  protected SuballocatedIntVector m_prevsib;
  

  protected SuballocatedIntVector m_parent;
  

  protected Vector m_namespaceDeclSets = null;
  


  protected SuballocatedIntVector m_namespaceDeclSetElements = null;
  




  protected int[][][] m_elemIndexes;
  




  public static final int DEFAULT_BLOCKSIZE = 512;
  



  public static final int DEFAULT_NUMBLOCKS = 32;
  



  public static final int DEFAULT_NUMBLOCKS_SMALL = 4;
  



  protected static final int NOTPROCESSED = -2;
  



  public DTMManager m_mgr;
  



  protected DTMManagerDefault m_mgrDefault = null;
  




  protected SuballocatedIntVector m_dtmIdent;
  



  protected String m_documentBaseURI;
  



  protected DTMWSFilter m_wsfilter;
  



  protected boolean m_shouldStripWS = false;
  



  protected BoolStack m_shouldStripWhitespaceStack;
  



  protected XMLStringFactory m_xstrf;
  



  protected ExpandedNameTable m_expandedNameTable;
  



  protected boolean m_indexing;
  



  protected DTMAxisTraverser[] m_traversers;
  




  public DTMDefaultBase(DTMManager mgr, Source source, int dtmIdentity, DTMWSFilter whiteSpaceFilter, XMLStringFactory xstringfactory, boolean doIndexing)
  {
    this(mgr, source, dtmIdentity, whiteSpaceFilter, xstringfactory, doIndexing, 512, true, false);
  }
  











  public DTMDefaultBase(DTMManager mgr, Source source, int dtmIdentity, DTMWSFilter whiteSpaceFilter, XMLStringFactory xstringfactory, boolean doIndexing, int blocksize, boolean usePrevsib, boolean newNameTable)
  {
    int numblocks;
    










    if (blocksize <= 64)
    {
      int numblocks = 4;
      m_dtmIdent = new SuballocatedIntVector(4, 1);
    }
    else
    {
      numblocks = 32;
      m_dtmIdent = new SuballocatedIntVector(32);
    }
    
    m_exptype = new SuballocatedIntVector(blocksize, numblocks);
    m_firstch = new SuballocatedIntVector(blocksize, numblocks);
    m_nextsib = new SuballocatedIntVector(blocksize, numblocks);
    m_parent = new SuballocatedIntVector(blocksize, numblocks);
    



    if (usePrevsib) {
      m_prevsib = new SuballocatedIntVector(blocksize, numblocks);
    }
    m_mgr = mgr;
    if ((mgr instanceof DTMManagerDefault)) {
      m_mgrDefault = ((DTMManagerDefault)mgr);
    }
    m_documentBaseURI = (null != source ? source.getSystemId() : null);
    m_dtmIdent.setElementAt(dtmIdentity, 0);
    m_wsfilter = whiteSpaceFilter;
    m_xstrf = xstringfactory;
    m_indexing = doIndexing;
    
    if (doIndexing)
    {
      m_expandedNameTable = new ExpandedNameTable();

    }
    else
    {

      m_expandedNameTable = m_mgrDefault.getExpandedNameTable(this);
    }
    
    if (null != whiteSpaceFilter)
    {
      m_shouldStripWhitespaceStack = new BoolStack();
      
      pushShouldStripWhitespace(false);
    }
  }
  







  protected void ensureSizeOfIndex(int namespaceID, int LocalNameID)
  {
    if (null == m_elemIndexes)
    {
      m_elemIndexes = new int[namespaceID + 20][][];
    }
    else if (m_elemIndexes.length <= namespaceID)
    {
      int[][][] indexes = m_elemIndexes;
      
      m_elemIndexes = new int[namespaceID + 20][][];
      
      System.arraycopy(indexes, 0, m_elemIndexes, 0, indexes.length);
    }
    
    int[][] localNameIndex = m_elemIndexes[namespaceID];
    
    if (null == localNameIndex)
    {
      localNameIndex = new int[LocalNameID + 100][];
      m_elemIndexes[namespaceID] = localNameIndex;
    }
    else if (localNameIndex.length <= LocalNameID)
    {
      int[][] indexes = localNameIndex;
      
      localNameIndex = new int[LocalNameID + 100][];
      
      System.arraycopy(indexes, 0, localNameIndex, 0, indexes.length);
      
      m_elemIndexes[namespaceID] = localNameIndex;
    }
    
    int[] elemHandles = localNameIndex[LocalNameID];
    
    if (null == elemHandles)
    {
      elemHandles = new int[''];
      localNameIndex[LocalNameID] = elemHandles;
      elemHandles[0] = 1;
    }
    else if (elemHandles.length <= elemHandles[0] + 1)
    {
      int[] indexes = elemHandles;
      
      elemHandles = new int[elemHandles[0] + 1024];
      
      System.arraycopy(indexes, 0, elemHandles, 0, indexes.length);
      
      localNameIndex[LocalNameID] = elemHandles;
    }
  }
  








  protected void indexNode(int expandedTypeID, int identity)
  {
    ExpandedNameTable ent = m_expandedNameTable;
    short type = ent.getType(expandedTypeID);
    
    if (1 == type)
    {
      int namespaceID = ent.getNamespaceID(expandedTypeID);
      int localNameID = ent.getLocalNameID(expandedTypeID);
      
      ensureSizeOfIndex(namespaceID, localNameID);
      
      int[] index = m_elemIndexes[namespaceID][localNameID];
      
      index[index[0]] = identity;
      
      index[0] += 1;
    }
  }
  














  protected int findGTE(int[] list, int start, int len, int value)
  {
    int low = start;
    int high = start + (len - 1);
    int end = high;
    
    while (low <= high)
    {
      int mid = low + high >>> 1;
      int c = list[mid];
      
      if (c > value) {
        high = mid - 1;
      } else if (c < value) {
        low = mid + 1;
      } else {
        return mid;
      }
    }
    return (low <= end) && (list[low] > value) ? low : -1;
  }
  












  int findElementFromIndex(int nsIndex, int lnIndex, int firstPotential)
  {
    int[][][] indexes = m_elemIndexes;
    
    if ((null != indexes) && (nsIndex < indexes.length))
    {
      int[][] lnIndexs = indexes[nsIndex];
      
      if ((null != lnIndexs) && (lnIndex < lnIndexs.length))
      {
        int[] elems = lnIndexs[lnIndex];
        
        if (null != elems)
        {
          int pos = findGTE(elems, 1, elems[0], firstPotential);
          
          if (pos > -1)
          {
            return elems[pos];
          }
        }
      }
    }
    
    return -2;
  }
  











  protected abstract int getNextNodeIdentity(int paramInt);
  










  protected abstract boolean nextNode();
  










  protected abstract int getNumberOfNodes();
  










  protected short _type(int identity)
  {
    int info = _exptype(identity);
    
    if (-1 != info) {
      return m_expandedNameTable.getType(info);
    }
    return -1;
  }
  







  protected int _exptype(int identity)
  {
    if (identity == -1) {
      return -1;
    }
    

    while (identity >= m_size)
    {
      if ((!nextNode()) && (identity >= m_size))
        return -1;
    }
    return m_exptype.elementAt(identity);
  }
  








  protected int _level(int identity)
  {
    while (identity >= m_size)
    {
      boolean isMore = nextNode();
      if ((!isMore) && (identity >= m_size)) {
        return -1;
      }
    }
    int i = 0;
    while (-1 != (identity = _parent(identity)))
      i++;
    return i;
  }
  









  protected int _firstch(int identity)
  {
    int info = identity >= m_size ? -2 : m_firstch.elementAt(identity);
    



    while (info == -2)
    {
      boolean isMore = nextNode();
      
      if ((identity >= m_size) && (!isMore)) {
        return -1;
      }
      
      info = m_firstch.elementAt(identity);
      if ((info == -2) && (!isMore)) {
        return -1;
      }
    }
    
    return info;
  }
  








  protected int _nextsib(int identity)
  {
    int info = identity >= m_size ? -2 : m_nextsib.elementAt(identity);
    



    while (info == -2)
    {
      boolean isMore = nextNode();
      
      if ((identity >= m_size) && (!isMore)) {
        return -1;
      }
      
      info = m_nextsib.elementAt(identity);
      if ((info == -2) && (!isMore)) {
        return -1;
      }
    }
    
    return info;
  }
  








  protected int _prevsib(int identity)
  {
    if (identity < m_size) {
      return m_prevsib.elementAt(identity);
    }
    


    for (;;)
    {
      boolean isMore = nextNode();
      
      if ((identity >= m_size) && (!isMore))
        return -1;
      if (identity < m_size) {
        return m_prevsib.elementAt(identity);
      }
    }
  }
  







  protected int _parent(int identity)
  {
    if (identity < m_size) {
      return m_parent.elementAt(identity);
    }
    


    for (;;)
    {
      boolean isMore = nextNode();
      
      if ((identity >= m_size) && (!isMore))
        return -1;
      if (identity < m_size) {
        return m_parent.elementAt(identity);
      }
    }
  }
  


  public void dumpDTM(OutputStream os)
  {
    try
    {
      if (os == null)
      {
        File f = new File("DTMDump" + hashCode() + ".txt");
        System.err.println("Dumping... " + f.getAbsolutePath());
        os = new FileOutputStream(f);
      }
      PrintStream ps = new PrintStream(os);
      
      while (nextNode()) {}
      
      int nRecords = m_size;
      
      ps.println("Total nodes: " + nRecords);
      
      for (int index = 0; index < nRecords; index++)
      {
        int i = makeNodeHandle(index);
        ps.println("=========== index=" + index + " handle=" + i + " ===========");
        ps.println("NodeName: " + getNodeName(i));
        ps.println("NodeNameX: " + getNodeNameX(i));
        ps.println("LocalName: " + getLocalName(i));
        ps.println("NamespaceURI: " + getNamespaceURI(i));
        ps.println("Prefix: " + getPrefix(i));
        
        int exTypeID = _exptype(index);
        
        ps.println("Expanded Type ID: " + Integer.toHexString(exTypeID));
        

        int type = _type(index);
        
        String typestring;
        switch (type)
        {
        case 2: 
          typestring = "ATTRIBUTE_NODE";
          break;
        case 4: 
          typestring = "CDATA_SECTION_NODE";
          break;
        case 8: 
          typestring = "COMMENT_NODE";
          break;
        case 11: 
          typestring = "DOCUMENT_FRAGMENT_NODE";
          break;
        case 9: 
          typestring = "DOCUMENT_NODE";
          break;
        case 10: 
          typestring = "DOCUMENT_NODE";
          break;
        case 1: 
          typestring = "ELEMENT_NODE";
          break;
        case 6: 
          typestring = "ENTITY_NODE";
          break;
        case 5: 
          typestring = "ENTITY_REFERENCE_NODE";
          break;
        case 13: 
          typestring = "NAMESPACE_NODE";
          break;
        case 12: 
          typestring = "NOTATION_NODE";
          break;
        case -1: 
          typestring = "NULL";
          break;
        case 7: 
          typestring = "PROCESSING_INSTRUCTION_NODE";
          break;
        case 3: 
          typestring = "TEXT_NODE";
          break;
        case 0: default: 
          typestring = "Unknown!";
        }
        
        
        ps.println("Type: " + typestring);
        
        int firstChild = _firstch(index);
        
        if (-1 == firstChild) {
          ps.println("First child: DTM.NULL");
        } else if (-2 == firstChild) {
          ps.println("First child: NOTPROCESSED");
        } else {
          ps.println("First child: " + firstChild);
        }
        if (m_prevsib != null)
        {
          int prevSibling = _prevsib(index);
          
          if (-1 == prevSibling) {
            ps.println("Prev sibling: DTM.NULL");
          } else if (-2 == prevSibling) {
            ps.println("Prev sibling: NOTPROCESSED");
          } else {
            ps.println("Prev sibling: " + prevSibling);
          }
        }
        int nextSibling = _nextsib(index);
        
        if (-1 == nextSibling) {
          ps.println("Next sibling: DTM.NULL");
        } else if (-2 == nextSibling) {
          ps.println("Next sibling: NOTPROCESSED");
        } else {
          ps.println("Next sibling: " + nextSibling);
        }
        int parent = _parent(index);
        
        if (-1 == parent) {
          ps.println("Parent: DTM.NULL");
        } else if (-2 == parent) {
          ps.println("Parent: NOTPROCESSED");
        } else {
          ps.println("Parent: " + parent);
        }
        int level = _level(index);
        
        ps.println("Level: " + level);
        ps.println("Node Value: " + getNodeValue(i));
        ps.println("String Value: " + getStringValue(i));
      }
    }
    catch (IOException ioe)
    {
      ioe.printStackTrace(System.err);
      throw new RuntimeException(ioe.getMessage());
    }
  }
  













  public String dumpNode(int nodeHandle)
  {
    if (nodeHandle == -1) {
      return "[null]";
    }
    String typestring;
    switch (getNodeType(nodeHandle))
    {
    case 2: 
      typestring = "ATTR";
      break;
    case 4: 
      typestring = "CDATA";
      break;
    case 8: 
      typestring = "COMMENT";
      break;
    case 11: 
      typestring = "DOC_FRAG";
      break;
    case 9: 
      typestring = "DOC";
      break;
    case 10: 
      typestring = "DOC_TYPE";
      break;
    case 1: 
      typestring = "ELEMENT";
      break;
    case 6: 
      typestring = "ENTITY";
      break;
    case 5: 
      typestring = "ENT_REF";
      break;
    case 13: 
      typestring = "NAMESPACE";
      break;
    case 12: 
      typestring = "NOTATION";
      break;
    case -1: 
      typestring = "null";
      break;
    case 7: 
      typestring = "PI";
      break;
    case 3: 
      typestring = "TEXT";
      break;
    case 0: default: 
      typestring = "Unknown!";
    }
    
    
    StringBuffer sb = new StringBuffer();
    sb.append("[" + nodeHandle + ": " + typestring + "(0x" + Integer.toHexString(getExpandedTypeID(nodeHandle)) + ") " + getNodeNameX(nodeHandle) + " {" + getNamespaceURI(nodeHandle) + "}" + "=\"" + getNodeValue(nodeHandle) + "\"]");
    


    return sb.toString();
  }
  













  public void setFeature(String featureId, boolean state) {}
  












  public boolean hasChildNodes(int nodeHandle)
  {
    int identity = makeNodeIdentity(nodeHandle);
    int firstChild = _firstch(identity);
    
    return firstChild != -1;
  }
  














  public final int makeNodeHandle(int nodeIdentity)
  {
    if (-1 == nodeIdentity) { return -1;
    }
    


    return m_dtmIdent.elementAt(nodeIdentity >>> 16) + (nodeIdentity & 0xFFFF);
  }
  

















  public final int makeNodeIdentity(int nodeHandle)
  {
    if (-1 == nodeHandle) { return -1;
    }
    if (m_mgrDefault != null)
    {




      int whichDTMindex = nodeHandle >>> 16;
      




      if (m_mgrDefault.m_dtms[whichDTMindex] != this) {
        return -1;
      }
      return m_mgrDefault.m_dtm_offsets[whichDTMindex] | nodeHandle & 0xFFFF;
    }
    


    int whichDTMid = m_dtmIdent.indexOf(nodeHandle & 0xFFFF0000);
    return whichDTMid == -1 ? -1 : (whichDTMid << 16) + (nodeHandle & 0xFFFF);
  }
  













  public int getFirstChild(int nodeHandle)
  {
    int identity = makeNodeIdentity(nodeHandle);
    int firstChild = _firstch(identity);
    
    return makeNodeHandle(firstChild);
  }
  










  public int getTypedFirstChild(int nodeHandle, int nodeType)
  {
    if (nodeType < 14) {
      for (int firstChild = _firstch(makeNodeIdentity(nodeHandle)); 
          firstChild != -1; 
          firstChild = _nextsib(firstChild)) {
        int eType = _exptype(firstChild);
        if ((eType == nodeType) || ((eType >= 14) && (m_expandedNameTable.getType(eType) == nodeType)))
        {

          return makeNodeHandle(firstChild);
        }
      }
    }
    for (int firstChild = _firstch(makeNodeIdentity(nodeHandle)); 
        firstChild != -1; 
        firstChild = _nextsib(firstChild)) {
      if (_exptype(firstChild) == nodeType) {
        return makeNodeHandle(firstChild);
      }
    }
    
    return -1;
  }
  










  public int getLastChild(int nodeHandle)
  {
    int identity = makeNodeIdentity(nodeHandle);
    int child = _firstch(identity);
    int lastChild = -1;
    
    while (child != -1)
    {
      lastChild = child;
      child = _nextsib(child);
    }
    
    return makeNodeHandle(lastChild);
  }
  










  public abstract int getAttributeNode(int paramInt, String paramString1, String paramString2);
  









  public int getFirstAttribute(int nodeHandle)
  {
    int nodeID = makeNodeIdentity(nodeHandle);
    
    return makeNodeHandle(getFirstAttributeIdentity(nodeID));
  }
  





  protected int getFirstAttributeIdentity(int identity)
  {
    int type = _type(identity);
    
    if (1 == type)
    {

      while (-1 != (identity = getNextNodeIdentity(identity)))
      {


        type = _type(identity);
        
        if (type == 2)
        {
          return identity;
        }
        if (13 != type) {
          break;
        }
      }
    }
    

    return -1;
  }
  








  protected int getTypedAttribute(int nodeHandle, int attType)
  {
    int type = getNodeType(nodeHandle);
    if (1 == type) {
      int identity = makeNodeIdentity(nodeHandle);
      
      while (-1 != (identity = getNextNodeIdentity(identity)))
      {
        type = _type(identity);
        
        if (type == 2)
        {
          if (_exptype(identity) == attType) return makeNodeHandle(identity);
        }
        else if (13 != type) {
          break;
        }
      }
    }
    

    return -1;
  }
  








  public int getNextSibling(int nodeHandle)
  {
    if (nodeHandle == -1)
      return -1;
    return makeNodeHandle(_nextsib(makeNodeIdentity(nodeHandle)));
  }
  








  public int getTypedNextSibling(int nodeHandle, int nodeType)
  {
    if (nodeHandle == -1)
      return -1;
    int node = makeNodeIdentity(nodeHandle);
    int eType;
    while (((node = _nextsib(node)) != -1) && ((eType = _exptype(node)) != nodeType) && (m_expandedNameTable.getType(eType) != nodeType)) {}
    



    return node == -1 ? -1 : makeNodeHandle(node);
  }
  









  public int getPreviousSibling(int nodeHandle)
  {
    if (nodeHandle == -1) {
      return -1;
    }
    if (m_prevsib != null) {
      return makeNodeHandle(_prevsib(makeNodeIdentity(nodeHandle)));
    }
    



    int nodeID = makeNodeIdentity(nodeHandle);
    int parent = _parent(nodeID);
    int node = _firstch(parent);
    int result = -1;
    while (node != nodeID)
    {
      result = node;
      node = _nextsib(node);
    }
    return makeNodeHandle(result);
  }
  









  public int getNextAttribute(int nodeHandle)
  {
    int nodeID = makeNodeIdentity(nodeHandle);
    
    if (_type(nodeID) == 2) {
      return makeNodeHandle(getNextAttributeIdentity(nodeID));
    }
    
    return -1;
  }
  










  protected int getNextAttributeIdentity(int identity)
  {
    while (-1 != (identity = getNextNodeIdentity(identity))) {
      int type = _type(identity);
      
      if (type == 2)
        return identity;
      if (type != 13) {
        break;
      }
    }
    
    return -1;
  }
  

  private Vector m_namespaceLists = null;
  














  protected void declareNamespaceInContext(int elementNodeIndex, int namespaceNodeIndex)
  {
    SuballocatedIntVector nsList = null;
    if (m_namespaceDeclSets == null)
    {


      m_namespaceDeclSetElements = new SuballocatedIntVector(32);
      m_namespaceDeclSetElements.addElement(elementNodeIndex);
      m_namespaceDeclSets = new Vector();
      nsList = new SuballocatedIntVector(32);
      m_namespaceDeclSets.addElement(nsList);

    }
    else
    {

      int last = m_namespaceDeclSetElements.size() - 1;
      
      if ((last >= 0) && (elementNodeIndex == m_namespaceDeclSetElements.elementAt(last)))
      {
        nsList = (SuballocatedIntVector)m_namespaceDeclSets.elementAt(last);
      }
    }
    if (nsList == null)
    {
      m_namespaceDeclSetElements.addElement(elementNodeIndex);
      
      SuballocatedIntVector inherited = findNamespaceContext(_parent(elementNodeIndex));
      

      if (inherited != null)
      {


        int isize = inherited.size();
        


        nsList = new SuballocatedIntVector(Math.max(Math.min(isize + 16, 2048), 32));
        

        for (int i = 0; i < isize; i++)
        {
          nsList.addElement(inherited.elementAt(i));
        }
      } else {
        nsList = new SuballocatedIntVector(32);
      }
      
      m_namespaceDeclSets.addElement(nsList);
    }
    




    int newEType = _exptype(namespaceNodeIndex);
    
    for (int i = nsList.size() - 1; i >= 0; i--)
    {
      if (newEType == getExpandedTypeID(nsList.elementAt(i)))
      {
        nsList.setElementAt(makeNodeHandle(namespaceNodeIndex), i);
        return;
      }
    }
    nsList.addElement(makeNodeHandle(namespaceNodeIndex));
  }
  







  protected SuballocatedIntVector findNamespaceContext(int elementNodeIndex)
  {
    if (null != m_namespaceDeclSetElements)
    {


      int wouldBeAt = findInSortedSuballocatedIntVector(m_namespaceDeclSetElements, elementNodeIndex);
      
      if (wouldBeAt >= 0)
        return (SuballocatedIntVector)m_namespaceDeclSets.elementAt(wouldBeAt);
      if (wouldBeAt == -1) {
        return null;
      }
      

      wouldBeAt = -1 - wouldBeAt;
      

      int candidate = m_namespaceDeclSetElements.elementAt(--wouldBeAt);
      int ancestor = _parent(elementNodeIndex);
      



      if ((wouldBeAt == 0) && (candidate < ancestor)) {
        int rootHandle = getDocumentRoot(makeNodeHandle(elementNodeIndex));
        int rootID = makeNodeIdentity(rootHandle);
        int uppermostNSCandidateID;
        int uppermostNSCandidateID;
        if (getNodeType(rootHandle) == 9) {
          int ch = _firstch(rootID);
          uppermostNSCandidateID = ch != -1 ? ch : rootID;
        } else {
          uppermostNSCandidateID = rootID;
        }
        
        if (candidate == uppermostNSCandidateID) {
          return (SuballocatedIntVector)m_namespaceDeclSets.elementAt(wouldBeAt);
        }
      }
      
      while ((wouldBeAt >= 0) && (ancestor > 0))
      {
        if (candidate == ancestor)
        {
          return (SuballocatedIntVector)m_namespaceDeclSets.elementAt(wouldBeAt); }
        if (candidate < ancestor)
        {
          do {
            ancestor = _parent(ancestor);
          } while (candidate < ancestor);
        } else { if (wouldBeAt <= 0)
            break;
          candidate = m_namespaceDeclSetElements.elementAt(--wouldBeAt);
        }
      }
    }
    


    return null;
  }
  














  protected int findInSortedSuballocatedIntVector(SuballocatedIntVector vector, int lookfor)
  {
    int i = 0;
    if (vector != null) {
      int first = 0;
      int last = vector.size() - 1;
      
      while (first <= last) {
        i = (first + last) / 2;
        int test = lookfor - vector.elementAt(i);
        if (test == 0) {
          return i;
        }
        if (test < 0) {
          last = i - 1;
        }
        else {
          first = i + 1;
        }
      }
      
      if (first > i) {
        i = first;
      }
    }
    
    return -1 - i;
  }
  














  public int getFirstNamespaceNode(int nodeHandle, boolean inScope)
  {
    if (inScope)
    {
      int identity = makeNodeIdentity(nodeHandle);
      if (_type(identity) == 1)
      {
        SuballocatedIntVector nsContext = findNamespaceContext(identity);
        if ((nsContext == null) || (nsContext.size() < 1)) {
          return -1;
        }
        return nsContext.elementAt(0);
      }
      
      return -1;
    }
    







    int identity = makeNodeIdentity(nodeHandle);
    if (_type(identity) == 1)
    {
      while (-1 != (identity = getNextNodeIdentity(identity)))
      {
        int type = _type(identity);
        if (type == 13)
          return makeNodeHandle(identity);
        if (2 != type)
          break;
      }
      return -1;
    }
    
    return -1;
  }
  












  public int getNextNamespaceNode(int baseHandle, int nodeHandle, boolean inScope)
  {
    if (inScope)
    {





      SuballocatedIntVector nsContext = findNamespaceContext(makeNodeIdentity(baseHandle));
      
      if (nsContext == null)
        return -1;
      int i = 1 + nsContext.indexOf(nodeHandle);
      if ((i <= 0) || (i == nsContext.size())) {
        return -1;
      }
      return nsContext.elementAt(i);
    }
    


    int identity = makeNodeIdentity(nodeHandle);
    while (-1 != (identity = getNextNodeIdentity(identity)))
    {
      int type = _type(identity);
      if (type == 13)
      {
        return makeNodeHandle(identity);
      }
      if (type != 2) {
        break;
      }
    }
    

    return -1;
  }
  








  public int getParent(int nodeHandle)
  {
    int identity = makeNodeIdentity(nodeHandle);
    
    if (identity > 0) {
      return makeNodeHandle(_parent(identity));
    }
    return -1;
  }
  








  public int getDocument()
  {
    return m_dtmIdent.elementAt(0);
  }
  













  public int getOwnerDocument(int nodeHandle)
  {
    if (9 == getNodeType(nodeHandle)) {
      return -1;
    }
    return getDocumentRoot(nodeHandle);
  }
  








  public int getDocumentRoot(int nodeHandle)
  {
    return getManager().getDTM(nodeHandle).getDocument();
  }
  












  public abstract XMLString getStringValue(int paramInt);
  











  public int getStringValueChunkCount(int nodeHandle)
  {
    error(XMLMessages.createXMLMessage("ER_METHOD_NOT_SUPPORTED", null));
    
    return 0;
  }
  
















  public char[] getStringValueChunk(int nodeHandle, int chunkIndex, int[] startAndLen)
  {
    error(XMLMessages.createXMLMessage("ER_METHOD_NOT_SUPPORTED", null));
    
    return null;
  }
  









  public int getExpandedTypeID(int nodeHandle)
  {
    int id = makeNodeIdentity(nodeHandle);
    if (id == -1)
      return -1;
    return _exptype(id);
  }
  

















  public int getExpandedTypeID(String namespace, String localName, int type)
  {
    ExpandedNameTable ent = m_expandedNameTable;
    
    return ent.getExpandedTypeID(namespace, localName, type);
  }
  






  public String getLocalNameFromExpandedNameID(int expandedNameID)
  {
    return m_expandedNameTable.getLocalName(expandedNameID);
  }
  







  public String getNamespaceFromExpandedNameID(int expandedNameID)
  {
    return m_expandedNameTable.getNamespace(expandedNameID);
  }
  






  public int getNamespaceType(int nodeHandle)
  {
    int identity = makeNodeIdentity(nodeHandle);
    int expandedNameID = _exptype(identity);
    
    return m_expandedNameTable.getNamespaceID(expandedNameID);
  }
  










  public abstract String getNodeName(int paramInt);
  









  public String getNodeNameX(int nodeHandle)
  {
    error(XMLMessages.createXMLMessage("ER_METHOD_NOT_SUPPORTED", null));
    
    return null;
  }
  










  public abstract String getLocalName(int paramInt);
  










  public abstract String getPrefix(int paramInt);
  










  public abstract String getNamespaceURI(int paramInt);
  










  public abstract String getNodeValue(int paramInt);
  









  public short getNodeType(int nodeHandle)
  {
    if (nodeHandle == -1)
      return -1;
    return m_expandedNameTable.getType(_exptype(makeNodeIdentity(nodeHandle)));
  }
  









  public short getLevel(int nodeHandle)
  {
    int identity = makeNodeIdentity(nodeHandle);
    return (short)(_level(identity) + 1);
  }
  












  public int getNodeIdent(int nodeHandle)
  {
    return makeNodeIdentity(nodeHandle);
  }
  












  public int getNodeHandle(int nodeId)
  {
    return makeNodeHandle(nodeId);
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
    return m_documentBaseURI;
  }
  










  public String getDocumentEncoding(int nodeHandle)
  {
    return "UTF-8";
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
    return true;
  }
  















  public abstract String getDocumentTypeDeclarationSystemIdentifier();
  















  public abstract String getDocumentTypeDeclarationPublicIdentifier();
  















  public abstract int getElementById(String paramString);
  















  public abstract String getUnparsedEntityURI(String paramString);
  















  public boolean supportsPreStripping()
  {
    return true;
  }
  

















  public boolean isNodeAfter(int nodeHandle1, int nodeHandle2)
  {
    int index1 = makeNodeIdentity(nodeHandle1);
    int index2 = makeNodeIdentity(nodeHandle2);
    
    return (index1 != -1) && (index2 != -1) && (index1 <= index2);
  }
  


















  public boolean isCharacterElementContentWhitespace(int nodeHandle)
  {
    return false;
  }
  












  public boolean isDocumentAllDeclarationsProcessed(int documentHandle)
  {
    return true;
  }
  












  public abstract boolean isAttributeSpecified(int paramInt);
  












  public abstract void dispatchCharactersEvents(int paramInt, ContentHandler paramContentHandler, boolean paramBoolean)
    throws SAXException;
  











  public abstract void dispatchToEvents(int paramInt, ContentHandler paramContentHandler)
    throws SAXException;
  











  public Node getNode(int nodeHandle)
  {
    return new DTMNodeProxy(this, nodeHandle);
  }
  














  public void appendChild(int newChild, boolean clone, boolean cloneDepth)
  {
    error(XMLMessages.createXMLMessage("ER_METHOD_NOT_SUPPORTED", null));
  }
  









  public void appendTextChild(String str)
  {
    error(XMLMessages.createXMLMessage("ER_METHOD_NOT_SUPPORTED", null));
  }
  





  protected void error(String msg)
  {
    throw new DTMException(msg);
  }
  






  protected boolean getShouldStripWhitespace()
  {
    return m_shouldStripWS;
  }
  







  protected void pushShouldStripWhitespace(boolean shouldStrip)
  {
    m_shouldStripWS = shouldStrip;
    
    if (null != m_shouldStripWhitespaceStack) {
      m_shouldStripWhitespaceStack.push(shouldStrip);
    }
  }
  




  protected void popShouldStripWhitespace()
  {
    if (null != m_shouldStripWhitespaceStack) {
      m_shouldStripWS = m_shouldStripWhitespaceStack.popAndTop();
    }
  }
  







  protected void setShouldStripWhitespace(boolean shouldStrip)
  {
    m_shouldStripWS = shouldStrip;
    
    if (null != m_shouldStripWhitespaceStack) {
      m_shouldStripWhitespaceStack.setTop(shouldStrip);
    }
  }
  







  public void documentRegistration() {}
  







  public void documentRelease() {}
  






  public void migrateTo(DTMManager mgr)
  {
    m_mgr = mgr;
    if ((mgr instanceof DTMManagerDefault)) {
      m_mgrDefault = ((DTMManagerDefault)mgr);
    }
  }
  





  public DTMManager getManager()
  {
    return m_mgr;
  }
  






  public SuballocatedIntVector getDTMIDs()
  {
    if (m_mgr == null) return null;
    return m_dtmIdent;
  }
}
