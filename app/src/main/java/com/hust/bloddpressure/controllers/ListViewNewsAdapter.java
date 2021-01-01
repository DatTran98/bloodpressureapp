package com.hust.bloddpressure.controllers;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hust.bloddpressure.R;
import com.hust.bloddpressure.model.entities.News;

import java.util.ArrayList;

public class ListViewNewsAdapter extends BaseAdapter {
    final ArrayList<News> listNews;

    public ListViewNewsAdapter(ArrayList<News> listNews) {
        this.listNews = listNews;
    }

    @Override
    public int getCount() {
        return listNews.size();
    }

    @Override
    public Object getItem(int i) {
        return listNews.get(i);
    }

    @Override
    public long getItemId(int i) {
        return listNews.get(i).getNewId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View newView;
        if (view == null) {
            newView = View.inflate(viewGroup.getContext(), R.layout.news_item_view, null);
        } else newView = view;
        News news = (News) getItem(i);
        ((TextView) newView.findViewById(R.id.news_id)).setText(String.format("Mã bản tin: %d", news.getNewId()));
        ((TextView) newView.findViewById(R.id.title_news)).setText(String.format("%s", news.getTitleNew()));
        ((TextView) newView.findViewById(R.id.content_news)).setText(String.format("Nội dung: %s", news.getContentNew()));
        return newView;
    }
}
