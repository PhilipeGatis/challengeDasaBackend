package com.philipe.dasa.labor.labormanager.web.domain;

import org.springframework.lang.Nullable;

public enum ActiveStatus {
    ACTIVE(0),
    INACTIVE(1);

    private int status;

    ActiveStatus(int status) {
        this.status = status;
    }

    public int value() {
        return status;
    }

    public static ActiveStatus get(int value) {
        ActiveStatus status = resolve(value);
        if (status == null) {
            throw new IllegalArgumentException("No matching constant for [" + value + "]");
        } else {
            return status;
        }
    }

    @Nullable
    private static ActiveStatus resolve(int statusCode) {
        ActiveStatus[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            ActiveStatus status = var1[var3];
            if (status.value() == statusCode) {
                return status;
            }
        }

        return null;
    }
}
