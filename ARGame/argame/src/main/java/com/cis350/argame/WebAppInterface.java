package com.cis350.argame;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseUser;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Anton on 3/16/14.
 */
public class WebAppInterface {
    private boolean isLoggedIn = ParseManager.isLoggedIn();

    private Context mContext;
    private WebView myWebView;

    // Create a new HttpClient and Post Header
    private HttpClient httpclient;
    private HttpPost httppost;

    public static final String DEFAULT_CURRENT_ID = "-69696969";

    /** This constructor instantiate the interface and set the context. */
    public WebAppInterface(final Context c, final WebView w) {
        mContext = c;
        myWebView = w;

        // Create a new HttpClient and Post Header
        httpclient = new DefaultHttpClient();
        httppost = new HttpPost("http://overpass-api.de/api/interpreter");

    }

    private String currentID;

    private class BuildingLoader extends AsyncTask<String, Integer, String> {

        private ArrayList<String> buildIDs;
        private ArrayList<String> ownerIDs;
        private ArrayList<ArrayList<String>> polys;
        private HashMap<String, ArrayList<Float>> points;
        private ArrayList<ArrayList<String>> polygons;
        private String pointData;

        protected String doInBackground(String... strs) {

            String bbox = strs[0];
            ownerIDs = new ArrayList<String>();
            buildIDs = new ArrayList<String>();

            // --------------- Data Structures ---------------- //
            // key = way id, value = array of node ids
            polygons = new ArrayList<ArrayList<String>>();
            polys = new ArrayList<ArrayList<String>>();
            // key = node id, value = array of latitude, longitude
            points = new HashMap<String, ArrayList<Float>>();

            String bounds[] = bbox.split(","); // w s e n

            XMLQueryHandler xmlHandler = new XMLQueryHandler();
            String output = xmlHandler.getXMLDataFromBBox(bounds, httpclient, httppost);

            try {
                // ------------ Parsing XML -------------- //
                // Create a DOM element to parse XML
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                factory.setNamespaceAware(true); // allows access to localName
                DocumentBuilder db = factory.newDocumentBuilder();
                InputSource inStream = new InputSource();
                inStream.setCharacterStream(new StringReader(output));
                Document doc = db.parse(inStream);

                // populate polygons
                polygons = xmlHandler.getPolygonData(doc);

                // populate points
                points = xmlHandler.getPointData(doc);

            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }

            ///getting building ids

            buildIDs = xmlHandler.getBuildIds();

            // polygons = xmlHandler.getPolygonData(doc );
            // points = xmlHandler.getPointData(doc);

            // --------------- to Leaflet --------------- //
            // send all polygon data to Leaflet to draw on the map

            pointData = "";
            String o_id = "";
            String[] out = new String[0];
            //getting the list of building ids
            String[] b_o = new String[buildIDs.size()];
            for (int k = 0; k < buildIDs.size(); k++) {
                b_o[k] = buildIDs.get(k);
            }
            try {
                out = ParseManager.makeArrayOfOwners(ParseManager.getBuildingsOwnersIds(b_o));
                //Log.w("HERE","I am here"+out.length+"");
                buildIDs.clear();
                ownerIDs.clear();
                int index = 0;
                for (int k = 0; k < out.length/2; k++) {
                    buildIDs.add(out[index]);
                    ownerIDs.add(out[index+1]);
                    for (int i = 0; i < polygons.size(); i++) {
                        if (polygons.get(i).get(0) == buildIDs.get(k)) {
                            ArrayList<String> p = polygons.get(i);
                            p.remove(0);
                            polys.add(p);
                            break;
                        }
                    }
                    //Log.w("ownerIDs buildingIDs", ownerIDs.get(k) + " " + buildIDs.get(k));
                    index += 2;
                }
                //Log.w("HERETHERE","I am here"+ownerIDs.size()+"");
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
            //Log.w("myAppOwners", "current owner ids size is " + out.length + "");

            //Iterator it = polygons.entrySet().iterator();
            //int polygon_num = polygons.size();
            //int index = 0;
            /*while (it.hasNext()) {
                HashMap.Entry pairs = (HashMap.Entry)it.next();
                String way_id = (String) pairs.getKey();
                ArrayList<String> polygon = (ArrayList<String>) pairs.getValue();
                //Log.w("myApp", "current ownerID at i "+o_id+"");
                // if (!ownerIDs.isEmpty()) { //REMOVE THIS
                if (ownerIDs.size() > 0) {
                    o_id = ownerIDs.get(index);
                } else {
                    o_id = null;
                }
                for( int j = 0; j < polygon.size(); j ++ ) {
                    ArrayList<Float> lat_lon = points.get(polygon.get(j));
                    if( lat_lon != null ) {
                        pointData += lat_lon.get(0) + "," + lat_lon.get(1);
                    }
                    if(j < polygon.size() - 1) {
                        pointData += ";";
                    }
                }
                if(pointData.compareTo("") != 0) {
                    myWebView.loadUrl("javascript:drawPolygonFromPoints(\""
                            +pointData+"\",\""+currentID+"\",\""+currentID+"\",\""+o_id+"\")");
                    pointData = "";
                }
                it.remove(); // avoids a ConcurrentModificationException
                index++;
            }*/
            return "";
        }
        protected void onPostExecute(String result) {
            outerPolygonLoop(ownerIDs, buildIDs, polys, points, pointData);
        }

        private void outerPolygonLoop(ArrayList<String> owner_ids, ArrayList<String> build_ids, ArrayList<ArrayList<String>> polygons, HashMap<String, ArrayList<Float>> points, String point_data) {
            String o_id;
            Log.w("myAppSize", "size " + owner_ids.size() + " " + build_ids.size());
            for( int i = 0; i < polygons.size(); i ++) {
                ArrayList<String> polygon = polygons.get(i);
                //Log.w("myApp", "current ownerID at i "+o_id+"");
                // if (!ownerIDs.isEmpty()) { //REMOVE THIS
                if (owner_ids.size() > 0) {
                    if (owner_ids.get(i) != null) {
                        o_id = owner_ids.get(i);
                    } else o_id = "";
                } else {
                    o_id = "";
                }
                Log.w("myApp", "current ownerID at i " + owner_ids.get(i) + " " + build_ids.get(i) + " ");
                //} //REMOVE THIS
                point_data = iterateThroughPolygons(build_ids, points, point_data, o_id, i, polygon);
            }
            owner_ids.clear();
        }

        private String iterateThroughPolygons(ArrayList<String> build_ids, HashMap<String, ArrayList<Float>> points, String point_data, String o_id, int i, ArrayList<String> polygon) {
            for( int j = 0; j < polygon.size(); j ++ ) {
                ArrayList<Float> lat_lon = points.get(polygon.get(j));
                if( lat_lon != null ) {
                    point_data += lat_lon.get(0) + "," + lat_lon.get(1);
                }
                if(j < polygon.size() - 1) {
                    point_data += ";";
                }
            }
            if(point_data.compareTo("") != 0) {
                loadURL(build_ids, point_data, o_id, i);
                point_data = "";
            }
            return point_data;
        }

        private void loadURL(ArrayList<String> build_ids, String point_data, String o_id, int i) {
            myWebView.loadUrl("javascript:drawPolygonFromPoints(\""
                    +point_data+"\",\""+build_ids.get(i)+"\",\""+ currentID +"\",\""+o_id+"\")");
        }

        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
        }

    }

