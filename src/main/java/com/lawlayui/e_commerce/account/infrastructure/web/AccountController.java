package com.lawlayui.e_commerce.account.infrastructure.web;

import com.lawlayui.e_commerce.account.application.services.command.UpdateUserProfileUseCaseImpl;
import com.lawlayui.e_commerce.account.application.services.query.GetAllUserUseCaseImpl;
import com.lawlayui.e_commerce.account.application.services.query.GetUserDetailUseCaseImpl;
import com.lawlayui.e_commerce.account.application.services.query.GetUserProfileByIdUseCaseImpl;
import com.lawlayui.e_commerce.account.application.services.query.GetUserProfileByUserProfileIdAndUserIdUseCaseImpl;
import com.lawlayui.e_commerce.account.infrastructure.dto.UpdateUserProfileRequest;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lawlayui.e_commerce.account.application.port.in.command.UpdateUserProfileCommand;
import com.lawlayui.e_commerce.account.application.port.in.dto.UserDto;
import com.lawlayui.e_commerce.account.application.port.in.dto.UserProfileDto;
import com.lawlayui.e_commerce.account.application.port.in.query.GetAllUserQuery;
import com.lawlayui.e_commerce.account.application.services.command.RegisterAsSellerUseCaseImpl;

@RestController 
@RequestMapping("/api/v1/accounts")
public class AccountController {
    public AccountController(RegisterAsSellerUseCaseImpl registerAsSellerUseCaseImpl,
            UpdateUserProfileUseCaseImpl updateUserProfileUseCaseImpl,
            GetUserProfileByUserProfileIdAndUserIdUseCaseImpl getUserProfileByUserProfileIdAndUserIdUseCaseImpl,
            GetAllUserUseCaseImpl getAllUserUseCaseImpl, GetUserDetailUseCaseImpl getUserDetailUseCaseImpl,
            GetUserProfileByIdUseCaseImpl getUserProfileByIdAndUserIdUseCaseImpl) {
        this.registerAsSellerUseCaseImpl = registerAsSellerUseCaseImpl;
        this.updateUserProfileUseCaseImpl = updateUserProfileUseCaseImpl;
        this.getUserProfileByUserProfileIdAndUserIdUseCaseImpl = getUserProfileByUserProfileIdAndUserIdUseCaseImpl;
        this.getAllUserUseCaseImpl = getAllUserUseCaseImpl;
        this.getUserDetailUseCaseImpl = getUserDetailUseCaseImpl;
        this.getUserProfileByIdAndUserIdUseCaseImpl = getUserProfileByIdAndUserIdUseCaseImpl;
    }

    private RegisterAsSellerUseCaseImpl registerAsSellerUseCaseImpl;
    private UpdateUserProfileUseCaseImpl updateUserProfileUseCaseImpl;

    private GetUserProfileByUserProfileIdAndUserIdUseCaseImpl getUserProfileByUserProfileIdAndUserIdUseCaseImpl;
    private GetAllUserUseCaseImpl getAllUserUseCaseImpl;
    private GetUserDetailUseCaseImpl getUserDetailUseCaseImpl;
    private GetUserProfileByIdUseCaseImpl getUserProfileByIdAndUserIdUseCaseImpl;

    @PostMapping("/{accountId}/register-as-seller")
    public void registerAsSellerAccount(@PathVariable String accountId) { 
        registerAsSellerUseCaseImpl.registerAsSeller(accountId);
    }

    @PatchMapping("/{accountId}/profiles/{profileId}")
    public void updateProfile(@PathVariable String accountId, @PathVariable String profileId, @Valid @RequestBody UpdateUserProfileRequest request) {
        updateUserProfileUseCaseImpl.updateUserProfile(new UpdateUserProfileCommand(profileId, accountId, request.fullName(), request.phoneNumber(), request.address()));
    }

    @GetMapping("/{accountId}/profiles/{profileId}")
    public ResponseEntity<UserProfileDto> getUserProfile(@PathVariable String accountId, @PathVariable String profileId) {
        UserProfileDto userProfileDto = getUserProfileByUserProfileIdAndUserIdUseCaseImpl.getByUserProfileIdAndUserId(profileId, accountId);
        return ResponseEntity.ok(userProfileDto);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUser(@RequestParam int page, @RequestParam int size) {
        List<UserDto> userProfiles = getAllUserUseCaseImpl.getAll(new GetAllUserQuery(page, size));
        return ResponseEntity.ok(userProfiles);
    }

    @GetMapping
    public ResponseEntity<UserDto> getUserDetail(@RequestParam String accountId) {
        UserDto userDto = getUserDetailUseCaseImpl.getUserDetail(accountId);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping 
    public ResponseEntity<UserProfileDto> getUserProfileById(@RequestParam String profileId) {
        UserProfileDto userProfileDto = getUserProfileByIdAndUserIdUseCaseImpl.getById(profileId);
        return ResponseEntity.ok(userProfileDto);
    }
}