package io.github.wxrlock.conversationframework.utils;

import com.google.common.collect.ImmutableList;
import lombok.experimental.UtilityClass;

import java.math.RoundingMode;
import java.text.DecimalFormat;

@UtilityClass
public class NumberFormatter {

    private static final ImmutableList<String> SUFFIXES = ImmutableList.of(
            "",
            "K",
            "M",
            "B",
            "T",
            "Q",
            "QQ",
            "S",
            "SS",
            "O",
            "N",
            "D",
            "UD",
            "DD",
            "TR",
            "QD",
            "QN",
            "SD",
            "SPD",
            "OD",
            "ND",
            "VG",
            "UVG",
            "DVG",
            "TVG",
            "QTV",
            "QNV",
            "SEV",
            "SPV"
    );

    private static final DecimalFormat SUFFIX_FORMAT = new DecimalFormat("#.##");

    static {
        SUFFIX_FORMAT.setRoundingMode(RoundingMode.FLOOR);
    }

    public static String applySuffix(double value) {
        int index = 0;
        while (value >= 1000 && index < SUFFIXES.size() - 1) {
            value /= 1000;
            index++;
        }
        return SUFFIX_FORMAT.format(value).replace(",", ".") + SUFFIXES.get(index);
    }

}