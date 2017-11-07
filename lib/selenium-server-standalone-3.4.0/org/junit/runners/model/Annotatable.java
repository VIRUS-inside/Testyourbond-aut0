package org.junit.runners.model;

import java.lang.annotation.Annotation;

public abstract interface Annotatable
{
  public abstract Annotation[] getAnnotations();
  
  public abstract <T extends Annotation> T getAnnotation(Class<T> paramClass);
}
