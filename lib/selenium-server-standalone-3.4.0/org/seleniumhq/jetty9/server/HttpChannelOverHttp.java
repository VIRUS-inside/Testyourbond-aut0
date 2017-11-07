package org.seleniumhq.jetty9.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import org.seleniumhq.jetty9.http.BadMessageException;
import org.seleniumhq.jetty9.http.HostPortHttpField;
import org.seleniumhq.jetty9.http.HttpCompliance;
import org.seleniumhq.jetty9.http.HttpField;
import org.seleniumhq.jetty9.http.HttpFields;
import org.seleniumhq.jetty9.http.HttpGenerator;
import org.seleniumhq.jetty9.http.HttpHeader;
import org.seleniumhq.jetty9.http.HttpHeaderValue;
import org.seleniumhq.jetty9.http.HttpMethod;
import org.seleniumhq.jetty9.http.HttpParser;
import org.seleniumhq.jetty9.http.HttpParser.ComplianceHandler;
import org.seleniumhq.jetty9.http.HttpParser.RequestHandler;
import org.seleniumhq.jetty9.http.HttpURI;
import org.seleniumhq.jetty9.http.HttpVersion;
import org.seleniumhq.jetty9.http.MetaData.Request;
import org.seleniumhq.jetty9.http.MetaData.Response;
import org.seleniumhq.jetty9.io.Connection;
import org.seleniumhq.jetty9.io.EndPoint;
import org.seleniumhq.jetty9.util.Trie;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;



















