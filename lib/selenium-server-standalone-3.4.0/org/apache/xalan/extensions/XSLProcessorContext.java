package org.apache.xalan.extensions;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import javax.xml.transform.TransformerException;
import org.apache.xalan.serialize.SerializerUtils;
import org.apache.xalan.templates.Stylesheet;
import org.apache.xalan.transformer.ClonerToResultTree;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xml.utils.QName;
import org.apache.xpath.NodeSetDTM;
import org.apache.xpath.XPathContext;
import org.apache.xpath.axes.DescendantIterator;
import org.apache.xpath.axes.OneStepIterator;
import org.apache.xpath.objects.XBoolean;
import org.apache.xpath.objects.XNodeSet;
import org.apache.xpath.objects.XNumber;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XRTreeFrag;
import org.apache.xpath.objects.XString;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.SAXException;




























public class XSLProcessorContext
{
  private TransformerImpl transformer;
  private Stylesheet stylesheetTree;
  private DTM sourceTree;
  private int sourceNode;
  private QName mode;
  
  public XSLProcessorContext(TransformerImpl transformer, Stylesheet stylesheetTree)
  {
    this.transformer = transformer;
    this.stylesheetTree = stylesheetTree;
    
    XPathContext xctxt = transformer.getXPathContext();
    mode = transformer.getMode();
    sourceNode = xctxt.getCurrentNode();
    sourceTree = xctxt.getDTM(sourceNode);
  }
  








  public TransformerImpl getTransformer()
  {
    return transformer;
  }
  








  public Stylesheet getStylesheet()
  {
    return stylesheetTree;
  }
  








  public Node getSourceTree()
  {
    return sourceTree.getNode(sourceTree.getDocumentRoot(sourceNode));
  }
  








  public Node getContextNode()
  {
    return sourceTree.getNode(sourceNode);
  }
  








  public QName getMode()
  {
    return mode;
  }
  
















  public void outputToResultTree(Stylesheet stylesheetTree, Object obj)
    throws TransformerException, MalformedURLException, FileNotFoundException, IOException
  {
    try
    {
      SerializationHandler rtreeHandler = transformer.getResultTreeHandler();
      XPathContext xctxt = transformer.getXPathContext();
      

      XObject value;
      
      XObject value;
      
      if ((obj instanceof XObject))
      {
        value = (XObject)obj;
      } else { XObject value;
        if ((obj instanceof String))
        {
          value = new XString((String)obj);
        } else { XObject value;
          if ((obj instanceof Boolean))
          {
            value = new XBoolean(((Boolean)obj).booleanValue());
          } else { XObject value;
            if ((obj instanceof Double))
            {
              value = new XNumber(((Double)obj).doubleValue());
            } else { XObject value;
              if ((obj instanceof DocumentFragment))
              {
                int handle = xctxt.getDTMHandleFromNode((DocumentFragment)obj);
                
                value = new XRTreeFrag(handle, xctxt);
              } else { XObject value;
                if ((obj instanceof DTM))
                {
                  DTM dtm = (DTM)obj;
                  DTMIterator iterator = new DescendantIterator();
                  




                  iterator.setRoot(dtm.getDocument(), xctxt);
                  value = new XNodeSet(iterator);
                } else { XObject value;
                  if ((obj instanceof DTMAxisIterator))
                  {
                    DTMAxisIterator iter = (DTMAxisIterator)obj;
                    DTMIterator iterator = new OneStepIterator(iter, -1);
                    value = new XNodeSet(iterator);
                  } else { XObject value;
                    if ((obj instanceof DTMIterator))
                    {
                      value = new XNodeSet((DTMIterator)obj);
                    } else { XObject value;
                      if ((obj instanceof NodeIterator))
                      {
                        value = new XNodeSet(new NodeSetDTM((NodeIterator)obj, xctxt));
                      } else { XObject value;
                        if ((obj instanceof Node))
                        {
                          value = new XNodeSet(xctxt.getDTMHandleFromNode((Node)obj), xctxt.getDTMManager());

                        }
                        else
                        {

                          value = new XString(obj.toString()); }
                      }
                    } } } } } } } }
      int type = value.getType();
      
      DTMIterator nl;
      switch (type)
      {
      case 1: 
      case 2: 
      case 3: 
        String s = value.str();
        
        rtreeHandler.characters(s.toCharArray(), 0, s.length());
        break;
      
      case 4: 
        nl = value.iter();
      case 5: 
        int pos;
        
        while (-1 != (pos = nl.nextNode()))
        {
          DTM dtm = nl.getDTM(pos);
          int top = pos;
          
          while (-1 != pos)
          {
            rtreeHandler.flushPending();
            ClonerToResultTree.cloneToResultTree(pos, dtm.getNodeType(pos), dtm, rtreeHandler, true);
            

            int nextNode = dtm.getFirstChild(pos);
            
            while (-1 == nextNode)
            {
              if (1 == dtm.getNodeType(pos))
              {
                rtreeHandler.endElement("", "", dtm.getNodeName(pos));
              }
              
              if (top != pos)
              {

                nextNode = dtm.getNextSibling(pos);
                
                if (-1 == nextNode)
                {
                  pos = dtm.getParent(pos);
                  
                  if (top == pos)
                  {
                    if (1 == dtm.getNodeType(pos))
                    {
                      rtreeHandler.endElement("", "", dtm.getNodeName(pos));
                    }
                    
                    nextNode = -1;
                  }
                }
              }
            }
            

            pos = nextNode;
          }
          continue;
          

          SerializerUtils.outputResultTreeFragment(rtreeHandler, value, transformer.getXPathContext());
        }
      

      }
      
    }
    catch (SAXException se)
    {
      throw new TransformerException(se);
    }
  }
}
