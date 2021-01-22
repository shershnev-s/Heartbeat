package by.tut.shershnev.server.service.impl;

import by.tut.shershnev.server.service.model.IPStatus;

import java.util.ArrayList;
import java.util.List;

public class Test {

    //static List<String> list = new ArrayList<>();

    static List<String> list1Ip = new ArrayList<>();
    static List<String> list2Ip = new ArrayList<>();
    static IPStatus ipStatus = new IPStatus();

    private String deadHost;

    public void addToPing(String reachable, List<String> ipStatuses) {
        int counter = 0;

        //ipStatus.setIPStatus(reachable);


        if (reachable.contains("10.10.10.5")) {
            if (list1Ip.size() > 6) {
                list1Ip.remove(0);
            }
            list1Ip.add(reachable);
            for (String s : list1Ip) {
                System.out.println(s);
                if (s.contains("false")) {
                    counter++;
                    deadHost = s;
                }
            }
        } else {
            list2Ip.add(reachable);
            for (String s : list2Ip) {
                System.out.println(s);
                if (s.contains("false")) {
                    counter++;
                    deadHost = s;
                }
            }
        }
        System.out.println("Кол-во false " + counter);
        if (counter > 3) {
            System.out.println(deadHost + " отвалился");
        }

        }
    }

