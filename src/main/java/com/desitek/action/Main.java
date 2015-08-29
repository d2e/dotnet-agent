package com.desitek.action;


import com.desitek.utils.ETCDClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by thakurd on 29/08/15.
 */
public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void registerService() {
        try {
            InetAddress ip = InetAddress.getLocalHost();
            ETCDClient.broadcastService("http://52.76.39.70:9192/v2/keys/services", ip.getHostAddress(), 80, 30);
            log.info(ip.getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }


    }

    public static boolean healthCheck() {

        boolean status = ETCDClient.isServiceAvailable("http://52.74.209.116/testwebservice/Service1.asmx");
        log.info("is service available " + status);
        return status;
    }


    public static void main(String[] arr) {
// {"action":"get","node":{"key":"/service","value":"52.74.209.116:80","modifiedIndex":9,"createdIndex":9}}
        //curl -L http://52.76.39.70:9192/v2/keys/service -XPUT -d value=52.74.209.116:80 -d ttl=15
        log.info("starting dotnet agent");

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Runnable() {
            public void run() {

                boolean isActive = healthCheck();

                if (isActive) {

                    registerService();
                    log.info("service Registered");
                } else {
                    log.info("service is unavailable");
                }
            }
        }, 0, 30, TimeUnit.SECONDS);


    }
}
