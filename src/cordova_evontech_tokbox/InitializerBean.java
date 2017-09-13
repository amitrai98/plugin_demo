package cordova_evontech_tokbox;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by amitrai on 7/9/17.
 * User for :-
 */

public class InitializerBean {

    @SerializedName("ApiKey")
    @Expose
    private String apiKey;
    @SerializedName("SessionId")
    @Expose
    private String sessionId;
    @SerializedName("Token")
    @Expose
    private String token;


    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
