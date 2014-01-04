package com.apesRise.hotPointer.thrift.crawler_gen;

/**
 * Autogenerated by Thrift Compiler (0.9.1)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */

import java.util.Map;
import java.util.HashMap;
import org.apache.thrift.TEnum;

public enum Type implements org.apache.thrift.TEnum {
  Weibo(0),
  News(1),
  BBS(2),
  Weixin(3),
  QQ(4),
  Edit(5),
  Other(6);

  private final int value;

  private Type(int value) {
    this.value = value;
  }

  /**
   * Get the integer value of this enum value, as defined in the Thrift IDL.
   */
  public int getValue() {
    return value;
  }

  /**
   * Find a the enum type by its integer value, as defined in the Thrift IDL.
   * @return null if the value is not found.
   */
  public static Type findByValue(int value) { 
    switch (value) {
      case 0:
        return Weibo;
      case 1:
        return News;
      case 2:
        return BBS;
      case 3:
        return Weixin;
      case 4:
        return QQ;
      case 5:
        return Edit;
      case 6:
        return Other;
      default:
        return null;
    }
  }
}