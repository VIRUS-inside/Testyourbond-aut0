package com.google.common.base;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import javax.annotation.Nullable;

































@GwtCompatible(emulated=true)
public final class Predicates
{
  private Predicates() {}
  
  @GwtCompatible(serializable=true)
  public static <T> Predicate<T> alwaysTrue()
  {
    return ObjectPredicate.ALWAYS_TRUE.withNarrowedType();
  }
  


  @GwtCompatible(serializable=true)
  public static <T> Predicate<T> alwaysFalse()
  {
    return ObjectPredicate.ALWAYS_FALSE.withNarrowedType();
  }
  



  @GwtCompatible(serializable=true)
  public static <T> Predicate<T> isNull()
  {
    return ObjectPredicate.IS_NULL.withNarrowedType();
  }
  



  @GwtCompatible(serializable=true)
  public static <T> Predicate<T> notNull()
  {
    return ObjectPredicate.NOT_NULL.withNarrowedType();
  }
  



  public static <T> Predicate<T> not(Predicate<T> predicate)
  {
    return new NotPredicate(predicate);
  }
  







  public static <T> Predicate<T> and(Iterable<? extends Predicate<? super T>> components)
  {
    return new AndPredicate(defensiveCopy(components), null);
  }
  







  public static <T> Predicate<T> and(Predicate<? super T>... components)
  {
    return new AndPredicate(defensiveCopy(components), null);
  }
  




  public static <T> Predicate<T> and(Predicate<? super T> first, Predicate<? super T> second)
  {
    return new AndPredicate(asList((Predicate)Preconditions.checkNotNull(first), (Predicate)Preconditions.checkNotNull(second)), null);
  }
  







  public static <T> Predicate<T> or(Iterable<? extends Predicate<? super T>> components)
  {
    return new OrPredicate(defensiveCopy(components), null);
  }
  







  public static <T> Predicate<T> or(Predicate<? super T>... components)
  {
    return new OrPredicate(defensiveCopy(components), null);
  }
  




  public static <T> Predicate<T> or(Predicate<? super T> first, Predicate<? super T> second)
  {
    return new OrPredicate(asList((Predicate)Preconditions.checkNotNull(first), (Predicate)Preconditions.checkNotNull(second)), null);
  }
  



  public static <T> Predicate<T> equalTo(@Nullable T target)
  {
    return target == null ? isNull() : new IsEqualToPredicate(target, null);
  }
  












  @GwtIncompatible
  public static Predicate<Object> instanceOf(Class<?> clazz)
  {
    return new InstanceOfPredicate(clazz, null);
  }
  
















  @Deprecated
  @GwtIncompatible
  @Beta
  public static Predicate<Class<?>> assignableFrom(Class<?> clazz)
  {
    return subtypeOf(clazz);
  }
  











  @GwtIncompatible
  @Beta
  public static Predicate<Class<?>> subtypeOf(Class<?> clazz)
  {
    return new SubtypeOfPredicate(clazz, null);
  }
  










  public static <T> Predicate<T> in(Collection<? extends T> target)
  {
    return new InPredicate(target, null);
  }
  






  public static <A, B> Predicate<A> compose(Predicate<B> predicate, Function<A, ? extends B> function)
  {
    return new CompositionPredicate(predicate, function, null);
  }
  







  @GwtIncompatible
  public static Predicate<CharSequence> containsPattern(String pattern)
  {
    return new ContainsPatternFromStringPredicate(pattern);
  }
  






  @GwtIncompatible("java.util.regex.Pattern")
  public static Predicate<CharSequence> contains(Pattern pattern)
  {
    return new ContainsPatternPredicate(new JdkPattern(pattern));
  }
  


  static abstract enum ObjectPredicate
    implements Predicate<Object>
  {
    ALWAYS_TRUE, 
    










    ALWAYS_FALSE, 
    










    IS_NULL, 
    










    NOT_NULL;
    




    private ObjectPredicate() {}
    




    <T> Predicate<T> withNarrowedType()
    {
      return this;
    }
  }
  
  private static class NotPredicate<T> implements Predicate<T>, Serializable {
    final Predicate<T> predicate;
    private static final long serialVersionUID = 0L;
    
    NotPredicate(Predicate<T> predicate) {
      this.predicate = ((Predicate)Preconditions.checkNotNull(predicate));
    }
    
    public boolean apply(@Nullable T t)
    {
      return !predicate.apply(t);
    }
    
