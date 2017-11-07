package org.openqa.grid.internal.utils.configuration.converters;

import com.beust.jcommander.IStringConverter;
import org.openqa.selenium.remote.DesiredCapabilities;
















public class BrowserDesiredCapabilityConverter
  implements IStringConverter<DesiredCapabilities>
{
  public BrowserDesiredCapabilityConverter() {}
  
  public DesiredCapabilities convert(String value)
  {
    DesiredCapabilities capabilities = new DesiredCapabilities();
    for (String cap : value.split(",")) {
      String[] pieces = cap.split("=");
      String capabilityName = pieces[0].trim();
      String capabilityValue = pieces[1].trim();
      if (capabilityName.equals("version"))
      {
        capabilities.setCapability(capabilityName, capabilityValue);
      } else {
        try
        {
          Long x = Long.valueOf(Long.parseLong(capabilityValue));
          capabilities.setCapability(capabilityName, x);
        }
        catch (NumberFormatException e) {
          if ((capabilityValue.equals("true")) || (capabilityValue.equals("false"))) {
            capabilities.setCapability(capabilityName, Boolean.parseBoolean(capabilityValue));
          } else
            capabilities.setCapability(capabilityName, capabilityValue);
        }
      }
    }
    return capabilities;
  }
}
