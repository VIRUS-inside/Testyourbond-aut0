package net.sourceforge.htmlunit.corejs.javascript;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.sourceforge.htmlunit.corejs.javascript.ast.ArrayComprehension;
import net.sourceforge.htmlunit.corejs.javascript.ast.ArrayComprehensionLoop;
import net.sourceforge.htmlunit.corejs.javascript.ast.ArrayLiteral;
import net.sourceforge.htmlunit.corejs.javascript.ast.AstNode;
import net.sourceforge.htmlunit.corejs.javascript.ast.AstRoot;
import net.sourceforge.htmlunit.corejs.javascript.ast.Block;
import net.sourceforge.htmlunit.corejs.javascript.ast.CatchClause;
import net.sourceforge.htmlunit.corejs.javascript.ast.Comment;
import net.sourceforge.htmlunit.corejs.javascript.ast.ConditionalExpression;
import net.sourceforge.htmlunit.corejs.javascript.ast.ContinueStatement;
import net.sourceforge.htmlunit.corejs.javascript.ast.DoLoop;
import net.sourceforge.htmlunit.corejs.javascript.ast.ElementGet;
import net.sourceforge.htmlunit.corejs.javascript.ast.ExpressionStatement;
import net.sourceforge.htmlunit.corejs.javascript.ast.ForInLoop;
import net.sourceforge.htmlunit.corejs.javascript.ast.FunctionCall;
import net.sourceforge.htmlunit.corejs.javascript.ast.FunctionNode;
import net.sourceforge.htmlunit.corejs.javascript.ast.GeneratorExpression;
import net.sourceforge.htmlunit.corejs.javascript.ast.GeneratorExpressionLoop;
import net.sourceforge.htmlunit.corejs.javascript.ast.IdeErrorReporter;
import net.sourceforge.htmlunit.corejs.javascript.ast.IfStatement;
import net.sourceforge.htmlunit.corejs.javascript.ast.InfixExpression;
import net.sourceforge.htmlunit.corejs.javascript.ast.Label;
import net.sourceforge.htmlunit.corejs.javascript.ast.LabeledStatement;
import net.sourceforge.htmlunit.corejs.javascript.ast.LetNode;
import net.sourceforge.htmlunit.corejs.javascript.ast.Loop;
import net.sourceforge.htmlunit.corejs.javascript.ast.Name;
import net.sourceforge.htmlunit.corejs.javascript.ast.NewExpression;
import net.sourceforge.htmlunit.corejs.javascript.ast.ObjectLiteral;
import net.sourceforge.htmlunit.corejs.javascript.ast.ObjectProperty;
import net.sourceforge.htmlunit.corejs.javascript.ast.ParenthesizedExpression;
import net.sourceforge.htmlunit.corejs.javascript.ast.PropertyGet;
import net.sourceforge.htmlunit.corejs.javascript.ast.Scope;
import net.sourceforge.htmlunit.corejs.javascript.ast.ScriptNode;
import net.sourceforge.htmlunit.corejs.javascript.ast.StringLiteral;
import net.sourceforge.htmlunit.corejs.javascript.ast.SwitchCase;
import net.sourceforge.htmlunit.corejs.javascript.ast.SwitchStatement;
import net.sourceforge.htmlunit.corejs.javascript.ast.TryStatement;
import net.sourceforge.htmlunit.corejs.javascript.ast.UnaryExpression;
import net.sourceforge.htmlunit.corejs.javascript.ast.VariableDeclaration;
import net.sourceforge.htmlunit.corejs.javascript.ast.VariableInitializer;
import net.sourceforge.htmlunit.corejs.javascript.ast.WhileLoop;
import net.sourceforge.htmlunit.corejs.javascript.ast.WithStatement;
import net.sourceforge.htmlunit.corejs.javascript.ast.XmlDotQuery;
import net.sourceforge.htmlunit.corejs.javascript.ast.XmlElemRef;
import net.sourceforge.htmlunit.corejs.javascript.ast.XmlLiteral;
import net.sourceforge.htmlunit.corejs.javascript.ast.XmlPropRef;

public class Parser
{
  public static final int ARGC_LIMIT = 65536;
  static final int CLEAR_TI_MASK = 65535;
  static final int TI_AFTER_EOL = 65536;
  static final int TI_CHECK_LABEL = 131072;
  CompilerEnvirons compilerEnv;
  private ErrorReporter errorReporter;
  private IdeErrorReporter errorCollector;
  private String sourceURI;
  private char[] sourceChars;
  boolean calledByCompileFunction;
  private boolean parseFinished;
  private TokenStream ts;
  private int currentFlaggedToken = 0;
  
  private int currentToken;
  
  private int syntaxErrorCount;
  
  private List<Comment> scannedComments;
  
  private Comment currentJsDocComment;
  
  protected int nestingOfFunction;
  
  private LabeledStatement currentLabel;
  
  private boolean inDestructuringAssignment;
  
  protected boolean inUseStrictDirective;
  
  ScriptNode currentScriptOrFn;
  
  Scope currentScope;
  private int endFlags;
  private boolean inForInit;
  private Map<String, LabeledStatement> labelSet;
  private List<Loop> loopSet;
  private List<net.sourceforge.htmlunit.corejs.javascript.ast.Jump> loopAndSwitchSet;
  private int prevNameTokenStart;
  private String prevNameTokenString = "";
  
  private int prevNameTokenLineno;
  private static final int PROP_ENTRY = 1;
  private static final int GET_ENTRY = 2;
  private static final int SET_ENTRY = 4;
  
  public Parser()
  {
    this(new CompilerEnvirons());
  }
  
  public Parser(CompilerEnvirons compilerEnv) {
    this(compilerEnv, compilerEnv.getErrorReporter());
  }
  
  public Parser(CompilerEnvirons compilerEnv, ErrorReporter errorReporter) {
    this.compilerEnv = compilerEnv;
    this.errorReporter = errorReporter;
    if ((errorReporter instanceof IdeErrorReporter)) {
      errorCollector = ((IdeErrorReporter)errorReporter);
    }
  }
  
  void addStrictWarning(String messageId, String messageArg)
  {
    int beg = -1;int end = -1;
    if (ts != null) {
      beg = ts.tokenBeg;
      end = ts.tokenEnd - ts.tokenBeg;
    }
    addStrictWarning(messageId, messageArg, beg, end);
  }
  
  void addStrictWarning(String messageId, String messageArg, int position, int length)
  {
    if (compilerEnv.isStrictMode())
      addWarning(messageId, messageArg, position, length);
  }
  
  void addWarning(String messageId, String messageArg) {
    int beg = -1;int end = -1;
    if (ts != null) {
      beg = ts.tokenBeg;
      end = ts.tokenEnd - ts.tokenBeg;
    }
    addWarning(messageId, messageArg, beg, end);
  }
  
  void addWarning(String messageId, int position, int length) {
    addWarning(messageId, null, position, length);
  }
  
  void addWarning(String messageId, String messageArg, int position, int length)
  {
    String message = lookupMessage(messageId, messageArg);
    if (compilerEnv.reportWarningAsError()) {
      addError(messageId, messageArg, position, length);
    } else if (errorCollector != null) {
      errorCollector.warning(message, sourceURI, position, length);
    } else {
      errorReporter.warning(message, sourceURI, ts.getLineno(), ts
        .getLine(), ts.getOffset());
    }
  }
  
  void addError(String messageId) {
    addError(messageId, ts.tokenBeg, ts.tokenEnd - ts.tokenBeg);
  }
  
  void addError(String messageId, int position, int length) {
    addError(messageId, null, position, length);
  }
  
  void addError(String messageId, String messageArg) {
    addError(messageId, messageArg, ts.tokenBeg, ts.tokenEnd - ts.tokenBeg);
  }
  
  void addError(String messageId, String messageArg, int position, int length)
  {
    syntaxErrorCount += 1;
    String message = lookupMessage(messageId, messageArg);
    if (errorCollector != null) {
      errorCollector.error(message, sourceURI, position, length);
    } else {
      int lineno = 1;int offset = 1;
      String line = "";
      if (ts != null) {
        lineno = ts.getLineno();
        line = ts.getLine();
        offset = ts.getOffset();
      }
      errorReporter.error(message, sourceURI, lineno, line, offset);
    }
  }
  

  private void addStrictWarning(String messageId, String messageArg, int position, int length, int line, String lineSource, int lineOffset)
  {
    if (compilerEnv.isStrictMode()) {
      addWarning(messageId, messageArg, position, length, line, lineSource, lineOffset);
    }
  }
  

  private void addWarning(String messageId, String messageArg, int position, int length, int line, String lineSource, int lineOffset)
  {
    String message = lookupMessage(messageId, messageArg);
    if (compilerEnv.reportWarningAsError()) {
      addError(messageId, messageArg, position, length, line, lineSource, lineOffset);
    }
    else if (errorCollector != null) {
      errorCollector.warning(message, sourceURI, position, length);
    } else {
      errorReporter.warning(message, sourceURI, line, lineSource, lineOffset);
    }
  }
  

  private void addError(String messageId, String messageArg, int position, int length, int line, String lineSource, int lineOffset)
  {
    syntaxErrorCount += 1;
    String message = lookupMessage(messageId, messageArg);
    if (errorCollector != null) {
      errorCollector.error(message, sourceURI, position, length);
    } else {
      errorReporter.error(message, sourceURI, line, lineSource, lineOffset);
    }
  }
  
  String lookupMessage(String messageId)
  {
    return lookupMessage(messageId, null);
  }
  
  String lookupMessage(String messageId, String messageArg) {
    return messageArg == null ? ScriptRuntime.getMessage0(messageId) : 
      ScriptRuntime.getMessage1(messageId, messageArg);
  }
  
  void reportError(String messageId) {
    reportError(messageId, null);
  }
  
  void reportError(String messageId, String messageArg) {
    if (ts == null) {
      reportError(messageId, messageArg, 1, 1);
    } else {
      reportError(messageId, messageArg, ts.tokenBeg, ts.tokenEnd - ts.tokenBeg);
    }
  }
  
  void reportError(String messageId, int position, int length)
  {
    reportError(messageId, null, position, length);
  }
  
  void reportError(String messageId, String messageArg, int position, int length)
  {
    addError(messageId, position, length);
    
    if (!compilerEnv.recoverFromErrors()) {
      throw new ParserException(null);
    }
  }
  


  private int getNodeEnd(AstNode n)
  {
    return n.getPosition() + n.getLength();
  }
  
  private void recordComment(int lineno, String comment) {
    if (scannedComments == null) {
      scannedComments = new ArrayList();
    }
    Comment commentNode = new Comment(ts.tokenBeg, ts.getTokenLength(), ts.commentType, comment);
    
    if ((ts.commentType == Token.CommentType.JSDOC) && 
      (compilerEnv.isRecordingLocalJsDocComments())) {
      currentJsDocComment = commentNode;
    }
    commentNode.setLineno(lineno);
    scannedComments.add(commentNode);
  }
  
  private Comment getAndResetJsDoc() {
    Comment saved = currentJsDocComment;
    currentJsDocComment = null;
    return saved;
  }
  
  private int getNumberOfEols(String comment) {
    int lines = 0;
    for (int i = comment.length() - 1; i >= 0; i--) {
      if (comment.charAt(i) == '\n') {
        lines++;
      }
    }
    return lines;
  }
  



  private static class ParserException
    extends RuntimeException
  {
    static final long serialVersionUID = 5882582646773765630L;
    


    private ParserException() {}
  }
  


  private int peekToken()
    throws IOException
  {
    if (currentFlaggedToken != 0) {
      return currentToken;
    }
    
    int lineno = ts.getLineno();
    int tt = ts.getToken();
    boolean sawEOL = false;
    

    while ((tt == 1) || (tt == 161)) {
      if (tt == 1) {
        lineno++;
        sawEOL = true;
      }
      else if (compilerEnv.isRecordingComments()) {
        String comment = ts.getAndResetCurrentComment();
        recordComment(lineno, comment);
        

        lineno += getNumberOfEols(comment);
      }
      
      tt = ts.getToken();
    }
    
    currentToken = tt;
    currentFlaggedToken = (tt | (sawEOL ? 65536 : 0));
    return currentToken;
  }
  
  private int peekFlaggedToken() throws IOException {
    peekToken();
    return currentFlaggedToken;
  }
  
  private void consumeToken() {
    currentFlaggedToken = 0;
  }
  
  private int nextToken() throws IOException {
    int tt = peekToken();
    consumeToken();
    return tt;
  }
  
  private int nextFlaggedToken() throws IOException {
    peekToken();
    int ttFlagged = currentFlaggedToken;
    consumeToken();
    return ttFlagged;
  }
  
  private boolean matchToken(int toMatch) throws IOException {
    if (peekToken() != toMatch) {
      return false;
    }
    consumeToken();
    return true;
  }
  



  private int peekTokenOrEOL()
    throws IOException
  {
    int tt = peekToken();
    
    if ((currentFlaggedToken & 0x10000) != 0) {
      tt = 1;
    }
    return tt;
  }
  
  private boolean mustMatchToken(int toMatch, String messageId) throws IOException
  {
    return mustMatchToken(toMatch, messageId, ts.tokenBeg, ts.tokenEnd - ts.tokenBeg);
  }
  
  private boolean mustMatchToken(int toMatch, String msgId, int pos, int len)
    throws IOException
  {
    if (matchToken(toMatch)) {
      return true;
    }
    reportError(msgId, pos, len);
    return false;
  }
  
