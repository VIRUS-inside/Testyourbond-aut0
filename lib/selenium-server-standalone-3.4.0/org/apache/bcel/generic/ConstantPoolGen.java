package org.apache.bcel.generic;

import java.util.HashMap;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantCP;
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
























































public class ConstantPoolGen
{
  protected int size = 1024;
  protected Constant[] constants = new Constant[size];
  protected int index = 1;
  private static final String METHODREF_DELIM = ":";
  private static final String IMETHODREF_DELIM = "#";
  private static final String FIELDREF_DELIM = "&";
  private static final String NAT_DELIM = "%";
  
  private static class Index {
    int index;
    
    Index(int i) { index = i; }
  }
  




  public ConstantPoolGen(Constant[] cs)
  {
    if (cs.length > size) {
      size = cs.length;
      constants = new Constant[size];
    }
    
    System.arraycopy(cs, 0, constants, 0, cs.length);
    
    if (cs.length > 0) {
      index = cs.length;
    }
    for (int i = 1; i < index; i++) {
      Constant c = constants[i];
      
      if ((c instanceof ConstantString)) {
        ConstantString s = (ConstantString)c;
        ConstantUtf8 u8 = (ConstantUtf8)constants[s.getStringIndex()];
        
        string_table.put(u8.getBytes(), new Index(i));
      } else if ((c instanceof ConstantClass)) {
        ConstantClass s = (ConstantClass)c;
        ConstantUtf8 u8 = (ConstantUtf8)constants[s.getNameIndex()];
        
        class_table.put(u8.getBytes(), new Index(i));
      } else if ((c instanceof ConstantNameAndType)) {
        ConstantNameAndType n = (ConstantNameAndType)c;
        ConstantUtf8 u8 = (ConstantUtf8)constants[n.getNameIndex()];
        ConstantUtf8 u8_2 = (ConstantUtf8)constants[n.getSignatureIndex()];
        
        n_a_t_table.put(u8.getBytes() + "%" + u8_2.getBytes(), new Index(i));
      } else if ((c instanceof ConstantUtf8)) {
        ConstantUtf8 u = (ConstantUtf8)c;
        
        utf8_table.put(u.getBytes(), new Index(i));
      } else if ((c instanceof ConstantCP)) {
        ConstantCP m = (ConstantCP)c;
        ConstantClass clazz = (ConstantClass)constants[m.getClassIndex()];
        ConstantNameAndType n = (ConstantNameAndType)constants[m.getNameAndTypeIndex()];
        
        ConstantUtf8 u8 = (ConstantUtf8)constants[clazz.getNameIndex()];
        String class_name = u8.getBytes().replace('/', '.');
        
        u8 = (ConstantUtf8)constants[n.getNameIndex()];
        String method_name = u8.getBytes();
        
        u8 = (ConstantUtf8)constants[n.getSignatureIndex()];
        String signature = u8.getBytes();
        
        String delim = ":";
        
        if ((c instanceof ConstantInterfaceMethodref)) {
          delim = "#";
        } else if ((c instanceof ConstantFieldref)) {
          delim = "&";
        }
        cp_table.put(class_name + delim + method_name + delim + signature, new Index(i));
      }
    }
  }
  


  public ConstantPoolGen(ConstantPool cp)
  {
    this(cp.getConstantPool());
  }
  


  public ConstantPoolGen() {}
  


  protected void adjustSize()
  {
    if (index + 3 >= size) {
      Constant[] cs = constants;
      
      size *= 2;
      constants = new Constant[size];
      System.arraycopy(cs, 0, constants, 0, index);
    }
  }
  
  private HashMap string_table = new HashMap();
  





  public int lookupString(String str)
  {
    Index index = (Index)string_table.get(str);
    return index != null ? index : -1;
  }
  







  public int addString(String str)
  {
    if ((ret = lookupString(str)) != -1) {
      return ret;
    }
    adjustSize();
    
    ConstantUtf8 u8 = new ConstantUtf8(str);
    ConstantString s = new ConstantString(index);
    
    constants[(index++)] = u8;
    int ret = index;
    constants[(index++)] = s;
    
    string_table.put(str, new Index(ret));
    
    return ret;
  }
  
  private HashMap class_table = new HashMap();
  





  public int lookupClass(String str)
  {
    Index index = (Index)class_table.get(str.replace('.', '/'));
    return index != null ? index : -1;
  }
  

  private int addClass_(String clazz)
  {
    if ((ret = lookupClass(clazz)) != -1) {
      return ret;
    }
    adjustSize();
    
    ConstantClass c = new ConstantClass(addUtf8(clazz));
    
    int ret = index;
    constants[(index++)] = c;
    
    class_table.put(clazz, new Index(ret));
    
    return ret;
  }
  





  public int addClass(String str)
  {
    return addClass_(str.replace('.', '/'));
  }
  





