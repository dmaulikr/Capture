package com.cis350.argame;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Created by Anton on 3/15/14.
 */
public class XMLQueryHandler {

    // private HttpPost httppost;
    // private HttpClient httpclient;

    public XMLQueryHandler() {
    }

    public ArrayList<String> build_ids = new ArrayList<String>();

    public String getXMLDataFromBBox(String[] bounds,HttpClient httpclient, HttpPost httppost) {
        String output = "";

        try {

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

            // Find all ways with key "building" and all their member nodes
            String data=
                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?><osm-script timeout=\"900\" element-limit=\"1073741824\">"+
                            "<query type=\"node\">"+
                            "<bbox-query s=\""+bounds[1]+"\" w=\""+bounds[0]+"\" n=\""+bounds[3]+"\" e=\""+bounds[2]+"\"/>"+
                            "</query>"+
                            "<union>"+
                            "<item />"+
                            "<recurse type=\"node-way\"/>"+
                            "</union>"+
                            "<print/></osm-script>";

            nameValuePairs.add(new BasicNameValuePair("form-data", data));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);

            HttpEntity entity = response.getEntity();
            // get the result from query in XML format and convert to string
            if (entity != null) {
                InputStream instream = entity.getContent();

                InputStreamReader is = new InputStreamReader(instream);
                StringBuilder sb=new StringBuilder();
                BufferedReader br = new BufferedReader(is);
                String read = br.readLine();

                while(read != null) {
                    //System.out.println(read);
                    sb.append(read);
                    read =br.readLine();

                }
                output = sb.toString(); // XML
            }

        }  catch (ClientProtocolException e) {
            Log.w("xml", "wrong client");
        } catch (IOException e) {
            Log.w("xml", "IO exception");
        }

        // Create a DOM element to parse XML
        return output;
    }

    public ArrayList<String> getBuildIds() {
        return this.build_ids;
    }

    /**
     * returns polygons
     *
     * @param doc - DOM with all XML data
     * @return
     */
     public ArrayList<ArrayList<String>> getPolygonData(Document doc) {
        ArrayList<ArrayList<String>> polygons = new ArrayList<ArrayList<String>>();
        NodeList ways = doc.getElementsByTagName("way");
        for (int i = 0; i < ways.getLength(); i++) {
            Node w_item = ways.item(i);
            String w_id = "";
            if(w_item instanceof Element){
                //a child element to process
                Element child = (Element) w_item;
                String id = child.getAttribute("id");
                if(id.compareTo("") != 0) {
                    w_id = id; // way id
                    //WebAppInterface.build_id = w_id; //
                } else continue;
            }

            //int w_id = Integer.parseInt(w_item.getAttributes().item(0).getNodeValue()); // way id
            NodeList w_nodes = w_item.getChildNodes();
            boolean is_building = false;
            ArrayList<String> node_ids = new ArrayList<String>();
            for(int j = 0; j < w_nodes.getLength(); j++) {
                Node w_child = w_nodes.item(j);

                if(w_child instanceof Element){
                    //a child element to process
                    Element child = (Element) w_child;
                    String ref = child.getAttribute("ref");
                    if(ref.compareTo("") != 0) {
                        String n_id = ref; // node id
                        node_ids.add(n_id);
                    }
                    String build = child.getAttribute("k");
                    if(build.compareTo("building") == 0) {
                        build_ids.add(w_id);
                        is_building = true;
                    }
                }
            }

            if(node_ids.size() > 0 && is_building) {
                polygons.add(node_ids);
            }
        }
        return polygons;
    }

    /**
     * returns points
     * @param doc - DOM with all XML data
     * @return
     */
    public HashMap<String, ArrayList<Float>> getPointData(Document doc) {
        HashMap<String, ArrayList<Float>> points = new HashMap<String, ArrayList<Float>>();
        NodeList nodes = doc.getElementsByTagName("node");
        for (int i = 0; i < nodes.getLength() ; i++) {
            Node n_item = nodes.item(i);
            String n_id = "";
            float latitude = -1; float longitude = -1;
            if(n_item instanceof Element){
                //a child element to process
                Element child = (Element) n_item;

                String id = child.getAttribute("id");
                if(id.compareTo("") != 0) {
                    n_id = id; // node id
                }
                String lat = child.getAttribute("lat");
                if(lat.compareTo("") != 0) {
                    latitude = Float.parseFloat(lat); // lat
                }
                String lon = child.getAttribute("lon");
                if(lat.compareTo("") != 0) {
                    longitude = Float.parseFloat(lon); // lon
                }
            }
            if(n_id != "" && latitude != -1 && longitude != -1) {
                ArrayList<Float> node_coords = new ArrayList<Float>();
                node_coords.add(latitude);
                node_coords.add(longitude);

                points.put(n_id, node_coords);
                n_id = ""; latitude = -1; longitude = -1;
            }
        }

        return points;
    }
}
