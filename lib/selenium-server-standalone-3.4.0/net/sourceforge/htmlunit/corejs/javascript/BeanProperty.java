package net.sourceforge.htmlunit.corejs.javascript;

















































































































































































































class BeanProperty
{
  MemberBox getter;
  















































































































































































































  MemberBox setter;
  















































































































































































































  NativeJavaMethod setters;
  















































































































































































































  BeanProperty(MemberBox getter, MemberBox setter, NativeJavaMethod setters)
  {
    this.getter = getter;
    this.setter = setter;
    this.setters = setters;
  }
}
