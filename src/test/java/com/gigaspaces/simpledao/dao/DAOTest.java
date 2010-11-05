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

import com.j_spaces.core.IJSpace;
import org.openspaces.core.GigaSpace;
import org.openspaces.core.GigaSpaceConfigurer;
import org.openspaces.core.space.UrlSpaceConfigurer;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

// add NO DATA TO THIS, please. It's to clarify some cobertura stats.

public class DAOTest {
    UrlSpaceConfigurer configurer;
    GigaSpace space;

    {
        configurer = new UrlSpaceConfigurer("/./gigaspace");
        IJSpace ijspace = configurer.space();
        space = new GigaSpaceConfigurer(ijspace).gigaSpace();
    }

    SampleDAO dao;

    @BeforeTest
    public void createDAO() {
        dao = new SampleDAOImpl();
        dao.setGigaspace(space);
    }

    @BeforeMethod
    public void cleanSpace() {
        space.clean();
    }

    @Test
    public void manageStats() {
        dao.reset();
        dao.getReads();
        dao.getWrites();
        dao.getTakes();

        BaseEntityStat be = new BaseEntityStat();
        BaseEntityStat b2 = new BaseEntityStat();
        initBaseEntityStat(be);
        initBaseEntityStat(b2);
        assertNotNull(be.hashCode());
        assertTrue(be.equals(b2));
        assertNotNull(be.toString());
        SampleEntity se = new SampleEntity();
        assertNotNull(se.toString());
    }

    private void initBaseEntityStat(BaseEntityStat be) {
        be.setId("a");
        be.setCreateTime(1L);
        be.setUpdateTime(2L);
    }


    @Test
    public void testWrite() {
        SampleEntity se = new SampleEntity();
        se.setText("this is a test");
        dao.write(se);
        assertNotNull(se.getId());
        assertNotNull(se.getCreateTime());
        assertNotNull(se.getUpdateTime());
    }

    /**
     * we can put a ton of tests in here because reads are nondestructive.
     */
    @Test(dependsOnMethods = "testWrite")
    public void testRead() {
        SampleEntity se = new SampleEntity();
        se.setText("sample text");
        dao.write(se);

        // single element reads
        SampleEntity s = dao.readById(se.getId());
        assertEquals(s.getText(), se.getText());

        SampleEntity template = new SampleEntity();
        template.setText("sample text");

        s = dao.read(template);
        assertEquals(s.getId(), se.getId());

        s = dao.read(se);
        assertEquals(s.getId(), se.getId());
        assertEquals(s.getText(), se.getText());

        s = dao.readByQuery("text='sample text'");
        assertNotNull(s);
        assertEquals(s.getId(), se.getId());
        assertEquals(s.getText(), se.getText());

        s = dao.readByQuery(" where text=?", "sample text");
        assertNotNull(s);
        assertEquals(s.getId(), se.getId());
        assertEquals(s.getText(), se.getText());

        s = dao.readByQuery("text=?", "sample texts");
        assertNull(s);

        // multiple reads
        SampleEntity se2 = new SampleEntity();
        se2.setText("other text");
        dao.write(se2);
    }

    @Test
    public void testTakeTemplate() {
        SampleEntity[] entities = {
                writeSample("sample text"),
                writeSample("other text")
        };
        // test template ...
        SampleEntity template = new SampleEntity();
        template.setText("sample text");
        SampleEntity s = dao.take(template);
        assertNotNull(s);
        assertEquals(s, entities[0]);

        s = dao.read(template);
        assertNull(s);

        s = dao.take(template);
        assertNull(s);
    }

    @Test
    public void testTakeTemplateWithId() {
        SampleEntity[] entities = {
                writeSample("sample text"),
                writeSample("other text")
        };

        // test template ...
        SampleEntity s = dao.take(entities[0]);
        assertNotNull(s);
        assertEquals(s, entities[0]);

        s = dao.read(entities[0]);
        assertNull(s);

        s = dao.take(entities[0]);
        assertNull(s);
    }

    @Test
    public void testTakeById() {
        SampleEntity[] entities = {
                writeSample("sample text"),
                writeSample("other text")
        };

        // test template ...
        SampleEntity s = dao.takeById(entities[0].getId());
        assertNotNull(s);
        assertEquals(s, entities[0]);

        s = dao.readById(entities[0].getId());
        assertNull(s);

        s = dao.takeById(entities[0].getId());
        assertNull(s);
    }


