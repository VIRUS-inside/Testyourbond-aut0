package org.apache.bcel.classfile;

public abstract interface Node
{
  public abstract void accept(Visitor paramVisitor);
}
