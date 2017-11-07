package org.apache.xml.serializer.utils;

import java.text.MessageFormat;
import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;


























































































public final class Messages
{
  private final Locale m_locale = Locale.getDefault();
  





  private ListResourceBundle m_resourceBundle;
  





  private String m_resourceBundleName;
  






  Messages(String resourceBundle)
  {
    m_resourceBundleName = resourceBundle;
  }
  



















  private Locale getLocale()
  {
    return m_locale;
  }
  





  private ListResourceBundle getResourceBundle()
  {
    return m_resourceBundle;
  }
  











  public final String createMessage(String msgKey, Object[] args)
  {
    if (m_resourceBundle == null) {
      m_resourceBundle = loadResourceBundle(m_resourceBundleName);
    }
    if (m_resourceBundle != null)
    {
      return createMsg(m_resourceBundle, msgKey, args);
    }
    
    return "Could not load the resource bundles: " + m_resourceBundleName;
  }
  


















  private final String createMsg(ListResourceBundle fResourceBundle, String msgKey, Object[] args)
  {
    String fmsg = null;
    boolean throwex = false;
    String msg = null;
    
    if (msgKey != null) {
      msg = fResourceBundle.getString(msgKey);
    } else {
      msgKey = "";
    }
    if (msg == null)
    {
      throwex = true;
      



      try
      {
        msg = MessageFormat.format("BAD_MSGKEY", new Object[] { msgKey, m_resourceBundleName });



      }
      catch (Exception e)
      {



        msg = "The message key '" + msgKey + "' is not in the message class '" + m_resourceBundleName + "'";

      }
      


    }
    else if (args != null)
    {


      try
      {

        int n = args.length;
        
        for (int i = 0; i < n; i++)
        {
          if (null == args[i]) {
            args[i] = "";
          }
        }
        fmsg = MessageFormat.format(msg, args);

      }
      catch (Exception e)
      {
        throwex = true;
        
        try
        {
          fmsg = MessageFormat.format("BAD_MSGFORMAT", new Object[] { msgKey, m_resourceBundleName });
          


          fmsg = fmsg + " " + msg;

        }
        catch (Exception formatfailed)
        {

          fmsg = "The format of message '" + msgKey + "' in message class '" + m_resourceBundleName + "' failed.";
        }
        
      }
      

    }
    else
    {

      fmsg = msg;
    }
    if (throwex)
    {
      throw new RuntimeException(fmsg);
    }
    
    return fmsg;
  }
  










  private ListResourceBundle loadResourceBundle(String resourceBundle)
    throws MissingResourceException
  {
    m_resourceBundleName = resourceBundle;
    Locale locale = getLocale();
    

    ListResourceBundle lrb;
    
    try
    {
      ResourceBundle rb = ResourceBundle.getBundle(m_resourceBundleName, locale);
      
      lrb = (ListResourceBundle)rb;

    }
    catch (MissingResourceException e)
    {

      try
      {

        lrb = (ListResourceBundle)ResourceBundle.getBundle(m_resourceBundleName, new Locale("en", "US"));



      }
      catch (MissingResourceException e2)
      {



        throw new MissingResourceException("Could not load any resource bundles." + m_resourceBundleName, m_resourceBundleName, "");
      }
    }
    


    m_resourceBundle = lrb;
    return lrb;
  }
  










  private static String getResourceSuffix(Locale locale)
  {
    String suffix = "_" + locale.getLanguage();
    String country = locale.getCountry();
    
    if (country.equals("TW")) {
      suffix = suffix + "_" + country;
    }
    return suffix;
  }
}
