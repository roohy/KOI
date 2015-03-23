package org.musk.koi.koi;

import android.content.Context;
import android.content.Intent;
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

    private Session.StatusCallback sessionCallback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        context = activity.getApplicationContext();
        uiHelper = new UiLifecycleHelper(activity, sessionCallback);
        uiHelper.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                          ViewGroup container,
                          Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.activity_main, container, false);
        LoginButton authButton = (LoginButton) view.findViewById(R.id.authButton);
        authButton.setFragment(this);
        authButton.setReadPermissions(Arrays.asList("user_friends","email"));
        Session activeSession = Session.getActiveSession();
        if(activeSession == null)
            activeSession = Session.openActiveSessionFromCache(getActivity());

    return view;
}

    @SuppressWarnings("deprecation")
    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            Log.i("kossher", "Logged in...");
        } else if (state.isClosed()) {
            Log.i("kossher", "Logged out...");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);

    }
}
