package org.seleniumhq.jetty9.http;

import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import org.seleniumhq.jetty9.util.ArrayTrie;
import org.seleniumhq.jetty9.util.BufferUtil;
import org.seleniumhq.jetty9.util.StringUtil;
import org.seleniumhq.jetty9.util.Trie;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;































public class HttpGenerator
{
  private static final Logger LOG = Log.getLogger(HttpGenerator.class);
  
  public static final boolean __STRICT = Boolean.getBoolean("org.seleniumhq.jetty9.http.HttpGenerator.STRICT");
  
  private static final byte[] __colon_space = { 58, 32 };
  public static final MetaData.Response CONTINUE_100_INFO = new MetaData.Response(HttpVersion.HTTP_1_1, 100, null, null, -1L);
  public static final MetaData.Response PROGRESS_102_INFO = new MetaData.Response(HttpVersion.HTTP_1_1, 102, null, null, -1L);
  public static final MetaData.Response RESPONSE_500_INFO = new MetaData.Response(HttpVersion.HTTP_1_1, 500, null, new HttpFields() {}, 0L);
  public static final int CHUNK_SIZE = 12;
  
  public static enum State {
    START,  COMMITTED,  COMPLETING,  COMPLETING_1XX,  END;
    private State() {} } public static enum Result { NEED_CHUNK,  NEED_INFO,  NEED_HEADER,  FLUSH,  CONTINUE,  SHUTDOWN_OUT,  DONE;
    
    private Result() {}
  }
  
  private State _state = State.START;
  private HttpTokens.EndOfContent _endOfContent = HttpTokens.EndOfContent.UNKNOWN_CONTENT;
  
  private long _contentPrepared = 0L;
  private boolean _noContentResponse = false;
  private Boolean _persistent = null;
  
  private final int _send;
  private static final int SEND_SERVER = 1;
  private static final int SEND_XPOWEREDBY = 2;
  private static final Trie<Boolean> __assumedContentMethods = new ArrayTrie(8);
  






  public static void setJettyVersion(String serverVersion)
  {
    SEND[1] = StringUtil.getBytes("Server: " + serverVersion + "\r\n");
    SEND[2] = StringUtil.getBytes("X-Powered-By: " + serverVersion + "\r\n");
    SEND[3] = StringUtil.getBytes("Server: " + serverVersion + "\r\nX-Powered-By: " + serverVersion + "\r\n");
  }
  



  private boolean _needCRLF = false;
  private static final byte[] LAST_CHUNK;
  private static final byte[] CONTENT_LENGTH_0;
  private static final byte[] CONNECTION_CLOSE;
  
  public HttpGenerator() { this(false, false); }
  


  public HttpGenerator(boolean sendServerVersion, boolean sendXPoweredBy)
  {
    _send = ((sendServerVersion ? 1 : 0) | (sendXPoweredBy ? 2 : 0));
  }
  

  public void reset()
  {
    _state = State.START;
    _endOfContent = HttpTokens.EndOfContent.UNKNOWN_CONTENT;
    _noContentResponse = false;
    _persistent = null;
    _contentPrepared = 0L;
    _needCRLF = false;
  }
  

  @Deprecated
  public boolean getSendServerVersion()
  {
    return (_send & 0x1) != 0;
  }
  

  @Deprecated
  public void setSendServerVersion(boolean sendServerVersion)
  {
    throw new UnsupportedOperationException();
  }
  

  public State getState()
  {
    return _state;
  }
  

  public boolean isState(State state)
  {
    return _state == state;
  }
  

  public boolean isIdle()
  {
    return _state == State.START;
  }
  

  public boolean isEnd()
  {
    return _state == State.END;
  }
  

  public boolean isCommitted()
  {
    return _state.ordinal() >= State.COMMITTED.ordinal();
  }
  

  public boolean isChunking()
  {
    return _endOfContent == HttpTokens.EndOfContent.CHUNKED_CONTENT;
  }
  

  public boolean isNoContent()
  {
    return _noContentResponse;
  }
  

  public void setPersistent(boolean persistent)
  {
    _persistent = Boolean.valueOf(persistent);
  }
  
