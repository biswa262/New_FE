package com.example.e_comerce.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class PaymenyLinkResponse {
    private  String payment_link_id;
    private  String payment_link_url;
}
