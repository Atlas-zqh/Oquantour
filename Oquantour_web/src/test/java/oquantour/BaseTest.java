package oquantour;

import junit.framework.TestCase;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 所有的测试类都要继承该类
 * <p>
 * 测试类写法
 *
 * @Autowired private MemberDao memberDao;
 * @Test public void testAdd() {
 * Member member = new Member();
 * member.setId(1234567);
 * member.setUsername("123");
 * member.setPassword("123");
 * memberDao.addMember(member);
 * }
 * <p>
 * Created by keenan on 11/05/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext.xml"})
public class BaseTest extends TestCase {
}
