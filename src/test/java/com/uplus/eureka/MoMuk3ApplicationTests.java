//package com.uplus.eureka;
//
//import com.uplus.eureka.dao.VoteDao;
//import com.uplus.eureka.dto.VoteDto;
//import org.junit.jupiter.api.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//class MoMuk3ApplicationTests {
//    private static final Logger logger = LoggerFactory.getLogger(MoMuk3ApplicationTests.class);
//
//    @Autowired
//    private VoteDao voteDao;
//
//    @Test
//    void testFindAllActiveVotes() {
//        logger.debug("Testing findAllActiveVotes...");
//
//        List<VoteDto> activeVotes = voteDao.findAllActiveVotes();
//        assertNotNull(activeVotes, "Active votes should not be null");
//        assertTrue(activeVotes.size() > 0, "There should be at least one active vote");
//    }
//}
//
