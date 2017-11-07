package org.apache.xpath.compiler;

import java.io.PrintStream;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xml.utils.ObjectVector;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xpath.XPathProcessorException;
import org.apache.xpath.domapi.XPathStylesheetDOM3Exception;
import org.apache.xpath.objects.XNumber;
import org.apache.xpath.objects.XString;










































public class XPathParser
{
  public static final String CONTINUE_AFTER_FATAL_ERROR = "CONTINUE_AFTER_FATAL_ERROR";
  private OpMap m_ops;
  transient String m_token;
  transient char m_tokenChar = '\000';
  



  int m_queueMark = 0;
  
  protected static final int FILTER_MATCH_FAILED = 0;
  
  protected static final int FILTER_MATCH_PRIMARY = 1;
  
  protected static final int FILTER_MATCH_PREDICATES = 2;
  PrefixResolver m_namespaceContext;
  private ErrorListener m_errorListener;
  SourceLocator m_sourceLocator;
  private FunctionTable m_functionTable;
  
  public XPathParser(ErrorListener errorListener, SourceLocator sourceLocator)
  {
    m_errorListener = errorListener;
    m_sourceLocator = sourceLocator;
  }
  



















  public void initXPath(Compiler compiler, String expression, PrefixResolver namespaceContext)
    throws TransformerException
  {
    m_ops = compiler;
    m_namespaceContext = namespaceContext;
    m_functionTable = compiler.getFunctionTable();
    
    Lexer lexer = new Lexer(compiler, namespaceContext, this);
    
    lexer.tokenize(expression);
    
    m_ops.setOp(0, 1);
    m_ops.setOp(1, 2);
    









    try
    {
      nextToken();
      Expr();
      
      if (null != m_token)
      {
        String extraTokens = "";
        
        while (null != m_token)
        {
          extraTokens = extraTokens + "'" + m_token + "'";
          
          nextToken();
          
          if (null != m_token) {
            extraTokens = extraTokens + ", ";
          }
        }
        error("ER_EXTRA_ILLEGAL_TOKENS", new Object[] { extraTokens });
      }
      

    }
    catch (XPathProcessorException e)
    {
      if ("CONTINUE_AFTER_FATAL_ERROR".equals(e.getMessage()))
      {



        initXPath(compiler, "/..", namespaceContext);
      }
      else {
        throw e;
      }
    }
    compiler.shrink();
  }
  













  public void initMatchPattern(Compiler compiler, String expression, PrefixResolver namespaceContext)
    throws TransformerException
  {
    m_ops = compiler;
    m_namespaceContext = namespaceContext;
    m_functionTable = compiler.getFunctionTable();
    
    Lexer lexer = new Lexer(compiler, namespaceContext, this);
    
    lexer.tokenize(expression);
    
    m_ops.setOp(0, 30);
    m_ops.setOp(1, 2);
    
    nextToken();
    Pattern();
    
    if (null != m_token)
    {
      String extraTokens = "";
      
      while (null != m_token)
      {
        extraTokens = extraTokens + "'" + m_token + "'";
        
        nextToken();
        
        if (null != m_token) {
          extraTokens = extraTokens + ", ";
        }
      }
      error("ER_EXTRA_ILLEGAL_TOKENS", new Object[] { extraTokens });
    }
    


    m_ops.setOp(m_ops.getOp(1), -1);
    m_ops.setOp(1, m_ops.getOp(1) + 1);
    
    m_ops.shrink();
  }
  


















  public void setErrorHandler(ErrorListener handler)
  {
    m_errorListener = handler;
  }
  





  public ErrorListener getErrorListener()
  {
    return m_errorListener;
  }
  








  final boolean tokenIs(String s)
  {
    return s == null ? true : m_token != null ? m_token.equals(s) : false;
  }
  








  final boolean tokenIs(char c)
  {
    return m_tokenChar == c;
  }
  











  final boolean lookahead(char c, int n)
  {
    int pos = m_queueMark + n;
    boolean b;
    boolean b;
    if ((pos <= m_ops.getTokenQueueSize()) && (pos > 0) && (m_ops.getTokenQueueSize() != 0))
    {

      String tok = (String)m_ops.m_tokenQueue.elementAt(pos - 1);
      
      b = tok.charAt(0) == c;
    }
    else
    {
      b = false;
    }
    
    return b;
  }
  
















  private final boolean lookbehind(char c, int n)
  {
    int lookBehindPos = m_queueMark - (n + 1);
    boolean isToken;
    boolean isToken; if (lookBehindPos >= 0)
    {
      String lookbehind = (String)m_ops.m_tokenQueue.elementAt(lookBehindPos);
      boolean isToken;
      if (lookbehind.length() == 1)
      {
        char c0 = lookbehind == null ? '|' : lookbehind.charAt(0);
        
        isToken = c0 != '|';
      }
      else
      {
        isToken = false;
      }
    }
    else
    {
      isToken = false;
    }
    
    return isToken;
  }
  





