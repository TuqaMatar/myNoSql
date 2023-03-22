package com.example.myNoSql.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Schema {
    private String name;
    private String $schema;
    private String title;
    private String type;
    private SchemaProperties properties;

    public static class SchemaProperties {
        private Data data;

        public static class Data {
            private String type;
            private DataProperties properties;
            private List<String> required;

            public static class DataProperties {
                private StringProperty title;
                private StringProperty author;
                private IntegerProperty year;
                private StringProperty genre;

                public static class StringProperty {
                    private String type;
                }

                public static class IntegerProperty {
                    private String type;
                }
            }
        }
    }

}