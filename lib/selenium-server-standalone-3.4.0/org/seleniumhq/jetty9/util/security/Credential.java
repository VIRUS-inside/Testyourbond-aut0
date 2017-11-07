package org.seleniumhq.jetty9.util.security;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ServiceLoader;
import org.seleniumhq.jetty9.util.TypeUtil;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;



































public abstract class Credential
  implements Serializable
{
  private static final ServiceLoader<CredentialProvider> CREDENTIAL_PROVIDER_LOADER = ServiceLoader.load(CredentialProvider.class);
  
  private static final Logger LOG = Log.getLogger(Credential.class);
  




  private static final long serialVersionUID = -7760551052768181572L;
  





  public Credential() {}
  




  public abstract boolean check(Object paramObject);
  




  public static Credential getCredential(String credential)
  {
    if (credential.startsWith("CRYPT:"))
      return new Crypt(credential);
    if (credential.startsWith("MD5:")) {
      return new MD5(credential);
    }
    for (CredentialProvider cp : CREDENTIAL_PROVIDER_LOADER)
    {
      if (credential.startsWith(cp.getPrefix()))
      {
        Credential credentialObj = cp.getCredential(credential);
        if (credentialObj != null)
        {
          return credentialObj;
        }
      }
    }
    
    return new Password(credential);
  }
  


  public static class Crypt
    extends Credential
  {
    private static final long serialVersionUID = -2027792997664744210L;
    
    public static final String __TYPE = "CRYPT:";
    
    private final String _cooked;
    

    Crypt(String cooked)
    {
      _cooked = (cooked.startsWith("CRYPT:") ? cooked.substring("CRYPT:".length()) : cooked);
    }
    

    public boolean check(Object credentials)
    {
      if ((credentials instanceof char[]))
        credentials = new String((char[])credentials);
      if ((!(credentials instanceof String)) && (!(credentials instanceof Password))) {
        Credential.LOG.warn("Can't check " + credentials.getClass() + " against CRYPT", new Object[0]);
      }
      String passwd = credentials.toString();
      return _cooked.equals(UnixCrypt.crypt(passwd, _cooked));
    }
    

    public boolean equals(Object credential)
    {
      if (!(credential instanceof Crypt)) {
        return false;
      }
      Crypt c = (Crypt)credential;
      
      return _cooked.equals(_cooked);
    }
    
    public static String crypt(String user, String pw)
    {
      return "CRYPT:" + UnixCrypt.crypt(pw, user);
    }
  }
  


  public static class MD5
    extends Credential
  {
    private static final long serialVersionUID = 5533846540822684240L;
    

    public static final String __TYPE = "MD5:";
    
    public static final Object __md5Lock = new Object();
    
    private static MessageDigest __md;
    
    private final byte[] _digest;
    

    MD5(String digest)
    {
      digest = digest.startsWith("MD5:") ? digest.substring("MD5:".length()) : digest;
      _digest = TypeUtil.parseBytes(digest, 16);
    }
    

    public byte[] getDigest()
    {
      return _digest;
    }
    


    public boolean check(Object credentials)
    {
      try
      {
        byte[] digest = null;
        
        if ((credentials instanceof char[]))
          credentials = new String((char[])credentials);
        if (((credentials instanceof Password)) || ((credentials instanceof String)))
        {
          synchronized (__md5Lock)
          {
            if (__md == null)
              __md = MessageDigest.getInstance("MD5");
            __md.reset();
            __md.update(credentials.toString().getBytes(StandardCharsets.ISO_8859_1));
            digest = __md.digest();
          }
          if ((digest == null) || (digest.length != _digest.length))
            return false;
          boolean digestMismatch = false;
          for (int i = 0; i < digest.length; i++)
            digestMismatch |= digest[i] != _digest[i];
          return !digestMismatch;
        }
        if ((credentials instanceof MD5))
        {
          return equals((MD5)credentials);
        }
        if ((credentials instanceof Credential))
        {


          return ((Credential)credentials).check(this);
        }
        

        Credential.LOG.warn("Can't check " + credentials.getClass() + " against MD5", new Object[0]);
        return false;

      }
      catch (Exception e)
      {
        Credential.LOG.warn(e); }
      return false;
    }
    


    public boolean equals(Object obj)
    {
      if ((obj instanceof MD5))
      {
        MD5 md5 = (MD5)obj;
        if (_digest.length != _digest.length)
          return false;
        boolean digestMismatch = false;
        for (int i = 0; i < _digest.length; i++)
          digestMismatch |= _digest[i] != _digest[i];
        return !digestMismatch;
      }
      
      return false;
    }
    

    public static String digest(String password)
    {
      try
      {
        byte[] digest;
        synchronized (__md5Lock)
        {
          if (__md == null)
          {
            try
            {
              __md = MessageDigest.getInstance("MD5");
            }
            catch (Exception e)
            {
              Credential.LOG.warn(e);
              return null;
            }
          }
          
          __md.reset();
          __md.update(password.getBytes(StandardCharsets.ISO_8859_1));
          digest = __md.digest();
        }
        byte[] digest;
        return "MD5:" + TypeUtil.toString(digest, 16);
      }
      catch (Exception e)
      {
        Credential.LOG.warn(e); }
      return null;
    }
  }
}
