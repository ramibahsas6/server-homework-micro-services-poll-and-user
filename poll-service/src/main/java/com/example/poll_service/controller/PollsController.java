package com.example.poll_service.controller;

import com.example.poll_service.model.AnswerOfUser;
import com.example.poll_service.model.Question;
import com.example.poll_service.service.AnswerService;
import com.example.poll_service.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/polls")
public class PollsController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private AnswerService answerService;

    /// /////////////// questions //////////////////////////////////
    @GetMapping("/questions/{id}")
    public ResponseEntity<?> getQuestion(@PathVariable Integer id)
    {
        try{
             Question question = questionService.getQuestionById(id);

            return new ResponseEntity<>(question, HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("the question with id " + id + " is not found");
        }
    }

    @GetMapping("questions/all")
    public ResponseEntity<?> getAllQuestions(){
        List<Question> questions = questionService.getAllQuestions();

        if(questions.isEmpty())
            return ResponseEntity.status(HttpStatus.OK).body("questions table is empty");

        return new ResponseEntity<>(questions, HttpStatus.OK);
    }

    @PostMapping("/questions/create")
    public ResponseEntity<String> createNewQuestion(@RequestBody Question question){
        try {
            int rows = questionService.createNewQuestion(question);

            if(rows > 0)
                return ResponseEntity.status(HttpStatus.CREATED).body("the question created successfully");
            else
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("the question can't be created (SERVER_ERROR)");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/questions/{id}")
    public ResponseEntity<String> updateQuestion(@PathVariable Integer id, @RequestBody Question question){
        question.setId(id);
        try {
            int rows = questionService.updateQuestion(question);

            if(rows > 0)
                return ResponseEntity.ok("the question with id " + question.getId() + " updated successfully");
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("can't update, the question with id " + question.getId() + " is not found");
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/questions/{id}")
    public ResponseEntity<String> deleteQuestion(@PathVariable Integer id){
        int rows = questionService.deleteQuestion(id);

        if(rows > 0)
            return ResponseEntity.ok("the question with id " + id + " deleted successfully");
        else if (rows == 0)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("can't delete, question with id " + id + " because it not found");
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("can't delete, they are some users that answered that question");
    }

    /// //////////////////// answers ////////////////////////////////////////////
    @GetMapping("/answers/{id}")
    public ResponseEntity<?> getAnswerById(@PathVariable Integer id)
    {
        try{
            AnswerOfUser answer = answerService.getAnswerById(id);

            return new ResponseEntity<>(answer, HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("the answer with id " + id + " is not found");
        }
    }

    @GetMapping("/answers/all")
    public ResponseEntity<?> getAllAnswers(){
        List<AnswerOfUser> answers = answerService.getAllAnswers();

        if(answers.isEmpty())
            return ResponseEntity.status(HttpStatus.OK).body("answers table is empty");

        return new ResponseEntity<>(answers, HttpStatus.OK);
    }

    @PostMapping("/answers/create")
    public ResponseEntity<String> createNewAnswer(@RequestBody AnswerOfUser answer){
        try {
            int rows = answerService.createNewAnswer(answer);

            if(rows > 0)
                return ResponseEntity.status(HttpStatus.CREATED).body("the answer created successfully");
            else
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("the answer can't be created (SERVER_ERROR)");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/answers/{id}")
    public ResponseEntity<String> updateAnswer(@PathVariable Integer id, @RequestBody AnswerOfUser answer){
        answer.setId(id);
        try {
            int rows = answerService.updateAnswer(answer);

            if(rows > 0)
                return ResponseEntity.ok("the answer with id " + answer.getId() + " updated successfully");
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("can't update, the answer with id " + answer.getId() + " is not found");
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/answers/{id}")
    public ResponseEntity<String> deleteAnswer(@PathVariable Integer id){
        int rows = answerService.deleteAnswer(id);

        if(rows > 0)
            return ResponseEntity.ok("the answer with id " + id + " deleted successfully");
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("can't delete, question with id " + id + " because it not found");
    }

    @DeleteMapping("/delete_answers/{user_id}")
    public Boolean deleteAnswersPerUser(@PathVariable Long user_id)
    {
        Integer rows = answerService.deleteAnswersPerUser(user_id);

        return rows > 0;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    @GetMapping("/answers/users-selected-each-of-question-options")
    public ResponseEntity<Map<String, Object>> getUsersNumbersPerQuestionOptions(@RequestParam(value = "question_id") int questionId) {
        Map<String, Object> result = answerService.getUsersNumbersPerQuestionOptions(questionId);
        return buildResponse(result);
    }

    @GetMapping("/answers/total-users-answered-the-question")
    public ResponseEntity<Map<String, Object>> getTotalUsersAnsweredQuestion(@RequestParam(value = "question_id") Integer questionId) {
        Map<String, Object> result = answerService.getTotalUsersAnsweredQuestion(questionId);
        return buildResponse(result);
    }

    // פונקציית עזר אחידה לבניית ResponseEntity
    private ResponseEntity<Map<String, Object>> buildResponse(Map<String, Object> result) {
        String status = (String) result.get("status");
        switch (status) {
            case "QUESTION_NOT_FOUND":
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
            case "NO_ANSWERS_FOUND":
                return ResponseEntity.status(HttpStatus.OK).body(result);
            case "SUCCESSFULLY_CALCULATED":
                return ResponseEntity.ok(result);
            default:
                result.put("message", "Unexpected error");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////

    @GetMapping("/answers/answer-each-question-user-submitted ")
    public ResponseEntity<Map<String, Object>> getAnswersByUser(@RequestParam("user_id") Long userId) {
        Map<String, Object> result = answerService.getUserAnswers(userId);

        String status = (String) result.get("status");
        if ("NO_ANSWERS_FOUND".equals(status))
            return ResponseEntity.status(HttpStatus.OK).body(result);
        else if ("SUCCESSFULLY_RESULTS".equals(status))
            return ResponseEntity.ok(result);
        else if("USER_NOT_FOUND".equals(status))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);

        result.put("message", "Unexpected error");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    @GetMapping("/answers/questions-user-answered-to") // הפונקציה מחזירה את סכום השאלות השונות עבור המשתמש אבל מכיוון שדאגתי שלא יהיה שכפול תשובות על אותה השאלות לכל משתמש ,מקרה שכפול זה לא יקרה
    public ResponseEntity<String> getQuestionCount(@RequestParam("user_id") Long userId) {
        int count = answerService.getQuestionCountForUser(userId);

        if(count == -1)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("the user is not exist");

        if(count == 0)
            return ResponseEntity.status(HttpStatus.OK).body(String.format("the user with the id : %d, didn't answer any question", userId));

        String summary = String.format("the user with the id : %d, answered %d questions", userId, count);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/answers/questions-and-users-choose-each-of-question-options")
    public ResponseEntity<Map<Question, Map<String, Object>>> getAll(){
        try {
            Map<Question, Map<String, Object>> all = questionService.allQuestionsWithCountUsersPerOptions();
            return ResponseEntity.ok().body(all);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // it is very very small case maybe never happened so i didn't want to change the return from ResponseEntity<Map<Question, Map<String, Object>>> for Map<?>
            // that's why i chose to return null for that small case "for now"
        }

    }
}
