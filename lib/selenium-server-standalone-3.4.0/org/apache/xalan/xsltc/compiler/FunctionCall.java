package org.apache.xalan.xsltc.compiler;

import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.IFEQ;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.NEW;
import org.apache.bcel.generic.PUSH;
import org.apache.xalan.xsltc.compiler.util.BooleanType;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.IntType;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodType;
import org.apache.xalan.xsltc.compiler.util.MultiHashtable;
import org.apache.xalan.xsltc.compiler.util.ObjectType;
import org.apache.xalan.xsltc.compiler.util.ReferenceType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
































class FunctionCall
  extends Expression
{
  private QName _fname;
  private final Vector _arguments;
  private static final Vector EMPTY_ARG_LIST = new Vector(0);
  

  protected static final String EXT_XSLTC = "http://xml.apache.org/xalan/xsltc";
  

  protected static final String JAVA_EXT_XSLTC = "http://xml.apache.org/xalan/xsltc/java";
  

  protected static final String EXT_XALAN = "http://xml.apache.org/xalan";
  

  protected static final String JAVA_EXT_XALAN = "http://xml.apache.org/xalan/java";
  

  protected static final String JAVA_EXT_XALAN_OLD = "http://xml.apache.org/xslt/java";
  

  protected static final String EXSLT_COMMON = "http://exslt.org/common";
  

  protected static final String EXSLT_MATH = "http://exslt.org/math";
  

  protected static final String EXSLT_SETS = "http://exslt.org/sets";
  

  protected static final String EXSLT_DATETIME = "http://exslt.org/dates-and-times";
  

  protected static final String EXSLT_STRINGS = "http://exslt.org/strings";
  
  protected static final int NAMESPACE_FORMAT_JAVA = 0;
  
  protected static final int NAMESPACE_FORMAT_CLASS = 1;
  
  protected static final int NAMESPACE_FORMAT_PACKAGE = 2;
  
  protected static final int NAMESPACE_FORMAT_CLASS_OR_PACKAGE = 3;
  
  private int _namespace_format = 0;
  



  Expression _thisArgument = null;
  
  private String _className;
  
  private Class _clazz;
  
  private Method _chosenMethod;
  
  private Constructor _chosenConstructor;
  
  private MethodType _chosenMethodType;
  
  private boolean unresolvedExternal;
  private boolean _isExtConstructor = false;
  

  private boolean _isStatic = false;
  

  private static final MultiHashtable _internal2Java = new MultiHashtable();
  

  private static final Hashtable _java2Internal = new Hashtable();
  

  private static final Hashtable _extensionNamespaceTable = new Hashtable();
  

  private static final Hashtable _extensionFunctionTable = new Hashtable();
  

  static class JavaType
  {
    public Class type;
    
    public int distance;
    
    public JavaType(Class type, int distance)
    {
      this.type = type;
      this.distance = distance;
    }
    
    public boolean equals(Object query) { return query.equals(type); }
  }
  





  static
  {
    try
    {
      Class nodeClass = Class.forName("org.w3c.dom.Node");
      Class nodeListClass = Class.forName("org.w3c.dom.NodeList");
      



      _internal2Java.put(Type.Boolean, new JavaType(Boolean.TYPE, 0));
      _internal2Java.put(Type.Boolean, new JavaType(Boolean.class, 1));
      _internal2Java.put(Type.Boolean, new JavaType(Object.class, 2));
      


      _internal2Java.put(Type.Real, new JavaType(Double.TYPE, 0));
      _internal2Java.put(Type.Real, new JavaType(Double.class, 1));
      _internal2Java.put(Type.Real, new JavaType(Float.TYPE, 2));
      _internal2Java.put(Type.Real, new JavaType(Long.TYPE, 3));
      _internal2Java.put(Type.Real, new JavaType(Integer.TYPE, 4));
      _internal2Java.put(Type.Real, new JavaType(Short.TYPE, 5));
      _internal2Java.put(Type.Real, new JavaType(Byte.TYPE, 6));
      _internal2Java.put(Type.Real, new JavaType(Character.TYPE, 7));
      _internal2Java.put(Type.Real, new JavaType(Object.class, 8));
      

      _internal2Java.put(Type.Int, new JavaType(Double.TYPE, 0));
      _internal2Java.put(Type.Int, new JavaType(Double.class, 1));
      _internal2Java.put(Type.Int, new JavaType(Float.TYPE, 2));
      _internal2Java.put(Type.Int, new JavaType(Long.TYPE, 3));
      _internal2Java.put(Type.Int, new JavaType(Integer.TYPE, 4));
      _internal2Java.put(Type.Int, new JavaType(Short.TYPE, 5));
      _internal2Java.put(Type.Int, new JavaType(Byte.TYPE, 6));
      _internal2Java.put(Type.Int, new JavaType(Character.TYPE, 7));
      _internal2Java.put(Type.Int, new JavaType(Object.class, 8));
      

      _internal2Java.put(Type.String, new JavaType(String.class, 0));
      _internal2Java.put(Type.String, new JavaType(Object.class, 1));
      

      _internal2Java.put(Type.NodeSet, new JavaType(nodeListClass, 0));
      _internal2Java.put(Type.NodeSet, new JavaType(nodeClass, 1));
      _internal2Java.put(Type.NodeSet, new JavaType(Object.class, 2));
      _internal2Java.put(Type.NodeSet, new JavaType(String.class, 3));
      

      _internal2Java.put(Type.Node, new JavaType(nodeListClass, 0));
      _internal2Java.put(Type.Node, new JavaType(nodeClass, 1));
      _internal2Java.put(Type.Node, new JavaType(Object.class, 2));
      _internal2Java.put(Type.Node, new JavaType(String.class, 3));
      

      _internal2Java.put(Type.ResultTree, new JavaType(nodeListClass, 0));
      _internal2Java.put(Type.ResultTree, new JavaType(nodeClass, 1));
      _internal2Java.put(Type.ResultTree, new JavaType(Object.class, 2));
      _internal2Java.put(Type.ResultTree, new JavaType(String.class, 3));
      
      _internal2Java.put(Type.Reference, new JavaType(Object.class, 0));
      

      _java2Internal.put(Boolean.TYPE, Type.Boolean);
      _java2Internal.put(Void.TYPE, Type.Void);
      _java2Internal.put(Character.TYPE, Type.Real);
      _java2Internal.put(Byte.TYPE, Type.Real);
      _java2Internal.put(Short.TYPE, Type.Real);
      _java2Internal.put(Integer.TYPE, Type.Real);
      _java2Internal.put(Long.TYPE, Type.Real);
      _java2Internal.put(Float.TYPE, Type.Real);
      _java2Internal.put(Double.TYPE, Type.Real);
      
      _java2Internal.put(String.class, Type.String);
      
      _java2Internal.put(Object.class, Type.Reference);
      

      _java2Internal.put(nodeListClass, Type.NodeSet);
      _java2Internal.put(nodeClass, Type.NodeSet);
      

      _extensionNamespaceTable.put("http://xml.apache.org/xalan", "org.apache.xalan.lib.Extensions");
      _extensionNamespaceTable.put("http://exslt.org/common", "org.apache.xalan.lib.ExsltCommon");
      _extensionNamespaceTable.put("http://exslt.org/math", "org.apache.xalan.lib.ExsltMath");
      _extensionNamespaceTable.put("http://exslt.org/sets", "org.apache.xalan.lib.ExsltSets");
      _extensionNamespaceTable.put("http://exslt.org/dates-and-times", "org.apache.xalan.lib.ExsltDatetime");
      _extensionNamespaceTable.put("http://exslt.org/strings", "org.apache.xalan.lib.ExsltStrings");
      

      _extensionFunctionTable.put("http://exslt.org/common:nodeSet", "nodeset");
      _extensionFunctionTable.put("http://exslt.org/common:objectType", "objectType");
      _extensionFunctionTable.put("http://xml.apache.org/xalan:nodeset", "nodeset");
    }
    catch (ClassNotFoundException e) {
      System.err.println(e);
    }
  }
  
  public FunctionCall(QName fname, Vector arguments) {
    _fname = fname;
    _arguments = arguments;
    _type = null;
  }
  
  public FunctionCall(QName fname) {
    this(fname, EMPTY_ARG_LIST);
  }
  
  public String getName() {
    return _fname.toString();
  }
  
  public void setParser(Parser parser) {
    super.setParser(parser);
    if (_arguments != null) {
      int n = _arguments.size();
      for (int i = 0; i < n; i++) {
        Expression exp = (Expression)_arguments.elementAt(i);
        exp.setParser(parser);
        exp.setParent(this);
      }
    }
  }
  
  public String getClassNameFromUri(String uri)
  {
    String className = (String)_extensionNamespaceTable.get(uri);
    
    if (className != null) {
      return className;
    }
    if (uri.startsWith("http://xml.apache.org/xalan/xsltc/java")) {
      int length = "http://xml.apache.org/xalan/xsltc/java".length() + 1;
      return uri.length() > length ? uri.substring(length) : "";
    }
    if (uri.startsWith("http://xml.apache.org/xalan/java")) {
      int length = "http://xml.apache.org/xalan/java".length() + 1;
      return uri.length() > length ? uri.substring(length) : "";
    }
    if (uri.startsWith("http://xml.apache.org/xslt/java")) {
      int length = "http://xml.apache.org/xslt/java".length() + 1;
      return uri.length() > length ? uri.substring(length) : "";
    }
    
    int index = uri.lastIndexOf('/');
    return index > 0 ? uri.substring(index + 1) : uri;
  }
  






  public Type typeCheck(SymbolTable stable)
    throws TypeCheckError
  {
    if (_type != null) { return _type;
    }
    String namespace = _fname.getNamespace();
    String local = _fname.getLocalPart();
    
    if (isExtension()) {
      _fname = new QName(null, null, local);
      return typeCheckStandard(stable);
    }
    if (isStandard()) {
      return typeCheckStandard(stable);
    }
    
    try
    {
      _className = getClassNameFromUri(namespace);
      
      int pos = local.lastIndexOf('.');
      if (pos > 0) {
        _isStatic = true;
        if ((_className != null) && (_className.length() > 0)) {
          _namespace_format = 2;
          _className = (_className + "." + local.substring(0, pos));
        }
        else {
          _namespace_format = 0;
          _className = local.substring(0, pos);
        }
        
        _fname = new QName(namespace, null, local.substring(pos + 1));
      }
      else {
        if ((_className != null) && (_className.length() > 0)) {
          try {
            _clazz = ObjectFactory.findProviderClass(_className, ObjectFactory.findClassLoader(), true);
            
            _namespace_format = 1;
          }
          catch (ClassNotFoundException e) {
            _namespace_format = 2;
          }
          
        } else {
          _namespace_format = 0;
        }
        if (local.indexOf('-') > 0) {
          local = replaceDash(local);
        }
        
        String extFunction = (String)_extensionFunctionTable.get(namespace + ":" + local);
        if (extFunction != null) {
          _fname = new QName(null, null, extFunction);
          return typeCheckStandard(stable);
        }
        
        _fname = new QName(namespace, null, local);
      }
      
      return typeCheckExternal(stable);
    }
    catch (TypeCheckError e) {
      ErrorMsg errorMsg = e.getErrorMsg();
      if (errorMsg == null) {
        String name = _fname.getLocalPart();
        errorMsg = new ErrorMsg("METHOD_NOT_FOUND_ERR", name);
      }
      getParser().reportError(3, errorMsg); }
    return this._type = Type.Void;
  }
  





  public Type typeCheckStandard(SymbolTable stable)
    throws TypeCheckError
  {
    _fname.clearNamespace();
    
    int n = _arguments.size();
    Vector argsType = typeCheckArgs(stable);
    MethodType args = new MethodType(Type.Void, argsType);
    MethodType ptype = lookupPrimop(stable, _fname.getLocalPart(), args);
    

    if (ptype != null) {
      for (int i = 0; i < n; i++) {
        Type argType = (Type)ptype.argsType().elementAt(i);
        Expression exp = (Expression)_arguments.elementAt(i);
        if (!argType.identicalTo(exp.getType())) {
          try {
            _arguments.setElementAt(new CastExpr(exp, argType), i);
          }
          catch (TypeCheckError e) {
            throw new TypeCheckError(this);
          }
        }
      }
      _chosenMethodType = ptype;
      return this._type = ptype.resultType();
    }
    throw new TypeCheckError(this);
  }
  
  public Type typeCheckConstructor(SymbolTable stable)
    throws TypeCheckError
  {
    Vector constructors = findConstructors();
    if (constructors == null)
    {
      throw new TypeCheckError("CONSTRUCTOR_NOT_FOUND", _className);
    }
    


    int nConstructors = constructors.size();
    int nArgs = _arguments.size();
    Vector argsType = typeCheckArgs(stable);
    

    int bestConstrDistance = Integer.MAX_VALUE;
    _type = null;
    for (int i = 0; i < nConstructors; i++)
    {
      Constructor constructor = (Constructor)constructors.elementAt(i);
      
      Class[] paramTypes = constructor.getParameterTypes();
      
      Class extType = null;
      int currConstrDistance = 0;
      for (int j = 0; j < nArgs; j++)
      {
        extType = paramTypes[j];
        Type intType = (Type)argsType.elementAt(j);
        Object match = _internal2Java.maps(intType, extType);
        if (match != null) {
          currConstrDistance += distance;
        }
        else if ((intType instanceof ObjectType)) {
          ObjectType objectType = (ObjectType)intType;
          if (objectType.getJavaClass() != extType)
          {
            if (extType.isAssignableFrom(objectType.getJavaClass())) {
              currConstrDistance++;
            } else {
              currConstrDistance = Integer.MAX_VALUE;
              break;
            }
          }
        }
        else {
          currConstrDistance = Integer.MAX_VALUE;
          break;
        }
      }
      
      if ((j == nArgs) && (currConstrDistance < bestConstrDistance)) {
        _chosenConstructor = constructor;
        _isExtConstructor = true;
        bestConstrDistance = currConstrDistance;
        
        _type = (_clazz != null ? Type.newObjectType(_clazz) : Type.newObjectType(_className));
      }
    }
    

    if (_type != null) {
      return _type;
    }
    
    throw new TypeCheckError("ARGUMENT_CONVERSION_ERR", getMethodSignature(argsType));
  }
  






  public Type typeCheckExternal(SymbolTable stable)
    throws TypeCheckError
  {
    int nArgs = _arguments.size();
    String name = _fname.getLocalPart();
    

    if (_fname.getLocalPart().equals("new")) {
      return typeCheckConstructor(stable);
    }
    

    boolean hasThisArgument = false;
    
    if (nArgs == 0) {
      _isStatic = true;
    }
    if (!_isStatic) {
      if ((_namespace_format == 0) || (_namespace_format == 2))
      {
        hasThisArgument = true;
      }
      Expression firstArg = (Expression)_arguments.elementAt(0);
      Type firstArgType = firstArg.typeCheck(stable);
      
      if ((_namespace_format == 1) && ((firstArgType instanceof ObjectType)) && (_clazz != null) && (_clazz.isAssignableFrom(((ObjectType)firstArgType).getJavaClass())))
      {


        hasThisArgument = true;
      }
      if (hasThisArgument) {
        _thisArgument = ((Expression)_arguments.elementAt(0));
        _arguments.remove(0);nArgs--;
        if ((firstArgType instanceof ObjectType)) {
          _className = ((ObjectType)firstArgType).getJavaClassName();
        }
        else {
          throw new TypeCheckError("NO_JAVA_FUNCT_THIS_REF", name);
        }
      }
    } else if (_className.length() == 0)
    {





      Parser parser = getParser();
      if (parser != null) {
        reportWarning(this, parser, "FUNCTION_RESOLVE_ERR", _fname.toString());
      }
      
      unresolvedExternal = true;
      return this._type = Type.Int;
    }
    

    Vector methods = findMethods();
    
    if (methods == null)
    {
      throw new TypeCheckError("METHOD_NOT_FOUND_ERR", _className + "." + name);
    }
    
    Class extType = null;
    int nMethods = methods.size();
    Vector argsType = typeCheckArgs(stable);
    

    int bestMethodDistance = Integer.MAX_VALUE;
    _type = null;
    for (int i = 0; i < nMethods; i++)
    {
      Method method = (Method)methods.elementAt(i);
      Class[] paramTypes = method.getParameterTypes();
      
      int currMethodDistance = 0;
      for (int j = 0; j < nArgs; j++)
      {
        extType = paramTypes[j];
        Type intType = (Type)argsType.elementAt(j);
        Object match = _internal2Java.maps(intType, extType);
        if (match != null) {
          currMethodDistance += distance;





        }
        else if ((intType instanceof ReferenceType)) {
          currMethodDistance++;
        }
        else if ((intType instanceof ObjectType)) {
          ObjectType object = (ObjectType)intType;
          if (extType.getName().equals(object.getJavaClassName())) {
            currMethodDistance += 0;
          } else if (extType.isAssignableFrom(object.getJavaClass())) {
            currMethodDistance++;
          } else {
            currMethodDistance = Integer.MAX_VALUE;
            break;
          }
        }
        else {
          currMethodDistance = Integer.MAX_VALUE;
          break;
        }
      }
      

      if (j == nArgs)
      {
        extType = method.getReturnType();
        
        _type = ((Type)_java2Internal.get(extType));
        if (_type == null) {
          _type = Type.newObjectType(extType);
        }
        

        if ((_type != null) && (currMethodDistance < bestMethodDistance)) {
          _chosenMethod = method;
          bestMethodDistance = currMethodDistance;
        }
      }
    }
    


    if ((_chosenMethod != null) && (_thisArgument == null) && (!Modifier.isStatic(_chosenMethod.getModifiers())))
    {
      throw new TypeCheckError("NO_JAVA_FUNCT_THIS_REF", getMethodSignature(argsType));
    }
    
    if (_type != null) {
      if (_type == Type.NodeSet) {
        getXSLTC().setMultiDocument(true);
      }
      return _type;
    }
    
    throw new TypeCheckError("ARGUMENT_CONVERSION_ERR", getMethodSignature(argsType));
  }
  

  public Vector typeCheckArgs(SymbolTable stable)
    throws TypeCheckError
  {
    Vector result = new Vector();
    Enumeration e = _arguments.elements();
    while (e.hasMoreElements()) {
      Expression exp = (Expression)e.nextElement();
      result.addElement(exp.typeCheck(stable));
    }
    return result;
  }
  
  protected final Expression argument(int i) {
    return (Expression)_arguments.elementAt(i);
  }
  
  protected final Expression argument() {
    return argument(0);
  }
  
  protected final int argumentCount() {
    return _arguments.size();
  }
  
  protected final void setArgument(int i, Expression exp) {
    _arguments.setElementAt(exp, i);
  }
  





  public void translateDesynthesized(ClassGenerator classGen, MethodGenerator methodGen)
  {
    Type type = Type.Boolean;
    if (_chosenMethodType != null) {
      type = _chosenMethodType.resultType();
    }
    InstructionList il = methodGen.getInstructionList();
    translate(classGen, methodGen);
    
    if (((type instanceof BooleanType)) || ((type instanceof IntType))) {
      _falseList.add(il.append(new IFEQ(null)));
    }
  }
  




  public void translate(ClassGenerator classGen, MethodGenerator methodGen)
  {
    int n = argumentCount();
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = methodGen.getInstructionList();
    boolean isSecureProcessing = classGen.getParser().getXSLTC().isSecureProcessing();
    


    if ((isStandard()) || (isExtension())) {
      for (int i = 0; i < n; i++) {
        Expression exp = argument(i);
        exp.translate(classGen, methodGen);
        exp.startIterator(classGen, methodGen);
      }
      

      String name = _fname.toString().replace('-', '_') + "F";
      String args = "";
      

      if (name.equals("sumF")) {
        args = "Lorg/apache/xalan/xsltc/DOM;";
        il.append(methodGen.loadDOM());
      }
      else if ((name.equals("normalize_spaceF")) && 
        (_chosenMethodType.toSignature(args).equals("()Ljava/lang/String;")))
      {
        args = "ILorg/apache/xalan/xsltc/DOM;";
        il.append(methodGen.loadContextNode());
        il.append(methodGen.loadDOM());
      }
      


      int index = cpg.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", name, _chosenMethodType.toSignature(args));
      
      il.append(new INVOKESTATIC(index));


    }
    else if (unresolvedExternal) {
      int index = cpg.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "unresolved_externalF", "(Ljava/lang/String;)V");
      

      il.append(new PUSH(cpg, _fname.toString()));
      il.append(new INVOKESTATIC(index));
    }
    else if (_isExtConstructor) {
      if (isSecureProcessing) {
        translateUnallowedExtension(cpg, il);
      }
      String clazz = _chosenConstructor.getDeclaringClass().getName();
      
      Class[] paramTypes = _chosenConstructor.getParameterTypes();
      LocalVariableGen[] paramTemp = new LocalVariableGen[n];
      









      for (int i = 0; i < n; i++) {
        Expression exp = argument(i);
        Type expType = exp.getType();
        exp.translate(classGen, methodGen);
        
        exp.startIterator(classGen, methodGen);
        expType.translateTo(classGen, methodGen, paramTypes[i]);
        paramTemp[i] = methodGen.addLocalVariable("function_call_tmp" + i, expType.toJCType(), null, null);
        


        paramTemp[i].setStart(il.append(expType.STORE(paramTemp[i].getIndex())));
      }
      

      il.append(new NEW(cpg.addClass(_className)));
      il.append(InstructionConstants.DUP);
      
      for (int i = 0; i < n; i++) {
        Expression arg = argument(i);
        paramTemp[i].setEnd(il.append(arg.getType().LOAD(paramTemp[i].getIndex())));
      }
      

      StringBuffer buffer = new StringBuffer();
      buffer.append('(');
      for (int i = 0; i < paramTypes.length; i++) {
        buffer.append(getSignature(paramTypes[i]));
      }
      buffer.append(')');
      buffer.append("V");
      
      int index = cpg.addMethodref(clazz, "<init>", buffer.toString());
      

      il.append(new INVOKESPECIAL(index));
      

      Type.Object.translateFrom(classGen, methodGen, _chosenConstructor.getDeclaringClass());

    }
    else
    {

      if (isSecureProcessing) {
        translateUnallowedExtension(cpg, il);
      }
      String clazz = _chosenMethod.getDeclaringClass().getName();
      Class[] paramTypes = _chosenMethod.getParameterTypes();
      

      if (_thisArgument != null) {
        _thisArgument.translate(classGen, methodGen);
      }
      
      for (int i = 0; i < n; i++) {
        Expression exp = argument(i);
        exp.translate(classGen, methodGen);
        
        exp.startIterator(classGen, methodGen);
        exp.getType().translateTo(classGen, methodGen, paramTypes[i]);
      }
      
      StringBuffer buffer = new StringBuffer();
      buffer.append('(');
      for (int i = 0; i < paramTypes.length; i++) {
        buffer.append(getSignature(paramTypes[i]));
      }
      buffer.append(')');
      buffer.append(getSignature(_chosenMethod.getReturnType()));
      
      if ((_thisArgument != null) && (_clazz.isInterface())) {
        int index = cpg.addInterfaceMethodref(clazz, _fname.getLocalPart(), buffer.toString());
        

        il.append(new INVOKEINTERFACE(index, n + 1));
      }
      else {
        int index = cpg.addMethodref(clazz, _fname.getLocalPart(), buffer.toString());
        

        il.append(_thisArgument != null ? new INVOKEVIRTUAL(index) : new INVOKESTATIC(index));
      }
      


      _type.translateFrom(classGen, methodGen, _chosenMethod.getReturnType());
    }
  }
  
  public String toString()
  {
    return "funcall(" + _fname + ", " + _arguments + ')';
  }
  
  public boolean isStandard() {
    String namespace = _fname.getNamespace();
    return (namespace == null) || (namespace.equals(""));
  }
  
  public boolean isExtension() {
    String namespace = _fname.getNamespace();
    return (namespace != null) && (namespace.equals("http://xml.apache.org/xalan/xsltc"));
  }
  





  private Vector findMethods()
  {
    Vector result = null;
    String namespace = _fname.getNamespace();
    
    if ((_className != null) && (_className.length() > 0)) {
      int nArgs = _arguments.size();
      try {
        if (_clazz == null) {
          _clazz = ObjectFactory.findProviderClass(_className, ObjectFactory.findClassLoader(), true);
          

          if (_clazz == null) {
            ErrorMsg msg = new ErrorMsg("CLASS_NOT_FOUND_ERR", _className);
            
            getParser().reportError(3, msg);
          }
        }
        
        String methodName = _fname.getLocalPart();
        Method[] methods = _clazz.getMethods();
        
        for (int i = 0; i < methods.length; i++) {
          int mods = methods[i].getModifiers();
          
          if ((Modifier.isPublic(mods)) && (methods[i].getName().equals(methodName)) && (methods[i].getParameterTypes().length == nArgs))
          {


            if (result == null) {
              result = new Vector();
            }
            result.addElement(methods[i]);
          }
        }
      }
      catch (ClassNotFoundException e) {
        ErrorMsg msg = new ErrorMsg("CLASS_NOT_FOUND_ERR", _className);
        getParser().reportError(3, msg);
      }
    }
    return result;
  }
  




  private Vector findConstructors()
  {
    Vector result = null;
    String namespace = _fname.getNamespace();
    
    int nArgs = _arguments.size();
    try {
      if (_clazz == null) {
        _clazz = ObjectFactory.findProviderClass(_className, ObjectFactory.findClassLoader(), true);
        

        if (_clazz == null) {
          ErrorMsg msg = new ErrorMsg("CLASS_NOT_FOUND_ERR", _className);
          getParser().reportError(3, msg);
        }
      }
      
      Constructor[] constructors = _clazz.getConstructors();
      
      for (int i = 0; i < constructors.length; i++) {
        int mods = constructors[i].getModifiers();
        
        if ((Modifier.isPublic(mods)) && (constructors[i].getParameterTypes().length == nArgs))
        {

          if (result == null) {
            result = new Vector();
          }
          result.addElement(constructors[i]);
        }
      }
    }
    catch (ClassNotFoundException e) {
      ErrorMsg msg = new ErrorMsg("CLASS_NOT_FOUND_ERR", _className);
      getParser().reportError(3, msg);
    }
    
    return result;
  }
  



  static final String getSignature(Class clazz)
  {
    if (clazz.isArray()) {
      StringBuffer sb = new StringBuffer();
      Class cl = clazz;
      while (cl.isArray()) {
        sb.append("[");
        cl = cl.getComponentType();
      }
      sb.append(getSignature(cl));
      return sb.toString();
    }
    if (clazz.isPrimitive()) {
      if (clazz == Integer.TYPE) {
        return "I";
      }
      if (clazz == Byte.TYPE) {
        return "B";
      }
      if (clazz == Long.TYPE) {
        return "J";
      }
      if (clazz == Float.TYPE) {
        return "F";
      }
      if (clazz == Double.TYPE) {
        return "D";
      }
      if (clazz == Short.TYPE) {
        return "S";
      }
      if (clazz == Character.TYPE) {
        return "C";
      }
      if (clazz == Boolean.TYPE) {
        return "Z";
      }
      if (clazz == Void.TYPE) {
        return "V";
      }
      
      String name = clazz.toString();
      ErrorMsg err = new ErrorMsg("UNKNOWN_SIG_TYPE_ERR", name);
      throw new Error(err.toString());
    }
    

    return "L" + clazz.getName().replace('.', '/') + ';';
  }
  



  static final String getSignature(Method meth)
  {
    StringBuffer sb = new StringBuffer();
    sb.append('(');
    Class[] params = meth.getParameterTypes();
    for (int j = 0; j < params.length; j++) {
      sb.append(getSignature(params[j]));
    }
    return ')' + getSignature(meth.getReturnType());
  }
  



  static final String getSignature(Constructor cons)
  {
    StringBuffer sb = new StringBuffer();
    sb.append('(');
    Class[] params = cons.getParameterTypes();
    for (int j = 0; j < params.length; j++) {
      sb.append(getSignature(params[j]));
    }
    return ")V";
  }
  


  private String getMethodSignature(Vector argsType)
  {
    StringBuffer buf = new StringBuffer(_className);
    buf.append('.').append(_fname.getLocalPart()).append('(');
    
    int nArgs = argsType.size();
    for (int i = 0; i < nArgs; i++) {
      Type intType = (Type)argsType.elementAt(i);
      buf.append(intType.toString());
      if (i < nArgs - 1) { buf.append(", ");
      }
    }
    buf.append(')');
    return buf.toString();
  }
  





  protected static String replaceDash(String name)
  {
    char dash = '-';
    StringBuffer buff = new StringBuffer("");
    for (int i = 0; i < name.length(); i++) {
      if ((i > 0) && (name.charAt(i - 1) == dash)) {
        buff.append(Character.toUpperCase(name.charAt(i)));
      } else if (name.charAt(i) != dash)
        buff.append(name.charAt(i));
    }
    return buff.toString();
  }
  




  private void translateUnallowedExtension(ConstantPoolGen cpg, InstructionList il)
  {
    int index = cpg.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "unallowed_extension_functionF", "(Ljava/lang/String;)V");
    

    il.append(new PUSH(cpg, _fname.toString()));
    il.append(new INVOKESTATIC(index));
  }
}
