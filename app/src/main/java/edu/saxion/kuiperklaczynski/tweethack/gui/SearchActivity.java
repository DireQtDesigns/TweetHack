package edu.saxion.kuiperklaczynski.tweethack.gui;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;

import edu.saxion.kuiperklaczynski.tweethack.R;

public class SearchActivity extends AppCompatActivity implements TextView.OnEditorActionListener {

    //TODO: Javadoc

    SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("");
        setContentView(R.layout.activity_search);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(R.layout.actionbar_view_search);
        actionBar.setDisplayHomeAsUpEnabled(true);

        EditText search = (EditText) actionBar.getCustomView().findViewById(R.id.searchfield);
        search.setOnEditorActionListener(this);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(v.getText());
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}
