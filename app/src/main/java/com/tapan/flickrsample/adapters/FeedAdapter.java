package com.tapan.flickrsample.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.tapan.flickrsample.R;
import com.tapan.flickrsample.listeners.OnClickListener;
import com.tapan.flickrsample.objects.FeedObject;
import com.tapan.flickrsample.utils.Utils;

import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int TYPE_INFO = 0;
    private final int TYPE_DATA = 1;

    private Context context;
    private List<FeedObject.Items> feedList;
    private int selectedPos = -1;
    private OnClickListener onClickListener;

    public FeedAdapter(Context context, List<FeedObject.Items> feedList) {

        onClickListener = (OnClickListener) context;
        this.context = context;
        this.feedList = feedList;
    }

    @Override
    public int getItemViewType(int position) {

        if (selectedPos!= -1){

            int infoPos = Utils.getInfoPosition(selectedPos);

            if(position == infoPos) {
                 return TYPE_INFO;
             } else {
                return TYPE_DATA;
             }
        } else {
            return TYPE_DATA;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType == TYPE_DATA) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
            return new DataViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_info, parent, false);
            return new InformationViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        if(holder instanceof DataViewHolder) {

            DataViewHolder viewHolder = (DataViewHolder) holder;

            int feedlistPosition = position;

            if(selectedPos!=-1) {

               int infoPos = Utils.getInfoPosition(selectedPos);

               if(infoPos<=position) {
                   feedlistPosition = feedlistPosition-1;
               }

               if(selectedPos == position) {

                   viewHolder.vSelection.setVisibility(View.VISIBLE);
                   viewHolder.vTriangle.setVisibility(View.VISIBLE);

               } else {
                   viewHolder.vSelection.setVisibility(View.GONE);
                   viewHolder.vTriangle.setVisibility(View.INVISIBLE);
               }
            } else {

                viewHolder.vSelection.setVisibility(View.GONE);
                viewHolder.vTriangle.setVisibility(View.INVISIBLE);
            }

            FeedObject.Items item = feedList.get(feedlistPosition);

            if (item.getMedia() != null && item.getMedia().getM() != null && item.getMedia().getM().length() > 0) {

                Glide.with(context)
                        .load(item.getMedia().getM())
                        .apply(new RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                .placeholder(R.drawable.default_placeholder))
                        .into(viewHolder.iv);
            } else {

                Glide.with(context)
                        .load(R.drawable.default_placeholder)
                        .apply(new RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                        .into(viewHolder.iv);
            }

            final int finalFeedlistPosition = feedlistPosition;

            viewHolder.iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (onClickListener != null) {

                        onClickListener.onClick(finalFeedlistPosition);
                    }
                }
            });

        } else {

            InformationViewHolder viewHolder = (InformationViewHolder) holder;
            FeedObject.Items item = feedList.get(selectedPos);
            viewHolder.tv.setText(Html.fromHtml(item.getDescription()));
        }
    }

    @Override
    public int getItemCount() {

        if(selectedPos==-1) {
            return feedList.size();
        } else {
            return feedList.size()+1;
        }
    }

    public void setData(List<FeedObject.Items> feedList) {
        this.feedList = feedList;
        notifyDataSetChanged();
    }

    public static class DataViewHolder extends RecyclerView.ViewHolder {

        ImageView iv;
        View vSelection;
        View vTriangle;

        public DataViewHolder(View v) {
            super(v);
            iv = v.findViewById(R.id.iv);
            vSelection = v.findViewById(R.id.vSelection);
            vTriangle = v.findViewById(R.id.vTriangle);
        }
    }

    public static class InformationViewHolder extends RecyclerView.ViewHolder {

        TextView tv;

        public InformationViewHolder(View v) {
            super(v);
            tv = v.findViewById(R.id.tv);
        }
    }

    public int getSelectedPos() {

        return selectedPos;
    }

    public void setSelectedPos(int selectedPos) {

        this.selectedPos = selectedPos;
    }
}
