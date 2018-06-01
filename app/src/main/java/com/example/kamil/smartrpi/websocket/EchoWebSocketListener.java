package com.example.kamil.smartrpi.websocket;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;


public final class EchoWebSocketListener extends WebSocketListener {
    private static final int NORMAL_CLOSURE_STATUS = 1000;
    private WebSocket websocket;
    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        System.out.println("WebSocket onOpen");
    }
    @Override
    public void onMessage(WebSocket webSocket, String text) {
        System.out.println(text);
    }
    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        System.out.println(bytes.hex());
    }
    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        webSocket.close(NORMAL_CLOSURE_STATUS, null);
    }
    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {}

}