package cordova_evontech_tokbox;

import android.Manifest;
import android.app.Activity;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opentok.android.Connection;
import com.opentok.android.OpentokError;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static android.R.attr.data;

/**
 * Created by amitrai on 7/9/17.
 * User for :-
 */

public class ConnectionManager implements Session.SessionListener,
        PublisherKit.PublisherListener, Session.SignalListener{

    private static final int RC_VIDEO_APP_PERM = 101;
    private InitializerBean initializerBean = null;
    private Session mSession = null;
    private Activity activity;
    private OpenTokListener listener = null;
    public boolean isConnected = false;
    Gson gson= new GsonBuilder().create();
    private final String TAG = getClass().getSimpleName();

    public ConnectionManager(Activity activity, InitializerBean initializerBean, OpenTokListener listener){
        this.initializerBean = initializerBean;
        this.listener = listener;
        this.activity = activity;
    }

    public void initializeSession(){
        try {
            if (initializerBean.getApiKey() != null && !initializerBean.getApiKey().isEmpty() ){
                if (initializerBean.getSessionId() != null && !initializerBean.getSessionId().isEmpty() ){
                    if (initializerBean.getToken() != null && !initializerBean.getToken().isEmpty() ){
                        mSession = new Session(activity, initializerBean.getApiKey(), initializerBean.getSessionId());
                        mSession.setSessionListener(this);
                        mSession.connect(Appconstants.TOKEN);
                    }else
                        listener.onError(Appconstants.TOKEN_ERROR);

                    requestPermissions();
                }else
                    listener.onError(Appconstants.SESSION_ID_ERROR);
            }else
                listener.onError(Appconstants.API_KEY_ERROR);
        }catch (Exception exp){
            exp.printStackTrace();
            if (exp.getMessage() != null)
                listener.onError(Appconstants.UNKNOWN_ERROR+" "+exp.getMessage());
            else
                listener.onError(Appconstants.UNKNOWN_ERROR);
        }

    }

    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {

    }

    @Override
    public void onConnected(Session session) {
        isConnected = true;
        if (mSession != null)
            mSession.setSignalListener(this);

        session.sendSignal(Appconstants.MESSAGE_TYPE_CHAT, "this is my message");
    }

    @Override
    public void onDisconnected(Session session) {
        isConnected = false;
    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {


    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {
    }

    @Override
    public void onError(Session session, OpentokError opentokError) {

    }

    @Override
    public void onSignalReceived(Session session, String message_type, String message, Connection connection) {
        Log.e(TAG, "session is "+session+" message type is "+message_type +" message "+message +" connection is "+connection );

        boolean remote = !connection.equals(mSession.getConnection());
        if (message_type != null && message_type.equalsIgnoreCase(Appconstants.MESSAGE_TYPE_CHAT)) {
            Log.e(TAG, "data is "+data+" message is "+remote+" message type "+message_type+
                    " message  "+message);
        }

        if (remote && listener !=null){
            try {
                listener.onMessageReceived(""+message_type, ""+message);
            }catch (Exception exp){
                exp.printStackTrace();
                if (listener != null)
                    listener.onError(Appconstants.MESSAGE_ERROR);
            }
        }

        session.disconnect();

    }


    public void sendMessage(SignalMessage signalMessage) {
        try {
            if (signalMessage != null && signalMessage.getMessage() != null &&
                    !signalMessage.getMessage().isEmpty()){
                mSession.sendSignal(signalMessage.getMessage_type(), signalMessage.getMessage());
            }
        }catch (Exception exp){
            exp.printStackTrace();
        }
    }


    @AfterPermissionGranted(RC_VIDEO_APP_PERM)
    private void requestPermissions() {
        String[] perms = { Manifest.permission.INTERNET, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO };
        if (EasyPermissions.hasPermissions(activity, perms)) {
            // initialize view objects from your layout
//            mPublisherViewContainer = (FrameLayout) findViewById(R.id.publisher_container);
//            mSubscriberViewContainer = (FrameLayout) findViewById(R.id.subscriber_container);

            // initialize and connect to the session
//            mSession = new Session.Builder(this, API_KEY, SESSION_ID).build();
//            mSession.setSessionListener(this);
//            mSession.connect(TOKEN);

        } else {
            EasyPermissions.requestPermissions(activity, "This app needs access to your camera and mic to make video calls", RC_VIDEO_APP_PERM, perms);
        }
    }

}
