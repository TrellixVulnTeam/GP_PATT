package org.telegram.messenger.rubika.models;

import org.telegram.messenger.rubika.ApiRequestMessangerRx;

public class JanusSendCondidateInput {
    public String janus = "trickle";
    public String transaction = ApiRequestMessangerRx.getTempSession();
    public Data candidate = new Data();


    public static class Data {
        public String sdpMid = "0";
        public int sdpMLineIndex = 0;
        public String candidate;
    }


}

