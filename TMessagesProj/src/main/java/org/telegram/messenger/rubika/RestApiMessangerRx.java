package org.telegram.messenger.rubika;


import org.telegram.messenger.rubika.models.JanusAttachInput;
import org.telegram.messenger.rubika.models.JanusAttachOutput;
import org.telegram.messenger.rubika.models.JanusCreateInput;
import org.telegram.messenger.rubika.models.JanusCreateOutput;
import org.telegram.messenger.rubika.models.JanusGetEventOutput;
import org.telegram.messenger.rubika.models.JanusJoinFeedInput;
import org.telegram.messenger.rubika.models.JanusJoinInput;
import org.telegram.messenger.rubika.models.JanusMessageOutput;
import org.telegram.messenger.rubika.models.JanusOfferSdpInput;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RestApiMessangerRx {
    @POST("./")
    Observable<JanusCreateOutput> janusCreate(@Body JanusCreateInput input);

    @POST("/janus/{sessionId}")
    Observable<JanusAttachOutput> janusAttach(@Path("sessionId") String sessionId, @Body JanusAttachInput input);

    @POST("/janus/{sessionId}/{handleId}")
    Observable<JanusMessageOutput> janusJoin(@Path("sessionId") String sessionId, @Path("handleId") String handleId, @Body JanusJoinInput input);

    @POST("/janus/{sessionId}/{handleId}")
    Observable<JanusMessageOutput> janusJoinFeed(@Path("sessionId") String sessionId, @Path("handleId") String handleId, @Body JanusJoinFeedInput input);

    @POST("/janus/{sessionId}/{handleId}")
    Observable<JanusMessageOutput> janusSdpOffer(@Path("sessionId") String sessionId, @Path("handleId") String handleId, @Body JanusOfferSdpInput input);

    @GET("/janus/{sessionId}/{handleId}")
    Observable<JanusGetEventOutput> getEvent(@Path("sessionId") String sessionId, @Path("handleId") String handleId);

}