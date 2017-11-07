package net.sf.cglib.core;

import net.sf.cglib.asm..ClassVisitor;

public abstract interface ClassGenerator
{
  public abstract void generateClass(.ClassVisitor paramClassVisitor)
    throws Exception;
}
