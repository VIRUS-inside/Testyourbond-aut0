package net.sourceforge.htmlunit.corejs.classfile;

















































































































































































































































































































































































































































































































































































































































final class ClassFileMethod
{
  private String itsName;
  















































































































































































































































































































































































































































































































































































































































  private String itsType;
  















































































































































































































































































































































































































































































































































































































































  private short itsNameIndex;
  















































































































































































































































































































































































































































































































































































































































  private short itsTypeIndex;
  















































































































































































































































































































































































































































































































































































































































  private short itsFlags;
  















































































































































































































































































































































































































































































































































































































































  private byte[] itsCodeAttribute;
  
















































































































































































































































































































































































































































































































































































































































  ClassFileMethod(String name, short nameIndex, String type, short typeIndex, short flags)
  {
    itsName = name;
    itsNameIndex = nameIndex;
    itsType = type;
    itsTypeIndex = typeIndex;
    itsFlags = flags;
  }
  
  void setCodeAttribute(byte[] codeAttribute) {
    itsCodeAttribute = codeAttribute;
  }
  
  int write(byte[] data, int offset) {
    offset = ClassFileWriter.putInt16(itsFlags, data, offset);
    offset = ClassFileWriter.putInt16(itsNameIndex, data, offset);
    offset = ClassFileWriter.putInt16(itsTypeIndex, data, offset);
    
    offset = ClassFileWriter.putInt16(1, data, offset);
    System.arraycopy(itsCodeAttribute, 0, data, offset, itsCodeAttribute.length);
    
    offset += itsCodeAttribute.length;
    return offset;
  }
  
  int getWriteSize() {
    return 8 + itsCodeAttribute.length;
  }
  
  String getName() {
    return itsName;
  }
  
  String getType() {
    return itsType;
  }
  
  short getFlags() {
    return itsFlags;
  }
}