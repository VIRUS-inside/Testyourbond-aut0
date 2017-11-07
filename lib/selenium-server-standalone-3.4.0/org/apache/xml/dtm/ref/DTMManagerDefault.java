package org.apache.xml.dtm.ref;

import java.io.PrintStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMException;
import org.apache.xml.dtm.DTMFilter;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.dtm.DTMWSFilter;
import org.apache.xml.dtm.ref.dom2dtm.DOM2DTM;
import org.apache.xml.dtm.ref.dom2dtm.DOM2DTMdefaultNamespaceDeclarationNode;
import org.apache.xml.dtm.ref.sax2dtm.SAX2DTM;
import org.apache.xml.dtm.ref.sax2dtm.SAX2RTFDTM;
import org.apache.xml.res.XMLMessages;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.SuballocatedIntVector;
import org.apache.xml.utils.SystemIDResolver;
import org.apache.xml.utils.WrappedRuntimeException;
import org.apache.xml.utils.XMLReaderManager;
import org.apache.xml.utils.XMLStringFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;





















































public class DTMManagerDefault
  extends DTMManager
{
  private static final boolean DUMPTREE = false;
  private static final boolean DEBUG = false;
  protected DTM[] m_dtms = new DTM['Ā'];
  













  int[] m_dtm_offsets = new int['Ā'];
  




  protected XMLReaderManager m_readerManager = null;
  



  protected DefaultHandler m_defaultHandler = new DefaultHandler();
  






  public synchronized void addDTM(DTM dtm, int id)
  {
    addDTM(dtm, id, 0);
  }
  










  public synchronized void addDTM(DTM dtm, int id, int offset)
  {
    if (id >= 65536)
    {

      throw new DTMException(XMLMessages.createXMLMessage("ER_NO_DTMIDS_AVAIL", null));
    }
    





    int oldlen = m_dtms.length;
    if (oldlen <= id)
    {





      int newlen = Math.min(id + 256, 65536);
      
      DTM[] new_m_dtms = new DTM[newlen];
      System.arraycopy(m_dtms, 0, new_m_dtms, 0, oldlen);
      m_dtms = new_m_dtms;
      int[] new_m_dtm_offsets = new int[newlen];
      System.arraycopy(m_dtm_offsets, 0, new_m_dtm_offsets, 0, oldlen);
      m_dtm_offsets = new_m_dtm_offsets;
    }
    
    m_dtms[id] = dtm;
    m_dtm_offsets[id] = offset;
    dtm.documentRegistration();
  }
  





  public synchronized int getFirstFreeDTMID()
  {
    int n = m_dtms.length;
    for (int i = 1; i < n; i++)
    {
      if (null == m_dtms[i])
      {
        return i;
      }
    }
    return n;
  }
  



  private ExpandedNameTable m_expandedNameTable = new ExpandedNameTable();
  





















  public DTMManagerDefault() {}
  




















  public synchronized DTM getDTM(Source source, boolean unique, DTMWSFilter whiteSpaceFilter, boolean incremental, boolean doIndexing)
  {
    XMLStringFactory xstringFactory = m_xsf;
    int dtmPos = getFirstFreeDTMID();
    int documentID = dtmPos << 16;
    
    if ((null != source) && ((source instanceof DOMSource)))
    {
      DOM2DTM dtm = new DOM2DTM(this, (DOMSource)source, documentID, whiteSpaceFilter, xstringFactory, doIndexing);
      

      addDTM(dtm, dtmPos, 0);
      





      return dtm;
    }
    

    boolean isSAXSource = null != source ? source instanceof SAXSource : true;
    
    boolean isStreamSource = null != source ? source instanceof StreamSource : false;
    

    if ((isSAXSource) || (isStreamSource)) {
      XMLReader reader = null;
      
      try
      {
        InputSource xmlSource;
        InputSource xmlSource;
        if (null == source) {
          xmlSource = null;
        } else {
          reader = getXMLReader(source);
          xmlSource = SAXSource.sourceToInputSource(source);
          
          String urlOfSource = xmlSource.getSystemId();
          
          if (null != urlOfSource) {
            try {
              urlOfSource = SystemIDResolver.getAbsoluteURI(urlOfSource);
            }
            catch (Exception e) {
              System.err.println("Can not absolutize URL: " + urlOfSource);
            }
            
            xmlSource.setSystemId(urlOfSource);
          } }
        SAX2DTM dtm;
        SAX2DTM dtm;
        if ((source == null) && (unique) && (!incremental) && (!doIndexing))
        {






          dtm = new SAX2RTFDTM(this, source, documentID, whiteSpaceFilter, xstringFactory, doIndexing);




        }
        else
        {




          dtm = new SAX2DTM(this, source, documentID, whiteSpaceFilter, xstringFactory, doIndexing);
        }
        




        addDTM(dtm, dtmPos, 0);
        

        boolean haveXercesParser = (null != reader) && (reader.getClass().getName().equals("org.apache.xerces.parsers.SAXParser"));
        




        if (haveXercesParser) {
          incremental = true;
        }
        
        IncrementalSAXSource coParser;
        
        if ((m_incremental) && (incremental))
        {
          coParser = null;
          
          if (haveXercesParser) {
            try
            {
              coParser = (IncrementalSAXSource)Class.forName("org.apache.xml.dtm.ref.IncrementalSAXSource_Xerces").newInstance();
            }
            catch (Exception ex) {
              ex.printStackTrace();
              coParser = null;
            }
          }
          IncrementalSAXSource_Filter filter;
          if (coParser == null)
          {
            if (null == reader) {
              coParser = new IncrementalSAXSource_Filter();
            } else {
              filter = new IncrementalSAXSource_Filter();
              
              filter.setXMLReader(reader);
              coParser = filter;
            }
          }
          




















          dtm.setIncrementalSAXSource(coParser);
          
          if (null == xmlSource)
          {

            return dtm;
          }
          
          if (null == reader.getErrorHandler()) {
            reader.setErrorHandler(dtm);
          }
          reader.setDTDHandler(dtm);
          


          try
          {
            coParser.startParse(xmlSource);
          }
          catch (RuntimeException re) {
            dtm.clearCoRoutine();
            
            throw re;
          }
          catch (Exception e) {
            dtm.clearCoRoutine();
            
            throw new WrappedRuntimeException(e);
          }
        } else {
          if (null == reader)
          {

            return dtm;
          }
          

          reader.setContentHandler(dtm);
          reader.setDTDHandler(dtm);
          if (null == reader.getErrorHandler()) {
            reader.setErrorHandler(dtm);
          }
          try
          {
            reader.setProperty("http://xml.org/sax/properties/lexical-handler", dtm);
          }
          catch (SAXNotRecognizedException e) {}catch (SAXNotSupportedException e) {}
          

          try
          {
            reader.parse(xmlSource);
          } catch (RuntimeException re) {
            dtm.clearCoRoutine();
            
            throw re;
          } catch (Exception e) {
            dtm.clearCoRoutine();
            
            throw new WrappedRuntimeException(e);
          }
        }
        





        return dtm;
      }
      finally
      {
        if ((reader != null) && ((!m_incremental) || (!incremental))) {
          reader.setContentHandler(m_defaultHandler);
          reader.setDTDHandler(m_defaultHandler);
          reader.setErrorHandler(m_defaultHandler);
          
          try
          {
            reader.setProperty("http://xml.org/sax/properties/lexical-handler", null);
          }
          catch (Exception e) {}
        }
        releaseXMLReader(reader);
      }
    }
    


    throw new DTMException(XMLMessages.createXMLMessage("ER_NOT_SUPPORTED", new Object[] { source }));
  }
  











  public synchronized int getDTMHandleFromNode(Node node)
  {
    if (null == node) {
      throw new IllegalArgumentException(XMLMessages.createXMLMessage("ER_NODE_NON_NULL", null));
    }
    if ((node instanceof DTMNodeProxy)) {
      return ((DTMNodeProxy)node).getDTMNodeNumber();
    }
    






















    int max = m_dtms.length;
    for (int i = 0; i < max; i++)
    {
      DTM thisDTM = m_dtms[i];
      if ((null != thisDTM) && ((thisDTM instanceof DOM2DTM)))
      {
        int handle = ((DOM2DTM)thisDTM).getHandleOfNode(node);
        if (handle != -1) { return handle;
        }
      }
    }
    


















    Node root = node;
    for (Node p = root.getNodeType() == 2 ? ((Attr)root).getOwnerElement() : root.getParentNode(); 
        p != null; p = p.getParentNode())
    {
      root = p;
    }
    
    DOM2DTM dtm = (DOM2DTM)getDTM(new DOMSource(root), false, null, true, true);
    

    int handle;
    
    if ((node instanceof DOM2DTMdefaultNamespaceDeclarationNode))
    {



      int handle = dtm.getHandleOfNode(((Attr)node).getOwnerElement());
      handle = dtm.getAttributeNode(handle, node.getNamespaceURI(), node.getLocalName());
    }
    else {
      handle = dtm.getHandleOfNode(node);
    }
    if (-1 == handle) {
      throw new RuntimeException(XMLMessages.createXMLMessage("ER_COULD_NOT_RESOLVE_NODE", null));
    }
    return handle;
  }
  
















  public synchronized XMLReader getXMLReader(Source inputSource)
  {
    try
    {
      XMLReader reader = (inputSource instanceof SAXSource) ? ((SAXSource)inputSource).getXMLReader() : null;
      


      if (null == reader) {
        if (m_readerManager == null) {
          m_readerManager = XMLReaderManager.getInstance();
        }
      }
      return m_readerManager.getXMLReader();

    }
    catch (SAXException se)
    {

      throw new DTMException(se.getMessage(), se);
    }
  }
  









  public synchronized void releaseXMLReader(XMLReader reader)
  {
    if (m_readerManager != null) {
      m_readerManager.releaseXMLReader(reader);
    }
  }
  








  public synchronized DTM getDTM(int nodeHandle)
  {
    try
    {
      return m_dtms[(nodeHandle >>> 16)];
    }
    catch (ArrayIndexOutOfBoundsException e)
    {
      if (nodeHandle == -1) {
        return null;
      }
      throw e;
    }
  }
  












  public synchronized int getDTMIdentity(DTM dtm)
  {
    if ((dtm instanceof DTMDefaultBase))
    {
      DTMDefaultBase dtmdb = (DTMDefaultBase)dtm;
      if (dtmdb.getManager() == this) {
        return dtmdb.getDTMIDs().elementAt(0);
      }
      return -1;
    }
    
    int n = m_dtms.length;
    
    for (int i = 0; i < n; i++)
    {
      DTM tdtm = m_dtms[i];
      
      if ((tdtm == dtm) && (m_dtm_offsets[i] == 0)) {
        return i << 16;
      }
    }
    return -1;
  }
  


























  public synchronized boolean release(DTM dtm, boolean shouldHardDelete)
  {
    if ((dtm instanceof SAX2DTM))
    {
      ((SAX2DTM)dtm).clearCoRoutine();
    }
    








    if ((dtm instanceof DTMDefaultBase))
    {
      SuballocatedIntVector ids = ((DTMDefaultBase)dtm).getDTMIDs();
      for (int i = ids.size() - 1; i >= 0; i--) {
        m_dtms[(ids.elementAt(i) >>> 16)] = null;
      }
    }
    else {
      int i = getDTMIdentity(dtm);
      if (i >= 0)
      {
        m_dtms[(i >>> 16)] = null;
      }
    }
    
    dtm.documentRelease();
    return true;
  }
  







  public synchronized DTM createDocumentFragment()
  {
    try
    {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      
      dbf.setNamespaceAware(true);
      
      DocumentBuilder db = dbf.newDocumentBuilder();
      Document doc = db.newDocument();
      Node df = doc.createDocumentFragment();
      
      return getDTM(new DOMSource(df), true, null, false, false);
    }
    catch (Exception e)
    {
      throw new DTMException(e);
    }
  }
  













  public synchronized DTMIterator createDTMIterator(int whatToShow, DTMFilter filter, boolean entityReferenceExpansion)
  {
    return null;
  }
  












  public synchronized DTMIterator createDTMIterator(String xpathString, PrefixResolver presolver)
  {
    return null;
  }
  










  public synchronized DTMIterator createDTMIterator(int node)
  {
    return null;
  }
  











  public synchronized DTMIterator createDTMIterator(Object xpathCompiler, int pos)
  {
    return null;
  }
  







  public ExpandedNameTable getExpandedNameTable(DTM dtm)
  {
    return m_expandedNameTable;
  }
}
