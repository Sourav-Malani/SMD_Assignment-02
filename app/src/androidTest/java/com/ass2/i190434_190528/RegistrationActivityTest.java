package com.ass2.i190434_190528;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class RegistrationActivityTest {

    @Rule
    public ActivityScenarioRule<SignupActivity> activityRule = new ActivityScenarioRule<>(SignupActivity.class);

    @Before
    public void setUp() {
        // Add any setup code here, if needed.
    }

    @Test
    public void testSuccessfulSignUp() {
        // Input valid user details and click the Sign Up button
        //Espresso.onView(ViewMatchers.withId(R.id.User_Name)).perform(ViewActions.typeText("Sourav Malani"));
        Espresso.onView(ViewMatchers.withId(R.id.User_Email)).perform(ViewActions.typeText("souravmalani88@gmail.com"));
        Espresso.onView(ViewMatchers.withId(R.id.User_Password)).perform(ViewActions.typeText("malani213"));
        Espresso.onView(ViewMatchers.withId(R.id.btn_signUp)).perform(ViewActions.click());

        // Add assertions to verify the expected behavior, such as navigating to the next activity

    }

    @Test
    public void testEmptyFields() {
        // Leave fields empty and click the Sign Up button
        Espresso.onView(ViewMatchers.withId(R.id.btn_signUp)).perform(ViewActions.click());

        // Add assertions to verify that the app displays a message about empty fields.
    }

    @Test
    public void testInvalidEmail() {
        // Input an invalid email and click the Sign Up button
        Espresso.onView(ViewMatchers.withId(R.id.User_Email)).perform(ViewActions.typeText("invalid-email"));
        Espresso.onView(ViewMatchers.withId(R.id.User_Password)).perform(ViewActions.typeText("password123"));
        Espresso.onView(ViewMatchers.withId(R.id.btn_signUp)).perform(ViewActions.click());

        // Add assertions to verify that the app displays a message about an invalid email format.
    }

    // Add more test cases to cover different scenarios, such as invalid passwords and email collisions.
}