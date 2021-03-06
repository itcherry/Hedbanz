package com.transcendensoft.hedbanz.presentation.game.list;
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

import android.support.annotation.NonNull;
import android.view.View;

import com.transcendensoft.hedbanz.domain.entity.Message;
import com.transcendensoft.hedbanz.domain.entity.Question;
import com.transcendensoft.hedbanz.domain.entity.Word;
import com.transcendensoft.hedbanz.presentation.base.RecyclerDelegationAdapter;
import com.transcendensoft.hedbanz.presentation.game.list.delegates.AdvertiseAdapterDelegate;
import com.transcendensoft.hedbanz.presentation.game.list.delegates.AskingQuestionOtherUserAdapterDelegate;
import com.transcendensoft.hedbanz.presentation.game.list.delegates.AskingQuestionThisUserAdapterDelegate;
import com.transcendensoft.hedbanz.presentation.game.list.delegates.GameOverAdapterDelegate;
import com.transcendensoft.hedbanz.presentation.game.list.delegates.GuessWordOtherUserAdapterDelegate;
import com.transcendensoft.hedbanz.presentation.game.list.delegates.GuessWordThisUserAdapterDelegate;
import com.transcendensoft.hedbanz.presentation.game.list.delegates.JoinedLeftUserAdapterDelegate;
import com.transcendensoft.hedbanz.presentation.game.list.delegates.KickWarningAdapterDelegate;
import com.transcendensoft.hedbanz.presentation.game.list.delegates.KickedAdapterDelegate;
import com.transcendensoft.hedbanz.presentation.game.list.delegates.LoadingAdapterDelegate;
import com.transcendensoft.hedbanz.presentation.game.list.delegates.MessageOtherUserAdapterDelegate;
import com.transcendensoft.hedbanz.presentation.game.list.delegates.MessageThisUserAdapterDelegate;
import com.transcendensoft.hedbanz.presentation.game.list.delegates.NetworkErrorAdapterDelegate;
import com.transcendensoft.hedbanz.presentation.game.list.delegates.ServerErrorAdapterDelegate;
import com.transcendensoft.hedbanz.presentation.game.list.delegates.UpdateUsersInfoAdapterDelegate;
import com.transcendensoft.hedbanz.presentation.game.list.delegates.UserAfkReturnedAdapterDelegate;
import com.transcendensoft.hedbanz.presentation.game.list.delegates.UserWinsAdapterDelegate;
import com.transcendensoft.hedbanz.presentation.game.list.delegates.WaitingForGameAdapterDelegate;
import com.transcendensoft.hedbanz.presentation.game.list.delegates.WordSettedAdapterDelegate;
import com.transcendensoft.hedbanz.presentation.game.list.delegates.WordSettingAdapterDelegate;

import javax.annotation.Nullable;
import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Adapter for game mode recycler.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
public class GameListAdapter extends RecyclerDelegationAdapter<Message> {
    private ServerErrorAdapterDelegate mServerErrorAdapterDelegate;
    private NetworkErrorAdapterDelegate mNetworkErrorAdapterDelegate;
    private WordSettingAdapterDelegate mWordSettingAdapterDelegate;
    private GuessWordThisUserAdapterDelegate mGuessWordThisUserAdapterDelegate;
    private AskingQuestionOtherUserAdapterDelegate mAskingQuestionOtherUserAdapterDelegate;
    private GameOverAdapterDelegate mGameOverAdapterDelegate;

