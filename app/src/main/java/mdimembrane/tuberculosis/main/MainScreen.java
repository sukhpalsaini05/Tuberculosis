package mdimembrane.tuberculosis.main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;

import mdimembrane.tuberculosis.main_fragments.HomeFragment;
import mdimembrane.tuberculosis.main_fragments.MedicineMainFragment;
import mdimembrane.tuberculosis.main_fragments.PatientMainFragment;
import mdimembrane.tuberculosis.main_fragments.ReportMainFragment;
import mdimembrane.tuberculosis.main_fragments.SampleMainFragment;

public class MainScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences sharedpreferences;

    ImageView profilePicIMV;
    TextView usernameTV,accountTypeTV;
    DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedpreferences = getSharedPreferences(PreferencesConstants.APP_MAIN_PREF, Context.MODE_PRIVATE);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView =  navigationView.getHeaderView(0);

        if (savedInstanceState == null) {
            MenuItem item =  navigationView.getMenu().getItem(0);
            onNavigationItemSelected(item);
            navigationView.setCheckedItem(0);
        }

        profilePicIMV=(ImageView)hView.findViewById(R.id.profilePicIMV);
        usernameTV=(TextView)hView.findViewById(R.id.userName);
        accountTypeTV=(TextView)hView.findViewById(R.id.accountType);
        SetProfileInfo();
        drawer.openDrawer(Gravity.START);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_screen, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        Fragment fragment = null;
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {
            fragment = new HomeFragment();
        } else if (id == R.id.nav_patients) {
            fragment = new PatientMainFragment();
        } else if (id == R.id.nav_samples) {
            fragment = new SampleMainFragment();

        } else if (id == R.id.nav_medicine) {
            fragment = new MedicineMainFragment();

        } else if (id == R.id.nav_reports) {
            fragment = new ReportMainFragment();

        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(MainScreen.this,AccountSettings.class);
            startActivity(intent);
        } else if (id == R.id.nav_help) {
            Intent intent = new Intent(MainScreen.this,AccountHelp.class);
            startActivity(intent);

        }else if (id == R.id.nav_aboutus) {

        }else if (id == R.id.nav_logout) {
            LogoutAlert();
        }


        // Insert the fragment by replacing any existing fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void SetProfileInfo()
    {
        usernameTV.setText(sharedpreferences.getString(PreferencesConstants.SessionManager.MY_PERSON_NAME,"NA"));
        accountTypeTV.setText(sharedpreferences.getString(PreferencesConstants.SessionManager.MY_ACCOUNT_TYPE,"NA"));

        Bitmap  bitmap2;
        ByteArrayOutputStream bytearrayoutputstream;
        bytearrayoutputstream = new ByteArrayOutputStream();
        byte[] BYTE;
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        Bitmap sourceBitmap=BitmapFactory.decodeFile(new FileHandling(getApplicationContext()).getOutputMediaFile().toString());
        Bitmap rotatedBitmap = Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight(), matrix, true);

        rotatedBitmap.compress(Bitmap.CompressFormat.JPEG,20,bytearrayoutputstream);
        BYTE = bytearrayoutputstream.toByteArray();
        bitmap2 = BitmapFactory.decodeByteArray(BYTE,0,BYTE.length);

        profilePicIMV.setImageBitmap(bitmap2);
    }

    public void LogoutAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setTitle(getResources().getString(R.string.logout_alert));
        alertDialogBuilder.setMessage(getResources().getString(R.string.logout_alert_message));
        alertDialogBuilder.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.clear();
                        editor.commit();

                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                });
        alertDialogBuilder.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