  private static final byte[] HTTP_1_1_SPACE;
  private static final byte[] TRANSFER_ENCODING_CHUNKED;
  private static final byte[][] SEND;
  private static final PreparedResponse[] __preprepared;
  public boolean isPersistent()
  {
    return Boolean.TRUE.equals(_persistent);
  }
  

  public boolean isWritten()
  {
    return _contentPrepared > 0L;
  }
  

  public long getContentPrepared()
  {
    return _contentPrepared;
  }
  

  public void abort()
  {
    _persistent = Boolean.valueOf(false);
    _state = State.END;
    _endOfContent = null;
  }
  
  public Result generateRequest(MetaData.Request info, ByteBuffer header, ByteBuffer chunk, ByteBuffer content, boolean last)
    throws IOException
  {
    switch (2.$SwitchMap$org$eclipse$jetty$http$HttpGenerator$State[_state.ordinal()])
    {

    case 1: 
      if (info == null) {
        return Result.NEED_INFO;
      }
      if (header == null) {
        return Result.NEED_HEADER;
      }
      
      if (_persistent == null)
      {
        _persistent = Boolean.valueOf(info.getHttpVersion().ordinal() > HttpVersion.HTTP_1_0.ordinal());
        if ((!_persistent.booleanValue()) && (HttpMethod.CONNECT.is(info.getMethod()))) {
          _persistent = Boolean.valueOf(true);
        }
      }
      
      int pos = BufferUtil.flipToFill(header);
      
      try
      {
        generateRequestLine(info, header);
        
        if (info.getHttpVersion() == HttpVersion.HTTP_0_9) {
          throw new BadMessageException(500, "HTTP/0.9 not supported");
        }
        generateHeaders(info, header, content, last);
        
        boolean expect100 = info.getFields().contains(HttpHeader.EXPECT, HttpHeaderValue.CONTINUE.asString());
        int len;
        if (expect100)
        {
          _state = State.COMMITTED;

        }
        else
        {
          len = BufferUtil.length(content);
          if (len > 0)
          {
            _contentPrepared += len;
            if (isChunking())
              prepareChunk(header, len);
          }
          _state = (last ? State.COMPLETING : State.COMMITTED);
        }
        
        return Result.FLUSH;
      }
      catch (BadMessageException e)
      {
        throw e;
      }
      catch (BufferOverflowException e)
      {
        throw new BadMessageException(500, "Request header too large", e);
      }
      catch (Exception e)
      {
        throw new BadMessageException(500, e.getMessage(), e);
      }
      finally
      {
        BufferUtil.flipToFlush(header, pos);
      }
    


    case 2: 
      int len = BufferUtil.length(content);
      
      if (len > 0)
      {

        if (isChunking())
        {

          if (chunk == null)
            return Result.NEED_CHUNK;
          BufferUtil.clearToFill(chunk);
          prepareChunk(chunk, len);
          BufferUtil.flipToFlush(chunk, 0);
        }
        _contentPrepared += len;
      }
      
      if (last) {
        _state = State.COMPLETING;
      }
      return len > 0 ? Result.FLUSH : Result.CONTINUE;
    


    case 3: 
      if (BufferUtil.hasContent(content))
      {
        if (LOG.isDebugEnabled())
          LOG.debug("discarding content in COMPLETING", new Object[0]);
        BufferUtil.clear(content);
      }
      
      if (isChunking())
      {

        if (chunk == null)
          return Result.NEED_CHUNK;
        BufferUtil.clearToFill(chunk);
        prepareChunk(chunk, 0);
        BufferUtil.flipToFlush(chunk, 0);
        _endOfContent = HttpTokens.EndOfContent.UNKNOWN_CONTENT;
        return Result.FLUSH;
      }
      
      _state = State.END;
      return Boolean.TRUE.equals(_persistent) ? Result.DONE : Result.SHUTDOWN_OUT;
    

    case 4: 
      if (BufferUtil.hasContent(content))
      {
        if (LOG.isDebugEnabled())
          LOG.debug("discarding content in COMPLETING", new Object[0]);
        BufferUtil.clear(content);
      }
      return Result.DONE;
    }
    
    throw new IllegalStateException();
  }
  

  public Result generateResponse(MetaData.Response info, ByteBuffer header, ByteBuffer chunk, ByteBuffer content, boolean last)
    throws IOException
  {
    return generateResponse(info, false, header, chunk, content, last);
  }
  