  private final boolean lookbehindHasToken(int n)
  {
    boolean hasToken;
    



    boolean hasToken;
    



    if (m_queueMark - n > 0)
    {
      String lookbehind = (String)m_ops.m_tokenQueue.elementAt(m_queueMark - (n - 1));
      char c0 = lookbehind == null ? '|' : lookbehind.charAt(0);
      
      hasToken = c0 != '|';
    }
    else
    {
      hasToken = false;
    }
    
    return hasToken;
  }
  




  private final boolean lookahead(String s, int n)
  {
    boolean isToken;
    



    boolean isToken;
    



    if (m_queueMark + n <= m_ops.getTokenQueueSize())
    {
      String lookahead = (String)m_ops.m_tokenQueue.elementAt(m_queueMark + (n - 1));
      
      isToken = s == null ? true : lookahead != null ? lookahead.equals(s) : false;
    }
    else
    {
      isToken = null == s;
    }
    
    return isToken;
  }
  





  private final void nextToken()
  {
    if (m_queueMark < m_ops.getTokenQueueSize())
    {
      m_token = ((String)m_ops.m_tokenQueue.elementAt(m_queueMark++));
      m_tokenChar = m_token.charAt(0);
    }
    else
    {
      m_token = null;
      m_tokenChar = '\000';
    }
  }
  










  private final String getTokenRelative(int i)
  {
    int relative = m_queueMark + i;
    String tok;
    String tok; if ((relative > 0) && (relative < m_ops.getTokenQueueSize()))
    {
      tok = (String)m_ops.m_tokenQueue.elementAt(relative);
    }
    else
    {
      tok = null;
    }
    
    return tok;
  }
  





  private final void prevToken()
  {
    if (m_queueMark > 0)
    {
      m_queueMark -= 1;
      
      m_token = ((String)m_ops.m_tokenQueue.elementAt(m_queueMark));
      m_tokenChar = m_token.charAt(0);
    }
    else
    {
      m_token = null;
      m_tokenChar = '\000';
    }
  }
  









  private final void consumeExpected(String expected)
    throws TransformerException
  {
    if (tokenIs(expected))
    {
      nextToken();
    }
    else
    {
      error("ER_EXPECTED_BUT_FOUND", new Object[] { expected, m_token });
      




      throw new XPathProcessorException("CONTINUE_AFTER_FATAL_ERROR");
    }
  }
  









  private final void consumeExpected(char expected)
    throws TransformerException
  {
    if (tokenIs(expected))
    {
      nextToken();
    }
    else
    {
      error("ER_EXPECTED_BUT_FOUND", new Object[] { String.valueOf(expected), m_token });
      





      throw new XPathProcessorException("CONTINUE_AFTER_FATAL_ERROR");
    }
  }
  












  void warn(String msg, Object[] args)
    throws TransformerException
  {
    String fmsg = XSLMessages.createXPATHWarning(msg, args);
    ErrorListener ehandler = getErrorListener();
    
    if (null != ehandler)
    {

      ehandler.warning(new TransformerException(fmsg, m_sourceLocator));

    }
    else
    {
      System.err.println(fmsg);
    }
  }
  










  private void assertion(boolean b, String msg)
  {
    if (!b)
    {
      String fMsg = XSLMessages.createXPATHMessage("ER_INCORRECT_PROGRAMMER_ASSERTION", new Object[] { msg });
      


      throw new RuntimeException(fMsg);
    }
  }
  













  void error(String msg, Object[] args)
    throws TransformerException
  {
    String fmsg = XSLMessages.createXPATHMessage(msg, args);
    ErrorListener ehandler = getErrorListener();
    
    TransformerException te = new TransformerException(fmsg, m_sourceLocator);
    if (null != ehandler)
    {

      ehandler.fatalError(te);

    }
    else
    {
      throw te;
    }
  }
  


























  void errorForDOM3(String msg, Object[] args)
    throws TransformerException
  {
    String fmsg = XSLMessages.createXPATHMessage(msg, args);
    ErrorListener ehandler = getErrorListener();
    
    TransformerException te = new XPathStylesheetDOM3Exception(fmsg, m_sourceLocator);
    if (null != ehandler)
    {

      ehandler.fatalError(te);

    }
    else
    {
      throw te;
    }
  }
  







  protected String dumpRemainingTokenQueue()
  {
    int q = m_queueMark;
    String returnMsg;
    String returnMsg;
    if (q < m_ops.getTokenQueueSize())
    {
      String msg = "\n Remaining tokens: (";
      
      while (q < m_ops.getTokenQueueSize())
      {
        String t = (String)m_ops.m_tokenQueue.elementAt(q++);
        
        msg = msg + " '" + t + "'";
      }
      
      returnMsg = msg + ")";
    }
    else
    {
      returnMsg = "";
    }
    
    return returnMsg;
  }
  








