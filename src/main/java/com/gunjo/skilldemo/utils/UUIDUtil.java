package com.gunjo.skilldemo.utils;

import java.util.*;

public class UUIDUtil {

    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
