package com.wei.spring_boot_practice.controller;

import com.wei.spring_boot_practice.entity.SendMailRequest;
import com.wei.spring_boot_practice.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


//if there is any problem please check that javax.mail and javax.mail-api have the same version

@Validated
@RestController

@RequestMapping(value = "/mail")
public class MailController {


    @Autowired
    private MailService mailService;

    @PostMapping
    public ResponseEntity<Void> sendMail(@Valid @RequestBody SendMailRequest request) {
        mailService.sendMail(request);
        return ResponseEntity.noContent().build();
    }

}
