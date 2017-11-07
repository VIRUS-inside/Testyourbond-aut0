package com.google.common.cache;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;

































































@GwtIncompatible
public final class CacheBuilderSpec
{
  private static final Splitter KEYS_SPLITTER = Splitter.on(',').trimResults();
  

  private static final Splitter KEY_VALUE_SPLITTER = Splitter.on('=').trimResults();
  


  private static final ImmutableMap<String, ValueParser> VALUE_PARSERS = ImmutableMap.builder()
    .put("initialCapacity", new InitialCapacityParser())
    .put("maximumSize", new MaximumSizeParser())
    .put("maximumWeight", new MaximumWeightParser())
    .put("concurrencyLevel", new ConcurrencyLevelParser())
    .put("weakKeys", new KeyStrengthParser(LocalCache.Strength.WEAK))
    .put("softValues", new ValueStrengthParser(LocalCache.Strength.SOFT))
    .put("weakValues", new ValueStrengthParser(LocalCache.Strength.WEAK))
    .put("recordStats", new RecordStatsParser())
    .put("expireAfterAccess", new AccessDurationParser())
    .put("expireAfterWrite", new WriteDurationParser())
    .put("refreshAfterWrite", new RefreshDurationParser())
    .put("refreshInterval", new RefreshDurationParser())
    .build();
  

  @VisibleForTesting
  Integer initialCapacity;
  
  @VisibleForTesting
  Long maximumSize;
  @VisibleForTesting
  Long maximumWeight;
  @VisibleForTesting
  Integer concurrencyLevel;
  @VisibleForTesting
  LocalCache.Strength keyStrength;
  @VisibleForTesting
  LocalCache.Strength valueStrength;
  @VisibleForTesting
  Boolean recordStats;
  
  private CacheBuilderSpec(String specification) { this.specification = specification; }
  
  @VisibleForTesting
  long writeExpirationDuration;
  @VisibleForTesting
  TimeUnit writeExpirationTimeUnit;
  @VisibleForTesting
  long accessExpirationDuration;
  
  public static CacheBuilderSpec parse(String cacheBuilderSpecification) { CacheBuilderSpec spec = new CacheBuilderSpec(cacheBuilderSpecification);
    if (!cacheBuilderSpecification.isEmpty()) {
      for (String keyValuePair : KEYS_SPLITTER.split(cacheBuilderSpecification)) {
        List<String> keyAndValue = ImmutableList.copyOf(KEY_VALUE_SPLITTER.split(keyValuePair));
        Preconditions.checkArgument(!keyAndValue.isEmpty(), "blank key-value pair");
        Preconditions.checkArgument(keyAndValue
          .size() <= 2, "key-value pair %s with more than one equals sign", keyValuePair);
        



        String key = (String)keyAndValue.get(0);
        ValueParser valueParser = (ValueParser)VALUE_PARSERS.get(key);
        Preconditions.checkArgument(valueParser != null, "unknown key %s", key);
        
        String value = keyAndValue.size() == 1 ? null : (String)keyAndValue.get(1);
        valueParser.parse(spec, key, value);
      }
    }
    
    return spec;
  }
  



  public static CacheBuilderSpec disableCaching()
  {
    return parse("maximumSize=0");
  }
  


  CacheBuilder<Object, Object> toCacheBuilder()
  {
    CacheBuilder<Object, Object> builder = CacheBuilder.newBuilder();
    if (initialCapacity != null) {
      builder.initialCapacity(initialCapacity.intValue());
    }
    if (maximumSize != null) {
      builder.maximumSize(maximumSize.longValue());
    }
    if (maximumWeight != null) {
      builder.maximumWeight(maximumWeight.longValue());
    }
    if (concurrencyLevel != null) {
      builder.concurrencyLevel(concurrencyLevel.intValue());
    }
    if (keyStrength != null) {
      switch (1.$SwitchMap$com$google$common$cache$LocalCache$Strength[keyStrength.ordinal()]) {
      case 1: 
        builder.weakKeys();
        break;
      default: 
        throw new AssertionError();
      }
    }
    if (valueStrength != null) {
      switch (1.$SwitchMap$com$google$common$cache$LocalCache$Strength[valueStrength.ordinal()]) {
      case 2: 
        builder.softValues();
        break;
      case 1: 
        builder.weakValues();
        break;
      default: 
        throw new AssertionError();
      }
    }
    if ((recordStats != null) && (recordStats.booleanValue())) {
      builder.recordStats();
    }
    if (writeExpirationTimeUnit != null) {
      builder.expireAfterWrite(writeExpirationDuration, writeExpirationTimeUnit);
    }
    if (accessExpirationTimeUnit != null) {
      builder.expireAfterAccess(accessExpirationDuration, accessExpirationTimeUnit);
    }
    if (refreshTimeUnit != null) {
      builder.refreshAfterWrite(refreshDuration, refreshTimeUnit);
    }
    
    return builder;
  }
  




