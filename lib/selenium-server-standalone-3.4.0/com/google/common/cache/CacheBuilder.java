package com.google.common.cache;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Ascii;
import com.google.common.base.Equivalence;
import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.base.Ticker;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.CheckReturnValue;

































































































































@GwtCompatible(emulated=true)
public final class CacheBuilder<K, V>
{
  private static final int DEFAULT_INITIAL_CAPACITY = 16;
  private static final int DEFAULT_CONCURRENCY_LEVEL = 4;
  private static final int DEFAULT_EXPIRATION_NANOS = 0;
  private static final int DEFAULT_REFRESH_NANOS = 0;
  static final Supplier<? extends AbstractCache.StatsCounter> NULL_STATS_COUNTER = Suppliers.ofInstance(new AbstractCache.StatsCounter()
  {
    public void recordHits(int count) {}
    


    public void recordMisses(int count) {}
    

    public void recordLoadSuccess(long loadTime) {}
    

    public void recordLoadException(long loadTime) {}
    

    public void recordEviction() {}
    

    public CacheStats snapshot()
    {
      return CacheBuilder.EMPTY_STATS;
    }
  });
  




















  static final CacheStats EMPTY_STATS = new CacheStats(0L, 0L, 0L, 0L, 0L, 0L);
  
  static final Supplier<AbstractCache.StatsCounter> CACHE_STATS_COUNTER = new Supplier()
  {

    public AbstractCache.StatsCounter get() {
      return new AbstractCache.SimpleStatsCounter(); }
  };
  CacheBuilder() {}
  
  static enum NullListener implements RemovalListener<Object, Object> {
    INSTANCE;
    
    private NullListener() {}
    
    public void onRemoval(RemovalNotification<Object, Object> notification) {}
  }
  
  static enum OneWeigher implements Weigher<Object, Object> { INSTANCE;
    
    private OneWeigher() {}
    
    public int weigh(Object key, Object value) { return 1; }
  }
  

  static final Ticker NULL_TICKER = new Ticker()
  {
    public long read()
    {
      return 0L;
    }
  };
  
  private static final Logger logger = Logger.getLogger(CacheBuilder.class.getName());
  
  static final int UNSET_INT = -1;
  
  boolean strictParsing = true;
  
  int initialCapacity = -1;
  int concurrencyLevel = -1;
  long maximumSize = -1L;
  long maximumWeight = -1L;
  
  Weigher<? super K, ? super V> weigher;
  
  LocalCache.Strength keyStrength;
  LocalCache.Strength valueStrength;
  long expireAfterWriteNanos = -1L;
  long expireAfterAccessNanos = -1L;
  long refreshNanos = -1L;
  
  Equivalence<Object> keyEquivalence;
  
  Equivalence<Object> valueEquivalence;
  
  RemovalListener<? super K, ? super V> removalListener;
  Ticker ticker;
  Supplier<? extends AbstractCache.StatsCounter> statsCounterSupplier = NULL_STATS_COUNTER;
  






  public static CacheBuilder<Object, Object> newBuilder()
  {
    return new CacheBuilder();
  }
  




  @GwtIncompatible
  public static CacheBuilder<Object, Object> from(CacheBuilderSpec spec)
  {
    return spec.toCacheBuilder().lenientParsing();
  }
  






  @GwtIncompatible
  public static CacheBuilder<Object, Object> from(String spec)
  {
    return from(CacheBuilderSpec.parse(spec));
  }
  




  @GwtIncompatible
  CacheBuilder<K, V> lenientParsing()
  {
    strictParsing = false;
    return this;
  }
  







  @GwtIncompatible
  CacheBuilder<K, V> keyEquivalence(Equivalence<Object> equivalence)
  {
    Preconditions.checkState(keyEquivalence == null, "key equivalence was already set to %s", keyEquivalence);
    keyEquivalence = ((Equivalence)Preconditions.checkNotNull(equivalence));
    return this;
  }
  
  Equivalence<Object> getKeyEquivalence() {
    return (Equivalence)MoreObjects.firstNonNull(keyEquivalence, getKeyStrength().defaultEquivalence());
  }
  








  @GwtIncompatible
  CacheBuilder<K, V> valueEquivalence(Equivalence<Object> equivalence)
  {
    Preconditions.checkState(valueEquivalence == null, "value equivalence was already set to %s", valueEquivalence);
    
    valueEquivalence = ((Equivalence)Preconditions.checkNotNull(equivalence));
    return this;
  }
  
  Equivalence<Object> getValueEquivalence() {
    return (Equivalence)MoreObjects.firstNonNull(valueEquivalence, getValueStrength().defaultEquivalence());
  }
  










  public CacheBuilder<K, V> initialCapacity(int initialCapacity)
  {
    Preconditions.checkState(this.initialCapacity == -1, "initial capacity was already set to %s", this.initialCapacity);
    


    Preconditions.checkArgument(initialCapacity >= 0);
    this.initialCapacity = initialCapacity;
    return this;
  }
  
