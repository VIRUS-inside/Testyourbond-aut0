package net.sourceforge.htmlunit.corejs.javascript.tools.shell;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ContextFactory;
import net.sourceforge.htmlunit.corejs.javascript.ErrorReporter;



public class ShellContextFactory
  extends ContextFactory
{
  private boolean strictMode;
  private boolean warningAsError;
  private int languageVersion = 180;
  private int optimizationLevel;
  private boolean generatingDebug;
  private boolean allowReservedKeywords = true;
  private ErrorReporter errorReporter;
  private String characterEncoding;
  
  public ShellContextFactory() {}
  
  protected boolean hasFeature(Context cx, int featureIndex) { switch (featureIndex) {
    case 8: 
    case 9: 
    case 11: 
      return strictMode;
    
    case 3: 
      return allowReservedKeywords;
    
    case 12: 
      return warningAsError;
    
    case 10: 
      return generatingDebug;
    }
    return super.hasFeature(cx, featureIndex);
  }
  
  protected void onContextCreated(Context cx)
  {
    cx.setLanguageVersion(languageVersion);
    cx.setOptimizationLevel(optimizationLevel);
    if (errorReporter != null) {
      cx.setErrorReporter(errorReporter);
    }
    cx.setGeneratingDebug(generatingDebug);
    super.onContextCreated(cx);
  }
  
  public void setStrictMode(boolean flag) {
    checkNotSealed();
    strictMode = flag;
  }
  
  public void setWarningAsError(boolean flag) {
    checkNotSealed();
    warningAsError = flag;
  }
  
  public void setLanguageVersion(int version) {
    Context.checkLanguageVersion(version);
    checkNotSealed();
    languageVersion = version;
  }
  
  public void setOptimizationLevel(int optimizationLevel) {
    Context.checkOptimizationLevel(optimizationLevel);
    checkNotSealed();
    this.optimizationLevel = optimizationLevel;
  }
  
  public void setErrorReporter(ErrorReporter errorReporter) {
    if (errorReporter == null)
      throw new IllegalArgumentException();
    this.errorReporter = errorReporter;
  }
  
  public void setGeneratingDebug(boolean generatingDebug) {
    this.generatingDebug = generatingDebug;
  }
  
  public String getCharacterEncoding() {
    return characterEncoding;
  }
  
  public void setCharacterEncoding(String characterEncoding) {
    this.characterEncoding = characterEncoding;
  }
  
  public void setAllowReservedKeywords(boolean allowReservedKeywords) {
    this.allowReservedKeywords = allowReservedKeywords;
  }
}
