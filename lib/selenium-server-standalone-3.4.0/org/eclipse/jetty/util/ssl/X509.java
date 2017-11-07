package org.eclipse.jetty.util.ssl;

import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;
import javax.security.auth.x500.X500Principal;
import org.eclipse.jetty.util.StringUtil;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;




















public class X509
{
  private static final Logger LOG = Log.getLogger(X509.class);
  

  private static final int KEY_USAGE__KEY_CERT_SIGN = 5;
  

  private static final int SUBJECT_ALTERNATIVE_NAMES__DNS_NAME = 2;
  
  private final X509Certificate _x509;
  
  private final String _alias;
  

  public static boolean isCertSign(X509Certificate x509)
  {
    boolean[] key_usage = x509.getKeyUsage();
    return (key_usage != null) && (key_usage[5] != 0);
  }
  


  private final List<String> _hosts = new ArrayList();
  private final List<String> _wilds = new ArrayList();
  
  public X509(String alias, X509Certificate x509) throws CertificateParsingException, InvalidNameException
  {
    _alias = alias;
    _x509 = x509;
    

    boolean named = false;
    Collection<List<?>> altNames = x509.getSubjectAlternativeNames();
    Iterator localIterator; if (altNames != null)
    {
      for (localIterator = altNames.iterator(); localIterator.hasNext();) { list = (List)localIterator.next();
        
        if (((Number)list.get(0)).intValue() == 2)
        {
          String cn = list.get(1).toString();
          if (LOG.isDebugEnabled())
            LOG.debug("Certificate SAN alias={} CN={} in {}", new Object[] { alias, cn, this });
          if (cn != null)
          {
            named = true;
            addName(cn);
          }
        }
      }
    }
    
    List<?> list;
    if (!named)
    {
      LdapName name = new LdapName(x509.getSubjectX500Principal().getName("RFC2253"));
      for (Rdn rdn : name.getRdns())
      {
        if (rdn.getType().equalsIgnoreCase("CN"))
        {
          String cn = rdn.getValue().toString();
          if (LOG.isDebugEnabled())
            LOG.debug("Certificate CN alias={} CN={} in {}", new Object[] { alias, cn, this });
          if ((cn != null) && (cn.contains(".")) && (!cn.contains(" "))) {
            addName(cn);
          }
        }
      }
    }
  }
  
  protected void addName(String cn) {
    cn = StringUtil.asciiToLowerCase(cn);
    if (cn.startsWith("*.")) {
      _wilds.add(cn.substring(2));
    } else {
      _hosts.add(cn);
    }
  }
  
  public String getAlias() {
    return _alias;
  }
  
  public X509Certificate getCertificate()
  {
    return _x509;
  }
  
  public Set<String> getHosts()
  {
    return new HashSet(_hosts);
  }
  
  public Set<String> getWilds()
  {
    return new HashSet(_wilds);
  }
  
  public boolean matches(String host)
  {
    host = StringUtil.asciiToLowerCase(host);
    if ((_hosts.contains(host)) || (_wilds.contains(host))) {
      return true;
    }
    int dot = host.indexOf('.');
    if (dot >= 0)
    {
      String domain = host.substring(dot + 1);
      if (_wilds.contains(domain))
        return true;
    }
    return false;
  }
  

  public String toString()
  {
    return String.format("%s@%x(%s,h=%s,w=%s)", new Object[] {
      getClass().getSimpleName(), 
      Integer.valueOf(hashCode()), _alias, _hosts, _wilds });
  }
}
