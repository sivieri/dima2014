package it.polimi.dima2014;

import android.app.Activity;
import android.os.Bundle;

public class WikiActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wiki);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.wikicontainer, new WikiFragment()).commit();
        }
    }
}
