package com.lawlayui.e_commerce.account.application.port.in.command;

public class UpdateUserProfileCommand {
    private String userProfileId;
    private String userId;
    private String fullName; 
    private String phoneNumber; 
    private String address; 

    public UpdateUserProfileCommand(String userProfileId, String userId, String fullName, String phoneNumber, String address) {
        this.userProfileId = userProfileId;
        this.userId = userId;
        this.fullName = fullName; 
        this.phoneNumber = phoneNumber; 
        this.address = address; 
    }

    public void setUserProfileId(String userProfileId) {this.userProfileId = userProfileId;}
    public String getUserProfileId() {return userProfileId;}
    public void setUserId(String userId) {this.userId = userId;}
    public String getUserId() {return userId;}
    public void setFullName(String fullName) {this.fullName = fullName;}
    public String getFullName() {return fullName;}
    public void setPhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber;}
    public String getPhoneNumber() {return phoneNumber;}
    public void setAddress(String address) {this.address = address;}
    public String getAddress() {return address;}
}