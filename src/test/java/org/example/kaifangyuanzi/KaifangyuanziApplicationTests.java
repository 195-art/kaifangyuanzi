package org.example.kaifangyuanzi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {"spring.sql.init.mode=never", "jwt.secret=test-key-012345678901234567890123456789", "spring.datasource.password=test"})
@AutoConfigureMockMvc
class KaifangyuanziApplicationTests {
    @Autowired MockMvc mvc;

    @Test
    void contextLoads() {
    }

    @Test
    void unauthenticatedWriteUsesUnifiedResponse() throws Exception {
        mvc.perform(post("/Q11/event")).andExpect(status().isUnauthorized()).andExpect(jsonPath("$.code").value(401));
    }

    @Test
    void malformedIdUsesUnifiedResponse() throws Exception {
        mvc.perform(get("/Q11/event/not-a-number")).andExpect(status().isBadRequest()).andExpect(jsonPath("$.code").value(400));
    }

    @Test
    void invalidTokenDoesNotLogIn() throws Exception {
        mvc.perform(post("/Q10/auth/login").header("token", "expired-token"))
                .andExpect(status().isUnauthorized()).andExpect(jsonPath("$.code").value(401));
    }

    @Test
    void unknownRouteUsesUnifiedResponse() throws Exception {
        mvc.perform(get("/Q11/event/missing/route"))
                .andExpect(status().isNotFound()).andExpect(jsonPath("$.code").value(404));
    }
}