  final int getFunctionToken(String key)
  {
    int tok;
    






    try
    {
      Object id = Keywords.lookupNodeTest(key);
      if (null == id) id = m_functionTable.getFunctionID(key);
      tok = ((Integer)id).intValue();
    }
    catch (NullPointerException npe)
    {
      tok = -1;
    }
    catch (ClassCastException cce)
    {
      tok = -1;
    }
    
    return tok;
  }
  










  void insertOp(int pos, int length, int op)
  {
    int totalLen = m_ops.getOp(1);
    
    for (int i = totalLen - 1; i >= pos; i--)
    {
      m_ops.setOp(i + length, m_ops.getOp(i));
    }
    
    m_ops.setOp(pos, op);
    m_ops.setOp(1, totalLen + length);
  }
  









  void appendOp(int length, int op)
  {
    int totalLen = m_ops.getOp(1);
    
    m_ops.setOp(totalLen, op);
    m_ops.setOp(totalLen + 1, length);
    m_ops.setOp(1, totalLen + length);
  }
  









  protected void Expr()
    throws TransformerException
  {
    OrExpr();
  }
  









  protected void OrExpr()
    throws TransformerException
  {
    int opPos = m_ops.getOp(1);
    
    AndExpr();
    
    if ((null != m_token) && (tokenIs("or")))
    {
      nextToken();
      insertOp(opPos, 2, 2);
      OrExpr();
      
      m_ops.setOp(opPos + 1, m_ops.getOp(1) - opPos);
    }
  }
  










  protected void AndExpr()
    throws TransformerException
  {
    int opPos = m_ops.getOp(1);
    
    EqualityExpr(-1);
    
    if ((null != m_token) && (tokenIs("and")))
    {
      nextToken();
      insertOp(opPos, 2, 3);
      AndExpr();
      
      m_ops.setOp(opPos + 1, m_ops.getOp(1) - opPos);
    }
  }
  
















  protected int EqualityExpr(int addPos)
    throws TransformerException
  {
    int opPos = m_ops.getOp(1);
    
    if (-1 == addPos) {
      addPos = opPos;
    }
    RelationalExpr(-1);
    
    if (null != m_token)
    {
      if ((tokenIs('!')) && (lookahead('=', 1)))
      {
        nextToken();
        nextToken();
        insertOp(addPos, 2, 4);
        
        int opPlusLeftHandLen = m_ops.getOp(1) - addPos;
        
        addPos = EqualityExpr(addPos);
        m_ops.setOp(addPos + 1, m_ops.getOp(addPos + opPlusLeftHandLen + 1) + opPlusLeftHandLen);
        
        addPos += 2;
      }
      else if (tokenIs('='))
      {
        nextToken();
        insertOp(addPos, 2, 5);
        
        int opPlusLeftHandLen = m_ops.getOp(1) - addPos;
        
        addPos = EqualityExpr(addPos);
        m_ops.setOp(addPos + 1, m_ops.getOp(addPos + opPlusLeftHandLen + 1) + opPlusLeftHandLen);
        
        addPos += 2;
      }
    }
    
    return addPos;
  }
  


















  protected int RelationalExpr(int addPos)
    throws TransformerException
  {
    int opPos = m_ops.getOp(1);
    
    if (-1 == addPos) {
      addPos = opPos;
    }
    AdditiveExpr(-1);
    
    if (null != m_token)
    {
      if (tokenIs('<'))
      {
        nextToken();
        
        if (tokenIs('='))
        {
          nextToken();
          insertOp(addPos, 2, 6);
        }
        else
        {
          insertOp(addPos, 2, 7);
        }
        
        int opPlusLeftHandLen = m_ops.getOp(1) - addPos;
        
        addPos = RelationalExpr(addPos);
        m_ops.setOp(addPos + 1, m_ops.getOp(addPos + opPlusLeftHandLen + 1) + opPlusLeftHandLen);
        
        addPos += 2;
      }
      else if (tokenIs('>'))
      {
        nextToken();
        
        if (tokenIs('='))
        {
          nextToken();
          insertOp(addPos, 2, 8);
        }
        else
        {
          insertOp(addPos, 2, 9);
        }
        
        int opPlusLeftHandLen = m_ops.getOp(1) - addPos;
        
        addPos = RelationalExpr(addPos);
        m_ops.setOp(addPos + 1, m_ops.getOp(addPos + opPlusLeftHandLen + 1) + opPlusLeftHandLen);
        
        addPos += 2;
      }
    }
    
    return addPos;
  }
  
















