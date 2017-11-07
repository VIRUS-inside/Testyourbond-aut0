package org.apache.bcel.generic;

import java.io.PrintStream;
import java.util.AbstractCollection;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.bcel.classfile.AccessFlags;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.FieldOrMethod;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.classfile.SourceFile;






















































public class ClassGen
  extends AccessFlags
  implements Cloneable
{
  private int class_name_index = -1; private int superclass_name_index = -1;
  private int major = 45; private int minor = 3;
  



  private ArrayList field_vec = new ArrayList();
  private ArrayList method_vec = new ArrayList();
  private ArrayList attribute_vec = new ArrayList();
  private ArrayList interface_vec = new ArrayList();
  
  private String class_name;
  
  private String super_class_name;
  
  private String file_name;
  private ConstantPoolGen cp;
  private ArrayList observers;
  
  public ClassGen(String class_name, String super_class_name, String file_name, int access_flags, String[] interfaces)
  {
    this.class_name = class_name;
    this.super_class_name = super_class_name;
    this.file_name = file_name;
    this.access_flags = access_flags;
    cp = new ConstantPoolGen();
    

    addAttribute(new SourceFile(cp.addUtf8("SourceFile"), 2, cp.addUtf8(file_name), cp.getConstantPool()));
    
    class_name_index = cp.addClass(class_name);
    superclass_name_index = cp.addClass(super_class_name);
    
    if (interfaces != null) {
      for (int i = 0; i < interfaces.length; i++) {
        addInterface(interfaces[i]);
      }
    }
  }
  

  public ClassGen(JavaClass clazz)
  {
    class_name_index = clazz.getClassNameIndex();
    superclass_name_index = clazz.getSuperclassNameIndex();
    class_name = clazz.getClassName();
    super_class_name = clazz.getSuperclassName();
    file_name = clazz.getSourceFileName();
    access_flags = clazz.getAccessFlags();
    cp = new ConstantPoolGen(clazz.getConstantPool());
    major = clazz.getMajor();
    minor = clazz.getMinor();
    
    Attribute[] attributes = clazz.getAttributes();
    Method[] methods = clazz.getMethods();
    Field[] fields = clazz.getFields();
    String[] interfaces = clazz.getInterfaceNames();
    
    for (int i = 0; i < interfaces.length; i++) {
      addInterface(interfaces[i]);
    }
    for (int i = 0; i < attributes.length; i++) {
      addAttribute(attributes[i]);
    }
    for (int i = 0; i < methods.length; i++) {
      addMethod(methods[i]);
    }
    for (int i = 0; i < fields.length; i++) {
      addField(fields[i]);
    }
  }
  

  public JavaClass getJavaClass()
  {
    int[] interfaces = getInterfaces();
    Field[] fields = getFields();
    Method[] methods = getMethods();
    Attribute[] attributes = getAttributes();
    

    ConstantPool cp = this.cp.getFinalConstantPool();
    
    return new JavaClass(class_name_index, superclass_name_index, file_name, major, minor, access_flags, cp, interfaces, fields, methods, attributes);
  }
  





  public void addInterface(String name)
  {
    interface_vec.add(name);
  }
  



  public void removeInterface(String name)
  {
    interface_vec.remove(name);
  }
  

  public int getMajor()
  {
    return major;
  }
  

  public void setMajor(int major)
  {
    this.major = major;
  }
  


  public void setMinor(int minor)
  {
    this.minor = minor;
  }
  

  public int getMinor()
  {
    return minor;
  }
  

  public void addAttribute(Attribute a)
  {
    attribute_vec.add(a);
  }
  

  public void addMethod(Method m)
  {
    method_vec.add(m);
  }
  




  public void addEmptyConstructor(int access_flags)
  {
    InstructionList il = new InstructionList();
    il.append(InstructionConstants.THIS);
    il.append(new INVOKESPECIAL(cp.addMethodref(super_class_name, "<init>", "()V")));
    
    il.append(InstructionConstants.RETURN);
    
    MethodGen mg = new MethodGen(access_flags, Type.VOID, Type.NO_ARGS, null, "<init>", class_name, il, cp);
    
    mg.setMaxStack(1);
    addMethod(mg.getMethod());
  }
  




  public void addField(Field f) { field_vec.add(f); }
  
  public boolean containsField(Field f) { return field_vec.contains(f); }
  

  public Field containsField(String name)
  {
    for (Iterator e = field_vec.iterator(); e.hasNext();) {
      Field f = (Field)e.next();
      if (f.getName().equals(name)) {
        return f;
      }
    }
    return null;
  }
  

  public Method containsMethod(String name, String signature)
  {
    for (Iterator e = method_vec.iterator(); e.hasNext();) {
      Method m = (Method)e.next();
      if ((m.getName().equals(name)) && (m.getSignature().equals(signature))) {
        return m;
      }
    }
    return null;
  }
  


  public void removeAttribute(Attribute a)
  {
    attribute_vec.remove(a);
  }
  

  public void removeMethod(Method m)
  {
    method_vec.remove(m);
  }
  

  public void replaceMethod(Method old, Method new_)
  {
    if (new_ == null) {
      throw new ClassGenException("Replacement method must not be null");
    }
    int i = method_vec.indexOf(old);
    
    if (i < 0) {
      method_vec.add(new_);
    } else {
      method_vec.set(i, new_);
    }
  }
  

  public void replaceField(Field old, Field new_)
  {
    if (new_ == null) {
      throw new ClassGenException("Replacement method must not be null");
    }
    int i = field_vec.indexOf(old);
    
    if (i < 0) {
      field_vec.add(new_);
    } else {
      field_vec.set(i, new_);
    }
  }
  



  public void removeField(Field f) { field_vec.remove(f); }
  
  public String getClassName() { return class_name; }
  public String getSuperclassName() { return super_class_name; }
  public String getFileName() { return file_name; }
  
  public void setClassName(String name) {
    class_name = name.replace('/', '.');
    class_name_index = cp.addClass(name);
  }
  
  public void setSuperclassName(String name) {
    super_class_name = name.replace('/', '.');
    superclass_name_index = cp.addClass(name);
  }
  
  public Method[] getMethods() {
    Method[] methods = new Method[method_vec.size()];
    method_vec.toArray(methods);
    return methods;
  }
  
  public void setMethods(Method[] methods) {
    method_vec.clear();
    for (int m = 0; m < methods.length; m++)
      addMethod(methods[m]);
  }
  
  public void setMethodAt(Method method, int pos) {
    method_vec.set(pos, method);
  }
  
  public Method getMethodAt(int pos) {
    return (Method)method_vec.get(pos);
  }
  
  public String[] getInterfaceNames() {
    int size = interface_vec.size();
    String[] interfaces = new String[size];
    
    interface_vec.toArray(interfaces);
    return interfaces;
  }
  
  public int[] getInterfaces() {
    int size = interface_vec.size();
    int[] interfaces = new int[size];
    
    for (int i = 0; i < size; i++) {
      interfaces[i] = cp.addClass((String)interface_vec.get(i));
    }
    return interfaces;
  }
  
  public Field[] getFields() {
    Field[] fields = new Field[field_vec.size()];
    field_vec.toArray(fields);
    return fields;
  }
  
  public Attribute[] getAttributes() {
    Attribute[] attributes = new Attribute[attribute_vec.size()];
    attribute_vec.toArray(attributes);
    return attributes;
  }
  
  public ConstantPoolGen getConstantPool() { return cp; }
  
  public void setConstantPool(ConstantPoolGen constant_pool) { cp = constant_pool; }
  
  public void setClassNameIndex(int class_name_index)
  {
    this.class_name_index = class_name_index;
    class_name = cp.getConstantPool().getConstantString(class_name_index, (byte)7).replace('/', '.');
  }
  
  public void setSuperclassNameIndex(int superclass_name_index)
  {
    this.superclass_name_index = superclass_name_index;
    super_class_name = cp.getConstantPool().getConstantString(superclass_name_index, (byte)7).replace('/', '.');
  }
  

  public int getSuperclassNameIndex() { return superclass_name_index; }
  
  public int getClassNameIndex() { return class_name_index; }
  



  public void addObserver(ClassObserver o)
  {
    if (observers == null) {
      observers = new ArrayList();
    }
    observers.add(o);
  }
  

  public void removeObserver(ClassObserver o)
  {
    if (observers != null) {
      observers.remove(o);
    }
  }
  


  public void update()
  {
    if (observers != null)
      for (Iterator e = observers.iterator(); e.hasNext();)
        ((ClassObserver)e.next()).notify(this);
  }
  
  public Object clone() {
    try {
      return super.clone();
    } catch (CloneNotSupportedException e) {
      System.err.println(e); }
    return null;
  }
}
