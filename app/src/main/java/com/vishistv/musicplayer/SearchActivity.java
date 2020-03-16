package com.vishistv.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;
import com.jakewharton.rxbinding.widget.RxSearchView;
import com.vishistv.musicplayer.Helpers.InputStreamVolleyRequest;
import com.vishistv.musicplayer.Variables.Song;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import rx.android.schedulers.AndroidSchedulers;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = "SearchActivity";

    SearchResultFragmentParent searchResultFragmentParent;

    View fSearchResult;

    SearchView svSearch;
//    EditText etSearch;

//    long delay = 1000;
//    long last_text_edit = 0;
//    Handler handler = new Handler();

    ArrayList<Song> searchResult = new ArrayList<>();

    private int resultCount = 0;

    //TO DO: 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        fSearchResult = findViewById(R.id.f_search_result);
        searchResultFragmentParent = (SearchResultFragmentParent) this.getSupportFragmentManager().findFragmentById(R.id.f_search_result);

//        etSearch = findViewById(R.id.sv_search);
        svSearch = findViewById(R.id.sv_search);
        svSearch.setQueryHint(Html.fromHtml("<font color = #E0E0E0>" + getResources().getString(R.string.search_hint) + "</font>"));

//        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(final String s) {
//
//                Runnable input_finish_checker = new Runnable() {
//                    public void run() {
//                        if (System.currentTimeMillis() > (last_text_edit + delay - 500)) {
//                            if (s.length() > 3) {
//                                fSearchResult.setVisibility(View.VISIBLE);
//
//                                String str = s.replaceAll(" ", "+");
//                                searchResult.clear();
//                                Log.d(TAG, "term: " + s);
//                                searchSong(str);
//
//                            } else {
//                                fSearchResult.setVisibility(View.INVISIBLE);
//                            }
//                        }
//                    }
//                };
//
//                handler.removeCallbacks(input_finish_checker);
//                if(s.length() > 0) {
//                    last_text_edit = System.currentTimeMillis();
//                    handler.postDelayed(input_finish_checker, delay);
//                }
//
//                return false;
//            }
//        });

        RxSearchView.queryTextChanges(svSearch)
                .debounce(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(charSequence -> {
                    String s = charSequence.toString();
                    if (s.length() > 3) {
                        fSearchResult.setVisibility(View.VISIBLE);

                        String str = s.replaceAll(" ", "+");
                        searchResult.clear();
                        Log.d(TAG, "term: " + s);
                        searchSong(str);

                    } else {
                        fSearchResult.setVisibility(View.INVISIBLE);
                    }
                });

    }


    void searchSong(String term) {
        String mUrl = "http://itunes.apple.com/search?term=" + term + "&limit=20";
        Log.d(TAG, "url: " + mUrl);

        InputStreamVolleyRequest request = new InputStreamVolleyRequest(Request.Method.GET, mUrl,
                new Response.Listener<byte[]>() {
                    @Override
                    public void onResponse(byte[] response) {
                        // TODO handle the response
                        try {
                            if (response != null) {

                                String res = new String(response);

                                parseResponse(res);


                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            Log.d("KEY_ERROR", e + "");
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "UNABLE TO DOWNLOAD FILE");
                error.printStackTrace();
            }
        }, null);
        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext(), new HurlStack());
        mRequestQueue.add(request);
    }


    void parseResponse(String response) {
        try {
            JSONObject rootObj = new JSONObject(response);

            resultCount = rootObj.getInt("resultCount");

            JSONArray resultArray = rootObj.getJSONArray("results");

            for (int i = 0; i < resultArray.length(); i++) {
                JSONObject resultObj = resultArray.getJSONObject(i);
                String kind = resultObj.getString("kind");

                if (kind.equals("song")) {
                    String songName = resultObj.getString("trackName");
                    String artistName = resultObj.getString("artistName");
                    String albumName = resultObj.getString("collectionName");
                    String prevUrl = resultObj.getString("previewUrl");
                    String albumPic100Url = resultObj.getString("artworkUrl100");
                    String albumPic60Url = resultObj.getString("artworkUrl60");
                    String trackId = resultObj.getString("trackId");

                    Song song = new Song(songName, artistName, albumName, prevUrl, albumPic100Url, albumPic60Url, trackId);
                    searchResult.add(song);
                }
            }

            resultCount = searchResult.size();
            if (resultCount > 0) {
                searchResultFragmentParent.getInfo(resultCount, searchResult);
            }

        } catch (Exception e) {
            Log.d("KEY_ERROR", e + "");
        }

    }

    public void onClickFavSongs(View view) {
        startActivity(new Intent(SearchActivity.this, FavSongActivity.class));
    }


}
