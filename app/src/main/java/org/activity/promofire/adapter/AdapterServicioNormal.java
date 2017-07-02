package org.activity.promofire.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;

import org.activity.promofire.R;
import org.activity.promofire.base.ImageLoader;
import org.activity.promofire.entity.Servicio;
import org.activity.promofire.utils.Tools;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DESARROLLO on 07/01/17.
 */

public class AdapterServicioNormal extends RecyclerView.Adapter<AdapterServicioNormal.ViewHolder> {



    private List<Servicio> items;
    private ImageLoader imageLoader;
    private OnItemClickListener onItemClickListener;
    private Context ctx;

    public AdapterServicioNormal(Context ctx, List<Servicio> items) {
        this.ctx = ctx;
        this.items = items;
    }

    public void setItems(List<Servicio> items) {

        //  List<Servicio> itemsAux = items;
        //for (Servicio o : itemsAux){
        //  for (DetalleServicio a : o.detalleServicioList){
        //    if (a.principal == null){
        //      o.detalleServicioList.remove(a);
        //}
        //}
        //}
        this.items = items;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, Servicio obj);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void addPhoto(Servicio servicio) {
        items.add(0, servicio);
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_item_image_view, parent, false);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_normal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Servicio servicio = items.get(position);
        //holder.setOnItemClickListener(servicio, onItemClickListener);
        //Tools.displayImageOriginal(ctx, holder.imgPhoto, servicio.detalleServicioList.get(0).urlPhoto);
        //imageLoader.load(holder.imgPhoto, servicio.detalleServicioList.get(0).urlPhoto);
        //holder.textViewInfo.setText(servicio.informacion);

        holder.setOnItemClickListener(servicio, onItemClickListener);
        //Tools.displayImageOriginal(ctx, holder.image, servicio.detalleServicioList.get(0).urlPhoto);
        Tools.displayImageThumbnail(ctx, holder.image, servicio.detalleServicioList.get(0).urlPhoto, 0.5f);
        holder.brief.setText(servicio.informacion);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //@BindView(R.id.imgPhoto)
        //ImageView imgPhoto;
        //@BindView(R.id.textViewInfo)
        //TextView textViewInfo;

        View itemView;

        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.lyt_color)
        LinearLayout lytColor;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.brief)
        TextView brief;
        @BindView(R.id.lyt_parent)
        MaterialRippleLayout lytParent;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }

        public void setOnItemClickListener(final Servicio servicio,
                                           final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(view, servicio);
                }
            });
        }


    }
}
