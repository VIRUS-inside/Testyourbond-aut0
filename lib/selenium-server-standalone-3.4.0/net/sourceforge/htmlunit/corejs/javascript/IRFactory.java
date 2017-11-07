package net.sourceforge.htmlunit.corejs.javascript;

import java.util.List;
import net.sourceforge.htmlunit.corejs.javascript.ast.ArrayComprehension;
import net.sourceforge.htmlunit.corejs.javascript.ast.ArrayLiteral;
import net.sourceforge.htmlunit.corejs.javascript.ast.Assignment;
import net.sourceforge.htmlunit.corejs.javascript.ast.AstNode;
import net.sourceforge.htmlunit.corejs.javascript.ast.ElementGet;
import net.sourceforge.htmlunit.corejs.javascript.ast.FunctionNode;
import net.sourceforge.htmlunit.corejs.javascript.ast.GeneratorExpression;
import net.sourceforge.htmlunit.corejs.javascript.ast.Jump;
import net.sourceforge.htmlunit.corejs.javascript.ast.LetNode;
import net.sourceforge.htmlunit.corejs.javascript.ast.Name;
import net.sourceforge.htmlunit.corejs.javascript.ast.ObjectProperty;
import net.sourceforge.htmlunit.corejs.javascript.ast.PropertyGet;
import net.sourceforge.htmlunit.corejs.javascript.ast.Scope;
import net.sourceforge.htmlunit.corejs.javascript.ast.ScriptNode;
import net.sourceforge.htmlunit.corejs.javascript.ast.StringLiteral;
import net.sourceforge.htmlunit.corejs.javascript.ast.UnaryExpression;
import net.sourceforge.htmlunit.corejs.javascript.ast.VariableDeclaration;

public final class IRFactory extends Parser
{
  private static final int LOOP_DO_WHILE = 0;
  private static final int LOOP_WHILE = 1;
  private static final int LOOP_FOR = 2;
  private static final int ALWAYS_TRUE_BOOLEAN = 1;
  private static final int ALWAYS_FALSE_BOOLEAN = -1;
  private Decompiler decompiler = new Decompiler();
  

  public IRFactory() {}
  
  public IRFactory(CompilerEnvirons env)
  {
    this(env, env.getErrorReporter());
  }
  
  public IRFactory(CompilerEnvirons env, ErrorReporter errorReporter) {
    super(env, errorReporter);
  }
  



  public ScriptNode transformTree(net.sourceforge.htmlunit.corejs.javascript.ast.AstRoot root)
  {
    currentScriptOrFn = root;
    inUseStrictDirective = root.isInStrictMode();
    int sourceStartOffset = decompiler.getCurrentOffset();
    




    ScriptNode script = (ScriptNode)transform(root);
    
    int sourceEndOffset = decompiler.getCurrentOffset();
    script.setEncodedSourceBounds(sourceStartOffset, sourceEndOffset);
    
    if (compilerEnv.isGeneratingSource()) {
      script.setEncodedSource(decompiler.getEncodedSource());
    }
    
    decompiler = null;
    return script;
  }
  



  public Node transform(AstNode node)
  {
    switch (node.getType()) {
    case 157: 
      return transformArrayComp((ArrayComprehension)node);
    case 65: 
      return transformArrayLiteral((ArrayLiteral)node);
    case 129: 
      return transformBlock(node);
    case 120: 
      return transformBreak((net.sourceforge.htmlunit.corejs.javascript.ast.BreakStatement)node);
    case 38: 
      return transformFunctionCall((net.sourceforge.htmlunit.corejs.javascript.ast.FunctionCall)node);
    case 121: 
      return transformContinue((net.sourceforge.htmlunit.corejs.javascript.ast.ContinueStatement)node);
    case 118: 
      return transformDoLoop((net.sourceforge.htmlunit.corejs.javascript.ast.DoLoop)node);
    case 128: 
      return node;
    case 119: 
      if ((node instanceof net.sourceforge.htmlunit.corejs.javascript.ast.ForInLoop)) {
        return transformForInLoop((net.sourceforge.htmlunit.corejs.javascript.ast.ForInLoop)node);
      }
      return transformForLoop((net.sourceforge.htmlunit.corejs.javascript.ast.ForLoop)node);
    
    case 109: 
      return transformFunction((FunctionNode)node);
    case 162: 
      return transformGenExpr((GeneratorExpression)node);
    case 36: 
      return transformElementGet((ElementGet)node);
    case 33: 
      return transformPropertyGet((PropertyGet)node);
    case 102: 
      return transformCondExpr((net.sourceforge.htmlunit.corejs.javascript.ast.ConditionalExpression)node);
    case 112: 
      return transformIf((net.sourceforge.htmlunit.corejs.javascript.ast.IfStatement)node);
    
    case 42: 
    case 43: 
    case 44: 
    case 45: 
    case 160: 
      return transformLiteral(node);
    
    case 39: 
      return transformName((Name)node);
    case 40: 
      return transformNumber((net.sourceforge.htmlunit.corejs.javascript.ast.NumberLiteral)node);
    case 30: 
      return transformNewExpr((net.sourceforge.htmlunit.corejs.javascript.ast.NewExpression)node);
    case 66: 
      return transformObjectLiteral((net.sourceforge.htmlunit.corejs.javascript.ast.ObjectLiteral)node);
    case 48: 
      return transformRegExp((net.sourceforge.htmlunit.corejs.javascript.ast.RegExpLiteral)node);
    case 4: 
      return transformReturn((net.sourceforge.htmlunit.corejs.javascript.ast.ReturnStatement)node);
    case 136: 
      return transformScript((ScriptNode)node);
    case 41: 
      return transformString((StringLiteral)node);
    case 114: 
      return transformSwitch((net.sourceforge.htmlunit.corejs.javascript.ast.SwitchStatement)node);
    case 50: 
      return transformThrow((net.sourceforge.htmlunit.corejs.javascript.ast.ThrowStatement)node);
    case 81: 
      return transformTry((net.sourceforge.htmlunit.corejs.javascript.ast.TryStatement)node);
    case 117: 
      return transformWhileLoop((net.sourceforge.htmlunit.corejs.javascript.ast.WhileLoop)node);
    case 123: 
      return transformWith((net.sourceforge.htmlunit.corejs.javascript.ast.WithStatement)node);
    case 72: 
      return transformYield((net.sourceforge.htmlunit.corejs.javascript.ast.Yield)node);
    }
    if ((node instanceof net.sourceforge.htmlunit.corejs.javascript.ast.ExpressionStatement)) {
      return transformExprStmt((net.sourceforge.htmlunit.corejs.javascript.ast.ExpressionStatement)node);
    }
    if ((node instanceof Assignment)) {
      return transformAssignment((Assignment)node);
    }
    if ((node instanceof UnaryExpression)) {
      return transformUnary((UnaryExpression)node);
    }
    if ((node instanceof net.sourceforge.htmlunit.corejs.javascript.ast.XmlMemberGet)) {
      return transformXmlMemberGet((net.sourceforge.htmlunit.corejs.javascript.ast.XmlMemberGet)node);
    }
    if ((node instanceof net.sourceforge.htmlunit.corejs.javascript.ast.InfixExpression)) {
      return transformInfix((net.sourceforge.htmlunit.corejs.javascript.ast.InfixExpression)node);
    }
    if ((node instanceof VariableDeclaration)) {
      return transformVariables((VariableDeclaration)node);
    }
    if ((node instanceof net.sourceforge.htmlunit.corejs.javascript.ast.ParenthesizedExpression)) {
      return transformParenExpr((net.sourceforge.htmlunit.corejs.javascript.ast.ParenthesizedExpression)node);
    }
    if ((node instanceof net.sourceforge.htmlunit.corejs.javascript.ast.LabeledStatement)) {
      return transformLabeledStatement((net.sourceforge.htmlunit.corejs.javascript.ast.LabeledStatement)node);
    }
    if ((node instanceof LetNode)) {
      return transformLetNode((LetNode)node);
    }
    if ((node instanceof net.sourceforge.htmlunit.corejs.javascript.ast.XmlRef)) {
      return transformXmlRef((net.sourceforge.htmlunit.corejs.javascript.ast.XmlRef)node);
    }
    if ((node instanceof net.sourceforge.htmlunit.corejs.javascript.ast.XmlLiteral)) {
      return transformXmlLiteral((net.sourceforge.htmlunit.corejs.javascript.ast.XmlLiteral)node);
    }
    throw new IllegalArgumentException("Can't transform: " + node);
  }
  




















