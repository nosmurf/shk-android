package com.nosmurf.shk.view.adapter;

import android.view.View;
import android.widget.TextView;

import com.nosmurf.domain.model.Access;
import com.nosmurf.shk.R;
import com.sergiocasero.extendedrecycleradapter.ExtendedRecyclerAdapter;

import java.text.DateFormat;

import butterknife.Bind;

/**
 * Created by Daniel on 03/12/2016.
 */

public class AccessAdapter extends ExtendedRecyclerAdapter<Access> {

    @Override
    protected ExtendedRecyclerViewHolder getViewHolder(View view) {
        return new AccessViewHolder(view);
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_access;
    }

    public class AccessViewHolder extends ExtendedRecyclerViewHolder {

        @Bind(R.id.color)
        TextView color;

        @Bind(R.id.displayName)
        TextView displayName;

        @Bind(R.id.date)
        TextView date;

        @Bind(R.id.time)
        TextView time;

        public AccessViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bind(Access item) {
            if (item.isFace()) {
                this.color.setBackgroundResource(R.drawable.green_circle);
            } else {
                if (item.isNfc()) {
                    this.color.setBackgroundResource(R.drawable.yellow_circle);
                } else {
                    this.color.setBackgroundResource(R.drawable.red_circle);
                }
            }
            if (item.getDisplayName() != null) {
                this.displayName.setText((!item.getDisplayName().isEmpty()) ? item.getDisplayName() : "User not found");
            } else {
                this.displayName.setText("User not found");
            }
            DateFormat timeFormat = new java.text.SimpleDateFormat("HH:mm");
            this.time.setText(timeFormat.format(item.getDate()));
            DateFormat dateFormat = new java.text.SimpleDateFormat("dd/MM/yyyy");
            this.date.setText(dateFormat.format(item.getDate()));
        }
    }

}
