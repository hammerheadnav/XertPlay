package com.hammerhead.xertplay;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.SeekBar;
import android.util.Log;
import com.baronbiosys.xertandroid.Xert;
//import com.example.hammerhead.xertplay.R;

public class MainActivity extends AppCompatActivity {

    private SeekBar s_pp = null, s_ftp = null, s_hie = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        final Button button = findViewById(R.id.button);

        s_pp = (SeekBar) findViewById(R.id.seekBarPeakPower);
        s_pp.setProgress( s_pp.getMax() / 2 );
        s_ftp = (SeekBar) findViewById(R.id.seekBarFTP);
        s_ftp.setProgress( s_ftp.getMax() / 2 );
        s_hie = (SeekBar) findViewById(R.id.seekBarHIE);
        s_hie.setProgress(( s_hie.getMax() / 2 ));

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendMessage( v );
//            TextView testTextView = (TextView) findViewById(R.id.textView);
//            testTextView.setText("this is the new text.");
            }
        });



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


        public void runFakeWorkout( Xert  client ) {
            final double inc = 1.0;
            double sec = 0.0;
            for ( int i = 0; i < 1000; i++, sec += inc ) {
                double vary = ((double)( i % 10 ) ) * 20.0;

                // Add fake power data with timestamp increasing by 1.0 seconds.
                client.AddPower(sec, 180.0 + vary );
            }
        }




    // Called when the user taps the Send button.
        public void sendMessage(View view){

            TextView textView = (TextView) findViewById(R.id.textView);

            // Load XERT, init and get ftp value from it
            Xert client = new Xert() {
                @Override
                public void onUpdate(XertSensorData data, boolean interval_completed) {
                    double whats_my_ftp[] = this.GetWhatsMyFTP();
                    Log.d("xert", "t: " + data.time + "; p: " + data.power_3s_avg + "; ftp_meter: " + whats_my_ftp[1] + "%" + "; mpa: " + data.mpa);

                    double ftp = this.GetFTP();
                }
            };


            // Initialize the library with a fitness signature of {pp:1200, ftp:250, hie:20000}
            client.InitWithCallback( Integer.valueOf(s_pp.getProgress()), Integer.valueOf(s_ftp.getProgress()),  Integer.valueOf(s_hie.getProgress()));

            // Calling Init again will do nothing, as the library checks that it has already been init.
            // client.Init( 1200.0, 300.0, 25000.0 );

            // simulate doing a bunch of power inputs
            runFakeWorkout(client);

            // get result
            double ftp = client.GetFTP();
            double whats_my_ftp[] = client.GetWhatsMyFTP();

            // deallocate internals
            client.Shutdown();


            // report
            String message = "Inputs:\nPP: " + Integer.valueOf(s_pp.getProgress())
                    + "\nFTP: " + Integer.valueOf(s_ftp.getProgress())
                    + "\nHIE: " + Integer.valueOf(s_hie.getProgress())
                    + "\n\n"
                    + "Outputs:\nFTP: " + String.valueOf(ftp) + "\n\n" +
                    "What's My FTP: " + String.valueOf(whats_my_ftp[0]) + "; " + String.valueOf(whats_my_ftp[1]) + "%";

            textView.setText(message);
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
