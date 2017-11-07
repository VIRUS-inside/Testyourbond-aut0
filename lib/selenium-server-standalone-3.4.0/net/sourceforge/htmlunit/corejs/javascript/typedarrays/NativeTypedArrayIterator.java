package net.sourceforge.htmlunit.corejs.javascript.typedarrays;

import java.util.ListIterator;
import java.util.NoSuchElementException;







public class NativeTypedArrayIterator<T>
  implements ListIterator<T>
{
  private final NativeTypedArrayView<T> view;
  private int position;
  private int lastPosition = -1;
  
  NativeTypedArrayIterator(NativeTypedArrayView<T> view, int start) {
    this.view = view;
    position = start;
  }
  
  public boolean hasNext()
  {
    return position < view.length;
  }
  
  public boolean hasPrevious()
  {
    return position > 0;
  }
  
  public int nextIndex()
  {
    return position;
  }
  
  public int previousIndex()
  {
    return position - 1;
  }
  
  public T next()
  {
    if (hasNext()) {
      T ret = view.get(position);
      lastPosition = position;
      position += 1;
      return ret;
    }
    throw new NoSuchElementException();
  }
  
  public T previous()
  {
    if (hasPrevious()) {
      position -= 1;
      lastPosition = position;
      return view.get(position);
    }
    throw new NoSuchElementException();
  }
  
  public void set(T t)
  {
    if (lastPosition < 0) {
      throw new IllegalStateException();
    }
    view.js_set(lastPosition, t);
  }
  
  public void remove()
  {
    throw new UnsupportedOperationException();
  }
  
  public void add(T t)
  {
    throw new UnsupportedOperationException();
  }
}