  private void mustHaveXML() {
    if (!compilerEnv.isXmlAvailable()) {
      reportError("msg.XML.not.available");
    }
  }
  
  public boolean eof() {
    return ts.eof();
  }
  
  boolean insideFunction() {
    return nestingOfFunction != 0;
  }
  
  void pushScope(Scope scope) {
    Scope parent = scope.getParentScope();
    

    if (parent != null) {
      if (parent != currentScope)
        codeBug();
    } else {
      currentScope.addChildScope(scope);
    }
    currentScope = scope;
  }
  
  void popScope() {
    currentScope = currentScope.getParentScope();
  }
  
  private void enterLoop(Loop loop) {
    if (loopSet == null)
      loopSet = new ArrayList();
    loopSet.add(loop);
    if (loopAndSwitchSet == null)
      loopAndSwitchSet = new ArrayList();
    loopAndSwitchSet.add(loop);
    pushScope(loop);
    if (currentLabel != null) {
      currentLabel.setStatement(loop);
      currentLabel.getFirstLabel().setLoop(loop);
      



      loop.setRelative(-currentLabel.getPosition());
    }
  }
  
  private void exitLoop() {
    Loop loop = (Loop)loopSet.remove(loopSet.size() - 1);
    loopAndSwitchSet.remove(loopAndSwitchSet.size() - 1);
    if (loop.getParent() != null) {
      loop.setRelative(loop.getParent().getPosition());
    }
    popScope();
  }
  
  private void enterSwitch(SwitchStatement node) {
    if (loopAndSwitchSet == null)
      loopAndSwitchSet = new ArrayList();
    loopAndSwitchSet.add(node);
  }
  
  private void exitSwitch() {
    loopAndSwitchSet.remove(loopAndSwitchSet.size() - 1);
  }
  







  public AstRoot parse(String sourceString, String sourceURI, int lineno)
  {
    if (parseFinished)
      throw new IllegalStateException("parser reused");
    this.sourceURI = sourceURI;
    if (compilerEnv.isIdeMode()) {
      sourceChars = sourceString.toCharArray();
    }
    ts = new TokenStream(this, null, sourceString, lineno);
    try {
      return parse();
    }
    catch (IOException iox) {
      throw new IllegalStateException();
    } finally {
      parseFinished = true;
    }
  }
  






  public AstRoot parse(java.io.Reader sourceReader, String sourceURI, int lineno)
    throws IOException
  {
    if (parseFinished)
      throw new IllegalStateException("parser reused");
    if (compilerEnv.isIdeMode()) {
      return parse(readFully(sourceReader), sourceURI, lineno);
    }
    try {
      this.sourceURI = sourceURI;
      ts = new TokenStream(this, sourceReader, null, lineno);
      return parse();
    } finally {
      parseFinished = true;
    }
  }
  
  private AstRoot parse() throws IOException {
    int pos = 0;
    AstRoot root = new AstRoot(pos);
    currentScope = (this.currentScriptOrFn = root);
    
    int baseLineno = ts.lineno;
    int end = pos;
    
    boolean inDirectivePrologue = true;
    boolean savedStrictMode = inUseStrictDirective;
    
    inUseStrictDirective = false;
    String msg;
    try {
      for (;;) {
        int tt = peekToken();
        if (tt <= 0) {
          break;
        }
        
        AstNode n;
        if (tt == 109) {
          consumeToken();
          try {
            AstNode n = function(calledByCompileFunction ? 2 : 1);
            

            FunctionNode functionNode = (FunctionNode)n;
            if (functionNode.getName().indexOf('.') != -1) {
              String functionName = functionNode.getName();
              String left = functionName.substring(0, functionName
                .indexOf('.'));
              
              String right = functionName.substring(functionName.indexOf('.') + 1);
              PropertyGet propertyGet = new PropertyGet(new Name(0, left), new Name(0, right));
              
              net.sourceforge.htmlunit.corejs.javascript.ast.Assignment assignment = new net.sourceforge.htmlunit.corejs.javascript.ast.Assignment(90, propertyGet, functionNode, -1);
              
              functionNode.setFunctionName(null);
              functionNode.setFunctionType(2);
              

              n = new ExpressionStatement(assignment, !insideFunction());
            }
          } catch (ParserException e) {
            break;
          }
        } else {
          n = statement();
          if (inDirectivePrologue) {
            String directive = getDirective(n);
            if (directive == null) {
              inDirectivePrologue = false;
            } else if (directive.equals("use strict")) {
              inUseStrictDirective = true;
              root.setInStrictMode(true);
            }
          }
        }
        
        end = getNodeEnd(n);
        root.addChildToBack(n);
        n.setParent(root);
      }
    } catch (StackOverflowError ex) {
      msg = lookupMessage("msg.too.deep.parser.recursion");
      if (!compilerEnv.isIdeMode()) {
        throw Context.reportRuntimeError(msg, sourceURI, ts.lineno, null, 0);
      }
    } finally {
      inUseStrictDirective = savedStrictMode;
    }
    
    if (syntaxErrorCount != 0) {
      String msg = String.valueOf(syntaxErrorCount);
      msg = lookupMessage("msg.got.syntax.errors", msg);
      if (!compilerEnv.isIdeMode()) {
        throw errorReporter.runtimeError(msg, sourceURI, baseLineno, null, 0);
      }
    }
    

    if (scannedComments != null)
    {

      int last = scannedComments.size() - 1;
      end = Math.max(end, getNodeEnd((AstNode)scannedComments.get(last)));
      for (Comment c : scannedComments) {
        root.addComment(c);
      }
    }
    
    root.setLength(end - pos);
    root.setSourceName(sourceURI);
    root.setBaseLineno(baseLineno);
    root.setEndLineno(ts.lineno);
    return root;
  }
  
  private AstNode parseFunctionBody() throws IOException {
    boolean isExpressionClosure = false;
    if (!matchToken(85)) {
      if (compilerEnv.getLanguageVersion() < 180) {
        reportError("msg.no.brace.body");
      } else {
        isExpressionClosure = true;
      }
    }
    nestingOfFunction += 1;
    int pos = ts.tokenBeg;
    Block pn = new Block(pos);
    
    boolean inDirectivePrologue = true;
    boolean savedStrictMode = inUseStrictDirective;
    

    pn.setLineno(ts.lineno);
    try {
      if (isExpressionClosure) {
        net.sourceforge.htmlunit.corejs.javascript.ast.ReturnStatement n = new net.sourceforge.htmlunit.corejs.javascript.ast.ReturnStatement(ts.lineno);
        n.setReturnValue(assignExpr());
        
        n.putProp(25, Boolean.TRUE);
        pn.putProp(25, Boolean.TRUE);
        pn.addStatement(n);
      }
      else {
        for (;;) {
          int tt = peekToken();
          AstNode n; AstNode n; switch (tt)
          {
          case -1: 
          case 0: 
          case 86: 
            break;
          case 109: 
            consumeToken();
            n = function(1);
            break;
          default: 
            n = statement();
            if (inDirectivePrologue) {
              String directive = getDirective(n);
              if (directive == null) {
                inDirectivePrologue = false;
              } else if (directive.equals("use strict")) {
                inUseStrictDirective = true;
              }
            }
            break;
          }
          pn.addStatement(n);
        }
      }
    }
    catch (ParserException localParserException) {}finally
    {
      nestingOfFunction -= 1;
      inUseStrictDirective = savedStrictMode;
    }
    
    int end = ts.tokenEnd;
    getAndResetJsDoc();
    if ((!isExpressionClosure) && 
      (mustMatchToken(86, "msg.no.brace.after.body")))
      end = ts.tokenEnd;
    pn.setLength(end - pos);
    return pn;
  }
  
  private String getDirective(AstNode n) {
    if ((n instanceof ExpressionStatement)) {
      AstNode e = ((ExpressionStatement)n).getExpression();
      if ((e instanceof StringLiteral)) {
        return ((StringLiteral)e).getValue();
      }
    }
    return null;
  }
  
  private void parseFunctionParams(FunctionNode fnNode) throws IOException {
    if (matchToken(88)) {
      fnNode.setRp(ts.tokenBeg - fnNode.getPosition());
      return;
    }
    

    Map<String, Node> destructuring = null;
    Set<String> paramNames = new java.util.HashSet();
    String paramName;
    do { int tt = peekToken();
      if ((tt == 83) || (tt == 85)) {
        AstNode expr = destructuringPrimaryExpr();
        markDestructuring(expr);
        fnNode.addParam(expr);
        


        if (destructuring == null) {
          destructuring = new java.util.HashMap();
        }
        String pname = currentScriptOrFn.getNextTempName();
        defineSymbol(87, pname, false);
        destructuring.put(pname, expr);
      }
      else if (mustMatchToken(39, "msg.no.parm")) {
        fnNode.addParam(createNameNode());
        paramName = ts.getString();
        defineSymbol(87, paramName);
        if (inUseStrictDirective) {
          if (("eval".equals(paramName)) || 
            ("arguments".equals(paramName))) {
            reportError("msg.bad.id.strict", paramName);
          }
          if (paramNames.contains(paramName))
            addError("msg.dup.param.strict", paramName);
          paramNames.add(paramName);
        }
      } else {
        fnNode.addParam(makeErrorNode());
      }
      
    } while (matchToken(89));
    
    if (destructuring != null) {
      Node destructuringNode = new Node(89);
      
      for (java.util.Map.Entry<String, Node> param : destructuring.entrySet()) {
        Node assign = createDestructuringAssignment(122, 
          (Node)param.getValue(), createName((String)param.getKey()));
        destructuringNode.addChildToBack(assign);
      }
      
      fnNode.putProp(23, destructuringNode);
    }
    
    if (mustMatchToken(88, "msg.no.paren.after.parms")) {
      fnNode.setRp(ts.tokenBeg - fnNode.getPosition());
    }
  }
  
  private FunctionNode function(int type) throws IOException {
    int syntheticType = type;
    int baseLineno = ts.lineno;
    int functionSourceStart = ts.tokenBeg;
    Name name = null;
    AstNode memberExprNode = null;
    
    if (matchToken(39)) {
      name = createNameNode(true, 39);
      if (inUseStrictDirective) {
        String id = name.getIdentifier();
        if (("eval".equals(id)) || ("arguments".equals(id))) {
          reportError("msg.bad.id.strict", id);
        }
      }
      if (!matchToken(87)) {
        if (Context.getContext().hasFeature(108))
        {
          if ((matchToken(108)) && (matchToken(39))) {
            name.setIdentifier(name.getIdentifier() + '.' + 
              createNameNode(true, 39).getIdentifier());
            mustMatchToken(87, "msg.no.paren.parms");
            break label252; } }
        if (compilerEnv.isAllowMemberExprAsFunctionName()) {
          AstNode memberExprHead = name;
          name = null;
          memberExprNode = memberExprTail(false, memberExprHead);
        }
        mustMatchToken(87, "msg.no.paren.parms");
      }
    }
    else if (!matchToken(87))
    {

      if (compilerEnv.isAllowMemberExprAsFunctionName())
      {


        memberExprNode = memberExpr(false);
      }
      mustMatchToken(87, "msg.no.paren.parms"); }
    label252:
    int lpPos = currentToken == 87 ? ts.tokenBeg : -1;
    
    if (memberExprNode != null) {
      syntheticType = 2;
    }
    
    if ((syntheticType != 2) && (name != null) && 
      (name.length() > 0))
    {
      defineSymbol(109, name.getIdentifier());
    }
    
    FunctionNode fnNode = new FunctionNode(functionSourceStart, name);
    fnNode.setFunctionType(type);
    if (lpPos != -1) {
      fnNode.setLp(lpPos - functionSourceStart);
    }
    fnNode.setJsDocNode(getAndResetJsDoc());
    
    PerFunctionVariables savedVars = new PerFunctionVariables(fnNode);
    try {
      parseFunctionParams(fnNode);
      fnNode.setBody(parseFunctionBody());
      fnNode.setEncodedSourceBounds(functionSourceStart, ts.tokenEnd);
      fnNode.setLength(ts.tokenEnd - functionSourceStart);
      
      if ((compilerEnv.isStrictMode()) && 
        (!fnNode.getBody().hasConsistentReturnUsage())) {
        String msg = (name != null) && (name.length() > 0) ? "msg.no.return.value" : "msg.anon.no.return.value";
        
        addStrictWarning(msg, name == null ? "" : name.getIdentifier());
      }
    } finally {
      savedVars.restore();
    }
    
    if (memberExprNode != null)
    {
      Kit.codeBug();
      fnNode.setMemberExprNode(memberExprNode);
    }
    







    fnNode.setSourceName(sourceURI);
    fnNode.setBaseLineno(baseLineno);
    fnNode.setEndLineno(ts.lineno);
    




    if (compilerEnv.isIdeMode()) {
      fnNode.setParentScope(currentScope);
    }
    return fnNode;
  }
  






  private AstNode statements(AstNode parent)
    throws IOException
  {
    if ((currentToken != 85) && 
      (!compilerEnv.isIdeMode()))
      codeBug();
    int pos = ts.tokenBeg;
    AstNode block = parent != null ? parent : new Block(pos);
    block.setLineno(ts.lineno);
    
    int tt;
    while (((tt = peekToken()) > 0) && (tt != 86)) {
      block.addChild(statement());
    }
    block.setLength(ts.tokenBeg - pos);
    return block;
  }
  