    public int hashCode()
    {
      return predicate.hashCode() ^ 0xFFFFFFFF;
    }
    
    public boolean equals(@Nullable Object obj)
    {
      if ((obj instanceof NotPredicate)) {
        NotPredicate<?> that = (NotPredicate)obj;
        return predicate.equals(predicate);
      }
      return false;
    }
    
    public String toString()
    {
      return "Predicates.not(" + predicate + ")";
    }
  }
  


  private static final Joiner COMMA_JOINER = Joiner.on(',');
  
  private static class AndPredicate<T> implements Predicate<T>, Serializable {
    private final List<? extends Predicate<? super T>> components;
    private static final long serialVersionUID = 0L;
    
    private AndPredicate(List<? extends Predicate<? super T>> components) {
      this.components = components;
    }
    

    public boolean apply(@Nullable T t)
    {
      for (int i = 0; i < components.size(); i++) {
        if (!((Predicate)components.get(i)).apply(t)) {
          return false;
        }
      }
      return true;
    }
    

    public int hashCode()
    {
      return components.hashCode() + 306654252;
    }
    
    public boolean equals(@Nullable Object obj)
    {
      if ((obj instanceof AndPredicate)) {
        AndPredicate<?> that = (AndPredicate)obj;
        return components.equals(components);
      }
      return false;
    }
    
    public String toString()
    {
      return "Predicates.and(" + Predicates.COMMA_JOINER.join(components) + ")";
    }
  }
  
  private static class OrPredicate<T> implements Predicate<T>, Serializable
  {
    private final List<? extends Predicate<? super T>> components;
    private static final long serialVersionUID = 0L;
    
    private OrPredicate(List<? extends Predicate<? super T>> components)
    {
      this.components = components;
    }
    

    public boolean apply(@Nullable T t)
    {
      for (int i = 0; i < components.size(); i++) {
        if (((Predicate)components.get(i)).apply(t)) {
          return true;
        }
      }
      return false;
    }
    

    public int hashCode()
    {
      return components.hashCode() + 87855567;
    }
    
    public boolean equals(@Nullable Object obj)
    {
      if ((obj instanceof OrPredicate)) {
        OrPredicate<?> that = (OrPredicate)obj;
        return components.equals(components);
      }
      return false;
    }
    
    public String toString()
    {
      return "Predicates.or(" + Predicates.COMMA_JOINER.join(components) + ")";
    }
  }
  
  private static class IsEqualToPredicate<T> implements Predicate<T>, Serializable
  {
    private final T target;
    private static final long serialVersionUID = 0L;
    
    private IsEqualToPredicate(T target)
    {
      this.target = target;
    }
    
    public boolean apply(T t)
    {
      return target.equals(t);
    }
    
    public int hashCode()
    {
      return target.hashCode();
    }
    
    public boolean equals(@Nullable Object obj)
    {
      if ((obj instanceof IsEqualToPredicate)) {
        IsEqualToPredicate<?> that = (IsEqualToPredicate)obj;
        return target.equals(target);
      }
      return false;
    }
    
    public String toString()
    {
      return "Predicates.equalTo(" + target + ")";
    }
  }
  
  @GwtIncompatible
  private static class InstanceOfPredicate implements Predicate<Object>, Serializable
  {
    private final Class<?> clazz;
    private static final long serialVersionUID = 0L;
    
    private InstanceOfPredicate(Class<?> clazz)
    {
      this.clazz = ((Class)Preconditions.checkNotNull(clazz));
    }
    
    public boolean apply(@Nullable Object o)
    {
      return clazz.isInstance(o);
    }
    
    public int hashCode()
    {
      return clazz.hashCode();
    }
    
    public boolean equals(@Nullable Object obj)
    {
      if ((obj instanceof InstanceOfPredicate)) {
        InstanceOfPredicate that = (InstanceOfPredicate)obj;
        return clazz == clazz;
      }
      return false;
    }
    
    public String toString()
    {
      return "Predicates.instanceOf(" + clazz.getName() + ")";
    }
  }
  
  @GwtIncompatible
  private static class SubtypeOfPredicate implements Predicate<Class<?>>, Serializable
  {
    private final Class<?> clazz;
    private static final long serialVersionUID = 0L;
    
    private SubtypeOfPredicate(Class<?> clazz)
    {
      this.clazz = ((Class)Preconditions.checkNotNull(clazz));
    }
    
    public boolean apply(Class<?> input)
    {
      return clazz.isAssignableFrom(input);
    }
    
