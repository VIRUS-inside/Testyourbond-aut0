package org.openqa.selenium.interactions.internal;

import com.google.common.base.Preconditions;
import java.util.Optional;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.Locatable;


























public abstract class BaseAction
{
  protected final Locatable where;
  
  protected BaseAction(Locatable actionLocation)
  {
    where = actionLocation;
  }
  
  protected Optional<WebElement> getTargetElement() {
    if (where == null) {
      return Optional.empty();
    }
    
    Preconditions.checkState(
      where.getCoordinates().getAuxiliary() instanceof WebElement, "%s: Unable to find element to use: %s", this, where
      

      .getCoordinates());
    return Optional.of((WebElement)where.getCoordinates().getAuxiliary());
  }
}
