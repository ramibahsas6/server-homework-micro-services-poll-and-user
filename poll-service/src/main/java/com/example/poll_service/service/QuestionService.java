package com.example.poll_service.service;

import com.example.poll_service.model.Question;
import com.example.poll_service.repository.AnswerRepository;
import com.example.poll_service.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QuestionService implements AddableQuestionService{

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    /// /////////////////// crud ////////////////////////////////////
    public Question getQuestionById(Integer id){
        return questionRepository.findQuestionById(id);
    }

    public List<Question> getAllQuestions(){
        return questionRepository.getQuestions();
    }

    public int createNewQuestion(Question question){
        if(question.getId() <= 0) // if id NULL (0) or minus than 0
            throw new IllegalArgumentException("can't create the question, the question must be given and not minus than 1");

        if(question.getTitle() == null)
            throw  new IllegalArgumentException("can't create the question, the title can't be null");

        if(question.getOptionA() == null || question.getOptionB() == null || question.getOptionC() == null ||
        question.getOptionD() == null)
            throw  new IllegalArgumentException("can't create the question, no option can be null");



        if( !questionRepository.questionExistsByTheSameValuesHelper(question).isEmpty() )
            throw new IllegalArgumentException("can't create the question, the values of the question is the same");

        return questionRepository.saveQuestion(question);
    }

    public Integer updateQuestion(Question question){

//        if(questionRepository.questionExistsByIdHelper(question.getId()).isEmpty()) // if id is not exist
//            throw new IllegalArgumentException("can't update, there is no question with the id " + question.getId());

        if(question.getTitle() == null || question.getOptionA() == null || question.getOptionB() == null || question.getOptionC() == null ||
                question.getOptionD() == null)
            throw new IllegalArgumentException("can't update, non of the fields can be null");

        if(!questionRepository.questionExistsByIdHelper(question.getId()).isEmpty() &&
                !questionRepository.questionExistsByTheSameValuesHelper(question).isEmpty() )
            throw new IllegalArgumentException("can't update the question, there is another question with the same values");

        return questionRepository.updateQuestion(question);
    }

    public int deleteQuestion(Integer id){
        if( !answerRepository.answerExistsByQuestionIdHelper(id).isEmpty() )
            return -1;

        return questionRepository.deleteQuestion(id);
    }

    /// /////////////// addable question service //////////////////////
    @Override
    public Map<Question, Map<String, Object>> allQuestionsWithCountUsersPerOptions(){

        Question question = new Question();
        List<Question> allQuestions = questionRepository.getQuestions();
        if(allQuestions == null)
            throw new IllegalArgumentException("internal problem with creating questions list");
        int totalResponses;
        Map<Question, Map<String, Object>> all = new HashMap<>();

        int i = 0;
        while (i < allQuestions.size()) {
            question = allQuestions.get(i);
            Map<String, Object> countUsersPerOptions = new HashMap<>();
            Map<Character, Integer> stats = answerRepository.countAnswersByQuestionId(question.getId());
            if(stats == null)
                throw new IllegalArgumentException("internal problem with count answers map");
            totalResponses = stats.values().stream().mapToInt(Integer::intValue).sum();
            countUsersPerOptions.put("question_id", question.getId());
            countUsersPerOptions.put("users_count", stats);
            if(totalResponses == 0)
                countUsersPerOptions.put("status", "NO_ANSWERS_FOUND");
            else
                countUsersPerOptions.put("status", "SUCCESSFULLY_CALCULATED");
            countUsersPerOptions.put("total users answered", totalResponses);
            //usersPerOptions.put("message", "No answers found for this question");
            all.put(question, countUsersPerOptions);

            i++;
        }
        return all;
    }
}
