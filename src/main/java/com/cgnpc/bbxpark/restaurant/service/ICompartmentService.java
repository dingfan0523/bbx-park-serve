
package com.cgnpc.bbxpark.restaurant.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.restaurant.domain.Compartment;
import com.cgnpc.bbxpark.restaurant.dto.model.*;
import com.cgnpc.bbxpark.restaurant.dto.param.AppComboListParam;
import com.cgnpc.bbxpark.restaurant.dto.param.CompartmentListParam;
import com.cgnpc.bbxpark.restaurant.dto.param.CompartmentPageParam;
import com.cgnpc.bbxpark.restaurant.dto.param.CompartmentParam;

import java.util.List;
import java.util.Map;


public interface ICompartmentService extends IService<Compartment> {


    IPage<CompartmentModel> pageResult(CompartmentPageParam param);


    List<CompartmentModel> list(CompartmentListParam param);

    Long save(CompartmentParam param);

    Boolean edit(CompartmentParam param);

    AppCompartmentModel detailApp(Long id);

    Boolean deleteById(Long id);

    CompartmentModel get(Long id);

    List<CompartmentComboModel> getCombolistByIds(List<Long> compartmentIds);

    List<AppComboModel> findComboListById(AppComboListParam param);

    Map<Long,List<AppCompartmentDeviceModel>> getDeviceMap(List<Long> compartmentIdList);

    Boolean disable(Long id);
}
