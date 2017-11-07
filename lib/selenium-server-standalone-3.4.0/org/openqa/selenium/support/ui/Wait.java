package org.openqa.selenium.support.ui;

import java.util.function.Function;

public abstract interface Wait<F>
{
  public abstract <T> T until(Function<? super F, T> paramFunction);
}
