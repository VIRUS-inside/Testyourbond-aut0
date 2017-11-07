package org.apache.bcel;






























































public abstract interface ExceptionConstants
{
  public static final Class THROWABLE = 1.class$java$lang$Throwable == null ? (1.class$java$lang$Throwable = 1.class$("java.lang.Throwable")) : 1.class$java$lang$Throwable;
  


  public static final Class RUNTIME_EXCEPTION = 1.class$java$lang$RuntimeException == null ? (1.class$java$lang$RuntimeException = 1.class$("java.lang.RuntimeException")) : 1.class$java$lang$RuntimeException;
  


  public static final Class LINKING_EXCEPTION = 1.class$java$lang$LinkageError == null ? (1.class$java$lang$LinkageError = 1.class$("java.lang.LinkageError")) : 1.class$java$lang$LinkageError;
  


  public static final Class CLASS_CIRCULARITY_ERROR = 1.class$java$lang$ClassCircularityError == null ? (1.class$java$lang$ClassCircularityError = 1.class$("java.lang.ClassCircularityError")) : 1.class$java$lang$ClassCircularityError;
  public static final Class CLASS_FORMAT_ERROR = 1.class$java$lang$ClassFormatError == null ? (1.class$java$lang$ClassFormatError = 1.class$("java.lang.ClassFormatError")) : 1.class$java$lang$ClassFormatError;
  public static final Class EXCEPTION_IN_INITIALIZER_ERROR = 1.class$java$lang$ExceptionInInitializerError == null ? (1.class$java$lang$ExceptionInInitializerError = 1.class$("java.lang.ExceptionInInitializerError")) : 1.class$java$lang$ExceptionInInitializerError;
  public static final Class INCOMPATIBLE_CLASS_CHANGE_ERROR = 1.class$java$lang$IncompatibleClassChangeError == null ? (1.class$java$lang$IncompatibleClassChangeError = 1.class$("java.lang.IncompatibleClassChangeError")) : 1.class$java$lang$IncompatibleClassChangeError;
  public static final Class ABSTRACT_METHOD_ERROR = 1.class$java$lang$AbstractMethodError == null ? (1.class$java$lang$AbstractMethodError = 1.class$("java.lang.AbstractMethodError")) : 1.class$java$lang$AbstractMethodError;
  public static final Class ILLEGAL_ACCESS_ERROR = 1.class$java$lang$IllegalAccessError == null ? (1.class$java$lang$IllegalAccessError = 1.class$("java.lang.IllegalAccessError")) : 1.class$java$lang$IllegalAccessError;
  public static final Class INSTANTIATION_ERROR = 1.class$java$lang$InstantiationError == null ? (1.class$java$lang$InstantiationError = 1.class$("java.lang.InstantiationError")) : 1.class$java$lang$InstantiationError;
  public static final Class NO_SUCH_FIELD_ERROR = 1.class$java$lang$NoSuchFieldError == null ? (1.class$java$lang$NoSuchFieldError = 1.class$("java.lang.NoSuchFieldError")) : 1.class$java$lang$NoSuchFieldError;
  public static final Class NO_SUCH_METHOD_ERROR = 1.class$java$lang$NoSuchMethodError == null ? (1.class$java$lang$NoSuchMethodError = 1.class$("java.lang.NoSuchMethodError")) : 1.class$java$lang$NoSuchMethodError;
  public static final Class NO_CLASS_DEF_FOUND_ERROR = 1.class$java$lang$NoClassDefFoundError == null ? (1.class$java$lang$NoClassDefFoundError = 1.class$("java.lang.NoClassDefFoundError")) : 1.class$java$lang$NoClassDefFoundError;
  public static final Class UNSATISFIED_LINK_ERROR = 1.class$java$lang$UnsatisfiedLinkError == null ? (1.class$java$lang$UnsatisfiedLinkError = 1.class$("java.lang.UnsatisfiedLinkError")) : 1.class$java$lang$UnsatisfiedLinkError;
  public static final Class VERIFY_ERROR = 1.class$java$lang$VerifyError == null ? (1.class$java$lang$VerifyError = 1.class$("java.lang.VerifyError")) : 1.class$java$lang$VerifyError;
  





  public static final Class NULL_POINTER_EXCEPTION = 1.class$java$lang$NullPointerException == null ? (1.class$java$lang$NullPointerException = 1.class$("java.lang.NullPointerException")) : 1.class$java$lang$NullPointerException;
  public static final Class ARRAY_INDEX_OUT_OF_BOUNDS_EXCEPTION = 1.class$java$lang$ArrayIndexOutOfBoundsException == null ? (1.class$java$lang$ArrayIndexOutOfBoundsException = 1.class$("java.lang.ArrayIndexOutOfBoundsException")) : 1.class$java$lang$ArrayIndexOutOfBoundsException;
  public static final Class ARITHMETIC_EXCEPTION = 1.class$java$lang$ArithmeticException == null ? (1.class$java$lang$ArithmeticException = 1.class$("java.lang.ArithmeticException")) : 1.class$java$lang$ArithmeticException;
  public static final Class NEGATIVE_ARRAY_SIZE_EXCEPTION = 1.class$java$lang$NegativeArraySizeException == null ? (1.class$java$lang$NegativeArraySizeException = 1.class$("java.lang.NegativeArraySizeException")) : 1.class$java$lang$NegativeArraySizeException;
  public static final Class CLASS_CAST_EXCEPTION = 1.class$java$lang$ClassCastException == null ? (1.class$java$lang$ClassCastException = 1.class$("java.lang.ClassCastException")) : 1.class$java$lang$ClassCastException;
  public static final Class ILLEGAL_MONITOR_STATE = 1.class$java$lang$IllegalMonitorStateException == null ? (1.class$java$lang$IllegalMonitorStateException = 1.class$("java.lang.IllegalMonitorStateException")) : 1.class$java$lang$IllegalMonitorStateException;
  



  public static final Class[] EXCS_CLASS_AND_INTERFACE_RESOLUTION = { NO_CLASS_DEF_FOUND_ERROR, CLASS_FORMAT_ERROR, VERIFY_ERROR, ABSTRACT_METHOD_ERROR, EXCEPTION_IN_INITIALIZER_ERROR, ILLEGAL_ACCESS_ERROR };
  



  public static final Class[] EXCS_FIELD_AND_METHOD_RESOLUTION = { NO_SUCH_FIELD_ERROR, ILLEGAL_ACCESS_ERROR, NO_SUCH_METHOD_ERROR };
  


  public static final Class[] EXCS_INTERFACE_METHOD_RESOLUTION = new Class[0];
  public static final Class[] EXCS_STRING_RESOLUTION = new Class[0];
  

  public static final Class[] EXCS_ARRAY_EXCEPTION = { NULL_POINTER_EXCEPTION, ARRAY_INDEX_OUT_OF_BOUNDS_EXCEPTION };
}
