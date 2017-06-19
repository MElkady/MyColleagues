package me.elkady.mycolleagues.mycolleagues;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import me.elkady.mycolleagues.data.ColleaguesRepository;
import me.elkady.mycolleagues.models.Colleague;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

/**
 * Created by MAK on 6/19/17.
 */
public class MyColleaguesPresenterTest {
    private MyColleaguesContract.Presenter mPresenter;

    @Mock
    private ColleaguesRepository mColleaguesRepository;

    @Mock
    private MyColleaguesContract.View mView;

    @Captor
    private ArgumentCaptor<ColleaguesRepository.OnColleaguesLoaded> mOnColleaguesLoaded;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mPresenter = new MyColleaguesPresenter(mColleaguesRepository);
        mPresenter.attachView(mView);
    }

    @After
    public void tearDown() throws Exception {
        mPresenter.detachView();
    }

    @Test
    public void loadColleagues() throws Exception {
        List<Colleague> otherColleagues = new ArrayList<>();
        List<Colleague> recentColleagues = new ArrayList<>();

        mPresenter.loadColleagues();

        verify(mColleaguesRepository).loadColleagues(mOnColleaguesLoaded.capture());
        mOnColleaguesLoaded.getValue().onColleaguesLoaded(recentColleagues, otherColleagues);

        verify(mView).setColleagues(recentColleagues, otherColleagues);
    }

}