  private Node transformArrayComp(ArrayComprehension node)
  {
    int lineno = node.getLineno();
    Scope scopeNode = createScopeNode(157, lineno);
    String arrayName = currentScriptOrFn.getNextTempName();
    pushScope(scopeNode);
    try {
      defineSymbol(153, arrayName, false);
      Node block = new Node(129, lineno);
      Node newArray = createCallOrNew(30, createName("Array"));
      Node init = new Node(133, createAssignment(90, 
        createName(arrayName), newArray), lineno);
      block.addChildToBack(init);
      block.addChildToBack(arrayCompTransformHelper(node, arrayName));
      scopeNode.addChildToBack(block);
      scopeNode.addChildToBack(createName(arrayName));
      return scopeNode;
    } finally {
      popScope();
    }
  }
  
  private Node arrayCompTransformHelper(ArrayComprehension node, String arrayName)
  {
    decompiler.addToken(83);
    int lineno = node.getLineno();
    Node expr = transform(node.getResult());
    
    List<net.sourceforge.htmlunit.corejs.javascript.ast.ArrayComprehensionLoop> loops = node.getLoops();
    int numLoops = loops.size();
    

    Node[] iterators = new Node[numLoops];
    Node[] iteratedObjs = new Node[numLoops];
    
    for (int i = 0; i < numLoops; i++) {
      net.sourceforge.htmlunit.corejs.javascript.ast.ArrayComprehensionLoop acl = (net.sourceforge.htmlunit.corejs.javascript.ast.ArrayComprehensionLoop)loops.get(i);
      decompiler.addName(" ");
      decompiler.addToken(119);
      if (acl.isForEach()) {
        decompiler.addName("each ");
      }
      decompiler.addToken(87);
      
      AstNode iter = acl.getIterator();
      String name = null;
      if (iter.getType() == 39) {
        name = iter.getString();
        decompiler.addName(name);
      }
      else {
        decompile(iter);
        name = currentScriptOrFn.getNextTempName();
        defineSymbol(87, name, false);
        expr = createBinary(89, 
          createAssignment(90, iter, createName(name)), expr);
      }
      
      Node init = createName(name);
      

      defineSymbol(153, name, false);
      iterators[i] = init;
      
      decompiler.addToken(52);
      iteratedObjs[i] = transform(acl.getIteratedObject());
      decompiler.addToken(88);
    }
    

    Node call = createCallOrNew(38, 
      createPropertyGet(createName(arrayName), null, "push", 0));
    
    Node body = new Node(133, call, lineno);
    
    if (node.getFilter() != null) {
      decompiler.addName(" ");
      decompiler.addToken(112);
      decompiler.addToken(87);
      body = createIf(transform(node.getFilter()), body, null, lineno);
      decompiler.addToken(88);
    }
    

    int pushed = 0;
    try {
      for (int i = numLoops - 1; i >= 0; i--) {
        net.sourceforge.htmlunit.corejs.javascript.ast.ArrayComprehensionLoop acl = (net.sourceforge.htmlunit.corejs.javascript.ast.ArrayComprehensionLoop)loops.get(i);
        Scope loop = createLoopNode(null, acl
          .getLineno());
        pushScope(loop);
        pushed++;
        body = createForIn(153, loop, iterators[i], iteratedObjs[i], body, acl
          .isForEach());
      }
    } finally { int i;
      for (int i = 0; i < pushed; i++) {
        popScope();
      }
    }
    
    decompiler.addToken(84);
    


    call.addChildToBack(expr);
    return body;
  }
  
  private Node transformArrayLiteral(ArrayLiteral node) {
    if (node.isDestructuring()) {
      return node;
    }
    decompiler.addToken(83);
    List<AstNode> elems = node.getElements();
    Node array = new Node(65);
    List<Integer> skipIndexes = null;
    for (int i = 0; i < elems.size(); i++) {
      AstNode elem = (AstNode)elems.get(i);
      if (elem.getType() != 128) {
        array.addChildToBack(transform(elem));
      } else {
        if (skipIndexes == null) {
          skipIndexes = new java.util.ArrayList();
        }
        skipIndexes.add(Integer.valueOf(i));
      }
      if (i < elems.size() - 1)
        decompiler.addToken(89);
    }
    decompiler.addToken(84);
    array.putIntProp(21, node
      .getDestructuringLength());
    if (skipIndexes != null) {
      int[] skips = new int[skipIndexes.size()];
      for (int i = 0; i < skipIndexes.size(); i++)
        skips[i] = ((Integer)skipIndexes.get(i)).intValue();
      array.putProp(11, skips);
    }
    return array;
  }
  
  private Node transformAssignment(Assignment node) {
    AstNode left = removeParens(node.getLeft());
    left = transformAssignmentLeft(node, left);
    Node target = null;
    if (isDestructuring(left)) {
      decompile(left);
      target = left;
    } else {
      target = transform(left);
    }
    decompiler.addToken(node.getType());
    return createAssignment(node.getType(), target, 
      transform(node.getRight()));
  }
  
  private AstNode transformAssignmentLeft(Assignment node, AstNode left) {
    AstNode right = node.getRight();
    
    if ((right.getType() == 42) && (node.getType() == 90) && ((left instanceof Name)) && ((right instanceof net.sourceforge.htmlunit.corejs.javascript.ast.KeywordLiteral)))
    {
      if (Context.getCurrentContext().hasFeature(103))
      {

        String identifier = ((Name)left).getIdentifier();
        for (AstNode p = node.getParent(); p != null; p = p.getParent())
          if ((p instanceof FunctionNode))
          {
            Name functionName = ((FunctionNode)p).getFunctionName();
            if ((functionName != null) && 
              (functionName.getIdentifier().equals(identifier))) {
              PropertyGet propertyGet = new PropertyGet();
              net.sourceforge.htmlunit.corejs.javascript.ast.KeywordLiteral thisKeyword = new net.sourceforge.htmlunit.corejs.javascript.ast.KeywordLiteral();
              thisKeyword.setType(43);
              propertyGet.setLeft(thisKeyword);
              propertyGet.setRight(left);
              node.setLeft(propertyGet);
              return propertyGet;
            }
          }
      }
    }
    return left;
  }
  
  private Node transformBlock(AstNode node) {
    if ((node instanceof Scope)) {
      pushScope((Scope)node);
    }
    try {
      List<Node> kids = new java.util.ArrayList();
      for (Object localObject1 = node.iterator(); ((java.util.Iterator)localObject1).hasNext();) { Node kid = (Node)((java.util.Iterator)localObject1).next();
        kids.add(transform((AstNode)kid));
      }
      node.removeChildren();
      for (localObject1 = kids.iterator(); ((java.util.Iterator)localObject1).hasNext();) { Node kid = (Node)((java.util.Iterator)localObject1).next();
        node.addChildToBack(kid);
      }
      return node;
    } finally {
      if ((node instanceof Scope)) {
        popScope();
      }
    }
  }
  
  private Node transformBreak(net.sourceforge.htmlunit.corejs.javascript.ast.BreakStatement node) {
    decompiler.addToken(120);
    if (node.getBreakLabel() != null) {
      decompiler.addName(node.getBreakLabel().getIdentifier());
    }
    decompiler.addEOL(82);
    return node;
  }
  
  private Node transformCondExpr(net.sourceforge.htmlunit.corejs.javascript.ast.ConditionalExpression node) {
    Node test = transform(node.getTestExpression());
    decompiler.addToken(102);
    Node ifTrue = transform(node.getTrueExpression());
    decompiler.addToken(103);
    Node ifFalse = transform(node.getFalseExpression());
    return createCondExpr(test, ifTrue, ifFalse);
  }
  
  private Node transformContinue(net.sourceforge.htmlunit.corejs.javascript.ast.ContinueStatement node) {
    decompiler.addToken(121);
    if (node.getLabel() != null) {
      decompiler.addName(node.getLabel().getIdentifier());
    }
    decompiler.addEOL(82);
    return node;
  }
  
  private Node transformDoLoop(net.sourceforge.htmlunit.corejs.javascript.ast.DoLoop loop) {
    loop.setType(132);
    pushScope(loop);
    try {
      decompiler.addToken(118);
      decompiler.addEOL(85);
      Node body = transform(loop.getBody());
      decompiler.addToken(86);
      decompiler.addToken(117);
      decompiler.addToken(87);
      Node cond = transform(loop.getCondition());
      decompiler.addToken(88);
      decompiler.addEOL(82);
      return createLoop(loop, 0, body, cond, null, null);
    } finally {
      popScope();
    }
  }
  
  private Node transformElementGet(ElementGet node)
  {
    if ((getElementtype == 41) && 
      ("eval".equals(((StringLiteral)node.getElement()).getValue()))) {
      PropertyGet propertyGet = new PropertyGet();
      propertyGet.setLeft(node.getTarget());
      Name name = new Name();
      name.setIdentifier("eval");
      propertyGet.setRight(name);
      return transform(propertyGet);
    }
    

    Node target = transform(node.getTarget());
    decompiler.addToken(83);
    Node element = transform(node.getElement());
    decompiler.addToken(84);
    return new Node(36, target, element);
  }
  
