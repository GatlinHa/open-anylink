// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: msg.proto

// Protobuf Java Version: 4.26.1
package com.hibob.anylink.common.protobuf;

/**
 * Protobuf type {@code com.hibob.anylink.common.protobuf.Msg}
 */
public final class Msg extends
    com.google.protobuf.GeneratedMessage implements
    // @@protoc_insertion_point(message_implements:com.hibob.anylink.common.protobuf.Msg)
    MsgOrBuilder {
private static final long serialVersionUID = 0L;
  static {
    com.google.protobuf.RuntimeVersion.validateProtobufGencodeVersion(
      com.google.protobuf.RuntimeVersion.RuntimeDomain.PUBLIC,
      /* major= */ 4,
      /* minor= */ 26,
      /* patch= */ 1,
      /* suffix= */ "",
      Msg.class.getName());
  }
  // Use Msg.newBuilder() to construct.
  private Msg(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
    super(builder);
  }
  private Msg() {
  }

  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.hibob.anylink.common.protobuf.MsgOuterClass.internal_static_com_hibob_anylink_common_protobuf_Msg_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.hibob.anylink.common.protobuf.MsgOuterClass.internal_static_com_hibob_anylink_common_protobuf_Msg_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.hibob.anylink.common.protobuf.Msg.class, com.hibob.anylink.common.protobuf.Msg.Builder.class);
  }

  private int bitField0_;
  public static final int HEADER_FIELD_NUMBER = 1;
  private com.hibob.anylink.common.protobuf.Header header_;
  /**
   * <code>.com.hibob.anylink.common.protobuf.Header header = 1;</code>
   * @return Whether the header field is set.
   */
  @java.lang.Override
  public boolean hasHeader() {
    return ((bitField0_ & 0x00000001) != 0);
  }
  /**
   * <code>.com.hibob.anylink.common.protobuf.Header header = 1;</code>
   * @return The header.
   */
  @java.lang.Override
  public com.hibob.anylink.common.protobuf.Header getHeader() {
    return header_ == null ? com.hibob.anylink.common.protobuf.Header.getDefaultInstance() : header_;
  }
  /**
   * <code>.com.hibob.anylink.common.protobuf.Header header = 1;</code>
   */
  @java.lang.Override
  public com.hibob.anylink.common.protobuf.HeaderOrBuilder getHeaderOrBuilder() {
    return header_ == null ? com.hibob.anylink.common.protobuf.Header.getDefaultInstance() : header_;
  }

  public static final int BODY_FIELD_NUMBER = 2;
  private com.hibob.anylink.common.protobuf.Body body_;
  /**
   * <code>.com.hibob.anylink.common.protobuf.Body body = 2;</code>
   * @return Whether the body field is set.
   */
  @java.lang.Override
  public boolean hasBody() {
    return ((bitField0_ & 0x00000002) != 0);
  }
  /**
   * <code>.com.hibob.anylink.common.protobuf.Body body = 2;</code>
   * @return The body.
   */
  @java.lang.Override
  public com.hibob.anylink.common.protobuf.Body getBody() {
    return body_ == null ? com.hibob.anylink.common.protobuf.Body.getDefaultInstance() : body_;
  }
  /**
   * <code>.com.hibob.anylink.common.protobuf.Body body = 2;</code>
   */
  @java.lang.Override
  public com.hibob.anylink.common.protobuf.BodyOrBuilder getBodyOrBuilder() {
    return body_ == null ? com.hibob.anylink.common.protobuf.Body.getDefaultInstance() : body_;
  }

  public static final int EXTENSION_FIELD_NUMBER = 99;
  private com.hibob.anylink.common.protobuf.Extension extension_;
  /**
   * <code>optional .com.hibob.anylink.common.protobuf.Extension extension = 99;</code>
   * @return Whether the extension field is set.
   */
  @java.lang.Override
  public boolean hasExtension() {
    return ((bitField0_ & 0x00000004) != 0);
  }
  /**
   * <code>optional .com.hibob.anylink.common.protobuf.Extension extension = 99;</code>
   * @return The extension.
   */
  @java.lang.Override
  public com.hibob.anylink.common.protobuf.Extension getExtension() {
    return extension_ == null ? com.hibob.anylink.common.protobuf.Extension.getDefaultInstance() : extension_;
  }
  /**
   * <code>optional .com.hibob.anylink.common.protobuf.Extension extension = 99;</code>
   */
  @java.lang.Override
  public com.hibob.anylink.common.protobuf.ExtensionOrBuilder getExtensionOrBuilder() {
    return extension_ == null ? com.hibob.anylink.common.protobuf.Extension.getDefaultInstance() : extension_;
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
    if (((bitField0_ & 0x00000001) != 0)) {
      output.writeMessage(1, getHeader());
    }
    if (((bitField0_ & 0x00000002) != 0)) {
      output.writeMessage(2, getBody());
    }
    if (((bitField0_ & 0x00000004) != 0)) {
      output.writeMessage(99, getExtension());
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (((bitField0_ & 0x00000001) != 0)) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(1, getHeader());
    }
    if (((bitField0_ & 0x00000002) != 0)) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(2, getBody());
    }
    if (((bitField0_ & 0x00000004) != 0)) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(99, getExtension());
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
    if (!(obj instanceof com.hibob.anylink.common.protobuf.Msg)) {
      return super.equals(obj);
    }
    com.hibob.anylink.common.protobuf.Msg other = (com.hibob.anylink.common.protobuf.Msg) obj;

    if (hasHeader() != other.hasHeader()) return false;
    if (hasHeader()) {
      if (!getHeader()
          .equals(other.getHeader())) return false;
    }
    if (hasBody() != other.hasBody()) return false;
    if (hasBody()) {
      if (!getBody()
          .equals(other.getBody())) return false;
    }
    if (hasExtension() != other.hasExtension()) return false;
    if (hasExtension()) {
      if (!getExtension()
          .equals(other.getExtension())) return false;
    }
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
    if (hasHeader()) {
      hash = (37 * hash) + HEADER_FIELD_NUMBER;
      hash = (53 * hash) + getHeader().hashCode();
    }
    if (hasBody()) {
      hash = (37 * hash) + BODY_FIELD_NUMBER;
      hash = (53 * hash) + getBody().hashCode();
    }
    if (hasExtension()) {
      hash = (37 * hash) + EXTENSION_FIELD_NUMBER;
      hash = (53 * hash) + getExtension().hashCode();
    }
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.hibob.anylink.common.protobuf.Msg parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.hibob.anylink.common.protobuf.Msg parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.hibob.anylink.common.protobuf.Msg parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.hibob.anylink.common.protobuf.Msg parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.hibob.anylink.common.protobuf.Msg parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.hibob.anylink.common.protobuf.Msg parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.hibob.anylink.common.protobuf.Msg parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage
        .parseWithIOException(PARSER, input);
  }
  public static com.hibob.anylink.common.protobuf.Msg parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  public static com.hibob.anylink.common.protobuf.Msg parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage
        .parseDelimitedWithIOException(PARSER, input);
  }

  public static com.hibob.anylink.common.protobuf.Msg parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.hibob.anylink.common.protobuf.Msg parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage
        .parseWithIOException(PARSER, input);
  }
  public static com.hibob.anylink.common.protobuf.Msg parseFrom(
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
  public static Builder newBuilder(com.hibob.anylink.common.protobuf.Msg prototype) {
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
   * Protobuf type {@code com.hibob.anylink.common.protobuf.Msg}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessage.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:com.hibob.anylink.common.protobuf.Msg)
      com.hibob.anylink.common.protobuf.MsgOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.hibob.anylink.common.protobuf.MsgOuterClass.internal_static_com_hibob_anylink_common_protobuf_Msg_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.hibob.anylink.common.protobuf.MsgOuterClass.internal_static_com_hibob_anylink_common_protobuf_Msg_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.hibob.anylink.common.protobuf.Msg.class, com.hibob.anylink.common.protobuf.Msg.Builder.class);
    }

    // Construct using com.hibob.anylink.common.protobuf.Msg.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessage
              .alwaysUseFieldBuilders) {
        getHeaderFieldBuilder();
        getBodyFieldBuilder();
        getExtensionFieldBuilder();
      }
    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      bitField0_ = 0;
      header_ = null;
      if (headerBuilder_ != null) {
        headerBuilder_.dispose();
        headerBuilder_ = null;
      }
      body_ = null;
      if (bodyBuilder_ != null) {
        bodyBuilder_.dispose();
        bodyBuilder_ = null;
      }
      extension_ = null;
      if (extensionBuilder_ != null) {
        extensionBuilder_.dispose();
        extensionBuilder_ = null;
      }
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.hibob.anylink.common.protobuf.MsgOuterClass.internal_static_com_hibob_anylink_common_protobuf_Msg_descriptor;
    }

    @java.lang.Override
    public com.hibob.anylink.common.protobuf.Msg getDefaultInstanceForType() {
      return com.hibob.anylink.common.protobuf.Msg.getDefaultInstance();
    }

    @java.lang.Override
    public com.hibob.anylink.common.protobuf.Msg build() {
      com.hibob.anylink.common.protobuf.Msg result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.hibob.anylink.common.protobuf.Msg buildPartial() {
      com.hibob.anylink.common.protobuf.Msg result = new com.hibob.anylink.common.protobuf.Msg(this);
      if (bitField0_ != 0) { buildPartial0(result); }
      onBuilt();
      return result;
    }

    private void buildPartial0(com.hibob.anylink.common.protobuf.Msg result) {
      int from_bitField0_ = bitField0_;
      int to_bitField0_ = 0;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        result.header_ = headerBuilder_ == null
            ? header_
            : headerBuilder_.build();
        to_bitField0_ |= 0x00000001;
      }
      if (((from_bitField0_ & 0x00000002) != 0)) {
        result.body_ = bodyBuilder_ == null
            ? body_
            : bodyBuilder_.build();
        to_bitField0_ |= 0x00000002;
      }
      if (((from_bitField0_ & 0x00000004) != 0)) {
        result.extension_ = extensionBuilder_ == null
            ? extension_
            : extensionBuilder_.build();
        to_bitField0_ |= 0x00000004;
      }
      result.bitField0_ |= to_bitField0_;
    }

    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof com.hibob.anylink.common.protobuf.Msg) {
        return mergeFrom((com.hibob.anylink.common.protobuf.Msg)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.hibob.anylink.common.protobuf.Msg other) {
      if (other == com.hibob.anylink.common.protobuf.Msg.getDefaultInstance()) return this;
      if (other.hasHeader()) {
        mergeHeader(other.getHeader());
      }
      if (other.hasBody()) {
        mergeBody(other.getBody());
      }
      if (other.hasExtension()) {
        mergeExtension(other.getExtension());
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
              input.readMessage(
                  getHeaderFieldBuilder().getBuilder(),
                  extensionRegistry);
              bitField0_ |= 0x00000001;
              break;
            } // case 10
            case 18: {
              input.readMessage(
                  getBodyFieldBuilder().getBuilder(),
                  extensionRegistry);
              bitField0_ |= 0x00000002;
              break;
            } // case 18
            case 794: {
              input.readMessage(
                  getExtensionFieldBuilder().getBuilder(),
                  extensionRegistry);
              bitField0_ |= 0x00000004;
              break;
            } // case 794
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

    private com.hibob.anylink.common.protobuf.Header header_;
    private com.google.protobuf.SingleFieldBuilder<
        com.hibob.anylink.common.protobuf.Header, com.hibob.anylink.common.protobuf.Header.Builder, com.hibob.anylink.common.protobuf.HeaderOrBuilder> headerBuilder_;
    /**
     * <code>.com.hibob.anylink.common.protobuf.Header header = 1;</code>
     * @return Whether the header field is set.
     */
    public boolean hasHeader() {
      return ((bitField0_ & 0x00000001) != 0);
    }
    /**
     * <code>.com.hibob.anylink.common.protobuf.Header header = 1;</code>
     * @return The header.
     */
    public com.hibob.anylink.common.protobuf.Header getHeader() {
      if (headerBuilder_ == null) {
        return header_ == null ? com.hibob.anylink.common.protobuf.Header.getDefaultInstance() : header_;
      } else {
        return headerBuilder_.getMessage();
      }
    }
    /**
     * <code>.com.hibob.anylink.common.protobuf.Header header = 1;</code>
     */
    public Builder setHeader(com.hibob.anylink.common.protobuf.Header value) {
      if (headerBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        header_ = value;
      } else {
        headerBuilder_.setMessage(value);
      }
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     * <code>.com.hibob.anylink.common.protobuf.Header header = 1;</code>
     */
    public Builder setHeader(
        com.hibob.anylink.common.protobuf.Header.Builder builderForValue) {
      if (headerBuilder_ == null) {
        header_ = builderForValue.build();
      } else {
        headerBuilder_.setMessage(builderForValue.build());
      }
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     * <code>.com.hibob.anylink.common.protobuf.Header header = 1;</code>
     */
    public Builder mergeHeader(com.hibob.anylink.common.protobuf.Header value) {
      if (headerBuilder_ == null) {
        if (((bitField0_ & 0x00000001) != 0) &&
          header_ != null &&
          header_ != com.hibob.anylink.common.protobuf.Header.getDefaultInstance()) {
          getHeaderBuilder().mergeFrom(value);
        } else {
          header_ = value;
        }
      } else {
        headerBuilder_.mergeFrom(value);
      }
      if (header_ != null) {
        bitField0_ |= 0x00000001;
        onChanged();
      }
      return this;
    }
    /**
     * <code>.com.hibob.anylink.common.protobuf.Header header = 1;</code>
     */
    public Builder clearHeader() {
      bitField0_ = (bitField0_ & ~0x00000001);
      header_ = null;
      if (headerBuilder_ != null) {
        headerBuilder_.dispose();
        headerBuilder_ = null;
      }
      onChanged();
      return this;
    }
    /**
     * <code>.com.hibob.anylink.common.protobuf.Header header = 1;</code>
     */
    public com.hibob.anylink.common.protobuf.Header.Builder getHeaderBuilder() {
      bitField0_ |= 0x00000001;
      onChanged();
      return getHeaderFieldBuilder().getBuilder();
    }
    /**
     * <code>.com.hibob.anylink.common.protobuf.Header header = 1;</code>
     */
    public com.hibob.anylink.common.protobuf.HeaderOrBuilder getHeaderOrBuilder() {
      if (headerBuilder_ != null) {
        return headerBuilder_.getMessageOrBuilder();
      } else {
        return header_ == null ?
            com.hibob.anylink.common.protobuf.Header.getDefaultInstance() : header_;
      }
    }
    /**
     * <code>.com.hibob.anylink.common.protobuf.Header header = 1;</code>
     */
    private com.google.protobuf.SingleFieldBuilder<
        com.hibob.anylink.common.protobuf.Header, com.hibob.anylink.common.protobuf.Header.Builder, com.hibob.anylink.common.protobuf.HeaderOrBuilder> 
        getHeaderFieldBuilder() {
      if (headerBuilder_ == null) {
        headerBuilder_ = new com.google.protobuf.SingleFieldBuilder<
            com.hibob.anylink.common.protobuf.Header, com.hibob.anylink.common.protobuf.Header.Builder, com.hibob.anylink.common.protobuf.HeaderOrBuilder>(
                getHeader(),
                getParentForChildren(),
                isClean());
        header_ = null;
      }
      return headerBuilder_;
    }

    private com.hibob.anylink.common.protobuf.Body body_;
    private com.google.protobuf.SingleFieldBuilder<
        com.hibob.anylink.common.protobuf.Body, com.hibob.anylink.common.protobuf.Body.Builder, com.hibob.anylink.common.protobuf.BodyOrBuilder> bodyBuilder_;
    /**
     * <code>.com.hibob.anylink.common.protobuf.Body body = 2;</code>
     * @return Whether the body field is set.
     */
    public boolean hasBody() {
      return ((bitField0_ & 0x00000002) != 0);
    }
    /**
     * <code>.com.hibob.anylink.common.protobuf.Body body = 2;</code>
     * @return The body.
     */
    public com.hibob.anylink.common.protobuf.Body getBody() {
      if (bodyBuilder_ == null) {
        return body_ == null ? com.hibob.anylink.common.protobuf.Body.getDefaultInstance() : body_;
      } else {
        return bodyBuilder_.getMessage();
      }
    }
    /**
     * <code>.com.hibob.anylink.common.protobuf.Body body = 2;</code>
     */
    public Builder setBody(com.hibob.anylink.common.protobuf.Body value) {
      if (bodyBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        body_ = value;
      } else {
        bodyBuilder_.setMessage(value);
      }
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     * <code>.com.hibob.anylink.common.protobuf.Body body = 2;</code>
     */
    public Builder setBody(
        com.hibob.anylink.common.protobuf.Body.Builder builderForValue) {
      if (bodyBuilder_ == null) {
        body_ = builderForValue.build();
      } else {
        bodyBuilder_.setMessage(builderForValue.build());
      }
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     * <code>.com.hibob.anylink.common.protobuf.Body body = 2;</code>
     */
    public Builder mergeBody(com.hibob.anylink.common.protobuf.Body value) {
      if (bodyBuilder_ == null) {
        if (((bitField0_ & 0x00000002) != 0) &&
          body_ != null &&
          body_ != com.hibob.anylink.common.protobuf.Body.getDefaultInstance()) {
          getBodyBuilder().mergeFrom(value);
        } else {
          body_ = value;
        }
      } else {
        bodyBuilder_.mergeFrom(value);
      }
      if (body_ != null) {
        bitField0_ |= 0x00000002;
        onChanged();
      }
      return this;
    }
    /**
     * <code>.com.hibob.anylink.common.protobuf.Body body = 2;</code>
     */
    public Builder clearBody() {
      bitField0_ = (bitField0_ & ~0x00000002);
      body_ = null;
      if (bodyBuilder_ != null) {
        bodyBuilder_.dispose();
        bodyBuilder_ = null;
      }
      onChanged();
      return this;
    }
    /**
     * <code>.com.hibob.anylink.common.protobuf.Body body = 2;</code>
     */
    public com.hibob.anylink.common.protobuf.Body.Builder getBodyBuilder() {
      bitField0_ |= 0x00000002;
      onChanged();
      return getBodyFieldBuilder().getBuilder();
    }
    /**
     * <code>.com.hibob.anylink.common.protobuf.Body body = 2;</code>
     */
    public com.hibob.anylink.common.protobuf.BodyOrBuilder getBodyOrBuilder() {
      if (bodyBuilder_ != null) {
        return bodyBuilder_.getMessageOrBuilder();
      } else {
        return body_ == null ?
            com.hibob.anylink.common.protobuf.Body.getDefaultInstance() : body_;
      }
    }
    /**
     * <code>.com.hibob.anylink.common.protobuf.Body body = 2;</code>
     */
    private com.google.protobuf.SingleFieldBuilder<
        com.hibob.anylink.common.protobuf.Body, com.hibob.anylink.common.protobuf.Body.Builder, com.hibob.anylink.common.protobuf.BodyOrBuilder> 
        getBodyFieldBuilder() {
      if (bodyBuilder_ == null) {
        bodyBuilder_ = new com.google.protobuf.SingleFieldBuilder<
            com.hibob.anylink.common.protobuf.Body, com.hibob.anylink.common.protobuf.Body.Builder, com.hibob.anylink.common.protobuf.BodyOrBuilder>(
                getBody(),
                getParentForChildren(),
                isClean());
        body_ = null;
      }
      return bodyBuilder_;
    }

    private com.hibob.anylink.common.protobuf.Extension extension_;
    private com.google.protobuf.SingleFieldBuilder<
        com.hibob.anylink.common.protobuf.Extension, com.hibob.anylink.common.protobuf.Extension.Builder, com.hibob.anylink.common.protobuf.ExtensionOrBuilder> extensionBuilder_;
    /**
     * <code>optional .com.hibob.anylink.common.protobuf.Extension extension = 99;</code>
     * @return Whether the extension field is set.
     */
    public boolean hasExtension() {
      return ((bitField0_ & 0x00000004) != 0);
    }
    /**
     * <code>optional .com.hibob.anylink.common.protobuf.Extension extension = 99;</code>
     * @return The extension.
     */
    public com.hibob.anylink.common.protobuf.Extension getExtension() {
      if (extensionBuilder_ == null) {
        return extension_ == null ? com.hibob.anylink.common.protobuf.Extension.getDefaultInstance() : extension_;
      } else {
        return extensionBuilder_.getMessage();
      }
    }
    /**
     * <code>optional .com.hibob.anylink.common.protobuf.Extension extension = 99;</code>
     */
    public Builder setExtension(com.hibob.anylink.common.protobuf.Extension value) {
      if (extensionBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        extension_ = value;
      } else {
        extensionBuilder_.setMessage(value);
      }
      bitField0_ |= 0x00000004;
      onChanged();
      return this;
    }
    /**
     * <code>optional .com.hibob.anylink.common.protobuf.Extension extension = 99;</code>
     */
    public Builder setExtension(
        com.hibob.anylink.common.protobuf.Extension.Builder builderForValue) {
      if (extensionBuilder_ == null) {
        extension_ = builderForValue.build();
      } else {
        extensionBuilder_.setMessage(builderForValue.build());
      }
      bitField0_ |= 0x00000004;
      onChanged();
      return this;
    }
    /**
     * <code>optional .com.hibob.anylink.common.protobuf.Extension extension = 99;</code>
     */
    public Builder mergeExtension(com.hibob.anylink.common.protobuf.Extension value) {
      if (extensionBuilder_ == null) {
        if (((bitField0_ & 0x00000004) != 0) &&
          extension_ != null &&
          extension_ != com.hibob.anylink.common.protobuf.Extension.getDefaultInstance()) {
          getExtensionBuilder().mergeFrom(value);
        } else {
          extension_ = value;
        }
      } else {
        extensionBuilder_.mergeFrom(value);
      }
      if (extension_ != null) {
        bitField0_ |= 0x00000004;
        onChanged();
      }
      return this;
    }
    /**
     * <code>optional .com.hibob.anylink.common.protobuf.Extension extension = 99;</code>
     */
    public Builder clearExtension() {
      bitField0_ = (bitField0_ & ~0x00000004);
      extension_ = null;
      if (extensionBuilder_ != null) {
        extensionBuilder_.dispose();
        extensionBuilder_ = null;
      }
      onChanged();
      return this;
    }
    /**
     * <code>optional .com.hibob.anylink.common.protobuf.Extension extension = 99;</code>
     */
    public com.hibob.anylink.common.protobuf.Extension.Builder getExtensionBuilder() {
      bitField0_ |= 0x00000004;
      onChanged();
      return getExtensionFieldBuilder().getBuilder();
    }
    /**
     * <code>optional .com.hibob.anylink.common.protobuf.Extension extension = 99;</code>
     */
    public com.hibob.anylink.common.protobuf.ExtensionOrBuilder getExtensionOrBuilder() {
      if (extensionBuilder_ != null) {
        return extensionBuilder_.getMessageOrBuilder();
      } else {
        return extension_ == null ?
            com.hibob.anylink.common.protobuf.Extension.getDefaultInstance() : extension_;
      }
    }
    /**
     * <code>optional .com.hibob.anylink.common.protobuf.Extension extension = 99;</code>
     */
    private com.google.protobuf.SingleFieldBuilder<
        com.hibob.anylink.common.protobuf.Extension, com.hibob.anylink.common.protobuf.Extension.Builder, com.hibob.anylink.common.protobuf.ExtensionOrBuilder> 
        getExtensionFieldBuilder() {
      if (extensionBuilder_ == null) {
        extensionBuilder_ = new com.google.protobuf.SingleFieldBuilder<
            com.hibob.anylink.common.protobuf.Extension, com.hibob.anylink.common.protobuf.Extension.Builder, com.hibob.anylink.common.protobuf.ExtensionOrBuilder>(
                getExtension(),
                getParentForChildren(),
                isClean());
        extension_ = null;
      }
      return extensionBuilder_;
    }

    // @@protoc_insertion_point(builder_scope:com.hibob.anylink.common.protobuf.Msg)
  }

  // @@protoc_insertion_point(class_scope:com.hibob.anylink.common.protobuf.Msg)
  private static final com.hibob.anylink.common.protobuf.Msg DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.hibob.anylink.common.protobuf.Msg();
  }

  public static com.hibob.anylink.common.protobuf.Msg getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<Msg>
      PARSER = new com.google.protobuf.AbstractParser<Msg>() {
    @java.lang.Override
    public Msg parsePartialFrom(
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

  public static com.google.protobuf.Parser<Msg> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<Msg> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.hibob.anylink.common.protobuf.Msg getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

