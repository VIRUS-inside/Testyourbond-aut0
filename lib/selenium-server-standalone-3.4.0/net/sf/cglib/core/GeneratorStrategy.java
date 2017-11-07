package net.sf.cglib.core;

public abstract interface GeneratorStrategy
{
  public abstract byte[] generate(ClassGenerator paramClassGenerator)
    throws Exception;
  
  public abstract boolean equals(Object paramObject);
}
