package org.zalando.nakadi.repository.kafka;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.generic.GenericRecordBuilder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.message.BinaryMessageEncoder;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.zalando.nakadi.domain.EnvelopeHolder;
import org.zalando.nakadi.domain.VersionedAvroSchema;
import org.zalando.nakadi.generated.avro.Envelope;
import org.zalando.nakadi.generated.avro.Metadata;
import org.zalando.nakadi.mapper.NakadiRecordMapper;
import org.zalando.nakadi.service.LocalSchemaRegistry;
import org.zalando.nakadi.service.SchemaProviderService;
import org.zalando.nakadi.service.TestSchemaProviderService;
import org.zalando.nakadi.util.AvroUtils;
import org.zalando.nakadi.utils.TestUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.Optional;

public class KafkaRecordDeserializerTest {

    private static final long SOME_TIME = 1643290232172l;
    private static final String SOME_TIME_DATE_STRING = "2022-01-27T13:30:32.172Z";
    private final LocalSchemaRegistry localSchemaRegistry;
    private final SchemaProviderService schemaService;
    private final VersionedAvroSchema metadataSchema;
    private final NakadiRecordMapper nakadiRecordMapper;
    private final Schema schema = AvroUtils.getParsedSchema(new DefaultResourceLoader()
            .getResource("test.deserialize.avro.avsc").getInputStream());
    private final KafkaRecordDeserializer deserializer;

    public KafkaRecordDeserializerTest() throws IOException {
        // FIXME: doesn't work without the trailing slash
        final Resource eventTypeRes = new DefaultResourceLoader().getResource("avro-schema/");
        localSchemaRegistry = TestUtils.getLocalSchemaRegistry();
        schemaService = new TestSchemaProviderService(localSchemaRegistry);
        metadataSchema = localSchemaRegistry.getLatestEventTypeSchemaVersion(LocalSchemaRegistry.METADATA_KEY);
        nakadiRecordMapper = new NakadiRecordMapper(localSchemaRegistry);
        final SchemaProviderService singleSchemaProvider = new SchemaProviderService() {
            @Override
            public Schema getAvroSchema(final String etName, final String version) {
                return schema;
            }

            @Override
            public String getAvroSchemaVersion(final String etName, final Schema schema) {
                return null;
            }
        };
        deserializer = new KafkaRecordDeserializer(nakadiRecordMapper, singleSchemaProvider, localSchemaRegistry);
    }

    @Test
    public void testDeserializeAvro() throws IOException {
        final KafkaRecordDeserializer deserializer = new KafkaRecordDeserializer(
                nakadiRecordMapper, schemaService, localSchemaRegistry);

        final JSONObject jsonObject = new JSONObject()
                .put("flow_id", "hek")
                .put("partition", "0")
                .put("published_by", "nakadi-test");

        // prepare the same bytes as we would put in Kafka record
        final byte[] data0 = EnvelopeHolder.produceBytes(
                metadataSchema.getVersionAsByte(),
                getMetadataWriter(metadataSchema.getSchema(), "0", jsonObject),
                getEventWriter1());

        final byte[] data1 = EnvelopeHolder.produceBytes(
                metadataSchema.getVersionAsByte(),
                getMetadataWriter(metadataSchema.getSchema(), "1", jsonObject),
                getEventWriter2());

        // try to deserialize that data when we would read Kafka record
        final byte[] deserializedEvent0 = deserializer.deserializeToJsonBytes(
                NakadiRecordMapper.AVRO_FORMAT,
                data0
        );
        final byte[] deserializedEvent1 = deserializer.deserializeToJsonBytes(
                NakadiRecordMapper.AVRO_FORMAT,
                data1
        );

        Assert.assertTrue(
                getExpectedNode1(null).similar(new JSONObject(new String(deserializedEvent0))));

        Assert.assertTrue(
                getExpectedNode2().similar(new JSONObject(new String(deserializedEvent1))));
    }

    @Test
    public void testDeserializeAvroNullEventInLogCompactedEventType() {
        final KafkaRecordDeserializer deserializer = new KafkaRecordDeserializer(
                nakadiRecordMapper, schemaService, localSchemaRegistry);

        Assert.assertNull(deserializer.deserializeToJsonBytes(null, null));
    }

