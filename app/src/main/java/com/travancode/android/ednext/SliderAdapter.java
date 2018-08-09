package com.travancode.android.ednext;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SliderAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;

    public SliderAdapter(Context context)
    {
        this.context = context;
    }

    public int[] slide_images = {
            R.drawable.kerala_logo_red,
            R.drawable.uckdsc,
            R.drawable.alpha
    };

    public String[] slide_headings = {
            "KERALA UNIVERSITY\nCOLLEGE OF ENGINEERING",
            "UCK Developer Student Club",
            "I am an alpha"
    };

    public String[] slide_descs = {
            "University of Kerala\nKariavattam Campus",
            "Developed and maintained by UCK DSC",
            "This is an alpha version. Please check for updates.",
    };

    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        if(layoutInflater == null)
        {
            Log.d("SliderAdapter", "LayoutInflator error");
        }

        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        ImageView slideImageView = (ImageView) view.findViewById(R.id.slide_pic);
        TextView slideHeading = (TextView) view.findViewById(R.id.slide_heading);
        TextView slideDescription = (TextView) view.findViewById(R.id.slide_text);

        slideImageView.setImageResource(slide_images[position]);
        slideHeading.setText(slide_headings[position]);
        slideDescription.setText(slide_descs[position]);

        slideHeading.setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/Raleway-Bold.ttf" ));
        slideDescription.setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/Raleway-Regular.ttf" ));

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        container.removeView((RelativeLayout)object);
    }
}
