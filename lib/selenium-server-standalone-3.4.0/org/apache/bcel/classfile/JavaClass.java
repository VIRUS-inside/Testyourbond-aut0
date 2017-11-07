package org.apache.bcel.classfile;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.StringTokenizer;
import org.apache.bcel.Repository;























































public class JavaClass
  extends AccessFlags
  implements Cloneable, Node
{
  private String file_name;
  private String package_name;
  private String source_file_name = "<Unknown>";
  private int class_name_index;
  private int superclass_name_index;
  private String class_name;
  private String superclass_name;
  private int major;
  private int minor;
  private ConstantPool constant_pool;
  private int[] interfaces;
  private String[] interface_names;
  private Field[] fields;
  private Method[] methods;
  private Attribute[] attributes; private byte source = 1;
  
  public static final byte HEAP = 1;
  
  public static final byte FILE = 2;
  public static final byte ZIP = 3;
  static boolean debug = false;
  static char sep = '/';
  



























  public JavaClass(int class_name_index, int superclass_name_index, String file_name, int major, int minor, int access_flags, ConstantPool constant_pool, int[] interfaces, Field[] fields, Method[] methods, Attribute[] attributes, byte source)
  {
    if (interfaces == null)
      interfaces = new int[0];
    if (attributes == null)
      this.attributes = new Attribute[0];
    if (fields == null)
      fields = new Field[0];
    if (methods == null) {
      methods = new Method[0];
    }
    this.class_name_index = class_name_index;
    this.superclass_name_index = superclass_name_index;
    this.file_name = file_name;
    this.major = major;
    this.minor = minor;
    this.access_flags = access_flags;
    this.constant_pool = constant_pool;
    this.interfaces = interfaces;
    this.fields = fields;
    this.methods = methods;
    this.attributes = attributes;
    this.source = source;
    

    for (int i = 0; i < attributes.length; i++) {
      if ((attributes[i] instanceof SourceFile)) {
        source_file_name = ((SourceFile)attributes[i]).getSourceFileName();
        break;
      }
    }
    







    class_name = constant_pool.getConstantString(class_name_index, (byte)7);
    
    class_name = Utility.compactClassName(class_name, false);
    
    int index = class_name.lastIndexOf('.');
    if (index < 0) {
      package_name = "";
    } else {
      package_name = class_name.substring(0, index);
    }
    if (superclass_name_index > 0) {
      superclass_name = constant_pool.getConstantString(superclass_name_index, (byte)7);
      
      superclass_name = Utility.compactClassName(superclass_name, false);
    }
    else {
      superclass_name = "java.lang.Object";
    }
    interface_names = new String[interfaces.length];
    for (int i = 0; i < interfaces.length; i++) {
      String str = constant_pool.getConstantString(interfaces[i], (byte)7);
      interface_names[i] = Utility.compactClassName(str, false);
    }
  }
  
























  public JavaClass(int class_name_index, int superclass_name_index, String file_name, int major, int minor, int access_flags, ConstantPool constant_pool, int[] interfaces, Field[] fields, Method[] methods, Attribute[] attributes)
  {
    this(class_name_index, superclass_name_index, file_name, major, minor, access_flags, constant_pool, interfaces, fields, methods, attributes, (byte)1);
  }
  








  public void accept(Visitor v)
  {
    v.visitJavaClass(this);
  }
  

  static final void Debug(String str)
  {
    if (debug) {
      System.out.println(str);
    }
  }
  




  public void dump(File file)
    throws IOException
  {
    String parent = file.getParent();
    
    if (parent != null) {
      File dir = new File(parent);
      
      if (dir != null) {
        dir.mkdirs();
      }
    }
    dump(new DataOutputStream(new FileOutputStream(file)));
  }
  





  public void dump(String file_name)
    throws IOException
  {
    dump(new File(file_name));
  }
  


  public byte[] getBytes()
  {
    ByteArrayOutputStream s = new ByteArrayOutputStream();
    DataOutputStream ds = new DataOutputStream(s);
    try
    {
      dump(ds);
      ds.close();
    } catch (IOException e) { e.printStackTrace();
    }
    return s.toByteArray();
  }
  




  public void dump(OutputStream file)
    throws IOException
  {
    dump(new DataOutputStream(file));
  }
  





  public void dump(DataOutputStream file)
    throws IOException
  {
    file.writeInt(-889275714);
    file.writeShort(minor);
    file.writeShort(major);
    
    constant_pool.dump(file);
    
    file.writeShort(access_flags);
    file.writeShort(class_name_index);
    file.writeShort(superclass_name_index);
    
    file.writeShort(interfaces.length);
    for (int i = 0; i < interfaces.length; i++) {
      file.writeShort(interfaces[i]);
    }
    file.writeShort(fields.length);
    for (int i = 0; i < fields.length; i++) {
      fields[i].dump(file);
    }
    file.writeShort(methods.length);
    for (int i = 0; i < methods.length; i++) {
      methods[i].dump(file);
    }
    if (attributes != null) {
      file.writeShort(attributes.length);
      for (int i = 0; i < attributes.length; i++) {
        attributes[i].dump(file);
      }
    } else {
      file.writeShort(0);
    }
    file.close();
  }
  

  public Attribute[] getAttributes()
  {
    return attributes;
  }
  
  public String getClassName()
  {
    return class_name;
  }
  
  public String getPackageName()
  {
    return package_name;
  }
  
  public int getClassNameIndex()
  {
    return class_name_index;
  }
  
  public ConstantPool getConstantPool()
  {
    return constant_pool;
  }
  
  public Field[] getFields()
  {
    return fields;
  }
  
  public String getFileName() {
    return file_name;
  }
  
  public String[] getInterfaceNames() {
    return interface_names;
  }
  
  public int[] getInterfaces() {
    return interfaces;
  }
  
  public int getMajor() {
    return major;
  }
  
  public Method[] getMethods() {
    return methods;
  }
  
  public int getMinor() {
    return minor;
  }
  
  public String getSourceFileName()
  {
    return source_file_name;
  }
  
  public String getSuperclassName()
  {
    return superclass_name;
  }
  
  public int getSuperclassNameIndex() {
    return superclass_name_index;
  }
  
  static {
    String debug = System.getProperty("JavaClass.debug");
    
    if (debug != null) {
      debug = new Boolean(debug).booleanValue();
    }
    
    String sep = System.getProperty("file.separator");
    
    if (sep != null) {
      try {
        sep = sep.charAt(0);
      }
      catch (StringIndexOutOfBoundsException e) {}
    }
  }
  
  public void setAttributes(Attribute[] attributes)
  {
    this.attributes = attributes;
  }
  

  public void setClassName(String class_name)
  {
    this.class_name = class_name;
  }
  

  public void setClassNameIndex(int class_name_index)
  {
    this.class_name_index = class_name_index;
  }
  

  public void setConstantPool(ConstantPool constant_pool)
  {
    this.constant_pool = constant_pool;
  }
  

  public void setFields(Field[] fields)
  {
    this.fields = fields;
  }
  

  public void setFileName(String file_name)
  {
    this.file_name = file_name;
  }
  

  public void setInterfaceNames(String[] interface_names)
  {
    this.interface_names = interface_names;
  }
  

  public void setInterfaces(int[] interfaces)
  {
    this.interfaces = interfaces;
  }
  

  public void setMajor(int major)
  {
    this.major = major;
  }
  

  public void setMethods(Method[] methods)
  {
    this.methods = methods;
  }
  

  public void setMinor(int minor)
  {
    this.minor = minor;
  }
  

  public void setSourceFileName(String source_file_name)
  {
    this.source_file_name = source_file_name;
  }
  

  public void setSuperclassName(String superclass_name)
  {
    this.superclass_name = superclass_name;
  }
  

  public void setSuperclassNameIndex(int superclass_name_index)
  {
    this.superclass_name_index = superclass_name_index;
  }
  

  public String toString()
  {
    String access = Utility.accessToString(access_flags, true);
    access = access + " ";
    
    StringBuffer buf = new StringBuffer(access + Utility.classOrInterface(access_flags) + " " + class_name + " extends " + Utility.compactClassName(superclass_name, false) + '\n');
    




    int size = interfaces.length;
    
    if (size > 0) {
      buf.append("implements\t\t");
      
      for (int i = 0; i < size; i++) {
        buf.append(interface_names[i]);
        if (i < size - 1) {
          buf.append(", ");
        }
      }
      buf.append('\n');
    }
    
    buf.append("filename\t\t" + file_name + '\n');
    buf.append("compiled from\t\t" + source_file_name + '\n');
    buf.append("compiler version\t" + major + "." + minor + '\n');
    buf.append("access flags\t\t" + access_flags + '\n');
    buf.append("constant pool\t\t" + constant_pool.getLength() + " entries\n");
    buf.append("ACC_SUPER flag\t\t" + isSuper() + "\n");
    
    if (attributes.length > 0) {
      buf.append("\nAttribute(s):\n");
      for (int i = 0; i < attributes.length; i++) {
        buf.append(indent(attributes[i]));
      }
    }
    if (fields.length > 0) {
      buf.append("\n" + fields.length + " fields:\n");
      for (int i = 0; i < fields.length; i++) {
        buf.append("\t" + fields[i] + '\n');
      }
    }
    if (methods.length > 0) {
      buf.append("\n" + methods.length + " methods:\n");
      for (int i = 0; i < methods.length; i++) {
        buf.append("\t" + methods[i] + '\n');
      }
    }
    return buf.toString();
  }
  
  private static final String indent(Object obj) {
    StringTokenizer tok = new StringTokenizer(obj.toString(), "\n");
    StringBuffer buf = new StringBuffer();
    
    while (tok.hasMoreTokens()) {
      buf.append("\t" + tok.nextToken() + "\n");
    }
    return buf.toString();
  }
  


  public JavaClass copy()
  {
    JavaClass c = null;
    try
    {
      c = (JavaClass)clone();
    }
    catch (CloneNotSupportedException e) {}
    constant_pool = constant_pool.copy();
    interfaces = ((int[])interfaces.clone());
    interface_names = ((String[])interface_names.clone());
    
    fields = new Field[fields.length];
    for (int i = 0; i < fields.length; i++) {
      fields[i] = fields[i].copy(constant_pool);
    }
    methods = new Method[methods.length];
    for (int i = 0; i < methods.length; i++) {
      methods[i] = methods[i].copy(constant_pool);
    }
    attributes = new Attribute[attributes.length];
    for (int i = 0; i < attributes.length; i++) {
      attributes[i] = attributes[i].copy(constant_pool);
    }
    return c;
  }
  
  public final boolean instanceOf(JavaClass super_class) {
    return Repository.instanceOf(this, super_class);
  }
  
  public final boolean isSuper() {
    return (access_flags & 0x20) != 0;
  }
  
  public final boolean isClass() {
    return (access_flags & 0x200) == 0;
  }
  

  public final byte getSource()
  {
    return source;
  }
}
