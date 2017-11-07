package org.apache.xalan.templates;

import javax.xml.transform.TransformerException;
import org.apache.xalan.serialize.SerializerUtils;
import org.apache.xalan.trace.TraceManager;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xalan.transformer.TreeWalker2Result;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.dtm.ref.DTMTreeWalker;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xpath.Expression;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;
import org.xml.sax.SAXException;
































public class ElemCopyOf
  extends ElemTemplateElement
{
  static final long serialVersionUID = -7433828829497411127L;
  public XPath m_selectExpression = null;
  


  public ElemCopyOf() {}
  


  public void setSelect(XPath expr)
  {
    m_selectExpression = expr;
  }
  






  public XPath getSelect()
  {
    return m_selectExpression;
  }
  





  public void compose(StylesheetRoot sroot)
    throws TransformerException
  {
    super.compose(sroot);
    
    StylesheetRoot.ComposeState cstate = sroot.getComposeState();
    m_selectExpression.fixupVariables(cstate.getVariableNames(), cstate.getGlobalsSize());
  }
  






  public int getXSLToken()
  {
    return 74;
  }
  





  public String getNodeName()
  {
    return "copy-of";
  }
  











  public void execute(TransformerImpl transformer)
    throws TransformerException
  {
    if (transformer.getDebug()) {
      transformer.getTraceManager().fireTraceEvent(this);
    }
    try
    {
      XPathContext xctxt = transformer.getXPathContext();
      int sourceNode = xctxt.getCurrentNode();
      XObject value = m_selectExpression.execute(xctxt, sourceNode, this);
      
      if (transformer.getDebug()) {
        transformer.getTraceManager().fireSelectedEvent(sourceNode, this, "select", m_selectExpression, value);
      }
      
      SerializationHandler handler = transformer.getSerializationHandler();
      
      if (null != value)
      {
        int type = value.getType();
        String s;
        DTMIterator nl;
        DTMTreeWalker tw; switch (type)
        {
        case 1: 
        case 2: 
        case 3: 
          s = value.str();
          
          handler.characters(s.toCharArray(), 0, s.length());
          break;
        

        case 4: 
          nl = value.iter();
          

          tw = new TreeWalker2Result(transformer, handler);
        case 5: default: 
          int pos;
          while (-1 != (pos = nl.nextNode()))
          {
            DTM dtm = xctxt.getDTMManager().getDTM(pos);
            short t = dtm.getNodeType(pos);
            


            if (t == 9)
            {
              for (int child = dtm.getFirstChild(pos); child != -1; 
                  child = dtm.getNextSibling(child))
              {
                tw.traverse(child);
              }
              
            } else if (t == 2)
            {
              SerializerUtils.addAttribute(handler, pos);
            }
            else
            {
              tw.traverse(pos);
            }
            continue;
            


            SerializerUtils.outputResultTreeFragment(handler, value, transformer.getXPathContext());
            
            break;
            

            s = value.str();
            
            handler.characters(s.toCharArray(), 0, s.length());
          }
        




        }
        
      }
    }
    catch (SAXException se)
    {
      throw new TransformerException(se);
    }
    finally
    {
      if (transformer.getDebug()) {
        transformer.getTraceManager().fireTraceEndEvent(this);
      }
    }
  }
  








  public ElemTemplateElement appendChild(ElemTemplateElement newChild)
  {
    error("ER_CANNOT_ADD", new Object[] { newChild.getNodeName(), getNodeName() });
    



    return null;
  }
  




  protected void callChildVisitors(XSLTVisitor visitor, boolean callAttrs)
  {
    if (callAttrs)
      m_selectExpression.getExpression().callVisitors(m_selectExpression, visitor);
    super.callChildVisitors(visitor, callAttrs);
  }
}
