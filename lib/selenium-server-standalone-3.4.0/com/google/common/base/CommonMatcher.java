package com.google.common.base;

import com.google.common.annotations.GwtCompatible;

@GwtCompatible
abstract class CommonMatcher
{
  CommonMatcher() {}
  
  abstract boolean matches();
  
  abstract boolean find();
  
  abstract boolean find(int paramInt);
  
  abstract String replaceAll(String paramString);
  
  abstract int end();
  
  abstract int start();
}
