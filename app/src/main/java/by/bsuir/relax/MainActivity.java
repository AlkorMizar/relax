package by.bsuir.relax;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

import by.bsuir.relax.ui.eyes.EyesFragment;
import by.bsuir.relax.ui.help.HelpSlider;
import by.bsuir.relax.ui.home.HomeFragment;
import by.bsuir.relax.ui.music.MusicFragment;
import by.bsuir.relax.ui.profile.ProfileFragment;
import by.bsuir.relax.user.HeightListner;
import by.bsuir.relax.user.ImgListner;
import by.bsuir.relax.user.Mood;
import by.bsuir.relax.user.MoodListener;
import by.bsuir.relax.user.UserDistributor;
import by.bsuir.relax.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private  View headerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        binding.appBarMain.toolbar.setOnClickListener(view -> {
            NavigationView navigationView = findViewById(R.id.nav_view);
            View headerView = navigationView.getHeaderView(0);
            TextView mood = headerView.findViewById(R.id.emotion);
            mood.setText(UserDistributor.getUser().getCurrentMood().toString());
        });

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        headerView= navigationView.getHeaderView(0);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_profile, R.id.nav_statistic)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        setUpUser();
        logOutBtn();
        setUpAbout();
        setUpHelp();

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);

        binding.appBarMain.bottomNavigationView.setOnItemSelectedListener((item)->{

            switch (item.getItemId()){
                case R.id.navigation_home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.navigation_profile:
                    replaceFragment(new ProfileFragment());
                    break;
                case R.id.navigation_music:
                    replaceFragment(new MusicFragment());
                    break;
            }
            return true;
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void setUpUser(){
        System.out.println(UserDistributor.getUser().getAvatar());
        binding.appBarMain.upMenuAva.setImageBitmap(UserDistributor.getUser().getAvatar());

        TextView navUsername = headerView.findViewById(R.id.name);
        navUsername.setText(UserDistributor.getUser().getName());
        TextView moodTxt = headerView.findViewById(R.id.emotion);
        moodTxt.setText(UserDistributor.getUser().getCurrentMood().toString());
        ImageView ava = headerView.findViewById(R.id.avatar);
        ava.setImageBitmap(UserDistributor.getUser().getAvatar());
        TextView imt=headerView.findViewById(R.id.bmi_calc);
        imt.setText(UserDistributor.getUser().getBMI());
        UserDistributor.getUser().addListener(mood -> moodTxt.setText(mood.toString()));

        UserDistributor.getUser().addListener((HeightListner) () -> {
            imt.setText(UserDistributor.getUser().getBMI());
        });

        UserDistributor.getUser().addListener((ImgListner) () -> {
            ava.setImageBitmap(UserDistributor.getUser().getAvatar());
            binding.appBarMain.upMenuAva.setImageBitmap(UserDistributor.getUser().getAvatar());
        });
    }

    private void logOutBtn(){
        headerView.findViewById(R.id.log_out_btn).setOnClickListener(view -> {
            UserDistributor.logout();
            Intent intent = new Intent(headerView.getContext(), RegistrActivity.class);
            startActivity(intent);
        });
    }

    private void setUpAbout(){
        binding.navView.findViewById(R.id.about_btn).setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
            startActivity(intent);
        });
    }

    private void setUpHelp(){

        binding.navView.findViewById(R.id.help_btn).setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), HelpSlider.class);
            startActivity(intent);
        });
    }

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private long lastUpdate = System.currentTimeMillis();
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 50;

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);

                float speed = Math.abs(x + y + z - last_x - last_y - last_z)/ diffTime * 10000;

                if (speed > SHAKE_THRESHOLD) {
                    lastUpdate = curTime;
                }else{
                    if (diffTime>60000){
                        replaceFragment(new EyesFragment());
                        lastUpdate=curTime;
                    }
                }

                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void replaceFragment(Fragment fragment){
        if(fragment!=null){
            FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
            trans.replace(R.id.nav_host_fragment_content_main,fragment).addToBackStack( "tag" ).commit();
        }

    }

    protected void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
    }

    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}