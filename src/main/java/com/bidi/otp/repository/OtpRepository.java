package com.bidi.otp.repository;

import com.bidi.otp.entity.OtpEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<OtpEntity,Long> {
   OtpEntity findByOtpCode (String otpCode);
}
