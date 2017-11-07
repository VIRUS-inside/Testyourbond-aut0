package org.seleniumhq.jetty9.server;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ReadPendingException;
import java.nio.channels.WritePendingException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import org.seleniumhq.jetty9.io.AbstractConnection;
import org.seleniumhq.jetty9.io.Connection;
import org.seleniumhq.jetty9.io.EndPoint;
import org.seleniumhq.jetty9.util.AttributesMap;
import org.seleniumhq.jetty9.util.BufferUtil;
import org.seleniumhq.jetty9.util.Callback;
import org.seleniumhq.jetty9.util.TypeUtil;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;




























public class ProxyConnectionFactory
  extends AbstractConnectionFactory
{
  public static final String TLS_VERSION = "TLS_VERSION";
  private static final Logger LOG = Log.getLogger(ProxyConnectionFactory.class);
  private final String _next;
  private int _maxProxyHeader = 1024;
  




  public ProxyConnectionFactory()
  {
    super("proxy");
    _next = null;
  }
  
  public ProxyConnectionFactory(String nextProtocol)
  {
    super("proxy");
    _next = nextProtocol;
  }
  
  public int getMaxProxyHeader()
  {
    return _maxProxyHeader;
  }
  
  public void setMaxProxyHeader(int maxProxyHeader)
  {
    _maxProxyHeader = maxProxyHeader;
  }
  

  public Connection newConnection(Connector connector, EndPoint endp)
  {
    String next = _next;
    Iterator<String> i; if (next == null)
    {
      for (i = connector.getProtocols().iterator(); i.hasNext();)
      {
        String p = (String)i.next();
        if (getProtocol().equalsIgnoreCase(p))
        {
          next = (String)i.next();
          break;
        }
      }
    }
    
    return new ProxyProtocolV1orV2Connection(endp, connector, next);
  }
  
  public class ProxyProtocolV1orV2Connection extends AbstractConnection
  {
    private final Connector _connector;
    private final String _next;
    private ByteBuffer _buffer = BufferUtil.allocate(16);
    
    protected ProxyProtocolV1orV2Connection(EndPoint endp, Connector connector, String next)
    {
      super(connector.getExecutor());
      _connector = connector;
      _next = next;
    }
    

    public void onOpen()
    {
      super.onOpen();
      fillInterested();
    }
    

    public void onFillable()
    {
      try
      {
        while (BufferUtil.space(_buffer) > 0)
        {

          int fill = getEndPoint().fill(_buffer);
          if (fill < 0)
          {
            getEndPoint().shutdownOutput();
            return;
          }
          if (fill == 0)
          {
            fillInterested();
            return;
          }
        }
        

        switch (_buffer.get(0))
        {

        case 80: 
          ProxyConnectionFactory.ProxyProtocolV1Connection v1 = new ProxyConnectionFactory.ProxyProtocolV1Connection(getEndPoint(), _connector, _next, _buffer);
          getEndPoint().upgrade(v1);
          return;
        

        case 13: 
          ProxyConnectionFactory.ProxyProtocolV2Connection v2 = new ProxyConnectionFactory.ProxyProtocolV2Connection(ProxyConnectionFactory.this, getEndPoint(), _connector, _next, _buffer);
          getEndPoint().upgrade(v2);
          return;
        }
        
        ProxyConnectionFactory.LOG.warn("Not PROXY protocol for {}", new Object[] { getEndPoint() });
        close();

      }
      catch (Throwable x)
      {
        ProxyConnectionFactory.LOG.warn("PROXY error for " + getEndPoint(), x);
        close();
      }
    }
  }
  



  public static class ProxyProtocolV1Connection
    extends AbstractConnection
  {
    private final int[] __size = { 29, 23, 21, 13, 5, 3, 1 };
    private final Connector _connector;
    private final String _next;
    private final StringBuilder _builder = new StringBuilder();
    private final String[] _field = new String[6];
    private int _fields;
    private int _length;
    
    protected ProxyProtocolV1Connection(EndPoint endp, Connector connector, String next, ByteBuffer buffer)
    {
      super(connector.getExecutor());
      _connector = connector;
      _next = next;
      _length = buffer.remaining();
      parse(buffer);
    }
    

    public void onOpen()
    {
      super.onOpen();
      fillInterested();
    }
    


    private boolean parse(ByteBuffer buffer)
    {
      while (buffer.hasRemaining())
      {
        byte b = buffer.get();
        if (_fields < 6)
        {
          if ((b == 32) || ((b == 13) && (_fields == 5)))
          {
            _field[(_fields++)] = _builder.toString();
            _builder.setLength(0);
          } else {
            if (b < 32)
            {
              ProxyConnectionFactory.LOG.warn("Bad character {} for {}", new Object[] { Integer.valueOf(b & 0xFF), getEndPoint() });
              close();
              return false;
            }
            

            _builder.append((char)b);
          }
        }
        else
        {
          if (b == 10)
          {
            _fields = 7;
            return true;
          }
          
          ProxyConnectionFactory.LOG.warn("Bad CRLF for {}", new Object[] { getEndPoint() });
          close();
          return false;
        }
      }
      
      return true;
    }
    

    public void onFillable()
    {
      try
      {
        ByteBuffer buffer = null;
        while (_fields < 7)
        {



          int size = Math.max(1, __size[_fields] - _builder.length());
          if ((buffer == null) || (buffer.capacity() != size)) {
            buffer = BufferUtil.allocate(size);
          } else {
            BufferUtil.clear(buffer);
          }
          
          int fill = getEndPoint().fill(buffer);
          if (fill < 0)
          {
            getEndPoint().shutdownOutput();
            return;
          }
          if (fill == 0)
          {
            fillInterested();
            return;
          }
          
          _length += fill;
          if (_length >= 108)
          {
            ProxyConnectionFactory.LOG.warn("PROXY line too long {} for {}", new Object[] { Integer.valueOf(_length), getEndPoint() });
            close();
            return;
          }
          
          if (!parse(buffer)) {
            return;
          }
        }
        
        if (!"PROXY".equals(_field[0]))
        {
          ProxyConnectionFactory.LOG.warn("Not PROXY protocol for {}", new Object[] { getEndPoint() });
          close();
          return;
        }
        

        InetSocketAddress remote = new InetSocketAddress(_field[2], Integer.parseInt(_field[4]));
        InetSocketAddress local = new InetSocketAddress(_field[3], Integer.parseInt(_field[5]));
        

        ConnectionFactory connectionFactory = _connector.getConnectionFactory(_next);
        if (connectionFactory == null)
        {
          ProxyConnectionFactory.LOG.warn("No Next protocol '{}' for {}", new Object[] { _next, getEndPoint() });
          close();
          return;
        }
        
        if (ProxyConnectionFactory.LOG.isDebugEnabled()) {
          ProxyConnectionFactory.LOG.warn("Next protocol '{}' for {} r={} l={}", new Object[] { _next, getEndPoint(), remote, local });
        }
        EndPoint endPoint = new ProxyConnectionFactory.ProxyEndPoint(getEndPoint(), remote, local);
        Connection newConnection = connectionFactory.newConnection(_connector, endPoint);
        endPoint.upgrade(newConnection);
      }
      catch (Throwable x)
      {
        ProxyConnectionFactory.LOG.warn("PROXY error for " + getEndPoint(), x);
        close();
      }
    }
  }
  
  static enum Family {
    UNSPEC,  INET,  INET6,  UNIX;
    private Family() {} } static enum Transport { UNSPEC,  STREAM,  DGRAM;
    private Transport() {} } private static final byte[] MAGIC = { 13, 10, 13, 10, 0, 13, 10, 81, 85, 73, 84, 10 };
  
  public class ProxyProtocolV2Connection
    extends AbstractConnection
  {
    private final Connector _connector;
    private final String _next;
    private final boolean _local;
    private final ProxyConnectionFactory.Family _family;
    private final ProxyConnectionFactory.Transport _transport;
    private final int _length;
    private final ByteBuffer _buffer;
    
    protected ProxyProtocolV2Connection(EndPoint endp, Connector connector, String next, ByteBuffer buffer) throws IOException
    {
      super(connector.getExecutor());
      _connector = connector;
      _next = next;
      
      if (buffer.remaining() != 16) {
        throw new IllegalStateException();
      }
      if (ProxyConnectionFactory.LOG.isDebugEnabled()) {
        ProxyConnectionFactory.LOG.debug("PROXYv2 header {} for {}", new Object[] { BufferUtil.toHexSummary(buffer), this });
      }
      





      for (int i = 0; i < ProxyConnectionFactory.MAGIC.length; i++) {
        if (buffer.get() != ProxyConnectionFactory.MAGIC[i])
          throw new IOException("Bad PROXY protocol v2 signature");
      }
      int versionAndCommand = 0xFF & buffer.get();
      if ((versionAndCommand & 0xF0) != 32)
        throw new IOException("Bad PROXY protocol v2 version");
      _local = ((versionAndCommand & 0xF) == 0);
      
      int transportAndFamily = 0xFF & buffer.get();
      switch (transportAndFamily >> 4) {
      case 0: 
        _family = ProxyConnectionFactory.Family.UNSPEC; break;
      case 1:  _family = ProxyConnectionFactory.Family.INET; break;
      case 2:  _family = ProxyConnectionFactory.Family.INET6; break;
      case 3:  _family = ProxyConnectionFactory.Family.UNIX; break;
      default: 
        throw new IOException("Bad PROXY protocol v2 family");
      }
      
      switch (0xF & transportAndFamily) {
      case 0: 
        _transport = ProxyConnectionFactory.Transport.UNSPEC; break;
      case 1:  _transport = ProxyConnectionFactory.Transport.STREAM; break;
      case 2:  _transport = ProxyConnectionFactory.Transport.DGRAM; break;
      default: 
        throw new IOException("Bad PROXY protocol v2 family");
      }
      
      _length = buffer.getChar();
      
      if ((!_local) && ((_family == ProxyConnectionFactory.Family.UNSPEC) || (_family == ProxyConnectionFactory.Family.UNIX) || (_transport != ProxyConnectionFactory.Transport.STREAM))) {
        throw new IOException(String.format("Unsupported PROXY protocol v2 mode 0x%x,0x%x", new Object[] { Integer.valueOf(versionAndCommand), Integer.valueOf(transportAndFamily) }));
      }
      if (_length > _maxProxyHeader) {
        throw new IOException(String.format("Unsupported PROXY protocol v2 mode 0x%x,0x%x,0x%x", new Object[] { Integer.valueOf(versionAndCommand), Integer.valueOf(transportAndFamily), Integer.valueOf(_length) }));
      }
      _buffer = (_length > 0 ? BufferUtil.allocate(_length) : BufferUtil.EMPTY_BUFFER);
    }
    

    public void onOpen()
    {
      super.onOpen();
      if (_buffer.remaining() == _length) {
        next();
      } else {
        fillInterested();
      }
    }
    
    public void onFillable()
    {
      try
      {
        while (_buffer.remaining() < _length)
        {

          int fill = getEndPoint().fill(_buffer);
          if (fill < 0)
          {
            getEndPoint().shutdownOutput();
            return;
          }
          if (fill == 0)
          {
            fillInterested();
            return;
          }
        }
      }
      catch (Throwable x)
      {
        ProxyConnectionFactory.LOG.warn("PROXY error for " + getEndPoint(), x);
        close();
        return;
      }
      
      next();
    }
    
    private void next()
    {
      if (ProxyConnectionFactory.LOG.isDebugEnabled()) {
        ProxyConnectionFactory.LOG.debug("PROXYv2 next {} from {} for {}", new Object[] { _next, BufferUtil.toHexSummary(_buffer), this });
      }
      
      ConnectionFactory connectionFactory = _connector.getConnectionFactory(_next);
      if (connectionFactory == null)
      {
        ProxyConnectionFactory.LOG.info("Next protocol '{}' for {}", new Object[] { _next, getEndPoint() });
        close();
        return;
      }
      

      EndPoint endPoint = getEndPoint();
      if (!_local) {
        try
        {
          int dp;
          

          int dp;
          

          switch (ProxyConnectionFactory.1.$SwitchMap$org$eclipse$jetty$server$ProxyConnectionFactory$Family[_family.ordinal()])
          {

          case 1: 
            byte[] addr = new byte[4];
            _buffer.get(addr);
            InetAddress src = Inet4Address.getByAddress(addr);
            _buffer.get(addr);
            InetAddress dst = Inet4Address.getByAddress(addr);
            int sp = _buffer.getChar();
            dp = _buffer.getChar();
            
            break;
          


          case 2: 
            byte[] addr = new byte[16];
            _buffer.get(addr);
            InetAddress src = Inet6Address.getByAddress(addr);
            _buffer.get(addr);
            InetAddress dst = Inet6Address.getByAddress(addr);
            int sp = _buffer.getChar();
            dp = _buffer.getChar();
            break;
          

          default: 
            throw new IllegalStateException(); }
          int dp;
          int sp;
          InetAddress dst;
          InetAddress src;
          InetSocketAddress remote = new InetSocketAddress(src, sp);
          InetSocketAddress local = new InetSocketAddress(dst, dp);
          ProxyConnectionFactory.ProxyEndPoint proxyEndPoint = new ProxyConnectionFactory.ProxyEndPoint(endPoint, remote, local);
          endPoint = proxyEndPoint;
          


          while (_buffer.hasRemaining())
          {
            int type = 0xFF & _buffer.get();
            int length = _buffer.getShort();
            byte[] value = new byte[length];
            _buffer.get(value);
            
            if (ProxyConnectionFactory.LOG.isDebugEnabled())
              ProxyConnectionFactory.LOG.debug(String.format("T=%x L=%d V=%s for %s", new Object[] { Integer.valueOf(type), Integer.valueOf(length), TypeUtil.toHexString(value), this }), new Object[0]);
            int i;
            int client;
            int verify; switch (type)
            {
            case 1: 
              break;
            case 2: 
              break;
            
            case 32: 
              i = 0;
              client = 0xFF & value[(i++)];
              verify = (0xFF & value[(i++)]) << 24 + (0xFF & value[(i++)]) << 16 + (0xFF & value[(i++)]) << 8 + (0xFF & value[(i++)]);
            case 33: case 34: case 48:  while (i < value.length)
              {
                int ssl_type = 0xFF & value[(i++)];
                int ssl_length = (0xFF & value[(i++)]) * 256 + (0xFF & value[(i++)]);
                byte[] ssl_val = new byte[ssl_length];
                System.arraycopy(value, i, ssl_val, 0, ssl_length);
                i += ssl_length;
                
                switch (ssl_type)
                {
                case 33: 
                  String version = new String(ssl_val, 0, ssl_length, StandardCharsets.ISO_8859_1);
                  if (client == 1) {
                    proxyEndPoint.setAttribute("TLS_VERSION", version);
                  }
                  
                  break;
                }
                
                continue;
                


                break;
                
                break;
              }
            }
            
          }
          


          if (ProxyConnectionFactory.LOG.isDebugEnabled()) {
            ProxyConnectionFactory.LOG.debug("{} {}", new Object[] { getEndPoint(), proxyEndPoint.toString() });
          }
          
        }
        catch (Exception e)
        {
          ProxyConnectionFactory.LOG.warn(e);
        }
      }
      
      Connection newConnection = connectionFactory.newConnection(_connector, endPoint);
      endPoint.upgrade(newConnection);
    }
  }
  
  public static class ProxyEndPoint
    extends AttributesMap implements EndPoint
  {
    private final EndPoint _endp;
    private final InetSocketAddress _remote;
    private final InetSocketAddress _local;
    
    public ProxyEndPoint(EndPoint endp, InetSocketAddress remote, InetSocketAddress local)
    {
      _endp = endp;
      _remote = remote;
      _local = local;
    }
    

    public boolean isOptimizedForDirectBuffers()
    {
      return _endp.isOptimizedForDirectBuffers();
    }
    
    public InetSocketAddress getLocalAddress()
    {
      return _local;
    }
    
    public InetSocketAddress getRemoteAddress()
    {
      return _remote;
    }
    
    public boolean isOpen()
    {
      return _endp.isOpen();
    }
    
    public long getCreatedTimeStamp()
    {
      return _endp.getCreatedTimeStamp();
    }
    
    public void shutdownOutput()
    {
      _endp.shutdownOutput();
    }
    
    public boolean isOutputShutdown()
    {
      return _endp.isOutputShutdown();
    }
    
    public boolean isInputShutdown()
    {
      return _endp.isInputShutdown();
    }
    
    public void close()
    {
      _endp.close();
    }
    
    public int fill(ByteBuffer buffer) throws IOException
    {
      return _endp.fill(buffer);
    }
    
    public boolean flush(ByteBuffer... buffer) throws IOException
    {
      return _endp.flush(buffer);
    }
    
    public Object getTransport()
    {
      return _endp.getTransport();
    }
    
    public long getIdleTimeout()
    {
      return _endp.getIdleTimeout();
    }
    
    public void setIdleTimeout(long idleTimeout)
    {
      _endp.setIdleTimeout(idleTimeout);
    }
    
    public void fillInterested(Callback callback) throws ReadPendingException
    {
      _endp.fillInterested(callback);
    }
    
    public boolean tryFillInterested(Callback callback)
    {
      return _endp.tryFillInterested(callback);
    }
    

    public boolean isFillInterested()
    {
      return _endp.isFillInterested();
    }
    
    public void write(Callback callback, ByteBuffer... buffers) throws WritePendingException
    {
      _endp.write(callback, buffers);
    }
    
    public Connection getConnection()
    {
      return _endp.getConnection();
    }
    
    public void setConnection(Connection connection)
    {
      _endp.setConnection(connection);
    }
    
    public void onOpen()
    {
      _endp.onOpen();
    }
    
    public void onClose()
    {
      _endp.onClose();
    }
    

    public void upgrade(Connection newConnection)
    {
      _endp.upgrade(newConnection);
    }
  }
}