  protected int AdditiveExpr(int addPos)
    throws TransformerException
  {
    int opPos = m_ops.getOp(1);
    
    if (-1 == addPos) {
      addPos = opPos;
    }
    MultiplicativeExpr(-1);
    
    if (null != m_token)
    {
      if (tokenIs('+'))
      {
        nextToken();
        insertOp(addPos, 2, 10);
        
        int opPlusLeftHandLen = m_ops.getOp(1) - addPos;
        
        addPos = AdditiveExpr(addPos);
        m_ops.setOp(addPos + 1, m_ops.getOp(addPos + opPlusLeftHandLen + 1) + opPlusLeftHandLen);
        
        addPos += 2;
      }
      else if (tokenIs('-'))
      {
        nextToken();
        insertOp(addPos, 2, 11);
        
        int opPlusLeftHandLen = m_ops.getOp(1) - addPos;
        
        addPos = AdditiveExpr(addPos);
        m_ops.setOp(addPos + 1, m_ops.getOp(addPos + opPlusLeftHandLen + 1) + opPlusLeftHandLen);
        
        addPos += 2;
      }
    }
    
    return addPos;
  }
  

















  protected int MultiplicativeExpr(int addPos)
    throws TransformerException
  {
    int opPos = m_ops.getOp(1);
    
    if (-1 == addPos) {
      addPos = opPos;
    }
    UnaryExpr();
    
    if (null != m_token)
    {
      if (tokenIs('*'))
      {
        nextToken();
        insertOp(addPos, 2, 12);
        
        int opPlusLeftHandLen = m_ops.getOp(1) - addPos;
        
        addPos = MultiplicativeExpr(addPos);
        m_ops.setOp(addPos + 1, m_ops.getOp(addPos + opPlusLeftHandLen + 1) + opPlusLeftHandLen);
        
        addPos += 2;
      }
      else if (tokenIs("div"))
      {
        nextToken();
        insertOp(addPos, 2, 13);
        
        int opPlusLeftHandLen = m_ops.getOp(1) - addPos;
        
        addPos = MultiplicativeExpr(addPos);
        m_ops.setOp(addPos + 1, m_ops.getOp(addPos + opPlusLeftHandLen + 1) + opPlusLeftHandLen);
        
        addPos += 2;
      }
      else if (tokenIs("mod"))
      {
        nextToken();
        insertOp(addPos, 2, 14);
        
        int opPlusLeftHandLen = m_ops.getOp(1) - addPos;
        
        addPos = MultiplicativeExpr(addPos);
        m_ops.setOp(addPos + 1, m_ops.getOp(addPos + opPlusLeftHandLen + 1) + opPlusLeftHandLen);
        
        addPos += 2;
      }
      else if (tokenIs("quo"))
      {
        nextToken();
        insertOp(addPos, 2, 15);
        
        int opPlusLeftHandLen = m_ops.getOp(1) - addPos;
        
        addPos = MultiplicativeExpr(addPos);
        m_ops.setOp(addPos + 1, m_ops.getOp(addPos + opPlusLeftHandLen + 1) + opPlusLeftHandLen);
        
        addPos += 2;
      }
    }
    
    return addPos;
  }
  








  protected void UnaryExpr()
    throws TransformerException
  {
    int opPos = m_ops.getOp(1);
    boolean isNeg = false;
    
    if (m_tokenChar == '-')
    {
      nextToken();
      appendOp(2, 16);
      
      isNeg = true;
    }
    
    UnionExpr();
    
    if (isNeg) {
      m_ops.setOp(opPos + 1, m_ops.getOp(1) - opPos);
    }
  }
  







  protected void StringExpr()
    throws TransformerException
  {
    int opPos = m_ops.getOp(1);
    
    appendOp(2, 17);
    Expr();
    
    m_ops.setOp(opPos + 1, m_ops.getOp(1) - opPos);
  }
  









  protected void BooleanExpr()
    throws TransformerException
  {
    int opPos = m_ops.getOp(1);
    
    appendOp(2, 18);
    Expr();
    
    int opLen = m_ops.getOp(1) - opPos;
    
    if (opLen == 2)
    {
      error("ER_BOOLEAN_ARG_NO_LONGER_OPTIONAL", null);
    }
    
    m_ops.setOp(opPos + 1, opLen);
  }
  








  protected void NumberExpr()
    throws TransformerException
  {
    int opPos = m_ops.getOp(1);
    
    appendOp(2, 19);
    Expr();
    
    m_ops.setOp(opPos + 1, m_ops.getOp(1) - opPos);
  }
  














  protected void UnionExpr()
    throws TransformerException
  {
    int opPos = m_ops.getOp(1);
    boolean continueOrLoop = true;
    boolean foundUnion = false;
    
    do
    {
      PathExpr();
      
      if (!tokenIs('|'))
        break;
      if (false == foundUnion)
      {
        foundUnion = true;
        
        insertOp(opPos, 2, 20);
      }
      
      nextToken();







    }
    while (continueOrLoop);
    
    m_ops.setOp(opPos + 1, m_ops.getOp(1) - opPos);
  }
  












