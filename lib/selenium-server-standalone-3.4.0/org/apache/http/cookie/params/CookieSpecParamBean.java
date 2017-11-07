package org.apache.http.cookie.params;

import java.util.Collection;
import org.apache.http.params.HttpAbstractParamBean;
import org.apache.http.params.HttpParams;





































@Deprecated
public class CookieSpecParamBean
  extends HttpAbstractParamBean
{
  public CookieSpecParamBean(HttpParams params)
  {
    super(params);
  }
  
  public void setDatePatterns(Collection<String> patterns) {
    params.setParameter("http.protocol.cookie-datepatterns", patterns);
  }
  
  public void setSingleHeader(boolean singleHeader) {
    params.setBooleanParameter("http.protocol.single-cookie-header", singleHeader);
  }
}
