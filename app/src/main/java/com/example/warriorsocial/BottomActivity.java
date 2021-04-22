package com.example.warriorsocial;

import android.app.Notification;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

// For access to NOTIFICATION_S in SettingsFragment
import static com.example.warriorsocial.ui.settings.SettingsFragment.NOTIFICATION_S;


// This is the activity that holds the icons on the bottom of the screen
public class BottomActivity extends AppCompatActivity {

    // For sending notifications
    private NotificationManagerCompat notificationManager;
    private static BottomActivity instance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("Inside BottomActivity onCreate");
        setContentView(R.layout.activity_bottom);

        // For sending notifications
        notificationManager = NotificationManagerCompat.from(this);

        // Connect to the icons
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Configuration for the action bar (top bar) with established hierarchies
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_organizations, R.id.navigation_discover, R.id.navigation_settings)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        // Set instance to this for notifications
        instance = this;

    }

    // Navigation for back arrows on fragments
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Ensures that back arrow will navigate to parent fragment
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    // To send notifications
    public void sendNotification(View v) {
        Notification notification = new NotificationCompat.Builder(BottomActivity.this, NOTIFICATION_S)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle("Greetings from Warrior Social")
                .setContentText("Someone liked your post!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        notificationManager.notify(1, notification);
    }

    // For sending notifications to SettingsFragment
    public static BottomActivity getInstance() {
        return instance;
    }
}
