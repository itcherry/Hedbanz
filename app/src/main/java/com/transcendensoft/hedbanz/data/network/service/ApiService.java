package com.transcendensoft.hedbanz.data.network.service;
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

import com.transcendensoft.hedbanz.data.models.FriendDTO;
import com.transcendensoft.hedbanz.data.models.RoomDTO;
import com.transcendensoft.hedbanz.data.models.RoomFilterDTO;
import com.transcendensoft.hedbanz.data.models.UserDTO;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Interface that describes all API methods with server
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public interface ApiService {
    @PUT("user")
    Observable<UserDTO> registerUser(@Body UserDTO user);

    @POST("user")
    Observable<UserDTO> authUser(@Body UserDTO user);

    @PATCH("user")
    Observable<UserDTO> updateUser(@Body HashMap<String, Object> userMap);

    @GET("rooms/{page}")
    Observable<List<RoomDTO>> getRooms(@Path("page") int page);

    @PUT("rooms")
    Observable<RoomDTO> createRoom(@Body HashMap<String, Object> roomDataMap);

    @POST("rooms/{page}")
    Observable<List<RoomDTO>> filterRooms(
            @Path("page") int page,
            @Body RoomFilterDTO roomFilter);

    @GET("friends")
    Observable<List<FriendDTO>> getFriends(@Query("userId") long userId);
}