  public Result generateResponse(MetaData.Response info, boolean head, ByteBuffer header, ByteBuffer chunk, ByteBuffer content, boolean last)
    throws IOException
  {
    switch (2.$SwitchMap$org$eclipse$jetty$http$HttpGenerator$State[_state.ordinal()])
    {

    case 1: 
      if (info == null)
        return Result.NEED_INFO;
      HttpVersion version = info.getHttpVersion();
      if (version == null)
        throw new BadMessageException(500, "No version");
      switch (version)
      {
      case HTTP_1_0: 
        if (_persistent == null) {
          _persistent = Boolean.FALSE;
        }
        break;
      case HTTP_1_1: 
        if (_persistent == null) {
          _persistent = Boolean.TRUE;
        }
        break;
      default: 
        _persistent = Boolean.valueOf(false);
        _endOfContent = HttpTokens.EndOfContent.EOF_CONTENT;
        if (BufferUtil.hasContent(content))
          _contentPrepared += content.remaining();
        _state = (last ? State.COMPLETING : State.COMMITTED);
        return Result.FLUSH;
      }
      
      
      if (header == null) {
        return Result.NEED_HEADER;
      }
      
      int pos = BufferUtil.flipToFill(header);
      
      try
      {
        generateResponseLine(info, header);
        

        int status = info.getStatus();
        if ((status >= 100) && (status < 200))
        {
          _noContentResponse = true;
          
          if (status != 101)
          {
            header.put(HttpTokens.CRLF);
            _state = State.COMPLETING_1XX;
            return Result.FLUSH;
          }
        }
        else if ((status == 204) || (status == 304))
        {
          _noContentResponse = true;
        }
        
        generateHeaders(info, header, content, last);
        

        int len = BufferUtil.length(content);
        if (len > 0)
        {
          _contentPrepared += len;
          if ((isChunking()) && (!head))
            prepareChunk(header, len);
        }
        _state = (last ? State.COMPLETING : State.COMMITTED);
      }
      catch (BadMessageException e)
      {
        throw e;
      }
      catch (BufferOverflowException e)
      {
        throw new BadMessageException(500, "Request header too large", e);
      }
      catch (Exception e)
      {
        throw new BadMessageException(500, e.getMessage(), e);
      }
      finally
      {
        BufferUtil.flipToFlush(header, pos);
      }
      
      return Result.FLUSH;
    


    case 2: 
      int len = BufferUtil.length(content);
      

      if (len > 0)
      {
        if (isChunking())
        {
          if (chunk == null)
            return Result.NEED_CHUNK;
          BufferUtil.clearToFill(chunk);
          prepareChunk(chunk, len);
          BufferUtil.flipToFlush(chunk, 0);
        }
        _contentPrepared += len;
      }
      
      if (last)
      {
        _state = State.COMPLETING;
        return len > 0 ? Result.FLUSH : Result.CONTINUE;
      }
      return len > 0 ? Result.FLUSH : Result.DONE;
    



    case 5: 
      reset();
      return Result.DONE;
    


    case 3: 
      if (BufferUtil.hasContent(content))
      {
        if (LOG.isDebugEnabled())
          LOG.debug("discarding content in COMPLETING", new Object[0]);
        BufferUtil.clear(content);
      }
      
      if (isChunking())
      {

        if (chunk == null) {
          return Result.NEED_CHUNK;
        }
        
        BufferUtil.clearToFill(chunk);
        prepareChunk(chunk, 0);
        BufferUtil.flipToFlush(chunk, 0);
        _endOfContent = HttpTokens.EndOfContent.UNKNOWN_CONTENT;
        return Result.FLUSH;
      }
      
      _state = State.END;
      
      return Boolean.TRUE.equals(_persistent) ? Result.DONE : Result.SHUTDOWN_OUT;
    

    case 4: 
      if (BufferUtil.hasContent(content))
      {
        if (LOG.isDebugEnabled())
          LOG.debug("discarding content in COMPLETING", new Object[0]);
        BufferUtil.clear(content);
      }
      return Result.DONE;
    }
    
    throw new IllegalStateException();
  }
  



