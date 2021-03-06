package com.transcendensoft.hedbanz.data.exception;
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

import com.transcendensoft.hedbanz.data.models.common.ServerError;

/**
 * Exception throw by the application when a there
 * was returned error from Hedbanz API.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */

public class HedbanzApiException extends RuntimeException{
    private ServerError mServerError;

    public HedbanzApiException(ServerError mServerError) {
        this.mServerError = mServerError;
    }

    public HedbanzApiException(String message) {
        super(message);
        mServerError = new ServerError();
        mServerError.setErrorMessage(message);
        mServerError.setErrorCode(-1);
    }

    public String getServerErrorMessage(){
        if(mServerError == null){
            return "Error object from API is NULL!";
        } else {
            return mServerError.getErrorMessage();
        }
    }

    public int getServerErrorCode(){
        if(mServerError == null){
            return -1;
        } else {
            return mServerError.getErrorCode();
        }
    }
}