  public int addClass(ObjectType type)
  {
    return addClass(type.getClassName());
  }
  






  public int addArrayClass(ArrayType type)
  {
    return addClass_(type.getSignature());
  }
  





  public int lookupInteger(int n)
  {
    for (int i = 1; i < index; i++) {
      if ((constants[i] instanceof ConstantInteger)) {
        ConstantInteger c = (ConstantInteger)constants[i];
        
        if (c.getBytes() == n) {
          return i;
        }
      }
    }
    return -1;
  }
  







  public int addInteger(int n)
  {
    if ((ret = lookupInteger(n)) != -1) {
      return ret;
    }
    adjustSize();
    
    int ret = index;
    constants[(index++)] = new ConstantInteger(n);
    
    return ret;
  }
  





  public int lookupFloat(float n)
  {
    for (int i = 1; i < index; i++) {
      if ((constants[i] instanceof ConstantFloat)) {
        ConstantFloat c = (ConstantFloat)constants[i];
        
        if (c.getBytes() == n) {
          return i;
        }
      }
    }
    return -1;
  }
  







  public int addFloat(float n)
  {
    if ((ret = lookupFloat(n)) != -1) {
      return ret;
    }
    adjustSize();
    
    int ret = index;
    constants[(index++)] = new ConstantFloat(n);
    
    return ret;
  }
  
  private HashMap utf8_table = new HashMap();
  





  public int lookupUtf8(String n)
  {
    Index index = (Index)utf8_table.get(n);
    
    return index != null ? index : -1;
  }
  







  public int addUtf8(String n)
  {
    if ((ret = lookupUtf8(n)) != -1) {
      return ret;
    }
    adjustSize();
    
    int ret = index;
    constants[(index++)] = new ConstantUtf8(n);
    
    utf8_table.put(n, new Index(ret));
    
    return ret;
  }
  





  public int lookupLong(long n)
  {
    for (int i = 1; i < index; i++) {
      if ((constants[i] instanceof ConstantLong)) {
        ConstantLong c = (ConstantLong)constants[i];
        
        if (c.getBytes() == n) {
          return i;
        }
      }
    }
    return -1;
  }
  







  public int addLong(long n)
  {
    if ((ret = lookupLong(n)) != -1) {
      return ret;
    }
    adjustSize();
    
    int ret = index;
    constants[index] = new ConstantLong(n);
    index += 2;
    
    return ret;
  }
  





  public int lookupDouble(double n)
  {
    for (int i = 1; i < index; i++) {
      if ((constants[i] instanceof ConstantDouble)) {
        ConstantDouble c = (ConstantDouble)constants[i];
        
        if (c.getBytes() == n) {
          return i;
        }
      }
    }
    return -1;
  }
  







  public int addDouble(double n)
  {
    if ((ret = lookupDouble(n)) != -1) {
      return ret;
    }
    adjustSize();
    
    int ret = index;
    constants[index] = new ConstantDouble(n);
    index += 2;
    
    return ret;
  }
  
  private HashMap n_a_t_table = new HashMap();
  






  public int lookupNameAndType(String name, String signature)
  {
    Index index = (Index)n_a_t_table.get(name + "%" + signature);
    return index != null ? index : -1;
  }
  









  public int addNameAndType(String name, String signature)
  {
    if ((ret = lookupNameAndType(name, signature)) != -1) {
      return ret;
    }
    adjustSize();
    
    int name_index = addUtf8(name);
    int signature_index = addUtf8(signature);
    int ret = index;
    constants[(index++)] = new ConstantNameAndType(name_index, signature_index);
    
    n_a_t_table.put(name + "%" + signature, new Index(ret));
    return ret;
  }
  
  private HashMap cp_table = new HashMap();
  







  public int lookupMethodref(String class_name, String method_name, String signature)
  {
    Index index = (Index)cp_table.get(class_name + ":" + method_name + ":" + signature);
    
    return index != null ? index : -1;
  }
  
  public int lookupMethodref(MethodGen method) {
    return lookupMethodref(method.getClassName(), method.getName(), method.getSignature());
  }
  









  public int addMethodref(String class_name, String method_name, String signature)
  {
    if ((ret = lookupMethodref(class_name, method_name, signature)) != -1) {
      return ret;
    }
    adjustSize();
    
    int name_and_type_index = addNameAndType(method_name, signature);
    int class_index = addClass(class_name);
    int ret = index;
    constants[(index++)] = new ConstantMethodref(class_index, name_and_type_index);
    
    cp_table.put(class_name + ":" + method_name + ":" + signature, new Index(ret));
    

    return ret;
  }
  
  public int addMethodref(MethodGen method) {
    return addMethodref(method.getClassName(), method.getName(), method.getSignature());
  }
  








  public int lookupInterfaceMethodref(String class_name, String method_name, String signature)
  {
    Index index = (Index)cp_table.get(class_name + "#" + method_name + "#" + signature);
    
    return index != null ? index : -1;
  }
  
