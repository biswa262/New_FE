package com.example.e_comerce.model;

import com.example.e_comerce.user.domain.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDetails {
//    private String paymentMethord;
    private String status;
    private String paymentId;
    private String razorpayPaymentLinkId;
    private String razorpayPaymentLinkStatus;
    private String razorpayPaymentLinkReferenceId;
    private String razorpayPaymentId;


}