  protected void PathExpr()
    throws TransformerException
  {
    int opPos = m_ops.getOp(1);
    
    int filterExprMatch = FilterExpr();
    
    if (filterExprMatch != 0)
    {


      boolean locationPathStarted = filterExprMatch == 2;
      
      if (tokenIs('/'))
      {
        nextToken();
        
        if (!locationPathStarted)
        {

          insertOp(opPos, 2, 28);
          
          locationPathStarted = true;
        }
        
        if (!RelativeLocationPath())
        {

          error("ER_EXPECTED_REL_LOC_PATH", null);
        }
      }
      


      if (locationPathStarted)
      {
        m_ops.setOp(m_ops.getOp(1), -1);
        m_ops.setOp(1, m_ops.getOp(1) + 1);
        m_ops.setOp(opPos + 1, m_ops.getOp(1) - opPos);
      }
      
    }
    else
    {
      LocationPath();
    }
  }
  

















  protected int FilterExpr()
    throws TransformerException
  {
    int opPos = m_ops.getOp(1);
    
    int filterMatch;
    int filterMatch;
    if (PrimaryExpr()) {
      int filterMatch;
      if (tokenIs('['))
      {


        insertOp(opPos, 2, 28);
        
        while (tokenIs('['))
        {
          Predicate();
        }
        
        filterMatch = 2;
      }
      else
      {
        filterMatch = 1;
      }
    }
    else
    {
      filterMatch = 0;
    }
    
    return filterMatch;
  }
  






















  protected boolean PrimaryExpr()
    throws TransformerException
  {
    int opPos = m_ops.getOp(1);
    boolean matchFound;
    boolean matchFound; if ((m_tokenChar == '\'') || (m_tokenChar == '"'))
    {
      appendOp(2, 21);
      Literal();
      
      m_ops.setOp(opPos + 1, m_ops.getOp(1) - opPos);
      

      matchFound = true;
    } else { boolean matchFound;
      if (m_tokenChar == '$')
      {
        nextToken();
        appendOp(2, 22);
        QName();
        
        m_ops.setOp(opPos + 1, m_ops.getOp(1) - opPos);
        

        matchFound = true;
      } else { boolean matchFound;
        if (m_tokenChar == '(')
        {
          nextToken();
          appendOp(2, 23);
          Expr();
          consumeExpected(')');
          
          m_ops.setOp(opPos + 1, m_ops.getOp(1) - opPos);
          

          matchFound = true;
        } else { boolean matchFound;
          if ((null != m_token) && ((('.' == m_tokenChar) && (m_token.length() > 1) && (Character.isDigit(m_token.charAt(1)))) || (Character.isDigit(m_tokenChar))))
          {

            appendOp(2, 27);
            Number();
            
            m_ops.setOp(opPos + 1, m_ops.getOp(1) - opPos);
            

            matchFound = true;
          } else { boolean matchFound;
            if ((lookahead('(', 1)) || ((lookahead(':', 1)) && (lookahead('(', 3))))
            {
              matchFound = FunctionCall();
            }
            else
            {
              matchFound = false; }
          }
        } } }
    return matchFound;
  }
  







  protected void Argument()
    throws TransformerException
  {
    int opPos = m_ops.getOp(1);
    
    appendOp(2, 26);
    Expr();
    
    m_ops.setOp(opPos + 1, m_ops.getOp(1) - opPos);
  }
  









  protected boolean FunctionCall()
    throws TransformerException
  {
    int opPos = m_ops.getOp(1);
    
    if (lookahead(':', 1))
    {
      appendOp(4, 24);
      
      m_ops.setOp(opPos + 1 + 1, m_queueMark - 1);
      
      nextToken();
      consumeExpected(':');
      
      m_ops.setOp(opPos + 1 + 2, m_queueMark - 1);
      
      nextToken();
    }
    else
    {
      int funcTok = getFunctionToken(m_token);
      
      if (-1 == funcTok)
      {
        error("ER_COULDNOT_FIND_FUNCTION", new Object[] { m_token });
      }
      

      switch (funcTok)
      {

      case 1030: 
      case 1031: 
      case 1032: 
      case 1033: 
        return false; }
      
      appendOp(3, 25);
      
      m_ops.setOp(opPos + 1 + 1, funcTok);
      

      nextToken();
    }
    
    consumeExpected('(');
    
    while ((!tokenIs(')')) && (m_token != null))
    {
      if (tokenIs(','))
      {
        error("ER_FOUND_COMMA_BUT_NO_PRECEDING_ARG", null);
      }
      
      Argument();
      
      if (!tokenIs(')'))
      {
        consumeExpected(',');
        
        if (tokenIs(')'))
        {
          error("ER_FOUND_COMMA_BUT_NO_FOLLOWING_ARG", null);
        }
      }
    }
    

    consumeExpected(')');
    

    m_ops.setOp(m_ops.getOp(1), -1);
    m_ops.setOp(1, m_ops.getOp(1) + 1);
    m_ops.setOp(opPos + 1, m_ops.getOp(1) - opPos);
    

    return true;
  }
  










