package com.xhsoft.demo.web;

import com.xhsoft.demo.DemoWebApplication;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * 测试 Foo controller
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DemoWebApplication.class)
@WebAppConfiguration
@IntegrationTest
public class FooTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void testHelloFoo() throws Exception {
        MvcResult result = mockMvc.perform(get("/foo?name=Foo")
                .accept(MediaType.TEXT_HTML))
                .andExpect(view().name("foo")) //验证viewName
                .andExpect(status().isOk())
                .andReturn();

        Assert.assertNotNull(result.getModelAndView());
        Assert.assertEquals("Foo", result.getModelAndView().getModel().get("name"));
    }
}
