package org.apache.http.impl.auth;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.InvalidCredentialsException;
import org.apache.http.auth.KerberosCredentials;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.message.BufferedHeader;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;
import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.GSSManager;
import org.ietf.jgss.GSSName;
import org.ietf.jgss.Oid;































public abstract class GGSSchemeBase
  extends AuthSchemeBase
{
  static enum State
  {
    UNINITIATED, 
    CHALLENGE_RECEIVED, 
    TOKEN_GENERATED, 
    FAILED;
    
    private State() {} }
  private final Log log = LogFactory.getLog(getClass());
  
  private final Base64 base64codec;
  
  private final boolean stripPort;
  
  private final boolean useCanonicalHostname;
  
  private State state;
  
  private byte[] token;
  
  GGSSchemeBase(boolean stripPort, boolean useCanonicalHostname)
  {
    base64codec = new Base64(0);
    this.stripPort = stripPort;
    this.useCanonicalHostname = useCanonicalHostname;
    state = State.UNINITIATED;
  }
  
  GGSSchemeBase(boolean stripPort) {
    this(stripPort, true);
  }
  
  GGSSchemeBase() {
    this(true, true);
  }
  
  protected GSSManager getManager() {
    return GSSManager.getInstance();
  }
  
  protected byte[] generateGSSToken(byte[] input, Oid oid, String authServer) throws GSSException
  {
    return generateGSSToken(input, oid, authServer, null);
  }
  



  protected byte[] generateGSSToken(byte[] input, Oid oid, String authServer, Credentials credentials)
    throws GSSException
  {
    GSSManager manager = getManager();
    GSSName serverName = manager.createName("HTTP@" + authServer, GSSName.NT_HOSTBASED_SERVICE);
    GSSCredential gssCredential;
    GSSCredential gssCredential;
    if ((credentials instanceof KerberosCredentials)) {
      gssCredential = ((KerberosCredentials)credentials).getGSSCredential();
    } else {
      gssCredential = null;
    }
    
    GSSContext gssContext = createGSSContext(manager, oid, serverName, gssCredential);
    if (input != null) {
      return gssContext.initSecContext(input, 0, input.length);
    }
    return gssContext.initSecContext(new byte[0], 0, 0);
  }
  



  GSSContext createGSSContext(GSSManager manager, Oid oid, GSSName serverName, GSSCredential gssCredential)
    throws GSSException
  {
    GSSContext gssContext = manager.createContext(serverName.canonicalize(oid), oid, gssCredential, 0);
    
    gssContext.requestMutualAuth(true);
    return gssContext;
  }
  
  @Deprecated
  protected byte[] generateToken(byte[] input, String authServer)
    throws GSSException
  {
    return null;
  }
  




  protected byte[] generateToken(byte[] input, String authServer, Credentials credentials)
    throws GSSException
  {
    return generateToken(input, authServer);
  }
  
  public boolean isComplete()
  {
    return (state == State.TOKEN_GENERATED) || (state == State.FAILED);
  }
  





  @Deprecated
  public Header authenticate(Credentials credentials, HttpRequest request)
    throws AuthenticationException
  {
    return authenticate(credentials, request, null);
  }
  


  public Header authenticate(Credentials credentials, HttpRequest request, HttpContext context)
    throws AuthenticationException
  {
    Args.notNull(request, "HTTP request");
    switch (1.$SwitchMap$org$apache$http$impl$auth$GGSSchemeBase$State[state.ordinal()]) {
    case 1: 
      throw new AuthenticationException(getSchemeName() + " authentication has not been initiated");
    case 2: 
      throw new AuthenticationException(getSchemeName() + " authentication has failed");
    case 3: 
      try {
        HttpRoute route = (HttpRoute)context.getAttribute("http.route");
        if (route == null) {
          throw new AuthenticationException("Connection route is not available");
        }
        HttpHost host;
        if (isProxy()) {
          HttpHost host = route.getProxyHost();
          if (host == null) {
            host = route.getTargetHost();
          }
        } else {
          host = route.getTargetHost();
        }
        
        String hostname = host.getHostName();
        
        if (useCanonicalHostname)
        {

          try
          {

            hostname = resolveCanonicalHostname(hostname);
          } catch (UnknownHostException ignore) {} }
        String authServer;
        String authServer;
        if (stripPort) {
          authServer = hostname;
        } else {
          authServer = hostname + ":" + host.getPort();
        }
        
        if (log.isDebugEnabled()) {
          log.debug("init " + authServer);
        }
        token = generateToken(token, authServer, credentials);
        state = State.TOKEN_GENERATED;
      } catch (GSSException gsse) {
        state = State.FAILED;
        if ((gsse.getMajor() == 9) || (gsse.getMajor() == 8))
        {
          throw new InvalidCredentialsException(gsse.getMessage(), gsse);
        }
        if (gsse.getMajor() == 13) {
          throw new InvalidCredentialsException(gsse.getMessage(), gsse);
        }
        if ((gsse.getMajor() == 10) || (gsse.getMajor() == 19) || (gsse.getMajor() == 20))
        {

          throw new AuthenticationException(gsse.getMessage(), gsse);
        }
        
        throw new AuthenticationException(gsse.getMessage());
      }
    case 4: 
      String tokenstr = new String(base64codec.encode(token));
      if (log.isDebugEnabled()) {
        log.debug("Sending response '" + tokenstr + "' back to the auth server");
      }
      CharArrayBuffer buffer = new CharArrayBuffer(32);
      if (isProxy()) {
        buffer.append("Proxy-Authorization");
      } else {
        buffer.append("Authorization");
      }
      buffer.append(": Negotiate ");
      buffer.append(tokenstr);
      return new BufferedHeader(buffer);
    }
    throw new IllegalStateException("Illegal state: " + state);
  }
  


  protected void parseChallenge(CharArrayBuffer buffer, int beginIndex, int endIndex)
    throws MalformedChallengeException
  {
    String challenge = buffer.substringTrimmed(beginIndex, endIndex);
    if (log.isDebugEnabled()) {
      log.debug("Received challenge '" + challenge + "' from the auth server");
    }
    if (state == State.UNINITIATED) {
      token = Base64.decodeBase64(challenge.getBytes());
      state = State.CHALLENGE_RECEIVED;
    } else {
      log.debug("Authentication already attempted");
      state = State.FAILED;
    }
  }
  
  private String resolveCanonicalHostname(String host) throws UnknownHostException {
    InetAddress in = InetAddress.getByName(host);
    String canonicalServer = in.getCanonicalHostName();
    if (in.getHostAddress().contentEquals(canonicalServer)) {
      return host;
    }
    return canonicalServer;
  }
}