  private void prepareChunk(ByteBuffer chunk, int remaining)
  {
    if (_needCRLF) {
      BufferUtil.putCRLF(chunk);
    }
    
    if (remaining > 0)
    {
      BufferUtil.putHexInt(chunk, remaining);
      BufferUtil.putCRLF(chunk);
      _needCRLF = true;
    }
    else
    {
      chunk.put(LAST_CHUNK);
      _needCRLF = false;
    }
  }
  

  private void generateRequestLine(MetaData.Request request, ByteBuffer header)
  {
    header.put(StringUtil.getBytes(request.getMethod()));
    header.put((byte)32);
    header.put(StringUtil.getBytes(request.getURIString()));
    header.put((byte)32);
    header.put(request.getHttpVersion().toBytes());
    header.put(HttpTokens.CRLF);
  }
  


  private void generateResponseLine(MetaData.Response response, ByteBuffer header)
  {
    int status = response.getStatus();
    PreparedResponse preprepared = status < __preprepared.length ? __preprepared[status] : null;
    String reason = response.getReason();
    if (preprepared != null)
    {
      if (reason == null) {
        header.put(_responseLine);
      }
      else {
        header.put(_schemeCode);
        header.put(getReasonBytes(reason));
        header.put(HttpTokens.CRLF);
      }
    }
    else
    {
      header.put(HTTP_1_1_SPACE);
      header.put((byte)(48 + status / 100));
      header.put((byte)(48 + status % 100 / 10));
      header.put((byte)(48 + status % 10));
      header.put((byte)32);
      if (reason == null)
      {
        header.put((byte)(48 + status / 100));
        header.put((byte)(48 + status % 100 / 10));
        header.put((byte)(48 + status % 10));
      }
      else {
        header.put(getReasonBytes(reason)); }
      header.put(HttpTokens.CRLF);
    }
  }
  

  private byte[] getReasonBytes(String reason)
  {
    if (reason.length() > 1024)
      reason = reason.substring(0, 1024);
    byte[] _bytes = StringUtil.getBytes(reason);
    
    for (int i = _bytes.length; i-- > 0;)
      if ((_bytes[i] == 13) || (_bytes[i] == 10))
        _bytes[i] = 63;
    return _bytes;
  }
  

