package net.sourceforge.htmlunit.corejs.javascript.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.sourceforge.htmlunit.corejs.javascript.Node;












public class ScriptNode
  extends Scope
{
  private int encodedSourceStart = -1;
  private int encodedSourceEnd = -1;
  private String sourceName;
  private String encodedSource;
  private int endLineno = -1;
  
  private List<FunctionNode> functions;
  private List<RegExpLiteral> regexps;
  private List<FunctionNode> EMPTY_LIST = Collections.emptyList();
  
  private List<Symbol> symbols = new ArrayList(4);
  private int paramCount = 0;
  
  private String[] variableNames;
  private boolean[] isConsts;
  private Object compilerData;
  private int tempNumber = 0;
  
  public ScriptNode()
  {
    top = this;
    type = 136;
  }
  


  public ScriptNode(int pos)
  {
    super(pos);top = this;type = 136;
  }
  



  public String getSourceName()
  {
    return sourceName;
  }
  



  public void setSourceName(String sourceName)
  {
    this.sourceName = sourceName;
  }
  



  public int getEncodedSourceStart()
  {
    return encodedSourceStart;
  }
  




  public void setEncodedSourceStart(int start)
  {
    encodedSourceStart = start;
  }
  



  public int getEncodedSourceEnd()
  {
    return encodedSourceEnd;
  }
  




  public void setEncodedSourceEnd(int end)
  {
    encodedSourceEnd = end;
  }
  




  public void setEncodedSourceBounds(int start, int end)
  {
    encodedSourceStart = start;
    encodedSourceEnd = end;
  }
  




  public void setEncodedSource(String encodedSource)
  {
    this.encodedSource = encodedSource;
  }
  















  public String getEncodedSource()
  {
    return encodedSource;
  }
  
  public int getBaseLineno() {
    return lineno;
  }
  




  public void setBaseLineno(int lineno)
  {
    if ((lineno < 0) || (this.lineno >= 0))
      codeBug();
    this.lineno = lineno;
  }
  
  public int getEndLineno() {
    return endLineno;
  }
  
  public void setEndLineno(int lineno)
  {
    if ((lineno < 0) || (endLineno >= 0))
      codeBug();
    endLineno = lineno;
  }
  
  public int getFunctionCount() {
    return functions == null ? 0 : functions.size();
  }
  
  public FunctionNode getFunctionNode(int i) {
    return (FunctionNode)functions.get(i);
  }
  
  public List<FunctionNode> getFunctions() {
    return functions == null ? EMPTY_LIST : functions;
  }
  





  public int addFunction(FunctionNode fnNode)
  {
    if (fnNode == null)
      codeBug();
    if (functions == null)
      functions = new ArrayList();
    functions.add(fnNode);
    return functions.size() - 1;
  }
  
  public int getRegexpCount() {
    return regexps == null ? 0 : regexps.size();
  }
  
  public String getRegexpString(int index) {
    return ((RegExpLiteral)regexps.get(index)).getValue();
  }
  
  public String getRegexpFlags(int index) {
    return ((RegExpLiteral)regexps.get(index)).getFlags();
  }
  


  public void addRegExp(RegExpLiteral re)
  {
    if (re == null)
      codeBug();
    if (regexps == null)
      regexps = new ArrayList();
    regexps.add(re);
    re.putIntProp(4, regexps.size() - 1);
  }
  
  public int getIndexForNameNode(Node nameNode) {
    if (variableNames == null)
      codeBug();
    Scope node = nameNode.getScope();
    
    Symbol symbol = node == null ? null : node.getSymbol(((Name)nameNode).getIdentifier());
    return symbol == null ? -1 : symbol.getIndex();
  }
  
  public String getParamOrVarName(int index) {
    if (variableNames == null)
      codeBug();
    return variableNames[index];
  }
  
  public int getParamCount() {
    return paramCount;
  }
  
  public int getParamAndVarCount() {
    if (variableNames == null)
      codeBug();
    return symbols.size();
  }
  
  public String[] getParamAndVarNames() {
    if (variableNames == null)
      codeBug();
    return variableNames;
  }
  
  public boolean[] getParamAndVarConst() {
    if (variableNames == null)
      codeBug();
    return isConsts;
  }
  
  void addSymbol(Symbol symbol) {
    if (variableNames != null)
      codeBug();
    if (symbol.getDeclType() == 87) {
      paramCount += 1;
    }
    symbols.add(symbol);
  }
  
  public List<Symbol> getSymbols() {
    return symbols;
  }
  
  public void setSymbols(List<Symbol> symbols) {
    this.symbols = symbols;
  }
  








  public void flattenSymbolTable(boolean flattenAllTables)
  {
    if (!flattenAllTables) {
      List<Symbol> newSymbols = new ArrayList();
      if (symbolTable != null)
      {


        for (int i = 0; i < symbols.size(); i++) {
          Symbol symbol = (Symbol)symbols.get(i);
          if (symbol.getContainingTable() == this) {
            newSymbols.add(symbol);
          }
        }
      }
      symbols = newSymbols;
    }
    variableNames = new String[symbols.size()];
    isConsts = new boolean[symbols.size()];
    for (int i = 0; i < symbols.size(); i++) {
      Symbol symbol = (Symbol)symbols.get(i);
      variableNames[i] = symbol.getName();
      isConsts[i] = (symbol.getDeclType() == 154 ? 1 : false);
      symbol.setIndex(i);
    }
  }
  
  public Object getCompilerData() {
    return compilerData;
  }
  
  public void setCompilerData(Object data) {
    assertNotNull(data);
    
    if (compilerData != null)
      throw new IllegalStateException();
    compilerData = data;
  }
  
  public String getNextTempName() {
    return "$" + tempNumber++;
  }
  
  public void visit(NodeVisitor v)
  {
    if (v.visit(this)) {
      for (Node kid : this) {
        ((AstNode)kid).visit(v);
      }
    }
  }
}
