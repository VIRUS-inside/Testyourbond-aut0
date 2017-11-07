package org.junit.experimental.categories;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;




























































































public class Categories
  extends Suite
{
  public static class CategoryFilter
    extends Filter
  {
    private final Set<Class<?>> included;
    private final Set<Class<?>> excluded;
    private final boolean includedAny;
    private final boolean excludedAny;
    
    public static CategoryFilter include(boolean matchAny, Class<?>... categories)
    {
      if (hasNull(categories)) {
        throw new NullPointerException("has null category");
      }
      return categoryFilter(matchAny, Categories.createSet(categories), true, null);
    }
    
    public static CategoryFilter include(Class<?> category) {
      return include(true, new Class[] { category });
    }
    
    public static CategoryFilter include(Class<?>... categories) {
      return include(true, categories);
    }
    
    public static CategoryFilter exclude(boolean matchAny, Class<?>... categories) {
      if (hasNull(categories)) {
        throw new NullPointerException("has null category");
      }
      return categoryFilter(true, null, matchAny, Categories.createSet(categories));
    }
    
    public static CategoryFilter exclude(Class<?> category) {
      return exclude(true, new Class[] { category });
    }
    
    public static CategoryFilter exclude(Class<?>... categories) {
      return exclude(true, categories);
    }
    
    public static CategoryFilter categoryFilter(boolean matchAnyInclusions, Set<Class<?>> inclusions, boolean matchAnyExclusions, Set<Class<?>> exclusions)
    {
      return new CategoryFilter(matchAnyInclusions, inclusions, matchAnyExclusions, exclusions);
    }
    
    protected CategoryFilter(boolean matchAnyIncludes, Set<Class<?>> includes, boolean matchAnyExcludes, Set<Class<?>> excludes)
    {
      includedAny = matchAnyIncludes;
      excludedAny = matchAnyExcludes;
      included = copyAndRefine(includes);
      excluded = copyAndRefine(excludes);
    }
    



    public String describe()
    {
      return toString();
    }
    












    public String toString()
    {
      StringBuilder description = new StringBuilder("categories ").append(included.isEmpty() ? "[all]" : included);
      
      if (!excluded.isEmpty()) {
        description.append(" - ").append(excluded);
      }
      return description.toString();
    }
    
    public boolean shouldRun(Description description)
    {
      if (hasCorrectCategoryAnnotation(description)) {
        return true;
      }
      
      for (Description each : description.getChildren()) {
        if (shouldRun(each)) {
          return true;
        }
      }
      
      return false;
    }
    
    private boolean hasCorrectCategoryAnnotation(Description description) {
      Set<Class<?>> childCategories = categories(description);
      

      if (childCategories.isEmpty()) {
        return included.isEmpty();
      }
      
      if (!excluded.isEmpty()) {
        if (excludedAny) {
          if (matchesAnyParentCategories(childCategories, excluded)) {
            return false;
          }
        }
        else if (matchesAllParentCategories(childCategories, excluded)) {
          return false;
        }
      }
      

      if (included.isEmpty())
      {
        return true;
      }
      if (includedAny) {
        return matchesAnyParentCategories(childCategories, included);
      }
      return matchesAllParentCategories(childCategories, included);
    }
    





    private boolean matchesAnyParentCategories(Set<Class<?>> childCategories, Set<Class<?>> parentCategories)
    {
      for (Class<?> parentCategory : parentCategories) {
        if (Categories.hasAssignableTo(childCategories, parentCategory)) {
          return true;
        }
      }
      return false;
    }
    



    private boolean matchesAllParentCategories(Set<Class<?>> childCategories, Set<Class<?>> parentCategories)
    {
      for (Class<?> parentCategory : parentCategories) {
        if (!Categories.hasAssignableTo(childCategories, parentCategory)) {
          return false;
        }
      }
      return true;
    }
    
    private static Set<Class<?>> categories(Description description) {
      Set<Class<?>> categories = new HashSet();
      Collections.addAll(categories, directCategories(description));
      Collections.addAll(categories, directCategories(parentDescription(description)));
      return categories;
    }
    
    private static Description parentDescription(Description description) {
      Class<?> testClass = description.getTestClass();
      return testClass == null ? null : Description.createSuiteDescription(testClass);
    }
    
    private static Class<?>[] directCategories(Description description) {
      if (description == null) {
        return new Class[0];
      }
      
      Category annotation = (Category)description.getAnnotation(Category.class);
      return annotation == null ? new Class[0] : annotation.value();
    }
    
    private static Set<Class<?>> copyAndRefine(Set<Class<?>> classes) {
      HashSet<Class<?>> c = new HashSet();
      if (classes != null) {
        c.addAll(classes);
      }
      c.remove(null);
      return c;
    }
    
    private static boolean hasNull(Class<?>... classes) {
      if (classes == null) return false;
      for (Class<?> clazz : classes) {
        if (clazz == null) {
          return true;
        }
      }
      return false;
    }
  }
  
  public Categories(Class<?> klass, RunnerBuilder builder) throws InitializationError {
    super(klass, builder);
    try {
      Set<Class<?>> included = getIncludedCategory(klass);
      Set<Class<?>> excluded = getExcludedCategory(klass);
      boolean isAnyIncluded = isAnyIncluded(klass);
      boolean isAnyExcluded = isAnyExcluded(klass);
      
      filter(CategoryFilter.categoryFilter(isAnyIncluded, included, isAnyExcluded, excluded));
    } catch (NoTestsRemainException e) {
      throw new InitializationError(e);
    }
    assertNoCategorizedDescendentsOfUncategorizeableParents(getDescription());
  }
  
  private static Set<Class<?>> getIncludedCategory(Class<?> klass) {
    IncludeCategory annotation = (IncludeCategory)klass.getAnnotation(IncludeCategory.class);
    return createSet(annotation == null ? null : annotation.value());
  }
  
  private static boolean isAnyIncluded(Class<?> klass) {
    IncludeCategory annotation = (IncludeCategory)klass.getAnnotation(IncludeCategory.class);
    return (annotation == null) || (annotation.matchAny());
  }
  
  private static Set<Class<?>> getExcludedCategory(Class<?> klass) {
    ExcludeCategory annotation = (ExcludeCategory)klass.getAnnotation(ExcludeCategory.class);
    return createSet(annotation == null ? null : annotation.value());
  }
  
  private static boolean isAnyExcluded(Class<?> klass) {
    ExcludeCategory annotation = (ExcludeCategory)klass.getAnnotation(ExcludeCategory.class);
    return (annotation == null) || (annotation.matchAny());
  }
  
  private static void assertNoCategorizedDescendentsOfUncategorizeableParents(Description description) throws InitializationError {
    if (!canHaveCategorizedChildren(description)) {
      assertNoDescendantsHaveCategoryAnnotations(description);
    }
    for (Description each : description.getChildren()) {
      assertNoCategorizedDescendentsOfUncategorizeableParents(each);
    }
  }
  
  private static void assertNoDescendantsHaveCategoryAnnotations(Description description) throws InitializationError {
    for (Description each : description.getChildren()) {
      if (each.getAnnotation(Category.class) != null) {
        throw new InitializationError("Category annotations on Parameterized classes are not supported on individual methods.");
      }
      assertNoDescendantsHaveCategoryAnnotations(each);
    }
  }
  
  private static boolean canHaveCategorizedChildren(Description description)
  {
    for (Description each : description.getChildren()) {
      if (each.getTestClass() == null) {
        return false;
      }
    }
    return true;
  }
  
  private static boolean hasAssignableTo(Set<Class<?>> assigns, Class<?> to) {
    for (Class<?> from : assigns) {
      if (to.isAssignableFrom(from)) {
        return true;
      }
    }
    return false;
  }
  
  private static Set<Class<?>> createSet(Class<?>... t) {
    Set<Class<?>> set = new HashSet();
    if (t != null) {
      Collections.addAll(set, t);
    }
    return set;
  }
  
  @Retention(RetentionPolicy.RUNTIME)
  public static @interface ExcludeCategory
  {
    Class<?>[] value() default {};
    
    boolean matchAny() default true;
  }
  
  @Retention(RetentionPolicy.RUNTIME)
  public static @interface IncludeCategory
  {
    Class<?>[] value() default {};
    
    boolean matchAny() default true;
  }
}
