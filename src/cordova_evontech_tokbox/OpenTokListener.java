package cordova_evontech_tokbox;

/**
 * Created by amitrai on 7/9/17.
 * User for :- conveing messages to the opentok callbacks
 */

public interface OpenTokListener {
    void onError(String error_message);
    void onSuccess(String message);
    void onMessageReceived(String message_type, String message);
}
