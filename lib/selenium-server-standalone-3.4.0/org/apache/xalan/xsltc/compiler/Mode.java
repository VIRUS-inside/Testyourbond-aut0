package org.apache.xalan.xsltc.compiler;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.DUP;
import org.apache.bcel.generic.GOTO_W;
import org.apache.bcel.generic.IFLT;
import org.apache.bcel.generic.ILOAD;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.ISTORE;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.SWITCH;
import org.apache.bcel.generic.TargetLostException;
import org.apache.bcel.generic.Type;
import org.apache.bcel.util.InstructionFinder;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.NamedMethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Util;




















































final class Mode
  implements Constants
{
  private final QName _name;
  private final Stylesheet _stylesheet;
  private final String _methodName;
  private Vector _templates;
  private Vector _childNodeGroup = null;
  



  private TestSeq _childNodeTestSeq = null;
  



  private Vector _attribNodeGroup = null;
  



  private TestSeq _attribNodeTestSeq = null;
  



  private Vector _idxGroup = null;
  



  private TestSeq _idxTestSeq = null;
  




  private Vector[] _patternGroups;
  



  private TestSeq[] _testSeq;
  



  private Hashtable _neededTemplates = new Hashtable();
  



  private Hashtable _namedTemplates = new Hashtable();
  



  private Hashtable _templateIHs = new Hashtable();
  



  private Hashtable _templateILs = new Hashtable();
  



  private LocationPathPattern _rootPattern = null;
  




  private Hashtable _importLevels = null;
  



  private Hashtable _keys = null;
  





  private int _currentIndex;
  





  public Mode(QName name, Stylesheet stylesheet, String suffix)
  {
    _name = name;
    _stylesheet = stylesheet;
    _methodName = ("applyTemplates" + suffix);
    _templates = new Vector();
    _patternGroups = new Vector[32];
  }
  






  public String functionName()
  {
    return _methodName;
  }
  
  public String functionName(int min, int max) {
    if (_importLevels == null) {
      _importLevels = new Hashtable();
    }
    _importLevels.put(new Integer(max), new Integer(min));
    return _methodName + '_' + max;
  }
  


  private String getClassName()
  {
    return _stylesheet.getClassName();
  }
  
  public Stylesheet getStylesheet() {
    return _stylesheet;
  }
  
  public void addTemplate(Template template) {
    _templates.addElement(template);
  }
  
  private Vector quicksort(Vector templates, int p, int r) {
    if (p < r) {
      int q = partition(templates, p, r);
      quicksort(templates, p, q);
      quicksort(templates, q + 1, r);
    }
    return templates;
  }
  
  private int partition(Vector templates, int p, int r) {
    Template x = (Template)templates.elementAt(p);
    int i = p - 1;
    int j = r + 1;
    for (;;) {
      if (x.compareTo((Template)templates.elementAt(--j)) <= 0) {
        while (x.compareTo((Template)templates.elementAt(++i)) < 0) {}
        if (i >= j) break;
        templates.set(j, templates.set(i, templates.elementAt(j)));
      }
    }
    return j;
  }
  




  public void processPatterns(Hashtable keys)
  {
    _keys = keys;
    










    _templates = quicksort(_templates, 0, _templates.size() - 1);
    











    Enumeration templates = _templates.elements();
    while (templates.hasMoreElements())
    {
      Template template = (Template)templates.nextElement();
      





      if ((template.isNamed()) && (!template.disabled())) {
        _namedTemplates.put(template, this);
      }
      

      Pattern pattern = template.getPattern();
      if (pattern != null) {
        flattenAlternative(pattern, template, keys);
      }
    }
    prepareTestSequences();
  }
  









  private void flattenAlternative(Pattern pattern, Template template, Hashtable keys)
  {
    if ((pattern instanceof IdKeyPattern)) {
      IdKeyPattern idkey = (IdKeyPattern)pattern;
      idkey.setTemplate(template);
      if (_idxGroup == null) _idxGroup = new Vector();
      _idxGroup.add(pattern);

    }
    else if ((pattern instanceof AlternativePattern)) {
      AlternativePattern alt = (AlternativePattern)pattern;
      flattenAlternative(alt.getLeft(), template, keys);
      flattenAlternative(alt.getRight(), template, keys);

    }
    else if ((pattern instanceof LocationPathPattern)) {
      LocationPathPattern lpp = (LocationPathPattern)pattern;
      lpp.setTemplate(template);
      addPatternToGroup(lpp);
    }
  }
  




  private void addPatternToGroup(LocationPathPattern lpp)
  {
    if ((lpp instanceof IdKeyPattern)) {
      addPattern(-1, lpp);

    }
    else
    {
      StepPattern kernel = lpp.getKernelPattern();
      if (kernel != null) {
        addPattern(kernel.getNodeType(), lpp);
      }
      else if ((_rootPattern == null) || (lpp.noSmallerThan(_rootPattern)))
      {
        _rootPattern = lpp;
      }
    }
  }
  



  private void addPattern(int kernelType, LocationPathPattern pattern)
  {
    int oldLength = _patternGroups.length;
    if (kernelType >= oldLength) {
      Vector[] newGroups = new Vector[kernelType * 2];
      System.arraycopy(_patternGroups, 0, newGroups, 0, oldLength);
      _patternGroups = newGroups;
    }
    
    Vector patterns;
    
    Vector patterns;
    if (kernelType == -1) { Vector patterns;
      if (pattern.getAxis() == 2) {
        patterns = _attribNodeGroup == null ? (this._attribNodeGroup = new Vector(2)) : _attribNodeGroup;
      }
      else
      {
        patterns = _childNodeGroup == null ? (this._childNodeGroup = new Vector(2)) : _childNodeGroup;
      }
    }
    else
    {
      patterns = _patternGroups[kernelType] == null ? (_patternGroups[kernelType] =  = new Vector(2)) : _patternGroups[kernelType];
    }
    


    if (patterns.size() == 0) {
      patterns.addElement(pattern);
    }
    else {
      boolean inserted = false;
      for (int i = 0; i < patterns.size(); i++) {
        LocationPathPattern lppToCompare = (LocationPathPattern)patterns.elementAt(i);
        

        if (pattern.noSmallerThan(lppToCompare)) {
          inserted = true;
          patterns.insertElementAt(pattern, i);
          break;
        }
      }
      if (!inserted) {
        patterns.addElement(pattern);
      }
    }
  }
  



  private void completeTestSequences(int nodeType, Vector patterns)
  {
    if (patterns != null) {
      if (_patternGroups[nodeType] == null) {
        _patternGroups[nodeType] = patterns;
      }
      else {
        int m = patterns.size();
        for (int j = 0; j < m; j++) {
          addPattern(nodeType, (LocationPathPattern)patterns.elementAt(j));
        }
      }
    }
  }
  





  private void prepareTestSequences()
  {
    Vector starGroup = _patternGroups[1];
    Vector atStarGroup = _patternGroups[2];
    

    completeTestSequences(3, _childNodeGroup);
    

    completeTestSequences(1, _childNodeGroup);
    

    completeTestSequences(7, _childNodeGroup);
    

    completeTestSequences(8, _childNodeGroup);
    

    completeTestSequences(2, _attribNodeGroup);
    
    Vector names = _stylesheet.getXSLTC().getNamesIndex();
    if ((starGroup != null) || (atStarGroup != null) || (_childNodeGroup != null) || (_attribNodeGroup != null))
    {

      int n = _patternGroups.length;
      

      for (int i = 14; i < n; i++) {
        if (_patternGroups[i] != null)
        {
          String name = (String)names.elementAt(i - 14);
          
          if (isAttributeName(name))
          {
            completeTestSequences(i, atStarGroup);
            

            completeTestSequences(i, _attribNodeGroup);
          }
          else
          {
            completeTestSequences(i, starGroup);
            

            completeTestSequences(i, _childNodeGroup);
          }
        }
      }
    }
    _testSeq = new TestSeq[14 + names.size()];
    
    int n = _patternGroups.length;
    for (int i = 0; i < n; i++) {
      Vector patterns = _patternGroups[i];
      if (patterns != null) {
        TestSeq testSeq = new TestSeq(patterns, i, this);
        
        testSeq.reduce();
        _testSeq[i] = testSeq;
        testSeq.findTemplates(_neededTemplates);
      }
    }
    
    if ((_childNodeGroup != null) && (_childNodeGroup.size() > 0)) {
      _childNodeTestSeq = new TestSeq(_childNodeGroup, -1, this);
      _childNodeTestSeq.reduce();
      _childNodeTestSeq.findTemplates(_neededTemplates);
    }
    








    if ((_idxGroup != null) && (_idxGroup.size() > 0)) {
      _idxTestSeq = new TestSeq(_idxGroup, this);
      _idxTestSeq.reduce();
      _idxTestSeq.findTemplates(_neededTemplates);
    }
    
    if (_rootPattern != null)
    {
      _neededTemplates.put(_rootPattern.getTemplate(), this);
    }
  }
  
  private void compileNamedTemplate(Template template, ClassGenerator classGen)
  {
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = new InstructionList();
    String methodName = Util.escape(template.getName().toString());
    
    int numParams = 0;
    if (template.isSimpleNamedTemplate()) {
      Vector parameters = template.getParameters();
      numParams = parameters.size();
    }
    

    Type[] types = new Type[4 + numParams];
    
    String[] names = new String[4 + numParams];
    types[0] = Util.getJCRefType("Lorg/apache/xalan/xsltc/DOM;");
    types[1] = Util.getJCRefType("Lorg/apache/xml/dtm/DTMAxisIterator;");
    types[2] = Util.getJCRefType(TRANSLET_OUTPUT_SIG);
    types[3] = Type.INT;
    names[0] = "document";
    names[1] = "iterator";
    names[2] = "handler";
    names[3] = "node";
    



    for (int i = 4; i < 4 + numParams; i++) {
      types[i] = Util.getJCRefType("Ljava/lang/Object;");
      names[i] = ("param" + String.valueOf(i - 4));
    }
    
    NamedMethodGenerator methodGen = new NamedMethodGenerator(1, Type.VOID, types, names, methodName, getClassName(), il, cpg);
    




    il.append(template.compile(classGen, methodGen));
    il.append(RETURN);
    
    classGen.addMethod(methodGen);
  }
  


  private void compileTemplates(ClassGenerator classGen, MethodGenerator methodGen, InstructionHandle next)
  {
    Enumeration templates = _namedTemplates.keys();
    while (templates.hasMoreElements()) {
      Template template = (Template)templates.nextElement();
      compileNamedTemplate(template, classGen);
    }
    
    templates = _neededTemplates.keys();
    while (templates.hasMoreElements()) {
      Template template = (Template)templates.nextElement();
      if (template.hasContents())
      {
        InstructionList til = template.compile(classGen, methodGen);
        til.append(new GOTO_W(next));
        _templateILs.put(template, til);
        _templateIHs.put(template, til.getStart());
      }
      else
      {
        _templateIHs.put(template, next);
      }
    }
  }
  
  private void appendTemplateCode(InstructionList body) {
    Enumeration templates = _neededTemplates.keys();
    while (templates.hasMoreElements()) {
      Object iList = _templateILs.get(templates.nextElement());
      
      if (iList != null) {
        body.append((InstructionList)iList);
      }
    }
  }
  
  private void appendTestSequences(InstructionList body) {
    int n = _testSeq.length;
    for (int i = 0; i < n; i++) {
      TestSeq testSeq = _testSeq[i];
      if (testSeq != null) {
        InstructionList il = testSeq.getInstructionList();
        if (il != null) {
          body.append(il);
        }
      }
    }
  }
  

  public static void compileGetChildren(ClassGenerator classGen, MethodGenerator methodGen, int node)
  {
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = methodGen.getInstructionList();
    int git = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getChildren", "(I)Lorg/apache/xml/dtm/DTMAxisIterator;");
    

    il.append(methodGen.loadDOM());
    il.append(new ILOAD(node));
    il.append(new INVOKEINTERFACE(git, 2));
  }
  




  private InstructionList compileDefaultRecursion(ClassGenerator classGen, MethodGenerator methodGen, InstructionHandle next)
  {
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = new InstructionList();
    String applyTemplatesSig = classGen.getApplyTemplatesSig();
    int git = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getChildren", "(I)Lorg/apache/xml/dtm/DTMAxisIterator;");
    

    int applyTemplates = cpg.addMethodref(getClassName(), functionName(), applyTemplatesSig);
    

    il.append(classGen.loadTranslet());
    il.append(methodGen.loadDOM());
    
    il.append(methodGen.loadDOM());
    il.append(new ILOAD(_currentIndex));
    il.append(new INVOKEINTERFACE(git, 2));
    il.append(methodGen.loadHandler());
    il.append(new INVOKEVIRTUAL(applyTemplates));
    il.append(new GOTO_W(next));
    return il;
  }
  





  private InstructionList compileDefaultText(ClassGenerator classGen, MethodGenerator methodGen, InstructionHandle next)
  {
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = new InstructionList();
    
    int chars = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "characters", CHARACTERS_SIG);
    

    il.append(methodGen.loadDOM());
    il.append(new ILOAD(_currentIndex));
    il.append(methodGen.loadHandler());
    il.append(new INVOKEINTERFACE(chars, 3));
    il.append(new GOTO_W(next));
    return il;
  }
  




  private InstructionList compileNamespaces(ClassGenerator classGen, MethodGenerator methodGen, boolean[] isNamespace, boolean[] isAttribute, boolean attrFlag, InstructionHandle defaultTarget)
  {
    XSLTC xsltc = classGen.getParser().getXSLTC();
    ConstantPoolGen cpg = classGen.getConstantPool();
    

    Vector namespaces = xsltc.getNamespaceIndex();
    Vector names = xsltc.getNamesIndex();
    int namespaceCount = namespaces.size() + 1;
    int namesCount = names.size();
    
    InstructionList il = new InstructionList();
    int[] types = new int[namespaceCount];
    InstructionHandle[] targets = new InstructionHandle[types.length];
    
    if (namespaceCount > 0) {
      boolean compiled = false;
      

      for (int i = 0; i < namespaceCount; i++) {
        targets[i] = defaultTarget;
        types[i] = i;
      }
      

      for (int i = 14; i < 14 + namesCount; i++) {
        if ((isNamespace[i] != 0) && (isAttribute[i] == attrFlag)) {
          String name = (String)names.elementAt(i - 14);
          String namespace = name.substring(0, name.lastIndexOf(':'));
          int type = xsltc.registerNamespace(namespace);
          
          if ((i < _testSeq.length) && (_testSeq[i] != null))
          {
            targets[type] = _testSeq[i].compile(classGen, methodGen, defaultTarget);
            


            compiled = true;
          }
        }
      }
      

      if (!compiled) { return null;
      }
      
      int getNS = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getNamespaceType", "(I)I");
      

      il.append(methodGen.loadDOM());
      il.append(new ILOAD(_currentIndex));
      il.append(new INVOKEINTERFACE(getNS, 2));
      il.append(new SWITCH(types, targets, defaultTarget));
      return il;
    }
    
    return null;
  }
  




  public void compileApplyTemplates(ClassGenerator classGen)
  {
    XSLTC xsltc = classGen.getParser().getXSLTC();
    ConstantPoolGen cpg = classGen.getConstantPool();
    Vector names = xsltc.getNamesIndex();
    

    Type[] argTypes = new Type[3];
    
    argTypes[0] = Util.getJCRefType("Lorg/apache/xalan/xsltc/DOM;");
    argTypes[1] = Util.getJCRefType("Lorg/apache/xml/dtm/DTMAxisIterator;");
    argTypes[2] = Util.getJCRefType(TRANSLET_OUTPUT_SIG);
    
    String[] argNames = new String[3];
    argNames[0] = "document";
    argNames[1] = "iterator";
    argNames[2] = "handler";
    
    InstructionList mainIL = new InstructionList();
    
    MethodGenerator methodGen = new MethodGenerator(17, Type.VOID, argTypes, argNames, functionName(), getClassName(), mainIL, classGen.getConstantPool());
    




    methodGen.addException("org.apache.xalan.xsltc.TransletException");
    


    mainIL.append(NOP);
    


    LocalVariableGen current = methodGen.addLocalVariable2("current", Type.INT, null);
    

    _currentIndex = current.getIndex();
    


    InstructionList body = new InstructionList();
    body.append(NOP);
    


    InstructionList ilLoop = new InstructionList();
    ilLoop.append(methodGen.loadIterator());
    ilLoop.append(methodGen.nextNode());
    ilLoop.append(DUP);
    ilLoop.append(new ISTORE(_currentIndex));
    


    BranchHandle ifeq = ilLoop.append(new IFLT(null));
    BranchHandle loop = ilLoop.append(new GOTO_W(null));
    ifeq.setTarget(ilLoop.append(RETURN));
    InstructionHandle ihLoop = ilLoop.getStart();
    
    current.setStart(mainIL.append(new GOTO_W(ihLoop)));
    

    current.setEnd(loop);
    

    InstructionList ilRecurse = compileDefaultRecursion(classGen, methodGen, ihLoop);
    
    InstructionHandle ihRecurse = ilRecurse.getStart();
    

    InstructionList ilText = compileDefaultText(classGen, methodGen, ihLoop);
    
    InstructionHandle ihText = ilText.getStart();
    

    int[] types = new int[14 + names.size()];
    for (int i = 0; i < types.length; i++) {
      types[i] = i;
    }
    

    boolean[] isAttribute = new boolean[types.length];
    boolean[] isNamespace = new boolean[types.length];
    for (int i = 0; i < names.size(); i++) {
      String name = (String)names.elementAt(i);
      isAttribute[(i + 14)] = isAttributeName(name);
      isNamespace[(i + 14)] = isNamespaceName(name);
    }
    

    compileTemplates(classGen, methodGen, ihLoop);
    

    TestSeq elemTest = _testSeq[1];
    InstructionHandle ihElem = ihRecurse;
    if (elemTest != null) {
      ihElem = elemTest.compile(classGen, methodGen, ihRecurse);
    }
    
    TestSeq attrTest = _testSeq[2];
    InstructionHandle ihAttr = ihText;
    if (attrTest != null) {
      ihAttr = attrTest.compile(classGen, methodGen, ihAttr);
    }
    
    InstructionList ilKey = null;
    if (_idxTestSeq != null) {
      loop.setTarget(_idxTestSeq.compile(classGen, methodGen, body.getStart()));
      ilKey = _idxTestSeq.getInstructionList();
    }
    else {
      loop.setTarget(body.getStart());
    }
    


    if (_childNodeTestSeq != null)
    {
      double nodePrio = _childNodeTestSeq.getPriority();
      int nodePos = _childNodeTestSeq.getPosition();
      double elemPrio = -1.7976931348623157E308D;
      int elemPos = Integer.MIN_VALUE;
      
      if (elemTest != null) {
        elemPrio = elemTest.getPriority();
        elemPos = elemTest.getPosition();
      }
      if ((elemPrio == NaN.0D) || (elemPrio < nodePrio) || ((elemPrio == nodePrio) && (elemPos < nodePos)))
      {

        ihElem = _childNodeTestSeq.compile(classGen, methodGen, ihLoop);
      }
      

      TestSeq textTest = _testSeq[3];
      double textPrio = -1.7976931348623157E308D;
      int textPos = Integer.MIN_VALUE;
      
      if (textTest != null) {
        textPrio = textTest.getPriority();
        textPos = textTest.getPosition();
      }
      if ((Double.isNaN(textPrio)) || (textPrio < nodePrio) || ((textPrio == nodePrio) && (textPos < nodePos)))
      {

        ihText = _childNodeTestSeq.compile(classGen, methodGen, ihLoop);
        _testSeq[3] = _childNodeTestSeq;
      }
    }
    

    InstructionHandle elemNamespaceHandle = ihElem;
    InstructionList nsElem = compileNamespaces(classGen, methodGen, isNamespace, isAttribute, false, ihElem);
    

    if (nsElem != null) { elemNamespaceHandle = nsElem.getStart();
    }
    
    InstructionHandle attrNamespaceHandle = ihAttr;
    InstructionList nsAttr = compileNamespaces(classGen, methodGen, isNamespace, isAttribute, true, ihAttr);
    

    if (nsAttr != null) { attrNamespaceHandle = nsAttr.getStart();
    }
    
    InstructionHandle[] targets = new InstructionHandle[types.length];
    for (int i = 14; i < targets.length; i++) {
      TestSeq testSeq = _testSeq[i];
      
      if (isNamespace[i] != 0) {
        if (isAttribute[i] != 0) {
          targets[i] = attrNamespaceHandle;
        } else {
          targets[i] = elemNamespaceHandle;
        }
      }
      else if (testSeq != null) {
        if (isAttribute[i] != 0) {
          targets[i] = testSeq.compile(classGen, methodGen, attrNamespaceHandle);
        }
        else {
          targets[i] = testSeq.compile(classGen, methodGen, elemNamespaceHandle);
        }
      }
      else {
        targets[i] = ihLoop;
      }
    }
    


    targets[0] = (_rootPattern != null ? getTemplateInstructionHandle(_rootPattern.getTemplate()) : ihRecurse);
    



    targets[9] = (_rootPattern != null ? getTemplateInstructionHandle(_rootPattern.getTemplate()) : ihRecurse);
    



    targets[3] = (_testSeq[3] != null ? _testSeq[3].compile(classGen, methodGen, ihText) : ihText);
    



    targets[13] = ihLoop;
    

    targets[1] = elemNamespaceHandle;
    

    targets[2] = attrNamespaceHandle;
    

    InstructionHandle ihPI = ihLoop;
    if (_childNodeTestSeq != null) ihPI = ihElem;
    if (_testSeq[7] != null) {
      targets[7] = _testSeq[7].compile(classGen, methodGen, ihPI);
    }
    else
    {
      targets[7] = ihPI;
    }
    
    InstructionHandle ihComment = ihLoop;
    if (_childNodeTestSeq != null) ihComment = ihElem;
    targets[8] = (_testSeq[8] != null ? _testSeq[8].compile(classGen, methodGen, ihComment) : ihComment);
    



    targets[4] = ihLoop;
    

    targets[11] = ihLoop;
    

    targets[10] = ihLoop;
    

    targets[6] = ihLoop;
    

    targets[5] = ihLoop;
    

    targets[12] = ihLoop;
    


    for (int i = 14; i < targets.length; i++) {
      TestSeq testSeq = _testSeq[i];
      
      if ((testSeq == null) || (isNamespace[i] != 0)) {
        if (isAttribute[i] != 0) {
          targets[i] = attrNamespaceHandle;
        } else {
          targets[i] = elemNamespaceHandle;
        }
        
      }
      else if (isAttribute[i] != 0) {
        targets[i] = testSeq.compile(classGen, methodGen, attrNamespaceHandle);
      }
      else {
        targets[i] = testSeq.compile(classGen, methodGen, elemNamespaceHandle);
      }
    }
    

    if (ilKey != null) { body.insert(ilKey);
    }
    
    int getType = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getExpandedTypeID", "(I)I");
    

    body.append(methodGen.loadDOM());
    body.append(new ILOAD(_currentIndex));
    body.append(new INVOKEINTERFACE(getType, 2));
    

    InstructionHandle disp = body.append(new SWITCH(types, targets, ihLoop));
    

    appendTestSequences(body);
    
    appendTemplateCode(body);
    

    if (nsElem != null) { body.append(nsElem);
    }
    if (nsAttr != null) { body.append(nsAttr);
    }
    
    body.append(ilRecurse);
    
    body.append(ilText);
    

    mainIL.append(body);
    
    mainIL.append(ilLoop);
    
    peepHoleOptimization(methodGen);
    
    classGen.addMethod(methodGen);
    

    if (_importLevels != null) {
      Enumeration levels = _importLevels.keys();
      while (levels.hasMoreElements()) {
        Integer max = (Integer)levels.nextElement();
        Integer min = (Integer)_importLevels.get(max);
        compileApplyImports(classGen, min.intValue(), max.intValue());
      }
    }
  }
  

  private void compileTemplateCalls(ClassGenerator classGen, MethodGenerator methodGen, InstructionHandle next, int min, int max)
  {
    Enumeration templates = _neededTemplates.keys();
    while (templates.hasMoreElements()) {
      Template template = (Template)templates.nextElement();
      int prec = template.getImportPrecedence();
      if ((prec >= min) && (prec < max)) {
        if (template.hasContents()) {
          InstructionList til = template.compile(classGen, methodGen);
          til.append(new GOTO_W(next));
          _templateILs.put(template, til);
          _templateIHs.put(template, til.getStart());
        }
        else
        {
          _templateIHs.put(template, next);
        }
      }
    }
  }
  
  public void compileApplyImports(ClassGenerator classGen, int min, int max)
  {
    XSLTC xsltc = classGen.getParser().getXSLTC();
    ConstantPoolGen cpg = classGen.getConstantPool();
    Vector names = xsltc.getNamesIndex();
    

    _namedTemplates = new Hashtable();
    _neededTemplates = new Hashtable();
    _templateIHs = new Hashtable();
    _templateILs = new Hashtable();
    _patternGroups = new Vector[32];
    _rootPattern = null;
    

    Vector oldTemplates = _templates;
    

    _templates = new Vector();
    Enumeration templates = oldTemplates.elements();
    Template template; for (; templates.hasMoreElements(); 
        

        addTemplate(template))
    {
      template = (Template)templates.nextElement();
      int prec = template.getImportPrecedence();
      if ((prec < min) || (prec >= max)) {}
    }
    

    processPatterns(_keys);
    

    Type[] argTypes = new Type[4];
    
    argTypes[0] = Util.getJCRefType("Lorg/apache/xalan/xsltc/DOM;");
    argTypes[1] = Util.getJCRefType("Lorg/apache/xml/dtm/DTMAxisIterator;");
    argTypes[2] = Util.getJCRefType(TRANSLET_OUTPUT_SIG);
    argTypes[3] = Type.INT;
    
    String[] argNames = new String[4];
    argNames[0] = "document";
    argNames[1] = "iterator";
    argNames[2] = "handler";
    argNames[3] = "node";
    
    InstructionList mainIL = new InstructionList();
    MethodGenerator methodGen = new MethodGenerator(17, Type.VOID, argTypes, argNames, functionName() + '_' + max, getClassName(), mainIL, classGen.getConstantPool());
    




    methodGen.addException("org.apache.xalan.xsltc.TransletException");
    


    LocalVariableGen current = methodGen.addLocalVariable2("current", Type.INT, null);
    

    _currentIndex = current.getIndex();
    
    mainIL.append(new ILOAD(methodGen.getLocalIndex("node")));
    current.setStart(mainIL.append(new ISTORE(_currentIndex)));
    


    InstructionList body = new InstructionList();
    body.append(NOP);
    


    InstructionList ilLoop = new InstructionList();
    ilLoop.append(RETURN);
    InstructionHandle ihLoop = ilLoop.getStart();
    

    InstructionList ilRecurse = compileDefaultRecursion(classGen, methodGen, ihLoop);
    
    InstructionHandle ihRecurse = ilRecurse.getStart();
    

    InstructionList ilText = compileDefaultText(classGen, methodGen, ihLoop);
    
    InstructionHandle ihText = ilText.getStart();
    

    int[] types = new int[14 + names.size()];
    for (int i = 0; i < types.length; i++) {
      types[i] = i;
    }
    
    boolean[] isAttribute = new boolean[types.length];
    boolean[] isNamespace = new boolean[types.length];
    for (int i = 0; i < names.size(); i++) {
      String name = (String)names.elementAt(i);
      isAttribute[(i + 14)] = isAttributeName(name);
      isNamespace[(i + 14)] = isNamespaceName(name);
    }
    

    compileTemplateCalls(classGen, methodGen, ihLoop, min, max);
    

    TestSeq elemTest = _testSeq[1];
    InstructionHandle ihElem = ihRecurse;
    if (elemTest != null) {
      ihElem = elemTest.compile(classGen, methodGen, ihLoop);
    }
    

    TestSeq attrTest = _testSeq[2];
    InstructionHandle ihAttr = ihLoop;
    if (attrTest != null) {
      ihAttr = attrTest.compile(classGen, methodGen, ihAttr);
    }
    

    InstructionList ilKey = null;
    if (_idxTestSeq != null) {
      ilKey = _idxTestSeq.getInstructionList();
    }
    


    if (_childNodeTestSeq != null)
    {
      double nodePrio = _childNodeTestSeq.getPriority();
      int nodePos = _childNodeTestSeq.getPosition();
      double elemPrio = -1.7976931348623157E308D;
      int elemPos = Integer.MIN_VALUE;
      
      if (elemTest != null) {
        elemPrio = elemTest.getPriority();
        elemPos = elemTest.getPosition();
      }
      
      if ((elemPrio == NaN.0D) || (elemPrio < nodePrio) || ((elemPrio == nodePrio) && (elemPos < nodePos)))
      {

        ihElem = _childNodeTestSeq.compile(classGen, methodGen, ihLoop);
      }
      

      TestSeq textTest = _testSeq[3];
      double textPrio = -1.7976931348623157E308D;
      int textPos = Integer.MIN_VALUE;
      
      if (textTest != null) {
        textPrio = textTest.getPriority();
        textPos = textTest.getPosition();
      }
      
      if ((Double.isNaN(textPrio)) || (textPrio < nodePrio) || ((textPrio == nodePrio) && (textPos < nodePos)))
      {

        ihText = _childNodeTestSeq.compile(classGen, methodGen, ihLoop);
        _testSeq[3] = _childNodeTestSeq;
      }
    }
    

    InstructionHandle elemNamespaceHandle = ihElem;
    InstructionList nsElem = compileNamespaces(classGen, methodGen, isNamespace, isAttribute, false, ihElem);
    

    if (nsElem != null) { elemNamespaceHandle = nsElem.getStart();
    }
    
    InstructionList nsAttr = compileNamespaces(classGen, methodGen, isNamespace, isAttribute, true, ihAttr);
    

    InstructionHandle attrNamespaceHandle = ihAttr;
    if (nsAttr != null) { attrNamespaceHandle = nsAttr.getStart();
    }
    
    InstructionHandle[] targets = new InstructionHandle[types.length];
    for (int i = 14; i < targets.length; i++) {
      TestSeq testSeq = _testSeq[i];
      
      if (isNamespace[i] != 0) {
        if (isAttribute[i] != 0) {
          targets[i] = attrNamespaceHandle;
        } else {
          targets[i] = elemNamespaceHandle;
        }
      }
      else if (testSeq != null) {
        if (isAttribute[i] != 0) {
          targets[i] = testSeq.compile(classGen, methodGen, attrNamespaceHandle);
        }
        else {
          targets[i] = testSeq.compile(classGen, methodGen, elemNamespaceHandle);
        }
      }
      else {
        targets[i] = ihLoop;
      }
    }
    

    targets[0] = (_rootPattern != null ? getTemplateInstructionHandle(_rootPattern.getTemplate()) : ihRecurse);
    


    targets[9] = (_rootPattern != null ? getTemplateInstructionHandle(_rootPattern.getTemplate()) : ihRecurse);
    



    targets[3] = (_testSeq[3] != null ? _testSeq[3].compile(classGen, methodGen, ihText) : ihText);
    



    targets[13] = ihLoop;
    

    targets[1] = elemNamespaceHandle;
    

    targets[2] = attrNamespaceHandle;
    

    InstructionHandle ihPI = ihLoop;
    if (_childNodeTestSeq != null) ihPI = ihElem;
    if (_testSeq[7] != null) {
      targets[7] = _testSeq[7].compile(classGen, methodGen, ihPI);

    }
    else
    {
      targets[7] = ihPI;
    }
    

    InstructionHandle ihComment = ihLoop;
    if (_childNodeTestSeq != null) ihComment = ihElem;
    targets[8] = (_testSeq[8] != null ? _testSeq[8].compile(classGen, methodGen, ihComment) : ihComment);
    



    targets[4] = ihLoop;
    

    targets[11] = ihLoop;
    

    targets[10] = ihLoop;
    

    targets[6] = ihLoop;
    

    targets[5] = ihLoop;
    

    targets[12] = ihLoop;
    



    for (int i = 14; i < targets.length; i++) {
      TestSeq testSeq = _testSeq[i];
      
      if ((testSeq == null) || (isNamespace[i] != 0)) {
        if (isAttribute[i] != 0) {
          targets[i] = attrNamespaceHandle;
        } else {
          targets[i] = elemNamespaceHandle;
        }
        
      }
      else if (isAttribute[i] != 0) {
        targets[i] = testSeq.compile(classGen, methodGen, attrNamespaceHandle);
      }
      else {
        targets[i] = testSeq.compile(classGen, methodGen, elemNamespaceHandle);
      }
    }
    

    if (ilKey != null) { body.insert(ilKey);
    }
    
    int getType = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getExpandedTypeID", "(I)I");
    

    body.append(methodGen.loadDOM());
    body.append(new ILOAD(_currentIndex));
    body.append(new INVOKEINTERFACE(getType, 2));
    

    InstructionHandle disp = body.append(new SWITCH(types, targets, ihLoop));
    

    appendTestSequences(body);
    
    appendTemplateCode(body);
    

    if (nsElem != null) { body.append(nsElem);
    }
    if (nsAttr != null) { body.append(nsAttr);
    }
    
    body.append(ilRecurse);
    
    body.append(ilText);
    

    mainIL.append(body);
    

    current.setEnd(body.getEnd());
    

    mainIL.append(ilLoop);
    
    peepHoleOptimization(methodGen);
    
    classGen.addMethod(methodGen);
    

    _templates = oldTemplates;
  }
  


  private void peepHoleOptimization(MethodGenerator methodGen)
  {
    InstructionList il = methodGen.getInstructionList();
    InstructionFinder find = new InstructionFinder(il);
    



    String pattern = "LoadInstruction POP";
    for (Iterator iter = find.search(pattern); iter.hasNext();) {
      InstructionHandle[] match = (InstructionHandle[])iter.next();
      try {
        if ((!match[0].hasTargeters()) && (!match[1].hasTargeters())) {
          il.delete(match[0], match[1]);
        }
      }
      catch (TargetLostException e) {}
    }
    



    pattern = "ILOAD ILOAD SWAP ISTORE";
    for (Iterator iter = find.search(pattern); iter.hasNext();) {
      InstructionHandle[] match = (InstructionHandle[])iter.next();
      try {
        ILOAD iload1 = (ILOAD)match[0].getInstruction();
        
        ILOAD iload2 = (ILOAD)match[1].getInstruction();
        
        ISTORE istore = (ISTORE)match[3].getInstruction();
        

        if ((!match[1].hasTargeters()) && (!match[2].hasTargeters()) && (!match[3].hasTargeters()) && (iload1.getIndex() == iload2.getIndex()) && (iload2.getIndex() == istore.getIndex()))
        {




          il.delete(match[1], match[3]);
        }
      }
      catch (TargetLostException e) {}
    }
    



    pattern = "LoadInstruction LoadInstruction SWAP";
    for (Iterator iter = find.search(pattern); iter.hasNext();) {
      InstructionHandle[] match = (InstructionHandle[])iter.next();
      try {
        if ((!match[0].hasTargeters()) && (!match[1].hasTargeters()) && (!match[2].hasTargeters()))
        {


          Instruction load_m = match[1].getInstruction();
          il.insert(match[0], load_m);
          il.delete(match[1], match[2]);
        }
      }
      catch (TargetLostException e) {}
    }
    



    pattern = "ALOAD ALOAD";
    for (Iterator iter = find.search(pattern); iter.hasNext();) {
      InstructionHandle[] match = (InstructionHandle[])iter.next();
      try {
        if (!match[1].hasTargeters()) {
          ALOAD aload1 = (ALOAD)match[0].getInstruction();
          
          ALOAD aload2 = (ALOAD)match[1].getInstruction();
          

          if (aload1.getIndex() == aload2.getIndex()) {
            il.insert(match[1], new DUP());
            il.delete(match[1]);
          }
        }
      }
      catch (TargetLostException e) {}
    }
  }
  

  public InstructionHandle getTemplateInstructionHandle(Template template)
  {
    return (InstructionHandle)_templateIHs.get(template);
  }
  


  private static boolean isAttributeName(String qname)
  {
    int col = qname.lastIndexOf(':') + 1;
    return qname.charAt(col) == '@';
  }
  



  private static boolean isNamespaceName(String qname)
  {
    int col = qname.lastIndexOf(':');
    return (col > -1) && (qname.charAt(qname.length() - 1) == '*');
  }
}
