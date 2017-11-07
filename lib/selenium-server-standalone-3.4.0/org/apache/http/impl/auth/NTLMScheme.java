package org.apache.http.impl.auth;

import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.InvalidCredentialsException;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.auth.NTCredentials;
import org.apache.http.message.BufferedHeader;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;




























public class NTLMScheme
  extends AuthSchemeBase
{
  private final NTLMEngine engine;
  private State state;
  private String challenge;
  
  static enum State
  {
    UNINITIATED, 
    CHALLENGE_RECEIVED, 
    MSG_TYPE1_GENERATED, 
    MSG_TYPE2_RECEVIED, 
    MSG_TYPE3_GENERATED, 
    FAILED;
    


    private State() {}
  }
  

  public NTLMScheme(NTLMEngine engine)
  {
    Args.notNull(engine, "NTLM engine");
    this.engine = engine;
    state = State.UNINITIATED;
    challenge = null;
  }
  


  public NTLMScheme()
  {
    this(new NTLMEngineImpl());
  }
  
  public String getSchemeName()
  {
    return "ntlm";
  }
  

  public String getParameter(String name)
  {
    return null;
  }
  

  public String getRealm()
  {
    return null;
  }
  
  public boolean isConnectionBased()
  {
    return true;
  }
  

  protected void parseChallenge(CharArrayBuffer buffer, int beginIndex, int endIndex)
    throws MalformedChallengeException
  {
    challenge = buffer.substringTrimmed(beginIndex, endIndex);
    if (challenge.isEmpty()) {
      if (state == State.UNINITIATED) {
        state = State.CHALLENGE_RECEIVED;
      } else {
        state = State.FAILED;
      }
    } else {
      if (state.compareTo(State.MSG_TYPE1_GENERATED) < 0) {
        state = State.FAILED;
        throw new MalformedChallengeException("Out of sequence NTLM response message"); }
      if (state == State.MSG_TYPE1_GENERATED) {
        state = State.MSG_TYPE2_RECEVIED;
      }
    }
  }
  

  public Header authenticate(Credentials credentials, HttpRequest request)
    throws AuthenticationException
  {
    NTCredentials ntcredentials = null;
    try {
      ntcredentials = (NTCredentials)credentials;
    } catch (ClassCastException e) {
      throw new InvalidCredentialsException("Credentials cannot be used for NTLM authentication: " + credentials.getClass().getName());
    }
    

    String response = null;
    if (state == State.FAILED)
      throw new AuthenticationException("NTLM authentication failed");
    if (state == State.CHALLENGE_RECEIVED) {
      response = engine.generateType1Msg(ntcredentials.getDomain(), ntcredentials.getWorkstation());
      

      state = State.MSG_TYPE1_GENERATED;
    } else if (state == State.MSG_TYPE2_RECEVIED) {
      response = engine.generateType3Msg(ntcredentials.getUserName(), ntcredentials.getPassword(), ntcredentials.getDomain(), ntcredentials.getWorkstation(), challenge);
      




      state = State.MSG_TYPE3_GENERATED;
    } else {
      throw new AuthenticationException("Unexpected state: " + state);
    }
    CharArrayBuffer buffer = new CharArrayBuffer(32);
    if (isProxy()) {
      buffer.append("Proxy-Authorization");
    } else {
      buffer.append("Authorization");
    }
    buffer.append(": NTLM ");
    buffer.append(response);
    return new BufferedHeader(buffer);
  }
  
  public boolean isComplete()
  {
    return (state == State.MSG_TYPE3_GENERATED) || (state == State.FAILED);
  }
}
