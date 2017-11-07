package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.Iterator;
import java.util.NoSuchElementException;























@GwtCompatible
abstract class MultitransformedIterator<F, T>
  implements Iterator<T>
{
  final Iterator<? extends F> backingIterator;
  private Iterator<? extends T> current = Iterators.emptyIterator();
  private Iterator<? extends T> removeFrom;
  
  MultitransformedIterator(Iterator<? extends F> backingIterator) {
    this.backingIterator = ((Iterator)Preconditions.checkNotNull(backingIterator));
  }
  
  abstract Iterator<? extends T> transform(F paramF);
  
  public boolean hasNext()
  {
    Preconditions.checkNotNull(current);
    if (current.hasNext()) {
      return true;
    }
    while (backingIterator.hasNext())
    {
      Preconditions.checkNotNull(this.current = transform(backingIterator.next()));
      if (current.hasNext()) {
        return true;
      }
    }
    return false;
  }
  
  public T next()
  {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }
    removeFrom = current;
    return current.next();
  }
  
  public void remove()
  {
    CollectPreconditions.checkRemove(removeFrom != null);
    removeFrom.remove();
    removeFrom = null;
  }
}