    @Inject
    public GameListAdapter(LoadingAdapterDelegate loadingAdapterDelegate,
                           ServerErrorAdapterDelegate serverErrorAdapterDelegate,
                           NetworkErrorAdapterDelegate networkErrorAdapterDelegate,
                           MessageThisUserAdapterDelegate messageThisUserAdapterDelegate,
                           MessageOtherUserAdapterDelegate messageOtherUserAdapterDelegate,
                           JoinedLeftUserAdapterDelegate joinedLeftUserAdapterDelegate,
                           WordSettedAdapterDelegate wordSettedAdapterDelegate,
                           WordSettingAdapterDelegate wordSettingAdapterDelegate,
                           UserAfkReturnedAdapterDelegate userAfkReturnedAdapterDelegate,
                           GuessWordThisUserAdapterDelegate guessWordThisUserAdapterDelegate,
                           GuessWordOtherUserAdapterDelegate guessWordOtherUserAdapterDelegate,
                           AskingQuestionThisUserAdapterDelegate askingQuestionThisUserAdapterDelegate,
                           AskingQuestionOtherUserAdapterDelegate askingQuestionOtherUserAdapterDelegate,
                           KickedAdapterDelegate kickedAdapterDelegate,
                           KickWarningAdapterDelegate kickWarningAdapterDelegate,
                           UserWinsAdapterDelegate userWinsAdapterDelegate,
                           GameOverAdapterDelegate gameOverAdapterDelegate,
                           WaitingForGameAdapterDelegate waitingForGameAdapterDelegate,
                           UpdateUsersInfoAdapterDelegate updateUsersInfoAdapterDelegate,
                           AdvertiseAdapterDelegate advertiseAdapterDelegate) {
        super();

        this.mServerErrorAdapterDelegate = serverErrorAdapterDelegate;
        this.mNetworkErrorAdapterDelegate = networkErrorAdapterDelegate;
        this.mWordSettingAdapterDelegate = wordSettingAdapterDelegate;
        this.mGuessWordThisUserAdapterDelegate = guessWordThisUserAdapterDelegate;
        this.mAskingQuestionOtherUserAdapterDelegate = askingQuestionOtherUserAdapterDelegate;
        this.mGameOverAdapterDelegate = gameOverAdapterDelegate;

        delegatesManager.addDelegate(loadingAdapterDelegate)
                .addDelegate(serverErrorAdapterDelegate)
                .addDelegate(networkErrorAdapterDelegate)
                .addDelegate(messageThisUserAdapterDelegate)
                .addDelegate(messageOtherUserAdapterDelegate)
                .addDelegate(joinedLeftUserAdapterDelegate)
                .addDelegate(wordSettingAdapterDelegate)
                .addDelegate(wordSettedAdapterDelegate)
                .addDelegate(guessWordThisUserAdapterDelegate)
                .addDelegate(guessWordOtherUserAdapterDelegate)
                .addDelegate(userAfkReturnedAdapterDelegate)
                .addDelegate(askingQuestionOtherUserAdapterDelegate)
                .addDelegate(askingQuestionThisUserAdapterDelegate)
                .addDelegate(kickedAdapterDelegate)
                .addDelegate(kickWarningAdapterDelegate)
                .addDelegate(userWinsAdapterDelegate)
                .addDelegate(gameOverAdapterDelegate)
                .addDelegate(waitingForGameAdapterDelegate)
                .addDelegate(updateUsersInfoAdapterDelegate)
                .addDelegate(advertiseAdapterDelegate);
    }

    @Nullable
    public Observable<Object> retryServerClickObservable() {
        return mServerErrorAdapterDelegate.getRetryServerClickObservable();
    }

    @Nullable
    public Observable<Object> retryNetworkClickObservable() {
        return mNetworkErrorAdapterDelegate.getRetryNetworkClickObservable();
    }

    @Nullable
    public Observable<Word> setWordObservable() {
        return mWordSettingAdapterDelegate.getSetWordObservable();
    }

    @NonNull
    public Observable<Question> guessWordSubmitObservable() {
        return mGuessWordThisUserAdapterDelegate.guessWordObservable();
    }

    @NonNull
    public Observable<Question> guessWordHelperStringObservable() {
        return mGuessWordThisUserAdapterDelegate.guessWordHelperStringsObservable();
    }

    @NonNull
    public Observable<Long> askingQuestionThumbsUpObservable() {
        return mAskingQuestionOtherUserAdapterDelegate.thumbsUpClickObservable();
    }

    @NonNull
    public Observable<Long> askingQuestionThumbsDownObservable() {
        return mAskingQuestionOtherUserAdapterDelegate.thumbsDownClickObservable();
    }

    @NonNull
    public Observable<Long> askingQuestionWinObservable() {
        return mAskingQuestionOtherUserAdapterDelegate.winClickObservable();
    }

    @NonNull
    public Observable<View> restartGameObservable() {
        return mGameOverAdapterDelegate.getRestartSubject();
    }

    @NonNull
    public Observable<View> cancelGameObservable() {
        return mGameOverAdapterDelegate.getCancelSubject();
    }

    @NonNull
    public Observable<Boolean> setWordFocusedObservable() {
        return mWordSettingAdapterDelegate.getSetWordFocusedSubject();
    }

    @NonNull
    public Observable<Boolean> guessWordFocusedObservable() {
        return mGuessWordThisUserAdapterDelegate.guessWordFocusedObservable();
    }
}
