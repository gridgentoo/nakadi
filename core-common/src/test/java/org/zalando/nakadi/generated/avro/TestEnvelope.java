/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package org.zalando.nakadi.generated.avro;

import org.apache.avro.generic.GenericArray;
import org.apache.avro.specific.SpecificData;
import org.apache.avro.util.Utf8;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.SchemaStore;

@org.apache.avro.specific.AvroGenerated
public class TestEnvelope extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = 4371697005384448255L;


  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"TestEnvelope\",\"namespace\":\"org.zalando.nakadi.generated.avro\",\"fields\":[{\"name\":\"metadata\",\"type\":{\"type\":\"record\",\"name\":\"TestMetadata\",\"doc\":\"Event metadata defines data about the payload and additional information for Nakadi operations\",\"fields\":[{\"name\":\"occurred_at\",\"type\":{\"type\":\"long\",\"logicalType\":\"timestamp-millis\"}},{\"name\":\"eid\",\"type\":{\"type\":\"string\",\"logicalType\":\"uuid\"}},{\"name\":\"flow_id\",\"type\":[\"null\",{\"type\":\"string\",\"avro.java.string\":\"String\"}],\"default\":null},{\"name\":\"received_at\",\"type\":[\"null\",{\"type\":\"long\",\"logicalType\":\"timestamp-millis\"}],\"default\":null},{\"name\":\"version\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"published_by\",\"type\":[\"null\",{\"type\":\"string\",\"avro.java.string\":\"String\"}],\"default\":null},{\"name\":\"event_type\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"partition\",\"type\":[\"null\",{\"type\":\"string\",\"avro.java.string\":\"String\"}],\"default\":null},{\"name\":\"partition_keys\",\"type\":[\"null\",{\"type\":\"array\",\"items\":{\"type\":\"string\",\"avro.java.string\":\"String\"}}],\"default\":null},{\"name\":\"partition_compaction_key\",\"type\":[\"null\",{\"type\":\"string\",\"avro.java.string\":\"String\"}],\"default\":null},{\"name\":\"parent_eids\",\"type\":[\"null\",{\"type\":\"array\",\"items\":{\"type\":\"string\",\"logicalType\":\"uuid\"}}],\"default\":null},{\"name\":\"span_ctx\",\"type\":[\"null\",{\"type\":\"string\",\"avro.java.string\":\"String\"}],\"default\":null}]}},{\"name\":\"payload\",\"type\":\"bytes\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static final SpecificData MODEL$ = new SpecificData();
  static {
    MODEL$.addLogicalTypeConversion(new org.apache.avro.data.TimeConversions.TimestampMillisConversion());
  }

  private static final BinaryMessageEncoder<TestEnvelope> ENCODER =
      new BinaryMessageEncoder<TestEnvelope>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<TestEnvelope> DECODER =
      new BinaryMessageDecoder<TestEnvelope>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageEncoder instance used by this class.
   * @return the message encoder used by this class
   */
  public static BinaryMessageEncoder<TestEnvelope> getEncoder() {
    return ENCODER;
  }

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   * @return the message decoder used by this class
   */
  public static BinaryMessageDecoder<TestEnvelope> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   * @return a BinaryMessageDecoder instance for this class backed by the given SchemaStore
   */
  public static BinaryMessageDecoder<TestEnvelope> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<TestEnvelope>(MODEL$, SCHEMA$, resolver);
  }

  /**
   * Serializes this TestEnvelope to a ByteBuffer.
   * @return a buffer holding the serialized data for this instance
   * @throws java.io.IOException if this instance could not be serialized
   */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /**
   * Deserializes a TestEnvelope from a ByteBuffer.
   * @param b a byte buffer holding serialized data for an instance of this class
   * @return a TestEnvelope instance decoded from the given buffer
   * @throws java.io.IOException if the given bytes could not be deserialized into an instance of this class
   */
  public static TestEnvelope fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  private org.zalando.nakadi.generated.avro.TestMetadata metadata;
  private java.nio.ByteBuffer payload;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public TestEnvelope() {}

  /**
   * All-args constructor.
   * @param metadata The new value for metadata
   * @param payload The new value for payload
   */
  public TestEnvelope(org.zalando.nakadi.generated.avro.TestMetadata metadata, java.nio.ByteBuffer payload) {
    this.metadata = metadata;
    this.payload = payload;
  }

  public org.apache.avro.specific.SpecificData getSpecificData() { return MODEL$; }
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return metadata;
    case 1: return payload;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: metadata = (org.zalando.nakadi.generated.avro.TestMetadata)value$; break;
    case 1: payload = (java.nio.ByteBuffer)value$; break;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  /**
   * Gets the value of the 'metadata' field.
   * @return The value of the 'metadata' field.
   */
  public org.zalando.nakadi.generated.avro.TestMetadata getMetadata() {
    return metadata;
  }


  /**
   * Sets the value of the 'metadata' field.
   * @param value the value to set.
   */
  public void setMetadata(org.zalando.nakadi.generated.avro.TestMetadata value) {
    this.metadata = value;
  }

  /**
   * Gets the value of the 'payload' field.
   * @return The value of the 'payload' field.
   */
  public java.nio.ByteBuffer getPayload() {
    return payload;
  }


  /**
   * Sets the value of the 'payload' field.
   * @param value the value to set.
   */
  public void setPayload(java.nio.ByteBuffer value) {
    this.payload = value;
  }

  /**
   * Creates a new TestEnvelope RecordBuilder.
   * @return A new TestEnvelope RecordBuilder
   */
  public static org.zalando.nakadi.generated.avro.TestEnvelope.Builder newBuilder() {
    return new org.zalando.nakadi.generated.avro.TestEnvelope.Builder();
  }

  /**
   * Creates a new TestEnvelope RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new TestEnvelope RecordBuilder
   */
  public static org.zalando.nakadi.generated.avro.TestEnvelope.Builder newBuilder(org.zalando.nakadi.generated.avro.TestEnvelope.Builder other) {
    if (other == null) {
      return new org.zalando.nakadi.generated.avro.TestEnvelope.Builder();
    } else {
      return new org.zalando.nakadi.generated.avro.TestEnvelope.Builder(other);
    }
  }

  /**
   * Creates a new TestEnvelope RecordBuilder by copying an existing TestEnvelope instance.
   * @param other The existing instance to copy.
   * @return A new TestEnvelope RecordBuilder
   */
  public static org.zalando.nakadi.generated.avro.TestEnvelope.Builder newBuilder(org.zalando.nakadi.generated.avro.TestEnvelope other) {
    if (other == null) {
      return new org.zalando.nakadi.generated.avro.TestEnvelope.Builder();
    } else {
      return new org.zalando.nakadi.generated.avro.TestEnvelope.Builder(other);
    }
  }

  /**
   * RecordBuilder for TestEnvelope instances.
   */
  @org.apache.avro.specific.AvroGenerated
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<TestEnvelope>
    implements org.apache.avro.data.RecordBuilder<TestEnvelope> {

    private org.zalando.nakadi.generated.avro.TestMetadata metadata;
    private org.zalando.nakadi.generated.avro.TestMetadata.Builder metadataBuilder;
    private java.nio.ByteBuffer payload;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$, MODEL$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(org.zalando.nakadi.generated.avro.TestEnvelope.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.metadata)) {
        this.metadata = data().deepCopy(fields()[0].schema(), other.metadata);
        fieldSetFlags()[0] = other.fieldSetFlags()[0];
      }
      if (other.hasMetadataBuilder()) {
        this.metadataBuilder = org.zalando.nakadi.generated.avro.TestMetadata.newBuilder(other.getMetadataBuilder());
      }
      if (isValidValue(fields()[1], other.payload)) {
        this.payload = data().deepCopy(fields()[1].schema(), other.payload);
        fieldSetFlags()[1] = other.fieldSetFlags()[1];
      }
    }

    /**
     * Creates a Builder by copying an existing TestEnvelope instance
     * @param other The existing instance to copy.
     */
    private Builder(org.zalando.nakadi.generated.avro.TestEnvelope other) {
      super(SCHEMA$, MODEL$);
      if (isValidValue(fields()[0], other.metadata)) {
        this.metadata = data().deepCopy(fields()[0].schema(), other.metadata);
        fieldSetFlags()[0] = true;
      }
      this.metadataBuilder = null;
      if (isValidValue(fields()[1], other.payload)) {
        this.payload = data().deepCopy(fields()[1].schema(), other.payload);
        fieldSetFlags()[1] = true;
      }
    }

    /**
      * Gets the value of the 'metadata' field.
      * @return The value.
      */
    public org.zalando.nakadi.generated.avro.TestMetadata getMetadata() {
      return metadata;
    }


    /**
      * Sets the value of the 'metadata' field.
      * @param value The value of 'metadata'.
      * @return This builder.
      */
    public org.zalando.nakadi.generated.avro.TestEnvelope.Builder setMetadata(org.zalando.nakadi.generated.avro.TestMetadata value) {
      validate(fields()[0], value);
      this.metadataBuilder = null;
      this.metadata = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'metadata' field has been set.
      * @return True if the 'metadata' field has been set, false otherwise.
      */
    public boolean hasMetadata() {
      return fieldSetFlags()[0];
    }

    /**
     * Gets the Builder instance for the 'metadata' field and creates one if it doesn't exist yet.
     * @return This builder.
     */
    public org.zalando.nakadi.generated.avro.TestMetadata.Builder getMetadataBuilder() {
      if (metadataBuilder == null) {
        if (hasMetadata()) {
          setMetadataBuilder(org.zalando.nakadi.generated.avro.TestMetadata.newBuilder(metadata));
        } else {
          setMetadataBuilder(org.zalando.nakadi.generated.avro.TestMetadata.newBuilder());
        }
      }
      return metadataBuilder;
    }

    /**
     * Sets the Builder instance for the 'metadata' field
     * @param value The builder instance that must be set.
     * @return This builder.
     */

    public org.zalando.nakadi.generated.avro.TestEnvelope.Builder setMetadataBuilder(org.zalando.nakadi.generated.avro.TestMetadata.Builder value) {
      clearMetadata();
      metadataBuilder = value;
      return this;
    }

    /**
     * Checks whether the 'metadata' field has an active Builder instance
     * @return True if the 'metadata' field has an active Builder instance
     */
    public boolean hasMetadataBuilder() {
      return metadataBuilder != null;
    }

    /**
      * Clears the value of the 'metadata' field.
      * @return This builder.
      */
    public org.zalando.nakadi.generated.avro.TestEnvelope.Builder clearMetadata() {
      metadata = null;
      metadataBuilder = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'payload' field.
      * @return The value.
      */
    public java.nio.ByteBuffer getPayload() {
      return payload;
    }


    /**
      * Sets the value of the 'payload' field.
      * @param value The value of 'payload'.
      * @return This builder.
      */
    public org.zalando.nakadi.generated.avro.TestEnvelope.Builder setPayload(java.nio.ByteBuffer value) {
      validate(fields()[1], value);
      this.payload = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'payload' field has been set.
      * @return True if the 'payload' field has been set, false otherwise.
      */
    public boolean hasPayload() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'payload' field.
      * @return This builder.
      */
    public org.zalando.nakadi.generated.avro.TestEnvelope.Builder clearPayload() {
      payload = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public TestEnvelope build() {
      try {
        TestEnvelope record = new TestEnvelope();
        if (metadataBuilder != null) {
          try {
            record.metadata = this.metadataBuilder.build();
          } catch (org.apache.avro.AvroMissingFieldException e) {
            e.addParentField(record.getSchema().getField("metadata"));
            throw e;
          }
        } else {
          record.metadata = fieldSetFlags()[0] ? this.metadata : (org.zalando.nakadi.generated.avro.TestMetadata) defaultValue(fields()[0]);
        }
        record.payload = fieldSetFlags()[1] ? this.payload : (java.nio.ByteBuffer) defaultValue(fields()[1]);
        return record;
      } catch (org.apache.avro.AvroMissingFieldException e) {
        throw e;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<TestEnvelope>
    WRITER$ = (org.apache.avro.io.DatumWriter<TestEnvelope>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<TestEnvelope>
    READER$ = (org.apache.avro.io.DatumReader<TestEnvelope>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

}










