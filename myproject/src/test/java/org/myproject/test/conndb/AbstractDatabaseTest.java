package org.myproject.test.conndb;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/ctx-myproject-test-conndb.xml"})
@TransactionConfiguration(defaultRollback = false)
@Transactional
public abstract class AbstractDatabaseTest {


}