  private Node transformExprStmt(net.sourceforge.htmlunit.corejs.javascript.ast.ExpressionStatement node) {
    Node expr = transform(node.getExpression());
    decompiler.addEOL(82);
    return new Node(node.getType(), expr, node.getLineno());
  }
  
  private Node transformForInLoop(net.sourceforge.htmlunit.corejs.javascript.ast.ForInLoop loop) {
    decompiler.addToken(119);
    if (loop.isForEach())
      decompiler.addName("each ");
    decompiler.addToken(87);
    
    loop.setType(132);
    pushScope(loop);
    try {
      int declType = -1;
      AstNode iter = loop.getIterator();
      if ((iter instanceof VariableDeclaration)) {
        declType = ((VariableDeclaration)iter).getType();
      }
      Node lhs = transform(iter);
      decompiler.addToken(52);
      Node obj = transform(loop.getIteratedObject());
      decompiler.addToken(88);
      decompiler.addEOL(85);
      Node body = transform(loop.getBody());
      decompiler.addEOL(86);
      return createForIn(declType, loop, lhs, obj, body, loop
        .isForEach());
    } finally {
      popScope();
    }
  }
  
  private Node transformForLoop(net.sourceforge.htmlunit.corejs.javascript.ast.ForLoop loop) {
    decompiler.addToken(119);
    decompiler.addToken(87);
    loop.setType(132);
    

    Scope savedScope = currentScope;
    currentScope = loop;
    try {
      Node init = transform(loop.getInitializer());
      decompiler.addToken(82);
      Node test = transform(loop.getCondition());
      decompiler.addToken(82);
      Node incr = transform(loop.getIncrement());
      decompiler.addToken(88);
      decompiler.addEOL(85);
      Node body = transform(loop.getBody());
      decompiler.addEOL(86);
      return createFor(loop, init, test, incr, body);
    } finally {
      currentScope = savedScope;
    }
  }
  
  private Node transformFunction(FunctionNode fn) {
    int functionType = fn.getFunctionType();
    int start = decompiler.markFunctionStart(functionType);
    Node mexpr = decompileFunctionHeader(fn);
    int index = currentScriptOrFn.addFunction(fn);
    
    Parser.PerFunctionVariables savedVars = new Parser.PerFunctionVariables(this, fn);
    
    try
    {
      Node destructuring = (Node)fn.getProp(23);
      fn.removeProp(23);
      
      int lineno = fn.getBody().getLineno();
      nestingOfFunction += 1;
      Node body = transform(fn.getBody());
      
      if (!fn.isExpressionClosure()) {
        decompiler.addToken(86);
      }
      fn.setEncodedSourceBounds(start, decompiler.markFunctionEnd(start));
      
      if ((functionType != 2) && 
        (!fn.isExpressionClosure()))
      {

        decompiler.addToken(1);
      }
      
      if (destructuring != null) {
        body.addChildToFront(new Node(133, destructuring, lineno));
      }
      

      int syntheticType = fn.getFunctionType();
      Node pn = initFunction(fn, index, body, syntheticType);
      if (mexpr != null) {
        pn = createAssignment(90, mexpr, pn);
        if (syntheticType != 2) {
          pn = createExprStatementNoReturn(pn, fn.getLineno());
        }
      }
      return pn;
    }
    finally {
      nestingOfFunction -= 1;
      savedVars.restore();
    }
  }
  
  private Node transformFunctionCall(net.sourceforge.htmlunit.corejs.javascript.ast.FunctionCall node) {
    Node call = createCallOrNew(38, transform(node.getTarget()));
    call.setLineno(node.getLineno());
    decompiler.addToken(87);
    List<AstNode> args = node.getArguments();
    for (int i = 0; i < args.size(); i++) {
      AstNode arg = (AstNode)args.get(i);
      call.addChildToBack(transform(arg));
      if (i < args.size() - 1) {
        decompiler.addToken(89);
      }
    }
    decompiler.addToken(88);
    return call;
  }
  

  private Node transformGenExpr(GeneratorExpression node)
  {
    FunctionNode fn = new FunctionNode();
    fn.setSourceName(currentScriptOrFn.getNextTempName());
    fn.setIsGenerator();
    fn.setFunctionType(2);
    fn.setRequiresActivation();
    
    int functionType = fn.getFunctionType();
    int start = decompiler.markFunctionStart(functionType);
    Node mexpr = decompileFunctionHeader(fn);
    int index = currentScriptOrFn.addFunction(fn);
    
    Parser.PerFunctionVariables savedVars = new Parser.PerFunctionVariables(this, fn);
    
    try
    {
      Node destructuring = (Node)fn.getProp(23);
      fn.removeProp(23);
      
      int lineno = lineno;
      nestingOfFunction += 1;
      Node body = genExprTransformHelper(node);
      
      if (!fn.isExpressionClosure()) {
        decompiler.addToken(86);
      }
      fn.setEncodedSourceBounds(start, decompiler.markFunctionEnd(start));
      
      if ((functionType != 2) && 
        (!fn.isExpressionClosure()))
      {

        decompiler.addToken(1);
      }
      
      if (destructuring != null) {
        body.addChildToFront(new Node(133, destructuring, lineno));
      }
      

      int syntheticType = fn.getFunctionType();
      Node pn = initFunction(fn, index, body, syntheticType);
      if (mexpr != null) {
        pn = createAssignment(90, mexpr, pn);
        if (syntheticType != 2) {
          pn = createExprStatementNoReturn(pn, fn.getLineno());
        }
      }
    } finally {
      nestingOfFunction -= 1;
      savedVars.restore();
    }
    Node pn;
    Node call = createCallOrNew(38, pn);
    call.setLineno(node.getLineno());
    decompiler.addToken(87);
    decompiler.addToken(88);
    return call;
  }
  
  private Node genExprTransformHelper(GeneratorExpression node) {
    decompiler.addToken(87);
    int lineno = node.getLineno();
    Node expr = transform(node.getResult());
    
    List<net.sourceforge.htmlunit.corejs.javascript.ast.GeneratorExpressionLoop> loops = node.getLoops();
    int numLoops = loops.size();
    

    Node[] iterators = new Node[numLoops];
    Node[] iteratedObjs = new Node[numLoops];
    
    for (int i = 0; i < numLoops; i++) {
      net.sourceforge.htmlunit.corejs.javascript.ast.GeneratorExpressionLoop acl = (net.sourceforge.htmlunit.corejs.javascript.ast.GeneratorExpressionLoop)loops.get(i);
      decompiler.addName(" ");
      decompiler.addToken(119);
      decompiler.addToken(87);
      
      AstNode iter = acl.getIterator();
      String name = null;
      if (iter.getType() == 39) {
        name = iter.getString();
        decompiler.addName(name);
      }
      else {
        decompile(iter);
        name = currentScriptOrFn.getNextTempName();
        defineSymbol(87, name, false);
        expr = createBinary(89, 
          createAssignment(90, iter, createName(name)), expr);
      }
      
      Node init = createName(name);
      

      defineSymbol(153, name, false);
      iterators[i] = init;
      
      decompiler.addToken(52);
      iteratedObjs[i] = transform(acl.getIteratedObject());
      decompiler.addToken(88);
    }
    

    Node yield = new Node(72, expr, node.getLineno());
    
    Node body = new Node(133, yield, lineno);
    
    if (node.getFilter() != null) {
      decompiler.addName(" ");
      decompiler.addToken(112);
      decompiler.addToken(87);
      body = createIf(transform(node.getFilter()), body, null, lineno);
      decompiler.addToken(88);
    }
    

    int pushed = 0;
    try {
      for (int i = numLoops - 1; i >= 0; i--) {
        net.sourceforge.htmlunit.corejs.javascript.ast.GeneratorExpressionLoop acl = (net.sourceforge.htmlunit.corejs.javascript.ast.GeneratorExpressionLoop)loops.get(i);
        Scope loop = createLoopNode(null, acl
          .getLineno());
        pushScope(loop);
        pushed++;
        body = createForIn(153, loop, iterators[i], iteratedObjs[i], body, acl
          .isForEach());
      }
    } finally { int i;
      for (int i = 0; i < pushed; i++) {
        popScope();
      }
    }
    
    decompiler.addToken(88);
    
    return body;
  }
  
  private Node transformIf(net.sourceforge.htmlunit.corejs.javascript.ast.IfStatement n) {
    decompiler.addToken(112);
    decompiler.addToken(87);
    Node cond = transform(n.getCondition());
    decompiler.addToken(88);
    decompiler.addEOL(85);
    Node ifTrue = transform(n.getThenPart());
    Node ifFalse = null;
    if (n.getElsePart() != null) {
      decompiler.addToken(86);
      decompiler.addToken(113);
      decompiler.addEOL(85);
      ifFalse = transform(n.getElsePart());
    }
    decompiler.addEOL(86);
    return createIf(cond, ifTrue, ifFalse, n.getLineno());
  }
  
