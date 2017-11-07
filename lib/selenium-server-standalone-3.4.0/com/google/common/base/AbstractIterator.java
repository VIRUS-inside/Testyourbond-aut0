package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Iterator;
import java.util.NoSuchElementException;
import javax.annotation.Nullable;



















@GwtCompatible
abstract class AbstractIterator<T>
  implements Iterator<T>
{
  private State state = State.NOT_READY;
  private T next;
  protected AbstractIterator() {}
  
  private static enum State {
    READY, 
    NOT_READY, 
    DONE, 
    FAILED;
    
    private State() {}
  }
  
  protected abstract T computeNext();
  
  @Nullable
  @CanIgnoreReturnValue
  protected final T endOfData() {
    state = State.DONE;
    return null;
  }
  
  public final boolean hasNext()
  {
    Preconditions.checkState(state != State.FAILED);
    switch (1.$SwitchMap$com$google$common$base$AbstractIterator$State[state.ordinal()]) {
    case 1: 
      return true;
    case 2: 
      return false;
    }
    
    return tryToComputeNext();
  }
  
  private boolean tryToComputeNext() {
    state = State.FAILED;
    next = computeNext();
    if (state != State.DONE) {
      state = State.READY;
      return true;
    }
    return false;
  }
  
  public final T next()
  {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }
    state = State.NOT_READY;
    T result = next;
    next = null;
    return result;
  }
  
  public final void remove()
  {
    throw new UnsupportedOperationException();
  }
}
