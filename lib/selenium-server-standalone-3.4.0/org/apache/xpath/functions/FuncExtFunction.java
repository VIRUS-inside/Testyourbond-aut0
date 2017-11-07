package org.apache.xpath.functions;

import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xpath.Expression;
import org.apache.xpath.ExpressionNode;
import org.apache.xpath.ExpressionOwner;
import org.apache.xpath.ExtensionsProvider;
import org.apache.xpath.XPathContext;
import org.apache.xpath.XPathVisitor;
import org.apache.xpath.objects.XNull;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.res.XPATHMessages;


















































public class FuncExtFunction
  extends Function
{
  static final long serialVersionUID = 5196115554693708718L;
  String m_namespace;
  String m_extensionName;
  Object m_methodKey;
  Vector m_argVec = new Vector();
  












  public void fixupVariables(Vector vars, int globalsSize)
  {
    if (null != m_argVec)
    {
      int nArgs = m_argVec.size();
      
      for (int i = 0; i < nArgs; i++)
      {
        Expression arg = (Expression)m_argVec.elementAt(i);
        
        arg.fixupVariables(vars, globalsSize);
      }
    }
  }
  





  public String getNamespace()
  {
    return m_namespace;
  }
  





  public String getFunctionName()
  {
    return m_extensionName;
  }
  





  public Object getMethodKey()
  {
    return m_methodKey;
  }
  





  public Expression getArg(int n)
  {
    if ((n >= 0) && (n < m_argVec.size())) {
      return (Expression)m_argVec.elementAt(n);
    }
    return null;
  }
  





  public int getArgCount()
  {
    return m_argVec.size();
  }
  













  public FuncExtFunction(String namespace, String extensionName, Object methodKey)
  {
    m_namespace = namespace;
    m_extensionName = extensionName;
    m_methodKey = methodKey;
  }
  








  public XObject execute(XPathContext xctxt)
    throws TransformerException
  {
    if (xctxt.isSecureProcessing()) {
      throw new TransformerException(XPATHMessages.createXPATHMessage("ER_EXTENSION_FUNCTION_CANNOT_BE_INVOKED", new Object[] { toString() }));
    }
    



    Vector argVec = new Vector();
    int nArgs = m_argVec.size();
    
    for (int i = 0; i < nArgs; i++)
    {
      Expression arg = (Expression)m_argVec.elementAt(i);
      
      XObject xobj = arg.execute(xctxt);
      


      xobj.allowDetachToRelease(false);
      argVec.addElement(xobj);
    }
    
    ExtensionsProvider extProvider = (ExtensionsProvider)xctxt.getOwnerObject();
    Object val = extProvider.extFunction(this, argVec);
    XObject result;
    XObject result; if (null != val)
    {
      result = XObject.create(val, xctxt);
    }
    else
    {
      result = new XNull();
    }
    
    return result;
  }
  










  public void setArg(Expression arg, int argNum)
    throws WrongNumberArgsException
  {
    m_argVec.addElement(arg);
    arg.exprSetParent(this);
  }
  



  public void checkNumberArgs(int argNum)
    throws WrongNumberArgsException
  {}
  


  class ArgExtOwner
    implements ExpressionOwner
  {
    Expression m_exp;
    


    ArgExtOwner(Expression exp)
    {
      m_exp = exp;
    }
    



    public Expression getExpression()
    {
      return m_exp;
    }
    




    public void setExpression(Expression exp)
    {
      exp.exprSetParent(FuncExtFunction.this);
      m_exp = exp;
    }
  }
  




  public void callArgVisitors(XPathVisitor visitor)
  {
    for (int i = 0; i < m_argVec.size(); i++)
    {
      Expression exp = (Expression)m_argVec.elementAt(i);
      exp.callVisitors(new ArgExtOwner(exp), visitor);
    }
  }
  









  public void exprSetParent(ExpressionNode n)
  {
    super.exprSetParent(n);
    
    int nArgs = m_argVec.size();
    
    for (int i = 0; i < nArgs; i++)
    {
      Expression arg = (Expression)m_argVec.elementAt(i);
      
      arg.exprSetParent(n);
    }
  }
  





  protected void reportWrongNumberArgs()
    throws WrongNumberArgsException
  {
    String fMsg = XSLMessages.createXPATHMessage("ER_INCORRECT_PROGRAMMER_ASSERTION", new Object[] { "Programmer's assertion:  the method FunctionMultiArgs.reportWrongNumberArgs() should never be called." });
    


    throw new RuntimeException(fMsg);
  }
  



  public String toString()
  {
    if ((m_namespace != null) && (m_namespace.length() > 0)) {
      return "{" + m_namespace + "}" + m_extensionName;
    }
    return m_extensionName;
  }
}