  private Node transformInfix(net.sourceforge.htmlunit.corejs.javascript.ast.InfixExpression node) {
    Node left = transform(node.getLeft());
    decompiler.addToken(node.getType());
    Node right = transform(node.getRight());
    if ((node instanceof net.sourceforge.htmlunit.corejs.javascript.ast.XmlDotQuery)) {
      decompiler.addToken(88);
    }
    return createBinary(node.getType(), left, right);
  }
  
  private Node transformLabeledStatement(net.sourceforge.htmlunit.corejs.javascript.ast.LabeledStatement ls) {
    net.sourceforge.htmlunit.corejs.javascript.ast.Label label = ls.getFirstLabel();
    List<net.sourceforge.htmlunit.corejs.javascript.ast.Label> labels = ls.getLabels();
    decompiler.addName(label.getName());
    if (labels.size() > 1)
    {
      for (net.sourceforge.htmlunit.corejs.javascript.ast.Label lb : labels.subList(1, labels.size())) {
        decompiler.addEOL(103);
        decompiler.addName(lb.getName());
      }
    }
    if (ls.getStatement().getType() == 129)
    {
      decompiler.addToken(66);
      decompiler.addEOL(85);
    } else {
      decompiler.addEOL(103);
    }
    Node statement = transform(ls.getStatement());
    if (ls.getStatement().getType() == 129) {
      decompiler.addEOL(86);
    }
    


    Node breakTarget = Node.newTarget();
    Node block = new Node(129, label, statement, breakTarget);
    target = breakTarget;
    
    return block;
  }
  
  private Node transformLetNode(LetNode node) {
    pushScope(node);
    try {
      decompiler.addToken(153);
      decompiler.addToken(87);
      Node vars = transformVariableInitializers(node.getVariables());
      decompiler.addToken(88);
      node.addChildToBack(vars);
      boolean letExpr = node.getType() == 158;
      if (node.getBody() != null) {
        if (letExpr) {
          decompiler.addName(" ");
        } else {
          decompiler.addEOL(85);
        }
        node.addChildToBack(transform(node.getBody()));
        if (!letExpr) {
          decompiler.addEOL(86);
        }
      }
      return node;
    } finally {
      popScope();
    }
  }
  
  private Node transformLiteral(AstNode node) {
    decompiler.addToken(node.getType());
    return node;
  }
  
  private Node transformName(Name node) {
    decompiler.addName(node.getIdentifier());
    return node;
  }
  
  private Node transformNewExpr(net.sourceforge.htmlunit.corejs.javascript.ast.NewExpression node) {
    decompiler.addToken(30);
    Node nx = createCallOrNew(30, transform(node.getTarget()));
    nx.setLineno(node.getLineno());
    List<AstNode> args = node.getArguments();
    decompiler.addToken(87);
    for (int i = 0; i < args.size(); i++) {
      AstNode arg = (AstNode)args.get(i);
      nx.addChildToBack(transform(arg));
      if (i < args.size() - 1) {
        decompiler.addToken(89);
      }
    }
    decompiler.addToken(88);
    if (node.getInitializer() != null) {
      nx.addChildToBack(transformObjectLiteral(node.getInitializer()));
    }
    return nx;
  }
  
  private Node transformNumber(net.sourceforge.htmlunit.corejs.javascript.ast.NumberLiteral node) {
    decompiler.addNumber(node.getNumber());
    return node;
  }
  
  private Node transformObjectLiteral(net.sourceforge.htmlunit.corejs.javascript.ast.ObjectLiteral node) {
    if (node.isDestructuring()) {
      return node;
    }
    


    decompiler.addToken(85);
    List<ObjectProperty> elems = node.getElements();
    Node object = new Node(66);
    Object[] properties;
    int size; int i; Object[] properties; if (elems.isEmpty()) {
      properties = ScriptRuntime.emptyArgs;
    } else {
      size = elems.size();i = 0;
      properties = new Object[size];
      for (ObjectProperty prop : elems) {
        if (prop.isGetter()) {
          decompiler.addToken(151);
        } else if (prop.isSetter()) {
          decompiler.addToken(152);
        }
        
        properties[(i++)] = getPropKey(prop.getLeft());
        


        if ((!prop.isGetter()) && (!prop.isSetter())) {
          decompiler.addToken(66);
        }
        
        Node right = transform(prop.getRight());
        if (prop.isGetter()) {
          right = createUnary(151, right);
        } else if (prop.isSetter()) {
          right = createUnary(152, right);
        }
        object.addChildToBack(right);
        
        if (i < size) {
          decompiler.addToken(89);
        }
      }
    }
    decompiler.addToken(86);
    object.putProp(12, properties);
    return object;
  }
  
  private Object getPropKey(Node id) {
    Object key;
    if ((id instanceof Name)) {
      String s = ((Name)id).getIdentifier();
      decompiler.addName(s);
      key = ScriptRuntime.getIndexObject(s); } else { Object key;
      if ((id instanceof StringLiteral)) {
        String s = ((StringLiteral)id).getValue();
        decompiler.addString(s);
        key = ScriptRuntime.getIndexObject(s); } else { Object key;
        if ((id instanceof net.sourceforge.htmlunit.corejs.javascript.ast.NumberLiteral)) {
          double n = ((net.sourceforge.htmlunit.corejs.javascript.ast.NumberLiteral)id).getNumber();
          decompiler.addNumber(n);
          key = ScriptRuntime.getIndexObject(n);
        } else {
          throw Kit.codeBug(); } } }
    Object key;
    return key;
  }
  
  private Node transformParenExpr(net.sourceforge.htmlunit.corejs.javascript.ast.ParenthesizedExpression node) {
    AstNode expr = node.getExpression();
    decompiler.addToken(87);
    int count = 1;
    while ((expr instanceof net.sourceforge.htmlunit.corejs.javascript.ast.ParenthesizedExpression)) {
      decompiler.addToken(87);
      count++;
      expr = ((net.sourceforge.htmlunit.corejs.javascript.ast.ParenthesizedExpression)expr).getExpression();
    }
    Node result = transform(expr);
    for (int i = 0; i < count; i++) {
      decompiler.addToken(88);
    }
    result.putProp(19, Boolean.TRUE);
    return result;
  }
  
  private Node transformPropertyGet(PropertyGet node) {
    Node target = transform(node.getTarget());
    String name = node.getProperty().getIdentifier();
    decompiler.addToken(108);
    decompiler.addName(name);
    return createPropertyGet(target, null, name, 0);
  }
  
  private Node transformRegExp(net.sourceforge.htmlunit.corejs.javascript.ast.RegExpLiteral node) {
    decompiler.addRegexp(node.getValue(), node.getFlags());
    currentScriptOrFn.addRegExp(node);
    return node;
  }
  
  private Node transformReturn(net.sourceforge.htmlunit.corejs.javascript.ast.ReturnStatement node)
  {
    boolean expClosure = Boolean.TRUE.equals(node.getProp(25));
    if (expClosure) {
      decompiler.addName(" ");
    } else {
      decompiler.addToken(4);
    }
    AstNode rv = node.getReturnValue();
    Node value = rv == null ? null : transform(rv);
    if (!expClosure)
      decompiler.addEOL(82);
    return rv == null ? new Node(4, node.getLineno()) : new Node(4, value, node
      .getLineno());
  }
  
  private Node transformScript(ScriptNode node) {
    decompiler.addToken(136);
    if (currentScope != null)
      Kit.codeBug();
    currentScope = node;
    Node body = new Node(129);
    for (Node kid : node) {
      body.addChildToBack(transform((AstNode)kid));
    }
    node.removeChildren();
    Node children = body.getFirstChild();
    if (children != null) {
      node.addChildrenToBack(children);
    }
    return node;
  }
  
  private Node transformString(StringLiteral node) {
    decompiler.addString(node.getValue());
    return Node.newString(node.getValue());
  }
  






































  private Node transformSwitch(net.sourceforge.htmlunit.corejs.javascript.ast.SwitchStatement node)
  {
    decompiler.addToken(114);
    decompiler.addToken(87);
    Node switchExpr = transform(node.getExpression());
    decompiler.addToken(88);
    node.addChildToBack(switchExpr);
    
    Node block = new Node(129, node, node.getLineno());
    decompiler.addEOL(85);
    
    for (net.sourceforge.htmlunit.corejs.javascript.ast.SwitchCase sc : node.getCases()) {
      AstNode expr = sc.getExpression();
      Node caseExpr = null;
      
      if (expr != null) {
        decompiler.addToken(115);
        caseExpr = transform(expr);
      } else {
        decompiler.addToken(116);
      }
      decompiler.addEOL(103);
      
      List<AstNode> stmts = sc.getStatements();
      Node body = new net.sourceforge.htmlunit.corejs.javascript.ast.Block();
      if (stmts != null) {
        for (AstNode kid : stmts) {
          body.addChildToBack(transform(kid));
        }
      }
      addSwitchCase(block, caseExpr, body);
    }
    decompiler.addEOL(86);
    closeSwitch(block);
    return block;
  }
  
