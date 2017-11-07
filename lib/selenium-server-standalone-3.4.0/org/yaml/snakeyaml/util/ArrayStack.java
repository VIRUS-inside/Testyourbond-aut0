package org.yaml.snakeyaml.util;

import java.util.ArrayList;














public class ArrayStack<T>
{
  private ArrayList<T> stack;
  
  public ArrayStack(int initSize)
  {
    stack = new ArrayList(initSize);
  }
  
  public void push(T obj) {
    stack.add(obj);
  }
  
  public T pop() {
    return stack.remove(stack.size() - 1);
  }
  
  public boolean isEmpty() {
    return stack.isEmpty();
  }
  
  public void clear() {
    stack.clear();
  }
}
