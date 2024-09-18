package com.group4.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.group4.user.data.model.User;
import com.group4.user.dto.UserDTO;
import com.group4.user.dto.UserSaveDTO;
import com.group4.user.dto.UserUpdateDTO;
import com.group4.user.services.UserService;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@Validated
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // [Admin] Get all users - Paginated.
    // [GET] /
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<User>> getAllUsers(Pageable pageable) {
        Page<User> users = userService.getAllUsers(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    // [-] Create a new user, For signup purpose
    // [POST] /signup
    @PostMapping("/signup")
    public Mono<ResponseEntity<Map<String, String>>> signup(@RequestBody @Valid UserSaveDTO userSaveDTO) {
        return userService.signup(userSaveDTO);
    }

    // [Customer, Admin] Update an existing user.
    // [PUT] /:id
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<UserDTO> updateUser(@PathVariable String id, @RequestBody @Valid UserUpdateDTO userUpdateDTO) {
        UserDTO updatedUser = userService.updateUser(id, userUpdateDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }

    // [Admin] Update user status.
    // [PATCH] /:id/status
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> updateUserStatus(@PathVariable String id, @RequestParam String status) {
        UserDTO userDTO = userService.updateUserStatus(id, status);
        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }

    // [Customer, Admin] Update user password.
    // [PATCH] /:id/password
    @PatchMapping("/{id}/password")
    public ResponseEntity<UserDTO> updatePassword(@PathVariable String id, @RequestBody String newPassword) {
        UserDTO updatedUser = userService.updatePassword(id, newPassword);
        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }
}
