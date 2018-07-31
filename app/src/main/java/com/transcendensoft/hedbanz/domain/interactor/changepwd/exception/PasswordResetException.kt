package com.transcendensoft.hedbanz.domain.interactor.changepwd.exception

import com.transcendensoft.hedbanz.domain.validation.PasswordResetError

/**
 * Copyright 2018. Andrii Chernysh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
/**
 * Exceptions, that can be thrown from interactor in order to
 * show password reset errors of some
 * {@link com.transcendensoft.hedbanz.domain.entity.User}
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
class PasswordResetException(): RuntimeException() {
    val passwordResetErrors: MutableList<PasswordResetError> = mutableListOf()

    constructor(passwordResetError: PasswordResetError) : this(){
        passwordResetErrors.add(passwordResetError)
    }

    fun addError(passwordResetError: PasswordResetError) {
        passwordResetErrors.add(passwordResetError)
    }
}