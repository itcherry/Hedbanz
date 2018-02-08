package com.transcendensoft.hedbanz.presenter;
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

import android.widget.EditText;

import com.transcendensoft.hedbanz.model.entity.User;

/**
 * Interface that shows describes methods which is needed
 * to sign up some user.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public interface RegisterPresenter {
    void registerUser(User user);

    void initAnimEditTextListener(EditText editText);

    void initNameCheckingListener(EditText editText);
}