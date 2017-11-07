package net.sf.cglib.core;















public class ClassesKey
{
  private static final Key FACTORY = (Key)KeyFactory.create(Key.class);
  


  private ClassesKey() {}
  


  public static Object create(Object[] array)
  {
    return FACTORY.newInstance(classNames(array));
  }
  
  private static String[] classNames(Object[] objects) {
    if (objects == null) {
      return null;
    }
    String[] classNames = new String[objects.length];
    for (int i = 0; i < objects.length; i++) {
      Object object = objects[i];
      if (object != null) {
        Class<?> aClass = object.getClass();
        classNames[i] = (aClass == null ? null : aClass.getName());
      }
    }
    return classNames;
  }
  
  static abstract interface Key
  {
    public abstract Object newInstance(Object[] paramArrayOfObject);
  }
}
