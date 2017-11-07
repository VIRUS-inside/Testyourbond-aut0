package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.NoSuchElementException;






















































@GwtCompatible
public abstract class AbstractIterator<T>
  extends UnmodifiableIterator<T>
{
  private State state = State.NOT_READY;
  private T next;
  protected AbstractIterator() {}
  
  protected abstract T computeNext();
  
  private static enum State {
    READY, 
    

    NOT_READY, 
    

    DONE, 
    

    FAILED;
    


















    private State() {}
  }
  


















  @CanIgnoreReturnValue
  protected final T endOfData()
  {
    state = State.DONE;
    return null;
  }
  
  @CanIgnoreReturnValue
  public final boolean hasNext()
  {
    Preconditions.checkState(state != State.FAILED);
    switch (1.$SwitchMap$com$google$common$collect$AbstractIterator$State[state.ordinal()]) {
    case 1: 
      return false;
    case 2: 
      return true;
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
  
  @CanIgnoreReturnValue
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
  






  public final T peek()
  {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }
    return next;
  }
}
