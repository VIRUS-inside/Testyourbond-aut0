package org.apache.xml.serializer;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;




























































public class NamespaceMappings
{
  private int count = 0;
  








  private Hashtable m_namespaces = new Hashtable();
  













  private Stack m_nodeStack = new Stack();
  

  private static final String EMPTYSTRING = "";
  

  private static final String XML_PREFIX = "xml";
  

  public NamespaceMappings()
  {
    initNamespaces();
  }
  










  private void initNamespaces()
  {
    MappingRecord nn = new MappingRecord("", "", -1);
    Stack stack = createPrefixStack("");
    stack.push(nn);
    

    nn = new MappingRecord("xml", "http://www.w3.org/XML/1998/namespace", -1);
    stack = createPrefixStack("xml");
    stack.push(nn);
  }
  







  public String lookupNamespace(String prefix)
  {
    String uri = null;
    Stack stack = getPrefixStack(prefix);
    if ((stack != null) && (!stack.isEmpty())) {
      uri = peekm_uri;
    }
    if (uri == null)
      uri = "";
    return uri;
  }
  
  MappingRecord getMappingFromPrefix(String prefix)
  {
    Stack stack = (Stack)m_namespaces.get(prefix);
    return (stack != null) && (!stack.isEmpty()) ? (MappingRecord)stack.peek() : null;
  }
  









  public String lookupPrefix(String uri)
  {
    String foundPrefix = null;
    Enumeration prefixes = m_namespaces.keys();
    while (prefixes.hasMoreElements())
    {
      String prefix = (String)prefixes.nextElement();
      String uri2 = lookupNamespace(prefix);
      if ((uri2 != null) && (uri2.equals(uri)))
      {
        foundPrefix = prefix;
        break;
      }
    }
    return foundPrefix;
  }
  
  MappingRecord getMappingFromURI(String uri)
  {
    MappingRecord foundMap = null;
    Enumeration prefixes = m_namespaces.keys();
    while (prefixes.hasMoreElements())
    {
      String prefix = (String)prefixes.nextElement();
      MappingRecord map2 = getMappingFromPrefix(prefix);
      if ((map2 != null) && (m_uri.equals(uri)))
      {
        foundMap = map2;
        break;
      }
    }
    return foundMap;
  }
  




  boolean popNamespace(String prefix)
  {
    if (prefix.startsWith("xml"))
    {
      return false;
    }
    
    Stack stack;
    if ((stack = getPrefixStack(prefix)) != null)
    {
      stack.pop();
      return true;
    }
    return false;
  }
  







  public boolean pushNamespace(String prefix, String uri, int elemDepth)
  {
    if (prefix.startsWith("xml"))
    {
      return false;
    }
    
    Stack stack;
    
    if ((stack = (Stack)m_namespaces.get(prefix)) == null)
    {
      m_namespaces.put(prefix, stack = new Stack());
    }
    
    if (!stack.empty())
    {
      MappingRecord mr = (MappingRecord)stack.peek();
      if ((uri.equals(m_uri)) || (elemDepth == m_declarationDepth))
      {



        return false;
      }
    }
    MappingRecord map = new MappingRecord(prefix, uri, elemDepth);
    stack.push(map);
    m_nodeStack.push(map);
    return true;
  }
  








  void popNamespaces(int elemDepth, ContentHandler saxHandler)
  {
    for (;;)
    {
      if (m_nodeStack.isEmpty())
        return;
      MappingRecord map = (MappingRecord)m_nodeStack.peek();
      int depth = m_declarationDepth;
      if ((elemDepth < 1) || (m_declarationDepth < elemDepth)) {
        break;
      }
      


      MappingRecord nm1 = (MappingRecord)m_nodeStack.pop();
      
      String prefix = m_prefix;
      
      Stack prefixStack = getPrefixStack(prefix);
      MappingRecord nm2 = (MappingRecord)prefixStack.peek();
      if (nm1 == nm2)
      {













        prefixStack.pop();
        if (saxHandler != null)
        {
          try
          {
            saxHandler.endPrefixMapping(prefix);
          }
          catch (SAXException e) {}
        }
      }
    }
  }
  








  public String generateNextPrefix()
  {
    return "ns" + count++;
  }
  



  public Object clone()
    throws CloneNotSupportedException
  {
    NamespaceMappings clone = new NamespaceMappings();
    m_nodeStack = ((Stack)m_nodeStack.clone());
    count = count;
    m_namespaces = ((Hashtable)m_namespaces.clone());
    
    count = count;
    return clone;
  }
  

  final void reset()
  {
    count = 0;
    m_namespaces.clear();
    m_nodeStack.clear();
    
    initNamespaces();
  }
  

  static class MappingRecord
  {
    final String m_prefix;
    
    final String m_uri;
    
    final int m_declarationDepth;
    

    MappingRecord(String prefix, String uri, int depth)
    {
      m_prefix = prefix;
      m_uri = (uri == null ? "" : uri);
      m_declarationDepth = depth;
    }
  }
  




  private class Stack
  {
    private int top = -1;
    private int max = 20;
    Object[] m_stack = new Object[max];
    
    public Object clone() throws CloneNotSupportedException {
      Stack clone = new Stack(NamespaceMappings.this);
      max = max;
      top = top;
      m_stack = new Object[max];
      for (int i = 0; i <= top; i++)
      {

        m_stack[i] = m_stack[i];
      }
      return clone;
    }
    

    public Stack() {}
    
    public Object push(Object o)
    {
      top += 1;
      if (max <= top) {
        int newMax = 2 * max + 1;
        Object[] newArray = new Object[newMax];
        System.arraycopy(m_stack, 0, newArray, 0, max);
        max = newMax;
        m_stack = newArray;
      }
      m_stack[top] = o;
      return o;
    }
    
    public Object pop() {
      Object o;
      if (0 <= top) {
        Object o = m_stack[top];
        
        top -= 1;
      }
      else {
        o = null; }
      return o;
    }
    
    public Object peek() { Object o;
      Object o;
      if (0 <= top) {
        o = m_stack[top];
      }
      else
        o = null;
      return o;
    }
    
    public Object peek(int idx) {
      return m_stack[idx];
    }
    
    public boolean isEmpty() {
      return top < 0;
    }
    
    public boolean empty() { return top < 0; }
    
    public void clear()
    {
      for (int i = 0; i <= top; i++)
        m_stack[i] = null;
      top = -1;
    }
    
    public Object getElement(int index) {
      return m_stack[index];
    }
  }
  




  private Stack getPrefixStack(String prefix)
  {
    Stack fs = (Stack)m_namespaces.get(prefix);
    return fs;
  }
  




  private Stack createPrefixStack(String prefix)
  {
    Stack fs = new Stack();
    m_namespaces.put(prefix, fs);
    return fs;
  }
  









  public String[] lookupAllPrefixes(String uri)
  {
    ArrayList foundPrefixes = new ArrayList();
    Enumeration prefixes = m_namespaces.keys();
    while (prefixes.hasMoreElements())
    {
      String prefix = (String)prefixes.nextElement();
      String uri2 = lookupNamespace(prefix);
      if ((uri2 != null) && (uri2.equals(uri)))
      {
        foundPrefixes.add(prefix);
      }
    }
    String[] prefixArray = new String[foundPrefixes.size()];
    foundPrefixes.toArray(prefixArray);
    return prefixArray;
  }
}
