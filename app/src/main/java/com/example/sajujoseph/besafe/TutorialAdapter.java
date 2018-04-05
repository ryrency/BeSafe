package com.example.sajujoseph.besafe;

/**
 * Created by sajujoseph on 3/30/18.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.support.v4.app.ActivityCompat.startActivityForResult;
import static android.support.v4.content.ContextCompat.startActivity;


/**
     * Created by sajujoseph on 3/27/18.
     */
    public class TutorialAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private final Context mContext;
        private final TutorialImages[] imageList;
        private LayoutInflater inflater;
        TextView done;
        private final View.OnClickListener mOnClickListener = new MyOnClickListener() ;


        TutorialImages image;

        public TutorialAdapter(Context context, TutorialImages[] imageList) {
            this.mContext = context;
            this.imageList = imageList;
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            Log.i("RENCY","into onCreateViewHolder");
            RecyclerView.ViewHolder viewHolder=null;
            inflater = LayoutInflater.from(parent.getContext());
            View view  = inflater.inflate(R.layout.tutorial_image_layout, parent,false);
            viewHolder = new MyViewHolder(view);


            return viewHolder;
        }



        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            Log.i("RENCY","into onBindViewHolder");
                    MyViewHolder vh1 = (MyViewHolder)viewHolder;
            ((MyViewHolder) viewHolder).imageToUpload.setBackground(imageList[position].getImagePath());
            ((MyViewHolder) viewHolder).question_textview.setText(imageList[position].getDescription());


        }

        @Override
        public int getItemCount() {

            return imageList.length;
        }


        public class MyViewHolder extends RecyclerView.ViewHolder{
            public TextView question_textview;
            private ImageView imageToUpload;
            public MyViewHolder(View itemView){
                super(itemView);
                done = (TextView)itemView.findViewById(R.id.tutorial_done);

                imageToUpload = (ImageView) itemView.findViewById(R.id.image_ToUpload);
                question_textview = (TextView) itemView.findViewById(R.id.image_question_tv);
                done.setOnClickListener(mOnClickListener);


            }


        }
    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent collectNameInfo = new Intent(mContext, CollectPersonalInfo.class);
            mContext.startActivity(collectNameInfo);

        }



    }
    }



