package org.apache.bcel;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashMap;
import org.apache.bcel.classfile.AccessFlags;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.util.ClassPath;
import org.apache.bcel.util.ClassPath.ClassFile;
import org.apache.bcel.util.ClassQueue;
import org.apache.bcel.util.ClassVector;



























































public abstract class Repository
{
  private static ClassPath class_path = new ClassPath();
  private static HashMap classes;
  private static JavaClass OBJECT;
  
  static { clearCache(); }
  

  public static JavaClass lookupClass(String class_name)
  {
    if ((class_name == null) || (class_name.equals(""))) {
      throw new RuntimeException("Invalid class name");
    }
    class_name = class_name.replace('/', '.');
    
    JavaClass clazz = (JavaClass)classes.get(class_name);
    
    if (clazz == null) {
      try {
        InputStream is = class_path.getInputStream(class_name);
        clazz = new ClassParser(is, class_name).parse();
        class_name = clazz.getClassName();
      } catch (IOException e) { return null;
      }
      classes.put(class_name, clazz);
    }
    
    return clazz;
  }
  
  public static ClassPath.ClassFile lookupClassFile(String class_name)
  {
    try
    {
      return class_path.getClassFile(class_name); } catch (IOException e) {}
    return null;
  }
  

  public static void clearCache()
  {
    classes = new HashMap();
    OBJECT = lookupClass("java.lang.Object");
    
    if (OBJECT == null) {
      System.err.println("Warning: java.lang.Object not found on CLASSPATH!");
    } else {
      classes.put("java.lang.Object", OBJECT);
    }
  }
  



  public static JavaClass addClass(JavaClass clazz)
  {
    String name = clazz.getClassName();
    JavaClass cl = (JavaClass)classes.get(name);
    
    if (cl == null) {
      cl = clazz;classes.put(name, clazz);
    }
    return cl;
  }
  


  public static void removeClass(String clazz)
  {
    classes.remove(clazz);
  }
  


  public static void removeClass(JavaClass clazz)
  {
    removeClass(clazz.getClassName());
  }
  
  private static final JavaClass getSuperClass(JavaClass clazz)
  {
    if (clazz == OBJECT) {
      return null;
    }
    return lookupClass(clazz.getSuperclassName());
  }
  



  public static JavaClass[] getSuperClasses(JavaClass clazz)
  {
    ClassVector vec = new ClassVector();
    
    for (clazz = getSuperClass(clazz); clazz != null; clazz = getSuperClass(clazz)) {
      vec.addElement(clazz);
    }
    return vec.toArray();
  }
  



  public static JavaClass[] getSuperClasses(String class_name)
  {
    JavaClass jc = lookupClass(class_name);
    return jc == null ? null : getSuperClasses(jc);
  }
  



  public static JavaClass[] getInterfaces(JavaClass clazz)
  {
    ClassVector vec = new ClassVector();
    ClassQueue queue = new ClassQueue();
    
    queue.enqueue(clazz);
    String[] interfaces;
    int i; for (; !queue.empty(); 
        









        i < interfaces.length)
    {
      clazz = queue.dequeue();
      
      String s = clazz.getSuperclassName();
      interfaces = clazz.getInterfaceNames();
      
      if (clazz.isInterface()) {
        vec.addElement(clazz);
      } else if (!s.equals("java.lang.Object")) {
        queue.enqueue(lookupClass(s));
      }
      i = 0; continue;
      queue.enqueue(lookupClass(interfaces[i]));i++;
    }
    

    return vec.toArray();
  }
  



  public static JavaClass[] getInterfaces(String class_name)
  {
    return getInterfaces(lookupClass(class_name));
  }
  


  public static boolean instanceOf(JavaClass clazz, JavaClass super_class)
  {
    if (clazz == super_class) {
      return true;
    }
    JavaClass[] super_classes = getSuperClasses(clazz);
    
    for (int i = 0; i < super_classes.length; i++) {
      if (super_classes[i] == super_class)
        return true;
    }
    if (super_class.isInterface()) {
      return implementationOf(clazz, super_class);
    }
    return false;
  }
  


  public static boolean instanceOf(String clazz, String super_class)
  {
    return instanceOf(lookupClass(clazz), lookupClass(super_class));
  }
  


  public static boolean instanceOf(JavaClass clazz, String super_class)
  {
    return instanceOf(clazz, lookupClass(super_class));
  }
  


  public static boolean instanceOf(String clazz, JavaClass super_class)
  {
    return instanceOf(lookupClass(clazz), super_class);
  }
  


  public static boolean implementationOf(JavaClass clazz, JavaClass inter)
  {
    if (clazz == inter) {
      return true;
    }
    JavaClass[] super_interfaces = getInterfaces(clazz);
    
    for (int i = 0; i < super_interfaces.length; i++) {
      if (super_interfaces[i] == inter)
        return true;
    }
    return false;
  }
  


  public static boolean implementationOf(String clazz, String inter)
  {
    return implementationOf(lookupClass(clazz), lookupClass(inter));
  }
  


  public static boolean implementationOf(JavaClass clazz, String inter)
  {
    return implementationOf(clazz, lookupClass(inter));
  }
  


  public static boolean implementationOf(String clazz, JavaClass inter)
  {
    return implementationOf(lookupClass(clazz), inter);
  }
  
  public Repository() {}
}