    public int hashCode()
    {
      return clazz.hashCode();
    }
    
    public boolean equals(@Nullable Object obj)
    {
      if ((obj instanceof SubtypeOfPredicate)) {
        SubtypeOfPredicate that = (SubtypeOfPredicate)obj;
        return clazz == clazz;
      }
      return false;
    }
    
    public String toString()
    {
      return "Predicates.subtypeOf(" + clazz.getName() + ")";
    }
  }
  
  private static class InPredicate<T> implements Predicate<T>, Serializable
  {
    private final Collection<?> target;
    private static final long serialVersionUID = 0L;
    
    private InPredicate(Collection<?> target)
    {
      this.target = ((Collection)Preconditions.checkNotNull(target));
    }
    
    public boolean apply(@Nullable T t)
    {
      try {
        return target.contains(t);
      } catch (NullPointerException e) {
        return false;
      } catch (ClassCastException e) {}
      return false;
    }
    

    public boolean equals(@Nullable Object obj)
    {
      if ((obj instanceof InPredicate)) {
        InPredicate<?> that = (InPredicate)obj;
        return target.equals(target);
      }
      return false;
    }
    
    public int hashCode()
    {
      return target.hashCode();
    }
    
    public String toString()
    {
      return "Predicates.in(" + target + ")";
    }
  }
  
  private static class CompositionPredicate<A, B> implements Predicate<A>, Serializable
  {
    final Predicate<B> p;
    final Function<A, ? extends B> f;
    private static final long serialVersionUID = 0L;
    
    private CompositionPredicate(Predicate<B> p, Function<A, ? extends B> f)
    {
      this.p = ((Predicate)Preconditions.checkNotNull(p));
      this.f = ((Function)Preconditions.checkNotNull(f));
    }
    
    public boolean apply(@Nullable A a)
    {
      return p.apply(f.apply(a));
    }
    
    public boolean equals(@Nullable Object obj)
    {
      if ((obj instanceof CompositionPredicate)) {
        CompositionPredicate<?, ?> that = (CompositionPredicate)obj;
        return (f.equals(f)) && (p.equals(p));
      }
      return false;
    }
    
    public int hashCode()
    {
      return f.hashCode() ^ p.hashCode();
    }
    

    public String toString()
    {
      return p + "(" + f + ")";
    }
  }
  
  @GwtIncompatible
  private static class ContainsPatternPredicate implements Predicate<CharSequence>, Serializable
  {
    final CommonPattern pattern;
    private static final long serialVersionUID = 0L;
    
    ContainsPatternPredicate(CommonPattern pattern)
    {
      this.pattern = ((CommonPattern)Preconditions.checkNotNull(pattern));
    }
    
    public boolean apply(CharSequence t)
    {
      return pattern.matcher(t).find();
    }
    



    public int hashCode()
    {
      return Objects.hashCode(new Object[] { pattern.pattern(), Integer.valueOf(pattern.flags()) });
    }
    
    public boolean equals(@Nullable Object obj)
    {
      if ((obj instanceof ContainsPatternPredicate)) {
        ContainsPatternPredicate that = (ContainsPatternPredicate)obj;
        


        return (Objects.equal(pattern.pattern(), pattern.pattern())) && 
          (pattern.flags() == pattern.flags());
      }
      return false;
    }
    




    public String toString()
    {
      String patternString = MoreObjects.toStringHelper(pattern).add("pattern", pattern.pattern()).add("pattern.flags", pattern.flags()).toString();
      return "Predicates.contains(" + patternString + ")";
    }
  }
  
  @GwtIncompatible
  private static class ContainsPatternFromStringPredicate extends Predicates.ContainsPatternPredicate
  {
    private static final long serialVersionUID = 0L;
    
    ContainsPatternFromStringPredicate(String string)
    {
      super();
    }
    
    public String toString()
    {
      return "Predicates.containsPattern(" + pattern.pattern() + ")";
    }
  }
  



  private static <T> List<Predicate<? super T>> asList(Predicate<? super T> first, Predicate<? super T> second)
  {
    return Arrays.asList(new Predicate[] { first, second });
  }
  
  private static <T> List<T> defensiveCopy(T... array) {
    return defensiveCopy(Arrays.asList(array));
  }
  
  static <T> List<T> defensiveCopy(Iterable<T> iterable) {
    ArrayList<T> list = new ArrayList();
    for (T element : iterable) {
      list.add(Preconditions.checkNotNull(element));
    }
    return list;
  }
}