  private void generateHeaders(MetaData info, ByteBuffer header, ByteBuffer content, boolean last)
  {
    MetaData.Request request = (info instanceof MetaData.Request) ? (MetaData.Request)info : null;
    MetaData.Response response = (info instanceof MetaData.Response) ? (MetaData.Response)info : null;
    
    if (LOG.isDebugEnabled())
    {
      LOG.debug("generateHeaders {} last={} content={}", new Object[] { info, Boolean.valueOf(last), BufferUtil.toDetailString(content) });
      LOG.debug(info.getFields().toString(), new Object[0]);
    }
    

    int send = _send;
    HttpField transfer_encoding = null;
    boolean http11 = info.getHttpVersion() == HttpVersion.HTTP_1_1;
    boolean close = false;
    boolean chunked = false;
    boolean content_type = false;
    long content_length = info.getContentLength();
    boolean content_length_field = false;
    

    HttpFields fields = info.getFields();
    if (fields != null)
    {
      int n = fields.size();
      for (int f = 0; f < n; f++)
      {
        HttpField field = fields.getField(f);
        String v = field.getValue();
        if ((v != null) && (v.length() != 0))
        {

          HttpHeader h = field.getHeader();
          if (h == null) {
            putTo(field, header);
          }
          else {
            switch (2.$SwitchMap$org$eclipse$jetty$http$HttpHeader[h.ordinal()])
            {
            case 1: 
              if (content_length < 0L) {
                content_length = field.getLongValue();
              } else if (content_length != field.getLongValue())
                throw new BadMessageException(500, String.format("Incorrect Content-Length %d!=%d", new Object[] { Long.valueOf(content_length), Long.valueOf(field.getLongValue()) }));
              content_length_field = true;
              break;
            


            case 2: 
              content_type = true;
              putTo(field, header);
              break;
            


            case 3: 
              if (http11)
              {


                transfer_encoding = field;
                chunked = field.contains(HttpHeaderValue.CHUNKED.asString());
              }
              


              break;
            case 4: 
              putTo(field, header);
              if (field.contains(HttpHeaderValue.CLOSE.asString()))
              {
                close = true;
                _persistent = Boolean.valueOf(false);
              }
              
              if ((!http11) && (field.contains(HttpHeaderValue.KEEP_ALIVE.asString())))
              {
                _persistent = Boolean.valueOf(true);
              }
              


              break;
            case 5: 
              send &= 0xFFFFFFFE;
              putTo(field, header);
              break;
            

            default: 
              putTo(field, header);
            }
            
          }
        }
      }
    }
    if ((last) && (content_length < 0L)) {
      content_length = _contentPrepared + BufferUtil.length(content);
    }
    


    boolean assumed_content_request = (request != null) && (Boolean.TRUE.equals(__assumedContentMethods.get(request.getMethod())));
    boolean assumed_content = (assumed_content_request) || (content_type) || (chunked);
    boolean nocontent_request = (request != null) && (content_length <= 0L) && (!assumed_content);
    

    if ((_noContentResponse) || (nocontent_request))
    {

      _endOfContent = HttpTokens.EndOfContent.NO_CONTENT;
      

      if ((_contentPrepared > 0L) || (content_length > 0L))
      {
        if ((_contentPrepared == 0L) && (last))
        {


          content.clear();
          content_length = 0L;
        }
        else {
          throw new BadMessageException(500, "Content for no content response");
        }
        
      }
    }
    else if ((http11) && (content_length < 0L) && ((_persistent.booleanValue()) || (assumed_content_request)))
    {

      _endOfContent = HttpTokens.EndOfContent.CHUNKED_CONTENT;
      chunked = true;
      

      if (transfer_encoding == null) {
        header.put(TRANSFER_ENCODING_CHUNKED);
      } else if (transfer_encoding.toString().endsWith(HttpHeaderValue.CHUNKED.toString()))
      {
        putTo(transfer_encoding, header);
        transfer_encoding = null;
      }
      else {
        throw new BadMessageException(500, "Bad Transfer-Encoding");
      }
    }
    else if ((content_length >= 0L) && ((request != null) || (_persistent.booleanValue())))
    {

      _endOfContent = HttpTokens.EndOfContent.CONTENT_LENGTH;
      putContentLength(header, content_length);

    }
    else if (response != null)
    {

      _endOfContent = HttpTokens.EndOfContent.EOF_CONTENT;
      _persistent = Boolean.valueOf(false);
      if ((content_length >= 0L) && ((content_length > 0L) || (assumed_content) || (content_length_field))) {
        putContentLength(header, content_length);
      }
      if ((http11) && (!close)) {
        header.put(CONNECTION_CLOSE);
      }
      
    }
    else
    {
      throw new BadMessageException(500, "Unknown content length for request");
    }
    
    if (LOG.isDebugEnabled()) {
      LOG.debug(_endOfContent.toString(), new Object[0]);
    }
    
    if ((transfer_encoding != null) && (!chunked)) {
      putTo(transfer_encoding, header);
    }
    
    int status = response != null ? response.getStatus() : -1;
    if (status > 199) {
      header.put(SEND[send]);
    }
    
    header.put(HttpTokens.CRLF);
  }
  

  private static void putContentLength(ByteBuffer header, long contentLength)
  {
    if (contentLength == 0L) {
      header.put(CONTENT_LENGTH_0);
    }
    else {
      header.put(HttpHeader.CONTENT_LENGTH.getBytesColonSpace());
      BufferUtil.putDecLong(header, contentLength);
      header.put(HttpTokens.CRLF);
    }
  }
  

  public static byte[] getReasonBuffer(int code)
  {
    PreparedResponse status = code < __preprepared.length ? __preprepared[code] : null;
    if (status != null)
      return _reason;
    return null;
  }
  


  public String toString()
  {
    return String.format("%s@%x{s=%s}", new Object[] {
      getClass().getSimpleName(), 
      Integer.valueOf(hashCode()), _state });
  }
  
