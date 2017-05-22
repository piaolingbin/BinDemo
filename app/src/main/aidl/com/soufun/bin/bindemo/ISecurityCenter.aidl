// ISecurityCenter.aidl
package com.soufun.bin.bindemo;

// Declare any non-default types here with import statements

interface ISecurityCenter {
   String encrypt(String content);
   String decrypt(String password);
}
