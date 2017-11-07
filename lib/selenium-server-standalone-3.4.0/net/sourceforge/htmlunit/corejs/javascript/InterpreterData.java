package net.sourceforge.htmlunit.corejs.javascript;

import net.sourceforge.htmlunit.corejs.javascript.debug.DebuggableScript;

final class InterpreterData implements java.io.Serializable, DebuggableScript {
  static final long serialVersionUID = 5067677351589230234L;
  static final int INITIAL_MAX_ICODE_LENGTH = 1024;
  static final int INITIAL_STRINGTABLE_SIZE = 64;
  static final int INITIAL_NUMBERTABLE_SIZE = 64;
  String itsName;
  String itsSourceFile;
  boolean itsNeedsActivation;
  int itsFunctionType;
  String[] itsStringTable;
  double[] itsDoubleTable;
  InterpreterData[] itsNestedFunctions;
  Object[] itsRegExpLiterals;
  byte[] itsICode;
  int[] itsExceptionTable;
  int itsMaxVars;
  
  InterpreterData(int languageVersion, String sourceFile, String encodedSource, boolean isStrict) { this.languageVersion = languageVersion;
    itsSourceFile = sourceFile;
    this.encodedSource = encodedSource;
    this.isStrict = isStrict;
    init();
  }
  
  InterpreterData(InterpreterData parent) {
    parentData = parent;
    languageVersion = languageVersion;
    itsSourceFile = itsSourceFile;
    encodedSource = encodedSource;
    
    init();
  }
  
  private void init() {
    itsICode = new byte['Ѐ'];
    itsStringTable = new String[64];
  }
  


  int itsMaxLocals;
  

  int itsMaxStack;
  

  int itsMaxFrameArray;
  

  String[] argNames;
  

  boolean[] argIsConst;
  

  int argCount;
  

  int itsMaxCalleeArgs;
  

  String encodedSource;
  
  int encodedSourceStart;
  
  int encodedSourceEnd;
  
  int languageVersion;
  
  boolean isStrict;
  
  boolean topLevel;
  
  Object[] literalIds;
  
  UintMap longJumps;
  
  int firstLinePC = -1;
  
  InterpreterData parentData;
  
  boolean evalScriptFlag;
  
  boolean declaredAsVar;
  
  public boolean isTopLevel()
  {
    return topLevel;
  }
  
  public boolean isFunction() {
    return itsFunctionType != 0;
  }
  
  public String getFunctionName() {
    return itsName;
  }
  
  public int getParamCount() {
    return argCount;
  }
  
  public int getParamAndVarCount() {
    return argNames.length;
  }
  
  public String getParamOrVarName(int index) {
    return argNames[index];
  }
  
  public boolean getParamOrVarConst(int index) {
    return argIsConst[index];
  }
  
  public String getSourceName() {
    return itsSourceFile;
  }
  
  public boolean isGeneratedScript() {
    return ScriptRuntime.isGeneratedScript(itsSourceFile);
  }
  
  public int[] getLineNumbers() {
    return Interpreter.getLineNumbers(this);
  }
  
  public int getFunctionCount() {
    return itsNestedFunctions == null ? 0 : itsNestedFunctions.length;
  }
  
  public DebuggableScript getFunction(int index) {
    return itsNestedFunctions[index];
  }
  
  public DebuggableScript getParent() {
    return parentData;
  }
}
