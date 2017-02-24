package tkapps.questionaire;

/**
 * Created by behrends on 16/02/2017.
 */

public class Questions {
    private String question;
    private String answer1;
    private String answer2;
    private String answer3;
    private String answer4;
    private String correct_answer;

    public Questions(String question, String answer1, String answer2, String answer3, String answer4, String correct_answer){
        this.question = question;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
        this.correct_answer = correct_answer;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer1() {
        return answer1;
    }
    public String getAnswer2() {
        return answer2;
    }
    public String getAnswer3() {
        return answer3;
    }
    public String getAnswer4() {
        return answer4;
    }

    public String getCorrect_answer() {
        return correct_answer;
    }
}