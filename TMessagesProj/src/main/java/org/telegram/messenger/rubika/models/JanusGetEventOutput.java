package org.telegram.messenger.rubika.models;

import java.util.ArrayList;
import java.util.stream.Stream;

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
        public long private_id;
        public ArrayList<Publisher> publishers;
    }

    public static class Publisher {
        public long id;
        public ArrayList<StreamObject> streams;

    }

    public static class Jsep {
        public String type;
        public String sdp;
    }


    public static class StreamObject {
        public String type;
        public String mid;
        public long id;

    }
}
