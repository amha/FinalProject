package com.udacity.gradle.builditbigger;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Pair;
import java.lang.String;
import java.util.concurrent.CountDownLatch;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {

    CountDownLatch countDownLatch = null;
    String returnedJoke;

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
        // This method of testing the async task was inspired by
        // http://marksunghunpark.blogspot.com/2015/05/how-to-test-asynctask-in-android.html
        EndpointsTask task = new EndpointsTask();
        task.setListener(new EndpointsTask.EndpointTaskListener() {
            @Override
            public void onEndpointReturned(String joke) {
                returnedJoke = joke;
                countDownLatch.countDown();
            }
        }).execute(new Pair<>(getContext(), "smile"));

        countDownLatch.await();

        assertEquals(expectedJoke, returnedJoke);
    }
}