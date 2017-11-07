package org.apache.xml.utils;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;




















































public abstract class Hashtree2Node
{
  public Hashtree2Node() {}
  
  public static void appendHashToNode(Hashtable hash, String name, Node container, Document factory)
  {
    if ((null == container) || (null == factory) || (null == hash))
    {
      return;
    }
    

    String elemName = null;
    if ((null == name) || ("".equals(name))) {
      elemName = "appendHashToNode";
    } else {
      elemName = name;
    }
    try
    {
      Element hashNode = factory.createElement(elemName);
      container.appendChild(hashNode);
      
      Enumeration keys = hash.keys();
      List v = new ArrayList();
      
      while (keys.hasMoreElements())
      {
        Object key = keys.nextElement();
        String keyStr = key.toString();
        Object item = hash.get(key);
        
        if ((item instanceof Hashtable))
        {



          v.add(keyStr);
          v.add((Hashtable)item);

        }
        else
        {
          try
          {
            Element node = factory.createElement("item");
            node.setAttribute("key", keyStr);
            node.appendChild(factory.createTextNode((String)item));
            hashNode.appendChild(node);
          }
          catch (Exception e)
          {
            Element node = factory.createElement("item");
            node.setAttribute("key", keyStr);
            node.appendChild(factory.createTextNode("ERROR: Reading " + key + " threw: " + e.toString()));
            hashNode.appendChild(node);
          }
        }
      }
      

      Iterator it = v.iterator();
      while (it.hasNext())
      {

        String n = (String)it.next();
        Hashtable h = (Hashtable)it.next();
        
        appendHashToNode(h, n, hashNode, factory);
      }
      

    }
    catch (Exception e2)
    {
      e2.printStackTrace();
    }
  }
}
