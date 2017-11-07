package org.junit.validator;

import java.util.concurrent.ConcurrentHashMap;





public class AnnotationValidatorFactory
{
  private static final ConcurrentHashMap<ValidateWith, AnnotationValidator> VALIDATORS_FOR_ANNOTATION_TYPES = new ConcurrentHashMap();
  




  public AnnotationValidatorFactory() {}
  



  public AnnotationValidator createAnnotationValidator(ValidateWith validateWithAnnotation)
  {
    AnnotationValidator validator = (AnnotationValidator)VALIDATORS_FOR_ANNOTATION_TYPES.get(validateWithAnnotation);
    if (validator != null) {
      return validator;
    }
    
    Class<? extends AnnotationValidator> clazz = validateWithAnnotation.value();
    if (clazz == null) {
      throw new IllegalArgumentException("Can't create validator, value is null in annotation " + validateWithAnnotation.getClass().getName());
    }
    try {
      AnnotationValidator annotationValidator = (AnnotationValidator)clazz.newInstance();
      VALIDATORS_FOR_ANNOTATION_TYPES.putIfAbsent(validateWithAnnotation, annotationValidator);
      return (AnnotationValidator)VALIDATORS_FOR_ANNOTATION_TYPES.get(validateWithAnnotation);
    } catch (Exception e) {
      throw new RuntimeException("Exception received when creating AnnotationValidator class " + clazz.getName(), e);
    }
  }
}
