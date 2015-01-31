package org.musk.koi.koi;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;

import java.util.Arrays;

/**
 * Created by majid on 1/31/2015.
 */
public class MainFragment extends Fragment {

    private MainActivity activity;
    private Context context;
    private UiLifecycleHelper uiHelper;

    private static final String TAG = "MainFragment";
    private class SessionStatusCallback implements Session.StatusCallback {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            // Respond to session state changes, ex: updating the view
            onSessionStateChange(session, state, exception);
        }
    }

    public SessionStatusCallback statusCallback = new SessionStatusCallback();




    @Override
    public View onCreateView(LayoutInflater inflater,
                          ViewGroup container,
                          Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.activity_main, container, false);
        LoginButton authButton = (LoginButton) view.findViewById(R.id.authButton);
        authButton.setFragment(this);
        authButton.setReadPermissions(Arrays.asList("public_profile", "user_likes", "user_status"));
        Session activeSession = Session.getActiveSession();
        if(activeSession == null)
            activeSession = Session.openActiveSessionFromCache(getActivity());

    return view;
}


    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            Log.i("kossher", "Logged in...");
        } else if (state.isClosed()) {
            Log.i("kossher", "Logged out...");
        }
    }
}
