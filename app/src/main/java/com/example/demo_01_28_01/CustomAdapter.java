package com.example.demo_01_28_01;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    Context context;
    String[] questionsList;
    LayoutInflater inflater;

    public static ArrayList<String> selectedAnswers;

    public CustomAdapter(Context context, String[] questionsList) {
        this.context = context;
        this.questionsList = questionsList;

        // khởi tạo danh sách mảng và thêm chuỗi tĩnh cho tất cả các câu hỏi
        selectedAnswers = new ArrayList<>();
        for (int i = 0; i < questionsList.length; i++) {
            selectedAnswers.add("Chưa chọn");
        }
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return questionsList.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = inflater.inflate(R.layout.layout_item, null);

        // Lấy tham chiếu của TextView và Button
        TextView question = convertView.findViewById(R.id.question);
        RadioButton A =  convertView.findViewById(R.id.A);
        RadioButton B =  convertView.findViewById(R.id.B);
        RadioButton C =  convertView.findViewById(R.id.C);
        RadioButton D =  convertView.findViewById(R.id.D);

        // Thực hiện sự kiện setOnCheckedChangeListener trên nút "A"
        A.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                // Đặt giá trị "A" trong ArrayList nếu RadioButton được chọn
                if (isChecked) {
                    selectedAnswers.set(position, "A");
                }
            }
        });

        // Thực hiện sự kiện setOnCheckedChangeListener trên nút "B"
        B.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                // Đặt giá trị "B" trong ArrayList nếu RadioButton được chọn
                if (isChecked) {
                    selectedAnswers.set(position, "B");
                }
            }
        });

        // Thực hiện sự kiện setOnCheckedChangeListener trên nút "C"
        C.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                // Đặt giá trị "C" trong ArrayList nếu RadioButton được chọn
                if (isChecked) {
                    selectedAnswers.set(position, "C");
                }
            }
        });

        // Thực hiện sự kiện setOnCheckedChangeListener trên nút "D"
        D.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                // Đặt giá trị "D" trong ArrayList nếu RadioButton được chọn
                if (isChecked) {
                    selectedAnswers.set(position, "D");
                }
            }
        });

        // Xử lý sau khi cuộn lên/xuống không bị mất đáp án
        if(selectedAnswers.get(position).equals("A")){
            A.setChecked(true);
        }else if(selectedAnswers.get(position).equals("B")){
            B.setChecked(true);
        }else if(selectedAnswers.get(position).equals("C")){
            C.setChecked(true);
        }else if(selectedAnswers.get(position).equals("D")){
            D.setChecked(true);
        }

        // Đặt giá trị trong TextView
        question.setText(questionsList[position]);

        return convertView;
    }
}
