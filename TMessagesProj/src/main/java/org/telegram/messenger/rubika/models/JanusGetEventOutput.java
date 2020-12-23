package org.telegram.messenger.rubika.models;

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
        String private_id;
    }

    public static class Jsep {
        public String type;
        public String sdp;
    }


}
