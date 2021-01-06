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
import org.telegram.messenger.rubika.models.JanusSendCondidateInput;
import org.telegram.messenger.voip.Instance;
import org.telegram.messenger.voip.VoIPService;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

import static org.telegram.messenger.voip.VoIPBaseService.STATE_WAIT_INIT;
import static org.telegram.messenger.voip.VoIPBaseService.STATE_WAIT_INIT_ACK;

public class JanusHelper {
    static public String sessionId = "";
    static public String handleId = "";
    private static long privateId = 0;
    private static String handleIdSubscribe = "";
    static ArrayList<JanusJoinFeedInput.StreamObject> streamsPublishes = new ArrayList<>();

    public static void create() {
        ApiRequestMessangerRx.getInstance().createJanus(new JanusCreateInput()).subscribe(new DisposableObserver<JanusCreateOutput>() {
            @Override
            public void onNext(JanusCreateOutput value) {
                MyLog.e("janus", "session " + value.data.id);
                sessionId = value.data.id;
                attach();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {

            }
        });

    }

    public static void attach() {
        ApiRequestMessangerRx.getInstance().attachJanus(sessionId, new JanusAttachInput()).subscribe(new DisposableObserver<JanusAttachOutput>() {
            @Override
            public void onNext(@NonNull JanusAttachOutput janusAttachOutput) {
                MyLog.e("janus", "handleId " + janusAttachOutput.data.id);
                handleId = janusAttachOutput.data.id;
                joinRoom();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    public static void attachSubscribe() {
        ApiRequestMessangerRx.getInstance().attachJanus(sessionId, new JanusAttachInput()).subscribe(new DisposableObserver<JanusAttachOutput>() {
            @Override
            public void onNext(@NonNull JanusAttachOutput janusAttachOutput) {
                MyLog.e("janus", "handleId " + janusAttachOutput.data.id);
                handleIdSubscribe = janusAttachOutput.data.id;

            }

            @Override
            public void onError(@NonNull Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    public static void joinRoom() {
        ApiRequestMessangerRx.getInstance().sendMessageJoin(sessionId, handleId, new JanusJoinInput()).subscribe(new DisposableObserver<JanusMessageOutput>() {
            @Override
            public void onNext(@NonNull JanusMessageOutput output) {
                startGetEvents();


            }

            @Override
            public void onError(@NonNull Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    static public void joinFeed() {
        JanusJoinFeedInput input = new JanusJoinFeedInput();
        input.body.private_id = privateId;
        input.body.streams = streamsPublishes;
        DisposableObserver<JanusMessageOutput> a = ApiRequestMessangerRx.getInstance().joinFeed(sessionId, handleIdSubscribe, input).subscribeWith(new DisposableObserver<JanusMessageOutput>() {
            @Override
            public void onNext(@NonNull JanusMessageOutput output) {


            }

            @Override
            public void onError(@NonNull Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    public static void sendOffer(String sdp) {
        JanusOfferSdpInput str = new JanusOfferSdpInput();
        str.jsep.sdp = sdp;
        ApiRequestMessangerRx.getInstance().sendSetOffer(sessionId, handleIdSubscribe, str).subscribeWith(new DisposableObserver<JanusMessageOutput>() {
            @Override
            public void onNext(@NonNull JanusMessageOutput janusMessageOutput) {

            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private static void startGetEvents() {
        DisposableObserver<JanusGetEventOutput> a = ApiRequestMessangerRx.getInstance().sendGetEvent(sessionId, handleId).delay(1, TimeUnit.SECONDS).repeat(200).observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableObserver<JanusGetEventOutput>() {
            @Override
            public void onNext(@NonNull JanusGetEventOutput janusGetEventOutput) {
                if (janusGetEventOutput.plugindata != null) {
                    if (janusGetEventOutput.plugindata.data.private_id != 0) {
                        privateId = janusGetEventOutput.plugindata.data.private_id;
                    }
                    if (janusGetEventOutput.plugindata.data.publishers != null) {
                        streamsPublishes = new ArrayList<>();
                        for (JanusGetEventOutput.Publisher publisher : janusGetEventOutput.plugindata.data.publishers) {
                            for (JanusGetEventOutput.StreamObject s : publisher.streams) {
                                if (s.type.equals("audio")) {
                                    JanusJoinFeedInput.StreamObject s2 = new JanusJoinFeedInput.StreamObject();
                                    s2.feed = publisher.id;
                                    s2.mid = s.mid;
                                    streamsPublishes.add(s2);
                                }
                            }

                        }
                        if (handleIdSubscribe.isEmpty()) {
                            attachSubscribe();
                        }
                    }
                }
                if (janusGetEventOutput.jsep != null && janusGetEventOutput.jsep.sdp != null && janusGetEventOutput.jsep.type.equals("offer")) {
                    parseSdpToJson(janusGetEventOutput.jsep.sdp);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    static public boolean isOneTimeUsed = false;

    private static void parseSdpToJson(String sdp) {

        isOneTimeUsed = true;
        String a = sdp;
        String[] lines = a.split("\r\n");
        String ufrag = "";
        String pwd = "";
        String fingerPrint;
        String fingerPrintHash;
        String fingerPrintSetup = "active";
        String foundation;
        String component;
        String protocol;
        String priority;
        String ip;
        String port;
        String type;
        int generation = 0;

        Instance.Fingerprint[] fingerprints = new Instance.Fingerprint[1];
        Instance.Candidate[] candidates = new Instance.Candidate[1];


        for (String line : lines) {
            if (line.indexOf("ice-ufrag") > 0) {
                ufrag = line.substring(line.indexOf(":") + 1);

            }
            if (line.indexOf("ice-pwd") > 0) {
                pwd = line.substring(line.indexOf(":") + 1);

            }
            if (line.indexOf("fingerprint") > 0) {
                String[] s = line.substring(line.indexOf(":") + 1).split(" ");
                fingerPrint = s[1];
                fingerPrintHash = s[0];

                fingerprints[0] = new Instance.Fingerprint(
                        fingerPrintHash,
                        fingerPrintSetup,
                        fingerPrint
                );
            }
            if (line.indexOf("candidate:") > 0) {
                String l = line.substring(line.indexOf(":") + 1);
                System.out.println(l);
                String[] s = l.split(" ");
                foundation = s[0];
                component = s[1];
                protocol = s[2];
                priority = s[3];
                ip = s[4];
                port = s[5];
                type = s[7];
                candidates[0] = new Instance.Candidate(
                        port,
                        protocol,
                        "",//item.optString("network", ""),
                        "0",//item.optString("generation", ""),
                        "",//item.optString("id", ""),
                        component,//item.optString("component", ""),
                        foundation,//item.optString("foundation", ""),
                        priority,//item.optString("priority", ""),
                        ip,//item.optString("ip", ""),
                        type,// item.optString("type", ""),
                        "passive", //item.optString("tcpType", ""),
                        "", //item.optString("relAddr", ""),
                        ""//item.optString("relPort", "")
                );

            }
        }


        VoIPService.getSharedInstance().tgVoip.setJoinResponsePayload(ufrag, pwd, fingerprints, candidates, sdp);
        if (VoIPService.getSharedInstance().currentState == STATE_WAIT_INIT) {
            VoIPService.getSharedInstance().dispatchStateChanged(STATE_WAIT_INIT_ACK);
        }
    }

    public static void sendCondidate(String condidate) {

        JanusSendCondidateInput input = new JanusSendCondidateInput();
        input.candidate = new JanusSendCondidateInput.Data();
        input.candidate.candidate = condidate;
        ApiRequestMessangerRx.getInstance().sendCandidate(sessionId, handleIdSubscribe, input).subscribeWith(new DisposableObserver<JanusMessageOutput>() {
            @Override
            public void onNext(@NonNull JanusMessageOutput janusMessageOutput) {

            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
}