  protected void LocationPath()
    throws TransformerException
  {
    int opPos = m_ops.getOp(1);
    

    appendOp(2, 28);
    
    boolean seenSlash = tokenIs('/');
    
    if (seenSlash)
    {
      appendOp(4, 50);
      

      m_ops.setOp(m_ops.getOp(1) - 2, 4);
      m_ops.setOp(m_ops.getOp(1) - 1, 35);
      
      nextToken();
    } else if (m_token == null) {
      error("ER_EXPECTED_LOC_PATH_AT_END_EXPR", null);
    }
    
    if (m_token != null)
    {
      if ((!RelativeLocationPath()) && (!seenSlash))
      {


        error("ER_EXPECTED_LOC_PATH", new Object[] { m_token });
      }
    }
    


    m_ops.setOp(m_ops.getOp(1), -1);
    m_ops.setOp(1, m_ops.getOp(1) + 1);
    m_ops.setOp(opPos + 1, m_ops.getOp(1) - opPos);
  }
  











  protected boolean RelativeLocationPath()
    throws TransformerException
  {
    if (!Step())
    {
      return false;
    }
    
    while (tokenIs('/'))
    {
      nextToken();
      
      if (!Step())
      {


        error("ER_EXPECTED_LOC_STEP", null);
      }
    }
    
    return true;
  }
  








  protected boolean Step()
    throws TransformerException
  {
    int opPos = m_ops.getOp(1);
    
    boolean doubleSlash = tokenIs('/');
    



    if (doubleSlash)
    {
      nextToken();
      
      appendOp(2, 42);
      






      m_ops.setOp(1, m_ops.getOp(1) + 1);
      m_ops.setOp(m_ops.getOp(1), 1033);
      m_ops.setOp(1, m_ops.getOp(1) + 1);
      

      m_ops.setOp(opPos + 1 + 1, m_ops.getOp(1) - opPos);
      


      m_ops.setOp(opPos + 1, m_ops.getOp(1) - opPos);
      

      opPos = m_ops.getOp(1);
    }
    
    if (tokenIs("."))
    {
      nextToken();
      
      if (tokenIs('['))
      {
        error("ER_PREDICATE_ILLEGAL_SYNTAX", null);
      }
      
      appendOp(4, 48);
      

      m_ops.setOp(m_ops.getOp(1) - 2, 4);
      m_ops.setOp(m_ops.getOp(1) - 1, 1033);
    }
    else if (tokenIs(".."))
    {
      nextToken();
      appendOp(4, 45);
      

      m_ops.setOp(m_ops.getOp(1) - 2, 4);
      m_ops.setOp(m_ops.getOp(1) - 1, 1033);




    }
    else if ((tokenIs('*')) || (tokenIs('@')) || (tokenIs('_')) || ((m_token != null) && (Character.isLetter(m_token.charAt(0)))))
    {

      Basis();
      
      while (tokenIs('['))
      {
        Predicate();
      }
      

      m_ops.setOp(opPos + 1, m_ops.getOp(1) - opPos);

    }
    else
    {

      if (doubleSlash)
      {

        error("ER_EXPECTED_LOC_STEP", null);
      }
      
      return false;
    }
    
    return true;
  }
  







  protected void Basis()
    throws TransformerException
  {
    int opPos = m_ops.getOp(1);
    
    int axesType;
    
    if (lookahead("::", 1))
    {
      int axesType = AxisName();
      
      nextToken();
      nextToken();
    }
    else if (tokenIs('@'))
    {
      int axesType = 39;
      
      appendOp(2, axesType);
      nextToken();
    }
    else
    {
      axesType = 40;
      
      appendOp(2, axesType);
    }
    

    m_ops.setOp(1, m_ops.getOp(1) + 1);
    
    NodeTest(axesType);
    

    m_ops.setOp(opPos + 1 + 1, m_ops.getOp(1) - opPos);
  }
  










  protected int AxisName()
    throws TransformerException
  {
    Object val = Keywords.getAxisName(m_token);
    
    if (null == val)
    {
      error("ER_ILLEGAL_AXIS_NAME", new Object[] { m_token });
    }
    

    int axesType = ((Integer)val).intValue();
    
    appendOp(2, axesType);
    
    return axesType;
  }
  










