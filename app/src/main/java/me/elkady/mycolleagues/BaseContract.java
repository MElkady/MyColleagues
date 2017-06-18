package me.elkady.mycolleagues;

/**
 * Created by mak on 6/16/17.
 */

public interface BaseContract {
    interface BasePresenter<T extends BaseView> {
        void attachView(T view);
        void detachView();
    }

    interface BaseView {
    }
}
