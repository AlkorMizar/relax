package by.bsuir.relax.ui.statistic;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.util.Pair;
import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import by.bsuir.relax.R;
import by.bsuir.relax.user.Mood;
import by.bsuir.relax.user.UsersMood;

public class StatisticAdapter extends BaseAdapter {
    Context ctx;
    FragmentActivity activity;
    AlertDialog.Builder alertName;
    LayoutInflater lInflater;
    List<UsersMood> objects;
    EditText editText;

    EditText txt; // user input bar

    StatisticAdapter(Context context,FragmentActivity activity, List<UsersMood> moods) {
        ctx = context;
        this.activity = activity;
        objects = moods;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }


    // кол-во элементов
    @Override
    public int getCount() {
        return objects.size();
    }

    // элемент по позиции
    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    // id по позиции
    @Override
    public long getItemId(int position) {
        return position;
    }

    // пункт списка
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.mood_list_item, parent, false);
        }
        UsersMood p = getProduct(position);
        System.out.println(p.getDate());
        ((TextView)view.findViewById(R.id.itemMood)).setText(p.getMood().toString());
        ((TextView)view.findViewById(R.id.itemDate)).setText(p.getDate().toString());

        return view;
    }

    // товар по позиции
    UsersMood getProduct(int position) {
        return ((UsersMood) getItem(position));
    }
}