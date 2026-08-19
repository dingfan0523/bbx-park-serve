package com.cgnpc.bbxpark.workbench.controller;

import com.cgnpc.cud.core.controller.BaseController;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
public class QSCABaseController extends BaseController {

    /**
     * 修复静态扫描漏洞
     */
    @InitBinder
    public void registerCustomRequest(WebDataBinder binder) {
        binder.setDisallowedFields(new String[]{});
    }
}
