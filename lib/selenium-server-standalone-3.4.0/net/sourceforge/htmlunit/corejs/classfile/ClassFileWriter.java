package net.sourceforge.htmlunit.corejs.classfile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import net.sourceforge.htmlunit.corejs.javascript.ObjArray;
import net.sourceforge.htmlunit.corejs.javascript.UintMap;





public class ClassFileWriter
{
  public static final short ACC_PUBLIC = 1;
  public static final short ACC_PRIVATE = 2;
  public static final short ACC_PROTECTED = 4;
  public static final short ACC_STATIC = 8;
  public static final short ACC_FINAL = 16;
  public static final short ACC_SUPER = 32;
  public static final short ACC_SYNCHRONIZED = 32;
  public static final short ACC_VOLATILE = 64;
  public static final short ACC_TRANSIENT = 128;
  public static final short ACC_NATIVE = 256;
  public static final short ACC_ABSTRACT = 1024;
  
  public static class ClassFileFormatException
    extends RuntimeException
  {
    private static final long serialVersionUID = 1263998431033790599L;
    
    ClassFileFormatException(String message)
    {
      super();
    }
  }
  













  public ClassFileWriter(String className, String superClassName, String sourceFileName)
  {
    generatedClassName = className;
    itsConstantPool = new ConstantPool(this);
    itsThisClassIndex = itsConstantPool.addClass(className);
    itsSuperClassIndex = itsConstantPool.addClass(superClassName);
    if (sourceFileName != null) {
      itsSourceFileNameIndex = itsConstantPool.addUtf8(sourceFileName);
    }
    

    itsFlags = 33;
  }
  
  public final String getClassName() {
    return generatedClassName;
  }
  









  public void addInterface(String interfaceName)
  {
    short interfaceIndex = itsConstantPool.addClass(interfaceName);
    itsInterfaces.add(Short.valueOf(interfaceIndex));
  }
  















  public void setFlags(short flags)
  {
    itsFlags = flags;
  }
  
  static String getSlashedForm(String name) {
    return name.replace('.', '/');
  }
  




  public static String classNameToSignature(String name)
  {
    int nameLength = name.length();
    int colonPos = 1 + nameLength;
    char[] buf = new char[colonPos + 1];
    buf[0] = 'L';
    buf[colonPos] = ';';
    name.getChars(0, nameLength, buf, 1);
    for (int i = 1; i != colonPos; i++) {
      if (buf[i] == '.') {
        buf[i] = '/';
      }
    }
    return new String(buf, 0, colonPos + 1);
  }
  










  public void addField(String fieldName, String type, short flags)
  {
    short fieldNameIndex = itsConstantPool.addUtf8(fieldName);
    short typeIndex = itsConstantPool.addUtf8(type);
    itsFields.add(new ClassFileField(fieldNameIndex, typeIndex, flags));
  }
  













  public void addField(String fieldName, String type, short flags, int value)
  {
    short fieldNameIndex = itsConstantPool.addUtf8(fieldName);
    short typeIndex = itsConstantPool.addUtf8(type);
    ClassFileField field = new ClassFileField(fieldNameIndex, typeIndex, flags);
    
    field.setAttributes(itsConstantPool.addUtf8("ConstantValue"), (short)0, (short)0, itsConstantPool
      .addConstant(value));
    itsFields.add(field);
  }
  













  public void addField(String fieldName, String type, short flags, long value)
  {
    short fieldNameIndex = itsConstantPool.addUtf8(fieldName);
    short typeIndex = itsConstantPool.addUtf8(type);
    ClassFileField field = new ClassFileField(fieldNameIndex, typeIndex, flags);
    
    field.setAttributes(itsConstantPool.addUtf8("ConstantValue"), (short)0, (short)2, itsConstantPool
      .addConstant(value));
    itsFields.add(field);
  }
  













  public void addField(String fieldName, String type, short flags, double value)
  {
    short fieldNameIndex = itsConstantPool.addUtf8(fieldName);
    short typeIndex = itsConstantPool.addUtf8(type);
    ClassFileField field = new ClassFileField(fieldNameIndex, typeIndex, flags);
    
    field.setAttributes(itsConstantPool.addUtf8("ConstantValue"), (short)0, (short)2, itsConstantPool
      .addConstant(value));
    itsFields.add(field);
  }
  















  public void addVariableDescriptor(String name, String type, int startPC, int register)
  {
    int nameIndex = itsConstantPool.addUtf8(name);
    int descriptorIndex = itsConstantPool.addUtf8(type);
    int[] chunk = { nameIndex, descriptorIndex, startPC, register };
    if (itsVarDescriptors == null) {
      itsVarDescriptors = new ObjArray();
    }
    itsVarDescriptors.add(chunk);
  }
  













  public void startMethod(String methodName, String type, short flags)
  {
    short methodNameIndex = itsConstantPool.addUtf8(methodName);
    short typeIndex = itsConstantPool.addUtf8(type);
    itsCurrentMethod = new ClassFileMethod(methodName, methodNameIndex, type, typeIndex, flags);
    
    itsJumpFroms = new UintMap();
    itsMethods.add(itsCurrentMethod);
    addSuperBlockStart(0);
  }
  









  public void stopMethod(short maxLocals)
  {
    if (itsCurrentMethod == null) {
      throw new IllegalStateException("No method to stop");
    }
    fixLabelGotos();
    
    itsMaxLocals = maxLocals;
    
    StackMapTable stackMap = null;
    if (GenerateStackMap) {
      finalizeSuperBlockStarts();
      stackMap = new StackMapTable();
      stackMap.generate();
    }
    
    int lineNumberTableLength = 0;
    if (itsLineNumberTable != null)
    {


      lineNumberTableLength = 8 + itsLineNumberTableTop * 4;
    }
    
    int variableTableLength = 0;
    if (itsVarDescriptors != null)
    {


      variableTableLength = 8 + itsVarDescriptors.size() * 10;
    }
    
    int stackMapTableLength = 0;
    if (stackMap != null) {
      int stackMapWriteSize = stackMap.computeWriteSize();
      if (stackMapWriteSize > 0) {
        stackMapTableLength = 6 + stackMapWriteSize;
      }
    }
    
    int attrLength = 14 + itsCodeBufferTop + 2 + itsExceptionTableTop * 8 + 2 + lineNumberTableLength + variableTableLength + stackMapTableLength;
    








    if (attrLength > 65536)
    {



      throw new ClassFileFormatException("generated bytecode for method exceeds 64K limit.");
    }
    
    byte[] codeAttribute = new byte[attrLength];
    int index = 0;
    int codeAttrIndex = itsConstantPool.addUtf8("Code");
    index = putInt16(codeAttrIndex, codeAttribute, index);
    attrLength -= 6;
    index = putInt32(attrLength, codeAttribute, index);
    index = putInt16(itsMaxStack, codeAttribute, index);
    index = putInt16(itsMaxLocals, codeAttribute, index);
    index = putInt32(itsCodeBufferTop, codeAttribute, index);
    System.arraycopy(itsCodeBuffer, 0, codeAttribute, index, itsCodeBufferTop);
    
    index += itsCodeBufferTop;
    
    if (itsExceptionTableTop > 0) {
      index = putInt16(itsExceptionTableTop, codeAttribute, index);
      for (int i = 0; i < itsExceptionTableTop; i++) {
        ExceptionTableEntry ete = itsExceptionTable[i];
        short startPC = (short)getLabelPC(itsStartLabel);
        short endPC = (short)getLabelPC(itsEndLabel);
        short handlerPC = (short)getLabelPC(itsHandlerLabel);
        short catchType = itsCatchType;
        if (startPC == -1)
          throw new IllegalStateException("start label not defined");
        if (endPC == -1)
          throw new IllegalStateException("end label not defined");
        if (handlerPC == -1) {
          throw new IllegalStateException("handler label not defined");
        }
        
        index = putInt16(startPC, codeAttribute, index);
        index = putInt16(endPC, codeAttribute, index);
        index = putInt16(handlerPC, codeAttribute, index);
        index = putInt16(catchType, codeAttribute, index);
      }
    }
    else {
      index = putInt16(0, codeAttribute, index);
    }
    
    int attributeCount = 0;
    if (itsLineNumberTable != null)
      attributeCount++;
    if (itsVarDescriptors != null)
      attributeCount++;
    if (stackMapTableLength > 0) {
      attributeCount++;
    }
    index = putInt16(attributeCount, codeAttribute, index);
    
    if (itsLineNumberTable != null)
    {
      int lineNumberTableAttrIndex = itsConstantPool.addUtf8("LineNumberTable");
      index = putInt16(lineNumberTableAttrIndex, codeAttribute, index);
      int tableAttrLength = 2 + itsLineNumberTableTop * 4;
      index = putInt32(tableAttrLength, codeAttribute, index);
      index = putInt16(itsLineNumberTableTop, codeAttribute, index);
      for (int i = 0; i < itsLineNumberTableTop; i++) {
        index = putInt32(itsLineNumberTable[i], codeAttribute, index);
      }
    }
    
    if (itsVarDescriptors != null)
    {
      int variableTableAttrIndex = itsConstantPool.addUtf8("LocalVariableTable");
      index = putInt16(variableTableAttrIndex, codeAttribute, index);
      int varCount = itsVarDescriptors.size();
      int tableAttrLength = 2 + varCount * 10;
      index = putInt32(tableAttrLength, codeAttribute, index);
      index = putInt16(varCount, codeAttribute, index);
      for (int i = 0; i < varCount; i++) {
        int[] chunk = (int[])itsVarDescriptors.get(i);
        int nameIndex = chunk[0];
        int descriptorIndex = chunk[1];
        int startPC = chunk[2];
        int register = chunk[3];
        int length = itsCodeBufferTop - startPC;
        
        index = putInt16(startPC, codeAttribute, index);
        index = putInt16(length, codeAttribute, index);
        index = putInt16(nameIndex, codeAttribute, index);
        index = putInt16(descriptorIndex, codeAttribute, index);
        index = putInt16(register, codeAttribute, index);
      }
    }
    
    if (stackMapTableLength > 0)
    {
      int stackMapTableAttrIndex = itsConstantPool.addUtf8("StackMapTable");
      int start = index;
      index = putInt16(stackMapTableAttrIndex, codeAttribute, index);
      index = stackMap.write(codeAttribute, index);
    }
    
    itsCurrentMethod.setCodeAttribute(codeAttribute);
    
    itsExceptionTable = null;
    itsExceptionTableTop = 0;
    itsLineNumberTableTop = 0;
    itsCodeBufferTop = 0;
    itsCurrentMethod = null;
    itsMaxStack = 0;
    itsStackTop = 0;
    itsLabelTableTop = 0;
    itsFixupTableTop = 0;
    itsVarDescriptors = null;
    itsSuperBlockStarts = null;
    itsSuperBlockStartsTop = 0;
    itsJumpFroms = null;
  }
  





