package com.likeparentjp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout;


public class MainActivity extends ActionBarActivity {
	
	private LinearLayout btn_container;
	private Button mResetImage;
	private Button mAnalyze;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        btn_container = (LinearLayout)findViewById(R.id.btn_container);
        mResetImage = (Button) findViewById(R.id.btn_reset);
        mAnalyze = (Button) findViewById(R.id.btn_analyze);
        
        mResetImage.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN ) {
					btn_container.setBackgroundResource(R.drawable.btn1);
					
                    return true;
                }
				if (event.getAction() == MotionEvent.ACTION_UP ) {
					btn_container.setBackgroundResource(R.drawable.btn);
                    return true;
                }
				return false;
			}
		});
        
        mAnalyze.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				if (event.getAction() == MotionEvent.ACTION_DOWN ) {
					btn_container.setBackgroundResource(R.drawable.btn2);
					
                    return true;
                }
				if (event.getAction() == MotionEvent.ACTION_UP ) {
					btn_container.setBackgroundResource(R.drawable.btn);
                    return true;
                }
				return false;
			}
		});
        
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
}
