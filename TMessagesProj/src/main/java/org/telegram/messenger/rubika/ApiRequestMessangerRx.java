package org.telegram.messenger.rubika;


import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import org.telegram.messenger.rubika.models.JanusAttachInput;
import org.telegram.messenger.rubika.models.JanusAttachOutput;
import org.telegram.messenger.rubika.models.JanusCreateInput;
import org.telegram.messenger.rubika.models.JanusCreateOutput;
import org.telegram.messenger.rubika.models.JanusGetEventOutput;
import org.telegram.messenger.rubika.models.JanusJoinFeedInput;
import org.telegram.messenger.rubika.models.JanusJoinInput;
import org.telegram.messenger.rubika.models.JanusMessageOutput;
import org.telegram.messenger.rubika.models.JanusOfferSdpInput;

import java.nio.charset.Charset;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiRequestMessangerRx {
    static Retrofit retrofit = null;
    static RxHelper rxHelper = new RxHelper();
    public static int counter = 0;
    static final Charset UTF8 = Charset.forName("UTF-8");
    volatile static ApiRequestMessangerRx instance;

    public String auth = "";
    static RestApiMessangerRx restApiService;
    long debugDelay = 3;
    private long lastTimeIncreased = 0;

    public ApiRequestMessangerRx() {
        setRestApiService();
    }

    private RestApiMessangerRx getRestApiService() {
        return restApiService;
    }

    private static OkHttpClient createOkHttpClient() {
        try {
            HttpLoggingMessanger logging = new HttpLoggingMessanger();
            // set your desired log level
            if (MyLog.isDebugAble) {
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            } else {
                logging.setLevel(HttpLoggingInterceptor.Level.NONE);
            }
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            return new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .sslSocketFactory(sslContext.getSocketFactory())
                    .hostnameVerifier((hostname, session) -> true)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setRestApiService() {

        HttpLoggingMessanger logging = new HttpLoggingMessanger();
        // set your desired log level
        if (MyLog.isDebugAble) {
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            logging.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
        OkHttpClient httpClient = createOkHttpClient();

        OkHttpClient httpClient2 = new OkHttpClient.Builder()

                .addInterceptor(logging)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(25, TimeUnit.SECONDS)
                .writeTimeout(25, TimeUnit.SECONDS)
                .build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://192.168.88.16:8089/janus/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();
        restApiService = retrofit.create(RestApiMessangerRx.class);
    }


    static public ApiRequestMessangerRx getInstance() {

        if (instance == null) {
            instance = new ApiRequestMessangerRx();
        }


        return instance;

    }

    public Observable<JanusCreateOutput> createJanus(JanusCreateInput input) {


        return restApiService.janusCreate(input)
                .compose(rxHelper.applySchedulers())
                .compose(rxHelper.addRetryAndDelay(1, 2, 3));

    }

    public Observable<JanusAttachOutput> attachJanus(String sessionId, JanusAttachInput input) {
        return restApiService.janusAttach(sessionId, input)
                .compose(rxHelper.applySchedulers())
                .compose(rxHelper.addRetryAndDelay(1, 2, 3));

    }

    public Observable<JanusMessageOutput> sendMessageJoin(String sessionId, String handleId, JanusJoinInput input) {
        return restApiService.janusJoin(sessionId, handleId, input)
                .compose(rxHelper.applySchedulers())
                .compose(rxHelper.addRetryAndDelay(1, 2, 3));

    }

    public Observable<JanusMessageOutput> joinFeed(String sessionId, String handleId, JanusJoinFeedInput input) {
        return restApiService.janusJoinFeed(sessionId, handleId, input)
                .compose(rxHelper.applySchedulers())
                .compose(rxHelper.addRetryAndDelay(1, 2, 3));

    }

    public Observable<JanusMessageOutput> sendSetOffer(String sessionId, String handleId, JanusOfferSdpInput input) {
        return restApiService.janusSdpOffer(sessionId, handleId, input)
                .compose(rxHelper.applySchedulers())
                .compose(rxHelper.addRetryAndDelay(1, 2, 3));

    }

    public Observable<JanusGetEventOutput> sendGetEvent(String sessionId, String handleId) {
        return restApiService.getEvent(sessionId, handleId)
                .compose(rxHelper.applySchedulers())
                .compose(rxHelper.addRetryAndDelay(1, 2, 3));

    }


    public static String getTempSession() {

        String SALTCHARS = "abcdefghigklmnopqrstuvwxyz1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 32) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        return salt.toString();


    }

}
