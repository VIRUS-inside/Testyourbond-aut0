package org.seleniumhq.jetty9.util.preventers;

import javax.xml.parsers.DocumentBuilderFactory;
import org.seleniumhq.jetty9.util.log.Logger;
































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
