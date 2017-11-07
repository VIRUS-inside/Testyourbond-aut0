package org.apache.xalan.xsltc.util;

import java.io.PrintStream;






















public final class IntegerArray
  implements Cloneable
{
  private static final int InitialSize = 32;
  private int[] _array;
  private int _size;
  private int _free = 0;
  
  public IntegerArray() {
    this(32);
  }
  
  public IntegerArray(int size) {
    _array = new int[this._size = size];
  }
  
  public IntegerArray(int[] array) {
    this(array.length);
    System.arraycopy(array, 0, _array, 0, this._free = _size);
  }
  
  public void clear() {
    _free = 0;
  }
  
  public Object clone() {
    IntegerArray clone = new IntegerArray(_free > 0 ? _free : 1);
    System.arraycopy(_array, 0, _array, 0, _free);
    _free = _free;
    return clone;
  }
  
  public int[] toIntArray() {
    int[] result = new int[cardinality()];
    System.arraycopy(_array, 0, result, 0, cardinality());
    return result;
  }
  
  public final int at(int index) {
    return _array[index];
  }
  
  public final void set(int index, int value) {
    _array[index] = value;
  }
  
  public int indexOf(int n) {
    for (int i = 0; i < _free; i++) {
      if (n == _array[i]) return i;
    }
    return -1;
  }
  
  public final void add(int value) {
    if (_free == _size) {
      growArray(_size * 2);
    }
    _array[(_free++)] = value;
  }
  


  public void addNew(int value)
  {
    for (int i = 0; i < _free; i++) {
      if (_array[i] == value) return;
    }
    add(value);
  }
  
  public void reverse() {
    int left = 0;
    int right = _free - 1;
    
    while (left < right) {
      int temp = _array[left];
      _array[(left++)] = _array[right];
      _array[(right--)] = temp;
    }
  }
  



  public void merge(IntegerArray other)
  {
    int newSize = _free + _free;
    
    int[] newArray = new int[newSize];
    

    int i = 0;int j = 0;
    for (int k = 0; (i < _free) && (j < _free); k++) {
      int x = _array[i];
      int y = _array[j];
      
      if (x < y) {
        newArray[k] = x;
        i++;
      }
      else if (x > y) {
        newArray[k] = y;
        j++;
      }
      else {
        newArray[k] = x;
        i++;j++;
      }
    }
    

    if (i >= _free) {
      while (j < _free) {
        newArray[(k++)] = _array[(j++)];
      }
    }
    
    while (i < _free) {
      newArray[(k++)] = _array[(i++)];
    }
    


    _array = newArray;
    _free = (this._size = newSize);
  }
  
  public void sort()
  {
    quicksort(_array, 0, _free - 1);
  }
  
  private static void quicksort(int[] array, int p, int r) {
    if (p < r) {
      int q = partition(array, p, r);
      quicksort(array, p, q);
      quicksort(array, q + 1, r);
    }
  }
  
  private static int partition(int[] array, int p, int r) {
    int x = array[(p + r >>> 1)];
    int i = p - 1;int j = r + 1;
    for (;;)
    {
      if (x >= array[(--j)]) {
        while (x > array[(++i)]) {}
        if (i >= j) break;
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
      }
    }
    return j;
  }
  

  private void growArray(int size)
  {
    int[] newArray = new int[this._size = size];
    System.arraycopy(_array, 0, newArray, 0, _free);
    _array = newArray;
  }
  
  public int popLast() {
    return _array[(--_free)];
  }
  
  public int last() {
    return _array[(_free - 1)];
  }
  
  public void setLast(int n) {
    _array[(_free - 1)] = n;
  }
  
  public void pop() {
    _free -= 1;
  }
  
  public void pop(int n) {
    _free -= n;
  }
  
  public final int cardinality() {
    return _free;
  }
  
  public void print(PrintStream out) {
    if (_free > 0) {
      for (int i = 0; i < _free - 1; i++) {
        out.print(_array[i]);
        out.print(' ');
      }
      out.println(_array[(_free - 1)]);
    }
    else {
      out.println("IntegerArray: empty");
    }
  }
}
