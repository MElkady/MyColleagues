package me.elkady.mycolleagues.mycolleagues;

import android.support.annotation.StringRes;

import java.util.List;

import me.elkady.mycolleagues.BaseContract;
import me.elkady.mycolleagues.models.Colleague;

public interface MyColleaguesContract {
    interface View extends BaseContract.BaseView {
        void setColleagues(List<Colleague> recentColleagues, List<Colleague> otherColleagues);
        void showError(@StringRes int errorMessage);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void loadColleagues();
        void reset();
        void giveFeedback(Colleague colleague);
    }
}
