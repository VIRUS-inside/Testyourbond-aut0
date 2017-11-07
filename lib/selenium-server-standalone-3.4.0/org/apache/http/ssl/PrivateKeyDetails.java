package org.apache.http.ssl;

import java.security.cert.X509Certificate;
import java.util.Arrays;
import org.apache.http.util.Args;

































public final class PrivateKeyDetails
{
  private final String type;
  private final X509Certificate[] certChain;
  
  public PrivateKeyDetails(String type, X509Certificate[] certChain)
  {
    this.type = ((String)Args.notNull(type, "Private key type"));
    this.certChain = certChain;
  }
  
  public String getType() {
    return type;
  }
  
  public X509Certificate[] getCertChain() {
    return certChain;
  }
  
  public String toString()
  {
    return type + ':' + Arrays.toString(certChain);
  }
}
