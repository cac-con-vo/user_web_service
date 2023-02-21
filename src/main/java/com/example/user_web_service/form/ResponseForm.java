package com.example.user_web_service.form;

import java.util.HashMap;
import java.util.Map;

public class ResponseForm <T> {
    private T data;

    private Map<String, String> response = new HashMap<>();

    public Map<String, String> getResponse() {
        return response;
    }

    public void setResponse(Map<String, String> response) {
        this.response = response;
    }

    public ResponseForm(String message, Boolean result) {
        super();
        setMessage(message);
        setResult(result);
    }

    public ResponseForm() {
        super();
    }

    public void setResult(Boolean result) {
        response.put("result", result.toString());
    }

    public void setMessage(String message) {
        response.put("message", message);
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
