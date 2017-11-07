package org.apache.xalan.templates;

import java.util.Vector;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;
import org.apache.xalan.trace.TraceManager;
import org.apache.xalan.transformer.StackGuard;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xml.utils.IntStack;
import org.apache.xml.utils.QName;
import org.apache.xpath.Expression;
import org.apache.xpath.VariableStack;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XNodeSet;
import org.apache.xpath.objects.XObject;
import org.xml.sax.SAXException;



































public class ElemApplyTemplates
  extends ElemCallTemplate
{
  static final long serialVersionUID = 2903125371542621004L;
  private QName m_mode = null;
  


  public ElemApplyTemplates() {}
  

  public void setMode(QName mode)
  {
    m_mode = mode;
  }
  





  public QName getMode()
  {
    return m_mode;
  }
  







  private boolean m_isDefaultTemplate = false;
  














  public void setIsDefaultTemplate(boolean b)
  {
    m_isDefaultTemplate = b;
  }
  






  public int getXSLToken()
  {
    return 50;
  }
  





  public void compose(StylesheetRoot sroot)
    throws TransformerException
  {
    super.compose(sroot);
  }
  





  public String getNodeName()
  {
    return "apply-templates";
  }
  








  public void execute(TransformerImpl transformer)
    throws TransformerException
  {
    transformer.pushCurrentTemplateRuleIsNull(false);
    
    boolean pushMode = false;
    




    try
    {
      QName mode = transformer.getMode();
      
      if (!m_isDefaultTemplate)
      {
        if (((null == mode) && (null != m_mode)) || ((null != mode) && (!mode.equals(m_mode))))
        {

          pushMode = true;
          
          transformer.pushMode(m_mode);
        }
      }
      if (transformer.getDebug()) {
        transformer.getTraceManager().fireTraceEvent(this);
      }
      transformSelectedNodes(transformer);
    }
    finally
    {
      if (transformer.getDebug()) {
        transformer.getTraceManager().fireTraceEndEvent(this);
      }
      if (pushMode) {
        transformer.popMode();
      }
      transformer.popCurrentTemplateRuleIsNull();
    }
  }
  










  public void transformSelectedNodes(TransformerImpl transformer)
    throws TransformerException
  {
    XPathContext xctxt = transformer.getXPathContext();
    int sourceNode = xctxt.getCurrentNode();
    DTMIterator sourceNodes = m_selectExpression.asIterator(xctxt, sourceNode);
    VariableStack vars = xctxt.getVarStack();
    int nParams = getParamElemCount();
    int thisframe = vars.getStackFrame();
    StackGuard guard = transformer.getStackGuard();
    boolean check = guard.getRecursionLimit() > -1;
    
    boolean pushContextNodeListFlag = false;
    

    try
    {
      xctxt.pushCurrentNode(-1);
      xctxt.pushCurrentExpressionNode(-1);
      xctxt.pushSAXLocatorNull();
      transformer.pushElemTemplateElement(null);
      Vector keys = m_sortElems == null ? null : transformer.processSortKeys(this, sourceNode);
      



      if (null != keys) {
        sourceNodes = sortNodes(xctxt, keys, sourceNodes);
      }
      if (transformer.getDebug())
      {
        transformer.getTraceManager().fireSelectedEvent(sourceNode, this, "select", new XPath(m_selectExpression), new XNodeSet(sourceNodes));
      }
      


      SerializationHandler rth = transformer.getSerializationHandler();
      
      StylesheetRoot sroot = transformer.getStylesheet();
      TemplateList tl = sroot.getTemplateListComposed();
      boolean quiet = transformer.getQuietConflictWarnings();
      

      DTM dtm = xctxt.getDTM(sourceNode);
      
      int argsFrame = -1;
      if (nParams > 0)
      {



        argsFrame = vars.link(nParams);
        vars.setStackFrame(thisframe);
        
        for (int i = 0; i < nParams; i++)
        {
          ElemWithParam ewp = m_paramElems[i];
          if (transformer.getDebug())
            transformer.getTraceManager().fireTraceEvent(ewp);
          XObject obj = ewp.getValue(transformer, sourceNode);
          if (transformer.getDebug()) {
            transformer.getTraceManager().fireTraceEndEvent(ewp);
          }
          vars.setLocalVariable(i, obj, argsFrame);
        }
        vars.setStackFrame(argsFrame);
      }
      
      xctxt.pushContextNodeList(sourceNodes);
      pushContextNodeListFlag = true;
      
      IntStack currentNodes = xctxt.getCurrentNodeStack();
      
      IntStack currentExpressionNodes = xctxt.getCurrentExpressionNodeStack();
      

      int child;
      
      while (-1 != (child = sourceNodes.nextNode()))
      {
        currentNodes.setTop(child);
        currentExpressionNodes.setTop(child);
        
        if (xctxt.getDTM(child) != dtm)
        {
          dtm = xctxt.getDTM(child);
        }
        
        int exNodeType = dtm.getExpandedTypeID(child);
        
        int nodeType = dtm.getNodeType(child);
        
        QName mode = transformer.getMode();
        
        ElemTemplate template = tl.getTemplateFast(xctxt, child, exNodeType, mode, -1, quiet, dtm);
        



        if (null == template)
        {
          switch (nodeType)
          {
          case 1: 
          case 11: 
            template = sroot.getDefaultRule();
            
            break;
          

          case 2: 
          case 3: 
          case 4: 
            transformer.pushPairCurrentMatched(sroot.getDefaultTextRule(), child);
            transformer.setCurrentElement(sroot.getDefaultTextRule());
            
            dtm.dispatchCharactersEvents(child, rth, false);
            transformer.popCurrentMatched();
            break;
          case 9: 
            template = sroot.getDefaultRootRule();
            break;
          


          }
          
        }
        else
        {
          transformer.setCurrentElement(template);
          

          transformer.pushPairCurrentMatched(template, child);
          if (check) {
            guard.checkForInfinateLoop();
          }
          int currentFrameBottom;
          if (m_frameSize > 0)
          {
            xctxt.pushRTFContext();
            int currentFrameBottom = vars.getStackFrame();
            vars.link(m_frameSize);
            

            if (m_inArgsSize > 0)
            {
              int paramIndex = 0;
              for (ElemTemplateElement elem = template.getFirstChildElem(); 
                  null != elem; elem = elem.getNextSiblingElem())
              {
                if (41 != elem.getXSLToken())
                  break;
                ElemParam ep = (ElemParam)elem;
                

                for (int i = 0; i < nParams; i++)
                {
                  ElemWithParam ewp = m_paramElems[i];
                  if (m_qnameID == m_qnameID)
                  {
                    XObject obj = vars.getLocalVariable(i, argsFrame);
                    vars.setLocalVariable(paramIndex, obj);
                    break;
                  }
                }
                if (i == nParams) {
                  vars.setLocalVariable(paramIndex, null);
                }
                

                paramIndex++;
              }
            }
          }
          else
          {
            currentFrameBottom = 0;
          }
          
          if (transformer.getDebug()) {
            transformer.getTraceManager().fireTraceEvent(template);
          }
          


          for (ElemTemplateElement t = m_firstChild; 
              t != null; t = m_nextSibling)
          {
            xctxt.setSAXLocator(t);
            try
            {
              transformer.pushElemTemplateElement(t);
              t.execute(transformer);
            }
            finally
            {
              transformer.popElemTemplateElement();
            }
          }
          
          if (transformer.getDebug()) {
            transformer.getTraceManager().fireTraceEndEvent(template);
          }
          if (m_frameSize > 0)
          {















            vars.unlink(currentFrameBottom);
            xctxt.popRTFContext();
          }
          
          transformer.popCurrentMatched();
        }
      }
    }
    catch (SAXException se)
    {
      transformer.getErrorListener().fatalError(new TransformerException(se));
    }
    finally
    {
      if (transformer.getDebug()) {
        transformer.getTraceManager().fireSelectedEndEvent(sourceNode, this, "select", new XPath(m_selectExpression), new XNodeSet(sourceNodes));
      }
      


      if (nParams > 0)
        vars.unlink(thisframe);
      xctxt.popSAXLocator();
      if (pushContextNodeListFlag) xctxt.popContextNodeList();
      transformer.popElemTemplateElement();
      xctxt.popCurrentExpressionNode();
      xctxt.popCurrentNode();
      sourceNodes.detach();
    }
  }
}
