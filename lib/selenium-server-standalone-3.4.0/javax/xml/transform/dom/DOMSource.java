package javax.xml.transform.dom;

import javax.xml.transform.Source;
import org.w3c.dom.Node;

public class DOMSource
  implements Source
{
  private Node node;
  private String systemID;
  public static final String FEATURE = "http://javax.xml.transform.dom.DOMSource/feature";
  
  public DOMSource() {}
  
  public DOMSource(Node paramNode)
  {
    setNode(paramNode);
  }
  
  public DOMSource(Node paramNode, String paramString)
  {
    setNode(paramNode);
    setSystemId(paramString);
  }
  
  public void setNode(Node paramNode)
  {
    node = paramNode;
  }
  
  public Node getNode()
  {
    return node;
  }
  
  public void setSystemId(String paramString)
  {
    systemID = paramString;
  }
  
  public String getSystemId()
  {
    return systemID;
  }
}
