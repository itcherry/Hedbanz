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

import com.transcendensoft.hedbanz.domain.entity.Room;
import com.transcendensoft.hedbanz.domain.entity.RoomFilter;
import com.transcendensoft.hedbanz.domain.interactor.PaginationState;
import com.transcendensoft.hedbanz.domain.interactor.rooms.FilterRoomsInteractor;
import com.transcendensoft.hedbanz.domain.interactor.rooms.GetRoomsInteractor;
import com.transcendensoft.hedbanz.presentation.base.BasePresenter;
import com.transcendensoft.hedbanz.presentation.base.MvpRecyclerAdapter;
import com.transcendensoft.hedbanz.presentation.base.MvpViewHolder;
import com.transcendensoft.hedbanz.presentation.mainscreen.rooms.list.RoomItemViewHolder;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.observers.DisposableObserver;

/**
 * Presenter from MVP pattern, that contains
 * methods show all available rooms
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class RoomsPresenter extends BasePresenter<List<Room>, RoomsContract.View>
        implements RoomsContract.Presenter, MvpRecyclerAdapter.OnBottomReachedListener {
    private RoomItemViewHolder mLastHolder;
    private RoomFilter mRoomFilter;

    private FilterRoomsInteractor mFilterRoomsInteractor;
    private GetRoomsInteractor mGetRoomsInteractor;

    @Inject
    public RoomsPresenter(FilterRoomsInteractor filterRoomsInteractor,
                          GetRoomsInteractor getRoomsInteractor) {
        this.mFilterRoomsInteractor = filterRoomsInteractor;
        this.mGetRoomsInteractor = getRoomsInteractor;
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
        mGetRoomsInteractor.loadNextPage()
                .execute(new RoomListObserver(), null);
    }

    @Override
    public void refreshRooms() {
        view().clearRooms();
        mGetRoomsInteractor.refresh(null)
                .execute(new RoomListObserver(), null);
    }

    @Override
    public void onBottomReached(MvpViewHolder holder) {
        mLastHolder = (RoomItemViewHolder) holder;

        if (mRoomFilter == null) {
            loadNextRooms();
        } else {
            mFilterRoomsInteractor.loadNextPage()
                    .execute(new RoomListObserver(), null);
        }
    }

    @Override
    public void filterRooms(RoomFilter roomFilter) {
        mRoomFilter = roomFilter;
        view().clearRooms();
        if(mRoomFilter == null){
            mRoomFilter = new RoomFilter.Builder().build();
        }
        mFilterRoomsInteractor.refresh(mRoomFilter)
                .execute(new RoomListObserver(), null);
    }

    @Override
    public void updateFilter(RoomFilter roomFilter) {
        if(roomFilter != null) {
            if (mRoomFilter == null) {
                mRoomFilter = roomFilter;
            } else {
                if (roomFilter.getMaxPlayers() != null) {
                    mRoomFilter.setMaxPlayers(roomFilter.getMaxPlayers());
                }
                if (roomFilter.getMinPlayers() != null) {
                    mRoomFilter.setMinPlayers(roomFilter.getMinPlayers());
                }
                if (roomFilter.getRoomName() != null) {
                    mRoomFilter.setRoomName(roomFilter.getRoomName());
                }
                if (roomFilter.isPrivate() != null) {
                    mRoomFilter.setPrivate(roomFilter.isPrivate());
                }
            }
        }
        filterRooms(mRoomFilter);
    }

    @Override
    public void clearFilters() {
        mRoomFilter = null;
    }

    private final class RoomListObserver extends DisposableObserver<PaginationState<Room>> {
        @Override
        public void onNext(PaginationState<Room> roomPaginationState) {
            if (roomPaginationState != null) {
                if(processErrors(roomPaginationState)){
                    return;
                }

                List<Room> rooms = roomPaginationState.getData();
                if (rooms == null || rooms.isEmpty()) {
                    processEmptyRoomList(roomPaginationState);
                } else {
                    processNotEmptyRoomList(roomPaginationState, rooms);
                }
            }
        }

        private boolean processErrors(PaginationState<Room> roomPaginationState) {
            boolean hasErrors = false;
            if(roomPaginationState.isHasInternetError()){
                processNetworkError(roomPaginationState);
                hasErrors = true;
            }
            if(roomPaginationState.isHasServerError()){
                processServerError(roomPaginationState);
                hasErrors = true;
            }
            return hasErrors;
        }

        private void processNetworkError(PaginationState<Room> roomPaginationState) {
            if (roomPaginationState.isRefreshed()) {
                view().showNetworkError();
            } else {
                mLastHolder.showErrorNetwork();
            }
        }

        private void processServerError(PaginationState<Room> roomPaginationState) {
            if (roomPaginationState.isRefreshed()) {
                view().showServerError();
            } else {
                mLastHolder.showErrorServer();
            }
        }

        private void processEmptyRoomList(PaginationState<Room> roomPaginationState) {
            if (roomPaginationState.isRefreshed()) {
                view().showEmptyList();
            } else {
                model.remove(model.size() - 1);
                view().removeLastRoom();
            }
        }

        private void processNotEmptyRoomList(PaginationState<Room> roomPaginationState, List<Room> rooms) {
            if (!roomPaginationState.isRefreshed()) {
                model.remove(model.size() - 1);
                view().removeLastRoom();
            }

            rooms.add(new Room.Builder().setId(-1).build()); //ProgressBar view

            view().addRoomsToRecycler(rooms);
            view().showContent();
            model.addAll(rooms);
        }

        @Override
        public void onError(Throwable e) {
            if (view() != null) {
                view().showServerError();
            }
        }

        @Override
        public void onComplete() {
            if (view() != null) {
                view().stopRefreshingBar();
            }
        }

        @Override
        protected void onStart() {
            view().showLoading();
        }
    }
}

