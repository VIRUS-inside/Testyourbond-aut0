package org.apache.commons.lang3;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;



















































final class CharRange
  implements Iterable<Character>, Serializable
{
  private static final long serialVersionUID = 8270183163158333422L;
  private final char start;
  private final char end;
  private final boolean negated;
  private transient String iToString;
  
  private CharRange(char start, char end, boolean negated)
  {
    if (start > end) {
      char temp = start;
      start = end;
      end = temp;
    }
    
    this.start = start;
    this.end = end;
    this.negated = negated;
  }
  







  public static CharRange is(char ch)
  {
    return new CharRange(ch, ch, false);
  }
  







  public static CharRange isNot(char ch)
  {
    return new CharRange(ch, ch, true);
  }
  








  public static CharRange isIn(char start, char end)
  {
    return new CharRange(start, end, false);
  }
  








  public static CharRange isNotIn(char start, char end)
  {
    return new CharRange(start, end, true);
  }
  






  public char getStart()
  {
    return start;
  }
  




  public char getEnd()
  {
    return end;
  }
  







  public boolean isNegated()
  {
    return negated;
  }
  







  public boolean contains(char ch)
  {
    return ((ch >= start) && (ch <= end)) != negated;
  }
  







  public boolean contains(CharRange range)
  {
    if (range == null) {
      throw new IllegalArgumentException("The Range must not be null");
    }
    if (negated) {
      if (negated) {
        return (start >= start) && (end <= end);
      }
      return (end < start) || (start > end);
    }
    if (negated) {
      return (start == 0) && (end == 65535);
    }
    return (start <= start) && (end >= end);
  }
  









  public boolean equals(Object obj)
  {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof CharRange)) {
      return false;
    }
    CharRange other = (CharRange)obj;
    return (start == start) && (end == end) && (negated == negated);
  }
  





  public int hashCode()
  {
    return 'S' + start + '\007' * end + (negated ? 1 : 0);
  }
  





  public String toString()
  {
    if (iToString == null) {
      StringBuilder buf = new StringBuilder(4);
      if (isNegated()) {
        buf.append('^');
      }
      buf.append(start);
      if (start != end) {
        buf.append('-');
        buf.append(end);
      }
      iToString = buf.toString();
    }
    return iToString;
  }
  









  public Iterator<Character> iterator()
  {
    return new CharacterIterator(this, null);
  }
  


  private static class CharacterIterator
    implements Iterator<Character>
  {
    private char current;
    

    private final CharRange range;
    

    private boolean hasNext;
    


    private CharacterIterator(CharRange r)
    {
      range = r;
      hasNext = true;
      
      if (range.negated) {
        if (range.start == 0) {
          if (range.end == 65535)
          {
            hasNext = false;
          } else {
            current = ((char)(range.end + '\001'));
          }
        } else {
          current = '\000';
        }
      } else {
        current = range.start;
      }
    }
    


    private void prepareNext()
    {
      if (range.negated) {
        if (current == 65535) {
          hasNext = false;
        } else if (current + '\001' == range.start) {
          if (range.end == 65535) {
            hasNext = false;
          } else {
            current = ((char)(range.end + '\001'));
          }
        } else {
          current = ((char)(current + '\001'));
        }
      } else if (current < range.end) {
        current = ((char)(current + '\001'));
      } else {
        hasNext = false;
      }
    }
    





    public boolean hasNext()
    {
      return hasNext;
    }
    





    public Character next()
    {
      if (!hasNext) {
        throw new NoSuchElementException();
      }
      char cur = current;
      prepareNext();
      return Character.valueOf(cur);
    }
    






    public void remove()
    {
      throw new UnsupportedOperationException();
    }
  }
}
