package org.apache.bcel.classfile;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;





































































public final class ClassParser
{
  private DataInputStream file;
  private ZipFile zip;
  private String file_name;
  private int class_name_index;
  private int superclass_name_index;
  private int major;
  private int minor;
  private int access_flags;
  private int[] interfaces;
  private ConstantPool constant_pool;
  private Field[] fields;
  private Method[] methods;
  private Attribute[] attributes;
  private boolean is_zip;
  private static final int BUFSIZE = 8192;
  
  public ClassParser(InputStream file, String file_name)
  {
    this.file_name = file_name;
    
    String clazz = file.getClass().getName();
    is_zip = ((clazz.startsWith("java.util.zip.")) || (clazz.startsWith("java.util.jar.")));
    
    if ((file instanceof DataInputStream)) {
      this.file = ((DataInputStream)file);
    } else {
      this.file = new DataInputStream(new BufferedInputStream(file, 8192));
    }
  }
  



  public ClassParser(String file_name)
    throws IOException
  {
    is_zip = false;
    this.file_name = file_name;
    file = new DataInputStream(new BufferedInputStream(new FileInputStream(file_name), 8192));
  }
  





  public ClassParser(String zip_file, String file_name)
    throws IOException
  {
    is_zip = true;
    zip = new ZipFile(zip_file);
    ZipEntry entry = zip.getEntry(file_name);
    
    this.file_name = file_name;
    
    file = new DataInputStream(new BufferedInputStream(zip.getInputStream(entry), 8192));
  }
  













  public JavaClass parse()
    throws IOException, ClassFormatError
  {
    readID();
    

    readVersion();
    


    readConstantPool();
    

    readClassInfo();
    

    readInterfaces();
    


    readFields();
    

    readMethods();
    

    readAttributes();
    


















    file.close();
    if (zip != null) {
      zip.close();
    }
    
    return new JavaClass(class_name_index, superclass_name_index, file_name, major, minor, access_flags, constant_pool, interfaces, fields, methods, attributes, (byte)(is_zip ? 3 : 2));
  }
  









  private final void readAttributes()
    throws IOException, ClassFormatError
  {
    int attributes_count = file.readUnsignedShort();
    attributes = new Attribute[attributes_count];
    
    for (int i = 0; i < attributes_count; i++) {
      attributes[i] = Attribute.readAttribute(file, constant_pool);
    }
  }
  



  private final void readClassInfo()
    throws IOException, ClassFormatError
  {
    access_flags = file.readUnsignedShort();
    



    if ((access_flags & 0x200) != 0) {
      access_flags |= 0x400;
    }
    if (((access_flags & 0x400) != 0) && ((access_flags & 0x10) != 0))
    {
      throw new ClassFormatError("Class can't be both final and abstract");
    }
    class_name_index = file.readUnsignedShort();
    superclass_name_index = file.readUnsignedShort();
  }
  



  private final void readConstantPool()
    throws IOException, ClassFormatError
  {
    constant_pool = new ConstantPool(file);
  }
  






  private final void readFields()
    throws IOException, ClassFormatError
  {
    int fields_count = file.readUnsignedShort();
    fields = new Field[fields_count];
    
    for (int i = 0; i < fields_count; i++) {
      fields[i] = new Field(file, constant_pool);
    }
  }
  






  private final void readID()
    throws IOException, ClassFormatError
  {
    int magic = -889275714;
    
    if (file.readInt() != magic) {
      throw new ClassFormatError(file_name + " is not a Java .class file");
    }
  }
  




  private final void readInterfaces()
    throws IOException, ClassFormatError
  {
    int interfaces_count = file.readUnsignedShort();
    interfaces = new int[interfaces_count];
    
    for (int i = 0; i < interfaces_count; i++) {
      interfaces[i] = file.readUnsignedShort();
    }
  }
  




  private final void readMethods()
    throws IOException, ClassFormatError
  {
    int methods_count = file.readUnsignedShort();
    methods = new Method[methods_count];
    
    for (int i = 0; i < methods_count; i++) {
      methods[i] = new Method(file, constant_pool);
    }
  }
  


  private final void readVersion()
    throws IOException, ClassFormatError
  {
    minor = file.readUnsignedShort();
    major = file.readUnsignedShort();
  }
}
