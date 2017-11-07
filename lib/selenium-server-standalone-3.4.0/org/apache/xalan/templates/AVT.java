package org.apache.xalan.templates;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.processor.StylesheetHandler;
import org.apache.xalan.res.XSLMessages;
import org.apache.xml.utils.FastStringBuffer;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathContext;
import org.xml.sax.SAXException;








































public class AVT
  implements Serializable, XSLTVisitable
{
  static final long serialVersionUID = 5167607155517042691L;
  private static final boolean USE_OBJECT_POOL = false;
  private static final int INIT_BUFFER_CHUNK_BITS = 8;
  private String m_simpleString = null;
  




  private Vector m_parts = null;
  


  private String m_rawName;
  


  private String m_name;
  

  private String m_uri;
  


  public String getRawName()
  {
    return m_rawName;
  }
  





  public void setRawName(String rawName)
  {
    m_rawName = rawName;
  }
  











  public String getName()
  {
    return m_name;
  }
  





  public void setName(String name)
  {
    m_name = name;
  }
  











  public String getURI()
  {
    return m_uri;
  }
  





  public void setURI(String uri)
  {
    m_uri = uri;
  }
  
















  public AVT(StylesheetHandler handler, String uri, String name, String rawName, String stringedValue, ElemTemplateElement owner)
    throws TransformerException
  {
    m_uri = uri;
    m_name = name;
    m_rawName = rawName;
    
    StringTokenizer tokenizer = new StringTokenizer(stringedValue, "{}\"'", true);
    
    int nTokens = tokenizer.countTokens();
    
    if (nTokens < 2)
    {
      m_simpleString = stringedValue;
    }
    else
    {
      FastStringBuffer buffer = null;
      FastStringBuffer exprBuffer = null;
      



      buffer = new FastStringBuffer(6);
      exprBuffer = new FastStringBuffer(6);
      
      try
      {
        m_parts = new Vector(nTokens + 1);
        
        String t = null;
        String lookahead = null;
        String error = null;
        
        while (tokenizer.hasMoreTokens())
        {
          if (lookahead != null)
          {
            t = lookahead;
            lookahead = null;
          }
          else {
            t = tokenizer.nextToken();
          }
          if (t.length() == 1)
          {
            switch (t.charAt(0))
            {



            case '"': 
            case '\'': 
              buffer.append(t);
              
              break;
            



            case '{': 
              try
              {
                lookahead = tokenizer.nextToken();
                
                if (lookahead.equals("{"))
                {


                  buffer.append(lookahead);
                  
                  lookahead = null;






                }
                else
                {





                  if (buffer.length() > 0)
                  {
                    m_parts.addElement(new AVTPartSimple(buffer.toString()));
                    buffer.setLength(0);
                  }
                  
                  exprBuffer.setLength(0);
                  
                  while (null != lookahead)
                  {
                    if (lookahead.length() == 1)
                    {
                      switch (lookahead.charAt(0))
                      {



                      case '"': 
                      case '\'': 
                        exprBuffer.append(lookahead);
                        
                        String quote = lookahead;
                        

                        lookahead = tokenizer.nextToken();
                        
                        while (!lookahead.equals(quote))
                        {
                          exprBuffer.append(lookahead);
                          
                          lookahead = tokenizer.nextToken();
                        }
                        
                        exprBuffer.append(lookahead);
                        
                        lookahead = tokenizer.nextToken();
                        
                        break;
                      



                      case '{': 
                        error = XSLMessages.createMessage("ER_NO_CURLYBRACE", null);
                        

                        lookahead = null;
                        
                        break;
                      




                      case '}': 
                        buffer.setLength(0);
                        
                        XPath xpath = handler.createXPath(exprBuffer.toString(), owner);
                        

                        m_parts.addElement(new AVTPartXPath(xpath));
                        
                        lookahead = null;
                        
                        break;
                      



                      default: 
                        exprBuffer.append(lookahead);
                        
                        lookahead = tokenizer.nextToken();break;
                      

                      }
                      
                    }
                    else
                    {
                      exprBuffer.append(lookahead);
                      
                      lookahead = tokenizer.nextToken();
                    }
                  }
                  
                  if (error != null) {
                    break;
                  }
                  
                }
                

              }
              catch (NoSuchElementException ex)
              {
                error = XSLMessages.createMessage("ER_ILLEGAL_ATTRIBUTE_VALUE", new Object[] { name, stringedValue });
              }
            


            case '}': 
              lookahead = tokenizer.nextToken();
              
              if (lookahead.equals("}"))
              {


                buffer.append(lookahead);
                
                lookahead = null;

              }
              else
              {

                try
                {
                  handler.warn("WG_FOUND_CURLYBRACE", null);
                }
                catch (SAXException se)
                {
                  throw new TransformerException(se);
                }
                
                buffer.append("}");
              }
              


              break;
            



            default: 
              buffer.append(t);break;
            



            }
            
          } else {
            buffer.append(t);
          }
          
          if (null != error)
          {
            try
            {
              handler.warn("WG_ATTR_TEMPLATE", new Object[] { error });

            }
            catch (SAXException se)
            {
              throw new TransformerException(se);
            }
          }
        }
        


        if (buffer.length() > 0)
        {
          m_parts.addElement(new AVTPartSimple(buffer.toString()));
          buffer.setLength(0);

        }
        

      }
      finally
      {

        buffer = null;
        exprBuffer = null;
      }
    }
    

    if ((null == m_parts) && (null == m_simpleString))
    {


      m_simpleString = "";
    }
  }
  






  public String getSimpleString()
  {
    if (null != m_simpleString) {
      return m_simpleString;
    }
    if (null != m_parts) {
      FastStringBuffer buf = getBuffer();
      String out = null;
      
      int n = m_parts.size();
      try {
        for (int i = 0; i < n; i++) {
          AVTPart part = (AVTPart)m_parts.elementAt(i);
          buf.append(part.getSimpleString());
        }
        out = buf.toString();

      }
      finally
      {
        buf.setLength(0);
      }
      
      return out;
    }
    return "";
  }
  













  public String evaluate(XPathContext xctxt, int context, PrefixResolver nsNode)
    throws TransformerException
  {
    if (null != m_simpleString)
      return m_simpleString;
    if (null != m_parts) {
      FastStringBuffer buf = getBuffer();
      String out = null;
      int n = m_parts.size();
      try {
        for (int i = 0; i < n; i++) {
          AVTPart part = (AVTPart)m_parts.elementAt(i);
          part.evaluate(xctxt, buf, context, nsNode);
        }
        out = buf.toString();

      }
      finally
      {
        buf.setLength(0);
      }
      
      return out;
    }
    return "";
  }
  












  public boolean isContextInsensitive()
  {
    return null != m_simpleString;
  }
  







  public boolean canTraverseOutsideSubtree()
  {
    if (null != m_parts)
    {
      int n = m_parts.size();
      
      for (int i = 0; i < n; i++)
      {
        AVTPart part = (AVTPart)m_parts.elementAt(i);
        
        if (part.canTraverseOutsideSubtree()) {
          return true;
        }
      }
    }
    return false;
  }
  










  public void fixupVariables(Vector vars, int globalsSize)
  {
    if (null != m_parts)
    {
      int n = m_parts.size();
      
      for (int i = 0; i < n; i++)
      {
        AVTPart part = (AVTPart)m_parts.elementAt(i);
        
        part.fixupVariables(vars, globalsSize);
      }
    }
  }
  



  public void callVisitors(XSLTVisitor visitor)
  {
    if ((visitor.visitAVT(this)) && (null != m_parts))
    {
      int n = m_parts.size();
      
      for (int i = 0; i < n; i++)
      {
        AVTPart part = (AVTPart)m_parts.elementAt(i);
        
        part.callVisitors(visitor);
      }
    }
  }
  



  public boolean isSimple()
  {
    return m_simpleString != null;
  }
  


  private final FastStringBuffer getBuffer()
  {
    return new FastStringBuffer(8);
  }
}
