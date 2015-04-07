package org.musk.koi.koi;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;


import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardExpand;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.view.CardListView;
import it.sephiroth.android.library.floatingmenu.FloatingActionItem;
import it.sephiroth.android.library.floatingmenu.FloatingActionMenu;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.facebook.Session;
import com.facebook.SessionState;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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

import android.support.v4.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;



public class MainActivity extends ActionBarActivity
        implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener, NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private GoogleApiClient mGoogleApiClient;
    private boolean mResolvingError = false;
    private PlaceAutocompleteAdapter mAdapter;
    private AutoCompleteTextView mAutocompleteView;
    private TextView mPlaceDetailsText;




    private MainFragment mainFragment;
    private Session activeSession = null;
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    CardListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //String accessToken = "CAAEoqDa2RKABAJBlI5HCNzZAlsci0NAEAe0gmksLbyro076j1nZBPxTjZBjia1Qp0JjNdyzs13sxm5VWth9B6nF6u0pESo32RbsSb2TNgmWiilo29azWsJZAZCyX1KjMDQVaaZCXLqtLF1IaLXPM0fGhPtDTiTA377MI4vsGxT2g0ZAaT08GpbUNOkjIc75jE7ImUqJDMjTwQrTfMJmzC9r";
        //Session.restoreSession(null,)
        //----------- majid
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
        try {
            LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
                    new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));
            mAdapter = new PlaceAutocompleteAdapter(this, android.R.layout.simple_list_item_1,
                    BOUNDS_GREATER_SYDNEY, null);

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Places.GEO_DATA_API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
            mGoogleApiClient.connect();

            mAutocompleteView = (AutoCompleteTextView)
                    findViewById(R.id.autocompplaces);
            mPlaceDetailsText = (TextView) findViewById(R.id.placedetails);
            mAutocompleteView.setAdapter(mAdapter);
            mAutocompleteView.setOnItemClickListener(mAutocompleteClickListener);


        }catch(Exception e){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(baos);
            e.printStackTrace(ps);
            String content = baos.toString();
            Log.d("kossher map",content);
        }
        //---------------------------

        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));


        CardListView container= (CardListView) findViewById(R.id.myList);

//
//        ArrayList<Card> cards = new ArrayList<Card>();

        new MyAsyncTask().execute("");

