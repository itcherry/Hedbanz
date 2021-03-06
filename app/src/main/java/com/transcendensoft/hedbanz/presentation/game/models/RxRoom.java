package com.transcendensoft.hedbanz.presentation.game.models;
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

import com.transcendensoft.hedbanz.data.prefs.PreferenceManager;
import com.transcendensoft.hedbanz.domain.entity.Room;
import com.transcendensoft.hedbanz.domain.entity.User;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.subjects.PublishSubject;

/**
 * Common entity for game activity and menu fragment to show
 * room info reactively
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
public class RxRoom {
    private Room mRoom;
    private List<RxUser> mPlayers;
    private boolean isGameActive;
    private boolean isGameStarted;
    private PublishSubject<RxRoom> mRoomInfoSubject;
    private PublishSubject<RxUser> mRemoveUserSubject;
    private PublishSubject<RxUser> mAddUserSubject;

    @Inject ObservableTransformer mSchedulersTransformer;
    @Inject PreferenceManager mPreferenceManager;

    public RxRoom(Room room) {
        this.mRoom = room;

        mPlayers = new ArrayList<>();
        if (mRoom != null && mRoom.getPlayers() != null) {
            for (User user : mRoom.getPlayers()) {
                mPlayers.add(new RxUser(user));
            }
        }
        mRoomInfoSubject = PublishSubject.create();
        mRemoveUserSubject = PublishSubject.create();
        mAddUserSubject = PublishSubject.create();
    }

    public Room getRoom() {
        return mRoom;
    }

    public void setRoom(Room room) {
        mPlayers.clear();
        this.mRoom = room;

        for (User user : room.getPlayers()) {
            RxUser rxUser = new RxUser(user);
            mPlayers.add(rxUser);
            //if(!mRoom.getPlayers().contains(user)) {
            //    mRoom.getPlayers().add(user);
            // }
        }
        mRoomInfoSubject.onNext(this);
    }

    public List<RxUser> getRxPlayers() {
        return mPlayers;
    }

    public void setId(long id) {
        mRoom.setId(id);
    }

    public void setPassword(String password) {
        mRoom.setPassword(password);
    }

    public void setMaxPlayers(byte maxPlayers) {
        mRoom.setMaxPlayers(maxPlayers);
    }

    public void setPlayers(List<RxUser> players) {
        mPlayers = players;
    }

    public void setCurrentPlayersNumber(byte currentPlayersNumber) {
        mRoom.setCurrentPlayersNumber(currentPlayersNumber);
        mRoomInfoSubject.onNext(this);
    }

    public void setStartDate(long startDate) {
        mRoom.setStartDate(startDate);
    }

    public void setEndDate(long endDate) {
        mRoom.setEndDate(endDate);
    }

    public void setName(String name) {
        mRoom.setName(name);
    }

    public void setWithPassword(boolean withPassword) {
        mRoom.setWithPassword(withPassword);
    }

    public void notifyRoomInfo(){
        mRoomInfoSubject.onNext(this);
    }

    public void addPlayer(User user) {
        RxUser rxUser = new RxUser(user);
        mPlayers.add(rxUser);
        if (!mRoom.getPlayers().contains(user)) {
            mRoom.getPlayers().add(user);
        }
        mAddUserSubject.onNext(rxUser);
    }

    public void removePlayer(User user) {
        RxUser rxUserResult = null;
        for (RxUser rxUser : mPlayers) {
            if (rxUser.getUser().equals(user)) {
                rxUserResult = rxUser;
                break;
            }
        }
        if (rxUserResult != null) {
            mPlayers.remove(rxUserResult);
            User userToDelete = null;
            for (User innerUser : mRoom.getPlayers()) {
                if (innerUser.equals(user)) {
                    userToDelete = innerUser;
                }
            }
            if (userToDelete != null) {
                mRoom.getPlayers().remove(userToDelete);
            }
            mRemoveUserSubject.onNext(rxUserResult);
        }
    }

    public boolean isGameActive() {
        return isGameActive;
    }

    public RxRoom setGameActive(boolean gameActive) {
        isGameActive = gameActive;
        return this;
    }

    public void setGameStarted(Boolean isGameStarted) {
        for (RxUser rxUser : mPlayers) {
            rxUser.setWordVisible(isGameStarted);
        }
    }

    public Observable<RxRoom> roomInfoObservable() {
        return mRoomInfoSubject;
    }

    public Observable<RxUser> addUserObservable() {
        return mAddUserSubject;
    }

    public Observable<RxUser> removeUserObservable() {
        return mRemoveUserSubject;
    }
}
