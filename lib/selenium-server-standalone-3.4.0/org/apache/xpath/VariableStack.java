package org.apache.xpath;

import javax.xml.transform.TransformerException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.ElemVariable;
import org.apache.xalan.templates.Stylesheet;
import org.apache.xalan.templates.StylesheetRoot;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.QName;
import org.apache.xpath.objects.XObject;

























public class VariableStack
  implements Cloneable
{
  public static final int CLEARLIMITATION = 1024;
  XObject[] _stackFrames;
  int _frameTop;
  private int _currentFrameBottom;
  int[] _links;
  int _linksTop;
  
  public VariableStack()
  {
    reset();
  }
  






  public VariableStack(int initStackSize)
  {
    reset(initStackSize, initStackSize * 2);
  }
  







  public synchronized Object clone()
    throws CloneNotSupportedException
  {
    VariableStack vs = (VariableStack)super.clone();
    

    _stackFrames = ((XObject[])_stackFrames.clone());
    _links = ((int[])_links.clone());
    
    return vs;
  }
  






































  public XObject elementAt(int i)
  {
    return _stackFrames[i];
  }
  





  public int size()
  {
    return _frameTop;
  }
  






  public void reset()
  {
    int linksSize = _links == null ? 4096 : _links.length;
    
    int varArraySize = _stackFrames == null ? 8192 : _stackFrames.length;
    
    reset(linksSize, varArraySize);
  }
  




  protected void reset(int linksSize, int varArraySize)
  {
    _frameTop = 0;
    _linksTop = 0;
    

    if (_links == null) {
      _links = new int[linksSize];
    }
    



    _links[(_linksTop++)] = 0;
    

    _stackFrames = new XObject[varArraySize];
  }
  





  public void setStackFrame(int sf)
  {
    _currentFrameBottom = sf;
  }
  







  public int getStackFrame()
  {
    return _currentFrameBottom;
  }
  
















  public int link(int size)
  {
    _currentFrameBottom = _frameTop;
    _frameTop += size;
    
    if (_frameTop >= _stackFrames.length)
    {
      XObject[] newsf = new XObject[_stackFrames.length + 4096 + size];
      
      System.arraycopy(_stackFrames, 0, newsf, 0, _stackFrames.length);
      
      _stackFrames = newsf;
    }
    
    if (_linksTop + 1 >= _links.length)
    {
      int[] newlinks = new int[_links.length + 2048];
      
      System.arraycopy(_links, 0, newlinks, 0, _links.length);
      
      _links = newlinks;
    }
    
    _links[(_linksTop++)] = _currentFrameBottom;
    
    return _currentFrameBottom;
  }
  




  public void unlink()
  {
    _frameTop = _links[(--_linksTop)];
    _currentFrameBottom = _links[(_linksTop - 1)];
  }
  






  public void unlink(int currentFrame)
  {
    _frameTop = _links[(--_linksTop)];
    _currentFrameBottom = currentFrame;
  }
  









  public void setLocalVariable(int index, XObject val)
  {
    _stackFrames[(index + _currentFrameBottom)] = val;
  }
  










  public void setLocalVariable(int index, XObject val, int stackFrame)
  {
    _stackFrames[(index + stackFrame)] = val;
  }
  















  public XObject getLocalVariable(XPathContext xctxt, int index)
    throws TransformerException
  {
    index += _currentFrameBottom;
    
    XObject val = _stackFrames[index];
    
    if (null == val) {
      throw new TransformerException(XSLMessages.createXPATHMessage("ER_VARIABLE_ACCESSED_BEFORE_BIND", null), xctxt.getSAXLocator());
    }
    


    if (val.getType() == 600) {
      return _stackFrames[index] =  = val.execute(xctxt);
    }
    return val;
  }
  













  public XObject getLocalVariable(int index, int frame)
    throws TransformerException
  {
    index += frame;
    
    XObject val = _stackFrames[index];
    
    return val;
  }
  















  public XObject getLocalVariable(XPathContext xctxt, int index, boolean destructiveOK)
    throws TransformerException
  {
    index += _currentFrameBottom;
    
    XObject val = _stackFrames[index];
    
    if (null == val) {
      throw new TransformerException(XSLMessages.createXPATHMessage("ER_VARIABLE_ACCESSED_BEFORE_BIND", null), xctxt.getSAXLocator());
    }
    


    if (val.getType() == 600) {
      return _stackFrames[index] =  = val.execute(xctxt);
    }
    return destructiveOK ? val : val.getFresh();
  }
  









  public boolean isLocalSet(int index)
    throws TransformerException
  {
    return _stackFrames[(index + _currentFrameBottom)] != null;
  }
  

  private static XObject[] m_nulls = new XObject['Ѐ'];
  










  public void clearLocalSlots(int start, int len)
  {
    start += _currentFrameBottom;
    
    System.arraycopy(m_nulls, 0, _stackFrames, start, len);
  }
  









  public void setGlobalVariable(int index, XObject val)
  {
    _stackFrames[index] = val;
  }
  















  public XObject getGlobalVariable(XPathContext xctxt, int index)
    throws TransformerException
  {
    XObject val = _stackFrames[index];
    

    if (val.getType() == 600) {
      return _stackFrames[index] =  = val.execute(xctxt);
    }
    return val;
  }
  















  public XObject getGlobalVariable(XPathContext xctxt, int index, boolean destructiveOK)
    throws TransformerException
  {
    XObject val = _stackFrames[index];
    

    if (val.getType() == 600) {
      return _stackFrames[index] =  = val.execute(xctxt);
    }
    return destructiveOK ? val : val.getFresh();
  }
  















  public XObject getVariableOrParam(XPathContext xctxt, QName qname)
    throws TransformerException
  {
    PrefixResolver prefixResolver = xctxt.getNamespaceContext();
    







    if ((prefixResolver instanceof ElemTemplateElement))
    {



      ElemTemplateElement prev = (ElemTemplateElement)prefixResolver;
      

      if (!(prev instanceof Stylesheet))
      {
        while (!(prev.getParentNode() instanceof Stylesheet))
        {
          ElemTemplateElement savedprev = prev;
          
          while (null != (prev = prev.getPreviousSiblingElem()))
          {
            if ((prev instanceof ElemVariable))
            {
              ElemVariable vvar = (ElemVariable)prev;
              
              if (vvar.getName().equals(qname))
                return getLocalVariable(xctxt, vvar.getIndex());
            }
          }
          prev = savedprev.getParentElem();
        }
      }
      
      ElemVariable vvar = prev.getStylesheetRoot().getVariableOrParamComposed(qname);
      if (null != vvar) {
        return getGlobalVariable(xctxt, vvar.getIndex());
      }
    }
    throw new TransformerException(XSLMessages.createXPATHMessage("ER_VAR_NOT_RESOLVABLE", new Object[] { qname.toString() }));
  }
}
