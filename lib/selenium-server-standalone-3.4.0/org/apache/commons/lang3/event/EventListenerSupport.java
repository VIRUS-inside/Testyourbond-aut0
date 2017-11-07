package org.apache.commons.lang3.event;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.commons.lang3.Validate;



























































public class EventListenerSupport<L>
  implements Serializable
{
  private static final long serialVersionUID = 3593265990380473632L;
  private List<L> listeners = new CopyOnWriteArrayList();
  







  private transient L proxy;
  







  private transient L[] prototypeArray;
  








  public static <T> EventListenerSupport<T> create(Class<T> listenerInterface)
  {
    return new EventListenerSupport(listenerInterface);
  }
  











  public EventListenerSupport(Class<L> listenerInterface)
  {
    this(listenerInterface, Thread.currentThread().getContextClassLoader());
  }
  












  public EventListenerSupport(Class<L> listenerInterface, ClassLoader classLoader)
  {
    this();
    Validate.notNull(listenerInterface, "Listener interface cannot be null.", new Object[0]);
    Validate.notNull(classLoader, "ClassLoader cannot be null.", new Object[0]);
    Validate.isTrue(listenerInterface.isInterface(), "Class {0} is not an interface", new Object[] {listenerInterface
      .getName() });
    initializeTransientFields(listenerInterface, classLoader);
  }
  






  private EventListenerSupport() {}
  






  public L fire()
  {
    return proxy;
  }
  











  public void addListener(L listener)
  {
    addListener(listener, true);
  }
  










  public void addListener(L listener, boolean allowDuplicate)
  {
    Validate.notNull(listener, "Listener object cannot be null.", new Object[0]);
    if (allowDuplicate) {
      listeners.add(listener);
    } else if (!listeners.contains(listener)) {
      listeners.add(listener);
    }
  }
  




  int getListenerCount()
  {
    return listeners.size();
  }
  







  public void removeListener(L listener)
  {
    Validate.notNull(listener, "Listener object cannot be null.", new Object[0]);
    listeners.remove(listener);
  }
  





  public L[] getListeners()
  {
    return listeners.toArray(prototypeArray);
  }
  



  private void writeObject(ObjectOutputStream objectOutputStream)
    throws IOException
  {
    ArrayList<L> serializableListeners = new ArrayList();
    

    ObjectOutputStream testObjectOutputStream = new ObjectOutputStream(new ByteArrayOutputStream());
    for (L listener : listeners) {
      try {
        testObjectOutputStream.writeObject(listener);
        serializableListeners.add(listener);
      }
      catch (IOException exception) {
        testObjectOutputStream = new ObjectOutputStream(new ByteArrayOutputStream());
      }
    }
    



    objectOutputStream.writeObject(serializableListeners.toArray(prototypeArray));
  }
  






  private void readObject(ObjectInputStream objectInputStream)
    throws IOException, ClassNotFoundException
  {
    L[] srcListeners = (Object[])objectInputStream.readObject();
    
    listeners = new CopyOnWriteArrayList(srcListeners);
    


    Class<L> listenerInterface = srcListeners.getClass().getComponentType();
    
    initializeTransientFields(listenerInterface, Thread.currentThread().getContextClassLoader());
  }
  






  private void initializeTransientFields(Class<L> listenerInterface, ClassLoader classLoader)
  {
    L[] array = (Object[])Array.newInstance(listenerInterface, 0);
    prototypeArray = array;
    createProxy(listenerInterface, classLoader);
  }
  




  private void createProxy(Class<L> listenerInterface, ClassLoader classLoader)
  {
    proxy = listenerInterface.cast(Proxy.newProxyInstance(classLoader, new Class[] { listenerInterface }, 
      createInvocationHandler()));
  }
  




  protected InvocationHandler createInvocationHandler()
  {
    return new ProxyInvocationHandler();
  }
  






  protected class ProxyInvocationHandler
    implements InvocationHandler
  {
    protected ProxyInvocationHandler() {}
    





    public Object invoke(Object unusedProxy, Method method, Object[] args)
      throws Throwable
    {
      for (L listener : listeners) {
        method.invoke(listener, args);
      }
      return null;
    }
  }
}
