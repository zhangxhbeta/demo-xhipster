package com.xhsoft.demo.foo;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.convention.annotation.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;


/**
 * 测试 Controller
 */
@Action(value = "foo", results = {
        @Result(name = "result", location = "/foo.jsp") })
@Namespace("foo")
@Controller
@Scope("prototype")
public class FooAction extends ActionSupport {

    String name = "World";

    public String list() {
        return "result";
    }

    public String execute() { return "result"; }

}
