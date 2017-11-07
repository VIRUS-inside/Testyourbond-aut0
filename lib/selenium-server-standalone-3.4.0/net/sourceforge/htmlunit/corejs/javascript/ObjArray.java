package net.sourceforge.htmlunit.corejs.javascript;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


public class ObjArray
  implements Serializable
{
  static final long serialVersionUID = 4174889037736658296L;
  private int size;
  private boolean sealed;
  private static final int FIELDS_STORE_SIZE = 5;
  private transient Object f0;
  private transient Object f1;
  private transient Object f2;
  private transient Object f3;
  private transient Object f4;
  private transient Object[] data;
  
  public ObjArray() {}
  
  public final boolean isSealed()
  {
    return sealed;
  }
  
  public final void seal() {
    sealed = true;
  }
  
  public final boolean isEmpty() {
    return size == 0;
  }
  
  public final int size() {
    return size;
  }
  
  public final void setSize(int newSize) {
    if (newSize < 0)
      throw new IllegalArgumentException();
    if (sealed)
      throw onSeledMutation();
    int N = size;
    if (newSize < N) {
      for (int i = newSize; i != N; i++) {
        setImpl(i, null);
      }
    } else if ((newSize > N) && 
      (newSize > 5)) {
      ensureCapacity(newSize);
    }
    
    size = newSize;
  }
  
  public final Object get(int index) {
    if ((0 > index) || (index >= size))
      throw onInvalidIndex(index, size);
    return getImpl(index);
  }
  
  public final void set(int index, Object value) {
    if ((0 > index) || (index >= size))
      throw onInvalidIndex(index, size);
    if (sealed)
      throw onSeledMutation();
    setImpl(index, value);
  }
  
  private Object getImpl(int index) {
    switch (index) {
    case 0: 
      return f0;
    case 1: 
      return f1;
    case 2: 
      return f2;
    case 3: 
      return f3;
    case 4: 
      return f4;
    }
    return data[(index - 5)];
  }
  
  private void setImpl(int index, Object value) {
    switch (index) {
    case 0: 
      f0 = value;
      break;
    case 1: 
      f1 = value;
      break;
    case 2: 
      f2 = value;
      break;
    case 3: 
      f3 = value;
      break;
    case 4: 
      f4 = value;
      break;
    default: 
      data[(index - 5)] = value;
    }
  }
  
  public int indexOf(Object obj)
  {
    int N = size;
    for (int i = 0; i != N; i++) {
      Object current = getImpl(i);
      if ((current == obj) || ((current != null) && (current.equals(obj)))) {
        return i;
      }
    }
    return -1;
  }
  
  public int lastIndexOf(Object obj) {
    for (int i = size; i != 0;) {
      i--;
      Object current = getImpl(i);
      if ((current == obj) || ((current != null) && (current.equals(obj)))) {
        return i;
      }
    }
    return -1;
  }
  
  public final Object peek() {
    int N = size;
    if (N == 0)
      throw onEmptyStackTopRead();
    return getImpl(N - 1);
  }
  
  public final Object pop() {
    if (sealed)
      throw onSeledMutation();
    int N = size;
    N--;
    Object top;
    switch (N) {
    case -1: 
      throw onEmptyStackTopRead();
    case 0: 
      Object top = f0;
      f0 = null;
      break;
    case 1: 
      Object top = f1;
      f1 = null;
      break;
    case 2: 
      Object top = f2;
      f2 = null;
      break;
    case 3: 
      Object top = f3;
      f3 = null;
      break;
    case 4: 
      Object top = f4;
      f4 = null;
      break;
    default: 
      top = data[(N - 5)];
      data[(N - 5)] = null;
    }
    size = N;
    return top;
  }
  
  public final void push(Object value) {
    add(value);
  }
  
  public final void add(Object value) {
    if (sealed)
      throw onSeledMutation();
    int N = size;
    if (N >= 5) {
      ensureCapacity(N + 1);
    }
    size = (N + 1);
    setImpl(N, value);
  }
  
  public final void add(int index, Object value) {
    int N = size;
    if ((0 > index) || (index > N))
      throw onInvalidIndex(index, N + 1);
    if (sealed) {
      throw onSeledMutation();
    }
    switch (index) {
    case 0: 
      if (N == 0) {
        f0 = value;
      }
      else {
        Object tmp = f0;
        f0 = value;
        value = tmp;
      }
      break; case 1:  if (N == 1) {
        f1 = value;
      }
      else {
        Object tmp = f1;
        f1 = value;
        value = tmp;
      }
      break; case 2:  if (N == 2) {
        f2 = value;
      }
      else {
        Object tmp = f2;
        f2 = value;
        value = tmp;
      }
      break; case 3:  if (N == 3) {
        f3 = value;
      }
      else {
        Object tmp = f3;
        f3 = value;
        value = tmp;
      }
      break; case 4:  if (N == 4) {
        f4 = value;
      }
      else {
        Object tmp = f4;
        f4 = value;
        value = tmp;
        
        index = 5;
      }
      break; default:  ensureCapacity(N + 1);
      if (index != N) {
        System.arraycopy(data, index - 5, data, index - 5 + 1, N - index);
      }
      
      data[(index - 5)] = value;
    }
    size = (N + 1);
  }
  
  public final void remove(int index) {
    int N = size;
    if ((0 > index) || (index >= N))
      throw onInvalidIndex(index, N);
    if (sealed)
      throw onSeledMutation();
    N--;
    switch (index) {
    case 0: 
      if (N == 0) {
        f0 = null;
      }
      else
        f0 = f1;
      break;
    case 1:  if (N == 1) {
        f1 = null;
      }
      else
        f1 = f2;
      break;
    case 2:  if (N == 2) {
        f2 = null;
      }
      else
        f2 = f3;
      break;
    case 3:  if (N == 3) {
        f3 = null;
      }
      else
        f3 = f4;
      break;
    case 4:  if (N == 4) {
        f4 = null;
      }
      else {
        f4 = data[0];
        
        index = 5;
      }
      break; default:  if (index != N) {
        System.arraycopy(data, index - 5 + 1, data, index - 5, N - index);
      }
      
      data[(N - 5)] = null;
    }
    size = N;
  }
  
  public final void clear() {
    if (sealed)
      throw onSeledMutation();
    int N = size;
    for (int i = 0; i != N; i++) {
      setImpl(i, null);
    }
    size = 0;
  }
  
  public final Object[] toArray() {
    Object[] array = new Object[size];
    toArray(array, 0);
    return array;
  }
  
  public final void toArray(Object[] array) {
    toArray(array, 0);
  }
  
  public final void toArray(Object[] array, int offset) {
    int N = size;
    switch (N) {
    default: 
      System.arraycopy(data, 0, array, offset + 5, N - 5);
    
    case 5: 
      array[(offset + 4)] = f4;
    case 4: 
      array[(offset + 3)] = f3;
    case 3: 
      array[(offset + 2)] = f2;
    case 2: 
      array[(offset + 1)] = f1;
    case 1: 
      array[(offset + 0)] = f0;
    }
    
  }
  
  private void ensureCapacity(int minimalCapacity)
  {
    int required = minimalCapacity - 5;
    if (required <= 0)
      throw new IllegalArgumentException();
    if (data == null) {
      int alloc = 10;
      if (alloc < required) {
        alloc = required;
      }
      data = new Object[alloc];
    } else {
      int alloc = data.length;
      if (alloc < required) {
        if (alloc <= 5) {
          alloc = 10;
        } else {
          alloc *= 2;
        }
        if (alloc < required) {
          alloc = required;
        }
        Object[] tmp = new Object[alloc];
        if (size > 5) {
          System.arraycopy(data, 0, tmp, 0, size - 5);
        }
        data = tmp;
      }
    }
  }
  
  private static RuntimeException onInvalidIndex(int index, int upperBound)
  {
    String msg = index + " ∉ [0, " + upperBound + ')';
    throw new IndexOutOfBoundsException(msg);
  }
  
  private static RuntimeException onEmptyStackTopRead() {
    throw new RuntimeException("Empty stack");
  }
  
  private static RuntimeException onSeledMutation() {
    throw new IllegalStateException("Attempt to modify sealed array");
  }
  
  private void writeObject(ObjectOutputStream os) throws IOException {
    os.defaultWriteObject();
    int N = size;
    for (int i = 0; i != N; i++) {
      Object obj = getImpl(i);
      os.writeObject(obj);
    }
  }
  
  private void readObject(ObjectInputStream is) throws IOException, ClassNotFoundException
  {
    is.defaultReadObject();
    int N = size;
    if (N > 5) {
      data = new Object[N - 5];
    }
    for (int i = 0; i != N; i++) {
      Object obj = is.readObject();
      setImpl(i, obj);
    }
  }
}