public class HttpChannelOverHttp
  extends HttpChannel
  implements HttpParser.RequestHandler, HttpParser.ComplianceHandler
{
  private static final Logger LOG = Log.getLogger(HttpChannelOverHttp.class);
  private static final HttpField PREAMBLE_UPGRADE_H2C = new HttpField(HttpHeader.UPGRADE, "h2c");
  
  private static final String ATTR_COMPLIANCE_VIOLATIONS = "org.seleniumhq.jetty9.http.compliance.violations";
  private final HttpFields _fields = new HttpFields();
  private final MetaData.Request _metadata = new MetaData.Request(_fields);
  private final HttpConnection _httpConnection;
  private HttpField _connection;
  private HttpField _upgrade = null;
  private boolean _delayedForContent;
  private boolean _unknownExpectation = false;
  private boolean _expect100Continue = false;
  private boolean _expect102Processing = false;
  private List<String> _complianceViolations;
  
  public HttpChannelOverHttp(HttpConnection httpConnection, Connector connector, HttpConfiguration config, EndPoint endPoint, HttpTransport transport)
  {
    super(connector, config, endPoint, transport);
    _httpConnection = httpConnection;
    _metadata.setURI(new HttpURI());
  }
  

  protected HttpInput newHttpInput(HttpChannelState state)
  {
    return new HttpInputOverHTTP(state);
  }
  

  public void recycle()
  {
    super.recycle();
    _unknownExpectation = false;
    _expect100Continue = false;
    _expect102Processing = false;
    _metadata.recycle();
    _connection = null;
    _fields.clear();
    _upgrade = null;
  }
  

  public boolean isExpecting100Continue()
  {
    return _expect100Continue;
  }
  

  public boolean isExpecting102Processing()
  {
    return _expect102Processing;
  }
  

  public boolean startRequest(String method, String uri, HttpVersion version)
  {
    _metadata.setMethod(method);
    _metadata.getURI().parseRequestTarget(method, uri);
    _metadata.setHttpVersion(version);
    _unknownExpectation = false;
    _expect100Continue = false;
    _expect102Processing = false;
    return false;
  }
  

  public void parsedHeader(HttpField field)
  {
    HttpHeader header = field.getHeader();
    String value = field.getValue();
    if (header != null)
    {
      switch (1.$SwitchMap$org$eclipse$jetty$http$HttpHeader[header.ordinal()])
      {
      case 1: 
        _connection = field;
        break;
      
      case 2: 
        if ((!_metadata.getURI().isAbsolute()) && ((field instanceof HostPortHttpField)))
        {
          HostPortHttpField hp = (HostPortHttpField)field;
          _metadata.getURI().setAuthority(hp.getHost(), hp.getPort()); }
        break;
      


      case 3: 
        if (_metadata.getHttpVersion() == HttpVersion.HTTP_1_1)
        {
          HttpHeaderValue expect = (HttpHeaderValue)HttpHeaderValue.CACHE.get(value);
          switch (expect)
          {
          case CONTINUE: 
            _expect100Continue = true;
            break;
          
          case PROCESSING: 
            _expect102Processing = true;
            break;
          
          default: 
            String[] values = field.getValues();
            for (int i = 0; (values != null) && (i < values.length); i++)
            {
              expect = (HttpHeaderValue)HttpHeaderValue.CACHE.get(values[i].trim());
              if (expect == null) {
                _unknownExpectation = true;
              }
              else
                switch (expect)
                {
                case CONTINUE: 
                  _expect100Continue = true;
                  break;
                case PROCESSING: 
                  _expect102Processing = true;
                  break;
                default: 
                  _unknownExpectation = true;
                }
            }
          }
        }
        break;
      


      case 4: 
        _upgrade = field;
        break;
      }
      
    }
    

    _fields.add(field);
  }
  









  public void continue100(int available)
    throws IOException
  {
    if (isExpecting100Continue())
    {
      _expect100Continue = false;
      

      if (available == 0)
      {
        if (getResponse().isCommitted()) {
          throw new IOException("Committed before 100 Continues");
        }
        boolean committed = sendResponse(HttpGenerator.CONTINUE_100_INFO, null, false);
        if (!committed) {
          throw new IOException("Concurrent commit while trying to send 100-Continue");
        }
      }
    }
  }
  
  public void earlyEOF()
  {
    _httpConnection.getGenerator().setPersistent(false);
    
    if (_metadata.getMethod() == null) {
      _httpConnection.close();
    } else if ((onEarlyEOF()) || (_delayedForContent))
    {
      _delayedForContent = false;
      handle();
    }
  }
  

  public boolean content(ByteBuffer content)
  {
    HttpInput.Content c = _httpConnection.newContent(content);
    boolean handle = (onContent(c)) || (_delayedForContent);
    _delayedForContent = false;
    return handle;
  }
  
  public void asyncReadFillInterested()
  {
    _httpConnection.asyncReadFillInterested();
  }
  

  public void badMessage(int status, String reason)
  {
    _httpConnection.getGenerator().setPersistent(false);
    
    try
    {
      onRequest(_metadata);
      getRequest().getHttpInput().earlyEOF();
    }
    catch (Exception e)
    {
      LOG.ignore(e);
    }
    
    onBadMessage(status, reason);
  }
  

  public boolean headerComplete()
  {
    if (_complianceViolations != null) {
      getRequest().setAttribute("org.seleniumhq.jetty9.http.compliance.violations", _complianceViolations);
    }
    
    boolean persistent;
    switch (1.$SwitchMap$org$eclipse$jetty$http$HttpVersion[_metadata.getHttpVersion().ordinal()])
    {

    case 1: 
      persistent = false;
      break;
    case 2: 
      boolean persistent;
      boolean persistent;
      if (getHttpConfiguration().isPersistentConnectionsEnabled()) {
        boolean persistent;
        if (_connection != null) {
          boolean persistent;
          if (_connection.contains(HttpHeaderValue.KEEP_ALIVE.asString())) {
            persistent = true;
          } else {
            persistent = _fields.contains(HttpHeader.CONNECTION, HttpHeaderValue.KEEP_ALIVE.asString());
          }
        } else {
          persistent = false;
        }
      } else {
        persistent = false;
      }
      if (!persistent)
        persistent = HttpMethod.CONNECT.is(_metadata.getMethod());
      if (persistent) {
        getResponse().getHttpFields().add(HttpHeader.CONNECTION, HttpHeaderValue.KEEP_ALIVE);
      }
      


      break;
    case 3: 
      if (_unknownExpectation)
      {
        badMessage(417, null);
        return false; }
      boolean persistent;
      boolean persistent;
      if (getHttpConfiguration().isPersistentConnectionsEnabled()) {
        boolean persistent;
        if (_connection != null) {
          boolean persistent;
          if (_connection.contains(HttpHeaderValue.CLOSE.asString())) {
            persistent = false;
          } else {
            persistent = !_fields.contains(HttpHeader.CONNECTION, HttpHeaderValue.CLOSE.asString());
          }
        } else {
          persistent = true;
        }
      } else {
        persistent = false;
      }
      if (!persistent)
        persistent = HttpMethod.CONNECT.is(_metadata.getMethod());
      if (!persistent) {
        getResponse().getHttpFields().add(HttpHeader.CONNECTION, HttpHeaderValue.CLOSE);
      }
      if ((_upgrade != null) && (upgrade())) {
        return true;
      }
      



      break;
    case 4: 
      _upgrade = PREAMBLE_UPGRADE_H2C;
      
      if ((HttpMethod.PRI.is(_metadata.getMethod())) && 
        ("*".equals(_metadata.getURI().toString())) && 
        (_fields.size() == 0) && 
        (upgrade())) {
        return true;
      }
      badMessage(426, null);
      _httpConnection.getParser().close();
      return false;
    


    default: 
      throw new IllegalStateException("unsupported version " + _metadata.getHttpVersion());
    }
    
    boolean persistent;
    if (!persistent) {
      _httpConnection.getGenerator().setPersistent(false);
    }
    onRequest(_metadata);
    






    _delayedForContent = ((getHttpConfiguration().isDelayDispatchUntilContent()) && ((_httpConnection.getParser().getContentLength() > 0L) || (_httpConnection.getParser().isChunking())) && (!isExpecting100Continue()) && (!isCommitted()) && (_httpConnection.isRequestBufferEmpty()));
    
    return !_delayedForContent;
  }
  










  private boolean upgrade()
    throws BadMessageException
  {
    if (LOG.isDebugEnabled()) {
      LOG.debug("upgrade {} {}", new Object[] { this, _upgrade });
    }
    if ((_upgrade != PREAMBLE_UPGRADE_H2C) && ((_connection == null) || (!_connection.contains("upgrade")))) {
      throw new BadMessageException(400);
    }
    
    ConnectionFactory.Upgrading factory = null;
    for (ConnectionFactory f : getConnector().getConnectionFactories())
    {
      if ((f instanceof ConnectionFactory.Upgrading))
      {
        if (f.getProtocols().contains(_upgrade.getValue()))
        {
          factory = (ConnectionFactory.Upgrading)f;
          break;
        }
      }
    }
    
    if (factory == null)
    {
      if (LOG.isDebugEnabled())
        LOG.debug("No factory for {} in {}", new Object[] { _upgrade, getConnector() });
      return false;
    }
    

    HttpFields response101 = new HttpFields();
    Connection upgrade_connection = factory.upgradeConnection(getConnector(), getEndPoint(), _metadata, response101);
    if (upgrade_connection == null)
    {
      if (LOG.isDebugEnabled())
        LOG.debug("Upgrade ignored for {} by {}", new Object[] { _upgrade, factory });
      return false;
    }
    

    try
    {
      if (_upgrade != PREAMBLE_UPGRADE_H2C) {
        sendResponse(new MetaData.Response(HttpVersion.HTTP_1_1, 101, response101, 0L), null, true);
      }
    }
    catch (IOException e) {
      throw new BadMessageException(500, null, e);
    }
    
    if (LOG.isDebugEnabled())
      LOG.debug("Upgrade from {} to {}", new Object[] { getEndPoint().getConnection(), upgrade_connection });
    getRequest().setAttribute("org.seleniumhq.jetty9.server.HttpConnection.UPGRADE", upgrade_connection);
    getResponse().setStatus(101);
    getHttpTransport().onCompleted();
    return true;
  }
  

  protected void handleException(Throwable x)
  {
    _httpConnection.getGenerator().setPersistent(false);
    super.handleException(x);
  }
  

  public void abort(Throwable failure)
  {
    super.abort(failure);
    _httpConnection.getGenerator().setPersistent(false);
  }
  

  public boolean messageComplete()
  {
    boolean handle = (onRequestComplete()) || (_delayedForContent);
    _delayedForContent = false;
    return handle;
  }
  

  public int getHeaderCacheSize()
  {
    return getHttpConfiguration().getHeaderCacheSize();
  }
  

  public void onComplianceViolation(HttpCompliance compliance, HttpCompliance required, String reason)
  {
    if (_httpConnection.isRecordHttpComplianceViolations())
    {
      if (_complianceViolations == null)
      {
        _complianceViolations = new ArrayList();
      }
      String violation = String.format("%s<%s: %s for %s", new Object[] { compliance, required, reason, getHttpTransport() });
      _complianceViolations.add(violation);
      if (LOG.isDebugEnabled()) {
        LOG.debug(violation, new Object[0]);
      }
    }
  }
}
