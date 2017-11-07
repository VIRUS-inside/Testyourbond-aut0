package com.beust.jcommander;

import com.beust.jcommander.converters.CommaParameterSplitter;
import com.beust.jcommander.converters.IParameterSplitter;
import com.beust.jcommander.converters.NoConverter;
import com.beust.jcommander.validators.NoValidator;
import com.beust.jcommander.validators.NoValueValidator;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.METHOD})
public @interface Parameter
{
  String[] names() default {};
  
  String description() default "";
  
  boolean required() default false;
  
  String descriptionKey() default "";
  
  int arity() default -1;
  
  boolean password() default false;
  
  Class<? extends IStringConverter<?>> converter() default NoConverter.class;
  
  Class<? extends IStringConverter<?>> listConverter() default NoConverter.class;
  
  boolean hidden() default false;
  
  Class<? extends IParameterValidator> validateWith() default NoValidator.class;
  
  Class<? extends IValueValidator> validateValueWith() default NoValueValidator.class;
  
  boolean variableArity() default false;
  
  Class<? extends IParameterSplitter> splitter() default CommaParameterSplitter.class;
  
  boolean echoInput() default false;
  
  boolean help() default false;
  
  boolean forceNonOverwritable() default false;
}
