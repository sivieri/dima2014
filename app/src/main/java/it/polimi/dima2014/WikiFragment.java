package it.polimi.dima2014;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

public class WikiFragment extends Fragment {
    private static final String WIKITIONARY_ENDPOINT = ".wiktionary.org/w/api.php";
    private static final String USER_AGENT = "DimaTodos/2014 (PoliMi teaching app; alessandro.sivieri@polimi.it)";
    private static final String LIMIT = "10";
    private static final int DELAY = 2000;

    private Map<String, String> codesToLanguagesMap = new HashMap<String, String>();
    private Map<String, String> languagesToCodesMap = new HashMap<String, String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.wiki_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        final Spinner spinner = (Spinner) getActivity().findViewById(R.id.languageSpinner);
        new Thread(new Runnable() {

            @Override
            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("action", "query"));
                params.add(new BasicNameValuePair("meta", "siteinfo"));
                params.add(new BasicNameValuePair("siprop", "languages"));
                params.add(new BasicNameValuePair("format", "json"));
                try {
                    // languages request to be done in the main Website
                    String result = makeHttpRequest("en", params);
                    JSONObject main = new JSONObject(result);
                    JSONObject query = main.getJSONObject("query");
                    JSONArray languages = query.getJSONArray("languages");
                    for (int i = 0; i < languages.length(); ++i) {
                        JSONObject language = languages.getJSONObject(i);
                        WikiFragment.this.codesToLanguagesMap.put(language.getString("code"), language.getString("*"));
                        WikiFragment.this.languagesToCodesMap.put(language.getString("*"), language.getString("code"));
                    }
                    String currentCode = Locale.getDefault().getLanguage();
                    final String currentLanguage = WikiFragment.this.codesToLanguagesMap.get(currentCode);
                    List<String> values = new ArrayList<String>(WikiFragment.this.codesToLanguagesMap.values());
                    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, values);
                    spinner.post(new Runnable() {

                        @Override
                        public void run() {
                            spinner.setAdapter(adapter);
                            spinner.setSelection(adapter.getPosition(currentLanguage));
                        }

                    });
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }).start();
        final ListView definitionsList = (ListView) getActivity().findViewById(R.id.definitionsList);
        final EditText searchEdit = (EditText) getActivity().findViewById(R.id.wordEdit);
        final Runnable filler = new Runnable() {

            @Override
            public void run() {
                String language = (String) spinner.getSelectedItem();
                String code = WikiFragment.this.languagesToCodesMap.get(language);
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("action", "query"));
                params.add(new BasicNameValuePair("list", "search"));
                params.add(new BasicNameValuePair("srsearch", searchEdit.getText().toString()));
                params.add(new BasicNameValuePair("format", "json"));
                params.add(new BasicNameValuePair("srprop", "snippet"));
                params.add(new BasicNameValuePair("limit", LIMIT));
                try {
                    String result = makeHttpRequest(code, params);
                    JSONObject main = new JSONObject(result);
                    JSONObject query = main.getJSONObject("query");
                    JSONArray pages = query.getJSONArray("search");
                    List<CharSequence> pagesHtml = new ArrayList<CharSequence>();
                    for (int i = 0; i < pages.length(); ++i) {
                        JSONObject page = pages.getJSONObject(i);
                        pagesHtml.add(Html.fromHtml(page.getString("snippet")));
                    }
                    final ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_list_item_1, pagesHtml);
                    definitionsList.post(new Runnable() {

                        @Override
                        public void run() {
                            definitionsList.setAdapter(adapter);
                        }

                    });
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

        };
        HandlerThread handlerThread = new HandlerThread("watcher");
        handlerThread.start();
        final Handler handler = new Handler(handlerThread.getLooper());
        searchEdit.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                handler.removeCallbacks(filler);
                handler.postDelayed(filler, DELAY);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // not used
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // not used
            }

        });
        Button clearButton = (Button) getActivity().findViewById(R.id.wordClear);
        clearButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                searchEdit.setText("");
                definitionsList.setAdapter(null);
            }

        });
    }

    public String makeHttpRequest(String code, List<NameValuePair> params) throws ClientProtocolException, IOException {
        StringBuilder result = new StringBuilder();
        HttpClient httpClient = new DefaultHttpClient();
        String query = URLEncodedUtils.format(params, "utf-8");
        HttpGet httpGet = new HttpGet("http://" + code + WIKITIONARY_ENDPOINT + "?" + query);
        httpGet.addHeader("User-Agent", USER_AGENT);
        HttpResponse httpResponse = httpClient.execute(httpGet);
        StatusLine statusLine = httpResponse.getStatusLine();
        if (statusLine.getStatusCode() == 200) {
            HttpEntity entity = httpResponse.getEntity();
            InputStream content = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(content));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
        }
        else {
            Log.e(MainActivity.TAG, "Error: " + statusLine.getStatusCode() + ", " + statusLine.getReasonPhrase());
        }

        return result.toString();
    }

}
