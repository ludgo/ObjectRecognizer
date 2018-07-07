package sk.stuba.fiit.vava.android.ui.activity;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import sk.stuba.fiit.vava.android.R;

/**
 * Template activity with included application settings menu
 */
public abstract class MenuActivity extends UserActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                toSettingsActivity();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * To {@link SettingsActivity}
     */
    private void toSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
