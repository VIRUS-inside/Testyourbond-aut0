package org.apache.commons.lang3.text.translate;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;





























public class LookupTranslator
  extends CharSequenceTranslator
{
  private final HashMap<String, String> lookupMap;
  private final HashSet<Character> prefixSet;
  private final int shortest;
  private final int longest;
  
  public LookupTranslator(CharSequence[]... lookup)
  {
    lookupMap = new HashMap();
    prefixSet = new HashSet();
    int _shortest = Integer.MAX_VALUE;
    int _longest = 0;
    if (lookup != null) {
      for (CharSequence[] seq : lookup) {
        lookupMap.put(seq[0].toString(), seq[1].toString());
        prefixSet.add(Character.valueOf(seq[0].charAt(0)));
        int sz = seq[0].length();
        if (sz < _shortest) {
          _shortest = sz;
        }
        if (sz > _longest) {
          _longest = sz;
        }
      }
    }
    shortest = _shortest;
    longest = _longest;
  }
  



  public int translate(CharSequence input, int index, Writer out)
    throws IOException
  {
    if (prefixSet.contains(Character.valueOf(input.charAt(index)))) {
      int max = longest;
      if (index + longest > input.length()) {
        max = input.length() - index;
      }
      
      for (int i = max; i >= shortest; i--) {
        CharSequence subSeq = input.subSequence(index, index + i);
        String result = (String)lookupMap.get(subSeq.toString());
        
        if (result != null) {
          out.write(result);
          return i;
        }
      }
    }
    return 0;
  }
}
