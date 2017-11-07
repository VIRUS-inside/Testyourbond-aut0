package org.apache.xalan.templates;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.trace.TraceManager;
import org.apache.xalan.transformer.NodeSorter;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.utils.IntStack;
import org.apache.xpath.Expression;
import org.apache.xpath.ExpressionOwner;
import org.apache.xpath.SourceTreeManager;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XNodeSet;
import org.apache.xpath.objects.XObject;

















































public class ElemForEach
  extends ElemTemplateElement
  implements ExpressionOwner
{
  static final long serialVersionUID = 6018140636363583690L;
  static final boolean DEBUG = false;
  public boolean m_doc_cache_off = false;
  









  protected Expression m_selectExpression = null;
  





  protected XPath m_xpath = null;
  


  public ElemForEach() {}
  

  public void setSelect(XPath xpath)
  {
    m_selectExpression = xpath.getExpression();
    


    m_xpath = xpath;
  }
  





  public Expression getSelect()
  {
    return m_selectExpression;
  }
  










  public void compose(StylesheetRoot sroot)
    throws TransformerException
  {
    super.compose(sroot);
    
    int length = getSortElemCount();
    
    for (int i = 0; i < length; i++)
    {
      getSortElem(i).compose(sroot);
    }
    
    Vector vnames = sroot.getComposeState().getVariableNames();
    
    if (null != m_selectExpression) {
      m_selectExpression.fixupVariables(vnames, sroot.getComposeState().getGlobalsSize());
    }
    else
    {
      m_selectExpression = getStylesheetRootm_selectDefault.getExpression();
    }
  }
  



  public void endCompose(StylesheetRoot sroot)
    throws TransformerException
  {
    int length = getSortElemCount();
    
    for (int i = 0; i < length; i++)
    {
      getSortElem(i).endCompose(sroot);
    }
    
    super.endCompose(sroot);
  }
  























  protected Vector m_sortElems = null;
  




  public int getSortElemCount()
  {
    return m_sortElems == null ? 0 : m_sortElems.size();
  }
  







  public ElemSort getSortElem(int i)
  {
    return (ElemSort)m_sortElems.elementAt(i);
  }
  






  public void setSortElem(ElemSort sortElem)
  {
    if (null == m_sortElems) {
      m_sortElems = new Vector();
    }
    m_sortElems.addElement(sortElem);
  }
  






  public int getXSLToken()
  {
    return 28;
  }
  





  public String getNodeName()
  {
    return "for-each";
  }
  







  public void execute(TransformerImpl transformer)
    throws TransformerException
  {
    transformer.pushCurrentTemplateRuleIsNull(true);
    if (transformer.getDebug()) {
      transformer.getTraceManager().fireTraceEvent(this);
    }
    try
    {
      transformSelectedNodes(transformer);
    }
    finally
    {
      if (transformer.getDebug())
        transformer.getTraceManager().fireTraceEndEvent(this);
      transformer.popCurrentTemplateRuleIsNull();
    }
  }
  






  protected ElemTemplateElement getTemplateMatch()
  {
    return this;
  }
  














  public DTMIterator sortNodes(XPathContext xctxt, Vector keys, DTMIterator sourceNodes)
    throws TransformerException
  {
    NodeSorter sorter = new NodeSorter(xctxt);
    sourceNodes.setShouldCacheNodes(true);
    sourceNodes.runTo(-1);
    xctxt.pushContextNodeList(sourceNodes);
    
    try
    {
      sorter.sort(sourceNodes, keys, xctxt);
      sourceNodes.setCurrentPos(0);
    }
    finally
    {
      xctxt.popContextNodeList();
    }
    
    return sourceNodes;
  }
  









  public void transformSelectedNodes(TransformerImpl transformer)
    throws TransformerException
  {
    XPathContext xctxt = transformer.getXPathContext();
    int sourceNode = xctxt.getCurrentNode();
    DTMIterator sourceNodes = m_selectExpression.asIterator(xctxt, sourceNode);
    


    try
    {
      Vector keys = m_sortElems == null ? null : transformer.processSortKeys(this, sourceNode);
      



      if (null != keys) {
        sourceNodes = sortNodes(xctxt, keys, sourceNodes);
      }
      if (transformer.getDebug())
      {















        Expression expr = m_xpath.getExpression();
        XObject xObject = expr.execute(xctxt);
        int current = xctxt.getCurrentNode();
        transformer.getTraceManager().fireSelectedEvent(current, this, "select", m_xpath, xObject);
      }
      







      xctxt.pushCurrentNode(-1);
      
      IntStack currentNodes = xctxt.getCurrentNodeStack();
      
      xctxt.pushCurrentExpressionNode(-1);
      
      IntStack currentExpressionNodes = xctxt.getCurrentExpressionNodeStack();
      
      xctxt.pushSAXLocatorNull();
      xctxt.pushContextNodeList(sourceNodes);
      transformer.pushElemTemplateElement(null);
      


      DTM dtm = xctxt.getDTM(sourceNode);
      int docID = sourceNode & 0xFFFF0000;
      
      int child;
      while (-1 != (child = sourceNodes.nextNode()))
      {
        currentNodes.setTop(child);
        currentExpressionNodes.setTop(child);
        
        if ((child & 0xFFFF0000) != docID)
        {
          dtm = xctxt.getDTM(child);
          docID = child & 0xFFFF0000;
        }
        

        int nodeType = dtm.getNodeType(child);
        

        if (transformer.getDebug())
        {
          transformer.getTraceManager().fireTraceEvent(this);
        }
        



        for (ElemTemplateElement t = m_firstChild; t != null; 
            t = m_nextSibling)
        {
          xctxt.setSAXLocator(t);
          transformer.setCurrentElement(t);
          t.execute(transformer);
        }
        
        if (transformer.getDebug())
        {


          transformer.setCurrentElement(null);
          transformer.getTraceManager().fireTraceEndEvent(this);
        }
        











        if (m_doc_cache_off)
        {






          xctxt.getSourceTreeManager().removeDocumentFromCache(dtm.getDocument());
          xctxt.release(dtm, false);
        }
      }
    }
    finally
    {
      if (transformer.getDebug()) {
        transformer.getTraceManager().fireSelectedEndEvent(sourceNode, this, "select", new XPath(m_selectExpression), new XNodeSet(sourceNodes));
      }
      

      xctxt.popSAXLocator();
      xctxt.popContextNodeList();
      transformer.popElemTemplateElement();
      xctxt.popCurrentExpressionNode();
      xctxt.popCurrentNode();
      sourceNodes.detach();
    }
  }
  













  public ElemTemplateElement appendChild(ElemTemplateElement newChild)
  {
    int type = newChild.getXSLToken();
    
    if (64 == type)
    {
      setSortElem((ElemSort)newChild);
      
      return newChild;
    }
    
    return super.appendChild(newChild);
  }
  




  public void callChildVisitors(XSLTVisitor visitor, boolean callAttributes)
  {
    if ((callAttributes) && (null != m_selectExpression)) {
      m_selectExpression.callVisitors(this, visitor);
    }
    int length = getSortElemCount();
    
    for (int i = 0; i < length; i++)
    {
      getSortElem(i).callVisitors(visitor);
    }
    
    super.callChildVisitors(visitor, callAttributes);
  }
  



  public Expression getExpression()
  {
    return m_selectExpression;
  }
  



  public void setExpression(Expression exp)
  {
    exp.exprSetParent(this);
    m_selectExpression = exp;
  }
  




  private void readObject(ObjectInputStream os)
    throws IOException, ClassNotFoundException
  {
    os.defaultReadObject();
    m_xpath = null;
  }
}
