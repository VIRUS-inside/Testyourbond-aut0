package org.apache.xalan.xsltc.compiler;

import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.IF_ICMPEQ;
import org.apache.bcel.generic.ILOAD;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.PUSH;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;


































final class Whitespace
  extends TopLevelElement
{
  public static final int USE_PREDICATE = 0;
  public static final int STRIP_SPACE = 1;
  public static final int PRESERVE_SPACE = 2;
  public static final int RULE_NONE = 0;
  public static final int RULE_ELEMENT = 1;
  public static final int RULE_NAMESPACE = 2;
  public static final int RULE_ALL = 3;
  private String _elementList;
  private int _action;
  private int _importPrecedence;
  Whitespace() {}
  
  private static final class WhitespaceRule
  {
    private final int _action;
    private String _namespace;
    private String _element;
    private int _type;
    private int _priority;
    
    public WhitespaceRule(int action, String element, int precedence)
    {
      _action = action;
      

      int colon = element.lastIndexOf(':');
      if (colon >= 0) {
        _namespace = element.substring(0, colon);
        _element = element.substring(colon + 1, element.length());
      }
      else {
        _namespace = "";
        _element = element;
      }
      

      _priority = (precedence << 2);
      

      if (_element.equals("*")) {
        if (_namespace == "") {
          _type = 3;
          _priority += 2;
        }
        else {
          _type = 2;
          _priority += 1;
        }
      }
      else {
        _type = 1;
      }
    }
    


    public int compareTo(WhitespaceRule other)
    {
      return _priority > _priority ? 1 : _priority < _priority ? -1 : 0;
    }
    


    public int getAction() { return _action; }
    public int getStrength() { return _type; }
    public int getPriority() { return _priority; }
    public String getElement() { return _element; }
    public String getNamespace() { return _namespace; }
  }
  




  public void parseContents(Parser parser)
  {
    _action = (_qname.getLocalPart().endsWith("strip-space") ? 1 : 2);
    


    _importPrecedence = parser.getCurrentImportPrecedence();
    

    _elementList = getAttribute("elements");
    if ((_elementList == null) || (_elementList.length() == 0)) {
      reportError(this, parser, "REQUIRED_ATTR_ERR", "elements");
      return;
    }
    
    SymbolTable stable = parser.getSymbolTable();
    StringTokenizer list = new StringTokenizer(_elementList);
    StringBuffer elements = new StringBuffer("");
    
    while (list.hasMoreElements()) {
      String token = list.nextToken();
      

      int col = token.indexOf(':');
      
      if (col != -1) {
        String namespace = lookupNamespace(token.substring(0, col));
        if (namespace != null) {
          elements.append(namespace + ":" + token.substring(col + 1, token.length()));
        }
        else {
          elements.append(token);
        }
      } else {
        elements.append(token);
      }
      
      if (list.hasMoreElements())
        elements.append(" ");
    }
    _elementList = elements.toString();
  }
  




  public Vector getRules()
  {
    Vector rules = new Vector();
    
    StringTokenizer list = new StringTokenizer(_elementList);
    while (list.hasMoreElements()) {
      rules.add(new WhitespaceRule(_action, list.nextToken(), _importPrecedence));
    }
    

    return rules;
  }
  





  private static WhitespaceRule findContradictingRule(Vector rules, WhitespaceRule rule)
  {
    for (int i = 0; i < rules.size(); i++)
    {
      WhitespaceRule currentRule = (WhitespaceRule)rules.elementAt(i);
      
      if (currentRule == rule) {
        return null;
      }
      





      switch (currentRule.getStrength()) {
      case 3: 
        return currentRule;
      
      case 1: 
        if (!rule.getElement().equals(currentRule.getElement())) {
          break;
        }
      
      case 2: 
        if (rule.getNamespace().equals(currentRule.getNamespace())) {
          return currentRule;
        }
        break;
      }
    }
    return null;
  }
  





  private static int prioritizeRules(Vector rules)
  {
    int defaultAction = 2;
    

    quicksort(rules, 0, rules.size() - 1);
    



    boolean strip = false;
    for (int i = 0; i < rules.size(); i++) {
      WhitespaceRule currentRule = (WhitespaceRule)rules.elementAt(i);
      if (currentRule.getAction() == 1) {
        strip = true;
      }
    }
    
    if (!strip) {
      rules.removeAllElements();
      return 2;
    }
    

    for (int idx = 0; idx < rules.size();) {
      WhitespaceRule currentRule = (WhitespaceRule)rules.elementAt(idx);
      

      if (findContradictingRule(rules, currentRule) != null) {
        rules.remove(idx);
      }
      else
      {
        if (currentRule.getStrength() == 3) {
          defaultAction = currentRule.getAction();
          for (int i = idx; i < rules.size(); i++) {
            rules.removeElementAt(i);
          }
        }
        
        idx++;
      }
    }
    

    if (rules.size() == 0) {
      return defaultAction;
    }
    

    do
    {
      WhitespaceRule currentRule = (WhitespaceRule)rules.lastElement();
      if (currentRule.getAction() != defaultAction) break;
      rules.removeElementAt(rules.size() - 1);



    }
    while (rules.size() > 0);
    

    return defaultAction;
  }
  

  public static void compileStripSpace(BranchHandle[] strip, int sCount, InstructionList il)
  {
    InstructionHandle target = il.append(ICONST_1);
    il.append(IRETURN);
    for (int i = 0; i < sCount; i++) {
      strip[i].setTarget(target);
    }
  }
  

  public static void compilePreserveSpace(BranchHandle[] preserve, int pCount, InstructionList il)
  {
    InstructionHandle target = il.append(ICONST_0);
    il.append(IRETURN);
    for (int i = 0; i < pCount; i++) {
      preserve[i].setTarget(target);
    }
  }
  
















  private static void compilePredicate(Vector rules, int defaultAction, ClassGenerator classGen)
  {
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = new InstructionList();
    XSLTC xsltc = classGen.getParser().getXSLTC();
    

    MethodGenerator stripSpace = new MethodGenerator(17, org.apache.bcel.generic.Type.BOOLEAN, new org.apache.bcel.generic.Type[] { Util.getJCRefType("Lorg/apache/xalan/xsltc/DOM;"), org.apache.bcel.generic.Type.INT, org.apache.bcel.generic.Type.INT }, new String[] { "dom", "node", "type" }, "stripSpace", classGen.getClassName(), il, cpg);
    









    classGen.addInterface("org/apache/xalan/xsltc/StripFilter");
    
    int paramDom = stripSpace.getLocalIndex("dom");
    int paramCurrent = stripSpace.getLocalIndex("node");
    int paramType = stripSpace.getLocalIndex("type");
    
    BranchHandle[] strip = new BranchHandle[rules.size()];
    BranchHandle[] preserve = new BranchHandle[rules.size()];
    int sCount = 0;
    int pCount = 0;
    

    for (int i = 0; i < rules.size(); i++)
    {
      WhitespaceRule rule = (WhitespaceRule)rules.elementAt(i);
      

      int gns = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getNamespaceName", "(I)Ljava/lang/String;");
      


      int strcmp = cpg.addMethodref("java/lang/String", "compareTo", "(Ljava/lang/String;)I");
      



      if (rule.getStrength() == 2) {
        il.append(new ALOAD(paramDom));
        il.append(new ILOAD(paramCurrent));
        il.append(new INVOKEINTERFACE(gns, 2));
        il.append(new PUSH(cpg, rule.getNamespace()));
        il.append(new INVOKEVIRTUAL(strcmp));
        il.append(ICONST_0);
        
        if (rule.getAction() == 1) {
          strip[(sCount++)] = il.append(new IF_ICMPEQ(null));
        }
        else {
          preserve[(pCount++)] = il.append(new IF_ICMPEQ(null));
        }
        
      }
      else if (rule.getStrength() == 1)
      {
        Parser parser = classGen.getParser();
        QName qname;
        QName qname; if (rule.getNamespace() != "") {
          qname = parser.getQName(rule.getNamespace(), null, rule.getElement());
        }
        else {
          qname = parser.getQName(rule.getElement());
        }
        
        int elementType = xsltc.registerElement(qname);
        il.append(new ILOAD(paramType));
        il.append(new PUSH(cpg, elementType));
        

        if (rule.getAction() == 1) {
          strip[(sCount++)] = il.append(new IF_ICMPEQ(null));
        } else {
          preserve[(pCount++)] = il.append(new IF_ICMPEQ(null));
        }
      }
    }
    if (defaultAction == 1) {
      compileStripSpace(strip, sCount, il);
      compilePreserveSpace(preserve, pCount, il);
    }
    else {
      compilePreserveSpace(preserve, pCount, il);
      compileStripSpace(strip, sCount, il);
    }
    
    classGen.addMethod(stripSpace);
  }
  



  private static void compileDefault(int defaultAction, ClassGenerator classGen)
  {
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = new InstructionList();
    XSLTC xsltc = classGen.getParser().getXSLTC();
    

    MethodGenerator stripSpace = new MethodGenerator(17, org.apache.bcel.generic.Type.BOOLEAN, new org.apache.bcel.generic.Type[] { Util.getJCRefType("Lorg/apache/xalan/xsltc/DOM;"), org.apache.bcel.generic.Type.INT, org.apache.bcel.generic.Type.INT }, new String[] { "dom", "node", "type" }, "stripSpace", classGen.getClassName(), il, cpg);
    









    classGen.addInterface("org/apache/xalan/xsltc/StripFilter");
    
    if (defaultAction == 1) {
      il.append(ICONST_1);
    } else
      il.append(ICONST_0);
    il.append(IRETURN);
    
    classGen.addMethod(stripSpace);
  }
  










  public static int translateRules(Vector rules, ClassGenerator classGen)
  {
    int defaultAction = prioritizeRules(rules);
    
    if (rules.size() == 0) {
      compileDefault(defaultAction, classGen);
      return defaultAction;
    }
    
    compilePredicate(rules, defaultAction, classGen);
    
    return 0;
  }
  


  private static void quicksort(Vector rules, int p, int r)
  {
    while (p < r) {
      int q = partition(rules, p, r);
      quicksort(rules, p, q);
      p = q + 1;
    }
  }
  


  private static int partition(Vector rules, int p, int r)
  {
    WhitespaceRule x = (WhitespaceRule)rules.elementAt(p + r >>> 1);
    int i = p - 1;int j = r + 1;
    for (;;) {
      if (x.compareTo((WhitespaceRule)rules.elementAt(--j)) >= 0)
      {
        while (x.compareTo((WhitespaceRule)rules.elementAt(++i)) > 0) {}
        
        if (i >= j) break;
        WhitespaceRule tmp = (WhitespaceRule)rules.elementAt(i);
        rules.setElementAt(rules.elementAt(j), i);
        rules.setElementAt(tmp, j);
      }
    }
    return j;
  }
  



  public org.apache.xalan.xsltc.compiler.util.Type typeCheck(SymbolTable stable)
    throws TypeCheckError
  {
    return org.apache.xalan.xsltc.compiler.util.Type.Void;
  }
  
  public void translate(ClassGenerator classGen, MethodGenerator methodGen) {}
}
