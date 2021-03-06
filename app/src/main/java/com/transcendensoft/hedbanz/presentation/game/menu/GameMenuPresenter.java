package com.transcendensoft.hedbanz.presentation.game.menu;
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

import com.transcendensoft.hedbanz.domain.entity.Room;
import com.transcendensoft.hedbanz.domain.entity.User;
import com.transcendensoft.hedbanz.presentation.base.BasePresenter;
import com.transcendensoft.hedbanz.presentation.game.models.RxRoom;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

/**
 * Implementation of game menu presenter.
 * Here are work with users, their state, their guess words
 * and general information about room.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
public class GameMenuPresenter extends BasePresenter<RxRoom, GameMenuContract.View>
        implements GameMenuContract.Presenter {
    private ObservableTransformer mSchedulersTransformer;

    @Inject
    public GameMenuPresenter(ObservableTransformer schedulersTransformer) {
        this.mSchedulersTransformer = schedulersTransformer;
    }

    @Override
    protected void updateView() {
        if (model != null) {
            updateRoomInfo();
            subscribeToRoomObservables();
        }
    }

    @Override
    public void destroy() {

    }

    private void updateRoomInfo() {
        if (view() != null) {
            Timber.i("CONCURRENT: updateRoomInfo");

            view().clearAndAddPlayers(model.getRxPlayers());
            view().setRoomName(model.getRoom().getName());
            view().setMaxPlayersCount(model.getRoom().getMaxPlayers());
            view().setCurrentPlayersCount(model.getRxPlayers().size());
            if(!model.isGameActive()) {
                if (model.getRxPlayers().size() >= model.getRoom().getMaxPlayers()) {
                    view().setInviteEnabled(false);
                } else {
                    view().setInviteEnabled(true);
                }
            }
        }
    }

    private void subscribeToRoomObservables() {
        addDisposable(model.roomInfoObservable()
               // .compose(applySchedulers())
                //.onBackpressureBuffer()
                //.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rxRoom -> {
                    Timber.i("CONCURRENT: onnext updateRoomInfo");
                    updateRoomInfo();
                }, Timber::e));

        addDisposable(model.addUserObservable()
                .compose(applySchedulers())
                .subscribe(rxUser -> {
                    Timber.i("CONCURRENT: onnext addPlayer");
                    view().addPlayer(rxUser);
                }, Timber::e));

        addDisposable(model.removeUserObservable()
                .compose(applySchedulers())
                .subscribe(rxUser -> {
                    Timber.i("CONCURRENT: onnext removePlayer");
                    view().removePlayer(rxUser);
                }, Timber::e));
    }

    @SuppressWarnings("unchecked")
    private <S> ObservableTransformer<S, S> applySchedulers() {
        return (ObservableTransformer<S, S>) mSchedulersTransformer;
    }

    @Override
    public void processPlayerClickListener(Observable<User> playerClickObservable) {
        addDisposable(playerClickObservable
                .subscribe(user -> {
                    view().onPlayerClicked(user);
                }, Timber::e));
    }

    @Override
    public Room getRoom() {
        return model.getRoom();
    }
}
