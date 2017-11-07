package com.google.common.hash;

import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.nio.ByteBuffer;
import javax.annotation.Nullable;

































final class SipHashFunction
  extends AbstractStreamingHashFunction
  implements Serializable
{
  private final int c;
  private final int d;
  private final long k0;
  private final long k1;
  private static final long serialVersionUID = 0L;
  
  SipHashFunction(int c, int d, long k0, long k1)
  {
    Preconditions.checkArgument(c > 0, "The number of SipRound iterations (c=%s) during Compression must be positive.", c);
    
    Preconditions.checkArgument(d > 0, "The number of SipRound iterations (d=%s) during Finalization must be positive.", d);
    
    this.c = c;
    this.d = d;
    this.k0 = k0;
    this.k1 = k1;
  }
  
  public int bits()
  {
    return 64;
  }
  
  public Hasher newHasher()
  {
    return new SipHasher(c, d, k0, k1);
  }
  


  public String toString()
  {
    return "Hashing.sipHash" + c + "" + d + "(" + k0 + ", " + k1 + ")";
  }
  
  public boolean equals(@Nullable Object object)
  {
    if ((object instanceof SipHashFunction)) {
      SipHashFunction other = (SipHashFunction)object;
      return (c == c) && (d == d) && (k0 == k0) && (k1 == k1);
    }
    


    return false;
  }
  
  public int hashCode()
  {
    return (int)(getClass().hashCode() ^ c ^ d ^ k0 ^ k1);
  }
  


  private static final class SipHasher
    extends AbstractStreamingHashFunction.AbstractStreamingHasher
  {
    private static final int CHUNK_SIZE = 8;
    

    private final int c;
    
    private final int d;
    
    private long v0 = 8317987319222330741L;
    private long v1 = 7237128888997146477L;
    private long v2 = 7816392313619706465L;
    private long v3 = 8387220255154660723L;
    

    private long b = 0L;
    


    private long finalM = 0L;
    
    SipHasher(int c, int d, long k0, long k1) {
      super();
      this.c = c;
      this.d = d;
      v0 ^= k0;
      v1 ^= k1;
      v2 ^= k0;
      v3 ^= k1;
    }
    
    protected void process(ByteBuffer buffer)
    {
      b += 8L;
      processM(buffer.getLong());
    }
    
    protected void processRemaining(ByteBuffer buffer)
    {
      b += buffer.remaining();
      for (int i = 0; buffer.hasRemaining(); i += 8) {
        finalM ^= (buffer.get() & 0xFF) << i;
      }
    }
    

    public HashCode makeHash()
    {
      finalM ^= b << 56;
      processM(finalM);
      

      v2 ^= 0xFF;
      sipRound(d);
      return HashCode.fromLong(v0 ^ v1 ^ v2 ^ v3);
    }
    
    private void processM(long m) {
      v3 ^= m;
      sipRound(c);
      v0 ^= m;
    }
    
    private void sipRound(int iterations) {
      for (int i = 0; i < iterations; i++) {
        v0 += v1;
        v2 += v3;
        v1 = Long.rotateLeft(v1, 13);
        v3 = Long.rotateLeft(v3, 16);
        v1 ^= v0;
        v3 ^= v2;
        v0 = Long.rotateLeft(v0, 32);
        v2 += v1;
        v0 += v3;
        v1 = Long.rotateLeft(v1, 17);
        v3 = Long.rotateLeft(v3, 21);
        v1 ^= v2;
        v3 ^= v0;
        v2 = Long.rotateLeft(v2, 32);
      }
    }
  }
}