  private Node transformThrow(net.sourceforge.htmlunit.corejs.javascript.ast.ThrowStatement node) {
    decompiler.addToken(50);
    Node value = transform(node.getExpression());
    decompiler.addEOL(82);
    return new Node(50, value, node.getLineno());
  }
  
  private Node transformTry(net.sourceforge.htmlunit.corejs.javascript.ast.TryStatement node) {
    decompiler.addToken(81);
    decompiler.addEOL(85);
    Node tryBlock = transform(node.getTryBlock());
    decompiler.addEOL(86);
    
    Node catchBlocks = new net.sourceforge.htmlunit.corejs.javascript.ast.Block();
    for (net.sourceforge.htmlunit.corejs.javascript.ast.CatchClause cc : node.getCatchClauses()) {
      decompiler.addToken(124);
      decompiler.addToken(87);
      
      String varName = cc.getVarName().getIdentifier();
      decompiler.addName(varName);
      
      Node catchCond = null;
      AstNode ccc = cc.getCatchCondition();
      if (ccc != null) {
        decompiler.addName(" ");
        decompiler.addToken(112);
        catchCond = transform(ccc);
      } else {
        catchCond = new net.sourceforge.htmlunit.corejs.javascript.ast.EmptyExpression();
      }
      decompiler.addToken(88);
      decompiler.addEOL(85);
      
      Node body = transform(cc.getBody());
      decompiler.addEOL(86);
      
      catchBlocks.addChildToBack(
        createCatch(varName, catchCond, body, cc.getLineno()));
    }
    Node finallyBlock = null;
    if (node.getFinallyBlock() != null) {
      decompiler.addToken(125);
      decompiler.addEOL(85);
      finallyBlock = transform(node.getFinallyBlock());
      decompiler.addEOL(86);
    }
    return createTryCatchFinally(tryBlock, catchBlocks, finallyBlock, node
      .getLineno());
  }
  
  private Node transformUnary(UnaryExpression node) {
    int type = node.getType();
    if (type == 74) {
      return transformDefaultXmlNamepace(node);
    }
    if (node.isPrefix()) {
      decompiler.addToken(type);
    }
    Node child = transform(node.getOperand());
    if (node.isPostfix()) {
      decompiler.addToken(type);
    }
    if ((type == 106) || (type == 107)) {
      return createIncDec(type, node.isPostfix(), child);
    }
    return createUnary(type, child);
  }
  
  private Node transformVariables(VariableDeclaration node) {
    decompiler.addToken(node.getType());
    transformVariableInitializers(node);
    


    AstNode parent = node.getParent();
    if ((!(parent instanceof net.sourceforge.htmlunit.corejs.javascript.ast.Loop)) && (!(parent instanceof LetNode))) {
      decompiler.addEOL(82);
    }
    return node;
  }
  
  private Node transformVariableInitializers(VariableDeclaration node) {
    List<net.sourceforge.htmlunit.corejs.javascript.ast.VariableInitializer> vars = node.getVariables();
    int size = vars.size();int i = 0;
    for (net.sourceforge.htmlunit.corejs.javascript.ast.VariableInitializer var : vars) {
      AstNode target = var.getTarget();
      AstNode init = var.getInitializer();
      
      Node left = null;
      if (var.isDestructuring()) {
        decompile(target);
        left = target;
      } else {
        left = transform(target);
      }
      
      Node right = null;
      if (init != null) {
        decompiler.addToken(90);
        right = transform(init);
      }
      
      if (var.isDestructuring()) {
        if (right == null) {
          node.addChildToBack(left);
        } else {
          Node d = createDestructuringAssignment(node.getType(), left, right);
          
          node.addChildToBack(d);
        }
      } else {
        if (right != null) {
          left.addChildToBack(right);
        }
        node.addChildToBack(left);
      }
      if (i++ < size - 1) {
        decompiler.addToken(89);
      }
    }
    return node;
  }
  
  private Node transformWhileLoop(net.sourceforge.htmlunit.corejs.javascript.ast.WhileLoop loop) {
    decompiler.addToken(117);
    loop.setType(132);
    pushScope(loop);
    try {
      decompiler.addToken(87);
      Node cond = transform(loop.getCondition());
      decompiler.addToken(88);
      decompiler.addEOL(85);
      Node body = transform(loop.getBody());
      decompiler.addEOL(86);
      return createLoop(loop, 1, body, cond, null, null);
    } finally {
      popScope();
    }
  }
  
  private Node transformWith(net.sourceforge.htmlunit.corejs.javascript.ast.WithStatement node) {
    decompiler.addToken(123);
    decompiler.addToken(87);
    Node expr = transform(node.getExpression());
    decompiler.addToken(88);
    decompiler.addEOL(85);
    Node stmt = transform(node.getStatement());
    decompiler.addEOL(86);
    return createWith(expr, stmt, node.getLineno());
  }
  
  private Node transformYield(net.sourceforge.htmlunit.corejs.javascript.ast.Yield node) {
    decompiler.addToken(72);
    Node kid = node.getValue() == null ? null : transform(node.getValue());
    if (kid != null) {
      return new Node(72, kid, node.getLineno());
    }
    return new Node(72, node.getLineno());
  }
  


  private Node transformXmlLiteral(net.sourceforge.htmlunit.corejs.javascript.ast.XmlLiteral node)
  {
    Node pnXML = new Node(30, node.getLineno());
    List<net.sourceforge.htmlunit.corejs.javascript.ast.XmlFragment> frags = node.getFragments();
    
    net.sourceforge.htmlunit.corejs.javascript.ast.XmlString first = (net.sourceforge.htmlunit.corejs.javascript.ast.XmlString)frags.get(0);
    boolean anon = first.getXml().trim().startsWith("<>");
    pnXML.addChildToBack(createName(anon ? "XMLList" : "XML"));
    
    Node pn = null;
    for (net.sourceforge.htmlunit.corejs.javascript.ast.XmlFragment frag : frags) {
      if ((frag instanceof net.sourceforge.htmlunit.corejs.javascript.ast.XmlString)) {
        String xml = ((net.sourceforge.htmlunit.corejs.javascript.ast.XmlString)frag).getXml();
        decompiler.addName(xml);
        if (pn == null) {
          pn = createString(xml);
        } else {
          pn = createBinary(21, pn, createString(xml));
        }
      } else {
        net.sourceforge.htmlunit.corejs.javascript.ast.XmlExpression xexpr = (net.sourceforge.htmlunit.corejs.javascript.ast.XmlExpression)frag;
        boolean isXmlAttr = xexpr.isXmlAttribute();
        
        decompiler.addToken(85);
        Node expr; Node expr; if ((xexpr.getExpression() instanceof net.sourceforge.htmlunit.corejs.javascript.ast.EmptyExpression)) {
          expr = createString("");
        } else {
          expr = transform(xexpr.getExpression());
        }
        decompiler.addToken(86);
        if (isXmlAttr)
        {
          expr = createUnary(75, expr);
          Node prepend = createBinary(21, createString("\""), expr);
          
          expr = createBinary(21, prepend, createString("\""));
        } else {
          expr = createUnary(76, expr);
        }
        pn = createBinary(21, pn, expr);
      }
    }
    
    pnXML.addChildToBack(pn);
    return pnXML;
  }
  
  private Node transformXmlMemberGet(net.sourceforge.htmlunit.corejs.javascript.ast.XmlMemberGet node) {
    net.sourceforge.htmlunit.corejs.javascript.ast.XmlRef ref = node.getMemberRef();
    Node pn = transform(node.getLeft());
    int flags = ref.isAttributeAccess() ? 2 : 0;
    if (node.getType() == 143) {
      flags |= 0x4;
      decompiler.addToken(143);
    } else {
      decompiler.addToken(108);
    }
    return transformXmlRef(pn, ref, flags);
  }
  
  private Node transformXmlRef(net.sourceforge.htmlunit.corejs.javascript.ast.XmlRef node)
  {
    int memberTypeFlags = node.isAttributeAccess() ? 2 : 0;
    
    return transformXmlRef(null, node, memberTypeFlags);
  }
  
