package org.apache.http.protocol;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.util.Args;






































@Deprecated
public final class BasicHttpProcessor
  implements HttpProcessor, HttpRequestInterceptorList, HttpResponseInterceptorList, Cloneable
{
  public BasicHttpProcessor() {}
  
  protected final List<HttpRequestInterceptor> requestInterceptors = new ArrayList();
  protected final List<HttpResponseInterceptor> responseInterceptors = new ArrayList();
  
  public void addRequestInterceptor(HttpRequestInterceptor itcp) {
    if (itcp == null) {
      return;
    }
    requestInterceptors.add(itcp);
  }
  
  public void addRequestInterceptor(HttpRequestInterceptor itcp, int index)
  {
    if (itcp == null) {
      return;
    }
    requestInterceptors.add(index, itcp);
  }
  
  public void addResponseInterceptor(HttpResponseInterceptor itcp, int index)
  {
    if (itcp == null) {
      return;
    }
    responseInterceptors.add(index, itcp);
  }
  
  public void removeRequestInterceptorByClass(Class<? extends HttpRequestInterceptor> clazz) {
    Iterator<HttpRequestInterceptor> it = requestInterceptors.iterator();
    while (it.hasNext()) {
      Object request = it.next();
      if (request.getClass().equals(clazz)) {
        it.remove();
      }
    }
  }
  
  public void removeResponseInterceptorByClass(Class<? extends HttpResponseInterceptor> clazz) {
    Iterator<HttpResponseInterceptor> it = responseInterceptors.iterator();
    while (it.hasNext()) {
      Object request = it.next();
      if (request.getClass().equals(clazz)) {
        it.remove();
      }
    }
  }
  
  public final void addInterceptor(HttpRequestInterceptor interceptor) {
    addRequestInterceptor(interceptor);
  }
  
  public final void addInterceptor(HttpRequestInterceptor interceptor, int index) {
    addRequestInterceptor(interceptor, index);
  }
  
  public int getRequestInterceptorCount() {
    return requestInterceptors.size();
  }
  
  public HttpRequestInterceptor getRequestInterceptor(int index) {
    if ((index < 0) || (index >= requestInterceptors.size())) {
      return null;
    }
    return (HttpRequestInterceptor)requestInterceptors.get(index);
  }
  
  public void clearRequestInterceptors() {
    requestInterceptors.clear();
  }
  
  public void addResponseInterceptor(HttpResponseInterceptor itcp) {
    if (itcp == null) {
      return;
    }
    responseInterceptors.add(itcp);
  }
  
  public final void addInterceptor(HttpResponseInterceptor interceptor) {
    addResponseInterceptor(interceptor);
  }
  
  public final void addInterceptor(HttpResponseInterceptor interceptor, int index) {
    addResponseInterceptor(interceptor, index);
  }
  
  public int getResponseInterceptorCount() {
    return responseInterceptors.size();
  }
  
  public HttpResponseInterceptor getResponseInterceptor(int index) {
    if ((index < 0) || (index >= responseInterceptors.size())) {
      return null;
    }
    return (HttpResponseInterceptor)responseInterceptors.get(index);
  }
  
  public void clearResponseInterceptors() {
    responseInterceptors.clear();
  }
  
















  public void setInterceptors(List<?> list)
  {
    Args.notNull(list, "Inteceptor list");
    requestInterceptors.clear();
    responseInterceptors.clear();
    for (Object obj : list) {
      if ((obj instanceof HttpRequestInterceptor)) {
        addInterceptor((HttpRequestInterceptor)obj);
      }
      if ((obj instanceof HttpResponseInterceptor)) {
        addInterceptor((HttpResponseInterceptor)obj);
      }
    }
  }
  


  public void clearInterceptors()
  {
    clearRequestInterceptors();
    clearResponseInterceptors();
  }
  

  public void process(HttpRequest request, HttpContext context)
    throws IOException, HttpException
  {
    for (HttpRequestInterceptor interceptor : requestInterceptors) {
      interceptor.process(request, context);
    }
  }
  

  public void process(HttpResponse response, HttpContext context)
    throws IOException, HttpException
  {
    for (HttpResponseInterceptor interceptor : responseInterceptors) {
      interceptor.process(response, context);
    }
  }
  





  protected void copyInterceptors(BasicHttpProcessor target)
  {
    requestInterceptors.clear();
    requestInterceptors.addAll(requestInterceptors);
    responseInterceptors.clear();
    responseInterceptors.addAll(responseInterceptors);
  }
  




  public BasicHttpProcessor copy()
  {
    BasicHttpProcessor clone = new BasicHttpProcessor();
    copyInterceptors(clone);
    return clone;
  }
  
  public Object clone() throws CloneNotSupportedException
  {
    BasicHttpProcessor clone = (BasicHttpProcessor)super.clone();
    copyInterceptors(clone);
    return clone;
  }
}
