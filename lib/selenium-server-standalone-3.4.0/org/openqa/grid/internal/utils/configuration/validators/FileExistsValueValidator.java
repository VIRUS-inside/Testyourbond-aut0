package org.openqa.grid.internal.utils.configuration.validators;

import com.beust.jcommander.IValueValidator;
import com.beust.jcommander.ParameterException;
import java.io.File;














public class FileExistsValueValidator<T>
  implements IValueValidator<T>
{
  public FileExistsValueValidator() {}
  
  public void validate(String parameterName, T parameterValue)
    throws ParameterException
  {
    File file = new File((String)parameterValue);
    if (!file.isFile()) {
      throw new ParameterException(parameterValue + " is not a valid file.");
    }
  }
}
