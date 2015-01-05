package it.polimi.dima2014.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import it.polimi.dima2014.R;

public class WikiHolder extends RecyclerView.ViewHolder {
    private final TextView labelView;
    private CharSequence labelContent;

    public WikiHolder(View itemView) {
        super(itemView);
        labelView = (TextView) itemView.findViewById(R.id.wiki_item_label);
    }

    public void bindLabel(CharSequence label) {
        labelContent = label;
        labelView.setText(labelContent);
    }

}
