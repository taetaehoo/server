package service;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.Reader;

public class SqlMapConfig {
    public static SqlSessionFactory sqlSession;

    static {
        String resource = "config/config.xml";
        try {
            Reader reader = Resources.getResourceAsReader(resource);
            sqlSession = new SqlSessionFactoryBuilder().build(reader);

            Class[] mapper = {
                    openedMapperInter.class
            };

            for (Class m: mapper) {
                sqlSession.getConfiguration().addMapper(m);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SqlSessionFactory getSqlSession() {
        return sqlSession;
    }
}
