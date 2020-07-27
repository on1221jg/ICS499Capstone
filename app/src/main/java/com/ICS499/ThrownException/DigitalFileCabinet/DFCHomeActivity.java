package com.ICS499.ThrownException.DigitalFileCabinet;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

/**
 * UI class and home of the digital file cabinet
 */
public class DFCHomeActivity extends AppCompatActivity {
    private FileCabinet cabinet;
    private User accountUser = null;
    public static final String TAG = "DFCHomeActivity";
    private Toolbar toolbar;
    private String[] navigationDrawerItemTitles;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private CharSequence drawerTitle;
    private CharSequence title;
    private ActionBarDrawerToggle drawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        cabinet = FileCabinet.getInstance(getApplication());
        accountUser = cabinet.getUser();

        title = drawerTitle = getTitle();
        navigationDrawerItemTitles= getResources().getStringArray(R.array.navigation_drawer_items_array);
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerList = findViewById(R.id.left_drawer);

        /* set up the navigation many items*/
        setupToolbar();
        NavigationDataModel[] drawerItem = new NavigationDataModel[7];

        drawerItem[0] = new NavigationDataModel(R.drawable.ic_scanner_24, "Scan");
        drawerItem[1] = new NavigationDataModel(R.drawable.ic_import_export_24, "import");
        drawerItem[2] = new NavigationDataModel(R.drawable.ic_encrypt_24, "Encrypt");
        drawerItem[3] = new NavigationDataModel(R.drawable.ic_decrypt_24, "Decrypt");
        drawerItem[4] = new NavigationDataModel(R.drawable.ic_browse_24, "Browse");
        drawerItem[5] = new NavigationDataModel(R.drawable.ic_edit_24, "Edit");
        drawerItem[6] = new NavigationDataModel(R.drawable.ic_logout_24, "Logout");

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        getSupportActionBar().setHomeButtonEnabled(true);
        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.navigation_list_view, drawerItem);
        drawerList.setAdapter(adapter);
        drawerList.setOnItemClickListener(new DrawerItemClickListener());
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.addDrawerListener(drawerToggle);
        setupDrawerToggle();


        final TextView userName = findViewById(R.id.profile_name_textView);
        final TextView userProfile = findViewById(R.id.profileDetailTextView);
        final Button profileButton = findViewById(R.id.profile_button);

        /*show the logged in user name */
        if (accountUser != null) {
            userName.setText(String.format("%s %s", accountUser.getFirstName(),
                                accountUser.getLastName()));
        }
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(accountUser != null) {
                    userProfile.setText(accountUser.toString());
                }else {
                    Toast.makeText(cabinet.getContext(),"Profile Not Available",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setupToolbar()throws NullPointerException {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void setupDrawerToggle(){
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.app_name, R.string.app_name);
        //This is necessary to change the icon of the Drawer Toggle upon state change.
        drawerToggle.syncState();
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

    }

    private void selectItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new ScanFragment();
                break;
            case 1:
                fragment = new ImportFragment();
                break;
            case 2:
                fragment = new EncryptFragment();
                break;
            case 3:
                fragment = new DecryptFragment();
                break;
            case 4:
                fragment = new BrowseFragment();
                break;
            case 5:
                fragment = new EditFragment();
                break;
            case 6:
                fragment = new LogoutFragment();
                break;
            default:
                break;
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

            drawerList.setItemChecked(position, true);
            drawerList.setSelection(position);
            setTitle(navigationDrawerItemTitles[position]);
            drawerLayout.closeDrawer(drawerList);
        } else {
            Log.e("DFCHomeActivity", "Error in creating fragment");
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        this.title = title;
        getSupportActionBar().setTitle(this.title);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

//    Intent homeActivityIntent = new Intent(myContext, DocumentViewActivity.class);
//    Intent loginIntent = getIntent();
//        Deneme dene = (Deneme)i.getSerializableExtra("sampleObject");

}