    @Test
    public void testDeserializeAvroMetadata0() throws IOException {
        final JSONObject jsonObject = new JSONObject()
                .put("flow_id", "hek")
                .put("partition", "0")
                .put("published_by", "nakadi-test");

        final var actualJson = getSerializedJsonObject(metadataSchema, jsonObject);
        final var expectedJson = getExpectedNode1(jsonObject);
        Assert.assertTrue(expectedJson.similar(actualJson));
    }

    @Test
    public void testDeserializeAvroEnvelope() throws IOException {
        final ByteArrayOutputStream payload = new ByteArrayOutputStream();
        new GenericDatumWriter<>(schema).write(
                new GenericRecordBuilder(schema).set("foo", "bar").build(),
                EncoderFactory.get().directBinaryEncoder(payload, null));

        final String expectedOccurredAt = "2022-06-15T15:17:00Z";
        final Instant now = Instant.parse(expectedOccurredAt);
        final Envelope envelope = Envelope.newBuilder()
                .setMetadata(Metadata.newBuilder()
                        .setEid("4623130E-2983-4134-A472-F35154CFF980")
                        .setEventOwner("nakadi")
                        .setFlowId("xxx-event-flow-id")
                        .setOccurredAt(now)
                        .setEventType("nakadi-test-event-type")
                        .setVersion("1.0.0")
                        .build())
                .setPayload(ByteBuffer.wrap(payload.toByteArray()))
                .build();

        final ByteBuffer byteBuffer = Envelope.getEncoder().encode(envelope);
        final byte[] jsonBytes = deserializer.deserializeToJsonBytes(null, byteBuffer.array());

        final JSONObject event = new JSONObject(new String(jsonBytes));
        Assert.assertEquals("bar", event.get("foo"));
        Assert.assertEquals("4623130E-2983-4134-A472-F35154CFF980",
                event.getJSONObject("metadata").get("eid"));
        Assert.assertEquals(expectedOccurredAt,
                event.getJSONObject("metadata").get("occurred_at"));
    }

    @Test
    public void testDeserializeAvroSingleObjectEncoding() throws IOException {
        final ByteBuffer payload = new BinaryMessageEncoder<GenericRecord>(GenericData.get(), schema)
                .encode(new GenericRecordBuilder(schema).set("foo", "bar").build());

        final String expectedOccurredAt = "2022-06-15T15:17:00Z";
        final Instant now = Instant.parse(expectedOccurredAt);
        final Envelope envelope = Envelope.newBuilder()
                .setMetadata(Metadata.newBuilder()
                        .setEid("4623130E-2983-4134-A472-F35154CFF980")
                        .setEventOwner("nakadi")
                        .setFlowId("xxx-event-flow-id")
                        .setOccurredAt(now)
                        .setEventType("nakadi-test-event-type")
                        .setVersion("1.0.0")
                        .build())
                .setPayload(ByteBuffer.wrap(payload.array()))
                .build();

        final ByteBuffer byteBuffer = Envelope.getEncoder().encode(envelope);
        final byte[] jsonBytes = deserializer.deserializeToJsonBytes(null, byteBuffer.array());

        final JSONObject event = new JSONObject(new String(jsonBytes));
        Assert.assertEquals("bar", event.get("foo"));
        Assert.assertEquals("4623130E-2983-4134-A472-F35154CFF980",
                event.getJSONObject("metadata").get("eid"));
        Assert.assertEquals(expectedOccurredAt,
                event.getJSONObject("metadata").get("occurred_at"));
    }

    private JSONObject getSerializedJsonObject(final VersionedAvroSchema metadataVersion,
                                               final JSONObject metadataOverride) throws IOException {
        final KafkaRecordDeserializer deserializer = new KafkaRecordDeserializer(
                nakadiRecordMapper, schemaService, localSchemaRegistry);

        final var eventWriter = getEventWriter1();

        // prepare the same bytes as we would put in Kafka record
        final byte[] data = EnvelopeHolder.produceBytes(
                metadataVersion.getVersionAsByte(),
                getMetadataWriter(metadataVersion.getSchema(), "0", metadataOverride),
                eventWriter);

        // try to deserialize that data when we would read Kafka record
        final byte[] deserializedEvent = deserializer.deserializeToJsonBytes(
                NakadiRecordMapper.AVRO_FORMAT,
                data
        );

        return new JSONObject(new String(deserializedEvent));
    }

