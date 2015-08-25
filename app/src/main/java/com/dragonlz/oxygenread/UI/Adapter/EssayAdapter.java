package com.dragonlz.oxygenread.UI.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.dragonlz.oxygenread.R;
import com.dragonlz.oxygenread.UI.Model.Essay;

import java.util.List;


/**
 * Created by DragonؼLz on 2015/8/18
 */
public class EssayAdapter extends ArrayAdapter<Essay> {

    private int resourceId;

    public EssayAdapter(Context context, int resource, List<Essay> objects) {
        super(context, resource, objects);

        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Essay essay = getItem(position);

        String title = essay.getTitle();
        String date = essay.getDate();
        String content = essay.getContent();
        int votePositive = essay.getVotePositive();
        int voteNegative = essay.getVoteNegative();
        int reply = essay.getReply();
        View view;
        ViewHolder viewHolder;
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,null);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) view.findViewById(R.id.tv_title);
            viewHolder.date = (TextView) view.findViewById(R.id.tv_date);
            viewHolder.votePositive = (TextView) view.findViewById(R.id.tv_vote_positive);
            viewHolder.voteNegative = (TextView) view.findViewById(R.id.tv_vote_negative);
            viewHolder.reply = (TextView) view.findViewById(R.id.tv_reply);
            viewHolder.content = (TextView) view.findViewById(R.id.tv_content);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.title.setText(title);
        viewHolder.date.setText(date);
        viewHolder.votePositive.setText("OO: " + votePositive);
        viewHolder.voteNegative.setText("XX: " + voteNegative);
        viewHolder.reply.setText(view.getResources().getString(R.string.reply) + ": " + reply);
        viewHolder.content.setText(content);
        return view;
    }

    class ViewHolder{
        TextView title;
        TextView date;
        TextView votePositive;
        TextView voteNegative;
        TextView reply;
        TextView content;
    }
}

