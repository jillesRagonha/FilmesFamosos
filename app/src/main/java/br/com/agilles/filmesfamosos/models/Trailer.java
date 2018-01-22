package br.com.agilles.filmesfamosos.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by jille on 22/01/2018.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Trailer {
    private String id;
    private String key;
    private String name;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
