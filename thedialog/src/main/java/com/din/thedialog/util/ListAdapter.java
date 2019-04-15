package com.din.thedialog.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.din.thedialog.R;

public class ListAdapter extends BaseAdapter {

    private String[] texts;
    private int[] icons;
    private boolean isIcon;
    private LayoutInflater inflater;

    public ListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public void setData(String[] texts) {
        this.texts = texts;
        isIcon = false;
    }

    public void setData(String[] texts, int[] icons) {
        this.texts = texts;
        this.icons = icons;
        isIcon = true;
    }

    @Override
    public int getCount() {
        return texts == null ? 0 : texts.length;
    }

    @Override
    public Object getItem(int position) {
        return texts[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.info_lv_item, null);
            viewHolder.tv_text = convertView.findViewById(R.id.tv_text);
            if (isIcon) {
                viewHolder.iv_icon = convertView.findViewById(R.id.iv_icon);
            }
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_text.setText(texts[position]);
        if (isIcon) {
            viewHolder.iv_icon.setImageResource(icons[position]);
        }
        return convertView;
    }

    public class ViewHolder {
        private TextView tv_text;
        private ImageView iv_icon;
    }
}
