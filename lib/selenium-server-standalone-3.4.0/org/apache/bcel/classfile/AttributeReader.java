package org.apache.bcel.classfile;

import java.io.DataInputStream;

public abstract interface AttributeReader
{
  public abstract Attribute createAttribute(int paramInt1, int paramInt2, DataInputStream paramDataInputStream, ConstantPool paramConstantPool);
}