  protected void NodeTest(int axesType)
    throws TransformerException
  {
    if (lookahead('(', 1))
    {
      Object nodeTestOp = Keywords.getNodeType(m_token);
      
      if (null == nodeTestOp)
      {
        error("ER_UNKNOWN_NODETYPE", new Object[] { m_token });

      }
      else
      {
        nextToken();
        
        int nt = ((Integer)nodeTestOp).intValue();
        
        m_ops.setOp(m_ops.getOp(1), nt);
        m_ops.setOp(1, m_ops.getOp(1) + 1);
        
        consumeExpected('(');
        
        if (1032 == nt)
        {
          if (!tokenIs(')'))
          {
            Literal();
          }
        }
        
        consumeExpected(')');
      }
      

    }
    else
    {
      m_ops.setOp(m_ops.getOp(1), 34);
      m_ops.setOp(1, m_ops.getOp(1) + 1);
      
      if (lookahead(':', 1))
      {
        if (tokenIs('*'))
        {
          m_ops.setOp(m_ops.getOp(1), -3);
        }
        else
        {
          m_ops.setOp(m_ops.getOp(1), m_queueMark - 1);
          


          if ((!Character.isLetter(m_tokenChar)) && (!tokenIs('_')))
          {

            error("ER_EXPECTED_NODE_TEST", null);
          }
        }
        
        nextToken();
        consumeExpected(':');
      }
      else
      {
        m_ops.setOp(m_ops.getOp(1), -2);
      }
      
      m_ops.setOp(1, m_ops.getOp(1) + 1);
      
      if (tokenIs('*'))
      {
        m_ops.setOp(m_ops.getOp(1), -3);
      }
      else
      {
        m_ops.setOp(m_ops.getOp(1), m_queueMark - 1);
        


        if ((!Character.isLetter(m_tokenChar)) && (!tokenIs('_')))
        {

          error("ER_EXPECTED_NODE_TEST", null);
        }
      }
      
      m_ops.setOp(1, m_ops.getOp(1) + 1);
      
      nextToken();
    }
  }
  







  protected void Predicate()
    throws TransformerException
  {
    if (tokenIs('['))
    {
      nextToken();
      PredicateExpr();
      consumeExpected(']');
    }
  }
  







  protected void PredicateExpr()
    throws TransformerException
  {
    int opPos = m_ops.getOp(1);
    
    appendOp(2, 29);
    Expr();
    

    m_ops.setOp(m_ops.getOp(1), -1);
    m_ops.setOp(1, m_ops.getOp(1) + 1);
    m_ops.setOp(opPos + 1, m_ops.getOp(1) - opPos);
  }
  








  protected void QName()
    throws TransformerException
  {
    if (lookahead(':', 1))
    {
      m_ops.setOp(m_ops.getOp(1), m_queueMark - 1);
      m_ops.setOp(1, m_ops.getOp(1) + 1);
      
      nextToken();
      consumeExpected(':');
    }
    else
    {
      m_ops.setOp(m_ops.getOp(1), -2);
      m_ops.setOp(1, m_ops.getOp(1) + 1);
    }
    

    m_ops.setOp(m_ops.getOp(1), m_queueMark - 1);
    m_ops.setOp(1, m_ops.getOp(1) + 1);
    
    nextToken();
  }
  





  protected void NCName()
  {
    m_ops.setOp(m_ops.getOp(1), m_queueMark - 1);
    m_ops.setOp(1, m_ops.getOp(1) + 1);
    
    nextToken();
  }
  










  protected void Literal()
    throws TransformerException
  {
    int last = m_token.length() - 1;
    char c0 = m_tokenChar;
    char cX = m_token.charAt(last);
    
    if (((c0 == '"') && (cX == '"')) || ((c0 == '\'') && (cX == '\'')))
    {



      int tokenQueuePos = m_queueMark - 1;
      
      m_ops.m_tokenQueue.setElementAt(null, tokenQueuePos);
      
      Object obj = new XString(m_token.substring(1, last));
      
      m_ops.m_tokenQueue.setElementAt(obj, tokenQueuePos);
      

      m_ops.setOp(m_ops.getOp(1), tokenQueuePos);
      m_ops.setOp(1, m_ops.getOp(1) + 1);
      
      nextToken();
    }
    else
    {
      error("ER_PATTERN_LITERAL_NEEDS_BE_QUOTED", new Object[] { m_token });
    }
  }
  








  protected void Number()
    throws TransformerException
  {
    if (null != m_token)
    {
      double num;
      




      try
      {
        if ((m_token.indexOf('e') > -1) || (m_token.indexOf('E') > -1))
          throw new NumberFormatException();
        num = Double.valueOf(m_token).doubleValue();
      }
      catch (NumberFormatException nfe)
      {
        num = 0.0D;
        
        error("ER_COULDNOT_BE_FORMATTED_TO_NUMBER", new Object[] { m_token });
      }
      

      m_ops.m_tokenQueue.setElementAt(new XNumber(num), m_queueMark - 1);
      m_ops.setOp(m_ops.getOp(1), m_queueMark - 1);
      m_ops.setOp(1, m_ops.getOp(1) + 1);
      
      nextToken();
    }
  }
  










