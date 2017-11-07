package com.google.common.hash;

import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;





















final class MessageDigestHashFunction
  extends AbstractStreamingHashFunction
  implements Serializable
{
  private final MessageDigest prototype;
  private final int bytes;
  private final boolean supportsClone;
  private final String toString;
  
  MessageDigestHashFunction(String algorithmName, String toString)
  {
    prototype = getMessageDigest(algorithmName);
    bytes = prototype.getDigestLength();
    this.toString = ((String)Preconditions.checkNotNull(toString));
    supportsClone = supportsClone(prototype);
  }
  
  MessageDigestHashFunction(String algorithmName, int bytes, String toString) {
    this.toString = ((String)Preconditions.checkNotNull(toString));
    prototype = getMessageDigest(algorithmName);
    int maxLength = prototype.getDigestLength();
    Preconditions.checkArgument((bytes >= 4) && (bytes <= maxLength), "bytes (%s) must be >= 4 and < %s", bytes, maxLength);
    
    this.bytes = bytes;
    supportsClone = supportsClone(prototype);
  }
  
  private static boolean supportsClone(MessageDigest digest) {
    try {
      digest.clone();
      return true;
    } catch (CloneNotSupportedException e) {}
    return false;
  }
  

  public int bits()
  {
    return bytes * 8;
  }
  
  public String toString()
  {
    return toString;
  }
  
  private static MessageDigest getMessageDigest(String algorithmName) {
    try {
      return MessageDigest.getInstance(algorithmName);
    } catch (NoSuchAlgorithmException e) {
      throw new AssertionError(e);
    }
  }
  
  public Hasher newHasher()
  {
    if (supportsClone) {
      try {
        return new MessageDigestHasher((MessageDigest)prototype.clone(), bytes, null);
      }
      catch (CloneNotSupportedException localCloneNotSupportedException) {}
    }
    
    return new MessageDigestHasher(getMessageDigest(prototype.getAlgorithm()), bytes, null);
  }
  
  private static final class SerializedForm implements Serializable {
    private final String algorithmName;
    private final int bytes;
    private final String toString;
    private static final long serialVersionUID = 0L;
    
    private SerializedForm(String algorithmName, int bytes, String toString) { this.algorithmName = algorithmName;
      this.bytes = bytes;
      this.toString = toString;
    }
    
    private Object readResolve() {
      return new MessageDigestHashFunction(algorithmName, bytes, toString);
    }
  }
  

  Object writeReplace()
  {
    return new SerializedForm(prototype.getAlgorithm(), bytes, toString, null);
  }
  
  private static final class MessageDigestHasher
    extends AbstractByteHasher
  {
    private final MessageDigest digest;
    private final int bytes;
    private boolean done;
    
    private MessageDigestHasher(MessageDigest digest, int bytes)
    {
      this.digest = digest;
      this.bytes = bytes;
    }
    
    protected void update(byte b)
    {
      checkNotDone();
      digest.update(b);
    }
    
    protected void update(byte[] b)
    {
      checkNotDone();
      digest.update(b);
    }
    
    protected void update(byte[] b, int off, int len)
    {
      checkNotDone();
      digest.update(b, off, len);
    }
    
    private void checkNotDone() {
      Preconditions.checkState(!done, "Cannot re-use a Hasher after calling hash() on it");
    }
    
    public HashCode hash()
    {
      checkNotDone();
      done = true;
      return bytes == digest.getDigestLength() ? 
        HashCode.fromBytesNoCopy(digest.digest()) : 
        HashCode.fromBytesNoCopy(Arrays.copyOf(digest.digest(), bytes));
    }
  }
}
