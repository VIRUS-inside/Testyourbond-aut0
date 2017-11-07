package org.yaml.snakeyaml.events;

import java.util.Map;
import org.yaml.snakeyaml.DumperOptions.Version;
import org.yaml.snakeyaml.error.Mark;





















public final class DocumentStartEvent
  extends Event
{
  private final boolean explicit;
  private final DumperOptions.Version version;
  private final Map<String, String> tags;
  
  public DocumentStartEvent(Mark startMark, Mark endMark, boolean explicit, DumperOptions.Version version, Map<String, String> tags)
  {
    super(startMark, endMark);
    this.explicit = explicit;
    this.version = version;
    



    this.tags = tags;
  }
  
  public boolean getExplicit() {
    return explicit;
  }
  







  public DumperOptions.Version getVersion()
  {
    return version;
  }
  





  public Map<String, String> getTags()
  {
    return tags;
  }
  
  public boolean is(Event.ID id)
  {
    return Event.ID.DocumentStart == id;
  }
}
