package org.seleniumhq.jetty9.util;

import java.util.concurrent.atomic.AtomicReference;























































public abstract class CompletableCallback
  implements Callback
{
  private final AtomicReference<State> state = new AtomicReference(State.IDLE);
  
  public CompletableCallback() {}
  
  public void succeeded()
  {
    for (;;) {
      State current = (State)state.get();
      switch (1.$SwitchMap$org$eclipse$jetty$util$CompletableCallback$State[current.ordinal()])
      {

      case 1: 
        if (state.compareAndSet(current, State.SUCCEEDED)) {
          return;
        }
        
        break;
      case 2: 
        if (state.compareAndSet(current, State.SUCCEEDED))
        {
          resume();
          return;
        }
        

        break;
      case 3: 
        return;
      

      default: 
        throw new IllegalStateException(current.toString());
      }
      
    }
  }
  

  public void failed(Throwable x)
  {
    for (;;)
    {
      State current = (State)state.get();
      switch (1.$SwitchMap$org$eclipse$jetty$util$CompletableCallback$State[current.ordinal()])
      {

      case 1: 
      case 2: 
        if (state.compareAndSet(current, State.FAILED))
        {
          abort(x);
          return;
        }
        

        break;
      case 3: 
        return;
      

      default: 
        throw new IllegalStateException(current.toString());
      }
      
    }
  }
  






  public abstract void resume();
  





  public abstract void abort(Throwable paramThrowable);
  





  public boolean tryComplete()
  {
    for (;;)
    {
      State current = (State)state.get();
      switch (1.$SwitchMap$org$eclipse$jetty$util$CompletableCallback$State[current.ordinal()])
      {

      case 1: 
        if (state.compareAndSet(current, State.COMPLETED)) {
          return true;
        }
        
        break;
      case 3: 
      case 4: 
        return false;
      
      case 2: 
      default: 
        throw new IllegalStateException(current.toString());
      }
      
    }
  }
  
  private static enum State
  {
    IDLE,  SUCCEEDED,  FAILED,  COMPLETED;
    
    private State() {}
  }
}
