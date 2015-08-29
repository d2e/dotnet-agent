package com.desitek.utils;


import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MultivaluedMap;
import java.util.Arrays;

/**
 * Created by thakurd on 29/08/15.
 */
public class ETCDClient {
    private static final Logger log = LoggerFactory.getLogger(ETCDClient.class);

    private static Client client = Client.create();

    public static boolean isServiceAvailable(String url) {
        boolean status = false;
        WebResource webResource = client.resource(url);
        ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);
        if (response.getStatus() == 200) {
            status = true;
        }
        return status;
    }


    public static void broadcastService(String url, String ip, int port, int ttl) {


        MultivaluedMap queryParams = new MultivaluedMapImpl();
        //curl -L http://52.76.39.70:9192/v2/keys/service -XPUT -d value=52.74.209.116:80 -d ttl=15
        queryParams.put("ttl", Arrays.asList(new String[]{String.valueOf(ttl)}));


        WebResource webResource = client.resource(url+"/"+ip+":"+String.valueOf(port));
        String s = webResource.queryParams(queryParams).put(String.class);


        log.info(s);


    }


}
