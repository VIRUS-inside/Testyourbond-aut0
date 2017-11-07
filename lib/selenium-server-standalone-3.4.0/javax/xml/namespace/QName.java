package javax.xml.namespace;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.security.AccessController;
import java.security.PrivilegedAction;

public class QName
  implements Serializable
{
  private static final long serialVersionUID = !"1.0".equals(str) ? -9120448754896609940L : 4418622981026545151L;
  private static final long defaultSerialVersionUID = -9120448754896609940L;
  private static final long compatabilitySerialVersionUID = 4418622981026545151L;
  private final String namespaceURI;
  private final String localPart;
  private String prefix;
  private transient String qNameAsString;
  
  public QName(String paramString1, String paramString2)
  {
    this(paramString1, paramString2, "");
  }
  
  public QName(String paramString1, String paramString2, String paramString3)
  {
    if (paramString1 == null) {
      namespaceURI = "";
    } else {
      namespaceURI = paramString1;
    }
    if (paramString2 == null) {
      throw new IllegalArgumentException("local part cannot be \"null\" when creating a QName");
    }
    localPart = paramString2;
    if (paramString3 == null) {
      throw new IllegalArgumentException("prefix cannot be \"null\" when creating a QName");
    }
    prefix = paramString3;
  }
  
  public QName(String paramString)
  {
    this("", paramString, "");
  }
  
  public String getNamespaceURI()
  {
    return namespaceURI;
  }
  
  public String getLocalPart()
  {
    return localPart;
  }
  
  public String getPrefix()
  {
    return prefix;
  }
  
  public final boolean equals(Object paramObject)
  {
    if (paramObject == this) {
      return true;
    }
    if ((paramObject instanceof QName))
    {
      QName localQName = (QName)paramObject;
      return (localPart.equals(localPart)) && (namespaceURI.equals(namespaceURI));
    }
    return false;
  }
  
  public final int hashCode()
  {
    return namespaceURI.hashCode() ^ localPart.hashCode();
  }
  
  public String toString()
  {
    String str = qNameAsString;
    if (str == null)
    {
      int i = namespaceURI.length();
      if (i == 0)
      {
        str = localPart;
      }
      else
      {
        StringBuffer localStringBuffer = new StringBuffer(i + localPart.length() + 2);
        localStringBuffer.append('{');
        localStringBuffer.append(namespaceURI);
        localStringBuffer.append('}');
        localStringBuffer.append(localPart);
        str = localStringBuffer.toString();
      }
      qNameAsString = str;
    }
    return str;
  }
  
  public static QName valueOf(String paramString)
  {
    if (paramString == null) {
      throw new IllegalArgumentException("cannot create QName from \"null\" or \"\" String");
    }
    if (paramString.length() == 0) {
      return new QName("", paramString, "");
    }
    if (paramString.charAt(0) != '{') {
      return new QName("", paramString, "");
    }
    if (paramString.startsWith("{}")) {
      throw new IllegalArgumentException("Namespace URI .equals(XMLConstants.NULL_NS_URI), .equals(\"\"), only the local part, \"" + paramString.substring(2 + "".length()) + "\", " + "should be provided.");
    }
    int i = paramString.indexOf('}');
    if (i == -1) {
      throw new IllegalArgumentException("cannot create QName from \"" + paramString + "\", missing closing \"}\"");
    }
    return new QName(paramString.substring(1, i), paramString.substring(i + 1), "");
  }
  
  private void readObject(ObjectInputStream paramObjectInputStream)
    throws IOException, ClassNotFoundException
  {
    paramObjectInputStream.defaultReadObject();
    if (prefix == null) {
      prefix = "";
    }
  }
  
  static
  {
    String str = null;
    try
    {
      str = (String)AccessController.doPrivileged(new PrivilegedAction()
      {
        public Object run()
        {
          return System.getProperty("org.apache.xml.namespace.QName.useCompatibleSerialVersionUID");
        }
      });
    }
    catch (Exception localException) {}
  }
}
