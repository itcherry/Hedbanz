package com.transcendensoft.hedbanz.presentation.game;
/**
 * Copyright 2017. Andrii Chernysh
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.support.annotation.Nullable;
import android.widget.EditText;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.transcendensoft.hedbanz.di.scope.ActivityScope;
import com.transcendensoft.hedbanz.domain.entity.Message;
import com.transcendensoft.hedbanz.domain.entity.MessageType;
import com.transcendensoft.hedbanz.domain.entity.Room;
import com.transcendensoft.hedbanz.domain.entity.User;
import com.transcendensoft.hedbanz.domain.interactor.game.GameInteractorFacade;
import com.transcendensoft.hedbanz.domain.interactor.game.exception.IncorrectJsonException;
import com.transcendensoft.hedbanz.presentation.base.BasePresenter;
import com.transcendensoft.hedbanz.presentation.game.models.TypingMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Implementation of game mode presenter.
 * Here are work with server by sockets and other like
 * processing game algorithm.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
@ActivityScope
public class GamePresenter extends BasePresenter<Room, GameContract.View> implements GameContract.Presenter {
    private GameInteractorFacade mGameInteractor;

    @Inject
    public GamePresenter(GameInteractorFacade gameInteractor) {
        this.mGameInteractor = gameInteractor;
    }

    @Override
    protected void updateView() {
        if (model.getName() == null) {
            initSockets();
        }
    }

    @Override
    public void destroy() {
        mGameInteractor.destroy();
    }

    @Override
    public void initSockets() {
        initSocketSystemListeners();
        initBusinessLogicListeners();

        mGameInteractor.connectSocketToServer(model.getId());

    }

    private void initSocketSystemListeners() {
        mGameInteractor.onConnectListener(
                str -> Timber.i("Socket connected: %s", str),
                this::processEventListenerOnError);
        mGameInteractor.onDisconnectListener(
                str -> Timber.i("Socket disconnected: %s", str),
                this::processEventListenerOnError);
        mGameInteractor.onConnectErrorListener(
                str -> Timber.e("Socket connect ERROR: %s", str),
                this::processEventListenerOnError);
        mGameInteractor.onConnectTimeoutListener(
                str -> Timber.i("Socket connect TIMEOUT: %s", str),
                this::processEventListenerOnError);
    }

    private void initBusinessLogicListeners() {
        mGameInteractor.onJoinedUserListener(
                user -> {
                    List<User> users = model.getUsers();
                    if(!users.contains(user)) {
                        users.add(user);
                    }
                    Message message = new Message.Builder()
                            .setUserFrom(user)
                            .setMessageType(MessageType.JOINED_USER)
                            .build();
                    model.getMessages().add(message);
                    view().addMessage(message);
                },
                this::processEventListenerOnError);
        mGameInteractor.onLeftUserListener(
                user -> {
                    List<User> users = model.getUsers();
                    if(!users.contains(user)) {
                        users.remove(user);
                    }
                    Message message = new Message.Builder()
                            .setUserFrom(user)
                            .setMessageType(MessageType.LEFT_USER)
                            .build();
                    model.getMessages().add(message);
                    view().addMessage(message);
                },
                this::processEventListenerOnError);
        mGameInteractor.onRoomInfoListener(
                room -> {
                    model = room;
                    model.setMessages(new ArrayList<>());
                    initTypingListeners();
                },
                this::processEventListenerOnError);
        mGameInteractor.onMessageReceivedListener(
                this::processSimpleMessage,
                this::processEventListenerOnError);
        mGameInteractor.onErrorListener(error -> {
            Timber.e("Error from server from socket. Code : %d; Message: %s",
                    error.getErrorCode(), error.getErrorMessage());
        }, this::processEventListenerOnError);
    }

    private void initTypingListeners() {
        mGameInteractor.onStartTypingListener(
                this::processStartTyping,
                this::processEventListenerOnError);
        mGameInteractor.onStopTypingListener(
                this::processStopTyping,
                this::processEventListenerOnError);
    }

    private void processSimpleMessage(Message message) {
        Message lastMessage = null;
        List<Message> messages = model.getMessages();

        if (!messages.isEmpty()) {
            lastMessage = messages.get(messages.size() - 1);
        }
        if (lastMessage instanceof TypingMessage) {
            messages.add(messages.size() - 1, message);
            view().addMessage(messages.size() - 1, message);
        } else {
            messages.add(message);
            view().addMessage(message);
        }
    }

    private void processStartTyping(TypingMessage typingMessage) {
        Message lastMessage = getLastMessage(model.getMessages());
        List<Message> messages = model.getMessages();

        if (lastMessage instanceof TypingMessage) {
            ((TypingMessage) lastMessage).addUser(typingMessage.getUserFrom());
            view().setMessage(messages.size() - 1, lastMessage);
        } else {
            messages.add(typingMessage);
            view().addMessage(typingMessage);
        }
    }

    private void processStopTyping(TypingMessage typingMessage) {
        Message lastMessage = getLastMessage(model.getMessages());
        List<Message> messages = model.getMessages();

        if (lastMessage instanceof TypingMessage) {
            TypingMessage typingLastMessage = (TypingMessage) lastMessage;
            typingLastMessage.removeUser(typingMessage.getUserFrom());
            if (typingLastMessage.getTypingUsers().isEmpty()) {
                messages.remove(messages.size() - 1);
                view().removeMessage(messages.size() - 1);
            }
        }
    }

    @Nullable
    private Message getLastMessage(List<Message> messages) {
        Message lastMessage = null;
        if (!messages.isEmpty()) {
            lastMessage = messages.get(messages.size() - 1);
        }
        return lastMessage;
    }

    private void processEventListenerOnError(Throwable err) {
        if (err instanceof IncorrectJsonException) {
            IncorrectJsonException incorrectJsonException = (IncorrectJsonException) err;
            Timber.e("Incorrect JSON from socket listener. JSON: %S; EVENT: %s",
                    incorrectJsonException.getJson(), incorrectJsonException.getMethod());
        }
    }

    @Override
    public void messageTextChanges(EditText editText) {
        addDisposable(
                RxTextView.textChanges(editText)
                        .skip(2)
                        .doOnEach(text -> {
                            mGameInteractor.startTyping();
                        })
                        .debounce(500, TimeUnit.MILLISECONDS)
                        .subscribe(
                                text -> {
                                    mGameInteractor.stopTyping();
                                },
                                err -> {
                                    Timber.e("Error while setting start/stop smile animation. " +
                                            "Message : " + err.getMessage());
                                }));

    }

    @Override
    public void sendMessage(String text) {
        Message message = new Message.Builder()
                .setMessage(text)
                .build();
        mGameInteractor.sendMessage(message);
    }
}
