/*
 * Copyright (c) 2011-2020, baomidou (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.cgnpc.bbxpark.acl.haikang.model;

import lombok.Data;

import java.util.Collections;
import java.util.List;


/**
 * 海康page
 * @param <T>
 */
@Data
public class HkPage<T> {


    /**
     * 查询数据列表
     */
    protected List<T> list = Collections.emptyList();

    /**
     * 总数
     */
    protected long total = 0;
    /**
     * 每页显示条数，默认 10
     */
    protected long pageSize = 10;

    /**
     * 当前页
     */
    protected long pageNo = 1;

}
