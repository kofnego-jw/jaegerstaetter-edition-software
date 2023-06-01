package at.kulinz.jaegerstaetter.index.service.impl;

import org.apache.commons.lang3.StringUtils;

import java.text.Normalizer;

public class Normalization {

    public static String nfc(String s) {
        if (StringUtils.isBlank(s)) {
            return "";
        }
        return Normalizer.normalize(s, Normalizer.Form.NFC);
    }
}