  private AstNode statements() throws IOException {
    return statements(null);
  }
  
  private static class ConditionData {
    AstNode condition;
    int lp = -1;
    int rp = -1;
    
    private ConditionData() {}
  }
  
  private ConditionData condition() throws IOException { ConditionData data = new ConditionData(null);
    
    if (mustMatchToken(87, "msg.no.paren.cond")) {
      lp = ts.tokenBeg;
    }
    condition = expr();
    
    if (mustMatchToken(88, "msg.no.paren.after.cond")) {
      rp = ts.tokenBeg;
    }
    

    if ((condition instanceof net.sourceforge.htmlunit.corejs.javascript.ast.Assignment)) {
      addStrictWarning("msg.equal.as.assign", "", condition
        .getPosition(), condition.getLength());
    }
    return data;
  }
  
  private AstNode statement() throws IOException {
    int pos = ts.tokenBeg;
    try {
      AstNode pn = statementHelper();
      if (pn != null) {
        if ((compilerEnv.isStrictMode()) && (!pn.hasSideEffects())) {
          int beg = pn.getPosition();
          beg = Math.max(beg, lineBeginningFor(beg));
          addStrictWarning((pn instanceof net.sourceforge.htmlunit.corejs.javascript.ast.EmptyStatement) ? "msg.extra.trailing.semi" : "msg.no.side.effects", "", beg, 
          
            nodeEnd(pn) - beg);
        }
        return pn;
      }
    }
    catch (ParserException localParserException) {}
    

    for (;;)
    {
      int tt = peekTokenOrEOL();
      consumeToken();
      switch (tt)
      {
      case -1: 
      case 0: 
      case 1: 
      case 82: 
        break label142;
      }
      
    }
    label142:
    return new net.sourceforge.htmlunit.corejs.javascript.ast.EmptyStatement(pos, ts.tokenBeg - pos);
  }
  
  private AstNode statementHelper() throws IOException
  {
    if ((currentLabel != null) && (currentLabel.getStatement() != null)) {
      currentLabel = null;
    }
    AstNode pn = null;
    int tt = peekToken();int pos = ts.tokenBeg;
    
    switch (tt) {
    case 112: 
      return ifStatement();
    
    case 114: 
      return switchStatement();
    
    case 117: 
      return whileLoop();
    
    case 118: 
      return doLoop();
    
    case 119: 
      return forLoop();
    
    case 81: 
      return tryStatement();
    
    case 50: 
      pn = throwStatement();
      break;
    
    case 120: 
      pn = breakStatement();
      break;
    
    case 121: 
      pn = continueStatement();
      break;
    
    case 123: 
      if (inUseStrictDirective) {
        reportError("msg.no.with.strict");
      }
      return withStatement();
    
    case 122: 
    case 154: 
      consumeToken();
      int lineno = ts.lineno;
      pn = variables(currentToken, ts.tokenBeg, true);
      pn.setLineno(lineno);
      break;
    
    case 153: 
      pn = letStatement();
      if ((!(pn instanceof VariableDeclaration)) || (peekToken() != 82))
      {
        return pn;
      }
      break;
    case 4: case 72: 
      pn = returnOrYield(tt, false);
      break;
    
    case 160: 
      consumeToken();
      pn = new net.sourceforge.htmlunit.corejs.javascript.ast.KeywordLiteral(ts.tokenBeg, ts.tokenEnd - ts.tokenBeg, tt);
      pn.setLineno(ts.lineno);
      break;
    
    case 85: 
      return block();
    
    case -1: 
      consumeToken();
      return makeErrorNode();
    
    case 82: 
      consumeToken();
      pos = ts.tokenBeg;
      pn = new net.sourceforge.htmlunit.corejs.javascript.ast.EmptyStatement(pos, ts.tokenEnd - pos);
      pn.setLineno(ts.lineno);
      return pn;
    
    case 109: 
      consumeToken();
      return function(3);
    
    case 116: 
      pn = defaultXmlNamespace();
      break;
    
    case 39: 
      pn = nameOrLabel();
      if (!(pn instanceof ExpressionStatement))
      {
        return pn; }
      break;
    default: 
      int lineno = ts.lineno;
      pn = new ExpressionStatement(expr(), !insideFunction());
      pn.setLineno(lineno);
    }
    
    
    autoInsertSemicolon(pn);
    return pn;
  }
  
  private void autoInsertSemicolon(AstNode pn) throws IOException {
    int ttFlagged = peekFlaggedToken();
    int pos = pn.getPosition();
    switch (ttFlagged & 0xFFFF)
    {
    case 82: 
      consumeToken();
      
      pn.setLength(ts.tokenEnd - pos);
      break;
    
    case -1: 
    case 0: 
    case 86: 
      warnMissingSemi(pos, nodeEnd(pn));
      break;
    default: 
      if ((ttFlagged & 0x10000) == 0)
      {
        reportError("msg.no.semi.stmt");
      } else {
        warnMissingSemi(pos, nodeEnd(pn));
      }
      break;
    }
  }
  
  private IfStatement ifStatement() throws IOException {
    if (currentToken != 112)
      codeBug();
    consumeToken();
    int pos = ts.tokenBeg;int lineno = ts.lineno;int elsePos = -1;
    ConditionData data = condition();
    AstNode ifTrue = statement();AstNode ifFalse = null;
    if (matchToken(113)) {
      elsePos = ts.tokenBeg - pos;
      ifFalse = statement();
    }
    int end = getNodeEnd(ifFalse != null ? ifFalse : ifTrue);
    IfStatement pn = new IfStatement(pos, end - pos);
    pn.setCondition(condition);
    pn.setParens(lp - pos, rp - pos);
    pn.setThenPart(ifTrue);
    pn.setElsePart(ifFalse);
    pn.setElsePosition(elsePos);
    pn.setLineno(lineno);
    return pn;
  }
  
  private SwitchStatement switchStatement() throws IOException {
    if (currentToken != 114)
      codeBug();
    consumeToken();
    int pos = ts.tokenBeg;
    
    SwitchStatement pn = new SwitchStatement(pos);
    if (mustMatchToken(87, "msg.no.paren.switch"))
      pn.setLp(ts.tokenBeg - pos);
    pn.setLineno(ts.lineno);
    
    AstNode discriminant = expr();
    pn.setExpression(discriminant);
    enterSwitch(pn);
    try
    {
      if (mustMatchToken(88, "msg.no.paren.after.switch")) {
        pn.setRp(ts.tokenBeg - pos);
      }
      mustMatchToken(85, "msg.no.brace.switch");
      
      boolean hasDefault = false;
      for (;;)
      {
        int tt = nextToken();
        int casePos = ts.tokenBeg;
        int caseLineno = ts.lineno;
        AstNode caseExpression = null;
        switch (tt) {
        case 86: 
          pn.setLength(ts.tokenEnd - pos);
          break;
        
        case 115: 
          caseExpression = expr();
          mustMatchToken(103, "msg.no.colon.case");
          break;
        
        case 116: 
          if (hasDefault) {
            reportError("msg.double.switch.default");
          }
          hasDefault = true;
          caseExpression = null;
          mustMatchToken(103, "msg.no.colon.case");
          break;
        
        default: 
          reportError("msg.bad.switch");
          break;
        }
        
        SwitchCase caseNode = new SwitchCase(casePos);
        caseNode.setExpression(caseExpression);
        caseNode.setLength(ts.tokenEnd - pos);
        caseNode.setLineno(caseLineno);
        
        while (((tt = peekToken()) != 86) && (tt != 115) && (tt != 116) && (tt != 0))
        {
          caseNode.addStatement(statement());
        }
        pn.addCase(caseNode);
      }
    } finally {
      exitSwitch();
    }
    return pn;
  }
  
  private WhileLoop whileLoop() throws IOException {
    if (currentToken != 117)
      codeBug();
    consumeToken();
    int pos = ts.tokenBeg;
    WhileLoop pn = new WhileLoop(pos);
    pn.setLineno(ts.lineno);
    enterLoop(pn);
    try {
      ConditionData data = condition();
      pn.setCondition(condition);
      pn.setParens(lp - pos, rp - pos);
      AstNode body = statement();
      pn.setLength(getNodeEnd(body) - pos);
      pn.setBody(body);
    } finally {
      exitLoop();
    }
    return pn;
  }
  
  private DoLoop doLoop() throws IOException {
    if (currentToken != 118)
      codeBug();
    consumeToken();
    int pos = ts.tokenBeg;
    DoLoop pn = new DoLoop(pos);
    pn.setLineno(ts.lineno);
    enterLoop(pn);
    try {
      AstNode body = statement();
      mustMatchToken(117, "msg.no.while.do");
      pn.setWhilePosition(ts.tokenBeg - pos);
      ConditionData data = condition();
      pn.setCondition(condition);
      pn.setParens(lp - pos, rp - pos);
      int end = getNodeEnd(body);
      pn.setBody(body);
    } finally {
      exitLoop();
    }
    
    int end;
    
    if (matchToken(82)) {
      end = ts.tokenEnd;
    }
    pn.setLength(end - pos);
    return pn;
  }
  
  private Loop forLoop() throws IOException {
    if (currentToken != 119)
      codeBug();
    consumeToken();
    int forPos = ts.tokenBeg;int lineno = ts.lineno;
    boolean isForEach = false;boolean isForIn = false;
    int eachPos = -1;int inPos = -1;int lp = -1;int rp = -1;
    AstNode init = null;
    AstNode cond = null;
    AstNode incr = null;
    Loop pn = null;
    
    Scope tempScope = new Scope();
    pushScope(tempScope);
    try
    {
      if (matchToken(39)) {
        if ("each".equals(ts.getString())) {
          isForEach = true;
          eachPos = ts.tokenBeg - forPos;
        } else {
          reportError("msg.no.paren.for");
        }
      }
      
      if (mustMatchToken(87, "msg.no.paren.for"))
        lp = ts.tokenBeg - forPos;
      int tt = peekToken();
      
      init = forLoopInit(tt);
      
      if (matchToken(52)) {
        isForIn = true;
        inPos = ts.tokenBeg - forPos;
        cond = expr();
      } else {
        mustMatchToken(82, "msg.no.semi.for");
        if (peekToken() == 82)
        {
          cond = new net.sourceforge.htmlunit.corejs.javascript.ast.EmptyExpression(ts.tokenBeg, 1);
          cond.setLineno(ts.lineno);
        } else {
          cond = expr();
        }
        
        mustMatchToken(82, "msg.no.semi.for.cond");
        int tmpPos = ts.tokenEnd;
        if (peekToken() == 88) {
          incr = new net.sourceforge.htmlunit.corejs.javascript.ast.EmptyExpression(tmpPos, 1);
          incr.setLineno(ts.lineno);
        } else {
          incr = expr();
        }
      }
      
      if (mustMatchToken(88, "msg.no.paren.for.ctrl")) {
        rp = ts.tokenBeg - forPos;
      }
      if (isForIn) {
        ForInLoop fis = new ForInLoop(forPos);
        if ((init instanceof VariableDeclaration))
        {

          if (((VariableDeclaration)init).getVariables().size() > 1) {
            reportError("msg.mult.index");
          }
        }
        fis.setIterator(init);
        fis.setIteratedObject(cond);
        fis.setInPosition(inPos);
        fis.setIsForEach(isForEach);
        fis.setEachPosition(eachPos);
        pn = fis;
      } else {
        net.sourceforge.htmlunit.corejs.javascript.ast.ForLoop fl = new net.sourceforge.htmlunit.corejs.javascript.ast.ForLoop(forPos);
        fl.setInitializer(init);
        fl.setCondition(cond);
        fl.setIncrement(incr);
        pn = fl;
      }
      

      currentScope.replaceWith(pn);
      popScope();
      



      enterLoop(pn);
      try {
        AstNode body = statement();
        pn.setLength(getNodeEnd(body) - forPos);
        pn.setBody(body);
      } finally {
        exitLoop();
      }
    }
    finally {
      if (currentScope == tempScope) {
        popScope();
      }
    }
    pn.setParens(lp, rp);
    pn.setLineno(lineno);
    return pn;
  }
  
  private AstNode forLoopInit(int tt) throws IOException {
    try {
      inForInit = true;
      AstNode init = null;
      if (tt == 82) {
        init = new net.sourceforge.htmlunit.corejs.javascript.ast.EmptyExpression(ts.tokenBeg, 1);
        init.setLineno(ts.lineno);
      } else if ((tt == 122) || (tt == 153)) {
        consumeToken();
        init = variables(tt, ts.tokenBeg, false);
      } else {
        init = expr();
        markDestructuring(init);
      }
      return init;
    } finally {
      inForInit = false;
    }
  }
  
