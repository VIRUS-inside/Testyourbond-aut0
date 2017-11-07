package javax.xml.transform.dom;

import javax.xml.transform.Result;
import org.w3c.dom.Node;

public class DOMResult
  implements Result
{
  public static final String FEATURE = "http://javax.xml.transform.dom.DOMResult/feature";
  private Node node = null;
  private Node nextSibling = null;
  private String systemId = null;
  
  public DOMResult()
  {
    setNode(null);
    setNextSibling(null);
    setSystemId(null);
  }
  
  public DOMResult(Node paramNode)
  {
    setNode(paramNode);
    setNextSibling(null);
    setSystemId(null);
  }
  
  public DOMResult(Node paramNode, String paramString)
  {
    setNode(paramNode);
    setNextSibling(null);
    setSystemId(paramString);
  }
  
  public DOMResult(Node paramNode1, Node paramNode2)
  {
    if (paramNode2 != null)
    {
      if (paramNode1 == null) {
        throw new IllegalArgumentException("Cannot create a DOMResult when the nextSibling is contained by the \"null\" node.");
      }
      if ((paramNode1.compareDocumentPosition(paramNode2) & 0x10) == 0) {
        throw new IllegalArgumentException("Cannot create a DOMResult when the nextSibling is not contained by the node.");
      }
    }
    setNode(paramNode1);
    setNextSibling(paramNode2);
    setSystemId(null);
  }
  
  public DOMResult(Node paramNode1, Node paramNode2, String paramString)
  {
    if (paramNode2 != null)
    {
      if (paramNode1 == null) {
        throw new IllegalArgumentException("Cannot create a DOMResult when the nextSibling is contained by the \"null\" node.");
      }
      if ((paramNode1.compareDocumentPosition(paramNode2) & 0x10) == 0) {
        throw new IllegalArgumentException("Cannot create a DOMResult when the nextSibling is not contained by the node.");
      }
    }
    setNode(paramNode1);
    setNextSibling(paramNode2);
    setSystemId(paramString);
  }
  
  public void setNode(Node paramNode)
  {
    if (nextSibling != null)
    {
      if (paramNode == null) {
        throw new IllegalStateException("Cannot create a DOMResult when the nextSibling is contained by the \"null\" node.");
      }
      if ((paramNode.compareDocumentPosition(nextSibling) & 0x10) == 0) {
        throw new IllegalArgumentException("Cannot create a DOMResult when the nextSibling is not contained by the node.");
      }
    }
    node = paramNode;
  }
  
  public Node getNode()
  {
    return node;
  }
  
  public void setNextSibling(Node paramNode)
  {
    if (paramNode != null)
    {
      if (node == null) {
        throw new IllegalStateException("Cannot create a DOMResult when the nextSibling is contained by the \"null\" node.");
      }
      if ((node.compareDocumentPosition(paramNode) & 0x10) == 0) {
        throw new IllegalArgumentException("Cannot create a DOMResult when the nextSibling is not contained by the node.");
      }
    }
    nextSibling = paramNode;
  }
  
  public Node getNextSibling()
  {
    return nextSibling;
  }
  
  public void setSystemId(String paramString)
  {
    systemId = paramString;
  }
  
  public String getSystemId()
  {
    return systemId;
  }
}
