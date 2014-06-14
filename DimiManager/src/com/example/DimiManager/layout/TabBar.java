package com.example.DimiManager.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import com.example.DimiManager.R;

/**
 * Created by Huying on 2014-06-11.
 */
public class TabBar extends LinearLayout
{
    Context context;
    LinearLayout _tabbar;

    Button _notice;
    Button _todo;

    int _iSelected = 1;

    public TabBar(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.context = context;

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        _tabbar = (LinearLayout) inflater.inflate(R.layout.tabbar, null);
        addView(_tabbar);

        _notice = (Button)_tabbar.findViewById(R.id.btnNotice);
        _notice.setBackgroundResource(R.drawable.notice_on);
        _notice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(_iSelected != 1) {
                    _notice.setBackgroundResource(R.drawable.notice_on);
                    _todo.setBackgroundResource(R.drawable.todo_off);
                    
                    NoticeEventSending();
                    _iSelected = 1;
                }
            }
        });

        _todo = (Button)_tabbar.findViewById(R.id.btnTodo);
        _todo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(_iSelected != 2) {
                    _todo.setBackgroundResource(R.drawable.todo_on);
                    _notice.setBackgroundResource(R.drawable.notice_off);

                    TodoEventSending();
                    _iSelected = 2;
                }
            }
        });
    }

    private void NoticeEventSending() {
        
    }

    private void TodoEventSending(){

    }
}
