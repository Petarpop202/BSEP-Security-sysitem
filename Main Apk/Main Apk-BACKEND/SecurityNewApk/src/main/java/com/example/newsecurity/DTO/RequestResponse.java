package com.example.newsecurity.DTO;

public class RequestResponse {
    private long requestId;
    private boolean response;

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public boolean isResponse(){return response;}
    public void setResponse(boolean response){this.response = response;}
}
