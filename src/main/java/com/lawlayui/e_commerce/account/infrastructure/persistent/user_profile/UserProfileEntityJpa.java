package com.lawlayui.e_commerce.account.infrastructure.persistent.user_profile;

import java.time.LocalDateTime;

import com.lawlayui.e_commerce.account.infrastructure.persistent.user.UserEntityJpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="user_profiles")
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
@Setter 
@Getter 
public class UserProfileEntityJpa {
    @Id 
    @Column(name = "id")
    private String profileId;

    @ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @JoinColumn(name="user_id")
    private  UserEntityJpa user;

    @Column(name = "full_name", nullable = false, length = 255)
    private String fullName;

    @Column(name = "phone_number", nullable = false, length = 255)
    private String phoneNumber;

    @Column(name = "address", nullable = false, length = 500)
    private String address; 

    @Column(name = "updated_at", nullable = true)
    private LocalDateTime updatedAt;
}
