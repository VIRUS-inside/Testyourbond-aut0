package org.apache.xpath.functions;

import java.util.Vector;
import org.apache.xalan.res.XSLMessages;
import org.apache.xpath.Expression;
import org.apache.xpath.ExpressionOwner;
import org.apache.xpath.XPathVisitor;





























public class FunctionOneArg
  extends Function
  implements ExpressionOwner
{
  static final long serialVersionUID = -5180174180765609758L;
  Expression m_arg0;
  
  public FunctionOneArg() {}
  
  public Expression getArg0()
  {
    return m_arg0;
  }
  










  public void setArg(Expression arg, int argNum)
    throws WrongNumberArgsException
  {
    if (0 == argNum)
    {
      m_arg0 = arg;
      arg.exprSetParent(this);
    }
    else {
      reportWrongNumberArgs();
    }
  }
  






  public void checkNumberArgs(int argNum)
    throws WrongNumberArgsException
  {
    if (argNum != 1) {
      reportWrongNumberArgs();
    }
  }
  



  protected void reportWrongNumberArgs()
    throws WrongNumberArgsException
  {
    throw new WrongNumberArgsException(XSLMessages.createXPATHMessage("one", null));
  }
  






  public boolean canTraverseOutsideSubtree()
  {
    return m_arg0.canTraverseOutsideSubtree();
  }
  










  public void fixupVariables(Vector vars, int globalsSize)
  {
    if (null != m_arg0) {
      m_arg0.fixupVariables(vars, globalsSize);
    }
  }
  


  public void callArgVisitors(XPathVisitor visitor)
  {
    if (null != m_arg0) {
      m_arg0.callVisitors(this, visitor);
    }
  }
  



  public Expression getExpression()
  {
    return m_arg0;
  }
  



  public void setExpression(Expression exp)
  {
    exp.exprSetParent(this);
    m_arg0 = exp;
  }
  



  public boolean deepEquals(Expression expr)
  {
    if (!super.deepEquals(expr)) {
      return false;
    }
    if (null != m_arg0)
    {
      if (null == m_arg0) {
        return false;
      }
      if (!m_arg0.deepEquals(m_arg0)) {
        return false;
      }
    } else if (null != m_arg0) {
      return false;
    }
    return true;
  }
}
