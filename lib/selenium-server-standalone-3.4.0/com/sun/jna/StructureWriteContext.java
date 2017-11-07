package com.sun.jna;

import java.lang.reflect.Field;













public class StructureWriteContext
  extends ToNativeContext
{
  private Structure struct;
  private Field field;
  
  StructureWriteContext(Structure struct, Field field)
  {
    this.struct = struct;
    this.field = field;
  }
  
  public Structure getStructure() { return struct; }
  
  public Field getField() {
    return field;
  }
}
