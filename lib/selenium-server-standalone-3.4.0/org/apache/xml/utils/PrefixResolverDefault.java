package org.apache.xml.utils;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;






































public class PrefixResolverDefault
  implements PrefixResolver
{
  Node m_context;
  
  public PrefixResolverDefault(Node xpathExpressionContext)
  {
    m_context = xpathExpressionContext;
  }
  








  public String getNamespaceForPrefix(String prefix)
  {
    return getNamespaceForPrefix(prefix, m_context);
  }
  












  public String getNamespaceForPrefix(String prefix, Node namespaceContext)
  {
    Node parent = namespaceContext;
    String namespace = null;
    
    if (prefix.equals("xml"))
    {
      namespace = "http://www.w3.org/XML/1998/namespace";
    }
    else
    {
      int type;
      

      while ((null != parent) && (null == namespace) && (((type = parent.getNodeType()) == 1) || (type == 5)))
      {

        if (type == 1)
        {
          if (parent.getNodeName().indexOf(prefix + ":") == 0)
            return parent.getNamespaceURI();
          NamedNodeMap nnm = parent.getAttributes();
          
          for (int i = 0; i < nnm.getLength(); i++)
          {
            Node attr = nnm.item(i);
            String aname = attr.getNodeName();
            boolean isPrefix = aname.startsWith("xmlns:");
            
            if ((isPrefix) || (aname.equals("xmlns")))
            {
              int index = aname.indexOf(':');
              String p = isPrefix ? aname.substring(index + 1) : "";
              
              if (p.equals(prefix))
              {
                namespace = attr.getNodeValue();
                
                break;
              }
            }
          }
        }
        
        parent = parent.getParentNode();
      }
    }
    
    return namespace;
  }
  





  public String getBaseIdentifier()
  {
    return null;
  }
  

  public boolean handlesNullPrefixes()
  {
    return false;
  }
}
