package com.google.common.base;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;













































































@GwtCompatible(emulated=true)
public final class Splitter
{
  private final CharMatcher trimmer;
  private final boolean omitEmptyStrings;
  private final Strategy strategy;
  private final int limit;
  
  private Splitter(Strategy strategy)
  {
    this(strategy, false, CharMatcher.none(), Integer.MAX_VALUE);
  }
  
  private Splitter(Strategy strategy, boolean omitEmptyStrings, CharMatcher trimmer, int limit) {
    this.strategy = strategy;
    this.omitEmptyStrings = omitEmptyStrings;
    this.trimmer = trimmer;
    this.limit = limit;
  }
  







  public static Splitter on(char separator)
  {
    return on(CharMatcher.is(separator));
  }
  









  public static Splitter on(CharMatcher separatorMatcher)
  {
    Preconditions.checkNotNull(separatorMatcher);
    
    new Splitter(new Strategy()
    {
      public Splitter.SplittingIterator iterator(Splitter splitter, CharSequence toSplit)
      {
        new Splitter.SplittingIterator(splitter, toSplit)
        {
          int separatorStart(int start) {
            return val$separatorMatcher.indexIn(toSplit, start);
          }
          
          int separatorEnd(int separatorPosition)
          {
            return separatorPosition + 1;
          }
        };
      }
    });
  }
  







  public static Splitter on(String separator)
  {
    Preconditions.checkArgument(separator.length() != 0, "The separator may not be the empty string.");
    if (separator.length() == 1) {
      return on(separator.charAt(0));
    }
    new Splitter(new Strategy()
    {
      public Splitter.SplittingIterator iterator(Splitter splitter, CharSequence toSplit)
      {
        new Splitter.SplittingIterator(splitter, toSplit)
        {
          public int separatorStart(int start) {
            int separatorLength = val$separator.length();
            

            int p = start; label80: for (int last = toSplit.length() - separatorLength; p <= last; p++) {
              for (int i = 0; i < separatorLength; i++) {
                if (toSplit.charAt(i + p) != val$separator.charAt(i)) {
                  break label80;
                }
              }
              return p;
            }
            return -1;
          }
          
          public int separatorEnd(int separatorPosition)
          {
            return separatorPosition + val$separator.length();
          }
        };
      }
    });
  }
  









  @GwtIncompatible
  public static Splitter on(Pattern separatorPattern)
  {
    return on(new JdkPattern(separatorPattern));
  }
  
  private static Splitter on(CommonPattern separatorPattern) {
    Preconditions.checkArgument(
      !separatorPattern.matcher("").matches(), "The pattern may not match the empty string: %s", separatorPattern);
    


    new Splitter(new Strategy()
    {
      public Splitter.SplittingIterator iterator(Splitter splitter, CharSequence toSplit)
      {
        final CommonMatcher matcher = val$separatorPattern.matcher(toSplit);
        new Splitter.SplittingIterator(splitter, toSplit)
        {
          public int separatorStart(int start) {
            return matcher.find(start) ? matcher.start() : -1;
          }
          
          public int separatorEnd(int separatorPosition)
          {
            return matcher.end();
          }
        };
      }
    });
  }
  











  @GwtIncompatible
  public static Splitter onPattern(String separatorPattern)
  {
    return on(Platform.compilePattern(separatorPattern));
  }
  
















  public static Splitter fixedLength(int length)
  {
    Preconditions.checkArgument(length > 0, "The length may not be less than 1");
    
    new Splitter(new Strategy()
    {
      public Splitter.SplittingIterator iterator(Splitter splitter, CharSequence toSplit)
      {
        new Splitter.SplittingIterator(splitter, toSplit)
        {
          public int separatorStart(int start) {
            int nextChunkStart = start + val$length;
            return nextChunkStart < toSplit.length() ? nextChunkStart : -1;
          }
          
          public int separatorEnd(int separatorPosition)
          {
            return separatorPosition;
          }
        };
      }
    });
  }
  















  public Splitter omitEmptyStrings()
  {
    return new Splitter(strategy, true, trimmer, limit);
  }
  
















  public Splitter limit(int limit)
  {
    Preconditions.checkArgument(limit > 0, "must be greater than zero: %s", limit);
    return new Splitter(strategy, omitEmptyStrings, trimmer, limit);
  }
  








  public Splitter trimResults()
  {
    return trimResults(CharMatcher.whitespace());
  }
  











  public Splitter trimResults(CharMatcher trimmer)
  {
    Preconditions.checkNotNull(trimmer);
    return new Splitter(strategy, omitEmptyStrings, trimmer, limit);
  }
  







