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

import com.gigaspaces.annotation.pojo.SpaceClass;
import com.gigaspaces.annotation.pojo.SpaceProperty;
import com.gigaspaces.simpledao.model.BaseEntity;

@SpaceClass
public class SampleEntity extends BaseEntity {
    String text;

    @SpaceProperty
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("SampleEntity");
        sb.append("{text='").append(text).append('\'');
        sb.append("}:").append(super.toString());
        return sb.toString();
    }
}
