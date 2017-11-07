package org.seleniumhq.jetty9.server;

import org.seleniumhq.jetty9.http.HttpCompliance;
import org.seleniumhq.jetty9.http.HttpVersion;
import org.seleniumhq.jetty9.io.Connection;
import org.seleniumhq.jetty9.io.EndPoint;
import org.seleniumhq.jetty9.util.annotation.Name;






















public class HttpConnectionFactory
  extends AbstractConnectionFactory
  implements HttpConfiguration.ConnectionFactory
{
  private final HttpConfiguration _config;
  private HttpCompliance _httpCompliance;
  private boolean _recordHttpComplianceViolations = false;
  
  public HttpConnectionFactory()
  {
    this(new HttpConfiguration());
  }
  
  public HttpConnectionFactory(@Name("config") HttpConfiguration config)
  {
    this(config, null);
  }
  
  public HttpConnectionFactory(@Name("config") HttpConfiguration config, @Name("compliance") HttpCompliance compliance)
  {
    super(HttpVersion.HTTP_1_1.asString());
    _config = config;
    _httpCompliance = (compliance == null ? HttpCompliance.RFC7230 : compliance);
    if (config == null)
      throw new IllegalArgumentException("Null HttpConfiguration");
    addBean(_config);
  }
  

  public HttpConfiguration getHttpConfiguration()
  {
    return _config;
  }
  
  public HttpCompliance getHttpCompliance()
  {
    return _httpCompliance;
  }
  
  public boolean isRecordHttpComplianceViolations()
  {
    return _recordHttpComplianceViolations;
  }
  



  public void setHttpCompliance(HttpCompliance httpCompliance)
  {
    _httpCompliance = httpCompliance;
  }
  

  public Connection newConnection(Connector connector, EndPoint endPoint)
  {
    HttpConnection conn = new HttpConnection(_config, connector, endPoint, _httpCompliance, isRecordHttpComplianceViolations());
    return configure(conn, connector, endPoint);
  }
  

  public void setRecordHttpComplianceViolations(boolean recordHttpComplianceViolations)
  {
    _recordHttpComplianceViolations = recordHttpComplianceViolations;
  }
}
