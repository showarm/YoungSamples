package info.ipeanut.youngsamples;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by chenshao on 15/12/6.
 */

public class SampleAdapter extends BaseAdapter {
    Context context;
    private List<SampleBean> mItems;

    public SampleAdapter(@Nullable Context context, List<SampleBean> mItems) {
        this.context = context;
        this.mItems = mItems;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1,
                    parent, false);
            convertView.setTag(convertView.findViewById(android.R.id.text1));
        }
        TextView tv = (TextView) convertView.getTag();
        tv.setText(mItems.get(position).name);
        return convertView;
    }

}