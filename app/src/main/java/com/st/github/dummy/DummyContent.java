package com.st.github.dummy;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.st.github.ItemListActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DummyContent {

    public static final List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();


    static class Github extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection urlConnection = null;
            try{
                URL url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoOutput(true);
                urlConnection.connect();

                BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();

                String jsonString = sb.toString();
                return jsonString;
            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONArray jsonArray = new JSONArray(s);
                Log.d("LOG", jsonArray.toString());
                int length = jsonArray.length();
                for (int i = 0; i < length; i++) {
                    JSONObject jsonrepo = (JSONObject) jsonArray.get(i);
                    String id= jsonrepo.getString("id");
                    String name= jsonrepo.getString("name");
                    String detail= jsonrepo.getString("description");
                    String html_url= jsonrepo.getString("html_url");

                    addItem(new DummyItem(id,name,detail,html_url));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private static final int COUNT = 25;

    static {
        // Add some sample items.
        new Github().execute("https://api.github.com/users/codanux/repos");


    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
        ItemListActivity.adapter.notifyDataSetChanged();
    }

    private static DummyItem createDummyItem(int position) {
        return new DummyItem(String.valueOf(position), "Item " + position, makeDetails(position),"html_url");
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public final String id;
        public final String content;
        public final String details;
        public final String html_url;

        public DummyItem(String id, String content, String details,String html_url) {
            this.id = id;
            this.content = content;
            this.details = details;
            this.html_url = html_url;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