  int getInitialCapacity() {
    return initialCapacity == -1 ? 16 : initialCapacity;
  }
  






























  public CacheBuilder<K, V> concurrencyLevel(int concurrencyLevel)
  {
    Preconditions.checkState(this.concurrencyLevel == -1, "concurrency level was already set to %s", this.concurrencyLevel);
    


    Preconditions.checkArgument(concurrencyLevel > 0);
    this.concurrencyLevel = concurrencyLevel;
    return this;
  }
  
  int getConcurrencyLevel() {
    return concurrencyLevel == -1 ? 4 : concurrencyLevel;
  }
  















  public CacheBuilder<K, V> maximumSize(long size)
  {
    Preconditions.checkState(maximumSize == -1L, "maximum size was already set to %s", maximumSize);
    
    Preconditions.checkState(maximumWeight == -1L, "maximum weight was already set to %s", maximumWeight);
    


    Preconditions.checkState(weigher == null, "maximum size can not be combined with weigher");
    Preconditions.checkArgument(size >= 0L, "maximum size must not be negative");
    maximumSize = size;
    return this;
  }
  























  @GwtIncompatible
  public CacheBuilder<K, V> maximumWeight(long weight)
  {
    Preconditions.checkState(maximumWeight == -1L, "maximum weight was already set to %s", maximumWeight);
    


    Preconditions.checkState(maximumSize == -1L, "maximum size was already set to %s", maximumSize);
    
    maximumWeight = weight;
    Preconditions.checkArgument(weight >= 0L, "maximum weight must not be negative");
    return this;
  }
  





























  @GwtIncompatible
  public <K1 extends K, V1 extends V> CacheBuilder<K1, V1> weigher(Weigher<? super K1, ? super V1> weigher)
  {
    Preconditions.checkState(this.weigher == null);
    if (strictParsing) {
      Preconditions.checkState(maximumSize == -1L, "weigher can not be combined with maximum size", maximumSize);
    }
    





    CacheBuilder<K1, V1> me = this;
    weigher = ((Weigher)Preconditions.checkNotNull(weigher));
    return me;
  }
  
  long getMaximumWeight() {
    if ((expireAfterWriteNanos == 0L) || (expireAfterAccessNanos == 0L)) {
      return 0L;
    }
    return weigher == null ? maximumSize : maximumWeight;
  }
  

  <K1 extends K, V1 extends V> Weigher<K1, V1> getWeigher()
  {
    return (Weigher)MoreObjects.firstNonNull(weigher, OneWeigher.INSTANCE);
  }
  













  @GwtIncompatible
  public CacheBuilder<K, V> weakKeys()
  {
    return setKeyStrength(LocalCache.Strength.WEAK);
  }
  
  CacheBuilder<K, V> setKeyStrength(LocalCache.Strength strength) {
    Preconditions.checkState(keyStrength == null, "Key strength was already set to %s", keyStrength);
    keyStrength = ((LocalCache.Strength)Preconditions.checkNotNull(strength));
    return this;
  }
  
  LocalCache.Strength getKeyStrength() {
    return (LocalCache.Strength)MoreObjects.firstNonNull(keyStrength, LocalCache.Strength.STRONG);
  }
  
















  @GwtIncompatible
  public CacheBuilder<K, V> weakValues()
  {
    return setValueStrength(LocalCache.Strength.WEAK);
  }
  



















  @GwtIncompatible
  public CacheBuilder<K, V> softValues()
  {
    return setValueStrength(LocalCache.Strength.SOFT);
  }
  
  CacheBuilder<K, V> setValueStrength(LocalCache.Strength strength) {
    Preconditions.checkState(valueStrength == null, "Value strength was already set to %s", valueStrength);
    valueStrength = ((LocalCache.Strength)Preconditions.checkNotNull(strength));
    return this;
  }
  
  LocalCache.Strength getValueStrength() {
    return (LocalCache.Strength)MoreObjects.firstNonNull(valueStrength, LocalCache.Strength.STRONG);
  }
  


















  public CacheBuilder<K, V> expireAfterWrite(long duration, TimeUnit unit)
  {
    Preconditions.checkState(expireAfterWriteNanos == -1L, "expireAfterWrite was already set to %s ns", expireAfterWriteNanos);
    


    Preconditions.checkArgument(duration >= 0L, "duration cannot be negative: %s %s", duration, unit);
    expireAfterWriteNanos = unit.toNanos(duration);
    return this;
  }
  
  long getExpireAfterWriteNanos() {
    return expireAfterWriteNanos == -1L ? 0L : expireAfterWriteNanos;
  }
  





















