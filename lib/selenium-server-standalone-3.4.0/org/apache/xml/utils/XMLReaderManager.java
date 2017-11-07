package org.apache.xml.utils;

import java.util.Hashtable;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;




























public class XMLReaderManager
{
  private static final String NAMESPACES_FEATURE = "http://xml.org/sax/features/namespaces";
  private static final String NAMESPACE_PREFIXES_FEATURE = "http://xml.org/sax/features/namespace-prefixes";
  private static final XMLReaderManager m_singletonManager = new XMLReaderManager();
  



  private static SAXParserFactory m_parserFactory;
  



  private ThreadLocal m_readers;
  



  private Hashtable m_inUse;
  




  private XMLReaderManager() {}
  



  public static XMLReaderManager getInstance()
  {
    return m_singletonManager;
  }
  







  public synchronized XMLReader getXMLReader()
    throws SAXException
  {
    if (m_readers == null)
    {

      m_readers = new ThreadLocal();
    }
    
    if (m_inUse == null) {
      m_inUse = new Hashtable();
    }
    


    XMLReader reader = (XMLReader)m_readers.get();
    boolean threadHasReader = reader != null;
    if ((!threadHasReader) || (m_inUse.get(reader) == Boolean.TRUE))
    {

      try
      {
        try
        {
          reader = XMLReaderFactory.createXMLReader();
        }
        catch (Exception e)
        {
          try {
            if (m_parserFactory == null) {
              m_parserFactory = SAXParserFactory.newInstance();
              m_parserFactory.setNamespaceAware(true);
            }
            
            reader = m_parserFactory.newSAXParser().getXMLReader();
          } catch (ParserConfigurationException pce) {
            throw pce;
          }
        }
        try {
          reader.setFeature("http://xml.org/sax/features/namespaces", true);
          reader.setFeature("http://xml.org/sax/features/namespace-prefixes", false);
        }
        catch (SAXException se) {}
      }
      catch (ParserConfigurationException ex)
      {
        throw new SAXException(ex);
      } catch (FactoryConfigurationError ex1) {
        throw new SAXException(ex1.toString());
      }
      catch (NoSuchMethodError ex2) {}catch (AbstractMethodError ame) {}
      



      if (!threadHasReader) {
        m_readers.set(reader);
        m_inUse.put(reader, Boolean.TRUE);
      }
    } else {
      m_inUse.put(reader, Boolean.TRUE);
    }
    
    return reader;
  }
  







  public synchronized void releaseXMLReader(XMLReader reader)
  {
    if ((m_readers.get() == reader) && (reader != null)) {
      m_inUse.remove(reader);
    }
  }
}
