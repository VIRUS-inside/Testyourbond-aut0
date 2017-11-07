package org.apache.bcel.util;

import java.util.ArrayList;
import org.apache.bcel.classfile.JavaClass;



























































public class ClassQueue
{
  public ClassQueue() {}
  
  protected int left = 0;
  private ArrayList vec = new ArrayList();
  
  public void enqueue(JavaClass clazz) { vec.add(clazz); }
  
  public JavaClass dequeue() { JavaClass clazz = (JavaClass)vec.get(left);
    vec.remove(left++);
    return clazz; }
  
  public boolean empty() { return vec.size() <= left; }
}
