package org.telegram.messenger.rubika.models;

import org.telegram.messenger.rubika.ApiRequestMessangerRx;

public class JanusCreateOutput {
    public String janus;
    public String transaction;
    public Data data;
    public static class Data {
        public String id;
    }
}
