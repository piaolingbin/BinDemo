package com.soufun.bin.bindemo;

import android.os.RemoteException;

/**
 * @描述
 * @入口
 * @进入要传的值
 * @备注说明
 */

public class ComputeImpl extends ICompute.Stub{
    @Override
    public int add(int a, int b) throws RemoteException {
        return a + b;
    }
}
