package com.ifesdjeen.cascading.cassandra;

import java.util.*;

public class SettingsHelper {

    public static  Map<String, String> getTypesByKey( Map<String, Object> settings, String key ) {
        if (settings.containsKey(key)) {
            return (Map<String, String>) settings.get(key);
        } else {
            throw new RuntimeException("no config type specs for key: " + key);
        }
    }

    /* static types and mappings */

    public static Map<String, String> getTypes( Map<String, Object> settings ) {
        return getTypesByKey(settings, "types");
    }

    public static List<String> getSourceMappings( Map<String, Object> settings ) {
        Object obj = settings.get("mappings.source");
        if (obj instanceof String) {
            List<String> singleMapping = Arrays.asList( (String)obj);
            return singleMapping;
        } else if (obj instanceof List) {
            return (List<String>)obj;
        } else {
            throw new RuntimeException("mappings.source must be a String or List<String>");
        }
    }

    public static String getSinkMappingRowKeyField( Map<String, Object> settings ) {
        String rowKeyField = (String)settings.get("mappings.sink.rowKeyField");
        if (rowKeyField == null) {
            throw new RuntimeException("no setting: mappings.sink.rowKeyField");
        }
        return rowKeyField;
    }

    public static Map<String,String> getSinkMappings( Map<String, Object> settings ) {
        Map<String, String> sinkMappings = (Map<String,String>)settings.get("mappings.sink");
        if (sinkMappings == null ) {
            throw new RuntimeException("no setting: mappings.sink");
        }
        return sinkMappings;
    }

    /* dynamic types and mappings */

    public static Map<String, String> getDynamicTypes( Map<String, Object> settings ) {
        Map<String, String> dynamicTypes = getTypesByKey(settings, "types.dynamic");
        if (dynamicTypes == null) {
            throw new RuntimeException( "no setting: types.dynamic" );
        }
        return dynamicTypes;
    }

    public static Map<String,String> getDynamicMappings( Map<String, Object> settings ) {
        Map<String, String> dynamicMappings = (Map<String,String>)settings.get("mappings.dynamic");
        if (dynamicMappings == null) {
            throw new RuntimeException( "no setting: mappings.dynamic" );
        }
        return dynamicMappings;
    }

    /** is the mapping dynamic */
    public static boolean isDynamicMapping( Map<String, Object> settings ) {
        return settings.containsKey("types.dynamic") &&
            settings.containsKey("mappings.dynamic");
    }

    /** is the mapping static */
    public static boolean isStaticMapping( Map<String, Object> settings ) {
        return settings.containsKey("types") &&
            ( settings.containsKey("mappings.source") ||
              (settings.containsKey("mappings.sink.rowKey") &&
               settings.containsKey("mappings.sink")));
    }

    /** checks the dynamic types have rowKey, columnName and columnValue types, and
        the dynamic mapping has rowKey, columnName and columnValue fields */
    public static void validateDynamicMapping( Map<String, Object> settings ) {
    }

    /** checks the */
    public static void validateStaticSourceMapping( Map<String, Object> settings ) {
    }

    /** checks the static sink mapping has */
    public static void validateStaticSinkMapping( Map<String, Object> settings ) {
    }

    /** checks that the settings contain either a static or dynamic mapping, but not both */
    public static void validateMapping( Map<String, Object> settings ) {
        boolean dyn = isDynamicMapping(settings);
        boolean stat = isStaticMapping(settings);

        if (dyn && stat) {
            throw new RuntimeException( "you can't specify both static and dynamic mappings" );
        }

        if (!dyn && !stat) {
            throw new RuntimeException( "you must specify either static or dynamic mappings" );
        }
    }


}
