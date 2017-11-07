package org.seleniumhq.jetty9.http;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Locale;
import org.seleniumhq.jetty9.util.ArrayTernaryTrie;
import org.seleniumhq.jetty9.util.ArrayTrie;
import org.seleniumhq.jetty9.util.BufferUtil;
import org.seleniumhq.jetty9.util.Trie;
import org.seleniumhq.jetty9.util.TypeUtil;
import org.seleniumhq.jetty9.util.Utf8StringBuilder;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;










































































public class HttpParser
{
  public static final Logger LOG = Log.getLogger(HttpParser.class);
  




  @Deprecated
  public static final String __STRICT = "org.seleniumhq.jetty9.http.HttpParser.STRICT";
  



  public static final int INITIAL_URI_LENGTH = 256;
  



  public static final Trie<HttpField> CACHE = new ArrayTrie(2048);
  

  public static enum State
  {
    START, 
    METHOD, 
    RESPONSE_VERSION, 
    SPACE1, 
    STATUS, 
    URI, 
    SPACE2, 
    REQUEST_VERSION, 
    REASON, 
    PROXY, 
    HEADER, 
    HEADER_IN_NAME, 
    HEADER_VALUE, 
    HEADER_IN_VALUE, 
    CONTENT, 
    EOF_CONTENT, 
    CHUNKED_CONTENT, 
    CHUNK_SIZE, 
    CHUNK_PARAMS, 
    CHUNK, 
    CHUNK_TRAILER, 
    CHUNK_END, 
    END, 
    CLOSE, 
    CLOSED;
    
    private State() {} }
  private static final EnumSet<State> __idleStates = EnumSet.of(State.START, State.END, State.CLOSE, State.CLOSED);
  private static final EnumSet<State> __completeStates = EnumSet.of(State.END, State.CLOSE, State.CLOSED);
  
  private final boolean DEBUG = LOG.isDebugEnabled();
  
  private final HttpHandler _handler;
  
  private final RequestHandler _requestHandler;
  private final ResponseHandler _responseHandler;
  private final ComplianceHandler _complianceHandler;
  private final int _maxHeaderBytes;
  private final HttpCompliance _compliance;
  private HttpField _field;
  private HttpHeader _header;
  private String _headerString;
  private HttpHeaderValue _value;
  private String _valueString;
  private int _responseStatus;
  private int _headerBytes;
  private boolean _host;
  private boolean _headerComplete;
  private volatile State _state = State.START;
  private volatile boolean _eof;
  private HttpMethod _method;
  private String _methodString;
  private HttpVersion _version;
  private Utf8StringBuilder _uri = new Utf8StringBuilder(256);
  
  private HttpTokens.EndOfContent _endOfContent;
  private long _contentLength;
  private long _contentPosition;
  private int _chunkLength;
  private int _chunkPosition;
  private boolean _headResponse;
  private boolean _cr;
  private ByteBuffer _contentChunk;
  private Trie<HttpField> _connectionFields;
  private int _length;
  private final StringBuilder _string = new StringBuilder();
  
























  private static final CharState[] __charState;
  
























  private static HttpCompliance compliance()
  {
    Boolean strict = Boolean.valueOf(Boolean.getBoolean("org.seleniumhq.jetty9.http.HttpParser.STRICT"));
    return strict.booleanValue() ? HttpCompliance.LEGACY : HttpCompliance.RFC7230;
  }
  

  public HttpParser(RequestHandler handler)
  {
    this(handler, -1, compliance());
  }
  

  public HttpParser(ResponseHandler handler)
  {
    this(handler, -1, compliance());
  }
  

  public HttpParser(RequestHandler handler, int maxHeaderBytes)
  {
    this(handler, maxHeaderBytes, compliance());
  }
  

  public HttpParser(ResponseHandler handler, int maxHeaderBytes)
  {
    this(handler, maxHeaderBytes, compliance());
  }
  

  @Deprecated
  public HttpParser(RequestHandler handler, int maxHeaderBytes, boolean strict)
  {
    this(handler, maxHeaderBytes, strict ? HttpCompliance.LEGACY : compliance());
  }
  

  @Deprecated
  public HttpParser(ResponseHandler handler, int maxHeaderBytes, boolean strict)
  {
    this(handler, maxHeaderBytes, strict ? HttpCompliance.LEGACY : compliance());
  }
  

  public HttpParser(RequestHandler handler, HttpCompliance compliance)
  {
    this(handler, -1, compliance);
  }
  

  public HttpParser(RequestHandler handler, int maxHeaderBytes, HttpCompliance compliance)
  {
    _handler = handler;
    _requestHandler = handler;
    _responseHandler = null;
    _maxHeaderBytes = maxHeaderBytes;
    _compliance = (compliance == null ? compliance() : compliance);
    _complianceHandler = ((ComplianceHandler)((handler instanceof ComplianceHandler) ? handler : null));
  }
  

  public HttpParser(ResponseHandler handler, int maxHeaderBytes, HttpCompliance compliance)
  {
    _handler = handler;
    _requestHandler = null;
    _responseHandler = handler;
    _maxHeaderBytes = maxHeaderBytes;
    _compliance = (compliance == null ? compliance() : compliance);
    _complianceHandler = ((ComplianceHandler)((handler instanceof ComplianceHandler) ? handler : null));
  }
  

  public HttpHandler getHandler()
  {
    return _handler;
  }
  






