// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: msg.proto

// Protobuf Java Version: 4.26.1
package com.hibob.anyim.netty.protobuf;

/**
 * <pre>
 * 当msgType为CHAT时
 * </pre>
 *
 * Protobuf type {@code com.hibob.anyim.netty.protobuf.ChatBody}
 */
public final class ChatBody extends
    com.google.protobuf.GeneratedMessage implements
    // @@protoc_insertion_point(message_implements:com.hibob.anyim.netty.protobuf.ChatBody)
    ChatBodyOrBuilder {
private static final long serialVersionUID = 0L;
  static {
    com.google.protobuf.RuntimeVersion.validateProtobufGencodeVersion(
      com.google.protobuf.RuntimeVersion.RuntimeDomain.PUBLIC,
      /* major= */ 4,
      /* minor= */ 26,
      /* patch= */ 1,
      /* suffix= */ "",
      ChatBody.class.getName());
  }
  // Use ChatBody.newBuilder() to construct.
  private ChatBody(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
    super(builder);
  }
  private ChatBody() {
    fromId_ = "";
    fromDev_ = "";
    toId_ = "";
    toDev_ = "";
    content_ = "";
  }

  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.hibob.anyim.netty.protobuf.MsgOuterClass.internal_static_com_hibob_anyim_netty_protobuf_ChatBody_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.hibob.anyim.netty.protobuf.MsgOuterClass.internal_static_com_hibob_anyim_netty_protobuf_ChatBody_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.hibob.anyim.netty.protobuf.ChatBody.class, com.hibob.anyim.netty.protobuf.ChatBody.Builder.class);
  }

  public static final int FROMID_FIELD_NUMBER = 1;
  @SuppressWarnings("serial")
  private volatile java.lang.Object fromId_ = "";
  /**
   * <code>string fromId = 1;</code>
   * @return The fromId.
   */
  @java.lang.Override
  public java.lang.String getFromId() {
    java.lang.Object ref = fromId_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      fromId_ = s;
      return s;
    }
  }
  /**
   * <code>string fromId = 1;</code>
   * @return The bytes for fromId.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString
      getFromIdBytes() {
    java.lang.Object ref = fromId_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      fromId_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int FROMDEV_FIELD_NUMBER = 2;
  @SuppressWarnings("serial")
  private volatile java.lang.Object fromDev_ = "";
  /**
   * <code>string fromDev = 2;</code>
   * @return The fromDev.
   */
  @java.lang.Override
  public java.lang.String getFromDev() {
    java.lang.Object ref = fromDev_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      fromDev_ = s;
      return s;
    }
  }
  /**
   * <code>string fromDev = 2;</code>
   * @return The bytes for fromDev.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString
      getFromDevBytes() {
    java.lang.Object ref = fromDev_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      fromDev_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int TOID_FIELD_NUMBER = 3;
  @SuppressWarnings("serial")
  private volatile java.lang.Object toId_ = "";
  /**
   * <code>string toId = 3;</code>
   * @return The toId.
   */
  @java.lang.Override
  public java.lang.String getToId() {
    java.lang.Object ref = toId_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      toId_ = s;
      return s;
    }
  }
  /**
   * <code>string toId = 3;</code>
   * @return The bytes for toId.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString
      getToIdBytes() {
    java.lang.Object ref = toId_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      toId_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int TODEV_FIELD_NUMBER = 4;
  @SuppressWarnings("serial")
  private volatile java.lang.Object toDev_ = "";
  /**
   * <code>string toDev = 4;</code>
   * @return The toDev.
   */
  @java.lang.Override
  public java.lang.String getToDev() {
    java.lang.Object ref = toDev_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      toDev_ = s;
      return s;
    }
  }
  /**
   * <code>string toDev = 4;</code>
   * @return The bytes for toDev.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString
      getToDevBytes() {
    java.lang.Object ref = toDev_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      toDev_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int SEQ_FIELD_NUMBER = 5;
  private int seq_ = 0;
  /**
   * <code>int32 seq = 5;</code>
   * @return The seq.
   */
  @java.lang.Override
  public int getSeq() {
    return seq_;
  }

  public static final int CONTENT_FIELD_NUMBER = 6;
  @SuppressWarnings("serial")
  private volatile java.lang.Object content_ = "";
  /**
   * <code>string content = 6;</code>
   * @return The content.
   */
  @java.lang.Override
  public java.lang.String getContent() {
    java.lang.Object ref = content_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      content_ = s;
      return s;
    }
  }
  /**
   * <code>string content = 6;</code>
   * @return The bytes for content.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString
      getContentBytes() {
    java.lang.Object ref = content_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      content_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  private byte memoizedIsInitialized = -1;
  @java.lang.Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @java.lang.Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (!com.google.protobuf.GeneratedMessage.isStringEmpty(fromId_)) {
      com.google.protobuf.GeneratedMessage.writeString(output, 1, fromId_);
    }
    if (!com.google.protobuf.GeneratedMessage.isStringEmpty(fromDev_)) {
      com.google.protobuf.GeneratedMessage.writeString(output, 2, fromDev_);
    }
    if (!com.google.protobuf.GeneratedMessage.isStringEmpty(toId_)) {
      com.google.protobuf.GeneratedMessage.writeString(output, 3, toId_);
    }
    if (!com.google.protobuf.GeneratedMessage.isStringEmpty(toDev_)) {
      com.google.protobuf.GeneratedMessage.writeString(output, 4, toDev_);
    }
    if (seq_ != 0) {
      output.writeInt32(5, seq_);
    }
    if (!com.google.protobuf.GeneratedMessage.isStringEmpty(content_)) {
      com.google.protobuf.GeneratedMessage.writeString(output, 6, content_);
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (!com.google.protobuf.GeneratedMessage.isStringEmpty(fromId_)) {
      size += com.google.protobuf.GeneratedMessage.computeStringSize(1, fromId_);
    }
    if (!com.google.protobuf.GeneratedMessage.isStringEmpty(fromDev_)) {
      size += com.google.protobuf.GeneratedMessage.computeStringSize(2, fromDev_);
    }
    if (!com.google.protobuf.GeneratedMessage.isStringEmpty(toId_)) {
      size += com.google.protobuf.GeneratedMessage.computeStringSize(3, toId_);
    }
    if (!com.google.protobuf.GeneratedMessage.isStringEmpty(toDev_)) {
      size += com.google.protobuf.GeneratedMessage.computeStringSize(4, toDev_);
    }
    if (seq_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(5, seq_);
    }
    if (!com.google.protobuf.GeneratedMessage.isStringEmpty(content_)) {
      size += com.google.protobuf.GeneratedMessage.computeStringSize(6, content_);
    }
    size += getUnknownFields().getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof com.hibob.anyim.netty.protobuf.ChatBody)) {
      return super.equals(obj);
    }
    com.hibob.anyim.netty.protobuf.ChatBody other = (com.hibob.anyim.netty.protobuf.ChatBody) obj;

    if (!getFromId()
        .equals(other.getFromId())) return false;
    if (!getFromDev()
        .equals(other.getFromDev())) return false;
    if (!getToId()
        .equals(other.getToId())) return false;
    if (!getToDev()
        .equals(other.getToDev())) return false;
    if (getSeq()
        != other.getSeq()) return false;
    if (!getContent()
        .equals(other.getContent())) return false;
    if (!getUnknownFields().equals(other.getUnknownFields())) return false;
    return true;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    hash = (37 * hash) + FROMID_FIELD_NUMBER;
    hash = (53 * hash) + getFromId().hashCode();
    hash = (37 * hash) + FROMDEV_FIELD_NUMBER;
    hash = (53 * hash) + getFromDev().hashCode();
    hash = (37 * hash) + TOID_FIELD_NUMBER;
    hash = (53 * hash) + getToId().hashCode();
    hash = (37 * hash) + TODEV_FIELD_NUMBER;
    hash = (53 * hash) + getToDev().hashCode();
    hash = (37 * hash) + SEQ_FIELD_NUMBER;
    hash = (53 * hash) + getSeq();
    hash = (37 * hash) + CONTENT_FIELD_NUMBER;
    hash = (53 * hash) + getContent().hashCode();
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.hibob.anyim.netty.protobuf.ChatBody parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.hibob.anyim.netty.protobuf.ChatBody parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.hibob.anyim.netty.protobuf.ChatBody parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.hibob.anyim.netty.protobuf.ChatBody parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.hibob.anyim.netty.protobuf.ChatBody parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.hibob.anyim.netty.protobuf.ChatBody parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.hibob.anyim.netty.protobuf.ChatBody parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage
        .parseWithIOException(PARSER, input);
  }
  public static com.hibob.anyim.netty.protobuf.ChatBody parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  public static com.hibob.anyim.netty.protobuf.ChatBody parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage
        .parseDelimitedWithIOException(PARSER, input);
  }

  public static com.hibob.anyim.netty.protobuf.ChatBody parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.hibob.anyim.netty.protobuf.ChatBody parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage
        .parseWithIOException(PARSER, input);
  }
  public static com.hibob.anyim.netty.protobuf.ChatBody parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @java.lang.Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(com.hibob.anyim.netty.protobuf.ChatBody prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @java.lang.Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessage.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * <pre>
   * 当msgType为CHAT时
   * </pre>
   *
   * Protobuf type {@code com.hibob.anyim.netty.protobuf.ChatBody}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessage.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:com.hibob.anyim.netty.protobuf.ChatBody)
      com.hibob.anyim.netty.protobuf.ChatBodyOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.hibob.anyim.netty.protobuf.MsgOuterClass.internal_static_com_hibob_anyim_netty_protobuf_ChatBody_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.hibob.anyim.netty.protobuf.MsgOuterClass.internal_static_com_hibob_anyim_netty_protobuf_ChatBody_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.hibob.anyim.netty.protobuf.ChatBody.class, com.hibob.anyim.netty.protobuf.ChatBody.Builder.class);
    }

    // Construct using com.hibob.anyim.netty.protobuf.ChatBody.newBuilder()
    private Builder() {

    }

    private Builder(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      super(parent);

    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      bitField0_ = 0;
      fromId_ = "";
      fromDev_ = "";
      toId_ = "";
      toDev_ = "";
      seq_ = 0;
      content_ = "";
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.hibob.anyim.netty.protobuf.MsgOuterClass.internal_static_com_hibob_anyim_netty_protobuf_ChatBody_descriptor;
    }

    @java.lang.Override
    public com.hibob.anyim.netty.protobuf.ChatBody getDefaultInstanceForType() {
      return com.hibob.anyim.netty.protobuf.ChatBody.getDefaultInstance();
    }

    @java.lang.Override
    public com.hibob.anyim.netty.protobuf.ChatBody build() {
      com.hibob.anyim.netty.protobuf.ChatBody result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.hibob.anyim.netty.protobuf.ChatBody buildPartial() {
      com.hibob.anyim.netty.protobuf.ChatBody result = new com.hibob.anyim.netty.protobuf.ChatBody(this);
      if (bitField0_ != 0) { buildPartial0(result); }
      onBuilt();
      return result;
    }

    private void buildPartial0(com.hibob.anyim.netty.protobuf.ChatBody result) {
      int from_bitField0_ = bitField0_;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        result.fromId_ = fromId_;
      }
      if (((from_bitField0_ & 0x00000002) != 0)) {
        result.fromDev_ = fromDev_;
      }
      if (((from_bitField0_ & 0x00000004) != 0)) {
        result.toId_ = toId_;
      }
      if (((from_bitField0_ & 0x00000008) != 0)) {
        result.toDev_ = toDev_;
      }
      if (((from_bitField0_ & 0x00000010) != 0)) {
        result.seq_ = seq_;
      }
      if (((from_bitField0_ & 0x00000020) != 0)) {
        result.content_ = content_;
      }
    }

    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof com.hibob.anyim.netty.protobuf.ChatBody) {
        return mergeFrom((com.hibob.anyim.netty.protobuf.ChatBody)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.hibob.anyim.netty.protobuf.ChatBody other) {
      if (other == com.hibob.anyim.netty.protobuf.ChatBody.getDefaultInstance()) return this;
      if (!other.getFromId().isEmpty()) {
        fromId_ = other.fromId_;
        bitField0_ |= 0x00000001;
        onChanged();
      }
      if (!other.getFromDev().isEmpty()) {
        fromDev_ = other.fromDev_;
        bitField0_ |= 0x00000002;
        onChanged();
      }
      if (!other.getToId().isEmpty()) {
        toId_ = other.toId_;
        bitField0_ |= 0x00000004;
        onChanged();
      }
      if (!other.getToDev().isEmpty()) {
        toDev_ = other.toDev_;
        bitField0_ |= 0x00000008;
        onChanged();
      }
      if (other.getSeq() != 0) {
        setSeq(other.getSeq());
      }
      if (!other.getContent().isEmpty()) {
        content_ = other.content_;
        bitField0_ |= 0x00000020;
        onChanged();
      }
      this.mergeUnknownFields(other.getUnknownFields());
      onChanged();
      return this;
    }

    @java.lang.Override
    public final boolean isInitialized() {
      return true;
    }

    @java.lang.Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      if (extensionRegistry == null) {
        throw new java.lang.NullPointerException();
      }
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            case 10: {
              fromId_ = input.readStringRequireUtf8();
              bitField0_ |= 0x00000001;
              break;
            } // case 10
            case 18: {
              fromDev_ = input.readStringRequireUtf8();
              bitField0_ |= 0x00000002;
              break;
            } // case 18
            case 26: {
              toId_ = input.readStringRequireUtf8();
              bitField0_ |= 0x00000004;
              break;
            } // case 26
            case 34: {
              toDev_ = input.readStringRequireUtf8();
              bitField0_ |= 0x00000008;
              break;
            } // case 34
            case 40: {
              seq_ = input.readInt32();
              bitField0_ |= 0x00000010;
              break;
            } // case 40
            case 50: {
              content_ = input.readStringRequireUtf8();
              bitField0_ |= 0x00000020;
              break;
            } // case 50
            default: {
              if (!super.parseUnknownField(input, extensionRegistry, tag)) {
                done = true; // was an endgroup tag
              }
              break;
            } // default:
          } // switch (tag)
        } // while (!done)
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.unwrapIOException();
      } finally {
        onChanged();
      } // finally
      return this;
    }
    private int bitField0_;

    private java.lang.Object fromId_ = "";
    /**
     * <code>string fromId = 1;</code>
     * @return The fromId.
     */
    public java.lang.String getFromId() {
      java.lang.Object ref = fromId_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        fromId_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string fromId = 1;</code>
     * @return The bytes for fromId.
     */
    public com.google.protobuf.ByteString
        getFromIdBytes() {
      java.lang.Object ref = fromId_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        fromId_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string fromId = 1;</code>
     * @param value The fromId to set.
     * @return This builder for chaining.
     */
    public Builder setFromId(
        java.lang.String value) {
      if (value == null) { throw new NullPointerException(); }
      fromId_ = value;
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     * <code>string fromId = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearFromId() {
      fromId_ = getDefaultInstance().getFromId();
      bitField0_ = (bitField0_ & ~0x00000001);
      onChanged();
      return this;
    }
    /**
     * <code>string fromId = 1;</code>
     * @param value The bytes for fromId to set.
     * @return This builder for chaining.
     */
    public Builder setFromIdBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) { throw new NullPointerException(); }
      checkByteStringIsUtf8(value);
      fromId_ = value;
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }

    private java.lang.Object fromDev_ = "";
    /**
     * <code>string fromDev = 2;</code>
     * @return The fromDev.
     */
    public java.lang.String getFromDev() {
      java.lang.Object ref = fromDev_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        fromDev_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string fromDev = 2;</code>
     * @return The bytes for fromDev.
     */
    public com.google.protobuf.ByteString
        getFromDevBytes() {
      java.lang.Object ref = fromDev_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        fromDev_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string fromDev = 2;</code>
     * @param value The fromDev to set.
     * @return This builder for chaining.
     */
    public Builder setFromDev(
        java.lang.String value) {
      if (value == null) { throw new NullPointerException(); }
      fromDev_ = value;
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     * <code>string fromDev = 2;</code>
     * @return This builder for chaining.
     */
    public Builder clearFromDev() {
      fromDev_ = getDefaultInstance().getFromDev();
      bitField0_ = (bitField0_ & ~0x00000002);
      onChanged();
      return this;
    }
    /**
     * <code>string fromDev = 2;</code>
     * @param value The bytes for fromDev to set.
     * @return This builder for chaining.
     */
    public Builder setFromDevBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) { throw new NullPointerException(); }
      checkByteStringIsUtf8(value);
      fromDev_ = value;
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }

    private java.lang.Object toId_ = "";
    /**
     * <code>string toId = 3;</code>
     * @return The toId.
     */
    public java.lang.String getToId() {
      java.lang.Object ref = toId_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        toId_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string toId = 3;</code>
     * @return The bytes for toId.
     */
    public com.google.protobuf.ByteString
        getToIdBytes() {
      java.lang.Object ref = toId_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        toId_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string toId = 3;</code>
     * @param value The toId to set.
     * @return This builder for chaining.
     */
    public Builder setToId(
        java.lang.String value) {
      if (value == null) { throw new NullPointerException(); }
      toId_ = value;
      bitField0_ |= 0x00000004;
      onChanged();
      return this;
    }
    /**
     * <code>string toId = 3;</code>
     * @return This builder for chaining.
     */
    public Builder clearToId() {
      toId_ = getDefaultInstance().getToId();
      bitField0_ = (bitField0_ & ~0x00000004);
      onChanged();
      return this;
    }
    /**
     * <code>string toId = 3;</code>
     * @param value The bytes for toId to set.
     * @return This builder for chaining.
     */
    public Builder setToIdBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) { throw new NullPointerException(); }
      checkByteStringIsUtf8(value);
      toId_ = value;
      bitField0_ |= 0x00000004;
      onChanged();
      return this;
    }

    private java.lang.Object toDev_ = "";
    /**
     * <code>string toDev = 4;</code>
     * @return The toDev.
     */
    public java.lang.String getToDev() {
      java.lang.Object ref = toDev_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        toDev_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string toDev = 4;</code>
     * @return The bytes for toDev.
     */
    public com.google.protobuf.ByteString
        getToDevBytes() {
      java.lang.Object ref = toDev_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        toDev_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string toDev = 4;</code>
     * @param value The toDev to set.
     * @return This builder for chaining.
     */
    public Builder setToDev(
        java.lang.String value) {
      if (value == null) { throw new NullPointerException(); }
      toDev_ = value;
      bitField0_ |= 0x00000008;
      onChanged();
      return this;
    }
    /**
     * <code>string toDev = 4;</code>
     * @return This builder for chaining.
     */
    public Builder clearToDev() {
      toDev_ = getDefaultInstance().getToDev();
      bitField0_ = (bitField0_ & ~0x00000008);
      onChanged();
      return this;
    }
    /**
     * <code>string toDev = 4;</code>
     * @param value The bytes for toDev to set.
     * @return This builder for chaining.
     */
    public Builder setToDevBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) { throw new NullPointerException(); }
      checkByteStringIsUtf8(value);
      toDev_ = value;
      bitField0_ |= 0x00000008;
      onChanged();
      return this;
    }

    private int seq_ ;
    /**
     * <code>int32 seq = 5;</code>
     * @return The seq.
     */
    @java.lang.Override
    public int getSeq() {
      return seq_;
    }
    /**
     * <code>int32 seq = 5;</code>
     * @param value The seq to set.
     * @return This builder for chaining.
     */
    public Builder setSeq(int value) {

      seq_ = value;
      bitField0_ |= 0x00000010;
      onChanged();
      return this;
    }
    /**
     * <code>int32 seq = 5;</code>
     * @return This builder for chaining.
     */
    public Builder clearSeq() {
      bitField0_ = (bitField0_ & ~0x00000010);
      seq_ = 0;
      onChanged();
      return this;
    }

    private java.lang.Object content_ = "";
    /**
     * <code>string content = 6;</code>
     * @return The content.
     */
    public java.lang.String getContent() {
      java.lang.Object ref = content_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        content_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string content = 6;</code>
     * @return The bytes for content.
     */
    public com.google.protobuf.ByteString
        getContentBytes() {
      java.lang.Object ref = content_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        content_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string content = 6;</code>
     * @param value The content to set.
     * @return This builder for chaining.
     */
    public Builder setContent(
        java.lang.String value) {
      if (value == null) { throw new NullPointerException(); }
      content_ = value;
      bitField0_ |= 0x00000020;
      onChanged();
      return this;
    }
    /**
     * <code>string content = 6;</code>
     * @return This builder for chaining.
     */
    public Builder clearContent() {
      content_ = getDefaultInstance().getContent();
      bitField0_ = (bitField0_ & ~0x00000020);
      onChanged();
      return this;
    }
    /**
     * <code>string content = 6;</code>
     * @param value The bytes for content to set.
     * @return This builder for chaining.
     */
    public Builder setContentBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) { throw new NullPointerException(); }
      checkByteStringIsUtf8(value);
      content_ = value;
      bitField0_ |= 0x00000020;
      onChanged();
      return this;
    }

    // @@protoc_insertion_point(builder_scope:com.hibob.anyim.netty.protobuf.ChatBody)
  }

  // @@protoc_insertion_point(class_scope:com.hibob.anyim.netty.protobuf.ChatBody)
  private static final com.hibob.anyim.netty.protobuf.ChatBody DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.hibob.anyim.netty.protobuf.ChatBody();
  }

  public static com.hibob.anyim.netty.protobuf.ChatBody getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<ChatBody>
      PARSER = new com.google.protobuf.AbstractParser<ChatBody>() {
    @java.lang.Override
    public ChatBody parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      Builder builder = newBuilder();
      try {
        builder.mergeFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(builder.buildPartial());
      } catch (com.google.protobuf.UninitializedMessageException e) {
        throw e.asInvalidProtocolBufferException().setUnfinishedMessage(builder.buildPartial());
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(e)
            .setUnfinishedMessage(builder.buildPartial());
      }
      return builder.buildPartial();
    }
  };

  public static com.google.protobuf.Parser<ChatBody> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<ChatBody> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.hibob.anyim.netty.protobuf.ChatBody getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

