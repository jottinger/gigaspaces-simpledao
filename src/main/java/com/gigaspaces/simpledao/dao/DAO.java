/*
 * Copyright 2010 Joseph B. Ottinger
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the license for the specific language governing permissions and
 * limitations under the license.
 */

package com.gigaspaces.simpledao.dao;

import com.gigaspaces.simpledao.model.BaseEntity;
import org.openspaces.core.GigaSpace;

public interface DAO<T extends BaseEntity> {
    // Utility methods
    void setGigaspace(GigaSpace space);

    // READ METHODS
    T[] readMultiple(T template);

    T[] readMultiple(T template, int count);

    T read(T template);

    T readById(String id);

    T readByQuery(String query, Object... parameters);

    T[] readMultipleByQuery(String query, int count, Object... parameters);

    // TAKE METHODS
    T[] takeMultiple(T template);

    T[] takeMultiple(T template, int count);

    T take(T template);

    T takeById(String id);

    T takeByQuery(String query, Object... parameters);

    T[] takeMultipleByQuery(String query, int count, Object... parameters);

    // MESSAGE METHODS
    T poll(T template, long timeout);

    T peek(T template, long timeout);

    T push(T entry);

    T push(T entry, long timeout);

    // WRITE METHODS
    T write(T entry);

    T write(T entry, long timeout);

    T update(T entry);

    T update(T entry, long timeout);

    int getReads();

    int getTakes();

    int getWrites();

    void reset();

}
