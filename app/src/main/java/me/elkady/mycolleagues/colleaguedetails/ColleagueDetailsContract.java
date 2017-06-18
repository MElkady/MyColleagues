package me.elkady.mycolleagues.colleaguedetails;

import android.support.annotation.StringRes;

import me.elkady.mycolleagues.BaseContract;
import me.elkady.mycolleagues.models.Colleague;

/**
 * Created by mak on 6/19/17.
 */

public interface ColleagueDetailsContract {
    interface View extends BaseContract.BaseView {
        void displayColleague(Colleague colleague);
        void showError(@StringRes int errorMessage);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void getColleagueById(int id);
    }
}
