package org.apache.xalan.xsltc.compiler;

public abstract interface Closure
{
  public abstract boolean inInnerClass();
  
  public abstract Closure getParentClosure();
  
  public abstract String getInnerClassName();
  
  public abstract void addVariable(VariableRefBase paramVariableRefBase);
}
