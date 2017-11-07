package org.seleniumhq.jetty9.security.authentication;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.BitSet;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.seleniumhq.jetty9.http.HttpHeader;
import org.seleniumhq.jetty9.security.Authenticator.AuthConfiguration;
import org.seleniumhq.jetty9.security.LoginService;
import org.seleniumhq.jetty9.security.ServerAuthException;
import org.seleniumhq.jetty9.security.UserAuthentication;
import org.seleniumhq.jetty9.server.Authentication;
import org.seleniumhq.jetty9.server.Authentication.User;
import org.seleniumhq.jetty9.server.Request;
import org.seleniumhq.jetty9.server.UserIdentity;
import org.seleniumhq.jetty9.util.B64Code;
import org.seleniumhq.jetty9.util.QuotedStringTokenizer;
import org.seleniumhq.jetty9.util.TypeUtil;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;
import org.seleniumhq.jetty9.util.security.Credential;
import org.seleniumhq.jetty9.util.security.Credential.MD5;
























public class DigestAuthenticator
  extends LoginAuthenticator
{
  private static final Logger LOG = Log.getLogger(DigestAuthenticator.class);
  
  private final SecureRandom _random = new SecureRandom();
  private long _maxNonceAgeMs = 60000L;
  private int _maxNC = 1024;
  private ConcurrentMap<String, Nonce> _nonceMap = new ConcurrentHashMap();
  private Queue<Nonce> _nonceQueue = new ConcurrentLinkedQueue();
  
  public DigestAuthenticator() {}
  
  public void setConfiguration(Authenticator.AuthConfiguration configuration) {
    super.setConfiguration(configuration);
    
    String mna = configuration.getInitParameter("maxNonceAge");
    if (mna != null)
      setMaxNonceAge(Long.valueOf(mna).longValue());
    String mnc = configuration.getInitParameter("maxNonceCount");
    if (mnc != null) {
      setMaxNonceCount(Integer.valueOf(mnc).intValue());
    }
  }
  
  public int getMaxNonceCount() {
    return _maxNC;
  }
  
  public void setMaxNonceCount(int maxNC)
  {
    _maxNC = maxNC;
  }
  
  public long getMaxNonceAge()
  {
    return _maxNonceAgeMs;
  }
  
  public void setMaxNonceAge(long maxNonceAgeInMillis)
  {
    _maxNonceAgeMs = maxNonceAgeInMillis;
  }
  

  public String getAuthMethod()
  {
    return "DIGEST";
  }
  
  public boolean secureResponse(ServletRequest req, ServletResponse res, boolean mandatory, Authentication.User validatedUser)
    throws ServerAuthException
  {
    return true;
  }
  
  public Authentication validateRequest(ServletRequest req, ServletResponse res, boolean mandatory)
    throws ServerAuthException
  {
    if (!mandatory) {
      return new DeferredAuthentication(this);
    }
    HttpServletRequest request = (HttpServletRequest)req;
    HttpServletResponse response = (HttpServletResponse)res;
    String credentials = request.getHeader(HttpHeader.AUTHORIZATION.asString());
    
    try
    {
      boolean stale = false;
      if (credentials != null)
      {
        if (LOG.isDebugEnabled())
          LOG.debug("Credentials: " + credentials, new Object[0]);
        QuotedStringTokenizer tokenizer = new QuotedStringTokenizer(credentials, "=, ", true, false);
        Digest digest = new Digest(request.getMethod());
        String last = null;
        String name = null;
        
        while (tokenizer.hasMoreTokens())
        {
          String tok = tokenizer.nextToken();
          char c = tok.length() == 1 ? tok.charAt(0) : '\000';
          
          switch (c)
          {
          case '=': 
            name = last;
            last = tok;
            break;
          case ',': 
            name = null;
            break;
          case ' ': 
            break;
          
          default: 
            last = tok;
            if (name != null)
            {
              if ("username".equalsIgnoreCase(name)) {
                username = tok;
              } else if ("realm".equalsIgnoreCase(name)) {
                realm = tok;
              } else if ("nonce".equalsIgnoreCase(name)) {
                nonce = tok;
              } else if ("nc".equalsIgnoreCase(name)) {
                nc = tok;
              } else if ("cnonce".equalsIgnoreCase(name)) {
                cnonce = tok;
              } else if ("qop".equalsIgnoreCase(name)) {
                qop = tok;
              } else if ("uri".equalsIgnoreCase(name)) {
                uri = tok;
              } else if ("response".equalsIgnoreCase(name))
                response = tok;
              name = null;
            }
            break;
          }
        }
        int n = checkNonce(digest, (Request)request);
        
        if (n > 0)
        {

          UserIdentity user = login(username, digest, req);
          if (user != null)
          {
            return new UserAuthentication(getAuthMethod(), user);
          }
        }
        else if (n == 0) {
          stale = true;
        }
      }
      
      if (!DeferredAuthentication.isDeferred(response))
      {
        String domain = request.getContextPath();
        if (domain == null)
          domain = "/";
        response.setHeader(HttpHeader.WWW_AUTHENTICATE.asString(), "Digest realm=\"" + _loginService.getName() + "\", domain=\"" + domain + "\", nonce=\"" + 
        


          newNonce((Request)request) + "\", algorithm=MD5, qop=\"auth\", stale=" + stale);
        

        response.sendError(401);
        
        return Authentication.SEND_CONTINUE;
      }
      
      return Authentication.UNAUTHENTICATED;
    }
    catch (IOException e)
    {
      throw new ServerAuthException(e);
    }
  }
  

  public UserIdentity login(String username, Object credentials, ServletRequest request)
  {
    Digest digest = (Digest)credentials;
    if (!Objects.equals(realm, _loginService.getName()))
      return null;
    return super.login(username, credentials, request);
  }
  

  public String newNonce(Request request)
  {
    Nonce nonce;
    do
    {
      byte[] nounce = new byte[24];
      _random.nextBytes(nounce);
      
      nonce = new Nonce(new String(B64Code.encode(nounce)), request.getTimeStamp(), getMaxNonceCount());
    }
    while (_nonceMap.putIfAbsent(_nonce, nonce) != null);
    _nonceQueue.add(nonce);
    
    return _nonce;
  }
  






  private int checkNonce(Digest digest, Request request)
  {
    long expired = request.getTimeStamp() - getMaxNonceAge();
    Nonce nonce = (Nonce)_nonceQueue.peek();
    while ((nonce != null) && (_ts < expired))
    {
      _nonceQueue.remove(nonce);
      _nonceMap.remove(_nonce);
      nonce = (Nonce)_nonceQueue.peek();
    }
    

    try
    {
      nonce = (Nonce)_nonceMap.get(nonce);
      if (nonce == null) {
        return 0;
      }
      long count = Long.parseLong(nc, 16);
      if (count >= _maxNC) {
        return 0;
      }
      if (nonce.seen((int)count)) {
        return -1;
      }
      return 1;
    }
    catch (Exception e)
    {
      LOG.ignore(e);
    }
    return -1;
  }
  
  private static class Nonce
  {
    final String _nonce;
    final long _ts;
    final BitSet _seen;
    
    public Nonce(String nonce, long ts, int size)
    {
      _nonce = nonce;
      _ts = ts;
      _seen = new BitSet(size);
    }
    
    public boolean seen(int count)
    {
      synchronized (this)
      {
        if (count >= _seen.size())
          return true;
        boolean s = _seen.get(count);
        _seen.set(count);
        return s;
      }
    }
  }
  
  private static class Digest extends Credential
  {
    private static final long serialVersionUID = -2484639019549527724L;
    final String method;
    String username = "";
    String realm = "";
    String nonce = "";
    String nc = "";
    String cnonce = "";
    String qop = "";
    String uri = "";
    String response = "";
    

    Digest(String m)
    {
      method = m;
    }
    


    public boolean check(Object credentials)
    {
      if ((credentials instanceof char[]))
        credentials = new String((char[])credentials);
      String password = (credentials instanceof String) ? (String)credentials : credentials.toString();
      
      try
      {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] ha1;
        byte[] ha1; if ((credentials instanceof Credential.MD5))
        {



          ha1 = ((Credential.MD5)credentials).getDigest();

        }
        else
        {
          md.update(username.getBytes(StandardCharsets.ISO_8859_1));
          md.update((byte)58);
          md.update(realm.getBytes(StandardCharsets.ISO_8859_1));
          md.update((byte)58);
          md.update(password.getBytes(StandardCharsets.ISO_8859_1));
          ha1 = md.digest();
        }
        
        md.reset();
        md.update(method.getBytes(StandardCharsets.ISO_8859_1));
        md.update((byte)58);
        md.update(uri.getBytes(StandardCharsets.ISO_8859_1));
        byte[] ha2 = md.digest();
        







        md.update(TypeUtil.toString(ha1, 16).getBytes(StandardCharsets.ISO_8859_1));
        md.update((byte)58);
        md.update(nonce.getBytes(StandardCharsets.ISO_8859_1));
        md.update((byte)58);
        md.update(nc.getBytes(StandardCharsets.ISO_8859_1));
        md.update((byte)58);
        md.update(cnonce.getBytes(StandardCharsets.ISO_8859_1));
        md.update((byte)58);
        md.update(qop.getBytes(StandardCharsets.ISO_8859_1));
        md.update((byte)58);
        md.update(TypeUtil.toString(ha2, 16).getBytes(StandardCharsets.ISO_8859_1));
        byte[] digest = md.digest();
        

        return TypeUtil.toString(digest, 16).equalsIgnoreCase(response);
      }
      catch (Exception e)
      {
        DigestAuthenticator.LOG.warn(e);
      }
      
      return false;
    }
    

    public String toString()
    {
      return username + "," + response;
    }
  }
}
