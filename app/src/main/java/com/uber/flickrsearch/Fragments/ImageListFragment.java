package com.uber.flickrsearch.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.uber.flickrsearch.Models.FlickrImageModel;
import com.uber.flickrsearch.R;
import com.uber.flickrsearch.Utils.ImageLoader;

import java.util.ArrayList;

public class ImageListFragment extends Fragment {

    private ArrayList<FlickrImageModel> imageModelList = new ArrayList<>();
    private ImageListAdapter imageGridAdapter;
    private GridView imageGridView;
    private ProgressBar mainProgressBar;
    private ImageListFragmentInterface mCallback;
    private static final String TAG = "ImageListFragment";
    private int preLast;

    public ImageListFragment() {
    }

    public interface ImageListFragmentInterface {
        void onGridViewScrollEnd();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (ImageListFragmentInterface) context;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement ImageListFragmentInterface");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_image_list, container, false);

        imageGridView = rootView.findViewById(R.id.fragment_image_list_grid_view);
        imageGridAdapter = new ImageListAdapter(imageModelList);
        imageGridView.setAdapter(imageGridAdapter);

        mainProgressBar = rootView.findViewById(R.id.fragment_image_list_progress_bar);
        hideLoadingProgress();

        setupEndlessListener();

        return rootView;
    }

    void setupEndlessListener() {

        imageGridView.setOnScrollListener(new AbsListView.OnScrollListener(){
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (totalItemCount == 0)
                    return;

                final int lastItem = firstVisibleItem + visibleItemCount;

                if (lastItem == totalItemCount) {
                    if (preLast != lastItem) { //to avoid multiple calls for last item
                        preLast = lastItem;

                        mCallback.onGridViewScrollEnd();
                        Log.d(TAG, "Grid View Scrolled to end");
                    }
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState){
            }
        });
    }

    public void populateImages(ArrayList<FlickrImageModel> modelList) {
        imageModelList.addAll(modelList);
        imageGridAdapter.notifyDataSetChanged();
        hideLoadingProgress();
    }

    public void clearImages() {
        imageModelList.clear();
        imageGridAdapter.notifyDataSetChanged();
        hideLoadingProgress();
    }

    public void displayLoadingProgress() {
        if (imageModelList.size() == 0) {
            mainProgressBar.setVisibility(View.VISIBLE);
        }
    }

    void hideLoadingProgress() {
        mainProgressBar.setVisibility(View.GONE);
    }

    class ImageListAdapter extends BaseAdapter {

        private ArrayList<FlickrImageModel> flickrImageModelsList;

        ImageListAdapter(ArrayList<FlickrImageModel> items) {
            this.flickrImageModelsList = items;
        }

        @Override
        public int getCount() {
            return flickrImageModelsList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder;

            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.item_list_image, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.imageView = convertView.findViewById(R.id.item_list_image_view);
                convertView.setTag(viewHolder);
            }
            else {
                viewHolder = (ViewHolder)convertView.getTag();
                //viewHolder.imageView.setImageBitmap(null);
            }

            FlickrImageModel imageModel = flickrImageModelsList.get(position);

            if (imageModel != null) {
                String imageUrl = imageModel.getUrl();
                new ImageLoader(viewHolder.imageView, imageUrl);
            }

            return convertView;
        }
    }

    public class ViewHolder{
        ImageView imageView;
    }
}
