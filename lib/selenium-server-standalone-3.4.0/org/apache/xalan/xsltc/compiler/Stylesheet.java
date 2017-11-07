package org.apache.xalan.xsltc.compiler;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.bcel.generic.ANEWARRAY;
import org.apache.bcel.generic.BasicType;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.FieldGen;
import org.apache.bcel.generic.GETFIELD;
import org.apache.bcel.generic.GETSTATIC;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.ISTORE;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.NEW;
import org.apache.bcel.generic.NEWARRAY;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.PUTFIELD;
import org.apache.bcel.generic.PUTSTATIC;
import org.apache.bcel.generic.TargetLostException;
import org.apache.bcel.util.InstructionFinder;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;
import org.apache.xml.utils.SystemIDResolver;












































public final class Stylesheet
  extends SyntaxTreeNode
{
  private String _version;
  private QName _name;
  private String _systemId;
  private Stylesheet _parentStylesheet;
  private Vector _globals = new Vector();
  



  private Boolean _hasLocalParams = null;
  



  private String _className;
  



  private final Vector _templates = new Vector();
  




  private Vector _allValidTemplates = null;
  
  private Vector _elementsWithNamespacesUsedDynamically = null;
  



  private int _nextModeSerial = 1;
  



  private final Hashtable _modes = new Hashtable();
  



  private Mode _defaultMode;
  



  private final Hashtable _extensions = new Hashtable();
  




  public Stylesheet _importedFrom = null;
  




  public Stylesheet _includedFrom = null;
  



  private Vector _includedStylesheets = null;
  



  private int _importPrecedence = 1;
  




  private int _minimumDescendantPrecedence = -1;
  



  private Hashtable _keys = new Hashtable();
  




  private SourceLoader _loader = null;
  



  private boolean _numberFormattingUsed = false;
  




  private boolean _simplified = false;
  



  private boolean _multiDocument = false;
  



  private boolean _callsNodeset = false;
  



  private boolean _hasIdCall = false;
  




  private boolean _templateInlining = false;
  



  private Output _lastOutputElement = null;
  



  private Properties _outputProperties = null;
  




  private int _outputMethod = 0;
  
  public static final int UNKNOWN_OUTPUT = 0;
  
  public static final int XML_OUTPUT = 1;
  public static final int HTML_OUTPUT = 2;
  public static final int TEXT_OUTPUT = 3;
  
  public Stylesheet() {}
  
  public int getOutputMethod()
  {
    return _outputMethod;
  }
  


  private void checkOutputMethod()
  {
    if (_lastOutputElement != null) {
      String method = _lastOutputElement.getOutputMethod();
      if (method != null) {
        if (method.equals("xml")) {
          _outputMethod = 1;
        } else if (method.equals("html")) {
          _outputMethod = 2;
        } else if (method.equals("text"))
          _outputMethod = 3;
      }
    }
  }
  
  public boolean getTemplateInlining() {
    return _templateInlining;
  }
  
  public void setTemplateInlining(boolean flag) {
    _templateInlining = flag;
  }
  
  public boolean isSimplified() {
    return _simplified;
  }
  
  public void setSimplified() {
    _simplified = true;
  }
  
  public void setHasIdCall(boolean flag) {
    _hasIdCall = flag;
  }
  
  public void setOutputProperty(String key, String value) {
    if (_outputProperties == null) {
      _outputProperties = new Properties();
    }
    _outputProperties.setProperty(key, value);
  }
  
  public void setOutputProperties(Properties props) {
    _outputProperties = props;
  }
  
  public Properties getOutputProperties() {
    return _outputProperties;
  }
  
  public Output getLastOutputElement() {
    return _lastOutputElement;
  }
  
  public void setMultiDocument(boolean flag) {
    _multiDocument = flag;
  }
  
  public boolean isMultiDocument() {
    return _multiDocument;
  }
  
  public void setCallsNodeset(boolean flag) {
    if (flag) setMultiDocument(flag);
    _callsNodeset = flag;
  }
  
  public boolean callsNodeset() {
    return _callsNodeset;
  }
  
  public void numberFormattingUsed() {
    _numberFormattingUsed = true;
    





    Stylesheet parent = getParentStylesheet();
    if (null != parent) parent.numberFormattingUsed();
  }
  
  public void setImportPrecedence(int precedence)
  {
    _importPrecedence = precedence;
    

    Enumeration elements = elements();
    while (elements.hasMoreElements()) {
      SyntaxTreeNode child = (SyntaxTreeNode)elements.nextElement();
      if ((child instanceof Include)) {
        Stylesheet included = ((Include)child).getIncludedStylesheet();
        if ((included != null) && (_includedFrom == this)) {
          included.setImportPrecedence(precedence);
        }
      }
    }
    

    if (_importedFrom != null) {
      if (_importedFrom.getImportPrecedence() < precedence) {
        Parser parser = getParser();
        int nextPrecedence = parser.getNextImportPrecedence();
        _importedFrom.setImportPrecedence(nextPrecedence);
      }
      
    }
    else if ((_includedFrom != null) && 
      (_includedFrom.getImportPrecedence() != precedence)) {
      _includedFrom.setImportPrecedence(precedence);
    }
  }
  
  public int getImportPrecedence() {
    return _importPrecedence;
  }
  




  public int getMinimumDescendantPrecedence()
  {
    if (_minimumDescendantPrecedence == -1)
    {
      int min = getImportPrecedence();
      

      int inclImpCount = _includedStylesheets != null ? _includedStylesheets.size() : 0;
      


      for (int i = 0; i < inclImpCount; i++) {
        int prec = ((Stylesheet)_includedStylesheets.elementAt(i)).getMinimumDescendantPrecedence();
        

        if (prec < min) {
          min = prec;
        }
      }
      
      _minimumDescendantPrecedence = min;
    }
    return _minimumDescendantPrecedence;
  }
  
  public boolean checkForLoop(String systemId)
  {
    if ((_systemId != null) && (_systemId.equals(systemId))) {
      return true;
    }
    
    if (_parentStylesheet != null) {
      return _parentStylesheet.checkForLoop(systemId);
    }
    return false;
  }
  
  public void setParser(Parser parser) {
    super.setParser(parser);
    _name = makeStylesheetName("__stylesheet_");
  }
  
  public void setParentStylesheet(Stylesheet parent) {
    _parentStylesheet = parent;
  }
  
  public Stylesheet getParentStylesheet() {
    return _parentStylesheet;
  }
  
  public void setImportingStylesheet(Stylesheet parent) {
    _importedFrom = parent;
    parent.addIncludedStylesheet(this);
  }
  
  public void setIncludingStylesheet(Stylesheet parent) {
    _includedFrom = parent;
    parent.addIncludedStylesheet(this);
  }
  
  public void addIncludedStylesheet(Stylesheet child) {
    if (_includedStylesheets == null) {
      _includedStylesheets = new Vector();
    }
    _includedStylesheets.addElement(child);
  }
  
  public void setSystemId(String systemId) {
    if (systemId != null) {
      _systemId = SystemIDResolver.getAbsoluteURI(systemId);
    }
  }
  
  public String getSystemId() {
    return _systemId;
  }
  
  public void setSourceLoader(SourceLoader loader) {
    _loader = loader;
  }
  
  public SourceLoader getSourceLoader() {
    return _loader;
  }
  
  private QName makeStylesheetName(String prefix) {
    return getParser().getQName(prefix + getXSLTC().nextStylesheetSerial());
  }
  


  public boolean hasGlobals()
  {
    return _globals.size() > 0;
  }
  




  public boolean hasLocalParams()
  {
    if (_hasLocalParams == null) {
      Vector templates = getAllValidTemplates();
      int n = templates.size();
      for (int i = 0; i < n; i++) {
        Template template = (Template)templates.elementAt(i);
        if (template.hasParams()) {
          _hasLocalParams = Boolean.TRUE;
          return true;
        }
      }
      _hasLocalParams = Boolean.FALSE;
      return false;
    }
    
    return _hasLocalParams.booleanValue();
  }
  





  protected void addPrefixMapping(String prefix, String uri)
  {
    if ((prefix.equals("")) && (uri.equals("http://www.w3.org/1999/xhtml"))) return;
    super.addPrefixMapping(prefix, uri);
  }
  


  private void extensionURI(String prefixes, SymbolTable stable)
  {
    if (prefixes != null) {
      StringTokenizer tokens = new StringTokenizer(prefixes);
      while (tokens.hasMoreTokens()) {
        String prefix = tokens.nextToken();
        String uri = lookupNamespace(prefix);
        if (uri != null) {
          _extensions.put(uri, prefix);
        }
      }
    }
  }
  
  public boolean isExtension(String uri) {
    return _extensions.get(uri) != null;
  }
  
  public void declareExtensionPrefixes(Parser parser) {
    SymbolTable stable = parser.getSymbolTable();
    String extensionPrefixes = getAttribute("extension-element-prefixes");
    extensionURI(extensionPrefixes, stable);
  }
  




  public void parseContents(Parser parser)
  {
    SymbolTable stable = parser.getSymbolTable();
    












    addPrefixMapping("xml", "http://www.w3.org/XML/1998/namespace");
    

    Stylesheet sheet = stable.addStylesheet(_name, this);
    if (sheet != null)
    {
      ErrorMsg err = new ErrorMsg("MULTIPLE_STYLESHEET_ERR", this);
      parser.reportError(3, err);
    }
    





    if (_simplified) {
      stable.excludeURI("http://www.w3.org/1999/XSL/Transform");
      Template template = new Template();
      template.parseSimplified(this, parser);
    }
    else
    {
      parseOwnChildren(parser);
    }
  }
  


  public final void parseOwnChildren(Parser parser)
  {
    SymbolTable stable = parser.getSymbolTable();
    String excludePrefixes = getAttribute("exclude-result-prefixes");
    String extensionPrefixes = getAttribute("extension-element-prefixes");
    

    stable.pushExcludedNamespacesContext();
    stable.excludeURI("http://www.w3.org/1999/XSL/Transform");
    stable.excludeNamespaces(excludePrefixes);
    stable.excludeNamespaces(extensionPrefixes);
    
    Vector contents = getContents();
    int count = contents.size();
    


    for (int i = 0; i < count; i++) {
      SyntaxTreeNode child = (SyntaxTreeNode)contents.elementAt(i);
      if (((child instanceof VariableBase)) || ((child instanceof NamespaceAlias)))
      {
        parser.getSymbolTable().setCurrentNode(child);
        child.parseContents(parser);
      }
    }
    

    for (int i = 0; i < count; i++) {
      SyntaxTreeNode child = (SyntaxTreeNode)contents.elementAt(i);
      if ((!(child instanceof VariableBase)) && (!(child instanceof NamespaceAlias)))
      {
        parser.getSymbolTable().setCurrentNode(child);
        child.parseContents(parser);
      }
      


      if ((!_templateInlining) && ((child instanceof Template))) {
        Template template = (Template)child;
        String name = "template$dot$" + template.getPosition();
        template.setName(parser.getQName(name));
      }
    }
    
    stable.popExcludedNamespacesContext();
  }
  
  public void processModes() {
    if (_defaultMode == null)
      _defaultMode = new Mode(null, this, "");
    _defaultMode.processPatterns(_keys);
    Enumeration modes = _modes.elements();
    while (modes.hasMoreElements()) {
      Mode mode = (Mode)modes.nextElement();
      mode.processPatterns(_keys);
    }
  }
  
  private void compileModes(ClassGenerator classGen) {
    _defaultMode.compileApplyTemplates(classGen);
    Enumeration modes = _modes.elements();
    while (modes.hasMoreElements()) {
      Mode mode = (Mode)modes.nextElement();
      mode.compileApplyTemplates(classGen);
    }
  }
  
  public Mode getMode(QName modeName) {
    if (modeName == null) {
      if (_defaultMode == null) {
        _defaultMode = new Mode(null, this, "");
      }
      return _defaultMode;
    }
    
    Mode mode = (Mode)_modes.get(modeName);
    if (mode == null) {
      String suffix = Integer.toString(_nextModeSerial++);
      _modes.put(modeName, mode = new Mode(modeName, this, suffix));
    }
    return mode;
  }
  


  public org.apache.xalan.xsltc.compiler.util.Type typeCheck(SymbolTable stable)
    throws TypeCheckError
  {
    int count = _globals.size();
    for (int i = 0; i < count; i++) {
      VariableBase var = (VariableBase)_globals.elementAt(i);
      var.typeCheck(stable);
    }
    return typeCheckContents(stable);
  }
  


  public void translate(ClassGenerator classGen, MethodGenerator methodGen)
  {
    translate();
  }
  
  private void addDOMField(ClassGenerator classGen) {
    FieldGen fgen = new FieldGen(1, Util.getJCRefType("Lorg/apache/xalan/xsltc/DOM;"), "_dom", classGen.getConstantPool());
    


    classGen.addField(fgen.getField());
  }
  




  private void addStaticField(ClassGenerator classGen, String type, String name)
  {
    FieldGen fgen = new FieldGen(12, Util.getJCRefType(type), name, classGen.getConstantPool());
    


    classGen.addField(fgen.getField());
  }
  



  public void translate()
  {
    _className = getXSLTC().getClassName();
    

    ClassGenerator classGen = new ClassGenerator(_className, "org.apache.xalan.xsltc.runtime.AbstractTranslet", "", 33, null, this);
    





    addDOMField(classGen);
    


    compileTransform(classGen);
    

    Enumeration elements = elements();
    while (elements.hasMoreElements()) {
      Object element = elements.nextElement();
      
      if ((element instanceof Template))
      {
        Template template = (Template)element;
        
        getMode(template.getModeName()).addTemplate(template);

      }
      else if ((element instanceof AttributeSet)) {
        ((AttributeSet)element).translate(classGen, null);
      }
      else if ((element instanceof Output))
      {
        Output output = (Output)element;
        if (output.enabled()) { _lastOutputElement = output;
        }
      }
    }
    




    checkOutputMethod();
    processModes();
    compileModes(classGen);
    compileStaticInitializer(classGen);
    compileConstructor(classGen, _lastOutputElement);
    
    if (!getParser().errorsFound()) {
      getXSLTC().dumpClass(classGen.getJavaClass());
    }
  }
  






















































  private void compileStaticInitializer(ClassGenerator classGen)
  {
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = new InstructionList();
    
    MethodGenerator staticConst = new MethodGenerator(9, org.apache.bcel.generic.Type.VOID, null, null, "<clinit>", _className, il, cpg);
    




    addStaticField(classGen, "[Ljava/lang/String;", "_sNamesArray");
    addStaticField(classGen, "[Ljava/lang/String;", "_sUrisArray");
    addStaticField(classGen, "[I", "_sTypesArray");
    addStaticField(classGen, "[Ljava/lang/String;", "_sNamespaceArray");
    

    int charDataFieldCount = getXSLTC().getCharacterDataCount();
    for (int i = 0; i < charDataFieldCount; i++) {
      addStaticField(classGen, "[C", "_scharData" + i);
    }
    


    Vector namesIndex = getXSLTC().getNamesIndex();
    int size = namesIndex.size();
    String[] namesArray = new String[size];
    String[] urisArray = new String[size];
    int[] typesArray = new int[size];
    

    for (int i = 0; i < size; i++) {
      String encodedName = (String)namesIndex.elementAt(i);
      int index; if ((index = encodedName.lastIndexOf(':')) > -1) {
        urisArray[i] = encodedName.substring(0, index);
      }
      
      index += 1;
      if (encodedName.charAt(index) == '@') {
        typesArray[i] = 2;
        index++;
      } else if (encodedName.charAt(index) == '?') {
        typesArray[i] = 13;
        index++;
      } else {
        typesArray[i] = 1;
      }
      
      if (index == 0) {
        namesArray[i] = encodedName;
      }
      else {
        namesArray[i] = encodedName.substring(index);
      }
    }
    
    staticConst.markChunkStart();
    il.append(new PUSH(cpg, size));
    il.append(new ANEWARRAY(cpg.addClass("java.lang.String")));
    int namesArrayRef = cpg.addFieldref(_className, "_sNamesArray", "[Ljava/lang/String;");
    

    il.append(new PUTSTATIC(namesArrayRef));
    staticConst.markChunkEnd();
    
    for (int i = 0; i < size; i++) {
      String name = namesArray[i];
      staticConst.markChunkStart();
      il.append(new GETSTATIC(namesArrayRef));
      il.append(new PUSH(cpg, i));
      il.append(new PUSH(cpg, name));
      il.append(AASTORE);
      staticConst.markChunkEnd();
    }
    
    staticConst.markChunkStart();
    il.append(new PUSH(cpg, size));
    il.append(new ANEWARRAY(cpg.addClass("java.lang.String")));
    int urisArrayRef = cpg.addFieldref(_className, "_sUrisArray", "[Ljava/lang/String;");
    

    il.append(new PUTSTATIC(urisArrayRef));
    staticConst.markChunkEnd();
    
    for (int i = 0; i < size; i++) {
      String uri = urisArray[i];
      staticConst.markChunkStart();
      il.append(new GETSTATIC(urisArrayRef));
      il.append(new PUSH(cpg, i));
      il.append(new PUSH(cpg, uri));
      il.append(AASTORE);
      staticConst.markChunkEnd();
    }
    
    staticConst.markChunkStart();
    il.append(new PUSH(cpg, size));
    il.append(new NEWARRAY(BasicType.INT));
    int typesArrayRef = cpg.addFieldref(_className, "_sTypesArray", "[I");
    

    il.append(new PUTSTATIC(typesArrayRef));
    staticConst.markChunkEnd();
    
    for (int i = 0; i < size; i++) {
      int nodeType = typesArray[i];
      staticConst.markChunkStart();
      il.append(new GETSTATIC(typesArrayRef));
      il.append(new PUSH(cpg, i));
      il.append(new PUSH(cpg, nodeType));
      il.append(IASTORE);
      staticConst.markChunkEnd();
    }
    

    Vector namespaces = getXSLTC().getNamespaceIndex();
    staticConst.markChunkStart();
    il.append(new PUSH(cpg, namespaces.size()));
    il.append(new ANEWARRAY(cpg.addClass("java.lang.String")));
    int namespaceArrayRef = cpg.addFieldref(_className, "_sNamespaceArray", "[Ljava/lang/String;");
    

    il.append(new PUTSTATIC(namespaceArrayRef));
    staticConst.markChunkEnd();
    
    for (int i = 0; i < namespaces.size(); i++) {
      String ns = (String)namespaces.elementAt(i);
      staticConst.markChunkStart();
      il.append(new GETSTATIC(namespaceArrayRef));
      il.append(new PUSH(cpg, i));
      il.append(new PUSH(cpg, ns));
      il.append(AASTORE);
      staticConst.markChunkEnd();
    }
    

    Vector namespaceAncestors = getXSLTC().getNSAncestorPointers();
    if ((namespaceAncestors != null) && (namespaceAncestors.size() != 0)) {
      addStaticField(classGen, "[I", "_sNamespaceAncestorsArray");
      
      staticConst.markChunkStart();
      il.append(new PUSH(cpg, namespaceAncestors.size()));
      il.append(new NEWARRAY(BasicType.INT));
      int namespaceAncestorsArrayRef = cpg.addFieldref(_className, "_sNamespaceAncestorsArray", "[I");
      

      il.append(new PUTSTATIC(namespaceAncestorsArrayRef));
      staticConst.markChunkEnd();
      for (int i = 0; i < namespaceAncestors.size(); i++) {
        int ancestor = ((Integer)namespaceAncestors.get(i)).intValue();
        staticConst.markChunkStart();
        il.append(new GETSTATIC(namespaceAncestorsArrayRef));
        il.append(new PUSH(cpg, i));
        il.append(new PUSH(cpg, ancestor));
        il.append(IASTORE);
        staticConst.markChunkEnd();
      }
    }
    

    Vector prefixURIPairsIdx = getXSLTC().getPrefixURIPairsIdx();
    if ((prefixURIPairsIdx != null) && (prefixURIPairsIdx.size() != 0)) {
      addStaticField(classGen, "[I", "_sPrefixURIsIdxArray");
      
      staticConst.markChunkStart();
      il.append(new PUSH(cpg, prefixURIPairsIdx.size()));
      il.append(new NEWARRAY(BasicType.INT));
      int prefixURIPairsIdxArrayRef = cpg.addFieldref(_className, "_sPrefixURIsIdxArray", "[I");
      


      il.append(new PUTSTATIC(prefixURIPairsIdxArrayRef));
      staticConst.markChunkEnd();
      for (int i = 0; i < prefixURIPairsIdx.size(); i++) {
        int idx = ((Integer)prefixURIPairsIdx.get(i)).intValue();
        staticConst.markChunkStart();
        il.append(new GETSTATIC(prefixURIPairsIdxArrayRef));
        il.append(new PUSH(cpg, i));
        il.append(new PUSH(cpg, idx));
        il.append(IASTORE);
        staticConst.markChunkEnd();
      }
    }
    


    Vector prefixURIPairs = getXSLTC().getPrefixURIPairs();
    if ((prefixURIPairs != null) && (prefixURIPairs.size() != 0)) {
      addStaticField(classGen, "[Ljava/lang/String;", "_sPrefixURIPairsArray");
      

      staticConst.markChunkStart();
      il.append(new PUSH(cpg, prefixURIPairs.size()));
      il.append(new ANEWARRAY(cpg.addClass("java.lang.String")));
      int prefixURIPairsRef = cpg.addFieldref(_className, "_sPrefixURIPairsArray", "[Ljava/lang/String;");
      


      il.append(new PUTSTATIC(prefixURIPairsRef));
      staticConst.markChunkEnd();
      for (int i = 0; i < prefixURIPairs.size(); i++) {
        String prefixOrURI = (String)prefixURIPairs.get(i);
        staticConst.markChunkStart();
        il.append(new GETSTATIC(prefixURIPairsRef));
        il.append(new PUSH(cpg, i));
        il.append(new PUSH(cpg, prefixOrURI));
        il.append(AASTORE);
        staticConst.markChunkEnd();
      }
    }
    

    int charDataCount = getXSLTC().getCharacterDataCount();
    int toCharArray = cpg.addMethodref("java.lang.String", "toCharArray", "()[C");
    for (int i = 0; i < charDataCount; i++) {
      staticConst.markChunkStart();
      il.append(new PUSH(cpg, getXSLTC().getCharacterData(i)));
      il.append(new INVOKEVIRTUAL(toCharArray));
      il.append(new PUTSTATIC(cpg.addFieldref(_className, "_scharData" + i, "[C")));
      

      staticConst.markChunkEnd();
    }
    
    il.append(RETURN);
    
    classGen.addMethod(staticConst);
  }
  




  private void compileConstructor(ClassGenerator classGen, Output output)
  {
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = new InstructionList();
    
    MethodGenerator constructor = new MethodGenerator(1, org.apache.bcel.generic.Type.VOID, null, null, "<init>", _className, il, cpg);
    





    il.append(classGen.loadTranslet());
    il.append(new INVOKESPECIAL(cpg.addMethodref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "<init>", "()V")));
    

    constructor.markChunkStart();
    il.append(classGen.loadTranslet());
    il.append(new GETSTATIC(cpg.addFieldref(_className, "_sNamesArray", "[Ljava/lang/String;")));
    

    il.append(new PUTFIELD(cpg.addFieldref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "namesArray", "[Ljava/lang/String;")));
    

    constructor.markChunkEnd();
    
    constructor.markChunkStart();
    il.append(classGen.loadTranslet());
    il.append(new GETSTATIC(cpg.addFieldref(_className, "_sUrisArray", "[Ljava/lang/String;")));
    

    il.append(new PUTFIELD(cpg.addFieldref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "urisArray", "[Ljava/lang/String;")));
    

    constructor.markChunkEnd();
    
    constructor.markChunkStart();
    il.append(classGen.loadTranslet());
    il.append(new GETSTATIC(cpg.addFieldref(_className, "_sTypesArray", "[I")));
    

    il.append(new PUTFIELD(cpg.addFieldref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "typesArray", "[I")));
    

    constructor.markChunkEnd();
    
    constructor.markChunkStart();
    il.append(classGen.loadTranslet());
    il.append(new GETSTATIC(cpg.addFieldref(_className, "_sNamespaceArray", "[Ljava/lang/String;")));
    

    il.append(new PUTFIELD(cpg.addFieldref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "namespaceArray", "[Ljava/lang/String;")));
    

    constructor.markChunkEnd();
    
    constructor.markChunkStart();
    il.append(classGen.loadTranslet());
    il.append(new PUSH(cpg, 101));
    il.append(new PUTFIELD(cpg.addFieldref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "transletVersion", "I")));
    

    constructor.markChunkEnd();
    
    if (_hasIdCall) {
      constructor.markChunkStart();
      il.append(classGen.loadTranslet());
      il.append(new PUSH(cpg, Boolean.TRUE));
      il.append(new PUTFIELD(cpg.addFieldref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "_hasIdCall", "Z")));
      

      constructor.markChunkEnd();
    }
    

    if (output != null)
    {
      constructor.markChunkStart();
      output.translate(classGen, constructor);
      constructor.markChunkEnd();
    }
    


    if (_numberFormattingUsed) {
      constructor.markChunkStart();
      DecimalFormatting.translateDefaultDFS(classGen, constructor);
      constructor.markChunkEnd();
    }
    
    il.append(RETURN);
    
    classGen.addMethod(constructor);
  }
  











  private String compileTopLevel(ClassGenerator classGen)
  {
    ConstantPoolGen cpg = classGen.getConstantPool();
    
    org.apache.bcel.generic.Type[] argTypes = { Util.getJCRefType("Lorg/apache/xalan/xsltc/DOM;"), Util.getJCRefType("Lorg/apache/xml/dtm/DTMAxisIterator;"), Util.getJCRefType(TRANSLET_OUTPUT_SIG) };
    




    String[] argNames = { "document", "iterator", "handler" };
    


    InstructionList il = new InstructionList();
    
    MethodGenerator toplevel = new MethodGenerator(1, org.apache.bcel.generic.Type.VOID, argTypes, argNames, "topLevel", _className, il, classGen.getConstantPool());
    





    toplevel.addException("org.apache.xalan.xsltc.TransletException");
    

    LocalVariableGen current = toplevel.addLocalVariable("current", org.apache.bcel.generic.Type.INT, null, null);
    



    int setFilter = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "setFilter", "(Lorg/apache/xalan/xsltc/StripFilter;)V");
    


    int gitr = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getIterator", "()Lorg/apache/xml/dtm/DTMAxisIterator;");
    

    il.append(toplevel.loadDOM());
    il.append(new INVOKEINTERFACE(gitr, 1));
    il.append(toplevel.nextNode());
    current.setStart(il.append(new ISTORE(current.getIndex())));
    

    Vector varDepElements = new Vector(_globals);
    Enumeration elements = elements();
    while (elements.hasMoreElements()) {
      Object element = elements.nextElement();
      if ((element instanceof Key)) {
        varDepElements.add(element);
      }
    }
    

    varDepElements = resolveDependencies(varDepElements);
    

    int count = varDepElements.size();
    for (int i = 0; i < count; i++) {
      TopLevelElement tle = (TopLevelElement)varDepElements.elementAt(i);
      tle.translate(classGen, toplevel);
      if ((tle instanceof Key)) {
        Key key = (Key)tle;
        _keys.put(key.getName(), key);
      }
    }
    

    Vector whitespaceRules = new Vector();
    elements = elements();
    while (elements.hasMoreElements()) {
      Object element = elements.nextElement();
      
      if ((element instanceof DecimalFormatting)) {
        ((DecimalFormatting)element).translate(classGen, toplevel);

      }
      else if ((element instanceof Whitespace)) {
        whitespaceRules.addAll(((Whitespace)element).getRules());
      }
    }
    

    if (whitespaceRules.size() > 0) {
      Whitespace.translateRules(whitespaceRules, classGen);
    }
    
    if (classGen.containsMethod("stripSpace", "(Lorg/apache/xalan/xsltc/DOM;II)Z") != null) {
      il.append(toplevel.loadDOM());
      il.append(classGen.loadTranslet());
      il.append(new INVOKEINTERFACE(setFilter, 2));
    }
    
    il.append(RETURN);
    

    classGen.addMethod(toplevel);
    
    return "(Lorg/apache/xalan/xsltc/DOM;Lorg/apache/xml/dtm/DTMAxisIterator;" + TRANSLET_OUTPUT_SIG + ")V";
  }
  



















  private Vector resolveDependencies(Vector input)
  {
    Vector result = new Vector();
    while (input.size() > 0) {
      boolean changed = false;
      for (int i = 0; i < input.size();) {
        TopLevelElement vde = (TopLevelElement)input.elementAt(i);
        Vector dep = vde.getDependencies();
        if ((dep == null) || (result.containsAll(dep))) {
          result.addElement(vde);
          input.remove(i);
          changed = true;
        }
        else {
          i++;
        }
      }
      

      if (!changed) {
        ErrorMsg err = new ErrorMsg("CIRCULAR_VARIABLE_ERR", input.toString(), this);
        
        getParser().reportError(3, err);
        return result;
      }
    }
    








    return result;
  }
  





  private String compileBuildKeys(ClassGenerator classGen)
  {
    ConstantPoolGen cpg = classGen.getConstantPool();
    
    org.apache.bcel.generic.Type[] argTypes = { Util.getJCRefType("Lorg/apache/xalan/xsltc/DOM;"), Util.getJCRefType("Lorg/apache/xml/dtm/DTMAxisIterator;"), Util.getJCRefType(TRANSLET_OUTPUT_SIG), org.apache.bcel.generic.Type.INT };
    





    String[] argNames = { "document", "iterator", "handler", "current" };
    


    InstructionList il = new InstructionList();
    
    MethodGenerator buildKeys = new MethodGenerator(1, org.apache.bcel.generic.Type.VOID, argTypes, argNames, "buildKeys", _className, il, classGen.getConstantPool());
    





    buildKeys.addException("org.apache.xalan.xsltc.TransletException");
    
    Enumeration elements = elements();
    while (elements.hasMoreElements())
    {
      Object element = elements.nextElement();
      if ((element instanceof Key)) {
        Key key = (Key)element;
        key.translate(classGen, buildKeys);
        _keys.put(key.getName(), key);
      }
    }
    
    il.append(RETURN);
    

    classGen.addMethod(buildKeys);
    
    return "(Lorg/apache/xalan/xsltc/DOM;Lorg/apache/xml/dtm/DTMAxisIterator;" + TRANSLET_OUTPUT_SIG + "I)V";
  }
  




  private void compileTransform(ClassGenerator classGen)
  {
    ConstantPoolGen cpg = classGen.getConstantPool();
    




    org.apache.bcel.generic.Type[] argTypes = new org.apache.bcel.generic.Type[3];
    
    argTypes[0] = Util.getJCRefType("Lorg/apache/xalan/xsltc/DOM;");
    argTypes[1] = Util.getJCRefType("Lorg/apache/xml/dtm/DTMAxisIterator;");
    argTypes[2] = Util.getJCRefType(TRANSLET_OUTPUT_SIG);
    
    String[] argNames = new String[3];
    argNames[0] = "document";
    argNames[1] = "iterator";
    argNames[2] = "handler";
    
    InstructionList il = new InstructionList();
    MethodGenerator transf = new MethodGenerator(1, org.apache.bcel.generic.Type.VOID, argTypes, argNames, "transform", _className, il, classGen.getConstantPool());
    






    transf.addException("org.apache.xalan.xsltc.TransletException");
    

    LocalVariableGen current = transf.addLocalVariable("current", org.apache.bcel.generic.Type.INT, null, null);
    


    String applyTemplatesSig = classGen.getApplyTemplatesSig();
    int applyTemplates = cpg.addMethodref(getClassName(), "applyTemplates", applyTemplatesSig);
    

    int domField = cpg.addFieldref(getClassName(), "_dom", "Lorg/apache/xalan/xsltc/DOM;");
    



    il.append(classGen.loadTranslet());
    

    if (isMultiDocument()) {
      il.append(new NEW(cpg.addClass("org.apache.xalan.xsltc.dom.MultiDOM")));
      il.append(DUP);
    }
    
    il.append(classGen.loadTranslet());
    il.append(transf.loadDOM());
    il.append(new INVOKEVIRTUAL(cpg.addMethodref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "makeDOMAdapter", "(Lorg/apache/xalan/xsltc/DOM;)Lorg/apache/xalan/xsltc/dom/DOMAdapter;")));
    




    if (isMultiDocument()) {
      int init = cpg.addMethodref("org.apache.xalan.xsltc.dom.MultiDOM", "<init>", "(Lorg/apache/xalan/xsltc/DOM;)V");
      

      il.append(new INVOKESPECIAL(init));
    }
    


    il.append(new PUTFIELD(domField));
    

    int gitr = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getIterator", "()Lorg/apache/xml/dtm/DTMAxisIterator;");
    

    il.append(transf.loadDOM());
    il.append(new INVOKEINTERFACE(gitr, 1));
    il.append(transf.nextNode());
    current.setStart(il.append(new ISTORE(current.getIndex())));
    

    il.append(classGen.loadTranslet());
    il.append(transf.loadHandler());
    int index = cpg.addMethodref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "transferOutputSettings", "(" + OUTPUT_HANDLER_SIG + ")V");
    

    il.append(new INVOKEVIRTUAL(index));
    






    String keySig = compileBuildKeys(classGen);
    int keyIdx = cpg.addMethodref(getClassName(), "buildKeys", keySig);
    


    Enumeration toplevel = elements();
    if ((_globals.size() > 0) || (toplevel.hasMoreElements()))
    {
      String topLevelSig = compileTopLevel(classGen);
      
      int topLevelIdx = cpg.addMethodref(getClassName(), "topLevel", topLevelSig);
      


      il.append(classGen.loadTranslet());
      il.append(classGen.loadTranslet());
      il.append(new GETFIELD(domField));
      il.append(transf.loadIterator());
      il.append(transf.loadHandler());
      il.append(new INVOKEVIRTUAL(topLevelIdx));
    }
    

    il.append(transf.loadHandler());
    il.append(transf.startDocument());
    

    il.append(classGen.loadTranslet());
    
    il.append(classGen.loadTranslet());
    il.append(new GETFIELD(domField));
    
    il.append(transf.loadIterator());
    il.append(transf.loadHandler());
    il.append(new INVOKEVIRTUAL(applyTemplates));
    
    il.append(transf.loadHandler());
    il.append(transf.endDocument());
    
    il.append(RETURN);
    

    classGen.addMethod(transf);
  }
  


  private void peepHoleOptimization(MethodGenerator methodGen)
  {
    String pattern = "`aload'`pop'`instruction'";
    InstructionList il = methodGen.getInstructionList();
    InstructionFinder find = new InstructionFinder(il);
    for (Iterator iter = find.search("`aload'`pop'`instruction'"); iter.hasNext();) {
      InstructionHandle[] match = (InstructionHandle[])iter.next();
      try {
        il.delete(match[0], match[1]);
      }
      catch (TargetLostException e) {}
    }
  }
  

  public int addParam(Param param)
  {
    _globals.addElement(param);
    return _globals.size() - 1;
  }
  
  public int addVariable(Variable global) {
    _globals.addElement(global);
    return _globals.size() - 1;
  }
  
  public void display(int indent) {
    indent(indent);
    Util.println("Stylesheet");
    displayContents(indent + 4);
  }
  
  public String getNamespace(String prefix)
  {
    return lookupNamespace(prefix);
  }
  
  public String getClassName() {
    return _className;
  }
  
  public Vector getTemplates() {
    return _templates;
  }
  
  public Vector getAllValidTemplates()
  {
    if (_includedStylesheets == null) {
      return _templates;
    }
    

    if (_allValidTemplates == null) {
      Vector templates = new Vector();
      int size = _includedStylesheets.size();
      for (int i = 0; i < size; i++) {
        Stylesheet included = (Stylesheet)_includedStylesheets.elementAt(i);
        templates.addAll(included.getAllValidTemplates());
      }
      templates.addAll(_templates);
      

      if (_parentStylesheet != null) {
        return templates;
      }
      _allValidTemplates = templates;
    }
    
    return _allValidTemplates;
  }
  
  protected void addTemplate(Template template) {
    _templates.addElement(template);
  }
}
