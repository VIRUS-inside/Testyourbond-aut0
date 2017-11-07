package org.apache.http;

import java.io.Serializable;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.util.Args;






















































@Contract(threading=ThreadingBehavior.IMMUTABLE)
public class ProtocolVersion
  implements Serializable, Cloneable
{
  private static final long serialVersionUID = 8950662842175091068L;
  protected final String protocol;
  protected final int major;
  protected final int minor;
  
  public ProtocolVersion(String protocol, int major, int minor)
  {
    this.protocol = ((String)Args.notNull(protocol, "Protocol name"));
    this.major = Args.notNegative(major, "Protocol minor version");
    this.minor = Args.notNegative(minor, "Protocol minor version");
  }
  




  public final String getProtocol()
  {
    return protocol;
  }
  




  public final int getMajor()
  {
    return major;
  }
  




  public final int getMinor()
  {
    return minor;
  }
  

















  public ProtocolVersion forVersion(int major, int minor)
  {
    if ((major == this.major) && (minor == this.minor)) {
      return this;
    }
    

    return new ProtocolVersion(protocol, major, minor);
  }
  






  public final int hashCode()
  {
    return protocol.hashCode() ^ major * 100000 ^ minor;
  }
  














  public final boolean equals(Object obj)
  {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof ProtocolVersion)) {
      return false;
    }
    ProtocolVersion that = (ProtocolVersion)obj;
    
    return (protocol.equals(protocol)) && (major == major) && (minor == minor);
  }
  












  public boolean isComparable(ProtocolVersion that)
  {
    return (that != null) && (protocol.equals(protocol));
  }
  
















  public int compareToVersion(ProtocolVersion that)
  {
    Args.notNull(that, "Protocol version");
    Args.check(protocol.equals(protocol), "Versions for different protocols cannot be compared: %s %s", new Object[] { this, that });
    
    int delta = getMajor() - that.getMajor();
    if (delta == 0) {
      delta = getMinor() - that.getMinor();
    }
    return delta;
  }
  










  public final boolean greaterEquals(ProtocolVersion version)
  {
    return (isComparable(version)) && (compareToVersion(version) >= 0);
  }
  










  public final boolean lessEquals(ProtocolVersion version)
  {
    return (isComparable(version)) && (compareToVersion(version) <= 0);
  }
  






  public String toString()
  {
    StringBuilder buffer = new StringBuilder();
    buffer.append(protocol);
    buffer.append('/');
    buffer.append(Integer.toString(major));
    buffer.append('.');
    buffer.append(Integer.toString(minor));
    return buffer.toString();
  }
  
  public Object clone() throws CloneNotSupportedException
  {
    return super.clone();
  }
}
