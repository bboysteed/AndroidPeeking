// IService.aidl
package com.example.serviceprovider;

// Declare any non-default types here with import statements

interface IService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    String getName();
    int    testAIDL(int num);
}