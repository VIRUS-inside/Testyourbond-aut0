package org.eclipse.jetty.util.preventers;

import javax.xml.parsers.DocumentBuilderFactory;
import org.eclipse.jetty.util.log.Logger;
































public class DOMLeakPreventer
  extends AbstractLeakPreventer
{
  public DOMLeakPreventer() {}
  
  public void prevent(ClassLoader loader)
  {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    try
    {
      factory.newDocumentBuilder();
    }
    catch (Exception e)
    {
      LOG.warn(e);
    }
  }
}
