package org.junit.runners.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import org.junit.internal.runners.model.ReflectiveCallable;










public class FrameworkMethod
  extends FrameworkMember<FrameworkMethod>
{
  private final Method method;
  
  public FrameworkMethod(Method method)
  {
    if (method == null) {
      throw new NullPointerException("FrameworkMethod cannot be created without an underlying method.");
    }
    
    this.method = method;
  }
  


  public Method getMethod()
  {
    return method;
  }
  




  public Object invokeExplosively(final Object target, final Object... params)
    throws Throwable
  {
    new ReflectiveCallable()
    {
      protected Object runReflectiveCall() throws Throwable {
        return method.invoke(target, params);
      }
    }.run();
  }
  



  public String getName()
  {
    return method.getName();
  }
  









  public void validatePublicVoidNoArg(boolean isStatic, List<Throwable> errors)
  {
    validatePublicVoid(isStatic, errors);
    if (method.getParameterTypes().length != 0) {
      errors.add(new Exception("Method " + method.getName() + " should have no parameters"));
    }
  }
  









  public void validatePublicVoid(boolean isStatic, List<Throwable> errors)
  {
    if (isStatic() != isStatic) {
      String state = isStatic ? "should" : "should not";
      errors.add(new Exception("Method " + method.getName() + "() " + state + " be static"));
    }
    if (!isPublic()) {
      errors.add(new Exception("Method " + method.getName() + "() should be public"));
    }
    if (method.getReturnType() != Void.TYPE) {
      errors.add(new Exception("Method " + method.getName() + "() should be void"));
    }
  }
  
  protected int getModifiers()
  {
    return method.getModifiers();
  }
  


  public Class<?> getReturnType()
  {
    return method.getReturnType();
  }
  



  public Class<?> getType()
  {
    return getReturnType();
  }
  



  public Class<?> getDeclaringClass()
  {
    return method.getDeclaringClass();
  }
  
  public void validateNoTypeParametersOnArgs(List<Throwable> errors) {
    new NoGenericTypeParametersValidator(method).validate(errors);
  }
  
  public boolean isShadowedBy(FrameworkMethod other)
  {
    if (!other.getName().equals(getName())) {
      return false;
    }
    if (other.getParameterTypes().length != getParameterTypes().length) {
      return false;
    }
    for (int i = 0; i < other.getParameterTypes().length; i++) {
      if (!other.getParameterTypes()[i].equals(getParameterTypes()[i])) {
        return false;
      }
    }
    return true;
  }
  
  public boolean equals(Object obj)
  {
    if (!FrameworkMethod.class.isInstance(obj)) {
      return false;
    }
    return method.equals(method);
  }
  
  public int hashCode()
  {
    return method.hashCode();
  }
  








  @Deprecated
  public boolean producesType(Type type)
  {
    return (getParameterTypes().length == 0) && ((type instanceof Class)) && (((Class)type).isAssignableFrom(method.getReturnType()));
  }
  
  private Class<?>[] getParameterTypes()
  {
    return method.getParameterTypes();
  }
  


  public Annotation[] getAnnotations()
  {
    return method.getAnnotations();
  }
  



  public <T extends Annotation> T getAnnotation(Class<T> annotationType)
  {
    return method.getAnnotation(annotationType);
  }
  
  public String toString()
  {
    return method.toString();
  }
}
