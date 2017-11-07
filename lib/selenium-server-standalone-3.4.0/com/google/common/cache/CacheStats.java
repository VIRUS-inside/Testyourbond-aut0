package com.google.common.cache;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import javax.annotation.Nullable;
























































@GwtCompatible
public final class CacheStats
{
  private final long hitCount;
  private final long missCount;
  private final long loadSuccessCount;
  private final long loadExceptionCount;
  private final long totalLoadTime;
  private final long evictionCount;
  
  public CacheStats(long hitCount, long missCount, long loadSuccessCount, long loadExceptionCount, long totalLoadTime, long evictionCount)
  {
    Preconditions.checkArgument(hitCount >= 0L);
    Preconditions.checkArgument(missCount >= 0L);
    Preconditions.checkArgument(loadSuccessCount >= 0L);
    Preconditions.checkArgument(loadExceptionCount >= 0L);
    Preconditions.checkArgument(totalLoadTime >= 0L);
    Preconditions.checkArgument(evictionCount >= 0L);
    
    this.hitCount = hitCount;
    this.missCount = missCount;
    this.loadSuccessCount = loadSuccessCount;
    this.loadExceptionCount = loadExceptionCount;
    this.totalLoadTime = totalLoadTime;
    this.evictionCount = evictionCount;
  }
  



  public long requestCount()
  {
    return hitCount + missCount;
  }
  


  public long hitCount()
  {
    return hitCount;
  }
  




  public double hitRate()
  {
    long requestCount = requestCount();
    return requestCount == 0L ? 1.0D : hitCount / requestCount;
  }
  





  public long missCount()
  {
    return missCount;
  }
  








  public double missRate()
  {
    long requestCount = requestCount();
    return requestCount == 0L ? 0.0D : missCount / requestCount;
  }
  




  public long loadCount()
  {
    return loadSuccessCount + loadExceptionCount;
  }
  









  public long loadSuccessCount()
  {
    return loadSuccessCount;
  }
  









  public long loadExceptionCount()
  {
    return loadExceptionCount;
  }
  




  public double loadExceptionRate()
  {
    long totalLoadCount = loadSuccessCount + loadExceptionCount;
    return totalLoadCount == 0L ? 0.0D : loadExceptionCount / totalLoadCount;
  }
  




  public long totalLoadTime()
  {
    return totalLoadTime;
  }
  



  public double averageLoadPenalty()
  {
    long totalLoadCount = loadSuccessCount + loadExceptionCount;
    return totalLoadCount == 0L ? 0.0D : totalLoadTime / totalLoadCount;
  }
  



  public long evictionCount()
  {
    return evictionCount;
  }
  




  public CacheStats minus(CacheStats other)
  {
    return new CacheStats(
      Math.max(0L, hitCount - hitCount), 
      Math.max(0L, missCount - missCount), 
      Math.max(0L, loadSuccessCount - loadSuccessCount), 
      Math.max(0L, loadExceptionCount - loadExceptionCount), 
      Math.max(0L, totalLoadTime - totalLoadTime), 
      Math.max(0L, evictionCount - evictionCount));
  }
  





  public CacheStats plus(CacheStats other)
  {
    return new CacheStats(hitCount + hitCount, missCount + missCount, loadSuccessCount + loadSuccessCount, loadExceptionCount + loadExceptionCount, totalLoadTime + totalLoadTime, evictionCount + evictionCount);
  }
  






  public int hashCode()
  {
    return Objects.hashCode(new Object[] {
      Long.valueOf(hitCount), Long.valueOf(missCount), Long.valueOf(loadSuccessCount), Long.valueOf(loadExceptionCount), Long.valueOf(totalLoadTime), Long.valueOf(evictionCount) });
  }
  
  public boolean equals(@Nullable Object object)
  {
    if ((object instanceof CacheStats)) {
      CacheStats other = (CacheStats)object;
      return (hitCount == hitCount) && (missCount == missCount) && (loadSuccessCount == loadSuccessCount) && (loadExceptionCount == loadExceptionCount) && (totalLoadTime == totalLoadTime) && (evictionCount == evictionCount);
    }
    




    return false;
  }
  
  public String toString()
  {
    return 
    





      MoreObjects.toStringHelper(this).add("hitCount", hitCount).add("missCount", missCount).add("loadSuccessCount", loadSuccessCount).add("loadExceptionCount", loadExceptionCount).add("totalLoadTime", totalLoadTime).add("evictionCount", evictionCount).toString();
  }
}