  private TryStatement tryStatement() throws IOException {
    if (currentToken != 81)
      codeBug();
    consumeToken();
    

    Comment jsdocNode = getAndResetJsDoc();
    
    int tryPos = ts.tokenBeg;int lineno = ts.lineno;int finallyPos = -1;
    if (peekToken() != 85) {
      reportError("msg.no.brace.try");
    }
    AstNode tryBlock = statement();
    int tryEnd = getNodeEnd(tryBlock);
    
    List<CatchClause> clauses = null;
    
    boolean sawDefaultCatch = false;
    int peek = peekToken();
    if (peek == 124)
      while (matchToken(124)) {
        int catchLineNum = ts.lineno;
        if (sawDefaultCatch) {
          reportError("msg.catch.unreachable");
        }
        int catchPos = ts.tokenBeg;int lp = -1;int rp = -1;int guardPos = -1;
        if (mustMatchToken(87, "msg.no.paren.catch")) {
          lp = ts.tokenBeg;
        }
        mustMatchToken(39, "msg.bad.catchcond");
        Name varName = createNameNode();
        String varNameString = varName.getIdentifier();
        if ((inUseStrictDirective) && (
          ("eval".equals(varNameString)) || 
          ("arguments".equals(varNameString)))) {
          reportError("msg.bad.id.strict", varNameString);
        }
        

        AstNode catchCond = null;
        if (matchToken(112)) {
          guardPos = ts.tokenBeg;
          catchCond = expr();
        } else {
          sawDefaultCatch = true;
        }
        
        if (mustMatchToken(88, "msg.bad.catchcond"))
          rp = ts.tokenBeg;
        mustMatchToken(85, "msg.no.brace.catchblock");
        
        Block catchBlock = (Block)statements();
        tryEnd = getNodeEnd(catchBlock);
        CatchClause catchNode = new CatchClause(catchPos);
        catchNode.setVarName(varName);
        catchNode.setCatchCondition(catchCond);
        catchNode.setBody(catchBlock);
        if (guardPos != -1) {
          catchNode.setIfPosition(guardPos - catchPos);
        }
        catchNode.setParens(lp, rp);
        catchNode.setLineno(catchLineNum);
        
        if (mustMatchToken(86, "msg.no.brace.after.body"))
          tryEnd = ts.tokenEnd;
        catchNode.setLength(tryEnd - catchPos);
        if (clauses == null)
          clauses = new ArrayList();
        clauses.add(catchNode);
      }
    if (peek != 125) {
      mustMatchToken(125, "msg.try.no.catchfinally");
    }
    
    AstNode finallyBlock = null;
    if (matchToken(125)) {
      finallyPos = ts.tokenBeg;
      finallyBlock = statement();
      tryEnd = getNodeEnd(finallyBlock);
    }
    
    TryStatement pn = new TryStatement(tryPos, tryEnd - tryPos);
    pn.setTryBlock(tryBlock);
    pn.setCatchClauses(clauses);
    pn.setFinallyBlock(finallyBlock);
    if (finallyPos != -1) {
      pn.setFinallyPosition(finallyPos - tryPos);
    }
    pn.setLineno(lineno);
    
    if (jsdocNode != null) {
      pn.setJsDocNode(jsdocNode);
    }
    
    return pn;
  }
  
  private net.sourceforge.htmlunit.corejs.javascript.ast.ThrowStatement throwStatement() throws IOException {
    if (currentToken != 50)
      codeBug();
    consumeToken();
    int pos = ts.tokenBeg;int lineno = ts.lineno;
    if (peekTokenOrEOL() == 1)
    {

      reportError("msg.bad.throw.eol");
    }
    AstNode expr = expr();
    net.sourceforge.htmlunit.corejs.javascript.ast.ThrowStatement pn = new net.sourceforge.htmlunit.corejs.javascript.ast.ThrowStatement(pos, getNodeEnd(expr), expr);
    pn.setLineno(lineno);
    return pn;
  }
  




  private LabeledStatement matchJumpLabelName()
    throws IOException
  {
    LabeledStatement label = null;
    
    if (peekTokenOrEOL() == 39) {
      consumeToken();
      if (labelSet != null) {
        label = (LabeledStatement)labelSet.get(ts.getString());
      }
      if (label == null) {
        reportError("msg.undef.label");
      }
    }
    
    return label;
  }
  
  private net.sourceforge.htmlunit.corejs.javascript.ast.BreakStatement breakStatement() throws IOException {
    if (currentToken != 120)
      codeBug();
    consumeToken();
    int lineno = ts.lineno;int pos = ts.tokenBeg;int end = ts.tokenEnd;
    Name breakLabel = null;
    if (peekTokenOrEOL() == 39) {
      breakLabel = createNameNode();
      end = getNodeEnd(breakLabel);
    }
    

    LabeledStatement labels = matchJumpLabelName();
    
    net.sourceforge.htmlunit.corejs.javascript.ast.Jump breakTarget = labels == null ? null : labels.getFirstLabel();
    
    if ((breakTarget == null) && (breakLabel == null)) {
      if ((loopAndSwitchSet == null) || (loopAndSwitchSet.size() == 0)) {
        if (breakLabel == null) {
          reportError("msg.bad.break", pos, end - pos);
        }
      } else {
        breakTarget = (net.sourceforge.htmlunit.corejs.javascript.ast.Jump)loopAndSwitchSet.get(loopAndSwitchSet.size() - 1);
      }
    }
    
    net.sourceforge.htmlunit.corejs.javascript.ast.BreakStatement pn = new net.sourceforge.htmlunit.corejs.javascript.ast.BreakStatement(pos, end - pos);
    pn.setBreakLabel(breakLabel);
    
    if (breakTarget != null)
      pn.setBreakTarget(breakTarget);
    pn.setLineno(lineno);
    return pn;
  }
  
  private ContinueStatement continueStatement() throws IOException {
    if (currentToken != 121)
      codeBug();
    consumeToken();
    int lineno = ts.lineno;int pos = ts.tokenBeg;int end = ts.tokenEnd;
    Name label = null;
    if (peekTokenOrEOL() == 39) {
      label = createNameNode();
      end = getNodeEnd(label);
    }
    

    LabeledStatement labels = matchJumpLabelName();
    Loop target = null;
    if ((labels == null) && (label == null)) {
      if ((loopSet == null) || (loopSet.size() == 0)) {
        reportError("msg.continue.outside");
      } else {
        target = (Loop)loopSet.get(loopSet.size() - 1);
      }
    } else {
      if ((labels == null) || (!(labels.getStatement() instanceof Loop))) {
        reportError("msg.continue.nonloop", pos, end - pos);
      }
      target = labels == null ? null : (Loop)labels.getStatement();
    }
    
    ContinueStatement pn = new ContinueStatement(pos, end - pos);
    if (target != null)
      pn.setTarget(target);
    pn.setLabel(label);
    pn.setLineno(lineno);
    return pn;
  }
  
  private WithStatement withStatement() throws IOException {
    if (currentToken != 123)
      codeBug();
    consumeToken();
    
    Comment withComment = getAndResetJsDoc();
    
    int lineno = ts.lineno;int pos = ts.tokenBeg;int lp = -1;int rp = -1;
    if (mustMatchToken(87, "msg.no.paren.with")) {
      lp = ts.tokenBeg;
    }
    AstNode obj = expr();
    
    if (mustMatchToken(88, "msg.no.paren.after.with")) {
      rp = ts.tokenBeg;
    }
    AstNode body = statement();
    
    WithStatement pn = new WithStatement(pos, getNodeEnd(body) - pos);
    pn.setJsDocNode(withComment);
    pn.setExpression(obj);
    pn.setStatement(body);
    pn.setParens(lp, rp);
    pn.setLineno(lineno);
    return pn;
  }
  
  private AstNode letStatement() throws IOException {
    if (currentToken != 153)
      codeBug();
    consumeToken();
    int lineno = ts.lineno;int pos = ts.tokenBeg;
    AstNode pn;
    AstNode pn; if (peekToken() == 87) {
      pn = let(true, pos);
    } else {
      pn = variables(153, pos, true);
    }
    pn.setLineno(lineno);
    return pn;
  }
  











  private static final boolean nowAllSet(int before, int after, int mask)
  {
    return ((before & mask) != mask) && ((after & mask) == mask);
  }
  
  private AstNode returnOrYield(int tt, boolean exprContext) throws IOException
  {
    if (!insideFunction()) {
      reportError(tt == 4 ? "msg.bad.return" : "msg.bad.yield");
    }
    
    consumeToken();
    int lineno = ts.lineno;int pos = ts.tokenBeg;int end = ts.tokenEnd;
    
    AstNode e = null;
    
    switch (peekTokenOrEOL()) {
    case -1: 
    case 0: 
    case 1: 
    case 72: 
    case 82: 
    case 84: 
    case 86: 
    case 88: 
      break;
    default: 
      e = expr();
      end = getNodeEnd(e);
    }
    
    int before = endFlags;
    
    AstNode ret;
    if (tt == 4) {
      endFlags |= (e == null ? 2 : 4);
      AstNode ret = new net.sourceforge.htmlunit.corejs.javascript.ast.ReturnStatement(pos, end - pos, e);
      

      if (nowAllSet(before, endFlags, 6))
      {
        addStrictWarning("msg.return.inconsistent", "", pos, end - pos); }
    } else {
      if (!insideFunction())
        reportError("msg.bad.yield");
      endFlags |= 0x8;
      ret = new net.sourceforge.htmlunit.corejs.javascript.ast.Yield(pos, end - pos, e);
      setRequiresActivation();
      setIsGenerator();
      if (!exprContext) {
        ret = new ExpressionStatement(ret);
      }
    }
    

    if ((insideFunction()) && (nowAllSet(before, endFlags, 12)))
    {
      Name name = ((FunctionNode)currentScriptOrFn).getFunctionName();
      if ((name == null) || (name.length() == 0)) {
        addError("msg.anon.generator.returns", "");
      } else {
        addError("msg.generator.returns", name.getIdentifier());
      }
    }
    ret.setLineno(lineno);
    return ret;
  }
  
  private AstNode block() throws IOException {
    if (currentToken != 85)
      codeBug();
    consumeToken();
    int pos = ts.tokenBeg;
    Scope block = new Scope(pos);
    block.setLineno(ts.lineno);
    pushScope(block);
    try {
      statements(block);
      mustMatchToken(86, "msg.no.brace.block");
      block.setLength(ts.tokenEnd - pos);
      return block;
    } finally {
      popScope();
    }
  }
  
  private AstNode defaultXmlNamespace() throws IOException {
    if (currentToken != 116)
      codeBug();
    consumeToken();
    mustHaveXML();
    setRequiresActivation();
    int lineno = ts.lineno;int pos = ts.tokenBeg;
    
    if ((!matchToken(39)) || (!"xml".equals(ts.getString()))) {
      reportError("msg.bad.namespace");
    }
    if ((!matchToken(39)) || (!"namespace".equals(ts.getString()))) {
      reportError("msg.bad.namespace");
    }
    if (!matchToken(90)) {
      reportError("msg.bad.namespace");
    }
    
    AstNode e = expr();
    UnaryExpression dxmln = new UnaryExpression(pos, getNodeEnd(e) - pos);
    dxmln.setOperator(74);
    dxmln.setOperand(e);
    dxmln.setLineno(lineno);
    
    ExpressionStatement es = new ExpressionStatement(dxmln, true);
    return es;
  }
  
  private void recordLabel(Label label, LabeledStatement bundle)
    throws IOException
  {
    if (peekToken() != 103)
      codeBug();
    consumeToken();
    String name = label.getName();
    if (labelSet == null) {
      labelSet = new java.util.HashMap();
    } else {
      LabeledStatement ls = (LabeledStatement)labelSet.get(name);
      if (ls != null) {
        if (compilerEnv.isIdeMode()) {
          Label dup = ls.getLabelByName(name);
          reportError("msg.dup.label", dup.getAbsolutePosition(), dup
            .getLength());
        }
        reportError("msg.dup.label", label.getPosition(), label
          .getLength());
      }
    }
    bundle.addLabel(label);
    labelSet.put(name, bundle);
  }
  




  private AstNode nameOrLabel()
    throws IOException
  {
    if (currentToken != 39)
      throw codeBug();
    int pos = ts.tokenBeg;
    

    currentFlaggedToken |= 0x20000;
    AstNode expr = expr();
    
    if (expr.getType() != 130) {
      AstNode n = new ExpressionStatement(expr, !insideFunction());
      lineno = lineno;
      return n;
    }
    
    LabeledStatement bundle = new LabeledStatement(pos);
    recordLabel((Label)expr, bundle);
    bundle.setLineno(ts.lineno);
    
    AstNode stmt = null;
    while (peekToken() == 39) {
      currentFlaggedToken |= 0x20000;
      expr = expr();
      if (expr.getType() != 130) {
        stmt = new ExpressionStatement(expr, !insideFunction());
        autoInsertSemicolon(stmt);
        break;
      }
      recordLabel((Label)expr, bundle);
    }
    
    try
    {
      currentLabel = bundle;
      if (stmt == null)
        stmt = statementHelper();
    } finally { java.util.Iterator localIterator1;
      Label lb;
      currentLabel = null;
      
      for (Label lb : bundle.getLabels()) {
        labelSet.remove(lb.getName());
      }
    }
    


    bundle.setLength(stmt.getParent() == null ? getNodeEnd(stmt) - pos : 
      getNodeEnd(stmt));
    bundle.setStatement(stmt);
    return bundle;
  }
  












