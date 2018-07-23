package com.transcendensoft.hedbanz.domain.interactor.changepwd;
/**
 * Copyright 2018. Andrii Chernysh
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

import com.transcendensoft.hedbanz.data.repository.UserDataRepositoryImpl;
import com.transcendensoft.hedbanz.domain.CompletableUseCase;
import com.transcendensoft.hedbanz.domain.repository.UserDataRepository;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.CompletableTransformer;
import io.reactivex.disposables.CompositeDisposable;

/**
 * This class is an implementation of {@link com.transcendensoft.hedbanz.domain.UseCase}
 * that represents a use case for user reseting password.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class ResetPasswordUseCase extends CompletableUseCase<ResetPasswordUseCase.Param>{
    private UserDataRepository mUserDataRepository;

    @Inject
    public ResetPasswordUseCase(CompletableTransformer completableTransformer,
                                CompositeDisposable mCompositeDisposable,
                                UserDataRepositoryImpl userDataRepository) {
        super(completableTransformer, mCompositeDisposable);
        this.mUserDataRepository = userDataRepository;
    }

    @Override
    protected Completable buildUseCaseCompletable(ResetPasswordUseCase.Param param) {
        return mUserDataRepository.resetPassword(param.login, param.keyword, param.password);
    }

    public static class Param{
        private String login;
        private String keyword;
        private String password;

        public Param(String login, String keyword, String password) {
            this.login = login;
            this.keyword = keyword;
            this.password = password;
        }

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getKeyword() {
            return keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}