  protected void Pattern()
    throws TransformerException
  {
    for (;;)
    {
      LocationPathPattern();
      
      if (!tokenIs('|'))
        break;
      nextToken();
    }
  }
  















  protected void LocationPathPattern()
    throws TransformerException
  {
    int opPos = m_ops.getOp(1);
    
    int RELATIVE_PATH_NOT_PERMITTED = 0;
    int RELATIVE_PATH_PERMITTED = 1;
    int RELATIVE_PATH_REQUIRED = 2;
    
    int relativePathStatus = 0;
    
    appendOp(2, 31);
    
    if ((lookahead('(', 1)) && ((tokenIs("id")) || (tokenIs("key"))))
    {


      IdKeyPattern();
      
      if (tokenIs('/'))
      {
        nextToken();
        
        if (tokenIs('/'))
        {
          appendOp(4, 52);
          
          nextToken();
        }
        else
        {
          appendOp(4, 53);
        }
        

        m_ops.setOp(m_ops.getOp(1) - 2, 4);
        m_ops.setOp(m_ops.getOp(1) - 1, 1034);
        
        relativePathStatus = 2;
      }
    }
    else if (tokenIs('/'))
    {
      if (lookahead('/', 1))
      {
        appendOp(4, 52);
        




        nextToken();
        
        relativePathStatus = 2;
      }
      else
      {
        appendOp(4, 50);
        
        relativePathStatus = 1;
      }
      


      m_ops.setOp(m_ops.getOp(1) - 2, 4);
      m_ops.setOp(m_ops.getOp(1) - 1, 35);
      
      nextToken();
    }
    else
    {
      relativePathStatus = 2;
    }
    
    if (relativePathStatus != 0)
    {
      if ((!tokenIs('|')) && (null != m_token))
      {
        RelativePathPattern();
      }
      else if (relativePathStatus == 2)
      {

        error("ER_EXPECTED_REL_PATH_PATTERN", null);
      }
    }
    

    m_ops.setOp(m_ops.getOp(1), -1);
    m_ops.setOp(1, m_ops.getOp(1) + 1);
    m_ops.setOp(opPos + 1, m_ops.getOp(1) - opPos);
  }
  









  protected void IdKeyPattern()
    throws TransformerException
  {
    FunctionCall();
  }
  











  protected void RelativePathPattern()
    throws TransformerException
  {
    boolean trailingSlashConsumed = StepPattern(false);
    
    while (tokenIs('/'))
    {
      nextToken();
      



      trailingSlashConsumed = StepPattern(!trailingSlashConsumed);
    }
  }
  











  protected boolean StepPattern(boolean isLeadingSlashPermitted)
    throws TransformerException
  {
    return AbbreviatedNodeTestStep(isLeadingSlashPermitted);
  }
  












  protected boolean AbbreviatedNodeTestStep(boolean isLeadingSlashPermitted)
    throws TransformerException
  {
    int opPos = m_ops.getOp(1);
    


    int matchTypePos = -1;
    int axesType;
    if (tokenIs('@'))
    {
      int axesType = 51;
      
      appendOp(2, axesType);
      nextToken();
    }
    else if (lookahead("::", 1))
    {
      if (tokenIs("attribute"))
      {
        int axesType = 51;
        
        appendOp(2, axesType);
      }
      else if (tokenIs("child"))
      {
        matchTypePos = m_ops.getOp(1);
        int axesType = 53;
        
        appendOp(2, axesType);
      }
      else
      {
        int axesType = -1;
        
        error("ER_AXES_NOT_ALLOWED", new Object[] { m_token });
      }
      

      nextToken();
      nextToken();
    }
    else if (tokenIs('/'))
    {
      if (!isLeadingSlashPermitted)
      {

        error("ER_EXPECTED_STEP_PATTERN", null);
      }
      int axesType = 52;
      
      appendOp(2, axesType);
      nextToken();
    }
    else
    {
      matchTypePos = m_ops.getOp(1);
      axesType = 53;
      
      appendOp(2, axesType);
    }
    

    m_ops.setOp(1, m_ops.getOp(1) + 1);
    
    NodeTest(axesType);
    

    m_ops.setOp(opPos + 1 + 1, m_ops.getOp(1) - opPos);
    

    while (tokenIs('['))
    {
      Predicate();
    }
    




    boolean trailingSlashConsumed;
    



    boolean trailingSlashConsumed;
    



    if ((matchTypePos > -1) && (tokenIs('/')) && (lookahead('/', 1)))
    {
      m_ops.setOp(matchTypePos, 52);
      
      nextToken();
      
      trailingSlashConsumed = true;
    }
    else
    {
      trailingSlashConsumed = false;
    }
    

    m_ops.setOp(opPos + 1, m_ops.getOp(1) - opPos);
    

    return trailingSlashConsumed;
  }
}
