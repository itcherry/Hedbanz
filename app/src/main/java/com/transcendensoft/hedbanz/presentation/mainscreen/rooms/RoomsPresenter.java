package com.transcendensoft.hedbanz.presentation.mainscreen.rooms;
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

import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.transcendensoft.hedbanz.data.source.DataPolicy;
import com.transcendensoft.hedbanz.domain.entity.Room;
import com.transcendensoft.hedbanz.domain.entity.RoomFilter;
import com.transcendensoft.hedbanz.domain.repository.RoomDataRepository;
import com.transcendensoft.hedbanz.presentation.base.BasePresenter;
import com.transcendensoft.hedbanz.presentation.base.MvpRecyclerAdapter;
import com.transcendensoft.hedbanz.presentation.base.MvpViewHolder;
import com.transcendensoft.hedbanz.presentation.mainscreen.rooms.list.RoomItemViewHolder;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

/**
 * Presenter from MVP pattern, that contains
 * methods show all available rooms
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class RoomsPresenter extends BasePresenter<List<Room>, RoomsContract.View>
        implements RoomsContract.Presenter, MvpRecyclerAdapter.OnBottomReachedListener {
    private static final String TAG = RoomsPresenter.class.getName();
    private int mCurrentPage = 0;
    private RoomItemViewHolder mLastHolder;
    private RoomFilter mFilter;
    private RoomDataRepository mRoomRepository;

    @Inject
    public RoomsPresenter(RoomDataRepository roomRepository) {
        this.mRoomRepository = roomRepository;
    }

    @Override
    protected void updateView() {
        if (model.isEmpty()) {
            refreshRooms();
        } else {
            view().clearRooms();
            view().addRoomsToRecycler(model);
        }
    }

    @Override
    public void loadNextRooms() {
        Disposable disposable = mRoomRepository
                .getRooms(mCurrentPage, DataPolicy.API)
                .subscribe(
                        this::processRoomsOnNext,
                        this::processRoomOnError,
                        () -> {
                            if (view() != null) {
                                view().stopRefreshingBar();
                            }
                        },
                        this::processRoomOnSubscribe);
        addDisposable(disposable);
    }

    private void processRoomOnError(Throwable err) {
        if (!(err instanceof IllegalStateException)) {
            Log.e(TAG, "Error " + err.getMessage());
            Crashlytics.logException(err);
            if (mCurrentPage == 0) {
                view().showServerError();
            } else {
                mLastHolder.showErrorServer();
            }
        }
    }

    private void processRoomOnSubscribe(Disposable d) {
        if (!d.isDisposed()) {
            if (view().isNetworkConnected()) {
                if (mCurrentPage == 0) {
                    view().showLoading();
                }
            } else {
                if (mCurrentPage == 0) {
                    view().showNetworkError();
                } else {
                    mLastHolder.showErrorNetwork();
                }
                throw new IllegalStateException();
            }
        }
    }

    private void processRoomsOnNext(List<Room> result) {
        if (result == null || result.isEmpty()) {
            if (mCurrentPage == 0) {
                view().showEmptyList();
            } else {
                model.remove(model.size() - 1);
                view().removeLastRoom();
            }
        } else {
            if (mCurrentPage != 0) {
                model.remove(model.size() - 1);
                view().removeLastRoom();
            }

            //List<Room> rooms = result;
            result.add(new Room.Builder().setId(-1).build()); //ProgressBar view

            view().addRoomsToRecycler(result);
            view().showContent();
            model.addAll(result);
        }
    }

    @Override
    public void refreshRooms() {
        mCurrentPage = 0;
        view().clearRooms();
        loadNextRooms();
    }

    @Override
    public void onBottomReached(MvpViewHolder holder) {
        mLastHolder = (RoomItemViewHolder) holder;
        mCurrentPage++;

        if (mFilter == null) {
            loadNextRooms();
        } else {
            loadNextFilteredRooms();
        }
    }

    @Override
    public void clearFiltersAndText() {
        mFilter = null;
    }

    @Override
    public void filterRooms(RoomFilter roomFilter) {
        mCurrentPage = 0;
        if (mFilter == null) {
            mFilter = roomFilter;
        } else {
            if (roomFilter.getMaxPlayers() != null) {
                mFilter.setMaxPlayers(roomFilter.getMaxPlayers());
            }
            if (roomFilter.getMinPlayers() != null) {
                mFilter.setMinPlayers(roomFilter.getMinPlayers());
            }
            if (roomFilter.getRoomName() != null) {
                mFilter.setRoomName(roomFilter.getRoomName());
            }
            if (roomFilter.isPrivate() != null) {
                mFilter.setPrivate(roomFilter.isPrivate());
            }
        }
        view().clearRooms();
        loadNextFilteredRooms();
    }

    private void loadNextFilteredRooms() {
        Disposable disposable = mRoomRepository
                .filterRooms(mCurrentPage, mFilter,DataPolicy.API)
                .subscribe(
                        this::processRoomsOnNext,
                        this::processRoomOnError,
                        () -> {
                            if (view() != null) {
                                view().stopRefreshingBar();
                            }
                        },
                        this::processRoomOnSubscribe);
        addDisposable(disposable);
    }

    @Override
    public void clearFilters() {
        mFilter.setPrivate(null);
        mFilter.setMinPlayers(null);
        mFilter.setMaxPlayers(null);
    }
}
