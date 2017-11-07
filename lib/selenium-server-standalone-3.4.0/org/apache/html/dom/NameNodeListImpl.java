package org.apache.html.dom;

import org.apache.xerces.dom.DeepNodeListImpl;
import org.apache.xerces.dom.ElementImpl;
import org.apache.xerces.dom.NodeImpl;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class NameNodeListImpl
  extends DeepNodeListImpl
  implements NodeList
{
  public NameNodeListImpl(NodeImpl paramNodeImpl, String paramString)
  {
    super(paramNodeImpl, paramString);
  }
  
  protected Node nextMatchingElementAfter(Node paramNode)
  {
    while (paramNode != null)
    {
      if (paramNode.hasChildNodes())
      {
        paramNode = paramNode.getFirstChild();
      }
      else
      {
        Node localNode;
        if ((paramNode != rootNode) && (null != (localNode = paramNode.getNextSibling())))
        {
          paramNode = localNode;
        }
        else
        {
          localNode = null;
          while (paramNode != rootNode)
          {
            localNode = paramNode.getNextSibling();
            if (localNode != null) {
              break;
            }
            paramNode = paramNode.getParentNode();
          }
          paramNode = localNode;
        }
      }
      if ((paramNode != rootNode) && (paramNode != null) && (paramNode.getNodeType() == 1))
      {
        String str = ((ElementImpl)paramNode).getAttribute("name");
        if ((str.equals("*")) || (str.equals(tagName))) {
          return paramNode;
        }
      }
    }
    return null;
  }
}