  private VariableDeclaration variables(int declType, int pos, boolean isStatement)
    throws IOException
  {
    VariableDeclaration pn = new VariableDeclaration(pos);
    pn.setType(declType);
    pn.setLineno(ts.lineno);
    Comment varjsdocNode = getAndResetJsDoc();
    if (varjsdocNode != null) {
      pn.setJsDocNode(varjsdocNode);
    }
    
    int end;
    for (;;)
    {
      AstNode destructuring = null;
      Name name = null;
      int tt = peekToken();int kidPos = ts.tokenBeg;
      end = ts.tokenEnd;
      
      if ((tt == 83) || (tt == 85))
      {
        destructuring = destructuringPrimaryExpr();
        end = getNodeEnd(destructuring);
        if (!(destructuring instanceof net.sourceforge.htmlunit.corejs.javascript.ast.DestructuringForm))
          reportError("msg.bad.assign.left", kidPos, end - kidPos);
        markDestructuring(destructuring);
      }
      else {
        mustMatchToken(39, "msg.bad.var");
        name = createNameNode();
        name.setLineno(ts.getLineno());
        if (inUseStrictDirective) {
          String id = ts.getString();
          if (("eval".equals(id)) || 
            ("arguments".equals(ts.getString()))) {
            reportError("msg.bad.id.strict", id);
          }
        }
        defineSymbol(declType, ts.getString(), inForInit);
      }
      
      int lineno = ts.lineno;
      
      Comment jsdocNode = getAndResetJsDoc();
      
      AstNode init = null;
      if (matchToken(90)) {
        init = assignExpr();
        end = getNodeEnd(init);
      }
      
      VariableInitializer vi = new VariableInitializer(kidPos, end - kidPos);
      
      if (destructuring != null) {
        if ((init == null) && (!inForInit)) {
          reportError("msg.destruct.assign.no.init");
        }
        vi.setTarget(destructuring);
      } else {
        vi.setTarget(name);
      }
      vi.setInitializer(init);
      vi.setType(declType);
      vi.setJsDocNode(jsdocNode);
      vi.setLineno(lineno);
      pn.addVariable(vi);
      
      if (!matchToken(89))
        break;
    }
    pn.setLength(end - pos);
    pn.setIsStatement(isStatement);
    return pn;
  }
  
  private AstNode let(boolean isStatement, int pos) throws IOException
  {
    LetNode pn = new LetNode(pos);
    pn.setLineno(ts.lineno);
    if (mustMatchToken(87, "msg.no.paren.after.let"))
      pn.setLp(ts.tokenBeg - pos);
    pushScope(pn);
    try {
      VariableDeclaration vars = variables(153, ts.tokenBeg, isStatement);
      
      pn.setVariables(vars);
      if (mustMatchToken(88, "msg.no.paren.let")) {
        pn.setRp(ts.tokenBeg - pos);
      }
      if ((isStatement) && (peekToken() == 85))
      {
        consumeToken();
        int beg = ts.tokenBeg;
        AstNode stmt = statements();
        mustMatchToken(86, "msg.no.curly.let");
        stmt.setLength(ts.tokenEnd - beg);
        pn.setLength(ts.tokenEnd - pos);
        pn.setBody(stmt);
        pn.setType(153);
      }
      else {
        AstNode expr = expr();
        pn.setLength(getNodeEnd(expr) - pos);
        pn.setBody(expr);
        if (isStatement)
        {

          ExpressionStatement es = new ExpressionStatement(pn, !insideFunction());
          es.setLineno(pn.getLineno());
          return es;
        }
      }
    } finally {
      popScope();
    }
    return pn;
  }
  
  void defineSymbol(int declType, String name) {
    defineSymbol(declType, name, false);
  }
  
  void defineSymbol(int declType, String name, boolean ignoreNotInBlock) {
    if (name == null) {
      if (compilerEnv.isIdeMode()) {
        return;
      }
      codeBug();
    }
    
    Scope definingScope = currentScope.getDefiningScope(name);
    net.sourceforge.htmlunit.corejs.javascript.ast.Symbol symbol = definingScope != null ? definingScope.getSymbol(name) : null;
    
    int symDeclType = symbol != null ? symbol.getDeclType() : -1;
    if ((symbol != null) && ((symDeclType == 154) || (declType == 154) || ((definingScope == currentScope) && (symDeclType == 153))))
    {

      addError(symDeclType == 109 ? "msg.fn.redecl" : symDeclType == 122 ? "msg.var.redecl" : symDeclType == 153 ? "msg.let.redecl" : symDeclType == 154 ? "msg.const.redecl" : "msg.parm.redecl", name);
      





      return;
    }
    switch (declType) {
    case 153: 
      if ((!ignoreNotInBlock) && ((currentScope.getType() == 112) || ((currentScope instanceof Loop))))
      {
        addError("msg.let.decl.not.in.block");
        return;
      }
      currentScope.putSymbol(new net.sourceforge.htmlunit.corejs.javascript.ast.Symbol(declType, name));
      return;
    
    case 109: 
    case 122: 
    case 154: 
      if (symbol != null) {
        if (symDeclType == 122) {
          addStrictWarning("msg.var.redecl", name);
        } else if (symDeclType == 87) {
          addStrictWarning("msg.var.hides.arg", name);
        }
      } else {
        currentScriptOrFn.putSymbol(new net.sourceforge.htmlunit.corejs.javascript.ast.Symbol(declType, name));
      }
      return;
    
    case 87: 
      if (symbol != null)
      {

        addWarning("msg.dup.parms", name);
      }
      currentScriptOrFn.putSymbol(new net.sourceforge.htmlunit.corejs.javascript.ast.Symbol(declType, name));
      return;
    }
    
    throw codeBug();
  }
  
  private AstNode expr() throws IOException
  {
    AstNode pn = assignExpr();
    int pos = pn.getPosition();
    while (matchToken(89)) {
      int opPos = ts.tokenBeg;
      if ((compilerEnv.isStrictMode()) && (!pn.hasSideEffects()))
        addStrictWarning("msg.no.side.effects", "", pos, 
          nodeEnd(pn) - pos);
      if (peekToken() == 72)
        reportError("msg.yield.parenthesized");
      pn = new InfixExpression(89, pn, assignExpr(), opPos);
    }
    return pn;
  }
  
  private AstNode assignExpr() throws IOException {
    int tt = peekToken();
    if (tt == 72) {
      return returnOrYield(tt, true);
    }
    AstNode pn = condExpr();
    tt = peekToken();
    if ((90 <= tt) && (tt <= 101)) {
      consumeToken();
      

      Comment jsdocNode = getAndResetJsDoc();
      
      markDestructuring(pn);
      int opPos = ts.tokenBeg;
      
      pn = new net.sourceforge.htmlunit.corejs.javascript.ast.Assignment(tt, pn, assignExpr(), opPos);
      
      if (jsdocNode != null) {
        pn.setJsDocNode(jsdocNode);
      }
    } else if (tt == 82)
    {

      if (currentJsDocComment != null) {
        pn.setJsDocNode(getAndResetJsDoc());
      }
    }
    return pn;
  }
  
  private AstNode condExpr() throws IOException {
    AstNode pn = orExpr();
    if (matchToken(102)) {
      int line = ts.lineno;
      int qmarkPos = ts.tokenBeg;int colonPos = -1;
      




      boolean wasInForInit = inForInit;
      inForInit = false;
      try
      {
        ifTrue = assignExpr();
      } finally { AstNode ifTrue;
        inForInit = wasInForInit; }
      AstNode ifTrue;
      if (mustMatchToken(103, "msg.no.colon.cond"))
        colonPos = ts.tokenBeg;
      AstNode ifFalse = assignExpr();
      int beg = pn.getPosition();int len = getNodeEnd(ifFalse) - beg;
      ConditionalExpression ce = new ConditionalExpression(beg, len);
      ce.setLineno(line);
      ce.setTestExpression(pn);
      ce.setTrueExpression(ifTrue);
      ce.setFalseExpression(ifFalse);
      ce.setQuestionMarkPosition(qmarkPos - beg);
      ce.setColonPosition(colonPos - beg);
      pn = ce;
    }
    return pn;
  }
  
  private AstNode orExpr() throws IOException {
    AstNode pn = andExpr();
    if (matchToken(104)) {
      int opPos = ts.tokenBeg;
      pn = new InfixExpression(104, pn, orExpr(), opPos);
    }
    return pn;
  }
  
  private AstNode andExpr() throws IOException {
    AstNode pn = bitOrExpr();
    if (matchToken(105)) {
      int opPos = ts.tokenBeg;
      pn = new InfixExpression(105, pn, andExpr(), opPos);
    }
    return pn;
  }
  
  private AstNode bitOrExpr() throws IOException {
    AstNode pn = bitXorExpr();
    while (matchToken(9)) {
      int opPos = ts.tokenBeg;
      pn = new InfixExpression(9, pn, bitXorExpr(), opPos);
    }
    return pn;
  }
  
  private AstNode bitXorExpr() throws IOException {
    AstNode pn = bitAndExpr();
    while (matchToken(10)) {
      int opPos = ts.tokenBeg;
      pn = new InfixExpression(10, pn, bitAndExpr(), opPos);
    }
    return pn;
  }
  
  private AstNode bitAndExpr() throws IOException {
    AstNode pn = eqExpr();
    while (matchToken(11)) {
      int opPos = ts.tokenBeg;
      pn = new InfixExpression(11, pn, eqExpr(), opPos);
    }
    return pn;
  }
  
  private AstNode eqExpr() throws IOException {
    AstNode pn = relExpr();
    for (;;) {
      int tt = peekToken();int opPos = ts.tokenBeg;
      switch (tt) {
      case 12: 
      case 13: 
      case 46: 
      case 47: 
        consumeToken();
        int parseToken = tt;
        if (compilerEnv.getLanguageVersion() == 120)
        {
          if (tt == 12) {
            parseToken = 46;
          } else if (tt == 13)
            parseToken = 47;
        }
        pn = new InfixExpression(parseToken, pn, relExpr(), opPos);
      }
      
    }
    
    return pn;
  }
  
  private AstNode relExpr() throws IOException {
    AstNode pn = shiftExpr();
    for (;;) {
      int tt = peekToken();int opPos = ts.tokenBeg;
      switch (tt) {
      case 52: 
        if (inForInit) {
          break;
        }
      case 14: 
      case 15: 
      case 16: 
      case 17: 
      case 53: 
        consumeToken();
        pn = new InfixExpression(tt, pn, shiftExpr(), opPos);
      }
      
    }
    
    return pn;
  }
  
  private AstNode shiftExpr() throws IOException {
    AstNode pn = addExpr();
    for (;;) {
      int tt = peekToken();int opPos = ts.tokenBeg;
      switch (tt) {
      case 18: 
      case 19: 
      case 20: 
        consumeToken();
        pn = new InfixExpression(tt, pn, addExpr(), opPos);
      }
      
    }
    
    return pn;
  }
  
  private AstNode addExpr() throws IOException {
    AstNode pn = mulExpr();
    for (;;) {
      int tt = peekToken();int opPos = ts.tokenBeg;
      if ((tt != 21) && (tt != 22)) break;
      consumeToken();
      pn = new InfixExpression(tt, pn, mulExpr(), opPos);
    }
    


    return pn;
  }
  
  private AstNode mulExpr() throws IOException {
    AstNode pn = unaryExpr();
    for (;;) {
      int tt = peekToken();int opPos = ts.tokenBeg;
      switch (tt) {
      case 23: 
      case 24: 
      case 25: 
        consumeToken();
        pn = new InfixExpression(tt, pn, unaryExpr(), opPos);
      }
      
    }
    
    return pn;
  }
  
  private AstNode unaryExpr() throws IOException
  {
    int tt = peekToken();
    int line = ts.lineno;
    
    switch (tt) {
    case 26: 
    case 27: 
    case 32: 
    case 126: 
      consumeToken();
      AstNode node = new UnaryExpression(tt, ts.tokenBeg, unaryExpr());
      node.setLineno(line);
      return node;
    
    case 21: 
      consumeToken();
      
      AstNode node = new UnaryExpression(28, ts.tokenBeg, unaryExpr());
      node.setLineno(line);
      return node;
    
    case 22: 
      consumeToken();
      
      AstNode node = new UnaryExpression(29, ts.tokenBeg, unaryExpr());
      node.setLineno(line);
      return node;
    
    case 106: 
    case 107: 
      consumeToken();
      
      UnaryExpression expr = new UnaryExpression(tt, ts.tokenBeg, memberExpr(true));
      expr.setLineno(line);
      checkBadIncDec(expr);
      return expr;
    
    case 31: 
      consumeToken();
      AstNode node = new UnaryExpression(tt, ts.tokenBeg, unaryExpr());
      node.setLineno(line);
      return node;
    
    case -1: 
      consumeToken();
      return makeErrorNode();
    

    case 14: 
      if (compilerEnv.isXmlAvailable()) {
        consumeToken();
        return memberExprTail(true, xmlInitializer());
      }
      break;
    }
    
    AstNode pn = memberExpr(true);
    
    tt = peekTokenOrEOL();
    if ((tt != 106) && (tt != 107)) {
      return pn;
    }
    consumeToken();
    UnaryExpression uexpr = new UnaryExpression(tt, ts.tokenBeg, pn, true);
    
    uexpr.setLineno(line);
    checkBadIncDec(uexpr);
    return uexpr;
  }
  
