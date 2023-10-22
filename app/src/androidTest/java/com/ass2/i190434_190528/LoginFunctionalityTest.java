package com.ass2.i190434_190528;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.platform.app.InstrumentationRegistry;

import com.ass2.i190434_190528.LoginActivity;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4ClassRunner.class)
public class LoginFunctionalityTest {

    private String validEmail = "souravmalani95@gmail.com";
    private String validPassword = "malani213";
    private String invalidEmail = "invalid@example.com";
    private String invalidPassword = "invalid123";
    private Context context;

    @Rule
    public ActivityScenarioRule<LoginActivity> activityScenarioRule = new ActivityScenarioRule<>(LoginActivity.class);

    @Before
    public void setUp() {
        // Disable animations
        ActivityScenario.launch(LoginActivity.class);
        Espresso.onIdle(); // Wait for any idle animations to finish
    }



    @Test
    public void testFailedLogin() {
        // Enter invalid email and password, and click the login button
        Espresso.onView(ViewMatchers.withId(R.id.User_Email))
                .perform(ViewActions.typeText(invalidEmail));

        Espresso.onView(ViewMatchers.withId(R.id.User_Password))
                .perform(ViewActions.typeText(invalidPassword));
        //close soft keyboard
        Espresso.closeSoftKeyboard();

        Espresso.onView(ViewMatchers.withId(R.id.btn_signIn))
                .perform(ViewActions.click());

        String errorMessage = mActivityRule.getActivity().getErrorMessage();

         assertEquals("Authentication failed.", errorMessage);    }

    @Test
    public void testEmptyFields() {
        // Leave email and password fields empty, and click the login button
        Espresso.onView(ViewMatchers.withId(R.id.btn_signIn))
                .perform(ViewActions.click());

        // Check if error messages are displayed for empty fields
        Espresso.onView(ViewMatchers.withId(R.id.User_Email))
                .check(ViewAssertions.matches(ViewMatchers.hasErrorText("Username or Email cannot be empty")));
        Espresso.onView(ViewMatchers.withId(R.id.User_Password))
                .check(ViewAssertions.matches(ViewMatchers.hasErrorText("Password cannot be empty")));
    }

    @Test
    public void testNavigateToSignup() {
        // Click on the "Sign Up" text view
        Espresso.onView(ViewMatchers.withId(R.id.txt_signUp))
                .perform(ViewActions.click());

        // Check if the app navigates to the SignupActivity or a relevant view
        // You may want to add assertions specific to the behavior when navigating to the signup screen
    }

    // Helper method to clear shared preferences
    private void clearSharedPreferences() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
    }
}