  private Node transformXmlRef(Node pn, net.sourceforge.htmlunit.corejs.javascript.ast.XmlRef node, int memberTypeFlags) {
    if ((memberTypeFlags & 0x2) != 0)
      decompiler.addToken(147);
    Name namespace = node.getNamespace();
    String ns = namespace != null ? namespace.getIdentifier() : null;
    if (ns != null) {
      decompiler.addName(ns);
      decompiler.addToken(144);
    }
    if ((node instanceof net.sourceforge.htmlunit.corejs.javascript.ast.XmlPropRef)) {
      String name = ((net.sourceforge.htmlunit.corejs.javascript.ast.XmlPropRef)node).getPropName().getIdentifier();
      decompiler.addName(name);
      return createPropertyGet(pn, ns, name, memberTypeFlags);
    }
    decompiler.addToken(83);
    Node expr = transform(((net.sourceforge.htmlunit.corejs.javascript.ast.XmlElemRef)node).getExpression());
    decompiler.addToken(84);
    return createElementGet(pn, ns, expr, memberTypeFlags);
  }
  
  private Node transformDefaultXmlNamepace(UnaryExpression node)
  {
    decompiler.addToken(116);
    decompiler.addName(" xml");
    decompiler.addName(" namespace");
    decompiler.addToken(90);
    Node child = transform(node.getOperand());
    return createUnary(74, child);
  }
  



  private void addSwitchCase(Node switchBlock, Node caseExpression, Node statements)
  {
    if (switchBlock.getType() != 129)
      throw Kit.codeBug();
    Jump switchNode = (Jump)switchBlock.getFirstChild();
    if (switchNode.getType() != 114) {
      throw Kit.codeBug();
    }
    Node gotoTarget = Node.newTarget();
    if (caseExpression != null) {
      Jump caseNode = new Jump(115, caseExpression);
      target = gotoTarget;
      switchNode.addChildToBack(caseNode);
    } else {
      switchNode.setDefault(gotoTarget);
    }
    switchBlock.addChildToBack(gotoTarget);
    switchBlock.addChildToBack(statements);
  }
  
  private void closeSwitch(Node switchBlock) {
    if (switchBlock.getType() != 129)
      throw Kit.codeBug();
    Jump switchNode = (Jump)switchBlock.getFirstChild();
    if (switchNode.getType() != 114) {
      throw Kit.codeBug();
    }
    Node switchBreakTarget = Node.newTarget();
    

    target = switchBreakTarget;
    
    Node defaultTarget = switchNode.getDefault();
    if (defaultTarget == null) {
      defaultTarget = switchBreakTarget;
    }
    
    switchBlock.addChildAfter(makeJump(5, defaultTarget), switchNode);
    
    switchBlock.addChildToBack(switchBreakTarget);
  }
  
  private Node createExprStatementNoReturn(Node expr, int lineno) {
    return new Node(133, expr, lineno);
  }
  
  private Node createString(String string) {
    return Node.newString(string);
  }
  













  private Node createCatch(String varName, Node catchCond, Node stmts, int lineno)
  {
    if (catchCond == null) {
      catchCond = new Node(128);
    }
    return new Node(124, createName(varName), catchCond, stmts, lineno);
  }
  

  private Node initFunction(FunctionNode fnNode, int functionIndex, Node statements, int functionType)
  {
    fnNode.setFunctionType(functionType);
    fnNode.addChildToBack(statements);
    
    int functionCount = fnNode.getFunctionCount();
    if (functionCount != 0)
    {
      fnNode.setRequiresActivation();
    }
    
    if (functionType == 2) {
      Name name = fnNode.getFunctionName();
      if ((name != null) && (name.length() != 0) && 
        (fnNode.getSymbol(name.getIdentifier()) == null))
      {






        fnNode.putSymbol(new net.sourceforge.htmlunit.corejs.javascript.ast.Symbol(109, name
          .getIdentifier()));
        

        Node setFn = new Node(133, new Node(8, Node.newString(49, name
          .getIdentifier()), new Node(63)));
        
        statements.addChildrenToFront(setFn);
      }
    }
    

    Node lastStmt = statements.getLastChild();
    if ((lastStmt == null) || (lastStmt.getType() != 4)) {
      statements.addChildToBack(new Node(4));
    }
    
    Node result = Node.newString(109, fnNode.getName());
    result.putIntProp(1, functionIndex);
    return result;
  }
  




  private Scope createLoopNode(Node loopLabel, int lineno)
  {
    Scope result = createScopeNode(132, lineno);
    if (loopLabel != null) {
      ((Jump)loopLabel).setLoop(result);
    }
    return result;
  }
  
  private Node createFor(Scope loop, Node init, Node test, Node incr, Node body)
  {
    if (init.getType() == 153)
    {


      Scope let = Scope.splitScope(loop);
      let.setType(153);
      let.addChildrenToBack(init);
      let.addChildToBack(createLoop(loop, 2, body, test, new Node(128), incr));
      
      return let;
    }
    return createLoop(loop, 2, body, test, init, incr);
  }
  
  private Node createLoop(Jump loop, int loopType, Node body, Node cond, Node init, Node incr)
  {
    Node bodyTarget = Node.newTarget();
    Node condTarget = Node.newTarget();
    if ((loopType == 2) && (cond.getType() == 128)) {
      cond = new Node(45);
    }
    Jump IFEQ = new Jump(6, cond);
    target = bodyTarget;
    Node breakTarget = Node.newTarget();
    
    loop.addChildToBack(bodyTarget);
    loop.addChildrenToBack(body);
    if ((loopType == 1) || (loopType == 2))
    {
      loop.addChildrenToBack(new Node(128, loop.getLineno()));
    }
    loop.addChildToBack(condTarget);
    loop.addChildToBack(IFEQ);
    loop.addChildToBack(breakTarget);
    
    target = breakTarget;
    Node continueTarget = condTarget;
    
    if ((loopType == 1) || (loopType == 2))
    {
      loop.addChildToFront(makeJump(5, condTarget));
      
      if (loopType == 2) {
        int initType = init.getType();
        if (initType != 128) {
          if ((initType != 122) && (initType != 153)) {
            init = new Node(133, init);
          }
          loop.addChildToFront(init);
        }
        Node incrTarget = Node.newTarget();
        loop.addChildAfter(incrTarget, body);
        if (incr.getType() != 128) {
          incr = new Node(133, incr);
          loop.addChildAfter(incr, incrTarget);
        }
        continueTarget = incrTarget;
      }
    }
    
    loop.setContinue(continueTarget);
    return loop;
  }
  



  private Node createForIn(int declType, Node loop, Node lhs, Node obj, Node body, boolean isForEach)
  {
    int destructuring = -1;
    int destructuringLen = 0;
    
    int type = lhs.getType();
    Node lvalue; Node lvalue; if ((type == 122) || (type == 153)) {
      Node kid = lhs.getLastChild();
      int kidType = kid.getType();
      if ((kidType == 65) || (kidType == 66)) {
        type = destructuring = kidType;
        Node lvalue = kid;
        destructuringLen = 0;
        if ((kid instanceof ArrayLiteral))
        {
          destructuringLen = ((ArrayLiteral)kid).getDestructuringLength(); } } else { Node lvalue;
        if (kidType == 39) {
          lvalue = Node.newString(39, kid.getString());
        } else {
          reportError("msg.bad.for.in.lhs");
          return null;
        }
      } } else if ((type == 65) || (type == 66)) {
      destructuring = type;
      Node lvalue = lhs;
      destructuringLen = 0;
      if ((lhs instanceof ArrayLiteral))
      {
        destructuringLen = ((ArrayLiteral)lhs).getDestructuringLength(); }
    } else {
      lvalue = makeReference(lhs);
      if (lvalue == null) {
        reportError("msg.bad.for.in.lhs");
        return null;
      }
    }
    
    Node localBlock = new Node(141);
    int initType = destructuring != -1 ? 60 : isForEach ? 59 : 58;
    

    Node init = new Node(initType, obj);
    init.putProp(3, localBlock);
    Node cond = new Node(61);
    cond.putProp(3, localBlock);
    Node id = new Node(62);
    id.putProp(3, localBlock);
    
    Node newBody = new Node(129);
    Node assign;
    if (destructuring != -1) {
      Node assign = createDestructuringAssignment(declType, lvalue, id);
      if ((!isForEach) && ((destructuring == 66) || (destructuringLen != 2)))
      {


        reportError("msg.bad.for.in.destruct");
      }
    } else {
      assign = simpleAssignment(lvalue, id);
    }
    newBody.addChildToBack(new Node(133, assign));
    newBody.addChildToBack(body);
    
    loop = createLoop((Jump)loop, 1, newBody, cond, null, null);
    loop.addChildToFront(init);
    if ((type == 122) || (type == 153))
      loop.addChildToFront(lhs);
    localBlock.addChildToBack(loop);
    
    return localBlock;
  }
  
















