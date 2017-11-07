package org.jsoup.parser;

import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;





public class TokenQueue
{
  private String queue;
  private int pos = 0;
  

  private static final char ESC = '\\';
  


  public TokenQueue(String data)
  {
    Validate.notNull(data);
    queue = data;
  }
  



  public boolean isEmpty()
  {
    return remainingLength() == 0;
  }
  
  private int remainingLength() {
    return queue.length() - pos;
  }
  



  public char peek()
  {
    return isEmpty() ? '\000' : queue.charAt(pos);
  }
  



  public void addFirst(Character c)
  {
    addFirst(c.toString());
  }
  




  public void addFirst(String seq)
  {
    queue = (seq + queue.substring(pos));
    pos = 0;
  }
  




  public boolean matches(String seq)
  {
    return queue.regionMatches(true, pos, seq, 0, seq.length());
  }
  




  public boolean matchesCS(String seq)
  {
    return queue.startsWith(seq, pos);
  }
  





  public boolean matchesAny(String... seq)
  {
    for (String s : seq) {
      if (matches(s))
        return true;
    }
    return false;
  }
  
  public boolean matchesAny(char... seq) {
    if (isEmpty()) {
      return false;
    }
    for (char c : seq) {
      if (queue.charAt(pos) == c)
        return true;
    }
    return false;
  }
  
  public boolean matchesStartTag()
  {
    return (remainingLength() >= 2) && (queue.charAt(pos) == '<') && (Character.isLetter(queue.charAt(pos + 1)));
  }
  





  public boolean matchChomp(String seq)
  {
    if (matches(seq)) {
      pos += seq.length();
      return true;
    }
    return false;
  }
  




  public boolean matchesWhitespace()
  {
    return (!isEmpty()) && (StringUtil.isWhitespace(queue.charAt(pos)));
  }
  



  public boolean matchesWord()
  {
    return (!isEmpty()) && (Character.isLetterOrDigit(queue.charAt(pos)));
  }
  


  public void advance()
  {
    if (!isEmpty()) { pos += 1;
    }
  }
  


  public char consume()
  {
    return queue.charAt(pos++);
  }
  






  public void consume(String seq)
  {
    if (!matches(seq))
      throw new IllegalStateException("Queue did not match expected sequence");
    int len = seq.length();
    if (len > remainingLength()) {
      throw new IllegalStateException("Queue not long enough to consume sequence");
    }
    pos += len;
  }
  




  public String consumeTo(String seq)
  {
    int offset = queue.indexOf(seq, pos);
    if (offset != -1) {
      String consumed = queue.substring(pos, offset);
      pos += consumed.length();
      return consumed;
    }
    return remainder();
  }
  
  public String consumeToIgnoreCase(String seq)
  {
    int start = pos;
    String first = seq.substring(0, 1);
    boolean canScan = first.toLowerCase().equals(first.toUpperCase());
    while ((!isEmpty()) && 
      (!matches(seq)))
    {

      if (canScan) {
        int skip = queue.indexOf(first, pos) - pos;
        if (skip == 0) {
          pos += 1;
        } else if (skip < 0) {
          pos = queue.length();
        } else {
          pos += skip;
        }
      } else {
        pos += 1;
      }
    }
    return queue.substring(start, pos);
  }
  






  public String consumeToAny(String... seq)
  {
    int start = pos;
    while ((!isEmpty()) && (!matchesAny(seq))) {
      pos += 1;
    }
    
    return queue.substring(start, pos);
  }
  







  public String chompTo(String seq)
  {
    String data = consumeTo(seq);
    matchChomp(seq);
    return data;
  }
  
  public String chompToIgnoreCase(String seq) {
    String data = consumeToIgnoreCase(seq);
    matchChomp(seq);
    return data;
  }
  








  public String chompBalanced(char open, char close)
  {
    int start = -1;
    int end = -1;
    int depth = 0;
    char last = '\000';
    boolean inQuote = false;
    do
    {
      if (isEmpty()) break;
      Character c = Character.valueOf(consume());
      if ((last == 0) || (last != '\\')) {
        if (((c.equals(Character.valueOf('\''))) || (c.equals(Character.valueOf('"')))) && (c.charValue() != open))
          inQuote = !inQuote;
        if (inQuote)
          continue;
        if (c.equals(Character.valueOf(open))) {
          depth++;
          if (start == -1) {
            start = pos;
          }
        } else if (c.equals(Character.valueOf(close))) {
          depth--;
        }
      }
      if ((depth > 0) && (last != 0))
        end = pos;
      last = c.charValue();
    } while (depth > 0);
    String out = end >= 0 ? queue.substring(start, end) : "";
    if (depth > 0) {
      Validate.fail("Did not find balanced maker at " + out);
    }
    return out;
  }
  




  public static String unescape(String in)
  {
    StringBuilder out = new StringBuilder();
    char last = '\000';
    for (char c : in.toCharArray()) {
      if (c == '\\') {
        if ((last != 0) && (last == '\\')) {
          out.append(c);
        }
      } else
        out.append(c);
      last = c;
    }
    return out.toString();
  }
  



  public boolean consumeWhitespace()
  {
    boolean seen = false;
    while (matchesWhitespace()) {
      pos += 1;
      seen = true;
    }
    return seen;
  }
  



  public String consumeWord()
  {
    int start = pos;
    while (matchesWord())
      pos += 1;
    return queue.substring(start, pos);
  }
  




  public String consumeTagName()
  {
    int start = pos;
    while (!isEmpty()) { if (!matchesWord()) if (!matchesAny(new char[] { ':', '_', '-' })) break;
      pos += 1;
    }
    return queue.substring(start, pos);
  }
  




  public String consumeElementSelector()
  {
    int start = pos;
    while (!isEmpty()) { if (!matchesWord()) if (!matchesAny(new String[] { "*|", "|", "_", "-" })) break;
      pos += 1;
    }
    return queue.substring(start, pos);
  }
  




  public String consumeCssIdentifier()
  {
    int start = pos;
    while (!isEmpty()) { if (!matchesWord()) if (!matchesAny(new char[] { '-', '_' })) break;
      pos += 1;
    }
    return queue.substring(start, pos);
  }
  



  public String consumeAttributeKey()
  {
    int start = pos;
    while (!isEmpty()) { if (!matchesWord()) if (!matchesAny(new char[] { '-', '_', ':' })) break;
      pos += 1;
    }
    return queue.substring(start, pos);
  }
  



  public String remainder()
  {
    String remainder = queue.substring(pos, queue.length());
    pos = queue.length();
    return remainder;
  }
  
  public String toString()
  {
    return queue.substring(pos);
  }
}
