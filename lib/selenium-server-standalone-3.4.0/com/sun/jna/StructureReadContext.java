package com.sun.jna;

import java.lang.reflect.Field;













public class StructureReadContext
  extends FromNativeContext
{
  private Structure structure;
  private Field field;
  
  StructureReadContext(Structure struct, Field field)
  {
    super(field.getType());
    structure = struct;
    this.field = field;
  }
  
  public Structure getStructure() { return structure; }
  
  public Field getField() { return field; }
}