  private AstNode xmlInitializer() throws IOException
  {
    if (currentToken != 14)
      codeBug();
    int pos = ts.tokenBeg;int tt = ts.getFirstXMLToken();
    if ((tt != 145) && (tt != 148)) {
      reportError("msg.syntax");
      return makeErrorNode();
    }
    
    XmlLiteral pn = new XmlLiteral(pos);
    pn.setLineno(ts.lineno);
    for (;; 
        tt = ts.getNextXMLToken()) {
      switch (tt) {
      case 145: 
        pn.addFragment(new net.sourceforge.htmlunit.corejs.javascript.ast.XmlString(ts.tokenBeg, ts.getString()));
        mustMatchToken(85, "msg.syntax");
        int beg = ts.tokenBeg;
        
        AstNode expr = peekToken() == 86 ? new net.sourceforge.htmlunit.corejs.javascript.ast.EmptyExpression(beg, ts.tokenEnd - beg) : expr();
        mustMatchToken(86, "msg.syntax");
        net.sourceforge.htmlunit.corejs.javascript.ast.XmlExpression xexpr = new net.sourceforge.htmlunit.corejs.javascript.ast.XmlExpression(beg, expr);
        xexpr.setIsXmlAttribute(ts.isXMLAttribute());
        xexpr.setLength(ts.tokenEnd - beg);
        pn.addFragment(xexpr);
        break;
      
      case 148: 
        pn.addFragment(new net.sourceforge.htmlunit.corejs.javascript.ast.XmlString(ts.tokenBeg, ts.getString()));
        return pn;
      
      default: 
        reportError("msg.syntax");
        return makeErrorNode();
      }
    }
  }
  
  private List<AstNode> argumentList() throws IOException {
    if (matchToken(88)) {
      return null;
    }
    List<AstNode> result = new ArrayList();
    boolean wasInForInit = inForInit;
    inForInit = false;
    try {
      do {
        if (peekToken() == 72) {
          reportError("msg.yield.parenthesized");
        }
        AstNode en = assignExpr();
        if (peekToken() == 119) {
          try {
            result.add(generatorExpression(en, 0, true));

          }
          catch (IOException localIOException) {}
        } else {
          result.add(en);
        }
      } while (matchToken(89));
    } finally {
      inForInit = wasInForInit;
    }
    
    mustMatchToken(88, "msg.no.paren.arg");
    return result;
  }
  





  private AstNode memberExpr(boolean allowCallSyntax)
    throws IOException
  {
    int tt = peekToken();int lineno = ts.lineno;
    AstNode pn;
    AstNode pn;
    if (tt != 30) {
      pn = primaryExpr();
    } else {
      consumeToken();
      int pos = ts.tokenBeg;
      NewExpression nx = new NewExpression(pos);
      
      AstNode target = memberExpr(false);
      int end = getNodeEnd(target);
      nx.setTarget(target);
      
      int lp = -1;
      if (matchToken(87)) {
        lp = ts.tokenBeg;
        List<AstNode> args = argumentList();
        if ((args != null) && (args.size() > 65536))
          reportError("msg.too.many.constructor.args");
        int rp = ts.tokenBeg;
        end = ts.tokenEnd;
        if (args != null)
          nx.setArguments(args);
        nx.setParens(lp - pos, rp - pos);
      }
      




      if (matchToken(85)) {
        ObjectLiteral initializer = objectLiteral();
        end = getNodeEnd(initializer);
        nx.setInitializer(initializer);
      }
      nx.setLength(end - pos);
      pn = nx;
    }
    pn.setLineno(lineno);
    AstNode tail = memberExprTail(allowCallSyntax, pn);
    return tail;
  }
  









  private AstNode memberExprTail(boolean allowCallSyntax, AstNode pn)
    throws IOException
  {
    if (pn == null)
      codeBug();
    int pos = pn.getPosition();
    int lineno;
    for (;;) {
      int tt = peekToken();
      switch (tt) {
      case 108: 
      case 143: 
        int lineno = ts.lineno;
        pn = propertyAccess(tt, pn);
        pn.setLineno(lineno);
        break;
      
      case 146: 
        consumeToken();
        int opPos = ts.tokenBeg;int rp = -1;
        int lineno = ts.lineno;
        mustHaveXML();
        setRequiresActivation();
        AstNode filter = expr();
        int end = getNodeEnd(filter);
        if (mustMatchToken(88, "msg.no.paren")) {
          rp = ts.tokenBeg;
          end = ts.tokenEnd;
        }
        XmlDotQuery q = new XmlDotQuery(pos, end - pos);
        q.setLeft(pn);
        q.setRight(filter);
        q.setOperatorPosition(opPos);
        q.setRp(rp - pos);
        q.setLineno(lineno);
        pn = q;
        break;
      
      case 83: 
        consumeToken();
        int lb = ts.tokenBeg;int rb = -1;
        int lineno = ts.lineno;
        AstNode expr = expr();
        int end = getNodeEnd(expr);
        if (mustMatchToken(84, "msg.no.bracket.index")) {
          rb = ts.tokenBeg;
          end = ts.tokenEnd;
        }
        ElementGet g = new ElementGet(pos, end - pos);
        g.setTarget(pn);
        g.setElement(expr);
        g.setParens(lb, rb);
        g.setLineno(lineno);
        pn = g;
        break;
      
      case 87: 
        if (!allowCallSyntax) {
          return pn;
        }
        int lineno = ts.lineno;
        consumeToken();
        checkCallRequiresActivation(pn);
        FunctionCall f = new FunctionCall(pos);
        f.setTarget(pn);
        

        f.setLineno(lineno);
        f.setLp(ts.tokenBeg - pos);
        List<AstNode> args = argumentList();
        if ((args != null) && (args.size() > 65536))
          reportError("msg.too.many.function.args");
        f.setArguments(args);
        f.setRp(ts.tokenBeg - pos);
        f.setLength(ts.tokenEnd - pos);
        pn = f;
        break;
      default: 
        return pn;
      }
      
    }
    return pn;
  }
  





  private AstNode propertyAccess(int tt, AstNode pn)
    throws IOException
  {
    if (pn == null)
      codeBug();
    int memberTypeFlags = 0;int lineno = ts.lineno;int dotPos = ts.tokenBeg;
    consumeToken();
    
    if (tt == 143) {
      mustHaveXML();
      memberTypeFlags = 4;
    }
    
    if (!compilerEnv.isXmlAvailable()) {
      int maybeName = nextToken();
      if ((maybeName != 39) && (
        (!compilerEnv.isReservedKeywordAsIdentifier()) || 
        (!TokenStream.isKeyword(ts.getString())))) {
        reportError("msg.no.name.after.dot");
      }
      
      Name name = createNameNode(true, 33);
      PropertyGet pg = new PropertyGet(pn, name, dotPos);
      pg.setLineno(lineno);
      return pg;
    }
    
    AstNode ref = null;
    
    int token = nextToken();
    switch (token)
    {
    case 50: 
      saveNameTokenData(ts.tokenBeg, "throw", ts.lineno);
      ref = propertyName(-1, "throw", memberTypeFlags);
      break;
    

    case 39: 
      ref = propertyName(-1, ts.getString(), memberTypeFlags);
      break;
    

    case 23: 
      saveNameTokenData(ts.tokenBeg, "*", ts.lineno);
      ref = propertyName(-1, "*", memberTypeFlags);
      break;
    


    case 147: 
      ref = attributeAccess();
      break;
    
    default: 
      if (compilerEnv.isReservedKeywordAsIdentifier())
      {
        String name = Token.keywordToName(token);
        if (name != null) {
          saveNameTokenData(ts.tokenBeg, name, ts.lineno);
          ref = propertyName(-1, name, memberTypeFlags);
          break;
        }
      }
      reportError("msg.no.name.after.dot");
      return makeErrorNode();
    }
    
    boolean xml = ref instanceof net.sourceforge.htmlunit.corejs.javascript.ast.XmlRef;
    InfixExpression result = xml ? new net.sourceforge.htmlunit.corejs.javascript.ast.XmlMemberGet() : new PropertyGet();
    if ((xml) && (tt == 108))
      result.setType(108);
    int pos = pn.getPosition();
    result.setPosition(pos);
    result.setLength(getNodeEnd(ref) - pos);
    result.setOperatorPosition(dotPos - pos);
    result.setLineno(pn.getLineno());
    result.setLeft(pn);
    result.setRight(ref);
    return result;
  }
  







  private AstNode attributeAccess()
    throws IOException
  {
    int tt = nextToken();int atPos = ts.tokenBeg;
    
    switch (tt)
    {
    case 39: 
      return propertyName(atPos, ts.getString(), 0);
    

    case 23: 
      saveNameTokenData(ts.tokenBeg, "*", ts.lineno);
      return propertyName(atPos, "*", 0);
    

    case 83: 
      return xmlElemRef(atPos, null, -1);
    }
    
    reportError("msg.no.name.after.xmlAttr");
    return makeErrorNode();
  }
  


















  private AstNode propertyName(int atPos, String s, int memberTypeFlags)
    throws IOException
  {
    int pos = atPos != -1 ? atPos : ts.tokenBeg;int lineno = ts.lineno;
    int colonPos = -1;
    Name name = createNameNode(true, currentToken);
    Name ns = null;
    
    if (matchToken(144)) {
      ns = name;
      colonPos = ts.tokenBeg;
      
      switch (nextToken())
      {
      case 39: 
        name = createNameNode();
        break;
      

      case 23: 
        saveNameTokenData(ts.tokenBeg, "*", ts.lineno);
        name = createNameNode(false, -1);
        break;
      

      case 83: 
        return xmlElemRef(atPos, ns, colonPos);
      
      default: 
        reportError("msg.no.name.after.coloncolon");
        return makeErrorNode();
      }
      
    }
    if ((ns == null) && (memberTypeFlags == 0) && (atPos == -1)) {
      return name;
    }
    
    XmlPropRef ref = new XmlPropRef(pos, getNodeEnd(name) - pos);
    ref.setAtPos(atPos);
    ref.setNamespace(ns);
    ref.setColonPos(colonPos);
    ref.setPropName(name);
    ref.setLineno(lineno);
    return ref;
  }
  



  private XmlElemRef xmlElemRef(int atPos, Name namespace, int colonPos)
    throws IOException
  {
    int lb = ts.tokenBeg;int rb = -1;int pos = atPos != -1 ? atPos : lb;
    AstNode expr = expr();
    int end = getNodeEnd(expr);
    if (mustMatchToken(84, "msg.no.bracket.index")) {
      rb = ts.tokenBeg;
      end = ts.tokenEnd;
    }
    XmlElemRef ref = new XmlElemRef(pos, end - pos);
    ref.setNamespace(namespace);
    ref.setColonPos(colonPos);
    ref.setAtPos(atPos);
    ref.setExpression(expr);
    ref.setBrackets(lb, rb);
    return ref;
  }
  
  private AstNode destructuringPrimaryExpr() throws IOException, Parser.ParserException
  {
    try {
      inDestructuringAssignment = true;
      return primaryExpr();
    } finally {
      inDestructuringAssignment = false;
    }
  }
  
  private AstNode primaryExpr() throws IOException {
    int ttFlagged = nextFlaggedToken();
    int tt = ttFlagged & 0xFFFF;
    
    switch (tt) {
    case 109: 
      return function(2);
    
    case 83: 
      return arrayLiteral();
    
    case 85: 
      return objectLiteral();
    
    case 153: 
      return let(false, ts.tokenBeg);
    
    case 87: 
      return parenExpr();
    
    case 147: 
      mustHaveXML();
      return attributeAccess();
    
    case 39: 
      return name(ttFlagged, tt);
    
    case 40: 
      String s = ts.getString();
      if ((inUseStrictDirective) && (ts.isNumberOctal())) {
        reportError("msg.no.octal.strict");
      }
      if (ts.isNumberOctal()) {
        s = "0" + s;
      }
      if (ts.isNumberHex()) {
        s = "0x" + s;
      }
      return new net.sourceforge.htmlunit.corejs.javascript.ast.NumberLiteral(ts.tokenBeg, s, ts.getNumber());
    

    case 41: 
      return createStringLiteral();
    

    case 24: 
    case 100: 
      ts.readRegExp(tt);
      int pos = ts.tokenBeg;int end = ts.tokenEnd;
      net.sourceforge.htmlunit.corejs.javascript.ast.RegExpLiteral re = new net.sourceforge.htmlunit.corejs.javascript.ast.RegExpLiteral(pos, end - pos);
      re.setValue(ts.getString());
      re.setFlags(ts.readAndClearRegExpFlags());
      return re;
    
    case 42: 
    case 43: 
    case 44: 
    case 45: 
      int pos = ts.tokenBeg;
      int end = ts.tokenEnd;
      return new net.sourceforge.htmlunit.corejs.javascript.ast.KeywordLiteral(pos, end - pos, tt);
    
    case 127: 
      reportError("msg.reserved.id");
      break;
    
    case -1: 
      break;
    

    case 0: 
      reportError("msg.unexpected.eof");
      break;
    
    default: 
      reportError("msg.syntax");
    }
    
    
    return makeErrorNode();
  }
  
  private AstNode parenExpr() throws IOException {
    boolean wasInForInit = inForInit;
    inForInit = false;
    try {
      Comment jsdocNode = getAndResetJsDoc();
      int lineno = ts.lineno;
      int begin = ts.tokenBeg;
      AstNode e = expr();
      if (peekToken() == 119) {
        return generatorExpression(e, begin);
      }
      ParenthesizedExpression pn = new ParenthesizedExpression(e);
      if (jsdocNode == null) {
        jsdocNode = getAndResetJsDoc();
      }
      if (jsdocNode != null) {
        pn.setJsDocNode(jsdocNode);
      }
      mustMatchToken(88, "msg.no.paren");
      pn.setLength(ts.tokenEnd - pn.getPosition());
      pn.setLineno(lineno);
      return pn;
    } finally {
      inForInit = wasInForInit;
    }
  }
  
