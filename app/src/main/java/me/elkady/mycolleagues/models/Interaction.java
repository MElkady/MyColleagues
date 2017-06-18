package me.elkady.mycolleagues.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mak on 6/16/17.
 */

public class Interaction implements Serializable {
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    private int id;
    private Date date;

    public Interaction() {
    }


    public Interaction(JSONObject jsonObject) throws JSONException, ParseException {
        this.id = jsonObject.getInt("id");
        this.date = formatter.parse(jsonObject.getString("date"));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
