package com.moodiemovies.data;

import com.moodiemovies.model.TestAnswer;
import com.moodiemovies.model.TestQuestion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LocalTestDataSource {

    /**
     * Uygulama içinde sabit olarak tutulan kişilik testi sorularını döndürür.
     * Bu yapı, React projenizdeki soru listesi mantığıyla aynıdır.
     * @return TestQuestion listesi.
     */
    public static List<TestQuestion> getQuestions() {
        List<TestQuestion> questions = new ArrayList<>();

        // Soru 1
        questions.add(new TestQuestion(
                "q1",
                "Genellikle yeni deneyimlere ve maceralara açık mıyımdır?",
                Arrays.asList(
                        new TestAnswer("a1_1", "Kesinlikle Katılıyorum", 5),
                        new TestAnswer("a1_2", "Katılıyorum", 4),
                        new TestAnswer("a1_3", "Kararsızım", 3),
                        new TestAnswer("a1_4", "Katılmıyorum", 2),
                        new TestAnswer("a1_5", "Kesinlikle Katılmıyorum", 1)
                )
        ));

        // Soru 2
        questions.add(new TestQuestion(
                "q2",
                "Sosyal etkinliklerde ve kalabalık ortamlarda enerjik hisseder miyim?",
                Arrays.asList(
                        new TestAnswer("a2_1", "Kesinlikle Katılıyorum", 5),
                        new TestAnswer("a2_2", "Katılıyorum", 4),
                        new TestAnswer("a2_3", "Kararsızım", 3),
                        new TestAnswer("a2_4", "Katılmıyorum", 2),
                        new TestAnswer("a2_5", "Kesinlikle Katılmıyorum", 1)
                )
        ));

        // Soru 3
        questions.add(new TestQuestion(
                "q3",
                "Başkalarına karşı düşünceli ve nazik davranmaya özen gösterir miyim?",
                Arrays.asList(
                        new TestAnswer("a3_1", "Kesinlikle Katılıyorum", 5),
                        new TestAnswer("a3_2", "Katılıyorum", 4),
                        new TestAnswer("a3_3", "Kararsızım", 3),
                        new TestAnswer("a3_4", "Katılmıyorum", 2),
                        new TestAnswer("a3_5", "Kesinlikle Katılmıyorum", 1)
                )
        ));

        // Soru 4
        questions.add(new TestQuestion(
                "q4",
                "Planlı ve düzenli olmayı mı, yoksa anı yaşamayı mı tercih ederim?",
                Arrays.asList(
                        new TestAnswer("a4_1", "Kesinlikle Planlı", 5),
                        new TestAnswer("a4_2", "Genellikle Planlı", 4),
                        new TestAnswer("a4_3", "Dengeli", 3),
                        new TestAnswer("a4_4", "Genellikle Anı Yaşarım", 2),
                        new TestAnswer("a4_5", "Kesinlikle Anı Yaşarım", 1)
                )
        ));

        // Soru 5
        questions.add(new TestQuestion(
                "q5",
                "Stresli durumlarda sakinliğimi koruyabilir miyim?",
                Arrays.asList(
                        new TestAnswer("a5_1", "Kesinlikle Evet", 5),
                        new TestAnswer("a5_2", "Genellikle Evet", 4),
                        new TestAnswer("a5_3", "Bazen", 3),
                        new TestAnswer("a5_4", "Nadiren", 2),
                        new TestAnswer("a5_5", "Hayır, Kolayca Endişelenirim", 1)
                )
        ));

        // TODO: Diğer 5 soruyu da buraya aynı formatta ekleyin. Toplam 10 soru olacak.

        return questions;
    }
}