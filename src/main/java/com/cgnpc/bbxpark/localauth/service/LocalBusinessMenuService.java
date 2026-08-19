package com.cgnpc.bbxpark.localauth.service;

import com.cgnpc.cud.form.common.dto.form.req.ReqGridMenuDto;
import com.cgnpc.cud.form.manage.service.impl.CudMenuManagerServiceImpl;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@Primary
@Profile("local-auth")
public class LocalBusinessMenuService extends CudMenuManagerServiceImpl {

    @Override
    public List<Map<String, Object>> getFormMenuTree(ReqGridMenuDto request) {
        return Collections.emptyList();
    }
}
