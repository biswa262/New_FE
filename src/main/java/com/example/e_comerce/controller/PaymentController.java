package com.example.e_comerce.controller;

import com.example.e_comerce.exception.OrderException;
import com.example.e_comerce.model.Order;
import com.example.e_comerce.repository.OrderRepository;
import com.example.e_comerce.response.ApiResponse;
import com.example.e_comerce.service.OrderService;
import com.example.e_comerce.service.UserService;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import netscape.javascript.JSObject;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PaymentController {
    @Value("${razorpay.api.key}")
    String apiKey;

    @Value("${razorpay.api.secret}")
    String apiSecret;
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderRepository orderRepository;

    @PostMapping("/payments/{orderId}")
    public ResponseEntity<PaymenyLinkResponse> createPaymentLink(@PathVariable Long orderId, @RequestHeader("Authorization") String jwt) throws OrderException, RazorpayException {
        Order order=orderService.findOrderById(orderId);
        try {
            RazorpayClient razorpay=new RazorpayClient(apiKey, apiSecret);

            JSONObject paymentLinkRequest=new JSONObject();
            paymentLinkRequest.put("amount",order.getTotalPrice()*100);
            paymentLinkRequest.put("currency","INR");

            JSONObject customer=new JSONObject();
            customer.put("name",order.getUser().getFirst_name());
            customer.put("email",order.getUser().getEmail());
            paymentLinkRequest.put("customer",customer);

            JSONObject notify=new JSONObject();
            notify.put("sms",true);
            notify.put("email",true);
            paymentLinkRequest.put("notify",notify);

            paymentLinkRequest.put("callback_url","http://localhost:3000/payment/"+orderId);
            paymentLinkRequest.put("callback_method","get");

            PaymentLink payment=razorpay.paymentLink.create(paymentLinkRequest);

            String paymentLinkId=payment.get("id");
            String paymentLinkUrl=payment.get("short_url");

            PaymenyLinkResponse res=new PaymenyLinkResponse();
            res.setPayment_link_id(paymentLinkId);
            res.setPayment_link_url(paymentLinkUrl);

            return  new ResponseEntity<PaymenyLinkResponse>(res, HttpStatus.CREATED);

        } catch (RazorpayException e) {
            throw new RazorpayException(e.getMessage());
        }

    }

    public ResponseEntity<ApiResponse>redirect(@RequestParam(name = "payment_id")String paymentId,@RequestParam(name="order_id")Long orderId) throws OrderException, RazorpayException {
        Order order=orderService.findOrderById(orderId);
        RazorpayClient razorpay=new RazorpayClient(apiKey,apiSecret);
        try {
            Payment payment=razorpay.payments.fetch(paymentId);
            if(payment.get("status").equals("captured")){
                order.getPaymentDetails().setRazorpayPaymentId(paymentId);
                order.getPaymentDetails().setStatus("COMPLETED");
                order.setOrderStatus("PLACED");
                orderRepository.save(order);
            }
            ApiResponse response=new ApiResponse();
            response.setMessage("Your order has been captured");
            response.setStatus(true);

            return  new ResponseEntity<ApiResponse>(response,HttpStatus.ACCEPTED);
        } catch (Exception e) {
            throw new RazorpayException(e.getMessage());
        }
    }
}
