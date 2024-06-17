package com.example.nckh8;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import static com.example.nckh8.ListExamCode.examCodeArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListCorrectAnswers extends AppCompatActivity {

    String[] numericalOrder;
    ListView lvCorrectAnswers;
    Button btnSelectImage;
    Toolbar tb_back_exam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_correct_answers);

        lvCorrectAnswers = findViewById(R.id.lv_correct_answers);
        btnSelectImage = findViewById(R.id.btn_select_image);
        tb_back_exam = findViewById(R.id.tb_back_exam);

        // Tải thư viện OpenCV
        if (!OpenCVLoader.initDebug()) {
            Log.e(TAG, "OpenCV library not loaded");
        } else {
            Log.d(TAG, "OpenCV library loaded successfully");
        }

        String code = getIntent().getStringExtra("code");
        ArrayList<String> answers = getIntent().getStringArrayListExtra("answers");
        String number = getIntent().getStringExtra("number");

        int index = 0;
        index = getIntent().getIntExtra("index", index);

        // Đặt tiêu đề cho trang chọn đáp án
        tb_back_exam.setTitle("Mã đề " + code);

        if (number != null) {
            numericalOrder = new String[Integer.parseInt(number)];

            for (int i = 0; i < numericalOrder.length; i++) {
                if (i<9) {
                    numericalOrder[i] = "0" + (i+1);
                    continue;
                }
                numericalOrder[i] = String.valueOf((i+1));
            }

            // Đặt bộ điều hợp để điền dữ liệu vào ListView
            AdapterCorrectAnswer adapterCorrectAnswer = new AdapterCorrectAnswer(getApplicationContext(), numericalOrder, code, answers, index);
            lvCorrectAnswers.setAdapter(adapterCorrectAnswer);

            btnSelectImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean check = true;

                    // Kiểm tra chọn hết đán án chưa
                    for (int i = 0; i < Integer.parseInt(number); i++) {
                        if(AdapterCorrectAnswer.correctAnswers.get(i).equals("Null ")) {
                            Toast.makeText(ListCorrectAnswers.this, "Chưa chọn đáp án câu " + (i+1), Toast.LENGTH_SHORT).show();
                            check =false;
                            break;
                        }
                        Log.d("check", AdapterCorrectAnswer.correctAnswers.get(i));
                    }
                    if (check) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent,1000);
                    }
                }
            });

            // setup toolbar back
            setSupportActionBar(tb_back_exam);
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        } else {
            finish();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && data != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());

                Mat img = new Mat();
                Utils.bitmapToMat(bitmap, img);

                List<Mat> cropImages = OMR2.cropImage(img);
                List<Mat> listAnswers = OMR2.processAnsBlocks(cropImages);
                List<Mat> processListAnswers = OMR2.processListAns(listAnswers);
                ArrayList<String> studentAnswers = new ArrayList<>();

                float totalScore;
                int numberCorrectAnswers = 0;
                int multipleAnswer = 0;
                boolean correctAnswer = false;


                for (int i = 0; i < numericalOrder.length*4; i++) {
                    Mat ans = processListAnswers.get(i);
                    int tes = Core.countNonZero(ans);
                    String mappedAnswer = OMR2.mapAnswer(i);

                    // Duyệt hết 1 câu
                    if (i % 4 == 0) {
                        studentAnswers.add("Null");
                        multipleAnswer = 0;
                        correctAnswer = false;
                    }

                    if (tes > 200) {
                        if (multipleAnswer > 0 ) {
                            studentAnswers.set(i/4,"Null");
                            if (correctAnswer) {
                                numberCorrectAnswers--;
                                correctAnswer = false;
                            }
                        } else {
                            studentAnswers.set(i/4,mappedAnswer);
                            if (AdapterCorrectAnswer.correctAnswers.get(i / 4).equals(mappedAnswer)) {
                                numberCorrectAnswers++;
                                correctAnswer = true;
                            }
                        }
                        multipleAnswer++;
                    }
                    Log.d("CHECK", i + " " + String.valueOf(correctAnswer));
                    Log.d("CHECK", i + " " + numberCorrectAnswers);
                }

                totalScore = (float) numberCorrectAnswers / numericalOrder.length * 10;

                Intent intent = new Intent(ListCorrectAnswers.this, ListStudentAnswers.class);

                // Lưu Bitmap vào bộ nhớ
                String imagePath = saveBitmapToStorage(bitmap);

                // Gửi đường dẫn hình ảnh thay vì Bitmap qua Intent
                intent.putExtra("image_path", imagePath);
                intent.putExtra("numerical_order", numericalOrder);
                intent.putExtra("total_score", totalScore);
                intent.putExtra("student_answers", studentAnswers);
                intent.putExtra("correct_answers", AdapterCorrectAnswer.correctAnswers);

                startActivity(intent);

            } catch (Exception e) {
                e.printStackTrace();
                // Xử lý ngoại lệ khác nếu cần
                Toast.makeText(ListCorrectAnswers.this, "Phiếu không hợp lệ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String saveBitmapToStorage(Bitmap bitmap) {
        String path = ""; // Đường dẫn lưu trữ ảnh

        try {
            // Tạo thư mục lưu trữ (nếu chưa tồn tại)
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File imageFile = File.createTempFile("image", ".jpg", storageDir);

            // Ghi Bitmap vào tệp
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();

            path = imageFile.getAbsolutePath(); // Lấy đường dẫn tệp
        } catch (IOException e) {
            e.printStackTrace();
        }

        return path;
    }

    // Khi ấn nút back sẽ quay lại màn hình trước
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}