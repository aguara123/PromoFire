package org.activity.promofire.photolist.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.activity.promofire.R;

/**
 * Created by DESARROLLO on 09/01/17.
 */

public class CustomAdapterPage extends PagerAdapter {

    private int[] img = {R.drawable.amazon, R.drawable.android, R.drawable.bing};

    private Context context;
    private LayoutInflater layoutInflater;

    public CustomAdapterPage(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return img.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == (LinearLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item = layoutInflater.inflate(R.layout.swipe, container, false);

        ImageView imageView = (ImageView) item.findViewById(R.id.imageView);
        TextView textView = (TextView) item.findViewById(R.id.textView2);

        imageView.setImageResource(img[position]);
        textView.setText("Image: " + position);

        container.addView(item);

        return item;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
      container.removeView((LinearLayout) object);
    }
}
