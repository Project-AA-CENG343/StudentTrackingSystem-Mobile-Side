package com.example.birdaha.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.birdaha.R;
import com.example.birdaha.Users.Student;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Map;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {


    private Context context;
    private String title;
    private Map<String, List<Student>> listItem;

    public CustomExpandableListAdapter(Context context, String title, Map<String, List<Student>> listItem) {
        this.context = context;
        this.title = title;
        this.listItem = listItem;
    }

    @Override
    public int getGroupCount() {
        return 1;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listItem.get(title).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return title;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listItem.get(title).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String title = (String) getGroup(groupPosition);
        if(convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.list_group, null);

        TextView txtTitle = (TextView) convertView.findViewById(R.id.TextView_listTitle);
        //txtTitle.setTypeface(null, Typeface.BOLD);
        txtTitle.setText(title);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String title = (String) getChild(groupPosition, childPosition).toString();
        if(convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, null);

        TextView txtChild = (TextView) convertView.findViewById(R.id.TextView_expandableListItem);
        ImageView imageView = convertView.findViewById(R.id.student_image);
        SharedPreferences preferences = context.getSharedPreferences("ParentPrefs",Context.MODE_PRIVATE);
        Student student = (Student) getChild(groupPosition,childPosition);
        String key = "parent_student_data_" + student.getStudent_id();
        String combinedData = preferences.getString(key,"");
        String[] dataParts = combinedData.split("\\|");
        if(dataParts[1].equals("null")){
            Glide.with(context)
                    .load(R.drawable.ic_account_circle_white_24)
                    .into(imageView);
        }
        else{
            if(dataParts.length == 2) {
                int studentId = Integer.parseInt(dataParts[0]);
                String encodedImage = dataParts[1];
                if (student.getStudent_id() == studentId) {
                    byte[] byteArray = Base64.decode(encodedImage, Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                    Glide.with(context)
                            .load(bitmap)
                            .circleCrop()
                            .into(imageView);
                }
            }
        }
        //txtChild.setTypeface(null, Typeface.BOLD);
        txtChild.setText(title);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
