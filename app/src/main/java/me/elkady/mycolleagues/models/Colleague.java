package me.elkady.mycolleagues.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by mak on 6/16/17.
 */

public class Colleague implements Serializable {
    private int id;
    private String name;
    private String email;
    private String avatar;
    private List<Interaction> last_interactions;
    private boolean recentlyInteracted = false;
    private long lastInteractionTime = 0;

    public Colleague() {
    }

    public Colleague(JSONObject jsonObject) throws JSONException, ParseException {
        id = jsonObject.getInt("id");
        name = jsonObject.getString("name");
        email = jsonObject.getString("email");
        avatar = jsonObject.getString("avatar");

        last_interactions = new ArrayList<>();
        JSONArray interactions = jsonObject.getJSONArray("last_interactions");
        for(int i = 0; i < interactions.length(); i++) {
            last_interactions.add(new Interaction(interactions.getJSONObject(i)));
        }

        calculateRecentlyInteracted();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public List<Interaction> getLastInteractions() {
        return last_interactions;
    }

    public void setLastInteractions(List<Interaction> last_interactions) {
        this.last_interactions = last_interactions;
        calculateRecentlyInteracted();
    }

    public long getLastInteractionTime() {
        return lastInteractionTime;
    }

    public boolean isRecentlyInteracted() {
        return recentlyInteracted;
    }

    public void addInteraction() {
        if(last_interactions == null) {
            last_interactions = new ArrayList<>();
        }

        Interaction i = new Interaction();
        i.setId((int) (Math.random() * 1000));
        i.setDate(new Date());
        last_interactions.add(i);
        calculateRecentlyInteracted();
    }

    private void calculateRecentlyInteracted(){
        recentlyInteracted = false;

        long now = System.currentTimeMillis();
        long comparisonPeriod = 1000 * 60 * 60 * 24 * 14;   // 14 days

        lastInteractionTime = 0;

        if (this.last_interactions != null) {
            for(Interaction interaction : last_interactions) {
                if(lastInteractionTime < interaction.getDate().getTime()) {
                    lastInteractionTime = interaction.getDate().getTime();
                }
            }
        }

        if(now - lastInteractionTime < comparisonPeriod) {
            recentlyInteracted = true;
        }
    }
}
