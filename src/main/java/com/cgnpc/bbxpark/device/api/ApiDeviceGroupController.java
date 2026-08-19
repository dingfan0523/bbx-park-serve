
package com.cgnpc.bbxpark.device.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.device.dto.model.DeviceGroupTreeModel;
import com.cgnpc.bbxpark.device.dto.model.IocDeviceModel;
import com.cgnpc.bbxpark.device.dto.param.DeviceGroupListParam;
import com.cgnpc.bbxpark.device.dto.param.IocDevicePageParam;
import com.cgnpc.bbxpark.device.service.IDeviceGroupRelService;
import com.cgnpc.bbxpark.device.service.IDeviceGroupService;
import com.cgnpc.mobile.annotation.RequiredToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/***
 * @Description 设备分组服务控制类
 * @author huangyongtao
 * @date 2024/8/12 17:31
 */
@RestController
@RequestMapping("/api/device/group")
@Api(tags = "设备分组")
public class ApiDeviceGroupController {

    /**
     * Logger.
     */

    /**
     * 设备分组服务接口.
     */
    @Autowired
    private IDeviceGroupService deviceGroupService;

    @Autowired
    private IDeviceGroupRelService deviceGroupRelService;

    /***
     * @Description 分页查询设备分组设备列表(对外提供)
     * @author huangyongtao
     * @date 2024/8/13 10:30
     * @param param
     */
    @ApiOperation(value = "分页查询设备分组设备列表(对外提供)")
    @PostMapping(value = "/pageGroupDevice")
    @RequiredToken
    public CudResult<IPage<IocDeviceModel>> pageGroupDevice(@RequestBody IocDevicePageParam param) {
        return CudResult.success(deviceGroupRelService.pageGroupDevice(param));
    }

    /**
     *  查询设备分组树
     */
    @ApiOperation(value = "查询设备分组树")
    @PostMapping(value = "/findTree")
    @RequiredToken
    public CudResult<List<DeviceGroupTreeModel>> findTree(@RequestBody DeviceGroupListParam param) {
        return CudResult.success(deviceGroupService.findTree(param));
    }
}