  private AstNode name(int ttFlagged, int tt) throws IOException {
    String nameString = ts.getString();
    int namePos = ts.tokenBeg;int nameLineno = ts.lineno;
    if ((0 != (ttFlagged & 0x20000)) && (peekToken() == 103))
    {

      Label label = new Label(namePos, ts.tokenEnd - namePos);
      label.setName(nameString);
      label.setLineno(ts.lineno);
      return label;
    }
    


    saveNameTokenData(namePos, nameString, nameLineno);
    
    if (compilerEnv.isXmlAvailable()) {
      return propertyName(-1, nameString, 0);
    }
    return createNameNode(true, 39);
  }
  


  private AstNode arrayLiteral()
    throws IOException
  {
    if (currentToken != 83)
      codeBug();
    int pos = ts.tokenBeg;int end = ts.tokenEnd;
    List<AstNode> elements = new ArrayList();
    ArrayLiteral pn = new ArrayLiteral(pos);
    boolean after_lb_or_comma = true;
    int afterComma = -1;
    int skipCount = 0;
    int tt;
    for (;;) { tt = peekToken();
      if (tt == 89) {
        consumeToken();
        afterComma = ts.tokenEnd;
        if (!after_lb_or_comma) {
          after_lb_or_comma = true;
        } else {
          elements.add(new net.sourceforge.htmlunit.corejs.javascript.ast.EmptyExpression(ts.tokenBeg, 1));
          skipCount++;
        }
      } else { if (tt == 84) {
          consumeToken();
          




          end = ts.tokenEnd;
          pn.setDestructuringLength(elements
            .size() + (after_lb_or_comma ? 1 : 0));
          pn.setSkipCount(skipCount);
          if (afterComma == -1) break;
          warnTrailingComma(pos, elements, afterComma); break;
        }
        if ((tt == 119) && (!after_lb_or_comma) && 
          (elements.size() == 1))
          return arrayComprehension((AstNode)elements.get(0), pos);
        if (tt == 0) {
          reportError("msg.no.bracket.arg");
          break;
        }
        if (!after_lb_or_comma) {
          reportError("msg.no.bracket.arg");
        }
        elements.add(assignExpr());
        after_lb_or_comma = false;
        afterComma = -1;
      }
    }
    for (AstNode e : elements) {
      pn.addElement(e);
    }
    pn.setLength(end - pos);
    return pn;
  }
  








  private AstNode arrayComprehension(AstNode result, int pos)
    throws IOException
  {
    List<ArrayComprehensionLoop> loops = new ArrayList();
    while (peekToken() == 119) {
      loops.add(arrayComprehensionLoop());
    }
    int ifPos = -1;
    ConditionData data = null;
    if (peekToken() == 112) {
      consumeToken();
      ifPos = ts.tokenBeg - pos;
      data = condition();
    }
    mustMatchToken(84, "msg.no.bracket.arg");
    ArrayComprehension pn = new ArrayComprehension(pos, ts.tokenEnd - pos);
    pn.setResult(result);
    pn.setLoops(loops);
    if (data != null) {
      pn.setIfPosition(ifPos);
      pn.setFilter(condition);
      pn.setFilterLp(lp - pos);
      pn.setFilterRp(rp - pos);
    }
    return pn;
  }
  
  private ArrayComprehensionLoop arrayComprehensionLoop() throws IOException {
    if (nextToken() != 119)
      codeBug();
    int pos = ts.tokenBeg;
    int eachPos = -1;int lp = -1;int rp = -1;int inPos = -1;
    ArrayComprehensionLoop pn = new ArrayComprehensionLoop(pos);
    
    pushScope(pn);
    try {
      if (matchToken(39)) {
        if (ts.getString().equals("each")) {
          eachPos = ts.tokenBeg - pos;
        } else {
          reportError("msg.no.paren.for");
        }
      }
      if (mustMatchToken(87, "msg.no.paren.for")) {
        lp = ts.tokenBeg - pos;
      }
      
      AstNode iter = null;
      switch (peekToken())
      {
      case 83: 
      case 85: 
        iter = destructuringPrimaryExpr();
        markDestructuring(iter);
        break;
      case 39: 
        consumeToken();
        iter = createNameNode();
        break;
      default: 
        reportError("msg.bad.var");
      }
      
      

      if (iter.getType() == 39) {
        defineSymbol(153, ts.getString(), true);
      }
      
      if (mustMatchToken(52, "msg.in.after.for.name"))
        inPos = ts.tokenBeg - pos;
      AstNode obj = expr();
      if (mustMatchToken(88, "msg.no.paren.for.ctrl")) {
        rp = ts.tokenBeg - pos;
      }
      pn.setLength(ts.tokenEnd - pos);
      pn.setIterator(iter);
      pn.setIteratedObject(obj);
      pn.setInPosition(inPos);
      pn.setEachPosition(eachPos);
      pn.setIsForEach(eachPos != -1);
      pn.setParens(lp, rp);
      return pn;
    } finally {
      popScope();
    }
  }
  
  private AstNode generatorExpression(AstNode result, int pos) throws IOException
  {
    return generatorExpression(result, pos, false);
  }
  
  private AstNode generatorExpression(AstNode result, int pos, boolean inFunctionParams)
    throws IOException
  {
    List<GeneratorExpressionLoop> loops = new ArrayList();
    while (peekToken() == 119) {
      loops.add(generatorExpressionLoop());
    }
    int ifPos = -1;
    ConditionData data = null;
    if (peekToken() == 112) {
      consumeToken();
      ifPos = ts.tokenBeg - pos;
      data = condition();
    }
    if (!inFunctionParams) {
      mustMatchToken(88, "msg.no.paren.let");
    }
    GeneratorExpression pn = new GeneratorExpression(pos, ts.tokenEnd - pos);
    
    pn.setResult(result);
    pn.setLoops(loops);
    if (data != null) {
      pn.setIfPosition(ifPos);
      pn.setFilter(condition);
      pn.setFilterLp(lp - pos);
      pn.setFilterRp(rp - pos);
    }
    return pn;
  }
  
  private GeneratorExpressionLoop generatorExpressionLoop() throws IOException
  {
    if (nextToken() != 119)
      codeBug();
    int pos = ts.tokenBeg;
    int lp = -1;int rp = -1;int inPos = -1;
    GeneratorExpressionLoop pn = new GeneratorExpressionLoop(pos);
    
    pushScope(pn);
    try {
      if (mustMatchToken(87, "msg.no.paren.for")) {
        lp = ts.tokenBeg - pos;
      }
      
      AstNode iter = null;
      switch (peekToken())
      {
      case 83: 
      case 85: 
        iter = destructuringPrimaryExpr();
        markDestructuring(iter);
        break;
      case 39: 
        consumeToken();
        iter = createNameNode();
        break;
      default: 
        reportError("msg.bad.var");
      }
      
      

      if (iter.getType() == 39) {
        defineSymbol(153, ts.getString(), true);
      }
      
      if (mustMatchToken(52, "msg.in.after.for.name"))
        inPos = ts.tokenBeg - pos;
      AstNode obj = expr();
      if (mustMatchToken(88, "msg.no.paren.for.ctrl")) {
        rp = ts.tokenBeg - pos;
      }
      pn.setLength(ts.tokenEnd - pos);
      pn.setIterator(iter);
      pn.setIteratedObject(obj);
      pn.setInPosition(inPos);
      pn.setParens(lp, rp);
      return pn;
    } finally {
      popScope();
    }
  }
  


  private ObjectLiteral objectLiteral()
    throws IOException
  {
    int pos = ts.tokenBeg;int lineno = ts.lineno;
    int afterComma = -1;
    List<ObjectProperty> elems = new ArrayList();
    Set<String> getterNames = null;
    Set<String> setterNames = null;
    if (inUseStrictDirective) {
      getterNames = new java.util.HashSet();
      setterNames = new java.util.HashSet();
    }
    Comment objJsdocNode = getAndResetJsDoc();
    for (;;)
    {
      String propertyName = null;
      int entryKind = 1;
      int tt = peekToken();
      Comment jsdocNode = getAndResetJsDoc();
      switch (tt) {
      case 39: 
        Name name = createNameNode();
        propertyName = ts.getString();
        int ppos = ts.tokenBeg;
        consumeToken();
        








        int peeked = peekToken();
        
        boolean maybeGetterOrSetter = ("get".equals(propertyName)) || ("set".equals(propertyName));
        if ((maybeGetterOrSetter) && (peeked != 89) && (peeked != 103) && (peeked != 86))
        {
          boolean isGet = "get".equals(propertyName);
          entryKind = isGet ? 2 : 4;
          AstNode pname = objliteralProperty();
          if (pname == null) {
            propertyName = null;
          } else {
            propertyName = ts.getString();
            ObjectProperty objectProp = getterSetterProperty(ppos, pname, isGet);
            
            pname.setJsDocNode(jsdocNode);
            elems.add(objectProp);
          }
        } else {
          name.setJsDocNode(jsdocNode);
          elems.add(plainProperty(name, tt));
        }
        break;
      
      case 86: 
        if (afterComma == -1) break label562;
        warnTrailingComma(pos, elems, afterComma); break;
      

      default: 
        AstNode pname = objliteralProperty();
        if (pname == null) {
          propertyName = null;
        } else {
          propertyName = ts.getString();
          pname.setJsDocNode(jsdocNode);
          elems.add(plainProperty(pname, tt));
        }
        break;
      }
      
      if ((inUseStrictDirective) && (propertyName != null)) {
        switch (entryKind) {
        case 1: 
          if ((getterNames.contains(propertyName)) || 
            (setterNames.contains(propertyName))) {
            addError("msg.dup.obj.lit.prop.strict", propertyName);
          }
          getterNames.add(propertyName);
          setterNames.add(propertyName);
          break;
        case 2: 
          if (getterNames.contains(propertyName)) {
            addError("msg.dup.obj.lit.prop.strict", propertyName);
          }
          getterNames.add(propertyName);
          break;
        case 4: 
          if (setterNames.contains(propertyName)) {
            addError("msg.dup.obj.lit.prop.strict", propertyName);
          }
          setterNames.add(propertyName);
        }
        
      }
      

      getAndResetJsDoc();
      
      if (!matchToken(89)) break;
      afterComma = ts.tokenEnd;
    }
    

    label562:
    
    mustMatchToken(86, "msg.no.brace.prop");
    ObjectLiteral pn = new ObjectLiteral(pos, ts.tokenEnd - pos);
    if (objJsdocNode != null) {
      pn.setJsDocNode(objJsdocNode);
    }
    pn.setElements(elems);
    pn.setLineno(lineno);
    return pn;
  }
  
  private AstNode objliteralProperty() throws IOException
  {
    int tt = peekToken();
    AstNode pname; AstNode pname; AstNode pname; switch (tt) {
    case 39: 
      pname = createNameNode();
      break;
    
    case 41: 
      pname = createStringLiteral();
      break;
    

    case 40: 
      pname = new net.sourceforge.htmlunit.corejs.javascript.ast.NumberLiteral(ts.tokenBeg, ts.getString(), ts.getNumber());
      break;
    default: 
      AstNode pname;
      if ((compilerEnv.isReservedKeywordAsIdentifier()) && 
        (TokenStream.isKeyword(ts.getString())))
      {
        pname = createNameNode();
      }
      else {
        reportError("msg.bad.prop");
        return null; }
      break; }
    AstNode pname;
    consumeToken();
    return pname;
  }
  

  private ObjectProperty plainProperty(AstNode property, int ptt)
    throws IOException
  {
    int tt = peekToken();
    if (((tt == 89) || (tt == 86)) && (ptt == 39) && 
      (compilerEnv.getLanguageVersion() >= 180)) {
      if (!inDestructuringAssignment) {
        reportError("msg.bad.object.init");
      }
      AstNode nn = new Name(property.getPosition(), property.getString());
      ObjectProperty pn = new ObjectProperty();
      pn.putProp(26, Boolean.TRUE);
      pn.setLeftAndRight(property, nn);
      return pn;
    }
    mustMatchToken(103, "msg.no.colon.prop");
    ObjectProperty pn = new ObjectProperty();
    pn.setOperatorPosition(ts.tokenBeg);
    pn.setLeftAndRight(property, assignExpr());
    return pn;
  }
  
  private ObjectProperty getterSetterProperty(int pos, AstNode propName, boolean isGetter) throws IOException
  {
    FunctionNode fn = function(2);
    
    Name name = fn.getFunctionName();
    if ((name != null) && (name.length() != 0)) {
      reportError("msg.bad.prop");
    }
    ObjectProperty pn = new ObjectProperty(pos);
    if (isGetter) {
      pn.setIsGetter();
      fn.setFunctionIsGetter();
    } else {
      pn.setIsSetter();
      fn.setFunctionIsSetter();
    }
    int end = getNodeEnd(fn);
    pn.setLeft(propName);
    pn.setRight(fn);
    pn.setLength(end - pos);
    return pn;
  }
  
  private Name createNameNode() {
    return createNameNode(false, 39);
  }
  





