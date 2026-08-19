package com.cgnpc.bbxpark.common.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cgnpc.cud.core.common.util.StringUtils;
import com.cgnpc.cud.core.dto.BaseDto;
import io.swagger.annotations.ApiModelProperty;

public class CudPageDto<T> extends BaseDto {
    @ApiModelProperty("当前第几页")
    protected long current = 1L;
    @ApiModelProperty("每页显示多少条数")
    protected long size = 10L;
    @ApiModelProperty("排序字段，多个逗号分隔name, createTime")
    protected String sort;
    @ApiModelProperty("升序降序，多个逗号分隔 asc, desc")
    protected String order;

    public long getCurrent() {
        return this.current;
    }

    public void setCurrent(long current) {
        this.current = (long)current;
    }

    public long getSize() {
        return this.size;
    }

    public void setSize(long size) {
        this.size = (long)size;
    }

    protected String getSort() {
        return this.sort;
    }

    protected void setSort(String sort) {
        this.sort = sort;
    }

    protected String getOrder() {
        return this.order;
    }

    protected void setOrder(String order) {
        this.order = order;
    }

    @ApiModelProperty(
            hidden = true
    )
    public IPage<T> getPage() {
        IPage<T> page = new Page(this.current, this.size);
        return page;
    }

    public void genSort(QueryWrapper wrapper) {
        if (!StringUtils.isEmpty(this.sort)) {
            String[] sorts = this.sort.split(",");
            String[] orders = this.order.split(",");

            for(int i = 0; i < sorts.length; ++i) {
                if ("ASC".equals(orders[i])) {
                    wrapper.orderByAsc(sorts[i]);
                } else if ("DESC".equals(orders[i])) {
                    wrapper.orderByDesc(sorts[i]);
                }
            }
        }

    }
}
