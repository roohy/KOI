package org.musk.koi.koi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.view.animation.OvershootInterpolator;
import android.widget.ArrayAdapter;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

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


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    private MainFragment mainFragment;
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    CardListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

/*
    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            Log.i("FACEBOOK", "Logged in...");
        } else if (state.isClosed()) {
            Log.i("FACEBOOK", "Logged out...");
        }
    }
*/


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
            //parse JSON data
            try {
//                Log.e("roohy","hahahahah");
                ArrayList<Card> cards = new ArrayList<Card>();
                JSONArray jArray = new JSONArray(result);
                for(int i=0; i < jArray.length(); i++) {


                    JSONObject jObject = jArray.getJSONObject(i);
                    String name = jObject.getString("Title");
                    String tab1_text = jObject.getString("Note");
//                    int active = jObject.getInt("active");

                    Card card = new Card(MainActivity.this);
                    // Create a CardHeader
                    CardHeader header = new CardHeader(MainActivity.this);
                    // Add Header to card
                    header.setTitle(name);
                    card.setTitle("sample title");
                    card.addCardHeader(header);
                    CardExpand expand = new CardExpand(MainActivity.this.getBaseContext());
//                    expand.setTitle("Rooooooohyyyyy joooonnnnnn");
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
//                            Log.e("harrrrrr","roohy swiped this or that ... whatever...");
                            MainActivity.this.overridePendingTransition(R.anim.abc_fade_in,R.anim.left2right);
                        }
                    });
            /*CardThumbnail thumb = new CardThumbnail(this);
            thumb.setDrawableResource(listImages[i]);
            card.addCardThumbnail(thumb);*/

                    cards.add(card);



//                    Toast.makeText(MainActivity.this,"title is "+name,Toast.LENGTH_LONG).show();

                } // End Loop


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
            } catch (JSONException e) {
                Log.e("JSONException", "Error: " + e.toString());
            } // catch (JSONException e)
        }
    }

}


