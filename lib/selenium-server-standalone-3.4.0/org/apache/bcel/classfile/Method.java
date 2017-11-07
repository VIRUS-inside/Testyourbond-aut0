package org.apache.bcel.classfile;

import java.io.DataInputStream;
import java.io.IOException;



































































public final class Method
  extends FieldOrMethod
{
  public Method() {}
  
  public Method(Method c)
  {
    super(c);
  }
  






  Method(DataInputStream file, ConstantPool constant_pool)
    throws IOException, ClassFormatError
  {
    super(file, constant_pool);
  }
  








  public Method(int access_flags, int name_index, int signature_index, Attribute[] attributes, ConstantPool constant_pool)
  {
    super(access_flags, name_index, signature_index, attributes, constant_pool);
  }
  






  public void accept(Visitor v)
  {
    v.visitMethod(this);
  }
  


  public final Code getCode()
  {
    for (int i = 0; i < attributes_count; i++) {
      if ((attributes[i] instanceof Code))
        return (Code)attributes[i];
    }
    return null;
  }
  



  public final ExceptionTable getExceptionTable()
  {
    for (int i = 0; i < attributes_count; i++) {
      if ((attributes[i] instanceof ExceptionTable))
        return (ExceptionTable)attributes[i];
    }
    return null;
  }
  


  public final LocalVariableTable getLocalVariableTable()
  {
    Code code = getCode();
    
    if (code != null) {
      return code.getLocalVariableTable();
    }
    return null;
  }
  


  public final LineNumberTable getLineNumberTable()
  {
    Code code = getCode();
    
    if (code != null) {
      return code.getLineNumberTable();
    }
    return null;
  }
  












  public final String toString()
  {
    String access = Utility.accessToString(access_flags);
    

    ConstantUtf8 c = (ConstantUtf8)constant_pool.getConstant(signature_index, (byte)1);
    
    String signature = c.getBytes();
    
    c = (ConstantUtf8)constant_pool.getConstant(name_index, (byte)1);
    String name = c.getBytes();
    
    signature = Utility.methodSignatureToString(signature, name, access, true, getLocalVariableTable());
    
    StringBuffer buf = new StringBuffer(signature);
    
    for (int i = 0; i < attributes_count; i++) {
      Attribute a = attributes[i];
      
      if ((!(a instanceof Code)) && (!(a instanceof ExceptionTable))) {
        buf.append(" [" + a.toString() + "]");
      }
    }
    ExceptionTable e = getExceptionTable();
    if (e != null) {
      String str = e.toString();
      if (!str.equals("")) {
        buf.append("\n\t\tthrows " + str);
      }
    }
    return buf.toString();
  }
  


  public final Method copy(ConstantPool constant_pool)
  {
    return (Method)copy_(constant_pool);
  }
}
