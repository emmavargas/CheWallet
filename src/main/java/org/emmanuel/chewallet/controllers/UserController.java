package org.emmanuel.chewallet.controllers;

import org.emmanuel.chewallet.dtos.UserDataDto.UpdateAliasRequestDto;
import org.emmanuel.chewallet.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/details")
    public ResponseEntity<?> getUserDetails() {
        var currentUser = userService.getUserInfo();
        return ResponseEntity.ok(currentUser);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateAlias(@RequestBody UpdateAliasRequestDto updateAliasRequestDto) {
        var updatedUser = userService.updateAlias(updateAliasRequestDto);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getUserResume() {
        var currentUser = userService.resumeUser();
        return ResponseEntity.ok(currentUser);
    }


}