  public void add(int theOpCode)
  {
    if (opcodeCount(theOpCode) != 0)
      throw new IllegalArgumentException("Unexpected operands");
    int newStack = itsStackTop + stackChange(theOpCode);
    if ((newStack < 0) || (32767 < newStack)) {
      badStack(newStack);
    }
    
    addToCodeBuffer(theOpCode);
    itsStackTop = ((short)newStack);
    if (newStack > itsMaxStack) {
      itsMaxStack = ((short)newStack);
    }
    


    if (theOpCode == 191) {
      addSuperBlockStart(itsCodeBufferTop);
    }
  }
  











  public void add(int theOpCode, int theOperand)
  {
    int newStack = itsStackTop + stackChange(theOpCode);
    if ((newStack < 0) || (32767 < newStack)) {
      badStack(newStack);
    }
    switch (theOpCode)
    {


    case 167: 
      addSuperBlockStart(itsCodeBufferTop + 3);
    
    case 153: 
    case 154: 
    case 155: 
    case 156: 
    case 157: 
    case 158: 
    case 159: 
    case 160: 
    case 161: 
    case 162: 
    case 163: 
    case 164: 
    case 165: 
    case 166: 
    case 168: 
    case 198: 
    case 199: 
      if (((theOperand & 0x80000000) != Integer.MIN_VALUE) && (
        (theOperand < 0) || (theOperand > 65535))) {
        throw new IllegalArgumentException("Bad label for branch");
      }
      int branchPC = itsCodeBufferTop;
      addToCodeBuffer(theOpCode);
      if ((theOperand & 0x80000000) != Integer.MIN_VALUE)
      {
        addToCodeInt16(theOperand);
        int target = theOperand + branchPC;
        addSuperBlockStart(target);
        itsJumpFroms.put(target, branchPC);
      } else {
        int targetPC = getLabelPC(theOperand);
        




        if (targetPC != -1) {
          int offset = targetPC - branchPC;
          addToCodeInt16(offset);
          addSuperBlockStart(targetPC);
          itsJumpFroms.put(targetPC, branchPC);
        } else {
          addLabelFixup(theOperand, branchPC + 1);
          addToCodeInt16(0);
        }
      }
      
      break;
    
    case 16: 
      if ((byte)theOperand != theOperand)
        throw new IllegalArgumentException("out of range byte");
      addToCodeBuffer(theOpCode);
      addToCodeBuffer((byte)theOperand);
      break;
    
    case 17: 
      if ((short)theOperand != theOperand)
        throw new IllegalArgumentException("out of range short");
      addToCodeBuffer(theOpCode);
      addToCodeInt16(theOperand);
      break;
    
    case 188: 
      if ((0 > theOperand) || (theOperand >= 256))
        throw new IllegalArgumentException("out of range index");
      addToCodeBuffer(theOpCode);
      addToCodeBuffer(theOperand);
      break;
    
    case 180: 
    case 181: 
      if ((0 > theOperand) || (theOperand >= 65536))
        throw new IllegalArgumentException("out of range field");
      addToCodeBuffer(theOpCode);
      addToCodeInt16(theOperand);
      break;
    
    case 18: 
    case 19: 
    case 20: 
      if ((0 > theOperand) || (theOperand >= 65536))
        throw new IllegalArgumentException("out of range index");
      if ((theOperand >= 256) || (theOpCode == 19) || (theOpCode == 20))
      {
        if (theOpCode == 18) {
          addToCodeBuffer(19);
        } else {
          addToCodeBuffer(theOpCode);
        }
        addToCodeInt16(theOperand);
      } else {
        addToCodeBuffer(theOpCode);
        addToCodeBuffer(theOperand);
      }
      break;
    
    case 21: 
    case 22: 
    case 23: 
    case 24: 
    case 25: 
    case 54: 
    case 55: 
    case 56: 
    case 57: 
    case 58: 
    case 169: 
      if ((0 > theOperand) || (theOperand >= 65536))
        throw new ClassFileFormatException("out of range variable");
      if (theOperand >= 256) {
        addToCodeBuffer(196);
        addToCodeBuffer(theOpCode);
        addToCodeInt16(theOperand);
      } else {
        addToCodeBuffer(theOpCode);
        addToCodeBuffer(theOperand);
      }
      break;
    
    default: 
      throw new IllegalArgumentException("Unexpected opcode for 1 operand");
    }
    
    
    itsStackTop = ((short)newStack);
    if (newStack > itsMaxStack) {
      itsMaxStack = ((short)newStack);
    }
  }
  








  public void addLoadConstant(int k)
  {
    switch (k) {
    case 0: 
      add(3);
      break;
    case 1: 
      add(4);
      break;
    case 2: 
      add(5);
      break;
    case 3: 
      add(6);
      break;
    case 4: 
      add(7);
      break;
    case 5: 
      add(8);
      break;
    default: 
      add(18, itsConstantPool.addConstant(k));
    }
    
  }
  





  public void addLoadConstant(long k)
  {
    add(20, itsConstantPool.addConstant(k));
  }
  





  public void addLoadConstant(float k)
  {
    add(18, itsConstantPool.addConstant(k));
  }
  





  public void addLoadConstant(double k)
  {
    add(20, itsConstantPool.addConstant(k));
  }
  





  public void addLoadConstant(String k)
  {
    add(18, itsConstantPool.addConstant(k));
  }
  














  public void add(int theOpCode, int theOperand1, int theOperand2)
  {
    int newStack = itsStackTop + stackChange(theOpCode);
    if ((newStack < 0) || (32767 < newStack)) {
      badStack(newStack);
    }
    if (theOpCode == 132) {
      if ((0 > theOperand1) || (theOperand1 >= 65536))
        throw new ClassFileFormatException("out of range variable");
      if ((0 > theOperand2) || (theOperand2 >= 65536)) {
        throw new ClassFileFormatException("out of range increment");
      }
      if ((theOperand1 > 255) || (theOperand2 < -128) || (theOperand2 > 127)) {
        addToCodeBuffer(196);
        addToCodeBuffer(132);
        addToCodeInt16(theOperand1);
        addToCodeInt16(theOperand2);
      } else {
        addToCodeBuffer(132);
        addToCodeBuffer(theOperand1);
        addToCodeBuffer(theOperand2);
      }
    } else if (theOpCode == 197) {
      if ((0 > theOperand1) || (theOperand1 >= 65536))
        throw new IllegalArgumentException("out of range index");
      if ((0 > theOperand2) || (theOperand2 >= 256)) {
        throw new IllegalArgumentException("out of range dimensions");
      }
      addToCodeBuffer(197);
      addToCodeInt16(theOperand1);
      addToCodeBuffer(theOperand2);
    } else {
      throw new IllegalArgumentException("Unexpected opcode for 2 operands");
    }
    
    itsStackTop = ((short)newStack);
    if (newStack > itsMaxStack) {
      itsMaxStack = ((short)newStack);
    }
  }
  







  public void add(int theOpCode, String className)
  {
    int newStack = itsStackTop + stackChange(theOpCode);
    if ((newStack < 0) || (32767 < newStack))
      badStack(newStack);
    switch (theOpCode) {
    case 187: 
    case 189: 
    case 192: 
    case 193: 
      short classIndex = itsConstantPool.addClass(className);
      addToCodeBuffer(theOpCode);
      addToCodeInt16(classIndex);
      
      break;
    case 188: case 190: 
    case 191: default: 
      throw new IllegalArgumentException("bad opcode for class reference");
    }
    
    itsStackTop = ((short)newStack);
    if (newStack > itsMaxStack) {
      itsMaxStack = ((short)newStack);
    }
  }
  







  public void add(int theOpCode, String className, String fieldName, String fieldType)
  {
    int newStack = itsStackTop + stackChange(theOpCode);
    char fieldTypeChar = fieldType.charAt(0);
    int fieldSize = (fieldTypeChar == 'J') || (fieldTypeChar == 'D') ? 2 : 1;
    switch (theOpCode) {
    case 178: 
    case 180: 
      newStack += fieldSize;
      break;
    case 179: 
    case 181: 
      newStack -= fieldSize;
      break;
    default: 
      throw new IllegalArgumentException("bad opcode for field reference");
    }
    
    if ((newStack < 0) || (32767 < newStack))
      badStack(newStack);
    short fieldRefIndex = itsConstantPool.addFieldRef(className, fieldName, fieldType);
    
    addToCodeBuffer(theOpCode);
    addToCodeInt16(fieldRefIndex);
    
    itsStackTop = ((short)newStack);
    if (newStack > itsMaxStack) {
      itsMaxStack = ((short)newStack);
    }
  }
  







  public void addInvoke(int theOpCode, String className, String methodName, String methodType)
  {
    int parameterInfo = sizeOfParameters(methodType);
    int parameterCount = parameterInfo >>> 16;
    int stackDiff = (short)parameterInfo;
    
    int newStack = itsStackTop + stackDiff;
    newStack += stackChange(theOpCode);
    if ((newStack < 0) || (32767 < newStack)) {
      badStack(newStack);
    }
    switch (theOpCode) {
    case 182: 
    case 183: 
    case 184: 
    case 185: 
      addToCodeBuffer(theOpCode);
      if (theOpCode == 185) {
        short ifMethodRefIndex = itsConstantPool.addInterfaceMethodRef(className, methodName, methodType);
        
        addToCodeInt16(ifMethodRefIndex);
        addToCodeBuffer(parameterCount + 1);
        addToCodeBuffer(0);
      } else {
        short methodRefIndex = itsConstantPool.addMethodRef(className, methodName, methodType);
        
        addToCodeInt16(methodRefIndex);
      }
      
      break;
    
    default: 
      throw new IllegalArgumentException("bad opcode for method reference");
    }
    
    itsStackTop = ((short)newStack);
    if (newStack > itsMaxStack) {
      itsMaxStack = ((short)newStack);
    }
  }
  








  public void addPush(int k)
  {
    if ((byte)k == k) {
      if (k == -1) {
        add(2);
      } else if ((0 <= k) && (k <= 5)) {
        add((byte)(3 + k));
      } else {
        add(16, (byte)k);
      }
    } else if ((short)k == k) {
      add(17, (short)k);
    } else {
      addLoadConstant(k);
    }
  }
  
  public void addPush(boolean k) {
    add(k ? 4 : 3);
  }
  





  public void addPush(long k)
  {
    int ik = (int)k;
    if (ik == k) {
      addPush(ik);
      add(133);
    } else {
      addLoadConstant(k);
    }
  }
  





  public void addPush(double k)
  {
    if (k == 0.0D)
    {
      add(14);
      if (1.0D / k < 0.0D)
      {
        add(119);
      }
    } else if ((k == 1.0D) || (k == -1.0D)) {
      add(15);
      if (k < 0.0D) {
        add(119);
      }
    } else {
      addLoadConstant(k);
    }
  }
  






