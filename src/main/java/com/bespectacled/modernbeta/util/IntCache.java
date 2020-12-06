package com.bespectacled.modernbeta.util;

import java.util.ArrayList;
import java.util.List;

public class IntCache {
    private static int intCacheSize = 256;
    private static List field_35271_b = new ArrayList();
    private static List field_35272_c = new ArrayList();
    private static List field_35269_d = new ArrayList();
    private static List field_35270_e = new ArrayList();

    public IntCache()
    {
    }

    public static int[] getIntCache(int i)
    {
        if(i <= 256)
        {
            if(field_35271_b.size() == 0)
            {
                int ai[] = new int[256];
                field_35272_c.add(ai);
                return ai;
            } else
            {
                int ai1[] = (int[])field_35271_b.remove(field_35271_b.size() - 1);
                field_35272_c.add(ai1);
                return ai1;
            }
        }
        if(i > intCacheSize)
        {
            intCacheSize = i;
            field_35269_d.clear();
            field_35270_e.clear();
            int ai2[] = new int[intCacheSize];
            field_35270_e.add(ai2);
            return ai2;
        }
        if(field_35269_d.size() == 0)
        {
            int ai3[] = new int[intCacheSize];
            field_35270_e.add(ai3);
            return ai3;
        } else
        {
            int ai4[] = (int[])field_35269_d.remove(field_35269_d.size() - 1);
            field_35270_e.add(ai4);
            return ai4;
        }
    }

    public static void func_35268_a()
    {
        if(field_35269_d.size() > 0)
        {
            field_35269_d.remove(field_35269_d.size() - 1);
        }
        if(field_35271_b.size() > 0)
        {
            field_35271_b.remove(field_35271_b.size() - 1);
        }
        field_35269_d.addAll(field_35270_e);
        field_35271_b.addAll(field_35272_c);
        field_35270_e.clear();
        field_35272_c.clear();
    }
}
