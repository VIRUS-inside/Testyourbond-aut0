package com.google.thirdparty.publicsuffix;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Lists;
import java.util.List;


















@GwtCompatible
final class TrieParser
{
  private static final Joiner PREFIX_JOINER = Joiner.on("");
  

  TrieParser() {}
  
  static ImmutableMap<String, PublicSuffixType> parseTrie(CharSequence encoded)
  {
    ImmutableMap.Builder<String, PublicSuffixType> builder = ImmutableMap.builder();
    int encodedLen = encoded.length();
    int idx = 0;
    while (idx < encodedLen)
    {
      idx = idx + doParseTrieToBuilder(
        Lists.newLinkedList(), encoded.subSequence(idx, encodedLen), builder);
    }
    return builder.build();
  }
  












  private static int doParseTrieToBuilder(List<CharSequence> stack, CharSequence encoded, ImmutableMap.Builder<String, PublicSuffixType> builder)
  {
    int encodedLen = encoded.length();
    int idx = 0;
    char c = '\000';
    for (; 
        
        idx < encodedLen; idx++) {
      c = encoded.charAt(idx);
      if ((c == '&') || (c == '?') || (c == '!') || (c == ':') || (c == ',')) {
        break;
      }
    }
    
    stack.add(0, reverse(encoded.subSequence(0, idx)));
    
    if ((c == '!') || (c == '?') || (c == ':') || (c == ','))
    {



      String domain = PREFIX_JOINER.join(stack);
      if (domain.length() > 0) {
        builder.put(domain, PublicSuffixType.fromCode(c));
      }
    }
    idx++;
    
    while ((c != '?') && (c != ',') && 
      (idx < encodedLen))
    {
      idx += doParseTrieToBuilder(stack, encoded.subSequence(idx, encodedLen), builder);
      if ((encoded.charAt(idx) == '?') || (encoded.charAt(idx) == ','))
      {
        idx++;
      }
    }
    

    stack.remove(0);
    return idx;
  }
  
  private static CharSequence reverse(CharSequence s) {
    return new StringBuilder(s).reverse();
  }
}
