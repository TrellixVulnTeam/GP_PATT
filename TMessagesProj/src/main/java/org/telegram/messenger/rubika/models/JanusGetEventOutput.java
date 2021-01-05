package org.telegram.messenger.rubika.models;

import java.util.ArrayList;

public class JanusGetEventOutput {
    public String janus;
    public String transaction;
    public String sender;
    public PluginData plugindata;
    public Jsep jsep;

    public static class PluginData {
        public String plugin;
        public Data data;
    }

    public static class Data {
        public String private_id;
        public ArrayList<Publisher> publishers;
    }

    public static class Publisher {
        public String id;

    }

    public static class Jsep {
        public String type;
        public String sdp;
    }


}
