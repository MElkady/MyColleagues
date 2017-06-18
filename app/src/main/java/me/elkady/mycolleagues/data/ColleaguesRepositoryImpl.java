package me.elkady.mycolleagues.data;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import me.elkady.mycolleagues.MyColleaguesApp;
import me.elkady.mycolleagues.models.Colleague;

/**
 * Created by mak on 6/18/17.
 */

public class ColleaguesRepositoryImpl implements ColleaguesRepository {
    private static List<Colleague> sRecentColleagues = new ArrayList<>();
    private static List<Colleague> sOtherColleagues = new ArrayList<>();

    private static final Comparator<Colleague> sorter = new Comparator<Colleague>() {
        @Override
        public int compare(Colleague o1, Colleague o2) {
            if(o1.getLastInteractionTime() > o2.getLastInteractionTime()) {
                return 1;
            }
            return -1;
        }
    };
    private static final Comparator<Colleague> reverseSorter = new Comparator<Colleague>() {
        @Override
        public int compare(Colleague o1, Colleague o2) {
            if(o1.getLastInteractionTime() > o2.getLastInteractionTime()) {
                return -1;
            }
            return 1;
        }
    };

    @Override
    public void loadColleagues(final OnColleaguesLoaded onColleaguesLoaded) {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            private boolean hasError = false;

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    InputStream is = MyColleaguesApp.getContext().getAssets().open("colleagues.json");

                    Scanner s = new Scanner(is);
                    StringBuilder sb = new StringBuilder();
                    while (s.hasNextLine()) {
                        sb.append(s.nextLine()).append("\n");
                    }

                    sRecentColleagues = new ArrayList<>();
                    sOtherColleagues = new ArrayList<>();

                    JSONObject jo = new JSONObject(sb.toString());
                    JSONArray users = jo.getJSONArray("users");
                    for(int i = 0; i < users.length(); i++) {
                        Colleague c = new Colleague(users.getJSONObject(i));
                        if(c.isRecentlyInteracted()) {
                            sRecentColleagues.add(c);
                        } else {
                            sOtherColleagues.add(c);
                        }
                    }

                    Collections.sort(sRecentColleagues, reverseSorter);
                    Collections.sort(sOtherColleagues, sorter);
                } catch (Exception ex) {
                    ex.printStackTrace();   // TODO report to crash logging system
                    hasError = true;
                }


                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if(hasError) {
                    onColleaguesLoaded.onError();
                } else {
                    onColleaguesLoaded.onColleaguesLoaded(sRecentColleagues, sOtherColleagues);
                }
            }
        };

        task.execute();
    }

    @Override
    public void reset(OnColleaguesLoaded onColleaguesLoaded) {
        loadColleagues(onColleaguesLoaded);
    }

    @Override
    public void giveFeedback(Colleague colleague, OnColleaguesLoaded onColleaguesLoaded) {
        colleague.addInteraction();
        sOtherColleagues.remove(colleague);
        if(!sRecentColleagues.contains(colleague)) {
            sRecentColleagues.add(0, colleague);
        }

        onColleaguesLoaded.onColleaguesLoaded(sRecentColleagues, sOtherColleagues);
    }

    @Override
    public void getColleagueById(int id, OnColleagueLoaded onColleagueLoaded) {
        Colleague result = null;

        for(Colleague colleague : sRecentColleagues) {
            if(colleague.getId() == id) {
                result = colleague;
                break;
            }
        }

        if(result == null) {
            for(Colleague colleague : sOtherColleagues) {
                if(colleague.getId() == id) {
                    result = colleague;
                    break;
                }
            }
        }

        if(result != null) {
            onColleagueLoaded.onColleagueLoaded(result);
        } else {
            onColleagueLoaded.onError();
        }
    }


}
