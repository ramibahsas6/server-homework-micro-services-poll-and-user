package com.example.poll_service.service;

import com.example.poll_service.client_polls_api.PollsSystemService;
import com.example.poll_service.model.AnswerOfUser;
import com.example.poll_service.repository.AnswerRepository;
import com.example.poll_service.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnswerService implements AddableAnswerService{

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private PollsSystemService pollsSystemService;

    private static Integer totalResponses;

    public AnswerOfUser getAnswerById(Integer id){
        return answerRepository.findAnswerById(id);
    }

    public List<AnswerOfUser> getAllAnswers(){
        return answerRepository.getAnswers();
    }

    /// ////////////// crud //////////////////////////////
    public int createNewAnswer(AnswerOfUser answer){
        if(answer.getId() <= 0) // if id NULL (0) or minus than 0
            throw new IllegalArgumentException("can't create the answer, the answer id must be given and not minus than 1");

        if(answer.getSelectedOption() < 'A' || answer.getSelectedOption() > 'D')
            throw new IllegalArgumentException("can't create the answer, the selected option must be only from A to D");

        if( !answerRepository.answerExistsByIdHelper(answer.getId()).isEmpty() ) // if the id already exist (unique id)
            throw new IllegalArgumentException("can't create the answer, the id : " + answer.getId() + " is already exist");

        if(questionRepository.questionExistsByIdHelper(answer.getQuestionId()).isEmpty()) // if question_id is not exist
            throw new IllegalArgumentException("can't create the answer, there is no question with the id " + answer.getQuestionId());

        if(!pollsSystemService.isRegisterByUserId(answer.getUserId())) // if user_id is not registered (exists)
            throw new IllegalArgumentException("can't create the answer, there is no user with the id " + answer.getUserId());

        if(!answerRepository.existsTheSameUserIdWithTheSameQuestionHelper(answer.getId(), answer.getUserId(), answer.getQuestionId()).isEmpty())
            throw new IllegalArgumentException("can't create the answer, the same user can't answer the same question twice - " +
                    "there is another row with the same user answered that question)");

        return answerRepository.saveAnswer(answer);
    }

    public Integer updateAnswer(AnswerOfUser answer){

        if(answer.getSelectedOption() < 'A' || answer.getSelectedOption() > 'D') // if selected option not exist
            throw new IllegalArgumentException("can't update the answer, there is no selected option " + answer.getSelectedOption() +" like that in the questions");

        if(answerRepository.answerExistsByIdHelper(answer.getId()).isEmpty()) // if id is not exist
            throw new IllegalArgumentException("can't update the answer, this id " + answer.getId() + " is not exist");

        if(questionRepository.questionExistsByIdHelper(answer.getQuestionId()).isEmpty()) // if question_id is not exist
            throw new IllegalArgumentException("can't update the answer, there is no question with the id " + answer.getQuestionId());

        if(!pollsSystemService.isRegisterByUserId(answer.getUserId())) // if user_id is not registered (exists)
            throw new IllegalArgumentException("can't update the answer, there is no user with the id " + answer.getUserId());

        if(!answerRepository.answerExistsByTheSameQuestionIdAndUserIdAtAnotherRowHelper(answer.getId(), answer.getQuestionId(), answer.getUserId()).isEmpty())
            throw new IllegalArgumentException("can't update the answer, the same user can't answer the same question twice - " +
                    "there is another row with the same user answered that question you can update that another row (id) or delete it first for allow you to update your requested row (id)");

        return answerRepository.updateAnswer(answer);
    }

    public int deleteAnswer(Integer id){
        return questionRepository.deleteQuestion(id);
    }

    /// /////////// from internal api
    public Integer deleteAnswersPerUser(Long user_id){
        return answerRepository.deleteAnswersPerUser(user_id);
    }

    /// /////////////// addable answer service ///////////////////
    @Override
    public Map<String, Object> getUsersNumbersPerQuestionOptions(Integer questionId) {
        Map<String, Object> usersPerOptions = new HashMap<>();

        //  אם השאלה קיימת
        if (!answerRepository.questionExists(questionId)) {
            usersPerOptions.put("status", "QUESTION_NOT_FOUND");
            //usersPerOptions.put("message", "Question not found");
            return usersPerOptions;
        }

        //  שלוף את הנתונים
        Map<Character, Integer> stats = answerRepository.countAnswersByQuestionId(questionId);

        int totalResponses = stats.values().stream().mapToInt(Integer::intValue).sum();
        AnswerService.totalResponses = totalResponses;

        //  אם אין תשובות
        if (totalResponses == 0) {
            usersPerOptions.put("question_id", questionId);
            usersPerOptions.put("users_count", stats);
            usersPerOptions.put("status", "NO_ANSWERS_FOUND");
            //usersPerOptions.put("totalResponses", 0);
            //usersPerOptions.put("message", "No answers found for this question");
            return usersPerOptions;
        }

        //  אם הכול תקין
        usersPerOptions.put("question_id", questionId);
        usersPerOptions.put("users_count", stats);
        usersPerOptions.put("status", "SUCCESSFULLY_CALCULATED");
        //usersPerOptions.put("totalResponses", totalResponses);
        //usersPerOptions.put("message", "calculated successfully");

        return usersPerOptions;
    }

    /**
      פונקציה חדשה:
     * מחזירה רק את מספר המשתמשים הכולל שענו על השאלה (סכום כל האפשרויות)
     * משתמשת באותה לוגיקה של getUsersNumbersPerQuestionOptions
     */
    @Override
    public Map<String, Object> getTotalUsersAnsweredQuestion(Integer questionId) { // it is only uses getUsersNumbersPerQuestionOptions
        // and the private static field totalResponses - all that because we didn't want to copy values just create a new values and not do double code

            Map<String, Object> statsData = getUsersNumbersPerQuestionOptions(questionId);

            String status = (String) statsData.get("status");

            // אם השאלה לא קיימת — מחזיר את אותה תשובה
            if ("QUESTION_NOT_FOUND".equals(status)) {
                return statsData;
            }

            // אם אין תשובות — מחזיר תשובה תואמת
            if ("NO_ANSWERS_FOUND".equals(status)) {
                Map<String, Object> result = new HashMap<>();
                result.put("question_id", questionId);
                result.put("total_users_answered", 0);
                result.put("status", "NO_ANSWERS_FOUND");
                return result;
            }

            // אם הכול תקין — מחזיר את מספר המשתמשים שענו
            int totalResponses = AnswerService.totalResponses;

            Map<String, Object> result = new HashMap<>();
            result.put("question_id", questionId);
            result.put("total_users_answered", totalResponses);
            result.put("status", "SUCCESSFULLY_CALCULATED");

            return result;
        }

    @Override
    public Map<String, Object> getUserAnswers(Long userId) {
        List<Map<String, Object>> answers = answerRepository.getAnswersByUserId(userId);
        Map<String, Object> response = new HashMap<>();

        if(!pollsSystemService.isRegisterByUserId(userId)){
            response.put("status", "USER_NOT_FOUND");
            return response;
        }

        response.put("user_id", userId);
        response.put("answers", answers);
        response.put("status", answers.isEmpty() ? "NO_ANSWERS_FOUND" : "SUCCESSFULLY_RESULTS");

        return response;
    }

    @Override
    public int getQuestionCountForUser(Long userId) {
        if(!pollsSystemService.isRegisterByUserId(userId)){
            return -1;
        }

        return answerRepository.countQuestionsByUserId(userId);
    }
}
