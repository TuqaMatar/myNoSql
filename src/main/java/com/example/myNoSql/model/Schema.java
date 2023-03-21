package com.example.myNoSql.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Schema {
    private JSONObject jsonSchema;

    public Schema(String jsonSchemaString) throws JSONException {
        this.jsonSchema = new JSONObject(jsonSchemaString);
    }


}
