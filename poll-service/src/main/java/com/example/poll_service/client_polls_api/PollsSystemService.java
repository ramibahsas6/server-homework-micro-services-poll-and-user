package com.example.poll_service.client_polls_api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "clientUsersApi",
        url = "${clientUsersApi.users.url}"
)
public interface PollsSystemService {
@GetMapping()
    Boolean isRegisterByUserId(@RequestParam Long user_id);
}
