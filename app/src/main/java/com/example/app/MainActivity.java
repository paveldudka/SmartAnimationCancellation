package com.example.app;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener {

    Button b;
    ObjectAnimator anim;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b = (Button)findViewById(R.id.but);
        b.setOnClickListener(this);
        findViewById(R.id.cancel).setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.but)
        {
            handleBut();
        }
        else
        {
            handleCancel();
        }

    }

    private void handleCancel() {
        if(anim != null && anim.isRunning())
        {
            ((MyInterpolator)anim.getInterpolator()).cancel();
        }
        else
        {
            b.animate().translationY(0);
        }
    }

    private void handleBut() {
        anim = ObjectAnimator.ofFloat(b, View.TRANSLATION_Y, 1000);
        anim.setInterpolator(new MyInterpolator());
        anim.setDuration(1000);
        anim.start();
    }

    static class MyInterpolator extends AccelerateDecelerateInterpolator {

        private float phaseShift = 0f;
        private boolean isCancelled = false;
        private float lastInput = 0f;

        /**
         * Maps a value representing the elapsed fraction of an animation to a value that represents
         * the interpolated fraction. This interpolated value is then multiplied by the change in
         * value of an animation to derive the animated value at the current elapsed animation time.
         *
         * @param input A value between 0 and 1.0 indicating our current point
         *              in the animation where 0 represents the start and 1.0 represents
         *              the end
         * @return The interpolation value. This value can be more than 1.0 for
         * interpolators which overshoot their targets, or less than 0 for
         * interpolators that undershoot their targets.
         */
        @Override
        public float getInterpolation(float input) {
            lastInput = input;
            if(!isCancelled)
            {
                return super.getInterpolation(input);
            }
            else
            {
                return getCancellationInterpolation(input) - phaseShift;
            }
        }

        public void cancel()
        {
            isCancelled = true;
            this.phaseShift = getCancellationInterpolation(lastInput) - super.getInterpolation(lastInput);

        }

        private float getCancellationInterpolation(float input)
        {
            return (1.0f - (1.0f - input) * (1.0f - input));
        }
    }
}
