package amnil.tm.controller;

import amnil.tm.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PublicController {

    @RequestMapping
    public ResponseEntity<ApiResponse> index() {
        return ResponseEntity.ok().body(new ApiResponse("Message", "Home Page"));
    }

}
