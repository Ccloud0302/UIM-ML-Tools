package com.markerhub;

import com.markerhub.entity.Sservice;
import com.markerhub.service.SserviceService;

import com.markerhub.util.Neo4jConfig;
import com.markerhub.util.Neo4jUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VueblogApplicationTests {

    @Autowired
    private SserviceService sserviceService;

    @Test
    public void contextLoads() {
        Sservice sservice = new Sservice();
        sservice.setName("test2");
        boolean b = sserviceService.save(sservice);
        System.out.println(b);
    }

    @Test
    public void neo4jTest() {
        Driver driver = GraphDatabase.driver( "bolt://localhost:7687", AuthTokens.basic( "neo4j", "neo4j123" ) );
        Session session = driver.session();
        System.out.println(session.isOpen());
    }

}
