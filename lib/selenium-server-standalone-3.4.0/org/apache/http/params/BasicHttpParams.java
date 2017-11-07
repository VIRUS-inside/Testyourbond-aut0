package org.apache.http.params;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;







































@Deprecated
@Contract(threading=ThreadingBehavior.SAFE)
public class BasicHttpParams
  extends AbstractHttpParams
  implements Serializable, Cloneable
{
  private static final long serialVersionUID = -7086398485908701455L;
  private final Map<String, Object> parameters = new ConcurrentHashMap();
  

  public BasicHttpParams() {}
  

  public Object getParameter(String name)
  {
    return parameters.get(name);
  }
  
  public HttpParams setParameter(String name, Object value)
  {
    if (name == null) {
      return this;
    }
    if (value != null) {
      parameters.put(name, value);
    } else {
      parameters.remove(name);
    }
    return this;
  }
  

  public boolean removeParameter(String name)
  {
    if (parameters.containsKey(name)) {
      parameters.remove(name);
      return true;
    }
    return false;
  }
  






  public void setParameters(String[] names, Object value)
  {
    for (String name : names) {
      setParameter(name, value);
    }
  }
  










  public boolean isParameterSet(String name)
  {
    return getParameter(name) != null;
  }
  









  public boolean isParameterSetLocally(String name)
  {
    return parameters.get(name) != null;
  }
  


  public void clear()
  {
    parameters.clear();
  }
  








  public HttpParams copy()
  {
    try
    {
      return (HttpParams)clone();
    } catch (CloneNotSupportedException ex) {
      throw new UnsupportedOperationException("Cloning not supported");
    }
  }
  



  public Object clone()
    throws CloneNotSupportedException
  {
    BasicHttpParams clone = (BasicHttpParams)super.clone();
    copyParams(clone);
    return clone;
  }
  






  public void copyParams(HttpParams target)
  {
    for (Map.Entry<String, Object> me : parameters.entrySet()) {
      target.setParameter((String)me.getKey(), me.getValue());
    }
  }
  









  public Set<String> getNames()
  {
    return new HashSet(parameters.keySet());
  }
}
