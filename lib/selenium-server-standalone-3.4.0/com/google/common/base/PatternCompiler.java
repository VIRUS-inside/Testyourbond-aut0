package com.google.common.base;

import com.google.common.annotations.GwtIncompatible;

@GwtIncompatible
abstract interface PatternCompiler
{
  public abstract CommonPattern compile(String paramString);
}
