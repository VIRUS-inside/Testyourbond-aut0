package org.apache.bcel.util;

import java.util.ArrayList;
import org.apache.bcel.classfile.JavaClass;



























































public class ClassVector
{
  public ClassVector() {}
  
  protected ArrayList vec = new ArrayList();
  
  public void addElement(JavaClass clazz) { vec.add(clazz); }
  public JavaClass elementAt(int index) { return (JavaClass)vec.get(index); }
  public void removeElementAt(int index) { vec.remove(index); }
  
  public JavaClass[] toArray() {
    JavaClass[] classes = new JavaClass[vec.size()];
    vec.toArray(classes);
    return classes;
  }
}
