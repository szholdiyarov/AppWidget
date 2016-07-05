package com.zholdiyarov.appwidget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zholdiyarov.appwidget.utils.Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by szholdiyarov on 6/28/16.
 * Reference : https://developer.android.com/guide/topics/appwidgets/index.html#Configuring
 * This is the configuration activity. When user add a first instance of the widget this activity is opened and it allows user to save RSS url.
 * Checking of url correctness should be handled here.
 * URL is saved in the Shared Preference and can be found by Util.getRssUrl() . See Util class for more information.
 */
public class WidgetConfigurationActivity extends Activity {

    private final String SAMPLE_RSS_URL = "http://rss.nytimes.com/services/xml/rss/nyt/HomePage.xml"; // just for testing purposes.
    private int mAppWidgetId = 0;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.widget_config_layout);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        final Button button_save = (Button) findViewById(R.id.button_save_rss);
        final EditText editText = (EditText) findViewById(R.id.edit_text_rss);

        editText.setText(SAMPLE_RSS_URL); // show sample url

        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String rssUrl = editText.getText().toString();
                if (!TextUtils.isEmpty(rssUrl)) { // check if url entered by user is not empty
                    closeConfigActivity(rssUrl);
                } else {
                    showWarning("Введите rss");
                }
            }
        });

    }

    /**
     * Called when the activity has become visible.
     */
    @Override
    protected void onResume() {
        super.onResume();
        // NOTHING TO DO HERE
    }

    /**
     * Called when another activity is taking focus.
     */
    @Override
    protected void onPause() {
        super.onPause();
        // NOTHING TO DO HERE
    }

    /**
     * Called when the activity is no longer visible.
     */
    @Override
    protected void onStop() {
        super.onStop();
        // NOTHING TO DO HERE
    }

    /**
     * Check if string is a valid URL address
     */
    private boolean isValidUrl(String url) {
        final String URL_REGEX = "^((https?|ftp)://|(www|ftp)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$";

        Pattern p = Pattern.compile(URL_REGEX);
        Matcher m = p.matcher(url);//replace with string to compare
        if (m.find()) {
            return true;
        }
        return false;
    }

    /**
     * Takes a url as a parameter, checks if it is valid url and then proceeds
     **/
    private void closeConfigActivity(String url) {
        if (isValidUrl(url)) {
            Util.saveRssUrlInSharedPreferences(this, url);
            Intent resultValue = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE, null, this, WidgetConfigurationActivity.class);
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        } else {
            showWarning("Введите правильный RSS");
        }
    }


    private void showWarning(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }


}
