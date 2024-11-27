package com.itq.proyectosoft.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.itq.proyectosoft.R;
import com.itq.proyectosoft.models.sliderItem;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

public class sliderAdapter extends SliderViewAdapter<sliderAdapter.SliderAdapterVH> {

    private Context context;
    private List<sliderItem> mSliderItems = new ArrayList<>();

    public sliderAdapter(Context context, List<sliderItem> sliderItems) {
        this.context = context;
        mSliderItems = sliderItems;
    }
    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slide_layout_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {

        sliderItem sliderItem = mSliderItems.get(position);
        if (sliderItem.getImgUrl() != null)
            if (!sliderItem.getImgUrl().isEmpty())
                Picasso.get().load(sliderItem.getImgUrl()).into(viewHolder.imgViewSlider);

    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return mSliderItems.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imgViewSlider;
        public SliderAdapterVH(View itemView) {
            super(itemView);
            imgViewSlider = itemView.findViewById(R.id.imgViewSlider);

            this.itemView = itemView;
        }
    }

}
