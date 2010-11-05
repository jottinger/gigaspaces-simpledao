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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

@ContextConfiguration(locations = {"classpath:/com/gigaspaces/simpledao/dao-context.xml", "classpath:/com/gigaspaces/simpledao/gigaspaces-context.xml"})
public class SpringTest extends AbstractTestNGSpringContextTests {
    @Autowired
    SampleDAO dao;

    @Test
    public void testConfig() {
        assertNotNull(dao);
    }
}
