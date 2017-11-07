package org.seleniumhq.jetty9.util.security;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Locale;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;













































public class Password
  extends Credential
{
  private static final Logger LOG = Log.getLogger(Password.class);
  


  private static final long serialVersionUID = 5062906681431569445L;
  

  public static final String __OBFUSCATE = "OBF:";
  

  private String _pw;
  


  public Password(String password)
  {
    _pw = password;
    

    while ((_pw != null) && (_pw.startsWith("OBF:"))) {
      _pw = deobfuscate(_pw);
    }
  }
  

  public String toString()
  {
    return _pw;
  }
  

  public String toStarString()
  {
    return "*****************************************************".substring(0, _pw.length());
  }
  


  public boolean check(Object credentials)
  {
    if (this == credentials) { return true;
    }
    if ((credentials instanceof Password)) { return credentials.equals(_pw);
    }
    if ((credentials instanceof String)) { return credentials.equals(_pw);
    }
    if ((credentials instanceof char[])) { return Arrays.equals(_pw.toCharArray(), (char[])credentials);
    }
    if ((credentials instanceof Credential)) { return ((Credential)credentials).check(_pw);
    }
    return false;
  }
  


  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if (null == o) {
      return false;
    }
    if ((o instanceof Password))
    {
      Password p = (Password)o;
      
      return (_pw == _pw) || ((null != _pw) && (_pw.equals(_pw)));
    }
    
    if ((o instanceof String)) {
      return o.equals(_pw);
    }
    return false;
  }
  


  public int hashCode()
  {
    return null == _pw ? super.hashCode() : _pw.hashCode();
  }
  

  public static String obfuscate(String s)
  {
    StringBuilder buf = new StringBuilder();
    byte[] b = s.getBytes(StandardCharsets.UTF_8);
    
    buf.append("OBF:");
    for (int i = 0; i < b.length; i++)
    {
      byte b1 = b[i];
      byte b2 = b[(b.length - (i + 1))];
      if ((b1 < 0) || (b2 < 0))
      {
        int i0 = (0xFF & b1) * 256 + (0xFF & b2);
        String x = Integer.toString(i0, 36).toLowerCase(Locale.ENGLISH);
        buf.append("U0000", 0, 5 - x.length());
        buf.append(x);
      }
      else
      {
        int i1 = Byte.MAX_VALUE + b1 + b2;
        int i2 = Byte.MAX_VALUE + b1 - b2;
        int i0 = i1 * 256 + i2;
        String x = Integer.toString(i0, 36).toLowerCase(Locale.ENGLISH);
        
        int j0 = Integer.parseInt(x, 36);
        int j1 = i0 / 256;
        int j2 = i0 % 256;
        byte bx = (byte)((j1 + j2 - 254) / 2);
        
        buf.append("000", 0, 4 - x.length());
        buf.append(x);
      }
    }
    
    return buf.toString();
  }
  


  public static String deobfuscate(String s)
  {
    if (s.startsWith("OBF:")) { s = s.substring(4);
    }
    byte[] b = new byte[s.length() / 2];
    int l = 0;
    for (int i = 0; i < s.length(); i += 4)
    {
      if (s.charAt(i) == 'U')
      {
        i++;
        String x = s.substring(i, i + 4);
        int i0 = Integer.parseInt(x, 36);
        byte bx = (byte)(i0 >> 8);
        b[(l++)] = bx;
      }
      else
      {
        String x = s.substring(i, i + 4);
        int i0 = Integer.parseInt(x, 36);
        int i1 = i0 / 256;
        int i2 = i0 % 256;
        byte bx = (byte)((i1 + i2 - 254) / 2);
        b[(l++)] = bx;
      }
    }
    
    return new String(b, 0, l, StandardCharsets.UTF_8);
  }
  















  public static Password getPassword(String realm, String dft, String promptDft)
  {
    String passwd = System.getProperty(realm, dft);
    if ((passwd == null) || (passwd.length() == 0))
    {
      try
      {
        System.out.print(realm + ((promptDft != null) && (promptDft.length() > 0) ? " [dft]" : "") + " : ");
        System.out.flush();
        byte[] buf = new byte['Ȁ'];
        int len = System.in.read(buf);
        if (len > 0) passwd = new String(buf, 0, len).trim();
      }
      catch (IOException e)
      {
        LOG.warn("EXCEPTION ", e);
      }
      if ((passwd == null) || (passwd.length() == 0)) passwd = promptDft;
    }
    return new Password(passwd);
  }
  
  public static void main(String[] arg)
  {
    if ((arg.length != 1) && (arg.length != 2))
    {
      System.err.println("Usage - java " + Password.class.getName() + " [<user>] <password>");
      System.err.println("If the password is ?, the user will be prompted for the password");
      System.exit(1);
    }
    String p = arg[1];
    Password pw = new Password(p);
    System.err.println(pw.toString());
    System.err.println(obfuscate(pw.toString()));
    System.err.println(Credential.MD5.digest(p));
    if (arg.length == 2) System.err.println(Credential.Crypt.crypt(arg[0], pw.toString()));
  }
}
