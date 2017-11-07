package org.apache.http.params;

import java.util.HashSet;
import java.util.Set;
import org.apache.http.util.Args;













































@Deprecated
public final class DefaultedHttpParams
  extends AbstractHttpParams
{
  private final HttpParams local;
  private final HttpParams defaults;
  
  public DefaultedHttpParams(HttpParams local, HttpParams defaults)
  {
    this.local = ((HttpParams)Args.notNull(local, "Local HTTP parameters"));
    this.defaults = defaults;
  }
  


  public HttpParams copy()
  {
    HttpParams clone = local.copy();
    return new DefaultedHttpParams(clone, defaults);
  }
  




  public Object getParameter(String name)
  {
    Object obj = local.getParameter(name);
    if ((obj == null) && (defaults != null)) {
      obj = defaults.getParameter(name);
    }
    return obj;
  }
  



  public boolean removeParameter(String name)
  {
    return local.removeParameter(name);
  }
  



  public HttpParams setParameter(String name, Object value)
  {
    return local.setParameter(name, value);
  }
  



  public HttpParams getDefaults()
  {
    return defaults;
  }
  











  public Set<String> getNames()
  {
    Set<String> combined = new HashSet(getNames(defaults));
    combined.addAll(getNames(local));
    return combined;
  }
  









  public Set<String> getDefaultNames()
  {
    return new HashSet(getNames(defaults));
  }
  









  public Set<String> getLocalNames()
  {
    return new HashSet(getNames(local));
  }
  
  private Set<String> getNames(HttpParams params)
  {
    if ((params instanceof HttpParamsNames)) {
      return ((HttpParamsNames)params).getNames();
    }
    throw new UnsupportedOperationException("HttpParams instance does not implement HttpParamsNames");
  }
}
