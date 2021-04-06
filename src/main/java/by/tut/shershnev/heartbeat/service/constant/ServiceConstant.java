package by.tut.shershnev.heartbeat.service.constant;

public interface ServiceConstant {

    String BOT_USERNAME = "heart_beat_shersh_bot";
    String BOT_TOKEN = "1793885216:AAFgIS9wqW_BTC1G5dNJSszZWYK6-pfPCL0";
    String IP_PATTERN = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";
    String PATH_TO_SAVED_IP_ADDRESSES = "./ipAddresses.obj";
    int COLLECTING_DATA_COUNTER_LIMIT = 5;
    int REACHABILITY_CONDITION = 800;
}
