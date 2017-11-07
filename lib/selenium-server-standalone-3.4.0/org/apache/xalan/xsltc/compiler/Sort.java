package org.apache.xalan.xsltc.compiler;

import java.util.ArrayList;
import java.util.Vector;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ANEWARRAY;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.CHECKCAST;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GETFIELD;
import org.apache.bcel.generic.ILOAD;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.NEW;
import org.apache.bcel.generic.NOP;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.PUTFIELD;
import org.apache.bcel.generic.TABLESWITCH;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.CompareGenerator;
import org.apache.xalan.xsltc.compiler.util.IntType;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.NodeSortRecordFactGenerator;
import org.apache.xalan.xsltc.compiler.util.NodeSortRecordGenerator;
import org.apache.xalan.xsltc.compiler.util.StringType;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;































final class Sort
  extends Instruction
  implements Closure
{
  private Expression _select;
  private AttributeValue _order;
  private AttributeValue _caseOrder;
  private AttributeValue _dataType;
  private AttributeValue _lang;
  private String _data = null;
  
  private String _className = null;
  private ArrayList _closureVars = null;
  private boolean _needsSortRecordFactory = false;
  


  Sort() {}
  

  public boolean inInnerClass()
  {
    return _className != null;
  }
  


  public Closure getParentClosure()
  {
    return null;
  }
  



  public String getInnerClassName()
  {
    return _className;
  }
  


  public void addVariable(VariableRefBase variableRef)
  {
    if (_closureVars == null) {
      _closureVars = new ArrayList();
    }
    

    if (!_closureVars.contains(variableRef)) {
      _closureVars.add(variableRef);
      _needsSortRecordFactory = true;
    }
  }
  

  private void setInnerClassName(String className)
  {
    _className = className;
  }
  



  public void parseContents(Parser parser)
  {
    SyntaxTreeNode parent = getParent();
    if ((!(parent instanceof ApplyTemplates)) && (!(parent instanceof ForEach)))
    {
      reportError(this, parser, "STRAY_SORT_ERR", null);
      return;
    }
    

    _select = parser.parseExpression(this, "select", "string(.)");
    

    String val = getAttribute("order");
    if (val.length() == 0) val = "ascending";
    _order = AttributeValue.create(this, val, parser);
    

    val = getAttribute("data-type");
    if (val.length() == 0) {
      try {
        org.apache.xalan.xsltc.compiler.util.Type type = _select.typeCheck(parser.getSymbolTable());
        if ((type instanceof IntType)) {
          val = "number";
        } else {
          val = "text";
        }
      } catch (TypeCheckError e) {
        val = "text";
      }
    }
    _dataType = AttributeValue.create(this, val, parser);
    
    val = getAttribute("lang");
    _lang = AttributeValue.create(this, val, parser);
    
    val = getAttribute("case-order");
    _caseOrder = AttributeValue.create(this, val, parser);
  }
  



  public org.apache.xalan.xsltc.compiler.util.Type typeCheck(SymbolTable stable)
    throws TypeCheckError
  {
    org.apache.xalan.xsltc.compiler.util.Type tselect = _select.typeCheck(stable);
    


    if (!(tselect instanceof StringType)) {
      _select = new CastExpr(_select, org.apache.xalan.xsltc.compiler.util.Type.String);
    }
    
    _order.typeCheck(stable);
    _caseOrder.typeCheck(stable);
    _dataType.typeCheck(stable);
    _lang.typeCheck(stable);
    return org.apache.xalan.xsltc.compiler.util.Type.Void;
  }
  




  public void translateSortType(ClassGenerator classGen, MethodGenerator methodGen)
  {
    _dataType.translate(classGen, methodGen);
  }
  
  public void translateSortOrder(ClassGenerator classGen, MethodGenerator methodGen)
  {
    _order.translate(classGen, methodGen);
  }
  
  public void translateCaseOrder(ClassGenerator classGen, MethodGenerator methodGen)
  {
    _caseOrder.translate(classGen, methodGen);
  }
  
  public void translateLang(ClassGenerator classGen, MethodGenerator methodGen)
  {
    _lang.translate(classGen, methodGen);
  }
  





  public void translateSelect(ClassGenerator classGen, MethodGenerator methodGen)
  {
    _select.translate(classGen, methodGen);
  }
  







  public void translate(ClassGenerator classGen, MethodGenerator methodGen) {}
  






  public static void translateSortIterator(ClassGenerator classGen, MethodGenerator methodGen, Expression nodeSet, Vector sortObjects)
  {
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = methodGen.getInstructionList();
    

    int init = cpg.addMethodref("org.apache.xalan.xsltc.dom.SortingIterator", "<init>", "(Lorg/apache/xml/dtm/DTMAxisIterator;Lorg/apache/xalan/xsltc/dom/NodeSortRecordFactory;)V");
    













    LocalVariableGen nodesTemp = methodGen.addLocalVariable("sort_tmp1", Util.getJCRefType("Lorg/apache/xml/dtm/DTMAxisIterator;"), null, null);
    



    LocalVariableGen sortRecordFactoryTemp = methodGen.addLocalVariable("sort_tmp2", Util.getJCRefType("Lorg/apache/xalan/xsltc/dom/NodeSortRecordFactory;"), null, null);
    




    if (nodeSet == null) {
      int children = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getAxisIterator", "(I)Lorg/apache/xml/dtm/DTMAxisIterator;");
      


      il.append(methodGen.loadDOM());
      il.append(new PUSH(cpg, 3));
      il.append(new INVOKEINTERFACE(children, 2));
    }
    else {
      nodeSet.translate(classGen, methodGen);
    }
    
    nodesTemp.setStart(il.append(new ASTORE(nodesTemp.getIndex())));
    


    compileSortRecordFactory(sortObjects, classGen, methodGen);
    sortRecordFactoryTemp.setStart(il.append(new ASTORE(sortRecordFactoryTemp.getIndex())));
    

    il.append(new NEW(cpg.addClass("org.apache.xalan.xsltc.dom.SortingIterator")));
    il.append(DUP);
    nodesTemp.setEnd(il.append(new ALOAD(nodesTemp.getIndex())));
    sortRecordFactoryTemp.setEnd(il.append(new ALOAD(sortRecordFactoryTemp.getIndex())));
    
    il.append(new INVOKESPECIAL(init));
  }
  






  public static void compileSortRecordFactory(Vector sortObjects, ClassGenerator classGen, MethodGenerator methodGen)
  {
    String sortRecordClass = compileSortRecord(sortObjects, classGen, methodGen);
    

    boolean needsSortRecordFactory = false;
    int nsorts = sortObjects.size();
    for (int i = 0; i < nsorts; i++) {
      Sort sort = (Sort)sortObjects.elementAt(i);
      needsSortRecordFactory |= _needsSortRecordFactory;
    }
    
    String sortRecordFactoryClass = "org/apache/xalan/xsltc/dom/NodeSortRecordFactory";
    if (needsSortRecordFactory) {
      sortRecordFactoryClass = compileSortRecordFactory(sortObjects, classGen, methodGen, sortRecordClass);
    }
    


    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = methodGen.getInstructionList();
    










    LocalVariableGen sortOrderTemp = methodGen.addLocalVariable("sort_order_tmp", Util.getJCRefType("[Ljava/lang/String;"), null, null);
    


    il.append(new PUSH(cpg, nsorts));
    il.append(new ANEWARRAY(cpg.addClass("java.lang.String")));
    for (int level = 0; level < nsorts; level++) {
      Sort sort = (Sort)sortObjects.elementAt(level);
      il.append(DUP);
      il.append(new PUSH(cpg, level));
      sort.translateSortOrder(classGen, methodGen);
      il.append(AASTORE);
    }
    sortOrderTemp.setStart(il.append(new ASTORE(sortOrderTemp.getIndex())));
    
    LocalVariableGen sortTypeTemp = methodGen.addLocalVariable("sort_type_tmp", Util.getJCRefType("[Ljava/lang/String;"), null, null);
    


    il.append(new PUSH(cpg, nsorts));
    il.append(new ANEWARRAY(cpg.addClass("java.lang.String")));
    for (int level = 0; level < nsorts; level++) {
      Sort sort = (Sort)sortObjects.elementAt(level);
      il.append(DUP);
      il.append(new PUSH(cpg, level));
      sort.translateSortType(classGen, methodGen);
      il.append(AASTORE);
    }
    sortTypeTemp.setStart(il.append(new ASTORE(sortTypeTemp.getIndex())));
    
    LocalVariableGen sortLangTemp = methodGen.addLocalVariable("sort_lang_tmp", Util.getJCRefType("[Ljava/lang/String;"), null, null);
    


    il.append(new PUSH(cpg, nsorts));
    il.append(new ANEWARRAY(cpg.addClass("java.lang.String")));
    for (int level = 0; level < nsorts; level++) {
      Sort sort = (Sort)sortObjects.elementAt(level);
      il.append(DUP);
      il.append(new PUSH(cpg, level));
      sort.translateLang(classGen, methodGen);
      il.append(AASTORE);
    }
    sortLangTemp.setStart(il.append(new ASTORE(sortLangTemp.getIndex())));
    
    LocalVariableGen sortCaseOrderTemp = methodGen.addLocalVariable("sort_case_order_tmp", Util.getJCRefType("[Ljava/lang/String;"), null, null);
    


    il.append(new PUSH(cpg, nsorts));
    il.append(new ANEWARRAY(cpg.addClass("java.lang.String")));
    for (int level = 0; level < nsorts; level++) {
      Sort sort = (Sort)sortObjects.elementAt(level);
      il.append(DUP);
      il.append(new PUSH(cpg, level));
      sort.translateCaseOrder(classGen, methodGen);
      il.append(AASTORE);
    }
    sortCaseOrderTemp.setStart(il.append(new ASTORE(sortCaseOrderTemp.getIndex())));
    

    il.append(new NEW(cpg.addClass(sortRecordFactoryClass)));
    il.append(DUP);
    il.append(methodGen.loadDOM());
    il.append(new PUSH(cpg, sortRecordClass));
    il.append(classGen.loadTranslet());
    
    sortOrderTemp.setEnd(il.append(new ALOAD(sortOrderTemp.getIndex())));
    sortTypeTemp.setEnd(il.append(new ALOAD(sortTypeTemp.getIndex())));
    sortLangTemp.setEnd(il.append(new ALOAD(sortLangTemp.getIndex())));
    sortCaseOrderTemp.setEnd(il.append(new ALOAD(sortCaseOrderTemp.getIndex())));
    

    il.append(new INVOKESPECIAL(cpg.addMethodref(sortRecordFactoryClass, "<init>", "(Lorg/apache/xalan/xsltc/DOM;Ljava/lang/String;Lorg/apache/xalan/xsltc/Translet;[Ljava/lang/String;[Ljava/lang/String;[Ljava/lang/String;[Ljava/lang/String;)V")));
    









    ArrayList dups = new ArrayList();
    
    for (int j = 0; j < nsorts; j++) {
      Sort sort = (Sort)sortObjects.get(j);
      int length = _closureVars == null ? 0 : _closureVars.size();
      

      for (int i = 0; i < length; i++) {
        VariableRefBase varRef = (VariableRefBase)_closureVars.get(i);
        

        if (!dups.contains(varRef))
        {
          VariableBase var = varRef.getVariable();
          

          il.append(DUP);
          il.append(var.loadInstruction());
          il.append(new PUTFIELD(cpg.addFieldref(sortRecordFactoryClass, var.getEscapedName(), var.getType().toSignature())));
          

          dups.add(varRef);
        }
      }
    }
  }
  

  public static String compileSortRecordFactory(Vector sortObjects, ClassGenerator classGen, MethodGenerator methodGen, String sortRecordClass)
  {
    XSLTC xsltc = ((Sort)sortObjects.firstElement()).getXSLTC();
    String className = xsltc.getHelperClassName();
    
    NodeSortRecordFactGenerator sortRecordFactory = new NodeSortRecordFactGenerator(className, "org/apache/xalan/xsltc/dom/NodeSortRecordFactory", className + ".java", 49, new String[0], classGen.getStylesheet());
    






    ConstantPoolGen cpg = sortRecordFactory.getConstantPool();
    

    int nsorts = sortObjects.size();
    ArrayList dups = new ArrayList();
    
    for (int j = 0; j < nsorts; j++) {
      Sort sort = (Sort)sortObjects.get(j);
      int length = _closureVars == null ? 0 : _closureVars.size();
      

      for (int i = 0; i < length; i++) {
        VariableRefBase varRef = (VariableRefBase)_closureVars.get(i);
        

        if (!dups.contains(varRef))
        {
          VariableBase var = varRef.getVariable();
          sortRecordFactory.addField(new Field(1, cpg.addUtf8(var.getEscapedName()), cpg.addUtf8(var.getType().toSignature()), null, cpg.getConstantPool()));
          


          dups.add(varRef);
        }
      }
    }
    
    org.apache.bcel.generic.Type[] argTypes = new org.apache.bcel.generic.Type[7];
    
    argTypes[0] = Util.getJCRefType("Lorg/apache/xalan/xsltc/DOM;");
    argTypes[1] = Util.getJCRefType("Ljava/lang/String;");
    argTypes[2] = Util.getJCRefType("Lorg/apache/xalan/xsltc/Translet;");
    argTypes[3] = Util.getJCRefType("[Ljava/lang/String;");
    argTypes[4] = Util.getJCRefType("[Ljava/lang/String;");
    argTypes[5] = Util.getJCRefType("[Ljava/lang/String;");
    argTypes[6] = Util.getJCRefType("[Ljava/lang/String;");
    
    String[] argNames = new String[7];
    argNames[0] = "document";
    argNames[1] = "className";
    argNames[2] = "translet";
    argNames[3] = "order";
    argNames[4] = "type";
    argNames[5] = "lang";
    argNames[6] = "case_order";
    

    InstructionList il = new InstructionList();
    MethodGenerator constructor = new MethodGenerator(1, org.apache.bcel.generic.Type.VOID, argTypes, argNames, "<init>", className, il, cpg);
    





    il.append(ALOAD_0);
    il.append(ALOAD_1);
    il.append(ALOAD_2);
    il.append(new ALOAD(3));
    il.append(new ALOAD(4));
    il.append(new ALOAD(5));
    il.append(new ALOAD(6));
    il.append(new ALOAD(7));
    il.append(new INVOKESPECIAL(cpg.addMethodref("org/apache/xalan/xsltc/dom/NodeSortRecordFactory", "<init>", "(Lorg/apache/xalan/xsltc/DOM;Ljava/lang/String;Lorg/apache/xalan/xsltc/Translet;[Ljava/lang/String;[Ljava/lang/String;[Ljava/lang/String;[Ljava/lang/String;)V")));
    







    il.append(RETURN);
    

    il = new InstructionList();
    MethodGenerator makeNodeSortRecord = new MethodGenerator(1, Util.getJCRefType("Lorg/apache/xalan/xsltc/dom/NodeSortRecord;"), new org.apache.bcel.generic.Type[] { org.apache.bcel.generic.Type.INT, org.apache.bcel.generic.Type.INT }, new String[] { "node", "last" }, "makeNodeSortRecord", className, il, cpg);
    







    il.append(ALOAD_0);
    il.append(ILOAD_1);
    il.append(ILOAD_2);
    il.append(new INVOKESPECIAL(cpg.addMethodref("org/apache/xalan/xsltc/dom/NodeSortRecordFactory", "makeNodeSortRecord", "(II)Lorg/apache/xalan/xsltc/dom/NodeSortRecord;")));
    
    il.append(DUP);
    il.append(new CHECKCAST(cpg.addClass(sortRecordClass)));
    

    int ndups = dups.size();
    for (int i = 0; i < ndups; i++) {
      VariableRefBase varRef = (VariableRefBase)dups.get(i);
      VariableBase var = varRef.getVariable();
      org.apache.xalan.xsltc.compiler.util.Type varType = var.getType();
      
      il.append(DUP);
      

      il.append(ALOAD_0);
      il.append(new GETFIELD(cpg.addFieldref(className, var.getEscapedName(), varType.toSignature())));
      



      il.append(new PUTFIELD(cpg.addFieldref(sortRecordClass, var.getEscapedName(), varType.toSignature())));
    }
    

    il.append(POP);
    il.append(ARETURN);
    
    constructor.setMaxLocals();
    constructor.setMaxStack();
    sortRecordFactory.addMethod(constructor);
    makeNodeSortRecord.setMaxLocals();
    makeNodeSortRecord.setMaxStack();
    sortRecordFactory.addMethod(makeNodeSortRecord);
    xsltc.dumpClass(sortRecordFactory.getJavaClass());
    
    return className;
  }
  




  private static String compileSortRecord(Vector sortObjects, ClassGenerator classGen, MethodGenerator methodGen)
  {
    XSLTC xsltc = ((Sort)sortObjects.firstElement()).getXSLTC();
    String className = xsltc.getHelperClassName();
    

    NodeSortRecordGenerator sortRecord = new NodeSortRecordGenerator(className, "org.apache.xalan.xsltc.dom.NodeSortRecord", "sort$0.java", 49, new String[0], classGen.getStylesheet());
    






    ConstantPoolGen cpg = sortRecord.getConstantPool();
    

    int nsorts = sortObjects.size();
    ArrayList dups = new ArrayList();
    
    for (int j = 0; j < nsorts; j++) {
      Sort sort = (Sort)sortObjects.get(j);
      

      sort.setInnerClassName(className);
      
      int length = _closureVars == null ? 0 : _closureVars.size();
      
      for (int i = 0; i < length; i++) {
        VariableRefBase varRef = (VariableRefBase)_closureVars.get(i);
        

        if (!dups.contains(varRef))
        {
          VariableBase var = varRef.getVariable();
          sortRecord.addField(new Field(1, cpg.addUtf8(var.getEscapedName()), cpg.addUtf8(var.getType().toSignature()), null, cpg.getConstantPool()));
          


          dups.add(varRef);
        }
      }
    }
    MethodGenerator init = compileInit(sortObjects, sortRecord, cpg, className);
    
    MethodGenerator extract = compileExtract(sortObjects, sortRecord, cpg, className);
    
    sortRecord.addMethod(init);
    sortRecord.addMethod(extract);
    
    xsltc.dumpClass(sortRecord.getJavaClass());
    return className;
  }
  








  private static MethodGenerator compileInit(Vector sortObjects, NodeSortRecordGenerator sortRecord, ConstantPoolGen cpg, String className)
  {
    InstructionList il = new InstructionList();
    MethodGenerator init = new MethodGenerator(1, org.apache.bcel.generic.Type.VOID, null, null, "<init>", className, il, cpg);
    





    il.append(ALOAD_0);
    il.append(new INVOKESPECIAL(cpg.addMethodref("org.apache.xalan.xsltc.dom.NodeSortRecord", "<init>", "()V")));
    



    il.append(RETURN);
    
    return init;
  }
  






  private static MethodGenerator compileExtract(Vector sortObjects, NodeSortRecordGenerator sortRecord, ConstantPoolGen cpg, String className)
  {
    InstructionList il = new InstructionList();
    

    CompareGenerator extractMethod = new CompareGenerator(17, org.apache.bcel.generic.Type.STRING, new org.apache.bcel.generic.Type[] { Util.getJCRefType("Lorg/apache/xalan/xsltc/DOM;"), org.apache.bcel.generic.Type.INT, org.apache.bcel.generic.Type.INT, Util.getJCRefType("Lorg/apache/xalan/xsltc/runtime/AbstractTranslet;"), org.apache.bcel.generic.Type.INT }, new String[] { "dom", "current", "level", "translet", "last" }, "extractValueFromDOM", className, il, cpg);
    

















    int levels = sortObjects.size();
    int[] match = new int[levels];
    InstructionHandle[] target = new InstructionHandle[levels];
    InstructionHandle tblswitch = null;
    

    if (levels > 1)
    {
      il.append(new ILOAD(extractMethod.getLocalIndex("level")));
      
      tblswitch = il.append(new NOP());
    }
    

    for (int level = 0; level < levels; level++) {
      match[level] = level;
      Sort sort = (Sort)sortObjects.elementAt(level);
      target[level] = il.append(NOP);
      sort.translateSelect(sortRecord, extractMethod);
      il.append(ARETURN);
    }
    

    if (levels > 1)
    {
      InstructionHandle defaultTarget = il.append(new PUSH(cpg, ""));
      
      il.insert(tblswitch, new TABLESWITCH(match, target, defaultTarget));
      il.append(ARETURN);
    }
    
    return extractMethod;
  }
}
