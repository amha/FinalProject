package com.udacity.gradle.builditbigger;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.content.Intent;
import android.test.ApplicationTestCase;
import android.util.Log;
import android.util.Pair;

import com.udacity.gradle.builditbigger.MainActivity;
import com.udacity.gradle.builditbigger.MainActivityFragment;

import java.lang.String;
import java.lang.Throwable;
import java.util.concurrent.CountDownLatch;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {

    CountDownLatch countDownLatch = null;
    String returnedJoke;

    Intent mIntent;
    String expectedJoke = "Q: What is the definition of diplomacy?\n" +
            "A: The ability to tell a person to go to hell in " +
            "such a way that they look forward to the trip.";

    public ApplicationTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        countDownLatch = new CountDownLatch(1);
    }

    @Override
    protected void tearDown() throws Exception {
        countDownLatch.countDown();
    }

    public void testEndpointsAsyncTask() throws InterruptedException {
        EndpointsTask task = new EndpointsTask();
        task.setListener(new EndpointsTask.EndpointTaskListener() {
            @Override
            public void onEndpointReturned(String joke) {
                Log.d("AMHA", joke);
                returnedJoke = joke;
                countDownLatch.countDown();
            }
        }).execute(new Pair<>(getContext(), "smile"));

        countDownLatch.await();

        assertEquals(expectedJoke, returnedJoke);
    }
}