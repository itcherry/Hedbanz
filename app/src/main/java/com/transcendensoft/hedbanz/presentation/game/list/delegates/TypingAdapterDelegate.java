package com.transcendensoft.hedbanz.presentation.game.list.delegates;
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
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.adapterdelegates3.AdapterDelegate;
import com.transcendensoft.hedbanz.R;
import com.transcendensoft.hedbanz.domain.entity.Message;
import com.transcendensoft.hedbanz.domain.entity.User;
import com.transcendensoft.hedbanz.presentation.game.list.holder.TypingViewHolder;
import com.transcendensoft.hedbanz.presentation.game.models.TypingMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * This delegate is responsible for creating
 * {@link com.transcendensoft.hedbanz.presentation.game.list.holder.TypingViewHolder}
 * and binding ViewHolder widgets according to model.
 * <p>
 * An AdapterDelegate get added to an AdapterDelegatesManager.
 * This manager is the man in the middle between RecyclerView.
 * Adapter and each AdapterDelegate.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class TypingAdapterDelegate extends AdapterDelegate<List<Message>> {
    private List<User> mTypingNowUsers;

    public TypingAdapterDelegate() {
        mTypingNowUsers = new ArrayList<>();
    }

    @Override
    protected boolean isForViewType(@NonNull List<Message> items, int position) {
        return items.get(position) instanceof TypingMessage;
    }

    @NonNull
    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.item_typing_message, parent, false);
        return new TypingViewHolder(context, itemView);
    }

    @Override
    protected void onBindViewHolder(@NonNull List<Message> items, int position,
                                    @NonNull RecyclerView.ViewHolder holder,
                                    @NonNull List<Object> payloads) {
        TypingViewHolder viewHolder = (TypingViewHolder) holder;
        TypingMessage message = (TypingMessage) items.get(position);
        viewHolder.bindTypingText(message.getTypingUsers());
        viewHolder.bindTypingIndicatorImage();
    }
}