package by.tut.shershnev.server.service.model;

import java.util.ArrayList;
import java.util.List;

public class IPStatus {

    private static String deadHost;
    List<String> ipStatuses = new ArrayList<>();

    public void setIPStatus(String ipStatus){
        ipStatuses = getObj();
        int counter = 0;
        ipStatuses.add(ipStatus);
        for (String status : ipStatuses) {
            System.out.println(status);
            if (status.contains("false")){
                counter++;
                deadHost = ipStatus;
            }
        }
        System.out.println("Кол-во false " + counter);
        if (counter > 3) {
            System.out.println(deadHost + " отвалился");
        }
    }

    private static ArrayList<String> getObj(){
        return new ArrayList<>();
    }
}
