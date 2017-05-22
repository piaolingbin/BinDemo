// IBinderPool.aidl
package com.soufun.bin.bindemo;

// Declare any non-default types here with import statements

interface IBinderPool {
    IBinder queryBinder(int binderCode);
}
