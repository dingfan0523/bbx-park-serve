
package com.cgnpc.bbxpark.restaurant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.ioc.dto.model.RestaurantCompartmentReserveModel;
import com.cgnpc.bbxpark.restaurant.domain.CompartmentReserve;
import com.cgnpc.bbxpark.restaurant.dto.model.CompartmentReserveModel;
import com.cgnpc.bbxpark.restaurant.dto.param.CompartmentReserveListParam;
import com.cgnpc.bbxpark.restaurant.dto.param.CompartmentReservePageParam;
import com.cgnpc.bbxpark.restaurant.dto.param.CompartmentReserveParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/***
 * @Description 包间预定数据操作接口
 * @author huangyongtao
 * @date 2024/7/30 14:56
 */
@Mapper
public interface CompartmentReserveRepository extends BaseMapper<CompartmentReserve> {

   /***
    * @Description 分页查询包间预约的信息
    * @author huangyongtao
    * @date 2024/7/31 14:09
    * @param page
    * @param condition
    */
    IPage<CompartmentReserveModel> pageReserve(IPage<CompartmentReserveModel> page, @Param("condition") CompartmentReservePageParam condition);

    /***
     * @Description 查询包间预约信息
     * @author huangyongtao
     * @date 2024/7/31 15:00
     * @param condition
     */
    List<CompartmentReserveModel> findReserve(@Param("condition") CompartmentReserveListParam condition);

   /***
    * @Description 查询时间端内包间的预约信息
    * @author huangyongtao
    * @date 2024/8/2 9:13
    * @param condition
    */
    List<CompartmentReserveModel> findByTime(@Param("condition") CompartmentReserveParam condition);

    /**
     * 近30天包间预约数量
     * @param tenantId
     * @return
     */
    List<RestaurantCompartmentReserveModel> getCompartmentReserveTrend(@Param("tenantId") Long tenantId);

}
