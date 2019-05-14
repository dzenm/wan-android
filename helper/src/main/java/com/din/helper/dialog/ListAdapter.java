package com.din.helper.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.din.helper.R;

/**
 * @author dinzhenyan
 * @date 2019-04-30 20:03
 * @IDE Android Studio
 * <p>
 * 带List的Dialog需要用到的Adapter
 */
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
            convertView = inflater.inflate(R.layout.lv_item, null);
            viewHolder.tvText = convertView.findViewById(R.id.tv_text);
            if (isIcon) {
                viewHolder.ivIcon = convertView.findViewById(R.id.iv_icon);
            }
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvText.setText(texts[position]);
        if (isIcon) {
            viewHolder.ivIcon.setImageResource(icons[position]);
        }
        return convertView;
    }

    public class ViewHolder {
        private TextView tvText;
        private ImageView ivIcon;
    }
}
