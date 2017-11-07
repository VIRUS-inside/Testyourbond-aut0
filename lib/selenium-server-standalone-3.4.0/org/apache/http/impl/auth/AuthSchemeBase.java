package org.apache.http.impl.auth;

import java.util.Locale;
import org.apache.http.FormattedHeader;
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.ChallengeState;
import org.apache.http.auth.ContextAwareAuthScheme;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;














































public abstract class AuthSchemeBase
  implements ContextAwareAuthScheme
{
  protected ChallengeState challengeState;
  
  @Deprecated
  public AuthSchemeBase(ChallengeState challengeState)
  {
    this.challengeState = challengeState;
  }
  






  public AuthSchemeBase() {}
  





  public void processChallenge(Header header)
    throws MalformedChallengeException
  {
    Args.notNull(header, "Header");
    String authheader = header.getName();
    if (authheader.equalsIgnoreCase("WWW-Authenticate")) {
      challengeState = ChallengeState.TARGET;
    } else if (authheader.equalsIgnoreCase("Proxy-Authenticate")) {
      challengeState = ChallengeState.PROXY;
    } else {
      throw new MalformedChallengeException("Unexpected header name: " + authheader);
    }
    int pos;
    CharArrayBuffer buffer;
    int pos;
    if ((header instanceof FormattedHeader)) {
      CharArrayBuffer buffer = ((FormattedHeader)header).getBuffer();
      pos = ((FormattedHeader)header).getValuePos();
    } else {
      String s = header.getValue();
      if (s == null) {
        throw new MalformedChallengeException("Header value is null");
      }
      buffer = new CharArrayBuffer(s.length());
      buffer.append(s);
      pos = 0;
    }
    while ((pos < buffer.length()) && (HTTP.isWhitespace(buffer.charAt(pos)))) {
      pos++;
    }
    int beginIndex = pos;
    while ((pos < buffer.length()) && (!HTTP.isWhitespace(buffer.charAt(pos)))) {
      pos++;
    }
    int endIndex = pos;
    String s = buffer.substring(beginIndex, endIndex);
    if (!s.equalsIgnoreCase(getSchemeName())) {
      throw new MalformedChallengeException("Invalid scheme identifier: " + s);
    }
    
    parseChallenge(buffer, pos, buffer.length());
  }
  




  public Header authenticate(Credentials credentials, HttpRequest request, HttpContext context)
    throws AuthenticationException
  {
    return authenticate(credentials, request);
  }
  


  protected abstract void parseChallenge(CharArrayBuffer paramCharArrayBuffer, int paramInt1, int paramInt2)
    throws MalformedChallengeException;
  

  public boolean isProxy()
  {
    return (challengeState != null) && (challengeState == ChallengeState.PROXY);
  }
  




  public ChallengeState getChallengeState()
  {
    return challengeState;
  }
  
  public String toString()
  {
    String name = getSchemeName();
    if (name != null) {
      return name.toUpperCase(Locale.ROOT);
    }
    return super.toString();
  }
}
