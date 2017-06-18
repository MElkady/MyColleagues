package me.elkady.mycolleagues.data;

import java.util.List;

import me.elkady.mycolleagues.models.Colleague;

/**
 * Created by mak on 6/18/17.
 */

public interface ColleaguesRepository {
    void loadColleagues(OnColleaguesLoaded onColleaguesLoaded);
    void reset(OnColleaguesLoaded onColleaguesLoaded);
    void giveFeedback(Colleague colleague, OnColleaguesLoaded onColleaguesLoaded);
    void getColleagueById(int id, OnColleagueLoaded onColleagueLoaded);

    interface OnColleaguesLoaded{
        void onColleaguesLoaded(List<Colleague> recentColleagues, List<Colleague> otherColleagues);
        void onError();
    }

    interface OnColleagueLoaded {
        void onColleagueLoaded(Colleague colleague);
        void onError();
    }
}