  private Node createTryCatchFinally(Node tryBlock, Node catchBlocks, Node finallyBlock, int lineno)
  {
    boolean hasFinally = (finallyBlock != null) && ((finallyBlock.getType() != 129) || (finallyBlock.hasChildren()));
    

    if ((tryBlock.getType() == 129) && (!tryBlock.hasChildren()) && (!hasFinally))
    {
      return tryBlock;
    }
    
    boolean hasCatch = catchBlocks.hasChildren();
    

    if ((!hasFinally) && (!hasCatch))
    {
      return tryBlock;
    }
    
    Node handlerBlock = new Node(141);
    Jump pn = new Jump(81, tryBlock, lineno);
    pn.putProp(3, handlerBlock);
    
    if (hasCatch)
    {
      Node endCatch = Node.newTarget();
      pn.addChildToBack(makeJump(5, endCatch));
      

      Node catchTarget = Node.newTarget();
      target = catchTarget;
      
      pn.addChildToBack(catchTarget);
      
















































      Node catchScopeBlock = new Node(141);
      

      Node cb = catchBlocks.getFirstChild();
      boolean hasDefault = false;
      int scopeIndex = 0;
      while (cb != null) {
        int catchLineNo = cb.getLineno();
        
        Node name = cb.getFirstChild();
        Node cond = name.getNext();
        Node catchStatement = cond.getNext();
        cb.removeChild(name);
        cb.removeChild(cond);
        cb.removeChild(catchStatement);
        




        catchStatement.addChildToBack(new Node(3));
        catchStatement.addChildToBack(makeJump(5, endCatch));
        
        Node condStmt;
        
        if (cond.getType() == 128) {
          Node condStmt = catchStatement;
          hasDefault = true;
        } else {
          condStmt = createIf(cond, catchStatement, null, catchLineNo);
        }
        




        Node catchScope = new Node(57, name, createUseLocal(handlerBlock));
        catchScope.putProp(3, catchScopeBlock);
        catchScope.putIntProp(14, scopeIndex);
        catchScopeBlock.addChildToBack(catchScope);
        

        catchScopeBlock.addChildToBack(
          createWith(createUseLocal(catchScopeBlock), condStmt, catchLineNo));
        


        cb = cb.getNext();
        scopeIndex++;
      }
      pn.addChildToBack(catchScopeBlock);
      if (!hasDefault)
      {
        Node rethrow = new Node(51);
        rethrow.putProp(3, handlerBlock);
        pn.addChildToBack(rethrow);
      }
      
      pn.addChildToBack(endCatch);
    }
    
    if (hasFinally) {
      Node finallyTarget = Node.newTarget();
      pn.setFinally(finallyTarget);
      

      pn.addChildToBack(makeJump(135, finallyTarget));
      

      Node finallyEnd = Node.newTarget();
      pn.addChildToBack(makeJump(5, finallyEnd));
      
      pn.addChildToBack(finallyTarget);
      Node fBlock = new Node(125, finallyBlock);
      fBlock.putProp(3, handlerBlock);
      pn.addChildToBack(fBlock);
      
      pn.addChildToBack(finallyEnd);
    }
    handlerBlock.addChildToBack(pn);
    return handlerBlock;
  }
  
  private Node createWith(Node obj, Node body, int lineno) {
    setRequiresActivation();
    Node result = new Node(129, lineno);
    result.addChildToBack(new Node(2, obj));
    Node bodyNode = new Node(123, body, lineno);
    result.addChildrenToBack(bodyNode);
    result.addChildToBack(new Node(3));
    return result;
  }
  
  private Node createIf(Node cond, Node ifTrue, Node ifFalse, int lineno) {
    int condStatus = isAlwaysDefinedBoolean(cond);
    if (condStatus == 1)
      return ifTrue;
    if (condStatus == -1) {
      if (ifFalse != null) {
        return ifFalse;
      }
      
      return new Node(129, lineno);
    }
    
    Node result = new Node(129, lineno);
    Node ifNotTarget = Node.newTarget();
    Jump IFNE = new Jump(7, cond);
    target = ifNotTarget;
    
    result.addChildToBack(IFNE);
    result.addChildrenToBack(ifTrue);
    
    if (ifFalse != null) {
      Node endTarget = Node.newTarget();
      result.addChildToBack(makeJump(5, endTarget));
      result.addChildToBack(ifNotTarget);
      result.addChildrenToBack(ifFalse);
      result.addChildToBack(endTarget);
    } else {
      result.addChildToBack(ifNotTarget);
    }
    
    return result;
  }
  
  private Node createCondExpr(Node cond, Node ifTrue, Node ifFalse) {
    int condStatus = isAlwaysDefinedBoolean(cond);
    if (condStatus == 1)
      return ifTrue;
    if (condStatus == -1) {
      return ifFalse;
    }
    return new Node(102, cond, ifTrue, ifFalse);
  }
  
  private Node createUnary(int nodeType, Node child) {
    int childType = child.getType();
    switch (nodeType) {
    case 31:  Node n;
      Node n;
      if (childType == 39)
      {

        child.setType(49);
        Node left = child;
        Node right = Node.newString(child.getString());
        n = new Node(nodeType, left, right); } else { Node n;
        if ((childType == 33) || (childType == 36))
        {
          Node left = child.getFirstChild();
          Node right = child.getLastChild();
          child.removeChild(left);
          child.removeChild(right);
          n = new Node(nodeType, left, right); } else { Node n;
          if (childType == 67) {
            Node ref = child.getFirstChild();
            child.removeChild(ref);
            n = new Node(69, ref);
          }
          else {
            n = new Node(nodeType, new Node(45), child);
          } } }
      return n;
    
    case 32: 
      if (childType == 39) {
        child.setType(137);
        return child;
      }
      break;
    case 27: 
      if (childType == 40) {
        int value = ScriptRuntime.toInt32(child.getDouble());
        child.setDouble(value ^ 0xFFFFFFFF);
        return child;
      }
      break;
    case 29: 
      if (childType == 40) {
        child.setDouble(-child.getDouble());
        return child;
      }
      break;
    case 26: 
      int status = isAlwaysDefinedBoolean(child);
      if (status != 0) { int type;
        int type;
        if (status == 1) {
          type = 44;
        } else {
          type = 45;
        }
        if ((childType == 45) || (childType == 44)) {
          child.setType(type);
          return child;
        }
        return new Node(type);
      }
      break;
    }
    
    return new Node(nodeType, child);
  }
  
  private Node createCallOrNew(int nodeType, Node child) {
    int type = 0;
    if (child.getType() == 39) {
      String name = child.getString();
      if (name.equals("eval")) {
        type = 1;
      } else if (name.equals("With")) {
        type = 2;
      }
    } else if (child.getType() == 33) {
      String name = child.getLastChild().getString();
      if (name.equals("eval")) {
        type = 1;
      }
    }
    Node node = new Node(nodeType, child);
    if (type != 0)
    {
      setRequiresActivation();
      node.putIntProp(10, type);
    }
    return node;
  }
  
  private Node createIncDec(int nodeType, boolean post, Node child) {
    child = makeReference(child);
    int childType = child.getType();
    
    switch (childType) {
    case 33: 
    case 36: 
    case 39: 
    case 67: 
      Node n = new Node(nodeType, child);
      int incrDecrMask = 0;
      if (nodeType == 107) {
        incrDecrMask |= 0x1;
      }
      if (post) {
        incrDecrMask |= 0x2;
      }
      n.putIntProp(13, incrDecrMask);
      return n;
    }
    
    throw Kit.codeBug();
  }
  
  private Node createPropertyGet(Node target, String namespace, String name, int memberTypeFlags)
  {
    if ((namespace == null) && (memberTypeFlags == 0)) {
      if (target == null) {
        return createName(name);
      }
      checkActivationName(name, 33);
      if (ScriptRuntime.isSpecialProperty(name)) {
        Node ref = new Node(71, target);
        ref.putProp(17, name);
        return new Node(67, ref);
      }
      return new Node(33, target, Node.newString(name));
    }
    Node elem = Node.newString(name);
    memberTypeFlags |= 0x1;
    return createMemberRefGet(target, namespace, elem, memberTypeFlags);
  }
  












  private Node createElementGet(Node target, String namespace, Node elem, int memberTypeFlags)
  {
    if ((namespace == null) && (memberTypeFlags == 0))
    {

      if (target == null)
        throw Kit.codeBug();
      return new Node(36, target, elem);
    }
    return createMemberRefGet(target, namespace, elem, memberTypeFlags);
  }
  
  private Node createMemberRefGet(Node target, String namespace, Node elem, int memberTypeFlags)
  {
    Node nsNode = null;
    if (namespace != null)
    {
      if (namespace.equals("*")) {
        nsNode = new Node(42);
      } else
        nsNode = createName(namespace);
    }
    Node ref;
    Node ref;
    if (target == null) { Node ref;
      if (namespace == null) {
        ref = new Node(79, elem);
      } else
        ref = new Node(80, nsNode, elem);
    } else {
      Node ref;
      if (namespace == null) {
        ref = new Node(77, target, elem);
      } else {
        ref = new Node(78, target, nsNode, elem);
      }
    }
    if (memberTypeFlags != 0) {
      ref.putIntProp(16, memberTypeFlags);
    }
    return new Node(67, ref);
  }
  
