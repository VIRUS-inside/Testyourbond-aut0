package org.apache.commons.lang3.concurrent;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.concurrent.atomic.AtomicReference;



























public abstract class AbstractCircuitBreaker<T>
  implements CircuitBreaker<T>
{
  public static final String PROPERTY_NAME = "open";
  protected final AtomicReference<State> state = new AtomicReference(State.CLOSED);
  

  private final PropertyChangeSupport changeSupport;
  


  public AbstractCircuitBreaker()
  {
    changeSupport = new PropertyChangeSupport(this);
  }
  


  public boolean isOpen()
  {
    return isOpen((State)state.get());
  }
  


  public boolean isClosed()
  {
    return !isOpen();
  }
  



  public abstract boolean checkState();
  



  public abstract boolean incrementAndCheckState(T paramT);
  


  public void close()
  {
    changeState(State.CLOSED);
  }
  


  public void open()
  {
    changeState(State.OPEN);
  }
  





  protected static boolean isOpen(State state)
  {
    return state == State.OPEN;
  }
  





  protected void changeState(State newState)
  {
    if (state.compareAndSet(newState.oppositeState(), newState)) {
      changeSupport.firePropertyChange("open", !isOpen(newState), isOpen(newState));
    }
  }
  






  public void addChangeListener(PropertyChangeListener listener)
  {
    changeSupport.addPropertyChangeListener(listener);
  }
  




  public void removeChangeListener(PropertyChangeListener listener)
  {
    changeSupport.removePropertyChangeListener(listener);
  }
  





  protected static abstract enum State
  {
    CLOSED, 
    








    OPEN;
    
    private State() {}
    
    public abstract State oppositeState();
  }
}
