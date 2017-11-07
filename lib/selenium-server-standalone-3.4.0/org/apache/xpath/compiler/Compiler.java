package org.apache.xpath.compiler;

import java.io.PrintStream;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.utils.ObjectVector;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.QName;
import org.apache.xml.utils.SAXSourceLocator;
import org.apache.xpath.Expression;
import org.apache.xpath.axes.UnionPathIterator;
import org.apache.xpath.axes.WalkerFactory;
import org.apache.xpath.functions.FuncExtFunction;
import org.apache.xpath.functions.FuncExtFunctionAvailable;
import org.apache.xpath.functions.Function;
import org.apache.xpath.functions.WrongNumberArgsException;
import org.apache.xpath.objects.XNumber;
import org.apache.xpath.objects.XString;
import org.apache.xpath.operations.And;
import org.apache.xpath.operations.Bool;
import org.apache.xpath.operations.Div;
import org.apache.xpath.operations.Equals;
import org.apache.xpath.operations.Gt;
import org.apache.xpath.operations.Gte;
import org.apache.xpath.operations.Lt;
import org.apache.xpath.operations.Lte;
import org.apache.xpath.operations.Minus;
import org.apache.xpath.operations.Mod;
import org.apache.xpath.operations.Mult;
import org.apache.xpath.operations.Neg;
import org.apache.xpath.operations.NotEquals;
import org.apache.xpath.operations.Number;
import org.apache.xpath.operations.Operation;
import org.apache.xpath.operations.Or;
import org.apache.xpath.operations.Plus;
import org.apache.xpath.operations.UnaryOperation;
import org.apache.xpath.operations.Variable;
import org.apache.xpath.patterns.FunctionPattern;
import org.apache.xpath.patterns.StepPattern;
import org.apache.xpath.patterns.UnionPattern;










