    private GenericRecord getBaseRecord(final String schemaVersion) {
        final GenericRecord event = new GenericData.Record(
                localSchemaRegistry.getEventTypeSchema("nakadi.access.log", schemaVersion));
        event.put("method", "POST");
        event.put("path", "/event-types");
        event.put("query", "");
        event.put("app", "nakadi");
        event.put("app_hashed", "hashed-app");
        event.put("status_code", 201);
        event.put("response_time_ms", 10);
        event.put("accept_encoding", "-");
        event.put("content_encoding", "--");
        return event;
    }

    private EnvelopeHolder.EventWriter getEventWriter1() {
        return os -> {
            final GenericRecord event = getBaseRecord("0");

            final GenericDatumWriter eventWriter = new GenericDatumWriter(event.getSchema());
            eventWriter.write(event, EncoderFactory.get()
                    .directBinaryEncoder(os, null));
        };
    }

    private EnvelopeHolder.EventWriter getEventWriter2() {
        return os -> {
            final GenericRecord event = getBaseRecord("1");
            event.put("user_agent", "test-user-agent");
            event.put("request_length", 111);
            event.put("response_length", 222);

            final GenericDatumWriter eventWriter = new GenericDatumWriter(event.getSchema());
            eventWriter.write(event, EncoderFactory.get()
                    .directBinaryEncoder(os, null));
        };
    }

    private EnvelopeHolder.EventWriter getMetadataWriter(final Schema metadataSchema,
                                                         final String schemaVersion,
                                                         final JSONObject metadataOverride) {
        return os -> {
            final GenericRecord metadata = new GenericData.Record(metadataSchema);

            metadata.put("occurred_at", SOME_TIME);
            metadata.put("received_at", SOME_TIME);
            metadata.put("eid", "32f5dae5-4fc4-4cda-be07-b313b58490ab");
            metadata.put("event_type", "nakadi.access.log");
            metadata.put("version", schemaVersion);
            metadata.put("partition", "0");

            Optional.ofNullable(metadataOverride).ifPresent(fn ->
                    metadataOverride.toMap().forEach(metadata::put)
            );

            final GenericDatumWriter eventWriter = new GenericDatumWriter(metadata.getSchema());
            eventWriter.write(metadata, EncoderFactory.get()
                    .directBinaryEncoder(os, null));
        };
    }

    private JSONObject getBaseExpectedNode(final String schemaVersion,
                                           final JSONObject metadataOverride) {
        final JSONObject metadata = new JSONObject().
                put("occurred_at", SOME_TIME_DATE_STRING)
                .put("eid", "32f5dae5-4fc4-4cda-be07-b313b58490ab")
                .put("flow_id", "hek")
                .put("received_at", SOME_TIME_DATE_STRING)
                .put("version", schemaVersion)
                .put("published_by", "nakadi-test")
                .put("event_type", "nakadi.access.log")
                .put("partition", "0");

        Optional.ofNullable(metadataOverride).ifPresent(fn -> {
            final var iterator = metadataOverride.keys();
            while (iterator.hasNext()) {
                final var key = iterator.next();
                metadata.put(key, metadataOverride.get(key));
            }
        });

        final JSONObject event = new JSONObject()
                .put("method", "POST")
                .put("path", "/event-types")
                .put("query", "")
                .put("app", "nakadi")
                .put("app_hashed", "hashed-app")
                .put("status_code", 201)
                .put("response_time_ms", 10)
                .put("accept_encoding", "-")
                .put("content_encoding", "--")
                .put("metadata", metadata);
        return event;
    }

    private JSONObject getExpectedNode1(final JSONObject metadataOverride) {
        return getBaseExpectedNode("0", metadataOverride);
    }

    private JSONObject getExpectedNode2() {
        final JSONObject event = getBaseExpectedNode("1", null);
        event.put("user_agent", "test-user-agent");
        event.put("request_length", 111);
        event.put("response_length", 222);
        return event;
    }
}
