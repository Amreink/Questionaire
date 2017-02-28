package tkapps.questionaire;

public class Answer {
    private String answer;
    private boolean correct;

    public Answer(String answer, boolean correct){
        this.answer = answer;
        this.correct = correct;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public boolean isCorrect(){
        return this.correct;
    }

    @Override
    public String toString(){
        return this.answer;
    }
}