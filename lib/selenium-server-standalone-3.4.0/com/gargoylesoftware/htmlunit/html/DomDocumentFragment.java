package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.SgmlPage;
import org.w3c.dom.DocumentFragment;























public class DomDocumentFragment
  extends DomNode
  implements DocumentFragment
{
  public static final String NODE_NAME = "#document-fragment";
  
  public DomDocumentFragment(SgmlPage page)
  {
    super(page);
  }
  




  public String getNodeName()
  {
    return "#document-fragment";
  }
  




  public short getNodeType()
  {
    return 11;
  }
  



  public String asXml()
  {
    StringBuilder sb = new StringBuilder();
    for (DomNode node : getChildren()) {
      sb.append(node.asXml());
    }
    return sb.toString();
  }
  





  public boolean isAttachedToPage()
  {
    return false;
  }
}
