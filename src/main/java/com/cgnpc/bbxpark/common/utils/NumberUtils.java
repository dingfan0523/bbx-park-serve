/**
 * Copyright © 2024-2025 AsiaInfo Technologies Limited. All Rights Reserved. - This software is the confidential and
 * proprietary information of AsiaInfo Technologies Limited. ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the terms of the license agreement you entered into
 * with asiainfo. - ASIAINFO MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE SOFTWARE, EITHER
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT.ASIAINFO SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING,
 * MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */
package com.cgnpc.bbxpark.common.utils;

import cn.hutool.core.util.StrUtil;



/**
 * 数字的工具类，补全 {@link cn.hutool.core.util.NumberUtil} 的功能
 *
 */
public class NumberUtils {

    public static Long parseLong(String str) {
        return StrUtil.isNotEmpty(str) ? Long.valueOf(str) : null;
    }

}
