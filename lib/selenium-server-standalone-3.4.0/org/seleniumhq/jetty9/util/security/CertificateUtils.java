package org.seleniumhq.jetty9.util.security;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.CRL;
import java.security.cert.CertificateFactory;
import java.util.Collection;
import org.seleniumhq.jetty9.util.resource.Resource;


















public class CertificateUtils
{
  public CertificateUtils() {}
  
  public static KeyStore getKeyStore(Resource store, String storeType, String storeProvider, String storePassword)
    throws Exception
  {
    KeyStore keystore = null;
    
    if (store != null)
    {
      if (storeProvider != null)
      {
        keystore = KeyStore.getInstance(storeType, storeProvider);
      }
      else
      {
        keystore = KeyStore.getInstance(storeType);
      }
      
      if (!store.exists()) {
        throw new IllegalStateException("no valid keystore");
      }
      InputStream inStream = store.getInputStream();Throwable localThrowable3 = null;
      try {
        keystore.load(inStream, storePassword == null ? null : storePassword.toCharArray());
      }
      catch (Throwable localThrowable1)
      {
        localThrowable3 = localThrowable1;throw localThrowable1;
      }
      finally {
        if (inStream != null) if (localThrowable3 != null) try { inStream.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else inStream.close();
      }
    }
    return keystore;
  }
  
  public static Collection<? extends CRL> loadCRL(String crlPath)
    throws Exception
  {
    Collection<? extends CRL> crlList = null;
    
    if (crlPath != null)
    {
      InputStream in = null;
      try
      {
        in = Resource.newResource(crlPath).getInputStream();
        crlList = CertificateFactory.getInstance("X.509").generateCRLs(in);
        


        if (in != null)
        {
          in.close();
        }
      }
      finally
      {
        if (in != null)
        {
          in.close();
        }
      }
    }
    
    return crlList;
  }
}
