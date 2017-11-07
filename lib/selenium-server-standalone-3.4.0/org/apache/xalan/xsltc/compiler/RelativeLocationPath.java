package org.apache.xalan.xsltc.compiler;

abstract class RelativeLocationPath
  extends Expression
{
  RelativeLocationPath() {}
  
  public abstract int getAxis();
  
  public abstract void setAxis(int paramInt);
}
