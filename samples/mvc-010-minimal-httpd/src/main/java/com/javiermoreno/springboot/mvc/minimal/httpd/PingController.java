/*
 * Copyright 2014, Javier Moreno.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 * And have fun ;-)
 */
package com.javiermoreno.springboot.mvc.minimal.httpd;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 *
 * @author ciberado
 */
@Controller
@RequestMapping("/ping")
public class PingController {

    
    @RequestMapping(value="/short", method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody 
    String[] shortPing() {
        return new String[]{String.format("Current server time: %s.", new Date())};
    }

    @RequestMapping(value="/long", method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    void longPing(HttpServletResponse response) throws IOException, InterruptedException {
        response.setBufferSize(0);
        PrintWriter out = response.getWriter();
        out.println();
        out.flush();
        for (int i=0; i < 60*5; i++) {
            out.format("Current server time: %s.\r\n", new Date());
            out.flush();
            Thread.sleep(1000);
        }
    }
    
    
}