    @JavascriptInterface
    public void showBuildings(String bbox) {
        new BuildingLoader().execute(bbox);

    }

    @JavascriptInterface
    public void showBuildingDialog(final String ids) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                mContext);

        // set title
        alertDialogBuilder.setTitle("Capture Structure");

        LayoutInflater inflater = LayoutInflater.from(mContext);

        // set dialog message
        String msg = "My id is" + ids;
        //owner of this building
        String owner = "";

        GameActivity game = (GameActivity) mContext;

        View dialog_view = inflater.inflate(R.layout.building_dialog, null);
        TextView tv1 = (TextView)dialog_view.findViewById(R.id.building_army_size_text);
        tv1.setText(ids);
        Log.w("build ID", "build id is " + ids);
        alertDialogBuilder
                .setView(dialog_view)
                        //.setMessage(msg)
                .setCancelable(true)
                .setPositiveButton("Capture",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                        Log.w("build ID", "build id is " + ids);
                        ParseManager.createPoint(ids, 10);

                        Log.w("Capture", "initiate building capture");
                    }
                })
                .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }

    @JavascriptInterface
    public void setCurrentIdInJs() {
        //setting the current user ID////
        if(isLoggedIn) {
            ParseUser curr_user = ParseManager.getCurrentUser();
            //Log.w("myApp", "current user is "+curr_user+"");
            String curr_id = curr_user.getObjectId();
            //currentID = curr_id;
            //Log.w("myApp", "current user id is "+curr_id+"");
            if (curr_id != null) {
                currentID = curr_id;
                myWebView.loadUrl("javascript:getCurrentId(\""+curr_id+"\")");
            } else {
                currentID = DEFAULT_CURRENT_ID;
            }
        } else {
            currentID = DEFAULT_CURRENT_ID;
        }
        //////
    }
}
