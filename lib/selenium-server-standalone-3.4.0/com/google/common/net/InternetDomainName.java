package com.google.common.net;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Ascii;
import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.thirdparty.publicsuffix.PublicSuffixPatterns;
import java.util.List;
import javax.annotation.Nullable;





















































@Beta
@GwtCompatible
public final class InternetDomainName
{
  private static final CharMatcher DOTS_MATCHER = CharMatcher.anyOf(".。．｡");
  private static final Splitter DOT_SPLITTER = Splitter.on('.');
  private static final Joiner DOT_JOINER = Joiner.on('.');
  




  private static final int NO_PUBLIC_SUFFIX_FOUND = -1;
  




  private static final String DOT_REGEX = "\\.";
  




  private static final int MAX_PARTS = 127;
  




  private static final int MAX_LENGTH = 253;
  




  private static final int MAX_DOMAIN_PART_LENGTH = 63;
  




  private final String name;
  




  private final ImmutableList<String> parts;
  




  private final int publicSuffixIndex;
  




  InternetDomainName(String name)
  {
    name = Ascii.toLowerCase(DOTS_MATCHER.replaceFrom(name, '.'));
    
    if (name.endsWith(".")) {
      name = name.substring(0, name.length() - 1);
    }
    
    Preconditions.checkArgument(name.length() <= 253, "Domain name too long: '%s':", name);
    this.name = name;
    
    parts = ImmutableList.copyOf(DOT_SPLITTER.split(name));
    Preconditions.checkArgument(parts.size() <= 127, "Domain has too many parts: '%s'", name);
    Preconditions.checkArgument(validateSyntax(parts), "Not a valid domain name: '%s'", name);
    
    publicSuffixIndex = findPublicSuffix();
  }
  





  private int findPublicSuffix()
  {
    int partsSize = parts.size();
    
    for (int i = 0; i < partsSize; i++) {
      String ancestorName = DOT_JOINER.join(parts.subList(i, partsSize));
      
      if (PublicSuffixPatterns.EXACT.containsKey(ancestorName)) {
        return i;
      }
      



      if (PublicSuffixPatterns.EXCLUDED.containsKey(ancestorName)) {
        return i + 1;
      }
      
      if (matchesWildcardPublicSuffix(ancestorName)) {
        return i;
      }
    }
    
    return -1;
  }
  

















  public static InternetDomainName from(String domain)
  {
    return new InternetDomainName((String)Preconditions.checkNotNull(domain));
  }
  





  private static boolean validateSyntax(List<String> parts)
  {
    int lastIndex = parts.size() - 1;
    


    if (!validatePart((String)parts.get(lastIndex), true)) {
      return false;
    }
    
    for (int i = 0; i < lastIndex; i++) {
      String part = (String)parts.get(i);
      if (!validatePart(part, false)) {
        return false;
      }
    }
    
    return true;
  }
  
  private static final CharMatcher DASH_MATCHER = CharMatcher.anyOf("-_");
  

  private static final CharMatcher PART_CHAR_MATCHER = CharMatcher.javaLetterOrDigit().or(DASH_MATCHER);
  











  private static boolean validatePart(String part, boolean isFinalPart)
  {
    if ((part.length() < 1) || (part.length() > 63)) {
      return false;
    }
    










    String asciiChars = CharMatcher.ascii().retainFrom(part);
    
    if (!PART_CHAR_MATCHER.matchesAllOf(asciiChars)) {
      return false;
    }
    


    if ((DASH_MATCHER.matches(part.charAt(0))) || 
      (DASH_MATCHER.matches(part.charAt(part.length() - 1)))) {
      return false;
    }
    







    if ((isFinalPart) && (CharMatcher.digit().matches(part.charAt(0)))) {
      return false;
    }
    
    return true;
  }
  




  public ImmutableList<String> parts()
  {
    return parts;
  }
  









  public boolean isPublicSuffix()
  {
    return publicSuffixIndex == 0;
  }
  








  public boolean hasPublicSuffix()
  {
    return publicSuffixIndex != -1;
  }
  





  public InternetDomainName publicSuffix()
  {
    return hasPublicSuffix() ? ancestor(publicSuffixIndex) : null;
  }
  
















  public boolean isUnderPublicSuffix()
  {
    return publicSuffixIndex > 0;
  }
  















  public boolean isTopPrivateDomain()
  {
    return publicSuffixIndex == 1;
  }
  


















  public InternetDomainName topPrivateDomain()
  {
    if (isTopPrivateDomain()) {
      return this;
    }
    Preconditions.checkState(isUnderPublicSuffix(), "Not under a public suffix: %s", name);
    return ancestor(publicSuffixIndex - 1);
  }
  


  public boolean hasParent()
  {
    return parts.size() > 1;
  }
  






  public InternetDomainName parent()
  {
    Preconditions.checkState(hasParent(), "Domain '%s' has no parent", name);
    return ancestor(1);
  }
  






  private InternetDomainName ancestor(int levels)
  {
    return from(DOT_JOINER.join(parts.subList(levels, parts.size())));
  }
  









  public InternetDomainName child(String leftParts)
  {
    return from((String)Preconditions.checkNotNull(leftParts) + "." + name);
  }
  



















  public static boolean isValid(String name)
  {
    try
    {
      from(name);
      return true;
    } catch (IllegalArgumentException e) {}
    return false;
  }
  



  private static boolean matchesWildcardPublicSuffix(String domain)
  {
    String[] pieces = domain.split("\\.", 2);
    return (pieces.length == 2) && (PublicSuffixPatterns.UNDER.containsKey(pieces[1]));
  }
  



  public String toString()
  {
    return name;
  }
  






  public boolean equals(@Nullable Object object)
  {
    if (object == this) {
      return true;
    }
    
    if ((object instanceof InternetDomainName)) {
      InternetDomainName that = (InternetDomainName)object;
      return name.equals(name);
    }
    
    return false;
  }
  
  public int hashCode()
  {
    return name.hashCode();
  }
}
