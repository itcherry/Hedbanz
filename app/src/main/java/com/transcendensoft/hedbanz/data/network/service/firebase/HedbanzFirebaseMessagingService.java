package com.transcendensoft.hedbanz.data.network.service.firebase;
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

import android.app.Service;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.transcendensoft.hedbanz.domain.entity.NotificationMessage;
import com.transcendensoft.hedbanz.domain.entity.NotificationMessageType;
import com.transcendensoft.hedbanz.presentation.notiification.NotificationManager;

import java.util.Map;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasServiceInjector;
import timber.log.Timber;

/**
 * Service that handles Firebase push notifications.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 * Developed by <u>Transcendensoft</u>
 */

public class HedbanzFirebaseMessagingService extends FirebaseMessagingService implements HasServiceInjector {
    private static final String TAG = HedbanzFirebaseMessagingService.class.getName();
    public static final String FIELD_TYPE = "type";
    public static final String DATA_TYPE = "data";

    @Inject DispatchingAndroidInjector<Service> serviceDispatchingAndroidInjector;
    @Inject Gson mGson;
    @Inject NotificationManager mNotificationManger;

    @Override
    public void onCreate() {
        AndroidInjection.inject(this);
        super.onCreate();
    }

    @Override
    public AndroidInjector<Service> serviceInjector() {
        return serviceDispatchingAndroidInjector;
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Timber.tag(TAG);

        // Check if message contains a notification payload.
        if (remoteMessage.getData() != null) {
            Map<String, String> bodyMap = remoteMessage.getData();
            int type = Integer.parseInt(bodyMap.get(FIELD_TYPE));
            Timber.i("FCM push. notification type:" + type);

            NotificationMessageType messageType = NotificationMessageType.
                    Companion.getTypeById(type);
            String dataJson = bodyMap.get(DATA_TYPE);
            Timber.i("FCM push. data:" + dataJson);

            processNotificationMessage(messageType, dataJson);
        }
    }

    private void processNotificationMessage(NotificationMessageType messageType, String dataJson) {
        switch (messageType) {
            case MESSAGE:
                NotificationMessage notificationMessage = mGson
                        .fromJson(dataJson, NotificationMessage.class);
                mNotificationManger.notifyMessage(notificationMessage);
                break;
            case SET_WORD:
                break;
            case GUESS_WORD:
                break;
            case FRIEND:
                break;
            case INVITE:
                break;
            case UNDEFINED:
                break;
            default:
        }
    }
}
