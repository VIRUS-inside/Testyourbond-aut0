package org.apache.xpath.functions;

import javax.xml.transform.TransformerException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xml.dtm.DTM;
import org.apache.xml.utils.XMLString;
import org.apache.xpath.Expression;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XString;


































public class FunctionDef1Arg
  extends FunctionOneArg
{
  static final long serialVersionUID = 2325189412814149264L;
  
  public FunctionDef1Arg() {}
  
  protected int getArg0AsNode(XPathContext xctxt)
    throws TransformerException
  {
    return null == m_arg0 ? xctxt.getCurrentNode() : m_arg0.asNode(xctxt);
  }
  





  public boolean Arg0IsNodesetExpr()
  {
    return null == m_arg0 ? true : m_arg0.isNodesetExpr();
  }
  













  protected XMLString getArg0AsString(XPathContext xctxt)
    throws TransformerException
  {
    if (null == m_arg0)
    {
      int currentNode = xctxt.getCurrentNode();
      if (-1 == currentNode) {
        return XString.EMPTYSTRING;
      }
      
      DTM dtm = xctxt.getDTM(currentNode);
      return dtm.getStringValue(currentNode);
    }
    


    return m_arg0.execute(xctxt).xstr();
  }
  














  protected double getArg0AsNumber(XPathContext xctxt)
    throws TransformerException
  {
    if (null == m_arg0)
    {
      int currentNode = xctxt.getCurrentNode();
      if (-1 == currentNode) {
        return 0.0D;
      }
      
      DTM dtm = xctxt.getDTM(currentNode);
      XMLString str = dtm.getStringValue(currentNode);
      return str.toDouble();
    }
    


    return m_arg0.execute(xctxt).num();
  }
  






  public void checkNumberArgs(int argNum)
    throws WrongNumberArgsException
  {
    if (argNum > 1) {
      reportWrongNumberArgs();
    }
  }
  



  protected void reportWrongNumberArgs()
    throws WrongNumberArgsException
  {
    throw new WrongNumberArgsException(XSLMessages.createXPATHMessage("ER_ZERO_OR_ONE", null));
  }
  






  public boolean canTraverseOutsideSubtree()
  {
    return null == m_arg0 ? false : super.canTraverseOutsideSubtree();
  }
}
