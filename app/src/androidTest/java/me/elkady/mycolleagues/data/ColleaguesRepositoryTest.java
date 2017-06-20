package me.elkady.mycolleagues.data;

import android.support.test.filters.MediumTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import me.elkady.mycolleagues.models.Colleague;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.*;

/**
 * Created by mak on 6/20/17.
 */
@RunWith(AndroidJUnit4.class)
@MediumTest
public class ColleaguesRepositoryTest {
    private ColleaguesRepository mColleaguesRepository;

    @Before
    public void setup() throws Exception {
        mColleaguesRepository = new ColleaguesRepositoryImpl();
    }

    @Test
    public void loadColleagues() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);
        mColleaguesRepository.loadColleagues(new ColleaguesRepository.OnColleaguesLoaded() {
            @Override
            public void onColleaguesLoaded(List<Colleague> recentColleagues, List<Colleague> otherColleagues) {
                assertThat(otherColleagues.size(), greaterThan(0));

                signal.countDown();
            }

            @Override
            public void onError() {
                fail();
                signal.countDown();
            }
        });
        signal.await();
    }

}