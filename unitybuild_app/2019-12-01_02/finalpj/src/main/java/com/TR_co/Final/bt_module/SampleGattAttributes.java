/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.TR_co.Final.bt_module;

import java.util.HashMap;

/**
 * This class includes a small subset of standard GATT attributes for demonstration purposes.
 */
public class SampleGattAttributes {
    private static HashMap<String, String> attributes = new HashMap();

    static {
        // Sample Characteristics.
        attributes.put("CLIENT_CHAR_CONFIG", "00002902-0000-1000-8000-00805f9b34fb");
        attributes.put("SERV_UUID", "0000FFF0-0000-1000-8000-00805f9b34fb");
        attributes.put("WRITE_UUID", "0000FFF1-0000-1000-8000-00805f9b34fb");
        attributes.put("NOTI_UUID", "0000FFF2-0000-1000-8000-00805f9b34fb");
        attributes.put("UUID_HEART_RATE_MEASUREMENT", "00002a37-0000-1000-8000-00805f9b34fb");
    }

    public static String lookup(String key) {
        String uuid = attributes.get(key);
        return uuid == null ? "null uuid" : uuid;
    }
}
