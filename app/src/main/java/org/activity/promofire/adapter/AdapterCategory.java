package org.activity.promofire.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;

import org.activity.promofire.R;
import org.activity.promofire.data.AppConfig;
import org.activity.promofire.data.Constant;
import org.activity.promofire.entity.Categoria;
import org.activity.promofire.utils.Tools;

import java.util.ArrayList;
import java.util.List;


public class AdapterCategory extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context ctx;
    private List<Categoria> items = new ArrayList<>();

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, Categoria obj);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView name;
        public TextView brief;
        public ImageView image;
        public LinearLayout lyt_color;
        public MaterialRippleLayout lyt_parent;

        public ViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.name);
            brief = (TextView) v.findViewById(R.id.brief);
            image = (ImageView) v.findViewById(R.id.image);
            lyt_color = (LinearLayout) v.findViewById(R.id.lyt_color);
            lyt_parent = (MaterialRippleLayout) v.findViewById(R.id.lyt_parent);
        }
    }

    public AdapterCategory(Context ctx, List<Categoria> items) {
        this.ctx = ctx;
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder vItem = (ViewHolder) holder;
            final Categoria c = items.get(position);
            vItem.name.setText(c.categoria);
            vItem.brief.setText(c.categoria);
            vItem.lyt_color.setBackgroundColor(Color.parseColor("red"));
            //Tools.displayImageThumbnail(ctx, vItem.image, c.urlPhoto, 0.8f);
            if (c.urlPhoto != null && !c.urlPhoto.equals("")) {
                //Tools.displayImageOriginal(ctx, vItem.image, c.urlPhoto);
                Tools.displayImageIcon(ctx, vItem.image, c.urlPhoto);
            }


            if (AppConfig.TINT_CATEGORY_ICON) {
                vItem.image.setColorFilter(Color.WHITE);
            }

            vItem.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, c);
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setItems(List<Categoria> items) {
        this.items = items;
        notifyDataSetChanged();
    }


}