  protected boolean complianceViolation(HttpCompliance compliance, String reason)
  {
    if (_complianceHandler == null)
      return _compliance.ordinal() >= compliance.ordinal();
    if (_compliance.ordinal() < compliance.ordinal())
    {
      _complianceHandler.onComplianceViolation(_compliance, compliance, reason);
      return false;
    }
    return true;
  }
  

  protected String legacyString(String orig, String cached)
  {
    return (_compliance != HttpCompliance.LEGACY) || (orig.equals(cached)) || (complianceViolation(HttpCompliance.RFC2616, "case sensitive")) ? cached : orig;
  }
  

  public long getContentLength()
  {
    return _contentLength;
  }
  

  public long getContentRead()
  {
    return _contentPosition;
  }
  




  public void setHeadResponse(boolean head)
  {
    _headResponse = head;
  }
  

  protected void setResponseStatus(int status)
  {
    _responseStatus = status;
  }
  

  public State getState()
  {
    return _state;
  }
  

  public boolean inContentState()
  {
    return (_state.ordinal() >= State.CONTENT.ordinal()) && (_state.ordinal() < State.END.ordinal());
  }
  

  public boolean inHeaderState()
  {
    return _state.ordinal() < State.CONTENT.ordinal();
  }
  

  public boolean isChunking()
  {
    return _endOfContent == HttpTokens.EndOfContent.CHUNKED_CONTENT;
  }
  

  public boolean isStart()
  {
    return isState(State.START);
  }
  

  public boolean isClose()
  {
    return isState(State.CLOSE);
  }
  

  public boolean isClosed()
  {
    return isState(State.CLOSED);
  }
  

  public boolean isIdle()
  {
    return __idleStates.contains(_state);
  }
  

  public boolean isComplete()
  {
    return __completeStates.contains(_state);
  }
  

  public boolean isState(State state)
  {
    return _state == state;
  }
  
  static enum CharState {
    ILLEGAL,  CR,  LF,  LEGAL;
    
    private CharState() {}
  }
  
