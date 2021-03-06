package com.transcendensoft.hedbanz.presentation.game.menu.list;
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
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.transcendensoft.hedbanz.R;
import com.transcendensoft.hedbanz.data.prefs.PreferenceManager;
import com.transcendensoft.hedbanz.domain.entity.PlayerStatus;
import com.transcendensoft.hedbanz.domain.entity.User;
import com.transcendensoft.hedbanz.presentation.base.MvpViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;

/**
 * View holder realization for concrete user in game mode
 * side bar menu.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */
public class UserMenuItemViewHolder extends MvpViewHolder<UserMenuItemPresenter>
        implements UserMenuItemContract.View {
    @BindView(R.id.tvUserWordMenu) TextView mTvWord;
    @BindView(R.id.tvUserLogin) TextView mTvLogin;
    @BindView(R.id.ivUserIcon) ImageView mIvUserIcon;
    @BindView(R.id.tvAfkShadow) TextView mTvAfk;
    @BindView(R.id.tvThisUser) TextView mTvThisUser;
    @BindView(R.id.ivThisUserStar) ImageView mIvThisUserStar;
    @BindView(R.id.ivWin) ImageView mIvWin;
    private View mItemView;

    private Context mContext;
    private PreferenceManager mPreferenceManager;

    public UserMenuItemViewHolder(Context context, View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        mContext = context;
        mItemView = itemView;
        this.mPreferenceManager = new PreferenceManager(mContext);
    }

    @Override
    public void setIcon(int icon) {
        Drawable d = VectorDrawableCompat.create(mContext.getResources(), icon, null);
        mIvUserIcon.setImageDrawable(d);
    }

    @Override
    public void setName(String name) {
        if (!TextUtils.isEmpty(name)) {
            mTvLogin.setText(name);
        } else {
            mTvLogin.setText("");
        }
    }

    @Override
    public void setStatus(PlayerStatus playerStatus) {
        switch (playerStatus) {
            case AFK:
                mTvAfk.setVisibility(View.VISIBLE);
                break;
            case ACTIVE:
                mTvAfk.setVisibility(View.GONE);
                break;
            default:
        }
    }

    @Override
    public void setIsWinner(boolean isWinner) {
        if(isWinner){
            mIvWin.setVisibility(View.VISIBLE);
        } else {
            mIvWin.setVisibility(View.GONE);
        }
    }

    @Override
    public void setWord(User user) {
        if (mPreferenceManager.getUser().equals(user)) {
            mTvThisUser.setVisibility(View.VISIBLE);
            mIvThisUserStar.setVisibility(View.VISIBLE);
            if(mIvWin.getVisibility() == View.VISIBLE) {
                mTvWord.setVisibility(View.INVISIBLE);
            } else {
                mTvWord.setVisibility(View.GONE);
            }
        } else {
            mTvThisUser.setVisibility(View.GONE);
            mIvThisUserStar.setVisibility(View.GONE);

            if (!TextUtils.isEmpty(user.getWord())) {
                mTvWord.setText(user.getWord());
                mTvWord.setVisibility(View.VISIBLE);
            } else {
                if(mIvWin.getVisibility() == View.VISIBLE) {
                    mTvWord.setVisibility(View.INVISIBLE);
                } else {
                    mTvWord.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void setWordVisible(User user) {
        if(!mPreferenceManager.getUser().equals(user) &&
                user.isWordVisible() && !TextUtils.isEmpty(user.getWord())){
            mTvWord.setVisibility(View.VISIBLE);
        } else {
            if(mIvWin.getVisibility() == View.VISIBLE) {
                mTvWord.setVisibility(View.INVISIBLE);
            } else {
                mTvWord.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public Observable<User> getClickObservable(User user) {
        return Observable.create(emitter -> {
            mItemView.setOnClickListener(v -> {
                emitter.onNext(user);
            });
        });
    }
}
