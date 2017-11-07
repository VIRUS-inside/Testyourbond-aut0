package com.sun.jna;




public class FunctionParameterContext
  extends ToNativeContext
{
  private Function function;
  


  private Object[] args;
  


  private int index;
  


  FunctionParameterContext(Function f, Object[] args, int index)
  {
    function = f;
    this.args = args;
    this.index = index;
  }
  
  public Function getFunction() { return function; }
  
  public Object[] getParameters() { return args; }
  public int getParameterIndex() { return index; }
}
