package org.apache.bcel.generic;

































































public final class BasicType
  extends Type
{
  BasicType(byte type)
  {
    super(type, org.apache.bcel.Constants.SHORT_TYPE_NAMES[type]);
    
    if ((type < 4) || (type > 12))
      throw new ClassGenException("Invalid type: " + type);
  }
  
  public static final BasicType getType(byte type) {
    switch (type) {
    case 12:  return Type.VOID;
    case 4:  return Type.BOOLEAN;
    case 8:  return Type.BYTE;
    case 9:  return Type.SHORT;
    case 5:  return Type.CHAR;
    case 10:  return Type.INT;
    case 11:  return Type.LONG;
    case 7:  return Type.DOUBLE;
    case 6:  return Type.FLOAT;
    }
    
    throw new ClassGenException("Invalid type: " + type);
  }
  


  public boolean equals(Object type)
  {
    return type == this.type;
  }
}