public class Compiler
  extends OpMap
{
  public Compiler(ErrorListener errorHandler, SourceLocator locator, FunctionTable fTable)
  {
    m_errorHandler = errorHandler;
    m_locator = locator;
    m_functionTable = fTable;
  }
  




  public Compiler()
  {
    m_errorHandler = null;
    m_locator = null;
  }
  








  public Expression compile(int opPos)
    throws TransformerException
  {
    int op = getOp(opPos);
    
    Expression expr = null;
    
    switch (op)
    {
    case 1: 
      expr = compile(opPos + 2); break;
    case 2: 
      expr = or(opPos); break;
    case 3: 
      expr = and(opPos); break;
    case 4: 
      expr = notequals(opPos); break;
    case 5: 
      expr = equals(opPos); break;
    case 6: 
      expr = lte(opPos); break;
    case 7: 
      expr = lt(opPos); break;
    case 8: 
      expr = gte(opPos); break;
    case 9: 
      expr = gt(opPos); break;
    case 10: 
      expr = plus(opPos); break;
    case 11: 
      expr = minus(opPos); break;
    case 12: 
      expr = mult(opPos); break;
    case 13: 
      expr = div(opPos); break;
    case 14: 
      expr = mod(opPos); break;
    

    case 16: 
      expr = neg(opPos); break;
    case 17: 
      expr = string(opPos); break;
    case 18: 
      expr = bool(opPos); break;
    case 19: 
      expr = number(opPos); break;
    case 20: 
      expr = union(opPos); break;
    case 21: 
      expr = literal(opPos); break;
    case 22: 
      expr = variable(opPos); break;
    case 23: 
      expr = group(opPos); break;
    case 27: 
      expr = numberlit(opPos); break;
    case 26: 
      expr = arg(opPos); break;
    case 24: 
      expr = compileExtension(opPos); break;
    case 25: 
      expr = compileFunction(opPos); break;
    case 28: 
      expr = locationPath(opPos); break;
    case 29: 
      expr = null; break;
    case 30: 
      expr = matchPattern(opPos + 2); break;
    case 31: 
      expr = locationPathPattern(opPos); break;
    case 15: 
      error("ER_UNKNOWN_OPCODE", new Object[] { "quo" });
      
      break;
    default: 
      error("ER_UNKNOWN_OPCODE", new Object[] { Integer.toString(getOp(opPos)) });
    }
    
    


    return expr;
  }
  











  private Expression compileOperation(Operation operation, int opPos)
    throws TransformerException
  {
    int leftPos = getFirstChildPos(opPos);
    int rightPos = getNextOpPos(leftPos);
    
    operation.setLeftRight(compile(leftPos), compile(rightPos));
    
    return operation;
  }
  











  private Expression compileUnary(UnaryOperation unary, int opPos)
    throws TransformerException
  {
    int rightPos = getFirstChildPos(opPos);
    
    unary.setRight(compile(rightPos));
    
    return unary;
  }
  








  protected Expression or(int opPos)
    throws TransformerException
  {
    return compileOperation(new Or(), opPos);
  }
  








  protected Expression and(int opPos)
    throws TransformerException
  {
    return compileOperation(new And(), opPos);
  }
  








  protected Expression notequals(int opPos)
    throws TransformerException
  {
    return compileOperation(new NotEquals(), opPos);
  }
  








  protected Expression equals(int opPos)
    throws TransformerException
  {
    return compileOperation(new Equals(), opPos);
  }
  








  protected Expression lte(int opPos)
    throws TransformerException
  {
    return compileOperation(new Lte(), opPos);
  }
  








  protected Expression lt(int opPos)
    throws TransformerException
  {
    return compileOperation(new Lt(), opPos);
  }
  








  protected Expression gte(int opPos)
    throws TransformerException
  {
    return compileOperation(new Gte(), opPos);
  }
  








  protected Expression gt(int opPos)
    throws TransformerException
  {
    return compileOperation(new Gt(), opPos);
  }
  








  protected Expression plus(int opPos)
    throws TransformerException
  {
    return compileOperation(new Plus(), opPos);
  }
  








  protected Expression minus(int opPos)
    throws TransformerException
  {
    return compileOperation(new Minus(), opPos);
  }
  








  protected Expression mult(int opPos)
    throws TransformerException
  {
    return compileOperation(new Mult(), opPos);
  }
  








  protected Expression div(int opPos)
    throws TransformerException
  {
    return compileOperation(new Div(), opPos);
  }
  








  protected Expression mod(int opPos)
    throws TransformerException
  {
    return compileOperation(new Mod(), opPos);
  }
  






















  protected Expression neg(int opPos)
    throws TransformerException
  {
    return compileUnary(new Neg(), opPos);
  }
  








  protected Expression string(int opPos)
    throws TransformerException
  {
    return compileUnary(new org.apache.xpath.operations.String(), opPos);
  }
  








  protected Expression bool(int opPos)
    throws TransformerException
  {
    return compileUnary(new Bool(), opPos);
  }
  








  protected Expression number(int opPos)
    throws TransformerException
  {
    return compileUnary(new Number(), opPos);
  }
  










  protected Expression literal(int opPos)
  {
    opPos = getFirstChildPos(opPos);
    
    return (XString)getTokenQueue().elementAt(getOp(opPos));
  }
  










  protected Expression numberlit(int opPos)
  {
    opPos = getFirstChildPos(opPos);
    
    return (XNumber)getTokenQueue().elementAt(getOp(opPos));
  }
  









  protected Expression variable(int opPos)
    throws TransformerException
  {
    Variable var = new Variable();
    
    opPos = getFirstChildPos(opPos);
    
    int nsPos = getOp(opPos);
    String namespace = -2 == nsPos ? null : (String)getTokenQueue().elementAt(nsPos);
    

    String localname = (String)getTokenQueue().elementAt(getOp(opPos + 1));
    
    QName qname = new QName(namespace, localname);
    
    var.setQName(qname);
    
    return var;
  }
  










  protected Expression group(int opPos)
    throws TransformerException
  {
    return compile(opPos + 2);
  }
  










  protected Expression arg(int opPos)
    throws TransformerException
  {
    return compile(opPos + 2);
  }
  









  protected Expression union(int opPos)
    throws TransformerException
  {
    locPathDepth += 1;
    try
    {
      return UnionPathIterator.createUnionIterator(this, opPos);
    }
    finally
    {
      locPathDepth -= 1;
    }
  }
  
  private int locPathDepth = -1;
  

  private static final boolean DEBUG = false;
  

  public int getLocationPathDepth()
  {
    return locPathDepth;
  }
  



  FunctionTable getFunctionTable()
  {
    return m_functionTable;
  }
  









  public Expression locationPath(int opPos)
    throws TransformerException
  {
    locPathDepth += 1;
    try
    {
      DTMIterator iter = WalkerFactory.newDTMIterator(this, opPos, locPathDepth == 0);
      return (Expression)iter;
    }
    finally
    {
      locPathDepth -= 1;
    }
  }
  








  public Expression predicate(int opPos)
    throws TransformerException
  {
    return compile(opPos + 2);
  }
  








  protected Expression matchPattern(int opPos)
    throws TransformerException
  {
    locPathDepth += 1;
    
    try
    {
      int nextOpPos = opPos;
      

      for (int i = 0; getOp(nextOpPos) == 31; i++)
      {
        nextOpPos = getNextOpPos(nextOpPos);
      }
      
      if (i == 1) {
        return compile(opPos);
      }
      UnionPattern up = new UnionPattern();
      StepPattern[] patterns = new StepPattern[i];
      
      for (i = 0; getOp(opPos) == 31; i++)
      {
        nextOpPos = getNextOpPos(opPos);
        patterns[i] = ((StepPattern)compile(opPos));
        opPos = nextOpPos;
      }
      
      up.setPatterns(patterns);
      
      return up;
    }
    finally
    {
      locPathDepth -= 1;
    }
  }
  










  public Expression locationPathPattern(int opPos)
    throws TransformerException
  {
    opPos = getFirstChildPos(opPos);
    
    return stepPattern(opPos, 0, null);
  }
  










  public int getWhatToShow(int opPos)
  {
    int axesType = getOp(opPos);
    int testType = getOp(opPos + 3);
    

    switch (testType)
    {
    case 1030: 
      return 128;
    
    case 1031: 
      return 12;
    case 1032: 
      return 64;
    
    case 1033: 
      switch (axesType)
      {
      case 49: 
        return 4096;
      case 39: 
      case 51: 
        return 2;
      case 38: 
      case 42: 
      case 48: 
        return -1;
      }
      if (getOp(0) == 30) {
        return 64253;
      }
      

      return -3;
    
    case 35: 
      return 1280;
    case 1034: 
      return 65536;
    case 34: 
      switch (axesType)
      {
      case 49: 
        return 4096;
      case 39: 
      case 51: 
        return 2;
      

      case 52: 
      case 53: 
        return 1;
      }
      
      
      return 1;
    }
    
    
    return -1;
  }
  

















  protected StepPattern stepPattern(int opPos, int stepCount, StepPattern ancestorPattern)
    throws TransformerException
  {
    int startOpPos = opPos;
    int stepType = getOp(opPos);
    
    if (-1 == stepType)
    {
      return null;
    }
    
    boolean addMagicSelf = true;
    
    int endStep = getNextOpPos(opPos);
    

    int argLen;
    

    StepPattern pattern;
    
    switch (stepType)
    {


    case 25: 
      addMagicSelf = false;
      argLen = getOp(opPos + 1);
      pattern = new FunctionPattern(compileFunction(opPos), 10, 3);
      break;
    

    case 50: 
      addMagicSelf = false;
      argLen = getArgLengthOfStep(opPos);
      opPos = getFirstChildPosOfStep(opPos);
      pattern = new StepPattern(1280, 10, 3);
      

      break;
    

    case 51: 
      argLen = getArgLengthOfStep(opPos);
      opPos = getFirstChildPosOfStep(opPos);
      pattern = new StepPattern(2, getStepNS(startOpPos), getStepLocalName(startOpPos), 10, 2);
      


      break;
    

    case 52: 
      argLen = getArgLengthOfStep(opPos);
      opPos = getFirstChildPosOfStep(opPos);
      int what = getWhatToShow(startOpPos);
      
      if (1280 == what)
        addMagicSelf = false;
      pattern = new StepPattern(getWhatToShow(startOpPos), getStepNS(startOpPos), getStepLocalName(startOpPos), 0, 3);
      


      break;
    

    case 53: 
      argLen = getArgLengthOfStep(opPos);
      opPos = getFirstChildPosOfStep(opPos);
      pattern = new StepPattern(getWhatToShow(startOpPos), getStepNS(startOpPos), getStepLocalName(startOpPos), 10, 3);
      


      break;
    default: 
      error("ER_UNKNOWN_MATCH_OPERATION", null);
      
      return null;
    }
    
    pattern.setPredicates(getCompiledPredicates(opPos + argLen));
    if (null != ancestorPattern)
    {























      pattern.setRelativePathPattern(ancestorPattern);
    }
    
    StepPattern relativePathPattern = stepPattern(endStep, stepCount + 1, pattern);
    

    return null != relativePathPattern ? relativePathPattern : pattern;
  }
  










  public Expression[] getCompiledPredicates(int opPos)
    throws TransformerException
  {
    int count = countPredicates(opPos);
    
    if (count > 0)
    {
      Expression[] predicates = new Expression[count];
      
      compilePredicates(opPos, predicates);
      
      return predicates;
    }
    
    return null;
  }
  









  public int countPredicates(int opPos)
    throws TransformerException
  {
    int count = 0;
    
    while (29 == getOp(opPos))
    {
      count++;
      
      opPos = getNextOpPos(opPos);
    }
    
    return count;
  }
  










  private void compilePredicates(int opPos, Expression[] predicates)
    throws TransformerException
  {
    for (int i = 0; 29 == getOp(opPos); i++)
    {
      predicates[i] = predicate(opPos);
      opPos = getNextOpPos(opPos);
    }
  }
  









  Expression compileFunction(int opPos)
    throws TransformerException
  {
    int endFunc = opPos + getOp(opPos + 1) - 1;
    
    opPos = getFirstChildPos(opPos);
    
    int funcID = getOp(opPos);
    
    opPos++;
    
    if (-1 != funcID)
    {
      Function func = m_functionTable.getFunction(funcID);
      





      if ((func instanceof FuncExtFunctionAvailable)) {
        ((FuncExtFunctionAvailable)func).setFunctionTable(m_functionTable);
      }
      func.postCompileStep(this);
      
      try
      {
        int i = 0;
        
        for (int p = opPos; p < endFunc; i++)
        {



          func.setArg(compile(p), i);p = getNextOpPos(p);
        }
        
        func.checkNumberArgs(i);
      }
      catch (WrongNumberArgsException wnae)
      {
        String name = m_functionTable.getFunctionName(funcID);
        
        m_errorHandler.fatalError(new TransformerException(XSLMessages.createXPATHMessage("ER_ONLY_ALLOWS", new Object[] { name, wnae.getMessage() }), m_locator));
      }
      



      return func;
    }
    

    error("ER_FUNCTION_TOKEN_NOT_FOUND", null);
    
    return null;
  }
  


  private static long s_nextMethodId = 0L;
  



  private synchronized long getNextMethodId()
  {
    if (s_nextMethodId == Long.MAX_VALUE) {
      s_nextMethodId = 0L;
    }
    return s_nextMethodId++;
  }
  










  private Expression compileExtension(int opPos)
    throws TransformerException
  {
    int endExtFunc = opPos + getOp(opPos + 1) - 1;
    
    opPos = getFirstChildPos(opPos);
    
    String ns = (String)getTokenQueue().elementAt(getOp(opPos));
    
    opPos++;
    
    String funcName = (String)getTokenQueue().elementAt(getOp(opPos));
    

    opPos++;
    




    Function extension = new FuncExtFunction(ns, funcName, String.valueOf(getNextMethodId()));
    
    try
    {
      int i = 0;
      
      while (opPos < endExtFunc)
      {
        int nextOpPos = getNextOpPos(opPos);
        
        extension.setArg(compile(opPos), i);
        
        opPos = nextOpPos;
        
        i++;
      }
    }
    catch (WrongNumberArgsException wnae) {}
    



    return extension;
  }
  












  public void warn(String msg, Object[] args)
    throws TransformerException
  {
    String fmsg = XSLMessages.createXPATHWarning(msg, args);
    
    if (null != m_errorHandler)
    {
      m_errorHandler.warning(new TransformerException(fmsg, m_locator));
    }
    else
    {
      System.out.println(fmsg + "; file " + m_locator.getSystemId() + "; line " + m_locator.getLineNumber() + "; column " + m_locator.getColumnNumber());
    }
  }
  













  public void assertion(boolean b, String msg)
  {
    if (!b)
    {
      String fMsg = XSLMessages.createXPATHMessage("ER_INCORRECT_PROGRAMMER_ASSERTION", new Object[] { msg });
      


      throw new RuntimeException(fMsg);
    }
  }
  













  public void error(String msg, Object[] args)
    throws TransformerException
  {
    String fmsg = XSLMessages.createXPATHMessage(msg, args);
    

    if (null != m_errorHandler)
    {
      m_errorHandler.fatalError(new TransformerException(fmsg, m_locator));



    }
    else
    {


      throw new TransformerException(fmsg, (SAXSourceLocator)m_locator);
    }
  }
  



  private PrefixResolver m_currentPrefixResolver = null;
  
  ErrorListener m_errorHandler;
  
  SourceLocator m_locator;
  private FunctionTable m_functionTable;
  
  public PrefixResolver getNamespaceContext()
  {
    return m_currentPrefixResolver;
  }
  





  public void setNamespaceContext(PrefixResolver pr)
  {
    m_currentPrefixResolver = pr;
  }
}
