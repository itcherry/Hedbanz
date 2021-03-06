package com.transcendensoft.hedbanz.data.models.mapper;
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

import com.transcendensoft.hedbanz.data.models.RoomDTO;
import com.transcendensoft.hedbanz.di.scope.ApplicationScope;
import com.transcendensoft.hedbanz.domain.entity.Room;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

/**
 * Mapper class used to transform {@link com.transcendensoft.hedbanz.data.models.RoomDTO}
 * (in the data layer) to {@link com.transcendensoft.hedbanz.domain.entity.Room} in the
 * domain layer.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
@ApplicationScope
public class RoomModelDataMapper {
    private UserModelDataMapper mUserModelDataMapper;

    @Inject
    public RoomModelDataMapper() {
        mUserModelDataMapper = new UserModelDataMapper();
    }

    public Room convert(RoomDTO roomDTO){
        Room roomResult = null;
        if(roomDTO != null){
            roomResult = new Room.Builder()
                    .setId(roomDTO.getId())
                    .setCurrentPlayersNumber(roomDTO.getCurrentPlayersNumber())
                    .setEndDate(roomDTO.getEndDate())
                    .setStartDate(roomDTO.getStartDate())
                    .setMaxPlayers(roomDTO.getMaxPlayers())
                    .setName(roomDTO.getName())
                    .setPassword(roomDTO.getPassword())
                    .setWithPassword(roomDTO.isPrivate())
                    .setPlayers(mUserModelDataMapper.convertToUsers(roomDTO.getUsers()))
                    .setIsActive(roomDTO.isActive())
                    .setStickerId(roomDTO.getStickerId())
                    .setIconId(roomDTO.getIconId())
                    .build();
        }
        return roomResult;
    }

    public RoomDTO convert(Room room){
        RoomDTO roomResult = null;
        if(room != null){
            roomResult = new RoomDTO.Builder()
                    .setId(room.getId())
                    .setCurrentPlayersNumber(room.getCurrentPlayersNumber())
                    .setEndDate(room.getEndDate())
                    .setStartDate(room.getStartDate())
                    .setMaxPlayers(room.getMaxPlayers())
                    .setName(room.getName())
                    .setPassword(room.getPassword())
                    .setIsPrivate(room.isWithPassword())
                    .setIsActive(room.isActive())
                    .setUsers(mUserModelDataMapper.convertToDtoUsers(room.getPlayers()))
                    .setStickerId(room.getStickerId())
                    .setIconId(room.getIconId())
                    .build();
        }
        return roomResult;
    }

    public List<Room> convert(Collection<RoomDTO> roomDTOCollection) {
        final List<Room> roomsResult = new ArrayList<>(20);
        for (RoomDTO roomDTO : roomDTOCollection) {
            final Room room = convert(roomDTO);
            if (room != null) {
                roomsResult.add(room);
            }
        }
        return roomsResult;
    }
}
