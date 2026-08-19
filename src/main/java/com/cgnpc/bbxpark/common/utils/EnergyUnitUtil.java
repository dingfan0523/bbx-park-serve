package com.cgnpc.bbxpark.common.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 能源数值单位换算工具（KWh / MWh / GWh）
 */
public class EnergyUnitUtil {

    /**
     * 持有换算后的数值和单位
     */
    public static class EnergyValue {
        private final BigDecimal value;
        private final String unit;

        public EnergyValue(BigDecimal value, String unit) {
            this.value = value;
            this.unit = unit;
        }

        public BigDecimal getValue() {
            return value;
        }

        public String getUnit() {
            return unit;
        }
    }

    private static final BigDecimal THOUSAND = BigDecimal.valueOf(1000);
    private static final BigDecimal MILLION = BigDecimal.valueOf(1_000_000);
    private static final int SCALE = 2;

    /**
     * 将电量值（单位：KWh）转换为合适的单位（KWh / MWh / GWh）
     *
     * @param energyKWh 电量值，单位 KWh
     * @return 包含换算后数值和单位的对象
     */
    public static EnergyValue formatEnergy(BigDecimal energyKWh) {
        if (energyKWh == null) {
            return new EnergyValue(BigDecimal.ZERO, "KWh");
        }
        // 绝对值比较，保留符号
        BigDecimal abs = energyKWh.abs();
        if (abs.compareTo(MILLION) >= 0) {
            return new EnergyValue(energyKWh.divide(MILLION, SCALE, RoundingMode.HALF_UP), "GWh");
        } else if (abs.compareTo(THOUSAND) >= 0) {
            return new EnergyValue(energyKWh.divide(THOUSAND, SCALE, RoundingMode.HALF_UP), "MWh");
        } else {
            return new EnergyValue(energyKWh.setScale(SCALE, RoundingMode.HALF_UP), "KWh");
        }
    }

    /**
     * 根据数值获取单位
     * @param value 数值
     * @return 单位
     */
    public static String getUnit(BigDecimal value) {
        if (value == null) {
            return "KWh";
        }
        // 绝对值比较，保留符号
        BigDecimal abs = value.abs();
        if (abs.compareTo(MILLION) >= 0) {
            return "GWh";
        } else if (abs.compareTo(THOUSAND) >= 0) {
            return "MWh";
        }
        return "KWh";
    }

    public static BigDecimal conversion(BigDecimal value,String unit){
        if(StringUtils.isEmpty(unit) || "KWh".equals(unit)){
            return value;
        }else if("MWh".equals(unit)){
            return value.divide(THOUSAND, SCALE, RoundingMode.HALF_UP);
        }
        return value.divide(MILLION, SCALE, RoundingMode.HALF_UP);
    }
}
