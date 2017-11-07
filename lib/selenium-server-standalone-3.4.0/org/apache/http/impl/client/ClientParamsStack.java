package org.apache.http.impl.client;

import org.apache.http.params.AbstractHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;


















































































@Deprecated
public class ClientParamsStack
  extends AbstractHttpParams
{
  protected final HttpParams applicationParams;
  protected final HttpParams clientParams;
  protected final HttpParams requestParams;
  protected final HttpParams overrideParams;
  
  public ClientParamsStack(HttpParams aparams, HttpParams cparams, HttpParams rparams, HttpParams oparams)
  {
    applicationParams = aparams;
    clientParams = cparams;
    requestParams = rparams;
    overrideParams = oparams;
  }
  







  public ClientParamsStack(ClientParamsStack stack)
  {
    this(stack.getApplicationParams(), stack.getClientParams(), stack.getRequestParams(), stack.getOverrideParams());
  }
  


















  public ClientParamsStack(ClientParamsStack stack, HttpParams aparams, HttpParams cparams, HttpParams rparams, HttpParams oparams)
  {
    this(aparams != null ? aparams : stack.getApplicationParams(), cparams != null ? cparams : stack.getClientParams(), rparams != null ? rparams : stack.getRequestParams(), oparams != null ? oparams : stack.getOverrideParams());
  }
  








  public final HttpParams getApplicationParams()
  {
    return applicationParams;
  }
  




  public final HttpParams getClientParams()
  {
    return clientParams;
  }
  




  public final HttpParams getRequestParams()
  {
    return requestParams;
  }
  




  public final HttpParams getOverrideParams()
  {
    return overrideParams;
  }
  










  public Object getParameter(String name)
  {
    Args.notNull(name, "Parameter name");
    
    Object result = null;
    
    if (overrideParams != null) {
      result = overrideParams.getParameter(name);
    }
    if ((result == null) && (requestParams != null)) {
      result = requestParams.getParameter(name);
    }
    if ((result == null) && (clientParams != null)) {
      result = clientParams.getParameter(name);
    }
    if ((result == null) && (applicationParams != null)) {
      result = applicationParams.getParameter(name);
    }
    return result;
  }
  














  public HttpParams setParameter(String name, Object value)
    throws UnsupportedOperationException
  {
    throw new UnsupportedOperationException("Setting parameters in a stack is not supported.");
  }
  














  public boolean removeParameter(String name)
  {
    throw new UnsupportedOperationException("Removing parameters in a stack is not supported.");
  }
  
















  public HttpParams copy()
  {
    return this;
  }
}
