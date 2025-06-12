package com.moodiemovies.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.moodiemovies.data.LocalTestDataSource;
import com.moodiemovies.model.TestQuestion;
import com.moodiemovies.model.TestSubmissionRequest;
import com.moodiemovies.repository.TestRepository;

import java.util.List;

public class TestViewModel extends ViewModel {

    private TestRepository testRepository;

    public TestViewModel() {
        this.testRepository = TestRepository.getInstance();
    }

    public List<TestQuestion> getLocalQuestions() {
        return LocalTestDataSource.getQuestions();
    }

    public LiveData<Boolean> submitAnswers(String token, TestSubmissionRequest request) {
        return testRepository.submitAnswers(token, request);
    }
}