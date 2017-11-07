package org.junit.validator;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.runners.model.Annotatable;
import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;







public final class AnnotationsValidator
  implements TestClassValidator
{
  private static final List<AnnotatableValidator<?>> VALIDATORS = Arrays.asList(new AnnotatableValidator[] { new ClassValidator(null), new MethodValidator(null), new FieldValidator(null) });
  



  public AnnotationsValidator() {}
  



  public List<Exception> validateTestClass(TestClass testClass)
  {
    List<Exception> validationErrors = new ArrayList();
    for (AnnotatableValidator<?> validator : VALIDATORS) {
      List<Exception> additionalErrors = validator.validateTestClass(testClass);
      
      validationErrors.addAll(additionalErrors);
    }
    return validationErrors;
  }
  
  private static abstract class AnnotatableValidator<T extends Annotatable> {
    private static final AnnotationValidatorFactory ANNOTATION_VALIDATOR_FACTORY = new AnnotationValidatorFactory();
    
    private AnnotatableValidator() {}
    
    abstract Iterable<T> getAnnotatablesForTestClass(TestClass paramTestClass);
    
    abstract List<Exception> validateAnnotatable(AnnotationValidator paramAnnotationValidator, T paramT);
    
    public List<Exception> validateTestClass(TestClass testClass) { List<Exception> validationErrors = new ArrayList();
      for (T annotatable : getAnnotatablesForTestClass(testClass)) {
        List<Exception> additionalErrors = validateAnnotatable(annotatable);
        validationErrors.addAll(additionalErrors);
      }
      return validationErrors;
    }
    
    private List<Exception> validateAnnotatable(T annotatable) {
      List<Exception> validationErrors = new ArrayList();
      for (Annotation annotation : annotatable.getAnnotations()) {
        Class<? extends Annotation> annotationType = annotation.annotationType();
        
        ValidateWith validateWith = (ValidateWith)annotationType.getAnnotation(ValidateWith.class);
        
        if (validateWith != null) {
          AnnotationValidator annotationValidator = ANNOTATION_VALIDATOR_FACTORY.createAnnotationValidator(validateWith);
          
          List<Exception> errors = validateAnnotatable(annotationValidator, annotatable);
          
          validationErrors.addAll(errors);
        }
      }
      return validationErrors;
    }
  }
  
  private static class ClassValidator extends AnnotationsValidator.AnnotatableValidator<TestClass> { private ClassValidator() { super(); }
    
    Iterable<TestClass> getAnnotatablesForTestClass(TestClass testClass) {
      return Collections.singletonList(testClass);
    }
    



    List<Exception> validateAnnotatable(AnnotationValidator validator, TestClass testClass) { return validator.validateAnnotatedClass(testClass); }
  }
  
  private static class MethodValidator extends AnnotationsValidator.AnnotatableValidator<FrameworkMethod> {
    private MethodValidator() { super(); }
    

    Iterable<FrameworkMethod> getAnnotatablesForTestClass(TestClass testClass)
    {
      return testClass.getAnnotatedMethods();
    }
    



    List<Exception> validateAnnotatable(AnnotationValidator validator, FrameworkMethod method) { return validator.validateAnnotatedMethod(method); }
  }
  
  private static class FieldValidator extends AnnotationsValidator.AnnotatableValidator<FrameworkField> {
    private FieldValidator() { super(); }
    
    Iterable<FrameworkField> getAnnotatablesForTestClass(TestClass testClass)
    {
      return testClass.getAnnotatedFields();
    }
    

    List<Exception> validateAnnotatable(AnnotationValidator validator, FrameworkField field)
    {
      return validator.validateAnnotatedField(field);
    }
  }
}
