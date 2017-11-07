package org.apache.bcel.util;

import java.util.Stack;
import org.apache.bcel.classfile.JavaClass;


























































public class ClassStack
{
  public ClassStack() {}
  
  private Stack stack = new Stack();
  
  public void push(JavaClass clazz) { stack.push(clazz); }
  public JavaClass pop() { return (JavaClass)stack.pop(); }
  public JavaClass top() { return (JavaClass)stack.peek(); }
  public boolean empty() { return stack.empty(); }
}
