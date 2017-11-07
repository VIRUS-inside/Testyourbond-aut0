package org.apache.http.impl.client;

import org.apache.http.conn.routing.HttpRoute;









































@Deprecated
public class RoutedRequest
{
  protected final RequestWrapper request;
  protected final HttpRoute route;
  
  public RoutedRequest(RequestWrapper req, HttpRoute route)
  {
    request = req;
    this.route = route;
  }
  
  public final RequestWrapper getRequest() {
    return request;
  }
  
  public final HttpRoute getRoute() {
    return route;
  }
}
