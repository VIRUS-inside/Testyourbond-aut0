package org.seleniumhq.jetty9.util;

import java.nio.ByteBuffer;
import java.util.Set;

public abstract interface Trie<V>
{
  public abstract boolean put(String paramString, V paramV);
  
  public abstract boolean put(V paramV);
  
  public abstract V remove(String paramString);
  
  public abstract V get(String paramString);
  
  public abstract V get(String paramString, int paramInt1, int paramInt2);
  
  public abstract V get(ByteBuffer paramByteBuffer);
  
  public abstract V get(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2);
  
  public abstract V getBest(String paramString);
  
  public abstract V getBest(String paramString, int paramInt1, int paramInt2);
  
  public abstract V getBest(byte[] paramArrayOfByte, int paramInt1, int paramInt2);
  
  public abstract V getBest(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2);
  
  public abstract Set<String> keySet();
  
  public abstract boolean isFull();
  
  public abstract boolean isCaseInsensitive();
  
  public abstract void clear();
}