  static
  {
    CACHE.put(new HttpField(HttpHeader.CONNECTION, HttpHeaderValue.CLOSE));
    CACHE.put(new HttpField(HttpHeader.CONNECTION, HttpHeaderValue.KEEP_ALIVE));
    CACHE.put(new HttpField(HttpHeader.CONNECTION, HttpHeaderValue.UPGRADE));
    CACHE.put(new HttpField(HttpHeader.ACCEPT_ENCODING, "gzip"));
    CACHE.put(new HttpField(HttpHeader.ACCEPT_ENCODING, "gzip, deflate"));
    CACHE.put(new HttpField(HttpHeader.ACCEPT_ENCODING, "gzip,deflate,sdch"));
    CACHE.put(new HttpField(HttpHeader.ACCEPT_LANGUAGE, "en-US,en;q=0.5"));
    CACHE.put(new HttpField(HttpHeader.ACCEPT_LANGUAGE, "en-GB,en-US;q=0.8,en;q=0.6"));
    CACHE.put(new HttpField(HttpHeader.ACCEPT_CHARSET, "ISO-8859-1,utf-8;q=0.7,*;q=0.3"));
    CACHE.put(new HttpField(HttpHeader.ACCEPT, "*/*"));
    CACHE.put(new HttpField(HttpHeader.ACCEPT, "image/png,image/*;q=0.8,*/*;q=0.5"));
    CACHE.put(new HttpField(HttpHeader.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"));
    CACHE.put(new HttpField(HttpHeader.PRAGMA, "no-cache"));
    CACHE.put(new HttpField(HttpHeader.CACHE_CONTROL, "private, no-cache, no-cache=Set-Cookie, proxy-revalidate"));
    CACHE.put(new HttpField(HttpHeader.CACHE_CONTROL, "no-cache"));
    CACHE.put(new HttpField(HttpHeader.CONTENT_LENGTH, "0"));
    CACHE.put(new HttpField(HttpHeader.CONTENT_ENCODING, "gzip"));
    CACHE.put(new HttpField(HttpHeader.CONTENT_ENCODING, "deflate"));
    CACHE.put(new HttpField(HttpHeader.TRANSFER_ENCODING, "chunked"));
    CACHE.put(new HttpField(HttpHeader.EXPIRES, "Fri, 01 Jan 1990 00:00:00 GMT"));
    

    for (String type : new String[] { "text/plain", "text/html", "text/xml", "text/json", "application/json", "application/x-www-form-urlencoded" })
    {
      HttpField field = new PreEncodedHttpField(HttpHeader.CONTENT_TYPE, type);
      CACHE.put(field);
      
      for (String charset : new String[] { "utf-8", "iso-8859-1" })
      {
        CACHE.put(new PreEncodedHttpField(HttpHeader.CONTENT_TYPE, type + ";charset=" + charset));
        CACHE.put(new PreEncodedHttpField(HttpHeader.CONTENT_TYPE, type + "; charset=" + charset));
        CACHE.put(new PreEncodedHttpField(HttpHeader.CONTENT_TYPE, type + ";charset=" + charset.toUpperCase(Locale.ENGLISH)));
        CACHE.put(new PreEncodedHttpField(HttpHeader.CONTENT_TYPE, type + "; charset=" + charset.toUpperCase(Locale.ENGLISH)));
      }
    }
    

    for (HttpHeader h : HttpHeader.values()) {
      if (!CACHE.put(new HttpField(h, (String)null)))
        throw new IllegalStateException("CACHE FULL");
    }
    CACHE.put(new HttpField(HttpHeader.REFERER, (String)null));
    CACHE.put(new HttpField(HttpHeader.IF_MODIFIED_SINCE, (String)null));
    CACHE.put(new HttpField(HttpHeader.IF_NONE_MATCH, (String)null));
    CACHE.put(new HttpField(HttpHeader.AUTHORIZATION, (String)null));
    CACHE.put(new HttpField(HttpHeader.COOKIE, (String)null));
    















































































































































































































    __charState = new CharState['Ā'];
    Arrays.fill(__charState, CharState.ILLEGAL);
    __charState[10] = CharState.LF;
    __charState[13] = CharState.CR;
    __charState[9] = CharState.LEGAL;
    __charState[32] = CharState.LEGAL;
    
    __charState[33] = CharState.LEGAL;
    __charState[35] = CharState.LEGAL;
    __charState[36] = CharState.LEGAL;
    __charState[37] = CharState.LEGAL;
    __charState[38] = CharState.LEGAL;
    __charState[39] = CharState.LEGAL;
    __charState[42] = CharState.LEGAL;
    __charState[43] = CharState.LEGAL;
    __charState[45] = CharState.LEGAL;
    __charState[46] = CharState.LEGAL;
    __charState[94] = CharState.LEGAL;
    __charState[95] = CharState.LEGAL;
    __charState[96] = CharState.LEGAL;
    __charState[124] = CharState.LEGAL;
    __charState[126] = CharState.LEGAL;
    
    __charState[34] = CharState.LEGAL;
    
    __charState[92] = CharState.LEGAL;
    __charState[40] = CharState.LEGAL;
    __charState[41] = CharState.LEGAL;
    Arrays.fill(__charState, 33, 40, CharState.LEGAL);
    Arrays.fill(__charState, 42, 92, CharState.LEGAL);
    Arrays.fill(__charState, 93, 127, CharState.LEGAL);
    Arrays.fill(__charState, 128, 256, CharState.LEGAL);
  }
  


  private byte next(ByteBuffer buffer)
  {
    byte ch = buffer.get();
    
    CharState s = __charState[(0xFF & ch)];
    switch (1.$SwitchMap$org$eclipse$jetty$http$HttpParser$CharState[s.ordinal()])
    {
    case 1: 
      throw new IllegalCharacterException(_state, ch, buffer, null);
    
    case 2: 
      _cr = false;
      break;
    
    case 3: 
      if (_cr) {
        throw new BadMessageException("Bad EOL");
      }
      _cr = true;
      if (buffer.hasRemaining())
      {
        if ((_maxHeaderBytes > 0) && (_state.ordinal() < State.END.ordinal()))
          _headerBytes += 1;
        return next(buffer);
      }
      


      return 0;
    
    case 4: 
      if (_cr) {
        throw new BadMessageException("Bad EOL");
      }
      break;
    }
    return ch;
  }
  




  private boolean quickStart(ByteBuffer buffer)
  {
    if (_requestHandler != null)
    {
      _method = HttpMethod.lookAheadGet(buffer);
      if (_method != null)
      {
        _methodString = _method.asString();
        buffer.position(buffer.position() + _methodString.length() + 1);
        
        setState(State.SPACE1);
        return false;
      }
    }
    else if (_responseHandler != null)
    {
      _version = HttpVersion.lookAheadGet(buffer);
      if (_version != null)
      {
        buffer.position(buffer.position() + _version.asString().length() + 1);
        setState(State.SPACE1);
        return false;
      }
    }
    

    while ((_state == State.START) && (buffer.hasRemaining()))
    {
      int ch = next(buffer);
      
      if (ch > 32)
      {
        _string.setLength(0);
        _string.append((char)ch);
        setState(_requestHandler != null ? State.METHOD : State.RESPONSE_VERSION);
        return false;
      }
      if (ch == 0)
        break;
      if (ch < 0) {
        throw new BadMessageException();
      }
      
      if ((_maxHeaderBytes > 0) && (++_headerBytes > _maxHeaderBytes))
      {
        LOG.warn("padding is too large >" + _maxHeaderBytes, new Object[0]);
        throw new BadMessageException(400);
      }
    }
    return false;
  }
  

  private void setString(String s)
  {
    _string.setLength(0);
    _string.append(s);
    _length = s.length();
  }
  

  private String takeString()
  {
    _string.setLength(_length);
    String s = _string.toString();
    _string.setLength(0);
    _length = -1;
    return s;
  }
  



  private boolean parseLine(ByteBuffer buffer)
  {
    boolean handle = false;
    

    while ((_state.ordinal() < State.HEADER.ordinal()) && (buffer.hasRemaining()) && (!handle))
    {

      byte ch = next(buffer);
      if (ch == 0) {
        break;
      }
      if ((_maxHeaderBytes > 0) && (++_headerBytes > _maxHeaderBytes))
      {
        if (_state == State.URI)
        {
          LOG.warn("URI is too large >" + _maxHeaderBytes, new Object[0]);
          throw new BadMessageException(414);
        }
        

        if (_requestHandler != null) {
          LOG.warn("request is too large >" + _maxHeaderBytes, new Object[0]);
        } else
          LOG.warn("response is too large >" + _maxHeaderBytes, new Object[0]);
        throw new BadMessageException(431);
      }
      

      switch (1.$SwitchMap$org$eclipse$jetty$http$HttpParser$State[_state.ordinal()])
      {
      case 1: 
        if (ch == 32)
        {
          _length = _string.length();
          _methodString = takeString();
          HttpMethod method = (HttpMethod)HttpMethod.CACHE.get(_methodString);
          if (method != null)
            _methodString = legacyString(_methodString, method.asString());
          setState(State.SPACE1);
        } else {
          if (ch < 32)
          {
            if (ch == 10) {
              throw new BadMessageException("No URI");
            }
            throw new IllegalCharacterException(_state, ch, buffer, null);
          }
          
          _string.append((char)ch); }
        break;
      
      case 2: 
        if (ch == 32)
        {
          _length = _string.length();
          String version = takeString();
          _version = ((HttpVersion)HttpVersion.CACHE.get(version));
          if (_version == null)
            throw new BadMessageException(400, "Unknown Version");
          setState(State.SPACE1);
        } else {
          if (ch < 32) {
            throw new IllegalCharacterException(_state, ch, buffer, null);
          }
          _string.append((char)ch); }
        break;
      
      case 3: 
        if ((ch > 32) || (ch < 0))
        {
          if (_responseHandler != null)
          {
            setState(State.STATUS);
            setResponseStatus(ch - 48);
          }
          else
          {
            _uri.reset();
            setState(State.URI);
            
            if (buffer.hasArray())
            {
              byte[] array = buffer.array();
              int p = buffer.arrayOffset() + buffer.position();
              int l = buffer.arrayOffset() + buffer.limit();
              int i = p;
              while ((i < l) && (array[i] > 32)) {
                i++;
              }
              int len = i - p;
              _headerBytes += len;
              
              if ((_maxHeaderBytes > 0) && (++_headerBytes > _maxHeaderBytes))
              {
                LOG.warn("URI is too large >" + _maxHeaderBytes, new Object[0]);
                throw new BadMessageException(414);
              }
              _uri.append(array, p - 1, len + 1);
              buffer.position(i - buffer.arrayOffset());
            }
            else {
              _uri.append(ch);
            }
          }
        } else if (ch < 32)
        {
          throw new BadMessageException(400, _requestHandler != null ? "No URI" : "No Status");
        }
        
        break;
      case 4: 
        if (ch == 32)
        {
          setState(State.SPACE2);
        }
        else if ((ch >= 48) && (ch <= 57))
        {
          _responseStatus = (_responseStatus * 10 + (ch - 48));
        }
        else if ((ch < 32) && (ch >= 0))
        {
          setState(State.HEADER);
          handle = (_responseHandler.startResponse(_version, _responseStatus, null)) || (handle);
        }
        else
        {
          throw new BadMessageException();
        }
        
        break;
      case 5: 
        if (ch == 32)
        {
          setState(State.SPACE2);
        } else {
          if ((ch < 32) && (ch >= 0))
          {

            if (complianceViolation(HttpCompliance.RFC7230, "HTTP/0.9"))
              throw new BadMessageException("HTTP/0.9 not supported");
            handle = _requestHandler.startRequest(_methodString, _uri.toString(), HttpVersion.HTTP_0_9);
            setState(State.END);
            BufferUtil.clear(buffer);
            handle = (_handler.headerComplete()) || (handle);
            _headerComplete = true;
            handle = (_handler.messageComplete()) || (handle);
            return handle;
          }
          

          _uri.append(ch);
        }
        break;
      
      case 6: 
        if (ch > 32)
        {
          _string.setLength(0);
          _string.append((char)ch);
          if (_responseHandler != null)
          {
            _length = 1;
            setState(State.REASON);
          }
          else
          {
            setState(State.REQUEST_VERSION);
            
            HttpVersion version;
            HttpVersion version;
            if ((buffer.position() > 0) && (buffer.hasArray())) {
              version = HttpVersion.lookAheadGet(buffer.array(), buffer.arrayOffset() + buffer.position() - 1, buffer.arrayOffset() + buffer.limit());
            } else {
              version = (HttpVersion)HttpVersion.CACHE.getBest(buffer, 0, buffer.remaining());
            }
            if (version != null)
            {
              int pos = buffer.position() + version.asString().length() - 1;
              if (pos < buffer.limit())
              {
                byte n = buffer.get(pos);
                if (n == 13)
                {
                  _cr = true;
                  _version = version;
                  _string.setLength(0);
                  buffer.position(pos + 1);
                }
                else if (n == 10)
                {
                  _version = version;
                  _string.setLength(0);
                  buffer.position(pos);
                }
              }
            }
          }
        }
        else if (ch == 10)
        {
          if (_responseHandler != null)
          {
            setState(State.HEADER);
            handle = (_responseHandler.startResponse(_version, _responseStatus, null)) || (handle);

          }
          else
          {
            if (complianceViolation(HttpCompliance.RFC7230, "HTTP/0.9")) {
              throw new BadMessageException("HTTP/0.9 not supported");
            }
            handle = _requestHandler.startRequest(_methodString, _uri.toString(), HttpVersion.HTTP_0_9);
            setState(State.END);
            BufferUtil.clear(buffer);
            handle = (_handler.headerComplete()) || (handle);
            _headerComplete = true;
            handle = (_handler.messageComplete()) || (handle);
            return handle;
          }
        }
        else if (ch < 0) {
          throw new BadMessageException();
        }
        break;
      case 7: 
        if (ch == 10)
        {
          if (_version == null)
          {
            _length = _string.length();
            _version = ((HttpVersion)HttpVersion.CACHE.get(takeString()));
          }
          if (_version == null) {
            throw new BadMessageException(400, "Unknown Version");
          }
          
          if ((_connectionFields == null) && (_version.getVersion() >= HttpVersion.HTTP_1_1.getVersion()) && (_handler.getHeaderCacheSize() > 0))
          {
            int header_cache = _handler.getHeaderCacheSize();
            _connectionFields = new ArrayTernaryTrie(header_cache);
          }
          
          setState(State.HEADER);
          
          handle = (_requestHandler.startRequest(_methodString, _uri.toString(), _version)) || (handle);

        }
        else if (ch >= 32) {
          _string.append((char)ch);
        } else {
          throw new BadMessageException();
        }
        
        break;
      case 8: 
        if (ch == 10)
        {
          String reason = takeString();
          setState(State.HEADER);
          handle = (_responseHandler.startResponse(_version, _responseStatus, reason)) || (handle);

        }
        else if (ch >= 32)
        {
          _string.append((char)ch);
          if ((ch != 32) && (ch != 9)) {
            _length = _string.length();
          }
        } else {
          throw new BadMessageException();
        }
        break;
      default: 
        throw new IllegalStateException(_state.toString());
      }
      
    }
    
    return handle;
  }
  

  private void parsedHeader()
  {
    if ((_headerString != null) || (_valueString != null))
    {

      if (_header != null)
      {
        boolean add_to_connection_trie = false;
        switch (1.$SwitchMap$org$eclipse$jetty$http$HttpHeader[_header.ordinal()])
        {
        case 1: 
          if (_endOfContent == HttpTokens.EndOfContent.CONTENT_LENGTH)
          {
            throw new BadMessageException(400, "Duplicate Content-Length");
          }
          if (_endOfContent != HttpTokens.EndOfContent.CHUNKED_CONTENT)
          {
            _contentLength = convertContentLength(_valueString);
            if (_contentLength <= 0L) {
              _endOfContent = HttpTokens.EndOfContent.NO_CONTENT;
            } else {
              _endOfContent = HttpTokens.EndOfContent.CONTENT_LENGTH;
            }
          }
          break;
        case 2: 
          if (_value == HttpHeaderValue.CHUNKED)
          {
            _endOfContent = HttpTokens.EndOfContent.CHUNKED_CONTENT;
            _contentLength = -1L;


          }
          else if (_valueString.endsWith(HttpHeaderValue.CHUNKED.toString())) {
            _endOfContent = HttpTokens.EndOfContent.CHUNKED_CONTENT;
          } else if (_valueString.contains(HttpHeaderValue.CHUNKED.toString())) {
            throw new BadMessageException(400, "Bad chunking");
          }
          
          break;
        case 3: 
          _host = true;
          if ((!(_field instanceof HostPortHttpField)) && (_valueString != null) && (!_valueString.isEmpty()))
          {
            _field = new HostPortHttpField(_header, legacyString(_headerString, _header.asString()), _valueString);
            add_to_connection_trie = _connectionFields != null;
          }
          

          break;
        case 4: 
          if ((_valueString != null) && (_valueString.contains("close"))) {
            _connectionFields = null;
          }
          
          break;
        case 5: 
        case 6: 
        case 7: 
        case 8: 
        case 9: 
        case 10: 
        case 11: 
        case 12: 
          add_to_connection_trie = (_connectionFields != null) && (_field == null);
          break;
        }
        
        


        if ((add_to_connection_trie) && (!_connectionFields.isFull()) && (_header != null) && (_valueString != null))
        {
          if (_field == null)
            _field = new HttpField(_header, legacyString(_headerString, _header.asString()), _valueString);
          _connectionFields.put(_field);
        }
      }
      _handler.parsedHeader(_field != null ? _field : new HttpField(_header, _headerString, _valueString));
    }
    
    _headerString = (this._valueString = null);
    _header = null;
    _value = null;
    _field = null;
  }
  
  private long convertContentLength(String valueString)
  {
    try
    {
      return Long.parseLong(valueString);
    }
    catch (NumberFormatException e)
    {
      LOG.ignore(e);
      throw new BadMessageException(400, "Invalid Content-Length Value");
    }
  }
  




  protected boolean parseHeaders(ByteBuffer buffer)
  {
    boolean handle = false;
    

    while ((_state.ordinal() < State.CONTENT.ordinal()) && (buffer.hasRemaining()) && (!handle))
    {

      byte ch = next(buffer);
      if (ch == 0) {
        break;
      }
      if ((_maxHeaderBytes > 0) && (++_headerBytes > _maxHeaderBytes))
      {
        LOG.warn("Header is too large >" + _maxHeaderBytes, new Object[0]);
        throw new BadMessageException(431);
      }
      
      switch (1.$SwitchMap$org$eclipse$jetty$http$HttpParser$State[_state.ordinal()])
      {
      case 9: 
        switch (ch)
        {

        case 9: 
        case 32: 
        case 58: 
          if (complianceViolation(HttpCompliance.RFC7230, "header folding")) {
            throw new BadMessageException(400, "Header Folding");
          }
          
          if (_valueString == null)
          {
            _string.setLength(0);
            _length = 0;
          }
          else
          {
            setString(_valueString);
            _string.append(' ');
            _length += 1;
            _valueString = null;
          }
          setState(State.HEADER_VALUE);
          break;
        



        case 10: 
          parsedHeader();
          
          _contentPosition = 0L;
          



          if ((!_host) && (_version == HttpVersion.HTTP_1_1) && (_requestHandler != null))
          {
            throw new BadMessageException(400, "No Host");
          }
          

          if ((_responseHandler != null) && ((_responseStatus == 304) || (_responseStatus == 204) || (_responseStatus < 200)))
          {


            _endOfContent = HttpTokens.EndOfContent.NO_CONTENT;

          }
          else if (_endOfContent == HttpTokens.EndOfContent.UNKNOWN_CONTENT)
          {
            if ((_responseStatus == 0) || (_responseStatus == 304) || (_responseStatus == 204) || (_responseStatus < 200))
            {


              _endOfContent = HttpTokens.EndOfContent.NO_CONTENT;
            } else {
              _endOfContent = HttpTokens.EndOfContent.EOF_CONTENT;
            }
          }
          
          switch (1.$SwitchMap$org$eclipse$jetty$http$HttpTokens$EndOfContent[_endOfContent.ordinal()])
          {
          case 1: 
            setState(State.EOF_CONTENT);
            handle = (_handler.headerComplete()) || (handle);
            _headerComplete = true;
            return handle;
          
          case 2: 
            setState(State.CHUNKED_CONTENT);
            handle = (_handler.headerComplete()) || (handle);
            _headerComplete = true;
            return handle;
          
          case 3: 
            setState(State.END);
            handle = (_handler.headerComplete()) || (handle);
            _headerComplete = true;
            handle = (_handler.messageComplete()) || (handle);
            return handle;
          }
          
          setState(State.CONTENT);
          handle = (_handler.headerComplete()) || (handle);
          _headerComplete = true;
          return handle;
        




        default: 
          if (ch < 32) {
            throw new BadMessageException();
          }
          
          parsedHeader();
          

          if (buffer.hasRemaining())
          {

            HttpField field = _connectionFields == null ? null : (HttpField)_connectionFields.getBest(buffer, -1, buffer.remaining());
            if (field == null) {
              field = (HttpField)CACHE.getBest(buffer, -1, buffer.remaining());
            }
            if (field != null)
            {
              String n;
              
              String v;
              if (_compliance == HttpCompliance.LEGACY)
              {

                String fn = field.getName();
                String n = legacyString(BufferUtil.toString(buffer, buffer.position() - 1, fn.length(), StandardCharsets.US_ASCII), fn);
                String fv = field.getValue();
                String v; if (fv == null) {
                  v = null;
                }
                else {
                  String v = legacyString(BufferUtil.toString(buffer, buffer.position() + fn.length() + 1, fv.length(), StandardCharsets.ISO_8859_1), fv);
                  field = new HttpField(field.getHeader(), n, v);
                }
              }
              else
              {
                n = field.getName();
                v = field.getValue();
              }
              
              _header = field.getHeader();
              _headerString = n;
              
              if (v == null)
              {

                setState(State.HEADER_VALUE);
                _string.setLength(0);
                _length = 0;
                buffer.position(buffer.position() + n.length() + 1);
                break;
              }
              


              int pos = buffer.position() + n.length() + v.length() + 1;
              byte b = buffer.get(pos);
              
              if ((b == 13) || (b == 10))
              {
                _field = field;
                _valueString = v;
                setState(State.HEADER_IN_VALUE);
                
                if (b == 13)
                {
                  _cr = true;
                  buffer.position(pos + 1); break;
                }
                
                buffer.position(pos);
                break;
              }
              

              setState(State.HEADER_IN_VALUE);
              setString(v);
              buffer.position(pos);
              break;
            }
          }
          



          setState(State.HEADER_IN_NAME);
          _string.setLength(0);
          _string.append((char)ch);
          _length = 1;
        }
        
        
        break;
      
      case 10: 
        if (ch == 58)
        {
          if (_headerString == null)
          {
            _headerString = takeString();
            _header = ((HttpHeader)HttpHeader.CACHE.get(_headerString));
          }
          _length = -1;
          
          setState(State.HEADER_VALUE);


        }
        else if (ch > 32)
        {
          if (_header != null)
          {
            setString(_header.asString());
            _header = null;
            _headerString = null;
          }
          
          _string.append((char)ch);
          if (ch > 32) {
            _length = _string.length();
          }
          
        }
        else if ((ch == 10) && (!complianceViolation(HttpCompliance.RFC7230, "name only header")))
        {
          if (_headerString == null)
          {
            _headerString = takeString();
            _header = ((HttpHeader)HttpHeader.CACHE.get(_headerString));
          }
          _value = null;
          _string.setLength(0);
          _valueString = "";
          _length = -1;
          
          setState(State.HEADER);
        }
        else
        {
          throw new IllegalCharacterException(_state, ch, buffer, null);
        }
        break;
      case 11:  if ((ch > 32) || (ch < 0))
        {
          _string.append((char)(0xFF & ch));
          _length = _string.length();
          setState(State.HEADER_IN_VALUE);


        }
        else if ((ch != 32) && (ch != 9))
        {

          if (ch == 10)
          {
            _value = null;
            _string.setLength(0);
            _valueString = "";
            _length = -1;
            
            setState(State.HEADER);
          }
          else {
            throw new IllegalCharacterException(_state, ch, buffer, null);
          }
        }
        break; case 12:  if ((ch >= 32) || (ch < 0) || (ch == 9))
        {
          if (_valueString != null)
          {
            setString(_valueString);
            _valueString = null;
            _field = null;
          }
          _string.append((char)(0xFF & ch));
          if ((ch > 32) || (ch < 0)) {
            _length = _string.length();
          }
          
        }
        else if (ch == 10)
        {
          if (_length > 0)
          {
            _value = null;
            _valueString = takeString();
            _length = -1;
          }
          setState(State.HEADER);
        }
        else
        {
          throw new IllegalCharacterException(_state, ch, buffer, null);
        }
        break;
      default:  throw new IllegalStateException(_state.toString());
      }
      
    }
    
    return handle;
  }
  






  public boolean parseNext(ByteBuffer buffer)
  {
    if (DEBUG) {
      LOG.debug("parseNext s={} {}", new Object[] { _state, BufferUtil.toDetailString(buffer) });
    }
    try
    {
      if (_state == State.START)
      {
        _version = null;
        _method = null;
        _methodString = null;
        _endOfContent = HttpTokens.EndOfContent.UNKNOWN_CONTENT;
        _header = null;
        if (quickStart(buffer)) {
          return true;
        }
      }
      
      if ((_state.ordinal() >= State.START.ordinal()) && (_state.ordinal() < State.HEADER.ordinal()))
      {
        if (parseLine(buffer)) {
          return true;
        }
      }
      
      if ((_state.ordinal() >= State.HEADER.ordinal()) && (_state.ordinal() < State.CONTENT.ordinal()))
      {
        if (parseHeaders(buffer)) {
          return true;
        }
      }
      
      if ((_state.ordinal() >= State.CONTENT.ordinal()) && (_state.ordinal() < State.END.ordinal()))
      {

        if ((_responseStatus > 0) && (_headResponse))
        {
          setState(State.END);
          return _handler.messageComplete();
        }
        

        if (parseContent(buffer)) {
          return true;
        }
      }
      

      if (_state == State.END)
      {

        while ((buffer.remaining() > 0) && (buffer.get(buffer.position()) <= 32))
          buffer.get();
      }
      if (_state == State.CLOSE)
      {

        if (BufferUtil.hasContent(buffer))
        {

          _headerBytes += buffer.remaining();
          BufferUtil.clear(buffer);
          if ((_maxHeaderBytes > 0) && (_headerBytes > _maxHeaderBytes))
          {

            throw new IllegalStateException("too much data seeking EOF");
          }
        }
      }
      else if (_state == State.CLOSED)
      {
        BufferUtil.clear(buffer);
      }
      

      if ((_eof) && (!buffer.hasRemaining()))
      {
        switch (1.$SwitchMap$org$eclipse$jetty$http$HttpParser$State[_state.ordinal()])
        {
        case 13: 
          break;
        
        case 14: 
          setState(State.CLOSED);
          _handler.earlyEOF();
          break;
        
        case 15: 
        case 16: 
          setState(State.CLOSED);
          break;
        
        case 17: 
        case 18: 
          setState(State.CLOSED);
          return _handler.messageComplete();
        
        case 19: 
        case 20: 
        case 21: 
        case 22: 
        case 23: 
        case 24: 
          setState(State.CLOSED);
          _handler.earlyEOF();
          break;
        
        default: 
          if (DEBUG)
            LOG.debug("{} EOF in {}", new Object[] { this, _state });
          setState(State.CLOSED);
          _handler.badMessage(400, null);
        }
        
      }
    }
    catch (BadMessageException e)
    {
      BufferUtil.clear(buffer);
      
      Throwable cause = e.getCause();
      boolean stack = (LOG.isDebugEnabled()) || ((!(cause instanceof NumberFormatException)) && (((cause instanceof RuntimeException)) || ((cause instanceof Error))));
      

      if (stack) {
        LOG.warn("bad HTTP parsed: " + _code + (e.getReason() != null ? " " + e.getReason() : "") + " for " + _handler, e);
      } else
        LOG.warn("bad HTTP parsed: " + _code + (e.getReason() != null ? " " + e.getReason() : "") + " for " + _handler, new Object[0]);
      setState(State.CLOSE);
      _handler.badMessage(e.getCode(), e.getReason());
    }
    catch (NumberFormatException|IllegalStateException e)
    {
      BufferUtil.clear(buffer);
      LOG.warn("parse exception: {} in {} for {}", new Object[] { e.toString(), _state, _handler });
      if (DEBUG)
        LOG.debug(e);
      badMessage();

    }
    catch (Exception|Error e)
    {
      BufferUtil.clear(buffer);
      LOG.warn("parse exception: " + e.toString() + " for " + _handler, e);
      badMessage();
    }
    return false;
  }
  
  protected void badMessage()
  {
    if (_headerComplete)
    {
      _handler.earlyEOF();
    }
    else if (_state != State.CLOSED)
    {
      setState(State.CLOSE);
      _handler.badMessage(400, _requestHandler != null ? "Bad Request" : "Bad Response");
    }
  }
  
  protected boolean parseContent(ByteBuffer buffer)
  {
    int remaining = buffer.remaining();
    if ((remaining == 0) && (_state == State.CONTENT))
    {
      long content = _contentLength - _contentPosition;
      if (content == 0L)
      {
        setState(State.END);
        return _handler.messageComplete();
      }
    }
    


    while ((_state.ordinal() < State.END.ordinal()) && (remaining > 0))
    {
      switch (1.$SwitchMap$org$eclipse$jetty$http$HttpParser$State[_state.ordinal()])
      {
      case 17: 
        _contentChunk = buffer.asReadOnlyBuffer();
        _contentPosition += remaining;
        buffer.position(buffer.position() + remaining);
        if (_handler.content(_contentChunk)) {
          return true;
        }
        
        break;
      case 19: 
        long content = _contentLength - _contentPosition;
        if (content == 0L)
        {
          setState(State.END);
          return _handler.messageComplete();
        }
        

        _contentChunk = buffer.asReadOnlyBuffer();
        

        if (remaining > content)
        {


          _contentChunk.limit(_contentChunk.position() + (int)content);
        }
        
        _contentPosition += _contentChunk.remaining();
        buffer.position(buffer.position() + _contentChunk.remaining());
        
        if (_handler.content(_contentChunk)) {
          return true;
        }
        if (_contentPosition == _contentLength)
        {
          setState(State.END);
          return _handler.messageComplete();
        }
        



        break;
      case 20: 
        byte ch = next(buffer);
        if (ch > 32)
        {
          _chunkLength = TypeUtil.convertHexDigit(ch);
          _chunkPosition = 0;
          setState(State.CHUNK_SIZE);
        }
        



        break;
      case 21: 
        byte ch = next(buffer);
        if (ch != 0)
        {
          if (ch == 10)
          {
            if (_chunkLength == 0) {
              setState(State.CHUNK_END);
            } else {
              setState(State.CHUNK);
            }
          } else if ((ch <= 32) || (ch == 59)) {
            setState(State.CHUNK_PARAMS);
          } else
            _chunkLength = (_chunkLength * 16 + TypeUtil.convertHexDigit(ch)); }
        break;
      


      case 22: 
        byte ch = next(buffer);
        if (ch == 10)
        {
          if (_chunkLength == 0) {
            setState(State.CHUNK_END);
          } else {
            setState(State.CHUNK);
          }
        }
        

        break;
      case 23: 
        int chunk = _chunkLength - _chunkPosition;
        if (chunk == 0)
        {
          setState(State.CHUNKED_CONTENT);
        }
        else
        {
          _contentChunk = buffer.asReadOnlyBuffer();
          
          if (remaining > chunk)
            _contentChunk.limit(_contentChunk.position() + chunk);
          chunk = _contentChunk.remaining();
          
          _contentPosition += chunk;
          _chunkPosition += chunk;
          buffer.position(buffer.position() + chunk);
          if (_handler.content(_contentChunk)) {
            return true;
          }
        }
        

        break;
      case 18: 
        byte ch = next(buffer);
        if (ch != 0)
        {
          if (ch == 10)
          {
            setState(State.END);
            return _handler.messageComplete();
          }
          setState(State.CHUNK_TRAILER); }
        break;
      



      case 24: 
        byte ch = next(buffer);
        if (ch != 0)
        {
          if (ch == 10) {
            setState(State.CHUNK_END);
          }
        }
        
        break;
      case 13: 
        BufferUtil.clear(buffer);
        return false;
      }
      
      




      remaining = buffer.remaining();
    }
    return false;
  }
  


  public boolean isAtEOF()
  {
    return _eof;
  }
  



  public void atEOF()
  {
    if (DEBUG)
      LOG.debug("atEOF {}", new Object[] { this });
    _eof = true;
  }
  



  public void close()
  {
    if (DEBUG)
      LOG.debug("close {}", new Object[] { this });
    setState(State.CLOSE);
  }
  

  public void reset()
  {
    if (DEBUG) {
      LOG.debug("reset {}", new Object[] { this });
    }
    
    if ((_state == State.CLOSE) || (_state == State.CLOSED)) {
      return;
    }
    setState(State.START);
    _endOfContent = HttpTokens.EndOfContent.UNKNOWN_CONTENT;
    _contentLength = -1L;
    _contentPosition = 0L;
    _responseStatus = 0;
    _contentChunk = null;
    _headerBytes = 0;
    _host = false;
    _headerComplete = false;
  }
  

  protected void setState(State state)
  {
    if (DEBUG)
      LOG.debug("{} --> {}", new Object[] { _state, state });
    _state = state;
  }
  

  public Trie<HttpField> getFieldCache()
  {
    return _connectionFields;
  }
  

  private String getProxyField(ByteBuffer buffer)
  {
    _string.setLength(0);
    _length = 0;
    
    while (buffer.hasRemaining())
    {

      byte ch = next(buffer);
      if (ch <= 32)
        return _string.toString();
      _string.append((char)ch);
    }
    throw new BadMessageException();
  }
  


  public String toString()
  {
    return String.format("%s{s=%s,%d of %d}", new Object[] {
      getClass().getSimpleName(), _state, 
      
      Long.valueOf(_contentPosition), 
      Long.valueOf(_contentLength) });
  }
  





  public static abstract interface HttpHandler
  {
    public abstract boolean content(ByteBuffer paramByteBuffer);
    





    public abstract boolean headerComplete();
    





    public abstract boolean messageComplete();
    





    public abstract void parsedHeader(HttpField paramHttpField);
    




    public abstract void earlyEOF();
    




    public abstract void badMessage(int paramInt, String paramString);
    




    public abstract int getHeaderCacheSize();
  }
  




  public static abstract interface RequestHandler
    extends HttpParser.HttpHandler
  {
    public abstract boolean startRequest(String paramString1, String paramString2, HttpVersion paramHttpVersion);
  }
  




  public static abstract interface ResponseHandler
    extends HttpParser.HttpHandler
  {
    public abstract boolean startResponse(HttpVersion paramHttpVersion, int paramInt, String paramString);
  }
  




  public static abstract interface ComplianceHandler
    extends HttpParser.HttpHandler
  {
    public abstract void onComplianceViolation(HttpCompliance paramHttpCompliance1, HttpCompliance paramHttpCompliance2, String paramString);
  }
  




  private static class IllegalCharacterException
    extends BadMessageException
  {
    private IllegalCharacterException(HttpParser.State state, byte ch, ByteBuffer buffer)
    {
      super(String.format("Illegal character 0x%X", new Object[] { Byte.valueOf(ch) }));
      
      HttpParser.LOG.warn(String.format("Illegal character 0x%X in state=%s for buffer %s", new Object[] { Byte.valueOf(ch), state, BufferUtil.toDetailString(buffer) }), new Object[0]);
    }
  }
}
