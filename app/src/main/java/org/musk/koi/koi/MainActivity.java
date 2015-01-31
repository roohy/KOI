package org.musk.koi.koi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;

import com.facebook.AppEventsLogger;
import com.facebook.Session;
import com.facebook.Session.NewPermissionsRequest;
import com.facebook.SessionDefaultAudience;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */



    private MainFragment mainFragment;
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private static final int PICK_FRIENDS_ACTIVITY = 1;
    private Button pickFriendsButton;
    private TextView resultsTextView;
    private UiLifecycleHelper lifecycleHelper;
    boolean pickFriendsWhenSessionOpened;
    String get_id, get_name, get_gender, get_email, get_birthday, get_locale, get_location;
    String s = new String("");
    Bundle savedInstanceState;

    private static final List<String> PERMISSIONS = new ArrayList<String>() {
        {
            add("user_friends");
            add("public_profile");
        }
    };

    //Majid




    private boolean sessionHasNecessaryPerms(Session session) {
        if (session != null && session.getPermissions() != null) {
            for (String requestedPerm : PERMISSIONS) {
                if (!session.getPermissions().contains(requestedPerm)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public void myButtonClick(View v){
        Session activeSession = Session.getActiveSession();
//        activeSession = Session.openActiveSessionFromCache(mainFragment.getActivity());


        TextView text1 = (TextView) findViewById(R.id.textView);

        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);


        Criteria criteria = new Criteria(); criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setCostAllowed(false);
        String providerName = lm.getBestProvider(criteria, true);
        //Location loc = lm.getLastKnownLocation(providerName);

        //Button b = (Button)v;

//        if(activeSession==null){
//            // try to restore from cache
//            activeSession = Session.openActiveSessionFromCache(mainFragment.getActivity());
//        }

        if(activeSession == null) {
            text1.setText("null");
            try {
                PackageInfo info = getPackageManager().getPackageInfo(
                        "org.musk.koi.koi",
                        PackageManager.GET_SIGNATURES);
                for (android.content.pm.Signature signature : info.signatures) {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }else {

//            Session.NewPermissionsRequest newPermissionsRequest = new Session
//                    .NewPermissionsRequest(this, Arrays.asList("user_checkins"));
//            activeSession.requestNewReadPermissions(newPermissionsRequest);


//            Request.newMyFriendsRequest(activeSession,
//                    new Request.GraphUserListCallback() {
//                        @Override
//                        public void onCompleted(List<GraphUser> users,
//                                                Response response) {
//                            if(users != null) {
//                                s = " haha ";
//                                for (GraphUser user : users) {
////                                    if (user != null)
//
//                                }
//                            }
//                            if(response != null){
////                                s = " haha ";
//                                s = response.toString();
//                            }
//                        }
//                    }).executeAsync();
            if(activeSession == null) {

                activeSession = null;
                Log.i("kossher", "active session is null");
            }else if (!activeSession.isOpened() && !activeSession.isClosed()) {
                activeSession.openForRead(new Session.OpenRequest(this).setPermissions(Arrays.asList("public_profile"))
                        .setCallback(mainFragment.statusCallback));;
                Log.i("kossher", "opened it!");
            } else {
                Session.openActiveSession(this, mainFragment, true, mainFragment.statusCallback);
                Log.i("kossher", "opened it 2");
            }

            new Request(
                    activeSession,
                    "/me",
                    null,
                    HttpMethod.GET,
                    new Request.Callback() {
                        public void onCompleted(Response response) {
                            /* handle the result */
                            Log.e("kossher", response.toString());
                        }
                    }
            ).executeAsync();


////            if(activeSession.getState().isOpened()) {
//                Request friendRequest = Request.newMyFriendsRequest(activeSession,
//                        new Request.GraphUserListCallback() {
//                            @Override
//                            public void onCompleted(List<GraphUser> users,
//                                                    Response response) {
//                                s = s + response.toString();
//
//                            }
//                        });
//                Bundle params = new Bundle();
//                params.putString("fields", "id, name, picture");
//                friendRequest.setParameters(params);
//                friendRequest.executeAsync();

//            }

            if(activeSession.getPermissions() == null)
                text1.setText("permissions null");
            else{
                for(String ss:activeSession.getPermissions())
                    Log.e("kossher", ss);
                Log.e("kossher", ((Boolean)activeSession.isOpened()).toString() + " " + ((Boolean)activeSession.isClosed()).toString());
                text1.setText("majid "+s);
            }
        }
    }
    //------------------------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        if (savedInstanceState == null) {
            // Add the fragment on initial activity setup
            mainFragment = new MainFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, mainFragment)
                    .commit();
        } else {
            // Or set the fragment from restored state info
            mainFragment = (MainFragment) getSupportFragmentManager()
                    .findFragmentById(android.R.id.content);
        }

        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));


//        authButton.setReadPermissions(Arrays.asList("public_profile"));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
