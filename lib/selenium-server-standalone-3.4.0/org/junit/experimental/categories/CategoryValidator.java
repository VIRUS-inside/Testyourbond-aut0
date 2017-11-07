package org.junit.experimental.categories;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runners.model.FrameworkMethod;
import org.junit.validator.AnnotationValidator;











public final class CategoryValidator
  extends AnnotationValidator
{
  private static final Set<Class<? extends Annotation>> INCOMPATIBLE_ANNOTATIONS = Collections.unmodifiableSet(new HashSet(Arrays.asList(new Class[] { BeforeClass.class, AfterClass.class, Before.class, After.class })));
  





  public CategoryValidator() {}
  




  public List<Exception> validateAnnotatedMethod(FrameworkMethod method)
  {
    List<Exception> errors = new ArrayList();
    Annotation[] annotations = method.getAnnotations();
    Annotation annotation; for (annotation : annotations) {
      for (Class<?> clazz : INCOMPATIBLE_ANNOTATIONS) {
        if (annotation.annotationType().isAssignableFrom(clazz)) {
          addErrorMessage(errors, clazz);
        }
      }
    }
    return Collections.unmodifiableList(errors);
  }
  
  private void addErrorMessage(List<Exception> errors, Class<?> clazz) {
    String message = String.format("@%s can not be combined with @Category", new Object[] { clazz.getSimpleName() });
    
    errors.add(new Exception(message));
  }
}
