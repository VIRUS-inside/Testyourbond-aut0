package org.apache.bcel.verifier.structurals;

import java.util.AbstractList;
import java.util.ArrayList;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.ReferenceType;
import org.apache.bcel.generic.Type;
import org.apache.bcel.verifier.exc.AssertionViolatedException;
import org.apache.bcel.verifier.exc.StructuralCodeConstraintException;




























































public class OperandStack
{
  private ArrayList stack = new ArrayList();
  

  private int maxStack;
  


  public OperandStack(int maxStack)
  {
    this.maxStack = maxStack;
  }
  



  public OperandStack(int maxStack, ObjectType obj)
  {
    this.maxStack = maxStack;
    push(obj);
  }
  



  protected Object clone()
  {
    OperandStack newstack = new OperandStack(maxStack);
    stack = ((ArrayList)stack.clone());
    return newstack;
  }
  


  public void clear()
  {
    stack = new ArrayList();
  }
  




  public boolean equals(Object o)
  {
    if (!(o instanceof OperandStack)) return false;
    OperandStack s = (OperandStack)o;
    return stack.equals(stack);
  }
  




  public OperandStack getClone()
  {
    return (OperandStack)clone();
  }
  


  public boolean isEmpty()
  {
    return stack.isEmpty();
  }
  


  public int maxStack()
  {
    return maxStack;
  }
  


  public Type peek()
  {
    return peek(0);
  }
  



  public Type peek(int i)
  {
    return (Type)stack.get(size() - i - 1);
  }
  


  public Type pop()
  {
    Type e = (Type)stack.remove(size() - 1);
    return e;
  }
  


  public Type pop(int i)
  {
    for (int j = 0; j < i; j++) {
      pop();
    }
    return null;
  }
  


  public void push(Type type)
  {
    if (type == null) throw new AssertionViolatedException("Cannot push NULL onto OperandStack.");
    if ((type == Type.BOOLEAN) || (type == Type.CHAR) || (type == Type.BYTE) || (type == Type.SHORT)) {
      throw new AssertionViolatedException("The OperandStack does not know about '" + type + "'; use Type.INT instead.");
    }
    if (slotsUsed() >= maxStack) {
      throw new AssertionViolatedException("OperandStack too small, should have thrown proper Exception elsewhere. Stack: " + this);
    }
    stack.add(type);
  }
  


  int size()
  {
    return stack.size();
  }
  







  public int slotsUsed()
  {
    int slots = 0;
    for (int i = 0; i < stack.size(); i++) {
      slots += peek(i).getSize();
    }
    return slots;
  }
  


  public String toString()
  {
    String s = "Slots used: " + slotsUsed() + " MaxStack: " + maxStack + ".\n";
    for (int i = 0; i < size(); i++) {
      s = s + peek(i) + " (Size: " + peek(i).getSize() + ")\n";
    }
    return s;
  }
  




  public void merge(OperandStack s)
  {
    if ((slotsUsed() != s.slotsUsed()) || (size() != s.size())) {
      throw new StructuralCodeConstraintException("Cannot merge stacks of different size:\nOperandStack A:\n" + this + "\nOperandStack B:\n" + s);
    }
    for (int i = 0; i < size(); i++)
    {

      if ((!(stack.get(i) instanceof UninitializedObjectType)) && ((stack.get(i) instanceof UninitializedObjectType))) {
        throw new StructuralCodeConstraintException("Backwards branch with an uninitialized object on the stack detected.");
      }
      

      if ((!stack.get(i).equals(stack.get(i))) && ((stack.get(i) instanceof UninitializedObjectType)) && (!(stack.get(i) instanceof UninitializedObjectType))) {
        throw new StructuralCodeConstraintException("Backwards branch with an uninitialized object on the stack detected.");
      }
      
      if (((stack.get(i) instanceof UninitializedObjectType)) && 
        (!(stack.get(i) instanceof UninitializedObjectType))) {
        stack.set(i, ((UninitializedObjectType)stack.get(i)).getInitialized());
      }
      
      if (!stack.get(i).equals(stack.get(i))) {
        if (((stack.get(i) instanceof ReferenceType)) && ((stack.get(i) instanceof ReferenceType)))
        {
          stack.set(i, ((ReferenceType)stack.get(i)).firstCommonSuperclass((ReferenceType)stack.get(i)));
        }
        else {
          throw new StructuralCodeConstraintException("Cannot merge stacks of different types:\nStack A:\n" + this + "\nStack B:\n" + s);
        }
      }
    }
  }
  



  public void initializeObject(UninitializedObjectType u)
  {
    for (int i = 0; i < stack.size(); i++) {
      if (stack.get(i) == u) {
        stack.set(i, u.getInitialized());
      }
    }
  }
}
