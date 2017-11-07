package net.sourceforge.htmlunit.corejs.javascript;

import java.util.Set;

public class CompilerEnvirons { private ErrorReporter errorReporter;
  private int languageVersion;
  private boolean generateDebugInfo;
  private boolean reservedKeywordAsIdentifier;
  private boolean allowMemberExprAsFunctionName;
  private boolean xmlAvailable;
  private int optimizationLevel;
  private boolean generatingSource;
  private boolean strictMode;
  
  public CompilerEnvirons() { errorReporter = DefaultErrorReporter.instance;
    languageVersion = 0;
    generateDebugInfo = true;
    reservedKeywordAsIdentifier = true;
    allowMemberExprAsFunctionName = false;
    xmlAvailable = true;
    optimizationLevel = 0;
    generatingSource = true;
    strictMode = false;
    warningAsError = false;
    generateObserverCount = false;
    allowSharpComments = false;
  }
  
  public void initFromContext(Context cx) {
    setErrorReporter(cx.getErrorReporter());
    languageVersion = cx.getLanguageVersion();
    
    generateDebugInfo = ((!cx.isGeneratingDebugChanged()) || (cx.isGeneratingDebug()));
    
    reservedKeywordAsIdentifier = cx.hasFeature(3);
    
    allowMemberExprAsFunctionName = cx.hasFeature(2);
    strictMode = cx.hasFeature(11);
    warningAsError = cx.hasFeature(12);
    xmlAvailable = cx.hasFeature(6);
    
    optimizationLevel = cx.getOptimizationLevel();
    
    generatingSource = cx.isGeneratingSource();
    activationNames = activationNames;
    

    generateObserverCount = generateObserverCount;
  }
  
  public final ErrorReporter getErrorReporter() {
    return errorReporter;
  }
  
  public void setErrorReporter(ErrorReporter errorReporter) {
    if (errorReporter == null)
      throw new IllegalArgumentException();
    this.errorReporter = errorReporter;
  }
  
  public final int getLanguageVersion() {
    return languageVersion;
  }
  
  public void setLanguageVersion(int languageVersion) {
    Context.checkLanguageVersion(languageVersion);
    this.languageVersion = languageVersion;
  }
  
  public final boolean isGenerateDebugInfo() {
    return generateDebugInfo;
  }
  
  public void setGenerateDebugInfo(boolean flag) {
    generateDebugInfo = flag;
  }
  
  public final boolean isReservedKeywordAsIdentifier() {
    return reservedKeywordAsIdentifier;
  }
  
  public void setReservedKeywordAsIdentifier(boolean flag) {
    reservedKeywordAsIdentifier = flag;
  }
  



  public final boolean isAllowMemberExprAsFunctionName()
  {
    return allowMemberExprAsFunctionName;
  }
  
  public void setAllowMemberExprAsFunctionName(boolean flag) {
    allowMemberExprAsFunctionName = flag;
  }
  
  public final boolean isXmlAvailable() {
    return xmlAvailable;
  }
  
  public void setXmlAvailable(boolean flag) {
    xmlAvailable = flag;
  }
  
  public final int getOptimizationLevel() {
    return optimizationLevel;
  }
  
  public void setOptimizationLevel(int level) {
    Context.checkOptimizationLevel(level);
    optimizationLevel = level;
  }
  
  public final boolean isGeneratingSource() {
    return generatingSource;
  }
  
  public boolean getWarnTrailingComma() {
    return warnTrailingComma;
  }
  
  public void setWarnTrailingComma(boolean warn) {
    warnTrailingComma = warn;
  }
  
  public final boolean isStrictMode() {
    return strictMode;
  }
  
  public void setStrictMode(boolean strict) {
    strictMode = strict;
  }
  
  public final boolean reportWarningAsError() {
    return warningAsError;
  }
  







  public void setGeneratingSource(boolean generatingSource)
  {
    this.generatingSource = generatingSource;
  }
  



  public boolean isGenerateObserverCount()
  {
    return generateObserverCount;
  }
  

  private boolean warningAsError;
  
  private boolean generateObserverCount;
  private boolean recordingComments;
  private boolean recordingLocalJsDocComments;
  private boolean recoverFromErrors;
  private boolean warnTrailingComma;
  private boolean ideMode;
  private boolean allowSharpComments;
  Set<String> activationNames;
  public void setGenerateObserverCount(boolean generateObserverCount)
  {
    this.generateObserverCount = generateObserverCount;
  }
  
  public boolean isRecordingComments() {
    return recordingComments;
  }
  
  public void setRecordingComments(boolean record) {
    recordingComments = record;
  }
  
  public boolean isRecordingLocalJsDocComments() {
    return recordingLocalJsDocComments;
  }
  
  public void setRecordingLocalJsDocComments(boolean record) {
    recordingLocalJsDocComments = record;
  }
  




  public void setRecoverFromErrors(boolean recover)
  {
    recoverFromErrors = recover;
  }
  
  public boolean recoverFromErrors() {
    return recoverFromErrors;
  }
  



  public void setIdeMode(boolean ide)
  {
    ideMode = ide;
  }
  
  public boolean isIdeMode() {
    return ideMode;
  }
  
  public Set<String> getActivationNames() {
    return activationNames;
  }
  
  public void setActivationNames(Set<String> activationNames) {
    this.activationNames = activationNames;
  }
  


  public void setAllowSharpComments(boolean allow)
  {
    allowSharpComments = allow;
  }
  
  public boolean getAllowSharpComments() {
    return allowSharpComments;
  }
  




  public static CompilerEnvirons ideEnvirons()
  {
    CompilerEnvirons env = new CompilerEnvirons();
    env.setRecoverFromErrors(true);
    env.setRecordingComments(true);
    env.setStrictMode(true);
    env.setWarnTrailingComma(true);
    env.setLanguageVersion(170);
    env.setReservedKeywordAsIdentifier(true);
    env.setIdeMode(true);
    env.setErrorReporter(new net.sourceforge.htmlunit.corejs.javascript.ast.ErrorCollector());
    return env;
  }
}