  static
  {
    __assumedContentMethods.put(HttpMethod.POST.asString(), Boolean.TRUE);
    __assumedContentMethods.put(HttpMethod.PUT.asString(), Boolean.TRUE);
    












































































































































































































































































































































































































































































































































































































































































































































































    LAST_CHUNK = new byte[] { 48, 13, 10, 13, 10 };
    CONTENT_LENGTH_0 = StringUtil.getBytes("Content-Length: 0\r\n");
    CONNECTION_CLOSE = StringUtil.getBytes("Connection: close\r\n");
    HTTP_1_1_SPACE = StringUtil.getBytes(HttpVersion.HTTP_1_1 + " ");
    TRANSFER_ENCODING_CHUNKED = StringUtil.getBytes("Transfer-Encoding: chunked\r\n");
    



    SEND = new byte[][] { new byte[0], StringUtil.getBytes("Server: Jetty(9.x.x)\r\n"), StringUtil.getBytes("X-Powered-By: Jetty(9.x.x)\r\n"), StringUtil.getBytes("Server: Jetty(9.x.x)\r\nX-Powered-By: Jetty(9.x.x)\r\n") };
    











    __preprepared = new PreparedResponse['Ȁ'];
    

    int versionLength = HttpVersion.HTTP_1_1.toString().length();
    
    for (int i = 0; i < __preprepared.length; i++)
    {
      HttpStatus.Code code = HttpStatus.getCode(i);
      if (code != null)
      {
        String reason = code.getMessage();
        byte[] line = new byte[versionLength + 5 + reason.length() + 2];
        HttpVersion.HTTP_1_1.toBuffer().get(line, 0, versionLength);
        line[(versionLength + 0)] = 32;
        line[(versionLength + 1)] = ((byte)(48 + i / 100));
        line[(versionLength + 2)] = ((byte)(48 + i % 100 / 10));
        line[(versionLength + 3)] = ((byte)(48 + i % 10));
        line[(versionLength + 4)] = 32;
        for (int j = 0; j < reason.length(); j++)
          line[(versionLength + 5 + j)] = ((byte)reason.charAt(j));
        line[(versionLength + 5 + reason.length())] = 13;
        line[(versionLength + 6 + reason.length())] = 10;
        
        __preprepared[i] = new PreparedResponse(null);
        __preprepared_schemeCode = Arrays.copyOfRange(line, 0, versionLength + 5);
        __preprepared_reason = Arrays.copyOfRange(line, versionLength + 5, line.length - 2);
        __preprepared_responseLine = line;
      }
    }
  }
  
  private static void putSanitisedName(String s, ByteBuffer buffer) {
    int l = s.length();
    for (int i = 0; i < l; i++)
    {
      char c = s.charAt(i);
      
      if ((c < 0) || (c > 'ÿ') || (c == '\r') || (c == '\n') || (c == ':')) {
        buffer.put((byte)63);
      } else {
        buffer.put((byte)(0xFF & c));
      }
    }
  }
  
  private static void putSanitisedValue(String s, ByteBuffer buffer) {
    int l = s.length();
    for (int i = 0; i < l; i++)
    {
      char c = s.charAt(i);
      
      if ((c < 0) || (c > 'ÿ') || (c == '\r') || (c == '\n')) {
        buffer.put((byte)32);
      } else {
        buffer.put((byte)(0xFF & c));
      }
    }
  }
  
  public static void putTo(HttpField field, ByteBuffer bufferInFillMode) {
    if ((field instanceof PreEncodedHttpField))
    {
      ((PreEncodedHttpField)field).putTo(bufferInFillMode, HttpVersion.HTTP_1_0);
    }
    else
    {
      HttpHeader header = field.getHeader();
      if (header != null)
      {
        bufferInFillMode.put(header.getBytesColonSpace());
        putSanitisedValue(field.getValue(), bufferInFillMode);
      }
      else
      {
        putSanitisedName(field.getName(), bufferInFillMode);
        bufferInFillMode.put(__colon_space);
        putSanitisedValue(field.getValue(), bufferInFillMode);
      }
      
      BufferUtil.putCRLF(bufferInFillMode);
    }
  }
  
  public static void putTo(HttpFields fields, ByteBuffer bufferInFillMode)
  {
    for (HttpField field : fields)
    {
      if (field != null)
        putTo(field, bufferInFillMode);
    }
    BufferUtil.putCRLF(bufferInFillMode);
  }
  
  private static class PreparedResponse
  {
    byte[] _reason;
    byte[] _schemeCode;
    byte[] _responseLine;
    
    private PreparedResponse() {}
  }
}
