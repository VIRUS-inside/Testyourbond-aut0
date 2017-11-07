package net.sourceforge.htmlunit.corejs.classfile;

import net.sourceforge.htmlunit.corejs.javascript.ObjToIntMap;
import net.sourceforge.htmlunit.corejs.javascript.UintMap;

























































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































final class ConstantPool
{
  private static final int ConstantPoolSize = 256;
  static final byte CONSTANT_Class = 7;
  static final byte CONSTANT_Fieldref = 9;
  static final byte CONSTANT_Methodref = 10;
  static final byte CONSTANT_InterfaceMethodref = 11;
  static final byte CONSTANT_String = 8;
  static final byte CONSTANT_Integer = 3;
  static final byte CONSTANT_Float = 4;
  static final byte CONSTANT_Long = 5;
  static final byte CONSTANT_Double = 6;
  static final byte CONSTANT_NameAndType = 12;
  static final byte CONSTANT_Utf8 = 1;
  private ClassFileWriter cfw;
  private static final int MAX_UTF_ENCODING_SIZE = 65535;
  
  ConstantPool(ClassFileWriter cfw)
  {
    this.cfw = cfw;
    itsTopIndex = 1;
    itsPool = new byte['Ā'];
    itsTop = 0;
  }
  






  int write(byte[] data, int offset)
  {
    offset = ClassFileWriter.putInt16((short)itsTopIndex, data, offset);
    System.arraycopy(itsPool, 0, data, offset, itsTop);
    offset += itsTop;
    return offset;
  }
  
  int getWriteSize() {
    return 2 + itsTop;
  }
  
  int addConstant(int k) {
    ensure(5);
    itsPool[(itsTop++)] = 3;
    itsTop = ClassFileWriter.putInt32(k, itsPool, itsTop);
    itsPoolTypes.put(itsTopIndex, 3);
    return (short)itsTopIndex++;
  }
  
  int addConstant(long k) {
    ensure(9);
    itsPool[(itsTop++)] = 5;
    itsTop = ClassFileWriter.putInt64(k, itsPool, itsTop);
    int index = itsTopIndex;
    itsTopIndex += 2;
    itsPoolTypes.put(index, 5);
    return index;
  }
  
  int addConstant(float k) {
    ensure(5);
    itsPool[(itsTop++)] = 4;
    int bits = Float.floatToIntBits(k);
    itsTop = ClassFileWriter.putInt32(bits, itsPool, itsTop);
    itsPoolTypes.put(itsTopIndex, 4);
    return itsTopIndex++;
  }
  
  int addConstant(double k) {
    ensure(9);
    itsPool[(itsTop++)] = 6;
    long bits = Double.doubleToLongBits(k);
    itsTop = ClassFileWriter.putInt64(bits, itsPool, itsTop);
    int index = itsTopIndex;
    itsTopIndex += 2;
    itsPoolTypes.put(index, 6);
    return index;
  }
  
  int addConstant(String k) {
    int utf8Index = 0xFFFF & addUtf8(k);
    int theIndex = itsStringConstHash.getInt(utf8Index, -1);
    if (theIndex == -1) {
      theIndex = itsTopIndex++;
      ensure(3);
      itsPool[(itsTop++)] = 8;
      itsTop = ClassFileWriter.putInt16(utf8Index, itsPool, itsTop);
      itsStringConstHash.put(utf8Index, theIndex);
    }
    itsPoolTypes.put(theIndex, 8);
    return theIndex;
  }
  
  boolean isUnderUtfEncodingLimit(String s) {
    int strLen = s.length();
    if (strLen * 3 <= 65535)
      return true;
    if (strLen > 65535) {
      return false;
    }
    return strLen == getUtfEncodingLimit(s, 0, strLen);
  }
  



  int getUtfEncodingLimit(String s, int start, int end)
  {
    if ((end - start) * 3 <= 65535) {
      return end;
    }
    int limit = 65535;
    for (int i = start; i != end; i++) {
      int c = s.charAt(i);
      if ((0 != c) && (c <= 127)) {
        limit--;
      } else if (c < 2047) {
        limit -= 2;
      } else {
        limit -= 3;
      }
      if (limit < 0) {
        return i;
      }
    }
    return end;
  }
  
  short addUtf8(String k) {
    int theIndex = itsUtf8Hash.get(k, -1);
    if (theIndex == -1) {
      int strLen = k.length();
      boolean tooBigString;
      boolean tooBigString; if (strLen > 65535) {
        tooBigString = true;
      } else {
        tooBigString = false;
        

        ensure(3 + strLen * 3);
        int top = itsTop;
        
        itsPool[(top++)] = 1;
        top += 2;
        
        char[] chars = cfw.getCharBuffer(strLen);
        k.getChars(0, strLen, chars, 0);
        
        for (int i = 0; i != strLen; i++) {
          int c = chars[i];
          if ((c != 0) && (c <= 127)) {
            itsPool[(top++)] = ((byte)c);
          } else if (c > 2047) {
            itsPool[(top++)] = ((byte)(0xE0 | c >> 12));
            itsPool[(top++)] = ((byte)(0x80 | c >> 6 & 0x3F));
            itsPool[(top++)] = ((byte)(0x80 | c & 0x3F));
          } else {
            itsPool[(top++)] = ((byte)(0xC0 | c >> 6));
            itsPool[(top++)] = ((byte)(0x80 | c & 0x3F));
          }
        }
        
        int utfLen = top - (itsTop + 1 + 2);
        if (utfLen > 65535) {
          tooBigString = true;
        }
        else {
          itsPool[(itsTop + 1)] = ((byte)(utfLen >>> 8));
          itsPool[(itsTop + 2)] = ((byte)utfLen);
          
          itsTop = top;
          theIndex = itsTopIndex++;
          itsUtf8Hash.put(k, theIndex);
        }
      }
      if (tooBigString) {
        throw new IllegalArgumentException("Too big string");
      }
    }
    setConstantData(theIndex, k);
    itsPoolTypes.put(theIndex, 1);
    return (short)theIndex;
  }
  