  public void addPush(String k)
  {
    int length = k.length();
    int limit = itsConstantPool.getUtfEncodingLimit(k, 0, length);
    if (limit == length) {
      addLoadConstant(k);
      return;
    }
    





    String SB = "java/lang/StringBuilder";
    add(187, "java/lang/StringBuilder");
    add(89);
    addPush(length);
    addInvoke(183, "java/lang/StringBuilder", "<init>", "(I)V");
    int cursor = 0;
    for (;;) {
      add(89);
      String s = k.substring(cursor, limit);
      addLoadConstant(s);
      addInvoke(182, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;");
      
      add(87);
      if (limit == length) {
        break;
      }
      cursor = limit;
      limit = itsConstantPool.getUtfEncodingLimit(k, limit, length);
    }
    addInvoke(182, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;");
  }
  







  public boolean isUnderStringSizeLimit(String k)
  {
    return itsConstantPool.isUnderUtfEncodingLimit(k);
  }
  





  public void addIStore(int local)
  {
    xop(59, 54, local);
  }
  





  public void addLStore(int local)
  {
    xop(63, 55, local);
  }
  





  public void addFStore(int local)
  {
    xop(67, 56, local);
  }
  





  public void addDStore(int local)
  {
    xop(71, 57, local);
  }
  





  public void addAStore(int local)
  {
    xop(75, 58, local);
  }
  





  public void addILoad(int local)
  {
    xop(26, 21, local);
  }
  





  public void addLLoad(int local)
  {
    xop(30, 22, local);
  }
  





  public void addFLoad(int local)
  {
    xop(34, 23, local);
  }
  





  public void addDLoad(int local)
  {
    xop(38, 24, local);
  }
  





  public void addALoad(int local)
  {
    xop(42, 25, local);
  }
  


  public void addLoadThis()
  {
    add(42);
  }
  
  private void xop(int shortOp, int op, int local) {
    switch (local) {
    case 0: 
      add(shortOp);
      break;
    case 1: 
      add(shortOp + 1);
      break;
    case 2: 
      add(shortOp + 2);
      break;
    case 3: 
      add(shortOp + 3);
      break;
    default: 
      add(op, local);
    }
    
  }
  


  public int addTableSwitch(int low, int high)
  {
    if (low > high) {
      throw new ClassFileFormatException("Bad bounds: " + low + ' ' + high);
    }
    
    int newStack = itsStackTop + stackChange(170);
    if ((newStack < 0) || (32767 < newStack)) {
      badStack(newStack);
    }
    int entryCount = high - low + 1;
    int padSize = 0x3 & (itsCodeBufferTop ^ 0xFFFFFFFF);
    
    int N = addReservedCodeSpace(1 + padSize + 4 * (3 + entryCount));
    int switchStart = N;
    itsCodeBuffer[(N++)] = -86;
    while (padSize != 0) {
      itsCodeBuffer[(N++)] = 0;
      padSize--;
    }
    N += 4;
    N = putInt32(low, itsCodeBuffer, N);
    putInt32(high, itsCodeBuffer, N);
    
    itsStackTop = ((short)newStack);
    if (newStack > itsMaxStack) {
      itsMaxStack = ((short)newStack);
    }
    



    return switchStart;
  }
  
  public final void markTableSwitchDefault(int switchStart) {
    addSuperBlockStart(itsCodeBufferTop);
    itsJumpFroms.put(itsCodeBufferTop, switchStart);
    setTableSwitchJump(switchStart, -1, itsCodeBufferTop);
  }
  
  public final void markTableSwitchCase(int switchStart, int caseIndex) {
    addSuperBlockStart(itsCodeBufferTop);
    itsJumpFroms.put(itsCodeBufferTop, switchStart);
    setTableSwitchJump(switchStart, caseIndex, itsCodeBufferTop);
  }
  
  public final void markTableSwitchCase(int switchStart, int caseIndex, int stackTop)
  {
    if ((0 > stackTop) || (stackTop > itsMaxStack))
      throw new IllegalArgumentException("Bad stack index: " + stackTop);
    itsStackTop = ((short)stackTop);
    addSuperBlockStart(itsCodeBufferTop);
    itsJumpFroms.put(itsCodeBufferTop, switchStart);
    setTableSwitchJump(switchStart, caseIndex, itsCodeBufferTop);
  }
  




  public void setTableSwitchJump(int switchStart, int caseIndex, int jumpTarget)
  {
    if ((0 > jumpTarget) || (jumpTarget > itsCodeBufferTop)) {
      throw new IllegalArgumentException("Bad jump target: " + jumpTarget);
    }
    if (caseIndex < -1) {
      throw new IllegalArgumentException("Bad case index: " + caseIndex);
    }
    int padSize = 0x3 & (switchStart ^ 0xFFFFFFFF);
    int caseOffset;
    int caseOffset; if (caseIndex < 0)
    {
      caseOffset = switchStart + 1 + padSize;
    } else {
      caseOffset = switchStart + 1 + padSize + 4 * (3 + caseIndex);
    }
    if ((0 > switchStart) || (switchStart > itsCodeBufferTop - 16 - padSize - 1))
    {
      throw new IllegalArgumentException(switchStart + " is outside a possible range of tableswitch in already generated code");
    }
    

    if ((0xFF & itsCodeBuffer[switchStart]) != 170) {
      throw new IllegalArgumentException(switchStart + " is not offset of tableswitch statement");
    }
    
    if ((0 > caseOffset) || (caseOffset + 4 > itsCodeBufferTop))
    {

      throw new ClassFileFormatException("Too big case index: " + caseIndex);
    }
    

    putInt32(jumpTarget - switchStart, itsCodeBuffer, caseOffset);
  }
  
  public int acquireLabel() {
    int top = itsLabelTableTop;
    if ((itsLabelTable == null) || (top == itsLabelTable.length)) {
      if (itsLabelTable == null) {
        itsLabelTable = new int[32];
      } else {
        int[] tmp = new int[itsLabelTable.length * 2];
        System.arraycopy(itsLabelTable, 0, tmp, 0, top);
        itsLabelTable = tmp;
      }
    }
    itsLabelTableTop = (top + 1);
    itsLabelTable[top] = -1;
    return top | 0x80000000;
  }
  
  public void markLabel(int label) {
    if (label >= 0) {
      throw new IllegalArgumentException("Bad label, no biscuit");
    }
    label &= 0x7FFFFFFF;
    if (label > itsLabelTableTop) {
      throw new IllegalArgumentException("Bad label");
    }
    if (itsLabelTable[label] != -1) {
      throw new IllegalStateException("Can only mark label once");
    }
    
    itsLabelTable[label] = itsCodeBufferTop;
  }
  
  public void markLabel(int label, short stackTop) {
    markLabel(label);
    itsStackTop = stackTop;
  }
  
  public void markHandler(int theLabel) {
    itsStackTop = 1;
    markLabel(theLabel);
  }
  
  public int getLabelPC(int label) {
    if (label >= 0)
      throw new IllegalArgumentException("Bad label, no biscuit");
    label &= 0x7FFFFFFF;
    if (label >= itsLabelTableTop)
      throw new IllegalArgumentException("Bad label");
    return itsLabelTable[label];
  }
  
  private void addLabelFixup(int label, int fixupSite) {
    if (label >= 0)
      throw new IllegalArgumentException("Bad label, no biscuit");
    label &= 0x7FFFFFFF;
    if (label >= itsLabelTableTop)
      throw new IllegalArgumentException("Bad label");
    int top = itsFixupTableTop;
    if ((itsFixupTable == null) || (top == itsFixupTable.length)) {
      if (itsFixupTable == null) {
        itsFixupTable = new long[40];
      } else {
        long[] tmp = new long[itsFixupTable.length * 2];
        System.arraycopy(itsFixupTable, 0, tmp, 0, top);
        itsFixupTable = tmp;
      }
    }
    itsFixupTableTop = (top + 1);
    itsFixupTable[top] = (label << 32 | fixupSite);
  }
  
  private void fixLabelGotos() {
    byte[] codeBuffer = itsCodeBuffer;
    for (int i = 0; i < itsFixupTableTop; i++) {
      long fixup = itsFixupTable[i];
      int label = (int)(fixup >> 32);
      int fixupSite = (int)fixup;
      int pc = itsLabelTable[label];
      if (pc == -1)
      {
        throw new RuntimeException();
      }
      
      addSuperBlockStart(pc);
      itsJumpFroms.put(pc, fixupSite - 1);
      int offset = pc - (fixupSite - 1);
      if ((short)offset != offset) {
        throw new ClassFileFormatException("Program too complex: too big jump offset");
      }
      
      codeBuffer[fixupSite] = ((byte)(offset >> 8));
      codeBuffer[(fixupSite + 1)] = ((byte)offset);
    }
    itsFixupTableTop = 0;
  }
  




  public int getCurrentCodeOffset()
  {
    return itsCodeBufferTop;
  }
  
  public short getStackTop() {
    return itsStackTop;
  }
  
  public void setStackTop(short n) {
    itsStackTop = n;
  }
  
  public void adjustStackTop(int delta) {
    int newStack = itsStackTop + delta;
    if ((newStack < 0) || (32767 < newStack))
      badStack(newStack);
    itsStackTop = ((short)newStack);
    if (newStack > itsMaxStack) {
      itsMaxStack = ((short)newStack);
    }
  }
  


  private void addToCodeBuffer(int b)
  {
    int N = addReservedCodeSpace(1);
    itsCodeBuffer[N] = ((byte)b);
  }
  
  private void addToCodeInt16(int value) {
    int N = addReservedCodeSpace(2);
    putInt16(value, itsCodeBuffer, N);
  }
  
  private int addReservedCodeSpace(int size) {
    if (itsCurrentMethod == null)
      throw new IllegalArgumentException("No method to add to");
    int oldTop = itsCodeBufferTop;
    int newTop = oldTop + size;
    if (newTop > itsCodeBuffer.length) {
      int newSize = itsCodeBuffer.length * 2;
      if (newTop > newSize) {
        newSize = newTop;
      }
      byte[] tmp = new byte[newSize];
      System.arraycopy(itsCodeBuffer, 0, tmp, 0, oldTop);
      itsCodeBuffer = tmp;
    }
    itsCodeBufferTop = newTop;
    return oldTop;
  }
  
  public void addExceptionHandler(int startLabel, int endLabel, int handlerLabel, String catchClassName)
  {
    if ((startLabel & 0x80000000) != Integer.MIN_VALUE)
      throw new IllegalArgumentException("Bad startLabel");
    if ((endLabel & 0x80000000) != Integer.MIN_VALUE)
      throw new IllegalArgumentException("Bad endLabel");
    if ((handlerLabel & 0x80000000) != Integer.MIN_VALUE) {
      throw new IllegalArgumentException("Bad handlerLabel");
    }
    





    short catch_type_index = catchClassName == null ? 0 : itsConstantPool.addClass(catchClassName);
    ExceptionTableEntry newEntry = new ExceptionTableEntry(startLabel, endLabel, handlerLabel, catch_type_index);
    
    int N = itsExceptionTableTop;
    if (N == 0) {
      itsExceptionTable = new ExceptionTableEntry[4];
    } else if (N == itsExceptionTable.length) {
      ExceptionTableEntry[] tmp = new ExceptionTableEntry[N * 2];
      System.arraycopy(itsExceptionTable, 0, tmp, 0, N);
      itsExceptionTable = tmp;
    }
    itsExceptionTable[N] = newEntry;
    itsExceptionTableTop = (N + 1);
  }
  
  public void addLineNumberEntry(short lineNumber)
  {
    if (itsCurrentMethod == null)
      throw new IllegalArgumentException("No method to stop");
    int N = itsLineNumberTableTop;
    if (N == 0) {
      itsLineNumberTable = new int[16];
    } else if (N == itsLineNumberTable.length) {
      int[] tmp = new int[N * 2];
      System.arraycopy(itsLineNumberTable, 0, tmp, 0, N);
      itsLineNumberTable = tmp;
    }
    itsLineNumberTable[N] = ((itsCodeBufferTop << 16) + lineNumber);
    itsLineNumberTableTop = (N + 1);
  }
  
  final class StackMapTable {
    private int[] locals;
    private int localsTop;
    private int[] stack;
    private int stackTop;
    private SuperBlock[] workList;
    private int workListTop;
    
    StackMapTable() {
      superBlocks = null;
      locals = (this.stack = null);
      workList = null;
      rawStackMap = null;
      localsTop = 0;
      stackTop = 0;
      workListTop = 0;
      rawStackMapTop = 0;
      wide = false;
    }
    
    void generate() {
      superBlocks = new SuperBlock[itsSuperBlockStartsTop];
      int[] initialLocals = ClassFileWriter.this.createInitialLocals();
      
      for (int i = 0; i < itsSuperBlockStartsTop; i++) {
        int start = itsSuperBlockStarts[i];
        int end;
        int end; if (i == itsSuperBlockStartsTop - 1) {
          end = itsCodeBufferTop;
        } else {
          end = itsSuperBlockStarts[(i + 1)];
        }
        superBlocks[i] = new SuperBlock(i, start, end, initialLocals);
      }
      










      superBlockDeps = getSuperBlockDependencies();
      
      verify();
    }
    









    private SuperBlock getSuperBlockFromOffset(int offset)
    {
      for (int i = 0; i < superBlocks.length; i++) {
        SuperBlock sb = superBlocks[i];
        if (sb == null)
          break;
        if ((offset >= sb.getStart()) && (offset < sb.getEnd())) {
          return sb;
        }
      }
      throw new IllegalArgumentException("bad offset: " + offset);
    }
    



    private boolean isSuperBlockEnd(int opcode)
    {
      switch (opcode) {
      case 167: 
      case 170: 
      case 171: 
      case 172: 
      case 173: 
      case 174: 
      case 176: 
      case 177: 
      case 191: 
      case 200: 
        return true;
      }
      return false;
    }
    






    private SuperBlock[] getSuperBlockDependencies()
    {
      SuperBlock[] deps = new SuperBlock[superBlocks.length];
      
      for (int i = 0; i < itsExceptionTableTop; i++) {
        ExceptionTableEntry ete = itsExceptionTable[i];
        short startPC = (short)getLabelPC(itsStartLabel);
        short handlerPC = (short)getLabelPC(itsHandlerLabel);
        SuperBlock handlerSB = getSuperBlockFromOffset(handlerPC);
        SuperBlock dep = getSuperBlockFromOffset(startPC);
        deps[handlerSB.getIndex()] = dep;
      }
      int[] targetPCs = itsJumpFroms.getKeys();
      for (int i = 0; i < targetPCs.length; i++) {
        int targetPC = targetPCs[i];
        int branchPC = itsJumpFroms.getInt(targetPC, -1);
        SuperBlock branchSB = getSuperBlockFromOffset(branchPC);
        SuperBlock targetSB = getSuperBlockFromOffset(targetPC);
        deps[targetSB.getIndex()] = branchSB;
      }
      
      return deps;
    }
    


    private SuperBlock getBranchTarget(int bci)
    {
      int target;
      
      int target;
      
      if ((itsCodeBuffer[bci] & 0xFF) == 200) {
        target = bci + getOperand(bci + 1, 4);
      } else {
        target = bci + (short)getOperand(bci + 1, 2);
      }
      return getSuperBlockFromOffset(target);
    }
    



    private boolean isBranch(int opcode)
    {
      switch (opcode) {
      case 153: 
      case 154: 
      case 155: 
      case 156: 
      case 157: 
      case 158: 
      case 159: 
      case 160: 
      case 161: 
      case 162: 
      case 163: 
      case 164: 
      case 165: 
      case 166: 
      case 167: 
      case 198: 
      case 199: 
      case 200: 
        return true;
      }
      return false;
    }
    
    private int getOperand(int offset)
    {
      return getOperand(offset, 1);
    }
    




    private int getOperand(int start, int size)
    {
      int result = 0;
      if (size > 4) {
        throw new IllegalArgumentException("bad operand size");
      }
      for (int i = 0; i < size; i++) {
        result = result << 8 | itsCodeBuffer[(start + i)] & 0xFF;
      }
      return result;
    }
    



    private void verify()
    {
      int[] initialLocals = ClassFileWriter.this.createInitialLocals();
      superBlocks[0].merge(initialLocals, initialLocals.length, new int[0], 0, 
        itsConstantPool);
      


      workList = new SuperBlock[] { superBlocks[0] };
      workListTop = 1;
      executeWorkList();
      

      for (int i = 0; i < superBlocks.length; i++) {
        SuperBlock sb = superBlocks[i];
        if (!sb.isInitialized()) {
          killSuperBlock(sb);
        }
      }
      executeWorkList();
    }
    

















    private void killSuperBlock(SuperBlock sb)
    {
      int[] locals = new int[0];
      
      int[] stack = {TypeInfo.OBJECT("java/lang/Throwable", itsConstantPool) };
      




      for (int i = 0; i < itsExceptionTableTop; i++) {
        ExceptionTableEntry ete = itsExceptionTable[i];
        int eteStart = getLabelPC(itsStartLabel);
        int eteEnd = getLabelPC(itsEndLabel);
        int handlerPC = getLabelPC(itsHandlerLabel);
        SuperBlock handlerSB = getSuperBlockFromOffset(handlerPC);
        if (((sb.getStart() > eteStart) && (sb.getStart() < eteEnd)) || (
          (eteStart > sb.getStart()) && (eteStart < sb.getEnd()) && 
          (handlerSB.isInitialized()))) {
          locals = handlerSB.getLocals();
          break;
        }
      }
      



      for (int i = 0; i < itsExceptionTableTop; i++) {
        ExceptionTableEntry ete = itsExceptionTable[i];
        int eteStart = getLabelPC(itsStartLabel);
        if (eteStart == sb.getStart()) {
          for (int j = i + 1; j < itsExceptionTableTop; j++) {
            itsExceptionTable[(j - 1)] = itsExceptionTable[j];
          }
          ClassFileWriter.access$410(ClassFileWriter.this);
          i--;
        }
      }
      
      sb.merge(locals, locals.length, stack, stack.length, 
        itsConstantPool);
      
      int end = sb.getEnd() - 1;
      itsCodeBuffer[end] = -65;
      for (int bci = sb.getStart(); bci < end; bci++) {
        itsCodeBuffer[bci] = 0;
      }
    }
    
    private void executeWorkList() {
      while (workListTop > 0) {
        SuperBlock work = workList[(--workListTop)];
        work.setInQueue(false);
        locals = work.getLocals();
        stack = work.getStack();
        localsTop = locals.length;
        stackTop = stack.length;
        executeBlock(work);
      }
    }
    


    private void executeBlock(SuperBlock work)
    {
      int bc = 0;
      int next = 0;
      







      for (int bci = work.getStart(); bci < work.getEnd(); bci += next) {
        bc = itsCodeBuffer[bci] & 0xFF;
        next = execute(bci);
        





        if (isBranch(bc)) {
          SuperBlock targetSB = getBranchTarget(bci);
          








          flowInto(targetSB);





        }
        else if (bc == 170) {
          int switchStart = bci + 1 + (0x3 & (bci ^ 0xFFFFFFFF));
          int defaultOffset = getOperand(switchStart, 4);
          SuperBlock targetSB = getSuperBlockFromOffset(bci + defaultOffset);
          




          flowInto(targetSB);
          int low = getOperand(switchStart + 4, 4);
          int high = getOperand(switchStart + 8, 4);
          int numCases = high - low + 1;
          int caseBase = switchStart + 12;
          for (int i = 0; i < numCases; i++) {
            int label = bci + getOperand(caseBase + 4 * i, 4);
            targetSB = getSuperBlockFromOffset(label);
            



            flowInto(targetSB);
          }
        }
        
        for (int i = 0; i < itsExceptionTableTop; i++) {
          ExceptionTableEntry ete = itsExceptionTable[i];
          short startPC = (short)getLabelPC(itsStartLabel);
          short endPC = (short)getLabelPC(itsEndLabel);
          if ((bci >= startPC) && (bci < endPC))
          {

            short handlerPC = (short)getLabelPC(itsHandlerLabel);
            SuperBlock sb = getSuperBlockFromOffset(handlerPC);
            int exceptionType;
            int exceptionType;
            if (itsCatchType == 0) {
              exceptionType = TypeInfo.OBJECT(itsConstantPool
                .addClass("java/lang/Throwable"));
            } else {
              exceptionType = TypeInfo.OBJECT(itsCatchType);
            }
            sb.merge(locals, localsTop, new int[] { exceptionType }, 1, 
              itsConstantPool);
            addToWorkList(sb);
          }
        }
      }
      








      if (!isSuperBlockEnd(bc)) {
        int nextIndex = work.getIndex() + 1;
        if (nextIndex < superBlocks.length)
        {



          flowInto(superBlocks[nextIndex]);
        }
      }
    }
    



    private void flowInto(SuperBlock sb)
    {
      if (sb.merge(locals, localsTop, stack, stackTop, itsConstantPool)) {
        addToWorkList(sb);
      }
    }
    
    private void addToWorkList(SuperBlock sb) {
      if (!sb.isInQueue()) {
        sb.setInQueue(true);
        sb.setInitialized(true);
        if (workListTop == workList.length) {
          SuperBlock[] tmp = new SuperBlock[workListTop * 2];
          System.arraycopy(workList, 0, tmp, 0, workListTop);
          workList = tmp;
        }
        workList[(workListTop++)] = sb;
      }
    }
    






    private int execute(int bci)
    {
      int bc = itsCodeBuffer[bci] & 0xFF;
      
      int length = 0;
      


      switch (bc)
      {
      case 0: 
      case 132: 
      case 167: 
      case 200: 
        break;
      case 192: 
        pop();
        push(TypeInfo.OBJECT(getOperand(bci + 1, 2)));
        break;
      case 79: 
      case 80: 
      case 81: 
      case 82: 
      case 83: 
      case 84: 
      case 85: 
      case 86: 
        pop();
      case 159: 
      case 160: 
      case 161: 
      case 162: 
      case 163: 
      case 164: 
      case 165: 
      case 166: 
      case 181: 
        pop();
      case 87: 
      case 153: 
      case 154: 
      case 155: 
      case 156: 
      case 157: 
      case 158: 
      case 179: 
      case 194: 
      case 195: 
      case 198: 
      case 199: 
        pop();
        break;
      case 88: 
        pop2();
        break;
      case 1: 
        push(5);
        break;
      case 46: 
      case 51: 
      case 52: 
      case 53: 
      case 96: 
      case 100: 
      case 104: 
      case 108: 
      case 112: 
      case 120: 
      case 122: 
      case 124: 
      case 126: 
      case 128: 
      case 130: 
      case 148: 
      case 149: 
      case 150: 
      case 151: 
      case 152: 
        pop();
      case 116: 
      case 136: 
      case 139: 
      case 142: 
      case 145: 
      case 146: 
      case 147: 
      case 190: 
      case 193: 
        pop();
      case 2: 
      case 3: 
      case 4: 
      case 5: 
      case 6: 
      case 7: 
      case 8: 
      case 16: 
      case 17: 
      case 21: 
      case 26: 
      case 27: 
      case 28: 
      case 29: 
        push(1);
        break;
      case 47: 
      case 97: 
      case 101: 
      case 105: 
      case 109: 
      case 113: 
      case 121: 
      case 123: 
      case 125: 
      case 127: 
      case 129: 
      case 131: 
        pop();
      case 117: 
      case 133: 
      case 140: 
      case 143: 
        pop();
      case 9: 
      case 10: 
      case 22: 
      case 30: 
      case 31: 
      case 32: 
      case 33: 
        push(4);
        break;
      case 48: 
      case 98: 
      case 102: 
      case 106: 
      case 110: 
      case 114: 
        pop();
      case 118: 
      case 134: 
      case 137: 
      case 144: 
        pop();
      case 11: 
      case 12: 
      case 13: 
      case 23: 
      case 34: 
      case 35: 
      case 36: 
      case 37: 
        push(2);
        break;
      case 49: 
      case 99: 
      case 103: 
      case 107: 
      case 111: 
      case 115: 
        pop();
      case 119: 
      case 135: 
      case 138: 
      case 141: 
        pop();
      case 14: 
      case 15: 
      case 24: 
      case 38: 
      case 39: 
      case 40: 
      case 41: 
        push(3);
        break;
      case 54: 
        executeStore(getOperand(bci + 1, wide ? 2 : 1), 1);
        
        break;
      case 59: 
      case 60: 
      case 61: 
      case 62: 
        executeStore(bc - 59, 1);
        break;
      case 55: 
        executeStore(getOperand(bci + 1, wide ? 2 : 1), 4);
        break;
      case 63: 
      case 64: 
      case 65: 
      case 66: 
        executeStore(bc - 63, 4);
        break;
      case 56: 
        executeStore(getOperand(bci + 1, wide ? 2 : 1), 2);
        break;
      case 67: 
      case 68: 
      case 69: 
      case 70: 
        executeStore(bc - 67, 2);
        break;
      case 57: 
        executeStore(getOperand(bci + 1, wide ? 2 : 1), 3);
        
        break;
      case 71: 
      case 72: 
      case 73: 
      case 74: 
        executeStore(bc - 71, 3);
        break;
      case 25: 
        executeALoad(getOperand(bci + 1, wide ? 2 : 1));
        break;
      case 42: 
      case 43: 
      case 44: 
      case 45: 
        executeALoad(bc - 42);
        break;
      case 58: 
        executeAStore(getOperand(bci + 1, wide ? 2 : 1));
        break;
      case 75: 
      case 76: 
      case 77: 
      case 78: 
        executeAStore(bc - 75);
        break;
      case 172: 
      case 173: 
      case 174: 
      case 175: 
      case 176: 
      case 177: 
        clearStack();
        break;
      case 191: 
        int type = pop();
        clearStack();
        push(type);
        break;
      case 95: 
        int type = pop();
        int type2 = pop();
        push(type);
        push(type2);
        break;
      case 18: case 19: case 20: 
        int index;
        int index;
        if (bc == 18) {
          index = getOperand(bci + 1);
        } else {
          index = getOperand(bci + 1, 2);
        }
        byte constType = itsConstantPool.getConstantType(index);
        switch (constType) {
        case 6: 
          push(3);
          break;
        case 4: 
          push(2);
          break;
        case 5: 
          push(4);
          break;
        case 3: 
          push(1);
          break;
        case 8: 
          push(TypeInfo.OBJECT("java/lang/String", itsConstantPool));
          break;
        case 7: default: 
          throw new IllegalArgumentException("bad const type " + constType);
        }
        
        break;
      case 187: 
        push(TypeInfo.UNINITIALIZED_VARIABLE(bci));
        break;
      case 188: 
        pop();
        char componentType = ClassFileWriter.arrayTypeToName(itsCodeBuffer[(bci + 1)]);
        int index = itsConstantPool.addClass("[" + componentType);
        push(TypeInfo.OBJECT((short)index));
        break;
      case 189: 
        int index = getOperand(bci + 1, 2);
        String className = (String)itsConstantPool.getConstantData(index);
        pop();
        push(TypeInfo.OBJECT("[L" + className + ';', itsConstantPool));
        break;
      case 182: 
      case 183: 
      case 184: 
      case 185: 
        int index = getOperand(bci + 1, 2);
        
        FieldOrMethodRef m = (FieldOrMethodRef)itsConstantPool.getConstantData(index);
        String methodType = m.getType();
        String methodName = m.getName();
        int parameterCount = ClassFileWriter.sizeOfParameters(methodType) >>> 16;
        for (int i = 0; i < parameterCount; i++) {
          pop();
        }
        if (bc != 184) {
          int instType = pop();
          int tag = TypeInfo.getTag(instType);
          if ((tag == TypeInfo.UNINITIALIZED_VARIABLE(0)) || (tag == 6))
          {
            if ("<init>".equals(methodName)) {
              int newType = TypeInfo.OBJECT(itsThisClassIndex);
              initializeTypeInfo(instType, newType);
            } else {
              throw new IllegalStateException("bad instance");
            }
          }
        }
        int rParen = methodType.indexOf(')');
        String returnType = methodType.substring(rParen + 1);
        returnType = ClassFileWriter.descriptorToInternalName(returnType);
        if (!returnType.equals("V")) {
          push(TypeInfo.fromType(returnType, itsConstantPool));
        }
        break;
      case 180: 
        pop();
      case 178: 
        int index = getOperand(bci + 1, 2);
        
        FieldOrMethodRef f = (FieldOrMethodRef)itsConstantPool.getConstantData(index);
        String fieldType = ClassFileWriter.descriptorToInternalName(f.getType());
        push(TypeInfo.fromType(fieldType, itsConstantPool));
        break;
      case 89: 
        int type = pop();
        push(type);
        push(type);
        break;
      case 90: 
        int type = pop();
        int type2 = pop();
        push(type);
        push(type2);
        push(type);
        break;
      case 91: 
        int type = pop();
        long lType = pop2();
        push(type);
        push2(lType);
        push(type);
        break;
      case 92: 
        long lType = pop2();
        push2(lType);
        push2(lType);
        break;
      case 93: 
        long lType = pop2();
        int type = pop();
        push2(lType);
        push(type);
        push2(lType);
        break;
      case 94: 
        long lType = pop2();
        long lType2 = pop2();
        push2(lType);
        push2(lType2);
        push2(lType);
        break;
      case 170: 
        int switchStart = bci + 1 + (0x3 & (bci ^ 0xFFFFFFFF));
        int low = getOperand(switchStart + 4, 4);
        int high = getOperand(switchStart + 8, 4);
        length = 4 * (high - low + 4) + switchStart - bci;
        pop();
        break;
      case 50: 
        pop();
        int typeIndex = pop() >>> 8;
        String className = (String)itsConstantPool.getConstantData(typeIndex);
        String arrayType = className;
        if (arrayType.charAt(0) != '[') {
          throw new IllegalStateException("bad array type");
        }
        String elementDesc = arrayType.substring(1);
        String elementType = ClassFileWriter.descriptorToInternalName(elementDesc);
        typeIndex = itsConstantPool.addClass(elementType);
        push(TypeInfo.OBJECT(typeIndex));
        break;
      
      case 196: 
        wide = true;
        break;
      case 168: 
      case 169: 
      case 171: 
      case 186: 
      case 197: 
      case 201: 
      default: 
        throw new IllegalArgumentException("bad opcode: " + bc);
      }
      
      if (length == 0) {
        length = ClassFileWriter.opcodeLength(bc, wide);
      }
      if ((wide) && (bc != 196)) {
        wide = false;
      }
      return length;
    }
    
    private void executeALoad(int localIndex) {
      int type = getLocal(localIndex);
      int tag = TypeInfo.getTag(type);
      if ((tag == 7) || (tag == 6) || (tag == 8) || (tag == 5))
      {

        push(type);
      } else {
        throw new IllegalStateException("bad local variable type: " + type + " at index: " + localIndex);
      }
    }
    
    private void executeAStore(int localIndex)
    {
      setLocal(localIndex, pop());
    }
    
    private void executeStore(int localIndex, int typeInfo) {
      pop();
      setLocal(localIndex, typeInfo);
    }
    




    private void initializeTypeInfo(int prevType, int newType)
    {
      initializeTypeInfo(prevType, newType, locals, localsTop);
      initializeTypeInfo(prevType, newType, stack, stackTop);
    }
    
    private void initializeTypeInfo(int prevType, int newType, int[] data, int dataTop)
    {
      for (int i = 0; i < dataTop; i++) {
        if (data[i] == prevType) {
          data[i] = newType;
        }
      }
    }
    
    private int getLocal(int localIndex) {
      if (localIndex < localsTop) {
        return locals[localIndex];
      }
      return 0;
    }
    
    private void setLocal(int localIndex, int typeInfo)
    {
      if (localIndex >= localsTop) {
        int[] tmp = new int[localIndex + 1];
        System.arraycopy(locals, 0, tmp, 0, localsTop);
        locals = tmp;
        localsTop = (localIndex + 1);
      }
      locals[localIndex] = typeInfo;
    }
    
    private void push(int typeInfo) {
      if (stackTop == stack.length) {
        int[] tmp = new int[Math.max(stackTop * 2, 4)];
        System.arraycopy(stack, 0, tmp, 0, stackTop);
        stack = tmp;
      }
      stack[(stackTop++)] = typeInfo;
    }
    
    private int pop() {
      return stack[(--stackTop)];
    }
    





    private void push2(long typeInfo)
    {
      push((int)(typeInfo & 0xFFFFFF));
      typeInfo >>>= 32;
      if (typeInfo != 0L) {
        push((int)(typeInfo & 0xFFFFFF));
      }
    }
    







    private long pop2()
    {
      long type = pop();
      if (TypeInfo.isTwoWords((int)type)) {
        return type;
      }
      return type << 32 | pop() & 0xFFFFFF;
    }
    
    private void clearStack()
    {
      stackTop = 0;
    }
    









    int computeWriteSize()
    {
      int writeSize = getWorstCaseWriteSize();
      rawStackMap = new byte[writeSize];
      computeRawStackMap();
      return rawStackMapTop + 2;
    }
    
    int write(byte[] data, int offset) {
      offset = ClassFileWriter.putInt32(rawStackMapTop + 2, data, offset);
      offset = ClassFileWriter.putInt16(superBlocks.length - 1, data, offset);
      System.arraycopy(rawStackMap, 0, data, offset, rawStackMapTop);
      return offset + rawStackMapTop;
    }
    


    private void computeRawStackMap()
    {
      SuperBlock prev = superBlocks[0];
      int[] prevLocals = prev.getTrimmedLocals();
      int prevOffset = -1;
      for (int i = 1; i < superBlocks.length; i++) {
        SuperBlock current = superBlocks[i];
        int[] currentLocals = current.getTrimmedLocals();
        int[] currentStack = current.getStack();
        int offsetDelta = current.getStart() - prevOffset - 1;
        
        if (currentStack.length == 0) {
          int last = prevLocals.length > currentLocals.length ? currentLocals.length : prevLocals.length;
          

          int delta = Math.abs(prevLocals.length - currentLocals.length);
          


          for (int j = 0; j < last; j++) {
            if (prevLocals[j] != currentLocals[j]) {
              break;
            }
          }
          if ((j == currentLocals.length) && (delta == 0))
          {

            writeSameFrame(currentLocals, offsetDelta);
          } else if ((j == currentLocals.length) && (delta <= 3))
          {

            writeChopFrame(delta, offsetDelta);
          } else if ((j == prevLocals.length) && (delta <= 3))
          {

            writeAppendFrame(currentLocals, delta, offsetDelta);
          }
          else
          {
            writeFullFrame(currentLocals, currentStack, offsetDelta);
          }
        }
        else if (currentStack.length == 1) {
          if (Arrays.equals(prevLocals, currentLocals)) {
            writeSameLocalsOneStackItemFrame(currentLocals, currentStack, offsetDelta);

          }
          else
          {
            writeFullFrame(currentLocals, currentStack, offsetDelta);
          }
          

        }
        else
        {
          writeFullFrame(currentLocals, currentStack, offsetDelta);
        }
        
        prev = current;
        prevLocals = currentLocals;
        prevOffset = current.getStart();
      }
    }
    






    private int getWorstCaseWriteSize()
    {
      return 
        (superBlocks.length - 1) * (7 + itsMaxLocals * 3 + itsMaxStack * 3);
    }
    
    private void writeSameFrame(int[] locals, int offsetDelta) {
      if (offsetDelta <= 63)
      {


        rawStackMap[(rawStackMapTop++)] = ((byte)offsetDelta);
      }
      else
      {
        rawStackMap[(rawStackMapTop++)] = -5;
        rawStackMapTop = ClassFileWriter.putInt16(offsetDelta, rawStackMap, rawStackMapTop);
      }
    }
    

    private void writeSameLocalsOneStackItemFrame(int[] locals, int[] stack, int offsetDelta)
    {
      if (offsetDelta <= 63)
      {


        rawStackMap[(rawStackMapTop++)] = ((byte)(64 + offsetDelta));

      }
      else
      {
        rawStackMap[(rawStackMapTop++)] = -9;
        rawStackMapTop = ClassFileWriter.putInt16(offsetDelta, rawStackMap, rawStackMapTop);
      }
      
      writeType(stack[0]);
    }
    
    private void writeFullFrame(int[] locals, int[] stack, int offsetDelta)
    {
      rawStackMap[(rawStackMapTop++)] = -1;
      rawStackMapTop = ClassFileWriter.putInt16(offsetDelta, rawStackMap, rawStackMapTop);
      rawStackMapTop = ClassFileWriter.putInt16(locals.length, rawStackMap, rawStackMapTop);
      
      rawStackMapTop = writeTypes(locals);
      rawStackMapTop = ClassFileWriter.putInt16(stack.length, rawStackMap, rawStackMapTop);
      
      rawStackMapTop = writeTypes(stack);
    }
    
    private void writeAppendFrame(int[] locals, int localsDelta, int offsetDelta)
    {
      int start = locals.length - localsDelta;
      rawStackMap[(rawStackMapTop++)] = ((byte)(251 + localsDelta));
      rawStackMapTop = ClassFileWriter.putInt16(offsetDelta, rawStackMap, rawStackMapTop);
      rawStackMapTop = writeTypes(locals, start);
    }
    
    private void writeChopFrame(int localsDelta, int offsetDelta) {
      rawStackMap[(rawStackMapTop++)] = ((byte)(251 - localsDelta));
      rawStackMapTop = ClassFileWriter.putInt16(offsetDelta, rawStackMap, rawStackMapTop);
    }
    
    private int writeTypes(int[] types) {
      return writeTypes(types, 0);
    }
    
    private int writeTypes(int[] types, int start) {
      int startOffset = rawStackMapTop;
      for (int i = start; i < types.length; i++) {
        rawStackMapTop = writeType(types[i]);
      }
      return rawStackMapTop;
    }
    
    private int writeType(int type) {
      int tag = type & 0xFF;
      rawStackMap[(rawStackMapTop++)] = ((byte)tag);
      if ((tag == 7) || (tag == 8))
      {
        rawStackMapTop = ClassFileWriter.putInt16(type >>> 8, rawStackMap, rawStackMapTop);
      }
      
      return rawStackMapTop;
    }
    



    private SuperBlock[] superBlocks;
    


    private SuperBlock[] superBlockDeps;
    


    private byte[] rawStackMap;
    

    private int rawStackMapTop;
    

    private boolean wide;
    

    static final boolean DEBUGSTACKMAP = false;
  }
  


  private static char arrayTypeToName(int type)
  {
    switch (type) {
    case 4: 
      return 'Z';
    case 5: 
      return 'C';
    case 6: 
      return 'F';
    case 7: 
      return 'D';
    case 8: 
      return 'B';
    case 9: 
      return 'S';
    case 10: 
      return 'I';
    case 11: 
      return 'J';
    }
    throw new IllegalArgumentException("bad operand");
  }
  





  private static String classDescriptorToInternalName(String descriptor)
  {
    return descriptor.substring(1, descriptor.length() - 1);
  }
  





  private static String descriptorToInternalName(String descriptor)
  {
    switch (descriptor.charAt(0)) {
    case 'B': 
    case 'C': 
    case 'D': 
    case 'F': 
    case 'I': 
    case 'J': 
    case 'S': 
    case 'V': 
    case 'Z': 
    case '[': 
      return descriptor;
    case 'L': 
      return classDescriptorToInternalName(descriptor);
    }
    throw new IllegalArgumentException("bad descriptor:" + descriptor);
  }
  






  private int[] createInitialLocals()
  {
    int[] initialLocals = new int[itsMaxLocals];
    int localsTop = 0;
    



    if ((itsCurrentMethod.getFlags() & 0x8) == 0) {
      if ("<init>".equals(itsCurrentMethod.getName())) {
        initialLocals[(localsTop++)] = 6;
      } else {
        initialLocals[(localsTop++)] = TypeInfo.OBJECT(itsThisClassIndex);
      }
    }
    

    String type = itsCurrentMethod.getType();
    int lParenIndex = type.indexOf('(');
    int rParenIndex = type.indexOf(')');
    if ((lParenIndex != 0) || (rParenIndex < 0)) {
      throw new IllegalArgumentException("bad method type");
    }
    int start = lParenIndex + 1;
    StringBuilder paramType = new StringBuilder();
    while (start < rParenIndex)
      switch (type.charAt(start)) {
      case 'B': 
      case 'C': 
      case 'D': 
      case 'F': 
      case 'I': 
      case 'J': 
      case 'S': 
      case 'Z': 
        paramType.append(type.charAt(start));
        start++;
        break;
      case 'L': 
        int end = type.indexOf(';', start) + 1;
        String name = type.substring(start, end);
        paramType.append(name);
        start = end;
        break;
      case '[': 
        paramType.append('[');
        start++;
        break;
      case 'E': case 'G': case 'H': case 'K': case 'M': case 'N': case 'O': case 'P': case 'Q': case 'R': case 'T': case 'U': case 'V': case 'W': case 'X': case 'Y': default: 
        String internalType = descriptorToInternalName(paramType
          .toString());
        int typeInfo = TypeInfo.fromType(internalType, itsConstantPool);
        initialLocals[(localsTop++)] = typeInfo;
        if (TypeInfo.isTwoWords(typeInfo)) {
          localsTop++;
        }
        paramType.setLength(0);
      }
    return initialLocals;
  }
  






  public void write(OutputStream oStream)
    throws IOException
  {
    byte[] array = toByteArray();
    oStream.write(array);
  }
  
  private int getWriteSize() {
    int size = 0;
    
    if (itsSourceFileNameIndex != 0) {
      itsConstantPool.addUtf8("SourceFile");
    }
    
    size += 8;
    size += itsConstantPool.getWriteSize();
    size += 2;
    size += 2;
    size += 2;
    size += 2;
    size += 2 * itsInterfaces.size();
    
    size += 2;
    for (int i = 0; i < itsFields.size(); i++) {
      size += ((ClassFileField)itsFields.get(i)).getWriteSize();
    }
    
    size += 2;
    for (int i = 0; i < itsMethods.size(); i++) {
      size += ((ClassFileMethod)itsMethods.get(i)).getWriteSize();
    }
    
    if (itsSourceFileNameIndex != 0) {
      size += 2;
      size += 2;
      size += 4;
      size += 2;
    } else {
      size += 2;
    }
    
    return size;
  }
  


  public byte[] toByteArray()
  {
    int dataSize = getWriteSize();
    byte[] data = new byte[dataSize];
    int offset = 0;
    
    short sourceFileAttributeNameIndex = 0;
    if (itsSourceFileNameIndex != 0)
    {
      sourceFileAttributeNameIndex = itsConstantPool.addUtf8("SourceFile");
    }
    
    offset = putInt32(-889275714, data, offset);
    offset = putInt16(MinorVersion, data, offset);
    offset = putInt16(MajorVersion, data, offset);
    offset = itsConstantPool.write(data, offset);
    offset = putInt16(itsFlags, data, offset);
    offset = putInt16(itsThisClassIndex, data, offset);
    offset = putInt16(itsSuperClassIndex, data, offset);
    offset = putInt16(itsInterfaces.size(), data, offset);
    for (int i = 0; i < itsInterfaces.size(); i++) {
      int interfaceIndex = ((Short)itsInterfaces.get(i)).shortValue();
      offset = putInt16(interfaceIndex, data, offset);
    }
    offset = putInt16(itsFields.size(), data, offset);
    for (int i = 0; i < itsFields.size(); i++) {
      ClassFileField field = (ClassFileField)itsFields.get(i);
      offset = field.write(data, offset);
    }
    offset = putInt16(itsMethods.size(), data, offset);
    for (int i = 0; i < itsMethods.size(); i++) {
      ClassFileMethod method = (ClassFileMethod)itsMethods.get(i);
      offset = method.write(data, offset);
    }
    if (itsSourceFileNameIndex != 0) {
      offset = putInt16(1, data, offset);
      offset = putInt16(sourceFileAttributeNameIndex, data, offset);
      offset = putInt32(2, data, offset);
      offset = putInt16(itsSourceFileNameIndex, data, offset);
    } else {
      offset = putInt16(0, data, offset);
    }
    
    if (offset != dataSize)
    {
      throw new RuntimeException();
    }
    
    return data;
  }
  
  static int putInt64(long value, byte[] array, int offset) {
    offset = putInt32((int)(value >>> 32), array, offset);
    return putInt32((int)value, array, offset);
  }
  
  private static void badStack(int value) { String s;
    String s;
    if (value < 0) {
      s = "Stack underflow: " + value;
    } else {
      s = "Too big stack: " + value;
    }
    throw new IllegalStateException(s);
  }
  






  private static int sizeOfParameters(String pString)
  {
    int length = pString.length();
    int rightParenthesis = pString.lastIndexOf(')');
    if ((3 <= length) && 
      (pString.charAt(0) == '(') && (1 <= rightParenthesis) && (rightParenthesis + 1 < length))
    {
      boolean ok = true;
      int index = 1;
      int stackDiff = 0;
      int count = 0;
      while (index != rightParenthesis) {
        switch (pString.charAt(index)) {
        case 'E': case 'G': case 'H': case 'K': case 'M': case 'N': case 'O': case 'P': case 'Q': case 'R': case 'T': case 'U': case 'V': case 'W': case 'X': case 'Y': default: 
          ok = false;
          break;
        case 'D': 
        case 'J': 
          stackDiff--;
        
        case 'B': 
        case 'C': 
        case 'F': 
        case 'I': 
        case 'S': 
        case 'Z': 
          stackDiff--;
          count++;
          index++;
          break;
        case '[': 
          index++;
          int c = pString.charAt(index);
          while (c == 91) {
            index++;
            c = pString.charAt(index);
          }
          switch (c) {
          case 69: case 71: case 72: case 75: case 77: case 78: case 79: case 80: case 81: case 82: case 84: case 85: case 86: case 87: case 88: case 89: default: 
            ok = false;
            break;
          case 66: 
          case 67: 
          case 68: 
          case 70: 
          case 73: 
          case 74: 
          case 83: 
          case 90: 
            stackDiff--;
            count++;
            index++; }
          break;
        



        case 'L': 
          stackDiff--;
          count++;
          index++;
          int semicolon = pString.indexOf(';', index);
          if ((index + 1 > semicolon) || (semicolon >= rightParenthesis))
          {
            ok = false;
            break label413;
          }
          index = semicolon + 1;
        }
        
      }
      label413:
      if (ok) {
        switch (pString.charAt(rightParenthesis + 1)) {
        case 'E': case 'G': case 'H': case 'K': case 'M': case 'N': case 'O': case 'P': case 'Q': case 'R': case 'T': case 'U': case 'W': case 'X': case 'Y': default: 
          ok = false;
          break;
        case 'D': 
        case 'J': 
          stackDiff++;
        
        case 'B': 
        case 'C': 
        case 'F': 
        case 'I': 
        case 'L': 
        case 'S': 
        case 'Z': 
        case '[': 
          stackDiff++;
        }
        
        

        if (ok) {
          return count << 16 | 0xFFFF & stackDiff;
        }
      }
    }
    throw new IllegalArgumentException("Bad parameter signature: " + pString);
  }
  
  static int putInt16(int value, byte[] array, int offset)
  {
    array[(offset + 0)] = ((byte)(value >>> 8));
    array[(offset + 1)] = ((byte)value);
    return offset + 2;
  }
  
  static int putInt32(int value, byte[] array, int offset) {
    array[(offset + 0)] = ((byte)(value >>> 24));
    array[(offset + 1)] = ((byte)(value >>> 16));
    array[(offset + 2)] = ((byte)(value >>> 8));
    array[(offset + 3)] = ((byte)value);
    return offset + 4;
  }
  





  static int opcodeLength(int opcode, boolean wide)
  {
    switch (opcode) {
    case 0: 
    case 1: 
    case 2: 
    case 3: 
    case 4: 
    case 5: 
    case 6: 
    case 7: 
    case 8: 
    case 9: 
    case 10: 
    case 11: 
    case 12: 
    case 13: 
    case 14: 
    case 15: 
    case 26: 
    case 27: 
    case 28: 
    case 29: 
    case 30: 
    case 31: 
    case 32: 
    case 33: 
    case 34: 
    case 35: 
    case 36: 
    case 37: 
    case 38: 
    case 39: 
    case 40: 
    case 41: 
    case 42: 
    case 43: 
    case 44: 
    case 45: 
    case 46: 
    case 47: 
    case 48: 
    case 49: 
    case 50: 
    case 51: 
    case 52: 
    case 53: 
    case 59: 
    case 60: 
    case 61: 
    case 62: 
    case 63: 
    case 64: 
    case 65: 
    case 66: 
    case 67: 
    case 68: 
    case 69: 
    case 70: 
    case 71: 
    case 72: 
    case 73: 
    case 74: 
    case 75: 
    case 76: 
    case 77: 
    case 78: 
    case 79: 
    case 80: 
    case 81: 
    case 82: 
    case 83: 
    case 84: 
    case 85: 
    case 86: 
    case 87: 
    case 88: 
    case 89: 
    case 90: 
    case 91: 
    case 92: 
    case 93: 
    case 94: 
    case 95: 
    case 96: 
    case 97: 
    case 98: 
    case 99: 
    case 100: 
    case 101: 
    case 102: 
    case 103: 
    case 104: 
    case 105: 
    case 106: 
    case 107: 
    case 108: 
    case 109: 
    case 110: 
    case 111: 
    case 112: 
    case 113: 
    case 114: 
    case 115: 
    case 116: 
    case 117: 
    case 118: 
    case 119: 
    case 120: 
    case 121: 
    case 122: 
    case 123: 
    case 124: 
    case 125: 
    case 126: 
    case 127: 
    case 128: 
    case 129: 
    case 130: 
    case 131: 
    case 133: 
    case 134: 
    case 135: 
    case 136: 
    case 137: 
    case 138: 
    case 139: 
    case 140: 
    case 141: 
    case 142: 
    case 143: 
    case 144: 
    case 145: 
    case 146: 
    case 147: 
    case 148: 
    case 149: 
    case 150: 
    case 151: 
    case 152: 
    case 172: 
    case 173: 
    case 174: 
    case 175: 
    case 176: 
    case 177: 
    case 190: 
    case 191: 
    case 194: 
    case 195: 
    case 196: 
    case 202: 
    case 254: 
    case 255: 
      return 1;
    case 16: 
    case 18: 
    case 188: 
      return 2;
    case 21: 
    case 22: 
    case 23: 
    case 24: 
    case 25: 
    case 54: 
    case 55: 
    case 56: 
    case 57: 
    case 58: 
    case 169: 
      return wide ? 3 : 2;
    
    case 17: 
    case 19: 
    case 20: 
    case 153: 
    case 154: 
    case 155: 
    case 156: 
    case 157: 
    case 158: 
    case 159: 
    case 160: 
    case 161: 
    case 162: 
    case 163: 
    case 164: 
    case 165: 
    case 166: 
    case 167: 
    case 168: 
    case 178: 
    case 179: 
    case 180: 
    case 181: 
    case 182: 
    case 183: 
    case 184: 
    case 187: 
    case 189: 
    case 192: 
    case 193: 
    case 198: 
    case 199: 
      return 3;
    
    case 132: 
      return wide ? 5 : 3;
    
    case 197: 
      return 4;
    
    case 185: 
    case 200: 
    case 201: 
      return 5;
    }
    
    


    throw new IllegalArgumentException("Bad opcode: " + opcode);
  }
  


  static int opcodeCount(int opcode)
  {
    switch (opcode) {
    case 0: 
    case 1: 
    case 2: 
    case 3: 
    case 4: 
    case 5: 
    case 6: 
    case 7: 
    case 8: 
    case 9: 
    case 10: 
    case 11: 
    case 12: 
    case 13: 
    case 14: 
    case 15: 
    case 26: 
    case 27: 
    case 28: 
    case 29: 
    case 30: 
    case 31: 
    case 32: 
    case 33: 
    case 34: 
    case 35: 
    case 36: 
    case 37: 
    case 38: 
    case 39: 
    case 40: 
    case 41: 
    case 42: 
    case 43: 
    case 44: 
    case 45: 
    case 46: 
    case 47: 
    case 48: 
    case 49: 
    case 50: 
    case 51: 
    case 52: 
    case 53: 
    case 59: 
    case 60: 
    case 61: 
    case 62: 
    case 63: 
    case 64: 
    case 65: 
    case 66: 
    case 67: 
    case 68: 
    case 69: 
    case 70: 
    case 71: 
    case 72: 
    case 73: 
    case 74: 
    case 75: 
    case 76: 
    case 77: 
    case 78: 
    case 79: 
    case 80: 
    case 81: 
    case 82: 
    case 83: 
    case 84: 
    case 85: 
    case 86: 
    case 87: 
    case 88: 
    case 89: 
    case 90: 
    case 91: 
    case 92: 
    case 93: 
    case 94: 
    case 95: 
    case 96: 
    case 97: 
    case 98: 
    case 99: 
    case 100: 
    case 101: 
    case 102: 
    case 103: 
    case 104: 
    case 105: 
    case 106: 
    case 107: 
    case 108: 
    case 109: 
    case 110: 
    case 111: 
    case 112: 
    case 113: 
    case 114: 
    case 115: 
    case 116: 
    case 117: 
    case 118: 
    case 119: 
    case 120: 
    case 121: 
    case 122: 
    case 123: 
    case 124: 
    case 125: 
    case 126: 
    case 127: 
    case 128: 
    case 129: 
    case 130: 
    case 131: 
    case 133: 
    case 134: 
    case 135: 
    case 136: 
    case 137: 
    case 138: 
    case 139: 
    case 140: 
    case 141: 
    case 142: 
    case 143: 
    case 144: 
    case 145: 
    case 146: 
    case 147: 
    case 148: 
    case 149: 
    case 150: 
    case 151: 
    case 152: 
    case 172: 
    case 173: 
    case 174: 
    case 175: 
    case 176: 
    case 177: 
    case 190: 
    case 191: 
    case 194: 
    case 195: 
    case 196: 
    case 202: 
    case 254: 
    case 255: 
      return 0;
    case 16: 
    case 17: 
    case 18: 
    case 19: 
    case 20: 
    case 21: 
    case 22: 
    case 23: 
    case 24: 
    case 25: 
    case 54: 
    case 55: 
    case 56: 
    case 57: 
    case 58: 
    case 153: 
    case 154: 
    case 155: 
    case 156: 
    case 157: 
    case 158: 
    case 159: 
    case 160: 
    case 161: 
    case 162: 
    case 163: 
    case 164: 
    case 165: 
    case 166: 
    case 167: 
    case 168: 
    case 169: 
    case 178: 
    case 179: 
    case 180: 
    case 181: 
    case 182: 
    case 183: 
    case 184: 
    case 185: 
    case 187: 
    case 188: 
    case 189: 
    case 192: 
    case 193: 
    case 198: 
    case 199: 
    case 200: 
    case 201: 
      return 1;
    
    case 132: 
    case 197: 
      return 2;
    
    case 170: 
    case 171: 
      return -1;
    }
    throw new IllegalArgumentException("Bad opcode: " + opcode);
  }
  




  static int stackChange(int opcode)
  {
    switch (opcode) {
    case 80: 
    case 82: 
      return -4;
    
    case 79: 
    case 81: 
    case 83: 
    case 84: 
    case 85: 
    case 86: 
    case 148: 
    case 151: 
    case 152: 
      return -3;
    
    case 55: 
    case 57: 
    case 63: 
    case 64: 
    case 65: 
    case 66: 
    case 71: 
    case 72: 
    case 73: 
    case 74: 
    case 88: 
    case 97: 
    case 99: 
    case 101: 
    case 103: 
    case 105: 
    case 107: 
    case 109: 
    case 111: 
    case 113: 
    case 115: 
    case 127: 
    case 129: 
    case 131: 
    case 159: 
    case 160: 
    case 161: 
    case 162: 
    case 163: 
    case 164: 
    case 165: 
    case 166: 
    case 173: 
    case 175: 
      return -2;
    
    case 46: 
    case 48: 
    case 50: 
    case 51: 
    case 52: 
    case 53: 
    case 54: 
    case 56: 
    case 58: 
    case 59: 
    case 60: 
    case 61: 
    case 62: 
    case 67: 
    case 68: 
    case 69: 
    case 70: 
    case 75: 
    case 76: 
    case 77: 
    case 78: 
    case 87: 
    case 96: 
    case 98: 
    case 100: 
    case 102: 
    case 104: 
    case 106: 
    case 108: 
    case 110: 
    case 112: 
    case 114: 
    case 120: 
    case 121: 
    case 122: 
    case 123: 
    case 124: 
    case 125: 
    case 126: 
    case 128: 
    case 130: 
    case 136: 
    case 137: 
    case 142: 
    case 144: 
    case 149: 
    case 150: 
    case 153: 
    case 154: 
    case 155: 
    case 156: 
    case 157: 
    case 158: 
    case 170: 
    case 171: 
    case 172: 
    case 174: 
    case 176: 
    case 180: 
    case 181: 
    case 182: 
    case 183: 
    case 185: 
    case 191: 
    case 194: 
    case 195: 
    case 198: 
    case 199: 
      return -1;
    
    case 0: 
    case 47: 
    case 49: 
    case 95: 
    case 116: 
    case 117: 
    case 118: 
    case 119: 
    case 132: 
    case 134: 
    case 138: 
    case 139: 
    case 143: 
    case 145: 
    case 146: 
    case 147: 
    case 167: 
    case 169: 
    case 177: 
    case 178: 
    case 179: 
    case 184: 
    case 188: 
    case 189: 
    case 190: 
    case 192: 
    case 193: 
    case 196: 
    case 200: 
    case 202: 
    case 254: 
    case 255: 
      return 0;
    
    case 1: 
    case 2: 
    case 3: 
    case 4: 
    case 5: 
    case 6: 
    case 7: 
    case 8: 
    case 11: 
    case 12: 
    case 13: 
    case 16: 
    case 17: 
    case 18: 
    case 19: 
    case 21: 
    case 23: 
    case 25: 
    case 26: 
    case 27: 
    case 28: 
    case 29: 
    case 34: 
    case 35: 
    case 36: 
    case 37: 
    case 42: 
    case 43: 
    case 44: 
    case 45: 
    case 89: 
    case 90: 
    case 91: 
    case 133: 
    case 135: 
    case 140: 
    case 141: 
    case 168: 
    case 187: 
    case 197: 
    case 201: 
      return 1;
    
    case 9: 
    case 10: 
    case 14: 
    case 15: 
    case 20: 
    case 22: 
    case 24: 
    case 30: 
    case 31: 
    case 32: 
    case 33: 
    case 38: 
    case 39: 
    case 40: 
    case 41: 
    case 92: 
    case 93: 
    case 94: 
      return 2;
    }
    throw new IllegalArgumentException("Bad opcode: " + opcode);
  }
  


















































































































































































































































































































































































































































































































  private static String bytecodeStr(int code)
  {
    return "";
  }
  
  final char[] getCharBuffer(int minimalSize) {
    if (minimalSize > tmpCharBuffer.length) {
      int newSize = tmpCharBuffer.length * 2;
      if (minimalSize > newSize) {
        newSize = minimalSize;
      }
      tmpCharBuffer = new char[newSize];
    }
    return tmpCharBuffer;
  }
  






  private void addSuperBlockStart(int pc)
  {
    if (GenerateStackMap) {
      if (itsSuperBlockStarts == null) {
        itsSuperBlockStarts = new int[4];
      } else if (itsSuperBlockStarts.length == itsSuperBlockStartsTop) {
        int[] tmp = new int[itsSuperBlockStartsTop * 2];
        System.arraycopy(itsSuperBlockStarts, 0, tmp, 0, itsSuperBlockStartsTop);
        
        itsSuperBlockStarts = tmp;
      }
      itsSuperBlockStarts[(itsSuperBlockStartsTop++)] = pc;
    }
  }
  





  private void finalizeSuperBlockStarts()
  {
    if (GenerateStackMap) {
      for (int i = 0; i < itsExceptionTableTop; i++) {
        ExceptionTableEntry ete = itsExceptionTable[i];
        short handlerPC = (short)getLabelPC(itsHandlerLabel);
        addSuperBlockStart(handlerPC);
      }
      Arrays.sort(itsSuperBlockStarts, 0, itsSuperBlockStartsTop);
      int prev = itsSuperBlockStarts[0];
      int copyTo = 1;
      for (int i = 1; i < itsSuperBlockStartsTop; i++) {
        int curr = itsSuperBlockStarts[i];
        if (prev != curr) {
          if (copyTo != i) {
            itsSuperBlockStarts[copyTo] = curr;
          }
          copyTo++;
          prev = curr;
        }
      }
      itsSuperBlockStartsTop = copyTo;
      if (itsSuperBlockStarts[(copyTo - 1)] == itsCodeBufferTop) {
        itsSuperBlockStartsTop -= 1;
      }
    }
  }
  
  private int[] itsSuperBlockStarts = null;
  private int itsSuperBlockStartsTop = 0;
  


  private static final int SuperBlockStartsSize = 4;
  

  private UintMap itsJumpFroms = null;
  
  private static final int LineNumberTableSize = 16;
  
  private static final int ExceptionTableSize = 4;
  private static final int MajorVersion;
  private static final int MinorVersion;
  private static final boolean GenerateStackMap;
  private static final int FileHeaderConstant = -889275714;
  private static final boolean DEBUGSTACK = false;
  private static final boolean DEBUGLABELS = false;
  private static final boolean DEBUGCODE = false;
  private String generatedClassName;
  private ExceptionTableEntry[] itsExceptionTable;
  private int itsExceptionTableTop;
  private int[] itsLineNumberTable;
  private int itsLineNumberTableTop;
  
  static
  {
    InputStream is = null;
    int major = 48;int minor = 0;
    try
    {
      is = ClassFileWriter.class.getResourceAsStream("ClassFileWriter.class");
      if (is == null) {
        is = ClassLoader.getSystemResourceAsStream("net/sourceforge/htmlunit/corejs/classfile/ClassFileWriter.class");
      }
      
      byte[] header = new byte[8];
      

      int read = 0;
      while (read < 8) {
        int c = is.read(header, read, 8 - read);
        if (c < 0)
          throw new IOException();
        read += c;
      }
      minor = header[4] << 8 | header[5] & 0xFF;
      major = header[6] << 8 | header[7] & 0xFF; return;
    }
    catch (Exception localException) {}finally
    {
      MinorVersion = minor;
      MajorVersion = major;
      GenerateStackMap = major >= 50;
      if (is != null) {
        try {
          is.close();
        }
        catch (IOException localIOException2) {}
      }
    }
  }
  














  private byte[] itsCodeBuffer = new byte['Ā'];
  
  private int itsCodeBufferTop;
  
  private ConstantPool itsConstantPool;
  
  private ClassFileMethod itsCurrentMethod;
  
  private short itsStackTop;
  private short itsMaxStack;
  private short itsMaxLocals;
  private ObjArray itsMethods = new ObjArray();
  private ObjArray itsFields = new ObjArray();
  private ObjArray itsInterfaces = new ObjArray();
  
  private short itsFlags;
  
  private short itsThisClassIndex;
  
  private short itsSuperClassIndex;
  
  private short itsSourceFileNameIndex;
  
  private static final int MIN_LABEL_TABLE_SIZE = 32;
  private int[] itsLabelTable;
  private int itsLabelTableTop;
  private static final int MIN_FIXUP_TABLE_SIZE = 40;
  private long[] itsFixupTable;
  private int itsFixupTableTop;
  private ObjArray itsVarDescriptors;
  private char[] tmpCharBuffer = new char[64];
}
