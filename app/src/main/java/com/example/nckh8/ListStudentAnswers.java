package com.example.nckh8;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListStudentAnswers extends AppCompatActivity {

    ListView lvStudentAnswers;
    Button btnSelectAnotherImage;
    ImageView imageView;
    Toolbar tb_back_correct;

    // Biến nhận Intent
    ArrayList<String> studentAnswers, correctAnswers;
    String[] numericalOrder;
    String imagePath;
    float totalScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_student_answers);

        lvStudentAnswers = findViewById(R.id.lv_student_answers);
        btnSelectAnotherImage = findViewById(R.id.btn_select_another_image);
        tb_back_correct = findViewById(R.id.tb_back_correct);
        imageView = findViewById(R.id.imageView);

        Intent intent = getIntent();

        // Nhận đường dẫn ảnh từ Intent
        imagePath = intent.getStringExtra("image_path");
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        studentAnswers = intent.getStringArrayListExtra("student_answers");
        correctAnswers = intent.getStringArrayListExtra("correct_answers");
        totalScore = intent.getFloatExtra("total_score", totalScore);
        numericalOrder = intent.getStringArrayExtra("numerical_order");


        imageView.setImageBitmap(bitmap);
        tb_back_correct.setTitle("Bạn được " + String.format("%.2f", totalScore) + " điểm");

        AdapterStudentAnswer adapterStudentAnswer = new AdapterStudentAnswer(getApplicationContext(), numericalOrder, correctAnswers, studentAnswers);
        lvStudentAnswers.setAdapter(adapterStudentAnswer);

        btnSelectAnotherImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,1001);
            }
        });

        // setup toolbar back
        setSupportActionBar(tb_back_correct);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1001 && data != null) {
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

                imageView.setImageBitmap(bitmap);
                tb_back_correct.setTitle("Phiếu được " + String.format("%.2f", totalScore) + " điểm");

                AdapterStudentAnswer adapterStudentAnswer = new AdapterStudentAnswer(getApplicationContext(), numericalOrder, correctAnswers, studentAnswers);
                lvStudentAnswers.setAdapter(adapterStudentAnswer);

            } catch (Exception e) {
                e.printStackTrace();

                Toast.makeText(ListStudentAnswers.this, "Phiếu không hợp lệ", Toast.LENGTH_SHORT).show();
            }
        }
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