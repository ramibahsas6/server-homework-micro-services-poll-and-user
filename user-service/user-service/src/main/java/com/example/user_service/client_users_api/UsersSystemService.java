package com.example.user_service.client_users_api;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "clientPollsApi",
        url = "${clientPollsApi.polls.url}"
)
public interface UsersSystemService {
    @DeleteMapping("/delete_answers/{user_id}")
    Boolean deleteAnswers(@PathVariable Long user_id);
}
