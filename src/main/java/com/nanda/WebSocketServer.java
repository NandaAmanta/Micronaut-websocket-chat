/*
 * Copyright 2022 ASUS.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nanda;

import io.micronaut.websocket.WebSocketBroadcaster;
import io.micronaut.websocket.WebSocketSession;
import io.micronaut.websocket.annotation.OnClose;
import io.micronaut.websocket.annotation.OnMessage;
import io.micronaut.websocket.annotation.OnOpen;
import io.micronaut.websocket.annotation.ServerWebSocket;
import java.util.function.Predicate;

/**
 *
 * @author ASUS
 */
@ServerWebSocket("/chat/{topic}/{username}")
public class WebSocketServer {

    private final WebSocketBroadcaster broadcaster;

    public WebSocketServer(WebSocketBroadcaster broadcaster) {
        this.broadcaster = broadcaster;
    }

    @OnOpen
    public void onOpen(String topic, String username, WebSocketSession session) {
        String msg = username + " Joined Ws!";
        broadcaster.broadcastSync(msg, isValid(topic));
    }

    @OnMessage
    public void onMessage(String topic, String username, String message, WebSocketSession session) {
        String msg = username + " : " + message;
        broadcaster.broadcastSync(msg, isValid(topic));
    }

    @OnClose
    public void onClose(String topic, String username, WebSocketSession session) {
        String msg = username + " has leaved ws !";
        broadcaster.broadcastSync(msg, isValid(topic));
    }

    private Predicate<WebSocketSession> isValid(String topic) {
        return s -> topic.equalsIgnoreCase(s.getUriVariables().get("topic", String.class, null));
    }
}