  private Name createNameNode(boolean checkActivation, int token)
  {
    int beg = ts.tokenBeg;
    String s = ts.getString();
    int lineno = ts.lineno;
    if (!"".equals(prevNameTokenString)) {
      beg = prevNameTokenStart;
      s = prevNameTokenString;
      lineno = prevNameTokenLineno;
      prevNameTokenStart = 0;
      prevNameTokenString = "";
      prevNameTokenLineno = 0;
    }
    if (s == null) {
      if (compilerEnv.isIdeMode()) {
        s = "";
      } else {
        codeBug();
      }
    }
    Name name = new Name(beg, s);
    name.setLineno(lineno);
    if (checkActivation) {
      checkActivationName(s, token);
    }
    return name;
  }
  
  private StringLiteral createStringLiteral() {
    int pos = ts.tokenBeg;int end = ts.tokenEnd;
    StringLiteral s = new StringLiteral(pos, end - pos);
    s.setLineno(ts.lineno);
    s.setValue(ts.getString());
    s.setQuoteCharacter(ts.getQuoteChar());
    return s;
  }
  
  protected void checkActivationName(String name, int token) {
    if (!insideFunction()) {
      return;
    }
    boolean activation = false;
    if (("arguments".equals(name)) || (
      (compilerEnv.getActivationNames() != null) && 
      (compilerEnv.getActivationNames().contains(name)))) {
      activation = true;
    } else if (("length".equals(name)) && 
      (token == 33) && 
      (compilerEnv.getLanguageVersion() == 120))
    {
      activation = true;
    }
    
    if (activation) {
      setRequiresActivation();
    }
  }
  
  protected void setRequiresActivation() {
    if (insideFunction()) {
      ((FunctionNode)currentScriptOrFn).setRequiresActivation();
    }
  }
  
  private void checkCallRequiresActivation(AstNode pn) {
    if (((pn.getType() == 39) && 
      ("eval".equals(((Name)pn).getIdentifier()))) || (
      (pn.getType() == 33) && ("eval".equals(((PropertyGet)pn)
      .getProperty().getIdentifier()))))
      setRequiresActivation();
  }
  
  protected void setIsGenerator() {
    if (insideFunction()) {
      ((FunctionNode)currentScriptOrFn).setIsGenerator();
    }
  }
  
  private void checkBadIncDec(UnaryExpression expr) {
    AstNode op = removeParens(expr.getOperand());
    int tt = op.getType();
    if ((tt != 39) && (tt != 33) && (tt != 36) && (tt != 67) && (tt != 38))
    {
      reportError(expr.getType() == 106 ? "msg.bad.incr" : "msg.bad.decr");
    }
  }
  
  private net.sourceforge.htmlunit.corejs.javascript.ast.ErrorNode makeErrorNode() {
    net.sourceforge.htmlunit.corejs.javascript.ast.ErrorNode pn = new net.sourceforge.htmlunit.corejs.javascript.ast.ErrorNode(ts.tokenBeg, ts.tokenEnd - ts.tokenBeg);
    pn.setLineno(ts.lineno);
    return pn;
  }
  
  private int nodeEnd(AstNode node)
  {
    return node.getPosition() + node.getLength();
  }
  
  private void saveNameTokenData(int pos, String name, int lineno) {
    prevNameTokenStart = pos;
    prevNameTokenString = name;
    prevNameTokenLineno = lineno;
  }
  













  private int lineBeginningFor(int pos)
  {
    if (sourceChars == null) {
      return -1;
    }
    if (pos <= 0) {
      return 0;
    }
    char[] buf = sourceChars;
    if (pos >= buf.length)
      pos = buf.length - 1;
    for (;;) {
      pos--; if (pos < 0) break;
      char c = buf[pos];
      if (ScriptRuntime.isJSLineTerminator(c)) {
        return pos + 1;
      }
    }
    return 0;
  }
  


  private void warnMissingSemi(int pos, int end)
  {
    if (compilerEnv.isStrictMode()) {
      int[] linep = new int[2];
      String line = ts.getLine(end, linep);
      


      int beg = compilerEnv.isIdeMode() ? Math.max(pos, end - linep[1]) : pos;
      
      if (line != null) {
        addStrictWarning("msg.missing.semi", "", beg, end - beg, linep[0], line, linep[1]);
      }
      else
      {
        addStrictWarning("msg.missing.semi", "", beg, end - beg);
      }
    }
  }
  
  private void warnTrailingComma(int pos, List<?> elems, int commaPos) {
    if (compilerEnv.getWarnTrailingComma())
    {
      if (!elems.isEmpty()) {
        pos = ((AstNode)elems.get(0)).getPosition();
      }
      pos = Math.max(pos, lineBeginningFor(commaPos));
      addWarning("msg.extra.trailing.comma", pos, commaPos - pos);
    }
  }
  
  private String readFully(java.io.Reader reader) throws IOException {
    java.io.BufferedReader in = new java.io.BufferedReader(reader);
    try {
      char[] cbuf = new char['Ѐ'];
      StringBuilder sb = new StringBuilder(1024);
      int bytes_read;
      while ((bytes_read = in.read(cbuf, 0, 1024)) != -1) {
        sb.append(cbuf, 0, bytes_read);
      }
      return sb.toString();
    } finally {
      in.close();
    }
  }
  
  protected class PerFunctionVariables
  {
    private ScriptNode savedCurrentScriptOrFn;
    private Scope savedCurrentScope;
    private int savedEndFlags;
    private boolean savedInForInit;
    private Map<String, LabeledStatement> savedLabelSet;
    private List<Loop> savedLoopSet;
    private List<net.sourceforge.htmlunit.corejs.javascript.ast.Jump> savedLoopAndSwitchSet;
    
    PerFunctionVariables(FunctionNode fnNode) {
      savedCurrentScriptOrFn = currentScriptOrFn;
      currentScriptOrFn = fnNode;
      
      savedCurrentScope = currentScope;
      currentScope = fnNode;
      
      savedLabelSet = labelSet;
      labelSet = null;
      
      savedLoopSet = loopSet;
      loopSet = null;
      
      savedLoopAndSwitchSet = loopAndSwitchSet;
      loopAndSwitchSet = null;
      
      savedEndFlags = endFlags;
      endFlags = 0;
      
      savedInForInit = inForInit;
      inForInit = false;
    }
    
    void restore() {
      currentScriptOrFn = savedCurrentScriptOrFn;
      currentScope = savedCurrentScope;
      labelSet = savedLabelSet;
      loopSet = savedLoopSet;
      loopAndSwitchSet = savedLoopAndSwitchSet;
      endFlags = savedEndFlags;
      inForInit = savedInForInit;
    }
  }
  















  Node createDestructuringAssignment(int type, Node left, Node right)
  {
    String tempName = currentScriptOrFn.getNextTempName();
    Node result = destructuringAssignmentHelper(type, left, right, tempName);
    
    Node comma = result.getLastChild();
    comma.addChildToBack(createName(tempName));
    return result;
  }
  
  Node destructuringAssignmentHelper(int variableType, Node left, Node right, String tempName)
  {
    Scope result = createScopeNode(158, left.getLineno());
    result.addChildToFront(new Node(153, 
      createName(39, tempName, right)));
    try {
      pushScope(result);
      defineSymbol(153, tempName, true);
    } finally {
      popScope();
    }
    Node comma = new Node(89);
    result.addChildToBack(comma);
    List<String> destructuringNames = new ArrayList();
    boolean empty = true;
    switch (left.getType()) {
    case 65: 
      empty = destructuringArray((ArrayLiteral)left, variableType, tempName, comma, destructuringNames);
      
      break;
    case 66: 
      empty = destructuringObject((ObjectLiteral)left, variableType, tempName, comma, destructuringNames);
      
      break;
    case 33: 
    case 36: 
      switch (variableType) {
      case 122: 
      case 153: 
      case 154: 
        reportError("msg.bad.assign.left");
      }
      comma.addChildToBack(simpleAssignment(left, createName(tempName)));
      break;
    default: 
      reportError("msg.bad.assign.left");
    }
    if (empty)
    {
      comma.addChildToBack(createNumber(0.0D));
    }
    result.putProp(22, destructuringNames);
    return result;
  }
  
  boolean destructuringArray(ArrayLiteral array, int variableType, String tempName, Node parent, List<String> destructuringNames)
  {
    boolean empty = true;
    int setOp = variableType == 154 ? 155 : 8;
    
    int index = 0;
    for (AstNode n : array.getElements())
      if (n.getType() == 128) {
        index++;
      }
      else
      {
        Node rightElem = new Node(36, createName(tempName), createNumber(index));
        if (n.getType() == 39) {
          String name = n.getString();
          parent.addChildToBack(new Node(setOp, 
            createName(49, name, null), rightElem));
          if (variableType != -1) {
            defineSymbol(variableType, name, true);
            destructuringNames.add(name);
          }
        } else {
          parent.addChildToBack(destructuringAssignmentHelper(variableType, n, rightElem, currentScriptOrFn
          
            .getNextTempName()));
        }
        index++;
        empty = false;
      }
    return empty;
  }
  
  boolean destructuringObject(ObjectLiteral node, int variableType, String tempName, Node parent, List<String> destructuringNames)
  {
    boolean empty = true;
    int setOp = variableType == 154 ? 155 : 8;
    

    for (ObjectProperty prop : node.getElements()) {
      int lineno = 0;
      


      if (ts != null) {
        lineno = ts.lineno;
      }
      AstNode id = prop.getLeft();
      Node rightElem = null;
      if ((id instanceof Name)) {
        Node s = Node.newString(((Name)id).getIdentifier());
        rightElem = new Node(33, createName(tempName), s);
      } else if ((id instanceof StringLiteral)) {
        Node s = Node.newString(((StringLiteral)id).getValue());
        rightElem = new Node(33, createName(tempName), s);
      } else if ((id instanceof net.sourceforge.htmlunit.corejs.javascript.ast.NumberLiteral)) {
        Node s = createNumber((int)((net.sourceforge.htmlunit.corejs.javascript.ast.NumberLiteral)id).getNumber());
        rightElem = new Node(36, createName(tempName), s);
      } else {
        throw codeBug();
      }
      rightElem.setLineno(lineno);
      AstNode value = prop.getRight();
      if (value.getType() == 39) {
        String name = ((Name)value).getIdentifier();
        parent.addChildToBack(new Node(setOp, 
          createName(49, name, null), rightElem));
        if (variableType != -1) {
          defineSymbol(variableType, name, true);
          destructuringNames.add(name);
        }
      } else {
        parent.addChildToBack(destructuringAssignmentHelper(variableType, value, rightElem, currentScriptOrFn
        
          .getNextTempName()));
      }
      empty = false;
    }
    return empty;
  }
  
  protected Node createName(String name) {
    checkActivationName(name, 39);
    return Node.newString(39, name);
  }
  
  protected Node createName(int type, String name, Node child) {
    Node result = createName(name);
    result.setType(type);
    if (child != null)
      result.addChildToBack(child);
    return result;
  }
  
  protected Node createNumber(double number) {
    return Node.newNumber(number);
  }
  









  protected Scope createScopeNode(int token, int lineno)
  {
    Scope scope = new Scope();
    scope.setType(token);
    scope.setLineno(lineno);
    return scope;
  }
  





















  protected Node simpleAssignment(Node left, Node right)
  {
    int nodeType = left.getType();
    switch (nodeType) {
    case 39: 
      if ((inUseStrictDirective) && 
        ("eval".equals(((Name)left).getIdentifier()))) {
        reportError("msg.bad.id.strict", ((Name)left).getIdentifier());
      }
      left.setType(49);
      return new Node(8, left, right);
    case 33: 
    case 36: 
      Node id;
      
      Node obj;
      
      Node id;
      
      if ((left instanceof PropertyGet)) {
        Node obj = ((PropertyGet)left).getTarget();
        id = ((PropertyGet)left).getProperty(); } else { Node id;
        if ((left instanceof ElementGet)) {
          Node obj = ((ElementGet)left).getTarget();
          id = ((ElementGet)left).getElement();
        }
        else {
          obj = left.getFirstChild();
          id = left.getLastChild();
        } }
      int type;
      if (nodeType == 33) {
        int type = 35;
        







        id.setType(41);
      } else {
        type = 37;
      }
      return new Node(type, obj, id, right);
    
    case 67: 
      Node ref = left.getFirstChild();
      checkMutableReference(ref);
      return new Node(68, ref, right);
    }
    
    
    throw codeBug();
  }
  
  protected void checkMutableReference(Node n) {
    int memberTypeFlags = n.getIntProp(16, 0);
    if ((memberTypeFlags & 0x4) != 0) {
      reportError("msg.bad.assign.left");
    }
  }
  
  protected AstNode removeParens(AstNode node)
  {
    while ((node instanceof ParenthesizedExpression)) {
      node = ((ParenthesizedExpression)node).getExpression();
    }
    return node;
  }
  
  void markDestructuring(AstNode node) {
    if ((node instanceof net.sourceforge.htmlunit.corejs.javascript.ast.DestructuringForm)) {
      ((net.sourceforge.htmlunit.corejs.javascript.ast.DestructuringForm)node).setIsDestructuring(true);
    } else if ((node instanceof ParenthesizedExpression)) {
      markDestructuring(((ParenthesizedExpression)node).getExpression());
    }
  }
  
  private RuntimeException codeBug() throws RuntimeException
  {
    throw Kit.codeBug("ts.cursor=" + ts.cursor + ", ts.tokenBeg=" + ts.tokenBeg + ", currentToken=" + currentToken);
  }
}
