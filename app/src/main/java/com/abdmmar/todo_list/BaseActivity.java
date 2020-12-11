package com.abdmmar.todo_list;

import android.app.Activity;
import android.content.Intent;

public class BaseActivity extends Activity {
    /**
     * Overrides the pending Activity transition by performing the "Enter" animation.
     */
    protected void overridePendingTransitionEnterSwipeRight() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        overridePendingTransitionExitSwipeRight();
    }

    protected void overridePendingTransitionEnterSwipeLeft() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
        overridePendingTransitionExitSwipeLeft();
    }

    /**
     * Overrides the pending Activity transition by performing the "Exit" animation.
     */
    protected void overridePendingTransitionExitSwipeRight() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    protected void overridePendingTransitionExitSwipeLeft() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransitionExitSwipeRight();
    }
}