  private short addNameAndType(String name, String type) {
    short nameIndex = addUtf8(name);
    short typeIndex = addUtf8(type);
    ensure(5);
    itsPool[(itsTop++)] = 12;
    itsTop = ClassFileWriter.putInt16(nameIndex, itsPool, itsTop);
    itsTop = ClassFileWriter.putInt16(typeIndex, itsPool, itsTop);
    itsPoolTypes.put(itsTopIndex, 12);
    return (short)itsTopIndex++;
  }
  
  short addClass(String className) {
    int theIndex = itsClassHash.get(className, -1);
    if (theIndex == -1) {
      String slashed = className;
      if (className.indexOf('.') > 0) {
        slashed = ClassFileWriter.getSlashedForm(className);
        theIndex = itsClassHash.get(slashed, -1);
        if (theIndex != -1) {
          itsClassHash.put(className, theIndex);
        }
      }
      if (theIndex == -1) {
        int utf8Index = addUtf8(slashed);
        ensure(3);
        itsPool[(itsTop++)] = 7;
        itsTop = ClassFileWriter.putInt16(utf8Index, itsPool, itsTop);
        theIndex = itsTopIndex++;
        itsClassHash.put(slashed, theIndex);
        if (className != slashed) {
          itsClassHash.put(className, theIndex);
        }
      }
    }
    setConstantData(theIndex, className);
    itsPoolTypes.put(theIndex, 7);
    return (short)theIndex;
  }
  
  short addFieldRef(String className, String fieldName, String fieldType) {
    FieldOrMethodRef ref = new FieldOrMethodRef(className, fieldName, fieldType);
    

    int theIndex = itsFieldRefHash.get(ref, -1);
    if (theIndex == -1) {
      short ntIndex = addNameAndType(fieldName, fieldType);
      short classIndex = addClass(className);
      ensure(5);
      itsPool[(itsTop++)] = 9;
      itsTop = ClassFileWriter.putInt16(classIndex, itsPool, itsTop);
      itsTop = ClassFileWriter.putInt16(ntIndex, itsPool, itsTop);
      theIndex = itsTopIndex++;
      itsFieldRefHash.put(ref, theIndex);
    }
    setConstantData(theIndex, ref);
    itsPoolTypes.put(theIndex, 9);
    return (short)theIndex;
  }
  
  short addMethodRef(String className, String methodName, String methodType) {
    FieldOrMethodRef ref = new FieldOrMethodRef(className, methodName, methodType);
    

    int theIndex = itsMethodRefHash.get(ref, -1);
    if (theIndex == -1) {
      short ntIndex = addNameAndType(methodName, methodType);
      short classIndex = addClass(className);
      ensure(5);
      itsPool[(itsTop++)] = 10;
      itsTop = ClassFileWriter.putInt16(classIndex, itsPool, itsTop);
      itsTop = ClassFileWriter.putInt16(ntIndex, itsPool, itsTop);
      theIndex = itsTopIndex++;
      itsMethodRefHash.put(ref, theIndex);
    }
    setConstantData(theIndex, ref);
    itsPoolTypes.put(theIndex, 10);
    return (short)theIndex;
  }
  
  short addInterfaceMethodRef(String className, String methodName, String methodType)
  {
    short ntIndex = addNameAndType(methodName, methodType);
    short classIndex = addClass(className);
    ensure(5);
    itsPool[(itsTop++)] = 11;
    itsTop = ClassFileWriter.putInt16(classIndex, itsPool, itsTop);
    itsTop = ClassFileWriter.putInt16(ntIndex, itsPool, itsTop);
    FieldOrMethodRef r = new FieldOrMethodRef(className, methodName, methodType);
    
    setConstantData(itsTopIndex, r);
    itsPoolTypes.put(itsTopIndex, 11);
    return (short)itsTopIndex++;
  }
  
  Object getConstantData(int index) {
    return itsConstantData.getObject(index);
  }
  
  void setConstantData(int index, Object data) {
    itsConstantData.put(index, data);
  }
  
  byte getConstantType(int index) {
    return (byte)itsPoolTypes.getInt(index, 0);
  }
  
  void ensure(int howMuch) {
    if (itsTop + howMuch > itsPool.length) {
      int newCapacity = itsPool.length * 2;
      if (itsTop + howMuch > newCapacity) {
        newCapacity = itsTop + howMuch;
      }
      byte[] tmp = new byte[newCapacity];
      System.arraycopy(itsPool, 0, tmp, 0, itsTop);
      itsPool = tmp;
    }
  }
  




  private UintMap itsStringConstHash = new UintMap();
  private ObjToIntMap itsUtf8Hash = new ObjToIntMap();
  private ObjToIntMap itsFieldRefHash = new ObjToIntMap();
  private ObjToIntMap itsMethodRefHash = new ObjToIntMap();
  private ObjToIntMap itsClassHash = new ObjToIntMap();
  
  private int itsTop;
  private int itsTopIndex;
  private UintMap itsConstantData = new UintMap();
  private UintMap itsPoolTypes = new UintMap();
  private byte[] itsPool;
}