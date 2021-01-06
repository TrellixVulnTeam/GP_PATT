package org.telegram.messenger.rubika.models;

import android.accessibilityservice.GestureDescription;
import android.os.Build;

import org.telegram.messenger.rubika.ApiRequestMessangerRx;

public class JanusOfferSdpInput {
    public String janus = "message";
    public String transaction = ApiRequestMessangerRx.getTempSession();
    public Data body = new Data();
    public Jsep jsep = new Jsep();

    public static class Data {

        public String request = "start";
    }

    public static class Jsep {
        public String type = "answer";
        public String sdp;
    }
}

