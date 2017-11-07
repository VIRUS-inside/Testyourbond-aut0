package com.google.common.hash;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.nio.charset.Charset;


















@CanIgnoreReturnValue
abstract class AbstractHasher
  implements Hasher
{
  AbstractHasher() {}
  
  public final Hasher putBoolean(boolean b)
  {
    return putByte((byte)(b ? 1 : 0));
  }
  
  public final Hasher putDouble(double d)
  {
    return putLong(Double.doubleToRawLongBits(d));
  }
  
  public final Hasher putFloat(float f)
  {
    return putInt(Float.floatToRawIntBits(f));
  }
  
  public Hasher putUnencodedChars(CharSequence charSequence)
  {
    int i = 0; for (int len = charSequence.length(); i < len; i++) {
      putChar(charSequence.charAt(i));
    }
    return this;
  }
  
  public Hasher putString(CharSequence charSequence, Charset charset)
  {
    return putBytes(charSequence.toString().getBytes(charset));
  }
}
