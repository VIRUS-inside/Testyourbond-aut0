package net.sourceforge.htmlunit.corejs.javascript.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sourceforge.htmlunit.corejs.javascript.Node;





























































public class FunctionNode
  extends ScriptNode
{
  public static final int FUNCTION_STATEMENT = 1;
  public static final int FUNCTION_EXPRESSION = 2;
  public static final int FUNCTION_EXPRESSION_STATEMENT = 3;
  
  public static enum Form
  {
    FUNCTION,  GETTER,  SETTER;
    
    private Form() {} }
  
  private static final List<AstNode> NO_PARAMS = Collections.unmodifiableList(new ArrayList());
  
  private Name functionName;
  private List<AstNode> params;
  private AstNode body;
  private boolean isExpressionClosure;
  private Form functionForm = Form.FUNCTION;
  private int lp = -1;
  private int rp = -1;
  
  private int functionType;
  private boolean needsActivation;
  private boolean isGenerator;
  private List<Node> generatorResumePoints;
  private Map<Node, int[]> liveLocals;
  private AstNode memberExprNode;
  
  public FunctionNode()
  {
    type = 109;
  }
  


  public FunctionNode(int pos)
  {
    super(pos);type = 109;
  }
  
  public FunctionNode(int pos, Name name) {
    super(pos);type = 109;
    setFunctionName(name);
  }
  




  public Name getFunctionName()
  {
    return functionName;
  }
  





  public void setFunctionName(Name name)
  {
    functionName = name;
    if (name != null) {
      name.setParent(this);
    }
  }
  



  public String getName()
  {
    return functionName != null ? functionName.getIdentifier() : "";
  }
  





  public List<AstNode> getParams()
  {
    return params != null ? params : NO_PARAMS;
  }
  






  public void setParams(List<AstNode> params)
  {
    if (params == null) {
      this.params = null;
    } else {
      if (this.params != null)
        this.params.clear();
      for (AstNode param : params) {
        addParam(param);
      }
    }
  }
  







  public void addParam(AstNode param)
  {
    assertNotNull(param);
    if (params == null) {
      params = new ArrayList();
    }
    params.add(param);
    param.setParent(this);
  }
  




  public boolean isParam(AstNode node)
  {
    return params == null ? false : params.contains(node);
  }
  





  public AstNode getBody()
  {
    return body;
  }
  













  public void setBody(AstNode body)
  {
    assertNotNull(body);
    this.body = body;
    if (Boolean.TRUE.equals(body.getProp(25))) {
      setIsExpressionClosure(true);
    }
    int absEnd = body.getPosition() + body.getLength();
    body.setParent(this);
    setLength(absEnd - position);
    setEncodedSourceBounds(position, absEnd);
  }
  


  public int getLp()
  {
    return lp;
  }
  


  public void setLp(int lp)
  {
    this.lp = lp;
  }
  


  public int getRp()
  {
    return rp;
  }
  


  public void setRp(int rp)
  {
    this.rp = rp;
  }
  


  public void setParens(int lp, int rp)
  {
    this.lp = lp;
    this.rp = rp;
  }
  


  public boolean isExpressionClosure()
  {
    return isExpressionClosure;
  }
  


  public void setIsExpressionClosure(boolean isExpressionClosure)
  {
    this.isExpressionClosure = isExpressionClosure;
  }
  









  public boolean requiresActivation()
  {
    return true;
  }
  
  public void setRequiresActivation() {
    needsActivation = true;
  }
  
  public boolean isGenerator() {
    return isGenerator;
  }
  
  public void setIsGenerator() {
    isGenerator = true;
  }
  
  public void addResumptionPoint(Node target) {
    if (generatorResumePoints == null)
      generatorResumePoints = new ArrayList();
    generatorResumePoints.add(target);
  }
  
  public List<Node> getResumptionPoints() {
    return generatorResumePoints;
  }
  
  public Map<Node, int[]> getLiveLocals() {
    return liveLocals;
  }
  
  public void addLiveLocals(Node node, int[] locals) {
    if (liveLocals == null)
      liveLocals = new HashMap();
    liveLocals.put(node, locals);
  }
  
  public int addFunction(FunctionNode fnNode)
  {
    int result = super.addFunction(fnNode);
    if (getFunctionCount() > 0) {
      needsActivation = true;
    }
    return result;
  }
  


  public int getFunctionType()
  {
    return functionType;
  }
  
  public void setFunctionType(int type) {
    functionType = type;
  }
  
  public boolean isGetterOrSetter() {
    return (functionForm == Form.GETTER) || (functionForm == Form.SETTER);
  }
  
  public boolean isGetter() {
    return functionForm == Form.GETTER;
  }
  
  public boolean isSetter() {
    return functionForm == Form.SETTER;
  }
  
  public void setFunctionIsGetter() {
    functionForm = Form.GETTER;
  }
  
  public void setFunctionIsSetter() {
    functionForm = Form.SETTER;
  }
  









  public void setMemberExprNode(AstNode node)
  {
    memberExprNode = node;
    if (node != null)
      node.setParent(this);
  }
  
  public AstNode getMemberExprNode() {
    return memberExprNode;
  }
  
  public String toSource(int depth)
  {
    StringBuilder sb = new StringBuilder();
    if (!isGetterOrSetter()) {
      sb.append(makeIndent(depth));
      sb.append("function");
    }
    if (functionName != null) {
      sb.append(" ");
      sb.append(functionName.toSource(0));
    }
    if (params == null) {
      sb.append("() ");
    } else {
      sb.append("(");
      printList(params, sb);
      sb.append(") ");
    }
    if (isExpressionClosure) {
      AstNode body = getBody();
      if ((body.getLastChild() instanceof ReturnStatement))
      {
        body = ((ReturnStatement)body.getLastChild()).getReturnValue();
        sb.append(body.toSource(0));
        if (functionType == 1) {
          sb.append(";");
        }
      }
      else {
        sb.append(" ");
        sb.append(body.toSource(0));
      }
    } else {
      sb.append(getBody().toSource(depth).trim());
    }
    if ((functionType == 1) || (isGetterOrSetter())) {
      sb.append("\n");
    }
    return sb.toString();
  }
  




  public void visit(NodeVisitor v)
  {
    if (v.visit(this)) {
      if (functionName != null) {
        functionName.visit(v);
      }
      for (AstNode param : getParams()) {
        param.visit(v);
      }
      getBody().visit(v);
      if ((!isExpressionClosure) && 
        (memberExprNode != null)) {
        memberExprNode.visit(v);
      }
    }
  }
}