  private Node createBinary(int nodeType, Node left, Node right) {
    switch (nodeType)
    {

    case 21: 
      if (type == 41) { String s2;
        String s2;
        if (type == 41) {
          s2 = right.getString();
        } else { if (type != 40) break;
          s2 = ScriptRuntime.numberToString(right.getDouble(), 10);
        }
        

        String s1 = left.getString();
        left.setString(s1.concat(s2));
        return left;
      } else if (type == 40) {
        if (type == 40) {
          left.setDouble(left.getDouble() + right.getDouble());
          return left; }
        if (type == 41)
        {
          String s1 = ScriptRuntime.numberToString(left.getDouble(), 10);
          String s2 = right.getString();
          right.setString(s1.concat(s2));
          return right;
        }
      }
      




      break;
    case 22: 
      if (type == 40) {
        double ld = left.getDouble();
        if (type == 40)
        {
          left.setDouble(ld - right.getDouble());
          return left; }
        if (ld == 0.0D)
        {
          return new Node(29, right);
        }
      } else if ((type == 40) && 
        (right.getDouble() == 0.0D))
      {

        return new Node(28, left);
      }
      


      break;
    case 23: 
      if (type == 40) {
        double ld = left.getDouble();
        if (type == 40)
        {
          left.setDouble(ld * right.getDouble());
          return left; }
        if (ld == 1.0D)
        {
          return new Node(28, right);
        }
      } else if ((type == 40) && 
        (right.getDouble() == 1.0D))
      {

        return new Node(28, left);
      }
      



      break;
    case 24: 
      if (type == 40) {
        double rd = right.getDouble();
        if (type == 40)
        {
          left.setDouble(left.getDouble() / rd);
          return left; }
        if (rd == 1.0D)
        {

          return new Node(28, left); }
      }
      break;
    





    case 105: 
      int leftStatus = isAlwaysDefinedBoolean(left);
      if (leftStatus == -1)
      {
        return left; }
      if (leftStatus == 1)
      {
        return right;
      }
      





      break;
    case 104: 
      int leftStatus = isAlwaysDefinedBoolean(left);
      if (leftStatus == 1)
      {
        return left; }
      if (leftStatus == -1)
      {
        return right;
      }
      
      break;
    }
    
    return new Node(nodeType, left, right);
  }
  
  private Node createAssignment(int assignType, Node left, Node right) {
    Node ref = makeReference(left);
    if (ref == null) {
      if ((left.getType() == 65) || 
        (left.getType() == 66)) {
        if (assignType != 90) {
          reportError("msg.bad.destruct.op");
          return right;
        }
        return createDestructuringAssignment(-1, left, right);
      }
      reportError("msg.bad.assign.left");
      return right;
    }
    left = ref;
    int assignOp;
    int assignOp;
    int assignOp; int assignOp; int assignOp; int assignOp; int assignOp; int assignOp; int assignOp; int assignOp; int assignOp; switch (assignType) {
    case 90: 
      return simpleAssignment(left, right);
    case 91: 
      assignOp = 9;
      break;
    case 92: 
      assignOp = 10;
      break;
    case 93: 
      assignOp = 11;
      break;
    case 94: 
      assignOp = 18;
      break;
    case 95: 
      assignOp = 19;
      break;
    case 96: 
      assignOp = 20;
      break;
    case 97: 
      assignOp = 21;
      break;
    case 98: 
      assignOp = 22;
      break;
    case 99: 
      assignOp = 23;
      break;
    case 100: 
      assignOp = 24;
      break;
    case 101: 
      assignOp = 25;
      break;
    default: 
      throw Kit.codeBug();
    }
    int assignOp;
    int nodeType = left.getType();
    switch (nodeType) {
    case 39: 
      Node op = new Node(assignOp, left, right);
      Node lvalueLeft = Node.newString(49, left.getString());
      return new Node(8, lvalueLeft, op);
    
    case 33: 
    case 36: 
      Node obj = left.getFirstChild();
      Node id = left.getLastChild();
      
      int type = nodeType == 33 ? 139 : 140;
      

      Node opLeft = new Node(138);
      Node op = new Node(assignOp, opLeft, right);
      return new Node(type, obj, id, op);
    
    case 67: 
      ref = left.getFirstChild();
      checkMutableReference(ref);
      Node opLeft = new Node(138);
      Node op = new Node(assignOp, opLeft, right);
      return new Node(142, ref, op);
    }
    
    
    throw Kit.codeBug();
  }
  
  private Node createUseLocal(Node localBlock) {
    if (141 != localBlock.getType())
      throw Kit.codeBug();
    Node result = new Node(54);
    result.putProp(3, localBlock);
    return result;
  }
  
  private Jump makeJump(int type, Node target) {
    Jump n = new Jump(type);
    target = target;
    return n;
  }
  
  private Node makeReference(Node node) {
    int type = node.getType();
    switch (type) {
    case 33: 
    case 36: 
    case 39: 
    case 67: 
      return node;
    case 38: 
      node.setType(70);
      return new Node(67, node);
    }
    
    return null;
  }
  
  private static int isAlwaysDefinedBoolean(Node node)
  {
    switch (node.getType()) {
    case 42: 
    case 44: 
      return -1;
    case 45: 
      return 1;
    case 40: 
      double num = node.getDouble();
      if ((num == num) && (num != 0.0D)) {
        return 1;
      }
      return -1;
    }
    
    
    return 0;
  }
  
  boolean isDestructuring(Node n)
  {
    return ((n instanceof net.sourceforge.htmlunit.corejs.javascript.ast.DestructuringForm)) && 
      (((net.sourceforge.htmlunit.corejs.javascript.ast.DestructuringForm)n).isDestructuring());
  }
  
  Node decompileFunctionHeader(FunctionNode fn) {
    Node mexpr = null;
    if (fn.getFunctionName() != null) {
      decompiler.addName(fn.getName());
    } else if (fn.getMemberExprNode() != null) {
      mexpr = transform(fn.getMemberExprNode());
    }
    decompiler.addToken(87);
    List<AstNode> params = fn.getParams();
    for (int i = 0; i < params.size(); i++) {
      decompile((AstNode)params.get(i));
      if (i < params.size() - 1) {
        decompiler.addToken(89);
      }
    }
    decompiler.addToken(88);
    if (!fn.isExpressionClosure()) {
      decompiler.addEOL(85);
    }
    return mexpr;
  }
  
  void decompile(AstNode node) {
    switch (node.getType()) {
    case 65: 
      decompileArrayLiteral((ArrayLiteral)node);
      break;
    case 66: 
      decompileObjectLiteral((net.sourceforge.htmlunit.corejs.javascript.ast.ObjectLiteral)node);
      break;
    case 41: 
      decompiler.addString(((StringLiteral)node).getValue());
      break;
    case 39: 
      decompiler.addName(((Name)node).getIdentifier());
      break;
    case 40: 
      decompiler.addNumber(((net.sourceforge.htmlunit.corejs.javascript.ast.NumberLiteral)node).getNumber());
      break;
    case 33: 
      decompilePropertyGet((PropertyGet)node);
      break;
    case 128: 
      break;
    case 36: 
      decompileElementGet((ElementGet)node);
      break;
    case 43: 
      decompiler.addToken(node.getType());
      break;
    default: 
      Kit.codeBug("unexpected token: " + 
        Token.typeToName(node.getType()));
    }
  }
  
  void decompileArrayLiteral(ArrayLiteral node)
  {
    decompiler.addToken(83);
    List<AstNode> elems = node.getElements();
    int size = elems.size();
    for (int i = 0; i < size; i++) {
      AstNode elem = (AstNode)elems.get(i);
      decompile(elem);
      if (i < size - 1) {
        decompiler.addToken(89);
      }
    }
    decompiler.addToken(84);
  }
  
  void decompileObjectLiteral(net.sourceforge.htmlunit.corejs.javascript.ast.ObjectLiteral node)
  {
    decompiler.addToken(85);
    List<ObjectProperty> props = node.getElements();
    int size = props.size();
    for (int i = 0; i < size; i++) {
      ObjectProperty prop = (ObjectProperty)props.get(i);
      
      boolean destructuringShorthand = Boolean.TRUE.equals(prop.getProp(26));
      decompile(prop.getLeft());
      if (!destructuringShorthand) {
        decompiler.addToken(103);
        decompile(prop.getRight());
      }
      if (i < size - 1) {
        decompiler.addToken(89);
      }
    }
    decompiler.addToken(86);
  }
  
  void decompilePropertyGet(PropertyGet node)
  {
    decompile(node.getTarget());
    decompiler.addToken(108);
    decompile(node.getProperty());
  }
  
  void decompileElementGet(ElementGet node)
  {
    decompile(node.getTarget());
    decompiler.addToken(83);
    decompile(node.getElement());
    decompiler.addToken(84);
  }
}
