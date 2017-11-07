package org.eclipse.jetty.util;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;










































public abstract class PatternMatcher
{
  public PatternMatcher() {}
  
  public abstract void matched(URI paramURI)
    throws Exception;
  
  public void match(Pattern pattern, URI[] uris, boolean isNullInclusive)
    throws Exception
  {
    int i;
    if (uris != null)
    {
      String[] patterns = pattern == null ? null : pattern.pattern().split(",");
      
      List<Pattern> subPatterns = new ArrayList();
      for (i = 0; (patterns != null) && (i < patterns.length); i++)
      {
        subPatterns.add(Pattern.compile(patterns[i]));
      }
      if (subPatterns.isEmpty()) {
        subPatterns.add(pattern);
      }
      if (subPatterns.isEmpty())
      {
        matchPatterns(null, uris, isNullInclusive);

      }
      else
      {
        for (Pattern p : subPatterns)
        {
          matchPatterns(p, uris, isNullInclusive);
        }
      }
    }
  }
  

  public void matchPatterns(Pattern pattern, URI[] uris, boolean isNullInclusive)
    throws Exception
  {
    for (int i = 0; i < uris.length; i++)
    {
      URI uri = uris[i];
      String s = uri.toString();
      if ((pattern != null) || (!isNullInclusive)) { if (pattern != null)
        {
          if (!pattern.matcher(s).matches()) {} }
      } else {
        matched(uris[i]);
      }
    }
  }
}
