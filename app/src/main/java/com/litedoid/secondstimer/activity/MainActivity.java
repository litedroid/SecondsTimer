package com.litedoid.secondstimer.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.litedoid.secondstimer.R;
import com.litedoid.secondstimer.helper.UIHelper;

import org.joda.time.DateTime;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity
{
    private static final String TAG = MainActivity.class.getSimpleName();

//    Create an Android app that accepts user input.
//      That user input should be validated as a parsable integer.
//      This integer will represent a duration for a timer.
//      The app should provide a UI to start that timer.
//      While the timer runs (after it’s started), the UI should show the remaining number of seconds.
//      When the total duration has been met, the UI should show that as well.


    private final static long MS_IN_SECONDS = 1000L;
    private final static long POLL_TIME = 100L;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.timer_action_button)
    Button actionButton;

    @BindView(R.id.seconds_edittext)
    EditText secondsEditText;

    @BindView(R.id.seconds_left_textview)
    TextView secondsLeftTextView;

    private boolean timerRunning = false;

    private Handler serviceHandler;

    private long endTime;

    private int pausedSecondsLeft;

    protected Runnable timerTick = new Runnable()
    {
        public void run()
        {
            int secondsLeft = getSecondsLeft();

            if (secondsLeft <= 0)
            {
                updateTimeLeft(0);
                stopTimer();
            }
            else
            {
                updateTimeLeft(secondsLeft);
                serviceHandler.postDelayed(this, POLL_TIME);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        toolbar.setTitle(R.string.app_name);

        updateTimeLeft(0);

        setTextWatcher();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        stopTimer();
    }

    private void setTextWatcher()
    {
        TextWatcher tw = new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                //changing text will indicate user wants to set a new duration
                pausedSecondsLeft = 0;
            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }
        };

        secondsEditText.addTextChangedListener(tw);
    }

    @OnClick(R.id.timer_action_button)
    protected void onClickActionButton()
    {
        UIHelper.hideKeyboard(MainActivity.this);

        if (!timerRunning)
        {
            if (pausedSecondsLeft > 0)
                startTimer(pausedSecondsLeft);
            else
            {
                String secondsInput = secondsEditText.getText().toString();
                if (secondsInput.length() == 0)
                    return;

                int seconds = Integer.parseInt(secondsInput);

                startTimer(seconds);
            }
        }
        else
        {
            stopTimer();
        }
    }

    private void updateTimeLeft(int secondsLeft)
    {
        Log.d(TAG, "updateTimeLeft: " + secondsLeft);

        secondsLeftTextView.setText(String.valueOf(secondsLeft));
    }

    private int getSecondsLeft()
    {
        long timeLeft = endTime - DateTime.now().getMillis();

        if (timeLeft < 0)
            return 0;

        return (int) (timeLeft / MS_IN_SECONDS);
    }

    private void startTimer(int seconds)
    {
        Log.d(TAG, "startTime: " + seconds);

        timerRunning = true;
        pausedSecondsLeft = 0;

        long currentTime = DateTime.now().getMillis();

        endTime = currentTime + (seconds * MS_IN_SECONDS);

        if (serviceHandler == null)
        {
            serviceHandler = new Handler();
        }

        serviceHandler.postDelayed(timerTick, POLL_TIME);

        actionButton.setText(R.string.stop_text);
    }

    private void stopTimer()
    {
        Log.d(TAG, "stopTimer");

        int secondsLeft = getSecondsLeft();

        if (getSecondsLeft() > 0)
            pausedSecondsLeft = secondsLeft;

        timerRunning = false;
        endTime = 0L;

        if (serviceHandler != null)
        {
            serviceHandler.removeCallbacks(timerTick);
        }

        actionButton.setText(R.string.start_text);
    }

}
