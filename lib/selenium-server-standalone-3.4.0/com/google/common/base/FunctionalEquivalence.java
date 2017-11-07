package com.google.common.base;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import javax.annotation.Nullable;





















@Beta
@GwtCompatible
final class FunctionalEquivalence<F, T>
  extends Equivalence<F>
  implements Serializable
{
  private static final long serialVersionUID = 0L;
  private final Function<F, ? extends T> function;
  private final Equivalence<T> resultEquivalence;
  
  FunctionalEquivalence(Function<F, ? extends T> function, Equivalence<T> resultEquivalence)
  {
    this.function = ((Function)Preconditions.checkNotNull(function));
    this.resultEquivalence = ((Equivalence)Preconditions.checkNotNull(resultEquivalence));
  }
  
  protected boolean doEquivalent(F a, F b)
  {
    return resultEquivalence.equivalent(function.apply(a), function.apply(b));
  }
  
  protected int doHash(F a)
  {
    return resultEquivalence.hash(function.apply(a));
  }
  
  public boolean equals(@Nullable Object obj)
  {
    if (obj == this) {
      return true;
    }
    if ((obj instanceof FunctionalEquivalence)) {
      FunctionalEquivalence<?, ?> that = (FunctionalEquivalence)obj;
      return (function.equals(function)) && (resultEquivalence.equals(resultEquivalence));
    }
    return false;
  }
  
  public int hashCode()
  {
    return Objects.hashCode(new Object[] { function, resultEquivalence });
  }
  
  public String toString()
  {
    return resultEquivalence + ".onResultOf(" + function + ")";
  }
}
