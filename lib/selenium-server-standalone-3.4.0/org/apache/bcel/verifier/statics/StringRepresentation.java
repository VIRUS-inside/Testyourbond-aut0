package org.apache.bcel.verifier.statics;

import org.apache.bcel.classfile.Code;
import org.apache.bcel.classfile.CodeException;
import org.apache.bcel.classfile.ConstantClass;
import org.apache.bcel.classfile.ConstantDouble;
import org.apache.bcel.classfile.ConstantFieldref;
import org.apache.bcel.classfile.ConstantFloat;
import org.apache.bcel.classfile.ConstantInteger;
import org.apache.bcel.classfile.ConstantInterfaceMethodref;
import org.apache.bcel.classfile.ConstantLong;
import org.apache.bcel.classfile.ConstantMethodref;
import org.apache.bcel.classfile.ConstantNameAndType;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.ConstantString;
import org.apache.bcel.classfile.ConstantUtf8;
import org.apache.bcel.classfile.ConstantValue;
import org.apache.bcel.classfile.Deprecated;
import org.apache.bcel.classfile.EmptyVisitor;
import org.apache.bcel.classfile.ExceptionTable;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.InnerClass;
import org.apache.bcel.classfile.InnerClasses;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.LineNumber;
import org.apache.bcel.classfile.LineNumberTable;
import org.apache.bcel.classfile.LocalVariable;
import org.apache.bcel.classfile.LocalVariableTable;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.classfile.Node;
import org.apache.bcel.classfile.SourceFile;
import org.apache.bcel.classfile.Synthetic;
import org.apache.bcel.classfile.Unknown;
import org.apache.bcel.classfile.Visitor;








































public class StringRepresentation
  extends EmptyVisitor
  implements Visitor
{
  private String tostring;
  
  public StringRepresentation(Node n)
  {
    n.accept(this);
  }
  

  public String toString()
  {
    return tostring;
  }
  

  private String toString(Node obj)
  {
    String ret;
    
    try
    {
      ret = obj.toString();
    }
    catch (RuntimeException e) {
      String s = obj.getClass().getName();
      s = s.substring(s.lastIndexOf(".") + 1);
      ret = "<<" + s + ">>";
    }
    return ret;
  }
  





  public void visitCode(Code obj)
  {
    tostring = "<CODE>";
  }
  
  public void visitCodeException(CodeException obj) { tostring = toString(obj); }
  
  public void visitConstantClass(ConstantClass obj) {
    tostring = toString(obj);
  }
  
  public void visitConstantDouble(ConstantDouble obj) { tostring = toString(obj); }
  
  public void visitConstantFieldref(ConstantFieldref obj) {
    tostring = toString(obj);
  }
  
  public void visitConstantFloat(ConstantFloat obj) { tostring = toString(obj); }
  
  public void visitConstantInteger(ConstantInteger obj) {
    tostring = toString(obj);
  }
  
  public void visitConstantInterfaceMethodref(ConstantInterfaceMethodref obj) { tostring = toString(obj); }
  
  public void visitConstantLong(ConstantLong obj) {
    tostring = toString(obj);
  }
  
  public void visitConstantMethodref(ConstantMethodref obj) { tostring = toString(obj); }
  
  public void visitConstantNameAndType(ConstantNameAndType obj) {
    tostring = toString(obj);
  }
  
  public void visitConstantPool(ConstantPool obj) { tostring = toString(obj); }
  
  public void visitConstantString(ConstantString obj) {
    tostring = toString(obj);
  }
  
  public void visitConstantUtf8(ConstantUtf8 obj) { tostring = toString(obj); }
  
  public void visitConstantValue(ConstantValue obj) {
    tostring = toString(obj);
  }
  
  public void visitDeprecated(Deprecated obj) { tostring = toString(obj); }
  
  public void visitExceptionTable(ExceptionTable obj) {
    tostring = toString(obj);
  }
  
  public void visitField(Field obj) { tostring = toString(obj); }
  
  public void visitInnerClass(InnerClass obj) {
    tostring = toString(obj);
  }
  
  public void visitInnerClasses(InnerClasses obj) { tostring = toString(obj); }
  
  public void visitJavaClass(JavaClass obj) {
    tostring = toString(obj);
  }
  
  public void visitLineNumber(LineNumber obj) { tostring = toString(obj); }
  
  public void visitLineNumberTable(LineNumberTable obj) {
    tostring = ("<LineNumberTable: " + toString(obj) + ">");
  }
  
  public void visitLocalVariable(LocalVariable obj) { tostring = toString(obj); }
  
  public void visitLocalVariableTable(LocalVariableTable obj) {
    tostring = ("<LocalVariableTable: " + toString(obj) + ">");
  }
  
  public void visitMethod(Method obj) { tostring = toString(obj); }
  
  public void visitSourceFile(SourceFile obj) {
    tostring = toString(obj);
  }
  
  public void visitSynthetic(Synthetic obj) { tostring = toString(obj); }
  
  public void visitUnknown(Unknown obj) {
    tostring = toString(obj);
  }
}