/*
        for (int i = 1; i<20; i++) {
            // Create a Card
            Card card = new Card(this);
            // Create a CardHeader
            CardHeader header = new CardHeader(this);
            // Add Header to card
            header.setTitle("Angry bird: " + i);
            card.setTitle("sample title");
            card.addCardHeader(header);
            CardExpand expand = new CardExpand(this.getBaseContext());
            expand.setTitle("Rooooooohyyyyy joooonnnnnn");
            card.addCardExpand(expand);
            card.setOnClickListener(new Card.OnCardClickListener() {
                @Override
                public void onClick(Card card, View view) {
                    card.doExpand();
//                    card.setBackgroundColorResourceId(R.color.lightBlue);
//                    view.setBackgroundResource(R.color.lightBlue);
                }
            });
            card.setOnSwipeListener(new Card.OnSwipeListener() {
                @Override
                public void onSwipe(Card card) {
                    Log.e("harrrrrr","roohy swiped this or that ... whatever...");
                    MainActivity.this.overridePendingTransition(R.anim.abc_fade_in,R.anim.left2right);
                }
            });
            card.setOnExpandAnimatorStartListener(new Card.OnExpandAnimatorStartListener() {
                @Override
                public void onExpandStart(Card card) {
                    Log.e("fapfappp","fafffffffffff");
                }
            });
            /*CardThumbnail thumb = new CardThumbnail(this);
           // thumb.setDrawableResource(listImages[i]);
            //card.addCardThumbnail(thumb);

            cards.add(card);
        }*/
    }

    public void newItemActivity(){
        Intent intent = new Intent(this,createReminder.class);
        startActivity(intent);
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

    @Override
    public void onConnected(Bundle bundle) {
        mAdapter.setGoogleApiClient(mGoogleApiClient);
        Log.d("kossher map", "GoogleApiClient connected.");
    }

    @Override
    public void onConnectionSuspended(int i) {
        mAdapter.setGoogleApiClient(null);
        Log.d("kossher map", "GoogleApiClient connection suspended.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e("kossher map", "onConnectionFailed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());

        // TODO(Developer): Check error code and notify the user of error state and resolution.
        Toast.makeText(this,
                "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();

        // Disable API access in the adapter because the client was not initialised correctly.
        mAdapter.setGoogleApiClient(null);
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

    /**
     * Listener that handles selections from suggestions from the AutoCompleteTextView that
     * displays Place suggestions.
     * Gets the place id of the selected item and issues a request to the Places Geo Data API
     * to retrieve more details about the place.
     *
     * @see com.google.android.gms.location.places.GeoDataApi#getPlaceById(com.google.android.gms.common.api.GoogleApiClient,
     * String...)
     */
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a PlaceAutocomplete object from which we
             read the place ID.
              */
            final PlaceAutocompleteAdapter.PlaceAutocomplete item = mAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i("kossher map", "Autocomplete item selected: " + item.description);

            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
              details about the place.
              */
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);

            Toast.makeText(getApplicationContext(), "Clicked: " + item.description,
                    Toast.LENGTH_SHORT).show();
            Log.i("kossher map", "Called getPlaceById to get Place details for " + item.placeId);
        }
    };

    /**
     * Callback for results from a Places Geo Data API query that shows the first place result in
     * the details view on screen.
     */
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                Log.e("kossher map", "Place query did not complete. Error: " + places.getStatus().toString());

                return;
            }
            // Get the Place object from the buffer.
            final Place place = places.get(0);

            // Format details of the place for display and show it in a TextView.
            mPlaceDetailsText.setText(formatPlaceDetails(getResources(), place.getName(),
                    place.getId(), place.getAddress(), place.getPhoneNumber(),
                    place.getWebsiteUri()) + "\nlat: "+ place.getLatLng().latitude + "\nlong: "+place.getLatLng().longitude);


            Log.i("kossher map", "Place details received: " + place.getName());
        }
    };

    private static Spanned formatPlaceDetails(Resources res, CharSequence name, String id,
                                              CharSequence address, CharSequence phoneNumber, Uri websiteUri) {
        Log.e("kossher map", res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));
        return Html.fromHtml(res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));

    }

    public void myButtonClick(View v){
        Log.i("kossher", "button clicked!!");
        try {
//            (SupportMapFragment)findViewById(R.id.mapFragment);

//            SupportMapFragment ff = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment));
//            if(ff == null)
//                Log.d("kossher map","ff NULLL!!!");
//            GoogleMap myMap = ff.getMap();
//            if(myMap == null)
//                Log.d("kossher map","myMap NULLL!!!");
//            myMap.setMyLocationEnabled(true);

            ////////////////////////////////////////////////////////////////////////
            mAutocompleteView = (AutoCompleteTextView)
                    findViewById(R.id.autocompplaces);
            mAutocompleteView.setAdapter(mAdapter);

            mPlaceDetailsText = (TextView) findViewById(R.id.placedetails);
            mAutocompleteView.setOnItemClickListener(mAutocompleteClickListener);
            mAutocompleteView.callOnClick();


            /////////////////////////////////////////////////////////////////////////


            Log.d("kossher map","We passed the test!!!");

        }catch(Exception e){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(baos);
            e.printStackTrace(ps);
            String content = baos.toString();
            Log.d("kossher map",content);
        }
//        Log.d("kossher map",mMap.getMyLocation().toString());
//        map.getMapAsync(callback);
        activeSession = Session.getActiveSession();
//        activeSession = Session.openActiveSessionFromCache(mainFragment.getActivity());


        //--------------------- Location -------------------------
//        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);


//        Criteria criteria = new Criteria();
//        criteria.setAccuracy(Criteria.ACCURACY_MEDIUM);
//        criteria.setCostAllowed(true);
//        String providerName = lm.getBestProvider(criteria, false);
//        Log.d("kossher", providerName);
        //Location loc = lm.getLastKnownLocation(providerName);
        //-----------------------------------------------------------------
        if(activeSession == null) {
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
            Request friendRequest = Request.newMyFriendsRequest(activeSession,
                    new Request.GraphUserListCallback(){
                        @Override
                        public void onCompleted(List<GraphUser> users,
                                                Response response) {
                            Log.i("kossher", users.size()+" " + response.toString());
                            for(GraphUser gu:users){
                                Log.i("kossher", gu.getId() + " " + gu.getName());  //show friends' info
                            }

                        }
                    }
            );
            Request.executeMeRequestAsync(activeSession,
                    new Request.GraphUserCallback(){
                        @Override
                        public void onCompleted(GraphUser user, Response response) {
                            String responseString = response.toString();
                            int i = responseString.indexOf("email");
                            responseString = responseString.substring(i);
                            i = responseString.indexOf(":");
                            responseString = responseString.substring(i+2);
                            i = responseString.indexOf("\"");
                            responseString = responseString.substring(0,i);

                            Log.i("kossher",responseString); // gives user information. user.getId()
                        }
                    }
            );
            Log.i("kossher access token",activeSession.getAccessToken());
            friendRequest.executeAsync();
        }
    }


    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (session == activeSession){
            if (state.isOpened()) {
                Log.i("FACEBOOK", "Logged in...");
            } else if (state.isClosed()) {
                Log.i("FACEBOOK", "Logged out...");
            }
        }
    }



    class MyAsyncTask extends AsyncTask<String, String, Void> {

        private ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        InputStream inputStream = null;
        String result = "";

        protected void onPreExecute() {
            progressDialog.setMessage("Downloading your data...");
            progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface arg0) {
                    MyAsyncTask.this.cancel(true);
                }
            });

        }


        @Override
        protected Void doInBackground(String... params) {

            String url_select = "http://koi.cloudapp.net/api/Authentication/GetReminders?userid=1";

            ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

            try {
                // Set up HTTP post

                // HttpClient is more then less deprecated. Need to change to URLConnection
                HttpClient httpClient = new DefaultHttpClient();

                HttpGet httpGet = new HttpGet(url_select);
//                httpGet.setEntity(new UrlEncodedFormEntity(param));
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();

                // Read content & Log
                inputStream = httpEntity.getContent();
            } catch (UnsupportedEncodingException e1) {
                Log.e("UnsupportedEncodingException", e1.toString());
                e1.printStackTrace();
            } catch (ClientProtocolException e2) {
                Log.e("ClientProtocolException", e2.toString());
                e2.printStackTrace();
            } catch (IllegalStateException e3) {
                Log.e("IllegalStateException", e3.toString());
                e3.printStackTrace();
            } catch (IOException e4) {
                Log.e("IOException", e4.toString());
                e4.printStackTrace();
            }
            // Convert response to string using String Builder
            try {
                BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"), 8);
                StringBuilder sBuilder = new StringBuilder();

                String line = null;
                while ((line = bReader.readLine()) != null) {
                    sBuilder.append(line + "\n");
                }

                inputStream.close();
                result = sBuilder.toString();

            } catch (Exception e) {
                Log.e("StringBuilding & BufferedReader", "Error converting result " + e.toString());
            }
            return null;
        }


        protected void onPostExecute(Void v) {
            ArrayList<Card> cards = new ArrayList<Card>();
            //parse JSON data
//            try {
////                Log.e("roohy","hahahahah");
//                ArrayList<Card> cards = new ArrayList<Card>();
//                JSONArray jArray = new JSONArray(result);
//                for(int i=0; i < jArray.length(); i++) {
//
//
//                    JSONObject jObject = jArray.getJSONObject(i);
//                    String name = jObject.getString("Title");
//                    String note = jObject.getString("Note");
////                    int active = jObject.getInt("active");
//
//                    Card card = new Card(MainActivity.this);
//                    // Create a CardHeader
//                    CardHeader header = new CardHeader(MainActivity.this);
//                    // Add Header to card
//                    header.setTitle(name);
//                    card.setTitle(note);
//                    card.addCardHeader(header);
//                    CardExpand expand = new CardExpand(MainActivity.this.getBaseContext());
////                    expand.setTitle("Rooooooohyyyyy joooonnnnnn");
//                    card.addCardExpand(expand);
//                    card.setOnClickListener(new Card.OnCardClickListener() {
//                        @Override
//                        public void onClick(Card card, View view) {
//                            if(card.isExpanded()){
//                                card.doToogleExpand();
//                            }
//                            card.doExpand();
////                    card.setBackgroundColorResourceId(R.color.lightBlue);
////                    view.setBackgroundResource(R.color.lightBlue);
//                        }
//                    });
//
//                    card.setOnSwipeListener(new Card.OnSwipeListener() {
//                        @Override
//                        public void onSwipe(Card card) {
////                            Log.e("harrrrrr","roohy swiped this or that ... whatever...");
//                            MainActivity.this.overridePendingTransition(R.anim.abc_fade_in,R.anim.left2right);
//                        }
//                    });
//            /*CardThumbnail thumb = new CardThumbnail(this);
//            thumb.setDrawableResource(listImages[i]);
//            card.addCardThumbnail(thumb);*/
//
//                    cards.add(card);
//
//
//
////                    Toast.makeText(MainActivity.this,"title is "+name,Toast.LENGTH_LONG).show();
//
//                } // End Loop


            CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(MainActivity.this, cards);


            listView = (CardListView) MainActivity.this.findViewById(R.id.myList);
            if (listView != null) {
                listView.setAdapter(mCardArrayAdapter);
            }




            FloatingActionItem item1 = new FloatingActionItem.Builder(0)
                    .withResId(R.drawable.ic_action_add)
                    .withDelay(0)
                            //.withPadding(action_item_padding)
                    .build();


            FloatingActionMenu mFloatingMenu = new FloatingActionMenu
                    .Builder(MainActivity.this)
                    .addItem(item1)
                    .withScrollDelegate(new FloatingActionMenu.AbsListViewScrollDelegate(listView))
                            //.withThreshold(R.dimen.float_action_threshold)
                            //.withGap(R.dimen.float_action_item_gap)
                            //.withHorizontalPadding(R.dimen.float_action_h_padding)
                            //.withVerticalPadding(R.dimen.float_action_v_padding)
                    .withGravity(FloatingActionMenu.Gravity.RIGHT | FloatingActionMenu.Gravity.BOTTOM)
                    .withDirection(FloatingActionMenu.Direction.Vertical)
                    .animationDuration(300)
                    .animationInterpolator(new OvershootInterpolator())
                    .visible(true)
                    .build();



            mFloatingMenu.setOnItemClickListener(new FloatingActionMenu.OnItemClickListener() {
                @Override
                public void onItemClick(FloatingActionMenu floatingActionMenu, int i) {
//                Toast.makeText(MainActivity.this,"ahahahahaha",Toast.LENGTH_LONG).show();
                    MainActivity.this.newItemActivity();
                }
            });






            this.progressDialog.dismiss();
//            } catch (JSONException e) {
//                Log.e("JSONException", "Error: " + e.toString());
//            } // catch (JSONException e)
        }
    }

}


