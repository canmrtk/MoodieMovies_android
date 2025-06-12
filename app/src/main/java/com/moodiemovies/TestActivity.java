package com.moodiemovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.moodiemovies.model.TestAnswer;
import com.moodiemovies.model.TestQuestion;
import com.moodiemovies.model.UserAnswer;
import com.moodiemovies.model.TestSubmissionRequest;
import com.moodiemovies.viewmodel.TestViewModel;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    private TestViewModel testViewModel;
    private List<TestQuestion> questions;
    private final List<UserAnswer> userAnswers = new ArrayList<>();
    private int currentQuestionIndex = 0;

    private ImageView backButton;
    private TextView progressText;
    private ProgressBar progressBarHorizontal;
    private TextView questionText;
    private RadioGroup answersRadioGroup;
    private Button continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        // ViewModel'i başlat
        testViewModel = new ViewModelProvider(this).get(TestViewModel.class);

        // UI Elemanlarını Bağla
        backButton = findViewById(R.id.backButton);
        progressText = findViewById(R.id.progressText);
        progressBarHorizontal = findViewById(R.id.progressBarHorizontal);
        questionText = findViewById(R.id.questionText);
        answersRadioGroup = findViewById(R.id.answersRadioGroup);
        continueButton = findViewById(R.id.continueButton);

        // Tıklama Olayları
        backButton.setOnClickListener(v -> onBackPressed());
        continueButton.setOnClickListener(v -> handleContinueClick());

        loadQuestionsFromLocal();
    }

    private void loadQuestionsFromLocal() {
        questions = testViewModel.getLocalQuestions();
        if (questions != null && !questions.isEmpty()) {
            displayQuestion();
        } else {
            Toast.makeText(this, "Test soruları yüklenirken bir hata oluştu.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void displayQuestion() {
        TestQuestion currentQuestion = questions.get(currentQuestionIndex);
        questionText.setText(currentQuestion.getQuestionText());

        int progress = (int) (((double) (currentQuestionIndex + 1) / questions.size()) * 100);
        progressBarHorizontal.setProgress(progress);
        progressText.setText((currentQuestionIndex + 1) + "/" + questions.size());

        answersRadioGroup.clearCheck();
        answersRadioGroup.removeAllViews();
        List<TestAnswer> answerOptions = currentQuestion.getAnswers();
        for (int i = 0; i < answerOptions.size(); i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(answerOptions.get(i).getAnswerText());
            radioButton.setId(i); // ID olarak index veriyoruz
            radioButton.setTag(answerOptions.get(i).getPoint()); // Puanı tag'de saklıyoruz
            radioButton.setTextColor(ContextCompat.getColor(this, android.R.color.white));
            radioButton.setPadding(20, 20, 20, 20);
            answersRadioGroup.addView(radioButton);
        }

        continueButton.setText(currentQuestionIndex == questions.size() - 1 ? "Testi Bitir" : "Devam Et");
    }

    private void handleContinueClick() {
        int selectedRadioButtonId = answersRadioGroup.getCheckedRadioButtonId();
        if (selectedRadioButtonId == -1) {
            Toast.makeText(this, "Lütfen bir cevap seçin.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cevabı kaydet
        String questionId = questions.get(currentQuestionIndex).getQuestionId();
        RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
        int point = (int) selectedRadioButton.getTag();
        userAnswers.add(new UserAnswer(questionId, point));

        // Sonraki adıma geç
        currentQuestionIndex++;
        if (currentQuestionIndex < questions.size()) {
            displayQuestion();
        } else {
            submitTest();
        }
    }

    private void submitTest() {
        continueButton.setEnabled(false);
        Toast.makeText(this, "Sonuçlar gönderiliyor...", Toast.LENGTH_SHORT).show();

        // SharedPreferences'ten kullanıcı bilgilerini al
        SharedPreferences sharedPreferences = getSharedPreferences("MoodieMoviesPrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("user_token", null);
        String userId = sharedPreferences.getString("user_id", null);

        if (token == null || userId == null) {
            Toast.makeText(this, "Oturum hatası! Lütfen tekrar giriş yapın.", Toast.LENGTH_LONG).show();
            // Kullanıcıyı Login ekranına yönlendir
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return;
        }

        TestSubmissionRequest request = new TestSubmissionRequest(userId, userAnswers);

        testViewModel.submitAnswers(token, request).observe(this, success -> {
            if (success != null && success) {
                Toast.makeText(this, "Test başarıyla tamamlandı! Size özel öneriler hazırlanıyor.", Toast.LENGTH_LONG).show();
                // Test başarılı, ana ekrana geri dön ve belki bir yenileme tetikle
                finish();
            } else {
                Toast.makeText(this, "Test gönderilirken bir hata oluştu. Lütfen tekrar deneyin.", Toast.LENGTH_LONG).show();
                continueButton.setEnabled(true);
            }
        });
    }
}