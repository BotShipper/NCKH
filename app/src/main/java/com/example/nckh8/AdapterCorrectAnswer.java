package com.example.nckh8;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterCorrectAnswer extends BaseAdapter {
    Context context;
    String[] listNumericalOrder;
    LayoutInflater inflater;

    TextView numericalOrder;
    RadioButton A, B, C, D;

    public static ArrayList<String> correctAnswers;

    public AdapterCorrectAnswer(Context context, String[] listNumericalOrder) {
        this.context = context;
        this.listNumericalOrder = listNumericalOrder;

        // khởi tạo danh sách mảng và thêm chuỗi tĩnh cho tất cả các câu hỏi
        correctAnswers = new ArrayList<>();
        for (int i = 0; i < listNumericalOrder.length; i++) {
            correctAnswers.add("Chưa chọn");
        }
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listNumericalOrder.length;
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

        convertView = inflater.inflate(R.layout.item_correct_answer, null);

        // Lấy tham chiếu của TextView và Button
        numericalOrder = convertView.findViewById(R.id.tv_numerical_order);
        A =  convertView.findViewById(R.id.rdo_a);
        B =  convertView.findViewById(R.id.rdo_b);
        C =  convertView.findViewById(R.id.rdo_c);
        D =  convertView.findViewById(R.id.rdo_d);

        // Thực hiện sự kiện setOnCheckedChangeListener trên nút "A"
        A.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                // Đặt giá trị "A" trong ArrayList nếu RadioButton được chọn
                if (isChecked) {
                    correctAnswers.set(position, "A");
                }
            }
        });

        // Thực hiện sự kiện setOnCheckedChangeListener trên nút "B"
        B.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                // Đặt giá trị "B" trong ArrayList nếu RadioButton được chọn
                if (isChecked) {
                    correctAnswers.set(position, "B");
                }
            }
        });

        // Thực hiện sự kiện setOnCheckedChangeListener trên nút "C"
        C.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                // Đặt giá trị "C" trong ArrayList nếu RadioButton được chọn
                if (isChecked) {
                    correctAnswers.set(position, "C");
                }
            }
        });

        // Thực hiện sự kiện setOnCheckedChangeListener trên nút "D"
        D.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                // Đặt giá trị "D" trong ArrayList nếu RadioButton được chọn
                if (isChecked) {
                    correctAnswers.set(position, "D");
                }
            }
        });

        // Xử lý sau khi cuộn lên/xuống không bị mất đáp án
        if(correctAnswers.get(position).equals("A")){
            A.setChecked(true);
        }else if(correctAnswers.get(position).equals("B")){
            B.setChecked(true);
        }else if(correctAnswers.get(position).equals("C")){
            C.setChecked(true);
        }else if(correctAnswers.get(position).equals("D")){
            D.setChecked(true);
        }

        // Đặt giá trị trong TextView
        numericalOrder.setText(listNumericalOrder[position]);

        return convertView;
    }
}

//public class AdapterCorrectAnswer extends BaseAdapter {
//    Context context;
//    String[] listNumericalOrder;
//    LayoutInflater inflater;
//
//    // Tham chiếu
//    TextView numericalOrder;
//    RadioButton A, B, C, D;
//
//    private SparseBooleanArray checkedStates; // Dùng để lưu trạng thái của RadioButton đã chọn
//
//    public AdapterCorrectAnswer(Context context, String[] listNumericalOrder) {
//        this.context = context;
//        this.listNumericalOrder = listNumericalOrder;
//        inflater = LayoutInflater.from(context);
//        checkedStates = new SparseBooleanArray();
//    }
//
//    @Override
//    public int getCount() {
//        return listNumericalOrder.length;
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return null;
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return 0;
//    }
//
//    @Override
//    public View getView(final int position, View convertView, ViewGroup parent) {
//        convertView = inflater.inflate(R.layout.item_correct_answer, null);
//
//        // Lấy tham chiếu của TextView và RadioButton
//        numericalOrder = convertView.findViewById(R.id.tv_numerical_order);
//        A = convertView.findViewById(R.id.rdo_a);
//        B = convertView.findViewById(R.id.rdo_b);
//        C = convertView.findViewById(R.id.rdo_c);
//        D = convertView.findViewById(R.id.rdo_d);
//
//        // Đặt giá trị trong TextView
//        numericalOrder.setText(listNumericalOrder[position]);
//
//        // Đặt trạng thái của RadioButton dựa trên checkedStates
//        A.setChecked(checkedStates.get(position) && checkedStates.keyAt(position) == A.getId());
//        B.setChecked(checkedStates.get(position) && checkedStates.keyAt(position) == B.getId());
//        C.setChecked(checkedStates.get(position) && checkedStates.keyAt(position) == C.getId());
//        D.setChecked(checkedStates.get(position) && checkedStates.keyAt(position) == D.getId());
//
//        // Xử lý sự kiện khi RadioButton được chọn
//        CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                // Lưu trạng thái của RadioButton đã chọn vào checkedStates
//                checkedStates.put(position, isChecked);
//            }
//        };
//
//        // Gán sự kiện cho RadioButton
//        A.setOnCheckedChangeListener(checkedChangeListener);
//        B.setOnCheckedChangeListener(checkedChangeListener);
//        C.setOnCheckedChangeListener(checkedChangeListener);
//        D.setOnCheckedChangeListener(checkedChangeListener);
//
//        return convertView;
//    }
//}
