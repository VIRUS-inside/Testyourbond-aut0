package com.google.common.hash;

import com.google.common.base.Preconditions;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;



















final class MacHashFunction
  extends AbstractStreamingHashFunction
{
  private final Mac prototype;
  private final Key key;
  private final String toString;
  private final int bits;
  private final boolean supportsClone;
  
  MacHashFunction(String algorithmName, Key key, String toString)
  {
    prototype = getMac(algorithmName, key);
    this.key = ((Key)Preconditions.checkNotNull(key));
    this.toString = ((String)Preconditions.checkNotNull(toString));
    bits = (prototype.getMacLength() * 8);
    supportsClone = supportsClone(prototype);
  }
  
  public int bits()
  {
    return bits;
  }
  
  private static boolean supportsClone(Mac mac) {
    try {
      mac.clone();
      return true;
    } catch (CloneNotSupportedException e) {}
    return false;
  }
  
  private static Mac getMac(String algorithmName, Key key)
  {
    try {
      Mac mac = Mac.getInstance(algorithmName);
      mac.init(key);
      return mac;
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException(e);
    } catch (InvalidKeyException e) {
      throw new IllegalArgumentException(e);
    }
  }
  
  public Hasher newHasher()
  {
    if (supportsClone) {
      try {
        return new MacHasher((Mac)prototype.clone(), null);
      }
      catch (CloneNotSupportedException localCloneNotSupportedException) {}
    }
    
    return new MacHasher(getMac(prototype.getAlgorithm(), key), null);
  }
  
  public String toString()
  {
    return toString;
  }
  
  private static final class MacHasher
    extends AbstractByteHasher
  {
    private final Mac mac;
    private boolean done;
    
    private MacHasher(Mac mac)
    {
      this.mac = mac;
    }
    
    protected void update(byte b)
    {
      checkNotDone();
      mac.update(b);
    }
    
    protected void update(byte[] b)
    {
      checkNotDone();
      mac.update(b);
    }
    
    protected void update(byte[] b, int off, int len)
    {
      checkNotDone();
      mac.update(b, off, len);
    }
    
    private void checkNotDone() {
      Preconditions.checkState(!done, "Cannot re-use a Hasher after calling hash() on it");
    }
    
    public HashCode hash()
    {
      checkNotDone();
      done = true;
      return HashCode.fromBytesNoCopy(mac.doFinal());
    }
  }
}
