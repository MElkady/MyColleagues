package me.elkady.mycolleagues.mycolleagues;

import java.util.List;

import me.elkady.mycolleagues.R;
import me.elkady.mycolleagues.data.ColleaguesRepository;
import me.elkady.mycolleagues.models.Colleague;

/**
 * Created by mak on 6/16/17.
 */

public class MyColleaguesPresenter implements MyColleaguesContract.Presenter {
    private MyColleaguesContract.View mView;
    private ColleaguesRepository mColleaguesRepository;

    public MyColleaguesPresenter(ColleaguesRepository colleaguesRepository) {
        this.mColleaguesRepository = colleaguesRepository;
    }

    @Override
    public void attachView(MyColleaguesContract.View view) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        this.mView = null;
    }


    @Override
    public void loadColleagues() {
        mColleaguesRepository.loadColleagues(new ColleaguesRepository.OnColleaguesLoaded() {
            @Override
            public void onColleaguesLoaded(List<Colleague> recentColleagues, List<Colleague> otherColleagues) {
                if(mView != null) {
                    mView.setColleagues(recentColleagues, otherColleagues);
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

    @Override
    public void reset() {
        mColleaguesRepository.reset(new ColleaguesRepository.OnColleaguesLoaded() {
            @Override
            public void onColleaguesLoaded(List<Colleague> recentColleagues, List<Colleague> otherColleagues) {
                if(mView != null) {
                    mView.setColleagues(recentColleagues, otherColleagues);
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

    @Override
    public void giveFeedback(Colleague colleague) {
        mColleaguesRepository.giveFeedback(colleague, new ColleaguesRepository.OnColleaguesLoaded() {
            @Override
            public void onColleaguesLoaded(List<Colleague> recentColleagues, List<Colleague> otherColleagues) {
                if(mView != null) {
                    mView.setColleagues(recentColleagues, otherColleagues);
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
