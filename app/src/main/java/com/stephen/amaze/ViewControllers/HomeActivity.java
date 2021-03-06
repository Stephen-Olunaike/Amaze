package com.stephen.amaze.ViewControllers;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.stephen.amaze.R;

public class HomeActivity extends AppCompatActivity implements GridFragment.OnFragmentInteractionListener {

    private FrameLayout helpLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        helpLayout = (FrameLayout) findViewById(R.id.home_helplayout);
        ImageButton helpButton = (ImageButton) findViewById(R.id.home_helpbutton);
        Button okButton = (Button) findViewById(R.id.home_okbutton);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOrHideHelpLayout();
            }
        });

        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOrHideHelpLayout();
            }
        });

        showGridFragment();
    }

    private void showOrHideHelpLayout() {
        if (helpLayout.getVisibility() == View.VISIBLE) {
            helpLayout.setVisibility(View.INVISIBLE);
        } else {
            helpLayout.setVisibility(View.VISIBLE);
        }
    }

    private void showGridFragment() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        GridFragment fragment = GridFragment.newInstance(null, null);
        transaction.replace(R.id.home_container, fragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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
