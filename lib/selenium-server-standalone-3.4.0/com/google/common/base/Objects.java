package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import java.util.Arrays;
import javax.annotation.Nullable;






































@GwtCompatible
public final class Objects
  extends ExtraObjectsMethodsForWeb
{
  private Objects() {}
  
  public static boolean equal(@Nullable Object a, @Nullable Object b)
  {
    return (a == b) || ((a != null) && (a.equals(b)));
  }
  



















  public static int hashCode(@Nullable Object... objects)
  {
    return Arrays.hashCode(objects);
  }
}
