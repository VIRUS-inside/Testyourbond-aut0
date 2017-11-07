package org.openqa.selenium.firefox;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.io.CharStreams;
import com.google.common.io.Closeables;
import com.google.common.io.LineReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.JsonToBeanConverter;


































class Preferences
{
  private static final String MAX_SCRIPT_RUN_TIME_KEY = "dom.max_script_run_time";
  private static final int DEFAULT_MAX_SCRIPT_RUN_TIME = 30;
  private static final Pattern PREFERENCE_PATTERN = Pattern.compile("user_pref\\(\"([^\"]+)\", (\"?.+?\"?)\\);");
  
  private Map<String, Object> immutablePrefs = Maps.newHashMap();
  private Map<String, Object> allPrefs = Maps.newHashMap();
  
  public Preferences(Reader defaults) {
    readDefaultPreferences(defaults);
  }
  
  public Preferences(Reader defaults, File userPrefs) {
    readDefaultPreferences(defaults);
    try { FileReader reader = new FileReader(userPrefs);Throwable localThrowable3 = null;
      try { readPreferences(reader);
      }
      catch (Throwable localThrowable1)
      {
        localThrowable3 = localThrowable1;throw localThrowable1;
      } finally {
        if (reader != null) if (localThrowable3 != null) try { reader.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else reader.close();
      } } catch (IOException e) { throw new WebDriverException(e);
    }
  }
  
  @VisibleForTesting
  public Preferences(Reader defaults, Reader reader) {
    readDefaultPreferences(defaults);
    try {
      readPreferences(reader); return;
    } catch (IOException e) {
      throw new WebDriverException(e);
    } finally {
      try {
        Closeables.close(reader, true);
      }
      catch (IOException localIOException2) {}
    }
  }
  
  private void readDefaultPreferences(Reader defaultsReader) {
    try {
      String rawJson = CharStreams.toString(defaultsReader);
      Map<String, Object> map = (Map)new JsonToBeanConverter().convert(Map.class, rawJson);
      
      Map<String, Object> frozen = (Map)map.get("frozen");
      for (Iterator localIterator = frozen.entrySet().iterator(); localIterator.hasNext();) { entry = (Map.Entry)localIterator.next();
        String key = (String)entry.getKey();
        Object value = entry.getValue();
        if ((value instanceof Long)) {
          value = new Integer(((Long)value).intValue());
        }
        setPreference(key, value);
        immutablePrefs.put(key, value);
      }
      
      Object mutable = (Map)map.get("mutable");
      for (Map.Entry<String, Object> entry : ((Map)mutable).entrySet()) {
        Object value = entry.getValue();
        if ((value instanceof Long)) {
          value = new Integer(((Long)value).intValue());
        }
        setPreference((String)entry.getKey(), value);
      }
    } catch (IOException e) { Map.Entry<String, Object> entry;
      throw new WebDriverException(e);
    }
  }
  
  private void setPreference(String key, Object value) {
    if ((value instanceof String)) {
      setPreference(key, (String)value);
    } else if ((value instanceof Boolean)) {
      setPreference(key, ((Boolean)value).booleanValue());
    } else {
      setPreference(key, ((Number)value).intValue());
    }
  }
  
  private void readPreferences(Reader reader) throws IOException {
    LineReader allLines = new LineReader(reader);
    String line = allLines.readLine();
    while (line != null) {
      Matcher matcher = PREFERENCE_PATTERN.matcher(line);
      if (matcher.matches()) {
        allPrefs.put(matcher.group(1), preferenceAsValue(matcher.group(2)));
      }
      line = allLines.readLine();
    }
  }
  
  public void setPreference(String key, String value) {
    checkPreference(key, value);
    if (isStringified(value))
    {
      throw new IllegalArgumentException(String.format("Preference values must be plain strings: %s: %s", new Object[] { key, value }));
    }
    
    allPrefs.put(key, value);
  }
  
  public void setPreference(String key, boolean value) {
    checkPreference(key, Boolean.valueOf(value));
    allPrefs.put(key, Boolean.valueOf(value));
  }
  
  public void setPreference(String key, int value) {
    checkPreference(key, Integer.valueOf(value));
    allPrefs.put(key, Integer.valueOf(value));
  }
  
  public void addTo(Preferences prefs)
  {
    allPrefs.putAll(allPrefs);
  }
  
  public void addTo(FirefoxProfile profile) {
    getAdditionalPreferencesallPrefs.putAll(allPrefs);
  }
  
  public void writeTo(Writer writer) throws IOException {
    for (Map.Entry<String, Object> pref : allPrefs.entrySet()) {
      writer.append("user_pref(\"").append((CharSequence)pref.getKey()).append("\", ");
      writer.append(valueAsPreference(pref.getValue()));
      writer.append(");\n");
    }
  }
  
  private String valueAsPreference(Object value) {
    if ((value instanceof String)) {
      return "\"" + escapeValueAsPreference((String)value) + "\"";
    }
    return escapeValueAsPreference(String.valueOf(value));
  }
  
  private String escapeValueAsPreference(String value) {
    return value.replaceAll("\\\\", "\\\\\\\\").replaceAll("\"", "\\\\\"");
  }
  
  private Object preferenceAsValue(String toConvert) {
    if ((toConvert.startsWith("\"")) && (toConvert.endsWith("\""))) {
      return toConvert.substring(1, toConvert.length() - 1).replaceAll("\\\\\\\\", "\\\\");
    }
    
    if (("false".equals(toConvert)) || ("true".equals(toConvert))) {
      return Boolean.valueOf(Boolean.parseBoolean(toConvert));
    }
    try
    {
      return Integer.valueOf(Integer.parseInt(toConvert));
    } catch (NumberFormatException e) {
      throw new WebDriverException(e);
    }
  }
  
  @VisibleForTesting
  protected Object getPreference(String key) {
    return allPrefs.get(key);
  }
  

  private boolean isStringified(String value)
  {
    return (value.startsWith("\"")) && (value.endsWith("\""));
  }
  
  public void putAll(Map<String, Object> frozenPreferences) {
    allPrefs.putAll(frozenPreferences);
  }
  
  private void checkPreference(String key, Object value) {
    Preconditions.checkNotNull(value);
    Preconditions.checkArgument((!immutablePrefs.containsKey(key)) || (
      (immutablePrefs.containsKey(key)) && (value.equals(immutablePrefs.get(key)))), "Preference %s may not be overridden: frozen value=%s, requested value=%s", key, immutablePrefs
      
      .get(key), value);
    if ("dom.max_script_run_time".equals(key)) {
      int n;
      if ((value instanceof String)) {
        n = Integer.parseInt((String)value); } else { int n;
        if ((value instanceof Integer)) {
          n = ((Integer)value).intValue();
        } else
          throw new IllegalArgumentException(String.format("%s value must be a number: %s", new Object[] { "dom.max_script_run_time", value
            .getClass().getName() })); }
      int n;
      Preconditions.checkArgument((n == 0) || (n >= 30), "%s must be == 0 || >= %s", "dom.max_script_run_time", 30);
    }
  }
}
