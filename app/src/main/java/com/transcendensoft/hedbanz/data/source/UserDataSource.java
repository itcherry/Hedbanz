package com.transcendensoft.hedbanz.data.source;
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

import com.transcendensoft.hedbanz.data.models.UserDTO;

import io.reactivex.Observable;

/**
 * Base interface for remote and local data that
 * describes methods of getting or updating user data
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */

public interface UserDataSource {
    Observable<UserDTO> registerUser(UserDTO user);

    Observable<UserDTO> authUser(UserDTO user);

    Observable<UserDTO> updateUser(long id, String newLogin,
                                   String oldPassword, String newPassword);

    Observable<UserDTO> updateUserInfo(UserDTO user);

    Observable<UserDTO> getUser(long id);

    Observable<?> forgotPassword(String login, String locale);

    Observable<?> checkKeyword(String login, String keyword);

    Observable<?> resetPassword(String login, String keyword, String password);
}
