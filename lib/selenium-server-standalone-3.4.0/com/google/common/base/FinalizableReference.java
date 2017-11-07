package com.google.common.base;

import com.google.common.annotations.GwtIncompatible;

@GwtIncompatible
public abstract interface FinalizableReference
{
  public abstract void finalizeReferent();
}
