package org.apache.bcel.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;



























































public final class Unknown
  extends Attribute
{
  private byte[] bytes;
  private String name;
  private static HashMap unknown_attributes = new HashMap();
  

  static Unknown[] getUnknownAttributes()
  {
    Unknown[] unknowns = new Unknown[unknown_attributes.size()];
    Iterator entries = unknown_attributes.values().iterator();
    
    for (int i = 0; entries.hasNext(); i++) {
      unknowns[i] = ((Unknown)entries.next());
    }
    unknown_attributes.clear();
    return unknowns;
  }
  



  public Unknown(Unknown c)
  {
    this(c.getNameIndex(), c.getLength(), c.getBytes(), c.getConstantPool());
  }
  









  public Unknown(int name_index, int length, byte[] bytes, ConstantPool constant_pool)
  {
    super((byte)-1, name_index, length, constant_pool);
    this.bytes = bytes;
    
    name = ((ConstantUtf8)constant_pool.getConstant(name_index, (byte)1)).getBytes();
    
    unknown_attributes.put(name, this);
  }
  









  Unknown(int name_index, int length, DataInputStream file, ConstantPool constant_pool)
    throws IOException
  {
    this(name_index, length, (byte[])null, constant_pool);
    
    if (length > 0) {
      bytes = new byte[length];
      file.readFully(bytes);
    }
  }
  






  public void accept(Visitor v)
  {
    v.visitUnknown(this);
  }
  




  public final void dump(DataOutputStream file)
    throws IOException
  {
    super.dump(file);
    if (length > 0) {
      file.write(bytes, 0, length);
    }
  }
  
  public final byte[] getBytes() {
    return bytes;
  }
  
  public final String getName()
  {
    return name;
  }
  

  public final void setBytes(byte[] bytes)
  {
    this.bytes = bytes;
  }
  


  public final String toString()
  {
    if ((length == 0) || (bytes == null)) {
      return "(Unknown attribute " + name + ")";
    }
    String hex;
    if (length > 10) {
      byte[] tmp = new byte[10];
      System.arraycopy(bytes, 0, tmp, 0, 10);
      hex = Utility.toHexString(tmp) + "... (truncated)";
    }
    else {
      hex = Utility.toHexString(bytes);
    }
    return "(Unknown attribute " + name + ": " + hex + ")";
  }
  


  public Attribute copy(ConstantPool constant_pool)
  {
    Unknown c = (Unknown)clone();
    
    if (bytes != null) {
      bytes = ((byte[])bytes.clone());
    }
    constant_pool = constant_pool;
    return c;
  }
}
