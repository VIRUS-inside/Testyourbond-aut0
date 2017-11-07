package org.apache.http.conn.util;

import java.util.Collections;
import java.util.List;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.util.Args;







































@Contract(threading=ThreadingBehavior.IMMUTABLE)
public final class PublicSuffixList
{
  private final DomainType type;
  private final List<String> rules;
  private final List<String> exceptions;
  
  public PublicSuffixList(DomainType type, List<String> rules, List<String> exceptions)
  {
    this.type = ((DomainType)Args.notNull(type, "Domain type"));
    this.rules = Collections.unmodifiableList((List)Args.notNull(rules, "Domain suffix rules"));
    this.exceptions = Collections.unmodifiableList(exceptions != null ? exceptions : Collections.emptyList());
  }
  
  public PublicSuffixList(List<String> rules, List<String> exceptions) {
    this(DomainType.UNKNOWN, rules, exceptions);
  }
  


  public DomainType getType()
  {
    return type;
  }
  
  public List<String> getRules() {
    return rules;
  }
  
  public List<String> getExceptions() {
    return exceptions;
  }
}