  public String toParsableString()
  {
    return specification;
  }
  




  public String toString()
  {
    return MoreObjects.toStringHelper(this).addValue(toParsableString()).toString();
  }
  
  public int hashCode()
  {
    return Objects.hashCode(new Object[] { initialCapacity, maximumSize, maximumWeight, concurrencyLevel, keyStrength, valueStrength, recordStats, 
    






      durationInNanos(writeExpirationDuration, writeExpirationTimeUnit), 
      durationInNanos(accessExpirationDuration, accessExpirationTimeUnit), 
      durationInNanos(refreshDuration, refreshTimeUnit) });
  }
  
  public boolean equals(@Nullable Object obj)
  {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof CacheBuilderSpec)) {
      return false;
    }
    CacheBuilderSpec that = (CacheBuilderSpec)obj;
    if ((Objects.equal(initialCapacity, initialCapacity)) && 
      (Objects.equal(maximumSize, maximumSize)) && 
      (Objects.equal(maximumWeight, maximumWeight)) && 
      (Objects.equal(concurrencyLevel, concurrencyLevel)) && 
      (Objects.equal(keyStrength, keyStrength)) && 
      (Objects.equal(valueStrength, valueStrength)) && 
      (Objects.equal(recordStats, recordStats)) && 
      (Objects.equal(
      durationInNanos(writeExpirationDuration, writeExpirationTimeUnit), 
      durationInNanos(writeExpirationDuration, writeExpirationTimeUnit)))) {
      if (!Objects.equal(
        durationInNanos(accessExpirationDuration, accessExpirationTimeUnit), 
        durationInNanos(accessExpirationDuration, accessExpirationTimeUnit))) {}
    }
    return 
    











      Objects.equal(
      durationInNanos(refreshDuration, refreshTimeUnit), 
      durationInNanos(refreshDuration, refreshTimeUnit));
  }
  
  @VisibleForTesting
  TimeUnit accessExpirationTimeUnit;
  @VisibleForTesting
  long refreshDuration;
  
  @Nullable
  private static Long durationInNanos(long duration, @Nullable TimeUnit unit) { return unit == null ? null : Long.valueOf(unit.toNanos(duration)); }
  
  private static abstract interface ValueParser { public abstract void parse(CacheBuilderSpec paramCacheBuilderSpec, String paramString1, @Nullable String paramString2);
  }
  
  static abstract class IntegerParser implements CacheBuilderSpec.ValueParser { IntegerParser() {}
    
    protected abstract void parseInteger(CacheBuilderSpec paramCacheBuilderSpec, int paramInt);
    
    public void parse(CacheBuilderSpec spec, String key, String value) { Preconditions.checkArgument((value != null) && (!value.isEmpty()), "value of key %s omitted", key);
      try {
        parseInteger(spec, Integer.parseInt(value));
      }
      catch (NumberFormatException e) {
        throw new IllegalArgumentException(CacheBuilderSpec.format("key %s value set to %s, must be integer", new Object[] { key, value }), e);
      }
    } }
  
  @VisibleForTesting
  TimeUnit refreshTimeUnit;
  private final String specification;
  static abstract class LongParser implements CacheBuilderSpec.ValueParser { LongParser() {}
    
    protected abstract void parseLong(CacheBuilderSpec paramCacheBuilderSpec, long paramLong);
    
    public void parse(CacheBuilderSpec spec, String key, String value) { Preconditions.checkArgument((value != null) && (!value.isEmpty()), "value of key %s omitted", key);
      try {
        parseLong(spec, Long.parseLong(value));
      }
      catch (NumberFormatException e) {
        throw new IllegalArgumentException(CacheBuilderSpec.format("key %s value set to %s, must be integer", new Object[] { key, value }), e);
      }
    }
  }
  
  static class InitialCapacityParser extends CacheBuilderSpec.IntegerParser {
    InitialCapacityParser() {}
    
    protected void parseInteger(CacheBuilderSpec spec, int value) {
      Preconditions.checkArgument(initialCapacity == null, "initial capacity was already set to ", initialCapacity);
      


      initialCapacity = Integer.valueOf(value);
    }
  }
  
  static class MaximumSizeParser extends CacheBuilderSpec.LongParser {
    MaximumSizeParser() {}
    
    protected void parseLong(CacheBuilderSpec spec, long value) {
      Preconditions.checkArgument(maximumSize == null, "maximum size was already set to ", maximumSize);
      Preconditions.checkArgument(maximumWeight == null, "maximum weight was already set to ", maximumWeight);
      
      maximumSize = Long.valueOf(value);
    }
  }
  
  static class MaximumWeightParser extends CacheBuilderSpec.LongParser {
    MaximumWeightParser() {}
    
    protected void parseLong(CacheBuilderSpec spec, long value) {
      Preconditions.checkArgument(maximumWeight == null, "maximum weight was already set to ", maximumWeight);
      
      Preconditions.checkArgument(maximumSize == null, "maximum size was already set to ", maximumSize);
      maximumWeight = Long.valueOf(value);
    }
  }
  
  static class ConcurrencyLevelParser extends CacheBuilderSpec.IntegerParser {
    ConcurrencyLevelParser() {}
    
    protected void parseInteger(CacheBuilderSpec spec, int value) {
      Preconditions.checkArgument(concurrencyLevel == null, "concurrency level was already set to ", concurrencyLevel);
      


      concurrencyLevel = Integer.valueOf(value);
    }
  }
  
  static class KeyStrengthParser implements CacheBuilderSpec.ValueParser
  {
    private final LocalCache.Strength strength;
    
    public KeyStrengthParser(LocalCache.Strength strength) {
      this.strength = strength;
    }
    
    public void parse(CacheBuilderSpec spec, String key, @Nullable String value)
    {
      Preconditions.checkArgument(value == null, "key %s does not take values", key);
      Preconditions.checkArgument(keyStrength == null, "%s was already set to %s", key, keyStrength);
      keyStrength = strength;
    }
  }
  
  static class ValueStrengthParser implements CacheBuilderSpec.ValueParser
  {
    private final LocalCache.Strength strength;
    
    public ValueStrengthParser(LocalCache.Strength strength) {
      this.strength = strength;
    }
    
    public void parse(CacheBuilderSpec spec, String key, @Nullable String value)
    {
      Preconditions.checkArgument(value == null, "key %s does not take values", key);
      Preconditions.checkArgument(valueStrength == null, "%s was already set to %s", key, valueStrength);
      

      valueStrength = strength;
    }
  }
  
  static class RecordStatsParser implements CacheBuilderSpec.ValueParser
  {
    RecordStatsParser() {}
    
    public void parse(CacheBuilderSpec spec, String key, @Nullable String value) {
      Preconditions.checkArgument(value == null, "recordStats does not take values");
      Preconditions.checkArgument(recordStats == null, "recordStats already set");
      recordStats = Boolean.valueOf(true);
    }
  }
  
  static abstract class DurationParser implements CacheBuilderSpec.ValueParser {
    DurationParser() {}
    
    protected abstract void parseDuration(CacheBuilderSpec paramCacheBuilderSpec, long paramLong, TimeUnit paramTimeUnit);
    
    public void parse(CacheBuilderSpec spec, String key, String value) {
      Preconditions.checkArgument((value != null) && (!value.isEmpty()), "value of key %s omitted", key);
      try {
        char lastChar = value.charAt(value.length() - 1);
        TimeUnit timeUnit;
        TimeUnit timeUnit; TimeUnit timeUnit; TimeUnit timeUnit; switch (lastChar) {
        case 'd': 
          timeUnit = TimeUnit.DAYS;
          break;
        case 'h': 
          timeUnit = TimeUnit.HOURS;
          break;
        case 'm': 
          timeUnit = TimeUnit.MINUTES;
          break;
        case 's': 
          timeUnit = TimeUnit.SECONDS;
          break;
        
        default: 
          throw new IllegalArgumentException(CacheBuilderSpec.format("key %s invalid format.  was %s, must end with one of [dDhHmMsS]", new Object[] { key, value }));
        }
        
        TimeUnit timeUnit;
        long duration = Long.parseLong(value.substring(0, value.length() - 1));
        parseDuration(spec, duration, timeUnit);
      }
      catch (NumberFormatException e) {
        throw new IllegalArgumentException(CacheBuilderSpec.format("key %s value set to %s, must be integer", new Object[] { key, value }));
      }
    }
  }
  
  static class AccessDurationParser extends CacheBuilderSpec.DurationParser {
    AccessDurationParser() {}
    
    protected void parseDuration(CacheBuilderSpec spec, long duration, TimeUnit unit) {
      Preconditions.checkArgument(accessExpirationTimeUnit == null, "expireAfterAccess already set");
      accessExpirationDuration = duration;
      accessExpirationTimeUnit = unit;
    }
  }
  
  static class WriteDurationParser extends CacheBuilderSpec.DurationParser {
    WriteDurationParser() {}
    
    protected void parseDuration(CacheBuilderSpec spec, long duration, TimeUnit unit) {
      Preconditions.checkArgument(writeExpirationTimeUnit == null, "expireAfterWrite already set");
      writeExpirationDuration = duration;
      writeExpirationTimeUnit = unit;
    }
  }
  
  static class RefreshDurationParser extends CacheBuilderSpec.DurationParser {
    RefreshDurationParser() {}
    
    protected void parseDuration(CacheBuilderSpec spec, long duration, TimeUnit unit) {
      Preconditions.checkArgument(refreshTimeUnit == null, "refreshAfterWrite already set");
      refreshDuration = duration;
      refreshTimeUnit = unit;
    }
  }
  
  private static String format(String format, Object... args) {
    return String.format(Locale.ROOT, format, args);
  }
}
