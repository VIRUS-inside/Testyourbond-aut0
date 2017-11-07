package org.seleniumhq.jetty9.util.component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.seleniumhq.jetty9.util.annotation.ManagedObject;
import org.seleniumhq.jetty9.util.annotation.ManagedOperation;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;


























































@ManagedObject("Implementation of Container and LifeCycle")
public class ContainerLifeCycle
  extends AbstractLifeCycle
  implements Container, Destroyable, Dumpable
{
  private static final Logger LOG = Log.getLogger(ContainerLifeCycle.class);
  private final List<Bean> _beans = new CopyOnWriteArrayList();
  private final List<Container.Listener> _listeners = new CopyOnWriteArrayList();
  
  private boolean _doStarted;
  private boolean _destroyed;
  
  public ContainerLifeCycle() {}
  
  protected void doStart()
    throws Exception
  {
    if (_destroyed) {
      throw new IllegalStateException("Destroyed container cannot be restarted");
    }
    
    _doStarted = true;
    

    for (Bean b : _beans)
    {
      if ((_bean instanceof LifeCycle))
      {
        LifeCycle l = (LifeCycle)_bean;
        switch (1.$SwitchMap$org$eclipse$jetty$util$component$ContainerLifeCycle$Managed[_managed.ordinal()])
        {
        case 1: 
          if (!l.isRunning())
            start(l);
          break;
        case 2: 
          if (l.isRunning()) {
            unmanage(b);
          }
          else {
            manage(b);
            start(l);
          }
          break;
        }
        
      }
    }
    super.doStart();
  }
  





  protected void start(LifeCycle l)
    throws Exception
  {
    l.start();
  }
  





  protected void stop(LifeCycle l)
    throws Exception
  {
    l.stop();
  }
  



  protected void doStop()
    throws Exception
  {
    _doStarted = false;
    super.doStop();
    List<Bean> reverse = new ArrayList(_beans);
    Collections.reverse(reverse);
    for (Bean b : reverse)
    {
      if ((_managed == Managed.MANAGED) && ((_bean instanceof LifeCycle)))
      {
        LifeCycle l = (LifeCycle)_bean;
        stop(l);
      }
    }
  }
  




  public void destroy()
  {
    _destroyed = true;
    List<Bean> reverse = new ArrayList(_beans);
    Collections.reverse(reverse);
    for (Bean b : reverse)
    {
      if (((_bean instanceof Destroyable)) && ((_managed == Managed.MANAGED) || (_managed == Managed.POJO)))
      {
        Destroyable d = (Destroyable)_bean;
        d.destroy();
      }
    }
    _beans.clear();
  }
  




  public boolean contains(Object bean)
  {
    for (Bean b : _beans)
      if (_bean == bean)
        return true;
    return false;
  }
  




  public boolean isManaged(Object bean)
  {
    for (Bean b : _beans)
      if (_bean == bean)
        return b.isManaged();
    return false;
  }
  












  public boolean addBean(Object o)
  {
    if ((o instanceof LifeCycle))
    {
      LifeCycle l = (LifeCycle)o;
      return addBean(o, l.isRunning() ? Managed.UNMANAGED : Managed.AUTO);
    }
    
    return addBean(o, Managed.POJO);
  }
  







  public boolean addBean(Object o, boolean managed)
  {
    if ((o instanceof LifeCycle))
      return addBean(o, managed ? Managed.MANAGED : Managed.UNMANAGED);
    return addBean(o, managed ? Managed.POJO : Managed.UNMANAGED);
  }
  
  public boolean addBean(Object o, Managed managed)
  {
    if ((o == null) || (contains(o))) {
      return false;
    }
    Bean new_bean = new Bean(o, null);
    

    if ((o instanceof Container.Listener)) {
      addEventListener((Container.Listener)o);
    }
    
    _beans.add(new_bean);
    

    for (Container.Listener l : _listeners) {
      l.beanAdded(this, o);
    }
    try
    {
      switch (1.$SwitchMap$org$eclipse$jetty$util$component$ContainerLifeCycle$Managed[managed.ordinal()])
      {
      case 3: 
        unmanage(new_bean);
        break;
      
      case 1: 
        manage(new_bean);
        
        if ((isStarting()) && (_doStarted))
        {
          LifeCycle l = (LifeCycle)o;
          if (!l.isRunning())
            start(l); }
        break;
      

      case 2: 
        if ((o instanceof LifeCycle))
        {
          LifeCycle l = (LifeCycle)o;
          if (isStarting())
          {
            if (l.isRunning()) {
              unmanage(new_bean);
            } else if (_doStarted)
            {
              manage(new_bean);
              start(l);
            }
            else {
              _managed = Managed.AUTO;
            }
          } else if (isStarted()) {
            unmanage(new_bean);
          } else {
            _managed = Managed.AUTO;
          }
        } else {
          _managed = Managed.POJO; }
        break;
      
      case 4: 
        _managed = Managed.POJO;
      }
    }
    catch (RuntimeException|Error e)
    {
      throw e;
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
    
    if (LOG.isDebugEnabled()) {
      LOG.debug("{} added {}", new Object[] { this, new_bean });
    }
    return true;
  }
  








  public void addManaged(LifeCycle lifecycle)
  {
    addBean(lifecycle, true);
    try
    {
      if ((isRunning()) && (!lifecycle.isRunning())) {
        start(lifecycle);
      }
    }
    catch (RuntimeException|Error e) {
      throw e;
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }
  

  public void addEventListener(Container.Listener listener)
  {
    if (_listeners.contains(listener)) {
      return;
    }
    _listeners.add(listener);
    

    for (Bean b : _beans)
    {
      listener.beanAdded(this, _bean);
      

      if (((listener instanceof Container.InheritedListener)) && (b.isManaged()) && ((_bean instanceof Container)))
      {
        if ((_bean instanceof ContainerLifeCycle)) {
          ((ContainerLifeCycle)_bean).addBean(listener, false);
        } else {
          ((Container)_bean).addBean(listener);
        }
      }
    }
  }
  





  public void manage(Object bean)
  {
    for (Bean b : _beans)
    {
      if (_bean == bean)
      {
        manage(b);
        return;
      }
    }
    throw new IllegalArgumentException("Unknown bean " + bean);
  }
  
  private void manage(Bean bean)
  {
    if (_managed != Managed.MANAGED)
    {
      _managed = Managed.MANAGED;
      
      if ((_bean instanceof Container))
      {
        for (Container.Listener l : _listeners)
        {
          if ((l instanceof Container.InheritedListener))
          {
            if ((_bean instanceof ContainerLifeCycle)) {
              ((ContainerLifeCycle)_bean).addBean(l, false);
            } else {
              ((Container)_bean).addBean(l);
            }
          }
        }
      }
      if ((_bean instanceof AbstractLifeCycle))
      {
        ((AbstractLifeCycle)_bean).setStopTimeout(getStopTimeout());
      }
    }
  }
  






  public void unmanage(Object bean)
  {
    for (Bean b : _beans)
    {
      if (_bean == bean)
      {
        unmanage(b);
        return;
      }
    }
    throw new IllegalArgumentException("Unknown bean " + bean);
  }
  
  private void unmanage(Bean bean)
  {
    if (_managed != Managed.UNMANAGED)
    {
      if ((_managed == Managed.MANAGED) && ((_bean instanceof Container)))
      {
        for (Container.Listener l : _listeners)
        {
          if ((l instanceof Container.InheritedListener))
            ((Container)_bean).removeBean(l);
        }
      }
      _managed = Managed.UNMANAGED;
    }
  }
  

  public Collection<Object> getBeans()
  {
    return getBeans(Object.class);
  }
  
  public void setBeans(Collection<Object> beans)
  {
    for (Object bean : beans) {
      addBean(bean);
    }
  }
  
  public <T> Collection<T> getBeans(Class<T> clazz)
  {
    ArrayList<T> beans = new ArrayList();
    for (Bean b : _beans)
    {
      if (clazz.isInstance(_bean))
        beans.add(clazz.cast(_bean));
    }
    return beans;
  }
  

  public <T> T getBean(Class<T> clazz)
  {
    for (Bean b : _beans)
    {
      if (clazz.isInstance(_bean))
        return clazz.cast(_bean);
    }
    return null;
  }
  



  public void removeBeans()
  {
    ArrayList<Bean> beans = new ArrayList(_beans);
    for (Bean b : beans) {
      remove(b);
    }
  }
  
  private Bean getBean(Object o) {
    for (Bean b : _beans)
    {
      if (_bean == o)
        return b;
    }
    return null;
  }
  

  public boolean removeBean(Object o)
  {
    Bean b = getBean(o);
    return (b != null) && (remove(b));
  }
  
  private boolean remove(Bean bean)
  {
    if (_beans.remove(bean))
    {
      boolean wasManaged = bean.isManaged();
      
      unmanage(bean);
      
      for (Container.Listener l : _listeners) {
        l.beanRemoved(this, _bean);
      }
      if ((_bean instanceof Container.Listener)) {
        removeEventListener((Container.Listener)_bean);
      }
      
      if ((wasManaged) && ((_bean instanceof LifeCycle)))
      {
        try
        {
          stop((LifeCycle)_bean);
        }
        catch (RuntimeException|Error e)
        {
          throw e;
        }
        catch (Exception e)
        {
          throw new RuntimeException(e);
        }
      }
      return true;
    }
    return false;
  }
  

  public void removeEventListener(Container.Listener listener)
  {
    if (_listeners.remove(listener))
    {

      for (Bean b : _beans)
      {
        listener.beanRemoved(this, _bean);
        
        if (((listener instanceof Container.InheritedListener)) && (b.isManaged()) && ((_bean instanceof Container))) {
          ((Container)_bean).removeBean(listener);
        }
      }
    }
  }
  
  public void setStopTimeout(long stopTimeout)
  {
    super.setStopTimeout(stopTimeout);
    for (Bean bean : _beans)
    {
      if ((bean.isManaged()) && ((_bean instanceof AbstractLifeCycle))) {
        ((AbstractLifeCycle)_bean).setStopTimeout(stopTimeout);
      }
    }
  }
  



  @ManagedOperation("Dump the object to stderr")
  public void dumpStdErr()
  {
    try
    {
      dump(System.err, "");
    }
    catch (IOException e)
    {
      LOG.warn(e);
    }
  }
  

  @ManagedOperation("Dump the object to a string")
  public String dump()
  {
    return dump(this);
  }
  
  public static String dump(Dumpable dumpable)
  {
    StringBuilder b = new StringBuilder();
    try
    {
      dumpable.dump(b, "");
    }
    catch (IOException e)
    {
      LOG.warn(e);
    }
    return b.toString();
  }
  
  public void dump(Appendable out) throws IOException
  {
    dump(out, "");
  }
  
  protected void dumpThis(Appendable out) throws IOException
  {
    out.append(String.valueOf(this)).append(" - ").append(getState()).append("\n");
  }
  
  public static void dumpObject(Appendable out, Object o) throws IOException
  {
    try
    {
      if ((o instanceof LifeCycle)) {
        out.append(String.valueOf(o)).append(" - ").append(AbstractLifeCycle.getState((LifeCycle)o)).append("\n");
      } else {
        out.append(String.valueOf(o)).append("\n");
      }
    }
    catch (Throwable th) {
      out.append(" => ").append(th.toString()).append('\n');
    }
  }
  
  public void dump(Appendable out, String indent)
    throws IOException
  {
    dumpBeans(out, indent, new Collection[0]);
  }
  
  protected void dumpBeans(Appendable out, String indent, Collection<?>... collections) throws IOException
  {
    dumpThis(out);
    int size = _beans.size();
    for (c : collections)
      size += c.size();
    if (size == 0)
      return;
    int i = 0;
    for (Object localObject1 = _beans.iterator(); ((Iterator)localObject1).hasNext();) { b = (Bean)((Iterator)localObject1).next();
      
      i++;
      
      switch (1.$SwitchMap$org$eclipse$jetty$util$component$ContainerLifeCycle$Managed[_managed.ordinal()])
      {
      case 4: 
        out.append(indent).append(" +- ");
        if ((_bean instanceof Dumpable)) {
          ((Dumpable)_bean).dump(out, indent + (i == size ? "    " : " |  "));
        } else
          dumpObject(out, _bean);
        break;
      
      case 1: 
        out.append(indent).append(" += ");
        if ((_bean instanceof Dumpable)) {
          ((Dumpable)_bean).dump(out, indent + (i == size ? "    " : " |  "));
        } else
          dumpObject(out, _bean);
        break;
      
      case 3: 
        out.append(indent).append(" +~ ");
        dumpObject(out, _bean);
        break;
      
      case 2: 
        out.append(indent).append(" +? ");
        if ((_bean instanceof Dumpable)) {
          ((Dumpable)_bean).dump(out, indent + (i == size ? "    " : " |  "));
        } else {
          dumpObject(out, _bean);
        }
        break;
      }
    }
    Bean b;
    if (i < size) {
      out.append(indent).append(" |\n");
    }
    localObject1 = collections;Collection<?> localCollection1 = localObject1.length; for (Collection<?> c = 0; c < localCollection1; c++) { Collection<?> c = localObject1[c];
      
      for (Object o : c)
      {
        i++;
        out.append(indent).append(" +> ");
        
        if ((o instanceof Dumpable)) {
          ((Dumpable)o).dump(out, indent + (i == size ? "    " : " |  "));
        } else {
          dumpObject(out, o);
        }
      }
    }
  }
  
  public static void dump(Appendable out, String indent, Collection<?>... collections) throws IOException {
    if (collections.length == 0)
      return;
    int size = 0;
    Collection<?>[] arrayOfCollection1 = collections;int i = arrayOfCollection1.length; for (Collection<?> localCollection1 = 0; localCollection1 < i; localCollection1++) { c = arrayOfCollection1[localCollection1];
      size += c.size(); }
    if (size == 0) {
      return;
    }
    int i = 0;
    Collection<?>[] arrayOfCollection2 = collections;localCollection1 = arrayOfCollection2.length; for (Collection<?> c = 0; c < localCollection1; c++) { Collection<?> c = arrayOfCollection2[c];
      
      for (Object o : c)
      {
        i++;
        out.append(indent).append(" +- ");
        
        if ((o instanceof Dumpable)) {
          ((Dumpable)o).dump(out, indent + (i == size ? "    " : " |  "));
        } else
          dumpObject(out, o);
      }
    }
  }
  
  static enum Managed { POJO,  MANAGED,  UNMANAGED,  AUTO;
    
    private Managed() {} }
  
  private static class Bean { private final Object _bean;
    private volatile ContainerLifeCycle.Managed _managed = ContainerLifeCycle.Managed.POJO;
    
    private Bean(Object b)
    {
      if (b == null)
        throw new NullPointerException();
      _bean = b;
    }
    
    public boolean isManaged()
    {
      return _managed == ContainerLifeCycle.Managed.MANAGED;
    }
    

    public String toString()
    {
      return String.format("{%s,%s}", new Object[] { _bean, _managed });
    }
  }
  
  public void updateBean(Object oldBean, Object newBean)
  {
    if (newBean != oldBean)
    {
      if (oldBean != null)
        removeBean(oldBean);
      if (newBean != null) {
        addBean(newBean);
      }
    }
  }
  
  public void updateBean(Object oldBean, Object newBean, boolean managed) {
    if (newBean != oldBean)
    {
      if (oldBean != null)
        removeBean(oldBean);
      if (newBean != null) {
        addBean(newBean, managed);
      }
    }
  }
  
  public void updateBeans(Object[] oldBeans, Object[] newBeans)
  {
    if (oldBeans != null) {
      label78:
      for (Object o : oldBeans)
      {
        if (newBeans != null)
        {
          for (Object n : newBeans)
            if (o == n)
              break label78;
        }
        removeBean(o);
      }
    }
    

    if (newBeans != null) {
      label162:
      for (Object n : newBeans)
      {
        if (oldBeans != null)
        {
          for (Object o : oldBeans)
            if (o == n)
              break label162;
        }
        addBean(n);
      }
    }
  }
}
