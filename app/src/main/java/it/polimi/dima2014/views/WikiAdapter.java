package it.polimi.dima2014.views;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import it.polimi.dima2014.R;

public class WikiAdapter extends RecyclerView.Adapter<WikiHolder> {
    private List<CharSequence> items;

    public WikiAdapter(List<CharSequence> items) {
        this.items = items;
    }

    @Override
    public WikiHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.wiki_item, viewGroup, false);
        return new WikiHolder(view);
    }

    @Override
    public void onBindViewHolder(WikiHolder wikiHolder, int i) {
        CharSequence label = items.get(i);
        wikiHolder.bindLabel(label);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
