package org.apache.xerces.dom;

import java.util.ArrayList;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DeepNodeListImpl
  implements NodeList
{
  protected NodeImpl rootNode;
  protected String tagName;
  protected int changes = 0;
  protected ArrayList nodes;
  protected String nsName;
  protected boolean enableNS = false;
  
  public DeepNodeListImpl(NodeImpl paramNodeImpl, String paramString)
  {
    rootNode = paramNodeImpl;
    tagName = paramString;
    nodes = new ArrayList();
  }
  
  public DeepNodeListImpl(NodeImpl paramNodeImpl, String paramString1, String paramString2)
  {
    this(paramNodeImpl, paramString2);
    nsName = ((paramString1 != null) && (paramString1.length() != 0) ? paramString1 : null);
    enableNS = true;
  }
  
  public int getLength()
  {
    item(Integer.MAX_VALUE);
    return nodes.size();
  }
  
  public Node item(int paramInt)
  {
    if (rootNode.changes() != changes)
    {
      nodes = new ArrayList();
      changes = rootNode.changes();
    }
    int i = nodes.size();
    if (paramInt < i) {
      return (Node)nodes.get(paramInt);
    }
    Object localObject;
    if (i == 0) {
      localObject = rootNode;
    } else {
      localObject = (NodeImpl)nodes.get(i - 1);
    }
    while ((localObject != null) && (paramInt >= nodes.size()))
    {
      localObject = nextMatchingElementAfter((Node)localObject);
      if (localObject != null) {
        nodes.add(localObject);
      }
    }
    return localObject;
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
      if ((paramNode != rootNode) && (paramNode != null) && (paramNode.getNodeType() == 1)) {
        if (!enableNS)
        {
          if ((tagName.equals("*")) || (((ElementImpl)paramNode).getTagName().equals(tagName))) {
            return paramNode;
          }
        }
        else
        {
          ElementImpl localElementImpl;
          if (tagName.equals("*"))
          {
            if ((nsName != null) && (nsName.equals("*"))) {
              return paramNode;
            }
            localElementImpl = (ElementImpl)paramNode;
            if (((nsName == null) && (localElementImpl.getNamespaceURI() == null)) || ((nsName != null) && (nsName.equals(localElementImpl.getNamespaceURI())))) {
              return paramNode;
            }
          }
          else
          {
            localElementImpl = (ElementImpl)paramNode;
            if ((localElementImpl.getLocalName() != null) && (localElementImpl.getLocalName().equals(tagName)))
            {
              if ((nsName != null) && (nsName.equals("*"))) {
                return paramNode;
              }
              if (((nsName == null) && (localElementImpl.getNamespaceURI() == null)) || ((nsName != null) && (nsName.equals(localElementImpl.getNamespaceURI())))) {
                return paramNode;
              }
            }
          }
        }
      }
    }
    return null;
  }
}
