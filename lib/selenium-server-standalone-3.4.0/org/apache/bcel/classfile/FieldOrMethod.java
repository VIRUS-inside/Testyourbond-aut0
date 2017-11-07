package org.apache.bcel.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;



























































public abstract class FieldOrMethod
  extends AccessFlags
  implements Cloneable, Node
{
  protected int name_index;
  protected int signature_index;
  protected int attributes_count;
  protected Attribute[] attributes;
  protected ConstantPool constant_pool;
  
  FieldOrMethod() {}
  
  protected FieldOrMethod(FieldOrMethod c)
  {
    this(c.getAccessFlags(), c.getNameIndex(), c.getSignatureIndex(), c.getAttributes(), c.getConstantPool());
  }
  







  protected FieldOrMethod(DataInputStream file, ConstantPool constant_pool)
    throws IOException, ClassFormatError
  {
    this(file.readUnsignedShort(), file.readUnsignedShort(), file.readUnsignedShort(), null, constant_pool);
    

    attributes_count = file.readUnsignedShort();
    attributes = new Attribute[attributes_count];
    for (int i = 0; i < attributes_count; i++) {
      attributes[i] = Attribute.readAttribute(file, constant_pool);
    }
  }
  







  protected FieldOrMethod(int access_flags, int name_index, int signature_index, Attribute[] attributes, ConstantPool constant_pool)
  {
    this.access_flags = access_flags;
    this.name_index = name_index;
    this.signature_index = signature_index;
    this.constant_pool = constant_pool;
    
    setAttributes(attributes);
  }
  





  public final void dump(DataOutputStream file)
    throws IOException
  {
    file.writeShort(access_flags);
    file.writeShort(name_index);
    file.writeShort(signature_index);
    file.writeShort(attributes_count);
    
    for (int i = 0; i < attributes_count; i++) {
      attributes[i].dump(file);
    }
  }
  
  public final Attribute[] getAttributes()
  {
    return attributes;
  }
  

  public final void setAttributes(Attribute[] attributes)
  {
    this.attributes = attributes;
    attributes_count = (attributes == null ? 0 : attributes.length);
  }
  

  public final ConstantPool getConstantPool()
  {
    return constant_pool;
  }
  

  public final void setConstantPool(ConstantPool constant_pool)
  {
    this.constant_pool = constant_pool;
  }
  

  public final int getNameIndex()
  {
    return name_index;
  }
  

  public final void setNameIndex(int name_index)
  {
    this.name_index = name_index;
  }
  

  public final int getSignatureIndex()
  {
    return signature_index;
  }
  

  public final void setSignatureIndex(int signature_index)
  {
    this.signature_index = signature_index;
  }
  



  public final String getName()
  {
    ConstantUtf8 c = (ConstantUtf8)constant_pool.getConstant(name_index, (byte)1);
    
    return c.getBytes();
  }
  



  public final String getSignature()
  {
    ConstantUtf8 c = (ConstantUtf8)constant_pool.getConstant(signature_index, (byte)1);
    
    return c.getBytes();
  }
  


  protected FieldOrMethod copy_(ConstantPool constant_pool)
  {
    FieldOrMethod c = null;
    try
    {
      c = (FieldOrMethod)clone();
    }
    catch (CloneNotSupportedException e) {}
    constant_pool = constant_pool;
    attributes = new Attribute[attributes_count];
    
    for (int i = 0; i < attributes_count; i++) {
      attributes[i] = attributes[i].copy(constant_pool);
    }
    return c;
  }
  
  public abstract void accept(Visitor paramVisitor);
}
