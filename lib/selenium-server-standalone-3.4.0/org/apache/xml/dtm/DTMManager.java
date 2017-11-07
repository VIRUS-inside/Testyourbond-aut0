package org.apache.xml.dtm;

import javax.xml.transform.Source;
import org.apache.xml.res.XMLMessages;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.XMLStringFactory;
import org.w3c.dom.Node;













































public abstract class DTMManager
{
  private static final String defaultPropName = "org.apache.xml.dtm.DTMManager";
  private static String defaultClassName = "org.apache.xml.dtm.ref.DTMManagerDefault";
  





  protected XMLStringFactory m_xsf = null;
  











  public XMLStringFactory getXMLStringFactory()
  {
    return m_xsf;
  }
  






  public void setXMLStringFactory(XMLStringFactory xsf)
  {
    m_xsf = xsf;
  }
  







































  public static DTMManager newInstance(XMLStringFactory xsf)
    throws DTMConfigurationException
  {
    DTMManager factoryImpl = null;
    try
    {
      factoryImpl = (DTMManager)ObjectFactory.createObject("org.apache.xml.dtm.DTMManager", defaultClassName);

    }
    catch (ObjectFactory.ConfigurationError e)
    {
      throw new DTMConfigurationException(XMLMessages.createXMLMessage("ER_NO_DEFAULT_IMPL", null), e.getException());
    }
    


    if (factoryImpl == null)
    {
      throw new DTMConfigurationException(XMLMessages.createXMLMessage("ER_NO_DEFAULT_IMPL", null));
    }
    


    factoryImpl.setXMLStringFactory(xsf);
    
    return factoryImpl;
  }
  


































































































































  public boolean m_incremental = false;
  






  public boolean m_source_location = false;
  private static boolean debug;
  public static final int IDENT_DTM_NODE_BITS = 16;
  public static final int IDENT_NODE_DEFAULT = 65535;
  public static final int IDENT_DTM_DEFAULT = -65536;
  public static final int IDENT_MAX_DTMS = 65536;
  
  public boolean getIncremental()
  {
    return m_incremental;
  }
  








  public void setIncremental(boolean incremental)
  {
    m_incremental = incremental;
  }
  







  public boolean getSource_location()
  {
    return m_source_location;
  }
  








  public void setSource_location(boolean sourceLocation)
  {
    m_source_location = sourceLocation;
  }
  








  static
  {
    try
    {
      debug = System.getProperty("dtm.debug") != null;
    }
    catch (SecurityException ex) {}
  }
  



















































  public int getDTMIdentityMask()
  {
    return -65536;
  }
  





  public int getNodeIdentityMask()
  {
    return 65535;
  }
  
  protected DTMManager() {}
  
  public abstract DTM getDTM(Source paramSource, boolean paramBoolean1, DTMWSFilter paramDTMWSFilter, boolean paramBoolean2, boolean paramBoolean3);
  
  public abstract DTM getDTM(int paramInt);
  
  public abstract int getDTMHandleFromNode(Node paramNode);
  
  public abstract DTM createDocumentFragment();
  
  public abstract boolean release(DTM paramDTM, boolean paramBoolean);
  
  public abstract DTMIterator createDTMIterator(Object paramObject, int paramInt);
  
  public abstract DTMIterator createDTMIterator(String paramString, PrefixResolver paramPrefixResolver);
  
  public abstract DTMIterator createDTMIterator(int paramInt, DTMFilter paramDTMFilter, boolean paramBoolean);
  
  public abstract DTMIterator createDTMIterator(int paramInt);
  
  public abstract int getDTMIdentity(DTM paramDTM);
}
