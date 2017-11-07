package org.apache.http.client.protocol;

import java.util.Queue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.auth.AuthOption;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.ContextAwareAuthScheme;
import org.apache.http.auth.Credentials;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Asserts;




























@Deprecated
abstract class RequestAuthenticationBase
  implements HttpRequestInterceptor
{
  final Log log = LogFactory.getLog(getClass());
  


  public RequestAuthenticationBase() {}
  


  void process(AuthState authState, HttpRequest request, HttpContext context)
  {
    AuthScheme authScheme = authState.getAuthScheme();
    Credentials creds = authState.getCredentials();
    switch (1.$SwitchMap$org$apache$http$auth$AuthProtocolState[authState.getState().ordinal()]) {
    case 1: 
      return;
    case 2: 
      ensureAuthScheme(authScheme);
      if (authScheme.isConnectionBased()) {
        return;
      }
      break;
    case 3: 
      Queue<AuthOption> authOptions = authState.getAuthOptions();
      if (authOptions != null) {
        while (!authOptions.isEmpty()) {
          AuthOption authOption = (AuthOption)authOptions.remove();
          authScheme = authOption.getAuthScheme();
          creds = authOption.getCredentials();
          authState.update(authScheme, creds);
          if (log.isDebugEnabled()) {
            log.debug("Generating response to an authentication challenge using " + authScheme.getSchemeName() + " scheme");
          }
          try
          {
            Header header = authenticate(authScheme, creds, request, context);
            request.addHeader(header);
          }
          catch (AuthenticationException ex) {
            if (log.isWarnEnabled()) {
              log.warn(authScheme + " authentication error: " + ex.getMessage());
            }
          }
        }
        return;
      }
      ensureAuthScheme(authScheme);
    }
    
    if (authScheme != null) {
      try {
        Header header = authenticate(authScheme, creds, request, context);
        request.addHeader(header);
      } catch (AuthenticationException ex) {
        if (log.isErrorEnabled()) {
          log.error(authScheme + " authentication error: " + ex.getMessage());
        }
      }
    }
  }
  
  private void ensureAuthScheme(AuthScheme authScheme) {
    Asserts.notNull(authScheme, "Auth scheme");
  }
  


  private Header authenticate(AuthScheme authScheme, Credentials creds, HttpRequest request, HttpContext context)
    throws AuthenticationException
  {
    Asserts.notNull(authScheme, "Auth scheme");
    if ((authScheme instanceof ContextAwareAuthScheme)) {
      return ((ContextAwareAuthScheme)authScheme).authenticate(creds, request, context);
    }
    return authScheme.authenticate(creds, request);
  }
}
