// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: msg.proto

// Protobuf Java Version: 4.26.1
package com.hibob.anyim.netty.protobuf;

public interface HeaderOrBuilder extends
    // @@protoc_insertion_point(interface_extends:com.hibob.anyim.netty.protobuf.Header)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>int32 magic = 1;</code>
   * @return The magic.
   */
  int getMagic();

  /**
   * <code>int32 version = 2;</code>
   * @return The version.
   */
  int getVersion();

  /**
   * <code>int32 msgType = 3;</code>
   * @return The msgType.
   */
  int getMsgType();

  /**
   * <code>bool isExtension = 4;</code>
   * @return The isExtension.
   */
  boolean getIsExtension();
}
