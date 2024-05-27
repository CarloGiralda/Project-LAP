package com.example.demo.model;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.ArrayList;

@Getter
@Setter
@Slf4j
public class Block implements Serializable {

    private String hash;
    private String previousHash;
    private ArrayList<Payment> data;
    private long timeStamp;
    private long nonce;


}
