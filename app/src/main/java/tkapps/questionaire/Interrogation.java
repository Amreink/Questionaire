package tkapps.questionaire;

public class Interrogation {
    private String question;
    private Answer[] answers = new Answer[4];

    public Interrogation(String question, Answer answer1, Answer answer2, Answer answer3, Answer answer4){
        this.question = question;
        this.answers[0] = answer1;
        this.answers[1] = answer2;
        this.answers[2] = answer3;
        this.answers[3] = answer4;
    }

    public boolean checkAnswer(Answer answer){
        return answer.isCorrect();
    }

    public String getQuestion() {
        return question;
    }

    public Answer[] getAnswers(){
        return this.answers;
    }

    public Answer getAnswer(int position){
        return this.answers[position];
    }
}