    @Test
    public void testTakeQuery() {
        SampleEntity[] entities = {
                writeSample("sample text"),
                writeSample("other text")
        };

        SampleEntity s = dao.takeByQuery("where text like 'sample text'");
        assertNotNull(s);
        assertEquals(s, entities[0]);

        s = dao.takeByQuery("where text like 'sample text'");
        assertNull(s);

        s = dao.read(entities[0]);
        assertNull(s);

        s = dao.take(entities[0]);
        assertNull(s);
    }

    private SampleEntity writeSample(String s) {
        SampleEntity se = new SampleEntity();
        se.setText(s);
        return dao.write(se);
    }

    @Test
    public void testReadMultiples() {
        SampleEntity[] entities = {
                writeSample("text"),
                writeSample("text"),
                writeSample("other text"),
        };
        SampleEntity template = new SampleEntity();
        SampleEntity[] results = dao.readMultiple(template);
        assertEquals(results.length, 3);

        template.setText("text");
        results = dao.readMultiple(template);
        assertEquals(results.length, 2);

        results = dao.readMultiple(template, 1);
        assertEquals(results.length, 1);
        assertEquals(results[0].getText(), "text");

        results = dao.readMultipleByQuery("text=?", Integer.MAX_VALUE, "text");
        assertEquals(results.length, 2);
    }

    @Test
    public void testTakeMultiples() {
        SampleEntity[] entities = {
                writeSample("text"),
                writeSample("text"),
                writeSample("other text"),
        };
        SampleEntity template = new SampleEntity();
        SampleEntity[] results = dao.takeMultiple(template);
        assertEquals(results.length, 3);

        results = dao.takeMultiple(template);
        assertEquals(results.length, 0);
    }

    @Test
    public void testTakeMultiples2() {
        SampleEntity[] entities = {
                writeSample("text"),
                writeSample("text"),
                writeSample("other text"),
        };
        SampleEntity template = new SampleEntity();
        template.setText("text");
        SampleEntity[] results = dao.takeMultiple(template);
        assertEquals(results.length, 2);

        results = dao.takeMultiple(template);
        assertEquals(results.length, 0);
    }

    @Test
    public void testTakeMultiples3() {
        SampleEntity[] entities = {
                writeSample("text"),
                writeSample("text"),
                writeSample("other text"),
        };
        SampleEntity template = new SampleEntity();
        template.setText("text");
        SampleEntity[] results = dao.takeMultiple(template, 1);
        assertEquals(results.length, 1);
        assertEquals(results[0].getText(), "text");

        results = dao.takeMultiple(template);
        assertEquals(results.length, 1);
    }

    @Test
    public void testUpdates() {
        SampleEntity entity = writeSample("foo bar");

        SampleEntity r = dao.readByQuery("text = 'foo bar'");
        assertNotNull(r);
        assertEquals(r.getText(), "foo bar");
        r.setText("baz bletch");
        dao.write(r);
        SampleEntity s = dao.readByQuery("text = 'foo bar'");
        assertNull(s);
        s = dao.readByQuery("text = 'baz bletch'");
        assertNotNull(s);
        assertEquals(s.getText(), "baz bletch");
    }

    @Test
    public void testUpdateMethod() {
        SampleEntity entity = writeSample("foo bar");

        SampleEntity r = dao.readByQuery("text = 'foo bar'");
        assertNotNull(r);
        assertEquals(r.getText(), "foo bar");
        r.setText("baz bletch");
        dao.update(r, 5000);
        SampleEntity s = dao.readByQuery("text = 'foo bar'");
        assertNull(s);
        s = dao.readByQuery("text = 'baz bletch'");
        assertNotNull(s);
        assertEquals(s.getText(), "baz bletch");
        SampleEntity t = new SampleEntity();
        t.setText("this is a test");
        dao.update(t);
        s = dao.readByQuery("text = 'this is a test'");
        assertNotNull(s);
        assertEquals(s.getText(), "this is a test");
    }

    @Test
    public void testMessaging() {
        SampleEntity s = new SampleEntity();
        s.setText("foo");
        dao.push(s);
        SampleEntity t = new SampleEntity();
        s.setText("bar");
        dao.push(s);

        SampleEntity template = new SampleEntity();
        template.setText("bar");

        SampleEntity r = dao.peek(template, 10);
        assertNotNull(r);

        r = dao.poll(template, 10);
        assertNotNull(r);

        r = dao.peek(template, 10);
        assertNull(r);
    }
}
