package org.hamcrest;

import org.hamcrest.core.AllOf;
import org.hamcrest.core.AnyOf;
import org.hamcrest.core.IsNull;

public class CoreMatchers
{
  public CoreMatchers() {}
  
  public static <T> Matcher<T> allOf(Iterable<Matcher<? super T>> matchers)
  {
    return AllOf.allOf(matchers);
  }
  





  public static <T> Matcher<T> allOf(Matcher<? super T>... matchers)
  {
    return AllOf.allOf(matchers);
  }
  





  public static <T> Matcher<T> allOf(Matcher<? super T> first, Matcher<? super T> second)
  {
    return AllOf.allOf(first, second);
  }
  





  public static <T> Matcher<T> allOf(Matcher<? super T> first, Matcher<? super T> second, Matcher<? super T> third)
  {
    return AllOf.allOf(first, second, third);
  }
  





  public static <T> Matcher<T> allOf(Matcher<? super T> first, Matcher<? super T> second, Matcher<? super T> third, Matcher<? super T> fourth)
  {
    return AllOf.allOf(first, second, third, fourth);
  }
  





  public static <T> Matcher<T> allOf(Matcher<? super T> first, Matcher<? super T> second, Matcher<? super T> third, Matcher<? super T> fourth, Matcher<? super T> fifth)
  {
    return AllOf.allOf(first, second, third, fourth, fifth);
  }
  





  public static <T> Matcher<T> allOf(Matcher<? super T> first, Matcher<? super T> second, Matcher<? super T> third, Matcher<? super T> fourth, Matcher<? super T> fifth, Matcher<? super T> sixth)
  {
    return AllOf.allOf(first, second, third, fourth, fifth, sixth);
  }
  





  public static <T> AnyOf<T> anyOf(Iterable<Matcher<? super T>> matchers)
  {
    return AnyOf.anyOf(matchers);
  }
  





  public static <T> AnyOf<T> anyOf(Matcher<T> first, Matcher<? super T> second, Matcher<? super T> third)
  {
    return AnyOf.anyOf(first, second, third);
  }
  





  public static <T> AnyOf<T> anyOf(Matcher<T> first, Matcher<? super T> second, Matcher<? super T> third, Matcher<? super T> fourth)
  {
    return AnyOf.anyOf(first, second, third, fourth);
  }
  





  public static <T> AnyOf<T> anyOf(Matcher<T> first, Matcher<? super T> second, Matcher<? super T> third, Matcher<? super T> fourth, Matcher<? super T> fifth)
  {
    return AnyOf.anyOf(first, second, third, fourth, fifth);
  }
  





  public static <T> AnyOf<T> anyOf(Matcher<T> first, Matcher<? super T> second, Matcher<? super T> third, Matcher<? super T> fourth, Matcher<? super T> fifth, Matcher<? super T> sixth)
  {
    return AnyOf.anyOf(first, second, third, fourth, fifth, sixth);
  }
  





  public static <T> AnyOf<T> anyOf(Matcher<T> first, Matcher<? super T> second)
  {
    return AnyOf.anyOf(first, second);
  }
  





  public static <T> AnyOf<T> anyOf(Matcher<? super T>... matchers)
  {
    return AnyOf.anyOf(matchers);
  }
  





  public static <LHS> org.hamcrest.core.CombinableMatcher.CombinableBothMatcher<LHS> both(Matcher<? super LHS> matcher)
  {
    return org.hamcrest.core.CombinableMatcher.both(matcher);
  }
  





  public static <LHS> org.hamcrest.core.CombinableMatcher.CombinableEitherMatcher<LHS> either(Matcher<? super LHS> matcher)
  {
    return org.hamcrest.core.CombinableMatcher.either(matcher);
  }
  













  public static <T> Matcher<T> describedAs(String description, Matcher<T> matcher, Object... values)
  {
    return org.hamcrest.core.DescribedAs.describedAs(description, matcher, values);
  }
  










  public static <U> Matcher<Iterable<U>> everyItem(Matcher<U> itemMatcher)
  {
    return org.hamcrest.core.Every.everyItem(itemMatcher);
  }
  







  public static <T> Matcher<T> is(T value)
  {
    return org.hamcrest.core.Is.is(value);
  }
  








  public static <T> Matcher<T> is(Matcher<T> matcher)
  {
    return org.hamcrest.core.Is.is(matcher);
  }
  






  /**
   * @deprecated
   */
  public static <T> Matcher<T> is(Class<T> type)
  {
    return org.hamcrest.core.Is.is(type);
  }
  







  public static <T> Matcher<T> isA(Class<T> type)
  {
    return org.hamcrest.core.Is.isA(type);
  }
  


  public static Matcher<Object> anything()
  {
    return org.hamcrest.core.IsAnything.anything();
  }
  






  public static Matcher<Object> anything(String description)
  {
    return org.hamcrest.core.IsAnything.anything(description);
  }
  











  public static <T> Matcher<Iterable<? super T>> hasItem(T item)
  {
    return org.hamcrest.core.IsCollectionContaining.hasItem(item);
  }
  











  public static <T> Matcher<Iterable<? super T>> hasItem(Matcher<? super T> itemMatcher)
  {
    return org.hamcrest.core.IsCollectionContaining.hasItem(itemMatcher);
  }
  











  public static <T> Matcher<Iterable<T>> hasItems(T... items)
  {
    return org.hamcrest.core.IsCollectionContaining.hasItems(items);
  }
  











  public static <T> Matcher<Iterable<T>> hasItems(Matcher<? super T>... itemMatchers)
  {
    return org.hamcrest.core.IsCollectionContaining.hasItems(itemMatchers);
  }
  





















  public static <T> Matcher<T> equalTo(T operand)
  {
    return org.hamcrest.core.IsEqual.equalTo(operand);
  }
  











  public static <T> Matcher<T> any(Class<T> type)
  {
    return org.hamcrest.core.IsInstanceOf.any(type);
  }
  









  public static <T> Matcher<T> instanceOf(Class<?> type)
  {
    return org.hamcrest.core.IsInstanceOf.instanceOf(type);
  }
  









  public static <T> Matcher<T> not(Matcher<T> matcher)
  {
    return org.hamcrest.core.IsNot.not(matcher);
  }
  










  public static <T> Matcher<T> not(T value)
  {
    return org.hamcrest.core.IsNot.not(value);
  }
  





  public static Matcher<Object> nullValue()
  {
    return IsNull.nullValue();
  }
  









  public static <T> Matcher<T> nullValue(Class<T> type)
  {
    return IsNull.nullValue(type);
  }
  







  public static Matcher<Object> notNullValue()
  {
    return IsNull.notNullValue();
  }
  











  public static <T> Matcher<T> notNullValue(Class<T> type)
  {
    return IsNull.notNullValue(type);
  }
  






  public static <T> Matcher<T> sameInstance(T target)
  {
    return org.hamcrest.core.IsSame.sameInstance(target);
  }
  






  public static <T> Matcher<T> theInstance(T target)
  {
    return org.hamcrest.core.IsSame.theInstance(target);
  }
  









  public static Matcher<String> containsString(String substring)
  {
    return org.hamcrest.core.StringContains.containsString(substring);
  }
  









  public static Matcher<String> startsWith(String prefix)
  {
    return org.hamcrest.core.StringStartsWith.startsWith(prefix);
  }
  









  public static Matcher<String> endsWith(String suffix)
  {
    return org.hamcrest.core.StringEndsWith.endsWith(suffix);
  }
}
