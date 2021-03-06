package com.transcendensoft.hedbanz.utils;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Utility class that converts ro calculates date
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class DateUtils {
    private static final String HOURS_MINUTES_MASK = "HH:mm";

    public static String convertDateToHoursMinutes(Long date){
        DateFormat sdf = new SimpleDateFormat(HOURS_MINUTES_MASK, Locale.getDefault());
        TimeZone tz = TimeZone.getDefault();
        sdf.setTimeZone(tz);

        return sdf.format(date);
    }
}
