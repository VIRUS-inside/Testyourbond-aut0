package org.eclipse.jetty.util;

import java.nio.channels.ClosedChannelException;
import org.eclipse.jetty.util.thread.Locker;
import org.eclipse.jetty.util.thread.Locker.Lock;
























































public abstract class IteratingCallback
  implements Callback
{
  private static enum State
  {
    IDLE, 
    





    PROCESSING, 
    



    PENDING, 
    



    CALLED, 
    




    SUCCEEDED, 
    



    FAILED, 
    



    CLOSED;
    




    private State() {}
  }
  



  protected static enum Action
  {
    IDLE, 
    




    SCHEDULED, 
    



    SUCCEEDED;
    
    private Action() {} }
  private Locker _locker = new Locker();
  
  private State _state;
  private boolean _iterate;
  
  protected IteratingCallback()
  {
    _state = State.IDLE;
  }
  
  protected IteratingCallback(boolean needReset)
  {
    _state = (needReset ? State.SUCCEEDED : State.IDLE);
  }
  










  protected abstract Action process()
    throws Throwable;
  










  protected void onCompleteSuccess() {}
  










  protected void onCompleteFailure(Throwable cause) {}
  









  public void iterate()
  {
    boolean process = false;
    


    Locker.Lock lock = _locker.lock();Throwable localThrowable6 = null;
    try {
      switch (1.$SwitchMap$org$eclipse$jetty$util$IteratingCallback$State[_state.ordinal()])
      {




















      case 1: 
      case 2: 
        if (lock != null) if (localThrowable6 != null) try { lock.close(); } catch (Throwable localThrowable) { localThrowable6.addSuppressed(localThrowable); } else lock.close();
        break;
      case 3: 
        _state = State.PROCESSING;
        process = true;
        













        if (lock != null) if (localThrowable6 != null) try { lock.close(); } catch (Throwable localThrowable1) { localThrowable6.addSuppressed(localThrowable1); } else lock.close();
        break;
      case 4: 
        _iterate = true;
        









        if (lock != null) if (localThrowable6 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable6.addSuppressed(localThrowable2); } else lock.close(); break; case 5: case 6:  if (lock != null) if (localThrowable6 != null) try { lock.close(); } catch (Throwable localThrowable3) { localThrowable6.addSuppressed(localThrowable3); } else lock.close();
        break;
      case 7: 
      default: 
        throw new IllegalStateException(toString());
      }
      
    }
    catch (Throwable localThrowable4)
    {
      localThrowable6 = localThrowable4;throw localThrowable4;











    }
    finally
    {











      if (lock != null) if (localThrowable6 != null) try { lock.close(); } catch (Throwable localThrowable5) { localThrowable6.addSuppressed(localThrowable5); } else lock.close();
    }
    if (process) {
      processing();
    }
  }
  


  private void processing()
  {
    boolean on_complete_success = false;
    



    for (;;)
    {
      try
      {
        action = process();
      }
      catch (Throwable x) {
        Action action;
        failed(x);
        break;
      }
      
      Action action;
      Locker.Lock lock = _locker.lock();Throwable localThrowable9 = null;
      try {
        switch (1.$SwitchMap$org$eclipse$jetty$util$IteratingCallback$State[_state.ordinal()])
        {

        case 4: 
          switch (1.$SwitchMap$org$eclipse$jetty$util$IteratingCallback$Action[action.ordinal()])
          {


          case 1: 
            if (_iterate)
            {

              _iterate = false;
              _state = State.PROCESSING;
              






















































              if (lock != null) if (localThrowable9 != null) { try { lock.close(); } catch (Throwable localThrowable1) {} localThrowable9.addSuppressed(localThrowable1); } else { lock.close();
                }
            }
            else
            {
              _state = State.IDLE;
              

















































              if (lock != null) if (localThrowable9 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable9.addSuppressed(localThrowable2); } else lock.close();
            }
            break;
          case 2: 
            _state = State.PENDING;
            










































            if (lock != null) if (localThrowable9 != null) try { lock.close(); } catch (Throwable localThrowable3) { localThrowable9.addSuppressed(localThrowable3); } else lock.close();
            break;
          case 3: 
            _iterate = false;
            _state = State.SUCCEEDED;
            on_complete_success = true;
            

































            if (lock != null) if (localThrowable9 != null) try { lock.close(); } catch (Throwable localThrowable4) { localThrowable9.addSuppressed(localThrowable4); } else lock.close();
            break;
          default: 
            throw new IllegalStateException(String.format("%s[action=%s]", new Object[] { this, action }));
          }
          
          
          break;
        case 2: 
          switch (action)
          {


          case SCHEDULED: 
            _state = State.PROCESSING;
            

















            if (lock != null) if (localThrowable9 != null) { try { lock.close(); } catch (Throwable localThrowable5) {} localThrowable9.addSuppressed(localThrowable5); } else { lock.close();
              }
            break;
          default: 
            throw new IllegalStateException(String.format("%s[action=%s]", new Object[] { this, action }));
          }
          
          






          break;
        case 5: 
        case 6: 
        case 7: 
          if (lock != null) if (localThrowable9 != null) try { lock.close(); } catch (Throwable localThrowable6) { localThrowable9.addSuppressed(localThrowable6); } else lock.close();
          break;
        case 1: 
        case 3: 
        default: 
          throw new IllegalStateException(String.format("%s[action=%s]", new Object[] { this, action }));
        }
        
      }
      catch (Throwable localThrowable7)
      {
        localThrowable9 = localThrowable7;throw localThrowable7;


































      }
      finally
      {

































        if (lock != null) if (localThrowable9 != null) try { lock.close(); } catch (Throwable localThrowable8) { localThrowable9.addSuppressed(localThrowable8); } else lock.close();
      }
    }
    if (on_complete_success) {
      onCompleteSuccess();
    }
  }
  





  public void succeeded()
  {
    boolean process = false;
    Locker.Lock lock = _locker.lock();Throwable localThrowable3 = null;
    try {
      switch (1.$SwitchMap$org$eclipse$jetty$util$IteratingCallback$State[_state.ordinal()])
      {

      case 4: 
        _state = State.CALLED;
        break;
      

      case 1: 
        _state = State.PROCESSING;
        process = true;
        break;
      
      case 5: 
      case 7: 
        break;
      
      case 2: 
      case 3: 
      case 6: 
      default: 
        throw new IllegalStateException(toString());
      }
      
    }
    catch (Throwable localThrowable1)
    {
      localThrowable3 = localThrowable1;throw localThrowable1;











    }
    finally
    {











      if (lock != null) if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else lock.close(); }
    if (process) {
      processing();
    }
  }
  





  public void failed(Throwable x)
  {
    boolean failure = false;
    Locker.Lock lock = _locker.lock();Throwable localThrowable3 = null;
    try {
      switch (1.$SwitchMap$org$eclipse$jetty$util$IteratingCallback$State[_state.ordinal()])
      {
      case 2: 
      case 3: 
      case 5: 
      case 6: 
      case 7: 
        break;
      


      case 1: 
      case 4: 
        _state = State.FAILED;
        failure = true;
        break;
      
      default: 
        throw new IllegalStateException(toString());
      }
    }
    catch (Throwable localThrowable1)
    {
      localThrowable3 = localThrowable1;throw localThrowable1;









    }
    finally
    {









      if (lock != null) if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else lock.close(); }
    if (failure) {
      onCompleteFailure(x);
    }
  }
  
  public void close() {
    boolean failure = false;
    Locker.Lock lock = _locker.lock();Throwable localThrowable3 = null;
    try {
      switch (1.$SwitchMap$org$eclipse$jetty$util$IteratingCallback$State[_state.ordinal()])
      {
      case 3: 
      case 5: 
      case 6: 
        _state = State.CLOSED;
        break;
      case 7: 
        break;
      
      case 4: 
      default: 
        _state = State.CLOSED;
        failure = true;
      }
    }
    catch (Throwable localThrowable1)
    {
      localThrowable3 = localThrowable1;throw localThrowable1;







    }
    finally
    {






      if (lock != null) if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else lock.close();
    }
    if (failure) {
      onCompleteFailure(new ClosedChannelException());
    }
  }
  



  boolean isIdle()
  {
    Locker.Lock lock = _locker.lock();Throwable localThrowable3 = null;
    try {
      return _state == State.IDLE;
    }
    catch (Throwable localThrowable4)
    {
      localThrowable3 = localThrowable4;throw localThrowable4;
    }
    finally {
      if (lock != null) if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else lock.close();
    }
  }
  
  public boolean isClosed() {
    Locker.Lock lock = _locker.lock();Throwable localThrowable3 = null;
    try {
      return _state == State.CLOSED;
    }
    catch (Throwable localThrowable4)
    {
      localThrowable3 = localThrowable4;throw localThrowable4;
    }
    finally {
      if (lock != null) { if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else { lock.close();
        }
      }
    }
  }
  
  public boolean isFailed()
  {
    Locker.Lock lock = _locker.lock();Throwable localThrowable3 = null;
    try {
      return _state == State.FAILED;
    }
    catch (Throwable localThrowable4)
    {
      localThrowable3 = localThrowable4;throw localThrowable4;
    }
    finally {
      if (lock != null) { if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else { lock.close();
        }
      }
    }
  }
  
  public boolean isSucceeded()
  {
    Locker.Lock lock = _locker.lock();Throwable localThrowable3 = null;
    try {
      return _state == State.SUCCEEDED;
    }
    catch (Throwable localThrowable4)
    {
      localThrowable3 = localThrowable4;throw localThrowable4;
    }
    finally {
      if (lock != null) { if (localThrowable3 != null) try { lock.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else { lock.close();
        }
      }
    }
  }
  






  public boolean reset()
  {
    Locker.Lock lock = _locker.lock();Throwable localThrowable5 = null;
    try { boolean bool;
      switch (1.$SwitchMap$org$eclipse$jetty$util$IteratingCallback$State[_state.ordinal()])
      {
      case 3: 
        return true;
      
      case 5: 
      case 6: 
        _iterate = false;
        _state = State.IDLE;
        return true;
      }
      
      return false;
    }
    catch (Throwable localThrowable6)
    {
      localThrowable5 = localThrowable6;throw localThrowable6;






    }
    finally
    {






      if (lock != null) if (localThrowable5 != null) try { lock.close(); } catch (Throwable localThrowable4) { localThrowable5.addSuppressed(localThrowable4); } else lock.close();
    }
  }
  
  public String toString()
  {
    return String.format("%s[%s]", new Object[] { super.toString(), _state });
  }
}
