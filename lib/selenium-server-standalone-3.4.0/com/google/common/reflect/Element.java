package com.google.common.reflect;

import com.google.common.base.Preconditions;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import javax.annotation.Nullable;






















class Element
  extends AccessibleObject
  implements Member
{
  private final AccessibleObject accessibleObject;
  private final Member member;
  
  <M extends AccessibleObject,  extends Member> Element(M member)
  {
    Preconditions.checkNotNull(member);
    accessibleObject = member;
    this.member = ((Member)member);
  }
  
  public TypeToken<?> getOwnerType() {
    return TypeToken.of(getDeclaringClass());
  }
  
  public final boolean isAnnotationPresent(Class<? extends Annotation> annotationClass)
  {
    return accessibleObject.isAnnotationPresent(annotationClass);
  }
  
  public final <A extends Annotation> A getAnnotation(Class<A> annotationClass)
  {
    return accessibleObject.getAnnotation(annotationClass);
  }
  
  public final Annotation[] getAnnotations()
  {
    return accessibleObject.getAnnotations();
  }
  
  public final Annotation[] getDeclaredAnnotations()
  {
    return accessibleObject.getDeclaredAnnotations();
  }
  
  public final void setAccessible(boolean flag) throws SecurityException
  {
    accessibleObject.setAccessible(flag);
  }
  
  public final boolean isAccessible()
  {
    return accessibleObject.isAccessible();
  }
  
  public Class<?> getDeclaringClass()
  {
    return member.getDeclaringClass();
  }
  
  public final String getName()
  {
    return member.getName();
  }
  
  public final int getModifiers()
  {
    return member.getModifiers();
  }
  
  public final boolean isSynthetic()
  {
    return member.isSynthetic();
  }
  
  public final boolean isPublic()
  {
    return Modifier.isPublic(getModifiers());
  }
  
  public final boolean isProtected()
  {
    return Modifier.isProtected(getModifiers());
  }
  
  public final boolean isPackagePrivate()
  {
    return (!isPrivate()) && (!isPublic()) && (!isProtected());
  }
  
  public final boolean isPrivate()
  {
    return Modifier.isPrivate(getModifiers());
  }
  
  public final boolean isStatic()
  {
    return Modifier.isStatic(getModifiers());
  }
  






  public final boolean isFinal()
  {
    return Modifier.isFinal(getModifiers());
  }
  
  public final boolean isAbstract()
  {
    return Modifier.isAbstract(getModifiers());
  }
  
  public final boolean isNative()
  {
    return Modifier.isNative(getModifiers());
  }
  
  public final boolean isSynchronized()
  {
    return Modifier.isSynchronized(getModifiers());
  }
  
  final boolean isVolatile()
  {
    return Modifier.isVolatile(getModifiers());
  }
  
  final boolean isTransient()
  {
    return Modifier.isTransient(getModifiers());
  }
  
  public boolean equals(@Nullable Object obj)
  {
    if ((obj instanceof Element)) {
      Element that = (Element)obj;
      return (getOwnerType().equals(that.getOwnerType())) && (member.equals(member));
    }
    return false;
  }
  
  public int hashCode()
  {
    return member.hashCode();
  }
  
  public String toString()
  {
    return member.toString();
  }
}