  public CacheBuilder<K, V> expireAfterAccess(long duration, TimeUnit unit)
  {
    Preconditions.checkState(expireAfterAccessNanos == -1L, "expireAfterAccess was already set to %s ns", expireAfterAccessNanos);
    


    Preconditions.checkArgument(duration >= 0L, "duration cannot be negative: %s %s", duration, unit);
    expireAfterAccessNanos = unit.toNanos(duration);
    return this;
  }
  
  long getExpireAfterAccessNanos() {
    return expireAfterAccessNanos == -1L ? 0L : expireAfterAccessNanos;
  }
  



























  @GwtIncompatible
  public CacheBuilder<K, V> refreshAfterWrite(long duration, TimeUnit unit)
  {
    Preconditions.checkNotNull(unit);
    Preconditions.checkState(refreshNanos == -1L, "refresh was already set to %s ns", refreshNanos);
    Preconditions.checkArgument(duration > 0L, "duration must be positive: %s %s", duration, unit);
    refreshNanos = unit.toNanos(duration);
    return this;
  }
  
  long getRefreshNanos() {
    return refreshNanos == -1L ? 0L : refreshNanos;
  }
  









  public CacheBuilder<K, V> ticker(Ticker ticker)
  {
    Preconditions.checkState(this.ticker == null);
    this.ticker = ((Ticker)Preconditions.checkNotNull(ticker));
    return this;
  }
  
  Ticker getTicker(boolean recordsTime) {
    if (ticker != null) {
      return ticker;
    }
    return recordsTime ? Ticker.systemTicker() : NULL_TICKER;
  }
  





















  @CheckReturnValue
  public <K1 extends K, V1 extends V> CacheBuilder<K1, V1> removalListener(RemovalListener<? super K1, ? super V1> listener)
  {
    Preconditions.checkState(removalListener == null);
    


    CacheBuilder<K1, V1> me = this;
    removalListener = ((RemovalListener)Preconditions.checkNotNull(listener));
    return me;
  }
  

  <K1 extends K, V1 extends V> RemovalListener<K1, V1> getRemovalListener()
  {
    return 
      (RemovalListener)MoreObjects.firstNonNull(removalListener, NullListener.INSTANCE);
  }
  








  public CacheBuilder<K, V> recordStats()
  {
    statsCounterSupplier = CACHE_STATS_COUNTER;
    return this;
  }
  
  boolean isRecordingStats() {
    return statsCounterSupplier == CACHE_STATS_COUNTER;
  }
  
  Supplier<? extends AbstractCache.StatsCounter> getStatsCounterSupplier() {
    return statsCounterSupplier;
  }
  












  public <K1 extends K, V1 extends V> LoadingCache<K1, V1> build(CacheLoader<? super K1, V1> loader)
  {
    checkWeightWithWeigher();
    return new LocalCache.LocalLoadingCache(this, loader);
  }
  











  public <K1 extends K, V1 extends V> Cache<K1, V1> build()
  {
    checkWeightWithWeigher();
    checkNonLoadingCache();
    return new LocalCache.LocalManualCache(this);
  }
  
  private void checkNonLoadingCache() {
    Preconditions.checkState(refreshNanos == -1L, "refreshAfterWrite requires a LoadingCache");
  }
  
  private void checkWeightWithWeigher() {
    if (weigher == null) {
      Preconditions.checkState(maximumWeight == -1L, "maximumWeight requires weigher");
    }
    else if (strictParsing) {
      Preconditions.checkState(maximumWeight != -1L, "weigher requires maximumWeight");
    }
    else if (maximumWeight == -1L) {
      logger.log(Level.WARNING, "ignoring weigher specified without maximumWeight");
    }
  }
  






  public String toString()
  {
    MoreObjects.ToStringHelper s = MoreObjects.toStringHelper(this);
    if (initialCapacity != -1) {
      s.add("initialCapacity", initialCapacity);
    }
    if (concurrencyLevel != -1) {
      s.add("concurrencyLevel", concurrencyLevel);
    }
    if (maximumSize != -1L) {
      s.add("maximumSize", maximumSize);
    }
    if (maximumWeight != -1L) {
      s.add("maximumWeight", maximumWeight);
    }
    if (expireAfterWriteNanos != -1L) {
      s.add("expireAfterWrite", expireAfterWriteNanos + "ns");
    }
    if (expireAfterAccessNanos != -1L) {
      s.add("expireAfterAccess", expireAfterAccessNanos + "ns");
    }
    if (keyStrength != null) {
      s.add("keyStrength", Ascii.toLowerCase(keyStrength.toString()));
    }
    if (valueStrength != null) {
      s.add("valueStrength", Ascii.toLowerCase(valueStrength.toString()));
    }
    if (keyEquivalence != null) {
      s.addValue("keyEquivalence");
    }
    if (valueEquivalence != null) {
      s.addValue("valueEquivalence");
    }
    if (removalListener != null) {
      s.addValue("removalListener");
    }
    return s.toString();
  }
}
