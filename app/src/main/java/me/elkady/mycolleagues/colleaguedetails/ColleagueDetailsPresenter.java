package me.elkady.mycolleagues.colleaguedetails;

import me.elkady.mycolleagues.R;
import me.elkady.mycolleagues.data.ColleaguesRepository;
import me.elkady.mycolleagues.models.Colleague;

/**
 * Created by mak on 6/19/17.
 */

public class ColleagueDetailsPresenter implements ColleagueDetailsContract.Presenter {
    private ColleagueDetailsContract.View mView;
    private ColleaguesRepository mColleaguesRepository;

    ColleagueDetailsPresenter (ColleaguesRepository colleaguesRepository) {
        mColleaguesRepository = colleaguesRepository;
    }

    @Override
    public void attachView(ColleagueDetailsContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void getColleagueById(int id) {
        mColleaguesRepository.getColleagueById(id, new ColleaguesRepository.OnColleagueLoaded() {
            @Override
            public void onColleagueLoaded(Colleague colleague) {
                if(mView != null) {
                    mView.displayColleague(colleague);
                }
            }

            @Override
            public void onError() {
                if(mView != null) {
                    mView.showError(R.string.error_loading_data);
                }
            }
        });
    }
}
