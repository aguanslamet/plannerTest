package com.production.planner.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ResponseWrapper<T>{
    private int statusCode;
    private String message;
    private T data;

    public ResponseWrapper(int statusCode, String message, T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

}
