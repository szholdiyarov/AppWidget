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
 */
public class WidgetConfigurationActivity extends Activity {
    String testing = "http://rss.nytimes.com/services/xml/rss/nyt/HomePage.xml";
    int mAppWidgetId = 0;

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
        Button button_save = (Button) findViewById(R.id.button_save_rss);
        final EditText editText = (EditText) findViewById(R.id.edit_text_rss);
        editText.setText(testing);
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String rssUrl = editText.getText().toString();
                if (!TextUtils.isEmpty(rssUrl)) {
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
    }

    /**
     * Called when another activity is taking focus.
     */
    @Override
    protected void onPause() {
        super.onPause();

    }

    /**
     * Called when the activity is no longer visible.
     */
    @Override
    protected void onStop() {
        super.onStop();
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