  public int lookupInterfaceMethodref(MethodGen method) {
    return lookupInterfaceMethodref(method.getClassName(), method.getName(), method.getSignature());
  }
  









  public int addInterfaceMethodref(String class_name, String method_name, String signature)
  {
    if ((ret = lookupInterfaceMethodref(class_name, method_name, signature)) != -1) {
      return ret;
    }
    adjustSize();
    
    int class_index = addClass(class_name);
    int name_and_type_index = addNameAndType(method_name, signature);
    int ret = index;
    constants[(index++)] = new ConstantInterfaceMethodref(class_index, name_and_type_index);
    
    cp_table.put(class_name + "#" + method_name + "#" + signature, new Index(ret));
    

    return ret;
  }
  
  public int addInterfaceMethodref(MethodGen method) {
    return addInterfaceMethodref(method.getClassName(), method.getName(), method.getSignature());
  }
  








  public int lookupFieldref(String class_name, String field_name, String signature)
  {
    Index index = (Index)cp_table.get(class_name + "&" + field_name + "&" + signature);
    
    return index != null ? index : -1;
  }
  









  public int addFieldref(String class_name, String field_name, String signature)
  {
    if ((ret = lookupFieldref(class_name, field_name, signature)) != -1) {
      return ret;
    }
    adjustSize();
    
    int class_index = addClass(class_name);
    int name_and_type_index = addNameAndType(field_name, signature);
    int ret = index;
    constants[(index++)] = new ConstantFieldref(class_index, name_and_type_index);
    
    cp_table.put(class_name + "&" + field_name + "&" + signature, new Index(ret));
    
    return ret;
  }
  


  public Constant getConstant(int i)
  {
    return constants[i];
  }
  



  public void setConstant(int i, Constant c)
  {
    constants[i] = c;
  }
  

  public ConstantPool getConstantPool()
  {
    return new ConstantPool(constants);
  }
  


  public int getSize()
  {
    return index;
  }
  


  public ConstantPool getFinalConstantPool()
  {
    Constant[] cs = new Constant[index];
    
    System.arraycopy(constants, 0, cs, 0, index);
    
    return new ConstantPool(cs);
  }
  


  public String toString()
  {
    StringBuffer buf = new StringBuffer();
    
    for (int i = 1; i < index; i++) {
      buf.append(i + ")" + constants[i] + "\n");
    }
    return buf.toString();
  }
  

  public int addConstant(Constant c, ConstantPoolGen cp)
  {
    Constant[] constants = cp.getConstantPool().getConstantPool();
    
    switch (c.getTag()) {
    case 8: 
      ConstantString s = (ConstantString)c;
      ConstantUtf8 u8 = (ConstantUtf8)constants[s.getStringIndex()];
      
      return addString(u8.getBytes());
    

    case 7: 
      ConstantClass s = (ConstantClass)c;
      ConstantUtf8 u8 = (ConstantUtf8)constants[s.getNameIndex()];
      
      return addClass(u8.getBytes());
    

    case 12: 
      ConstantNameAndType n = (ConstantNameAndType)c;
      ConstantUtf8 u8 = (ConstantUtf8)constants[n.getNameIndex()];
      ConstantUtf8 u8_2 = (ConstantUtf8)constants[n.getSignatureIndex()];
      
      return addNameAndType(u8.getBytes(), u8_2.getBytes());
    

    case 1: 
      return addUtf8(((ConstantUtf8)c).getBytes());
    
    case 6: 
      return addDouble(((ConstantDouble)c).getBytes());
    
    case 4: 
      return addFloat(((ConstantFloat)c).getBytes());
    
    case 5: 
      return addLong(((ConstantLong)c).getBytes());
    
    case 3: 
      return addInteger(((ConstantInteger)c).getBytes());
    case 9: 
    case 10: 
    case 11: 
      ConstantCP m = (ConstantCP)c;
      ConstantClass clazz = (ConstantClass)constants[m.getClassIndex()];
      ConstantNameAndType n = (ConstantNameAndType)constants[m.getNameAndTypeIndex()];
      ConstantUtf8 u8 = (ConstantUtf8)constants[clazz.getNameIndex()];
      String class_name = u8.getBytes().replace('/', '.');
      
      u8 = (ConstantUtf8)constants[n.getNameIndex()];
      String name = u8.getBytes();
      
      u8 = (ConstantUtf8)constants[n.getSignatureIndex()];
      String signature = u8.getBytes();
      
      switch (c.getTag()) {
      case 11: 
        return addInterfaceMethodref(class_name, name, signature);
      
      case 10: 
        return addMethodref(class_name, name, signature);
      
      case 9: 
        return addFieldref(class_name, name, signature);
      }
      
      throw new RuntimeException("Unknown constant type " + c);
    }
    
    

    throw new RuntimeException("Unknown constant type " + c);
  }
}
