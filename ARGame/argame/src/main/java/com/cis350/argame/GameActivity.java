package com.cis350.argame;

import com.cis350.argame.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
//import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
//import org.osmdroid.views.MapController;
//import org.osmdroid.views.MapView;


import android.app.Activity;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.GeolocationPermissions;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class GameActivity extends Activity {

    WebView myWebView;
    // Web Interface to bind the javascript file
    public class WebAppInterface {
        Context mContext;

        // Create a new HttpClient and Post Header
        HttpClient httpclient;
        HttpPost httppost;

        /** Instantiate the interface and set the context */
        WebAppInterface(Context c) {
            mContext = c;

            // Create a new HttpClient and Post Header
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost("http://overpass-api.de/api/interpreter");

        }

        /** Show a toast from the web page */
        @JavascriptInterface
        public void showToast(String toast) {
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
        }

        private class BuildingLoader extends AsyncTask<String, Integer, String> {

            protected String doInBackground(String... strs) {

                String bbox = strs[0];
                // --------------- Data Structures ---------------- //
                // key = way id, value = array of node ids
                ArrayList<ArrayList<String>> polygons = new ArrayList<ArrayList<String>>();
                // key = node id, value = array of latitude, longitude
                HashMap<String, ArrayList<Float>> points = new HashMap<String, ArrayList<Float>>();

                String output = "";
                // ------------------------------------------------ //

                //Toast.makeText(mContext, bbox, Toast.LENGTH_SHORT).show();
                String bounds[] = bbox.split(","); // w s e n

                // ---------------- OVERPASS API ------------------ //
                // use XML queries and send them to interpreter as POST methods

                try {

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

                    // Find all ways with key "building" and all their member nodes
                    String data=
/*
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?><osm-script timeout=\"900\" element-limit=\"1073741824\">"+
                "<query type=\"way\">"+
                "<has-kv k=\"building\" v=\"yes\"/>"+
                "<bbox-query s=\""+bounds[1]+"\" w=\""+bounds[0]+"\" n=\""+bounds[3]+"\" e=\""+bounds[2]+"\"/>"+
                "</query>"+
                "<union>"+
                "<item />"+
                "<recurse type=\"way-node\"/>"+
                "</union>"+
                "<print/></osm-script>";
*/
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
                        System.out.println(output);
                    }

                    // ------------ Parsing XML -------------- //
                    // Create a DOM element to parse XML
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    factory.setNamespaceAware(true); // allows access to localName
                    DocumentBuilder db = factory.newDocumentBuilder();
                    InputSource inStream = new InputSource();
                    inStream.setCharacterStream(new StringReader(output));
                    Document doc = db.parse(inStream);

                    // populate polygons
                    NodeList ways = doc.getElementsByTagName("way");
                    for (int i = 0; i < ways.getLength(); i++) {
                        Node w_item = ways.item(i);
                        String w_id = "";
                        if(w_item instanceof Element){
                            //a child element to process
                            Element child = (Element) w_item;
                            String id = child.getAttribute("id");
                            if(id.compareTo("") != 0) {
                                w_id = id; // node id
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
                                    is_building = true;
                                }
                            }
                        }

                        if(node_ids.size() > 0 && is_building) {
                            polygons.add(node_ids);
                        }
                    }

                    // populate points
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

                } catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                }

                // --------------- to Leaflet --------------- //
                // send all polygon data to Leaflet to draw on the map

                String point_data = "";
                for( int i = 0; i < polygons.size(); i ++) {
                    ArrayList<String> polygon = polygons.get(i);
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
                        myWebView.loadUrl("javascript:drawPolygonFromPoints(\""+point_data+"\")");
                        point_data = "";
                    }
                }

                //String msg = "Hello from Android";
                //myWebView.loadUrl("javascript:wave()");
                return "";
            }

            protected void onProgressUpdate(Integer... progress) {
                //setProgressPercent(progress[0]);
            }

            protected void onPostExecute(String result) {

            }
        }

        @JavascriptInterface
        public void showBuildings(String bbox) {
            new BuildingLoader().execute(bbox);

        }
    }
    // end Web Interface

    // global variables for osmdroid
    //private MapView myOpenMapView;
    //private MapController myMapController;


    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = true;

    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.gameactivity_layout);

        final View controlsView = findViewById(R.id.fullscreen_content_controls);
        final View contentView = findViewById(R.id.fullscreen_content);

        // Set up an instance of SystemUiHider to control the system UI for
        // this activity.
        mSystemUiHider = SystemUiHider.getInstance(this, contentView, HIDER_FLAGS);
        mSystemUiHider.setup();
        mSystemUiHider
                .setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
                    // Cached values.
                    int mControlsHeight;
                    int mShortAnimTime;

                    @Override
                    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
                    public void onVisibilityChange(boolean visible) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                            // If the ViewPropertyAnimator API is available
                            // (Honeycomb MR2 and later), use it to animate the
                            // in-layout UI controls at the bottom of the
                            // screen.
                            if (mControlsHeight == 0) {
                                mControlsHeight = controlsView.getHeight();
                            }
                            if (mShortAnimTime == 0) {
                                mShortAnimTime = getResources().getInteger(
                                        android.R.integer.config_shortAnimTime);
                            }
                            controlsView.animate()
                                    .translationY(visible ? 0 : mControlsHeight)
                                    .setDuration(mShortAnimTime);
                        } else {
                            // If the ViewPropertyAnimator APIs aren't
                            // available, simply show or hide the in-layout UI
                            // controls.
                            controlsView.setVisibility(visible ? View.VISIBLE : View.GONE);
                        }

                        if (visible && AUTO_HIDE) {
                            // Schedule a hide().
                            delayedHide(AUTO_HIDE_DELAY_MILLIS);
                        }
                    }
                });

        // Set up the user interaction to manually show or hide the system UI.
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TOGGLE_ON_CLICK) {
                    mSystemUiHider.toggle();
                } else {
                    mSystemUiHider.show();
                }
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);

        // Showing the Map
        // --------------- osmdroid --------------- //

         /*  myOpenMapView = (MapView)findViewById(R.id.openmapview);
        myOpenMapView.setBuiltInZoomControls(true);
        myMapController = myOpenMapView.getController();
        myMapController.setZoom(4);
        myOpenMapView.setTileSource(TileSourceFactory.MAPQUESTOSM);
        */
    }

    @Override
    protected void onStart() {
        super.onStart();

        //--------- LEAFLET ----------------//

        myWebView = (WebView) findViewById(R.id.webview);
        myWebView.clearCache(true); // clear the cached javascript

        // add web interface
        myWebView.addJavascriptInterface(new WebAppInterface(this), "Android");

        WebSettings webSettings = myWebView.getSettings();

        // Permission to get user's geolocation
        webSettings.setGeolocationEnabled(true);
        myWebView.setWebChromeClient(new WebChromeClient() {
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }
        });
        webSettings.setDomStorageEnabled(true);

        // Enable javascript in the html file of WebView
        webSettings.setJavaScriptEnabled(true);

        //----------File Location for loadUrl ------------//
        //use "file:///android_asset/map.html" for device
        //use "http://poroawards.net/Geolocation/map.html" for web or emulator, change hosting later

        myWebView.loadUrl("http://poroawards.net/Geolocation/map.html");
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }


    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    Handler mHideHandler = new Handler();
    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            mSystemUiHider.hide();
        }
    };

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}