  public Iterable<String> split(final CharSequence sequence)
  {
    Preconditions.checkNotNull(sequence);
    
    new Iterable()
    {
      public Iterator<String> iterator() {
        return Splitter.this.splittingIterator(sequence);
      }
      
      public String toString()
      {
        return 
          ']';
      }
    };
  }
  

  private Iterator<String> splittingIterator(CharSequence sequence)
  {
    return strategy.iterator(this, sequence);
  }
  







  @Beta
  public List<String> splitToList(CharSequence sequence)
  {
    Preconditions.checkNotNull(sequence);
    
    Iterator<String> iterator = splittingIterator(sequence);
    List<String> result = new ArrayList();
    
    while (iterator.hasNext()) {
      result.add(iterator.next());
    }
    
    return Collections.unmodifiableList(result);
  }
  





  @Beta
  public MapSplitter withKeyValueSeparator(String separator)
  {
    return withKeyValueSeparator(on(separator));
  }
  





  @Beta
  public MapSplitter withKeyValueSeparator(char separator)
  {
    return withKeyValueSeparator(on(separator));
  }
  





  @Beta
  public MapSplitter withKeyValueSeparator(Splitter keyValueSplitter)
  {
    return new MapSplitter(this, keyValueSplitter, null);
  }
  

  @Beta
  public static final class MapSplitter
  {
    private static final String INVALID_ENTRY_MESSAGE = "Chunk [%s] is not a valid entry";
    
    private final Splitter outerSplitter;
    
    private final Splitter entrySplitter;
    

    private MapSplitter(Splitter outerSplitter, Splitter entrySplitter)
    {
      this.outerSplitter = outerSplitter;
      this.entrySplitter = ((Splitter)Preconditions.checkNotNull(entrySplitter));
    }
    










    public Map<String, String> split(CharSequence sequence)
    {
      Map<String, String> map = new LinkedHashMap();
      for (String entry : outerSplitter.split(sequence)) {
        Iterator<String> entryFields = entrySplitter.splittingIterator(entry);
        
        Preconditions.checkArgument(entryFields.hasNext(), "Chunk [%s] is not a valid entry", entry);
        String key = (String)entryFields.next();
        Preconditions.checkArgument(!map.containsKey(key), "Duplicate key [%s] found.", key);
        
        Preconditions.checkArgument(entryFields.hasNext(), "Chunk [%s] is not a valid entry", entry);
        String value = (String)entryFields.next();
        map.put(key, value);
        
        Preconditions.checkArgument(!entryFields.hasNext(), "Chunk [%s] is not a valid entry", entry);
      }
      return Collections.unmodifiableMap(map);
    }
  }
  


  private static abstract interface Strategy
  {
    public abstract Iterator<String> iterator(Splitter paramSplitter, CharSequence paramCharSequence);
  }
  


  private static abstract class SplittingIterator
    extends AbstractIterator<String>
  {
    final CharSequence toSplit;
    
    final CharMatcher trimmer;
    
    final boolean omitEmptyStrings;
    

    abstract int separatorStart(int paramInt);
    

    int offset = 0;
    
    abstract int separatorEnd(int paramInt);
    
    protected SplittingIterator(Splitter splitter, CharSequence toSplit) { trimmer = trimmer;
      omitEmptyStrings = omitEmptyStrings;
      limit = limit;
      this.toSplit = toSplit;
    }
    


    int limit;
    

    protected String computeNext()
    {
      int nextStart = offset;
      while (offset != -1) {
        int start = nextStart;
        

        int separatorPosition = separatorStart(offset);
        int end; if (separatorPosition == -1) {
          int end = toSplit.length();
          offset = -1;
        } else {
          end = separatorPosition;
          offset = separatorEnd(separatorPosition);
        }
        if (offset == nextStart)
        {





          offset += 1;
          if (offset > toSplit.length()) {
            offset = -1;
          }
        }
        else
        {
          while ((start < end) && (trimmer.matches(toSplit.charAt(start)))) {
            start++;
          }
          while ((end > start) && (trimmer.matches(toSplit.charAt(end - 1)))) {
            end--;
          }
          
          if ((omitEmptyStrings) && (start == end))
          {
            nextStart = offset;
          }
          else
          {
            if (limit == 1)
            {


              end = toSplit.length();
              offset = -1;
              
              while ((end > start) && (trimmer.matches(toSplit.charAt(end - 1)))) {
                end--;
              }
            }
            limit -= 1;
            

            return toSplit.subSequence(start, end).toString();
          } } }
      return (String)endOfData();
    }
  }
}
