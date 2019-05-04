// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: currenncy_exchange.proto

package currency.exchange.gen;

/**
 * Protobuf type {@code CurrencyUpdatesSubscriptionRequest}
 */
public  final class CurrencyUpdatesSubscriptionRequest extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:CurrencyUpdatesSubscriptionRequest)
    CurrencyUpdatesSubscriptionRequestOrBuilder {
private static final long serialVersionUID = 0L;
  // Use CurrencyUpdatesSubscriptionRequest.newBuilder() to construct.
  private CurrencyUpdatesSubscriptionRequest(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private CurrencyUpdatesSubscriptionRequest() {
    crrencyToObserve_ = java.util.Collections.emptyList();
  }

  @Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private CurrencyUpdatesSubscriptionRequest(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    if (extensionRegistry == null) {
      throw new NullPointerException();
    }
    int mutable_bitField0_ = 0;
    com.google.protobuf.UnknownFieldSet.Builder unknownFields =
        com.google.protobuf.UnknownFieldSet.newBuilder();
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          case 8: {
            int rawValue = input.readEnum();
            if (!((mutable_bitField0_ & 0x00000001) != 0)) {
              crrencyToObserve_ = new java.util.ArrayList<Integer>();
              mutable_bitField0_ |= 0x00000001;
            }
            crrencyToObserve_.add(rawValue);
            break;
          }
          case 10: {
            int length = input.readRawVarint32();
            int oldLimit = input.pushLimit(length);
            while(input.getBytesUntilLimit() > 0) {
              int rawValue = input.readEnum();
              if (!((mutable_bitField0_ & 0x00000001) != 0)) {
                crrencyToObserve_ = new java.util.ArrayList<Integer>();
                mutable_bitField0_ |= 0x00000001;
              }
              crrencyToObserve_.add(rawValue);
            }
            input.popLimit(oldLimit);
            break;
          }
          default: {
            if (!parseUnknownField(
                input, unknownFields, extensionRegistry, tag)) {
              done = true;
            }
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e).setUnfinishedMessage(this);
    } finally {
      if (((mutable_bitField0_ & 0x00000001) != 0)) {
        crrencyToObserve_ = java.util.Collections.unmodifiableList(crrencyToObserve_);
      }
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return currency.exchange.gen.CurrencyExchangeProto.internal_static_CurrencyUpdatesSubscriptionRequest_descriptor;
  }

  @Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return currency.exchange.gen.CurrencyExchangeProto.internal_static_CurrencyUpdatesSubscriptionRequest_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            currency.exchange.gen.CurrencyUpdatesSubscriptionRequest.class, currency.exchange.gen.CurrencyUpdatesSubscriptionRequest.Builder.class);
  }

  public static final int CRRENCYTOOBSERVE_FIELD_NUMBER = 1;
  private java.util.List<Integer> crrencyToObserve_;
  private static final com.google.protobuf.Internal.ListAdapter.Converter<
      Integer, currency.exchange.gen.Currency> crrencyToObserve_converter_ =
          new com.google.protobuf.Internal.ListAdapter.Converter<
              Integer, currency.exchange.gen.Currency>() {
            public currency.exchange.gen.Currency convert(Integer from) {
              @SuppressWarnings("deprecation")
              currency.exchange.gen.Currency result = currency.exchange.gen.Currency.valueOf(from);
              return result == null ? currency.exchange.gen.Currency.UNRECOGNIZED : result;
            }
          };
  /**
   * <code>repeated .Currency crrencyToObserve = 1;</code>
   */
  public java.util.List<currency.exchange.gen.Currency> getCrrencyToObserveList() {
    return new com.google.protobuf.Internal.ListAdapter<
        Integer, currency.exchange.gen.Currency>(crrencyToObserve_, crrencyToObserve_converter_);
  }
  /**
   * <code>repeated .Currency crrencyToObserve = 1;</code>
   */
  public int getCrrencyToObserveCount() {
    return crrencyToObserve_.size();
  }
  /**
   * <code>repeated .Currency crrencyToObserve = 1;</code>
   */
  public currency.exchange.gen.Currency getCrrencyToObserve(int index) {
    return crrencyToObserve_converter_.convert(crrencyToObserve_.get(index));
  }
  /**
   * <code>repeated .Currency crrencyToObserve = 1;</code>
   */
  public java.util.List<Integer>
  getCrrencyToObserveValueList() {
    return crrencyToObserve_;
  }
  /**
   * <code>repeated .Currency crrencyToObserve = 1;</code>
   */
  public int getCrrencyToObserveValue(int index) {
    return crrencyToObserve_.get(index);
  }
  private int crrencyToObserveMemoizedSerializedSize;

  private byte memoizedIsInitialized = -1;
  @Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    getSerializedSize();
    if (getCrrencyToObserveList().size() > 0) {
      output.writeUInt32NoTag(10);
      output.writeUInt32NoTag(crrencyToObserveMemoizedSerializedSize);
    }
    for (int i = 0; i < crrencyToObserve_.size(); i++) {
      output.writeEnumNoTag(crrencyToObserve_.get(i));
    }
    unknownFields.writeTo(output);
  }

  @Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    {
      int dataSize = 0;
      for (int i = 0; i < crrencyToObserve_.size(); i++) {
        dataSize += com.google.protobuf.CodedOutputStream
          .computeEnumSizeNoTag(crrencyToObserve_.get(i));
      }
      size += dataSize;
      if (!getCrrencyToObserveList().isEmpty()) {  size += 1;
        size += com.google.protobuf.CodedOutputStream
          .computeUInt32SizeNoTag(dataSize);
      }crrencyToObserveMemoizedSerializedSize = dataSize;
    }
    size += unknownFields.getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof currency.exchange.gen.CurrencyUpdatesSubscriptionRequest)) {
      return super.equals(obj);
    }
    currency.exchange.gen.CurrencyUpdatesSubscriptionRequest other = (currency.exchange.gen.CurrencyUpdatesSubscriptionRequest) obj;

    if (!crrencyToObserve_.equals(other.crrencyToObserve_)) return false;
    if (!unknownFields.equals(other.unknownFields)) return false;
    return true;
  }

  @Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    if (getCrrencyToObserveCount() > 0) {
      hash = (37 * hash) + CRRENCYTOOBSERVE_FIELD_NUMBER;
      hash = (53 * hash) + crrencyToObserve_.hashCode();
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static currency.exchange.gen.CurrencyUpdatesSubscriptionRequest parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static currency.exchange.gen.CurrencyUpdatesSubscriptionRequest parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static currency.exchange.gen.CurrencyUpdatesSubscriptionRequest parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static currency.exchange.gen.CurrencyUpdatesSubscriptionRequest parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static currency.exchange.gen.CurrencyUpdatesSubscriptionRequest parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static currency.exchange.gen.CurrencyUpdatesSubscriptionRequest parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static currency.exchange.gen.CurrencyUpdatesSubscriptionRequest parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static currency.exchange.gen.CurrencyUpdatesSubscriptionRequest parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static currency.exchange.gen.CurrencyUpdatesSubscriptionRequest parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static currency.exchange.gen.CurrencyUpdatesSubscriptionRequest parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static currency.exchange.gen.CurrencyUpdatesSubscriptionRequest parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static currency.exchange.gen.CurrencyUpdatesSubscriptionRequest parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(currency.exchange.gen.CurrencyUpdatesSubscriptionRequest prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code CurrencyUpdatesSubscriptionRequest}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:CurrencyUpdatesSubscriptionRequest)
      currency.exchange.gen.CurrencyUpdatesSubscriptionRequestOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return currency.exchange.gen.CurrencyExchangeProto.internal_static_CurrencyUpdatesSubscriptionRequest_descriptor;
    }

    @Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return currency.exchange.gen.CurrencyExchangeProto.internal_static_CurrencyUpdatesSubscriptionRequest_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              currency.exchange.gen.CurrencyUpdatesSubscriptionRequest.class, currency.exchange.gen.CurrencyUpdatesSubscriptionRequest.Builder.class);
    }

    // Construct using currency.exchange.currency.exchange.gen.CurrencyUpdatesSubscriptionRequest.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
      }
    }
    @Override
    public Builder clear() {
      super.clear();
      crrencyToObserve_ = java.util.Collections.emptyList();
      bitField0_ = (bitField0_ & ~0x00000001);
      return this;
    }

    @Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return currency.exchange.gen.CurrencyExchangeProto.internal_static_CurrencyUpdatesSubscriptionRequest_descriptor;
    }

    @Override
    public currency.exchange.gen.CurrencyUpdatesSubscriptionRequest getDefaultInstanceForType() {
      return currency.exchange.gen.CurrencyUpdatesSubscriptionRequest.getDefaultInstance();
    }

    @Override
    public currency.exchange.gen.CurrencyUpdatesSubscriptionRequest build() {
      currency.exchange.gen.CurrencyUpdatesSubscriptionRequest result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @Override
    public currency.exchange.gen.CurrencyUpdatesSubscriptionRequest buildPartial() {
      currency.exchange.gen.CurrencyUpdatesSubscriptionRequest result = new currency.exchange.gen.CurrencyUpdatesSubscriptionRequest(this);
      int from_bitField0_ = bitField0_;
      if (((bitField0_ & 0x00000001) != 0)) {
        crrencyToObserve_ = java.util.Collections.unmodifiableList(crrencyToObserve_);
        bitField0_ = (bitField0_ & ~0x00000001);
      }
      result.crrencyToObserve_ = crrencyToObserve_;
      onBuilt();
      return result;
    }

    @Override
    public Builder clone() {
      return super.clone();
    }
    @Override
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        Object value) {
      return super.setField(field, value);
    }
    @Override
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return super.clearField(field);
    }
    @Override
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return super.clearOneof(oneof);
    }
    @Override
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, Object value) {
      return super.setRepeatedField(field, index, value);
    }
    @Override
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        Object value) {
      return super.addRepeatedField(field, value);
    }
    @Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof currency.exchange.gen.CurrencyUpdatesSubscriptionRequest) {
        return mergeFrom((currency.exchange.gen.CurrencyUpdatesSubscriptionRequest)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(currency.exchange.gen.CurrencyUpdatesSubscriptionRequest other) {
      if (other == currency.exchange.gen.CurrencyUpdatesSubscriptionRequest.getDefaultInstance()) return this;
      if (!other.crrencyToObserve_.isEmpty()) {
        if (crrencyToObserve_.isEmpty()) {
          crrencyToObserve_ = other.crrencyToObserve_;
          bitField0_ = (bitField0_ & ~0x00000001);
        } else {
          ensureCrrencyToObserveIsMutable();
          crrencyToObserve_.addAll(other.crrencyToObserve_);
        }
        onChanged();
      }
      this.mergeUnknownFields(other.unknownFields);
      onChanged();
      return this;
    }

    @Override
    public final boolean isInitialized() {
      return true;
    }

    @Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      currency.exchange.gen.CurrencyUpdatesSubscriptionRequest parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (currency.exchange.gen.CurrencyUpdatesSubscriptionRequest) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }
    private int bitField0_;

    private java.util.List<Integer> crrencyToObserve_ =
      java.util.Collections.emptyList();
    private void ensureCrrencyToObserveIsMutable() {
      if (!((bitField0_ & 0x00000001) != 0)) {
        crrencyToObserve_ = new java.util.ArrayList<Integer>(crrencyToObserve_);
        bitField0_ |= 0x00000001;
      }
    }
    /**
     * <code>repeated .Currency crrencyToObserve = 1;</code>
     */
    public java.util.List<currency.exchange.gen.Currency> getCrrencyToObserveList() {
      return new com.google.protobuf.Internal.ListAdapter<
          Integer, currency.exchange.gen.Currency>(crrencyToObserve_, crrencyToObserve_converter_);
    }
    /**
     * <code>repeated .Currency crrencyToObserve = 1;</code>
     */
    public int getCrrencyToObserveCount() {
      return crrencyToObserve_.size();
    }
    /**
     * <code>repeated .Currency crrencyToObserve = 1;</code>
     */
    public currency.exchange.gen.Currency getCrrencyToObserve(int index) {
      return crrencyToObserve_converter_.convert(crrencyToObserve_.get(index));
    }
    /**
     * <code>repeated .Currency crrencyToObserve = 1;</code>
     */
    public Builder setCrrencyToObserve(
        int index, currency.exchange.gen.Currency value) {
      if (value == null) {
        throw new NullPointerException();
      }
      ensureCrrencyToObserveIsMutable();
      crrencyToObserve_.set(index, value.getNumber());
      onChanged();
      return this;
    }
    /**
     * <code>repeated .Currency crrencyToObserve = 1;</code>
     */
    public Builder addCrrencyToObserve(currency.exchange.gen.Currency value) {
      if (value == null) {
        throw new NullPointerException();
      }
      ensureCrrencyToObserveIsMutable();
      crrencyToObserve_.add(value.getNumber());
      onChanged();
      return this;
    }
    /**
     * <code>repeated .Currency crrencyToObserve = 1;</code>
     */
    public Builder addAllCrrencyToObserve(
        Iterable<? extends currency.exchange.gen.Currency> values) {
      ensureCrrencyToObserveIsMutable();
      for (currency.exchange.gen.Currency value : values) {
        crrencyToObserve_.add(value.getNumber());
      }
      onChanged();
      return this;
    }
    /**
     * <code>repeated .Currency crrencyToObserve = 1;</code>
     */
    public Builder clearCrrencyToObserve() {
      crrencyToObserve_ = java.util.Collections.emptyList();
      bitField0_ = (bitField0_ & ~0x00000001);
      onChanged();
      return this;
    }
    /**
     * <code>repeated .Currency crrencyToObserve = 1;</code>
     */
    public java.util.List<Integer>
    getCrrencyToObserveValueList() {
      return java.util.Collections.unmodifiableList(crrencyToObserve_);
    }
    /**
     * <code>repeated .Currency crrencyToObserve = 1;</code>
     */
    public int getCrrencyToObserveValue(int index) {
      return crrencyToObserve_.get(index);
    }
    /**
     * <code>repeated .Currency crrencyToObserve = 1;</code>
     */
    public Builder setCrrencyToObserveValue(
        int index, int value) {
      ensureCrrencyToObserveIsMutable();
      crrencyToObserve_.set(index, value);
      onChanged();
      return this;
    }
    /**
     * <code>repeated .Currency crrencyToObserve = 1;</code>
     */
    public Builder addCrrencyToObserveValue(int value) {
      ensureCrrencyToObserveIsMutable();
      crrencyToObserve_.add(value);
      onChanged();
      return this;
    }
    /**
     * <code>repeated .Currency crrencyToObserve = 1;</code>
     */
    public Builder addAllCrrencyToObserveValue(
        Iterable<Integer> values) {
      ensureCrrencyToObserveIsMutable();
      for (int value : values) {
        crrencyToObserve_.add(value);
      }
      onChanged();
      return this;
    }
    @Override
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFields(unknownFields);
    }

    @Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:CurrencyUpdatesSubscriptionRequest)
  }

  // @@protoc_insertion_point(class_scope:CurrencyUpdatesSubscriptionRequest)
  private static final currency.exchange.gen.CurrencyUpdatesSubscriptionRequest DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new currency.exchange.gen.CurrencyUpdatesSubscriptionRequest();
  }

  public static currency.exchange.gen.CurrencyUpdatesSubscriptionRequest getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<CurrencyUpdatesSubscriptionRequest>
      PARSER = new com.google.protobuf.AbstractParser<CurrencyUpdatesSubscriptionRequest>() {
    @Override
    public CurrencyUpdatesSubscriptionRequest parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new CurrencyUpdatesSubscriptionRequest(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<CurrencyUpdatesSubscriptionRequest> parser() {
    return PARSER;
  }

  @Override
  public com.google.protobuf.Parser<CurrencyUpdatesSubscriptionRequest> getParserForType() {
    return PARSER;
  }

  @Override
  public currency.exchange.gen.CurrencyUpdatesSubscriptionRequest getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

