package com.transcendensoft.hedbanz.presentation.usercrud;
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

import android.content.Context;

import com.transcendensoft.hedbanz.di.qualifier.ActivityContext;
import com.transcendensoft.hedbanz.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;

/**
 * Module that provides fragments, presenters
 * and other instances for user crud presentations
 * like updating credentials and registration
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
@Module
public interface UserCrudModule {
    @ActivityScope
    @Binds
    UserCrudContract.Presenter userCrudPresenter(UserCrudPresenter userCrudPresenter);

    @Binds
    @ActivityContext
    Context provideRegisterActivityContext(RegisterActivity registerActivity);
}
