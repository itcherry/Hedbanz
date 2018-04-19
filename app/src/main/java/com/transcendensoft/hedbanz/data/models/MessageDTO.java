package com.transcendensoft.hedbanz.data.models;
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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * DTO for message entity.
 * All message properties received from server described here
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class MessageDTO {
    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("clientMessageId")
    @Expose
    private int clientMessageId;
    @SerializedName("senderId")
    @Expose
    private long senderId;
    @SerializedName("roomId")
    @Expose
    private long roomId;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("type")
    @Expose
    private int type;
    @SerializedName("createDate")
    @Expose
    private Long createDate;

    MessageDTO(long id, long senderId, long roomId, String text, int type, Long createDate, int clientMessageId) {
        this.id = id;
        this.senderId = senderId;
        this.roomId = roomId;
        this.text = text;
        this.type = type;
        this.createDate = createDate;
        this.clientMessageId = clientMessageId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Long createDate) {
        this.createDate = createDate;
    }

    public int getClientMessageId() {
        return clientMessageId;
    }

    public void setClientMessageId(int clientMessageId) {
        this.clientMessageId = clientMessageId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageDTO that = (MessageDTO) o;

        return clientMessageId == that.clientMessageId;
    }

    @Override
    public int hashCode() {
        return (int) (clientMessageId ^ (clientMessageId >>> 32));
    }

    public static class Builder {
        private long id;
        private long senderId;
        private long roomId;
        private String text;
        private int type;
        private Long createDate;
        private int clientMessageId;

        public Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Builder setSenderId(long senderId) {
            this.senderId = senderId;
            return this;
        }

        public Builder setRoomId(long roomId) {
            this.roomId = roomId;
            return this;
        }

        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        public Builder setType(int type) {
            this.type = type;
            return this;
        }

        public Builder setCreateDate(Long createDate) {
            this.createDate = createDate;
            return this;
        }

        public Builder setClientMessageId(int clientMessageId) {
            this.clientMessageId = clientMessageId;
            return this;
        }

        public MessageDTO build() {
            return new MessageDTO(id, senderId, roomId, text, type, createDate, clientMessageId);
        }
    }
}
